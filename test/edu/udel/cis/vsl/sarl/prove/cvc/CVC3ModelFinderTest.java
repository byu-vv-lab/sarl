package edu.udel.cis.vsl.sarl.prove.cvc;

import java.io.PrintStream;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cvc3.Expr;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3ModelFinderTest {
	
	// Static fields: instantiated once and used for all tests...

	private static PrintStream out = System.out;

	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();

	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);

	private static ExpressionFactory expressionFactory = factorySystem
			.expressionFactory();

	private static SymbolicRealType realType = universe.realType();

	private static NumericExpression two = universe.rational(2);

	private static NumericExpression five = universe.rational(5);
	
	private static BooleanExpression booleanExprTrue = universe.trueExpression();
	
	//Instance fields: instantiated before each test is run...
	
	private TheoremProverFactory proverFactory;

	private CVC3TheoremProver cvcProver;

	private ValidityChecker vc;
	
	@Before
	public void setUp() throws Exception {
		proverFactory = Prove.newCVC3TheoremProverFactory(universe);
		cvcProver = (CVC3TheoremProver) proverFactory
				.newProver(booleanExprTrue);
		vc = cvcProver.validityChecker();
	}
	
	@Test(expected=SARLInternalException.class)
	public void testCreateCVC3ModelFinder() {
		HashMap<Expr, Expr> h = new HashMap<Expr, Expr>();
		h.put(vc.falseExpr(),vc.falseExpr());
		CVC3TheoremProver prover = (CVC3TheoremProver) proverFactory.newProver(universe.bool(true));
		prover.setOutput(out);
		CVC3ModelFinder c = new CVC3ModelFinder(prover,h);
	}
}

