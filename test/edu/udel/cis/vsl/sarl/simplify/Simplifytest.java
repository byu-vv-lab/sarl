package edu.udel.cis.vsl.sarl.simplify;

import static org.junit.Assert.*;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.simplify.IF.Simplifier;

public class Simplifytest {
	//test setup, BeforeClass, Before, After adapted from S.Siegel's Demo475 junit example
	
	private static SymbolicUniverse universe;

	private static NumericSymbolicConstant x;

	private static NumericSymbolicConstant y;

	private static NumericExpression xpy;

	private static SymbolicType realType;

	private static SymbolicType integerType;

	private static NumericExpression one, two;

	private static PrintStream out = System.out;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		universe = SARL.newStandardUniverse();
		realType = universe.realType();
		integerType = universe.integerType();
		x = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("x"), realType);
		y = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("y"), realType);
		xpy = universe.add(x, y);
		one = universe.rational(1); // 1.0
		two = universe.rational(2); // 2.0
	}
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/*
	@Test
	public void test() {
		fail("Not yet implemented");
	}
	*/
	
	/**
	 * Takes in
	 * 	-PreUniverse universe,
	 *	-BooleanExpression assumption
	 *
	 * Returns a new IdentitySimplifier
	 */
	
	@Test
	public void identitySimplifier(){
		
	}
	
	/**
	 * Written by Daniel Fried
	 * Tests function of identitySimplifier method in Simplify class
	 */
	@Test 
	public void testCreation(){
		BooleanExpression xeq5 = universe.equals(x, universe.rational(5));
		//Simplify.identitySimplifier(PreUniverses.newIdealFactorySystem(), PreUniverses.);
		Simplifier check = Simplify.identitySimplifier(PreUniverses.newPreUniverse(PreUniverses.newIdealFactorySystem()), xeq5);
		BooleanExpression yeq5 = universe.equals(y, universe.rational(5));
		//assertEquals(check, universe.rational(5));
	}
	
	
	/**
	 * Takes in
	 * 	-PreUniverse universe,
	 *
	 * Returns a new IdentitySimplifierFactory
	 */
	@Test
	public void newIdentitySimplifierFactory(){
		
	}

}
