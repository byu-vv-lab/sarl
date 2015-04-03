/* Copyright 2013 Stephen F. Siegel, University of Delaware
 */
package edu.udel.cis.vsl.sarl.ideal.simplify;

import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.boundObj;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.num0;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.num0Int;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.num10000;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.num10000Int;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.num10pt5;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.num3;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.symbExpr_xpy;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.symbExpr_xpyInt;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.xpy;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.xpyInt;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.yNE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.number.real.CommonInterval;

/**
 * This test class provides testing coverage of the makeConstant
 * method in BoundsObject
 * 
 * @author danfried
 * 
 * @see CommonInterval.makeConstant
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
		assertTrue(boundObj.isConsistent());
		assertEquals(num0, boundObj.constant());
		boundObj.makeConstant(null);
		//out.println(boundObj.constant());
	}
	
	/**
	 * Tests "if (isIntegral() && !(value instanceof IntegerNumber)) {"
	 * under case True, True
	 */
	@Test
	public void makeConstantMixedIntegralTest(){
		boundObj = BoundsObject.newTightBound(xpyInt, num0);
		assertTrue(boundObj.isConsistent());
		boundObj.makeConstant(num10000);
		assertEquals(num10000Int, boundObj.constant());
	}
	
	/**
	 * Tests "if (isIntegral() && !(value instanceof IntegerNumber)) {"
	 * under case False, False
	 */
	@Test
	public void makeConstantIntegralTest(){
		boundObj = BoundsObject.newTightBound(xpy, num0);
		assertTrue(boundObj.isConsistent());
		boundObj.makeConstant(num0Int);
		assertEquals(num0Int, boundObj.constant());
	}
	
	/**
	 * Tests "if (isIntegral() && !(value instanceof IntegerNumber)) {"
	 * under case False, True
	 */
	@Test
	public void makeConstantIntegralTest2(){
		boundObj = BoundsObject.newTightBound(xpy, num0);
		assertTrue(boundObj.isConsistent());
		boundObj.makeConstant(num0);
		assertEquals(num0, boundObj.constant());
	}
	
	/**
	 * Tests "if (isIntegral() && !(value instanceof IntegerNumber)) {"
	 * under case True, False
	 */
	@Test 
	public void makeConstantIntegralTest3(){
		boundObj = BoundsObject.newTightBound(xpyInt, num0);
		assertTrue(boundObj.isConsistent());
		boundObj.makeConstant(num0Int);
		assertEquals(num0Int, boundObj.constant());
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
		assertTrue(boundObj.isConsistent());
		boundObj.makeConstant(num10pt5);
	}
	
	/**
	 * Tests makeConstant, when an integral is passed as an argument for a BoundsObject
	 * with an rational Symbolic Expression.
	 */
	@Test
	public void makeConstantIntRatTest(){
		boundObj = BoundsObject.newTightBound(symbExpr_xpy, num10000Int);
		assertTrue(boundObj.isConsistent());
		boundObj.makeConstant(num10000Int);
		assertEquals(num10000Int, boundObj.constant());
	}
	
	/**
	 * Application of makeConstant method with a non-integral real
	 * rational number.
	 */
	@Test
	public void makeConstantRationalTest(){
		boundObj = BoundsObject.newTightBound(yNE, num3);
		assertTrue(boundObj.isConsistent());
		boundObj.makeConstant(num10pt5);
		assertEquals(num10pt5, boundObj.constant());
		//out.println(num10pt5.getClass());
		assertEquals(num10pt5.getClass(), boundObj.constant().getClass());
		//out.println("integral? :  " + boundObj.isIntegral());
	}

}
