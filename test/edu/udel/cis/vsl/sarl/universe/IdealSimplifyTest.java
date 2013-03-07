package edu.udel.cis.vsl.sarl.universe;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.Simplifier;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

public class IdealSimplifyTest {
	// private static PrintStream out = System.out;
	private SymbolicUniverse universe;
	// private NumberFactory numberFactory;
	private StringObject Xobj; // "X"
	private StringObject Yobj; // "Y"
	private StringObject uobj; // "u"
	// private StringObject vobj; // "v"
	private SymbolicType realType, integerType;
	private SymbolicConstant x; // real symbolic constant "X"
	private SymbolicConstant y; // real symbolic constant "Y"
	private SymbolicConstant u; // integer symbolic constant "u"
	// private SymbolicConstant v; // integer symbolic constant "v"
	private SymbolicExpression two; // real 2.0
	private SymbolicExpression three; // real 3.0
	private SymbolicExpression trueExpr, falseExpr;

	@Before
	public void setUp() throws Exception {
		universe = Universes.newIdealUniverse();
		// numberFactory = universe.numberFactory();
		Xobj = universe.stringObject("X");
		Yobj = universe.stringObject("Y");
		uobj = universe.stringObject("u");
		// vobj = universe.stringObject("v");
		realType = universe.realType();
		integerType = universe.integerType();
		x = universe.symbolicConstant(Xobj, realType);
		y = universe.symbolicConstant(Yobj, realType);
		u = universe.symbolicConstant(uobj, integerType);
		// v = universe.symbolicConstant(vobj, integerType);
		two = universe.symbolic(2.0);
		three = universe.symbolic(3.0);
		trueExpr = universe.symbolic(true);
		falseExpr = universe.symbolic(false);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * True: X -> X
	 */
	@Test
	public void simplifyTrivial() {
		Simplifier simplifier = universe.simplifier(universe.symbolic(true));

		assertEquals(x, simplifier.apply(x));
		assertEquals(trueExpr, simplifier.newAssumption());
	}

	/**
	 * X==3: X -> 3.
	 */
	@Test
	public void simplifyConstant() {
		SymbolicExpression assumption = universe.equals(x, three);
		Simplifier simplifier = universe.simplifier(assumption);

		assertEquals(three, simplifier.apply(x));
		assertEquals(trueExpr, simplifier.newAssumption());
	}

	/**
	 * X<=3 && X>=3: X -> 3
	 */
	@Test
	public void simplifyTightBounds() {
		SymbolicExpression assumption = universe.and(
				universe.lessThanEquals(x, three),
				universe.lessThanEquals(three, x));
		Simplifier simplifier = universe.simplifier(assumption);

		assertEquals(three, simplifier.apply(x));
		assertEquals(trueExpr, simplifier.newAssumption());
	}

	/**
	 * u < 3 && u >=2: u -> 2
	 */
	@Test
	public void simplifyIntTight1() {
		SymbolicExpression assumption = universe.and(
				universe.lessThan(u, universe.symbolic(3)),
				universe.lessThanEquals(universe.symbolic(2), u));
		Simplifier simplifier = universe.simplifier(assumption);

		assertEquals(universe.symbolic(2), simplifier.apply(u));
		assertEquals(trueExpr, simplifier.newAssumption());
	}

	/**
	 * u < 3 && u >1: u -> 2
	 */
	@Test
	public void simplifyIntTight2() {
		SymbolicExpression assumption = universe.and(
				universe.lessThan(u, universe.symbolic(3)),
				universe.lessThan(universe.symbolic(1), u));
		Simplifier simplifier = universe.simplifier(assumption);

		assertEquals(universe.symbolic(2), simplifier.apply(u));
		assertEquals(trueExpr, simplifier.newAssumption());
	}

	/**
	 * u<3 && u>2 : contradiction
	 */
	@Test
	public void contradict1() {
		SymbolicExpression assumption = universe.and(
				universe.lessThan(u, universe.symbolic(3)),
				universe.lessThan(universe.symbolic(2), u));
		Simplifier simplifier = universe.simplifier(assumption);

		assertEquals(u, simplifier.apply(u));
		assertEquals(falseExpr, simplifier.newAssumption());
	}

	/**
	 * x<3 && x>2 : x->x
	 */
	@Test
	public void noSimplify() {
		SymbolicExpression assumption = universe.and(
				universe.lessThan(x, three), universe.lessThan(two, x));
		Simplifier simplifier = universe.simplifier(assumption);

		assertEquals(x, simplifier.apply(x));
		assertEquals(assumption, simplifier.newAssumption());
	}

	/**
	 * u=2 : a{5,6,7}[u]->7
	 */
	@Test
	public void simplifyArrayRead() {
		SymbolicExpression a = universe.symbolicConstant(
				universe.stringObject("a"), universe.arrayType(realType));

		a = universe.arrayWrite(a, universe.symbolic(0),
				universe.castToReal(universe.symbolic(5)));
		a = universe.arrayWrite(a, universe.symbolic(1),
				universe.castToReal(universe.symbolic(6)));
		a = universe.arrayWrite(a, universe.symbolic(2),
				universe.castToReal(universe.symbolic(7)));

		SymbolicExpression read = universe.arrayRead(a, u);
		SymbolicExpression assumption = universe
				.equals(u, universe.symbolic(2));
		Simplifier simplifier = universe.simplifier(assumption);

		assertEquals(universe.symbolic(7.0), simplifier.apply(read));
		assertEquals(trueExpr, simplifier.newAssumption());
	}

	/**
	 * X+Y=3 && X-Y=2 : X->5/2, Y->1/2
	 */
	@Test
	public void linearSolve1() {
		SymbolicExpression assumption = universe.and(
				universe.equals(universe.add(x, y), three),
				universe.equals(universe.subtract(x, y), two));
		Simplifier simplifier = universe.simplifier(assumption);

		assertEquals(universe.rational(5, 2), simplifier.apply(x));
		assertEquals(universe.rational(1, 2), simplifier.apply(y));
		assertEquals(trueExpr, simplifier.newAssumption());
	}

	/**
	 * X^3+Y^7=3 && X^3-Y^7=2 : X^3->5/2, Y^7->1/2
	 * 
	 * This requires linearizePolynomials to be true.
	 */
	@Test
	public void linearSolve2() {
		SymbolicExpression x3 = universe.power(x, 3);
		SymbolicExpression y7 = universe.power(y, 7);
		SymbolicExpression assumption = universe.and(
				universe.equals(universe.add(x3, y7), three),
				universe.equals(universe.subtract(x3, y7), two));
		SymbolicExpression newAssumption = universe.and(
				universe.equals(x3, universe.rational(5, 2)),
				universe.equals(y7, universe.rational(1, 2)));
		Simplifier simplifier = universe.simplifier(assumption);

		assertEquals(universe.rational(5, 2), simplifier.apply(x3));
		assertEquals(universe.rational(1, 2), simplifier.apply(y7));
		assertEquals(newAssumption, simplifier.newAssumption());
	}

	/**
	 * X<1 && X<1.5-> X<1
	 */
	@Test
	public void simplifyPCBound1() {
		SymbolicExpression p0 = universe.lessThan(x, universe.symbolic(1.0));
		SymbolicExpression p1 = universe.and(p0,
				universe.lessThan(x, universe.symbolic(1.5)));
		Simplifier simplifier = universe.simplifier(p1);

		assertEquals(p0, simplifier.newAssumption());
		assertEquals(x, simplifier.apply(x));
	}

	/**
	 * X<=1 : X>=1 -> X==1
	 */
	@Test
	public void simplifyBound1() {
		SymbolicExpression one = universe.symbolic(1.0);
		SymbolicExpression assumption = universe.lessThanEquals(x, one);
		Simplifier simplifier = universe.simplifier(assumption);

		assertEquals(universe.equals(x, one),
				simplifier.apply(universe.lessThanEquals(one, x)));
	}

	/**
	 * X<=1 : X<=1 -> true
	 */
	@Test
	public void simplifyBound2() {
		SymbolicExpression one = universe.symbolic(1.0);
		SymbolicExpression assumption = universe.lessThanEquals(x, one);
		Simplifier simplifier = universe.simplifier(assumption);

		assertEquals(universe.symbolic(true),
				simplifier.apply(universe.lessThanEquals(x, one)));
	}

	/**
	 * Integer division. true : 2(u/2) -> 2(u/2)
	 */
	@Test
	public void simplifyIntDivNo() {
		SymbolicExpression e = universe.multiply(universe.symbolic(2),
				universe.divide(u, universe.symbolic(2)));
		Simplifier simplifier = universe.simplifier(trueExpr);

		assertEquals(e, simplifier.apply(e));
	}

	/**
	 * Integer division. true : (2u)/2 -> u
	 */
	@Test
	public void simplifyIntDivYes() {
		SymbolicExpression e = universe.divide(
				universe.multiply(universe.symbolic(2), u),
				universe.symbolic(2));

		assertEquals(u, e);
	}

	/**
	 * Integer modulus. true : (2u)%2 -> 0
	 */
	@Test
	public void simplifyIntMod() {
		SymbolicExpression e = universe.modulo(
				universe.multiply(universe.symbolic(2), u),
				universe.symbolic(2));

		assertEquals(universe.zeroInt(), e);
	}
}
