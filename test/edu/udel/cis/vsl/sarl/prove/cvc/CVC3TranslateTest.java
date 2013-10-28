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
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
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
	private static NumericExpression twoInt = universe.integer(2);
	private static BooleanExpression booleanExprTrue = universe
			.trueExpression();
	//SymbolicConstants
	private static SymbolicConstant eReal = universe.symbolicConstant(universe.stringObject("e"), realType);
	private static SymbolicConstant eInt = universe.symbolicConstant(universe.stringObject("e"), intType);
	private static SymbolicConstant fReal = universe.symbolicConstant(universe.stringObject("f"), realType);
	private static SymbolicConstant fInt = universe.symbolicConstant(universe.stringObject("f"), intType);
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
	
	/**
	 * testTranslateCastRealToInt translates an integer numeric expression; and it also uses the
	 * symbolic operator CAST to create a symbolic expression. The test compares the created
	 * expr and the translated symbolic expression that was casted.
	 */

	@Test
	public void testTranslateCastRealToInt(){
		Expr oneIntExpr = cvcProver.translate(oneInt);

		SymbolicExpression castExp = expressionFactory
				.expression(SymbolicOperator.CAST, intType, one);
		Expr expr4 = cvcProver.translate(castExp);
		assertEquals(oneIntExpr, expr4);
	}
	
	/**
	 * testTranslateCastIntToReal translates an rational numeric expression; and it also uses the
	 * symbolic operator CAST to create a symbolic expression. The test compares the created
	 * expr and the translated symbolic expression that was casted.
	 */
	
	@Test
	public void testTranslateCastIntToReal(){
		Expr oneRealExpr = cvcProver.translate(one);

		SymbolicExpression castExp = expressionFactory
				.expression(SymbolicOperator.CAST, realType, oneInt);
		Expr expr4 = cvcProver.translate(castExp);
		assertEquals(oneRealExpr, expr4);
	}
	
	/**
	 * testTranslateCastDoubleToInt translates a double rational numeric expression; and it also uses the
	 * symbolic operator CAST to create a symbolic expression. The test compares the created
	 * expr and the translated symbolic expression that was casted.
	 */

	@Test
	public void testTranslateCastDoubleToInt(){
		Expr oneFiveDoubleExpr = cvcProver.translate(oneFiveDouble);
		
		SymbolicExpression castExp = expressionFactory
				.expression(SymbolicOperator.CAST, intType, oneFiveDouble);
		Expr expr4 = cvcProver.translate(castExp);
		Expr expr5 = vc.eqExpr(expr4, oneFiveDoubleExpr);
		assertEquals(QueryResult.VALID, vc.query(expr5));
	}
	
	/**
	 * testTranslateCondSymbolicInt translates a boolean expression and two symbolic int constants.
	 * The test creates a symbolic expression using the symbolic operator COND and the translated variables.
	 * The test compares the validity checker using .iteExpr and the translation of the symbolic expression.
	 */
	
	@Test
	public void testTranslateCondSymbolicInt(){	
		Expr trueExpr = cvcProver.translate(booleanExprTrue);
		Expr eExpr = cvcProver.translate(eInt);
		Expr fExpr = cvcProver.translate(fInt);

		SymbolicExpression condExp = expressionFactory
				.expression(SymbolicOperator.COND, boolType, booleanExprTrue, 
						eInt, fInt);
		Expr expr5 = cvcProver.translate(condExp);
		Expr expected5 = vc.iteExpr(trueExpr, eExpr, fExpr);
		assertEquals(expected5, expr5);
	}
	
	/**
	 * testTranslateCondSymbolicInt translates a boolean expression and two symbolic real constants.
	 * The test creates a symbolic expression using the symbolic operator COND and the translated variables.
	 * The test compares the validity checker using .iteExpr and the translation of the symbolic expression.
	 */
	
	@Test
	public void testTranslateCondSymbolicReal(){	
		Expr trueExpr = cvcProver.translate(booleanExprTrue);
		Expr eExpr = cvcProver.translate(eReal);
		Expr fExpr = cvcProver.translate(fReal);

		SymbolicExpression condExp = expressionFactory
				.expression(SymbolicOperator.COND, boolType, booleanExprTrue, 
						eReal, fReal);
		Expr expr5 = cvcProver.translate(condExp);
		Expr expected5 = vc.iteExpr(trueExpr, eExpr, fExpr);
		assertEquals(expected5, expr5);
	}
	
	/**
	 * testTranslateCondConcreteInt creates a symbolic expression using translated boolean expression
	 * , translated int numeric constants, and the symbolic operator COND. It then compares the validity checker
	 * using .iteExpr and the translation of that symbolic expression.
	 */
	
	@Test
	public void testTranslateCondConcreteInt(){	
		Expr trueExpr = cvcProver.translate(booleanExprTrue);
		Expr oneExpr = cvcProver.translate(oneInt);
		Expr twoExpr = cvcProver.translate(twoInt);

		SymbolicExpression condExp = expressionFactory
				.expression(SymbolicOperator.COND, boolType, booleanExprTrue, 
						oneInt, twoInt);
		Expr expr5 = cvcProver.translate(condExp);
		Expr expected5 = vc.iteExpr(trueExpr, oneExpr, twoExpr);
		assertEquals(expected5, expr5);
	}
	
	/**
	 * testTranslateCondConcreteInt creates a symbolic expression using translated boolean expression
	 * , translated rational numeric constants, and the symbolic operator COND. It then compares the validity checker
	 * using .iteExpr and the translation of that symbolic expression.
	 */
	
	@Test
	public void testTranslateCondConcreteReal(){	
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
	
	/**
	 * testTranslateNotBoolean creates a symbolic expression using the symbolic operator NOT
	 * anda booleanExprTrue. It then assesses the equality of the translation of the symbolic expression
	 * and the validity checker using .notExpr and .trueExpr.
	 */
	
	@Test
	public void testTranslateNotBoolean(){
		SymbolicExpression notExp = expressionFactory
				.expression(SymbolicOperator.NOT, boolType, booleanExprTrue);
		Expr translateResult = cvcProver.translate(notExp);
		Expr expected = vc.notExpr(vc.trueExpr());
		assertEquals(expected, translateResult);
	}

}
