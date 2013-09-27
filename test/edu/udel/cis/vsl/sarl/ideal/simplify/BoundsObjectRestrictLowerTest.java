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
public class BoundsObjectRestrictLowerTest {

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
	 * Tests BoundsObject.restrictLower when a null bound is given as parameter
	 */
	@Test
	public void restrictLowerNullTest() {
		boundObj = BoundsObject.newLowerBound(xpy, num0, true);
		boundObj.restrictLower(null, true);
	}
	
	/**
	 * Tests BoundsObject.restrictLower when a valid bound is given as parameter
	 */
	@Test
	public void restrictLowerNotNullTest() {
		boundObj = BoundsObject.newLowerBound(xpy, num0, true);
		boundObj.restrictLower(numNeg2000, true);
	}
	
	/**
	 * Tests BoundsObject.restrictLower when a valid bound is given as parameter 
	 * to replace current null value
	 */
	@Test
	public void restrictLowerFromNullTest() {
		boundObj = BoundsObject.newUpperBound(xpy, num0, true);
		boundObj.restrictLower(numNeg2000, true);
	}
	
	/**
	 * Tests BoundsObject.restrictLower when a valid bound is given as parameter 
	 * that is of a lesser absolute value than the existing lower bound
	 */
	@Test
	public void restrictLowererTest() {
		boundObj = BoundsObject.newLowerBound(xpy, numNeg2000, true);
		boundObj.restrictLower(num0, true);
		//out.println(boundObj);
	}
	
	/**
	 * Tests BoundsObject.restrictLower when a valid bound is given as parameter 
	 * that equal to the existing lower bound, but of differing strictness (True -> False)
	 */
	@Test
	public void restrictLowerEqualTest() {
		boundObj = BoundsObject.newLowerBound(xpy, numNeg2000, true);
		boundObj.restrictLower(numNeg2000, false);
		//out.println(boundObj);
	}
	
	/**
	 * Tests BoundsObject.restrictLower when a valid bound is given as parameter 
	 * that equal to the existing lower bound, but of differing strictness (False -> True)
	 */
	@Test
	public void restrictLowerEqualTest2() {
		boundObj = BoundsObject.newLowerBound(xpy, numNeg2000, false);
		boundObj.restrictLower(numNeg2000, true);
		//out.println(boundObj);
	}
	
	/**
	 * Tests BoundsObject.restrictLower when a valid bound is given as parameter 
	 * that equal to the existing lower bound, with the same strictness
	 */
	@Test
	public void restrictLowerEqualTest3() {
		boundObj = BoundsObject.newLowerBound(xpy, numNeg2000, true);
		boundObj.restrictLower(numNeg2000, true);
		//out.println(boundObj);
	}

}
