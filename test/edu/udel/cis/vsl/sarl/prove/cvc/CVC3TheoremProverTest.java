package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cvc3.Expr;
import cvc3.Type;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3TheoremProverTest {

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
		SymbolicType boolType = universe.booleanType();
		Type t = cvcProver.translateType(boolType);

		assertEquals(t, vc.boolType());
	}

	public void testQuotientRemainderPair() {

		SymbolicExpression numerator = universe.rational(1);
		SymbolicExpression denominator = universe.rational(2);

		// cvcProver.getQuotientRemainderPair(numerator, denominator);
	}

	@Test
	public void testTranslate() {
		SymbolicExpression expr = universe.divide(universe.rational(1),
				universe.rational(2));

		out.println(cvcProver.translate(expr));
	}

	@Test
	public void translateMultiplyTest() {
		NumericExpression mulExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.MULTIPLY, realType, two, five);
		Expr expr = cvcProver.translate(mulExp);
		Expr twoExpr = cvcProver.translate(two);
		Expr fiveExpr = cvcProver.translate(five);
		Expr expected = cvcProver.validityChecker().multExpr(twoExpr, fiveExpr);

		assertEquals(expected, expr);
	}
	
	@Test
	public void validTest() {
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
		
//		assertEquals(ValidityResult.ResultType.MAYBE,
//				cvcProver.valid(booleanExprMaybe).getResultType());
		
	}
}
