package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cvc3.Expr;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3UnionTests {
	// Static fields: instantiated once and used for all tests...
	private static PrintStream out = System.out;
	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();
	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);
	private static ExpressionFactory expressionFactory = factorySystem
			.expressionFactory();
	// types
	private static SymbolicType realType = universe.realType();
	private static SymbolicType intType = universe.integerType();
	private static SymbolicType boolType = universe.booleanType();
	private static SymbolicUnionType intRealBoolUnion;
	private static SymbolicType unionArrayType;
	// expressions
	private static SymbolicExpression tenAndHalf = universe.rational(10.5);
	private static BooleanExpression booleanExprTrue = universe
			.trueExpression();
	private static BooleanExpression booleanExprFalse = universe
			.falseExpression();
	private static SymbolicExpression unionArray;
	// constants
	
	// Instance fields: instantiated before each test is run...
	private TheoremProverFactory proverFactory;
	private CVC3TheoremProver cvcProver;
	private ValidityChecker vc;

	/**
	 * Set up each test. This method is run before each test.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		proverFactory = Prove.newCVC3TheoremProverFactory(universe);
		cvcProver = (CVC3TheoremProver) proverFactory
				.newProver(booleanExprTrue);
		vc = cvcProver.validityChecker();
		
		// make a union to test
		// union of int, real, bool
		intRealBoolUnion = universe.unionType(universe
				.stringObject("union1"), Arrays.asList(new SymbolicType[]
						{intType, realType, boolType}));
		// union array type
		unionArrayType = universe.arrayType(intRealBoolUnion);
		
		// union array expression to write values to
		unionArray = universe
				.symbolicConstant(universe.stringObject("unionArray"),
						unionArrayType);
		// add true bool
		unionArray = universe.arrayWrite(unionArray, 
				universe.integer(0), // index of array
				universe.unionInject(intRealBoolUnion,
						universe.intObject(2), // 2 is index of type (bool)
						booleanExprTrue));
		// add 10.5 real
		unionArray = universe.arrayWrite(unionArray, 
				universe.integer(1), // index of array
				universe.unionInject(intRealBoolUnion,
						universe.intObject(1), // 1 is index of type (real)
						tenAndHalf));
		// add 0 int
		unionArray = universe.arrayWrite(unionArray, 
				universe.integer(2), // index of array
				universe.unionInject(intRealBoolUnion,
						universe.intObject(0), // 0 is index of type (int)
						universe.integer(0)));
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testTranslateUnionInject() {
		// inject bool false
		SymbolicExpression injectBoolFalse = universe
				.unionInject(intRealBoolUnion, universe.intObject(2), booleanExprFalse);
		
		SymbolicExpression injectBoolFalse2 = expressionFactory
				.expression(SymbolicOperator.UNION_INJECT, intRealBoolUnion,
						universe.intObject(1), tenAndHalf);
		
		// TODO report/figure out bug related to translate (unknown constructor)
		Expr translateResult = cvcProver.translate(injectBoolFalse2);
		out.println(translateResult);
	}
	
	@Test
	public void testTranslateUnionExtract() {
		// inject statement
		SymbolicExpression injectedTenandHalf = universe
				.unionInject(intRealBoolUnion, universe.intObject(1), tenAndHalf);
		
		// extract 10.5 real
		
		// this version translates as concrete, so no coverage for union extract
		SymbolicExpression extractReal = universe
				.unionExtract(universe.intObject(1), injectedTenandHalf);
		
		// this version gives an error on translateUnionExtract (unknown constructor)
		SymbolicExpression extractReal2 = expressionFactory
				.expression(SymbolicOperator.UNION_EXTRACT, intRealBoolUnion,
						universe.intObject(1), tenAndHalf);
		
		Expr translateResult = cvcProver.translate(extractReal2);
		out.println("Extracted type: " + translateResult.getType());
		assertEquals(translateResult.getType(), cvcProver.translateType(realType));
	}
}
