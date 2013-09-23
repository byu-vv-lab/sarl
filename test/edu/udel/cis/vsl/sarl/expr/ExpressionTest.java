package edu.udel.cis.vsl.sarl.expr;

import static org.junit.Assert.*;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.cnf.CnfExpression;
import edu.udel.cis.vsl.sarl.expr.cnf.CnfFactory;
import edu.udel.cis.vsl.sarl.expr.cnf.CnfSymbolicConstant;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.cnf.CnfSymbolicConstant;
import edu.udel.cis.vsl.sarl.expr.common.CommonNumericExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.common.CommonSymbolicConstant;
import edu.udel.cis.vsl.sarl.expr.common.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
import edu.udel.cis.vsl.sarl.universe.Universes;

import java.util.List;

public class ExpressionTest {
	private SymbolicUniverse sUniverse;
	private static PrintStream out = System.out;
	
	//Kolby's Play Area
	private SymbolicTypeFactory stf;
	private CollectionFactory cf;
	private ObjectFactory of;
	StringObject string1;
	StringObject string2;
	StringObject string3;
	
	private SymbolicConstant zz;
	
	private StringObject Xobj; // "X"
	private StringObject Yobj; // "Y"
	private SymbolicType realType, integerType, arrayType, booleanType, tupleType;
	private NumericSymbolicConstant x; // real symbolic constant "X"
	private NumericSymbolicConstant xInt; // Int symbolic constant "X"
	private NumericSymbolicConstant y; // real symbolic constant "Y"
	private BooleanSymbolicConstant b;
	private SymbolicConstant t;
	private NumericExpression two; // real 2.0
	private NumericExpression three; // real 3.0
	private NumericExpression twoInt; // int 2.0
	private NumericExpression threeInt; // int 3.0
	private BooleanObject trueBoolObj; // True
	private BooleanObject falseBoolObj; // False
	private IntObject fiveIntObj; // 5
	private IntObject zeroIntObj; // 0
	private IntObject negIntObj; // -10

