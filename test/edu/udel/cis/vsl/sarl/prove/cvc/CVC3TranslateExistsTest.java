package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cvc3.Expr;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3TranslateExistsTest {
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
	private static NumericExpression oneInt = universe.integer(1);
	private static NumericExpression twoInt = universe.integer(2);
	private static NumericExpression fiveInt = universe.integer(5);
	private static BooleanExpression booleanExprTrue = universe
			.trueExpression();
	private static BooleanExpression booleanExprFalse = universe
			.falseExpression();
	// constants
	private static SymbolicConstant e = universe
			.symbolicConstant(universe.stringObject("e"), intType);
	private static SymbolicConstant f = universe
			.symbolicConstant(universe.stringObject("f"), intType);
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
	public void testTranslateExists() {
		// x real
		NumericSymbolicConstant xConstant = (NumericSymbolicConstant) universe.
				symbolicConstant(xString, realType);
		// N real
		StringObject NString = universe.stringObject("N");
		NumericSymbolicConstant NConstant = (NumericSymbolicConstant) universe.
				symbolicConstant(NString, realType);
		
		// 0 < x, (aka x > 0), no greater_than symbolic operator
		BooleanExpression xGreaterZero = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.LESS_THAN, boolType, 
						universe.zeroReal(), xConstant);
		// x < N
		BooleanExpression xLessN = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.LESS_THAN, boolType, 
						xConstant, NConstant);
		// 0 < x < N
		BooleanExpression zeroLessXLessN = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.AND, boolType, xGreaterZero, xLessN);
		// x exists
		SymbolicExpression xExistsExpression = expressionFactory
				.expression(SymbolicOperator.EXISTS, universe.realType(),
						xConstant, zeroLessXLessN);
		
		// CVC3 x expr values
		List<Expr> vars = new ArrayList<Expr>();
		Expr xExpr = cvcProver.translate(xConstant);
		Expr xBoundsExpr = cvcProver.translate(zeroLessXLessN);
		vars.add(xExpr);
		
		Expr existsExpr = cvcProver.translate(xExistsExpression);
		Expr expected = vc.existsExpr(vars, xBoundsExpr);
		assertEquals(expected, existsExpr);
		vars.clear();
		
		// x'1 real
		NumericSymbolicConstant x1Constant = (NumericSymbolicConstant) universe.
				symbolicConstant(xString, realType);
		// 0 < x'1
		xGreaterZero = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.LESS_THAN, boolType, 
						universe.zeroReal(), x1Constant);
		// x'1 exists
		SymbolicExpression x1ExistsExpression = expressionFactory
				.expression(SymbolicOperator.EXISTS, universe.realType(),
						x1Constant, xGreaterZero);
		// x'2 real
		NumericSymbolicConstant x2Constant = (NumericSymbolicConstant) universe.
				symbolicConstant(xString, realType);
		// 1 < x'2 
		BooleanExpression xGreaterOne = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.LESS_THAN, boolType, 
						universe.oneReal(), x2Constant);
		// x'2 exists
		SymbolicExpression x2ExistsExpression = expressionFactory
				.expression(SymbolicOperator.EXISTS, universe.realType(),
						x1Constant, xGreaterOne);
		
		// 0 < x'1 && 1 < x'2
		BooleanExpression andExpression = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.AND, boolType, 
						x1ExistsExpression, x2ExistsExpression);
		// CVC3 Expr values
		Expr x1Expr = cvcProver.translate(x1Constant);
		Expr x2Expr = cvcProver.translate(x2Constant);
		vars.add(x1Expr);
		vars.add(x2Expr);
		Expr x1Exists = cvcProver.translate(x1ExistsExpression);
		Expr x2Exists = cvcProver.translate(x2ExistsExpression);
		Expr x1and2Exist = vc.andExpr(x1Exists, x2Exists);
