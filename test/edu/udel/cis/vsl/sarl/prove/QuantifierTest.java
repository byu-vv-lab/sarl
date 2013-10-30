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
 * Tests translation of expressions involving quantifiers.
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
		provers = new LinkedList<TheoremProver>();
		provers.add(Prove.newCVC3TheoremProverFactory(universe).newProver(
				context));
		// add more provers here
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private void check(ResultType expected, BooleanExpression predicate) {
		for (TheoremProver prover : provers)
			assertEquals(prover.toString(), expected, prover.valid(predicate)
					.getResultType());
	}

	/**
	 * Checks whether "all x:INT.x=3" is valid. Answer should be NO. Note that
	 * the prover should confuse the bound x with the free x which is 5.
	 */
	@Test
	public void simpleForall1() {
		check(ResultType.NO, universe.forall(x, universe.equals(x, three)));
	}

	/**
	 * Checks whether "all x:INT.x=5" is valid. Answer should be NO. Note that
	 * the prover should confuse the bound x with the free x which is 5.
	 */
	@Test
	public void simpleForall2() {
		check(ResultType.NO, universe.forall(x, universe.equals(x, five)));
	}

}
