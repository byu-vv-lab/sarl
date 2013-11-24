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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.nyu.acsys.CVC4.Exception;
import edu.nyu.acsys.CVC4.Expr;
import edu.nyu.acsys.CVC4.ExprManager;
import edu.nyu.acsys.CVC4.Kind;
import edu.nyu.acsys.CVC4.Rational;
import edu.nyu.acsys.CVC4.Result;
import edu.nyu.acsys.CVC4.SmtEngine;
import edu.nyu.acsys.CVC4.Type;
import edu.nyu.acsys.CVC4.vectorExpr;
import edu.nyu.acsys.CVC4.vectorType;
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
 * through its JNI interface, and interprets the output.
 */
public class CVC4TheoremProver implements TheoremProver {

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
	// TODO not used locally?

	/**
	 * The printwriter used to print the queries and results. Initialized by
	 * constructor.
	 */
	private PrintStream out = System.out;
	// TODO not used locally?

	/**
	 * The CVC4 object used for creating CVC4 Exprs
	 */
	private ExprManager em = new ExprManager();

	/**
	 * The CVC4 object that checks queries
	 */
	private SmtEngine smt = new SmtEngine(em);

	/**
	 * Map from SARL expressions of funcional type to corresponding CVC4
	 * operators. In SARL, a function is a kind of symbolic expression. In CVC4,
	 * this concept is represented as an instance of "OpMut" (Operator Mutable),
	 * a subtype of "Op" (operator), which is not a subtype of Expr. Hence a
	 * separate map is needed.
	 */
	private Map<SymbolicExpression, Expr> functionMap = new HashMap<SymbolicExpression, Expr>();

	/**
	 * Mapping of CVC4 "Op"s to their corresponding symbolic constants. A CVC4
	 * "Op" is used to represent a function. In SARL, a function is represented
	 * by a symbolic constant of function type. This is used for finding models.
	 */
	private Map<Expr, SymbolicConstant> opMap = new HashMap<Expr, SymbolicConstant>();

	/**
	 * Mapping of CVC4 variables to their corresponding symbolic constants.
	 * Needed in order to construct model when there is a counter example.
	 */
	private Map<Expr, SymbolicConstant> varMap = new HashMap<Expr, SymbolicConstant>();

	/**
	 * Mapping of SARL symbolic type to corresponding CVC4 type. Set in method
	 * reset().
	 */
	private Map<SymbolicType, Type> typeMap = new HashMap<SymbolicType, Type>();

	/**
	 * Map from the root name (e.g., name of a symbolic constant) to the number
	 * of distinct CVC4 variables declared with that root. Since in CVC4 the
	 * names must be unique, the CVC4 name will be modified by appending the
	 * string "'n" where n is the value from this map, to all but the first
	 * instance of the root. I.e., the CVC4 names corresponding to "x" will be:
	 * x, x'1, x'2, ...
	 * 
	 * The names are for symbolic constants of all kinds, including functions.
	 */
	private Map<String, Integer> nameCountMap = new HashMap<String, Integer>();

	/**
	 * Mapping of SARL symbolic expression to corresponding CVC4 expresssion.
	 * Set in method reset().
	 */
	private Map<SymbolicExpression, Expr> expressionMap = new HashMap<SymbolicExpression, Expr>();

	/**
	 * Stack of SymbolicExpressions and their translations as Exprs
	 */
	private LinkedList<Map<SymbolicExpression, Expr>> translationStack = new LinkedList<Map<SymbolicExpression, Expr>>();

	/**
	 * The assumption under which this prover is operating.
	 */
	private BooleanExpression context;
	// TODO not used locally?

	/**
	 * The translation of the context to a CVC4 expression.
	 */
	private Expr cvcAssumption;

	CVC4TheoremProver(PreUniverse universe, BooleanExpression context) {
		assert universe != null;
		assert context != null;
		this.universe = universe;
		context = (BooleanExpression) universe.cleanBoundVariables(context);
		this.context = context;
		cvcAssumption = translate(context);
		smt.assertFormula(cvcAssumption);
	}

