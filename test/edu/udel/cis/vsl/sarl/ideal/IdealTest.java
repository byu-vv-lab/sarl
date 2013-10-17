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
//import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
//import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Monic;
import edu.udel.cis.vsl.sarl.ideal.IF.Monomial;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;
import edu.udel.cis.vsl.sarl.ideal.IF.RationalExpression;
import edu.udel.cis.vsl.sarl.ideal.common.CommonIdealFactory;
//import edu.udel.cis.vsl.sarl.ideal.common.IdealComparator;
//import edu.udel.cis.vsl.sarl.number.real.RealNumber;
import edu.udel.cis.vsl.sarl.number.real.RealNumberFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class IdealTest {

	private static PrintStream out = System.out;
	private NumberFactory numberFactory;
	private ObjectFactory objectFactory;
	//private CommonObjectFactory commonObjectFactory;
	private SymbolicTypeFactory typeFactory;
	private CollectionFactory collectionFactory;
	//private NumericExpressionFactory numericExpressionFactory;
	private IdealFactory idealFactory;
	private BooleanExpressionFactory booleanFactory;
	//private RealNumberFactory realNumberFactory;
	private CommonIdealFactory commonIdealFactory;

	private RationalNumber ratThree; // 3
	private RationalNumber ratFour; // 4
	private RationalNumber ratZero;
	private RationalNumber ratOnePointFive; // 3/2
	private RationalNumber ratNegPointTwoFive; // -1/4
	private RationalNumber ratOnePointTwoFive; // 5/4
	private Constant constZero;
	private Constant intZero;
	private Constant constOnePointFive; // real constant 3/2
	private Constant constNegPointTwoFive; // real constant -1/4
	//private Constant constThree;
	//private Constant constFour;
	private Constant intNegOne; // int constant -1
	private Constant intOne; // int constant 1
	private Constant intTen; // int constant 10
	private SymbolicIntegerType integerOne; // Integer 1
	private SymbolicIntegerType integerTen; // Integer 10
	StringObject Xobj; // "X"
	// StringObject mXobj;
	NumericSymbolicConstant x; // int symbolic constant "X"
	// NumericSymbolicConstant mx;
	NumericSymbolicConstant y; // int symbolic constant "Y"
	private NumericExpression fifteen;
	private NumericExpression five; 
	private NumericExpression zero;
	private NumericExpression one;
	private RationalNumber realZero;
	private RationalNumber realOne;
	private RationalNumber realFifteen;
	private RationalNumber realFive; 
	private NumericExpression three; 
	private RationalNumber realThree; 
	private NumericExpression intThree;
	private SymbolicType real;
	private SymbolicType integer;
	NumericExpression intHundred;
	NumericExpression intTwenty;
	NumericExpression e1; // 5 IsReal
	NumericExpression e2; // 5 + 3 ADD
	NumericExpression e3; // 5 > 3, 5, 3 COND
	NumericExpression e4; // 5 * 3 MULTIPLY
	NumericExpression e5; // -5 NEGATIVE
	NumericExpression e6; // 5 ^ 3 POWER
	NumericExpression e7; // 5 - 3 SUBTRACT
	NumericExpression e8; // DEFAULT
	NumericExpression e9; // 5 + 3 + 1 ADD

	@Before
	public void setUp() throws Exception {
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		numberFactory = system.numberFactory();
		objectFactory = system.objectFactory();
		typeFactory = system.typeFactory();
		collectionFactory = system.collectionFactory();
		//numericExpressionFactory = system.numericFactory();
		idealFactory = (IdealFactory) system.numericFactory();
		booleanFactory = system.booleanFactory();
		//realNumberFactory = (RealNumberFactory) system.numberFactory();
		commonIdealFactory = new CommonIdealFactory(numberFactory,
				objectFactory, typeFactory, collectionFactory, booleanFactory);
		ratZero = numberFactory.rational("0");
		ratOnePointFive = numberFactory.rational("1.5");
		ratNegPointTwoFive = numberFactory.rational("-.25");
		ratOnePointTwoFive = numberFactory.rational("1.25");
		ratThree = numberFactory.rational("3");
		ratFour = numberFactory.rational("4");
		constZero = idealFactory.constant(ratZero);
		intZero = idealFactory.intConstant(0);
		constOnePointFive = idealFactory.constant(ratOnePointFive);
		constNegPointTwoFive = idealFactory.constant(ratNegPointTwoFive);
		//constThree = idealFactory.constant(ratThree);
		//constFour = idealFactory.constant(ratFour);
		intNegOne = idealFactory.intConstant(-1);
		intOne = idealFactory.intConstant(1);
		intTen = idealFactory.intConstant(10);
		integerTen = typeFactory.integerType();
		integerOne = typeFactory.integerType();
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
		zero = commonIdealFactory.constant(realZero);
		one = commonIdealFactory.constant(realOne);
		fifteen = commonIdealFactory.constant(realFifteen);
		five = commonIdealFactory.constant(realFive);
		realThree = numberFactory.rational("3");
		three = commonIdealFactory.constant(realThree);
		intThree = commonIdealFactory.expression(SymbolicOperator.CAST,
				integer, three);
		intHundred = idealFactory.intConstant(100);
		intTwenty = idealFactory.intConstant(20);
		e1 = commonIdealFactory.constant(realFive);
		e2 = commonIdealFactory.expression(SymbolicOperator.ADD, integer, five,
				three); // 5 + 3 ADD
		e3 = commonIdealFactory.expression(SymbolicOperator.COND,
				typeFactory.booleanType(), booleanFactory.trueExpr(),
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

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void constantCreation() {
		out.println("constantCreation: " + intTen);
		assertEquals(10, ((IntegerNumber) intTen.number()).intValue());
	}

	@Test
	public void constantAdd() {
		Constant c3 = (Constant) idealFactory.add(constOnePointFive, constNegPointTwoFive);

		out.println("constantAdd: " + constOnePointFive + " + " + constNegPointTwoFive + " = " + c3);
		assertEquals(ratOnePointTwoFive, c3.number());
	}

	@Test
	public void constantMultiply() {
		Constant result = (Constant) idealFactory.multiply(constOnePointFive, constNegPointTwoFive);
		RationalNumber expected = numberFactory.rational("-.375");

		out.println("constantMultiply: " + constOnePointFive + " * " + constNegPointTwoFive + " = " + result);
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
				objectFactory.stringObject("X"), real);
		
		assertFalse(x.equals(x2));
	}

	/**
	 * Shows that the commutative property holds for two SymbolicExpressions
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
	public void realone() {
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
	public void xplus1squared() {
		NumericExpression xp1 = idealFactory.add(x, intOne);
		SymbolicExpression xp1squared = idealFactory.multiply(xp1, xp1);
		SymbolicExpression x2p2xp1 = idealFactory.add(idealFactory.multiply(x,
				x), idealFactory.add(idealFactory.multiply(idealFactory.intConstant(2), x),
				intOne));

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

	/*
	 * @Test public void rationalMinus() { RationalExpression r =
	 * (RationalExpression) idealFactory.divide(x, y); NumericExpression minusr
	 * =
	 * idealFactory.multiply(y,idealFactory.subtract(idealFactory.intConstant(1
	 * ), r)); Monomial negativex = idealFactory.monomial(c4, (Monic) x);
	 * RationalExpression negativer = (RationalExpression)
	 * idealFactory.divide(negativex, y); out.println("minus r: " + minusr
	 * +"vs. " + negativer); assertEquals(negativer, minusr); }
	 */

	/**
	 * Returns true if the first argument is 'less than' the second argument and vice-versa.
	 * 
	 * @param type
	 * 				NumericExpression
	 * 
	 * @return type
	 * 				BooleanExpression
	 */
	@Test
	public void lessThan() {
		NumericExpression n1 = idealFactory.subtract(x, intOne);
		NumericExpression n2 = idealFactory.add(x, intOne);		
		NumericExpression n3 = idealFactory.add(x, intOne);		
		NumericExpression n11 = idealFactory.add(x, y);
		NumericExpression n22 = idealFactory.subtract(x, y);		
		// out.println("less than=" +n);
		// out.println("less than1=" +n0);
		// out.println("less than2=" +n1122);
		BooleanExpression m1 = booleanFactory.symbolic(true);
		BooleanExpression m2 = booleanFactory.symbolic(false);
		
		BooleanExpression n = commonIdealFactory.lessThan(n1, n2);
		BooleanExpression n0 = commonIdealFactory.lessThan(n2, n3);
		BooleanExpression n1122 = commonIdealFactory.lessThan(n11, n22);
		assertEquals(m1, n);
		assertEquals(m2, n0);
	}

	/**
	 * Adds two polynomials
	 * 
	 * @param type
	 * 				Polynomial
	 */
	@Test
	public void addPoly() {
		NumericExpression p1 = idealFactory.add(idealFactory.multiply(x, x),
				intOne);
		NumericExpression p2 = idealFactory.add(
				idealFactory.multiply(idealFactory.intConstant(2),
						idealFactory.multiply(x, x)),
				intOne);
		NumericExpression p3 = idealFactory.multiply(intZero, x);
		NumericExpression p4 = idealFactory.add(
				idealFactory.multiply(idealFactory.intConstant(3), idealFactory.
						multiply(x, x)), idealFactory.intConstant(2));
		// NumericExpression p5 = idealFactory.add(idealFactory.multiply(three,
		// x), idealFactory.intConstant(5));
		// NumericExpression p6 = idealFactory.add(idealFactory.multiply(five,
		// x), idealFactory.intConstant(3));
		Polynomial poly1 = (Polynomial) p1;
		Polynomial poly2 = (Polynomial) p2;
		Polynomial poly3 = (Polynomial) p3;
		// Polynomial poly4 = (Polynomial) p4;
		// Polynomial poly5 = (Polynomial) p5;
		
		
		Polynomial b1 = commonIdealFactory.add(poly1, poly2);
		Polynomial b2 = commonIdealFactory.add(poly3, poly2);
		// Polynomial b3 = commonIdealFactory.add(poly4, poly5);		
		// Polynomial c = (Polynomial) idealFactory.intConstant(0);
		//out.println("ADD_Polynomial=" + b1);
		assertEquals(p4, b1);
		assertEquals(p2, b2);
	}

	/**
	 * Returns the multiplied result of two polynomials
	 * 
	 * @param type
	 * 				Polynomial
	 */
	@Test
	public void multPoly() {
		NumericExpression p1 = idealFactory.add(idealFactory.multiply(x, x),
				intOne);
		NumericExpression p2 = idealFactory.add(
				idealFactory.multiply(idealFactory.intConstant(2),
						idealFactory.multiply(x, x)), intOne);
		NumericExpression p3 = idealFactory.multiply(intZero, x);
		Polynomial poly1 = (Polynomial) p1;
		Polynomial poly2 = (Polynomial) p2;
		Polynomial poly3 = (Polynomial) p3;
		NumericExpression x2 = idealFactory.multiply(x, x);
		NumericExpression x4 = idealFactory.multiply(x2, x2);
		NumericExpression p4 = idealFactory.add(idealFactory.multiply(idealFactory.
				intConstant(3), idealFactory.multiply(x, x)), intOne);
		NumericExpression p5 = idealFactory.add(
				idealFactory.multiply(idealFactory.intConstant(2), x4), p4);
		
		
		Polynomial b1 = commonIdealFactory.multiply(poly1, poly2);
		Polynomial b2 = commonIdealFactory.multiply(poly1, poly3);		
		out.println("Multiply_Polynomial=" + b1);
		assertEquals(p5, b1);
		assertEquals(intZero, b2);
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
		//out.println("Constant Term Subtraction=" + b1);
		assertEquals(x, b1);
		assertEquals(x, b2);
		assertEquals(p, b3);
		assertEquals(constZero, b4);
	}

	/**
	 * Returns the negative version of the given argument
	 * 
	 * @param type
	 * 				SymbolicExpression of integer or real type
	 * 
	 */
	@Test
	public void minus() {
		NumericExpression p1 = idealFactory.add(idealFactory.multiply(x, x),intOne);
		NumericExpression p2 = intZero;
		NumericExpression m1 = commonIdealFactory.minus(p2);
		NumericExpression n1 = commonIdealFactory.minus(p1);
		NumericExpression n = idealFactory.minus(p1);
		NumericExpression m = idealFactory.minus(p2);
		
		// out.println("n=" +n);
		// out.println("ne=" +ne);
		assertEquals(n, n1);
		assertEquals(m, m1);
	}

	/**
	 * Returns true if the first argument is 'not less than' the second argument and vice-versa.
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
		
		// out.println("Not Less Than=" +n);
		assertEquals(m2, n);
		assertEquals(m1, nn);
	}

	/**
	 * Returns true if the first argument is 'not less than or equal' to the second argument and vice-versa.
	 * 
	 * @param type
	 * 				NumericExpression
	 * 
	 * @return type
	 * 				BooleanExpression
	 */
	@Test
	public void notLessThanEquals() {
		/*
		NumericExpression n1 = idealFactory.subtract(x, intOne);
		NumericExpression n2 = idealFactory.add(x, intOne);
		BooleanExpression n = commonIdealFactory.notLessThanEquals(n1, n2);
		// out.println("Not Less Than Equals=" +n);
		BooleanExpression m = booleanFactory.symbolic(false);
		assertEquals(m, n);
		*/
		NumericExpression n11 = idealFactory.add(x, y);
		NumericExpression n22 = idealFactory.subtract(x, y);
		NumericExpression n1 = idealFactory.subtract(x,intOne);
		NumericExpression n2 = idealFactory.add(x, intOne);
		NumericExpression n3 = idealFactory.add(x, intOne);
		BooleanExpression n1122 = commonIdealFactory.notLessThanEquals(n11, n22);
		BooleanExpression n = commonIdealFactory.notLessThanEquals(n1, n2);
		BooleanExpression n0 = commonIdealFactory.notLessThanEquals(n2, n3);		
		BooleanExpression m1 = booleanFactory.symbolic(false);
		BooleanExpression m2 = booleanFactory.symbolic(true);
		
		//out.println("Not Less Than=" +n1122);
		// out.println("Not Less Than=" +n);
		//assertEquals(m1, n);
		//assertEquals(m2, n0);
	}
	
	/**
	 * Returns true or false if the two symbolic Expressions are equal or not equal respectively
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
	 *            an integer polynomial assumed to be nonnegative
	 *            
	 * @param denominator
	 *            an integer polynomial assumed to be positive
	 *            
	 * @return the polynomial representing numerator%denominator
	 */
	@Test
	public void intModulusPoly() {
		NumericExpression n1 = idealFactory.add(idealFactory.multiply(x, x),
				intOne);
		NumericExpression n2 = idealFactory.add(x, intOne);
		NumericExpression n3 = intZero;
		NumericExpression n4 = intOne;
		NumericExpression n = commonIdealFactory.modulo(n1, n2);
		NumericExpression m = commonIdealFactory.modulo(n3, n2);
		NumericExpression p = commonIdealFactory.modulo(n1, n4);		
		NumericExpression ne1 = idealFactory.modulo(n1, n2);
		NumericExpression ne2 = idealFactory.modulo(n3, n2);
		NumericExpression ne3 = idealFactory.modulo(n1, n4);
		
		out.println("modulo=" + n);
		assertEquals(ne1, n);
		assertEquals(ne2, m);
		assertEquals(ne3, p);
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
		NumericExpression n4 = idealFactory.multiply(
				intOne, intOne);
		NumericExpression n5 = idealFactory.multiply(fifteen, three);
		NumericExpression n6 = idealFactory.multiply(five, three);
		NumericExpression n7 = idealFactory.multiply(zero, three);
		//NumericExpression n8 = idealFactory.multiply(IntegerOne, x);
		//NumericExpression n9 = idealFactory.multiply(IntegerTen, x);
		NumericExpression n = idealFactory.add(x, y);
		NumericExpression m = idealFactory.subtract(x, y);
		NumericExpression b1 = commonIdealFactory.divide(n1, n2);
		NumericExpression b2 = commonIdealFactory.divide(n3, n2);
		NumericExpression b3 = commonIdealFactory.divide(n1, n4);
		NumericExpression b4 = commonIdealFactory.divide(n5, n6);
		NumericExpression b5 = commonIdealFactory.divide(n7, n6);
		NumericExpression p1 = commonIdealFactory.divide(n, m);
		NumericExpression np = idealFactory.divide(n, m);
		
		out.println("b1=" + b1);
		assertEquals(np, p1);
		assertEquals(intTen, b1);
		assertEquals(intZero, b2);
		assertEquals(n1, b3);
		assertEquals(three, b4);
		assertEquals(zero, b5);
	}

	/**
	 * Returns true or false if the two symbolic Expressions are not equal or equal respectively
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
		NumericExpression n1 = idealFactory.add(y, idealFactory.intConstant(2));
		NumericExpression n2 = idealFactory.subtract(y,
				idealFactory.intConstant(2));
		NumericExpression n3 = idealFactory.add(y, idealFactory.intConstant(2));
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
		// out.println("zero=" +c);
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
		// out.println("monomial=" +m);
		
		assertEquals(constZero, m);
	}
    
	/**
	 * Multiplies two rational numbers. These numbers can also be Real type.
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

	/*
	 * @Test public void polynomialDividedByConstant(){ 
	 * NumericExpression n =
	 * idealFactory.multiply(idealFactory.intConstant(3),
	 * idealFactory.multiply(x,x)); Polynomial poly = (Polynomial) n; Polynomial
	 * p = commonIdealFactory.divide(poly, c3);
	 * out.println("poly divided by const.=" +p); }
	 */

	@Test
	public void cast() {
		NumericExpression m = commonIdealFactory.cast(e2, real); // ADD
		NumericExpression n = commonIdealFactory.cast(e4, real); // MULTIPLY
		NumericExpression o = commonIdealFactory.cast(e5, real); // MINUS
		NumericExpression p = commonIdealFactory.cast(e6, real); // POWER
		NumericExpression q = commonIdealFactory.cast(e7, real); // SUBTRACT
		NumericExpression r = commonIdealFactory.cast(e3, real);
		SymbolicOperator s = r.operator();		
		NumericExpression n11 = idealFactory.subtract(
				idealFactory.multiply(intNegOne, x), intTen);
		NumericExpression n22 = commonIdealFactory.cast(n11, real);
		NumericExpression m1 = idealFactory.add(five, three);
		NumericExpression n1 = idealFactory.multiply(five, three);
		NumericExpression o1 = idealFactory.minus(five);
		NumericExpression p1 = idealFactory.power(five, three);
		NumericExpression q1 = idealFactory.subtract(five, three);
		
		out.println("operator=" + s);
		out.println("n22=" + n22);
		out.println("ADD=" + m);
		out.println("MULTIPLY=" + n);
		out.println("MINUS=" + o);
		out.println("POWER=" + p);
		out.println("SUBTRACT=" + q);		
		assertEquals(m1, m);
		assertEquals(n1, n);
		assertEquals(o1, o);
		assertEquals(p1, p);
		assertEquals(q1, q);
	}

	/*
	 * @Test public void number(){ NumericExpression n =
	 * idealFactory.multiply(idealFactory.intConstant(5), x); Number number =
	 * commonIdealFactory.extractNumber(n); NumberObject s =
	 * commonObjectFactory.numberObject(number); NumericExpression s1 =
	 * commonIdealFactory.number(s); out.println("s1=" +s1); }
	 */
	@Test
	public void expression() {
		NumericExpression n1 = commonIdealFactory.expression(
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
		SymbolicMap<Monic, Monomial> termMap = commonIdealFactory.emptyMap();
		Monomial monomial = idealFactory.monomial(intTen, (Monic) x);
		Polynomial b = commonIdealFactory.polynomial(termMap, monomial);
		
		out.println("Zero Polynomial=" + b);
		assertEquals(intZero, b);
		}

	@Test
	public void primitiveSubtract() {
		NumericExpression subNine = commonIdealFactory.subtract(intTen, intOne);
		Constant nine = commonIdealFactory.intConstant(9);
		
		assertEquals(subNine, nine);

	}

	@Test
	public void primitiveNegSubtract() {
		NumericExpression subEleven = commonIdealFactory.subtract(intTen, intNegOne);
		Constant eleven = commonIdealFactory.intConstant(11);
		
		assertEquals(subEleven, eleven);
	}
	/*
	@Test
	public void nonneg(){
		SymbolicMap<Monic, Monomial> termMap = commonIdealFactory.emptyMap();
		Monomial factorization = idealFactory.monomial(constFour, (Monic) x);
		Polynomial numeratorIsFive = commonIdealFactory.polynomial(termMap, factorization);
		SymbolicMap<Monic, Monomial> map01 = numeratorIsFive.termMap(commonIdealFactory);		
		Polynomial poly = commonIdealFactory.polynomial(map01, factorization);
		BooleanExpression bool = commonIdealFactory.isNonnegative(poly);
		out.println("bool="  +poly);
		assertEquals(bool, commonIdealFactory.booleanFactory().trueExpr());
	}
	*/
	@Test
	//(21x^3 - 35x^2) / (7x) -------> 3x^2 - 5x
	public void complexRational() {
		NumericSymbolicConstant x = objectFactory.canonic(idealFactory.symbolicConstant(Xobj,
				typeFactory.realType()));
		IntObject exp3 = objectFactory.intObject(3);
		IntObject exp2 = objectFactory.intObject(2);
		NumericExpression three = commonIdealFactory.constant(
				numberFactory.integerToRational(numberFactory.integer(3)));
		NumericExpression five = commonIdealFactory.constant(
				numberFactory.integerToRational(numberFactory.integer(5)));
		NumericExpression seven = commonIdealFactory.constant(
				numberFactory.integerToRational(numberFactory.integer(7)));
		NumericExpression twentyOne = commonIdealFactory.constant(
				numberFactory.integerToRational(numberFactory.integer(21)));
		NumericExpression thirtyFive = commonIdealFactory.constant(
				numberFactory.integerToRational(numberFactory.integer(35)));
		NumericExpression complex1 = idealFactory.power(x, exp3);
		complex1 = idealFactory.multiply(complex1, twentyOne);
		NumericExpression complex2 = idealFactory.power(x, exp2);
		complex2 = idealFactory.multiply(complex2, thirtyFive);
		NumericExpression numer = idealFactory.subtract(complex1, complex2);		
		NumericExpression denom = idealFactory.multiply(x, seven);
		NumericExpression complex = idealFactory.divide(numer, denom);
		NumericExpression result1 = idealFactory.multiply(idealFactory.power(x, exp2), three);
		NumericExpression result2 = idealFactory.multiply(x, five);
		NumericExpression result = idealFactory.subtract(result1, result2);
		
		assertEquals(result, complex);
	}

	@Test
	public void comparingRationalExpressions() {
		NumericSymbolicConstant x2 = objectFactory.canonic(idealFactory
				.symbolicConstant(Xobj, typeFactory.realType()));
		NumericSymbolicConstant y2 = objectFactory.canonic(idealFactory
				.symbolicConstant(objectFactory.stringObject("Y"),
						typeFactory.realType()));
		RationalExpression r1 = (RationalExpression) commonIdealFactory.divide(
				x2, y2);
		BooleanExpression b1 = booleanFactory.booleanExpression(
				SymbolicOperator.LESS_THAN_EQUALS, r1,
				commonIdealFactory.zeroReal());
		BooleanExpression b2 = booleanFactory.booleanExpression(
				SymbolicOperator.LESS_THAN, commonIdealFactory.zeroReal(), r1);
		// BooleanExpression nb2 = booleanFactory.not(b2);
		BooleanExpression nb2 = idealFactory.notLessThan(
				commonIdealFactory.zeroReal(), r1);

		out.println("b1 = " + b1);
		out.println("b2 = " + b2);
		out.println("!b2 = " + nb2);
		//assertEquals(b1, nb2);
	}
}
