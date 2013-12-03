/* Copyright 2013 Stephen F. Siegel, University of Delaware
 */
package edu.udel.cis.vsl.sarl.ideal.simplify;

import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.boundObj;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.num0;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.xInt;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**Provides testing coverage of the toString method for BoundsObject
 * 
 * @author danfried
 * 
 * @see BoundsObject.toString
 *
 */
public class BoundsObjectToStringTest {

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
	 * Tests BoundsObject.toString when both 
	 * upper and lower bounds are null (-inft and + infty)
	 */
	@Test
	public void nullToStringTest() {
		boundObj = BoundsObject.newLowerBound(xInt, null, true);
		assertEquals("-infty < xInt < +infty", boundObj.toString());
	}
	
	/**
	 * Tests BoundsObject.toString on a tight bounding (non-null upper and lower)
	 */
	@Test
	public void notNullToStringTest(){
		boundObj = BoundsObject.newTightBound(xInt, num0);
		assertEquals("0 <= xInt <= 0", boundObj.toString());
	}

}
