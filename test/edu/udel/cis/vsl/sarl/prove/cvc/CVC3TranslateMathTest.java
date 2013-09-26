package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
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
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3TranslateMathTest {
	// Static fields: instantiated once and used for all tests...
	private static PrintStream out = System.out;
	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();
	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);
	private static ExpressionFactory expressionFactory = factorySystem
			.expressionFactory();
	// objects
	private static StringObject xString = universe.stringObject("x");
	// types
	private static SymbolicRealType realType = universe.realType();
	private static SymbolicIntegerType intType = universe.integerType();
	private static SymbolicType boolType = universe.booleanType();
	private static SymbolicType intArrayType = universe.arrayType(intType);
	// expressions
	private static NumericExpression ten = universe.rational(10);
	private static NumericExpression five = universe.rational(5);
	private static NumericExpression two = universe.rational(2);
	private static NumericExpression one = universe.rational(1);
	private static NumericExpression zero = universe.zeroReal();
	private static NumericExpression zeroInt = universe.zeroInt();
	private static NumericExpression oneInt = universe.integer(1);
	private static NumericExpression twoInt = universe.integer(2);
	private static NumericExpression fiveInt = universe.integer(5);
	private static NumericExpression tenInt = universe.integer(10);
	private static BooleanExpression booleanExprTrue = universe
			.trueExpression();
	private static BooleanExpression booleanExprFalse = universe
			.falseExpression();
	// constants
	private static SymbolicConstant e = universe
			.symbolicConstant(universe.stringObject("e"), intType);
	private static SymbolicConstant f = universe
			.symbolicConstant(universe.stringObject("f"), intType);
	private static SymbolicConstant xReal = universe
			.symbolicConstant(xString, realType);
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
	
	@Test(expected = SARLInternalException.class)
	public void testTranslateAdd(){
		Expr oneExpr = cvcProver.translate(one);
		Expr twoExpr = cvcProver.translate(two);
		Expr fiveExpr = cvcProver.translate(five);

		List<NumericExpression> s1 = new ArrayList<NumericExpression>();
		s1.add(one);
		s1.add(two);
		s1.add(five);
		
		SymbolicCollection<NumericExpression> addList = universe.basicCollection(s1);
		
		NumericExpression addExp1 = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.ADD, realType, one, two);
		Expr addExpr1 = cvcProver.translate(addExp1);
		Expr addExpected1 = vc.plusExpr(oneExpr, twoExpr);
		assertEquals(addExpected1, addExpr1);
		
		NumericExpression addExp2 = universe.add(addList);
		Expr addExpr2 = cvcProver.translate(addExp2);
		
		Expr addExpected2 = vc.plusExpr(oneExpr, twoExpr);
		Expr addExpected3 = vc.plusExpr(addExpected2, fiveExpr);
		Expr addExpected4 = vc.simplify(addExpected3);
		assertEquals(addExpected4, addExpr2);
		
		NumericExpression addExp3 = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.ADD, realType, one, two, five);
		cvcProver.translate(addExp3);
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
	public void testTranslateNegative(){

		NumericExpression negExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.NEGATIVE, intType, one);
		Expr expr7 = cvcProver.translate(negExp);
		Expr expected7 = vc.uminusExpr(cvcProver
				.translate((SymbolicExpression) negExp.argument(0)));
		assertEquals(expected7, expr7);
	}
	
	@Test
	public void testTranslatePower(){
		Expr twoExpr = cvcProver.translate(two);	
		Expr fiveExpr = cvcProver.translate(five);

		NumericExpression powerExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.POWER, realType, two, five);
		Expr expr9 = cvcProver.translate(powerExp);
		Expr expected9 = vc.powExpr(twoExpr, fiveExpr);
		assertEquals(expected9, expr9);
	}
	
	@Test
	public void testTranslateSubtract(){
		Expr oneExpr = cvcProver.translate(one);
		Expr twoExpr = cvcProver.translate(two);

		NumericExpression subExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.SUBTRACT, realType, two, one);
		Expr expr10 = cvcProver.translate(subExp);
		Expr expected10 = vc.minusExpr(twoExpr, oneExpr);
		assertEquals(expected10, expr10);
	}
	
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
}
