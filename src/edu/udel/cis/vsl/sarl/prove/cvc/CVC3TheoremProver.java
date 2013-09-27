/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.prove.cvc;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cvc3.Cvc3Exception;
import cvc3.Expr;
import cvc3.Op;
import cvc3.QueryResult;
import cvc3.Type;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject.SymbolicObjectKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;
import edu.udel.cis.vsl.sarl.util.Pair;

// TODO: add support for characters by converting them to integers

/**
 * An implementation of TheoremProver using the automated theorem prover CVC3.
 * Transforms a theorem proving query into the language of CVC3, invokes CVC3
 * through its JNI interface, and interprets the output.
 */
public class CVC3TheoremProver implements TheoremProver {

	/**
	 * The symbolic universe used for managing symbolic expressions. Initialized
	 * by constructor and never changes.
	 */
	private PreUniverse universe;

	/**
	 * Print the queries and results each time valid is called. Initialized by
	 * constructor.
	 */
	private boolean showProverQueries = false;

	/**
	 * The printwriter used to print the queries and results. Initialized by
	 * constructor.
	 */
	private PrintStream out = null;

	/** The CVC3 object used to check queries. */
	private ValidityChecker vc = ValidityChecker.create();

	/**
	 * Mapping of SARL symbolic type to corresponding CVC3 type. Set in method
	 * reset().
	 */
	private Map<SymbolicType, Type> typeMap = new HashMap<SymbolicType, Type>();

	/**
	 * Mapping of SARL symbolic expression to corresponding CVC3 expresssion.
	 * Set in method reset().
	 */
	private Map<SymbolicExpression, Expr> expressionMap = new HashMap<SymbolicExpression, Expr>();

	/**
	 * Map from the root name (e.g., name of a symbolic constant) to the number
	 * of distinct CVC3 variables declared with that root. Since in CVC3 the
	 * names must be unique, the CVC3 name will be modified by appending the
	 * string "'n" where n is the value from this map, to all but the first
	 * instance of the root. I.e., the CVC3 names corresponding to "x" will be:
	 * x, x'1, x'2, ...
	 * 
	 * The names are for symbolic constants of all kinds, including functions.
	 */
	private Map<String, Integer> nameCountMap = new HashMap<String, Integer>();

	/**
	 * Map from SARL expressions of funcional type to corresponding CVC3
	 * operators. In SARL, a function is a kind of symbolic expression. In CVC3,
	 * this concept is represented as an instance of "OpMut" (Operator Mutable),
	 * a subtype of "Op" (operator), which is not a subtype of Expr. Hence a
	 * separate map is needed.
	 */
	private Map<SymbolicExpression, Op> functionMap = new HashMap<SymbolicExpression, Op>();

	/**
	 * Mapping of CVC3 variables to their corresponding symbolic constants.
	 * Needed in order to construct model when there is a counter example.
	 */
	private Map<Expr, SymbolicConstant> varMap = new HashMap<Expr, SymbolicConstant>();

	/**
	 * Mapping of CVC3 "Op"s to their corresponding symbolic constants. A CVC3
	 * "Op" is used to represent a function. In SARL, a function is represented
	 * by a symbolic constant of function type. This is used for finding models.
	 */
	private Map<Op, SymbolicConstant> opMap = new HashMap<Op, SymbolicConstant>();

	/**
	 * Stack of Map giving integer division info objects in the current CVC3
	 * scope. This map is discarded when the CVC3 validity checker is popped,
	 * and a new one is created when the validity checker is pushed. Hence a
	 * unique map is created and used for checking each query.
	 * 
	 * A key is a numerator-denominator pair of symbolic expressions (in tree
	 * form). The value associated to that key is a pair of CVC3 expressions:
	 * the first element of the pair is the CVC3 expression (usually a variable)
	 * corresponding to the quotient, the second the CVC3 expression
	 * corresponding to the modulus.
	 */
	private LinkedList<Map<Pair<SymbolicExpression, SymbolicExpression>, IntDivisionInfo>> intDivisionStack = new LinkedList<Map<Pair<SymbolicExpression, SymbolicExpression>, IntDivisionInfo>>();

	/**
	 * Like above, but remains persistent from query to query.
	 */
	private Map<Pair<SymbolicExpression, SymbolicExpression>, IntDivisionInfo> permanentIntegerDivisionMap = new HashMap<Pair<SymbolicExpression, SymbolicExpression>, IntDivisionInfo>();

	private LinkedList<Map<SymbolicExpression, Expr>> translationStack = new LinkedList<Map<SymbolicExpression, Expr>>();

	/**
	 * The assumption under which this prover is operating.
	 */
	private BooleanExpression context;

	/**
	 * The translation of the context to a CVC3 expression.
	 */
	private Expr cvcAssumption;

	/**
	 * Constructs new CVC3 theorem prover with given symbolic universe.
	 * 
	 * @param universe
	 *            the controlling symbolic universe
	 * @param out
	 *            where to print debugging output; may be null
	 * @param showProverQueries
	 *            print the queries?
	 */
	CVC3TheoremProver(PreUniverse universe, BooleanExpression context) {
		assert universe != null;
		assert context != null;
		this.universe = universe;
		this.context = context;
		intDivisionStack
				.add(new HashMap<Pair<SymbolicExpression, SymbolicExpression>, IntDivisionInfo>());
		translationStack.add(new HashMap<SymbolicExpression, Expr>());
		cvcAssumption = translate(context);
		vc.assertFormula(cvcAssumption);
	}

	// Helper methods...

	private <E> List<E> newSingletonList(E element) {
		List<E> result = new LinkedList<E>();

		result.add(element);
		return result;
	}

	private String newCvcName(String root) {
		Integer count = nameCountMap.get(root);

		if (count == null) {
			nameCountMap.put(root, 1);
			return root;
		} else {
			String result = root + "'" + count;

			nameCountMap.put(root, nameCountMap.put(root, count + 1));
			return result;
		}
	}

	private Expr newAuxVariable(Type type) {
		return vc.varExpr(newCvcName("_x"), type);
	}

