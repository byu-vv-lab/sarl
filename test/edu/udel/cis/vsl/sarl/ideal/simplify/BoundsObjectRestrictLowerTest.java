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
		assertEquals(num0, boundObj.lower);
		boundObj.restrictLower(null, true);
		assertNotNull(boundObj.lower);
		assertEquals(num0, boundObj.lower);
	}
	
	/**
	 * Tests BoundsObject.restrictLower when a valid bound is given as parameter
	 */
	@Test
	public void restrictLowerNotNullTest() {
		boundObj = BoundsObject.newLowerBound(xpy, num0, true);
		assertEquals(num0, boundObj.lower);
		boundObj.restrictLower(numNeg2000, true);
		assertEquals(num0, boundObj.lower); //bound should not change, as numNeg2000 < num0
		assertEquals(1, boundObj.lower.compareTo(numNeg2000));
	}
	
	/**
	 * Tests BoundsObject.restrictLower when a valid bound is given as parameter 
	 * to replace current null value
	 */
	@Test
	public void restrictLowerFromNullTest() {
		boundObj = BoundsObject.newUpperBound(xpy, num0, true);
		assertNull(boundObj.lower);
		boundObj.restrictLower(numNeg2000, true);
		assertNotNull(boundObj.lower);
		assertEquals(numNeg2000, boundObj.lower);
	}
	
	/**
	 * Tests BoundsObject.restrictLower when a valid bound is given as parameter 
	 * that is of a lesser absolute value than the existing lower bound
	 */
	@Test
	public void restrictLowererTest() {
		boundObj = BoundsObject.newLowerBound(xpy, numNeg2000, true);
		assertEquals(numNeg2000, boundObj.lower);
		boundObj.restrictLower(num0, true);
		assertEquals(num0, boundObj.lower);
		//out.println(boundObj);
	}
	
	/**
	 * Tests BoundsObject.restrictLower when a valid bound is given as parameter 
	 * that equal to the existing lower bound, but of differing strictness (True -> False)
	 */
	@Test
	public void restrictLowerEqualTest() {
		boundObj = BoundsObject.newLowerBound(xpy, numNeg2000, true);
		assertEquals(true, boundObj.strictLower);
		boundObj.restrictLower(numNeg2000, false);
		assertEquals(numNeg2000, boundObj.lower);
		assertTrue(boundObj.strictLower);
		//out.println(boundObj.strictLower);
	}
	
	/**
	 * Tests BoundsObject.restrictLower when a valid bound is given as parameter 
	 * that equal to the existing lower bound, but of differing strictness (False -> True)
	 */
	@Test
	public void restrictLowerEqualTest2() {
		boundObj = BoundsObject.newLowerBound(xpy, numNeg2000, false);
		assertFalse(boundObj.strictLower);
		boundObj.restrictLower(numNeg2000, true);
		assertEquals(numNeg2000, boundObj.lower);
		assertTrue(boundObj.strictLower);
		//out.println(boundObj.strictLower);
	}
	
	/**
	 * Tests BoundsObject.restrictLower when a valid bound is given as parameter 
	 * that equal to the existing lower bound, with the same strictness
	 */
	@Test
	public void restrictLowerEqualTest3() {
		boundObj = BoundsObject.newLowerBound(xpy, numNeg2000, true);
		assertTrue(boundObj.strictLower);
		boundObj.restrictLower(numNeg2000, true);
		assertTrue(boundObj.strictLower);
		//out.println(boundObj);
	}
	
	/**
	 * Tests BoundsObject.restrictLower when a valid bound is given as parameter 
	 * that equal to the existing lower bound, with the same strictness
	 */
	@Test
	public void restrictLowerEqualTest4() {
		boundObj = BoundsObject.newLowerBound(xpy, numNeg2000, false);
		assertFalse(boundObj.strictLower);
		boundObj.restrictLower(numNeg2000, false);
		assertFalse(boundObj.strictLower);
		//out.println(boundObj.strictLower);
	}

}
