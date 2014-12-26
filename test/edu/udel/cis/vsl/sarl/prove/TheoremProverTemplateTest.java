package edu.udel.cis.vsl.sarl.prove;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.ModelResult;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.config.Configurations;
import edu.udel.cis.vsl.sarl.IF.config.Prover;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;

/**
 * A template for testing theorem provers. Instead of directly calling the
 * methods of a specific theorem prover, it is recommended to call the methods
 * of the theorem prover interface. In this way, the tests could be reused later
 * when more theorem provers are introduced.
 * 
 * A list of TheoremProver instances is defined, and in setUpBeforeClass, you
 * can initialize it with a number of provers.
 * 
 * In checkResult, a predicate is proved by each of the provers in the list, and
 * the counter example will be printed if the result is invalid.
 * 
 * In validOrModelTest, a number of predicates are defined to be tested.
 * 
 * @author Manchun
 * 
 */

public class TheoremProverTemplateTest {

	// Static fields: instantiated once and used for all tests...

	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();

	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);

	private static SymbolicType integerType = universe.integerType();

	private static NumericExpression five = universe.integer(5);

	private static NumericExpression ten = universe.integer(10);

	private static NumericSymbolicConstant x = (NumericSymbolicConstant) universe
			.symbolicConstant(universe.stringObject("x"), integerType);

	private static NumericSymbolicConstant y = (NumericSymbolicConstant) universe
			.symbolicConstant(universe.stringObject("y"), integerType);

	private static BooleanExpression context = universe.and(
			universe.lessThan(x, five), universe.lessThan(y, ten));

	private static LinkedList<TheoremProver> provers;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		provers = new LinkedList<TheoremProver>();
		// adjust as necessary to include only provers you want:
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
	 * Uses each prover to check the validity of the predicate, compare the
	 * result with the expected result, and print the counterexample if the
	 * predicate is invalid.
	 * 
	 * @param expected
	 *            The validity result (YES, NO or MAYBE) that is expected.
	 * @param predicate
	 *            The predicate to be examined.
	 */
	private void checkResult(ValidityResult.ResultType expected,
			BooleanExpression predicate) {
		System.out.println("\nProving the predicate " + predicate.toString()
				+ "...");
		for (TheoremProver prover : provers) {
			System.out.print("Current prover is " + prover.toString() + ", ");
			ValidityResult result = prover.validOrModel(predicate);
			assertEquals(expected, result.getResultType());
			System.out.println("result is " + result.getResultType().toString()
					+ ".");
			if (expected == ResultType.NO)
				System.out.println("The counterexample is "
						+ ((ModelResult) result).getModel().toString() + ".");
		}
	}

	/**
	 * Checks whether "x==y" / "5+10 < x+y" is valid. The provers should find
	 * the predicates are invalid and provide a counterexample.
	 */
	@Test
	public void validOrModelTest() {
		BooleanExpression predicate = universe.equals(x, y);
		checkResult(ResultType.NO, predicate);

		predicate = universe.lessThan(universe.add(five, ten),
				universe.add(x, y));
		checkResult(ResultType.NO, predicate);
	}

}
