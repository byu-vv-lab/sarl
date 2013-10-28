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
import org.junit.Ignore;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
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
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Monic;
import edu.udel.cis.vsl.sarl.ideal.IF.Monomial;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;
import edu.udel.cis.vsl.sarl.ideal.IF.RationalExpression;
import edu.udel.cis.vsl.sarl.ideal.common.CommonIdealFactory;
import edu.udel.cis.vsl.sarl.ideal.common.NumericPrimitive;
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
	private IdealFactory idealFactory;
	private BooleanExpressionFactory booleanFactory;
	private CommonIdealFactory commonIdealFactory;

	private RationalNumber ratNegPointTwoFive; // -1/4
	private RationalNumber ratZero; // 0
	private RationalNumber ratOne; // 1	
	private RationalNumber ratOnePointFive; // 3/2
	private RationalNumber ratOnePointTwoFive; // 5/4
	private RationalNumber ratThree; // 3
	private Constant constZero;
	private Constant constOnePointFive; // real constant 3/2
	private Constant constOne;
	private Constant constNegPointTwoFive; // real constant -1/4
	private Constant intNegOne; // int constant -1
	private Constant intZero; // int constant 0
	private Constant intOne; // int constant 1
	private Constant intTwo; // int constant 2
	private Constant intThree; // int constant 3
	private Constant intTen; // int constant 10
	StringObject Xobj; // "X"
	IntObject intObj3;
	NumberObject numObj3;
	NumericSymbolicConstant x; // int symbolic constant "X"
	NumericSymbolicConstant y; // int symbolic constant "Y"
	private NumericExpression fifteen;
	private NumericExpression five;
	private NumericExpression seven;
	private NumericExpression twentyOne;
	private NumericExpression thirtyFive;
	private NumericExpression oneTwoFive;
	private NumericExpression zero;
	private NumericExpression one;
	private RationalNumber realZero;
	private RationalNumber realOne;
	private RationalNumber realFifteen;
	private RationalNumber realFive; 
	private NumericExpression three; 
	private RationalNumber realThree; 
	private RationalNumber realSeven;
	private RationalNumber realTwentyOne;
	private RationalNumber realThirtyFive;
	private RationalNumber realOneTwoFive;
	private SymbolicType real;
	private SymbolicType integer;
	NumericExpression intHundred;
	NumericExpression intTwenty;
	NumericExpression e01; // Real 3 cast to integer 3
	NumericExpression e1; // 5 IsReal
	NumericExpression e2; // 5 + 3 ADD
	NumericExpression e3; // 5 > 3, 5, 3 COND
	NumericExpression e4; // 5 * 3 MULTIPLY
	NumericExpression e5; // -5 NEGATIVE
	NumericExpression e6; // 5 ^ 3 POWER
	NumericExpression e7; // 5 - 3 SUBTRACT
	NumericExpression e8; // DEFAULT
	NumericExpression e9; // 5 + 3 + 1 ADD
	NumericExpression e10; // 5 ^ 3   (3 - IntObject)

	@Before
	public void setUp() throws Exception {
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		numberFactory = system.numberFactory();
		objectFactory = system.objectFactory();
		typeFactory = system.typeFactory();
		collectionFactory = system.collectionFactory();
		idealFactory = (IdealFactory) system.numericFactory();
		booleanFactory = system.booleanFactory();
		commonIdealFactory = new CommonIdealFactory(numberFactory,
				objectFactory, typeFactory, collectionFactory, booleanFactory);
		ratZero = numberFactory.rational("0");
		ratOne = numberFactory.rational("1");
		ratOnePointFive = numberFactory.rational("1.5");
		ratNegPointTwoFive = numberFactory.rational("-.25");
		ratOnePointTwoFive = numberFactory.rational("1.25");
		ratThree = numberFactory.rational("3");
		constZero = idealFactory.constant(ratZero);
		intZero = idealFactory.intConstant(0);
		constOnePointFive = idealFactory.constant(ratOnePointFive);
		constOne = idealFactory.constant(ratOne);
		constNegPointTwoFive = idealFactory.constant(ratNegPointTwoFive);
		intNegOne = idealFactory.intConstant(-1);
		intOne = idealFactory.intConstant(1);
		intTwo = idealFactory.intConstant(2);
		intThree = idealFactory.intConstant(3);
		intTen = idealFactory.intConstant(10);
		typeFactory.integerType();
		typeFactory.integerType();
		intObj3 = objectFactory.intObject(3);
		numObj3 = objectFactory.numberObject(ratThree);
		Xobj = objectFactory.stringObject("X");
		x = objectFactory.canonic(idealFactory.symbolicConstant(Xobj,
				typeFactory.integerType()));
		y = objectFactory.canonic(idealFactory.symbolicConstant(
				objectFactory.stringObject("Y"), typeFactory.integerType()));
		real = typeFactory.realType();
		integer = typeFactory.integerType();
		realZero = numberFactory.rational("0");
		realOne = numberFactory.rational("1");
		realFifteen = numberFactory.rational("15");
		realFive = numberFactory.rational("5");
		realSeven = numberFactory.rational("7");
		realTwentyOne = numberFactory.rational("21");
		realThirtyFive = numberFactory.rational("35");
		realOneTwoFive = numberFactory.rational("125");
		zero = commonIdealFactory.constant(realZero);
		one = commonIdealFactory.constant(realOne);
		fifteen = commonIdealFactory.constant(realFifteen);
		five = commonIdealFactory.constant(realFive);
		seven = commonIdealFactory.constant(realSeven);
		twentyOne = commonIdealFactory.constant(realTwentyOne);
		thirtyFive = commonIdealFactory.constant(realThirtyFive);
		oneTwoFive = commonIdealFactory.constant(realOneTwoFive);
		realThree = numberFactory.rational("3");
		three = commonIdealFactory.constant(realThree);
		intHundred = idealFactory.intConstant(100);
		intTwenty = idealFactory.intConstant(20);
		e01 = commonIdealFactory.expression(SymbolicOperator.CAST, 
				real, three);
		e1 = commonIdealFactory.constant(realFive);
		e2 = commonIdealFactory.expression(SymbolicOperator.ADD, integer, five,
				three); // 5 + 3 ADD
		e3 = commonIdealFactory.expression(SymbolicOperator.COND,
				real, x, booleanFactory.trueExpr(), 
				booleanFactory.falseExpr());
		e4 = commonIdealFactory.expression(SymbolicOperator.MULTIPLY, integer,
				five, three); // 5 * 3 MULTIPLY
		e5 = commonIdealFactory.expression(SymbolicOperator.NEGATIVE, integer,
				five); // -5 NEGATIVE
		e6 = commonIdealFactory.expression(SymbolicOperator.POWER, integer,
				five, three); // 5 ^ 3 POWER
		e7 = commonIdealFactory.expression(SymbolicOperator.SUBTRACT, integer,
				five, three); // 5 - 3 SUBTRACT
		e8 = commonIdealFactory.zeroReal(); // DEFAULT}
		e9 = commonIdealFactory.expression(SymbolicOperator.ADD, integer, five,
				three, one); // 5 + 3 +1 ADD
		e10 = commonIdealFactory.expression(SymbolicOperator.POWER, integer,
				five, intObj3); // 5 ^ 3 POWER

	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	/**
	 * Checks whether the Constant is created correctly or not.
	 * 
	 * @param type
	 * 				 Constant
	 */
	@Test
	public void constantCreation() {
		out.println("constantCreation: " + intTen);
		
		assertEquals(10, ((IntegerNumber) intTen.number()).intValue());
	}
	
	/**
	 * Adds two Constants
	 * 
	 * @param type
	 * 				Constant
	 */
	@Test
	public void constantAdd() {
		Constant c3 = (Constant) idealFactory.add(constOnePointFive, constNegPointTwoFive);

		out.println("constantAdd: " + constOnePointFive + " + " + constNegPointTwoFive + " = " + c3);
		
		assertEquals(ratOnePointTwoFive, c3.number());
	}

	/**
	 * Multiplies two Constants
	 * 
	 * @param type
	 * 				Constant
	 */
	
	@Test
	public void constantMultiply() {
		Constant result = (Constant) idealFactory.multiply(constOnePointFive, constNegPointTwoFive);
		RationalNumber expected = numberFactory.rational("-.375");

		out.println("constantMultiply: " + constOnePointFive + " * " + constNegPointTwoFive + " = " + result);
		
		assertEquals(expected, result.number());
	}

	/**
	 * Asserts the Numeric Symbolic Constant x has the value "X"
	 * 
	 * @param type
	 * 				NumericSymbolicConstant
	 */
	@Test
	public void symbolicConstantCreate() {
		out.println("symbolicConstantCreate: " + x);
		
		assertEquals("X", x.name().getString());
		assertEquals(typeFactory.integerType(), x.type());
	}

	/**
	 * Checks if two Symbolic Constants are equal
	 * 
	 * @param type
	 * 				NumericSymbolicConstant
	 */
	@Test
	public void symbolicConstantEquality() {
		SymbolicConstant x2 = idealFactory.symbolicConstant(
				objectFactory.stringObject("X"), typeFactory.integerType());

		assertEquals(x, x2);
	}

	/**
	 * Checks if two Symbolic Constants are not equal
	 * 
	 * @param type
	 * 				NumericSymbolicConstant
	 */
	@Test
	public void symbolicConstantInequality1() {
		assertFalse(x.equals(y));
	}

	/**
	 * Returns false if two Symbolic Constants are of different type
	 * 
	 * @param type
	 * 				NumericSymbolicConstant
	 */
	@Test
	public void symbolicConstantInequality2() {
		SymbolicConstant x2 = idealFactory.symbolicConstant(
				objectFactory.stringObject("X"), real);
		
		assertFalse(x.equals(x2));
	}

	/**
	 * Shows that the commutative property holds for two Numeric Symbolic Constants
	 * 
	 * @param type
	 * 				NumericSymbolicConstant
	 */
	@Test
	public void commutativity1() {
		SymbolicExpression xpy = idealFactory.add(x, y);
		SymbolicExpression ypx = idealFactory.add(y, x);
		
		out.println("commutativity1: " + xpy + " vs. " + ypx);
		
		assertEquals(xpy, ypx);
	}

	/**
	 * Returns SymbolicExpression of Real type with the value equal to 1
	 */
	@Test
	public void realOne() {
		NumericExpression n2 = commonIdealFactory.oneReal();
		
		assertEquals(one, n2);
	}

	/**
	 * Asserts true if (x+1)^2 = x^2 + 2*x + 1
	 * 
	 * @param type
	 * 				SymbolicExpression of numeric type
	 */
	@Test
	public void xPlus1Squared() {
		NumericExpression xp1 = idealFactory.add(x, intOne);
		SymbolicExpression xp1squared = idealFactory.multiply(xp1, xp1);
		SymbolicExpression x2p2xp1 = idealFactory.add(idealFactory.multiply(x,
				x), idealFactory.add(idealFactory.multiply(intTwo, x), intOne));

		out.println("xplus1squared: " + xp1squared + " vs. " + x2p2xp1);
		
		assertEquals(xp1squared, x2p2xp1);
	}

	/**
	 * gives the result for [(x+y)^100] / [(x+y)^99] as (x+y)
	 * 
	 * @param type
	 * 				SymbolicExpression of numeric type
	 */
	@Ignore
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

	/**
	 * Asserts true if (x^2 - y^2) = (x+y) * (x-y)
	 * 
	 * @param type
	 * 				SymbolicExpression of Numeric type
	 */
	@Test
	public void xp1xm1() {
		NumericExpression xp1 = idealFactory.add(x, intOne);
		NumericExpression xm1 = idealFactory.add(x,
				idealFactory.minus(intOne));
		SymbolicExpression xp1xm1 = idealFactory.multiply(xp1, xm1);		
		SymbolicExpression x2m1 = idealFactory.subtract(idealFactory.multiply(x, x),
				idealFactory.multiply(intOne,intOne));
		
		out.println("xp1xm1=" + xp1xm1);
		out.println("x2m1=" + x2m1);
		
		assertEquals(x2m1, xp1xm1);
	}

	/**
	 * Adds two polynomials
	 * 
	 * @param type
	 * 				Polynomial
	 */
	@Test
	public void addPoly() {
		NumericExpression p1 = idealFactory.add(idealFactory.multiply(x, x), intOne);
		NumericExpression p2 = idealFactory.add(idealFactory.multiply(intTwo, 
				idealFactory.multiply(x, x)), intOne);
		NumericExpression p3 = idealFactory.multiply(intZero, x);
		NumericExpression p4 = idealFactory.add(idealFactory.multiply(intThree, 
				idealFactory.multiply(x, x)), intTwo);
		NumericExpression p5 = idealFactory.multiply(intTen, x);
		NumericExpression p6 = idealFactory.add(idealFactory.multiply(
				intTen, x), idealFactory.add(idealFactory.multiply(
						intTwo, idealFactory.multiply(x, x)), intOne));
		NumericExpression p7 = idealFactory.multiply(x, x);
		NumericExpression p8 = idealFactory.multiply(idealFactory.
				multiply(x, x), intTwo);
		NumericExpression p9 = idealFactory.multiply(idealFactory.
				multiply(x, x), intThree);
		NumericExpression p10 = idealFactory.add(x, idealFactory.multiply(x, x));
		NumericExpression p11 = idealFactory.add(x, intOne);
		Polynomial poly1 = (Polynomial) p1;
		Polynomial poly2 = (Polynomial) p2;
		Polynomial poly3 = (Polynomial) p3;
		Polynomial poly4 = (Polynomial) p5;
		Polynomial poly5 = (Polynomial) p7;
		Polynomial poly6 = (Polynomial) p8;
		Polynomial poly7 = (Polynomial) x;
						
		Polynomial b1 = commonIdealFactory.add(poly1, poly2);
		Polynomial b2 = commonIdealFactory.add(poly3, poly2);
		Polynomial b3 = commonIdealFactory.add(poly2, poly4);
		Polynomial b4 = commonIdealFactory.add(poly5, poly6);
		Polynomial b5 = commonIdealFactory.add(poly5, poly5);
		Polynomial b6 = commonIdealFactory.add(poly5, poly7);
		Polynomial b7 = commonIdealFactory.add(poly7, intOne);
		
		assertEquals(p4, b1);
		assertEquals(p2, b2);
		assertEquals(p6, b3);
		assertEquals(p9, b4);
		assertEquals(p8, b5);
		assertEquals(p10, b6);
		assertEquals(p11, b7);
	}

	/**
	 * Subtracts two polynomials
	 * 
	 * @param type
	 * 				Polynomial
	 */
	@Test
	public void subPoly() {
		NumericExpression p1 = idealFactory.add(idealFactory.multiply(x, x), intOne);
		NumericExpression p2 = idealFactory.add(idealFactory.multiply(intTwo, 
				idealFactory.multiply(x, x)), intOne);
		NumericExpression p3 = idealFactory.multiply(intZero, x);
		NumericExpression p4 = idealFactory.add(idealFactory.multiply(intThree, 
				idealFactory.multiply(x, x)), intTwo);
		NumericExpression p5 = idealFactory.multiply(intTen, x);
		NumericExpression p6 = idealFactory.add(idealFactory.multiply(
				intTen, x), idealFactory.add(idealFactory.multiply(
						intTwo, idealFactory.multiply(x, x)), intOne));
		NumericExpression p7 = idealFactory.multiply(x, x);
		NumericExpression p8 = idealFactory.multiply(idealFactory.
				multiply(x, x), intTwo);
		NumericExpression p9 = idealFactory.subtract(idealFactory.
				multiply(x, x), intOne);
		NumericExpression p10 = idealFactory.subtract(idealFactory.
				multiply(x, x), idealFactory.multiply(intTen, x));
		NumericExpression p11 = idealFactory.subtract(p7, x);
		Polynomial poly2 = (Polynomial) p2;
		Polynomial poly4 = (Polynomial) p4;
		Polynomial poly5 = (Polynomial) p5;
		Polynomial poly6 = (Polynomial) p6;
		Polynomial poly7 = (Polynomial) p7;
								
		NumericExpression b1 = commonIdealFactory.subtract(p2, p1);
		NumericExpression b2 = commonIdealFactory.subtract(poly4, poly2);
		NumericExpression b3 = commonIdealFactory.subtract(poly6, poly5);
		NumericExpression b4 = commonIdealFactory.subtract(poly5, poly5);
		NumericExpression b5 = commonIdealFactory.subtract(poly7, poly5);
		NumericExpression b6 = commonIdealFactory.subtract(poly7, poly7);
		NumericExpression b7 = commonIdealFactory.subtract(poly7, intOne);
		NumericExpression b8 = commonIdealFactory.subtract(poly7, x);
		
		assertEquals(p7, b1);
		assertEquals(p1, b2);
		assertEquals(p2, b3);
		assertEquals(intZero, b4);
		assertEquals(p10, b5);
		assertEquals(intZero, b6);
		assertEquals(p9, b7);
		assertEquals(p11, b8);
	}
	
	/**
	 * Returns the multiplication of two polynomials
	 * 
	 * @param type
	 * 				Polynomial
	 */
	@Test
	public void multPoly() {
		NumericExpression p1 = idealFactory.add(idealFactory.multiply(x, x), intOne);
		NumericExpression p2 = idealFactory.add(idealFactory.multiply(intTwo,
						idealFactory.multiply(x, x)), intOne);
		NumericExpression p3 = idealFactory.multiply(intZero, x);
		NumericExpression x2 = idealFactory.multiply(x, x);
		NumericExpression x4 = idealFactory.multiply(x2, x2);
		NumericExpression p4 = idealFactory.add(idealFactory.multiply(idealFactory.
				intConstant(3), idealFactory.multiply(x, x)), intOne);
		NumericExpression p5 = idealFactory.add(idealFactory.multiply(intTwo, x4), p4);
		NumericExpression p6 = idealFactory.multiply(intTen, x);
		NumericExpression p7 = idealFactory.add(idealFactory.multiply(intTen, idealFactory.multiply(x2, x)), p6);
		NumericExpression p8 = idealFactory.multiply(intTen, idealFactory.multiply(x2, x));
		NumericExpression p9 = idealFactory.multiply(x, x2);
		NumericExpression p10 = idealFactory.multiply(intTen, x);
		Polynomial poly1 = (Polynomial) p1;
		Polynomial poly2 = (Polynomial) p2;
		Polynomial poly3 = (Polynomial) p3;
		Polynomial poly4 = (Polynomial) p6;
		Polynomial poly5 = (Polynomial) x2;
		Polynomial poly6 = (Polynomial) x;
		
		Polynomial b1 = commonIdealFactory.multiply(poly1, poly2);
		Polynomial b2 = commonIdealFactory.multiply(poly1, poly3);
		Polynomial b3 = commonIdealFactory.multiply(poly4, poly1);
		Polynomial b4 = commonIdealFactory.multiply(poly4, poly5);
		Polynomial b5 = commonIdealFactory.multiply(poly5, poly5);
		Polynomial b6 = commonIdealFactory.multiply(poly5, poly6);
		Polynomial b7 = commonIdealFactory.multiply(poly6, intTen);
		out.println("Multiply_Polynomial=" + b1);
		
		assertEquals(p5, b1);
		assertEquals(intZero, b2);
		assertEquals(p7, b3);
		assertEquals(p8, b4);
		assertEquals(x4, b5);
		assertEquals(p9, b6);
		assertEquals(p10, b7);
	}

	/**
	 * Divides two polynomials
	 * 
	 * @param type
	 * 				the SymbolicExpression of numeric type of a Polynomial
	 */
	@Test
	public void divide() {
		NumericExpression n1 = idealFactory.multiply(intTen, x);
		NumericExpression n2 = idealFactory.multiply(intOne, x);
		NumericExpression n3 = idealFactory.multiply(intZero, x);
		NumericExpression n4 = idealFactory.multiply(intOne, intOne);
		NumericExpression n5 = idealFactory.multiply(fifteen, three);
		NumericExpression n6 = idealFactory.multiply(five, three);
		NumericExpression n7 = idealFactory.multiply(zero, three);
		NumericExpression n = idealFactory.add(x, y);
		NumericExpression m = idealFactory.subtract(x, y);
		NumericExpression np = idealFactory.divide(n, m);
		
		NumericExpression b1 = commonIdealFactory.divide(n1, n2);
		NumericExpression b2 = commonIdealFactory.divide(n3, n2);
		NumericExpression b3 = commonIdealFactory.divide(n1, n4);
		NumericExpression b4 = commonIdealFactory.divide(n5, n6);
		NumericExpression b5 = commonIdealFactory.divide(n7, n6);
		NumericExpression p1 = commonIdealFactory.divide(n, m);
		out.println("b1=" + b1);
		
		assertEquals(np, p1);
		assertEquals(intTen, b1);
		assertEquals(intZero, b2);
		assertEquals(n1, b3);
		assertEquals(three, b4);
		assertEquals(zero, b5);
	}
	
	/**
	 * Returns the expression by removing the constant term present in the passed argument.
	 * 
	 * @param type
	 * 				Polynomial
	 */
	@Test
	public void constantTermSubtraction() {
		NumericExpression n = idealFactory.add(idealFactory.multiply(one, x), intOne);
		NumericExpression m = idealFactory.add(idealFactory.multiply(one, x), intZero);
		NumericExpression o = idealFactory.add(idealFactory.multiply(intTen, 
				idealFactory.multiply(x, x)),(idealFactory.add(
						idealFactory.multiply(intTen, x), intOne)));
		NumericExpression p = idealFactory.add(idealFactory.multiply(intTen, 
				idealFactory.multiply(x, x)), idealFactory.multiply(intTen, x));
		Polynomial poly1 = (Polynomial) n;
		Polynomial poly2 = (Polynomial) m;
		Polynomial poly3 = (Polynomial) o;
		
		Polynomial b1 = commonIdealFactory.subtractConstantTerm(poly1);
		Polynomial b2 = commonIdealFactory.subtractConstantTerm(poly2);
		Polynomial b3 = commonIdealFactory.subtractConstantTerm(poly3);
		Polynomial b4 = commonIdealFactory.subtractConstantTerm(constZero);
		out.println("Constant Term Subtraction1=" + b3);
		
		assertEquals(x, b1);
		assertEquals(x, b2);
		assertEquals(p, b3);
		assertEquals(constZero, b4);
	}

	/**
	 * Returns the negation for the given argument
	 * 
	 * @param type
	 * 				SymbolicExpression of integer or real type
	 * 
	 */
	@Test
	public void minus() {
		NumericExpression p1 = idealFactory.add(idealFactory.multiply(x, x),intOne);
		NumericExpression p2 = intZero;
		NumericExpression n = idealFactory.minus(p1);
		NumericExpression m = idealFactory.minus(p2);
		
		NumericExpression m1 = commonIdealFactory.minus(p2);
		NumericExpression n1 = commonIdealFactory.minus(p1);
		
		assertEquals(n, n1);
		assertEquals(m, m1);
	}

	/**
	 * Returns true if the first argument is 'not less than' the second argument and 
	 * vice-versa.
	 * 
	 * @param type
	 * 				NumericExpression
	 * 
	 * @return type
	 * 				BooleanExpression
	 */
	@Test
	public void notLessThan() {
		NumericExpression n1 = idealFactory.subtract(x,intOne);
		NumericExpression n2 = idealFactory.add(x, intOne);
		
		BooleanExpression n = commonIdealFactory.notLessThan(n2, n1);
		BooleanExpression nn = commonIdealFactory.notLessThan(n1, n2);
		BooleanExpression m1 = booleanFactory.symbolic(false);
		BooleanExpression m2 = booleanFactory.symbolic(true);
		
		assertEquals(m2, n);
		assertEquals(m1, nn);
	}

	/**
	 * Returns true if the first argument is 'not less than or equal' to the second argument 
	 * and vice-versa.
	 * 
	 * @param type
	 * 				NumericExpression
	 * 
	 * @return type
	 * 				BooleanExpression
	 */
	@Test
	public void notLessThanEquals() {		
		NumericExpression n1 = idealFactory.subtract(x, intOne);
		NumericExpression n2 = idealFactory.add(x, intOne);
		BooleanExpression m = booleanFactory.symbolic(true);
		BooleanExpression n = booleanFactory.symbolic(false);
				
		BooleanExpression n01 = commonIdealFactory.notLessThanEquals(n1, n2);
		BooleanExpression n02 = commonIdealFactory.notLessThanEquals(n2, n1);
		
		assertEquals(n, n01);
		assertEquals(m, n02);
	}
	
	/**
	 * Returns true or false if the two symbolic Expressions are equal or not equal 
	 * respectively
	 * 
	 * @param type
	 * 				NumericExpression
	 * 
	 * @return type
	 * 				BooleanExpression
	 * 
	 */
	@Test
	public void equals() {
		NumericExpression n11 = idealFactory.add(x, y);
		NumericExpression n22 = idealFactory.subtract(x, y);
		NumericExpression n1 = idealFactory.add(y, idealFactory.intConstant(2));
		NumericExpression n2 = idealFactory.subtract(y,
				idealFactory.intConstant(2));
		NumericExpression n3 = idealFactory.add(y, idealFactory.intConstant(2));
		
		BooleanExpression n = commonIdealFactory.equals(n1, n2);
		BooleanExpression n0 = commonIdealFactory.equals(n1, n3);
		BooleanExpression n1122 = commonIdealFactory.equals(n11, n22);
		BooleanExpression m1 = booleanFactory.symbolic(false);
		BooleanExpression m2 = booleanFactory.symbolic(true);
		out.println("Equals=" + n0);
		out.println("Equals1=" + n);
		out.println("Equals2=" + n1122);				
		
		assertEquals(m1, n);
		assertEquals(m2, n0);
	}

	/**
	 * Integer modulus. Assume numerator is nonnegative and denominator is
	 * positive.
	 * 
	 * (ad)%(bd) = (a%b)d
	 * 
	 * Ex: (2u)%2 = (u%1)2 = 0
	 * 
	 * @param numerator
	 *            a nonnegative integer polynomial
	 *            
	 * @param denominator
	 *            a positive integer polynomial
	 *            
	 * @return the polynomial of the form numerator%denominator
	 */
	@Test
	public void intModulusPoly() {
		NumericExpression n1 = idealFactory.subtract(idealFactory.multiply(x, x),
				intOne);
		NumericExpression n2 = idealFactory.add(x, intOne);
		NumericExpression ne1 = idealFactory.modulo(n1, n2);
		
		NumericExpression n = commonIdealFactory.modulo(n1, n2);
		NumericExpression m = commonIdealFactory.modulo(intZero, n2);
		NumericExpression p = commonIdealFactory.modulo(n1, intOne);
		NumericExpression q = commonIdealFactory.modulo(constOnePointFive, constOne);
		out.println("modulo=" + q);
		
		assertEquals(ne1, n);
		assertEquals(intZero, m);
		assertEquals(intZero, p);
	}

	/**
	 * Returns true or false if the two symbolic Expressions are not equal or equal 
	 * respectively
	 * 
	 * @param type
	 * 				NumericExpression
	 * 
	 * @return type
	 * 				BooleanExpression
	 * 
	 */
	@Test
	public void neq() {
		NumericExpression n11 = idealFactory.add(x, y);
		NumericExpression n22 = idealFactory.subtract(x, y);
		NumericExpression n1 = idealFactory.add(y, intTwo);
		NumericExpression n2 = idealFactory.subtract(y,	intTwo);
		NumericExpression n3 = idealFactory.add(y, intTwo);
		
		BooleanExpression n = commonIdealFactory.neq(n1, n2);
		BooleanExpression n0 = commonIdealFactory.neq(n1, n3);
		BooleanExpression n1122 = commonIdealFactory.neq(n11, n22);
		BooleanExpression m1 = booleanFactory.symbolic(false);
		BooleanExpression m2 = booleanFactory.symbolic(true);
		out.println("neq=" + n0);
		out.println("neq1=" + n);
		out.println("neq2=" + n1122);		
		
		assertEquals(m1, n0);
		assertEquals(m2, n);
	}

	/**
	 * Returns a Zero Constant
	 * 
	 * @param type
	 * 				SymbolicType (Example: Real)
	 */
	@Test
	public void zero() {
		Constant c1 = commonIdealFactory.zero(real);
		
		assertEquals(constZero, c1);
	}

	/**
	 * Returns a zero monomial
	 * 
	 * @param type
	 * 				Constant, Monic
	 */
	@Test
	public void monomial() {
		Monic monic = (Monic) idealFactory.multiply(x, x);
		
		Monomial m = commonIdealFactory.monomial(constZero, monic);
		assertEquals(constZero, m);
	}
    
	/**
	 * Multiplies two rational numbers.
	 * 
	 * Also checks if the first or second argument is zero or one.
	 * 
	 * @param type
	 * 				SymbolicExpression of numeric type
	 */
	@Test
	public void rationalMultiply() {
		NumericExpression n1 = commonIdealFactory.multiply(three, five);
		NumericExpression n2 = commonIdealFactory.multiply(three, zero);
		NumericExpression n3 = commonIdealFactory.multiply(zero, five);
		NumericExpression n4 = commonIdealFactory.multiply(three, one);
		
		assertEquals(fifteen, n1);
		assertEquals(zero, n2);
		assertEquals(zero, n3);
		assertEquals(three, n4);
	}

	/**
	 * Performs type casting from one type to another type, which is passed as an argument
	 * 
	 * @param type
	 * 				NumericExpression along with the new type
	 */
	@Test
	public void cast() {
		NumericExpression n11 = idealFactory.subtract(idealFactory.multiply(
											intNegOne, x), intTen);
		NumericExpression m1 = idealFactory.add(five, three);
		NumericExpression n1 = idealFactory.multiply(five, three);
		NumericExpression o1 = idealFactory.minus(five);
		NumericExpression p1 = idealFactory.power(five, three);
		NumericExpression q1 = idealFactory.subtract(five, three);
				
		
		NumericExpression n22 = commonIdealFactory.cast(n11, real);
		NumericExpression m = commonIdealFactory.cast(e2, real); // ADD
		NumericExpression n = commonIdealFactory.cast(e4, real); // MULTIPLY
		NumericExpression o = commonIdealFactory.cast(e5, real); // MINUS
		NumericExpression p = commonIdealFactory.cast(e6, real); // POWER
		NumericExpression p0 = commonIdealFactory.cast(e10, real); // POWER
		NumericExpression q = commonIdealFactory.cast(e7, real); // SUBTRACT
		NumericExpression r = commonIdealFactory.cast(e3, real);
		NumericExpression s = commonIdealFactory.cast(e01, integer);
		out.println("n22=" + n22);
		out.println("ADD=" + m);
		out.println("MULTIPLY=" + n);
		out.println("MINUS=" + o);
		out.println("POWER=" + p);
		out.println("POWER0=" +p0);
		out.println("SUBTRACT=" + q);
		out.println("COND=" +r);
		out.println("CAST=" +s.type());
		
		assertEquals(m1, m);
		assertEquals(n1, n);
		assertEquals(o1, o);
		assertEquals(p1, p);
		assertEquals(oneTwoFive, p0);
		assertEquals(q1, q);
	}

	/**
	 * Displays the number of type NumberObject
	 * 
	 * @param type
	 * 				RationalNumber
	 */
	@Test
	public void number(){
		NumberObject n = objectFactory.numberObject(ratThree);
		
		NumericExpression ne = commonIdealFactory.number(n);
		out.println("Number=" +ne);
		
		assertEquals(three, ne);
	}
	
	/**
	 * Displays the expression consisting of addition of three arguments
	 * 
	 * @return type
	 * 				NumericPrimitive
	 */
	@Test
	public void expression() {
		NumericPrimitive n1 = commonIdealFactory.expression(
				SymbolicOperator.ADD, integer, five, three, one);
		
		out.println("Exp=" + n1);
		out.println("Expr2=" +e9);
		
		assertEquals(e9, n1);
	}
	
	/**
	 * Returns a zero polynomial from the given type and term map
	 * 
	 * @param type
	 * 				the numeric type of polynomial
	 * 
	 *  @param termMap
	 *            the terms of the polynomial expressed as a map; all of the
	 *            terms must have the specified type
	 */
	@Test
	public void polynomial() {
		Monomial monomial = idealFactory.monomial(intTen, (Monic) x);
		
		SymbolicMap<Monic, Monomial> termMap = commonIdealFactory.emptyMap();
		Polynomial b = commonIdealFactory.polynomial(termMap, monomial);
		out.println("Zero Polynomial=" + b);
		
		assertEquals(intZero, b);
		}

	/**
	 * Returns the subtraction of symbolic expression of same numeric type
	 * 
	 * @param type
	 * 				Symbolic Expression of Numeric type
	 */
	@Test
	public void primitiveSubtract() {
		NumericExpression subNine = commonIdealFactory.subtract(intTen, intOne);
		Constant nine = commonIdealFactory.intConstant(9);
		
		assertEquals(subNine, nine);
	}

	/**
	 * Returns the subtraction of symbolic expression of same numeric type
	 * 
	 * @param type
	 * 				Symbolic Expressions of same Numeric type
	 */
	@Test
	public void primitiveNegSubtract() {
		NumericExpression subEleven = commonIdealFactory.subtract(intTen, intNegOne);
		Constant eleven = commonIdealFactory.intConstant(11);
		
		assertEquals(subEleven, eleven);
	}

	/**
	 * Returns a rational expression by canceling out the common factors that are present in both numerator and denominator.
	 * 
	 * @param type
	 * 				Symbolic Expressions of same numeric type
	 * 
	 * the test below does this:
	 * 		(21x^3 - 35x^2) / (7x) -------> 3x^2 - 5x
	 */
	@Test
	public void complexRational() {
		NumericSymbolicConstant x = objectFactory.canonic(idealFactory.symbolicConstant(Xobj,
				typeFactory.realType()));
		IntObject exp3 = objectFactory.intObject(3);
		IntObject exp2 = objectFactory.intObject(2);
		
		
		NumericExpression complex1 = commonIdealFactory.multiply(twentyOne, idealFactory.
				power(x, exp3));
		NumericExpression complex2 = commonIdealFactory.multiply(thirtyFive, idealFactory.
				power(x, exp2));
		NumericExpression numer = commonIdealFactory.subtract(complex1, complex2);		
		NumericExpression denom = commonIdealFactory.multiply(x, seven);
		NumericExpression complex = commonIdealFactory.divide(numer, denom);
		NumericExpression result1 = commonIdealFactory.multiply(idealFactory.power(x, 
				exp2), three);
		NumericExpression result2 = commonIdealFactory.multiply(x, five);
		NumericExpression result = commonIdealFactory.subtract(result1, result2);
		
		assertEquals(result, complex);
	}
	
	/**
	 * Compares two Rational Expressions
	 * 
	 * @param type
	 * 				Symbolic Expressions of same numeric type
	 */
@Ignore
	@Test
	public void comparingRationalExpressions() {
		NumericSymbolicConstant x2 = objectFactory.canonic(idealFactory
				.symbolicConstant(Xobj, typeFactory.realType()));
		NumericSymbolicConstant y2 = objectFactory.canonic(idealFactory
				.symbolicConstant(objectFactory.stringObject("Y"),
						typeFactory.realType()));
		
		
		RationalExpression r1 = (RationalExpression) commonIdealFactory.divide(x2, y2);
		BooleanExpression b1 = booleanFactory.booleanExpression(
				SymbolicOperator.LESS_THAN_EQUALS, r1,
				commonIdealFactory.zeroReal());
		BooleanExpression b2 = booleanFactory.booleanExpression(
				SymbolicOperator.LESS_THAN, commonIdealFactory.zeroReal(), r1);
		BooleanExpression nb2 = idealFactory.notLessThan(commonIdealFactory.
				zeroReal(), r1);
		NumericExpression nb5 = commonIdealFactory.divide(
				commonIdealFactory.zeroReal(), r1);
		out.println("b1 = " + b1);
		out.println("b2 = " + b2);
		out.println("!b2 = " + nb2);

		assertEquals(zero, nb5);
	}
}
