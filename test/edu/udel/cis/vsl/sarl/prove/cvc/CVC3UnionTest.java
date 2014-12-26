package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cvc3.Expr;
import cvc3.QueryResult;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.config.Configurations;
import edu.udel.cis.vsl.sarl.IF.config.Prover.ProverKind;
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

public class CVC3UnionTest {
	// Static fields: instantiated once and used for all tests...
	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();
	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);
	private static ExpressionFactory expressionFactory = factorySystem
			.expressionFactory();
	// types
	private static SymbolicType realType = universe.realType();
	private static SymbolicType intType = universe.integerType();
	private static SymbolicUnionType intRealUnion;
	private static SymbolicType unionArrayType;
	// expressions
	private static SymbolicExpression tenAndHalf = universe.rational(10.5);
	private static BooleanExpression booleanExprTrue = universe
			.trueExpression();
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
		proverFactory = Prove.newProverFactory(universe,
				Configurations.CONFIG.getProverWithKind(ProverKind.CVC3_API));
		cvcProver = (CVC3TheoremProver) proverFactory
				.newProver(booleanExprTrue);
		vc = cvcProver.validityChecker();

		// make a union to test
		// union of int, real
		intRealUnion = universe.unionType(universe.stringObject("union1"),
				Arrays.asList(new SymbolicType[] { intType, realType }));
		// union array type
		unionArrayType = universe.arrayType(intRealUnion);

		// union array expression to write values to
		unionArray = universe.symbolicConstant(
				universe.stringObject("unionArray"), unionArrayType);
		// add 10.5 real
		// index 0 of union is of type real
		unionArray = universe.arrayWrite(unionArray, universe.integer(1),
				universe.unionInject(intRealUnion, universe.intObject(1),
						tenAndHalf));

		// add 0 int
		// index 1 of union is of type int
		unionArray = universe.arrayWrite(unionArray, universe.integer(2),
				universe.unionInject(intRealUnion, universe.intObject(0),
						universe.integer(0)));

	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * TranslateUnionInject translates union type and injects -0.5 rational and
	 * assest the equality of the translated result and th cvcProver when
	 * translating the symbolic expression after injecting -0.5.
	 */

	@Test
	public void TranslateUnionInject() {
		// translate union type
		cvcProver.translateType(intRealUnion);

		// inject -0.5 real
		SymbolicExpression injectNegHalf = universe.unionInject(intRealUnion,
				universe.intObject(1), universe.rational(-0.5));

		Expr translateResult = cvcProver.translate(injectNegHalf);
		assertEquals(translateResult.getType(),
				cvcProver.translateType(injectNegHalf.type()));
		// out.println(translateResult);
		// out.println(injectNegHalf);
	}

	/**
	 * translateUnionExtract asserts the type equality of translated real int
	 * and the symbolic expression after using the UNION_EXTRACT symbolic
	 * operator.
	 */

	@Test
	public void TranslateUnionExtract() {
		// inject statement
		SymbolicExpression injectedTenAndHalf = universe.unionInject(
				intRealUnion, universe.intObject(1), tenAndHalf);

		// extract 10.5 real
		cvcProver.translateType(intRealUnion);
		SymbolicExpression extractReal2 = expressionFactory.expression(
				SymbolicOperator.UNION_EXTRACT, realType,
				universe.intObject(1), injectedTenAndHalf);
		SymbolicExpression extractReal1 = universe.unionExtract(
				universe.intObject(1), injectedTenAndHalf);
		Expr translateResult = cvcProver.translate(extractReal1);
		assertEquals(translateResult.getType(),
				cvcProver.translateType(extractReal2.type()));
	}

	/**
	 * TranslateUnionTest creates an inject statement, and asserts a union test
	 * involving the UNION_TEST symbolic operator.
	 */

	@Test
	public void TranslateUnionTest() {
		// inject statement
		SymbolicExpression injectedTenAndHalf = universe.unionInject(
				intRealUnion, universe.intObject(1), tenAndHalf);
		// union test
		// translate type first
		cvcProver.translateType(intRealUnion);
		SymbolicExpression intTest = expressionFactory.expression(
				SymbolicOperator.UNION_TEST, universe.booleanType(),
				universe.intObject(0), injectedTenAndHalf);

		Expr translateResult = cvcProver.translate(intTest);
		// out.println(translateResult);
		assertEquals(intTest.isTrue(), translateResult.isTrue());

		intTest = expressionFactory.expression(SymbolicOperator.UNION_TEST,
				universe.booleanType(), universe.intObject(1),
				injectedTenAndHalf);
		translateResult = cvcProver.translate(intTest);
		// out.println(translateResult);
		assertEquals(intTest.isTrue(), translateResult.isTrue());

		// inject statement
		SymbolicExpression injectedZero = universe.unionInject(intRealUnion,
				universe.intObject(0), universe.zeroInt());

		intTest = expressionFactory.expression(SymbolicOperator.UNION_TEST,
				universe.booleanType(), universe.intObject(1), injectedZero);
		translateResult = cvcProver.translate(intTest);
		assertEquals(intTest.isTrue(), translateResult.isTrue());

		// extract statement
		SymbolicExpression extractedZero = universe.unionExtract(
				universe.intObject(0), injectedZero);
		Expr expr = cvcProver.translate(extractedZero);
		Expr expected = cvcProver.translate(universe.zeroInt());
		assertEquals(expected, expr);
		Expr boolExpr = vc.eqExpr(expr, expected);
		assertEquals(QueryResult.VALID, vc.query(boolExpr));
	}
}
