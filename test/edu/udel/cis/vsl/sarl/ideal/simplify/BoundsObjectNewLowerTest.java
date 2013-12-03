/* Copyright 2013 Stephen F. Siegel, University of Delaware
 */
package edu.udel.cis.vsl.sarl.ideal.simplify;

import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.*;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Provides testing of the newLowerBound method, which
 * is used as one of three public constructors for a BoundsObject
 * 
 * @author danfried
 * 
 * @see BoundsObject
 * @see BoundsObject.newLowerBound
 *
 */
public class BoundsObjectNewLowerTest {

	/**
	 * Calls the setUp() method in CommonObjects to make use of consolidated SARL object 
	 * declarations and initializations for testing of "Simplify" module.
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
	 * Test of BoundsObject.newLowerBound(SymbolicExpression expression, 
	 * Number bound, boolean strict)
	 */
	@Test
	public void newLowerTest() {
		boundObj = BoundsObject.newLowerBound(x, num0, true);
		assertEquals(num0, boundObj.lower);
	}

}
