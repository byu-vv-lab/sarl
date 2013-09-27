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
public class BoundsObjectRestrictUpperTest {

	/**
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
	 * Tests BoundsObject.restrictUpper when a null bound is given as parameter
	 */
	@Test
	public void restrictUpperNullTest() {
		boundObj = BoundsObject.newUpperBound(xpy, num0, true);
		boundObj.restrictUpper(null, true);
	}
	
	/**
	 * Tests BoundsObject.restrictUpper when a valid bound is given as parameter
	 */
	@Test
	public void restrictUpperNotNullTest() {
		boundObj = BoundsObject.newUpperBound(xpy, num0, true);
		boundObj.restrictUpper(numNeg2000, true);
	}
	
	/**
	 * Tests BoundsObject.restrictUpper when a valid bound is given as parameter 
	 * to replace current null value
	 */
	@Test
	public void restrictUpperFromNullTest() {
		boundObj = BoundsObject.newLowerBound(xpy, numNeg2000, true);
		boundObj.restrictUpper(num0, true);
	}
	
	/**
	 * Tests BoundsObject.restrictUpper when a valid bound is given as parameter 
	 * that is of a lesser absolute value than the existing lower bound
	 */
	@Test
	public void restrictUppererTest() {
		boundObj = BoundsObject.newUpperBound(xpy, num10000, true);
		boundObj.restrictUpper(num0, true);
		//out.println(boundObj);
	}
	
	/**
	 * Tests BoundsObject.restrictUpper when a valid bound is given as parameter 
	 * that equal to the existing upper bound, but of differing strictness (True -> False)
	 */
	@Test
	public void restrictUpperEqualTest() {
		boundObj = BoundsObject.newUpperBound(xpy, numNeg2000, true);
		boundObj.restrictUpper(numNeg2000, false);
		//out.println(boundObj);
	}
	
	/**
	 * Tests BoundsObject.restrictUpper when a valid bound is given as parameter 
	 * that equal to the existing upper bound, but of differing strictness (False -> True)
	 */
	@Test
	public void restrictUpperEqualTest2() {
		boundObj = BoundsObject.newUpperBound(xpy, numNeg2000, false);
		boundObj.restrictUpper(numNeg2000, true);
		//out.println(boundObj);
	}
	
	/**
	 * Tests BoundsObject.restrictUpper when a valid bound is given as parameter 
	 * that equal to the existing upper bound, with the same strictness
	 */
	@Test
	public void restrictUpperEqualTest3() {
		boundObj = BoundsObject.newUpperBound(xpy, numNeg2000, true);
		boundObj.restrictUpper(numNeg2000, true);
		//out.println(boundObj);
	}
	
	/**
	 * Tests BoundsObject.restrictUpper when a valid bound is given as parameter 
	 * that less than the existing upper bound, with the same strictness
	 */
	@Test
	public void restrictUpperEqualTest4() {
		boundObj = BoundsObject.newUpperBound(xpy, numNeg2000, false);
		boundObj.restrictUpper(num0, false);
		//out.println(boundObj);
	}

}