	/**
	 * Returns a new bound variable with given root name and type. The name of
	 * the variable will be the concatenation of root with a string of the form
	 * "'n".
	 * 
	 * @param root
	 *            root of name to give to this variable
	 * @param type
	 *            CVC3 type of this variable
	 * @return the new bound variable
	 */
	private Expr newBoundVariable(String root, Type type) {
		String name = newCvcName(root);
		Expr result = vc.boundVarExpr(name, name, type);

		return result;
	}

	/**
	 * Returns new bound variable with a generic name "i" followed by a
	 * distiguishing suffix.
	 * 
	 * @param type
	 *            the type of the new bound variable
	 * @return the new bound variable
	 */
	private Expr newBoundVariable(Type type) {
		return newBoundVariable("i", type);
	}

	/**
	 * Symbolic expressions of incomplete array type are represented by ordered
	 * pairs (length, array). This method tells whether the given symbolic
	 * expression type requires such a representation.
	 * 
	 * @param type
	 *            any symbolic type
	 * @return true iff the type is an incomplete array type
	 */
	private boolean isBigArrayType(SymbolicType type) {
		return type instanceof SymbolicArrayType
				&& !((SymbolicArrayType) type).isComplete();
	}
	
	private boolean isBigArray(SymbolicExpression expr) {
		return isBigArrayType(expr.type());
	}

	/**
	 * This method takes in the length and value of an Expr
	 * and returns the ordered pair represented by an incomplete 
	 * array type. 
	 * 
	 * @param length
	 * 			CVC3 Expr of length
	 * @param value
	 * 			CVC3 Expr of value
	 * @return CVC3 tuple of an ordered pair (length, array)
	 */
	private Expr bigArray(Expr length, Expr value) {
		List<Expr> list = new LinkedList<Expr>();

		list.add(length);
		list.add(value);
		return vc.tupleExpr(list);
	}

	/**
	 * This method takes any Expr that is of incomplete array type
	 * and returns the length. 
	 * @param bigArray
	 * 			CVC3 Expr of bigArray
	 * @return length of CVC3 Expr of incomplete array type
	 */
	private Expr bigArrayLength(Expr bigArray) {
		return vc.tupleSelectExpr(bigArray, 0);
	}
	
	/**
	 * This methods takes any Expr that is of incomplete array type
	 * and returns the value. 
	 * 
	 * @param bigArray
	 * 			CVC3 Expr of bigArray
	 * @return value of CVC3 Expr of incomplete array type
	 */
	private Expr bigArrayValue(Expr bigArray) {
		return vc.tupleSelectExpr(bigArray, 1);
	}

	private String selector(SymbolicUnionType unionType, int index) {
		return unionType.name().toString() + "_extract_" + index;
	}

	private String constructor(SymbolicUnionType unionType, int index) {
		return unionType.name().toString() + "_inject_" + index;
	}

	/**
	 * This methods takes any SymbolicCollection and returns a linked list
	 * of Expr for cvc3.
	 * 
	 * @param collection
	 * 			SymbolicCollection given to the translation.
	 * @return linkedlist of CVC3 Expr
	 */
	private List<Expr> translateCollection(SymbolicCollection<?> collection) {
		List<Expr> result = new LinkedList<Expr>();

		for (SymbolicExpression expr : collection)
			result.add(expr == null ? null : translate(expr));
		return result;
	}

	/**
	 * Translate a given SymbolicTypeSequence to an equivalent
	 * linkedlist of Types in CVC3.
	 * 
	 * @param sequence
	 * 			SymbolicTypeSequence given to the translation.
	 * @return linkedlist of CVC3 types.
	 */
	private List<Type> translateTypeSequence(SymbolicTypeSequence sequence) {
		List<Type> result = new LinkedList<Type>();

		for (SymbolicType t : sequence)
			result.add(t == null ? null : translateType(t));
		return result;
	}

	/**
	 * Translates a symbolic expression of functional type. In CVC3, functions
	 * have type Op; expressions have type Expr.
	 * 
	 */
	private Op translateFunction(SymbolicExpression expr) {
		Op result = functionMap.get(expr);

		if (result != null)
			return result;
		switch (expr.operator()) {
		case SYMBOLIC_CONSTANT: {
			String name = newCvcName(((SymbolicConstant) expr).name()
					.getString());

			result = vc.createOp(name, this.translateType(expr.type()));
			opMap.put(result, (SymbolicConstant) expr);
			break;
		}
		case LAMBDA:
			result = vc.lambdaExpr(
					newSingletonList(translateSymbolicConstant(
							(SymbolicConstant) expr.argument(0), true)),
					translate((SymbolicExpression) expr.argument(1)));
			break;
		default:
			throw new SARLInternalException(
					"unknown kind of expression of functional type: " + expr);
		}
		this.functionMap.put(expr, result);
		return result;
	}
	
	/**
	 * Translates any concrete SymbolicExpression with concrete type
	 * to equivalent CVC3 Expr using the validitychecker. 
	 * 
	 */
	private Expr translateConcrete(SymbolicExpression expr) {
		SymbolicType type = expr.type();
		SymbolicTypeKind kind = type.typeKind();
		SymbolicObject object = expr.argument(0);
		Expr result;

		switch (kind) {
		case ARRAY: {
			NumericExpression extentExpression = ((SymbolicCompleteArrayType) type)
					.extent();
			IntegerNumber extentNumber = (IntegerNumber) universe
					.extractNumber(extentExpression);
			SymbolicSequence<?> sequence = (SymbolicSequence<?>) object;
			int size = sequence.size();
			Type cvcType = translateType(type);

			assert extentNumber != null && extentNumber.intValue() == size;
			result = newAuxVariable(cvcType);
			for (int i = 0; i < size; i++)
				result = vc.writeExpr(result, vc.ratExpr(i),
						translate(sequence.get(i)));
			break;
		}
		case BOOLEAN:
			result = ((BooleanObject) object).getBoolean() ? vc.trueExpr() : vc
					.falseExpr();
			break;
		case INTEGER:
		case REAL:
			result = vc.ratExpr(((NumberObject) object).getNumber().toString());
			break;
		case TUPLE:
			result = vc
					.tupleExpr(translateCollection((SymbolicSequence<?>) object));
			break;
		default:
			throw new SARLInternalException("Unknown concrete object: " + expr);
		}
		return result;
	}

