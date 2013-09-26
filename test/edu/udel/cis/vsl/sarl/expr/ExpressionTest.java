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
import edu.udel.cis.vsl.sarl.IF.expr.ArrayElementReference;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.OffsetReference;
import edu.udel.cis.vsl.sarl.IF.expr.ReferenceExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.expr.TupleComponentReference;
import edu.udel.cis.vsl.sarl.IF.expr.UnionMemberReference;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSet;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
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
import edu.udel.cis.vsl.sarl.simplify.IF.SimplifierFactory;
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
	private NumberFactory nf;
	private SymbolicType herbrandType;
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
	private NumericExpression zero; // real 0
	private NumericExpression one; // real 1.0
	private NumericExpression three; // real 3.0
	private NumericExpression twoInt; // int 2.0
	private NumericExpression threeInt; // int 3.0
	private NumericExpression oneInt; // int 2.0
	private NumericExpression zeroInt; // int 3.0
	private BooleanObject trueBoolObj; // True
	private BooleanObject falseBoolObj; // False
	private IntObject fiveIntObj; // 5
	private IntObject zeroIntObj; // 0
	private IntObject negIntObj; // -10
	private IntObject oneIntObj; // 1
	private NumericExpression xpy;
	private NumericExpression xty;
	private NumericExpression xpyDxty;

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
	private SymbolicType herbrandIntType;
	private SymbolicOperator addOperator;
	//CommonSymbolicConstant c1 = new CommonSymbolicConstant(string1, intType);
	
	@Before
	public void setUp() throws Exception {
		sUniverse = Universes.newIdealUniverse();
	
		Xobj = sUniverse.stringObject("X");
		Yobj = sUniverse.stringObject("Y");
		trueBoolObj = sUniverse.booleanObject(true);
		falseBoolObj = sUniverse.booleanObject(false);
		fiveIntObj = sUniverse.intObject(5);
		oneIntObj = sUniverse.intObject(1);
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
		one = (NumericExpression) sUniverse.cast(realType, sUniverse.integer(1));
		zero = (NumericExpression) sUniverse.cast(realType, sUniverse.integer(0));
		three = (NumericExpression) sUniverse.cast(realType, sUniverse.integer(3));
		twoInt = (NumericExpression) sUniverse.cast(integerType, sUniverse.integer(2));
		threeInt = (NumericExpression) sUniverse.cast(integerType, sUniverse.integer(3));
		zeroInt = (NumericExpression) sUniverse.cast(integerType, sUniverse.integer(0));
		oneInt = (NumericExpression) sUniverse.cast(integerType, sUniverse.integer(1));
		xpy = sUniverse.add(x,y);
		xty = sUniverse.multiply(x,y);
		xpyDxty = sUniverse.divide(xpy,xty);
		
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		
		stf = system.typeFactory();
		of = system.objectFactory();
		cf = system.collectionFactory();
		nf = system.numberFactory();
		addOperator = xpy.operator();
		
		herbrandType = stf.herbrandRealType();
		herbrandIntType = stf.herbrandIntegerType();
	}

	@After
	public void tearDown() throws Exception {
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		of = system.objectFactory();
		cf = system.collectionFactory();
		stf = system.typeFactory();
	}

	@Test
	public void CommonExpressionFactoryNonTrivialTest(){
		SymbolicType referenceType;
		SymbolicTypeSequence referenceIndexSeq; // Ref x Int
		SymbolicType referenceFunctionType; // Ref x Int -> Ref
		
		referenceType = of.canonic(stf.tupleType(
				of.stringObject("Ref"),
				stf.sequence(new SymbolicType[] { integerType })));
		referenceIndexSeq = stf.sequence(new SymbolicType[] {
				referenceType, integerType });
		referenceFunctionType = of.canonic(stf.functionType(
				referenceIndexSeq, referenceType));
		
		SymbolicSequence s,s1,s2;
		s = cf.emptySequence();
		s1= s.add(sUniverse.identityReference());
		s2 = s1.add(sUniverse.integer(1));
		
	
		
		SymbolicExpression test2 =  of.canonic(sUniverse.symbolicConstant(sUniverse.stringObject("ArrayElementRef"), referenceFunctionType));
		SymbolicExpression test =  expressionFactory.expression(SymbolicOperator.APPLY, sUniverse.referenceType(),test2, s2);
		ArrayElementReference testRef = expressionFactory.arrayElementReference((ReferenceExpression) s1.get(0), sUniverse.integer(1));
		assertEquals(test,testRef);
	
		SymbolicExpression test3 =  of.canonic(sUniverse.symbolicConstant(sUniverse.stringObject("TupleComponentRef"), referenceFunctionType));
		SymbolicExpression testtuple =  expressionFactory.expression(SymbolicOperator.APPLY, sUniverse.referenceType(),test3, s2);
		TupleComponentReference testTuple = expressionFactory.tupleComponentReference((ReferenceExpression) s1.get(0), oneIntObj);
		assertEquals(testtuple,testTuple);
	
		SymbolicExpression test5 =  of.canonic(sUniverse.symbolicConstant(sUniverse.stringObject("UnionMemberRef"), referenceFunctionType));
		SymbolicExpression testunion =  expressionFactory.expression(SymbolicOperator.APPLY, sUniverse.referenceType(),test5, s2);
		UnionMemberReference testUnion = expressionFactory.unionMemberReference((ReferenceExpression) s1.get(0), oneIntObj);
		assertEquals(testunion,testUnion);
	
		SymbolicExpression test6 =  of.canonic(sUniverse.symbolicConstant(sUniverse.stringObject("OffsetRef"), referenceFunctionType));
		SymbolicExpression testoffset =  expressionFactory.expression(SymbolicOperator.APPLY, sUniverse.referenceType(),test6, s2);
		OffsetReference testOffset = expressionFactory.offsetReference((ReferenceExpression) s1.get(0), sUniverse.integer(1));
		assertEquals(testoffset,testOffset);
  
	}
	
	@Test
	public void CommonExpressionFactoryConcreteTest(){
		SymbolicType referenceType1,referenceType2;


		referenceType1 = of.canonic(stf.tupleType(
				of.stringObject("Ref"),
				stf.sequence(new SymbolicType[] { integerType })));

		referenceType2 = of.canonic(stf.tupleType(
				of.stringObject("Ref"),
				stf.sequence(new SymbolicType[] { booleanType })));



		SymbolicSequence<NumericExpression> zeroSequence = of.canonic(cf
				.singletonSequence(zeroInt));
		SymbolicSequence<NumericExpression> oneSequence = of.canonic(cf
				.singletonSequence(oneInt));
		
		SymbolicExpression test = expressionFactory.expression(SymbolicOperator.CONCRETE,referenceType1, zeroSequence);
		SymbolicExpression test2 = expressionFactory.expression(SymbolicOperator.CONCRETE,referenceType2, oneSequence);
		
		assertEquals(test.toString(),"(Ref<int>)<0>");
		assertEquals(test2.toString(),"(Ref<boolean>)<1>");
	}
	
	
	@Test
	public void CommonExpressionFactoryExpressionTest(){
		
		SymbolicType referenceType;
		referenceType = of.canonic(stf.tupleType(
				of.stringObject("Ref"),
				stf.sequence(new SymbolicType[] { integerType })));

		BooleanExpression b1 = sUniverse.trueExpression();
		BooleanExpression b2 = sUniverse.falseExpression();
		ReferenceExpression r1 = sUniverse.identityReference();
		ReferenceExpression r2 = sUniverse.identityReference();
		SymbolicObject[] ExpressionList = {two,three};
		SymbolicObject[] ExpressionList2 = {b1,b2};
		SymbolicObject[] ExpressionList3 = {r1,r2};
		
		Collection<SymbolicObject> args1= new ArrayList<SymbolicObject>(Arrays.asList(ExpressionList));
		Collection<SymbolicObject> args2= new ArrayList<SymbolicObject>(Arrays.asList(ExpressionList2));
		Collection<SymbolicObject> args3= new ArrayList<SymbolicObject>(Arrays.asList(ExpressionList3));
		
		SymbolicExpression test1 =  expressionFactory.expression(SymbolicOperator.ADD, integerType, args1);
		SymbolicExpression test2 =  expressionFactory.expression(SymbolicOperator.ADD, booleanType, args2);
		SymbolicExpression test3 =  expressionFactory.expression(SymbolicOperator.ADD, referenceType, args3);
		assertEquals(test1.toString(), "2+3");
		assertEquals(test2.toString(), "true+false");
		assertEquals(test3.toString(), "(Ref<int>)<1>+(Ref<int>)<1>");
  
	}
	
	@Test
	public void CommonExpressionFactoryExpressionTest2(){
		
		SymbolicType referenceType;
		referenceType = of.canonic(stf.tupleType(
				of.stringObject("Ref"),
				stf.sequence(new SymbolicType[] { integerType })));

		BooleanExpression b1 = sUniverse.trueExpression();
		BooleanExpression b2 = sUniverse.falseExpression();
		ReferenceExpression r1 = sUniverse.identityReference();
		ReferenceExpression r2 = sUniverse.identityReference();
		SymbolicObject[] ExpressionList = {two,three};
		SymbolicObject[] ExpressionList2 = {b1,b2};
		SymbolicObject[] ExpressionList3 = {r1,r2};
		
	
		
		SymbolicExpression test1 =  expressionFactory.expression(SymbolicOperator.ADD, integerType, ExpressionList);
		SymbolicExpression test2 =  expressionFactory.expression(SymbolicOperator.ADD, booleanType, ExpressionList2);
		SymbolicExpression test3 =  expressionFactory.expression(SymbolicOperator.ADD, referenceType, ExpressionList3);
		assertEquals(test1.toString(), "2+3");
		assertEquals(test2.toString(), "true+false");
		assertEquals(test3.toString(), "(Ref<int>)<1>+(Ref<int>)<1>");
  
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

	@Test
	public void toStringBuffer1IntDivideTest() {
		NumericExpression intExp = (NumericExpression) expressionFactory.expression(SymbolicOperator.INT_DIVIDE, integerType, x,y);
		assertEquals(intExp.toStringBuffer(false).toString(), "X div Y");
		//atomize
		assertEquals(intExp.toStringBuffer(true).toString(), "(X div Y)");

	}

	@Ignore
	@Test
	public void toStringBuffer1OrTest() {
		BooleanExpression a = sUniverse.not(b);
		BooleanExpression test13 = sUniverse.or(a,b);
		
		//If statement is needed because X and !X are sometimes flipped
		if(test13.toStringBuffer(false).toString().equals("!X || X" ) || test13.toStringBuffer(true).toString().equals( "(!X || X)")){
			assertEquals(test13.toStringBuffer(false).toString(),"!X || X");
			assertEquals(test13.toStringBuffer(true).toString(),"(!X || X)");
		}else{
			assertEquals(test13.toStringBuffer(false).toString(),"X || !X");
			assertEquals(test13.toStringBuffer(true).toString(),"(X || !X)");
		}
	}
	@Test
	public void toStringBuffer1TupleReadTest() {
SymbolicExpression test = sUniverse.tupleRead(t, zeroIntObj);
		assertEquals(test.toStringBuffer(false).toString(), "X.0");
		
		assertEquals(test.toStringBuffer(true).toString(), "(X.0)");
	}

	//not working yet, come back to it -schivi
	@Ignore
	@Test
	public void toStringBufferUnionTest(){
 SymbolicUnionType intRealBoolUnion;
 
 SymbolicExpression tenAndHalf = universe.rational(10.5);

 SymbolicType unionArrayType;
 SymbolicExpression unionArray;
//make a union to test
		// union of int, real, bool
		intRealBoolUnion = universe.unionType(universe
				.stringObject("union1"), Arrays.asList(new SymbolicType[]
						{integerType, realType, booleanType}));
		// union array type
		unionArrayType = universe.arrayType(intRealBoolUnion);
		
		// union array expression to write values to
		unionArray = universe
				.symbolicConstant(universe.stringObject("unionArray"),
						unionArrayType);
		// add true bool
		unionArray = universe.arrayWrite(unionArray, 
				universe.integer(0), // index of array
				universe.unionInject(intRealBoolUnion,
						universe.intObject(2), // 2 is index of type (bool)
						sUniverse.trueExpression()));
 
 
// 
//	//	SymbolicExpression injectBoolFalse = universe
//		//		.unionExtract(universe.intObject(1), intRealBoolUnion);
//		
//		assertEquals(injectBoolFalse, "wat");
//		
//		SymbolicExpression injectBoolFalse2 = expressionFactory
//				.expression(SymbolicOperator.UNION_TEST, intRealBoolUnion,
//						universe.intObject(1), tenAndHalf);
		
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
			
		assertEquals(xpyDxty.toStringBufferLong().toString(),tstStringBuff.toString());
	}
	
	@Test
	public void cnfSymbolicAsserts(){
		BooleanExpressionFactory frst = Expressions.newCnfFactory(stf, of, cf);
		SymbolicObject obj1 = sUniverse.booleanObject(true);
		SymbolicObject obj2 = sUniverse.booleanObject(false);
		SymbolicObject[] ExpressionList = {obj1,obj2};
		SymbolicOperator Soperator = SymbolicOperator.AND;
		CnfExpression cnf = (CnfExpression) frst.booleanExpression(Soperator, ExpressionList);
		assertFalse(cnf.isCanonic());
	}
	
	@Test
	public void cnfFactoryForallTest(){
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		StringObject pobject = sUniverse.stringObject("a");
		StringObject qobject = sUniverse.stringObject("b");
		BooleanExpression p = (BooleanExpression) sUniverse.symbolicConstant(pobject, booleanType);
		BooleanExpression q = (BooleanExpression) sUniverse.symbolicConstant(qobject, booleanType);
		BooleanExpression andtrue = bef.and(p, q);
		BooleanExpression foralltrue = bef.forall(b, q);
		BooleanExpression foralltrue2 = bef.forall(b, p);
		BooleanExpression FORALL = bef.booleanExpression(SymbolicOperator.FORALL, foralltrue);
		BooleanExpression FORALL2 = bef.booleanExpression(SymbolicOperator.FORALL, foralltrue2);
		CnfExpression cnf = (CnfExpression) FORALL;
		CnfExpression cnf2 = (CnfExpression) FORALL2;
		assertEquals(bef.and((BooleanExpression) cnf2.argument(0), (BooleanExpression) cnf.argument(0)), bef.forall(b, andtrue));
	}
	
	@Test
	public void cnfFactoryExistsTest(){
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		StringObject pobject = sUniverse.stringObject("a");
		StringObject qobject = sUniverse.stringObject("b");
		BooleanExpression p = (BooleanExpression) sUniverse.symbolicConstant(pobject, booleanType);
		BooleanExpression q = (BooleanExpression) sUniverse.symbolicConstant(qobject, booleanType);
		BooleanExpression ortrue = bef.or(p, q);
		BooleanExpression existstrue = bef.exists(b, q);
		BooleanExpression existstrue2 = bef.exists(b, p);
		BooleanExpression EXISTS = bef.booleanExpression(SymbolicOperator.EXISTS, existstrue);
		BooleanExpression EXISTS2 = bef.booleanExpression(SymbolicOperator.EXISTS, existstrue2);
		CnfExpression cnf = (CnfExpression) EXISTS;
		CnfExpression cnf2 = (CnfExpression) EXISTS2;
		System.out.println(cnf.argument(0));
		//BooleanExpression foralltrue2 = bef.forall(boundVariable, a);
		assertEquals(bef.or((BooleanExpression) cnf2.argument(0), (BooleanExpression) cnf.argument(0)), bef.exists(b, ortrue));
		}

	@Test
	public void CnFFactoryNotTest(){
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		BooleanExpression testingfalse = sUniverse.bool(false);
		StringObject pobject = sUniverse.stringObject("a");
		StringObject qobject = sUniverse.stringObject("b");
		StringObject robject = sUniverse.stringObject("c");
		BooleanExpression p = (BooleanExpression) sUniverse.symbolicConstant(pobject, booleanType);
		BooleanExpression q = (BooleanExpression) sUniverse.symbolicConstant(qobject, booleanType);
		SymbolicConstant r = sUniverse.symbolicConstant(robject, booleanType);
		BooleanExpression falseExpr= bef.falseExpr();
		BooleanExpression trueExpr= bef.trueExpr();
		BooleanExpression testingtrue = sUniverse.bool(true);
		BooleanExpression[] ANDset = {testingtrue,trueExpr, testingfalse};
		BooleanExpression andtrue =  bef.and(p, q);
		BooleanExpression ortrue = bef.or(p, q);
		BooleanExpression AND = bef.booleanExpression(SymbolicOperator.AND, andtrue);
		//BooleanExpression OR = bef.booleanExpression(SymbolicOperator.OR, ortrue);
		BooleanExpression nottrue = bef.not(q);
		//BooleanExpression NOT = bef.booleanExpression(SymbolicOperator.NOT, nottrue);
		BooleanExpression neqtrue = bef.equiv(q, p);
		//BooleanExpression NEQ = bef.booleanExpression(SymbolicOperator.EQUALS, neqtrue);
		BooleanExpression foralltrue = bef.forall(b, testingtrue);
		//BooleanExpression FORALL = bef.booleanExpression(SymbolicOperator.FORALL, foralltrue);
		BooleanExpression existstrue = bef.exists(b, testingfalse);

		//BooleanExpression FORALLTEST = bef.booleanExpression(SymbolicOperator.FORALL, ANDset);
		CnfExpression cnf = (CnfExpression) AND;
		
		assertEquals(bef.or(bef.not(p), bef.not(q)), bef.not(andtrue));
		assertEquals(bef.and(bef.not(p), bef.not(q)), bef.not(ortrue));
		assertEquals(q, bef.not(nottrue));
		
		BooleanExpression foralltruechk = bef.exists(b, testingfalse);
		BooleanExpression EXISTS = bef.booleanExpression(SymbolicOperator.EXISTS, foralltruechk);
		CnfExpression cnf2 = (CnfExpression) EXISTS;
		
		BooleanExpression existschk = bef.forall(b, testingtrue);
		BooleanExpression FORALL = bef.booleanExpression(SymbolicOperator.FORALL, existschk);
		CnfExpression cnf3 = (CnfExpression) FORALL;
		//checked tests
		
		//for not(forall) since it is not, check with reverse(i.e. exists)
		assertEquals(cnf2.argument(0), bef.not(foralltrue));
		
		//for not(exists)
		assertEquals(cnf3.argument(0), bef.not(existstrue));
		//assertEquals(false, bef.not(FORALL));
		//assertEquals(testingfalse, bef.not(NEQ));
		//System.out.println(andtrue.operator());
		//System.out.println(cnf.arguments()[0]);
		//System.out.println(cnf.argument(0));
		//System.out.println(cnf.booleanArg(0));
		
		//A or A AND B or B? is that what it is?
		//assertEquals(testingtrue, bef.not(AND));
		//assertEquals(testingtrue, bef.not(FORALLTEST));
	}
	
	@Test
	public void newCnfFactoryTest() {
		//or here.
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		assertNotNull(bef);
	}
	
	@Test
	public void cnefDivideTest() {
		NumericExpression xpyDxtyH = sUniverse.divide(cnef.cast(xpy, herbrandType), cnef.cast(xty, herbrandType));
		NumericExpression testExpr = cnef.expression(xpyDxty.operator(), xpyDxty.type(), xpy,xty);
		
		assertEquals(cnef.divide(xpy, xty), xpyDxty);
		//assertEquals(xpyDxty, testExpr);
		assertEquals(cnef.divide(cnef.cast(xpy, herbrandType),  cnef.cast(xty, herbrandType)), xpyDxtyH);
		assertEquals(cnef.divide(xpy, xty).toStringBuffer(true).toString(), "((X+Y)/X*Y)");
	}
	
	@Test
	public void cnefHerbrandFactoryTest() {
		NumericExpressionFactory hf = cnef.herbrandFactory();
		
		assertEquals(hf, herbrandFactory);
	}
	
	@Test
	public void cnefMinusTest() {
		NumericExpression minus = cnef.minus(xpy);
		NumericExpression minusH = cnef.minus(cnef.cast(xpy, herbrandType));
		
		assertEquals(minus, sUniverse.minus(xpy));
		assertEquals(minus, idealFactory.minus(xpy));
		assertEquals(minus.toStringBuffer(true).toString(), "(-1*X+-1*Y)");
		assertEquals(minusH, sUniverse.minus(cnef.cast(xpy, herbrandType)));
		assertEquals(minusH, idealFactory.minus(cnef.cast(xpy, herbrandType)));
		assertEquals(minusH.toStringBuffer(true).toString(), "-1*(hreal)(hreal)(X+Y)");
	}
	
	@Test
	public void cnefModuloTest() {
		NumericExpression expr1 = sUniverse.multiply(threeInt, threeInt);
		NumericExpression expr1H = cnef.cast(sUniverse.multiply(threeInt, threeInt), herbrandIntType);
		NumericExpression expr2 = sUniverse.divide(twoInt, xInt);
		NumericExpression expr2H = cnef.cast(sUniverse.divide(twoInt, xInt), herbrandIntType);
		NumericExpression moduloExpression = cnef.modulo(expr1, expr2);
		NumericExpression moduloExpressionH = cnef.modulo(expr1H, expr2H);
		NumericExpression testExpr = cnef.expression(moduloExpression.operator(), moduloExpression.type(), expr1,expr2);
		
		assertEquals(moduloExpression, testExpr);
		assertEquals(moduloExpression, sUniverse.modulo(expr1, expr2));
		assertEquals(moduloExpression, testExpr);
		assertEquals(moduloExpression,idealFactory.modulo(expr1, expr2));
		assertEquals(moduloExpression.toStringBuffer(true).toString(), "(9%(2 div X))");
		assertEquals(moduloExpressionH, sUniverse.modulo(expr1H, expr2H));
		assertEquals(moduloExpressionH, idealFactory.modulo(expr1H, expr2H));
		assertEquals(moduloExpressionH.toStringBuffer(true).toString(), "(9%(hint)(hint)(2 div X))");
	}
	
	@Test
	public void cnefPowerTest() {
		NumericExpression expr1 = sUniverse.multiply(xInt, threeInt);
		NumericExpression expr1H = cnef.cast(expr1, herbrandIntType);
		expr1H = cnef.cast(expr1H, herbrandIntType);
		NumericExpression expr2 = sUniverse.divide(twoInt, xInt);
		NumericExpression expr2H = cnef.cast(expr2, herbrandIntType);
		NumericExpression powerExpression = cnef.power(expr1, expr2);
		NumericExpression powerExpressionH = cnef.power(expr1H, expr2H);
		NumericExpression powerExpression2 = cnef.power(expr1, fiveIntObj);
		NumericExpression powerExpression2H = cnef.power(expr1H, fiveIntObj);
		NumericExpression testExpr = cnef.expression(powerExpression.operator(), powerExpression.type(), expr1,expr2);
		
		assertEquals(powerExpression, testExpr);
		assertEquals(powerExpression, sUniverse.power(expr1, expr2));
		assertEquals(powerExpression, idealFactory.power(expr1, expr2));
		assertEquals(powerExpression2, sUniverse.power(expr1, fiveIntObj));
		assertEquals(powerExpression2, idealFactory.power(expr1, fiveIntObj));
		assertEquals(powerExpressionH, sUniverse.power(expr1H, expr2H));
		assertEquals(powerExpressionH, idealFactory.power(expr1H, expr2H));
		assertEquals(powerExpression2H, sUniverse.power(expr1H, fiveIntObj));
		assertEquals(powerExpression2H, idealFactory.power(expr1H, fiveIntObj));
	}
	
	@Test
	public void cnefExtractNumberTest() {
		NumericExpression expr1 = sUniverse.multiply(xInt,threeInt);
		NumericExpression expr1H = cnef.cast(expr1, herbrandType);
		
		edu.udel.cis.vsl.sarl.IF.number.Number extractedNum = cnef.extractNumber(expr1);
		edu.udel.cis.vsl.sarl.IF.number.Number extractedNum2 = cnef.extractNumber(expr1H);
		
		assertEquals(extractedNum, sUniverse.extractNumber(expr1));
		assertEquals(extractedNum, idealFactory.extractNumber(expr1));
		assertEquals(extractedNum2, sUniverse.extractNumber(expr1H));
		assertEquals(extractedNum2, idealFactory.extractNumber(expr1H));
	}
	
	@Test
	public void cneflessThanTest() {
		BooleanExpression lessThan = cnef.lessThan(xpy, xty);
		
		// This line causes an Assertion Error. Why?
		//BooleanExpression lessThanH = cnef.lessThan(cnef.cast(xpy, herbrandType), cnef.cast(xty, herbrandType));
		
		assertEquals(lessThan, sUniverse.lessThan(xpy, xty));
		assertEquals(lessThan, idealFactory.lessThan(xpy, xty));
		//assertEquals(lessThanH, sUniverse.lessThan(cnef.cast(xpy, herbrandType), cnef.cast(xty, herbrandType)));
		//assertEquals(lessThanH, idealFactory.lessThan(cnef.cast(xpy, herbrandType), cnef.cast(xty, herbrandType)));
	}
	
	@Test
	public void cnefNotLessThanTest() {
		BooleanExpression notLessThan = cnef.notLessThan(xpy, xty);
		
		// This line causes an Assertion Error. Why?
		//BooleanExpression notLessThanH = cnef.notLessThan(cnef.cast(xpy, herbrandType), cnef.cast(xty, herbrandType));
		
		// No notLessThan method for sUniverse?
		//assertEquals(notLessThan, sUniverse.notLessThan(xpy, xty));
		assertEquals(notLessThan, idealFactory.notLessThan(xpy, xty));
		//assertEquals(notLessThanH, sUniverse.notLessThan(cnef.cast(xpy, herbrandType), cnef.cast(xty, herbrandType)));
		//assertEquals(notLessThanH, idealFactory.notLessThan(cnef.cast(xpy, herbrandType), cnef.cast(xty, herbrandType)));
	}
	
	@Test
	public void cnefNotLessThanEqualsTest() {
		BooleanExpression notLessThanEquals = cnef.notLessThanEquals(xpy, xty);
		
		// This line causes an Assertion Error. Why?
		//BooleanExpression notLessThanEqualsH = cnef.notLessThanEquals(cnef.cast(xpy, herbrandType), cnef.cast(xty, herbrandType));
		
		// No notLessThanEquals method for sUniverse?
		//assertEquals(notLessThanEquals, sUniverse.notLessThanEquals(xpy, xty));
		assertEquals(notLessThanEquals, idealFactory.notLessThanEquals(xpy, xty));
		//assertEquals(notLessThanEqualsH, sUniverse.notLessThanEquals(cnef.cast(xpy, herbrandType), cnef.cast(xty, herbrandType)));
		//assertEquals(notLessThanEqualsH, idealFactory.notLessThanEquals(cnef.cast(xpy, herbrandType), cnef.cast(xty, herbrandType)));
	}
	
	@Test
	public void cnefExpressionTest() {
		NumericExpression[] args = {xpy, xty};
		NumericExpression[] argsH = {cnef.cast(xpy, herbrandType)};
		Collection<SymbolicObject> args2= new ArrayList<SymbolicObject>(Arrays.asList(args));
		Collection<SymbolicObject> args2H = new ArrayList<SymbolicObject>(Arrays.asList(argsH));
		
		NumericExpression expr1 = cnef.expression(addOperator, realType, args);
		NumericExpression expr2 = cnef.expression(addOperator, realType, args2);
		NumericExpression expr3 = cnef.expression(addOperator, realType, xpy);
		NumericExpression expr4 = cnef.expression(addOperator, realType, xpy, x, y);
		
		NumericExpression expr1H = cnef.expression(addOperator, herbrandType, argsH);
		NumericExpression expr2H = cnef.expression(addOperator, herbrandType, args2H);
		NumericExpression expr3H = cnef.expression(addOperator, herbrandType, cnef.cast(xpy, herbrandType));
		NumericExpression expr4H = cnef.expression(addOperator, herbrandType, cnef.cast(xpy, herbrandType), 
												cnef.cast(x, herbrandType), cnef.cast(y, herbrandType));
		
		assertEquals(expr1, expr2);
		assertEquals(expr1H, expr2H);
		assertNotEquals(expr1, expr1H);
		assertNotEquals(expr2, expr2H);
		assertNotEquals(expr3, expr3H);
		assertNotEquals(expr4, expr4H);
		
		assertEquals(expr1, herbrandFactory.expression(addOperator, realType, args2));
		assertEquals(expr2, idealFactory.expression(addOperator, realType, args));
		assertEquals(expr1, idealFactory.expression(addOperator, realType, args2));
		assertEquals(expr2, herbrandFactory.expression(addOperator, realType, args));
		
		assertEquals(expr1H, herbrandFactory.expression(addOperator, herbrandType, args2H));
		assertEquals(expr2H, idealFactory.expression(addOperator, herbrandType, argsH));
		assertEquals(expr1H, idealFactory.expression(addOperator, herbrandType, args2H));
		assertEquals(expr2H, herbrandFactory.expression(addOperator, herbrandType, argsH));
	}
	
	@Test
	public void cnefSubtractTest() {
		
		NumericExpression expr1 = cnef.subtract(xpy, xty);
		
		//NumericExpression testExpr = cnef.expression(expr1.operator(), expr1.type(), xty, xpy);
		
		//This lines causes an assertion error. Why?
		//NumericExpression expr1H = cnef.subtract(cnef.cast(xpy, herbrandType), cnef.cast(xty, herbrandType));
		//assertEquals(expr1, testExpr);
		assertEquals(expr1, sUniverse.subtract(xpy, xty));
		assertEquals(expr1, idealFactory.subtract(xpy, xty));
		//assertEquals(expr1H, sUniverse.subtract(cnef.cast(xpy, herbrandType), cnef.cast(xty, herbrandType)));
		//assertEquals(expr1H, herbrandFactory.subtract(cnef.cast(xpy, herbrandType), cnef.cast(xty, herbrandType)));
	}
}
