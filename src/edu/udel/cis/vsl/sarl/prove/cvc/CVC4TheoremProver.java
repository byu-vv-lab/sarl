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
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import edu.nyu.acsys.CVC4.Datatype;
import edu.nyu.acsys.CVC4.DatatypeConstructor;
import edu.nyu.acsys.CVC4.DatatypeType;
import edu.nyu.acsys.CVC4.Expr;
import edu.nyu.acsys.CVC4.ExprManager;
import edu.nyu.acsys.CVC4.Integer;
import edu.nyu.acsys.CVC4.Kind;
import edu.nyu.acsys.CVC4.LogicInfo;
import edu.nyu.acsys.CVC4.Rational;
import edu.nyu.acsys.CVC4.Result;
import edu.nyu.acsys.CVC4.Result.Validity;
import edu.nyu.acsys.CVC4.SExpr;
import edu.nyu.acsys.CVC4.SmtEngine;
import edu.nyu.acsys.CVC4.TupleSelect;
import edu.nyu.acsys.CVC4.TupleType;
import edu.nyu.acsys.CVC4.TupleUpdate;
import edu.nyu.acsys.CVC4.Type;
import edu.nyu.acsys.CVC4.vectorExpr;
import edu.nyu.acsys.CVC4.vectorType;
import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.CharObject;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
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

/**
 * An implementation of TheoremProver using the automated theorem prover CVC4.
 * Transforms a theorem proving query into the language of CVC4, invokes CVC4
 * through its Java interface, and interprets the output.
 * 
 * Some notes about CVC4:
 * 
 * Documentation of mkVar: "This variable is guaranteed to be distinct from
 * every variable thus far in the ExprManager, even if it shares a name with
 * another; this is to support any kind of scoping policy on top of ExprManager.
 * The SymbolTable class can be used to store and lookup symbols by name, if
 * desired." Hence there is no need to re-name variables.
 * 
 * Think about making all arrays pairs.
 * 
 */
public class CVC4TheoremProver implements TheoremProver {

	// ************************** Static Fields *************************** //

	/**
	 * Should a new CVC4 statement engine and expression manager be instantiated
	 * for every query?
	 */
	public final static boolean resetAfterQuery = false;

	public final static int constBooleanKindInt = Kind.CONST_BOOLEAN
			.swigValue();

	public final static int constRationalKindInt = Kind.CONST_RATIONAL
			.swigValue();

	public final static int applyUFKindInt = Kind.APPLY_UF.swigValue();

	public final static int tupleKindInt = Kind.TUPLE.swigValue();

	public final static int selectKindInt = Kind.SELECT.swigValue();

	public final static int storeKindInt = Kind.STORE.swigValue();

	// ****************************** Fields ****************************** //

	/**
	 * The given assumption under which this theorem prover operates.
	 */
	private BooleanExpression context;

	/**
	 * The symbolic universe used for managing symbolic expressions. Initialized
	 * by constructor and never changes.
	 */
	private PreUniverse universe;

	/**
	 * The CVC4 object used for creating CVC4 {@link Expr}s (expressions).
	 * Created once during instantiation and never modified.
	 */
	private ExprManager em;

	/**
	 * The CVC4 SmtEngine used to checks queries when a model is not needed.
	 * 
	 */
	private SmtEngine basicSmt;

	/**
	 * The CVC4 SmtEngine used exclusively for finding models.
	 */
	private SmtEngine modelSmt;

	/**
	 * The number of auxiliary CVC4 variables created. These are the variables
	 * that do not correspond to any SARL variable but are needed for some
	 * reason to translate an expression. Includes both ordinary and bound CVC4
	 * variables.
	 */
	private int auxVarCount;

	/**
	 * Mapping of SARL symbolic expression to corresponding CVC4 expresssion.
	 * Used to cache the results of translation.
	 */
	private Map<SymbolicExpression, Expr> expressionMap;

	/**
	 * Map from SARL symbolic constants to corresponding CVC4 variables. Entries
	 * are a subset of those of {@link #expressionMap}.
	 */
	private Map<SymbolicConstant, Expr> variableMap;

	/**
	 * Mapping of SARL symbolic type to corresponding CVC4 type. Used to cache
	 * results of translation.
	 */
	private Map<SymbolicType, Type> typeMap;

	/**
	 * The translation of the given context to a CVC4 expression. Created once
	 * during instantiation and never modified.
	 */
	private Expr cvcAssumption;

	/**
	 * The CVC4 integer constant 0.
	 */
	private Expr cvc0;

	/**
	 * The CVC4 boolean constant "true".
	 */
	private Expr cvcTrue;

	/**
	 * The CVC4 boolean constant "false".
	 */
	private Expr cvcFalse;

	private Expr select0;

	private Expr select1;

	// *************************** Constructors *************************** //

	/**
	 * Constructs new CVC4 theorem prover with given symbolic universe.
	 * 
	 * @param universe
	 *            the controlling symbolic universe
	 * @param context
	 *            the assumption(s) the prover will use for queries
	 */
	CVC4TheoremProver(PreUniverse universe, BooleanExpression context) {
		assert universe != null;
		assert context != null;
		this.universe = universe;
		// The following is necessary since the same bound symbolic constant
		// can be used in different scopes in the context; CVC4 requires
		// that these map to distinct variables.
		//
		// Can we use a stack instead?
		// Every time you enter a quantified scope, push the
		// stack. Entries on the stack are maps from symbolic
		// expressions to Expr as above. When you translate
		// a binding instance you always make a new variable
		// and add an entry to the new stack frame. When you
		// look up an expression to see if it has already been
		// translated. Problem: if you see "x+1" you may find
		// an earlier "x+1" for a different x. There is no
		// way around it... you have to get a unique bound
		// variable somehow.
		this.context = (BooleanExpression) universe
				.cleanBoundVariables(context);
		init();
	}

	// ************************* Private Methods ************************** //