	/**
	 * Translates a symbolic constant to CVC3 variable. Special handling is
	 * required if the symbolic constant is used as a bound variable in a
	 * quantified (forall, exists) expression.
	 * 
	 * Precondition: ?
	 */
	private Expr translateSymbolicConstant(SymbolicConstant symbolicConstant,
			boolean isBoundVariable) throws Cvc3Exception {
		Type type = translateType(symbolicConstant.type());
		String root = symbolicConstant.name().getString();
		Expr result;

		if (isBoundVariable) {
			result = newBoundVariable(root, type);
		} else {
			result = vc.varExpr(newCvcName(root), type);
		}
		varMap.put(result, symbolicConstant);
		return result;
	}

	/**
	 * Translates a multiplication SymbolicExpression (a*b) into an 
	 * equivalent CVC3 multiplication Expr based upon number of 
	 * arguments given. 
	 * 
	 * @param expr
	 * 			a SARL SymbolicExpression of form a*b
	 * @return CVC3 Expr
	 */
	private Expr translateMultiply(SymbolicExpression expr) {
		int numArgs = expr.numArguments();
		Expr result;

		if (numArgs == 1) {
			result = vc.ratExpr(1);
			for (SymbolicExpression operand : (SymbolicCollection<?>) expr
					.argument(0))
				result = vc.multExpr(result, translate(operand));
		} else if (numArgs == 2)
			result = vc.multExpr(
					translate((SymbolicExpression) expr.argument(0)),
					translate((SymbolicExpression) expr.argument(1)));
		else
			throw new SARLInternalException(
					"Wrong number of arguments to multiply: " + expr);
		return result;
	}

	private Expr translateOr(SymbolicExpression expr) {
		int numArgs = expr.numArguments();
		Expr result;

		if (numArgs == 1)
			result = vc.orExpr(translateCollection((SymbolicCollection<?>) expr
					.argument(0)));
		else if (numArgs == 2)
			result = vc.orExpr(
					translate((SymbolicExpression) expr.argument(0)),
					translate((SymbolicExpression) expr.argument(1)));
		else
			throw new SARLInternalException("Wrong number of arguments to or: "
					+ expr);
		return result;
	}

	/**
	 * Looks for the existing IntDivisionInfo for the given
	 * numerator-denominator pair of symbolic expressions, or creates new one if
	 * not found.
	 * 
	 * Protocol: first, look in the currentIntegerDivisionMap. These are the
	 * ones that have already been processed in the current query processing. If
	 * found, return it.
	 * 
	 * Next, look in the permanentIntegerDivisionMap. If found, the quotient and
	 * remainder variables and constraints were created in a previous query.
	 * Only the constraints need to be re-asserted. Do that, and return the
	 * object.
	 * 
	 * Otherwise, create a new one, and enter it into both maps.
	 **/
	private IntDivisionInfo getIntDivisionInfo(
			SymbolicExpression numeratorExpression,
			SymbolicExpression denominatorExpression) throws Cvc3Exception {
		Pair<SymbolicExpression, SymbolicExpression> key = new Pair<SymbolicExpression, SymbolicExpression>(
				numeratorExpression, denominatorExpression);
		Iterator<Map<Pair<SymbolicExpression, SymbolicExpression>, IntDivisionInfo>> iter = intDivisionStack
				.descendingIterator();
		IntDivisionInfo value = null;

		while (iter.hasNext()) {
			Map<Pair<SymbolicExpression, SymbolicExpression>, IntDivisionInfo> map = iter
					.next();

			value = map.get(key);
		}
		if (value == null) {
			value = permanentIntegerDivisionMap.get(key);

			if (value == null) {
				int counter = permanentIntegerDivisionMap.size();
				Expr quotient = vc.varExpr("q_" + counter, vc.intType());
				Expr remainder = vc.varExpr("r_" + counter, vc.intType());
				Expr numerator = translate(numeratorExpression);
				Expr denominator = translate(denominatorExpression);

				value = new IntDivisionInfo(vc, numerator, denominator,
						quotient, remainder);
				permanentIntegerDivisionMap.put(key, value);
			}
			value.addConstraints(vc);
			intDivisionStack.getLast().put(key, value);
		}
		return value;
	}

	/**
	 * Translates an integer modulo symbolic expression (a%b) into an equivalent
	 * CVC3 expression. This involves possibly adding extra integer variables
	 * and constraints to the validity checker.
	 * 
	 * @param modExpression
	 *            a SARL symbolic expression of form a%b
	 * @return an equivalent CVC3 expression
	 * @throws Cvc3Exception
	 *             by CVC3
	 */
	private Expr translateIntegerModulo(SymbolicExpression modExpression)
			throws Cvc3Exception {
		IntDivisionInfo info = getIntDivisionInfo(
				(SymbolicExpression) modExpression.argument(0),
				(SymbolicExpression) modExpression.argument(1));

		return info.remainder;
	}

	/**
	 * Translates an integer division symbolic expression (a/b) into an
	 * equivalent CVC3 expression. This involves possibly adding extra integer
	 * variables and constraints to the validity checker.
	 * 
	 * @param quotientExpression
	 *            a SARL symbolic expression of form a (intdiv) b
	 * @return an equivalent CVC3 expression
	 * @throws Cvc3Exception
	 *             by CVC3
	 */
	private Expr translateIntegerDivision(SymbolicExpression quotientExpression)
			throws Cvc3Exception {
		IntDivisionInfo info = getIntDivisionInfo(
				(SymbolicExpression) quotientExpression.argument(0),
				(SymbolicExpression) quotientExpression.argument(1));

		return info.quotient;
	}

