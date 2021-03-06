/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.universe;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.Reasoner;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
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
	private NumericSymbolicConstant x; // real symbolic constant "X"
	private NumericSymbolicConstant y; // real symbolic constant "Y"
	private NumericSymbolicConstant u; // integer symbolic constant "u"
	// private NumericSymbolicConstant v; // integer symbolic constant "v"
	private NumericExpression two; // real 2.0
	private NumericExpression three; // real 3.0
	private BooleanExpression trueExpr, falseExpr;

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
		x = (NumericSymbolicConstant) universe.symbolicConstant(Xobj, realType);
		y = (NumericSymbolicConstant) universe.symbolicConstant(Yobj, realType);
		u = (NumericSymbolicConstant) universe.symbolicConstant(uobj,
				integerType);
		// v = universe.symbolicConstant(vobj, integerType);
		two = universe.rational(2.0);
		three = universe.rational(3.0);
		trueExpr = universe.bool(true);
		falseExpr = universe.bool(false);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * True: X -> X
	 */
	@Test
	public void simplifyTrivial() {
		Reasoner reasoner = universe.reasoner(universe.bool(true));

		assertEquals(x, reasoner.simplify(x));
		assertEquals(trueExpr, reasoner.getReducedContext());
	}

	/**
	 * X==3: X -> 3.
	 */
	@Test
	public void simplifyConstant() {
		BooleanExpression assumption = universe.equals(x, three);
		Reasoner reasoner = universe.reasoner(assumption);

		assertEquals(three, reasoner.simplify(x));
		assertEquals(trueExpr, reasoner.getReducedContext());
	}

	/**
	 * X<=3 && X>=3: X -> 3
	 */
	@Test
	public void simplifyTightBounds() {
		BooleanExpression assumption = universe.and(
				universe.lessThanEquals(x, three),
				universe.lessThanEquals(three, x));
		Reasoner reasoner = universe.reasoner(assumption);

		assertEquals(three, reasoner.simplify(x));
		assertEquals(trueExpr, reasoner.getReducedContext());
	}

	/**
	 * u < 3 && u >=2: u -> 2
	 */
	@Test
	public void simplifyIntTight1() {
		BooleanExpression assumption = universe.and(
				universe.lessThan(u, universe.integer(3)),
				universe.lessThanEquals(universe.integer(2), u));
		Reasoner reasoner = universe.reasoner(assumption);

		assertEquals(universe.integer(2), reasoner.simplify(u));
		assertEquals(trueExpr, reasoner.getReducedContext());
	}

	/**
	 * u < 3 && u >1: u -> 2
	 */
	@Test
	public void simplifyIntTight2() {
		BooleanExpression assumption = universe.and(
				universe.lessThan(u, universe.integer(3)),
				universe.lessThan(universe.integer(1), u));
		Reasoner reasoner = universe.reasoner(assumption);

		assertEquals(universe.integer(2), reasoner.simplify(u));
		assertEquals(trueExpr, reasoner.getReducedContext());
	}

	/**
	 * u<3 && u>2 : contradiction
	 */
	@Test
	public void contradict1() {
		BooleanExpression assumption = universe.and(
				universe.lessThan(u, universe.integer(3)),
				universe.lessThan(universe.integer(2), u));
		Reasoner reasoner = universe.reasoner(assumption);

		assertEquals(u, reasoner.simplify(u));
		assertEquals(falseExpr, reasoner.getReducedContext());
	}

	/**
	 * x<3 && x>2 : x->x
	 */
	@Test
	public void noSimplify() {
		BooleanExpression assumption = universe.and(
				universe.lessThan(x, three), universe.lessThan(two, x));
		Reasoner reasoner = universe.reasoner(assumption);

		assertEquals(x, reasoner.simplify(x));
		assertEquals(assumption, reasoner.getReducedContext());
	}

	/**
	 * u=2 : a{5,6,7}[u]->7
	 */
	@Test
	public void simplifyArrayRead() {
		SymbolicExpression a = universe.symbolicConstant(
				universe.stringObject("a"), universe.arrayType(realType));

		a = universe.arrayWrite(a, universe.integer(0),
				universe.cast(realType, universe.integer(5)));
		a = universe.arrayWrite(a, universe.integer(1),
				universe.cast(realType, universe.integer(6)));
		a = universe.arrayWrite(a, universe.integer(2),
				universe.cast(realType, universe.integer(7)));

		SymbolicExpression read = universe.arrayRead(a, u);
		BooleanExpression assumption = universe.equals(u, universe.integer(2));
		Reasoner reasoner = universe.reasoner(assumption);

		assertEquals(universe.rational(7.0), reasoner.simplify(read));
		assertEquals(trueExpr, reasoner.getReducedContext());
	}

	/**
	 * X+Y=3 && X-Y=2 : X->5/2, Y->1/2
	 */
	@Test
	public void linearSolve1() {
		BooleanExpression assumption = universe.and(
				universe.equals(universe.add(x, y), three),
				universe.equals(universe.subtract(x, y), two));
		Reasoner reasoner = universe.reasoner(assumption);

		assertEquals(universe.rational(5, 2), reasoner.simplify(x));
		assertEquals(universe.rational(1, 2), reasoner.simplify(y));
		assertEquals(trueExpr, reasoner.getReducedContext());
	}

	/**
	 * X^3+Y^7=3 && X^3-Y^7=2 : X^3->5/2, Y^7->1/2
	 * 
	 * This requires linearizePolynomials to be true.
	 */
	@Test
	public void linearSolve2() {
		NumericExpression x3 = universe.power(x, 3);
		NumericExpression y7 = universe.power(y, 7);
		BooleanExpression assumption = universe.and(
				universe.equals(universe.add(x3, y7), three),
				universe.equals(universe.subtract(x3, y7), two));
		SymbolicExpression newAssumption = universe.and(
				universe.equals(x3, universe.rational(5, 2)),
				universe.equals(y7, universe.rational(1, 2)));
		Reasoner reasoner = universe.reasoner(assumption);

		assertEquals(universe.rational(5, 2), reasoner.simplify(x3));
		assertEquals(universe.rational(1, 2), reasoner.simplify(y7));
		assertEquals(newAssumption, reasoner.getReducedContext());
	}

	/**
	 * X<1 && X<1.5-> X<1
	 */
	@Test
	public void simplifyPCBound1() {
		BooleanExpression p0 = universe.lessThan(x, universe.rational(1.0));
		BooleanExpression p1 = universe.and(p0,
				universe.lessThan(x, universe.rational(1.5)));
		Reasoner reasoner = universe.reasoner(p1);

		assertEquals(p0, reasoner.getReducedContext());
		assertEquals(x, reasoner.simplify(x));
	}

	/**
	 * X<=1 : X>=1 -> X==1
	 */
	@Test
	public void simplifyBound1() {
		NumericExpression one = universe.rational(1.0);
		BooleanExpression assumption = universe.lessThanEquals(x, one);
		Reasoner reasoner = universe.reasoner(assumption);

		assertEquals(universe.equals(x, one),
				reasoner.simplify(universe.lessThanEquals(one, x)));
	}

	/**
	 * X<=1 : X<=1 -> true
	 */
	@Test
	public void simplifyBound2() {
		NumericExpression one = universe.rational(1.0);
		BooleanExpression assumption = universe.lessThanEquals(x, one);
		Reasoner reasoner = universe.reasoner(assumption);

		assertEquals(universe.bool(true),
				reasoner.simplify(universe.lessThanEquals(x, one)));
	}

	/**
	 * Integer division. true : 2(u/2) -> 2(u/2)
	 */
	@Test
	public void simplifyIntDivNo() {
		SymbolicExpression e = universe.multiply(universe.integer(2),
				universe.divide(u, universe.integer(2)));
		Reasoner reasoner = universe.reasoner(trueExpr);

		assertEquals(e, reasoner.simplify(e));
	}

	/**
	 * Integer division. true : (2u)/2 -> u
	 */
	@Test
	public void simplifyIntDivYes() {
		SymbolicExpression e = universe.divide(
				universe.multiply(universe.integer(2), u), universe.integer(2));

		assertEquals(u, e);
	}

	/**
	 * Integer modulus. true : (2u)%2 -> 0
	 */
	@Test
	public void simplifyIntMod() {
		SymbolicExpression e = universe.modulo(
				universe.multiply(universe.integer(2), u), universe.integer(2));

		assertEquals(universe.zeroInt(), e);
	} 
	
	@Test 
	public void simplyifyTrig() { 
		
	}
}