	private SmtEngine newBasicSmt(ExprManager em) {
		LogicInfo info = new LogicInfo();
		SmtEngine smt = new SmtEngine(em);

		// the following is necessary if you are going to make
		// multiple verify calls with the same SmtEngine:
		if (!resetAfterQuery)
			smt.setOption("incremental", new SExpr(true));
		// forbid the SMT from finding models (counterexamples)...
		smt.setOption("produce-models", new SExpr(false));
		// this is necessary to use method getAssertions:
		if (universe.getShowProverQueries())
			smt.setOption("interactive-mode", new SExpr(true));
		// allows integer division/modulus with constant denominator:
		smt.setOption("rewrite-divk", new SExpr(true));
		// if you enable non-linear arithmetic, you DISABLE model-finding!
		info.arithNonLinear();
		info.enableQuantifiers();
		info.enableIntegers();
		info.enableReals();
		smt.setLogic(info);
		if (!context.isTrue()) {
			cvcAssumption = translate(context);
			if (universe.getShowProverQueries()) {
				universe.getOutputStream().println(
						"New CVC4 system formed with assumption:\n  "
								+ cvcAssumption);
			}
			smt.assertFormula(cvcAssumption);
		}
		return smt;
	}

	private SmtEngine newModelSmt(ExprManager em) {
		LogicInfo info = new LogicInfo();
		SmtEngine smt = new SmtEngine(em);

		// the following is necessary if you are going to make
		// multiple verify calls with the same SmtEngine:
		if (!resetAfterQuery)
			smt.setOption("incremental", new SExpr(true));
		// allow the SMT to find models (counterexamples)...
		smt.setOption("produce-models", new SExpr(true));
		// this is necessary to use method getAssertions:
		if (universe.getShowProverQueries())
			smt.setOption("interactive-mode", new SExpr(true));
		// allows integer division/modulus with constant denominator:
		smt.setOption("rewrite-divk", new SExpr(true));
		// if you enable non-linear arithmetic, you DISABLE model-finding!
		// info.arithNonLinear();
		info.enableQuantifiers();
		info.enableIntegers();
		info.enableReals();
		smt.setLogic(info);
		if (!context.isTrue()) {
			cvcAssumption = translate(context);
			if (universe.getShowProverQueries()) {
				universe.getOutputStream().println(
						"New CVC4 system formed with assumption:\n  "
								+ cvcAssumption);
			}
			smt.assertFormula(cvcAssumption);
		}
		return smt;
	}

	private void init() {
		em = new ExprManager();
		basicSmt = null;
		modelSmt = null;
		auxVarCount = 0;
		expressionMap = new HashMap<>();
		variableMap = new HashMap<>();
		typeMap = new HashMap<SymbolicType, Type>();
		cvc0 = em.mkConst(new Rational(0, 1));
		cvcTrue = em.mkConst(true);
		cvcFalse = em.mkConst(false);
		select0 = em.mkConst(new TupleSelect(0));
		select1 = em.mkConst(new TupleSelect(1));
	}

	/**
	 * Gets rid of the old expression manager and statement engine and replaces
	 * them with new ones; resets other state.
	 */
	private void reset() {
		if (universe.getShowProverQueries()) {
			universe.getOutputStream().println("Destroying old CVC4 system.");
		}
		init();
	}

	/**
	 * Creates a new CVC4 (ordinary) variable of given type with unique name;
	 * increments {@link #auxVarCount}.
	 * 
	 * @param type
	 *            a CVC4 type
	 * @return the new CVC4 variable
	 */
	private Expr newAuxVar(Type type) {
		Expr result = em.mkVar("tmp" + auxVarCount, type);

		auxVarCount++;
		return result;
	}

	/**
	 * Creates a new CVC4 bound variable of given type with unique name;
	 * increments {@link #auxVarCount}.
	 * 
	 * @param type
	 *            a CVC4 type
	 * @return the new bound variable
	 */
	private Expr newAuxBoundVar(Type type) {
		Expr result = em.mkBoundVar("tmp" + auxVarCount, type);

		auxVarCount++;
		return result;
	}

	/**
	 * This method takes in the length and value of an Expr and returns the
	 * ordered pair represented by an incomplete array type.
	 * 
	 * @param length
	 *            CVC4 Expr of length
	 * @param value
	 *            CVC4 Expr of value
	 * @return CVC4 tuple of an ordered pair (length, array)
	 */
	private Expr bigArray(Expr length, Expr value) {
		return em.mkExpr(Kind.TUPLE, length, value);
	}

	/**
	 * Given any CVC Expr which has type (Integer, array-of-T), returns the
	 * first component, i.e., the component of integer type.
	 * 
	 * @param bigArray
	 *            any CVC Expr of type (Integer, array-of-T) for some T
	 * @return the first component of that expression
	 */
	private Expr bigArrayLength(Expr bigArray) {
		Expr result;

		if (bigArray.getKind().equals(Kind.TUPLE)) {
			result = bigArray.getChild(0);
		} else {
			result = em.mkExpr(select0, bigArray);
		}
		return result;
	}

	/**
	 * Given any CVC Expr which has type (Integer, array-of-T), returns the
	 * second component, i.e., the component of array type.
	 * 
	 * @param bigArray
	 *            any CVC Expr of type (Integer, array-of-T) for some T
	 * @return the second component of that expression
	 */
	private Expr bigArrayValue(Expr bigArray) {
		Expr result;

		if (bigArray.getKind().equals(Kind.TUPLE)) {
			result = bigArray.getChild(1);
		} else {
			result = em.mkExpr(select1, bigArray);
		}
		return result;
	}

	/**
	 * Computes the name of the index-th selector function into a union type.
	 * This is the function that taken an element of the union and returns an
	 * element of the index-th member type.
	 * 
	 * @param unionType
	 *            a union type
	 * @param index
	 *            integer in [0,n), where n is the number of member types of the
	 *            union type
	 * @return the name of the index-th selector function
	 */
	private String selector(SymbolicUnionType unionType, int index) {
		return unionType.name().toString() + "_extract_" + index;
	}

	/**
	 * Computes the name of the index-th constructor function for a union type.
	 * This is the function which takes as input an element of the index-th
	 * member type and returns an element of the union type.
	 * 
	 * @param unionType
	 *            a union type
	 * @param index
	 *            an integer in [0,n), where n is the number of member types of
	 *            the union type
	 * @return the name of the index-th constructor function
	 */
	private String constructor(SymbolicUnionType unionType, int index) {
		return unionType.name().toString() + "_inject_" + index;
	}

