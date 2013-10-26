package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cvc3.Expr;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3TranslateDivisionTest {

	// Static fields: instantiated once and used for all tests...
	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();
	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);
	private static ExpressionFactory expressionFactory = factorySystem
			.expressionFactory();
	// types
	private static SymbolicRealType realType = universe.realType();
	// expressions
	private static NumericExpression two = universe.rational(2);
	private static NumericExpression one = universe.rational(1);
	private static BooleanExpression booleanExprTrue = universe
			.trueExpression();
	//SymbolicConstants
	private static SymbolicConstant e = universe.symbolicConstant(universe.stringObject("e"), realType);
	private static SymbolicConstant f = universe.symbolicConstant(universe.stringObject("f"), realType);
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
	public void testTranslateDivision(){
		Expr oneExpr = cvcProver.translate(one);
		Expr twoExpr = cvcProver.translate(two);

		NumericExpression divExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.DIVIDE, realType, one, two);
		Expr expr6 = cvcProver.translate(divExp);
		Expr expected6 = vc.divideExpr(oneExpr, twoExpr);
		assertEquals(expected6, expr6);
	}
	
	@Test
	public void testTranslateDivisionSymbolic(){
		Expr eExpr = cvcProver.translate(e);
		Expr fExpr = cvcProver.translate(f);

		NumericExpression divExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.DIVIDE, realType, e, f);
		Expr expr6 = cvcProver.translate(divExp);
		Expr expected6 = vc.divideExpr(eExpr, fExpr);
		assertEquals(expected6, expr6);
	}
}
