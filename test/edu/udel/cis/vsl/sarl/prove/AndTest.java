package edu.udel.cis.vsl.sarl.prove;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.config.Configurations;
import edu.udel.cis.vsl.sarl.IF.config.Prover;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;

@RunWith(JUnit4.class)
public class AndTest {

	// Static fields: instantiated once and used for all tests...

	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();

	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);

	private static ExpressionFactory expressionFactory = factorySystem
			.expressionFactory();

	private static SymbolicType boolType = universe.booleanType();

	private static SymbolicType integerType = universe.integerType();

	private static BooleanExpression boolTrue = universe.trueExpression();

	private static BooleanExpression boolFalse = universe.falseExpression();

	private static NumericExpression five = universe.integer(5);

	private static NumericSymbolicConstant x = (NumericSymbolicConstant) universe
			.symbolicConstant(universe.stringObject("x"), integerType);

	private static BooleanExpression context = universe.lessThan(x, five);

	private static Collection<TheoremProver> provers;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		universe.setShowProverQueries(false);
		provers = new LinkedList<TheoremProver>();
		for (Prover info : Configurations.CONFIG.getProvers()) {
			provers.add(Prove.newProverFactory(universe, info).newProver(
					context));
		}
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Checks that the result of applying the prover to the given predicate is
	 * as expected.
	 * 
	 * @param expected
	 *            expected result type (YES, NO, or MAYBE)
	 * @param predicate
	 *            boolean expression to be checked for validity
	 */
	private void check(ResultType expected, BooleanExpression predicate) {
		for (TheoremProver prover : provers) {
			assertEquals(prover.toString(), expected, prover.valid(predicate)
					.getResultType());
		}
	}

	@Test
	@Ignore
	public void testTranslateAndOneArg() {
		List<BooleanExpression> s1 = new ArrayList<BooleanExpression>();
		s1.add(boolFalse);
		s1.add(boolTrue);

		SymbolicCollection<BooleanExpression> boolList = universe
				.basicCollection(s1);
		BooleanExpression andExp = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.AND, boolType, boolList);
		for (TheoremProver prover : provers) {
			ValidityResult result = prover.valid(andExp);
			assertEquals(ResultType.NO, result.getResultType());
		}
	}

	@Test
	public void translateTwoArgNotEqual() {
		check(ResultType.NO, universe.and(boolTrue, boolFalse));
	}

	@Test
	public void translateTwoArgEqual() {
		check(ResultType.YES, universe.and(boolTrue, boolTrue));
	}

	@Test(expected = SARLInternalException.class)
	public void translateThreeArg() {
		BooleanExpression predicate = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.AND, boolType, boolTrue, boolTrue,
						boolFalse);
		check(ResultType.NO, predicate);
	}
}