	private void assertIndexInBounds(SymbolicExpression arrayExpression,
			NumericExpression index) {
		NumericExpression length = universe.length(arrayExpression);
		BooleanExpression predicate = universe.lessThan(index, length);
		Expr cvcPredicate;

		predicate = universe.and(
				universe.lessThanEquals(universe.zeroInt(), index), predicate);
		cvcPredicate = translate(predicate);
		vc.assertFormula(cvcPredicate);
	}

	/**
	 * Translates an array-read expression a[i] into equivalent CVC3 expression
	 * 
	 * @param expr
	 *            a SARL symbolic expression of form a[i]
	 * @return an equivalent CVC3 expression
	 * @throws Cvc3Exception
	 *             by CVC3
	 */
	private Expr translateArrayRead(SymbolicExpression expr)
			throws Cvc3Exception {
		SymbolicExpression arrayExpression = (SymbolicExpression) expr
				.argument(0);
		NumericExpression indexExpression = (NumericExpression) expr
				.argument(1);
		Expr array, index, result;

		assertIndexInBounds(arrayExpression, indexExpression);
		array = translate(arrayExpression);
		index = translate((SymbolicExpression) expr.argument(1));
		if (isBigArray(arrayExpression))
			array = bigArrayValue(array);
		result = vc.readExpr(array, index);
		return result;
	}

	/**
	 * Translates an array-write (or array update) SARL symbolic expression to
	 * equivalent CVC3 expression.
	 * 
	 * @param expr
	 *            an array update expression array[WITH i:=newValue].
	 * @return the equivalent CVC3 Expr
	 * @throws Cvc3Exception
	 *             by CVC3
	 */
	private Expr translateArrayWrite(SymbolicExpression expr)
			throws Cvc3Exception {
		SymbolicExpression arrayExpression = (SymbolicExpression) expr
				.argument(0);
		NumericExpression indexExpression = (NumericExpression) expr
				.argument(1);
		Expr array, index, value, result;

		assertIndexInBounds(arrayExpression, indexExpression);
		array = translate(arrayExpression);
		index = translate(indexExpression);
		value = translate((SymbolicExpression) expr.argument(2));
		result = isBigArray(arrayExpression) ? bigArray(bigArrayLength(array),
				vc.writeExpr(bigArrayValue(array), index, value)) : vc
				.writeExpr(array, index, value);
		return result;
	}
	
	/**
	 * Translates a multiple array-write (or array update) SARL symbolic expression
	 * to equivalent CVC3 expression. 
	 * 
	 * @param expr
	 * 			an array update expression array [WITH i:=newValue]...[WITH i:=newValue]
	 * @return the equivalent CVC3 Expr
	 * @throws Cvc3Exception
	 * 			by CVC3
	 */
	private Expr translateDenseArrayWrite(SymbolicExpression expr)
			throws Cvc3Exception {
		SymbolicExpression arrayExpression = (SymbolicExpression) expr
				.argument(0);
		boolean isBig = isBigArray(arrayExpression);
		Expr origin = translate(arrayExpression);
		Expr result = isBig ? bigArrayValue(origin) : origin;
		List<Expr> values = translateCollection((SymbolicSequence<?>) expr
				.argument(1));
		int index = 0;

		for (Expr value : values) {
			if (value != null)
				result = vc.writeExpr(result, vc.ratExpr(index), value);
			index++;
		}
		assertIndexInBounds(arrayExpression, universe.integer(index - 1));
		if (isBig)
			result = bigArray(bigArrayLength(origin), result);
		return result;
	}

	/**
	 * Translate a multiple tuple-write (or tuple update) SARL symbolic expression
	 * to equivalent CVC3 expression.
	 * 
	 * @param expr
	 * 			a tuple update expression 
	 * @return the equivalent CVC3 Expr
	 * @throws Cvc3Exception
	 *			by CVC3
	 */
	private Expr translateDenseTupleWrite(SymbolicExpression expr)
			throws Cvc3Exception {
		SymbolicExpression tupleExpression = (SymbolicExpression) expr
				.argument(0);
		Expr result = translate(tupleExpression);
		List<Expr> values = translateCollection((SymbolicSequence<?>) expr
				.argument(1));
		int index = 0;

		for (Expr value : values) {
			if (value != null)
				result = vc.tupleUpdateExpr(result, index, value);
			index++;
		}
		return result;
	}

	private Expr translateQuantifier(SymbolicExpression expr)
			throws Cvc3Exception {
		Expr variable = this.translateSymbolicConstant(
				(SymbolicConstant) (expr.argument(0)), true);
		List<Expr> vars = new LinkedList<Expr>();
		Expr predicate = translate((SymbolicExpression) expr.argument(1));
		SymbolicOperator kind = expr.operator();

		vars.add(variable);
		if (kind == SymbolicOperator.FORALL) {
			return vc.forallExpr(vars, predicate);
		} else if (kind == SymbolicOperator.EXISTS) {
			return vc.existsExpr(vars, predicate);
		} else {
			throw new SARLInternalException(
					"Cannot translate quantifier into CVC3: " + expr);
		}
	}

