package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cvc3.Expr;
import cvc3.QueryResult;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3TranslateMathTest {
	
	// Static fields: instantiated once and used for all tests...
	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();
	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);
	private static ExpressionFactory expressionFactory = factorySystem
			.expressionFactory();
	// types
	private static SymbolicRealType realType = universe.realType();
	private static SymbolicIntegerType intType = universe.integerType();
	// expressions
	private static NumericExpression ten = universe.rational(10);
	private static NumericExpression five = universe.rational(5);
	private static NumericExpression two = universe.rational(2);
	private static NumericExpression one = universe.rational(1);
	private static NumericExpression oneInt = universe.integer(1);
	private static BooleanExpression booleanExprTrue = universe
			.trueExpression();
	// constants
	private static SymbolicConstant e = universe
			.symbolicConstant(universe.stringObject("e"), intType);
	private static SymbolicConstant f = universe
			.symbolicConstant(universe.stringObject("f"), intType);
	private static SymbolicConstant g = universe
			.symbolicConstant(universe.stringObject("g"), intType);
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
	 * testTranslateIntegerDivision creates two numeric expressions, one using the symbolic operator
	 * INT_DIVIDE, the other using MODULO, and two symbolic constants. The test compares the validity checker
	 * and the queryresult.
	 */
	
	@Test
	public void testTranslateIntegerDivision(){
		
		NumericExpression q = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.INT_DIVIDE, intType, e, f);
		NumericExpression r = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.MODULO, intType, e, f);
		
		Expr e2 = cvcProver.translate(e);
		Expr f2 = cvcProver.translate(f);
		Expr q2 = cvcProver.translate(q);
		Expr r2 = cvcProver.translate(r);
		
		Expr equationOne = vc.eqExpr(e2, 
				vc.plusExpr(r2, vc.multExpr(f2, q2)));	//e2 = f2*q2+r2
		Expr equationTwo = vc.leExpr(vc.ratExpr(0), r2); // 0 < r2
		Expr equationThree = vc.ltExpr(r2, f2); // r2 < f2
		
		assertEquals(QueryResult.VALID, vc.query(equationOne));
		assertEquals(QueryResult.VALID, vc.query(equationTwo));
		assertEquals(QueryResult.VALID, vc.query(equationThree));
	}
	
	/**
	 * testTranslateMultiply compares translated numeric expressions
	 * when using the multiply symbolic operator and the validity checker
	 * using multExpr. The test asserts multiple arguments as well.
	 */
	

	@Test(expected=SARLInternalException.class)
	public void testTranslateMultiply() {
		
		Expr oneExpr = cvcProver.translate(one);
		Expr twoExpr = cvcProver.translate(two);
		Expr fiveExpr = cvcProver.translate(five);
		Expr tenExpr = cvcProver.translate(ten);
		
		List<NumericExpression> mulList = new ArrayList<NumericExpression>();
		mulList.add(five);
		mulList.add(ten);
		SymbolicCollection<NumericExpression> mulCollection = universe.basicCollection(mulList);
		
		//One argument
		NumericExpression mulExp1 = (NumericExpression)expressionFactory
				.expression(SymbolicOperator.MULTIPLY, realType, mulCollection);
		Expr expr1 = cvcProver.translate(mulExp1);
		Expr expr2 = vc.multExpr(vc.multExpr(oneExpr, fiveExpr), tenExpr);
		assertEquals(expr2, expr1);
		
		//Two arguments
		NumericExpression mulExp2 = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.MULTIPLY, realType, two, five);
		Expr expr3 = cvcProver.translate(mulExp2);
		Expr expected2 = vc.multExpr(twoExpr, fiveExpr);
		assertEquals(expected2, expr3);
		
		//More than two arguments
		NumericExpression mulExp3 = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.MULTIPLY, realType, two, five, ten);
		cvcProver.translate(mulExp3);
	}
	
	/**
	 * testTranslateMultiplySymbolic compares translated symbolic constant expressions
	 * when using the multiply symbolic operator and the validity checker
	 * using multExpr. The test asserts multiple arguments as well.
	 */
	
	@Test(expected=SARLInternalException.class)
	public void testTranslateMultiplySymbolic() {
		
		Expr oneExpr = cvcProver.translate(oneInt);
		Expr eExpr = cvcProver.translate(e);
		Expr fExpr = cvcProver.translate(f);
		
		List<SymbolicConstant> mulList = new ArrayList<SymbolicConstant>();
		mulList.add(e);
		mulList.add(f);
		SymbolicCollection<SymbolicConstant> mulCollection = universe.basicCollection(mulList);
		
		//One argument
		NumericExpression mulExp1 = (NumericExpression)expressionFactory
				.expression(SymbolicOperator.MULTIPLY, intType, mulCollection);
		
		Expr expr1 = cvcProver.translate(mulExp1);
		Expr expr2 = vc.multExpr(vc.multExpr(oneExpr, eExpr), fExpr);
		assertEquals(expr2, expr1);
		
		//Two arguments
		NumericExpression mulExp2 = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.MULTIPLY, intType, e, f);
		Expr expr3 = cvcProver.translate(mulExp2);
		Expr expected2 = vc.multExpr(eExpr, fExpr);
		assertEquals(expected2, expr3);
		
		//More than two arguments
		NumericExpression mulExp3 = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.MULTIPLY, intType, e, f, g);
		cvcProver.translate(mulExp3);
	}
}
