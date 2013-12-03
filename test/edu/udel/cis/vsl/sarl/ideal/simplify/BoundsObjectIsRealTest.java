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
 * Provides testing of the isReal method of BoundsObject
 * 
 * @author danfried
 * 
 * @see BoundsObject.isReal
 *
 */
public class BoundsObjectIsRealTest {

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
	 * Tests True branch of isReal()
	 */
	@Test
	public void isRealTest() {
		boundObj = BoundsObject.newTightBound(xy, num0);
		assertTrue(boundObj.isReal());
	}
	
	/**
	 * Tests False branch of isReal()
	 */
	@Test
	public void isNotRealTest(){
		boundObj = BoundsObject.newTightBound(xyInt, num0Int);
		assertFalse(boundObj.isReal());
	}

}
