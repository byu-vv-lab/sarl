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
package edu.udel.cis.vsl.sarl.ideal; 

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;
import edu.udel.cis.vsl.sarl.ideal.IF.RationalExpression;
import edu.udel.cis.vsl.sarl.ideal.common.CommonIdealFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class IdealMultiplyTest {

	private static PrintStream out = System.out;
	private NumberFactory numberFactory;
	private ObjectFactory objectFactory;
	private SymbolicTypeFactory typeFactory;
	private CollectionFactory collectionFactory;
	private IdealFactory idealFactory;
	private BooleanExpressionFactory booleanFactory;
	private CommonIdealFactory commonIdealFactory;

	private RationalNumber ratNegPointTwoFive; // -1/4
	private RationalNumber ratOnePointFive; // 3/2
	private Constant constOnePointFive; // real constant 3/2
	private Constant constNegPointTwoFive; // real constant -1/4
	private Constant intZero; // int constant 0
	private Constant intOne; // int constant 1
	private Constant intTwo; // int constant 2
	private Constant intTen; // int constant 10
	StringObject Xobj; // "X"
	NumericSymbolicConstant x; // int symbolic constant "X"
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
		ratOnePointFive = numberFactory.rational("1.5");
		ratNegPointTwoFive = numberFactory.rational("-.25");
		intZero = idealFactory.intConstant(0);
		constOnePointFive = idealFactory.constant(ratOnePointFive);
		constNegPointTwoFive = idealFactory.constant(ratNegPointTwoFive);
		intOne = idealFactory.intConstant(1);
		intTwo = idealFactory.intConstant(2);
		intTen = idealFactory.intConstant(10);
		Xobj = objectFactory.stringObject("X");
		x = objectFactory.canonic(idealFactory.symbolicConstant(Xobj,
				typeFactory.integerType()));
		y = objectFactory.canonic(idealFactory.symbolicConstant(
				objectFactory.stringObject("Y"), typeFactory.integerType()));
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
	}

	@After
	public void tearDown() throws Exception {
		
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
	
	@Test
	public void mulPolyToPoly() {
		NumericExpression p1 = idealFactory.add(idealFactory.multiply(x, x), intOne);
		NumericExpression p2 = idealFactory.add(idealFactory.multiply(intTwo,
						idealFactory.multiply(x, x)), intOne);
		NumericExpression p3 = idealFactory.multiply(intZero, x);
		NumericExpression x2 = idealFactory.multiply(x, x);
		NumericExpression x4 = idealFactory.multiply(x2, x2);
		NumericExpression p4 = idealFactory.add(idealFactory.multiply(idealFactory.
				intConstant(3), idealFactory.multiply(x, x)), intOne);
		NumericExpression p5 = idealFactory.add(idealFactory.
				multiply(intTwo, x4), p4);
		Polynomial poly1 = (Polynomial) p1;
		Polynomial poly2 = (Polynomial) p2;
		Polynomial poly3 = (Polynomial) p3;
		
		Polynomial b1 = commonIdealFactory.multiply(poly1, poly2);
		Polynomial b2 = commonIdealFactory.multiply(poly1, poly3);
		assertEquals(p5, b1);
		assertEquals(intZero, b2);
	}
	
	@Test
	public void mulPolyToMonomial() {
		NumericExpression p1 = idealFactory.add(idealFactory.multiply(x, x), intOne);
		NumericExpression p6 = idealFactory.multiply(intTen, x);
		NumericExpression x2 = idealFactory.multiply(x, x);
		NumericExpression p7 = idealFactory.add(idealFactory.multiply(intTen, 
				idealFactory.multiply(x2, x)), p6);
		Polynomial poly1 = (Polynomial) p1;
		Polynomial poly4 = (Polynomial) p6;
		
		Polynomial b3 = commonIdealFactory.multiply(poly4, poly1);
		assertEquals(p7, b3);
	}
	
	@Test
	public void mulMonomialToPrimitivePower() {
		NumericExpression p6 = idealFactory.multiply(intTen, x);
		NumericExpression x2 = idealFactory.multiply(x, x);
		NumericExpression p8 = idealFactory.multiply(intTen, idealFactory.
				multiply(x2, x));
		Polynomial poly4 = (Polynomial) p6;
		Polynomial poly5 = (Polynomial) x2;
		
		Polynomial b4 = commonIdealFactory.multiply(poly4, poly5);
		
		assertEquals(p8, b4);
	}
	
	@Test
	public void mulPrimitivePowerToItself() {
		NumericExpression x2 = idealFactory.multiply(x, x);
		NumericExpression x4 = idealFactory.multiply(x2, x2);
		Polynomial poly5 = (Polynomial) x2;
		
		Polynomial b5 = commonIdealFactory.multiply(poly5, poly5);
		
		assertEquals(x4, b5);
	}
	
	@Test
	public void mulPrimitivePowerToPrimitive() {
		NumericExpression x2 = idealFactory.multiply(x, x);
		NumericExpression p9 = idealFactory.multiply(x, x2);
		Polynomial poly5 = (Polynomial) x2;
		Polynomial poly6 = (Polynomial) x;
				
		Polynomial b6 = commonIdealFactory.multiply(poly5, poly6);
		
		assertEquals(p9, b6);
	}
	
	@Test
	public void mulConstantToPrimitive() {
		Polynomial poly6 = (Polynomial) x;
		NumericExpression p10 = idealFactory.multiply(intTen, x);
		
		Polynomial b7 = commonIdealFactory.multiply(poly6, intTen);
		
		assertEquals(p10, b7);
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
	 * Multiply various levels of numbers (primitive, monic, poly, etc.) with a rational number
	 * 
	 * @return type
	 * 				RationalExpression
	 */
	@Test
	public void mulToRational() {
		NumericSymbolicConstant x = objectFactory.canonic(idealFactory
				.symbolicConstant(objectFactory.stringObject("x"),
						typeFactory.realType()));
		NumericSymbolicConstant y = objectFactory.canonic(idealFactory
				.symbolicConstant(objectFactory.stringObject("Y"),
						typeFactory.realType()));	
		
		RationalExpression r1 = (RationalExpression) idealFactory.divide(x, y);	// x/y	
		NumericExpression x2 = idealFactory.multiply(x, x); //x^2
		NumericExpression monic = idealFactory.multiply(x2, y); //x^2 * y
		NumericExpression monomial = idealFactory.multiply(idealFactory.constant(realThree), 
				monic); //3x^2 * y
		NumericExpression polynomial = idealFactory.add(idealFactory.
				divide(monomial, idealFactory.constant(realThree)), x2); //x^2 * y + x^2
		RationalExpression mulPrimitive = (RationalExpression) 
				idealFactory.multiply(r1, x); //(x*x)/y 
		RationalExpression mulPrimitivePower = (RationalExpression) 
				idealFactory.multiply(r1, x2); //(x*x^2)/y 
		RationalExpression mulMonic = (RationalExpression) 
				idealFactory.multiply(r1, monic); //(x^3) 
		RationalExpression mulMonomial = (RationalExpression) 
				idealFactory.multiply(r1, idealFactory.divide(monomial, 
						idealFactory.constant(realThree))); //(x^3) 
		RationalExpression mulPolynomial = (RationalExpression) 
				idealFactory.multiply(r1, polynomial); //x^3 + (x^3/y)
		
		NumericExpression result1 = idealFactory.divide(idealFactory.
				multiply(x, x), y); //(x*x)/y 
		NumericExpression result2 = idealFactory.divide(idealFactory.
				multiply(x2, x), y); //(x^2*x)/y 
		NumericExpression result3 = idealFactory.multiply(x2, x); //(x^3) 
		NumericExpression result4 = idealFactory.add(idealFactory.
				multiply(x2, x), idealFactory.divide(idealFactory.
						multiply(x2, x), y)); //3*x^3 + (x^3/y) 
		
		assertEquals(result1, mulPrimitive);	
		assertEquals(result2, mulPrimitivePower);	
		assertEquals(result3, mulMonic);	
		assertEquals(result3, mulMonomial);
		assertEquals(result4, mulPolynomial);
	}
}