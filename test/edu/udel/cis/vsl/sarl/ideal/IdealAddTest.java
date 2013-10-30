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
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;
import edu.udel.cis.vsl.sarl.ideal.IF.RationalExpression;
import edu.udel.cis.vsl.sarl.ideal.common.CommonIdealFactory;
import edu.udel.cis.vsl.sarl.ideal.common.NumericPrimitive;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class IdealAddTest {

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
	private Constant constOnePointFive; // real constant 3/2
	private Constant constNegPointTwoFive; // real constant -1/4
	private Constant intZero; // int constant 0
	private Constant intOne; // int constant 1
	private Constant intTwo; // int constant 2
	private Constant intThree; // int constant 3
	private Constant intTen; // int constant 10
	StringObject Xobj; // "X"
	NumericSymbolicConstant x; // int symbolic constant "X"
	NumericSymbolicConstant y; // int symbolic constant "Y"
	private NumericExpression five;
	private NumericExpression one;
	private RationalNumber realOne;
	private RationalNumber realFive; 
	private NumericExpression three; 
	private RationalNumber realThree; 
	private SymbolicType integer;
	NumericExpression e1; // 5 + 3 + 1 ADD
	
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
		intZero = idealFactory.intConstant(0);
		constOnePointFive = idealFactory.constant(ratOnePointFive);
		constNegPointTwoFive = idealFactory.constant(ratNegPointTwoFive);
		intOne = idealFactory.intConstant(1);
		intTwo = idealFactory.intConstant(2);
		intThree = idealFactory.intConstant(3);
		intTen = idealFactory.intConstant(10);
		Xobj = objectFactory.stringObject("X");
		x = objectFactory.canonic(idealFactory.symbolicConstant(Xobj,
				typeFactory.integerType()));
		y = objectFactory.canonic(idealFactory.symbolicConstant(
				objectFactory.stringObject("Y"), typeFactory.integerType()));
		integer = typeFactory.integerType();
		realOne = numberFactory.rational("1");
		realFive = numberFactory.rational("5");
		one = commonIdealFactory.constant(realOne);
		five = commonIdealFactory.constant(realFive);
		realThree = numberFactory.rational("3");
		three = commonIdealFactory.constant(realThree);
		e1 = commonIdealFactory.expression(SymbolicOperator.ADD, integer, five,
				three, one); // 5 + 3 +1 ADD
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
				
		assertEquals(e1, n1);
	}
	
	/**
	 * Adds various levels of numbers (primitive, monic, poly, etc.) with a rational number
	 * 
	 * @return type
	 * 				NumericPrimitive
	 */
	@Test
	public void addToRational() {
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
		NumericExpression polynomial = idealFactory.add(monomial, x2); //3x^2 * y + x^2
		RationalExpression plusPrimitive = (RationalExpression) 
				idealFactory.add(r1, x); //(x*y + x)/y 
		RationalExpression plusPrimitivePower = (RationalExpression) 
				idealFactory.add(r1, x2); //(x^2*y + x)/y 
		RationalExpression plusMonic = (RationalExpression) 
				idealFactory.add(r1, monic); //(x^2*y^2 + x)/y 
		RationalExpression plusMonomial = (RationalExpression) 
				idealFactory.add(r1, monomial); //(3*x^2*y^2 + x)/y 
		RationalExpression plusPolynomial = (RationalExpression) 
				idealFactory.add(r1, polynomial); //(3*x^2*y^2 + x^2 * y + x)/y
		NumericExpression result1 = idealFactory.multiply(x, y);
		NumericExpression result2 = idealFactory.multiply(x2, y);
		NumericExpression result3 = idealFactory.multiply(monic, y);
		NumericExpression result4 = idealFactory.multiply(monomial, y);
		NumericExpression result5 = idealFactory.multiply(polynomial, y);
		
		result1 = idealFactory.add(result1, x);
		result1 = idealFactory.divide(result1, y); //(x*y + x)/y 
		result2 = idealFactory.add(result2, x);
		result2 = idealFactory.divide(result2, y); //(x^2*y + x)/y 
		result3 = idealFactory.add(result3, x);
		result3 = idealFactory.divide(result3, y); //(x^2*y^2 + x)/y 
		result4 = idealFactory.add(result4, x);
		result4 = idealFactory.divide(result4, y); //(3*x^2*y^2 + x)/y 
		result5 = idealFactory.add(result5, x);
		result5 = idealFactory.divide(result5, y); //(3*x^2*y^2 + x^2 * y + x)/y 
		
		assertEquals(result1, plusPrimitive);	
		assertEquals(result2, plusPrimitivePower);	
		assertEquals(result3, plusMonic);	
		assertEquals(result4, plusMonomial);
		assertEquals(result5, plusPolynomial);
	}
}