	/**
	 * Processes the equality of two arrays.  Arrays can be of complete type or
	 * incomplete type.  
	 * 
	 * @param type1
	 * 			a SARL SymbolicType
	 * @param type2
	 * 			a SARL SymbolicType
	 * @param cvcExpression1
	 * 			a CVC3 array
	 * @param cvcExpression2
	 * 			a CVC3 array
	 * @return
	 */
	private Expr processEquality(SymbolicType type1, SymbolicType type2,
			Expr cvcExpression1, Expr cvcExpression2) {
		if (type1.typeKind() == SymbolicTypeKind.ARRAY) {
			// length are equal and forall i (0<=i<length).a[i]=b[i].
			SymbolicArrayType arrayType1 = (SymbolicArrayType) type1;
			SymbolicArrayType arrayType2 = (SymbolicArrayType) type2;
			Expr extent1, extent2, array1, array2, readExpr1, readExpr2;
			Expr result, index, indexRangeExpr, elementEqualsExpr, forallExpr;

			if (arrayType1 instanceof SymbolicCompleteArrayType) {
				extent1 = translate(((SymbolicCompleteArrayType) arrayType1)
						.extent());
				array1 = cvcExpression1;
			} else {
				extent1 = bigArrayLength(cvcExpression1);
				array1 = bigArrayValue(cvcExpression1);
			}
			if (arrayType2 instanceof SymbolicCompleteArrayType) {
				extent2 = translate(((SymbolicCompleteArrayType) arrayType2)
						.extent());
				array2 = cvcExpression2;
			} else {
				extent2 = bigArrayLength(cvcExpression2);
				array2 = bigArrayValue(cvcExpression2);
			}
			result = vc.eqExpr(extent1, extent2);
			index = newBoundVariable(vc.intType());
			indexRangeExpr = vc.andExpr(vc.geExpr(index, vc.ratExpr(0)),
					vc.ltExpr(index, extent1));
			readExpr1 = vc.readExpr(array1, index);
			readExpr2 = vc.readExpr(array2, index);
			elementEqualsExpr = processEquality(arrayType1.elementType(),
					arrayType2.elementType(), readExpr1, readExpr2);
			forallExpr = vc.forallExpr(newSingletonList(index),
					vc.impliesExpr(indexRangeExpr, elementEqualsExpr));
			result = vc.andExpr(result, forallExpr);
			return result;
		} else {
			return vc.eqExpr(cvcExpression1, cvcExpression2);
		}
	}

	private Expr translateEquality(SymbolicExpression expr)
			throws Cvc3Exception {
		SymbolicExpression leftExpression = (SymbolicExpression) expr
				.argument(0);
		SymbolicExpression rightExpression = (SymbolicExpression) expr
				.argument(1);
		SymbolicType type1 = leftExpression.type();
		SymbolicType type2 = rightExpression.type();
		Expr cvcExpression1 = translate(leftExpression);
		Expr cvcExpression2 = translate(rightExpression);
		Expr result = processEquality(type1, type2, cvcExpression1,
				cvcExpression2);

		return result;
	}

	/**
	 * UNION_EXTRACT: 2 arguments: arg0 is an IntObject giving the index of a
	 * member type of a union type; arg1 is a symbolic expression whose type is
	 * the union type. The resulting expression has type the specified member
	 * type. This essentially pulls the expression out of the union and casts it
	 * to the member type. If arg1 does not belong to the member type (as
	 * determined by a UNION_TEST expression), the value of this expression is
	 * undefined.
	 * 
	 * Get the type of arg1. It is a union type. Get arg0 and call it i. Get the
	 * name of the i-th component of the union type. It must have a globally
	 * unique name. That is the selector. Translate arg1.
	 * 
	 * Every symbolic union type has a name, so name of selector could be
	 * unionName_i.
	 * 
	 * @param expr
	 *            a "union extract" expression
	 * @return the CVC3 translation of that expression
	 */
	private Expr translateUnionExtract(SymbolicExpression expr) {
		SymbolicExpression arg = (SymbolicExpression) expr.argument(1);
		SymbolicUnionType unionType = (SymbolicUnionType) arg.type();
		int index = ((IntObject) expr.argument(0)).getInt();
		String selector = selector(unionType, index);
		Expr result = vc.datatypeSelExpr(selector, translate(arg));

		return result;
	}

	/**
	 * UNION_INJECT: injects an element of a member type into a union type that
	 * inclues that member type. 2 arguments: arg0 is an IntObject giving the
	 * index of the member type of the union type; arg1 is a symbolic expression
	 * whose type is the member type. The union type itself is the type of the
	 * UNION_INJECT expression.
	 * 
	 * @param expr
	 *            a "union inject" expression
	 * @return the CVC3 translation of that expression
	 */
	private Expr translateUnionInject(SymbolicExpression expr) {
		int index = ((IntObject) expr.argument(0)).getInt();
		SymbolicExpression arg = (SymbolicExpression) expr.argument(1);
		SymbolicUnionType unionType = (SymbolicUnionType) expr.type();
		String constructor = constructor(unionType, index);
		List<Expr> argumentList = new LinkedList<Expr>();
		Expr result;

		argumentList.add(translate(arg));
		result = vc.datatypeConsExpr(constructor, argumentList);
		return result;
	}

	/**
	 * UNION_TEST: 2 arguments: arg0 is an IntObject giving the index of a
	 * member type of the union type; arg1 is a symbolic expression whose type
	 * is the union type. This is a boolean-valued expression whose value is
	 * true iff arg1 belongs to the specified member type of the union type.
	 * 
	 * @param expr
	 *            a "union test" expression
	 * @return the CVC3 translation of that expression
	 */
	private Expr translateUnionTest(SymbolicExpression expr) {
		int index = ((IntObject) expr.argument(0)).getInt();
		SymbolicExpression arg = (SymbolicExpression) expr.argument(1);
		SymbolicUnionType unionType = (SymbolicUnionType) arg.type();
		String constructor = constructor(unionType, index);
		Expr result = vc.datatypeTestExpr(constructor, translate(arg));

		return result;
	}

	/**
	 * Processes side-effects resulting from an integer division or modulus
	 * expression. Given an expr which has been previously translated, this
	 * finds its side effect constraints and adds them to the vc assumptions.
	 * Also recursively processes side effects on the arguments.
	 * 
	 * @param expr
	 *            an integer division or modulus expression which has been
	 *            translated
	 */
	private void sideEffectIntDiv(SymbolicExpression expr) {
		SymbolicExpression numerator = (SymbolicExpression) expr.argument(0);
		SymbolicExpression denominator = (SymbolicExpression) expr.argument(1);
		Pair<SymbolicExpression, SymbolicExpression> key = new Pair<SymbolicExpression, SymbolicExpression>(
				numerator, denominator);
		IntDivisionInfo info = permanentIntegerDivisionMap.get(key);

		if (info == null)
			throw new SARLInternalException(
					"sideEffectIntDiv should only be called after expression has been translated: "
							+ expr);
		info.addConstraints(vc);
		sideEffect(numerator);
		sideEffect(denominator);
	}

