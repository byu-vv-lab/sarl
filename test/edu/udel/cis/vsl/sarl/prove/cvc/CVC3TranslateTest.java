package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cvc3.Expr;
import cvc3.Op;
import cvc3.OpMut;
import cvc3.QueryResult;
import cvc3.Type;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject.SymbolicObjectKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class CVC3TranslateTest {
	
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
	public void testTranslateAnd(){
		Expr trueExpr = cvcProver.translate(booleanExprTrue);
		Expr falseExpr = cvcProver.translate(booleanExprFalse);

		List<BooleanExpression> s1 = new ArrayList<BooleanExpression>();
		s1.add(booleanExprFalse);
		s1.add(booleanExprTrue);
		
		SymbolicCollection<BooleanExpression> boolList = universe.basicCollection(s1);
		BooleanExpression andExp1 = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.AND, boolType, boolList);
		Expr expr1 = cvcProver.translate(andExp1);
		Expr expected1 = vc.andExpr(falseExpr, trueExpr);
		assertEquals(expected1, expr1);
		
		BooleanExpression andExp2 = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.AND, boolType, 
						booleanExprTrue, booleanExprTrue);
		Expr expr2 = cvcProver.translate(andExp2);
		Expr expected2 = vc.andExpr(trueExpr, trueExpr);
		assertEquals(expected2, expr2);
		
		BooleanExpression andExp3 = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.AND, boolType, 
						booleanExprTrue, booleanExprFalse);
		Expr expr3 = cvcProver.translate(andExp3);
		Expr expected3 = vc.andExpr(trueExpr, falseExpr);
		assertEquals(expected3, expr3);
		
		BooleanExpression andExp4 = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.AND, boolType, booleanExprTrue, 
						booleanExprFalse, booleanExprFalse);
		cvcProver.translate(andExp4);
	}

	@Test
	public void testTranslateCast(){
		Expr oneIntExpr = cvcProver.translate(oneInt);

		SymbolicExpression castExp = expressionFactory
				.expression(SymbolicOperator.CAST, intType, one);
		Expr expr4 = cvcProver.translate(castExp);
		Expr expected4 = oneIntExpr;
		assertEquals(expected4, expr4);
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
	public void testTranslateFunctionSymbolicConstant(){
	
		List <SymbolicType> types = new ArrayList<SymbolicType>();
		types.add(intType);
		
		SymbolicType type = universe.functionType(types, intType);
		SymbolicConstant symFunction = universe
				.symbolicConstant(universe.stringObject("SymbolicConstant"), type);
		
		List<SymbolicExpression> funList = new ArrayList<SymbolicExpression>();
		funList.add(oneInt); 
		SymbolicCollection<SymbolicExpression> collect = universe.basicCollection(funList);
		
		SymbolicExpression e = expressionFactory
				.expression(SymbolicOperator.APPLY, symFunction.type(), symFunction, collect);

		Expr expr = cvcProver.translate(e);
		Expr expr1 = expr.getOpExpr();
		Expr expr2 = expr.getChild(0);

		Expr oneIntExpr = cvcProver.translate(oneInt);
		
		Expr equationOne = vc.eqExpr(expr1, vc.exprFromString("SymbolicConstant"));
		Expr equationTwo = vc.eqExpr(expr2, oneIntExpr);

		assertEquals(QueryResult.VALID, vc.query(equationOne));
		assertEquals(QueryResult.VALID, vc.query(equationTwo));
	}
	
	@Test
	public void testTranslateFunctionLambda(){
		
		List <SymbolicType> types = new ArrayList<SymbolicType>();
		types.add(intType);
		
		SymbolicType type = universe.functionType(types, intType);
		List<SymbolicExpression> funList = new ArrayList<SymbolicExpression>();
		funList.add(oneInt); 
		SymbolicCollection<SymbolicExpression> collect = universe.basicCollection(funList);
		
		SymbolicExpression lamFunction = universe.lambda(e, f);
		SymbolicExpression e = expressionFactory
				.expression(SymbolicOperator.APPLY, type, lamFunction, collect);
		
		Expr expr = cvcProver.translate(e);
		Expr equationOne = vc.eqExpr(expr, vc.exprFromString("(LAMBDA (e: INT): f)(1)"));
		
		assertEquals(QueryResult.VALID, vc.query(equationOne));
	}
	
	@Test(expected = SARLInternalException.class)
	public void testTranslateFunctionUnexpected(){
	
		List <SymbolicType> types = new ArrayList<SymbolicType>();
		types.add(intType);
		
		SymbolicType type = universe.functionType(types, intType);
		SymbolicExpression symFunction = universe
				.symbolicConstant(universe.stringObject("SymbolicConstant"), type);
		
		List<SymbolicExpression> funList = new ArrayList<SymbolicExpression>();
		funList.add(symFunction);
		
		SymbolicExpression d = universe.array(symFunction.type(), funList);
		SymbolicExpression e = expressionFactory
				.expression(SymbolicOperator.APPLY, d.type(), d);
		
		cvcProver.translate(e);
	}

	@Test
	public void testTranslateNot(){
		SymbolicExpression notExp = expressionFactory
				.expression(SymbolicOperator.NOT, boolType, booleanExprTrue);
		Expr translateResult = cvcProver.translate(notExp);
		Expr expected = vc.notExpr(vc.trueExpr());
		assertEquals(expected, translateResult);
	}
	
	@Test
	public void testTranslateOr() {
		// holds cvc3 variables
		List<Expr> list = new ArrayList<Expr>();
		
		// true or true
		SymbolicExpression orExpression = expressionFactory
				.expression(SymbolicOperator.OR, boolType,
						booleanExprTrue, booleanExprTrue);
		Expr translateResult = cvcProver.translate(orExpression);
		
		Expr trueExpr = cvcProver.translate(booleanExprTrue);
		list.add(trueExpr);
		list.add(trueExpr);
		Expr expected = vc.orExpr(list);
		
		// diagnostics
		out.println("expected: " + expected);
		out.println("translateResult: " + translateResult);
		out.println();
		assertEquals(expected, translateResult);
		list.clear();
		
		// true or false
		SymbolicExpression orExpression2 = expressionFactory
				.expression(SymbolicOperator.OR, boolType,
						booleanExprTrue, booleanExprFalse);
		translateResult = cvcProver.translate(orExpression2);
		
		Expr falseExpr = cvcProver.translate(booleanExprFalse);
		list.add(trueExpr);
		list.add(falseExpr);
		expected = vc.orExpr(list);
		
		// diagnostics
		out.println("expected: " + expected);
		out.println("translateResult: " + translateResult);
		out.println();
		assertEquals(expected, translateResult);
	}

}
