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

/**Provides testing of the enlargeUpper method of BoundsObject
 * 
 * @author danfried
 *
 *@see BoundsObject.enlargeUpper
 */
public class BoundsObjectEnlargeUpperTest {

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
	 * Provides testing that confirms the behavior that an infinate upper
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
	
	@Ignore
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