	/**
	 * Processes all the side-effect Types of a given SymbolicTypeSequence.
	 * 
	 * @param sequence
	 * 			a SymbolicTypeSequence of an object with SymbolicObjectKind 
	 * 			TYPE_SEQUENCE
	 */
	private void sideEffectTypeSequence(SymbolicTypeSequence sequence) {
		for (SymbolicType t : sequence)
			sideEffectType(t);
	}
	
	/**
	 * Processes the side-effects of all SymbolicExpressions in a given 
	 * SymbolicCollection.
	 * 
	 * @param collection
	 * 			a SymbolicCollection of SymbolicExpressions
	 */
	private void sideEffectCollection(SymbolicCollection<?> collection) {
		for (SymbolicExpression expr : collection)
			sideEffect(expr);
	}

	/**
	 * Processes all the side-effect Types of a given SymbolicType. 
	 * 
	 * @param type
	 * 			a SymbolicType of an object with SymbolicObjectKind TYPE
	 * @throws Cvc3Exception
	 * 			by CVC3
	 */
	private void sideEffectType(SymbolicType type) throws Cvc3Exception {
		SymbolicTypeKind kind = type.typeKind();

		switch (kind) {
		case BOOLEAN:
		case INTEGER:
		case REAL:
			break;
		case ARRAY:
			sideEffectType(((SymbolicArrayType) type).elementType());
			break;
		case TUPLE:
			sideEffectTypeSequence(((SymbolicTupleType) type).sequence());
			break;
		case FUNCTION:
			sideEffectTypeSequence(((SymbolicFunctionType) type).inputTypes());
			sideEffectType(((SymbolicFunctionType) type).outputType());
			break;
		case UNION: {
			SymbolicUnionType unionType = (SymbolicUnionType) type;
			SymbolicTypeSequence sequence = unionType.sequence();

			for (SymbolicType t : sequence)
				sideEffectType(t);
			break;
		}
		default:
			throw new SARLInternalException("Unknown type: " + type);
		}
	}

	/**
	 * Processes what kind of SymbolicObject the SymbolicObject is that 
	 * has been given. 
	 * @param object
	 * 			a SymbolicObject
	 */
	private void sideEffectObject(SymbolicObject object) {
		SymbolicObjectKind kind = object.symbolicObjectKind();

		switch (kind) {
		case BOOLEAN:
		case CHAR:
		case INT:
		case NUMBER:
		case STRING:
			break;
		case EXPRESSION:
			sideEffect((SymbolicExpression) object);
			break;
		case EXPRESSION_COLLECTION:
			sideEffectCollection((SymbolicCollection<?>) object);
			break;
		case TYPE:
			sideEffectType((SymbolicType) object);
			break;
		case TYPE_SEQUENCE:
			sideEffectTypeSequence((SymbolicTypeSequence) object);
			break;
		default:
			throw new SARLInternalException("unreachable");
		}
	}

	/**
	 * Processes the side-effect of a given SymbolicExpression based upon
	 * the SymbolicOperator. 
	 * 
	 * @param expr
	 * 			a SymbolicExpression
	 */
	private void sideEffect(SymbolicExpression expr) {
		SymbolicOperator operator = expr.operator();

		if (operator == SymbolicOperator.INT_DIVIDE
				|| operator == SymbolicOperator.MODULO)
			sideEffectIntDiv(expr);
		else {
			int numArgs = expr.numArguments();

			for (int i = 0; i < numArgs; i++)
				sideEffectObject(expr.argument(i));
		}
	}

