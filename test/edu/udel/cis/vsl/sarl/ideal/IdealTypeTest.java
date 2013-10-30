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
import static org.junit.Assert.assertFalse;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
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
import edu.udel.cis.vsl.sarl.ideal.common.CommonIdealFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class IdealTypeTest {

	private static PrintStream out = System.out;
	private NumberFactory numberFactory;
	private ObjectFactory objectFactory;
	private SymbolicTypeFactory typeFactory;
	private CollectionFactory collectionFactory;
	private IdealFactory idealFactory;
	private BooleanExpressionFactory booleanFactory;
	private CommonIdealFactory commonIdealFactory;

	private RationalNumber ratZero; // 0
	private RationalNumber ratThree; // 3
	private Constant constZero;
	private Constant intNegOne; // int constant -1
	private Constant intZero; // int constant 0
	private Constant intTen; // int constant 10
	StringObject Xobj; // "X"
	IntObject intObj3;
	NumericSymbolicConstant x; // int symbolic constant "X"
	NumericSymbolicConstant y; // int symbolic constant "Y"
	private NumericExpression five;
	private NumericExpression oneTwoFive;
	private NumericExpression one;
	private RationalNumber realOne;
	private RationalNumber realFive; 
	private NumericExpression three; 
	private RationalNumber realThree; 
	private RationalNumber realOneTwoFive;
	private SymbolicType real;
	private SymbolicType integer;
	NumericExpression intHundred;
	NumericExpression intTwenty;
	NumericExpression e01; // Real 3 cast to integer 3
	NumericExpression e2; // 5 + 3 ADD
	NumericExpression e3; // 5 > 3, 5, 3 COND
	NumericExpression e4; // 5 * 3 MULTIPLY
	NumericExpression e5; // -5 NEGATIVE
	NumericExpression e6; // 5 ^ 3 POWER
	NumericExpression e7; // 5 - 3 SUBTRACT
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
		ratThree = numberFactory.rational("3");
		constZero = idealFactory.constant(ratZero);
		intZero = idealFactory.intConstant(0);
		intNegOne = idealFactory.intConstant(-1);
		intTen = idealFactory.intConstant(10);
		intObj3 = objectFactory.intObject(3);
		Xobj = objectFactory.stringObject("X");
		x = objectFactory.canonic(idealFactory.symbolicConstant(Xobj,
				typeFactory.integerType()));
		y = objectFactory.canonic(idealFactory.symbolicConstant(
				objectFactory.stringObject("Y"), typeFactory.integerType()));
		real = typeFactory.realType();
		integer = typeFactory.integerType();
		realOne = numberFactory.rational("1");
		realFive = numberFactory.rational("5");
		realOneTwoFive = numberFactory.rational("125");
		one = commonIdealFactory.constant(realOne);
		five = commonIdealFactory.constant(realFive);
		oneTwoFive = commonIdealFactory.constant(realOneTwoFive);
		realThree = numberFactory.rational("3");
		three = commonIdealFactory.constant(realThree);
		e01 = commonIdealFactory.expression(SymbolicOperator.CAST, 
				real, three);
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
		e10 = commonIdealFactory.expression(SymbolicOperator.POWER, integer,
				five, intObj3); // 5 ^ 3 POWER

	}

	@After
	public void tearDown() throws Exception {
		
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
	 * Returns SymbolicExpression of Real type with the value equal to 1
	 */
	@Test
	public void realOne() {
		NumericExpression n2 = commonIdealFactory.oneReal();
		
		assertEquals(one, n2);
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
}