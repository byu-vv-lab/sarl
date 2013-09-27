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

import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Number;

/**
 * @author danfried
 *
 */
public class BoundsObjectMakeConstantTest {

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
	 * Tests BoundsObject.makeConstant()
	 * if(value == null) in the case of a null value being passed
	 */
	@Test(expected = RuntimeException.class)
	public void makeConstantNullTest() {
		boundObj = BoundsObject.newTightBound(xpy, num0);
		boundObj.makeConstant(null);
	}
	
	/**
	 * Tests "if (isIntegral() && !(value instanceof IntegerNumber)) {"
	 * under case True, True
	 */
	@Test
	public void makeConstantMixedIntegralTest(){
		boundObj = BoundsObject.newTightBound(xpyInt, num0);
		boundObj.makeConstant(num10000);
	}
	
	/**
	 * Tests "if (isIntegral() && !(value instanceof IntegerNumber)) {"
	 * under case False, False
	 */
	@Test
	public void makeConstantIntegralTest(){
		boundObj = BoundsObject.newTightBound(xpy, num0);
		boundObj.makeConstant(num0Int);
	}
	
	/**
	 * Tests "if (isIntegral() && !(value instanceof IntegerNumber)) {"
	 * under case False, True
	 */
	@Test
	public void makeConstantIntegralTest2(){
		boundObj = BoundsObject.newTightBound(xpy, num0);
		boundObj.makeConstant(num0);
	}
	
	/**
	 * Tests "if (isIntegral() && !(value instanceof IntegerNumber)) {"
	 * under case True, False
	 */
	@Test 
	public void makeConstantIntegralTest3(){
		boundObj = BoundsObject.newTightBound(xpyInt, num0);
		boundObj.makeConstant(num0Int);
	}
	
	/**
	 * Tests makeConstant, when a null value is passed as an argument
	 */
	@Test(expected = RuntimeException.class)
	public void makeConstantTest(){
		boundObj = BoundsObject.newLowerBound(xpy, num0Int, true);
		//boundObj.makeConstant(num0Int);
		//boundObj.makeConstant(num0);
		//boundObj.makeConstant(num10pt5);
		boundObj.makeConstant(null);
	}
	
	/**
	 * Tests makeConstant, when a rational is passed as an argument for a BoundsObject
	 * with an integral Symbolic Expression.
	 */
	@Test(expected = RuntimeException.class)
	public void makeConstantIntRealTest(){
		boundObj = BoundsObject.newTightBound(symbExpr_xpyInt, num10000Int);
		assertTrue(boundObj.isIntegral());
		boundObj.makeConstant(num10pt5);
	}
	
	@Ignore
	@Test
	public void makeConstantIntRatTest(){
		boundObj = BoundsObject.newTightBound(symbExpr_xpyInt, num10000Int);
		boundObj.makeConstant(num10000Int);
	}

}
