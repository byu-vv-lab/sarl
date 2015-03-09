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
package edu.udel.cis.vsl.sarl.IF;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.PrintStream;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;

/**
 * Tests model (counterexample) finding abilities of the provers. ALL TESTS
 * IGNORED FOR NOW --- UNTIL model-finding is implemented.
 * 
 * @author siegel
 *
 */
public class FindModelTest {

	private SymbolicUniverse universe;

	private SymbolicRealType realType;

	private SymbolicIntegerType intType;

	private NumericSymbolicConstant x;

	private SymbolicConstant a; // array of int

	private BooleanExpression t;

	private PrintStream out = System.out;

	@Before
	public void setUp() throws Exception {
		universe = SARL.newStandardUniverse();
		// universe.setShowProverQueries(true); // DEBUGGING
		realType = universe.realType();
		intType = universe.integerType();
		x = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("X"), realType);
		t = universe.trueExpression();
		a = universe.symbolicConstant(universe.stringObject("a"),
				universe.arrayType(intType));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	@Test
	public void findX() {
		BooleanExpression p = universe.lessThanEquals(universe.zeroReal(), x);
		Reasoner reasoner = universe.reasoner(t);
		ValidityResult result = reasoner.validOrModel(p);
		Map<SymbolicConstant, SymbolicExpression> model;
		NumericExpression value;
		Number number;

		assertEquals(ResultType.NO, result.getResultType());
		assertTrue(result instanceof ModelResult);
		model = ((ModelResult) result).getModel();
		assertTrue(model != null);
		assertEquals(1, model.size());
		value = (NumericExpression) model.get(x);
		out.println("findX: X = " + value);
		assertTrue(value != null);
		assertEquals(realType, value.type());
		number = universe.extractNumber(value);
		assertTrue(number != null);
		assertTrue(number.signum() < 0);
	}

	@Ignore
	@Test
	public void dontFindX() {
		BooleanExpression assumption = universe
				.lessThan(x, universe.zeroReal());
		BooleanExpression predicate = universe.lessThanEquals(x,
				universe.rational(1));
		Reasoner reasoner = universe.reasoner(assumption);
		ValidityResult result = reasoner.validOrModel(predicate);

		assertEquals(ResultType.YES, result.getResultType());
		assertFalse(result instanceof ModelResult);
	}

	@Ignore
	@Test
	public void array1() {
		NumericExpression zero = universe.zeroInt();
		BooleanExpression p = universe.equals(zero,
				(NumericExpression) universe.arrayRead(a, zero));
		Reasoner reasoner = universe.reasoner(t);
		ValidityResult result = reasoner.validOrModel(p);
		Map<SymbolicConstant, SymbolicExpression> model;
		SymbolicExpression value;
		Number number;

		assertEquals(ResultType.NO, result.getResultType());
		model = ((ModelResult) result).getModel();
		assertTrue(model != null);
		value = model.get(a);
		out.println("array1: value = " + value);
		out.flush();
		assertTrue(value != null);
		number = universe.extractNumber((NumericExpression) universe.arrayRead(
				value, zero));
		assertTrue(number.signum() != 0);
	}

	/**
	 * Is "length(a)<5" valid?
	 */
	@Ignore
	@Test
	public void array2() {
		BooleanExpression p = universe.lessThan(universe.length(a),
				universe.integer(5));
		Reasoner reasoner = universe.reasoner(t);
		ValidityResult result = reasoner.validOrModel(p);
		Map<SymbolicConstant, SymbolicExpression> model;
		SymbolicExpression value;

		assertEquals(ResultType.NO, result.getResultType());
		model = ((ModelResult) result).getModel();
		assertTrue(model != null);
		value = model.get(a);
		out.println("array2: value = " + value);
		out.flush();
		assertTrue(value != null);
		assertEquals(SymbolicTypeKind.ARRAY, value.type().typeKind());
		{
			NumericExpression lengthExpression = universe.length(value);
			IntegerNumber number = (IntegerNumber) universe
					.extractNumber(lengthExpression);
			int length = number.intValue();

			assertTrue(length >= 5);
		}
	}