//		Expr x1and2Exist = cvcProver.translate(andExpression);
		expected = vc.existsExpr(vars, x1and2Exist);
		assertEquals(expected, x1and2Exist);
	}
	
	@Test
	public void testTranslateExistsAnd() {
		// (E x . x > 0) ^ (E x . x < 10)
		// to
	    // (E x . x > 0) ^ (E x'1 . x'1 < 10)
		
		// sarl
		// x real
		SymbolicConstant xReal = universe.symbolicConstant(xString, realType);
		// 0 < x
		BooleanExpression xGreaterZero = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.LESS_THAN, boolType, 
						universe.zeroReal(), xReal);
		// (E x . x > 0)
		BooleanExpression xExists = universe.exists(xReal, xGreaterZero);
		// x'1 real
		SymbolicConstant x1Real = universe.
				symbolicConstant(xString, realType);
		// x'1 < 10
		BooleanExpression x1LessTen = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.LESS_THAN, boolType, 
						x1Real, ten);
		// (E x'1 . x'1 < 10)
		BooleanExpression x1Exists = universe.exists(x1Real, x1LessTen);
		// (E x . x > 0) ^ (E x'1 . x'1 < 10)
		SymbolicExpression xandx1Exist = expressionFactory
				.expression(SymbolicOperator.AND, boolType, xExists, x1Exists);
		// cvc3 translation
		List<Expr> vars = new ArrayList<Expr>();
		Expr x1Expr = cvcProver.translate(xReal);
		Expr x2Expr = cvcProver.translate(x1Real);
		Expr x1GreaterZeroExpr = cvcProver.translate(xGreaterZero);
		Expr x2LessTenExpr = cvcProver.translate(x1LessTen);
		Expr x1ExistsExpr = cvcProver.translate(xExists);
		Expr x2ExistsExpr = cvcProver.translate(x1Exists);
		Expr translateResult = cvcProver.translate(xandx1Exist);
		vars.add(x1Expr);
		vars.clear();
		vars.add(x2Expr);
		Expr expected = vc.andExpr(x1ExistsExpr, x2ExistsExpr);
		out.println("Expected:  " + expected);
		out.println("Result:    " + translateResult);
		out.println("Should be: ((EXISTS (x: REAL) : (0 < x)) AND "
				+ "(EXISTS (x'1: REAL) : (x'1 < 10)))\n");
		assertEquals(expected, translateResult); // misleading results
		// expected: ((EXISTS (x'1: REAL) : (0 < x)) 
		// AND (EXISTS (x'1: REAL) : (x < 10)))
		
		// translateResult: ((EXISTS (x'1: REAL) : (0 < x)) 
		// AND (EXISTS (x'1: REAL) : (x < 10)))
	}
	
	@Test
	public void testTranslateExistsNested() {
		// TODO
		// (E x . x > 0 ^ (E x . x < 10 ^ (...)))
		// (E x1 . x1 > 0 ^ (E x2 . x2 < 10 ^ (...)))
		
		// sarl
		// x int
		SymbolicConstant xInt = universe.symbolicConstant(xString, intType);
		// 0 < x aka (x > 0)
		BooleanExpression xGTzero = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.LESS_THAN, boolType, 
						universe.zeroInt(), xInt);
		// x2 real
		SymbolicConstant xInt2 = universe.symbolicConstant(xString, intType);
		// x2 < 10
		BooleanExpression xLTten = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.LESS_THAN, boolType, 
						xInt2, ten);
		// x3 int
		SymbolicConstant xInt3 = universe.symbolicConstant(xString, intType);
		// x < 0
		BooleanExpression xLTzero = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.LESS_THAN, boolType, 
						xInt3, universe.zeroInt());
		
		// this and method returns in a random order, so doesn't help for testing
		BooleanExpression xSeriesExists = universe
				.and(Arrays.asList(new BooleanExpression[]
						{universe.exists(xInt, xGTzero),
						universe.exists(xInt2, xLTten),
						universe.exists(xInt3, xLTzero)}));

		// translate
		Expr translateResult = cvcProver.translate(xSeriesExists);
		out.println("Result:    " + translateResult);
		out.println("Should be: ((EXISTS (x: INT) : (0 < x)) AND "
				+ "(EXISTS (x'1: INT) : (x'1 < 10)) AND "
				+ "(EXISTS (x'2: INT) : (x'2 < 0)))");
		// cvc3
		List<Expr> vars = new ArrayList<Expr>();
	}
}
