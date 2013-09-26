/* Copyright 2013 Stephen F. Siegel, University of Delaware
 */
package edu.udel.cis.vsl.sarl.ideal.simplify;

import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author danfried
 *
 */
public class BoundsObjectIsConsistentTest {

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
	 * Tests BoundsObject.isConsistent():
	 * coverage for False || True branch of
	 * if (lower == null || upper == null)
	 */
	@Test
	public void isConsistentUpperNullTest() {
		boundObj = BoundsObject.newLowerBound(xxy, num10000Int, true);
		assertTrue(boundObj.isConsistent());
	}
	
	/**
	 * Tests BoundsObject.isConsistent():
	 * coverage for True || False branch of
	 * if (lower == null || upper == null)
	 */
	@Test
	public void isConsistentLowerNullTest() {
		boundObj = BoundsObject.newUpperBound(xxy, num10000Int, true);
		assertTrue(boundObj.isConsistent());
	}
	
	/**
	 * Tests BoundsObject.isConsistent():
	 * coverage for False || False branch of
	 * if (lower == null || upper == null)
	 */
	@Test
	public void isConsistentBothNotNullTest() {
		boundObj = BoundsObject.newUpperBound(xxy, num10000Int, true);
		boundObj.restrictLower(numNeg2000, true);
		assertTrue(boundObj.isConsistent());
	}
	
	/**
	 * Tests isConsistent compare = factory.compare(lower, upper);
	 * if (compare > 0) when lowerBound > upperBound
	 */
	@Test
	public void isConsistentCompare0(){
		boundObj = BoundsObject.newUpperBound(xxy, num0, true);
		boundObj.restrictLower(num10000, true);
		boundObj.isConsistent();
		out.println(boundObj);
	}

}
