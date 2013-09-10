package edu.udel.cis.vsl.sarl.simplify;

import static org.junit.Assert.assertEquals;

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
import edu.udel.cis.vsl.sarl.ideal.Ideal;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.simplify.IdealSimplifierFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.simplify.IF.Simplifier;
import edu.udel.cis.vsl.sarl.simplify.common.IdentitySimplifier;

public class Simplifytest {
	// test setup, BeforeClass, Before, After adapted from S.Siegel's Demo475
	// junit example

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
	 * @Test public void test() { fail("Not yet implemented"); }
	 */

	/**
	 * Takes in -PreUniverse universe, -BooleanExpression assumption
	 * 
	 * Returns a new IdentitySimplifier
	 */

	@Test
	public void identitySimplifier() {

	}

	/**
	 * Written by Daniel Fried Tests function of identitySimplifier method in
	 * Simplify class
	 */
	@Test
	public void testCreation() {
		// Simplify.identitySimplifier(PreUniverses.newIdealFactorySystem(),
		// PreUniverses.);
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		PreUniverse preUniv = PreUniverses.newPreUniverse(system);
		BooleanExpression xeq5 = preUniv.equals(x, universe.rational(5));

		IdealFactory idealFactory = (IdealFactory) system.expressionFactory()
				.numericFactory();
		IdealSimplifierFactory simplifierFactory = (IdealSimplifierFactory) Ideal
				.newIdealSimplifierFactory(idealFactory, preUniv);

		Simplifier simp1 = simplifierFactory.newSimplifier(xeq5);

		assertEquals(preUniv.rational(5), simp1.apply(x));

		out.println("tostring of preUbiv is: " + preUniv.toString());
		// Simplifier check =
		// Simplify.identitySimplifier(PreUniverses.newPreUniverse(PreUniverses.newIdealFactorySystem()),
		// xeq5);
		Simplifier check = Simplify.identitySimplifier(preUniv, xeq5);
		BooleanExpression xeq5also = universe.equals(x, universe.rational(5));
		Simplifier check2 = Simplify
				.identitySimplifier(PreUniverses.newPreUniverse(PreUniverses
						.newIdealFactorySystem()), xeq5also);
		Simplifier checkCopy = check;

		// BooleanExpression yeq5 = xeq5;
		// assertEquals(check.apply(xeq5), universe.rational(5));
		assertEquals(check.apply(xeq5), xeq5also);
		assertEquals(check, check);
		assertEquals(check, checkCopy);
		assertEquals(check.apply(xeq5), check2.apply(xeq5also));
		assertEquals(check.equals(checkCopy), true);
		assertEquals(check.getReducedContext(), xeq5also);
		// out.println("the class for check is: " + check.getClass());
		assertEquals(check.getClass(), IdentitySimplifier.class);
		out.println("check is:" + check.toString());
		out.println("hashcode of check is: " + check.hashCode());
		out.println("preuniverse for check is: " + check.universe());
		// asserEquals(check.)
	}

	/**
	 * Takes in -PreUniverse universe,
	 * 
	 * Returns a new IdentitySimplifierFactory
	 */
	@Test
	public void newIdentitySimplifierFactory() {

	}

}
