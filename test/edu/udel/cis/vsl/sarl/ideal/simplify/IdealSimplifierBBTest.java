/*
 * Copyright 2013 Stephen F. Siegel, University of Delaware
 */
package edu.udel.cis.vsl.sarl.ideal.simplify;

import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.assumption;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.bigMixedXYTermPoly;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.idealSimplifier;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.idealSimplifierFactory;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.out;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.preUniv;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.rat0;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.rat1;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.ratNeg1;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.threeX4th;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.trueExpr;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.x;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.x4th;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.xNE;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.yNE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

/**
 * Set of tests on IdealSimplifier based about assigning values to single
 * variables and a polynomial, and then confirming expected full and reduced
 * contexts.
 * 
 * @author danfried
 * 
 */
public class IdealSimplifierBBTest {

	/**
	 * Calls the setUp() method in CommonObjects to make use of consolidated
	 * SARL object declarations and initializations for testing of "Simplify"
	 * module. Also initialized objects in the CommonObjects class that are used
	 * often and therefore not given an initial value.
	 * 
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		CommonObjects.setUp();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Simple test to make sure simplifier has agreement between full and
	 * reduced context when variable, x, is set to 0.0
	 */
	@Test
	public void xGreater0Test() {
		assumption = preUniv.lessThan(rat0, xNE);
		idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
		// out.println(idealSimplifier.simplifyExpression(bigMixedXYTermPoly));
		assertEquals("0 < x", idealSimplifier.getReducedContext().toString());
		// out.println("full: " + idealSimplifier.getFullContext() +
		// " reduced: " + idealSimplifier.getReducedContext());
		// out.println(idealSimplifier.apply(bigMixedXYTermPoly));
		assertEquals(idealSimplifier.getReducedContext(),
				idealSimplifier.getFullContext());
	}

	/**
	 * This test involves the use of two separate assumptions that are
	 * compounded and applied to a mixed-term polynomial to test the
	 * simplification on large-term symbolic expressions.
	 */
	@Test
	public void twoStagePolyTest() {
		// first assumption: x == -1.0
		assumption = preUniv.equals(ratNeg1, x);
		idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
		out.println(idealSimplifier.apply(bigMixedXYTermPoly));
		SymbolicExpression noX = idealSimplifier.apply(bigMixedXYTermPoly); // intermediary
																			// symbolic
																			// expression

		// second assumption: y == 1.0
		assumption = preUniv.equals(rat1, yNE);
		idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
		out.println(idealSimplifier.apply(noX));
		// 0^3 should = 0...
		assertEquals(rat0.type(), idealSimplifier.apply(noX).type());
		assertEquals(rat0, idealSimplifier.apply(noX));
		out.println(idealSimplifier.getFullContext());
		out.println(idealSimplifier.getReducedContext());
	}

	/**
	 * Test on idealsimplifier's ability to determine that a reducedContext is
	 * true due to a value being solvable/ able to be determined
	 */
	@Test
	public void simplifySolvableTest() {
		assumption = preUniv.equals(rat0, yNE);
		idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
		// out.println("here: " + idealSimplifier.getFullContext());
		// out.println(idealSimplifier.getReducedContext());
		assertNotEquals(idealSimplifier.getFullContext(),
				idealSimplifier.getReducedContext());
		assertEquals(trueExpr, idealSimplifier.getReducedContext());
	}

	/**
	 * Tests idealSimplifer's ability to reduce a single-term variable of order
	 * > 1, when equal to 0
	 */
	@Test
	public void singlePowerTermSimplifyTest() {
		assumption = preUniv.equals(rat0, x4th);
		idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
		out.println("here: " + idealSimplifier.getFullContext());
		out.println(idealSimplifier.getReducedContext());
		assertEquals(rat0, idealSimplifier.apply(threeX4th));
		// x^4 == 0 should be reduced to x == 0
		assertNotEquals(idealSimplifier.getFullContext(),
				idealSimplifier.getReducedContext());
	}
}
