package edu.udel.cis.vsl.sarl.ideal.simplify;

import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.idealSimplifier;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.idealSimplifierFactory;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.preUniv;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.rat0;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.rat2;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.rat25;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.ratNeg25;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.trueExpr;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.x;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.xeq5;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.ideal.Ideal;

/**
 * Testing on IdealSimplifier based on Polynomials using methods 
 *  - getFullContext()
 *  - getReducedContext()
 * 
 * @see Ideal.simplifier
 * 
 * @author mbrahma
 */

public class IdealSimplifierTest {

	private static BooleanExpression boolArg1, boolArg2;

	
	/**
	 * Calls the setUp() method in CommonObjects to make use of consolidated
	 * SARL object declarations and initializations for testing of "Simplify"
	 * module. Also initialized objects in the CommonObjects class that are used
	 * often and therefore not given an initial value.
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
	
	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * Test on IdealSimplifier to check if a exception is thrown and if
	 * it is the correct one. 
	 */
	@Test(expected = NullPointerException.class)
	public void getFullContextTextTestnull(){
		
		idealSimplifier = idealSimplifierFactory.newSimplifier(null);
		BooleanExpression boolNull = idealSimplifier.getFullContext();
		assertEquals(null,boolNull);
	
	}
	
	/**
	 * Test on IdealSimplifier to get full context
	 */
	public void getFullContextTextTestTrivial(){
		
	idealSimplifier = idealSimplifierFactory.newSimplifier(xeq5);
	BooleanExpression boolXEq5 = idealSimplifier.getFullContext();
	assertEquals(xeq5,boolXEq5);
	
	}
	
	/**
	 * Test on IdealSimplifier to get full context
	 */
	public void getFullContextTestTrivial1(){
		boolArg1 = preUniv.lessThanEquals(rat25, preUniv.multiply(x, x));
		IdealSimplifier simpEq1 =idealSimplifierFactory.newSimplifier(boolArg1);
		BooleanExpression boolSimpEq1 = simpEq1.getFullContext();
		assertEquals(preUniv.lessThanEquals(rat0, preUniv.add(ratNeg25, preUniv.multiply(x, x))), boolSimpEq1);
	}
	
	/**
	 * Test on IdealSimplifier to get full context
	 */
	public void getFullContextTestTrivial2(){
		boolArg2 = preUniv.lessThanEquals(rat2, preUniv.multiply(x,x));
		IdealSimplifier simpEq2 =idealSimplifierFactory.newSimplifier(boolArg2);
		BooleanExpression boolSimpEq2 = simpEq2.getFullContext();
		assertEquals(boolArg2, boolSimpEq2);
	}
	/*@Test
	public void getFullReducedQuadTest(){
		boolArg1 = preUniv.lessThanEquals(twenty_five, xpyInt);
		boolArg2 = preUniv.lessThan(five, yInt);
		
		//IdealSimplifier idealSimp1 = idealSimplifierFactory.newSimplifier(boolArg1);
		//IdealSimplifier idealSimp2 = idealSimplifierFactory.newSimplifier(boolArg2);
		
		
		//BooleanExpression boolExpr1 = idealSimp1.getReducedContext();
		//BooleanExpression boolExpr2 = idealSimp2.getReducedContext();
		
		assumption = preUniv.equals(boolArg1, boolArg2);
		idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
		BooleanExpression boolExpr = idealSimplifier.getFullContext();
		
		assertEquals(boolArg1,boolExpr);
		
	}*/
	
	/**
	 * Test on IdealSimplifier to get reduced context
	 */
	
	@Test
	public void getReducedContextTest(){
		CommonObjects.setUp();
		idealSimplifier = idealSimplifierFactory.newSimplifier(trueExpr);
		BooleanExpression boolTrue = idealSimplifier.getReducedContext();
		assertEquals(trueExpr,boolTrue);
		
		boolArg2 = (BooleanExpression)preUniv.lessThanEquals(rat2, preUniv.multiply(x,x)).commit();
		IdealSimplifier simpEq2 =idealSimplifierFactory.newSimplifier(boolArg2);
		BooleanExpression boolSimpEq2 = simpEq2.getReducedContext();
		assertEquals(boolArg2, boolSimpEq2);
	}
	
/*	@Test
	public void assumptionAsIntervalTest(){
		boolArg1 = preUniv.lessThanEquals(twenty_five, preUniv.multiply(x, x));
		boolArg2 = preUniv.lessThan(x, two_hund);
		assumption = preUniv.and(boolArg1, boolArg2);
		
		idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
		Interval interval = idealSimplifier.assumptionAsInterval(xsqd);
		
		assertEquals(x,interval);
		
	}*/
}
