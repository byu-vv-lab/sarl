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
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
import edu.udel.cis.vsl.sarl.collections.common.PcollectionsSymbolicSequence;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3TranslateReadWriteTest {
	
	// Static fields: instantiated once and used for all tests...
	private static PrintStream out = System.out;
	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();
	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);
	private static ExpressionFactory expressionFactory = factorySystem
			.expressionFactory();
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
	public void testTranslateDenseTupleWrite(){
		
		Expr oneIntExpr = cvcProver.translate(oneInt);
		Expr twoIntExpr = cvcProver.translate(twoInt);
		Expr fiveIntExpr = cvcProver.translate(fiveInt);
		
		List<SymbolicExpression> tupleList = new ArrayList<SymbolicExpression>(); 
		tupleList.add(oneInt);
		tupleList.add(twoInt);
		tupleList.add(fiveInt);
		List<SymbolicType> tupleType = new ArrayList<SymbolicType>();
		tupleType.add(intType);
		tupleType.add(intType);
		tupleType.add(intType);
		
		SymbolicSequence<SymbolicExpression> tupleSequenceWrite = 
				new PcollectionsSymbolicSequence<SymbolicExpression>();
		tupleSequenceWrite = tupleSequenceWrite.add(fiveInt);
		tupleSequenceWrite = tupleSequenceWrite.add(oneInt);
		tupleSequenceWrite = tupleSequenceWrite.add(twoInt);
		
		SymbolicExpression s1 = universe.tuple(universe
				.tupleType(universe.stringObject("tuple"), tupleType), tupleList);
		SymbolicExpression s3 = expressionFactory
				.expression(SymbolicOperator.DENSE_TUPLE_WRITE, s1.type(), s1, tupleSequenceWrite);
		SymbolicExpression s4 = expressionFactory
				.expression(SymbolicOperator.TUPLE_READ, s3.type(), s3, universe.intObject(0));
		SymbolicExpression s5 = expressionFactory
				.expression(SymbolicOperator.TUPLE_READ, s3.type(), s3, universe.intObject(1));
		SymbolicExpression s6 = expressionFactory
				.expression(SymbolicOperator.TUPLE_READ, s3.type(), s3, universe.intObject(2));
		
		Expr tupleNumber1 = cvcProver.translate(s4);
		Expr tupleNumber2 = cvcProver.translate(s5);
		Expr tupleNumber3 = cvcProver.translate(s6);
		
		Expr equation1 = vc.eqExpr(tupleNumber1, fiveIntExpr);
		Expr equation2 = vc.eqExpr(tupleNumber2, oneIntExpr);
		Expr equation3 = vc.eqExpr(tupleNumber3, twoIntExpr);
	
		assertEquals(QueryResult.VALID, vc.query(equation1));
		assertEquals(QueryResult.VALID, vc.query(equation2));
		assertEquals(QueryResult.VALID, vc.query(equation3));
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
}
