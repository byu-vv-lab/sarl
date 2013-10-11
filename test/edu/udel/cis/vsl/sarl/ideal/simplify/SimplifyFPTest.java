/* Copyright 2013 Stephen F. Siegel, University of Delaware
 */
package edu.udel.cis.vsl.sarl.ideal.simplify;

import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author danfried
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
		out.println(idealSimplifier.simplifyExpression(xSqrLess1));
		out.println(idealSimplifier.apply(xSqrP1));
		//out.println("xx - 1 :  " + xSqrLess1.toString());
		//IdealSimplifier.
	}

}
