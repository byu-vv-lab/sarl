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
import edu.udel.cis.vsl.sarl.simplify.common.IdentitySimplifierFactory;

public class SimplifyTest {
	// test setup, BeforeClass, Before, After adapted from S.Siegel's Demo475
	// junit example

	private static SymbolicUniverse universe;

	private static NumericSymbolicConstant x;

	private static NumericSymbolicConstant y;

	private static NumericExpression xpy;

	private static SymbolicType realType;

	private static SymbolicType integerType;

	private static NumericExpression one, two, five;

	private static PrintStream out = System.out;
	
	private static FactorySystem system;
	
	static private PreUniverse preUniv;
	
	static private BooleanExpression xeq5;
	
	static private IdealFactory idealFactory;
	
	static private IdealSimplifierFactory simplifierFactory;
	
	static private Simplifier simp1_xeq5;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		system = PreUniverses.newIdealFactorySystem();
		preUniv = PreUniverses.newPreUniverse(system);
		universe = SARL.newStandardUniverse();
		idealFactory = (IdealFactory) system.expressionFactory()
				.numericFactory();
		simplifierFactory = (IdealSimplifierFactory) Ideal
				.newIdealSimplifierFactory(idealFactory, preUniv);
		realType = universe.realType();
		integerType = universe.integerType();
		x = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("x"), realType);
		y = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("y"), realType);
		xpy = universe.add(x, y);
		one = universe.rational(1); // 1.0
		two = universe.rational(2); // 2.0
		five = preUniv.rational(5); // 5.0
		xeq5 = preUniv.equals(x, five);
		simp1_xeq5 = simplifierFactory.newSimplifier(xeq5);
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

//	/**
//	 * Takes in -PreUniverse universe, -BooleanExpression assumption
//	 * 
//	 * Returns a new IdentitySimplifier
//	 */
//	@Ignore
//	@Test
//	public void identitySimplifier() {
//
//	}

	/**
	 * Written by Daniel Fried 
	 * Tests functions in Simplify class
	 */
	@Test
	public void testCreation() {
		// Simplify.identitySimplifier(PreUniverses.newIdealFactorySystem(),
		// PreUniverses.);
		
		//BooleanExpression xeq5 = preUniv.equals(x, universe.rational(5));

//		IdealFactory idealFactory = (IdealFactory) system.expressionFactory()
//				.numericFactory();
//		IdealSimplifierFactory simplifierFactory = (IdealSimplifierFactory) Ideal
//				.newIdealSimplifierFactory(idealFactory, preUniv);

//		Simplifier simp1 = simplifierFactory.newSimplifier(xeq5);

		assertEquals(preUniv.rational(5), simp1_xeq5.apply(x));

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

	/**Written by Daniel Fried
	 * Takes in -PreUniverse universe,
	 * 
	 * Returns a new IdentitySimplifierFactory
	 * 
	 * Aims to test IdentifySimplifierFactory.newSimplifier
	 */
	@Test
	public void newIdentitySimplifierFactory() {
		IdentitySimplifierFactory identSimplFact = new IdentitySimplifierFactory(preUniv);
		Simplifier simpl = identSimplFact.newSimplifier(xeq5);
		Simplifier check = Simplify.identitySimplifier(preUniv, xeq5);
		Simplify trivialClassDeclaration = new Simplify();  //completes coverage of Simplify.java by creating an instance of the class
		assertEquals(check.apply(xeq5), simpl.apply(xeq5));
		
		//provides test coverage for IdentitySimplifier.getFullContext()
		BooleanExpression boolAssumption = simpl.getFullContext();
		assertEquals(xeq5, boolAssumption);

	}

}
