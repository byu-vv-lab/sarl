package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cvc3.Expr;
import cvc3.QueryResult;
import cvc3.Type;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject.SymbolicObjectKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3TheoremProverTest {

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
	private static NumericExpression oneInt = universe.integer(1);
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
	public void testBoolean() {
		SymbolicType boolType = universe.booleanType();
		Type t = cvcProver.translateType(boolType);

		assertEquals(t, vc.boolType());
	}

	public void testQuotientRemainderPair() {

		SymbolicExpression numerator = universe.rational(1);
		SymbolicExpression denominator = universe.rational(2);

		// cvcProver.getQuotientRemainderPair(numerator, denominator);
	}
	
	@Test
	public void testToString(){
		
		String expected = "CVC3TheoremProver";
		assertEquals(expected, cvcProver.toString());
		
		String notExpected = "This is wrong";
		assertFalse(notExpected.equals(cvcProver.toString()));
		
	}

	@Test
	public void testTranslate() {
		
		Expr oneExpr = cvcProver.translate(one);
		Expr twoExpr = cvcProver.translate(two);
		Expr oneIntExpr = cvcProver.translate(oneInt);
		Expr trueExpr = cvcProver.translate(booleanExprTrue);
		Expr falseExpr = cvcProver.translate(booleanExprFalse);
		
		//Addition
		NumericExpression addExp = (NumericExpression) expressionFactory.expression(SymbolicOperator.ADD, realType, one, two);
		Expr expr = cvcProver.translate(addExp);
		Expr expected = vc.plusExpr(oneExpr, twoExpr);
		assertEquals(expected, expr);
		
		//And
		BooleanExpression andExp = (BooleanExpression) expressionFactory.expression(SymbolicOperator.AND, boolType, booleanExprTrue, booleanExprTrue);
		Expr expr2 = cvcProver.translate(andExp);
		Expr expected2 = vc.andExpr(trueExpr, trueExpr);
		assertEquals(expected2, expr2);
		BooleanExpression andExp2 = (BooleanExpression) expressionFactory.expression(SymbolicOperator.AND, boolType, booleanExprTrue, booleanExprFalse);
		Expr expr3 = cvcProver.translate(andExp2);
		Expr expected3 = vc.andExpr(trueExpr, falseExpr);
		assertEquals(expected3, expr3);
		
		//Cast
		SymbolicExpression castExp = expressionFactory.expression(SymbolicOperator.CAST, intType, one);
		Expr expr4 = cvcProver.translate(castExp);
		Expr expected4 = oneIntExpr;
		assertEquals(expected4, expr4);
		
		//Division
		NumericExpression divExp = (NumericExpression) expressionFactory.expression(SymbolicOperator.DIVIDE, realType, one, two);
		Expr expr5 = cvcProver.translate(divExp);
		Expr expected5 = vc.divideExpr(oneExpr, twoExpr);
		assertEquals(expected5, expr5);
		
		
	}
	
	@Test
	public void testTranslateArrayWrite(){
		
		//Array of a fixed size
		List<SymbolicExpression> array = new ArrayList<SymbolicExpression>(3);
		array.add(0, two);
		array.add(1, five);
		array.add(2, ten);
		
		SymbolicExpression newArray = universe.array(realType, array);
		
		SymbolicExpression translateArray = expressionFactory.expression(SymbolicOperator.ARRAY_WRITE, newArray.type(), newArray, universe.integer(0), zero);
		Expr expr = cvcProver.translate(translateArray);
		
		Expr expr2 = cvcProver.translate(newArray);
		
		Expr zeroModifier = cvcProver.translate(zero);
		Expr expected = vc.writeExpr(expr2, zeroModifier, zeroModifier);
		
		assertEquals(expected, expr);
		
		//Array of unfixed size
		List<SymbolicExpression> unfixedArray = new ArrayList<SymbolicExpression>();
		array.add(0, two);
		array.add(1, five);
		array.add(2, ten);
		
		SymbolicExpression newArray2 = universe.array(realType, unfixedArray);
		
		SymbolicExpression translateArray2 = expressionFactory.expression(SymbolicOperator.ARRAY_WRITE, newArray2.type(), newArray2, universe.integer(0), zero);
		Expr expr3 = cvcProver.translate(translateArray2);
		
		Expr expr4 = cvcProver.translate(newArray2);
		
		Expr expected2 = vc.writeExpr(expr4, zeroModifier, zeroModifier);
		
		assertEquals(expected2, expr3);
		
	}
	
	@Test
	public void testTranslateIntegerDivision(){
		
		NumericExpression q = (NumericExpression) expressionFactory.expression(SymbolicOperator.INT_DIVIDE, intType, e, f);
		NumericExpression r = (NumericExpression) expressionFactory.expression(SymbolicOperator.MODULO, intType, e, f);
		
		Expr e2 = cvcProver.translate(e);
		Expr f2 = cvcProver.translate(f);
		Expr q2 = cvcProver.translate(q);
		Expr r2 = cvcProver.translate(r);
		
		Expr equationOne = vc.eqExpr(e2, vc.plusExpr(r2, vc.multExpr(f2, q2)));	//e2 = f2*q2+r2
		Expr equationTwo = vc.leExpr(vc.ratExpr(0), r2); // 0 < r2
		Expr equationThree = vc.ltExpr(r2, f2); // r2 < f2
		out.println(equationOne);
		
		assertEquals(QueryResult.VALID, vc.query(equationOne));
		assertEquals(QueryResult.VALID, vc.query(equationTwo));
		assertEquals(QueryResult.VALID, vc.query(equationThree));
		
	}

	@Test
	public void testTranslateMultiply() {
		
		Expr twoExpr = cvcProver.translate(two);
		Expr fiveExpr = cvcProver.translate(five);
		
		NumericExpression mulExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.MULTIPLY, realType, two, five);
		Expr expr = cvcProver.translate(mulExp);
		Expr expected = vc.multExpr(twoExpr, fiveExpr);
		assertEquals(expected, expr);
		
		/*NumericExpression mulExp2 = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.MULTIPLY, realType, two, five, ten);
		Expr expr2 = cvcProver.translate(mulExp2);
		
		String expected2 = "Wrong number of arguments to multiply: " + mulExp2.argument(0) + "*" + mulExp2.argument(1);
		*/
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
		StringObject xString = universe.stringObject("x");
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
		Expr x1and2Exist = cvcProver.validityChecker().andExpr(x1Exists, x2Exists);
//		Expr x1and2Exist = cvcProver.translate(andExpression);
		expected = vc.existsExpr(vars, x1and2Exist);
		assertEquals(expected, x1and2Exist);
		
		// TODO
		// (Ex . x > 0) ^ (Ex . x < 10)
	    // (Ex1 . x1 > 0) ^ (Ex2 . x2 < 10)
		
		// (Ex . x > 0 ^ (Ex . x < 10 ^ (...)))
		// (Ex1 . x1 > 0 ^ (Ex2 . x2 < 10 ^ (...)))
	}
	
	@Test
	public void testTranslateType() {
		cvcProver = (CVC3TheoremProver) proverFactory
				.newProver(booleanExprTrue);
		
		// cvc3 int array (with int index type)
		Type intArrayDataType = vc.arrayType(vc.intType(), vc.intType());
		// give extent, along with array type in a tuple
		Type expected = vc.tupleType(vc.intType(), intArrayDataType);
		
		Type translateResult = cvcProver.translateType(intArrayType);
		Type expected2 = vc.tupleType(vc.intType(), intArrayDataType);
		boolean equals1 = expected.equals(translateResult);
		out.println(equals1);
		boolean equals2 = expected.equals(expected2);
		out.println(equals2);

		assertEquals(expected, translateResult);
		// need .isArray() to show translation success.
		
		// cvc3 tuple
		List<SymbolicType> typesList = new ArrayList<SymbolicType>();
		typesList.add(intType);
		typesList.add(intType);
		SymbolicTypeSequence types = universe.typeSequence(typesList);
		StringObject name = universe.stringObject("twoIntTuple");
		SymbolicType twoIntTupleType = universe.tupleType(name, types);
		
		expected = vc.tupleType(vc.intType(), vc.intType());
		translateResult = cvcProver.translateType(twoIntTupleType);
		assertEquals(expected, translateResult);
		// need .toString() to show translation success. equality is determined
		// by name and type sequence, but there is no name parameter for the 
		// cvc3 tuple type
				
	}
	
	@Test
	public void testValid() {
		// show queries
		cvcProver.setOutput(out);
		
		// if true, then true (valid)
		assertEquals(ValidityResult.ResultType.YES, 
				cvcProver.valid(booleanExprTrue).getResultType());
		// if true, then false (invalid)
		assertEquals(ValidityResult.ResultType.NO,
				cvcProver.valid(booleanExprFalse).getResultType());
		
		cvcProver = (CVC3TheoremProver) proverFactory.
				newProver(booleanExprFalse);
		
		// if false, then false (valid)
		assertEquals(ValidityResult.ResultType.YES,
				cvcProver.valid(booleanExprFalse).getResultType());
		// if false, then true (valid)
		assertEquals(ValidityResult.ResultType.YES,
				cvcProver.valid(booleanExprTrue).getResultType());		
	}
}