	/**
	 * Translates an ordered SARL symbolic collection of symbolic expressions to
	 * CVC expressions. Null elements are translated to Java <code>null</code>.
	 * 
	 * @param collection
	 *            an ordered SARL symbolic collection
	 * @return a CVC vectorExpr (a vector of Expr) in which element i is the
	 *         result of translating the i-th symbolic expression from the
	 *         collection
	 */
	private vectorExpr translateCollection(SymbolicCollection<?> collection) {
		vectorExpr result = new vectorExpr();

		for (SymbolicExpression expr : collection) {
			if (expr == null)
				result.add(null);
			else
				result.add(translate(expr));
		}
		return result;
	}

	/**
	 * Translates any concrete SymbolicExpression with concrete type to
	 * equivalent CVC4 Expr using the ExprManager.
	 * 
	 * @param expr
	 * @return the CVC4 equivalent Expr
	 */
	private Expr translateConcrete(SymbolicExpression expr) {
		SymbolicType type = expr.type();
		SymbolicTypeKind kind = type.typeKind();
		SymbolicObject object = expr.argument(0);
		Expr result;

		switch (kind) {
		case ARRAY: {
			// type must be a complete array type
			SymbolicCompleteArrayType arrayType = (SymbolicCompleteArrayType) type;
			SymbolicType elementType = arrayType.elementType();
			NumericExpression extentExpression = arrayType.extent();
			IntegerNumber extentNumber = (IntegerNumber) universe
					.extractNumber(extentExpression);
			SymbolicSequence<?> sequence = (SymbolicSequence<?>) object;
			int size = sequence.size();
			Type cvcElementType = translateType(elementType);
			Type cvcArrayType = em
					.mkArrayType(em.integerType(), cvcElementType);

			assert extentNumber != null && extentNumber.intValue() == size;
			result = newAuxVar(cvcArrayType);
			for (int i = 0; i < size; i++)
				result = em.mkExpr(Kind.STORE, result,
						em.mkConst(new Rational(i, 1)),
						translate(sequence.get(i)));
			result = bigArray(em.mkConst(new Rational(size, 1)), result);
			break;
		}
		case BOOLEAN:
			result = ((BooleanObject) object).getBoolean() ? cvcTrue : cvcFalse;
			break;
		case CHAR:
			result = em
					.mkConst(new Rational(((CharObject) object).getChar(), 1));
			break;
		case INTEGER: {
			IntegerNumber integerNumber = (IntegerNumber) ((NumberObject) object)
					.getNumber();
			Integer cvcInteger = new Integer(integerNumber.toString());

			// yes, toString is the only reliable and efficient way
			// to do this.
			result = em.mkConst(new Rational(cvcInteger));
			break;
		}
		case REAL: {
			RationalNumber rationalNumber = (RationalNumber) ((NumberObject) object)
					.getNumber();
			BigInteger numerator = rationalNumber.numerator(), denominator = rationalNumber
					.denominator();
			Integer cvcNumerator = new Integer(numerator.toString()), cvcDenominator = new Integer(
					denominator.toString());
			Rational rational = new Rational(cvcNumerator, cvcDenominator);

			result = em.mkConst(rational);
			break;
		}
		case TUPLE:
			vectorExpr members = translateCollection((SymbolicSequence<?>) object);

			result = em.mkExpr(Kind.TUPLE, members);
			break;
		default:
			throw new SARLInternalException("Unknown concrete object: " + expr);
		}
		return result;
	}

	/**
	 * Translates a symbolic constant to CVC4 variable. Special handling is
	 * required if the symbolic constant is used as a bound variable in a
	 * quantified (forall, exists) expression.
	 * 
	 * Precondition: ?
	 * 
	 * @param symbolicConstant
	 * @param isBoundVariable
	 * @return the CVC4 equivalent Expr
	 */
	private Expr translateSymbolicConstant(SymbolicConstant symbolicConstant,
			boolean isBoundVariable) {
		String name = symbolicConstant.name().getString();
		SymbolicType symbolicType = symbolicConstant.type();
		Type type = translateType(symbolicConstant.type());
		Expr result;

		if (isBoundVariable) {
			result = em.mkBoundVar(name, type);
		} else {
			if (symbolicType instanceof SymbolicCompleteArrayType) {
				Type cvcArrayType = (new TupleType(type)).getTypes().get(1);
				Expr var = em.mkVar(name, cvcArrayType);
				NumericExpression length = ((SymbolicCompleteArrayType) symbolicType)
						.extent();
				Expr cvcLength = translate(length);

				result = em.mkExpr(Kind.TUPLE, cvcLength, var);
			} else {
				result = em.mkVar(type);
			}
		}
		this.variableMap.put(symbolicConstant, result);
		return result;
	}

	private Expr translateLambda(SymbolicExpression expr) {
		Expr result = em.mkExpr(
				Kind.LAMBDA,
				em.mkExpr(
						Kind.BOUND_VAR_LIST,
						translateSymbolicConstant(
								(SymbolicConstant) expr.argument(0), true)),
				translate((SymbolicExpression) expr.argument(1)));

		return result;
	}

	// /**
	// * Tells CVC4 to assume that an array index is within bounds.
	// *
	// * @param arrayExpression
	// * @param index
	// */
	// private void assertIndexInBounds(SymbolicExpression arrayExpression,
	// NumericExpression index) {
	// NumericExpression length = universe.length(arrayExpression);
	// BooleanExpression predicate = universe.lessThan(index, length);
	// Expr cvcPredicate;
	//
	// predicate = universe.and(
	// universe.lessThanEquals(universe.zeroInt(), index), predicate);
	// cvcPredicate = translate(predicate);
	// smt.assertFormula(cvcPredicate);
	// }

