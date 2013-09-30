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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import cvc3.Cvc3Exception;
import cvc3.Expr;
import cvc3.Op;
import cvc3.Rational;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.type.common.CommonSymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.util.Util;

public class CVC3ModelFinder {

	private PreUniverse universe;

	private ValidityChecker vc;

	private PrintStream out;

	private static Iterable<SymbolicExpression> empty = Util.emptyIterable();

	private CVC3TheoremProver prover;

	/**
	 * Mapping of CVC3 variables to their corresponding symbolic constants.
	 * Needed in order to construct model when there is a counter example. Set
	 * in method reset();
	 */
	private Map<Expr, SymbolicConstant> varMap;

	private Map<Op, SymbolicConstant> opMap;

	private Map<?, ?> cvcModel;

	Map<SymbolicConstant, Object> preModel;

	Map<SymbolicConstant, SymbolicExpression> model;

	public CVC3ModelFinder(CVC3TheoremProver prover, Map<?, ?> cvcModel) {
		this.prover = prover;
		this.universe = prover.universe();
		this.vc = prover.validityChecker();
		this.varMap = prover.varMap();
		this.opMap = prover.opMap();
		this.out = prover.out();
		this.cvcModel = cvcModel;
		this.preModel = new HashMap<SymbolicConstant, Object>();
		this.model = new HashMap<SymbolicConstant, SymbolicExpression>();
		computeModel();
	}

	private void printOp(Op op) {
		if (op == null || op.isNull()) {
			out.print("Op[]");
		} else {
			Expr expr = op.getExpr();

			out.print("Op[" + op);
			out.flush();
			out.print(", kind = " + expr.getKind());
			out.flush();
			if (expr.arity() > 0) {
				boolean first = true;

				out.print(": ");
				for (Object o : expr.getChildren()) {
					Expr arg = (Expr) o;

					if (first)
						first = false;
					else
						out.print(", ");
					printExpr(arg, out);
				}
			}
			out.print("]");
		}
	}

	private void printExpr(Expr expr, PrintStream out) {
		boolean first = true;
		String kind = expr.getKind();

		out.print("Expr[" + expr + ", type=" + expr.getType() + ", kind="
				+ kind);
		if (kind.equals("_APPLY")) {
			Op op = expr.getOp();

			out.print(", op=");
			printOp(op);
		}
		out.flush();
		if (expr.arity() > 0) {
			out.print(": ");
			for (Object o : expr.getChildren()) {
				Expr arg = (Expr) o;

				if (first)
					first = false;
				else
					out.print(", ");
				printExpr(arg, out);
			}
		}
		out.print("]");
	}

	// TODO: find some way to share code with CommonSimpifier
	// rather than copying the code.

	private Iterable<? extends SymbolicType> simplifyTypeSequenceWork(
			SymbolicTypeSequence sequence) {
		int size = sequence.numTypes();

		for (int i = 0; i < size; i++) {
			SymbolicType type = sequence.getType(i);
			SymbolicType simplifiedType = simplifyType(type);

			if (type != simplifiedType) {
				SymbolicType[] newTypes = new SymbolicType[size];

				for (int j = 0; j < i; j++)
					newTypes[j] = sequence.getType(j);
				newTypes[i] = simplifiedType;
				for (int j = i + 1; j < size; j++)
					newTypes[j] = simplifyType(sequence.getType(j));
				return Arrays.asList(newTypes);
			}
		}
		return sequence;
	}

	private SymbolicTypeSequence simplifyTypeSequence(
			SymbolicTypeSequence sequence) {
		return new CommonSymbolicTypeSequence(
				simplifyTypeSequenceWork(sequence));
	}

