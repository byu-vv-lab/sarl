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

/**Provides testing of the enlargeLower method 
 * (and call to it from enlargeTo) of BoundsObject
 * 
 * @author danfried
 *
 * @see BoundsObject.enlargeLower
 * @see BoundsObject.enlargeTo
 */
public class BoundsObjectEnlargeLowerTest {

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
	 * Provides testing that confirms the behavior that an infinite lower
	 * bound should not adopt a greater bound when "enlarge" methods are called
	 * 
	 * @see BoundsObject
	 */
	@Test
	public void enlargeLowerFromNullTest(){
		boundObj = BoundsObject.newLowerBound(xInt, null, true);
		boundObj2 = BoundsObject.newLowerBound(xInt, num0, true);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeTo(boundObj2);
		assertNotEquals(boundObj, boundObj2);
		boundObj.enlargeLower(num0, true);
		assertEquals(boundObj, boundObj3);
		assertNotEquals(boundObj, boundObj2);
	}
	
	/**
	 * Testing on enlargeTo method of BoundsObject when
	 * initial boundage is not infinite, and an infinite lower bound
	 * is sought.
	 * 
	 * @see BoundsObject
	 */
	@Test
	public void enlargeToNullTest(){
		boundObj = BoundsObject.newLowerBound(xInt, num0, true);
		boundObj2 = BoundsObject.newLowerBound(xInt, null, true);
		boundObj3 = boundObj.clone();
		
		assertNotEquals(boundObj, boundObj2);
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeTo(boundObj2);
		assertNotEquals(boundObj, boundObj3);
		assertEquals(boundObj, boundObj2);
	}
	
	/**
	 * Tests enlargeUpper method of BoundsObject when a non-infinite lower bound
	 * is enlarged to an infinite lower bound.
	 */
	@Test
	public void enlargeLowerNullTest(){
		boundObj = BoundsObject.newLowerBound(xInt, num0, true);
		boundObj3 = boundObj.clone();
		
		boundObj.enlargeLower(null, true);
		assertNotEquals(boundObj, boundObj3);
	}
	
	/**
	 * Testing on BoundsObject.enlargeLower when providing a 
	 * lesser real rational bound with the same "true" strictness
	 */
	@Test
	public void enlargeLowerToLesserTrueTest(){
		boundObj = BoundsObject.newLowerBound(xpy, num5, true);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeLower(num0, true);
		assertNotEquals(boundObj, boundObj3);
	}
	
	/**
	 * Testing on BoundsObject.enlargeLower when providing a 
	 * lesser real rational bound with the same "false" strictness
	 */
	@Test
	public void enlargeLowerToLesserFalseTest(){
		boundObj = BoundsObject.newLowerBound(xpy, num5, false);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeLower(num0, false);;
		assertNotEquals(boundObj, boundObj3);
	}
	
	/**
	 * Testing on BoundsObject.enlargeLower when providing a 
	 * lesser real rational bound with true->false strictness
	 */
	@Test
	public void enlargeLowerToLesserTrueToFalseTest(){
		boundObj = BoundsObject.newLowerBound(xpy, num5, true);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeLower(num0, false);;
		assertNotEquals(boundObj, boundObj3);
	}
	
	/**
	 * Testing on BoundsObject.enlargeLower when providing a 
	 * lesser real rational bound with false->true strictness
	 */
	@Test
	public void enlargeLowerToLesserFalseToTrueTest(){
		boundObj = BoundsObject.newLowerBound(xpy, num5, false);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeLower(num0, true);;
		assertNotEquals(boundObj, boundObj3);
	}
	
	/**
	 * Testing on BoundsObject.enlargeLower when attempting to provide a
	 * real rational number value as parameter for the new bound that is 
	 * larger than the initial bound.  Both have a strictness of "true"
	 */
	@Test
	public void enlargeLowerToLargerTrueTest(){
		boundObj = BoundsObject.newLowerBound(xpy, num0, true);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeLower(num5, true);
		assertEquals(boundObj, boundObj3);
	}
	
	/**
	 * Testing on BoundsObject.enlargeLower when attempting to provide a
	 * real rational number value as parameter for the new bound that is 
	 * larger than the initial bound.  Both have a strictness of "false"
	 */
	@Test
	public void enlargeLowerToLargerFalseTest(){
		boundObj = BoundsObject.newLowerBound(xpy, num0, false);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeLower(num5, false);
		assertEquals(boundObj, boundObj3);
	}
	
	/**
	 * Testing on BoundsObject.enlargeLower when attempting to provide a
	 * real rational number value as parameter for the new bound that is 
	 * larger than the initial bound and strictness changes from 
	 * "True" to "False."
	 */
	@Test
	public void enlargeLowerToLargerTrueFalseTest(){
		boundObj = BoundsObject.newLowerBound(xpy, num0, true);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeLower(num3, false);
		assertEquals(boundObj, boundObj3);
	}
	
	/**
	 * Testing on BoundsObject.enlargeLower when attempting to provide a
	 * real rational number value as parameter for the new bound that is 
	 * larger than the initial bound and strictness changes from 
	 * "True" to "False."
	 */
	@Test
	public void enlargeLowerToLargerFalseTrueTest(){
		boundObj = BoundsObject.newLowerBound(xpy, num0, false);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeLower(num5, true);
		assertEquals(boundObj, boundObj3);
	}
	
	/**
	 * Testing on BoundsObject.enlargeLower when attempting to pass
	 * the same rational real number and "True" strictness
	 * as parameters.
	 */
	@Test
	public void enlargeLowerToSameTrueTest(){
		boundObj = BoundsObject.newLowerBound(xy, num3, true);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeLower(num3, true);
		assertEquals(boundObj, boundObj3);
	}
	
	/**
	 * Testing on BoundsObject.enlargeLower when attempting to pass
	 * the same rational real number and "False" strictness
	 * as parameters.
	 */
	@Test
	public void enlargeLowerToSameFalseTest(){
		boundObj = BoundsObject.newLowerBound(xy, num3, false);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeLower(num3, false);
		assertEquals(boundObj, boundObj3);
	}
	
	/**
	 * Testing on BoundsObject.enlargeLower when attempting to pass
	 * the same rational real number and strictness (true->false)
	 * as parameters.  The bound should remain the same, but the
	 * strictness of the lower bound should change.
	 */
	@Test
	public void enlargeLowerToSameTrueFalseTest(){
		boundObj = BoundsObject.newLowerBound(xy, num3, true);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeLower(num3, false);
		assertNotEquals(boundObj, boundObj3);
		assertFalse(boundObj.strictLower);
	}
	
	/**
	 * Testing on BoundsObject.enlargeLower when attempting to pass
	 * the same rational real number and strictness (false->true)
	 * as parameters.  Both the value and strictness of the lower
	 * bound is expected to remain the same.
	 */
	@Test
	public void enlargeLowerToSameFalseTrueTest(){
		boundObj = BoundsObject.newLowerBound(xy, num3, false);
		boundObj3 = boundObj.clone();
		
		assertEquals(boundObj, boundObj3);
		boundObj.enlargeLower(num3, true);
		assertEquals(boundObj, boundObj3);
		assertFalse(boundObj.strictLower);
	}

}
