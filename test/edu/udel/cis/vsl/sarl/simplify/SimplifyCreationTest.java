/* Copyright 2013 Stephen F. Siegel, University of Delaware
 */
package edu.udel.cis.vsl.sarl.simplify;

import edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.simplify.IF.Simplifier;
import edu.udel.cis.vsl.sarl.simplify.IF.SimplifierFactory;

/**
 * @author danfried
 * Tests the class and two methods of Simplify.java
 *
 */
public class SimplifyCreationTest {

	/**
	 * Calls the setup() methed of CommonObjects under the 
	 * test...ideal.simplify package
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		CommonObjects.setUp();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
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
	 * Test on instantiation of a Simplify class object
	 */
	@Test
	public void testCreation() {
		Simplify simplifier = new Simplify();
	}
	
	/**
	 * 
	 */
	@Test
	public void testIdentitySimplifier(){
		Simplifier simplifier = Simplify.identitySimplifier(getPreUniv(), CommonObjects.getXeq5());
	}
	
	/**
	 * 
	 */
	@Test
	public void simplifierFactoryTest(){
		SimplifierFactory simplifierFactory = Simplify.newIdentitySimplifierFactory(getPreUniv());
	}

}
