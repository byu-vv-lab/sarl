package edu.udel.cis.vsl.sarl.IF;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.universe.Universes;

public class MixedArithmeticTest {

	private static PrintStream out = System.out;
	private SymbolicUniverse universe;
	private SymbolicType herbrandReal, herbrandInteger;

	// private SymbolicType realType, integerType;

	@Before
	public void setUp() throws Exception {
		this.universe = Universes.newStandardUniverse();
		this.herbrandReal = universe.herbrandRealType();
		this.herbrandInteger = universe.herbrandIntegerType();
		// this.realType = universe.realType();
		// this.integerType = universe.integerType();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test12ideal() {
		NumericExpression one = universe.rational(1);
		NumericExpression two = universe.rational(2);
		NumericExpression a = universe.add(one, two);
		NumericExpression b = universe.add(two, one);

		out.println("test12ideal: a = " + a);
		out.println("test12ideal: b = " + b);
		assertEquals(a, b);
	}

	@Test
	public void test12herbrand() {
		NumericExpression one = (NumericExpression) universe.cast(herbrandReal,
				universe.rational(1));
		NumericExpression two = (NumericExpression) universe.cast(herbrandReal,
				universe.rational(2));
		NumericExpression a = universe.add(one, two);
		NumericExpression b = universe.add(two, one);

		out.println("test12herbrand: a = " + a);
		out.println("test12herbrand: b = " + b);
		assertFalse(a.equals(b));
	}

	@Test
	public void herbrandSame() {
		NumericExpression one = (NumericExpression) universe.cast(herbrandReal,
				universe.rational(1));
		NumericExpression two = (NumericExpression) universe.cast(herbrandReal,
				universe.rational(2));
		NumericExpression a = universe.multiply(one, two);
		NumericExpression b = universe.multiply(one, two);

		out.println("herbrandSame: a = " + a);
		out.println("herbrandSame: b = " + b);
		assertEquals(a, b);
	}

	@Test
	public void herbrandSimplify() {
		NumericExpression one = (NumericExpression) universe.cast(
				herbrandInteger, universe.integer(1));
		NumericExpression two = (NumericExpression) universe.cast(
				herbrandInteger, universe.integer(2));
		NumericSymbolicConstant x = (NumericSymbolicConstant) universe
				.symbolicConstant(universe.stringObject("X"), herbrandInteger);
		NumericExpression e1 = universe.add(x, one);
		BooleanExpression p = universe.equals(x, two);
		Reasoner reasoner = universe.reasoner(p);
		NumericExpression e2 = (NumericExpression) reasoner.simplify(e1);
		NumericExpression expected = universe.add(two, one);

		out.println("herbrandSimplify: e1 = " + e1);
		out.println("herbrandSimplify: p  = " + p);
		out.println("herbrandSimplify: e2 = " + e2);
		assertEquals(expected, e2);
	}

	@Test
	public void hrelations() {
		NumericExpression one = (NumericExpression) universe.cast(
				herbrandInteger, universe.integer(1));
		NumericSymbolicConstant x = (NumericSymbolicConstant) universe
				.symbolicConstant(universe.stringObject("X"), herbrandInteger);
		BooleanExpression assumption = universe.and(
				universe.lessThanEquals(x, one),
				universe.lessThanEquals(one, x));
		Reasoner reasoner = universe.reasoner(assumption);
		BooleanExpression newAssumption = reasoner.getReducedContext();

		out.println("hrelations: assumption    : " + assumption);
		out.println("hrelations: newAssumption : " + newAssumption);
		assertEquals(assumption, newAssumption);
	}
}
