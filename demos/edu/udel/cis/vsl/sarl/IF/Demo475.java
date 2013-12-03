package edu.udel.cis.vsl.sarl.IF;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

public class Demo475 {

	private static SymbolicUniverse universe;

	private static NumericSymbolicConstant x;

	private static NumericSymbolicConstant y;

	private static NumericExpression xpy;

	private static SymbolicType realType;

	// private static SymbolicType integerType;

	private static NumericExpression one, two;

	private static PrintStream out = System.out;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		universe = SARL.newStandardUniverse();
		realType = universe.realType();
		// integerType = universe.integerType();
		x = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("x"), realType);
		y = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("y"), realType);
		xpy = universe.add(x, y);
		one = universe.rational(1); // 1.0
		two = universe.rational(2); // 2.0
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	@Test
	public void testxy() {
		NumericExpression w;
		NumericExpression z;

		out.println("x = " + x);
		out.println("y = " + y);
		out.println("xpy = " + xpy);
		w = universe.add(xpy, y);
		out.println("w = " + w);
		z = universe.add(x, universe.multiply(two, y));
		assertEquals(z, w);
	}

	@Ignore
	@Test
	public void power() {
		NumericExpression pow = universe.power(xpy, 50);

		out.println("pow = " + pow);

	}

	@Ignore
	@Test
	public void simplify() {
		BooleanExpression xeq3 = universe.equals(x, universe.rational(3));
		Reasoner reasoner = universe.reasoner(xeq3);
		SymbolicExpression w = reasoner.simplify(xpy);
		// NumericExpression pow = universe.power(xpy, 50);
		// SymbolicExpression powSimp = reasoner.simplify(pow);

		// out.println("pow = " + powSimp);

		assertEquals(universe.add(y, universe.rational(3)), w);

	}

	@Ignore
	@Test
	public void simp2() {
		NumericExpression z = universe.subtract(universe.multiply(x, x), one);
		NumericExpression w = universe.multiply(universe.subtract(x, one),
				universe.add(x, one));

		out.println(" w = " + w);
		assertEquals(w, z);
	}

	@Ignore
	@Test
	public void simp3() {
		// BooleanExpression claim1 = universe.equals(y, universe.add(x, one));
		BooleanExpression claim2 = universe.equals(
				universe.multiply(universe.rational(3), y),
				universe.add(universe.multiply(two, x), one));
		// BooleanExpression claim = universe.and(claim1, claim2);
		Reasoner reasoner = universe.reasoner(claim2);
		SymbolicExpression x_simp = reasoner.simplify(x);
		SymbolicExpression y_simp = reasoner.simplify(y);

		out.println("x_simp = " + x_simp);
		out.println("y_simp = " + y_simp);
		assertEquals(universe.rational(-2), x_simp);
		assertEquals(universe.rational(-1), y_simp);
	}

	@Ignore
	@Test
	public void simpWrong() {
		BooleanExpression claim1 = universe.equals(y, universe.add(x, one));
		BooleanExpression claim2 = universe.equals(y, universe.add(x, two));
		BooleanExpression claim = universe.and(claim1, claim2);
		Reasoner reasoner = universe.reasoner(claim);
		SymbolicExpression x_simp = reasoner.simplify(x);
		SymbolicExpression y_simp = reasoner.simplify(y);

		out.println(reasoner.getReducedContext());
		out.println("x_simp = " + x_simp);
		out.println("y_simp = " + y_simp);
		assertEquals(universe.rational(-2), x_simp);
		assertEquals(universe.rational(-1), y_simp);
	}

	@Ignore
	@Test
	public void proof() {
		BooleanExpression claim1 = universe.equals(y, universe.add(x, one));
		// BooleanExpression claim2 = universe.equals(
		// universe.multiply(universe.rational(3), y),
		// universe.add(universe.multiply(two, x), one));
		// BooleanExpression claim = universe.and(claim1, claim2);
		Reasoner reasoner = universe.reasoner(claim1);
		BooleanExpression q1 = universe.equals(x, universe.minus(two));

		ValidityResult result = reasoner.validOrModel(q1);
		ModelResult modelResult = (ModelResult) result;

		assertEquals(ResultType.NO, result.getResultType());
		out.println("model = " + modelResult.getModel());
	}

	@Test
	public void array() {
		SymbolicExpression array = universe.array(
				realType,
				Arrays.asList(new SymbolicExpression[] { two,
						universe.rational(3.14159), universe.rational(-10) }));

		out.println("array = " + array);
		assertEquals(universe.rational(-10),
				universe.arrayRead(array, universe.integer(2)));

		// out.println(universe.arrayWrite(array, universe.integer(5), x));

		SymbolicType arrayType = universe.arrayType(realType);

		SymbolicConstant a = universe.symbolicConstant(
				universe.stringObject("a"), arrayType);

		out.println(universe.arrayWrite(a, universe.integer(5), x));

	}

}