	private SymbolicType simplifyType(SymbolicType type) {
		SymbolicTypeKind kind = type.typeKind();

		switch (kind) {
		case INTEGER:
		case REAL:
		case BOOLEAN:
			return type;
		case ARRAY: {
			SymbolicType oldElementType = ((SymbolicArrayType) type)
					.elementType();
			SymbolicType newElementType = simplifyType(oldElementType);

			if (type instanceof SymbolicCompleteArrayType) {
				NumericExpression oldExtent = ((SymbolicCompleteArrayType) type)
						.extent();

				if (universe.extractNumber(oldExtent) != null)
					return oldElementType == newElementType ? type : universe
							.arrayType(newElementType, oldExtent);
				else {
					NumericExpression newExtent = (NumericExpression) backTranslate(
							vc.simplify(prover.translate(oldExtent)),
							oldExtent.type());

					return universe.arrayType(newElementType, newExtent);
				}
			}
			return oldElementType == newElementType ? type : universe
					.arrayType(newElementType);
		}
		case FUNCTION: {
			SymbolicFunctionType functionType = (SymbolicFunctionType) type;
			SymbolicTypeSequence inputs = functionType.inputTypes();
			SymbolicTypeSequence simplifiedInputs = simplifyTypeSequence(inputs);
			SymbolicType output = functionType.outputType();
			SymbolicType simplifiedOutput = simplifyType(output);

			if (inputs != simplifiedInputs || output != simplifiedOutput)
				return universe
						.functionType(simplifiedInputs, simplifiedOutput);
			return type;
		}
		case TUPLE: {
			SymbolicTypeSequence sequence = ((SymbolicTupleType) type)
					.sequence();
			SymbolicTypeSequence simplifiedSequence = simplifyTypeSequence(sequence);

			if (simplifiedSequence != sequence)
				return universe.tupleType(((SymbolicTupleType) type).name(),
						simplifiedSequence);
			return type;
		}
		case UNION: {
			SymbolicTypeSequence sequence = ((SymbolicUnionType) type)
					.sequence();
			SymbolicTypeSequence simplifiedSequence = simplifyTypeSequence(sequence);

			if (simplifiedSequence != sequence)
				return universe.unionType(((SymbolicUnionType) type).name(),
						simplifiedSequence);
			return type;
		}
		default:
			throw new SARLInternalException("unreachable");
		}
	}

	private SymbolicType typeOf(Expr expr) {
		if (expr.isVar()) {
			SymbolicConstant x = varMap.get(expr);

			if (x == null)
				throw new SARLInternalException("Unknown CVC3 variable: " + x);
			return x.type();
		}
		if (expr.isRational()) {
			if (expr.getRational().isInteger())
				return universe.integerType();
			return universe.realType();
		}
		if (expr.getType().isBoolean())
			return universe.booleanType();
		if (expr.isRead())
			return ((SymbolicArrayType) typeOf(expr.getChild(0))).elementType();
		if (expr.isApply()) {
			Op op = expr.getOp();
			Expr opExpr = op.getExpr();
			String opKind = opExpr.getKind();

			if (opKind.equals("_TUPLE_SELECT")) {
				int index = opExpr.getChild(0).getRational().getInteger();
				Expr argExpr = expr.getChild(0);
				SymbolicType argType = typeOf(argExpr);

				if (argType instanceof SymbolicArrayType) {
					if (index == 1)
						return argType;
					else if (index == 0)
						return universe.integerType();
					throw new SARLInternalException(
							"Expected index 0 or 1, saw " + index + " in "
									+ expr);
				} else if (argType instanceof SymbolicTupleType)
					return ((SymbolicTupleType) argType).sequence().getType(
							index);
				throw new SARLInternalException(
						"Expected array or tuple type.  Saw " + argType
								+ " in \n" + expr);
			} else {
				SymbolicConstant f = opMap.get(op);

				if (f != null)
					return ((SymbolicFunctionType) f.type()).outputType();
			}
			throw new SARLInternalException(
					"Unknown type of APPLY expression: " + expr);
		}
		throw new SARLInternalException(
				"Unable to compute type of CVC3 expression: " + expr);
	}

	private NumericExpression backTranslateRational(Expr expr, SymbolicType type) {
		Rational rational = expr.getRational();
		NumberFactory numberFactory = universe.numberFactory();
		NumericExpression result;

		if (type.isInteger()) {
			try {
				result = universe.integer(rational.getInteger());
			} catch (Cvc3Exception e) {
				result = universe.number(numberFactory.integer(rational
						.toString()));
			}
			return result;
		} else {
			try {
				result = universe.rational(
						rational.getNumerator().getInteger(), rational
								.getDenominator().getInteger());
			} catch (Cvc3Exception e) {
				String numeratorString = rational.getNumerator().toString();
				String denominatorString = rational.getDenominator().toString();
				RationalNumber number = numberFactory.rational(new BigInteger(
						numeratorString), new BigInteger(denominatorString));

				result = universe.number(number);
			}
			return result;
		}
	}

	private BooleanExpression backTranslateBoolean(Expr expr) {
		if (expr.isTrue())
			return universe.trueExpression();
		else if (expr.isFalse())
			return universe.falseExpression();
		throw new SARLInternalException("Expected: a CVC boolean constant\n"
				+ "Saw: " + expr);
	}