	/**
	 * Translates an array-read expression a[i] into equivalent CVC4 expression
	 * 
	 * @param expr
	 *            a SARL symbolic expression of form a[i]
	 * @return an equivalent CVC4 expression
	 */
	private Expr translateArrayRead(SymbolicExpression expr) {
		SymbolicExpression arrayExpression = (SymbolicExpression) expr
				.argument(0);
		NumericExpression indexExpression = (NumericExpression) expr
				.argument(1);
		Expr array, arrayValue, index, result;

		// assertIndexInBounds(arrayExpression, indexExpression);
		array = translate(arrayExpression);
		arrayValue = bigArrayValue(array);
		index = translate(indexExpression);
		result = em.mkExpr(Kind.SELECT, arrayValue, index);
		return result;
	}

	/**
	 * Translates an array-write (or array update) SARL symbolic expression to
	 * equivalent CVC4 expression.
	 * 
	 * @param expr
	 *            an array update expression array[WITH i:=newValue].
	 * @return the equivalent CVC4 Expr
	 */
	private Expr translateArrayWrite(SymbolicExpression expr) {
		SymbolicExpression arrayExpression = (SymbolicExpression) expr
				.argument(0);
		NumericExpression indexExpression = (NumericExpression) expr
				.argument(1);
		SymbolicExpression valueExpression = (SymbolicExpression) expr
				.argument(2);
		Expr array, arrayValue, length, index, value, result;

		// assertIndexInBounds(arrayExpression, indexExpression);
		array = translate(arrayExpression);
		arrayValue = bigArrayValue(array);
		length = bigArrayLength(array);
		index = translate(indexExpression);
		value = translate(valueExpression);
		result = bigArray(length,
				em.mkExpr(Kind.STORE, arrayValue, index, value));
		return result;
	}

	/**
	 * Translates a multiple array-write (or array update) SARL symbolic
	 * expression to equivalent CVC4 expression.
	 * 
	 * @param expr
	 *            an array update expression array [WITH i:=newValue]...[WITH
	 *            i:=newValue]
	 * @return the equivalent CVC4 Expr
	 */
	private Expr translateDenseArrayWrite(SymbolicExpression expr) {
		SymbolicExpression arrayExpression = (SymbolicExpression) expr
				.argument(0);
		Expr origin = translate(arrayExpression);
		Expr result = bigArrayValue(origin);
		Expr length = bigArrayLength(origin);
		SymbolicSequence<?> values = (SymbolicSequence<?>) expr.argument(1);
		int numValues = values.size();

		for (int i = 0; i < numValues; i++) {
			SymbolicExpression value = values.get(i);

			if (value != null) {
				Expr cvcIndex = em.mkConst(new Rational(i, 1));
				Expr cvcValue = translate(value);

				result = em.mkExpr(Kind.STORE, result, cvcIndex, cvcValue);
			}
		}
		// assertIndexInBounds(arrayExpression, universe.integer(numValues -
		// 1));
		result = bigArray(length, result);
		return result;
	}

	/**
	 * Translate a multiple tuple-write (or tuple update) SARL symbolic
	 * expression to equivalent CVC4 expression.
	 * 
	 * @param expr
	 *            a tuple update expression
	 * @return the equivalent CVC4 Expr
	 */
	private Expr translateDenseTupleWrite(SymbolicExpression expr) {
		SymbolicExpression tupleExpression = (SymbolicExpression) expr
				.argument(0);
		Expr result = translate(tupleExpression);
		SymbolicSequence<?> values = (SymbolicSequence<?>) expr.argument(1);
		int numValues = values.size();

		for (int index = 0; index < numValues; index++) {
			SymbolicExpression value = values.get(index);

			if (value != null) {
				Expr update = em.mkConst(new TupleUpdate(index));
				Expr cvcValue = translate(value);

				result = em.mkExpr(update, result, cvcValue);
			}
		}
		return result;
	}

	/**
	 * Translates SymbolicExpressions of the type "exists" and "for all" into
	 * the CVC4 equivalent Expr
	 * 
	 * @param expr
	 *            a "exists" or "for all" expression
	 * @return the equivalent CVC4 Expr
	 */
	private Expr translateQuantifier(SymbolicExpression expr) {
		SymbolicOperator kind = expr.operator();
		SymbolicConstant boundVariable = (SymbolicConstant) (expr.argument(0));
		Expr boundVar = translateSymbolicConstant(boundVariable, true);
		Expr boundVariableList, predicate;

		expressionMap.put(boundVariable, boundVar);
		boundVariableList = em.mkExpr(Kind.BOUND_VAR_LIST, boundVar);
		predicate = translate((SymbolicExpression) expr.argument(1));
		if (kind == SymbolicOperator.FORALL)
			return em.mkExpr(Kind.FORALL, boundVariableList, predicate);
		else if (kind == SymbolicOperator.EXISTS)
			return em.mkExpr(Kind.EXISTS, boundVariableList, predicate);
		throw new SARLInternalException(
				"Cannot translate quantifier into CVC4: " + expr);
	}

	/**
	 * Processes the equality of two arrays. Arrays can be of complete type or
	 * incomplete type.
	 * 
	 * @param type1
	 *            a SARL SymbolicType
	 * @param type2
	 *            a SARL SymbolicType
	 * @param cvcExpression1
	 *            a CVC4 array
	 * @param cvcExpression2
	 *            a CVC4 array
	 * @return
	 */
	private Expr processEquality(SymbolicType type1, SymbolicType type2,
			Expr cvcExpression1, Expr cvcExpression2) {
		Expr result;

		if (type1.typeKind() == SymbolicTypeKind.ARRAY) {
			// lengths are equal and forall i (0<=i<length).a[i]=b[i].
			SymbolicArrayType arrayType1 = (SymbolicArrayType) type1;
			SymbolicArrayType arrayType2 = (SymbolicArrayType) type2;
			Expr extent1 = bigArrayLength(cvcExpression1);
			Expr array1 = bigArrayValue(cvcExpression1);
			Expr extent2 = bigArrayLength(cvcExpression2);
			Expr array2 = bigArrayValue(cvcExpression2);
			Expr index = newAuxBoundVar(em.integerType());
			Expr indexRangeExpr = em.mkExpr(Kind.AND,
					em.mkExpr(Kind.GEQ, index, cvc0),
					em.mkExpr(Kind.LT, index, extent1));
			Expr readExpr1 = em.mkExpr(Kind.SELECT, array1, index);
			Expr readExpr2 = em.mkExpr(Kind.SELECT, array2, index);
			Expr elementEqualsExpr = processEquality(arrayType1.elementType(),
					arrayType2.elementType(), readExpr1, readExpr2);
			Expr forallExpr = em.mkExpr(Kind.FORALL,
					em.mkExpr(Kind.BOUND_VAR_LIST, index),
					em.mkExpr(Kind.IMPLIES, indexRangeExpr, elementEqualsExpr));

			result = em.mkExpr(Kind.AND,
					em.mkExpr(Kind.EQUAL, extent1, extent2), forallExpr);
		} else {
			result = em.mkExpr(Kind.EQUAL, cvcExpression1, cvcExpression2);
		}
		return result;
	}