	/**
	 * Single element list used by processEquality and translateFunction
	 * 
	 * @param element
	 * @return the element as a list
	 */
	private <E> List<E> newSingletonList(E element) {
		List<E> result = new LinkedList<E>();

		result.add(element);
		return result;
	}

	/**
	 * Renaming function for keeping unique names between constants Example:
	 * new(x) = x; new(x) = x'1; etc.
	 * 
	 * @param root
	 * @return root's new name
	 */
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

	/**
	 * Creates a "default" CVC4 Expr with a given CVC4 Type
	 * 
	 * @param type
	 * @return the CVC4 Expr
	 */
	private Expr newAuxVariable(Type type) {
		return em.mkVar(newCvcName("_x"), type);
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
		String name = newCvcName("i");
		Expr result = em.mkBoundVar(name, type);

		return result;
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

	/**
	 * Like above, but takes SymbolicExpression as input.
	 * 
	 * @param expr
	 * @return true iff the type of expr is an incomplete array type
	 */
	private boolean isBigArray(SymbolicExpression expr) {
		return isBigArrayType(expr.type());
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
		List<Expr> list = new LinkedList<Expr>();

		list.add(length);
		list.add(value);
		return em.mkExpr(Kind.TUPLE, (vectorExpr) list);
	}

	/**
	 * This method takes any Expr that is of incomplete array type and returns
	 * the length.
	 * 
	 * @param bigArray
	 *            CVC4 Expr of bigArray
	 * @return length of CVC4 Expr of incomplete array type
	 */
	private Expr bigArrayLength(Expr bigArray) {
		Expr constant = em.mkConst(new Rational(0));
		return em.mkExpr(Kind.TUPLE_SELECT, bigArray, constant);
	}

	/**
	 * This methods takes any Expr that is of incomplete array type and returns
	 * the value.
	 * 
	 * @param bigArray
	 *            CVC4 Expr of bigArray
	 * @return value of CVC4 Expr of incomplete array type
	 */
	private Expr bigArrayValue(Expr bigArray) {
		Expr index = em.mkConst(new Rational(1));
		return em.mkExpr(Kind.TUPLE_SELECT, bigArray, index);
	}

	/**
	 * Formats the name of unionType during type translation.
	 * 
	 * @param unionType
	 * @param index
	 * @return the newly formatted name of the unionType
	 */
	private String selector(SymbolicUnionType unionType, int index) {
		return unionType.name().toString() + "_extract_" + index;
	}

	/**
	 * Formats the name of unionType during type translation.
	 * 
	 * @param unionType
	 * @param index
	 * @return the newly formatted name of the unionType
	 */
	private String constructor(SymbolicUnionType unionType, int index) {
		return unionType.name().toString() + "_inject_" + index;
	}

	/**
	 * This methods takes any SymbolicCollection and returns a linked list of
	 * Expr for cvc4.
	 * 
	 * @param collection
	 *            SymbolicCollection given to the translation.
	 * @return linkedlist of CVC4 Expr
	 */
	private List<Expr> translateCollection(SymbolicCollection<?> collection) {
		List<Expr> result = new LinkedList<Expr>();

		for (SymbolicExpression expr : collection)
			result.add(expr == null ? null : translate(expr));
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
				result = em
						.mkExpr(Kind.STORE, result,
								em.mkConst(new Rational(i)),
								translate(sequence.get(i)));
			break;
		}
		case BOOLEAN:
			result = ((BooleanObject) object).getBoolean() ? em.mkConst(true)
					: em.mkConst(false);
			break;
		case INTEGER:
		case REAL:
			result = em.mkConst(((NumberObject) object).getNumber().toString());
			break;
		case TUPLE:
			result = em
					.mkExpr(Kind.TUPLE,
							(vectorExpr) translateCollection((SymbolicSequence<?>) object));
			break;
		default:
			throw new SARLInternalException("Unknown concrete object: " + expr);
		}
		return result;
	}

	/**
	 * Translates a symbolic expression of functional type. In CVC4, functions
	 * have type Op; expressions have type Expr.
	 * 
	 * @param expr
	 * @return the function expression as a CVC4 Op
	 */
	private Expr translateFunction(SymbolicExpression expr) {
		Expr result = functionMap.get(expr);

		if (result != null)
			return result;
		switch (expr.operator()) {
		case SYMBOLIC_CONSTANT: {
			String name = newCvcName(((SymbolicConstant) expr).name()
					.getString());
			result = em.mkVar(name, this.translateType(expr.type()));
			opMap.put(result, (SymbolicConstant) expr);
			break;
		}
		case LAMBDA:
			result = em.mkExpr(
					Kind.LAMBDA,
					(Expr) newSingletonList(translateSymbolicConstant(
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
	 * Translates a multiplication SymbolicExpression (a*b) into an equivalent
	 * CVC4 multiplication Expr based upon number of arguments given.
	 * 
	 * @param expr
	 *            a SARL SymbolicExpression of form a*b
	 * @return CVC4 Expr
	 */
	private Expr translateMultiply(SymbolicExpression expr) {
		int numArgs = expr.numArguments();
		Expr result;

		if (numArgs == 1) {
			result = em.mkConst(new Rational(1));
			for (SymbolicExpression operand : (SymbolicCollection<?>) expr
					.argument(0))
				result = em.mkExpr(Kind.MULT, result, translate(operand));
		} else if (numArgs == 2)
			result = em.mkExpr(Kind.MULT,
					translate((SymbolicExpression) expr.argument(0)),
					translate((SymbolicExpression) expr.argument(1)));
		else
			throw new SARLInternalException(
					"Wrong number of arguments to multiply: " + expr);
		return result;
	}

	/**
	 * Translates a symbolic expression of functional type. In CVC4, functions
	 * have type Op; expressions have type Expr.
	 * 
	 * @param expr
	 * @return the function expression as a CVC4 Op
	 */

	/**
	 * Translates a SymbolicExpression of type (a || b) into an equivalent CVC4
	 * Expr
	 * 
	 * @param expr
	 * @return CVC4 representation of expr
	 */
	private Expr translateOr(SymbolicExpression expr) {
		int numArgs = expr.numArguments();
		Expr result;

		if (numArgs == 1)
			// TODO NEEDS TO BE ADDED FOR ONE ARG
			return null;
		else if (numArgs == 2)
			result = em.mkExpr(Kind.OR,
					translate((SymbolicExpression) expr.argument(0)),
					translate((SymbolicExpression) expr.argument(1)));
		else
			throw new SARLInternalException("Wrong number of arguments to or: "
					+ expr);
		return result;
	}

	/**
	 * Checks whether an index is in the bounds of an array SymbolicExpression
	 * by passing in the arguments to the validity checker
	 * 
	 * @param arrayExpression
	 * @param index
	 */
	private void assertIndexInBounds(SymbolicExpression arrayExpression,
			NumericExpression index) {
		NumericExpression length = universe.length(arrayExpression);
		BooleanExpression predicate = universe.lessThan(index, length);
		Expr cvcPredicate;

		predicate = universe.and(
				universe.lessThanEquals(universe.zeroInt(), index), predicate);
		cvcPredicate = translate(predicate);
		smt.assertFormula(cvcPredicate);
	}

	/**
	 * Translates an array-read expression a[i] into equivalent CVC4 expression
	 * 
	 * @param expr
	 *            a SARL symbolic expression of form a[i]
	 * @return an equivalent CVC4 expression
	 * @throws Exception
	 *             by CVC4
	 */
	private Expr translateArrayRead(SymbolicExpression expr) throws Exception {
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
		result = em.mkExpr(Kind.SELECT, array, index);
		return result;
	}

	/**
	 * Translates an array-write (or array update) SARL symbolic expression to
	 * equivalent CVC4 expression.
	 * 
	 * @param expr
	 *            an array update expression array[WITH i:=newValue].
	 * @return the equivalent CVC4 Expr
	 * @throws Exception
	 *             by CVC4
	 */
	private Expr translateArrayWrite(SymbolicExpression expr) throws Exception {
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
				em.mkExpr(Kind.STORE, bigArrayValue(array), index, value)) : em
				.mkExpr(Kind.STORE, array, index, value);
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
	 * @throws Exception
	 *             by CVC4
	 */
	private Expr translateDenseArrayWrite(SymbolicExpression expr)
			throws Exception {
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
				result = em.mkExpr(Kind.STORE, result,
						em.mkConst(new Rational(index)), value);
			index++;
		}
		assertIndexInBounds(arrayExpression, universe.integer(index - 1));
		if (isBig)
			result = bigArray(bigArrayLength(origin), result);
		return result;
	}

	/**
	 * Translate a multiple tuple-write (or tuple update) SARL symbolic
	 * expression to equivalent CVC4 expression.
	 * 
	 * @param expr
	 *            a tuple update expression
	 * @return the equivalent CVC4 Expr
	 * @throws Exception
	 *             by CVC4
	 */
	private Expr translateDenseTupleWrite(SymbolicExpression expr)
			throws Exception {
		SymbolicExpression tupleExpression = (SymbolicExpression) expr
				.argument(0);
		Expr result = translate(tupleExpression);
		List<Expr> values = translateCollection((SymbolicSequence<?>) expr
				.argument(1));
		int index = 0;
		Expr i;

		for (Expr value : values) {
			i = em.mkConst(new Rational(index));
			if (value != null)
				result = em.mkExpr(Kind.TUPLE_UPDATE, result, i, value);
			index++;
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
	 * @throws Exception
	 *             by CVC4
	 */
	private Expr translateQuantifier(SymbolicExpression expr)
			throws Exception {
		Expr variable = this.translateSymbolicConstant(
				(SymbolicConstant) (expr.argument(0)), true);
		Expr predicate = translate((SymbolicExpression) expr.argument(1));
		SymbolicOperator kind = expr.operator();

		if (kind == SymbolicOperator.FORALL) {
			return em.mkExpr(Kind.FORALL, variable, predicate);
		} else if (kind == SymbolicOperator.EXISTS) {
			return em.mkExpr(Kind.EXISTS, variable, predicate);
		} else { 
			throw new SARLInternalException(
					"Cannot translate quantifier into CVC4: " + expr);
		}
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
			result = em.mkExpr(Kind.EQUAL, extent1, extent2);
			index = newBoundVariable(em.integerType());
			indexRangeExpr = em.mkExpr(Kind.AND,
					em.mkExpr(Kind.GEQ, index, em.mkConst(new Rational(0))),
					em.mkExpr(Kind.LT, index, extent1));
			readExpr1 = em.mkExpr(Kind.SELECT, array1, index);
			readExpr2 = em.mkExpr(Kind.SELECT, array2, index);
			elementEqualsExpr = processEquality(arrayType1.elementType(),
					arrayType2.elementType(), readExpr1, readExpr2);
			forallExpr = em.mkExpr(Kind.FORALL, (Expr)newSingletonList(index),
					em.mkExpr(Kind.IMPLIES, indexRangeExpr, elementEqualsExpr));
			result = em.mkExpr(result, forallExpr);
			return result;
		} else {
			return em.mkExpr(Kind.EQUAL, cvcExpression1, cvcExpression2);
		}
	}

	/**
	 * Translates a SymbolicExpression that represents a == b into the CVC4
	 * equivalent Expr
	 * 
	 * @param expr
	 *            the equals type expression
	 * @return the equivalent CVC4 Expr
	 * @throws Exception
	 *             by CVC4
	 */
	private Expr translateEquality(SymbolicExpression expr) throws Exception {
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
		SymbolicExpression arg = (SymbolicExpression) expr.argument(1);
		SymbolicUnionType unionType = (SymbolicUnionType) arg.type();
		int index = ((IntObject) expr.argument(0)).getInt();
		String selector = selector(unionType, index);
		Expr selectorExpr = em.mkConst(selector);
		Expr result = em.mkExpr(Kind.APPLY_SELECTOR, selectorExpr,
				translate(arg));
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
		String constructor = constructor(unionType, index);
		Expr constructExpr = em.mkConst(constructor);
		Expr result;
		result = em.mkExpr(Kind.APPLY_CONSTRUCTOR, constructExpr, translate(arg));
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
		SymbolicUnionType unionType = (SymbolicUnionType) arg.type();
		String constructor = constructor(unionType, index);
		Expr constructorExpr = em.mkConst(constructor);
		Expr result = em.mkExpr(Kind.APPLY_TESTER, constructorExpr,
				translate(arg));
		return result;
	}
	
	/**
	 * Pops the CVC4 stack. This means all the assertions made between the last
	 * push and now will go away.
	 */
	private void popCVC4() {
		try {
			smt.pop();
		} catch (Exception e) {
			throw new SARLInternalException("CVC4 error: " + e);
		}
		translationStack.removeLast();
	}

	/**
	 * Translates which operation to perform based upon the given
	 * SymbolicExpression and the SymbolicOperator provided. Depending upon the
	 * number of arguments given, a different conditional will be executed. The
	 * result will be a CVC4 Expr.
	 * 
	 * @param expr
	 *            a SymbolicExpression
	 * @return
	 */
	private Expr translateWork(SymbolicExpression expr) {
		int numArgs = expr.numArguments();
		Expr result;

		// TODO NEED TO WORK ON
		switch (expr.operator()) {
		case ADD:
			if (numArgs == 2)
				result = em.mkExpr(Kind.PLUS,
						translate((SymbolicExpression) expr.argument(0)),
						translate((SymbolicExpression) expr.argument(1)));
			else if (numArgs == 1)
				result = em.mkExpr(Kind.PLUS,
						translate((SymbolicExpression) expr.argument(0)));
			else {
				result = null;
				throw new SARLInternalException(
						"Expected 1 or 2 arguments for ADD");
			}

			break;
		case AND:
			if (numArgs == 2)
				result = em.mkExpr(Kind.AND,
						translate((SymbolicExpression) expr.argument(0)),
						translate((SymbolicExpression) expr.argument(1)));
			else if (numArgs == 1)
				// TODO: this is wrong: if AND has one argument, it is a
				// SymbolicSequence. See documentation for SymbolicExpression.
				result = em.mkExpr(Kind.AND,
						translate((SymbolicExpression) expr.argument(0)));
			else {
				result = null;
				throw new SARLInternalException(
						"Expected 1 or 2 arguments for AND: " + expr);
			}
			break;
		case APPLY:
			result = em
					.mkExpr(Kind.FUNCTION,
							translateFunction((SymbolicExpression) expr
									.argument(0)),
							(vectorExpr) translateCollection((SymbolicCollection<?>) expr
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
			result = em.mkExpr(Kind.SELECT, var, body);
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
			result = em.mkExpr(Kind.ITE,
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
			result = em.mkExpr(Kind.DIVISION,
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
			result = em.mkExpr(Kind.INTS_DIVISION,
					translate((SymbolicExpression) expr.argument(0)),
					translate((SymbolicExpression) expr.argument(1)));
			break;
		case LENGTH:
			result = bigArrayLength(translate((SymbolicExpression) expr
					.argument(0)));
			break;
		case LESS_THAN:
			result = em.mkExpr(Kind.LT,
					translate((SymbolicExpression) expr.argument(0)),
					translate((SymbolicExpression) expr.argument(1)));
			break;
		case LESS_THAN_EQUALS:
			result = em.mkExpr(Kind.LEQ,
					translate((SymbolicExpression) expr.argument(0)),
					translate((SymbolicExpression) expr.argument(1)));
			break;
		case MODULO:
			result = em.mkExpr(Kind.INTS_MODULUS,
					translate((SymbolicExpression) expr.argument(0)),
					translate((SymbolicExpression) expr.argument(1)));
			break;
		case MULTIPLY:
			result = translateMultiply(expr);
			break;
		case NEGATIVE:
			result = em.mkExpr(Kind.UMINUS,
					translate((SymbolicExpression) expr.argument(0)));
			break;
		case NEQ:
			result = em.mkExpr(Kind.DISTINCT,
					translate((SymbolicExpression) expr.argument(0)));
			break;
		case NOT:
			result = em.mkExpr(Kind.NOT,
					translate((SymbolicExpression) expr.argument(0)));
			break;
		case OR:
			result = translateOr(expr);
			break;
		case POWER: {
			SymbolicObject exponent = expr.argument(1);

			if (exponent instanceof IntObject)
				result = em.mkExpr(Kind.POW,
						translate((SymbolicExpression) expr.argument(0)), em
								.mkConst(new Rational(((IntObject) exponent)
										.getInt())));
			else
				result = em.mkExpr(Kind.POW,
						translate((SymbolicExpression) expr.argument(0)),
						translate((SymbolicExpression) exponent));
			break;
		}
		case SUBTRACT:
			result = em.mkExpr(Kind.MINUS,
					translate((SymbolicExpression) expr.argument(0)),
					translate((SymbolicExpression) expr.argument(1)));
			break;
		case SYMBOLIC_CONSTANT:
			result = translateSymbolicConstant((SymbolicConstant) expr, false);
			break;
		case TUPLE_READ:
			result = em.mkExpr(Kind.TUPLE_SELECT,
					translate((SymbolicExpression) expr.argument(0)), em
							.mkConst(new Rational(
									((IntObject) expr.argument(1)).getInt())));
			break;
		case TUPLE_WRITE:
			result = em.mkExpr(Kind.TUPLE_UPDATE,
					translate((SymbolicExpression) expr.argument(0)), em
							.mkConst(new Rational(
									((IntObject) expr.argument(1)).getInt())),
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
			boolean isBoundVariable) throws Exception {
		Type type = translateType(symbolicConstant.type());
		String root = symbolicConstant.name().getString();
		Expr result;

		if (isBoundVariable) {
			result = em.mkBoundVar(root, type); 
			translationStack.getLast().put(symbolicConstant, result);
			this.expressionMap.put(symbolicConstant, result);
		} else {
			result = em.mkVar(newCvcName(root), type); 
		}
		varMap.put(result, symbolicConstant);
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
		if (showProverQueries) {
			out.println("CVC4 result      " + universe.numValidCalls() + ": "
					+ result);
			out.flush();
		}
		// unfortunately QueryResult is not an enum...
		if (result.equals(Result.Validity.VALID)) {
			return Prove.RESULT_YES;
		} else if (result.equals(Result.Validity.INVALID)) {
			return Prove.RESULT_NO;
		} else if (result.equals(Result.Validity.VALIDITY_UNKNOWN)) {
			return Prove.RESULT_MAYBE;
		} else {
			out.println("Warning: Unknown CVC4 query result: " + result);
			return Prove.RESULT_MAYBE;
		}
	}

	/**
	 * Translates the symbolic type to a CVC4 type.
	 * 
	 * @param type
	 *            a SARL symbolic expression type
	 * @return the equivalent CVC4 type
	 * @throws Exception
	 *             if CVC4 throws an exception
	 */
	public Type translateType(SymbolicType type) throws Exception {
		Type result = typeMap.get(type);

		if (result != null)
			return result;

		SymbolicTypeKind kind = type.typeKind();

		switch (kind) {

		case BOOLEAN:
			result = em.booleanType();
			break;
		case INTEGER:
			result = em.integerType();
			break;
		case REAL:
			result = em.realType();
			break;
		case ARRAY:
			result = em.mkArrayType(em.integerType(),
					translateType(((SymbolicArrayType) type).elementType()));
			if (!(type instanceof SymbolicCompleteArrayType))
				// tuple:<extent,array>
				result = em.mkTupleType((vectorType) Arrays.asList(
						em.integerType(), result));
			break;
		case TUPLE:
			result = em
					.mkTupleType((vectorType) translateTypeSequence(((SymbolicTupleType) type)
							.sequence()));
			break;
		case FUNCTION:
			result = em
					.mkFunctionType(
							(vectorType) translateTypeSequence(((SymbolicFunctionType) type)
									.inputTypes()),
							translateType(((SymbolicFunctionType) type)
									.outputType()));
			break;
		// TODO cvc4 union type?
		// case UNION: {
		// SymbolicUnionType unionType = (SymbolicUnionType) type;
		// List<String> constructors = new LinkedList<String>();
		// List<List<String>> selectors = new LinkedList<List<String>>();
		// List<List<Expr>> types = new LinkedList<List<Expr>>();
		// SymbolicTypeSequence sequence = unionType.sequence();
		// int index = 0;
		//
		// for (SymbolicType t : sequence) {
		// List<String> selectorList = new LinkedList<String>();
		// List<Expr> typeList = new LinkedList<Expr>();
		//
		// selectorList.add(selector(unionType, index));
		// typeList.add(translateType(t).getExpr());
		// selectors.add(selectorList);
		// types.add(typeList);
		// constructors.add(constructor(unionType, index));
		// index++;
		// }
		// result = vc.dataType(unionType.name().getString(), constructors,
		// selectors, types);
		// break;
		// }
		default:
			throw new RuntimeException("Unknown type: " + type);
		}
		typeMap.put(type, result);
		return result;
	}
	
	/**
	 * Translate expr from SARL to CVC4. This results in a CVC4
	 * expression (which is returned).
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
	 * the CVC4 assumption set, and these constraints need to be added to the
	 * current assumption set.
	 * 
	 * @param expr
	 *            any SARL expression
	 * @return the CVC4 expression resulting from translation
	 */
	
	Expr translate(SymbolicExpression expr) {
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
			translationStack.getLast().put(expr, result);
			return result;
		}
		result = translateWork(expr);
		translationStack.getLast().put(expr, result);
		this.expressionMap.put(expr, result);
		return result;
	}

//	/**
//	 * Translate expr from SARL to CVC4. This results in two things: a CVC4
//	 * expression (which is returned) and also side-effects: constraints added
//	 * to the CVC4 assumption set, possibly involving auxiliary variables.
//	 * 
//	 * @param SymbolicExpression
//	 * @returns Expr
//	 */
//	public Expr translate(SymbolicExpression expr) {
//		Expr result;
//		result = translateWork(expr);
//		return result;
//	}

	/**
	 * Translate a given SymbolicTypeSequence to an equivalent linkedlist of
	 * Types in CVC4.
	 * 
	 * @param sequence
	 *            SymbolicTypeSequence given to the translation.
	 * @return linkedlist of CVC4 types.
	 */
	private List<Type> translateTypeSequence(SymbolicTypeSequence sequence) {
		List<Type> result = new LinkedList<Type>();

		for (SymbolicType t : sequence)
			result.add(t == null ? null : translateType(t));
		return result;
	}

	@Override
	public PreUniverse universe() {
		return universe;
	}
	
	/**
	 * Returns the queries and results
	 * 
	 * @return PrintSteam
	 */

	public PrintStream out() {
		return out;
	}

	/**
	 * Outputs the boolean value of showProverQueries
	 * 
	 * @return boolean value, true if out in setOutput is not equal to null
	 */

	public boolean showProverQueries() {
		return showProverQueries;
	}

	/**
	 * Takes a BooleanExpression and passes it through translate. The translated
	 * predicate is then queried via smt and pops the smt.
	 * 
	 * @param expr
	 * @return ValidityResult from using translateResult
	 */
	public ValidityResult valid(BooleanExpression symbolicPredicate) {
		Expr cvc4Predicate = translate(symbolicPredicate);
	    Result result = smt.query(cvc4Predicate);
		
	    popCVC4();
		return translateResult(result);
	}

	@Override
	public ValidityResult validOrModel(BooleanExpression predicate) {
		// TODO
		popCVC4();
		return null;
	}

	/**
	 * opMap gets called from CVC4ModelFinder. Returns the opMap that maps
	 * operations and their symbolic constants.
	 * 
	 * @return Map of operations and symbolic constants
	 */

	public Map<Expr, SymbolicConstant> opMap() {
		return opMap;
	}

	@Override
	public void setOutput(PrintStream out) {
		this.out = out;
		showProverQueries = out != null;
	}

	@Override
	public String toString() {
		return "CVC4TheoremProver";
	}
}
