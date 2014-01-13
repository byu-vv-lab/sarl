package edu.udel.cis.vsl.sarl.prove;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import edu.udel.cis.vsl.sarl.IF.ModelResult;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;

// TODO: set this up like the QuantifierTest.
// It should use on the interfaces.
// These tests deal with the model finder for queries
// which are not valid.
// They should check that prover answers "no" to validOrModel,
// then examine the model and make sure it does not satisfy
// the query.

// until this class stops throwing exception, run in separate
// Java virtual machine:
@RunWith(JUnit4.class)
public class ModelTest {

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
			universe.equals(x, five), universe.lessThan(y, ten));

	private static Collection<TheoremProver> provers;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		provers = new LinkedList<TheoremProver>();
		{
			TheoremProver prover = Prove.newCVC3TheoremProverFactory(universe)
					.newProver(context);

			// TheoremProver cvc4prover =
			// Prove.newCVC4TheoremProverFactory(universe)
			// .newProver(context);

			// prover.setOutput(System.out); // for debugging
			// cvc4prover.setOutput(System.out);

			provers.add(prover);
			// provers.add(cvc4prover); // adds cvc3 and cvc4 theorem prover to
			// the collection
		}

		// TODO: until this is working, don't commit it, as it causes
		// subsequent tests to crash...

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

	@Test
	public void testValidProver() {
		BooleanExpression predicate = universe.equals(x, y);
		for (TheoremProver prover : provers) {
			prover.validOrModel(predicate);
		}
	}

}
