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
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

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

		NumericExpression addExp1 = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.ADD, realType, one, two);
		Expr addExpr1 = cvcProver.translate(addExp1);
		Expr addExpected1 = vc.plusExpr(oneExpr, twoExpr);
		assertEquals(addExpected1, addExpr1);
				
		NumericExpression addExp3 = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.ADD, realType, one, two, five);
		cvcProver.translate(addExp3);
	}
	
	@Test(expected = SARLInternalException.class)
	public void testTranslateAnd(){
		Expr trueExpr = cvcProver.translate(booleanExprTrue);
		Expr falseExpr = cvcProver.translate(booleanExprFalse);

		BooleanExpression andExp = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.AND, boolType, 
						booleanExprTrue, booleanExprTrue);
		Expr expr2 = cvcProver.translate(andExp);
		Expr expected2 = vc.andExpr(trueExpr, trueExpr);
		assertEquals(expected2, expr2);
		
		BooleanExpression andExp2 = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.AND, boolType, 
						booleanExprTrue, booleanExprFalse);
		Expr expr3 = cvcProver.translate(andExp2);
		Expr expected3 = vc.andExpr(trueExpr, falseExpr);
		assertEquals(expected3, expr3);
		
		BooleanExpression andExp3 = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.AND, boolType, booleanExprTrue, 
						booleanExprFalse, booleanExprFalse);
		cvcProver.translate(andExp3);
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
	public void testTranslateNot(){

		BooleanExpression notExp = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.NOT, boolType, booleanExprTrue);
		Expr expr8 = cvcProver.translate(notExp);
		Expr expected8 = vc.notExpr(cvcProver
				.translate((SymbolicExpression) notExp.argument(0)));
		assertEquals(expected8, expr8);
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
	public void testTranslateTupleRead() {
		
		List<SymbolicExpression> tupleList = new ArrayList<SymbolicExpression>(); 
		tupleList.add(oneInt);
		tupleList.add(twoInt);
		tupleList.add(fiveInt);
		List<SymbolicType> tupleType = new ArrayList<SymbolicType>();
		tupleType.add(intType);
		tupleType.add(intType);
		tupleType.add(intType);

		SymbolicExpression s1 = universe.tuple(universe
				.tupleType(universe.stringObject("tuple"), tupleType), tupleList);
		SymbolicExpression s2 = expressionFactory
				.expression(SymbolicOperator.TUPLE_READ, s1.type(), 
						s1, universe.intObject(1));
		Expr expr1 = cvcProver.translate(s2);
		Expr expr2 = cvcProver.translate(twoInt);
		Expr expr3 = vc.eqExpr(expr1, expr2);
		
		assertEquals(QueryResult.VALID, vc.query(expr3));
	}
	
	public void testTranslateTupleWrite(){
		
		
	}
	
	@Test
	public void testTranslateArrayWriteComplete(){
		
		List<SymbolicExpression> array = new ArrayList<SymbolicExpression>(3);
		array.add(0, two);
		array.add(1, five);
		array.add(2, ten);
		
		SymbolicExpression newArray = universe.array(realType, array);
		SymbolicExpression translateArray = expressionFactory
				.expression(SymbolicOperator.ARRAY_WRITE, newArray.type(), 
						newArray, universe.integer(0), zero);
		Expr expr = cvcProver.translate(translateArray);
		
		Expr expr2 = cvcProver.translate(newArray);
		Expr zeroModifier = cvcProver.translate(zero);
		Expr expected = vc.writeExpr(expr2, zeroModifier, zeroModifier);
		
		assertEquals(expected, expr);
		
	}
	
	@Test
	public void testTranslateArrayWriteIncomplete(){
		
		SymbolicType incompleteArrayType = universe.arrayType(realType);
		SymbolicExpression a = universe.symbolicConstant(universe
				.stringObject("a"), incompleteArrayType);

		SymbolicExpression s1 = expressionFactory
				.expression(SymbolicOperator.ARRAY_WRITE, a.type(), 
						a, twoInt, five);
		SymbolicExpression s2 = expressionFactory
				.expression(SymbolicOperator.ARRAY_READ, a.type(), 
						s1, twoInt);
		Expr expr1 = cvcProver.translate(s2);
		Expr expr2 = cvcProver.translate(five);
		Expr equation1 = vc.eqExpr(expr1, expr2);
		
		assertEquals(QueryResult.VALID, vc.query(equation1));
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
		
		Expr twoExpr = cvcProver.translate(two);
		Expr fiveExpr = cvcProver.translate(five);
		
		/*
		List<SymbolicExpression> collect = new ArrayList<SymbolicExpression>();
		collect.add(0, two);
		collect.add(1, five);
		
		SymbolicExpression newCollect = universe.array(realType, collect);
		NumericExpression mulExp1 = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.MULTIPLY, realType, newCollect);
		Expr expr1 = cvcProver.translate(mulExp1);
		
		Expr expr2;
		expr2 = vc.ratExpr(1);
		for (SymbolicExpression operand : (SymbolicCollection<?>) newCollect.argument(0)){
			expr2 = vc.multExpr(expr2, cvcProver.translate(operand));
		}
		
		assertEquals(expr2, expr1);
		*/
		
		NumericExpression mulExp2 = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.MULTIPLY, realType, two, five);
		Expr expr3 = cvcProver.translate(mulExp2);
		Expr expected = vc.multExpr(twoExpr, fiveExpr);
		assertEquals(expected, expr3);
		
		NumericExpression mulExp3 = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.MULTIPLY, realType, two, five, ten);
		cvcProver.translate(mulExp3);
	}
	
	@Test
	public void testTranslateOr() {
		// holds cvc3 variables
		List<Expr> list = new ArrayList<Expr>();
		
		// true or true
		BooleanExpression orExpression = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.OR, boolType, 
						booleanExprTrue, booleanExprTrue);
		Expr translateOr = cvcProver.translate(orExpression);
		Expr trueExpr = cvcProver.translate(booleanExprTrue);
		list.add(trueExpr);
		list.add(trueExpr);
		Expr expected = cvcProver.validityChecker().orExpr(list);
		assertEquals(expected, translateOr);
		list.clear();
		
		// true or false
		BooleanExpression orExpression2 = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.OR, boolType, 
						booleanExprTrue, booleanExprFalse);
		translateOr = cvcProver.translate(orExpression2);
		Expr falseExpr = cvcProver.translate(booleanExprFalse);
		list.add(trueExpr);
		list.add(falseExpr);
		expected = vc.orExpr(list);
		assertEquals(expected, translateOr);					  
	}
	
	@Test
	public void testTranslateQuantifier() {
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
		// set up pieces
		// 0 < x
		SymbolicExpression xGreaterZero =  expressionFactory
				.expression(SymbolicOperator.LESS_THAN, boolType, 
						universe.zeroReal(), xReal);
		// (E x . x > 0) ^ (E x . x < 10)
	    // (E x1 . x1 > 0) ^ (E x2 . x2 < 10)
		
		// sarl
		// (E x . x > 0)
		SymbolicExpression xExists = expressionFactory
				.expression(SymbolicOperator.EXISTS, realType, 
						xReal, xGreaterZero);
		// x2 real
		SymbolicConstant x2Real = universe.
				symbolicConstant(xString, realType);
		// x < 10
		SymbolicExpression x2LessTen = expressionFactory
				.expression(SymbolicOperator.LESS_THAN, boolType, 
						x2Real, ten);
		// (E x . x < 10)
		SymbolicExpression x2Exists = expressionFactory
				.expression(SymbolicOperator.EXISTS, realType, 
						x2Real, x2LessTen);
		// (E x . x > 0) ^ (E x . x < 10)
		SymbolicExpression x1andx2Exist = expressionFactory
				.expression(SymbolicOperator.AND, boolType, 
						xExists, x2Exists);
		
		// cvc3 translation
		List<Expr> vars = new ArrayList<Expr>();
		Expr x1Expr = cvcProver.translate(xReal);
		Expr x2Expr = cvcProver.translate(x2Real);
		Expr x1GreaterZeroExpr = cvcProver.translate(xGreaterZero);
		Expr x2LessTenExpr = cvcProver.translate(x2LessTen);
		Expr x1ExistsExpr = cvcProver.translate(xExists);
		Expr x2ExistsExpr = cvcProver.translate(x2Exists);
		Expr translateResult = cvcProver.translate(x1andx2Exist);
		out.println(translateResult);
		vars.add(x1Expr);
		vars.clear();
		vars.add(x2Expr);
		Expr expected = vc.andExpr(x1ExistsExpr, x2ExistsExpr);
		out.println(expected);
		assertEquals(expected, translateResult); // misleading results
		// expected: ((EXISTS (x'1: REAL) : (0 < x)) 
		// AND (EXISTS (x'1: REAL) : (x < 10)))
		
		// translateResult: ((EXISTS (x'1: REAL) : (0 < x)) 
		// AND (EXISTS (x'1: REAL) : (x < 10)))
		
		// should be: ((EXISTS (x'1: REAL) : (0 < x'1))
		// AND (EXISTS (x'2: REAL) : (x'2 < 10)))
		

	}
	
	@Test
	public void testTranslateExistsNested() {
		// TODO
		// (E x . x > 0 ^ (E x . x < 10 ^ (...)))
		// (E x1 . x1 > 0 ^ (E x2 . x2 < 10 ^ (...)))
	}
	
	@Test
	public void testTranslateType() {
		// cvc3 int array (with int index type)
		Type intArrayDataType = vc.arrayType(vc.intType(), vc.intType());
		// give extent, along with array type in a tuple
		Type expected = vc.tupleType(vc.intType(), intArrayDataType);
		Type translateResult = cvcProver.translateType(intArrayType);
		
		// TODO make comparisons that aren't don't assert equality
		// between the translated types directly
		
		// cvc3 tuple
		List<SymbolicType> typesList = new ArrayList<SymbolicType>();
		typesList.add(intType);
		typesList.add(intType);
		SymbolicTypeSequence types = universe.typeSequence(typesList);
		StringObject name = universe.stringObject("twoIntTuple");
		SymbolicType twoIntTupleType = universe.tupleType(name, types);
	}
	
}