	/**
	 * Translates a SymbolicExpression that represents a == b into the CVC4
	 * equivalent Expr
	 * 
	 * @param expr
	 *            the equals type expression
	 * @return the equivalent CVC4 Expr
	 */
	private Expr translateEquality(SymbolicExpression expr) {
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
	 * @return the CVC4 translation of that expression
	 */
	private Expr translateUnionExtract(SymbolicExpression expr) {
		int index = ((IntObject) expr.argument(0)).getInt();
		SymbolicExpression arg = (SymbolicExpression) expr.argument(1);
		Expr cvcArg = translate(arg);
		DatatypeType datatypeType = new DatatypeType(cvcArg.getType());
		Datatype datatype = datatypeType.getDatatype();
		DatatypeConstructor constructor = datatype.get(index);
		Expr selector = constructor.get(0).getSelector();
		Expr result = em.mkExpr(Kind.APPLY_SELECTOR, selector, cvcArg);

		return result;
	}

	/**
	 * UNION_INJECT: injects an element of a member type into a union type that
	 * includes that member type. 2 arguments: arg0 is an IntObject giving the
	 * index of the member type of the union type; arg1 is a symbolic expression
	 * whose type is the member type. The union type itself is the type of the
	 * UNION_INJECT expression.
	 * 
	 * @param expr
	 *            a "union inject" expression
	 * @return the CVC4 translation of that expression
	 */
	private Expr translateUnionInject(SymbolicExpression expr) {
		int index = ((IntObject) expr.argument(0)).getInt();
		SymbolicExpression arg = (SymbolicExpression) expr.argument(1);
		SymbolicUnionType unionType = (SymbolicUnionType) expr.type();
		DatatypeType datatypeType = new DatatypeType(translateType(unionType));
		Datatype datatype = datatypeType.getDatatype();
		Expr constructor = datatype.get(index).getConstructor();
		Expr result;

		result = em.mkExpr(Kind.APPLY_CONSTRUCTOR, constructor, translate(arg));
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
	 * @return the CVC4 translation of that expression
	 */
	private Expr translateUnionTest(SymbolicExpression expr) {
		int index = ((IntObject) expr.argument(0)).getInt();
		SymbolicExpression arg = (SymbolicExpression) expr.argument(1);
		Expr cvcArg = translate(arg);
		DatatypeType datatypeType = new DatatypeType(cvcArg.getType());
		Datatype datatype = datatypeType.getDatatype();
		DatatypeConstructor constructor = datatype.get(index);
		Expr tester = constructor.getTester();
		Expr result = em.mkExpr(Kind.APPLY_TESTER, tester, cvcArg);

		return result;
	}

	/**
	 * Translates a SARL symbolic expression to a CVC4 expr.
	 * 
	 * @param expression
	 *            a SymbolicExpression
	 * @return
	 */
	private Expr translateWork(SymbolicExpression expression) {
		int numArgs = expression.numArguments();
		SymbolicOperator operator = expression.operator();
		Expr result;

		switch (operator) {
		case ADD:
			if (numArgs == 2)
				result = em.mkExpr(Kind.PLUS,
						translate((SymbolicExpression) expression.argument(0)),
						translate((SymbolicExpression) expression.argument(1)));
			else if (numArgs == 1)
				result = em.mkAssociative(Kind.PLUS,
						translateCollection((SymbolicCollection<?>) expression
								.argument(0)));
			else
				throw new SARLInternalException(
						"Expected 1 or 2 arguments for ADD");
			break;
		case AND:
			if (numArgs == 2)
				result = em.mkExpr(Kind.AND,
						translate((SymbolicExpression) expression.argument(0)),
						translate((SymbolicExpression) expression.argument(1)));
			else if (numArgs == 1)
				result = em.mkAssociative(Kind.AND,
						translateCollection((SymbolicCollection<?>) expression
								.argument(0)));

			else
				throw new SARLInternalException(
						"Expected 1 or 2 arguments for AND: " + expression);
			break;
		case APPLY: {
			SymbolicExpression function = (SymbolicExpression) expression
					.argument(0);
			SymbolicSequence<?> arguments = (SymbolicSequence<?>) expression
					.argument(1);
			Expr cvcFunction = translate(function);
			// Type cvcType = cvcFunction.getType();
			// FunctionType cvcFunctionType = new FunctionType(cvcType);
			vectorExpr cvcArguments = translateCollection(arguments);

			result = em.mkExpr(Kind.APPLY_UF, cvcFunction, cvcArguments);
			break;
		}
		case ARRAY_LAMBDA: {
			// TODO
			// SymbolicExpression function = (SymbolicExpression) expression
			// .argument(0);
			// SymbolicOperator op0 = function.operator();
			// Expr var, body;
			//
			// if (op0 == SymbolicOperator.LAMBDA) {
			// var = translate((SymbolicConstant) function.argument(0));
			// body = translate((SymbolicExpression) function.argument(1));
			// } else {
			// // create new SymbolicConstantIF _SARL_i
			// // create new APPLY expression apply(f,i)
			// // need universe.
			// // or just assert forall i.a[i]=f(i)
			// }
			throw new UnsupportedOperationException(
					"Array lambdas are not supported in CVC4");
		}
		case ARRAY_READ:
			result = translateArrayRead(expression);
			break;
		case ARRAY_WRITE:
			result = translateArrayWrite(expression);
			break;
		case CAST:
			result = translate((SymbolicExpression) expression.argument(0));
			break;
		case CONCRETE:
			result = translateConcrete(expression);
			break;
		case COND:
			result = em.mkExpr(Kind.ITE,
					translate((SymbolicExpression) expression.argument(0)),
					translate((SymbolicExpression) expression.argument(1)),
					translate((SymbolicExpression) expression.argument(2)));
			break;
		case DENSE_ARRAY_WRITE:
			result = translateDenseArrayWrite(expression);
			break;
		case DENSE_TUPLE_WRITE:
			result = translateDenseTupleWrite(expression);
			break;
		case DIVIDE: // real division
			result = em.mkExpr(Kind.DIVISION,
					translate((SymbolicExpression) expression.argument(0)),
					translate((SymbolicExpression) expression.argument(1)));
			break;
		case EQUALS:
			result = translateEquality(expression);
			break;
		case EXISTS:
		case FORALL:
			result = translateQuantifier(expression);
			break;
		case INT_DIVIDE:
			result = em.mkExpr(Kind.INTS_DIVISION,
					translate((SymbolicExpression) expression.argument(0)),
					translate((SymbolicExpression) expression.argument(1)));
			break;
		case LENGTH:
			result = bigArrayLength(translate((SymbolicExpression) expression
					.argument(0)));
			break;
		case LESS_THAN:
			result = em.mkExpr(Kind.LT,
					translate((SymbolicExpression) expression.argument(0)),
					translate((SymbolicExpression) expression.argument(1)));
			break;
		case LESS_THAN_EQUALS:
			result = em.mkExpr(Kind.LEQ,
					translate((SymbolicExpression) expression.argument(0)),
					translate((SymbolicExpression) expression.argument(1)));
			break;
		case MODULO:
			result = em.mkExpr(Kind.INTS_MODULUS,
					translate((SymbolicExpression) expression.argument(0)),
					translate((SymbolicExpression) expression.argument(1)));
			break;
		case MULTIPLY:
			if (numArgs == 2)
				result = em.mkExpr(Kind.MULT,
						translate((SymbolicExpression) expression.argument(0)),
						translate((SymbolicExpression) expression.argument(1)));
			else if (numArgs == 1)
				result = em.mkAssociative(Kind.MULT,
						translateCollection((SymbolicCollection<?>) expression
								.argument(0)));
			else
				throw new SARLInternalException(
						"Expected 1 or 2 arguments for MULTIPLY: " + expression);
			break;
		case NEGATIVE:
			result = em.mkExpr(Kind.UMINUS,
					translate((SymbolicExpression) expression.argument(0)));
			break;
		case NEQ:
			result = translateEquality(expression);
			result = em.mkExpr(Kind.NOT, result);
			break;
		case NOT:
			result = em.mkExpr(Kind.NOT,
					translate((SymbolicExpression) expression.argument(0)));
			break;
		case OR:
			if (numArgs == 2)
				result = em.mkExpr(Kind.OR,
						translate((SymbolicExpression) expression.argument(0)),
						translate((SymbolicExpression) expression.argument(1)));
			else if (numArgs == 1)
				result = em.mkAssociative(Kind.OR,
						translateCollection((SymbolicCollection<?>) expression
								.argument(0)));

			else
				throw new SARLInternalException(
						"Expected 1 or 2 arguments for OR: " + expression);
			break;
		case POWER: {
			SymbolicObject exponent = expression.argument(1);

			if (exponent instanceof IntObject)
				result = em.mkExpr(Kind.POW,
						translate((SymbolicExpression) expression.argument(0)),
						em.mkConst(new Rational(
								((IntObject) exponent).getInt(), 1)));
			else
				result = em.mkExpr(Kind.POW,
						translate((SymbolicExpression) expression.argument(0)),
						translate((SymbolicExpression) exponent));
			break;
		}
		case SUBTRACT:
			result = em.mkExpr(Kind.MINUS,
					translate((SymbolicExpression) expression.argument(0)),
					translate((SymbolicExpression) expression.argument(1)));
			break;
		case SYMBOLIC_CONSTANT:
			result = translateSymbolicConstant((SymbolicConstant) expression,
					false);
			break;
		case TUPLE_READ:
			result = em.mkExpr(em.mkConst(new TupleSelect(
					((IntObject) expression.argument(1)).getInt())),
					translate((SymbolicExpression) expression.argument(0)));
			break;
		case TUPLE_WRITE:
			result = em.mkExpr(em.mkConst(new TupleUpdate(
					((IntObject) expression.argument(1)).getInt())),
					translate((SymbolicExpression) expression.argument(0)),
					translate((SymbolicExpression) expression.argument(2)));
			break;
		case UNION_EXTRACT:
			result = translateUnionExtract(expression);
			break;
		case UNION_INJECT:
			result = translateUnionInject(expression);
			break;
		case UNION_TEST:
			result = translateUnionTest(expression);
			break;
		case LAMBDA:
			result = translateLambda(expression);
			break;
		case NULL:
			result = null;
			break;
		default:
			throw new SARLInternalException("unreachable: unknown operator: "
					+ operator);
		}
		return result;
	}

	private DatatypeType translateUnionType(SymbolicUnionType unionType) {
		Datatype cvc4Union = new Datatype(unionType.name().getString());
		int index = 0;
		DatatypeType result;

		// make 1 constructor for each member type...
		for (SymbolicType memberType : unionType.sequence()) {
			DatatypeConstructor cons = new DatatypeConstructor(constructor(
					unionType, index));
			Type cvcMemberType = translateType(memberType);
			// note: the tester name is is_+constructor name.

			cons.addArg(selector(unionType, index), cvcMemberType);
			// add constructor to Datatype. extractor=selector
			cvc4Union.addConstructor(cons);
			index++;
		}
		result = em.mkDatatypeType(cvc4Union);
		return result;
	}

	/**
	 * Translates the symbolic type to a CVC4 type.
	 * 
	 * @param type
	 *            a SARL symbolic expression type
	 * @return the equivalent CVC4 type
	 */
	private Type translateType(SymbolicType type) {
		Type result = typeMap.get(type);

		if (result != null)
			return result;

		SymbolicTypeKind kind = type.typeKind();

		switch (kind) {
		case BOOLEAN:
			result = em.booleanType();
			break;
		case INTEGER:
		case CHAR:
			result = em.integerType();
			break;
		case REAL:
			result = em.realType();
			break;
		case ARRAY: {
			vectorType vector = new vectorType();
			Type arrayType = em.mkArrayType(em.integerType(),
					translateType(((SymbolicArrayType) type).elementType()));

			vector.add(em.integerType());
			vector.add(arrayType);
			result = em.mkTupleType(vector);
			break;
		}
		case TUPLE:
			result = em
					.mkTupleType(translateTypeSequence(((SymbolicTupleType) type)
							.sequence()));
			break;
		case FUNCTION: {
			SymbolicFunctionType funcType = (SymbolicFunctionType) type;
			SymbolicTypeSequence inputs = funcType.inputTypes();

			if (inputs.numTypes() == 0)
				throw new SARLException(
						"CVC3 requires function types to have at least one input");
			result = em.mkFunctionType(translateTypeSequence(inputs),
					translateType(funcType.outputType()));
			break;
		}
		case UNION:
			result = translateUnionType((SymbolicUnionType) type);
			break;
		default:
			throw new SARLInternalException("Unknown SARL type: " + type);
		}
		typeMap.put(type, result);
		return result;
	}

	/**
	 * Translate a given SymbolicTypeSequence to an equivalent linkedlist of
	 * Types in CVC4.
	 * 
	 * @param sequence
	 *            SymbolicTypeSequence given to the translation.
	 * @return linkedlist of CVC4 types.
	 */
	private vectorType translateTypeSequence(SymbolicTypeSequence sequence) {
		vectorType result = new vectorType();

		// TODO: is there something better than null here?
		for (SymbolicType t : sequence) {
			if (t == null)
				result.add(null);
			else
				result.add(translateType(t));
		}
		return result;
	}

	/**
	 * Translate a SARL symbolic expression to an equivalence CVC4 Expr. The
	 * translation result is cached.
	 * 
	 * @param expression
	 *            any SARL expression
	 * @return the CVC4 Expr resulting from translation
	 */
	private Expr translate(SymbolicExpression expression) {
		Expr result = expressionMap.get(expression);

		if (result == null) {
			result = translateWork(expression);
			this.expressionMap.put(expression, result);
		}
		return result;
	}

	private SymbolicExpression backTranslateArray(SymbolicType elementType,
			NumericExpression length, Expr expr) {
		IntegerNumber lengthNum = (IntegerNumber) universe
				.extractNumber(length);
		int lengthInt;
		SymbolicExpression[] elements;
		SymbolicExpression result;

		if (lengthNum == null)
			throw new SARLInternalException(
					"CVC4 model contains array of non-concrete length: "
							+ length);
		lengthInt = lengthNum.intValue();
		elements = new SymbolicExpression[lengthInt];
		for (int i = 0; i < lengthInt; i++) {
			Expr readExpr = em.mkExpr(Kind.SELECT, expr,
					em.mkConst(new Rational(i, 1)));
			Expr value = modelSmt.getValue(readExpr);
			SymbolicExpression sarlValue = backTranslate(elementType, value);

			elements[i] = sarlValue;
		}
		result = universe.array(elementType, Arrays.asList(elements));
		return result;
	}

	private SymbolicExpression backTranslateRational(SymbolicType sarlType,
			Expr expr) {
		Rational rational = expr.getConstRational();
		edu.nyu.acsys.CVC4.Integer numerator = rational.getNumerator();
		String numeratorString = numerator.toString();
		SymbolicExpression result;

		switch (sarlType.typeKind()) {
		case INTEGER:
			if (!rational.isIntegral())
				throw new SARLInternalException(
						"CVC4 produced model in which variable of integer type was assigned non-integer: "
								+ rational);
			result = universe.integer(new BigInteger(numerator.toString()));
			break;
		case CHAR:
			if (!rational.isIntegral())
				throw new SARLInternalException(
						"CVC4 produced model in which variable of char type was assigned non-integer: "
								+ rational);
			result = universe.character((char) numerator.getUnsignedInt());
			break;
		case REAL: {
			String denominatorString = rational.getDenominator().toString();

			result = universe.rational(new BigInteger(numeratorString),
					new BigInteger(denominatorString));
			break;
		}
		default:
			throw new SARLException(
					"CVC4 model produced rational value for SARL type "
							+ sarlType);
		}
		return result;
	}

	private SymbolicExpression backTranslateTuple(SymbolicType sarlType,
			Expr expr) {
		SymbolicExpression result;
		SymbolicTypeKind typeKind = sarlType.typeKind();

		// could be a tuple for an array, or for a tuple
		if (typeKind == SymbolicTypeKind.ARRAY) {
			// tuple is ordered pair (length,array); return a concrete array
			SymbolicType elementType = ((SymbolicArrayType) sarlType)
					.elementType();
			Expr lengthExpr = bigArrayLength(expr);
			Expr valueExpr = bigArrayValue(expr);
			NumericExpression length = (NumericExpression) backTranslate(
					universe.integerType(), lengthExpr);
			result = backTranslateArray(elementType, length, valueExpr);
		} else if (typeKind == SymbolicTypeKind.TUPLE) {
			SymbolicTupleType tupleType = (SymbolicTupleType) sarlType;
			SymbolicTypeSequence componentTypes = tupleType.sequence();
			int numComponents = componentTypes.numTypes();
			SymbolicExpression[] sarlComponents = new SymbolicExpression[numComponents];

			for (int i = 0; i < numComponents; i++) {
				SymbolicType componentType = componentTypes.getType(i);
				Expr component = expr.getChild(i);
				SymbolicExpression sarlComponent = backTranslate(componentType,
						component);

				sarlComponents[i] = sarlComponent;
			}
			result = universe.tuple(tupleType, Arrays.asList(sarlComponents));
		} else
			throw new SARLInternalException(
					"CVC4 model returned tuple for SARL type " + sarlType);
		return result;
	}

	/**
	 * Translates what should hopefully be a concrete CVC4 Expr to a concrete
	 * SARL symbolic expression.
	 * 
	 * @param expr
	 * @return
	 */
	private SymbolicExpression backTranslate(SymbolicType sarlType, Expr expr) {
		Kind kind = expr.getKind();
		int kindInt = kind.swigValue();
		SymbolicExpression result;

		if (kindInt == constBooleanKindInt) {
			result = universe.bool(expr.getConstBoolean());
		} else if (kindInt == constRationalKindInt) {
			result = backTranslateRational(sarlType, expr);
		} else if (kindInt == tupleKindInt) {
			result = backTranslateTuple(sarlType, expr);
		} else
			throw new SARLInternalException(
					"Encountered unexpected kind of CVC4 expression for SARL type "
							+ sarlType + ":\n" + expr);
		return result;
	}

	/**
	 * Prints the current CVC4 assumption(s) and the given cvcPredicate. Catches
	 * and ignores any exception, and just prints the exception to stderr.
	 * 
	 * @param cvcPredicate
	 *            the predicate that is being queried
	 */
	private void showQuery(SmtEngine smt, Expr cvcPredicate) {
		try {
			PrintStream out = universe.getOutputStream();
			int id = universe.numProverValidCalls() - 1;
			vectorExpr assertions;

			out.println();
			out.println("CVC4 assumptions " + id + ":");
			assertions = smt.getAssertions();
			for (int i = 0; i < assertions.size(); i++)
				out.println("  " + assertions.get(i));

			out.print("CVC4 predicate   " + id + ": ");
			out.println(cvcPredicate);
			out.flush();
		} catch (Exception e) {
			System.err
					.println("Warning: error in attempting to print CVC4 data.");
		}
	}

	/**
	 * Prints the given CVC4 query result. Catches and any exception and prints
	 * it to stderr, and otherwise ignores the exception.
	 * 
	 * @param result
	 *            a query result
	 */
	private void showResult(Result result) {
		try {
			PrintStream out = universe.getOutputStream();
			int id = universe.numProverValidCalls() - 1;

			out.println("CVC4 result      " + id + ": " + result);
			out.flush();
		} catch (Exception e) {
			System.err
					.println("Warning: error in attempting to print CVC4 result.");
		}
	}

	/**
	 * Pushes the CVC4 statement manager, translates the given SARL predicate,
	 * and invokes the CVC4 query method on the result.
	 * 
	 * @param predicate
	 *            a boolean-valued symbolic expression
	 * @return the result of running CVC4 query on the given predicate; tells
	 *         whether the predicate is valid (yes, no, or maybe)
	 */
	private Result queryCVC4(SmtEngine smt, BooleanExpression predicate) {
		Result result = null;
		boolean show = universe.getShowProverQueries();
		Expr cvcPredicate;

		universe.incrementProverValidCount();
		predicate = (BooleanExpression) universe.cleanBoundVariables(predicate);
		smt.push();
		cvcPredicate = translate(predicate);
		if (show)
			showQuery(smt, cvcPredicate);
		result = smt.query(cvcPredicate);
		if (show)
			showResult(result);
		return result;
	}

	/**
	 * translateResult takes a QueryResult and processes the equality between
	 * said QueryResult and the result types (valid, invalid, unknown, abort)
	 * and returns the SARL validity results.
	 * 
	 * @param result
	 * @return ValidityResult
	 */
	private ValidityResult translateResult(Result result) {
		if (result == null)
			return Prove.RESULT_MAYBE;
		else {
			Validity validity = result.isValid();

			if (validity.equals(Result.Validity.VALID)) {
				return Prove.RESULT_YES;
			} else if (validity.equals(Result.Validity.INVALID)) {
				return Prove.RESULT_NO;
			} else if (validity.equals(Result.Validity.VALIDITY_UNKNOWN)) {
				return Prove.RESULT_MAYBE;
			} else {
				System.err.println("Warning: Unknown CVC4 query result: "
						+ result);
				return Prove.RESULT_MAYBE;
			}
		}
	}

	// ******************** Methods from TheoremProver ******************** //

	@Override
	public PreUniverse universe() {
		return universe;
	}

	@Override
	public ValidityResult valid(BooleanExpression predicate) {
		try {
			Result result;
			ValidityResult validityResult;

			if (basicSmt == null)
				basicSmt = newBasicSmt(em);
			result = queryCVC4(basicSmt, predicate);
			validityResult = translateResult(result);
			basicSmt.pop();
			if (resetAfterQuery) {
				reset();
			}
			return validityResult;
		} catch (Exception e) {
			System.err.println("Warning: exception thrown by CVC4:\n" + e
					+ "\nThe SARL predicate queried was:\n  " + predicate);
			reset();
			return Prove.RESULT_MAYBE;
		}
	}

	@Override
	public ValidityResult validOrModel(BooleanExpression predicate) {
		try {
			Result cvcResult;
			ValidityResult result;

			if (modelSmt == null)
				modelSmt = newModelSmt(em);
			cvcResult = queryCVC4(modelSmt, predicate);
			if (cvcResult != null
					&& cvcResult.isValid().equals(Validity.INVALID)) {
				Map<SymbolicConstant, SymbolicExpression> model = new HashMap<SymbolicConstant, SymbolicExpression>();

				for (Entry<SymbolicConstant, Expr> entry : variableMap
						.entrySet()) {
					SymbolicConstant symbolicConstant = entry.getKey();
					SymbolicType sarlType = symbolicConstant.type();
					Expr cvcValue = modelSmt.getValue(entry.getValue());
					SymbolicExpression sarlExpression = backTranslate(sarlType,
							cvcValue);

					model.put(symbolicConstant, sarlExpression);
				}
				result = Prove.modelResult(model);
			} else {
				result = translateResult(cvcResult);
			}
			modelSmt.pop();
			if (resetAfterQuery) {
				reset();
			}
			return result;
		} catch (Exception e) {
			System.err.println("Warning: exception thrown by CVC4:\n" + e
					+ "\nThe SARL predicate queried was:\n  " + predicate);
			reset();
			return Prove.RESULT_MAYBE;
		}
	}

	// ************************ Methods from Object *********************** //

	@Override
	public String toString() {
		return "CVC4TheoremProver";
	}
}