	/**
	 * Translates which operation to perform based upon the given 
	 * SymbolicExpression and the SymbolicOperator provided.  Depending 
	 * upon the number of arguments given, a different conditional will
	 * be executed.  The result will be a CVC3 Expr. 
	 * 
	 * @param expr
	 * 			a SymbolicExpression 
	 * @return
	 */
	private Expr translateWork(SymbolicExpression expr) {
		int numArgs = expr.numArguments();
		Expr result;

		switch (expr.operator()) {
		case ADD:
			if (numArgs == 2)
				result = vc.plusExpr(
						translate((SymbolicExpression) expr.argument(0)),
						translate((SymbolicExpression) expr.argument(1)));
			else if (numArgs == 1)
				result = vc
						.plusExpr(translateCollection((SymbolicCollection<?>) expr
								.argument(0)));
			else
				throw new SARLInternalException(
						"Expected 1 or 2 arguments for ADD");
			break;
		case AND:
			if (numArgs == 2)
				result = vc.andExpr(
						translate((SymbolicExpression) expr.argument(0)),
						translate((SymbolicExpression) expr.argument(1)));
			else if (numArgs == 1)
				result = vc
						.andExpr(translateCollection((SymbolicCollection<?>) expr
								.argument(0)));
			else
				throw new SARLInternalException(
						"Expected 1 or 2 arguments for AND: " + expr);
			break;
		case APPLY:
			result = vc.funExpr(translateFunction((SymbolicExpression) expr
					.argument(0)),
					translateCollection((SymbolicCollection<?>) expr
							.argument(1)));
			break;
		case ARRAY_LAMBDA: {
			SymbolicExpression function = (SymbolicExpression) expr.argument(0);
			SymbolicOperator op0 = function.operator();
			Expr var, body;

			if (op0 == SymbolicOperator.LAMBDA) {
				var = translate((SymbolicConstant) function.argument(0));
				body = translate((SymbolicExpression) function.argument(1));
			} else {
				// create new SymbolicConstantIF _SARL_i
				// create new APPLY expression apply(f,i)
				// need universe.
				// or just assert forall i.a[i]=f(i)
				throw new UnsupportedOperationException("TO DO");
			}
			result = vc.arrayLiteral(var, body);
			break;
		}
		case ARRAY_READ:
			result = translateArrayRead(expr);
			break;
		case ARRAY_WRITE:
			result = translateArrayWrite(expr);
			break;
		case CAST:
			result = this.translate((SymbolicExpression) expr.argument(0));
			break;
		case CONCRETE:
			result = translateConcrete(expr);
			break;
		case COND:
			result = vc.iteExpr(
					translate((SymbolicExpression) expr.argument(0)),
					translate((SymbolicExpression) expr.argument(1)),
					translate((SymbolicExpression) expr.argument(2)));
			break;
		case DENSE_ARRAY_WRITE:
			result = translateDenseArrayWrite(expr);
			break;
		case DENSE_TUPLE_WRITE:
			result = translateDenseTupleWrite(expr);
			break;
		case DIVIDE: // real division
			result = vc.divideExpr(
					translate((SymbolicExpression) expr.argument(0)),
					translate((SymbolicExpression) expr.argument(1)));
			break;
		case EQUALS:
			result = translateEquality(expr);
			break;
		case EXISTS:
			result = translateQuantifier(expr);
			break;
		case FORALL:
			result = translateQuantifier(expr);
			break;
		case INT_DIVIDE:
			result = translateIntegerDivision(expr);
			break;
		case LENGTH:
			result = bigArrayLength(translate((SymbolicExpression) expr
					.argument(0)));
			break;
		case LESS_THAN:
			result = vc.ltExpr(
					translate((SymbolicExpression) expr.argument(0)),
					translate((SymbolicExpression) expr.argument(1)));
			break;
		case LESS_THAN_EQUALS:
			result = vc.leExpr(
					translate((SymbolicExpression) expr.argument(0)),
					translate((SymbolicExpression) expr.argument(1)));
			break;
		case MODULO:
			result = translateIntegerModulo(expr);
			break;
		case MULTIPLY:
			result = translateMultiply(expr);
			break;
		case NEGATIVE:
			result = vc.uminusExpr(translate((SymbolicExpression) expr
					.argument(0)));
			break;
		case NEQ:
			result = vc.notExpr(translateEquality(expr));
			break;
		case NOT:
			result = vc
					.notExpr(translate((SymbolicExpression) expr.argument(0)));
			break;
		case OR:
			result = translateOr(expr);
			break;
		case POWER: {
			SymbolicObject exponent = expr.argument(1);

			if (exponent instanceof IntObject)
				result = vc.powExpr(
						translate((SymbolicExpression) expr.argument(0)),
						vc.ratExpr(((IntObject) exponent).getInt()));
			else
				result = vc.powExpr(
						translate((SymbolicExpression) expr.argument(0)),
						translate((SymbolicExpression) exponent));
			break;
		}
		case SUBTRACT:
			result = vc.minusExpr(
					translate((SymbolicExpression) expr.argument(0)),
					translate((SymbolicExpression) expr.argument(1)));
			break;
		case SYMBOLIC_CONSTANT:
			result = translateSymbolicConstant((SymbolicConstant) expr, false);
			break;
		case TUPLE_READ:
			result = vc.tupleSelectExpr(
					translate((SymbolicExpression) expr.argument(0)),
					((IntObject) expr.argument(1)).getInt());
			break;
		case TUPLE_WRITE:
			result = vc.tupleUpdateExpr(
					translate((SymbolicExpression) expr.argument(0)),
					((IntObject) expr.argument(1)).getInt(),
					translate((SymbolicExpression) expr.argument(2)));
			break;
		case UNION_EXTRACT:
			result = translateUnionExtract(expr);
			break;
		case UNION_INJECT:
			result = translateUnionInject(expr);
			break;
		case UNION_TEST:
			result = translateUnionTest(expr);
			break;
		default:
			throw new SARLInternalException("unreachable");
		}
		return result;
	}

	private QueryResult queryCVC3(BooleanExpression symbolicPredicate) {
		QueryResult result = null;
		int numValidCalls = -1;

		universe.incrementProverValidCount();
		if (showProverQueries) {
			numValidCalls = universe.numValidCalls();
			out.println();
			out.print("SARL context " + numValidCalls + ": ");
			out.println(context);
			out.print("SARL predicate  " + numValidCalls + ": ");
			out.println(symbolicPredicate);
			out.flush();
		}
		try {
			Expr cvcPredicate;

			this.vc.push();
			intDivisionStack
					.add(new HashMap<Pair<SymbolicExpression, SymbolicExpression>, IntDivisionInfo>());
			translationStack.add(new HashMap<SymbolicExpression, Expr>());
			cvcPredicate = translate(symbolicPredicate);
			if (showProverQueries) {
				out.println();
				out.print("CVC3 assumptions " + numValidCalls + ": ");
				for (Object o : vc.getUserAssumptions()) {
					out.println(o);
				}
				out.print("CVC3 predicate   " + numValidCalls + ": ");
				out.println(cvcPredicate);
				out.flush();
			}
			result = vc.query(cvcPredicate);
		} catch (Cvc3Exception e) {
			e.printStackTrace();
			throw new SARLInternalException(
					"Error in parsing the symbolic expression or querying CVC3:\n"
							+ e);
		}
		return result;
	}

	/**
	 * Pops the CVC3 stack. This means all the assertions made between the last
	 * push and now will go away.
	 */
	private void popCVC3() {
		try {
			vc.pop();
		} catch (Cvc3Exception e) {
			throw new SARLInternalException("CVC3 error: " + e);
		}
		intDivisionStack.removeLast();
		translationStack.removeLast();
	}

	private ValidityResult translateResult(QueryResult result) {
		if (showProverQueries) {
			out.println("CVC3 result      " + universe.numValidCalls() + ": "
					+ result);
			out.flush();
		}
		// unfortunately QueryResult is not an enum...
		if (result.equals(QueryResult.VALID)) {
			return Prove.RESULT_YES;
		} else if (result.equals(QueryResult.INVALID)) {
			return Prove.RESULT_NO;
		} else if (result.equals(QueryResult.UNKNOWN)) {
			return Prove.RESULT_MAYBE;
		} else if (result.equals(QueryResult.ABORT)) {
			out.println("Warning: Query aborted by CVC3.");
			return Prove.RESULT_MAYBE;
		} else {
			out.println("Warning: Unknown CVC3 query result: " + result);
			return Prove.RESULT_MAYBE;
		}
	}

	// Public methods...

