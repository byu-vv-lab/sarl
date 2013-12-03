/* Copyright 2013 Stephen F. Siegel, University of Delaware
 */
package edu.udel.cis.vsl.sarl.ideal.simplify;

import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.assumption;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.idealSimplifier;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.idealSimplifierFactory;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.int0;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.preUniv;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.xInt;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.xSqrLess1;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.xSqrP1;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Provides testing on two methods of simplification; apply and simplifyExpression
 * in IdealSimplifier.
 * 
 * @author danfried
 * 
 * @see IdealSimplifier
 * @see IdealSimplifier.apply
 * @see IdealSimplifier.simplifyExpression
 *
 */
public class SimplifyFPTest {

	/**
	 * Calls the setUp() method in CommonObjects to make use of consolidated SARL object 
	 * declarations and initializations for testing of "Simplify" module.
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		CommonObjects.setUp();
		assumption = preUniv.lessThan(xInt, int0);
				//preUniv.equals(preUniv.multiply(rat5,x), preUniv.multiply(y, y));
		idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
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
	 * Testing of polynomial simplification where factoring results in non-real terms
	 */
	@Test
	public void assumptionTest() {
		assertEquals("xInt^2+-1", idealSimplifier.simplifyExpression(xSqrLess1).toString());
		assertEquals("xInt^2+1", idealSimplifier.apply(xSqrP1).toString());
		//out.println("xx - 1 :  " + xSqrLess1.toString());
		//IdealSimplifier.
	}

}
