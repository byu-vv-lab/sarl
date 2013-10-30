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

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
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
import edu.udel.cis.vsl.sarl.ideal.IF.RationalExpression;
import edu.udel.cis.vsl.sarl.ideal.common.CommonIdealFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class IdealEqualityTest {

	private static PrintStream out = System.out;
	private NumberFactory numberFactory;
	private ObjectFactory objectFactory;
	private SymbolicTypeFactory typeFactory;
	private CollectionFactory collectionFactory;
	private IdealFactory idealFactory;
	private BooleanExpressionFactory booleanFactory;
	private CommonIdealFactory commonIdealFactory;

	private RationalNumber ratThree; // 3
	private Constant intTwo; // int constant 2
	StringObject Xobj; // "X"
	IntObject intObj3;
	NumberObject numObj3;
	NumericSymbolicConstant x; // int symbolic constant "X"
	NumericSymbolicConstant y; // int symbolic constant "Y"
	private NumericExpression five;
	private NumericExpression zero;
	private NumericExpression one;
	private RationalNumber realZero;
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
		ratThree = numberFactory.rational("3");
		intTwo = idealFactory.intConstant(2);
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
		realFive = numberFactory.rational("5");
		zero = commonIdealFactory.constant(realZero);
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
	 * Compares two Rational Expressions
	 * 
	 * @param type
	 * 				Symbolic Expressions of same numeric type
	 */
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
		BooleanExpression nb3 = idealFactory.notLessThanEquals(commonIdealFactory.
				zeroReal(), r1);
		BooleanExpression nb4 = idealFactory.lessThanEquals(r1, commonIdealFactory.
				zeroReal());
		NumericExpression nb5 = commonIdealFactory.divide(
				commonIdealFactory.zeroReal(), r1);
		BooleanExpression nb6 = idealFactory.lessThan(r1, commonIdealFactory.
				zeroReal());
		
		out.println("b1=" +b1);
		out.println("b2=" +b2);
		
		assertEquals(zero, nb5);
		assertEquals(nb2, nb4);
		assertEquals(nb3, nb6);
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
}