	public Map<SymbolicExpression, Expr> expressionMap() {
		return expressionMap;
	}

	public Map<Op, SymbolicConstant> opMap() {
		return opMap;
	}

	public Map<Expr, SymbolicConstant> varMap() {
		return varMap;
	}

	public ValidityChecker validityChecker() {
		return vc;
	}

	public PrintStream out() {
		return out;
	}

	/**
	 * Translates the symbolic type to a CVC3 type.
	 * 
	 * @param type
	 *            a SARL symbolic expression type
	 * @return the equivalent CVC3 type
	 * @throws Cvc3Exception
	 *             if CVC3 throws an exception
	 */
	public Type translateType(SymbolicType type) throws Cvc3Exception {
		Type result = typeMap.get(type);

		if (result != null)
			return result;

		SymbolicTypeKind kind = type.typeKind();

		switch (kind) {

		case BOOLEAN:
			result = vc.boolType();
			break;
		case INTEGER:
			result = vc.intType();
			break;
		case REAL:
			result = vc.realType();
			break;
		case ARRAY:
			result = vc.arrayType(vc.intType(),
					translateType(((SymbolicArrayType) type).elementType()));
			if (!(type instanceof SymbolicCompleteArrayType))
				// tuple:<extent,array>
				result = vc.tupleType(vc.intType(), result);
			break;
		case TUPLE:
			result = vc
					.tupleType(translateTypeSequence(((SymbolicTupleType) type)
							.sequence()));
			break;
		case FUNCTION:
			result = vc.funType(
					translateTypeSequence(((SymbolicFunctionType) type)
							.inputTypes()),
					translateType(((SymbolicFunctionType) type).outputType()));
			break;
		case UNION: {
			SymbolicUnionType unionType = (SymbolicUnionType) type;
			List<String> constructors = new LinkedList<String>();
			List<List<String>> selectors = new LinkedList<List<String>>();
			List<List<Expr>> types = new LinkedList<List<Expr>>();
			SymbolicTypeSequence sequence = unionType.sequence();
			int index = 0;

			for (SymbolicType t : sequence) {
				List<String> selectorList = new LinkedList<String>();
				List<Expr> typeList = new LinkedList<Expr>();

				selectorList.add(selector(unionType, index));
				typeList.add(translateType(t).getExpr());
				selectors.add(selectorList);
				types.add(typeList);
				constructors.add(constructor(unionType, index));
				index++;
			}
			result = vc.dataType(unionType.name().getString(), constructors,
					selectors, types);
			break;
		}
		default:
			throw new RuntimeException("Unknown type: " + type);
		}
		typeMap.put(type, result);
		return result;
	}

	/**
	 * Translate expr from SARL to CVC3. This results in two things: a CVC3
	 * expression (which is returned) and also side-effects: constraints added
	 * to the CVC3 assumption set, possibly involving auxiliary variables.
	 * 
	 * Attempts to re-use previous cached translation results.
	 * 
	 * Protocol: look through the translationStack. If you find an entry for
	 * expr, return it.
	 * 
	 * Otherwise, look in the (permanent) expressionMap. If you find an entry
	 * for expr there, that means it was translated while processesing some
	 * previous query. You can re-use the result, but you still have to process
	 * expr for side effects. Side effects arise while translating integer
	 * division or modulus expressions. A side effect adds some constraint(s) to
	 * the CVC3 assumption set, and these constraints need to be added to the
	 * current assumption set.
	 * 
	 * If not found in expressionMap, then go through and translate the expr
	 * recursively, carrying out side-effects as you go along.
	 * 
	 * @param expr
	 *            any SARL expression
	 * @return the CVC3 expression resulting from translation
	 */
	public Expr translate(SymbolicExpression expr) {
		Expr result;
		Iterator<Map<SymbolicExpression, Expr>> iter = translationStack
				.descendingIterator();

		while (iter.hasNext()) {
			Map<SymbolicExpression, Expr> map = iter.next();

			result = map.get(expr);
			if (result != null)
				return result;
		}
		result = expressionMap.get(expr);
		if (result != null) {
			sideEffect(expr);
			translationStack.getLast().put(expr, result);
			return result;
		}
		result = translateWork(expr);
		translationStack.getLast().put(expr, result);
		this.expressionMap.put(expr, result);
		return result;
	}

	public boolean showProverQueries() {
		return showProverQueries;
	}

	@Override
	public ValidityResult valid(BooleanExpression symbolicPredicate) {
		QueryResult result = queryCVC3(symbolicPredicate);

		popCVC3();
		return translateResult(result);
	}

	@Override
	public PreUniverse universe() {
		return universe;
	}

	@Override
	public String toString() {
		return "CVC3TheoremProver";
	}

	@Override
	public void setOutput(PrintStream out) {
		this.out = out;
		showProverQueries = out != null;
	}

	/**
	 * In progress. Some notes from comments from CVC3 source for function
	 * vc_getConcreteModel:
	 * 
	 * "Will assign concrete values to all user created variables. This function
	 * should only be called after a query which return false. Returns an array
	 * of Exprs with size *size. The caller is responsible for freeing the array
	 * when finished with it by calling vc_deleteVector."
	 */
	@Override
	public ValidityResult validOrModel(BooleanExpression predicate) {
		QueryResult cvcResult = queryCVC3(predicate);

		if (cvcResult.equals(QueryResult.INVALID)) {
			Map<?, ?> cvcModel = vc.getConcreteModel();
			CVC3ModelFinder finder = new CVC3ModelFinder(this, cvcModel);
			Map<SymbolicConstant, SymbolicExpression> model = finder.getModel();

			return Prove.modelResult(model);
		}
		popCVC3();
		return translateResult(cvcResult);
	}

	@Override
	protected void finalize() {
		intDivisionStack.removeLast();
		translationStack.removeLast();
		// apparently this is necessary before the finalize
		// method in vc is invoked...
		try {
			if (vc != null)
				this.vc.delete();
		} catch (Cvc3Exception e) {
			throw new SARLInternalException(
					"CVC3: could not delete validity checker:\n" + e);
		}
	}

}
