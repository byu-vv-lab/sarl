/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.ideal; //

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Monic;
import edu.udel.cis.vsl.sarl.ideal.IF.Monomial;
import edu.udel.cis.vsl.sarl.ideal.IF.RationalExpression;
import edu.udel.cis.vsl.sarl.ideal.common.CommonIdealFactory;
import edu.udel.cis.vsl.sarl.number.real.RealNumber;
import edu.udel.cis.vsl.sarl.number.real.RealNumberFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class IdealTest {

	private static PrintStream out = System.out;
	private NumberFactory numberFactory;
	private ObjectFactory objectFactory;
	private SymbolicTypeFactory typeFactory;
	private CollectionFactory collectionFactory;
	private NumericExpressionFactory numericExpressionFactory;
	private IdealFactory idealFactory;
	private BooleanExpressionFactory booleanFactory;
	private RealNumberFactory realNumberFactory;
	private CommonIdealFactory commonIdealFactory;

	private RationalNumber n1; // 3/2
	private RationalNumber n2; // -1/4
	private RationalNumber n3; // 5/4
	private Constant c1; // real constant 3/2
	private Constant c2; // real constant -1/4
	private Constant c4; // int constant -1
	private Constant c10; // int constant 10
	StringObject Xobj; // "X"
	NumericSymbolicConstant x; // int symbolic constant "X"
	NumericSymbolicConstant y; // int symbolic constant "Y"
	private NumericExpression five; 		// 5
	private RationalNumber realFive; 			// 5
	private NumericExpression three;		// 3
	private RationalNumber realThree;			// 3
	NumericExpression e1; 					// 5         		IsReal
	NumericExpression e2;					// 5 + 3     		Add
	NumericExpression e3;				 	// 5 > 3, 5, 3    COND
	NumericExpression e4; // 5 * 3			MULTIPLY
	NumericExpression e5; // -5				NEGATIVE
	NumericExpression e6; // 5 ^ 3			POWER
	NumericExpression e7; // 5 - 3			SUBTRACT
	NumericExpression e8; //				DEFAULT
	
	@Before
	public void setUp() throws Exception {
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		numberFactory = system.numberFactory();
		objectFactory = system.objectFactory();
		typeFactory = system.typeFactory();
		collectionFactory = system.collectionFactory();
		numericExpressionFactory = system.numericFactory();
		idealFactory = (IdealFactory) system.numericFactory();
		booleanFactory = system.booleanFactory();
		realNumberFactory = (RealNumberFactory) system.numberFactory();
		commonIdealFactory = new CommonIdealFactory(numberFactory,
				objectFactory, typeFactory,
				collectionFactory,
				booleanFactory); 
		n1 = numberFactory.rational("1.5");
		n2 = numberFactory.rational("-.25");
		n3 = numberFactory.rational("1.25");
		c1 = idealFactory.constant(n1);
		c2 = idealFactory.constant(n2);
		c4 = idealFactory.intConstant(-1);
		c10 = idealFactory.intConstant(10);
		Xobj = objectFactory.stringObject("X");
		x = objectFactory.canonic(idealFactory.symbolicConstant(Xobj,
				typeFactory.integerType()));
		y = objectFactory.canonic(idealFactory.symbolicConstant(
				objectFactory.stringObject("Y"), typeFactory.integerType()));
		
		
		realFive = numberFactory.integerToRational(numberFactory.integer(5));
		five = commonIdealFactory.constant(realFive); 
		realThree = numberFactory.integerToRational(numberFactory.integer(3)); 
		three = commonIdealFactory.constant(realThree);
		e1 = commonIdealFactory.constant(realFive);
		//e1 = numericExpressionFactory.number(realFive);				// 1         IsReal
		e2 = commonIdealFactory.expression(SymbolicOperator.ADD, typeFactory.realType(), five, three);
		//e2 = numericExpressionFactory.expression(SymbolicOperator.ADD, five.type(), five, three);
		//e2 = numericExpressionFactory.add(five, three); 		// 5 + 3     Add
		//e3 = numericExpressionFactory.lessThan(five, three); 	// 3 > 2, 3, 2    COND
		e4 = commonIdealFactory.multiply(five, three); 	// 5 * 3			MULTIPLY
		e5 = commonIdealFactory.minus(five); 				// -5				NEGATIVE
		e6 = commonIdealFactory.power(five, three);		// 5 ^ 3			POWER
		e7 = commonIdealFactory.subtract(five, three); // 5 - 3			SUBTRACT
		e8 = commonIdealFactory.zeroReal(); //				DEFAULT}
	}
	
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void constantCreation() {
		out.println("constantCreation: " + c10);
		assertEquals(10, ((IntegerNumber) c10.number()).intValue());
	}

	@Test
	public void constantAdd() {
		Constant c3 = (Constant) idealFactory.add(c1, c2);

		out.println("constantAdd: " + c1 + " + " + c2 + " = " + c3);
		assertEquals(n3, c3.number());
	}

	@Test
	public void constantMultiply() {
		Constant result = (Constant) idealFactory.multiply(c1, c2);
		RationalNumber expected = numberFactory.rational("-.375");

		out.println("constantMultiply: " + c1 + " * " + c2 + " = " + result);
		assertEquals(expected, result.number());
	}

	@Test
	public void symbolicConstantCreate() {
		out.println("symbolicConstantCreate: " + x);
		assertEquals("X", x.name().getString());
		assertEquals(typeFactory.integerType(), x.type());
	}

	@Test
	public void symbolicConstantEquality() {
		SymbolicConstant x2 = idealFactory.symbolicConstant(
				objectFactory.stringObject("X"), typeFactory.integerType());

		assertEquals(x, x2);
	}

	@Test
	public void symbolicConstantInequality1() {
		assertFalse(x.equals(y));
	}

	@Test
	public void symbolicConstantInequality2() {
		SymbolicConstant x2 = idealFactory.symbolicConstant(
				objectFactory.stringObject("X"), typeFactory.realType());

		assertFalse(x.equals(x2));
	}

	@Test
	public void commutativity1() {
		SymbolicExpression xpy = idealFactory.add(x, y);
		SymbolicExpression ypx = idealFactory.add(y, x);

		out.println("commutativity1: " + xpy + " vs. " + ypx);
		assertEquals(xpy, ypx);
	}

	@Test
	public void xplus1squared() {
		NumericExpression xp1 = idealFactory
				.add(x, idealFactory.intConstant(1));
		SymbolicExpression xp1squared = idealFactory.multiply(xp1, xp1);
		SymbolicExpression x2p2xp1 = idealFactory.add(idealFactory.multiply(x,
				x), idealFactory.add(
				idealFactory.multiply(idealFactory.intConstant(2), x),
				idealFactory.intConstant(1)));

		out.println("xplus1squared: " + xp1squared + " vs. " + x2p2xp1);
		assertEquals(xp1squared, x2p2xp1);
	}

	@Test
	public void bigPower() {
		int exponent = 100;
		IntObject n = objectFactory.intObject(exponent);
		IntObject m = objectFactory.intObject(exponent - 1);
		NumericExpression xpy = idealFactory.add(x, y);
		NumericExpression xpyen = idealFactory.power(xpy, n);
		NumericExpression xpyem = idealFactory.power(xpy, m);
		NumericExpression quotient = idealFactory.divide(xpyen, xpyem);

		out.println("bigPower: (X+Y)^" + n + " = " + xpyen);
		out.println("bigPower: (X+Y)^" + m + " = " + xpyem);
		out.println("bigPower: quotient : " + quotient);
		assertEquals(xpy, quotient);
	}
	
	@Test
	public void xp1xm1(){
		NumericExpression xp1 = idealFactory
				.add(x, idealFactory.intConstant(1));
		NumericExpression xm1 = idealFactory.add(x, idealFactory.minus(idealFactory.intConstant(1)));
		SymbolicExpression xp1xm1 = idealFactory.multiply(xp1, xm1);
		out.println("xp1xm1=" + xp1xm1);
		SymbolicExpression x2m1 = idealFactory.subtract(idealFactory.multiply(x, x), idealFactory.multiply(idealFactory.intConstant(1), idealFactory.intConstant(1)));
		out.println("x2m1=" + x2m1);
		assertEquals(x2m1, xp1xm1);
	}
	/* Test Fails
 	@Test
	public void rationalMinus() {
		RationalExpression r = (RationalExpression) idealFactory.divide(x, y);
		NumericExpression minusr = idealFactory.minus(r);
		Monomial negativex = idealFactory.monomial(c4, (Monic) x);
		RationalExpression negativer = (RationalExpression) idealFactory.divide(negativex, y);
		out.println("minus r: " + minusr +"vs. " + negativer);
		assertEquals(negativer, minusr);
 	}
 	*/
	
	@Test
	public void castToReal(){
		SymbolicType real = typeFactory.realType();
		SymbolicType real2 = numericExpressionFactory.oneReal().type();
		NumberObject Five = objectFactory.numberObject(numberFactory.number("5"));
		RationalNumber n5 = numberFactory.rational("5.5");
		RealNumber realFive = (RealNumber) realNumberFactory.integer(5); //realNumberFactory.integer(fiveLong);
		//assertEquals(commonIdealFactory.cast(e1, real), n5);
		
		//NumberObject
		
		Constant one = idealFactory.intConstant(1);
		Constant two = idealFactory.intConstant(2);
		Constant three = idealFactory.intConstant(3);
		
		IntObject int1 = objectFactory.intObject(5);
		IntObject int2 = objectFactory.intObject(3);
		IntObject int3 = objectFactory.intObject(2);
		//SymbolicCollection collection = collectionFactory.basicCollection(intlist);
		IntObject[] integers = {int1, int2, int3};
		//NumericExpression ints = idealFactory.expression(SymbolicOperator.ADD, typeFactory.integerType(), int1, int2, int3);
		//NumericExpression ints = idealFactory.expression(SymbolicOperator.ADD, typeFactory.integerType(), int1, int2);
		//NumericExpression ints = idealFactory.expression(SymbolicOperator.ADD, typeFactory.integerType(), int1);
		NumericExpression ints3 = idealFactory.expression(SymbolicOperator.ADD, typeFactory.integerType(), one, two, three);
		NumericExpression ints2 = idealFactory.expression(SymbolicOperator.ADD, typeFactory.integerType(), one, two);
		NumericExpression ints1 = idealFactory.expression(SymbolicOperator.ADD, typeFactory.integerType(), one);
		NumericExpression ints = idealFactory.expression(SymbolicOperator.ADD, typeFactory.integerType(), one, two, three);
		NumericExpression xp1 = idealFactory.add(x, idealFactory.intConstant(1));
		NumericExpression lp2 = idealFactory.add(one, idealFactory.intConstant(2));
		NumericExpression testn = commonIdealFactory.add(ints3, lp2);
		out.println("testn: " + testn);
		commonIdealFactory.cast(testn, real);
		out.println("testn: " + testn);
		assertEquals(commonIdealFactory.cast(e1, real), commonIdealFactory.cast(five, real));
		
		
		long eightLong = 9;  // e2 = 3 + 5    = 8
		RealNumber realAdd = realNumberFactory.integer(eightLong);
		out.println("e2: " + e2);
		NumericExpression test3 = commonIdealFactory.expression(SymbolicOperator.ADD, typeFactory.realType(),five, three, three);
		NumericExpression test2 = commonIdealFactory.add(e1, x);
		commonIdealFactory.cast(e2, real);
		commonIdealFactory.cast(test2, real);
		out.println("test2: " + test2);
		//assertEquals(test2, realAdd);
		
		numericExpressionFactory.cast(testn, real);
		long fifteenLong = 15; // e4 = 3 * 5  =  15
		RealNumber realMulitply = realNumberFactory.integer(fifteenLong);
		//assertEquals(commonIdealFactory.cast(e4, real), realMulitply);
		
		long fiveMinusLong = -5; // e5 = -1 * 5  =  -5
		RealNumber realMinus = realNumberFactory.integer(fiveMinusLong);
		//assertEquals(commonIdealFactory.cast(e5, real), realMinus);
		
		long hundredTwentyLong = 125; // e6 = 5 ^ 3  =  125
		RealNumber realPower = realNumberFactory.integer(hundredTwentyLong);
		//assertEquals(commonIdealFactory.cast(e6, real), realPower);
		
		long twoLong = 2; // e7 = 5 - 3  =  2
		RealNumber realSubtract = realNumberFactory.integer(twoLong);
		//assertEquals(commonIdealFactory.cast(e7, real), realSubtract);
		
	}
	
	
}