	private static  FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();
	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);
	private ExpressionFactory expressionFactory = factorySystem
			.expressionFactory();
	private static NumericExpressionFactory idealFactory = factorySystem.numericFactory();
	private static NumericExpressionFactory herbrandFactory = factorySystem.numericFactory();
	CommonNumericExpressionFactory cnef = new CommonNumericExpressionFactory(idealFactory, herbrandFactory);
	
	
	SymbolicIntegerType intType;
	
	//CommonSymbolicConstant c1 = new CommonSymbolicConstant(string1, intType);
	
	@Before
	public void setUp() throws Exception {
		sUniverse = Universes.newIdealUniverse();

	
	
		Xobj = sUniverse.stringObject("X");
		Yobj = sUniverse.stringObject("Y");
		trueBoolObj = sUniverse.booleanObject(true);
		falseBoolObj = sUniverse.booleanObject(false);
		fiveIntObj = sUniverse.intObject(5);
		zeroIntObj = sUniverse.intObject(0);
		negIntObj = sUniverse.intObject(-10);
		realType = sUniverse.realType();
		booleanType = sUniverse.booleanType();
		integerType = sUniverse.integerType();
		arrayType = sUniverse.arrayType(integerType);
		List<SymbolicType> fieldType1 = new ArrayList<SymbolicType>();
		fieldType1.add(integerType);
		
		
		tupleType = sUniverse.tupleType(sUniverse.stringObject("typ1"), fieldType1);
		
	
		
		zz =  sUniverse.symbolicConstant(Xobj, arrayType);
		t = sUniverse.symbolicConstant(Xobj, tupleType);
		b = (BooleanSymbolicConstant) sUniverse.symbolicConstant(Xobj,booleanType );
		x = (NumericSymbolicConstant)  sUniverse.symbolicConstant(Xobj, realType);
		xInt = (NumericSymbolicConstant) sUniverse.symbolicConstant(Xobj, integerType);
		y = (NumericSymbolicConstant) sUniverse.symbolicConstant(Yobj, realType);
		two = (NumericExpression) sUniverse.cast(realType, sUniverse.integer(2));
		three = (NumericExpression) sUniverse.cast(realType, sUniverse.integer(3));
		twoInt = (NumericExpression) sUniverse.cast(integerType, sUniverse.integer(2));
		threeInt = (NumericExpression) sUniverse.cast(integerType, sUniverse.integer(3));

		
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		
		stf = system.typeFactory();
		of = system.objectFactory();
		cf = system.collectionFactory();
	}

	@After
	public void tearDown() throws Exception {
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		of = system.objectFactory();
		cf = system.collectionFactory();
		stf = system.typeFactory();
	}
	
	@Test
	public void toStringBuffer1PowerTest() {
		int exponent = 4;
		IntObject n = sUniverse.intObject(exponent);
		NumericExpression xpy = sUniverse.add(x, y);
		NumericExpression xpyp1 = sUniverse.power(xpy, n);
		NumericExpression xpyp2 = sUniverse.power(xpy, two);
		
		assertEquals(xpyp1.toString(), "X^4+4*(X^(3))*Y+6*(X^(2))*(Y^(2))+4*X*(Y^(3))+Y^4");
		assertEquals(xpyp2.toString(), "(X+Y)^2");
		
		//power test atomize
		assertEquals(xpyp1.toStringBuffer(true).toString(), "(X^(4)+4*(X^(3))*Y+6*(X^(2))*(Y^(2))+4*X*(Y^(3))+Y^(4))");
		assertEquals(xpyp2.toStringBuffer(true).toString(), "((X+Y)^2)");
		
	}
	
	@Test
	public void toStringBuffer1AddTest() {
		NumericExpression xpy = sUniverse.add(x, y);
		NumericExpression test1 = sUniverse.add(xpy, two);
		assertEquals(test1.toString(), "X+Y+2");
		
		//add test atomize
		assertEquals(test1.toStringBuffer(true).toString(), "(X+Y+2)");
	}
	
	@Test
	public void toStringBuffer1CondTest() {
		SymbolicExpression test2 = sUniverse.cond(sUniverse.equals(x, two), three, two);
		assertEquals(test2.toString(), "(0 == -1*X+2) ? 3 : 3");
		
		//cond test atomize
		assertEquals(test2.toStringBuffer(true).toString(), "((0 == -1*X+2) ? 3 : 3)");
	}
	
	@Test
	public void toStringBuffer1DivideTest() {
		NumericExpression test3 = sUniverse.divide(x, y);
		assertEquals(test3.toString(), "X/Y");
		
		//divide test atomize
		assertEquals(test3.toStringBuffer(true).toString(), "(X/Y)");
	}
	
	@Test
	public void toStringBuffer1ExistsTest() {
		BooleanExpression test4 = sUniverse.exists(x, sUniverse.equals(x, y));
		assertEquals(test4.toString(), "exists X : real . (0 == -1*X+Y)");
		
		//exists test atomize
		assertEquals(test4.toStringBuffer(true).toString(), "(exists X : real . (0 == -1*X+Y))");
	}
	
	@Test
	public void toStringBuffer1ForAllTest() {
		BooleanExpression test5 = sUniverse.forall(x, sUniverse.equals(x, y));
		assertEquals(test5.toString(), "forall X : real . (0 == -1*X+Y)");
		
		//forall test atomize
		assertEquals(test5.toStringBuffer(true).toString(), "(forall X : real . (0 == -1*X+Y))");
	}
	
	

	@Test
	public void toStringBuffer1LengthTest() {	
NumericExpression test6 = sUniverse.length(zz);
System.out.println(test6);
assertEquals(test6.toString(), "length(X)");
	}
	
	@Test
	public void toStringBuffer1LessThanTest() {
		BooleanExpression test7 = sUniverse.lessThan(x, three);
		out.println(x);
		out.println(three);
		for (SymbolicObject expr : test7.arguments())
			out.println(expr.toString());
		assertEquals(test7.toString(), "0 < -1*X+3");
		
		//Less_than test atomize
		assertEquals(test7.toStringBuffer(true).toString(), "(0 < -1*X+3)");
	}
	
	@Test
	public void toStringBuffer1LessThanEqualTest() {
		BooleanExpression test8 = sUniverse.lessThanEquals(x, three);
		assertEquals(test8.toString(), "0 <= -1*X+3");
		
		//Less_than_equals test atomize
		assertEquals(test8.toStringBuffer(true).toString(),"(0 <= -1*X+3)");
	}
	
	@Test
	public void toStringBuffer1ModuloTest() {
		NumericExpression test9 = sUniverse.modulo(xInt, threeInt);
		assertEquals(test9.toString(), "X%3");
		
		//Modulo test atomize
		assertEquals(test9.toStringBuffer(true).toString(), "(X%3)");
	}
	
	@Test
	public void toStringBuffer1NeqTest() {
		BooleanExpression test10 = sUniverse.neq(x, y);
		assertEquals(test10.toString(), "0 != -1*X+Y");
		
		//Neg test atomize
		assertEquals(test10.toStringBuffer(true).toString(), "(0 != -1*X+Y)");
	}
	
	@Test
	public void toStringBuffer1NegativeTest() {
		
		NumericExpression negExp = (NumericExpression) expressionFactory.expression(SymbolicOperator.NEGATIVE, integerType, two);
		

		assertEquals(negExp.toString(), "-2");

		//Neg test atomize
		assertEquals(negExp.toStringBuffer(true).toString(), "(-2)");
	}
	
	@Test
	public void toStringBuffer1NotTest() {
		
		BooleanExpression test11 = sUniverse.not(b);
		assertEquals(test11.toString(), "!X");
		
		//not test atomize
		assertEquals(test11.toStringBuffer(true).toString(), "(!X)");
	}
	
	@Test
	public void toStringBuffer1LambdaTest() {
		BooleanExpression a = sUniverse.not(b);
		SymbolicExpression test11 = sUniverse.lambda(x, a);
		assertEquals(test11.toString(), "lambda X : real . (!X)");
		
		//atomize
		assertEquals(test11.toStringBuffer(true).toString(), "(lambda X : real . (!X))");
	}
	
	
	@Test
	public void toStringBuffer1NullTest() {
		BooleanExpression a = sUniverse.not(b);
		BooleanExpression nullexp = (BooleanExpression) expressionFactory.expression(SymbolicOperator.NULL, booleanType, a);

		assertEquals(nullexp.toStringBuffer(false).toString(), "NULL");

	}
	
	@Ignore
	@Test
	public void toStringBuffer1IntDivideTest() {
		NumericExpression intExp = (NumericExpression) expressionFactory.expression(SymbolicOperator.INT_DIVIDE, integerType, x,y);
		// edited out by siegel as the characters were non-ASCII
		// replaced non-ASCII characters with ?
		assertEquals(intExp.toStringBuffer(false).toString(), "X??Y");
		//atomize
		assertEquals(intExp.toStringBuffer(true).toString(), "(X??Y)");

	}

	

	@Test
	public void toStringBuffer1OrTest() {
		BooleanExpression a = sUniverse.not(b);
		BooleanExpression test13 = sUniverse.or(a,b);
		
		//If statement is needed because X and !X are sometimes flipped
		if(test13.toStringBuffer(true).toString().equals("!X || X" ) || test13.toStringBuffer(true).toString().equals( "((!X || X))")){
			assertEquals(test13.toStringBuffer(false).toString(),"!X || X");
			assertEquals(test13.toStringBuffer(true).toString(),"((!X || X))");
		}else{
			assertEquals(test13.toStringBuffer(false).toString(),"X || !X");
			assertEquals(test13.toStringBuffer(true).toString(),"((X || !X))");
		}
		


	}
	@Test
	public void toStringBuffer1TupleReadTest() {
SymbolicExpression test = sUniverse.tupleRead(t, zeroIntObj);
		assertEquals(test.toStringBuffer(false).toString(), "X.0");
		
		assertEquals(test.toStringBuffer(true).toString(), "(X.0)");
	}
	
	
	//NOT FINISHEd yet, leave this alone for now
