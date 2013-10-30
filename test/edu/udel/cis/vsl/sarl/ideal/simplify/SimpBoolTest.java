/* Copyright 2013 Stephen F. Siegel, University of Delaware
 */
package edu.udel.cis.vsl.sarl.ideal.simplify;

import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

/**
 * Testing on IdealSimplifier on the basis of boolean
 * and BooleanExpressions for assumptions.
 * 
 * @see IdealSimplifier
 * 
 * @author danfried
 *
 */
public class SimpBoolTest {

	/**
	 * Calls the setUp() method in CommonObjects to make use of consolidated SARL object 
	 * declarations and initializations for testing of "Simplify" module.
	 * Also initialized objects in the CommonObjects class that
	 * are used often and therefore not given an initial value.
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
	 * Test on IdealSimplifier to check if a boolean-assigned
	 * value/evaluation for a variable, when applied,
	 * evaluates to the intended value.
	 */
	@Test
	public void boolExprTest() {
		assumption = xeq5;
		idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
		assertEquals(rat5.type(), idealSimplifier.apply(x).type());
		assertEquals(rat5, idealSimplifier.apply(x));
	}
	
	/**
	 * Test on IdealSimplifier to be sure that opposing
	 * claims applied to the same variable to not evaluate to
	 * equal to one another.
	 */
	@Test
	public void boolValTest(){
		/*//p = preUniv.trueExpression();
		out.println(pThanQ);
		out.println(preUniv.implies(p, preUniv.not(q)));
		//assumption = preUniv.equals(p, preUniv.trueExpression());
		assumption = preUniv.implies(p, preUniv.not(q));
		out.println(assumption.toStringBufferLong());
		idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
		out.println(idealSimplifier.apply(pThanQ));*/
		
		assumption = preUniv.not(xeq5);
		
		idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
		
		claim1 = xeq5;
		
		idealSimp2 = idealSimplifierFactory.newSimplifier(claim1);
		
		//check that type-matching is in place
		assertEquals(idealSimplifier.apply(x).type(), idealSimp2.apply(x).type());
		assertNotEquals(idealSimplifier.apply(x), idealSimp2.apply(x));
		assertEquals(claim1, preUniv.not(assumption));
		assertEquals(assumption, preUniv.not(claim1));
		//out.println(claim1);
		//out.println(assumption);
	}
	
	/**
	 * Test on IdealSimplifier to confirm
	 * that the return value from
	 * assumptionAsInterval is null when the provided
	 * assumption is counter to the defined value of the
	 * target variable.
	 * 
	 * @see IdealSimplifer.assumptionAsInterval
	 */
	@Test
	public void assumptionCounterValueTest(){
		//p = preUniv.falseExpression();
		//q = preUniv.trueExpression();
		
		//out.println(pThanQ);
		assumption = preUniv.equals(xeq5, preUniv.falseExpression());
		idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
		//out.println(idealSimplifier.apply(x));
		assertNull(idealSimplifier.assumptionAsInterval(x));
		//assumption = preUniv.and(preUniv.equals(p, preUniv.trueExpression()), pThanQ);
		//out.println(assumption);
		//idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
		//out.println("here: " + idealSimplifier.apply(pThanQ));
	}

}