	private SymbolicExpression backTranslateArrayLiteral(Expr expr, int length,
			SymbolicType elementType) {
		LinkedList<SymbolicExpression> elements = new LinkedList<SymbolicExpression>();

		for (int i = 0; i < length; i++) {
			Expr indexExpr = vc.ratExpr(i);
			Expr elementExpr = vc.readExpr(expr, indexExpr);
			SymbolicExpression element;

			elementExpr = vc.simplify(elementExpr);
			element = backTranslate(elementExpr, elementType);
			if (element == null)
				// element = defaultValue(elementType);
				throw new SARLInternalException("Unable to back translate: "
						+ elementExpr);
			elements.add(element);
		}
		return universe.array(elementType, elements);
	}

	private SymbolicExpression backTranslateArrayLiteral(Expr expr,
			SymbolicType type) {
		if (type instanceof SymbolicCompleteArrayType) {
			SymbolicCompleteArrayType arrayType = (SymbolicCompleteArrayType) type;
			NumericExpression extent = arrayType.extent();
			IntegerNumber extentNumber = (IntegerNumber) universe
					.extractNumber(extent);
			int length;

			// TODO need to get concrete value for extent...

			if (extentNumber == null)
				throw new SARLInternalException(
						"Array type extent not concrete:\n" + "Expr: " + expr
								+ "\nType: " + type);
			length = extentNumber.intValue();
			return backTranslateArrayLiteral(expr, length,
					arrayType.elementType());
		}
		throw new SARLInternalException(
				"Unable to back translate array expression:\n" + "Expr: "
						+ expr + "\nType: " + type);
	}

	private SymbolicExpression backTranslateTuple(Expr expr, SymbolicType type) {
		if (type instanceof SymbolicArrayType) {
			// component 0 is length, 1 is array
			SymbolicType elementType = ((SymbolicArrayType) type).elementType();
			Expr lengthExpr = expr.getChild(0);
			Expr arrayExpr = expr.getChild(1);
			int length;

			if (lengthExpr.isRational())
				length = lengthExpr.getRational().getInteger();
			else
				throw new SARLInternalException(
						"Expected constant in component 0: " + expr);
			if (arrayExpr.isArrayLiteral())
				return backTranslateArrayLiteral(arrayExpr, length, elementType);
			else {
				SymbolicExpression result = defaultValue(type);

				result = setArrayElement(result, length - 1,
						defaultValue(elementType));
				return result;
			}
		} else {
			LinkedList<SymbolicExpression> components = new LinkedList<SymbolicExpression>();
			SymbolicTupleType tupleType = (SymbolicTupleType) type;
			SymbolicTypeSequence sequence = tupleType.sequence();
			Iterator<SymbolicType> typeIter = sequence.iterator();

			for (Object child : expr.getChildren())
				components.add(backTranslate((Expr) child, typeIter.next()));
			return universe.tuple(tupleType, components);
		}
	}

	/**
	 * Translates a CVC3 concrete expression to a concrete SARL symbolic
	 * expression. Useful for obtaining a model when a counterexample is found.
	 * 
	 * @param expr
	 *            a CVC3 concrete expression
	 * @return translation back to SARL SymbolicExpression
	 */
	// add type here
	private SymbolicExpression backTranslate(Expr expr, SymbolicType type) {
		// TODO
		// concrete element of unions: selectExpr, consExpr
		// APPLY: application of function symbols to arguments
		if (expr.isRational())
			return backTranslateRational(expr, type);
		if (expr.isBooleanConst())
			return backTranslateBoolean(expr);
		if (expr.isArrayLiteral())
			return backTranslateArrayLiteral(expr, type);
		if ("_TUPLE".equals(expr.getKind()))
			return backTranslateTuple(expr, type);
		return null;
	}