//	@Test
//	public void toStringBuffer1TupleWriteTest() {
//SymbolicExpression test = sUniverse.tupleWrite(t, zeroIntObj, two);
//		assertEquals(test.toStringBuffer(false).toString(), "X.0");
//		
//		assertEquals(test.toStringBuffer(true).toString(), "(X.0)");
//	}
	
	
	@Test
	public void toStringBuffer1SubtractTest() {
		NumericExpression intExp = (NumericExpression) expressionFactory.expression(SymbolicOperator.SUBTRACT, integerType, x,y);
		
		assertEquals(intExp.toStringBuffer(false).toString(), "X - Y");
		
		//subtract test atomize
		assertEquals(intExp.toStringBuffer(true).toString(), "(X - Y)");
	}
	
	@Test
	public void toStringBufferLongTest() {
		NumericExpression xpy = sUniverse.add(x,y);
		NumericExpression xty = sUniverse.multiply(x,y);
		NumericExpression xpyDxty = sUniverse.divide(xpy,xty);
		
		StringBuffer tstStringBuff = new StringBuffer(xpyDxty.getClass().getSimpleName());
		tstStringBuff.append("[");
		tstStringBuff.append(xpyDxty.operator());
		tstStringBuff.append("; ");
		tstStringBuff.append(xpyDxty.type());
		tstStringBuff.append("; ");
		tstStringBuff.append("{");
		
		Boolean first = true;
		
		for (SymbolicObject obj : xpyDxty.arguments()) {
				if (first)
					first = false;
				else
					tstStringBuff.append(",");
				if (obj == null)
					tstStringBuff.append("null");
				else
					tstStringBuff.append(obj.toStringBufferLong());
		}
		tstStringBuff.append("}");	
		tstStringBuff.append("]");
			
		//out.println(xpyDxty.toStringBufferLong());
		//out.println(tstStringBuff);
		assertEquals(xpyDxty.toStringBufferLong().toString(),tstStringBuff.toString());
	}
	
	//added an ignore here because it counted as a failure and was committed for some reason
	@Ignore
	@Test
	public void SimplifierFactoryTest() {
		//NumericExpressionFactory numericFactory = standardExpressionFactory
		//		.numericFactory();
		//ExpressionFactory bef = Expressions.newExpressionFactory(numericFactory)
		//FactorySystem system = null;
		//PreUniverse universe = PreUniverses.newPreUniverse(system);
		//String Result;
		//Result = Expressions.standardSimplifierFactory(bef, universe)
	}
	
	@Test
	public void CnfSymbolicConstantTest() {
		StringObject name = sUniverse.stringObject("Hello");
		CnfFactory Test = new CnfFactory(stf, of, cf);
		CnfSymbolicConstant hellotest = (CnfSymbolicConstant) Test.booleanSymbolicConstant(name);
		StringObject hellomsg = sUniverse.stringObject("Hello");
		StringObject hellomsgfalse = sUniverse.stringObject("hello");
		assertEquals(hellomsg, hellotest.name());
		assertNotEquals(hellomsgfalse, hellotest.name());
		assertEquals("Hello", hellotest.toString());
		assertNotEquals("hello",hellotest.toString());
	}
	
	@Test
	public void CnfFactoryBooleanNotTest() {
		CnfFactory test = new CnfFactory(stf, of, cf);
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		BooleanExpression falseEx = bef.falseExpr();
		assertEquals(false, test.not(falseEx).isFalse());
	}
	
	@Test
	public void CnfFactoryForAllTest() {
		CnfFactory test = new CnfFactory(stf, of, cf);
		
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		BooleanExpression falseEx = bef.falseExpr();
		BooleanExpression trueEx = bef.trueExpr();

		assertEquals(true, test.forall(x, falseEx).isFalse());
		assertEquals(false, test.forall(x, falseEx).isTrue());
		assertEquals(true, test.forall(x, trueEx).isTrue());
		assertEquals(false, test.forall(x, trueEx).isFalse());
	}
	
	@Test
	public void CnfFactoryExistsTest() {
		CnfFactory test = new CnfFactory(stf, of, cf);
		
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		BooleanExpression falseEx = bef.falseExpr();
		BooleanExpression trueEx = bef.trueExpr();
		BooleanExpression b = bef.or(trueEx, falseEx);

		assertEquals(true, test.exists(x, b).isTrue());
		assertEquals(true, test.exists(x, falseEx).isFalse());
		assertEquals(false, test.exists(x, falseEx).isTrue());
		assertEquals(true, test.exists(x, trueEx).isTrue());
		assertEquals(false,test.exists(x, trueEx).isFalse());
	}
	
	@Test
	public void CnfFactoryEquivTest() {
		CnfFactory test = new CnfFactory(stf, of, cf);
		
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		BooleanExpression falseEx = bef.falseExpr();
		BooleanExpression trueEx = bef.trueExpr();
		BooleanExpression thisIsTrue = bef.trueExpr();
		BooleanObject bot = sUniverse.booleanObject(true);
		BooleanObject bof = sUniverse.booleanObject(false);
		
		SymbolicObject[] arg = sUniverse.and(trueEx, trueEx).arguments();
		Set<SymbolicObject> argSet = new HashSet<SymbolicObject>(Arrays.asList(arg));
		BooleanExpression b = test.booleanExpression(SymbolicOperator.AND, arg);
		BooleanExpression b2 = test.booleanExpression(SymbolicOperator.AND, argSet);
		
		assertEquals(false, b2.isTrue());
		assertEquals(false, b.isTrue());
		assertEquals(true, test.symbolic(bot).isTrue());
		assertEquals(false, test.symbolic(bof).isTrue());
		assertEquals(false, test.equiv(trueEx, falseEx).isTrue());
		assertEquals(true, test.equiv(trueEx, thisIsTrue).isTrue());
	}
	
	@Test
	public void CnfFactoryorTest(){
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		BooleanExpression testingtrue = sUniverse.bool(true);
		BooleanExpression testingfalse = sUniverse.bool(false);
		BooleanExpression falseExpr= bef.falseExpr();
		BooleanExpression trueExpr= bef.trueExpr();
		assertEquals(testingtrue, bef.and(testingtrue, testingtrue));
		assertEquals(testingtrue, bef.and(trueExpr, testingtrue));
		assertEquals(testingfalse, bef.and(trueExpr, testingfalse));
		assertEquals(testingfalse, bef.and(testingfalse, trueExpr));
		assertEquals(testingfalse, bef.and(falseExpr, testingtrue));
		assertEquals(testingfalse, bef.and(testingtrue, falseExpr));
		assertEquals(testingtrue, bef.or(testingtrue, testingtrue));
		assertEquals(testingtrue, bef.or(trueExpr, testingtrue));
		assertEquals(testingtrue, bef.or(trueExpr, testingfalse));
		assertEquals(testingtrue, bef.or(falseExpr, testingtrue));
		assertEquals(testingfalse, bef.or(testingfalse, falseExpr));
	}

	@Ignore
	@Test
	public void CnFFactoryNotTest(){
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		CnfExpression cnf;
		BooleanExpression testingtrue = sUniverse.bool(true);
		BooleanExpression testingfalse = sUniverse.bool(false);
		BooleanExpression falseExpr= bef.falseExpr();
		BooleanExpression trueExpr= bef.trueExpr();
		//assertEquals(testingfalse, bef.not(trueExpr1));
	}
	
	
	@Test
	public void newCnfFactoryTest() {
		//or here.
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		assertNotNull(bef);
	}
	
	@Test
	public void cnefDivideTest() {
		NumericExpression xpy = sUniverse.add(x,y);
		NumericExpression xty = sUniverse.multiply(x,y);
		NumericExpression xpyDxty = sUniverse.divide(xpy,xty);
		
		assertEquals(cnef.divide(xpy, xty),xpyDxty);
		assertEquals(cnef.divide(xpy, xty).toStringBuffer(true).toString(), "((X+Y)/X*Y)");
	}
	
	@Test
	public void cnefHerbrandFactoryTest() {
		NumericExpressionFactory hf = cnef.herbrandFactory();
		
		assertEquals(hf, herbrandFactory);
	}
	
	@Test
	public void cnefMinusTest() {
		NumericExpression xpy = sUniverse.add(x,y);
		NumericExpression minus = cnef.minus(xpy);
		
		assertEquals(minus, idealFactory.minus(xpy));
	}
	
	

}
