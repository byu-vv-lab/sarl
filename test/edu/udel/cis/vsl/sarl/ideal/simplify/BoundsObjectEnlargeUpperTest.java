/* Copyright 2013 Stephen F. Siegel, University of Delaware
 */
package edu.udel.cis.vsl.sarl.ideal.simplify;

import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.boundObj;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.boundObj2;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.boundObj3;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.num0;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.num3;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.num5;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.xInt;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.xpy;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.xy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.number.real.CommonInterval;

/**Provides testing of the enlargeUpper method 
 * (and call to it from enlargeTo) of BoundsObject
 * 
 * @author danfried
 *
 * @see CommonInterval.enlargeUpper
 * @see CommonInterval.enlargeTo
 */
public class BoundsObjectEnlargeUpperTest {

	/**
	 * Calls the setUp() method in CommonObjects to make use of consolidated SARL object 
	 * declarations and initializations for testing of "Simplify" module.
	 * 
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
	 * Provides testing that confirms the behavior that an infinite upper
	 * bound should not adopt a lower bound when "enlarge" methods are called
	 * 
	 * @see BoundsObject
	 */
	@Test
	public void enlargeUpperFromNullTest(){
		boundObj = BoundsObject.newUpperBound(xInt, null, true);
		boundObj2 = BoundsObject.newUpperBound(xInt, num0, true);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeTo(boundObj2);
		assertNotEquals(boundObj, boundObj2);
		boundObj.enlargeUpper(num0, true);
		assertEquals(boundObj, boundObj3);
		assertNotEquals(boundObj, boundObj2);
	}
	
	/**
	 * Testing on enlargeTo method of BoundsObject when
	 * initial boundage is not infinite, and an infinite upper bound
	 * is sought.
	 * 
	 * @see BoundsObject
	 */
	@Test
	public void enlargeToNullTest(){
		boundObj = BoundsObject.newUpperBound(xInt, num0, true);
		boundObj2 = BoundsObject.newUpperBound(xInt, null, true);
		boundObj3 = boundObj.clone();
		
		assertNotEquals(boundObj, boundObj2);
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeTo(boundObj2);
		assertNotEquals(boundObj, boundObj3);
		assertEquals(boundObj, boundObj2);
	}
	
	/**
	 * Tests enlargeUpper method of BoundsObject when a non-infinite upper bound
	 * is enlarged to an infinite upper bound.
	 */
	@Test
	public void enlargeUpperToNullTest(){
		boundObj = BoundsObject.newUpperBound(xInt, num0, true);
		boundObj3 = boundObj.clone();
		
		boundObj.enlargeUpper(null, true);
		assertNotEquals(boundObj, boundObj3);
	}
	
	/**
	 * Testing on BoundsObject.enlargeUpper when providing a 
	 * larger real rational bound with the same "true" strictness
	 */
	@Test
	public void enlargeUpperToLargerTrueTest(){
		boundObj = BoundsObject.newUpperBound(xpy, num0, true);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeUpper(num3, true);
		assertNotEquals(boundObj, boundObj3);
	}
	
	/**
	 * Testing on BoundsObject.enlargeUpper when providing a 
	 * larger real rational bound with the same "false" strictness
	 */
	@Test
	public void enlargeUpperToLargerFalseTest(){
		boundObj = BoundsObject.newUpperBound(xpy, num0, false);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeUpper(num3, false);;
		assertNotEquals(boundObj, boundObj3);
	}
	
	/**
	 * Testing on BoundsObject.enlargeUpper when providing a 
	 * larger real rational bound with true->false strictness
	 */
	@Test
	public void enlargeUpperToLargerTrueToFalseTest(){
		boundObj = BoundsObject.newUpperBound(xpy, num0, true);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeUpper(num3, false);;
		assertNotEquals(boundObj, boundObj3);
	}
	