	/**
	 * Translates a CVC3 "read" expression into a SymbolicExpression using the
	 * variable map. By "read" expression we mean a variable, an array-read,
	 * tuple read, or function application, or an expression build recursively
	 * from such operations.
	 * 
	 * @param expr
	 *            a CVC3 read expression
	 * @return symbolic expression translation of <code>expr</code>
	 */
	private SymbolicExpression read(Expr expr) {
		if (expr.isVar()) {
			SymbolicConstant x = varMap.get(expr);
			SymbolicExpression result;

			if (x == null)
				throw new SARLInternalException("Unknown CVC3 variable: "
						+ expr);
			result = model.get(x);
			if (result == null) {
				result = defaultValue(x.type());
				model.put(x, result);
			}
			return result;
		}
		if (expr.isRead()) {
			SymbolicExpression array = read(expr.getChild(0));
			SymbolicCompleteArrayType arrayType = (SymbolicCompleteArrayType) array
					.type();
			int index = expr.getChild(1).getRational().getInteger();
			@SuppressWarnings("unchecked")
			SymbolicSequence<SymbolicExpression> sequence = (SymbolicSequence<SymbolicExpression>) array
					.argument(0);
			int length = sequence.size();

			if (index >= length)
				return defaultValue(arrayType.elementType());
			return sequence.get(index);
		}
		if (expr.isApply()) {
			Op op = expr.getOp();
			Expr opExpr = op.getExpr();
			String opKind = opExpr.getKind();

			if (opKind.equals("_TUPLE_SELECT")) {
				int index = opExpr.getChild(0).getRational().getInteger();
				Expr tupleExpr = expr.getChild(0);
				SymbolicExpression tuple = read(tupleExpr);
				SymbolicType tupleType = tuple.type();

				if (tupleType instanceof SymbolicArrayType) {
					if (index == 1)
						return tuple;
					throw new SARLInternalException("Expected index 1, saw "
							+ index);
				}
				return universe.tupleRead(tuple, universe.intObject(index));
			}
			throw new SARLInternalException(
					"Function models not yet implemented");
		}
		throw new SARLInternalException("Unknown CVC3 expression: " + expr);
	}

	private SymbolicExpression defaultValue(SymbolicType type) {
		SymbolicTypeKind kind = type.typeKind();

		switch (kind) {
		case INTEGER:
			return universe.zeroInt();
		case REAL:
			return universe.zeroReal();
		case BOOLEAN:
			return universe.falseExpression();
		case ARRAY:
			// initializer of length 0
			SymbolicArrayType arrayType = (SymbolicArrayType) type;

			return universe.array(arrayType.elementType(), empty);
		case TUPLE: {
			SymbolicTupleType tupleType = (SymbolicTupleType) type;
			LinkedList<SymbolicExpression> initializer = new LinkedList<SymbolicExpression>();

			for (SymbolicType fieldType : tupleType.sequence())
				initializer.add(defaultValue(fieldType));
			return universe.tuple(tupleType, initializer);
		}
		case FUNCTION: {
			// send everything to default value of range
			// SymbolicFunctionType functionType = (SymbolicFunctionType) type;

			// TODO: need multi-variable lambda expression
			throw new SARLInternalException(
					"Default value for function not yet implemented");
		}
		case UNION: {
			SymbolicUnionType unionType = (SymbolicUnionType) type;
			SymbolicType type0 = unionType.sequence().getType(0);
			SymbolicExpression init0 = defaultValue(type0);

			return universe
					.unionInject(unionType, universe.intObject(0), init0);
		}
		default:
			throw new SARLInternalException("Unknown type: " + type);
		}
	}

	/**
	 * array must be a CONCRETE array.
	 * 
	 * @param array
	 * @param index
	 * @param value
	 */
	private SymbolicExpression setArrayElement(SymbolicExpression array,
			int index, SymbolicExpression value) {
		assert array.operator() == SymbolicOperator.CONCRETE;

		SymbolicCompleteArrayType type = (SymbolicCompleteArrayType) array
				.type();
		SymbolicType elementType = type.elementType();
		@SuppressWarnings("unchecked")
		SymbolicSequence<SymbolicExpression> sequence = (SymbolicSequence<SymbolicExpression>) array
				.argument(0);
		int size = sequence.size();

		if (index < size) {
			sequence = sequence.set(index, value);
		} else {
			if (index > size) {
				SymbolicExpression filler = defaultValue(elementType);

				while (index > size) {
					sequence = sequence.add(filler);
					size++;
				}
			}
			sequence = sequence.add(value);
		}
		return universe.array(elementType, sequence);
	}

	private void assignVariable(Expr expr, SymbolicExpression value) {
		SymbolicConstant x = varMap.get(expr);

		if (x == null)
			throw new SARLInternalException("Unknown CVC3 variable: " + expr);
		model.put(x, value);
	}

	/**
	 * Process an assignment in which the left-hand side is a CVC array-read
	 * 
	 * @param expr
	 * @param value
	 */
	private void assignRead(Expr expr, SymbolicExpression value) {
		Expr arrayExpr = expr.getChild(0);
		Expr indexExpr = expr.getChild(1);
		SymbolicExpression arrayExpression = read(arrayExpr);

		assign(arrayExpr,
				setArrayElement(arrayExpression, indexExpr.getRational()
						.getInteger(), value));
	}

