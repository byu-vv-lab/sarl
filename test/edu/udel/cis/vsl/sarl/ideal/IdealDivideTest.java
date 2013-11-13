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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
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

public class IdealDivideTest {

	private NumberFactory numberFactory;
	private ObjectFactory objectFactory;
	private SymbolicTypeFactory typeFactory;
	private CollectionFactory collectionFactory;
	private IdealFactory idealFactory;
	private BooleanExpressionFactory booleanFactory;
	private CommonIdealFactory commonIdealFactory;

	private Constant intZero; // int constant 0
	private Constant intOne; // int constant 1
	private Constant intTwo; // int constant 2
	private Constant intThree; // int constant 3
	private Constant intFive; // int constant 5
	private Constant intTen; // int constant 10
	StringObject Xobj; // "X"
	NumericSymbolicConstant x; // int symbolic constant "X"
	NumericSymbolicConstant y; // int symbolic constant "Y"
	private NumericExpression fifteen; // real constant 15
	private NumericExpression five; // real constant 5
	private NumericExpression seven; // real constant 7
	private NumericExpression twentyOne; // real constant 21
	private NumericExpression thirtyFive; // real constant 35
	private NumericExpression zero; // real constant 0
	private RationalNumber realZero; // real 0
	private RationalNumber realOne; // real 1
	private RationalNumber realFifteen; // real 15
	private RationalNumber realFive; // real 5
	private NumericExpression three; // real 3
	private RationalNumber realThree; // real 3
	private RationalNumber realSeven; // real 7
	private RationalNumber realTwentyOne; // real 21
	private RationalNumber realThirtyFive; // real 35
	
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
		intZero = idealFactory.intConstant(0);
		intOne = idealFactory.intConstant(1);
		intTwo = idealFactory.intConstant(2);
		intThree = idealFactory.intConstant(3);
		intFive = idealFactory.intConstant(5);
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
		realSeven = numberFactory.rational("7");
		realTwentyOne = numberFactory.rational("21");
		realThirtyFive = numberFactory.rational("35");
		zero = commonIdealFactory.constant(realZero);
		fifteen = commonIdealFactory.constant(realFifteen);
		five = commonIdealFactory.constant(realFive);
		seven = commonIdealFactory.constant(realSeven);
		twentyOne = commonIdealFactory.constant(realTwentyOne);
		thirtyFive = commonIdealFactory.constant(realThirtyFive);
		realThree = numberFactory.rational("3");
		three = commonIdealFactory.constant(realThree);
	}

	@After
	public void tearDown() throws Exception {
		
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
		
		assertEquals(np, p1);
		assertEquals(intTen, b1);
		assertEquals(intZero, b2);
		assertEquals(n1, b3);
		assertEquals(three, b4);
		assertEquals(zero, b5);
	}
	
	/**
	 * Also divides two polynomials
	 * 
	 * @param type
	 * 				Polynomial
	 */
	@Test
	public void dividePoly() {
		StringObject Zobj;
		Zobj = objectFactory.stringObject("Z"); // string object 'Z'
		
		NumericSymbolicConstant z = objectFactory.canonic(idealFactory.symbolicConstant(Zobj,
				typeFactory.integerType()));
		NumericExpression p01 = idealFactory.multiply(x, x);
		NumericExpression p02 = idealFactory.add(intTen, idealFactory.
				multiply(intTen, x));
		NumericExpression p03 = idealFactory.add(intTwo, idealFactory.
				multiply(intTwo, x));
		NumericExpression p04 = idealFactory.multiply(intTen, x);
		NumericExpression p05 = idealFactory.add(p04, idealFactory.
				multiply(intTen, p01));
		NumericExpression p06 = idealFactory.add(x, intOne);
		NumericExpression p07 = idealFactory.multiply(intTen, p01);
		NumericExpression p08 = idealFactory.divide(x, intTen);
		NumericExpression p09 = idealFactory.multiply(idealFactory.
				multiply(idealFactory.subtract(x, intOne),idealFactory.
						add(x, intOne)), idealFactory.add(idealFactory.
								multiply(x,y), intTwo));
		NumericExpression p10 = idealFactory.multiply(idealFactory.
				multiply(idealFactory.subtract(x, intOne), z), idealFactory.
				add(idealFactory.multiply(x, y), intThree));
		NumericExpression p11 = idealFactory.multiply(idealFactory.
						add(x, intOne), idealFactory.add(idealFactory.
								multiply(x,y), intTwo));
		NumericExpression p12 = idealFactory.multiply(z, idealFactory.
				add(idealFactory.multiply(x, y), intThree));
		NumericExpression p13 = idealFactory.divide(p11, p12);
		Polynomial poly1 = (Polynomial) p01;
		Polynomial poly2 = (Polynomial) p02;
		Polynomial poly3 = (Polynomial) p03;
		Polynomial poly4 = (Polynomial) p04;
		Polynomial poly5 = (Polynomial) p05;
		Polynomial poly6 = (Polynomial) p07;
		Polynomial poly7 = (Polynomial) p09;
		Polynomial poly8 = (Polynomial) p10;				
		Polynomial b1 = (Polynomial) commonIdealFactory.divide(poly2, poly3);//pp
		Polynomial b2 = (Polynomial) commonIdealFactory.divide(poly5, poly4);//pm
		Polynomial b3 = (Polynomial) commonIdealFactory.divide(poly6, poly1);//mpp
		Polynomial b4 = (Polynomial) commonIdealFactory.divide(poly1, poly1);//pppp
		Polynomial b5 = (Polynomial) commonIdealFactory.divide(poly4, x);//mp
		Polynomial b6 = (Polynomial) commonIdealFactory.divide(x, intTen);//pc
		Polynomial b7 = (Polynomial) commonIdealFactory.divide(poly7, poly8);
				
		assertEquals(intFive, b1);
		assertEquals(p06, b2);
		assertEquals(intTen, b3);
		assertEquals(intOne, b4);
		assertEquals(intTen, b5);
		assertEquals(p08, b6);
		assertEquals(p13, b7);
	}
	
	@Test
	public void dividePolyToPoly() {
		NumericExpression p02 = idealFactory.add(intTen, idealFactory.
				multiply(intTen, x));
		NumericExpression p03 = idealFactory.add(intTwo, idealFactory.
				multiply(intTwo, x));
		Polynomial poly2 = (Polynomial) p02;
		Polynomial poly3 = (Polynomial) p03;
		
		Polynomial b1 = (Polynomial) commonIdealFactory.divide(poly2, poly3);
		
		assertEquals(intFive, b1);
	}
	
	@Test
	public void dividePolyToMonomial() {
		NumericExpression p01 = idealFactory.multiply(x, x);
		NumericExpression p04 = idealFactory.multiply(intTen, x);
		NumericExpression p05 = idealFactory.add(p04, idealFactory.
				multiply(intTen, p01));
		NumericExpression p06 = idealFactory.add(x, intOne);
		Polynomial poly4 = (Polynomial) p04;
		Polynomial poly5 = (Polynomial) p05;
		
		Polynomial b2 = (Polynomial) commonIdealFactory.divide(poly5, poly4);
		
		assertEquals(p06, b2);
	}
	
	@Test
	public void divideMonomialToPrimitivePower() {
		NumericExpression p01 = idealFactory.multiply(x, x);
		NumericExpression p07 = idealFactory.multiply(intTen, x);
		NumericExpression p08 = idealFactory.divide(intTen, x);
		Polynomial poly1 = (Polynomial) p01;
		Polynomial poly6 = (Polynomial) p07;
		
		Polynomial b3 = (Polynomial) commonIdealFactory.divide(poly6, poly1);
		
		assertEquals(p08, b3);
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
				typeFactory.realType())); // value 'X' of type real
		IntObject exp3 = objectFactory.intObject(3); // integer object '3'
		IntObject exp2 = objectFactory.intObject(2); // integer object '2'
		
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
	 * Returns a rational expression by canceling out the common factors that are present in both numerator and denominator.
	 * 
	 * @param type
	 * 				Symbolic Expressions of same numeric type
	 * 
	 *  [(x+1)(x-1)(xy+3)]/[(x-1)*(xy+5)*z] = [(x+1)(xy+3)]/[(xy+5)*z]?
	 */
	@Test
	public void factoringRational() {
		NumericSymbolicConstant x = objectFactory.canonic(idealFactory
				.symbolicConstant(objectFactory.stringObject("x"),
						typeFactory.realType())); // value 'X' of type real
		NumericSymbolicConstant y = objectFactory.canonic(idealFactory
				.symbolicConstant(objectFactory.stringObject("y"),
						typeFactory.realType())); // value 'Y' of type real
		NumericSymbolicConstant z = objectFactory.canonic(idealFactory
				.symbolicConstant(objectFactory.stringObject("z"),
						typeFactory.realType())); // value 'Z' of type real
		
		NumericExpression xPlus1 = idealFactory.add(x, 
				idealFactory.constant(realOne)); //(x+1)
		NumericExpression xMinus1 = idealFactory.subtract(x, 
				idealFactory.constant(realOne)); //(x-1)
		NumericExpression xy = idealFactory.multiply(x, y); //xy
		NumericExpression xyPlusThree = idealFactory.add(xy, 
				idealFactory.constant(realThree)); //xy+3
		NumericExpression xyPlusFive = idealFactory.add(xy, 
				idealFactory.constant(realFive)); //xy+5
		NumericExpression numerator = idealFactory.multiply(
				idealFactory.multiply(xPlus1, xMinus1), xyPlusThree); //(x+1)(x-1)(xy+3)
		NumericExpression denominator = idealFactory.multiply(
				idealFactory.multiply(xMinus1, xyPlusFive), z); //(x-1)*(xy+5)*z		
		NumericExpression resultNumer = idealFactory.multiply(xPlus1, 
				xyPlusThree); //(x+1)(xy+3)
		NumericExpression resultDenom = idealFactory.multiply(xyPlusFive, 
				z); //(xy+5)*z
		NumericExpression r1 = idealFactory.divide(numerator, denominator);
		NumericExpression result = idealFactory.divide(resultNumer, resultDenom);
		
		assertEquals(result, r1);
	}
	
	/**
	 * Multiply various levels of numbers (primitive, monic, poly, etc.) with a rational number
	 * 
	 * @return type
	 * 				RationalExpression
	 */
	@Test
	public void divideToRational() {
		NumericSymbolicConstant x = objectFactory.canonic(idealFactory
				.symbolicConstant(objectFactory.stringObject("x"),
						typeFactory.realType())); // value 'X' of type real
		NumericSymbolicConstant y = objectFactory.canonic(idealFactory
				.symbolicConstant(objectFactory.stringObject("Y"),
						typeFactory.realType())); // value 'Y' of type real
		
		RationalExpression r1 = (RationalExpression) idealFactory.divide(x, y);	// x/y	
		NumericExpression x2 = idealFactory.multiply(x, x); //x^2
		NumericExpression y2 = idealFactory.multiply(y, y); //y^2
		NumericExpression monic = idealFactory.multiply(x2, y); //x^2 * y
		NumericExpression monomial = idealFactory.multiply(idealFactory.constant(realThree), 
				monic); //3x^2 * y
		NumericExpression polynomial = idealFactory.add(idealFactory.
				divide(monomial, idealFactory.constant(realThree)), x2); //x^2 * y + x^2
		RationalExpression divPrimitive = (RationalExpression) 
				idealFactory.divide(r1, x); //1/y 
		RationalExpression divPrimitivePower = (RationalExpression) 
				idealFactory.divide(r1, x2); //1/(x*y)
		RationalExpression divMonic = (RationalExpression) 
				idealFactory.divide(monic, r1); //(x*y^2) 
		RationalExpression divMonomial = (RationalExpression) 
				idealFactory.divide(idealFactory.divide(monomial, 
						idealFactory.constant(realThree)), r1); //(x*y^2) 
		RationalExpression divPolynomial = (RationalExpression) 
				idealFactory.divide(polynomial, r1); //x^3 + (x^3/y)
		
		NumericExpression result1 = idealFactory.divide(y, y2); //1/y 
		NumericExpression result2 = idealFactory.multiply(idealFactory.
				divide(x, x2), idealFactory.divide(y, y2)); //1/(x*y) 
		NumericExpression result3 = idealFactory.multiply(y2, x); //(x*y^2) 
		NumericExpression result4 = idealFactory.add(result3, 
				idealFactory.multiply(x, y)); //(x*y^2) + (x*y) 
		
		assertEquals(result1, divPrimitive);	
		assertEquals(result2, divPrimitivePower);	
		assertEquals(result3, divMonic);	
		assertEquals(result3, divMonomial);
		assertEquals(result4, divPolynomial);
	}
}