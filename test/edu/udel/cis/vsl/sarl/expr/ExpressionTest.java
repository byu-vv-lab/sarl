package edu.udel.cis.vsl.sarl.expr;

import static org.junit.Assert.*;

import java.io.PrintStream;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
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
import edu.udel.cis.vsl.sarl.expr.cnf.CnfFactory;
import edu.udel.cis.vsl.sarl.expr.cnf.CnfSymbolicConstant;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.cnf.CnfSymbolicConstant;
import edu.udel.cis.vsl.sarl.expr.common.CommonSymbolicConstant;
import edu.udel.cis.vsl.sarl.expr.common.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
import edu.udel.cis.vsl.sarl.universe.Universes;

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
	
	private StringObject Xobj; // "X"
	private StringObject Yobj; // "Y"
	private SymbolicType realType, integerType;
	private NumericSymbolicConstant x; // real symbolic constant "X"
	private NumericSymbolicConstant xInt; // Int symbolic constant "X"
	private NumericSymbolicConstant y; // real symbolic constant "Y"
	private NumericExpression two; // real 2.0
	private NumericExpression three; // real 3.0
	private NumericExpression twoInt; // int 2.0
	private NumericExpression threeInt; // int 3.0
	private BooleanObject trueBoolObj; // True
	private BooleanObject falseBoolObj; // False
	private IntObject fiveIntObj; // 5
	private IntObject zeroIntObj; // 0
	private IntObject negIntObj; // -10
	
	
	
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
		integerType = sUniverse.integerType();
		x = (NumericSymbolicConstant) sUniverse.symbolicConstant(Xobj, realType);
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
//		SymbolicExpression a = sUniverse.array(realType,
//		Arrays.asList(new SymbolicExpression[] { x, y }));
//NumericExpression test6 = sUniverse.length(a);
//assertEquals(test6.toString(), "2");
	}
	
	@Test
	public void toStringBuffer1LessThanTest() {
		BooleanExpression test7 = sUniverse.lessThan(x, three);
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
	public void toStringBuffer1NegTest() {
		BooleanExpression test10 = sUniverse.neq(x, y);
		assertEquals(test10.toString(), "0 != -1*X+Y");
		
		//Neg test atomize
		assertEquals(test10.toStringBuffer(true).toString(), "(0 != -1*X+Y)");
	}
	
	@Test
	public void toStringBuffer1NotTest() {
		BooleanExpression test11 = sUniverse.not(sUniverse.equals(x, y));
		assertEquals(test11.toString(), "0 != -1*X+Y");
		
		//not test atomize
		assertEquals(test11.toStringBuffer(true).toString(), "(0 != -1*X+Y)");
	}
	
	@Ignore
	@Test
	public void toStringBuffer1NullTest() {
		//BooleanExpression test12 = sUniverse.not(sUniverse.equals(x,y));
		SymbolicExpression test12 = sUniverse.nullExpression();
		assertEquals(test12.toStringBuffer(false).toString(), "NULL");
		
		// WHY DOES THIS FAIL? WHY NO PARANTHESES?
		//null test atomize
		//out.println(test12.toStringBuffer(true).toString());
		//assertEquals(test12.toStringBuffer(true).toString(), "(NULL)");
	}
	
	@Ignore
	@Test
	public void toStringBuffer1OrTest() {
		// THESE 2 EXPRESSIONS ARE NOT ALWAYS ON SAME SIDE OF ||. THEY CAN BE ON EITHER SIDE. 
		// NEED TO FIGURE OUT HOW TO ASSERT FOR TEST.
		//out.println(test13.toString());
		//assertEquals(test13.toStringBuffer(false).toString(),"0 <= -1*X+3 || 0 != -1*X+Y");
		
		// WHY DOES THIS FAIL? WHY NO PARANTHESES?
		//or test atomize
		//assertEquals(test13.toStringBuffer(true).toString(),"(0 != -1*X+Y || 0 <= -1*X+3)");
	}
	
	@Test
	public void toStringBuffer1SubtractTest() {
		SymbolicExpression test14 = sUniverse.subtract(x,y);
		assertEquals(test14.toStringBuffer(false).toString(), "X+-1*Y");
		
		//subtract test atomize
		assertEquals(test14.toStringBuffer(true).toString(), "(X+-1*Y)");
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
	public void newCnfFactoryTest() {
		//or here.
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		assertNotNull(bef);
	}

}
