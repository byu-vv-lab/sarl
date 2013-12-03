/* Copyright 2013 Stephen F. Siegel, University of Delaware
 */
package edu.udel.cis.vsl.sarl.ideal.simplify;

import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.boundObj;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.boundObj2;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.num0;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.x;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**Provides testing of the clone method of BoundsObject
 * 
 * @author danfried
 *
 *@see BoundsObject.clone
 */
public class BoundsObjectCloneTest {

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

	@Test
	public void cloneTest() {
		boundObj = BoundsObject.newLowerBound(x, num0, true);
		boundObj2 = boundObj.clone();
		assertEquals(boundObj, boundObj2);
	}

}
