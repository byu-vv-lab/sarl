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
import edu.udel.cis.vsl.sarl.ideal.common.NumericPrimitive;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class IdealAddTest {

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
	private RationalNumber ratOnePointTwoFive; // 5/4
	private RationalNumber ratThree; // 3
	private Constant constOnePointFive; // real constant 3/2
	private Constant constNegPointTwoFive; // real constant -1/4
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
	private NumericExpression five;
	private NumericExpression one;
	private RationalNumber realOne;

	private RationalNumber realFive; 
	private NumericExpression three; 
	private RationalNumber realThree; 
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
		ratOnePointFive = numberFactory.rational("1.5");
		ratNegPointTwoFive = numberFactory.rational("-.25");
		ratOnePointTwoFive = numberFactory.rational("1.25");
		ratThree = numberFactory.rational("3");
		intZero = idealFactory.intConstant(0);
		constOnePointFive = idealFactory.constant(ratOnePointFive);
		constNegPointTwoFive = idealFactory.constant(ratNegPointTwoFive);
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
		realOne = numberFactory.rational("1");
		realFive = numberFactory.rational("5");
		one = commonIdealFactory.constant(realOne);
		five = commonIdealFactory.constant(realFive);
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
}