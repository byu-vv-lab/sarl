/* Copyright 2013 Stephen F. Siegel, University of Delaware
 */
package edu.udel.cis.vsl.sarl.ideal.simplify;

import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

/**
 * @author danfried
 *
 */
public class IdealSimplifierBBTest {

	/**
	 * Calls the setUp() method in CommonObjects to make use of consolidated SARL object 
	 * declarations and initializations for testing of "Simplify" module.
	 * Also initialized objects in the CommonObjects class that
	 * are used often and therefore not given an initial value.
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

	@Test
	public void xGreater0Test() {
		assumption = preUniv.lessThan(rat0, xNE);
		idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
		out.println(idealSimplifier.simplifyExpression(bigMixedXYTermPoly));
		assertEquals("0 < x", idealSimplifier.getReducedContext().toString());
		assertEquals(idealSimplifier.getReducedContext(), idealSimplifier.getFullContext());
		
		assumption = preUniv.equals(ratNeg1, xNE);
		idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
		out.println(idealSimplifier.apply(bigMixedXYTermPoly));
		SymbolicExpression noX = idealSimplifier.apply(bigMixedXYTermPoly);
		
		assumption = preUniv.equals(rat1, yNE);
		idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
		out.println(idealSimplifier.apply(noX));
		//0^3 should = 0...
		assertEquals(rat0, idealSimplifier.apply(noX));
		out.println(idealSimplifier.getFullContext());
		out.println(idealSimplifier.getReducedContext());
		//unsure if a numeric expression should reduce to a boolean because of having a binary value
		//assertNotEquals(trueExpr, idealSimplifier.getReducedContext());
		
		assumption = preUniv.equals(rat0, yNE);
		idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
		out.println(idealSimplifier.getFullContext());
		out.println(idealSimplifier.getReducedContext());
		
		
		assumption = preUniv.equals(rat0, x4th);
		idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
		out.println(idealSimplifier.getFullContext());
		out.println(idealSimplifier.getReducedContext());
		assertEquals(rat0, idealSimplifier.apply(threeX4th));
	}

}
