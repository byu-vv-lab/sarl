/* Copyright 2013 Stephen F. Siegel, University of Delaware
 */
package edu.udel.cis.vsl.sarl.simplify;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.simplify.IF.Simplifier;
import edu.udel.cis.vsl.sarl.simplify.IF.SimplifierFactory;

/**
 * @author danfried
 * Tests the class and two methods of Simplify.java
 *
 */
public class SimplifyCreationTest {
	
	static FactorySystem system;
	
	private static PreUniverse preUniv;
	
	static BooleanExpression xeq5;
	
	static NumericExpression rat5;
	
	static SymbolicType realType;
	
	static NumericSymbolicConstant x;

	/**
	 * Calls the setup() method of CommonObjects under the 
	 * test...ideal.simplify package
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		system = PreUniverses.newIdealFactorySystem();
		preUniv = PreUniverses.newPreUniverse(system);
		rat5 = preUniv.rational(5);
		x = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("x"), realType);
		xeq5 = preUniv.equals(x, rat5);
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
		Simplifier simplifier = Simplify.identitySimplifier(preUniv, xeq5);
	}
	
	/**
	 * 
	 */
	@Test
	public void simplifierFactoryTest(){
		SimplifierFactory simplifierFactory = Simplify.newIdentitySimplifierFactory(preUniv);
	}

}