	/**
	 * Testing on BoundsObject.enlargeUpper when providing a 
	 * larger real rational bound with false->true strictness
	 */
	@Test
	public void enlargeUpperToLargerFalseToTrueTest(){
		boundObj = BoundsObject.newUpperBound(xpy, num0, false);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeUpper(num3, true);;
		assertNotEquals(boundObj, boundObj3);
	}
	
	/**
	 * Testing on BoundsObject.enlargeUpper when attempting to provide a
	 * real rational number value as parameter for the new bound that is 
	 * less than the initial bound.  Both have a strictness of "true"
	 */
	@Test
	public void enlargeUpperToSmallerTrueTest(){
		boundObj = BoundsObject.newUpperBound(xpy, num5, true);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeUpper(num3, true);
		assertEquals(boundObj, boundObj3);
	}
	
	/**
	 * Testing on BoundsObject.enlargeUpper when attempting to provide a
	 * real rational number value as parameter for the new bound that is 
	 * less than the initial bound.  Both have a strictness of "false"
	 */
	@Test
	public void enlargeUpperToSmallerFalseTest(){
		boundObj = BoundsObject.newUpperBound(xpy, num5, false);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeUpper(num3, false);
		assertEquals(boundObj, boundObj3);
	}
	
	/**
	 * Testing on BoundsObject.enlargeUpper when attempting to provide a
	 * real rational number value as parameter for the new bound that is 
	 * less than the initial bound and strictness changes from 
	 * "True" to "False."
	 */
	@Test
	public void enlargeUpperToSmallerTrueFalseTest(){
		boundObj = BoundsObject.newUpperBound(xpy, num5, true);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeUpper(num3, false);
		assertEquals(boundObj, boundObj3);
	}
	
	/**
	 * Testing on BoundsObject.enlargeUpper when attempting to provide a
	 * real rational number value as parameter for the new bound that is 
	 * less than the initial bound and strictness changes from 
	 * "True" to "False."
	 */
	@Test
	public void enlargeUpperToSmallerFalseTrueTest(){
		boundObj = BoundsObject.newUpperBound(xpy, num5, false);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeUpper(num3, true);
		assertEquals(boundObj, boundObj3);
	}
	
	/**
	 * Testing on BoundsObject.enlargeUpper when attempting to pass
	 * the same rational real number and "True" strictness
	 * as parameters.
	 */
	@Test
	public void enlargeUpperToSameTrueTest(){
		boundObj = BoundsObject.newUpperBound(xy, num3, true);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeUpper(num3, true);
		assertEquals(boundObj, boundObj3);
	}
	
	/**
	 * Testing on BoundsObject.enlargeUpper when attempting to pass
	 * the same rational real number and "False" strictness
	 * as parameters.
	 */
	@Test
	public void enlargeUpperToSameFalseTest(){
		boundObj = BoundsObject.newUpperBound(xy, num3, false);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeUpper(num3, false);
		assertEquals(boundObj, boundObj3);
	}
	
	/**
	 * Testing on BoundsObject.enlargeUpper when attempting to pass
	 * the same rational real number and strictness (true->false)
	 * as parameters.  The bound should remain the same, but the
	 * strictness of the upper bound should change.
	 */
	@Test
	public void enlargeUpperToSameTrueFalseTest(){
		boundObj = BoundsObject.newUpperBound(xy, num3, true);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeUpper(num3, false);
		assertNotEquals(boundObj, boundObj3);
		assertFalse(boundObj.strictUpper);
	}
	
	/**
	 * Testing on BoundsObject.enlargeUpper when attempting to pass
	 * the same rational real number and strictness (false->true)
	 * as parameters.  Both the value and strictness of the upper
	 * bound are expected to remain the same.
	 */
	@Test
	public void enlargeUpperToSameFalseTrueTest(){
		boundObj = BoundsObject.newUpperBound(xy, num3, false);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeUpper(num3, true);
		assertEquals(boundObj, boundObj3);
		assertFalse(boundObj.strictUpper);
	}
}