	/**
	 * is "a[0]=a[1] || a[0]=0 || a[1]=1" valid? Answer is NO, and a model
	 * should be found.
	 */
	@Ignore
	@Test
	public void array3() {
		NumericExpression zero = universe.zeroInt(), one = universe.oneInt();
		NumericExpression a0 = (NumericExpression) universe.arrayRead(a, zero);
		NumericExpression a1 = (NumericExpression) universe.arrayRead(a, one);
		BooleanExpression p = universe.or(
				universe.equals(a0, a1),
				universe.or(universe.equals(zero, a0),
						universe.equals(zero, a1)));
		Reasoner reasoner = universe.reasoner(t);
		ValidityResult result = reasoner.validOrModel(p);
		Map<SymbolicConstant, SymbolicExpression> model;
		SymbolicExpression value;
		Number v0, v1;

		assertEquals(ResultType.NO, result.getResultType());
		model = ((ModelResult) result).getModel();
		assertTrue(model != null);
		value = model.get(a);
		out.println("array3: value = " + value);
		out.flush();
		assertTrue(value != null);
		v0 = universe.extractNumber((NumericExpression) universe.arrayRead(
				value, zero));
		v1 = universe.extractNumber((NumericExpression) universe.arrayRead(
				value, one));
		assertTrue(v0.signum() != 0);
		assertTrue(v1.signum() != 0);
		assertTrue(!v0.equals(v1));
	}

	/**
	 * Is "x^2 != 2" valid? Correct answer is NO, but CVC3 only tells us MAYBE.
	 */
	@Ignore
	@Test
	public void sqrt2() {
		NumericSymbolicConstant x = (NumericSymbolicConstant) universe
				.symbolicConstant(universe.stringObject("x"),
						universe.realType());
		BooleanExpression p = universe.neq(universe.multiply(x, x),
				universe.rational(2));
		Reasoner reasoner = universe.reasoner(t);
		ValidityResult result = reasoner.validOrModel(p);
		ResultType resultType = result.getResultType();

		assertTrue(ResultType.NO.equals(resultType)
				|| ResultType.MAYBE.equals(resultType));
	}

	/**
	 * Is "x^2 != 4" valid? Correct answer is NO, with x=2 or x=-2.
	 */
	@Ignore
	@Test
	public void sqrt4() {
		NumericSymbolicConstant x = (NumericSymbolicConstant) universe
				.symbolicConstant(universe.stringObject("x"),
						universe.realType());
		BooleanExpression p = universe.neq(universe.multiply(x, x),
				universe.rational(4));
		Reasoner reasoner = universe.reasoner(t);
		ValidityResult result = reasoner.validOrModel(p);
		ResultType resultType = result.getResultType();
		SymbolicExpression value;

		if (resultType == ResultType.MAYBE) {
			// weak, but OK.
			out.println("Warning: could not solve x^2=4.\n"
					+ "This is not wrong, but pretty weak.");
		} else {
			assertEquals(ResultType.NO, resultType);
			value = ((ModelResult) result).getModel().get(x);
			out.println("sqrt4: value = " + value);
			assertTrue(universe.rational(2).equals(value)
					|| universe.rational(-2).equals(value));
		}
	}

	/**
	 * Let N be a positive integer and b be an array of length N. Is "b[0]=0"
	 * valid?
	 */
	@Ignore
	@Test
	public void completeArray() {
		NumericSymbolicConstant N = (NumericSymbolicConstant) universe
				.symbolicConstant(universe.stringObject("N"), intType);
		SymbolicConstant b = universe.symbolicConstant(
				universe.stringObject("b"), universe.arrayType(intType, N));
		BooleanExpression assumption = universe.lessThan(universe.zeroInt(), N);
		BooleanExpression predicate = universe.equals(universe.zeroInt(),
				universe.arrayRead(b, universe.zeroInt()));
		Reasoner reasoner = universe.reasoner(assumption);
		ModelResult result = (ModelResult) reasoner.validOrModel(predicate);
		ResultType resultType = result.getResultType();
		NumericExpression N_val, b_val0;
		SymbolicExpression b_val;

		assertEquals(ResultType.NO, resultType);
		N_val = (NumericExpression) result.getModel().get(N);
		b_val = result.getModel().get(b);
		out.println("completeArray : N_val = " + N_val);
		out.println("completeArray : b_val = " + b_val);
		assertTrue(((IntegerNumber) universe.extractNumber(N_val)).signum() > 0);
		b_val0 = (NumericExpression) universe.arrayRead(b_val,
				universe.zeroInt());
		assertTrue(((IntegerNumber) universe.extractNumber(b_val0)).signum() != 0);
	}
}
