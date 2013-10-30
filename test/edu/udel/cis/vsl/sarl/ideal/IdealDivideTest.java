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
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;
import edu.udel.cis.vsl.sarl.ideal.common.CommonIdealFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class IdealDivideTest {

	private static PrintStream out = System.out;
	private NumberFactory numberFactory;
	private ObjectFactory objectFactory;
	private SymbolicTypeFactory typeFactory;
	private CollectionFactory collectionFactory;
	private IdealFactory idealFactory;
	private BooleanExpressionFactory booleanFactory;
	private CommonIdealFactory commonIdealFactory;

	private RationalNumber ratThree; // 3
	private Constant intZero; // int constant 0
	private Constant intOne; // int constant 1
	private Constant intTwo; // int constant 2
	private Constant intThree; // int constant 3
	private Constant intFive; // int constant 5
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
		ratThree = numberFactory.rational("3");
		intZero = idealFactory.intConstant(0);
		intOne = idealFactory.intConstant(1);
		intTwo = idealFactory.intConstant(2);
		intThree = idealFactory.intConstant(3);
		intFive = idealFactory.intConstant(5);
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
		zero = commonIdealFactory.constant(realZero);
		one = commonIdealFactory.constant(realOne);
		fifteen = commonIdealFactory.constant(realFifteen);
		five = commonIdealFactory.constant(realFive);
		seven = commonIdealFactory.constant(realSeven);
		twentyOne = commonIdealFactory.constant(realTwentyOne);
		thirtyFive = commonIdealFactory.constant(realThirtyFive);
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
	 * Also divides two polynomials
	 * 
	 * @param type
	 * 				Polynomial
	 */
	@Test
	public void dividePoly() {
		StringObject Zobj;
		Zobj = objectFactory.stringObject("Z");
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
				
		Polynomial b1 = (Polynomial) commonIdealFactory.divide(poly2, poly3);
		Polynomial b2 = (Polynomial) commonIdealFactory.divide(poly5, poly4);
		Polynomial b3 = (Polynomial) commonIdealFactory.divide(poly6, poly1);
		Polynomial b4 = (Polynomial) commonIdealFactory.divide(poly1, poly1);
		Polynomial b5 = (Polynomial) commonIdealFactory.divide(poly4, x);
		Polynomial b6 = (Polynomial) commonIdealFactory.divide(x, intTen);
		Polynomial b7 = (Polynomial) commonIdealFactory.divide(poly7, poly8);
				
		assertEquals(intFive, b1);
		assertEquals(p06, b2);
		assertEquals(intTen, b3);
		assertEquals(intOne, b4);
		assertEquals(intTen, b5);
		assertEquals(p08, b6);
		assertEquals(p13, b7);
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
}