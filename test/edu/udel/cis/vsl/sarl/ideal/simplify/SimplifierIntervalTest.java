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

//private static SymbolicConstant t;

/**
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
	 * Testing of idealsimplifier on assumptionAsInterval method...
	 * Tests passing matched and mismatched symbolic constants as assumption
	 */
	@Test
	public void badTest() {
		//non-matching symbolic constant in assumptionAsInterval and 
		//the initial assumption should return null
		assertNull(idealSimplifier.assumptionAsInterval(x));
		//the upper bound should be -1
		assertEquals(intNeg1.toString(),idealSimplifier.assumptionAsInterval(xInt).upper().toString());
	}

}
