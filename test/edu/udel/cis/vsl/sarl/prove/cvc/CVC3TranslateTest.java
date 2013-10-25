package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cvc3.Expr;
import cvc3.QueryResult;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3TranslateTest {
	
	// Static fields: instantiated once and used for all tests...
	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();
	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);
	private static ExpressionFactory expressionFactory = factorySystem
			.expressionFactory();
	private static SymbolicIntegerType intType = universe.integerType();
	private static SymbolicType realType = universe.realType();
	private static SymbolicType boolType = universe.booleanType();
	// expressions
	private static NumericExpression two = universe.rational(2);
	private static NumericExpression one = universe.rational(1);
	private static NumericExpression oneFiveDouble = universe.rational(1.5);
	private static NumericExpression oneInt = universe.integer(1);
	private static BooleanExpression booleanExprTrue = universe
			.trueExpression();
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
	public void testTranslateCastRealToInt(){
		Expr oneIntExpr = cvcProver.translate(oneInt);

		SymbolicExpression castExp = expressionFactory
				.expression(SymbolicOperator.CAST, intType, one);
		Expr expr4 = cvcProver.translate(castExp);
		Expr expected4 = oneIntExpr;
		assertEquals(expected4, expr4);
	}
	
	@Test
	public void testTranslateCastIntToReal(){
		Expr oneRealExpr = cvcProver.translate(one);

		SymbolicExpression castExp = expressionFactory
				.expression(SymbolicOperator.CAST, realType, oneInt);
		Expr expr4 = cvcProver.translate(castExp);
		Expr expected4 = oneRealExpr;
		assertEquals(expected4, expr4);
	}

	@Test
	public void testTranslateCastDoubleToInt(){
		Expr oneFiveDoubleExpr = cvcProver.translate(oneFiveDouble);
		
		SymbolicExpression castExp = expressionFactory
				.expression(SymbolicOperator.CAST, intType, oneFiveDouble);
		Expr expr4 = cvcProver.translate(castExp);
		Expr expr5 = vc.eqExpr(expr4, oneFiveDoubleExpr);
		assertEquals(QueryResult.VALID, vc.query(expr5));
	}
	
	@Test
	public void testTranslateCond(){	
		Expr trueExpr = cvcProver.translate(booleanExprTrue);
		Expr oneExpr = cvcProver.translate(one);
		Expr twoExpr = cvcProver.translate(two);

		SymbolicExpression condExp = expressionFactory
				.expression(SymbolicOperator.COND, boolType, booleanExprTrue, 
						one, two);
		Expr expr5 = cvcProver.translate(condExp);
		Expr expected5 = vc.iteExpr(trueExpr, oneExpr, twoExpr);
		assertEquals(expected5, expr5);
	}
	
	@Test
	public void testTranslateNot(){
		SymbolicExpression notExp = expressionFactory
				.expression(SymbolicOperator.NOT, boolType, booleanExprTrue);
		Expr translateResult = cvcProver.translate(notExp);
		Expr expected = vc.notExpr(vc.trueExpr());
		assertEquals(expected, translateResult);
	}
}
