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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import edu.nyu.acsys.CVC4.ArrayType;
import edu.nyu.acsys.CVC4.Datatype;
import edu.nyu.acsys.CVC4.DatatypeConstructor;
import edu.nyu.acsys.CVC4.DatatypeType;
import edu.nyu.acsys.CVC4.Expr;
import edu.nyu.acsys.CVC4.ExprManager;
import edu.nyu.acsys.CVC4.Kind;
import edu.nyu.acsys.CVC4.Rational;
import edu.nyu.acsys.CVC4.Result;
import edu.nyu.acsys.CVC4.Result.Validity;
import edu.nyu.acsys.CVC4.SExpr;
import edu.nyu.acsys.CVC4.SmtEngine;
import edu.nyu.acsys.CVC4.TupleType;
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

	public static boolean debug = false;

	// ****************************** Fields ****************************** //

	/**
	 * The symbolic universe used for managing symbolic expressions. Initialized
	 * by constructor and never changes.
	 */
	private PreUniverse universe;

	/**
	 * The CVC4 object used for creating CVC4 {@link Expr}s (expressions).
	 * Created once during instantiation and never modified.
	 */
	private ExprManager em = new ExprManager();

	/**
	 * The CVC4 object used to checks queries. Created once during instantiation
	 * and never modified.
	 */
	private SmtEngine smt = new SmtEngine(em);

	/**
	 * The number of auxiliary CVC4 variables created. These are the variables
	 * that do not correspond to any SARL variable but are needed for some
	 * reason to translate an expression. Includes both ordinary and bound CVC4
	 * variables.
	 */
	private int auxVarCount = 0;

	/**
	 * Mapping of SARL symbolic expression to corresponding CVC4 expresssion.
	 * Used to cache the results of translation.
	 */
	private Map<SymbolicExpression, Expr> expressionMap = new HashMap<>();

	/**
	 * Map from SARL symbolic constants to corresponding CVC4 variables. Entries
	 * are a subset of those of {@link #expressionMap}.
	 */
	private Map<SymbolicConstant, Expr> variableMap = new HashMap<>();

	// /**
	// * Map from CVC variables back to SARL symbolic constants. This is used
	// for
	// * finding models. When CVC finds a model, it needs to be translated back
	// to
	// * a SARL model. This map is basically the inverse of {@link
	// #variableMap}.
	// */
	// private Map<Expr, SymbolicConstant> inverseMap = new HashMap<>();

	/**
	 * Mapping of SARL symbolic type to corresponding CVC4 type. Used to cache
	 * results of translation.
	 */
	private Map<SymbolicType, Type> typeMap = new HashMap<SymbolicType, Type>();

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
	 * The CVC4 integer constant 1.
	 */
	private Expr cvc1;

	/**
	 * The CVC4 boolean constant "true".
	 */
	private Expr cvcTrue;

	/**
	 * The CVC4 boolean constant "false".
	 */
	private Expr cvcFalse;

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
		// the following is necessary if you are going to make
		// multiple verify calls with the same SmtEngine
		// (this may change in the future)...
		smt.setOption("incremental", new SExpr(true));
		// allows the CVC4 to find models (counterexamples)...
		smt.setOption("produce-models", new SExpr(true));
		// this is necessary to use method getAssertions:
		smt.setOption("interactive-mode", new SExpr(true));
		smt.setOption("rewrite-divk", new SExpr(true));
		cvc0 = em.mkConst(new Rational(0));
		cvc1 = em.mkConst(new Rational(1));
		cvcTrue = em.mkConst(true);
		cvcFalse = em.mkConst(false);
		// The following is necessary since the same bound symbolic constant
		// can be used in different scopes in the context; CVC4 requires
		// that these map to distinct variables.
		context = (BooleanExpression) universe.cleanBoundVariables(context);
		if (!context.isTrue()) {
			cvcAssumption = translate(context);

			System.out.println("cvcAssumption: " + cvcAssumption);

			smt.assertFormula(cvcAssumption);
		}
	}

	// ************************* Private Methods ************************** //

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

	// /**
	// * Symbolic expressions of incomplete array type are represented by
	// ordered
	// * pairs (length, array). This method tells whether the given symbolic
	// * expression type requires such a representation.
	// *
	// * @param type
	// * any symbolic type
	// * @return true iff the type is an incomplete array type
	// */
	// private boolean isBigArrayType(SymbolicType type) {
	// return type instanceof SymbolicArrayType
	// && !((SymbolicArrayType) type).isComplete();
	// }
	//
	// /**
	// * Like above, but takes SymbolicExpression as input.
	// *
	// * @param expr
	// * @return true iff the type of expr is an incomplete array type
	// */
	// private boolean isBigArray(SymbolicExpression expr) {
	// return isBigArrayType(expr.type());
	// }

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
		vectorExpr list = new vectorExpr();

		list.add(length);
		list.add(value);
		return em.mkExpr(Kind.TUPLE, list);
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
		// check this
		if (bigArray.getKind().equals(Kind.TUPLE)) {
			return bigArray.getChild(0);
		}
		return em.mkExpr(Kind.TUPLE_SELECT, bigArray, cvc0);
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
		// if this is already a pair, get the 1 component, else do this
		if (bigArray.getKind().equals(Kind.TUPLE)) {
			return bigArray.getChild(1);
		}
		return em.mkExpr(Kind.TUPLE_SELECT, bigArray, cvc1);
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
			NumericExpression extentExpression = ((SymbolicCompleteArrayType) type)
					.extent();
			IntegerNumber extentNumber = (IntegerNumber) universe
					.extractNumber(extentExpression);
			SymbolicSequence<?> sequence = (SymbolicSequence<?>) object;
			int size = sequence.size();
			ArrayType cvcType = new ArrayType(translateType(type));

			assert extentNumber != null && extentNumber.intValue() == size;
			result = newAuxVar(cvcType);
			for (int i = 0; i < size; i++)
				result = em
						.mkExpr(Kind.STORE, result,
								em.mkConst(new Rational(i)),
								translate(sequence.get(i)));
			result = bigArray(em.mkConst(new Rational(size)), result);
			break;
		}
		case BOOLEAN:
			result = ((BooleanObject) object).getBoolean() ? cvcTrue : cvcFalse;
			break;
		case CHAR:
			result = em.mkConst(new Rational(((CharObject) object).getChar()));
			break;
		case INTEGER: {
			IntegerNumber integerNumber = (IntegerNumber) ((NumberObject) object)
					.getNumber();

			// yes, toString is the only reliable and efficient way
			// to do this...
			result = em.mkConst(new Rational(integerNumber.toString()));
			break;
		}
		case REAL: {
			RationalNumber rationalNumber = (RationalNumber) ((NumberObject) object)
					.getNumber();
			BigInteger numerator = rationalNumber.numerator(), denominator = rationalNumber
					.denominator();
			Rational rationalNumerator = new Rational(numerator.toString()), rationalDenominator = new Rational(
					denominator.toString());

			result = em.mkConst(rationalNumerator
					.dividedBy(rationalDenominator));
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
			if (symbolicType.typeKind() == SymbolicTypeKind.ARRAY) {
				Type cvcArrayType = (new TupleType(type)).getTypes().get(1);
				Expr constant = em.mkVar(name, cvcArrayType);
				NumericExpression length = universe.length(symbolicConstant);
				Expr cvcLength = translate(length);

				result = em.mkExpr(Kind.TUPLE, cvcLength, constant);
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
		// SymbolicArrayType arrayType = (SymbolicArrayType) arrayExpression
		// .type();
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
				Expr cvcIndex = em.mkConst(new Rational(i));
				Expr cvcValue = translate(value);

				result = em.mkExpr(Kind.STORE, result, cvcIndex, cvcValue);
			}
		}
		// why?:
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
				Expr cvcIndex = em.mkConst(new Rational(index));
				Expr cvcValue = translate(value);

				result = em.mkExpr(Kind.TUPLE_UPDATE, result, cvcIndex,
						cvcValue);
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
			result = em.mkExpr(Kind.DISTINCT,
					translate((SymbolicExpression) expression.argument(0)));
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
				result = em
						.mkExpr(Kind.POW,
								translate((SymbolicExpression) expression
										.argument(0)), em.mkConst(new Rational(
										((IntObject) exponent).getInt())));
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
			result = em.mkExpr(Kind.TUPLE_SELECT,
					translate((SymbolicExpression) expression.argument(0)), em
							.mkConst(new Rational(((IntObject) expression
									.argument(1)).getInt())));
			break;
		case TUPLE_WRITE:
			result = em.mkExpr(Kind.TUPLE_UPDATE,
					translate((SymbolicExpression) expression.argument(0)), em
							.mkConst(new Rational(((IntObject) expression
									.argument(1)).getInt())),
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
		case ARRAY:
			result = em.mkArrayType(em.integerType(),
					translateType(((SymbolicArrayType) type).elementType()));
			if (!(type instanceof SymbolicCompleteArrayType)) { // tuple:<extent,array>
				vectorType vector = new vectorType();

				vector.add(em.integerType());
				vector.add(result);
				result = em.mkTupleType(vector);
			}
			break;
		case TUPLE:
			result = em
					.mkTupleType(translateTypeSequence(((SymbolicTupleType) type)
							.sequence()));
			break;
		case FUNCTION:
			result = em.mkFunctionType(
					translateTypeSequence(((SymbolicFunctionType) type)
							.inputTypes()),
					translateType(((SymbolicFunctionType) type).outputType()));
			break;
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

	private SymbolicExpression backTranslate(Expr cvcExpression) {
		throw new SARLException("Model finding not yet supported for CVC4");
	}

	private void showQuery(Expr cvcPredicate) {
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
	 * queryCVC4 gets called by valid, prints out predicate and context, and the
	 * CVC4 assumptions and CVC4 predicate. Passes the symbolicPredicate through
	 * translate and uses the cvcPredicate for the validitychecker.
	 * 
	 * @param symbolicPredicate
	 * @return Result
	 */
	private Result queryCVC4(BooleanExpression symbolicPredicate) {
		Result result = null;
		boolean show = universe.getShowProverQueries();

		universe.incrementProverValidCount();
		symbolicPredicate = (BooleanExpression) universe
				.cleanBoundVariables(symbolicPredicate);
		try {
			Expr cvcPredicate;

			this.smt.push();
			cvcPredicate = translate(symbolicPredicate);
			if (show)
				showQuery(cvcPredicate);
			result = smt.query(cvcPredicate);
			if (show)
				showResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SARLInternalException("Error in querying CVC4:\n" + e
					+ "\nThe SARL predicate was:\n  " + symbolicPredicate);
		}
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
		Validity validity = result.isValid();

		if (validity.equals(Result.Validity.VALID)) {
			return Prove.RESULT_YES;
		} else if (validity.equals(Result.Validity.INVALID)) {
			return Prove.RESULT_NO;
		} else if (validity.equals(Result.Validity.VALIDITY_UNKNOWN)) {
			return Prove.RESULT_MAYBE;
		} else {
			System.err.println("Warning: Unknown CVC4 query result: " + result);
			return Prove.RESULT_MAYBE;
		}
	}

	// ******************** Methods from TheoremProver ******************** //

	@Override
	public PreUniverse universe() {
		return universe;
	}

	@Override
	public ValidityResult valid(BooleanExpression predicate) {
		Result result = queryCVC4(predicate);

		smt.pop();
		return translateResult(result);
	}

	@Override
	public ValidityResult validOrModel(BooleanExpression predicate) {
		Result cvcResult = queryCVC4(predicate);

		if (cvcResult.equals(Validity.INVALID)) {
			Map<SymbolicConstant, SymbolicExpression> model = new HashMap<SymbolicConstant, SymbolicExpression>();

			for (Entry<SymbolicConstant, Expr> entry : variableMap.entrySet()) {
				Expr cvcValue = smt.getValue(entry.getValue());
				SymbolicExpression sarlExpression = backTranslate(cvcValue);

				model.put(entry.getKey(), sarlExpression);
			}
			popCVC4();
			return Prove.modelResult(model);
		}
		popCVC4();
		return translateResult(cvcResult);
	}

	// ************************ Methods from Object *********************** //

	@Override
	public String toString() {
		return "CVC4TheoremProver";
	}
}
