/* Copyright 2013 Stephen F. Siegel, University of Delaware
 */
package edu.udel.cis.vsl.sarl.ideal.simplify;

import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.assumption;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.idealSimplifier;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.idealSimplifierFactory;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.int0;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.intNeg1;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.preUniv;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.x;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.xInt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

//private static SymbolicConstant t;

/**
 * Testing on assumptionAsInterval method in IdealSimplifier to look for expected behavior
 * when giving mixed-type value, and also to confirm bounds of the supplied interval
 * 
 * @author danfried
 *
 */
public class SimplifierIntervalTest {

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
		assumption = preUniv.lessThan(xInt, int0);
		//preUniv.equals(preUniv.multiply(rat5,x), preUniv.multiply(y, y));
		idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
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
	 * Testing of IdealSimplifier on assumptionAsInterval method...
	 * Tests passing matched and mismatched symbolic constants as assumption
	 */
	@Test
	public void mixedTypeNullTest() {
		//non-matching symbolic constant in assumptionAsInterval and 
		//the initial assumption should return null
		assertNull(idealSimplifier.assumptionAsInterval(x));
	}
	
	/**
	 * Testing of IdealSimplifier on assumptionAsInterval method, when matched-type (integer)
	 * expressions are used for the assumption and also expression assumption is applied to.
	 */
	@Test
	public void matchedTypeTest(){
		//the upper bound should be -1
		//out.println(intNeg1.toString());
		//out.println(idealSimplifier.assumptionAsInterval(xInt));
		//out.println(assumption.atomString());
		assertEquals(intNeg1.toString(),idealSimplifier.assumptionAsInterval(xInt).upper().toString());
	}

}
