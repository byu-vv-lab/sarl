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
public class BoundsObjectEqualsTest {

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
	 * Testing of BoundsObject.equals method,
	 * on the basis of dissimilar object types.
	 * Provides coverage of "if (object instanceof BoundsObject)"
	 * conditional statement for False.
	 */
	@Test
	public void notEqualByTypeTest() {
		boundObj = BoundsObject.newTightBound(x, num0);
		boundObj.equals(xeq5);
		assertNotEquals(boundObj, xeq5);
	}
	
	/**
	 * Testing of BoundsObject.equals method,
	 * on the basis of equivalent BoundsObjects.
	 * Provides coverage of "if (object instanceof BoundsObject)"
	 * conditional statement for True.
	 */
	@Test
	public void equalsTest(){
		boundObj = BoundsObject.newTightBound(x, num0);
		boundObj2 = BoundsObject.newTightBound(x, num0);
		boundObj.equals(boundObj2);
		assertEquals(boundObj, boundObj2);
	}
	
	/**
	 * Testing of BoundsObject.equals method:
	 * Provides coverage of False branch for
	 * "expression.equals(that.expression)"
	 */
	@Test
	public void unequalExpressionTest(){
		boundObj = BoundsObject.newTightBound(x, num0);
		boundObj2 = BoundsObject.newTightBound(y, num10000);
		boundObj.equals(boundObj2);
		assertNotEquals(boundObj.symbolicConstant(), boundObj2.symbolicConstant());
	}
	
	/**
	 * Testing of BoundsObject.equals method:
	 * Provides coverage of True branch for 
	 * ((upper == null && that.upper == null)
	 */
	@Test
	public void equalsNullTest(){
		boundObj = BoundsObject.newUpperBound(x, null, true);
		boundObj2 = BoundsObject.newUpperBound(x, null, true);
		boundObj.equals(boundObj2);
		assertEquals(boundObj.upper(), boundObj2.upper());
	}
	//TODO: Inspect for bug in upper/lower equals when .equals is called on a boundsobject with a null bound....
	/**
	 * Testing of BoundsObject.equals method:
	 * Provides coverage of False branch for 
	 * ((upper == null && that.upper == null)
	 * by providing one null upper bound and 
	 * one non-null upper bound.
	 */
	@Test
	public void equalsMixedNullTest(){
		//out.println("FAIL HERE:");
		boundObj = BoundsObject.newUpperBound(x, null, true);
		boundObj2 = BoundsObject.newUpperBound(x, num0, true);
		boundObj.equals(boundObj);
		boundObj2.equals(boundObj2);
		//out.println("bO1.upper: " + boundObj.upper);
		//out.println("bO2.upper: " + boundObj2.upper);
		boundObj.equals(boundObj2);
		assertNull(boundObj.upper());
		assertNotNull(boundObj2.upper());
		assertNotEquals(boundObj.upper(), boundObj2.upper());
	}
	
	/**
	 * Testing of BoundsObject.equals method:
	 * Provides coverage of True branch for
	 * "upper.equals(that.upper))"
	 * and False branch of "strictUpper == that.strictUpper"
	 */
	@Test
	public void upperNotEqualsTest(){
		boundObj = BoundsObject.newUpperBound(xpy, num10000, true);
		boundObj2 = BoundsObject.newUpperBound(xpy, num10000, false);
		boundObj.equals(boundObj2);
		assertEquals(boundObj.upper(), boundObj2.upper());
	}
	
	/**
	 * Testing of BoundsObject.equals method:
	 * Provides coverage of False branch for
	 * "upper.equals(that.upper))"
	 * and False branch of "strictUpper == that.strictUpper"
	 */
	@Test
	public void upperEqualsTest(){
		boundObj = BoundsObject.newUpperBound(xpy, num10pt5, true);
		boundObj2 = BoundsObject.newUpperBound(xpy, num0, false);
		boundObj.equals(boundObj2);
		assertNotEquals(boundObj.upper(), boundObj2.upper());
	}
	
	//lower-versioned tests new...
	//
	//
	
	/**
	 * Testing of BoundsObject.equals method:
	 * Provides coverage of True branch for 
	 * ((lower == null && that.lower == null)
	 */
	@Test
	public void equalsNullLowerTest(){
		boundObj = BoundsObject.newLowerBound(x, null, true);
		boundObj2 = BoundsObject.newLowerBound(x, null, true);
		boundObj.equals(boundObj2);
		assertEquals(boundObj.lower(), boundObj2.lower());
	}
	
	/**
	 * Testing of BoundsObject.equals method:
	 * Provides coverage of False branch for 
	 * ((lower == null && that.lower == null)
	 * by providing one null lower bound and 
	 * one non-null lower bound.
	 */
	@Test
	public void equalsMixedNullLowerTest(){
		boundObj = BoundsObject.newLowerBound(x, null, true);
		boundObj2 = BoundsObject.newLowerBound(x, num0, true);
		boundObj.equals(boundObj2);
		assertNull(boundObj.lower());
		assertNotNull(boundObj2.lower());
		assertNotEquals(boundObj.lower(), boundObj2.lower());
	}
	
	/**
	 * Testing of BoundsObject.equals method:
	 * Provides coverage of True branch for
	 * "lower.equals(that.lower))"
	 * and False branch of "strictLower == that.strictLower"
	 */
	@Test
	public void lowerNotEqualsTest(){
		boundObj = BoundsObject.newLowerBound(xpy, num10000, true);
		boundObj2 = BoundsObject.newLowerBound(xpy, num10000, false);
		boundObj.equals(boundObj2);
		assertEquals(boundObj.lower(), boundObj2.lower());
	}
	
	/**
	 * Testing of BoundsObject.equals method:
	 * Provides coverage of False branch for
	 * "lower.equals(that.lower))"
	 * and False branch of "strictLower == that.strictLower"
	 */
	@Test
	public void lowerEqualsTest(){
		boundObj = BoundsObject.newLowerBound(xpy, num10pt5, true);
		boundObj2 = BoundsObject.newLowerBound(xpy, num0, false);
		boundObj.equals(boundObj2);
		assertNotEquals(boundObj.lower(), boundObj2.lower());
	}
	

}
