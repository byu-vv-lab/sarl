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
	
	@Test
	public void testTranslateTupleWrite(){
		
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
				.expression(SymbolicOperator.TUPLE_WRITE, s1.type(), s1, universe.intObject(1), tenInt);
		SymbolicExpression s3 = universe.tupleRead(s2, universe.intObject(1));
		
		Expr expr1 = cvcProver.translate(s3);
		Expr expr2 = cvcProver.translate(ten);
		Expr expr3 = vc.eqExpr(expr2, expr1);
		assertEquals(QueryResult.VALID, vc.query(expr3));
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
	public void testTranslateDenseArrayWriteComplete(){
		
		Expr zeroExpr = cvcProver.translate(zero);
		Expr oneExpr = cvcProver.translate(one);
		Expr fiveExpr = cvcProver.translate(five);
		Expr tenExpr = cvcProver.translate(ten);
		
		List<SymbolicExpression> array = new ArrayList<SymbolicExpression>(3);
		array.add(0, two);
		array.add(1, five);
		array.add(2, ten);
		
		List<SymbolicExpression> newArray = new ArrayList<SymbolicExpression>(2);
		newArray.add(0, five);
		newArray.add(1, ten);
		
		SymbolicExpression regularArray = universe.array(realType, array);
		SymbolicExpression s1 = universe.denseArrayWrite(regularArray, newArray);
		Expr cvcArray = cvcProver.translate(s1);
		
		Expr cvcArray2 = cvcProver.translate(regularArray);
		Expr cvcArray3 = vc.writeExpr(cvcArray2, zeroExpr, fiveExpr);
		Expr expected = vc.writeExpr(cvcArray3, oneExpr, tenExpr);
		assertEquals(expected, cvcArray);
	}
	
	@Test
	public void translateDenseArrayWriteIncomplete(){
		
		SymbolicType incompleteArrayType = universe.arrayType(realType);
		SymbolicExpression a = universe.symbolicConstant(universe
				.stringObject("a"), incompleteArrayType);
		
		List<SymbolicExpression> newArray = new ArrayList<SymbolicExpression>();
		newArray.add(0, five);
		newArray.add(1, ten);
		newArray.add(2, two);

		SymbolicExpression s1 = universe.denseArrayWrite(a, newArray);
		SymbolicExpression s2 = expressionFactory.expression(SymbolicOperator.ARRAY_READ, a.type(), s1, zeroInt);
		SymbolicExpression s3 = expressionFactory.expression(SymbolicOperator.ARRAY_READ, a.type(), s1, oneInt);
		SymbolicExpression s4 =	expressionFactory.expression(SymbolicOperator.ARRAY_READ, a.type(), s1, twoInt);
		
		Expr expr1 = cvcProver.translate(s2);
		Expr expr2 = cvcProver.translate(s3);
		Expr expr3 = cvcProver.translate(s4);
		Expr expr4 = cvcProver.translate(five);
		Expr expr5 = cvcProver.translate(ten);
		Expr expr6 = cvcProver.translate(two);
		
		Expr equation1 = vc.eqExpr(expr1, expr4);
		Expr equation2 = vc.eqExpr(expr2, expr5);
		Expr equation3 = vc.eqExpr(expr3, expr6);
		
		assertEquals(QueryResult.VALID, vc.query(equation1));
		assertEquals(QueryResult.VALID, vc.query(equation2));
		assertEquals(QueryResult.VALID, vc.query(equation3));
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
		Expr tenExpr = cvcProver.translate(ten);
		
		List<NumericExpression> collect = new ArrayList<NumericExpression>();
		collect.add(two);
		collect.add(five);
		collect.add(ten);

		NumericExpression mulExp1 = universe.multiply(collect);
		out.println(mulExp1);
		Expr expr1 = cvcProver.translate(mulExp1);
		out.println(expr1);
		Expr expr2 = vc.multExpr(twoExpr, vc.multExpr(fiveExpr, tenExpr));
		Expr expected1 = vc.simplify(expr2);
		assertEquals(expected1, expr1);
		
		NumericExpression mulExp2 = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.MULTIPLY, realType, two, five);
		Expr expr3 = cvcProver.translate(mulExp2);
		Expr expected2 = vc.multExpr(twoExpr, fiveExpr);
		assertEquals(expected2, expr3);
		
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
	public void testTranslateType() {
		// cvc3 int array (with int index type)
		Type intArrayDataType = vc.arrayType(vc.intType(), vc.intType());
		// give extent, along with array type in a tuple
		Type expected = vc.tupleType(vc.intType(), intArrayDataType);
		out.println("expected: " + expected);
		Type translateResult = cvcProver.translateType(intArrayType);
		out.println("translateResult: " + translateResult);
		int eType = expected.arity();
		out.println("expected Type: " + eType);
		int trType = translateResult.arity();
		out.println("translateResult Type: " + trType);
		assertEquals(eType, trType);
		
		// cvc3 tuple
		List<SymbolicType> typesList = new ArrayList<SymbolicType>();
		typesList.add(intType);
		typesList.add(intType);
		typesList.add(realType);
		SymbolicTypeSequence types = universe.typeSequence(typesList);
		StringObject name = universe.stringObject("twoIntRealTuple");
		SymbolicType twoIntRealTupleType = universe.tupleType(name, types);
		translateResult = cvcProver.translateType(twoIntRealTupleType);
		
		List<Type> cvc3Types = new ArrayList<Type>();
		cvc3Types.add(vc.intType());
		cvc3Types.add(vc.intType());
		cvc3Types.add(vc.realType());
		expected = vc.tupleType(cvc3Types);
		out.println("expected: " + expected);
		out.println("translateResult: " + translateResult);
		
		eType = expected.arity();
		trType = translateResult.arity();
		out.println("expected type: " + eType);
		out.println("translateResult type: " + trType);
		assertEquals(eType, trType);
	}
	
}
