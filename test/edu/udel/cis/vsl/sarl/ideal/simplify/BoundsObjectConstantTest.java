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
public class BoundsObjectConstantTest {

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
	 * Tests BoundsObject.constant() on
	 * if (lower != null && upper != null && lower.equals(upper)
	 *			&& !strictLower && !strictUpper)
	 * When True
	 */
	@Test
	public void constantTest() {
		boundObj = BoundsObject.newTightBound(xpy, num0);
		assertEquals(num0, boundObj.constant());
	}
	
	/**
	 * Tests BoundsObject.constant() on
	 * if (lower != null && upper != null && lower.equals(upper)
	 *			&& !strictLower && !strictUpper)
	 * When BoundsObject is not tightly bound, and is bounded below
	 */
	@Test
	public void constantNullTest(){
		boundObj = BoundsObject.newLowerBound(xpy, num0, false);
		assertNull(boundObj.constant());
	}
	
	/**
	 * Tests BoundsObject.constant() on
	 * if (lower != null && upper != null && lower.equals(upper)
	 *			&& !strictLower && !strictUpper)
	 * When BoundsObject is not tightly bound, and is bounded above
	 */
	@Test
	public void constantNullTest2(){
		boundObj = BoundsObject.newUpperBound(xpy, num0, false);
		assertNull(boundObj.constant());
	}
	
	/**
	 * Tests BoundsObject.constant() on
	 * if (lower != null && upper != null && lower.equals(upper)
	 *			&& !strictLower && !strictUpper)
	 * When BoundsObject is not tightly bound, and is bounded above and below by different numbers
	 */
	@Test
	public void constantNotEqualTest(){
		boundObj = BoundsObject.newLowerBound(xpy, num0, false);
		boundObj.restrictUpper(num10000, true);
		//out.println(boundObj);
		assertNull(boundObj.constant());
	}
	
	/**
	 * Tests BoundsObject.constant() on
	 * if (lower != null && upper != null && lower.equals(upper)
	 *			&& !strictLower && !strictUpper)
	 * When BoundsObject is tightly bound, and the bounds are both strict
	 */
	@Test
	public void constantEqualStrictTest(){
		boundObj = BoundsObject.newUpperBound(xpy, num10000, true);
		boundObj.restrictLower(num10000, true);
		assertNull(boundObj.constant());
	}
	
	/**
	 * Tests BoundsObject.constant() on
	 * if (lower != null && upper != null && lower.equals(upper)
	 *			&& !strictLower && !strictUpper)
	 * When BoundsObject is tightly bound, and the bounds are both strict
	 */
	@Test
	public void constantEqualMixedStrictTest(){
		boundObj = BoundsObject.newUpperBound(xpy, num10000, true);
		boundObj.restrictLower(num10000, false);
		assertNull(boundObj.constant());
	}

}
