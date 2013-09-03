package edu.udel.cis.vsl.sarl.IF;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

public class malsulmiTest {

	private static SymbolicUniverse universe;
	private static NumericSymbolicConstant x_var;
	private static NumericSymbolicConstant y_var;
	private static NumericExpression x_plus_y;
	private static NumericExpression one, two, five;
	private static BooleanExpression bExpression;
	private static SymbolicType realType;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		universe = SARL.newStandardUniverse();
		realType = universe.realType();
		x_var = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("x"), realType);
		y_var = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("y"), realType);
		x_plus_y = (NumericExpression) universe.add(x_var, y_var);
		one = universe.rational(1);
		two = universe.rational(2);
		five = universe.integer(5);
			
	}
	
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	@Test
	public void test1(){
		NumericExpression expression1, expression2;
		System.out.println("x = "+x_var);
		System.out.println("Y = "+y_var);
		System.out.println(" X+Y = " + x_plus_y);
		System.out.println(universe.add(x_plus_y, x_plus_y));
	
		
		expression1 = universe.multiply(x_plus_y,two);
		expression2 = universe.add(x_plus_y,x_plus_y);
		System.out.println(">>> = "+expression1);
		assertEquals(expression1, expression2);;

	}
	@Ignore
	@Test
	public void test2(){
		NumericExpression power = universe.power(five, 2);
		System.out.println(power);
		
	}
	@Ignore
	@Test
	public void test3()
	{
		bExpression = universe.equals(x_var, universe.rational(5));
		Reasoner reasoner = universe.reasoner(bExpression);
		SymbolicExpression newExp = reasoner.simplify(x_plus_y);
		System.out.print(newExp);
		assertEquals(newExp,universe.add(y_var,universe.rational(5)));
				
	}
	@Test
	public void test4(){
		NumericExpression x_plus_one = universe.add(x_var,universe.rational(1));
		NumericExpression twoX_plus_one = universe.add(universe.multiply(x_var,universe.rational(2)),universe.rational(1));
		BooleanExpression claim1 = universe.equals(y_var, x_plus_one);
		BooleanExpression claim2 = universe.equals(universe.multiply(y_var, universe.rational(3)),twoX_plus_one);
		BooleanExpression claim = universe.and(claim1,claim2);
		Reasoner reasoner = universe.reasoner(claim);
		NumericExpression xsim = reasoner.simplify(x_var);
		NumericExpression ysim = reasoner.simplify(y_var);
		System.out.println(" X = "+xsim);
		System.out.println("Y = "+ysim);
	}

	
}
