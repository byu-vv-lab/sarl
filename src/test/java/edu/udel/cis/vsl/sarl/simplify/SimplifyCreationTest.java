/* Copyright 2013 Stephen F. Siegel, University of Delaware
 */
package edu.udel.cis.vsl.sarl.simplify;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
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
	
	private static FactorySystem system;
	
	private static PreUniverse preUniv;
	
	private static SymbolicType realType;
	
	private static NumericSymbolicConstant x;
	
	private static BooleanExpression xeq5;
	
	private static NumericExpression rat5;

	/**
	 * Calls the setup() method of CommonObjects under the 
	 * test...ideal.simplify package
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		system = PreUniverses.newIdealFactorySystem();
		preUniv = PreUniverses.newPreUniverse(system);
		realType = preUniv.realType();
		rat5 = preUniv.rational(5);
		x = (NumericSymbolicConstant)preUniv.symbolicConstant(
				preUniv.stringObject("x"), realType);
		xeq5 = preUniv.equals(x, rat5);
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
		@SuppressWarnings("unused")
		Simplify simplify = new Simplify();
	}
	
	/**
	 * 
	 */
	@Test
	public void testIdentitySimplifier(){
		@SuppressWarnings("unused")
		Simplifier simplifier = Simplify.identitySimplifier(preUniv, xeq5);
	}
	
	/**
	 * 
	 */
	@Test
	public void simplifierFactoryTest(){
		@SuppressWarnings("unused")
		SimplifierFactory simplifierFactory = Simplify.newIdentitySimplifierFactory(preUniv);
	}

}
