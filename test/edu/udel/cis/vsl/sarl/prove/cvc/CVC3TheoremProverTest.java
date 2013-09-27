package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cvc3.Expr;
import cvc3.Type;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3TheoremProverTest {

	// Static fields: instantiated once and used for all tests...
	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();
	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);
	// expressions
	private static BooleanExpression booleanExprTrue = universe
			.trueExpression();
	private static BooleanExpression booleanExprFalse = universe
			.falseExpression();
	
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
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBoolean() {
		cvcProver.setOutput(null);
		SymbolicType boolType = universe.booleanType();
		Type t = cvcProver.translateType(boolType);

		assertEquals(t, vc.boolType());
	}
	
	@Test
	public void testToString(){
		
		String expected = "CVC3TheoremProver";
		String notExpected = "This is wrong";
		
		assertEquals(expected, cvcProver.toString());
		assertFalse(notExpected.equals(cvcProver.toString()));
	}
	
	
	
	@Test
	public void testValid() {
		// including for coverage
		boolean show = cvcProver.showProverQueries();

		// if true, then true (valid)
		assertEquals(ValidityResult.ResultType.YES, 
				cvcProver.valid(booleanExprTrue).getResultType());
		// if true, then false (invalid)
		assertEquals(ValidityResult.ResultType.NO,
				cvcProver.valid(booleanExprFalse).getResultType());
		
		cvcProver = (CVC3TheoremProver) proverFactory.
				newProver(booleanExprFalse);
		
		// if false, then false (valid)
		assertEquals(ValidityResult.ResultType.YES,
				cvcProver.valid(booleanExprFalse).getResultType());
		// if false, then true (valid)
		assertEquals(ValidityResult.ResultType.YES,
				cvcProver.valid(booleanExprTrue).getResultType());	
		
		// including for coverage
		Map<SymbolicExpression, Expr> map = cvcProver.expressionMap();
	}
	
	@Test
	@Ignore
	public void testNewCVCName() {
		
		//use expression factory to get APPLY expression in order to use translate to hit switch case
		//in order to publicly get to translateFunction which uses newCvcName
		SymbolicExpression x = universe
				.symbolicConstant(universe.stringObject("x"),
						universe.booleanType());
		
		SymbolicExpression x2 = universe
				.symbolicConstant(universe.stringObject("x"),
						universe.booleanType());
		
		Expr xTranslateSym = cvcProver.translate(x);
		Expr xTranslateSym2 = cvcProver.translate(x2);
		
		assertEquals(xTranslateSym.toString(), "x");
		assertEquals(xTranslateSym2.toString(), "x'1");

	}
	
	
}
