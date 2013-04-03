package edu.udel.cis.vsl.sarl.IF;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.PrintStream;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.prove.TheoremProver;
import edu.udel.cis.vsl.sarl.IF.prove.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.prove.ValidityResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;

public class FindModelTest {

	private SymbolicUniverse universe;

	private SymbolicRealType realType;

	private SymbolicIntegerType intType;

	TheoremProver prover;

	private NumericSymbolicConstant x;

	private BooleanExpression t;

	private PrintStream out = System.out;

	@Before
	public void setUp() throws Exception {
		universe = SARL.newStandardUniverse();
		realType = universe.realType();
		intType = universe.integerType();
		x = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("X"), realType);
		prover = universe.prover();
		t = universe.trueExpression();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void findX() {
		BooleanExpression p = universe.lessThanEquals(universe.zeroReal(), x);
		ValidityResult result = prover.validOrModel(t, p);
		Map<SymbolicConstant, SymbolicExpression> model;
		NumericExpression value;
		Number number;

		assertEquals(ResultType.NO, result.getResultType());
		model = result.getModel();
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

	@Test
	public void dontFindX() {
		BooleanExpression assumption = universe
				.lessThan(x, universe.zeroReal());
		BooleanExpression predicate = universe.lessThanEquals(x,
				universe.rational(1));
		ValidityResult result = prover.validOrModel(assumption, predicate);
		Map<SymbolicConstant, SymbolicExpression> model;

		assertEquals(ResultType.YES, result.getResultType());
		model = result.getModel();
		assertTrue(model == null);
	}

	@Test
	public void array1() {
		prover.setOutput(System.out);

		SymbolicArrayType type = universe.arrayType(intType);
		SymbolicConstant a = universe.symbolicConstant(
				universe.stringObject("a"), type);
		NumericExpression zero = universe.zeroInt();
		BooleanExpression p = universe.equals(zero,
				(NumericExpression) universe.arrayRead(a, zero));
		ValidityResult result = prover.validOrModel(t, p);
		Map<SymbolicConstant, SymbolicExpression> model;
		SymbolicExpression value;
		Number number;

		assertEquals(ResultType.NO, result.getResultType());
		model = result.getModel();
		assertTrue(model != null);
		value = model.get(a);
		out.println("array1: value = " + value);
		out.flush();
		assertTrue(value != null);
		// assertEquals(type, value.type());
		number = universe.extractNumber((NumericExpression) universe.arrayRead(
				value, zero));
		assertTrue(number.signum() != 0);
	}

	@Test
	public void array2() {
		prover.setOutput(System.out);

		SymbolicArrayType type = universe.arrayType(intType);
		SymbolicConstant a = universe.symbolicConstant(
				universe.stringObject("a"), type);
		BooleanExpression p = universe.lessThan(universe.length(a),
				universe.integer(5));
		ValidityResult result = prover.validOrModel(t, p);
		Map<SymbolicConstant, SymbolicExpression> model;
		SymbolicExpression value;

		assertEquals(ResultType.NO, result.getResultType());
		model = result.getModel();
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
}
