package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.config.Configurations;
import edu.udel.cis.vsl.sarl.IF.config.ProverInfo.ProverKind;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3SideEffectTest {

	// Static fields: instantiated once and used for all tests...
	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();
	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);
	private static ExpressionFactory expressionFactory = factorySystem
			.expressionFactory();
	// types
	private static SymbolicIntegerType intType = universe.integerType();
	// expressions
	private static NumericExpression one = universe.integer(1);
	private static BooleanExpression booleanExprTrue = universe
			.trueExpression();
	// constants
	private static SymbolicConstant x = universe.symbolicConstant(
			universe.stringObject("x"), intType);
	private static SymbolicConstant y = universe.symbolicConstant(
			universe.stringObject("y"), intType);
	// Instance fields: instantiated before each test is run...
	private TheoremProverFactory proverFactory;
	private CVC3TheoremProver cvcProver;

	/**
	 * Set up each test. This method is run before each test.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		proverFactory = Prove.newProverFactory(
				universe,
				Configurations.getDefaultConfiguration().getProverWithKind(
						ProverKind.CVC3_API));
		cvcProver = (CVC3TheoremProver) proverFactory
				.newProver(booleanExprTrue);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * testIntDivSideEffect creates two numeric expressions with one using the
	 * symbolic operator INT_DIVIDE, and asserts that the two numeric
	 * expressions are not equal.
	 */

	@Test
	public void testIntDivSideEffect() {

		NumericExpression q = (NumericExpression) expressionFactory.expression(
				SymbolicOperator.INT_DIVIDE, intType, x, y);

		NumericExpression r = universe.add(q, one);

		assertEquals(ResultType.NO, cvcProver.valid(universe.equals(r, q))
				.getResultType());
		assertEquals(ResultType.NO, cvcProver.valid(universe.equals(r, q))
				.getResultType());
	}

	/**
	 * testModSideEffect creates two numeric expressions with one using the
	 * symbolic operator MODULO, and asserts that the two numeric expressions
	 * are not equal.
	 */

	@Test
	public void testModSideEffect() {

		NumericExpression q = (NumericExpression) expressionFactory.expression(
				SymbolicOperator.MODULO, intType, x, y);

		NumericExpression r = universe.add(q, one);

		assertEquals(ResultType.NO, cvcProver.valid(universe.equals(r, q))
				.getResultType());
		assertEquals(ResultType.NO, cvcProver.valid(universe.equals(r, q))
				.getResultType());
	}

	/**
	 * testIntDivModSideEffect creates three numeric expressions with one using
	 * the symbolic operator INT_DIVIDE and another using MODULO. The third
	 * numeric expression uses universe.add and the two numericExpressions. The
	 * test asserts that the third numeric expression is not equal to created
	 * integer variable one.
	 */

	@Test
	public void testIntDivModSideEffect() {

		NumericExpression q = (NumericExpression) expressionFactory.expression(
				SymbolicOperator.INT_DIVIDE, intType, x, y);
		NumericExpression r = (NumericExpression) expressionFactory.expression(
				SymbolicOperator.MODULO, intType, x, y);

		NumericExpression s = universe.add(q, r);

		assertEquals(ResultType.NO, cvcProver.valid(universe.equals(s, one))
				.getResultType());
		assertEquals(ResultType.NO, cvcProver.valid(universe.equals(s, one))
				.getResultType());
	}

}
