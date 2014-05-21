package edu.udel.cis.vsl.sarl.prove;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;

/**
 * Tests translation of expressions involving quantifiers, all under the
 * context: x=5 and y<10.
 * 
 * Examples to be turned into tests:
 * 
 * <pre>
 * all x:INT.x=3  ---> NO
 * exists x:INT.x=3 ---> YES
 * (all x:INT.x=3) || (exists x:INT.x=3) ---> YES
 * all x:INT.(exists x:INT.x=3) ---> YES
 * exists x:INT.(all x:INT.x=3) ---> NO
 * all x:INT.(exists x:INT.(forall x:INT.x=3)) ---> NO
 * all x: INT.(exists y:INT. x==y)) ---> TRUE
 * 
 * </pre>
 * 
 * @author siegel
 * 
 */
public class QuantifierTest {

	// Static fields: instantiated once and used for all tests...

	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();

	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);

	private static SymbolicType integerType = universe.integerType();

	private static NumericExpression three = universe.integer(3);

	private static NumericExpression five = universe.integer(5);

	private static NumericExpression ten = universe.integer(10);

	private static NumericSymbolicConstant x = (NumericSymbolicConstant) universe
			.symbolicConstant(universe.stringObject("x"), integerType);

	private static NumericSymbolicConstant y = (NumericSymbolicConstant) universe
			.symbolicConstant(universe.stringObject("y"), integerType);

	private static BooleanExpression context = universe.and(
			universe.equals(x, five), universe.lessThan(y, ten));

	private static Collection<TheoremProver> provers;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		universe.setShowProverQueries(true); // for debugging
		provers = new LinkedList<TheoremProver>();
		{
			TheoremProver prover = Prove.newCVC3TheoremProverFactory(universe)
					.newProver(context);

			provers.add(prover);
		}
		{
			// debugging:
			// context = universe.trueExpression();

			TheoremProver cvc4prover = Prove.newCVC4TheoremProverFactory(
					universe).newProver(context);

			provers.add(cvc4prover);
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

	/**
	 * Checks whether "all x:INT.x=3" is valid. Answer should be NO. Note that
	 * the prover should not confuse the bound x with the free x which is 5.
	 */
	@Test
	public void simpleForall1() {
		check(ResultType.NO, universe.forall(x, universe.equals(x, three)));
	}

	/**
	 * Checks whether "all x:INT.x=5" is valid. Answer should be NO. Note that
	 * the prover should not confuse the bound x with the free x which is 5.
	 */
	@Test
	public void simpleForall2() {
		check(ResultType.NO, universe.forall(x, universe.equals(x, five)));
	}

	/**
	 * Checks whether "exists x:INT.x=3" is valid. Answer should be YES. Note
	 * the bound x should not be confused with the free x.
	 */
	@Test
	public void exists1() {
		check(ResultType.YES, universe.exists(x, universe.equals(x, three)));
	}

	/**
	 * Checks whether "(all x:INT.x=3) || (exists x:INT.x=3)" is valid. Answer
	 * should be YES.
	 */
	@Test
	public void allOrExists() {
		check(ResultType.YES, universe.or(
				universe.forall(x, universe.equals(x, three)),
				universe.exists(x, universe.equals(x, three))));
	}

	/**
	 * Checks whether "all x:INT.(exists x:INT.x=3)" is valid. Answer should be
	 * YES.
	 */
	@Test
	public void allExists1() {
		check(ResultType.YES,
				universe.forall(x,
						universe.exists(x, universe.equals(x, three))));
	}

	/**
	 * Checks whether "exists x:INT.(all x:INT.x=3)" is valid. Answer should be
	 * NO.
	 */
	@Test
	public void existsAll1() {
		check(ResultType.NO,
				universe.exists(x,
						universe.forall(x, universe.equals(x, three))));
	}
}