	/**
	 * Process an assignment in which the left-hand side is a CVC3 "APPLY"
	 * expression.
	 * 
	 * @param expr
	 *            APPLY expression
	 * @param value
	 *            right-hand side of assignment
	 */
	private void assignApply(Expr expr, SymbolicExpression value) {
		Op op = expr.getOp();
		Expr opExpr = op.getExpr();
		String opKind = opExpr.getKind();

		if (opKind.equals("_TUPLE_SELECT")) {
			int index = opExpr.getChild(0).getRational().getInteger();
			Expr argumentExpr = expr.getChild(0);
			SymbolicExpression argument = read(argumentExpr);
			SymbolicType argumentType = argument.type();

			if (argumentType instanceof SymbolicArrayType) {
				if (index == 1) {
					// a.1 := value. a is the actual array...
					assign(argumentExpr, value);
				} else if (index == 0) {
					// a.0 := value. a.0 is length of a.
					assert argument.operator() == SymbolicOperator.CONCRETE;
					@SuppressWarnings("unchecked")
					SymbolicSequence<SymbolicExpression> sequence = (SymbolicSequence<SymbolicExpression>) argument
							.argument(0);
					int size = sequence.size();
					IntegerNumber valueNumber = (IntegerNumber) universe
							.extractNumber((NumericExpression) value);
					int valueInt = valueNumber.intValue();

					if (size < valueInt)
						assign(argumentExpr,
								setArrayElement(
										argument,
										valueInt - 1,
										defaultValue(((SymbolicArrayType) argumentType)
												.elementType())));
					else if (size > valueInt)
						throw new SARLInternalException(
								"CVC3 model assigns size " + valueInt
										+ " to array of size " + size
										+ ":\narray: " + argument);
				} else
					throw new SARLInternalException("Expected index 1, saw "
							+ index);
			} else if (argumentType instanceof SymbolicTupleType)
				assign(argumentExpr, universe.tupleWrite(argument,
						universe.intObject(index), value));
			else
				throw new SARLInternalException(
						"Unexpected type for tuple select argument: "
								+ argumentType);
		} else
			throw new SARLInternalException(
					"Function models not yet implemented");
	}

	/**
	 * Modifies the model by assigning the given value to the given expression.
	 * 
	 * @param expr
	 *            a CVC3 Expr
	 * @param value
	 *            a symbolic expression value to assign to expr
	 */
	private void assign(Expr expr, SymbolicExpression value) {
		if (expr.isVar()) // variable
			assignVariable(expr, value);
		else if (expr.isRead()) // array read
			assignRead(expr, value);
		else if (expr.isApply()) // tuple write or function application
			assignApply(expr, value);
		else
			throw new SARLInternalException("Unexpected key in CVC model: "
					+ expr);
	}

	/**
	 * Computes the model map. A big part of the problem is figuring out what a
	 * CVC3 model is, in the absence of any specification. Here are some
	 * examples of what can occur in a CVC3 model:
	 * 
	 * <pre>
	 * key = Expr[a, type=[INT, (ARRAY INT OF INT)], kind=_UCONST]
	 * 
	 * value = Expr[(-1, (ARRAY (arr_var: INT): -1)), type=[INT, (ARRAY
	 *   INT OF INT)], kind=_TUPLE: Expr[-1, type=INT,
	 *   kind=_RATIONAL_EXPR], Expr[(ARRAY (arr_var: INT): -1),
	 *   type=(ARRAY INT OF INT), kind=_ARRAY_LITERAL]]
	 *   
	 * key = Expr[(a).1[0], type=INT, kind=_READ: Expr[(a).1,
	 * 		 type=(ARRAY INT OF INT), kind=_APPLY, op=Op[Op(125 (_TUPLE_SELECT
	 * 		 1)), kind = _TUPLE_SELECT: Expr[1, type=INT,
	 * 		 kind=_RATIONAL_EXPR]]: Expr[a, type=[INT, (ARRAY INT OF INT)],
	 * 		 kind=_UCONST]], Expr[0, type=INT, kind=_RATIONAL_EXPR]]
	 * 
	 * 
	 * 
	 * 
	 * 
	 * </pre>
	 */
	private void computeModel() {
		for (Entry<?, ?> entry : cvcModel.entrySet()) {
			Expr key = (Expr) entry.getKey();
			Expr value = (Expr) entry.getValue();
			SymbolicExpression sarlValue;

			if (out != null) {
				// debugging...
				out.print("key = ");
				printExpr(key, out);
				out.println();
				out.print("value = ");
				printExpr(value, out);
				out.println();
				out.flush();
			}
			sarlValue = backTranslate(value, simplifyType(typeOf(key)));
			if (sarlValue == null)
				throw new SARLInternalException("Unable to back translate "
						+ value);
			assign(key, sarlValue);
		}
	}

	public Map<SymbolicConstant, SymbolicExpression> getModel() {
		return model;
	}
}
