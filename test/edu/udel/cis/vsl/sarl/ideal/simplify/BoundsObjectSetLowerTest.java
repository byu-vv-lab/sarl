/* Copyright 2013 Stephen F. Siegel, University of Delaware
 */
package edu.udel.cis.vsl.sarl.ideal.simplify;

import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.boundObj;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.num0;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.num0Int;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.num10000Int;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.x;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.xInt;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;

/**
 * Provides testing on the lower method of BoundsObject,
 * which is a getter method for returning the current lower
 * bound of a BoundsObject
 * 
 * @author danfried
 * 
 * @see BoundsObject
 * @see BoundsObject.lower()
 *
 */
public class BoundsObjectSetLowerTest {

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
	 * Tests BoundsObject.setLower
	 * coverage of last two lines in method, as well as,
	 * False, False for "if (bound == null && !strict)"
	 */
	@Test
	public void setLowerNoIfTest() {
		boundObj = BoundsObject.newLowerBound(x, num10000Int, true);
	}
	
	/**
	 * Tests "if (bound == null && !strict)" for 
	 * True, True
	 */
	@Test(expected = RuntimeException.class)
	public void firstIfStatementTest1(){
		boundObj = BoundsObject.newLowerBound(x, null, false);
	}
	
	/**
	 * Tests "if (bound == null && !strict)" for 
	 * True, False
	 * Also tests "if (isIntegral() && bound != null" for
	 * False, False
	 */
	@Test
	public void firstIfStatementTest2(){
		boundObj = BoundsObject.newLowerBound(x, null, true);
		assertFalse(boundObj.isIntegral());
	}
	
	/**
	 * Tests "if (isIntegral() && bound != null" for
	 * True, False
	 */
	@Test
	public void integralAndNullTest(){
		boundObj = BoundsObject.newLowerBound(xInt, null, true);
		assertTrue(boundObj.isIntegral());
		assertNull(boundObj.lower());
	}

	/**
	 * Tests "if (isIntegral() && bound != null" for
	 * True, True
	 */
	@Test
	public void integralAndNotNullTest(){
		boundObj = BoundsObject.newLowerBound(xInt, num0Int, true);
		assertTrue(boundObj.isIntegral());
		assertNotNull(boundObj.lower());
	}
	
	/**
	 * Tests "&& (strict || !(bound instanceof IntegerNumber))" for
	 * False (False || False)
	 */
	@Test
	public void strictOrNotIntegerTest(){
		boundObj = BoundsObject.newLowerBound(xInt, num0Int, false);
		assertFalse(boundObj.strictLower());
		assertFalse(!(boundObj.lower() instanceof IntegerNumber));
	}
	
	/**
	 * Tests "&& (strict || !(bound instanceof IntegerNumber))" for
	 * remaining cases
	 */
	@Test
	public void strictAndNotIntegerTest(){
		boundObj = BoundsObject.newLowerBound(xInt, num0, false);
		assertFalse(boundObj.strictLower());
		assertFalse(!(boundObj.lower() instanceof IntegerNumber));
	}
	
	
	
	
	
	
	
	
	
	
}
