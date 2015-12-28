package edu.udel.cis.vsl.sarl.IF;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.PrintStream;
import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

public class DemoTest {

	private static SymbolicUniverse universe;

	private static SymbolicType realType;

	private static SymbolicType integerType;

	private static PrintStream out = System.out;

	private static NumericSymbolicConstant x;

	private static NumericSymbolicConstant y;

	private static NumericExpression two;

	// private static NumericExpression three;

	private static NumericExpression pi;

	private static BooleanExpression trueExpr;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		universe = SARL.newStandardUniverse();
		realType = universe.realType();
		integerType = universe.integerType();
		x = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("x"), realType);
		y = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("y"), realType);
		two = universe.rational(2);
		// three = universe.rational(3);
		pi = universe.rational(3.1415926);
		trueExpr = universe.bool(true);
	}

	@Before
	public void setUp() throws Exception {

	}

	@Test
	@Ignore
	public void test() {
		NumericExpression xplusy;
		NumericExpression w1, w2;

		out.println("x = " + x);
		out.println("y = " + y);
		out.println("Type of x is " + x.type());
		xplusy = universe.add(x, y);
		out.println("x+y = " + xplusy);
		w1 = universe.add(xplusy, y);
		out.println("w1 = " + w1);
		w2 = universe.add(x, universe.multiply(two, y));
		out.println("w2 = " + w2);
		out.flush();
		assertEquals(w2, w1);
		// assertSame(w2, w1); // w1==w2?
		w1 = (NumericExpression) universe.canonic(w1);
		w2 = (NumericExpression) universe.canonic(w2);
		assertSame(w2, w1);
	}

	@Ignore
	@Test
	public void exp() {
		NumericExpression w = universe.power(universe.subtract(x, y), 100);

		out.println("(x-y)^100 = " + w);
		out.flush();
	}

	@Ignore
	@Test
	public void linear() {
		BooleanExpression assumption1 = universe.equals(universe.add(x, y),
				universe.rational(3)); // x+y = 3
		BooleanExpression assumption2 = universe.equals(
				universe.add(universe.multiply(two, x), universe.minus(y)),
				universe.rational(5));
		BooleanExpression assumption = universe.and(assumption1, assumption2);
		NumericExpression x_simple, y_simple;
		NumericExpression x_expected = universe.rational(8, 3);
		// NumericExpression y_expected = universe.rational(1, 3);

		out.println("assumption = " + assumption);
		out.flush();

		Reasoner reasoner = universe.reasoner(assumption);

		x_simple = (NumericExpression) reasoner.simplify(x);
		out.println("x_simple = " + x_simple);
		y_simple = (NumericExpression) reasoner.simplify(y);
		out.println("y_simple = " + y_simple);

		out.println("new assumption = " + reasoner.getReducedContext());

		ValidityResult result = reasoner.valid(universe.equals(x, x_expected));

		out.println("result = " + result.getResultType());

		Reasoner reasoner1 = universe.reasoner(assumption1);

		ValidityResult result1 = reasoner1
				.valid(universe.equals(x, x_expected));

		out.println("result1 = " + result1.getResultType());

		result1 = reasoner1.validOrModel(universe.equals(x, x_expected));

		out.println(((ModelResult) result1).getModel());

	}

	@Test
	@Ignore
	public void array() {
		// SymbolicType arrayType = universe.arrayType(realType);

		SymbolicExpression a = universe.array(realType,
				Arrays.asList(new SymbolicExpression[] { x, y, pi }));

		out.println("a = " + a);

		out.println("a[2] = " + universe.arrayRead(a, universe.integer(2)));

		SymbolicExpression b = universe.arrayWrite(a, universe.integer(2), two);

		out.println("b = " + b);

		// SymbolicExpression c = universe.arrayWrite(b, universe.integer(3),
		// two);
	}

	@Test
	public void abstractIndex() {
		SymbolicExpression a = universe.array(realType,
				Arrays.asList(new SymbolicExpression[] { x, y, pi }));
		NumericExpression i = (NumericExpression) universe.symbolicConstant(
				universe.stringObject("i"), integerType);
		SymbolicExpression read = universe.arrayRead(a, i);

		out.println("read = " + read);

		SymbolicExpression write = universe.arrayWrite(a, i, two);

		out.println("write = " + write);

		SymbolicExpression readwrite = universe.arrayRead(write, i);

		out.println("readwrite : " + readwrite);

		Reasoner reasoner = universe.reasoner(trueExpr);

		out.println("readwrite simplfied: " + reasoner.simplify(readwrite));

		out.println("Is readwrite=2? "
				+ reasoner.valid(universe.equals(two, readwrite))
						.getResultType());
	}

	@Test
	public void array2d() {

	}

}
