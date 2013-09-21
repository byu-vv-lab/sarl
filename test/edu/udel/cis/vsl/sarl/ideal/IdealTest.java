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

import static org.junit.Assert.*;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
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
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
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
import edu.udel.cis.vsl.sarl.ideal.common.IdealComparator;
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
		
	private RationalNumber n0;
	private RationalNumber n1; // 3/2
	private RationalNumber n2; // -1/4
	private RationalNumber n3; // 5/4
	private Constant c;
	private Constant c0;
	private Constant c1; // real constant 3/2
	private Constant c2; // real constant -1/4
	private Constant c4; // int constant -1
	private Constant c10; // int constant 10
	StringObject Xobj; // "X"
	NumericSymbolicConstant x; // int symbolic constant "X"
	NumericSymbolicConstant y; // int symbolic constant "Y"
	private NumericExpression five; 		// 5
	private NumericExpression zero;
	private NumericExpression one;
	private RationalNumber realZero;
	private RationalNumber realOne;
	private RationalNumber realFive; 			// 5
	private NumericExpression three;		// 3
	private RationalNumber realThree;			// 3
	private NumericExpression intThree;
	private SymbolicType real;
	private SymbolicType integer;
	NumericExpression a1;
	NumericExpression a2;
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
		n0 = numberFactory.rational("0");
		n1 = numberFactory.rational("1.5");
		n2 = numberFactory.rational("-.25");
		n3 = numberFactory.rational("1.25");
		c = idealFactory.constant(n0);
		c0 = idealFactory.intConstant(0);
		c1 = idealFactory.constant(n1);
		c2 = idealFactory.constant(n2);
		c4 = idealFactory.intConstant(-1);
		c10 = idealFactory.intConstant(10);
		Xobj = objectFactory.stringObject("X");
		x = objectFactory.canonic(idealFactory.symbolicConstant(Xobj,
				typeFactory.integerType()));
		y = objectFactory.canonic(idealFactory.symbolicConstant(
				objectFactory.stringObject("Y"), typeFactory.integerType()));
				
		real = typeFactory.realType();
		integer = typeFactory.integerType();
		realZero = numberFactory.integerToRational(numberFactory.integer(0));
		realOne = numberFactory.integerToRational(numberFactory.integer(1));
		realFive = numberFactory.integerToRational(numberFactory.integer(5));
		zero = commonIdealFactory.constant(realZero);
		one = commonIdealFactory.constant(realOne);
		five = commonIdealFactory.constant(realFive); 
		realThree = numberFactory.integerToRational(numberFactory.integer(3)); 
		three = commonIdealFactory.constant(realThree);
		//intThree = commonIdealFactory.multiply(commonIdealFactory.oneInt(), three);
		intThree = commonIdealFactory.expression(SymbolicOperator.CAST, integer, three);
		a1 = idealFactory.intConstant(100);
		a2 = idealFactory.intConstant(20);
		e1 = commonIdealFactory.constant(realFive);						// 1         IsReal
		//e1 = commonIdealFactory.expression(SymbolicOperator.ADD, typeFactory.integerType(), five);
		e2 = commonIdealFactory.expression(SymbolicOperator.ADD, integer, five, three);  // 5 + 3     Add
		//e3 = commonIdealFactory.expression(SymbolicOperator.COND, integer, five, three);  // 3 > 2, 3, 2    COND
		e3 = commonIdealFactory.expression(SymbolicOperator.COND, typeFactory.booleanType(), booleanFactory.falseExpr(),booleanFactory.falseExpr());
		e4 = commonIdealFactory.expression(SymbolicOperator.MULTIPLY, integer, five, three);  // 5 * 3			MULTIPLY
		e5 = commonIdealFactory.expression(SymbolicOperator.NEGATIVE, integer, five); 		  // -5				NEGATIVE
		e6 = commonIdealFactory.expression(SymbolicOperator.POWER, integer, five, three);     // 5 ^ 3			POWER
		e7 = commonIdealFactory.expression(SymbolicOperator.SUBTRACT, integer, five, three);  // 5 - 3			SUBTRACT
		e8 = commonIdealFactory.zeroReal(); //				DEFAULT}
		//e8 = commonIdealFactory.expression(SymbolicOperator., numericType, arguments)
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
	public void realone(){
		//NumericExpression n1 = idealFactory.intConstant(1);
		NumericExpression n2 = idealFactory.oneReal();
		//out.println("ne=" +ne);
		assertEquals(one, n2);
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
	/*
 	@Test
	public void rationalMinus() {
		RationalExpression r = (RationalExpression) idealFactory.divide(x, y);
		NumericExpression minusr = idealFactory.multiply(y,idealFactory.subtract(idealFactory.intConstant(1), r));
		Monomial negativex = idealFactory.monomial(c4, (Monic) x);
		RationalExpression negativer = (RationalExpression) idealFactory.divide(negativex, y);
		out.println("minus r: " + minusr +"vs. " + negativer);
		assertEquals(negativer, minusr);
 	}
 	*/
	/*
	@Test
	public void positivenumber(){
		Monomial monomial = idealFactory.monomial(c10, (Monic) x);
		SymbolicMap<Monic, Monomial> termMap = null;
		Polynomial poly = idealFactory.polynomial(termMap, monomial);
		BooleanExpression b = idealFactory.isPositive(poly);
		assertEquals(b, commonIdealFactory.booleanFactory().trueExpr());
		
	}
	*/
	
	@Test
	public void lessThan() {
		BooleanExpression result = commonIdealFactory.lessThan(zero, five);
		assertEquals(commonIdealFactory.booleanFactory().trueExpr(), result);
		RationalExpression r1 = (RationalExpression) x;
		RationalExpression r2 = (RationalExpression) commonIdealFactory.multiply(x, x);
		BooleanExpression result2 = commonIdealFactory.lessThan(r1, r2);
		//assertEquals(commonIdealFactory.booleanFactory().trueExpr(), result2);
	}
	/*
	@Test
	public void intModulusPolynomials(){
		SymbolicMap<Monic, Monomial> termMap0 = commonIdealFactory.emptyMap();
		Monic monic0 = (Monic) idealFactory.symbolicConstant(objectFactory.stringObject("X"), real);
		Monomial monomial0 = idealFactory.monomial(idealFactory.intConstant(0), monic0);
		termMap0.put(monic0, monomial0);
		//new SymbolicMap<Monic, Monomial>() {idealFactory.};
		Polynomial numeratorIsZero = commonIdealFactory.polynomial(termMap0, monomial0);
		Polynomial modulousIsZero = numeratorIsZero.factorization(commonIdealFactory);//commonIdealFactory.m
		SymbolicMap<Monic, Monomial> termMap1 = commonIdealFactory.emptyMap();
		Monic monic1 = (Monic) idealFactory.symbolicConstant(objectFactory.stringObject("X"), real);
		Monomial monomial1 = idealFactory.monomial(idealFactory.intConstant(1), monic1);
		termMap1.put(monic1, monomial1);
		Polynomial denominatorIsOne = commonIdealFactory.polynomial(termMap1, monomial1);
		Polynomial modulousIsOne = denominatorIsOne.factorization(commonIdealFactory);
		
		
		SymbolicMap<Monic, Monomial> termMap2 = commonIdealFactory.emptyMap();
		Monic monic2 = (Monic) idealFactory.symbolicConstant(objectFactory.stringObject("X"), real);
		Monomial monomial2 = idealFactory.monomial(idealFactory.intConstant(2), monic1);
		Monic monic3 = (Monic) idealFactory.symbolicConstant(objectFactory.stringObject("X"), real);
		Monomial monomial3 = idealFactory.monomial(idealFactory.intConstant(3), monic1);
		
		termMap2.put(monic2, monomial2);
		termMap2.put(monic3, monomial3);
		Polynomial numeratorIsFive = commonIdealFactory.polynomial(termMap2, monomial0);
		Polynomial modulousIsFive = numeratorIsFive.factorization(commonIdealFactory);
		
		//Numerator is Zero
		Polynomial zeroOverOne = (Polynomial) commonIdealFactory.modulo(numeratorIsZero, denominatorIsOne);idealFactory.divide(numeratorIsZero, idealFactory.intConstant(5));
		Polynomial zeroOverTwo  = numeratorIsZero.denominator(commonIdealFactory);//commonIdealFactory.divide(numeratorIsZero, denominatorIsOne);
		Polynomial zeroOverThree = numeratorIsZero.numerator(commonIdealFactory);
	
		//Denominator is One
		Polynomial oneOverOne = (Polynomial) commonIdealFactory.modulo(denominatorIsOne, denominatorIsOne);
		Polynomial fiveOverOne = (Polynomial) commonIdealFactory.modulo(numeratorIsFive, denominatorIsOne);
		SymbolicMap<Monic, Monomial> map01 = numeratorIsFive.termMap(commonIdealFactory); //numerator(commonIdealFactory);
	}
	*/
	
	@Test
	public void castToReal(){
		RationalNumber n5 = numberFactory.rational("5.5");
		RealNumber realFive = (RealNumber) realNumberFactory.integer(5); //realNumberFactory.integer(fiveLong);
		commonIdealFactory.cast(e1, real);
		//assertEquals(e1, realFive);
		
		Constant constant1 = commonIdealFactory.intConstant(1);
		Constant constant2 = commonIdealFactory.intConstant(2);
		Constant constant3 = commonIdealFactory.intConstant(3);
		NumericExpression constants1to3 = commonIdealFactory.expression(SymbolicOperator.ADD, integer, constant1, constant2, constant3);
		NumericExpression constants1to2 = commonIdealFactory.expression(SymbolicOperator.ADD, integer, constant1, constant2);
		NumericExpression constants1 = commonIdealFactory.expression(SymbolicOperator.ADD, integer, constant1);
		NumericExpression xp1 = commonIdealFactory.add(x, constant1);
		NumericExpression testn = commonIdealFactory.add(constants1to3, constants1to2);
		out.println("testn: " + testn);
		commonIdealFactory.cast(testn, real);
		out.println("testn: " + testn);
		assertEquals(commonIdealFactory.cast(e1, real), commonIdealFactory.cast(five, real));
		
		
		out.println("e2: " + e2);

		NumericExpression test3 = commonIdealFactory.expression(SymbolicOperator.ADD, typeFactory.realType(),five, three, three);
		//NumericExpression test2 = commonIdealFactory.add(e1, x);

		//NumericExpression test3 = commonIdealFactory.expression(SymbolicOperator.ADD, typeFactory.realType(),five, three, three);
		//NumericExpression test2 = commonIdealFactory.add(e1, x);

		commonIdealFactory.cast(e2, real);
		//commonIdealFactory.cast(test2, real);
		//out.println("test2: " + test2);
		//assertEquals(test2, realAdd);
		
		
		commonIdealFactory.cast(e3, real);
		//assertEquals(e3,
		
		//out.println("e4: " + e4);
		NumericExpression multiply = commonIdealFactory.expression(SymbolicOperator.MULTIPLY, real, five, three);
		NumericExpression e4New = commonIdealFactory.cast(e4, real);
		NumericExpression realMultiply = commonIdealFactory.cast(multiply, real);
		NumericExpression multiMultiply = commonIdealFactory.expression(SymbolicOperator.MULTIPLY, integer, e4, multiply);
		NumericExpression realMultiMultiply = commonIdealFactory.cast(multiMultiply, real);
		//out.println("e4New: " + e4New);
		out.println("realMultiMultiply: " + realMultiMultiply);
		assertEquals(e4.argument(0), multiply.argument(0));
		assertEquals(e4New.type(), multiply.type());
		assertEquals(e4.argument(1), multiply.argument(1));
		//assertEquals(e4, realMulitiply);
		
		//out.println("e5: " + e5);
		NumericExpression minus = commonIdealFactory.expression(SymbolicOperator.NEGATIVE, real, five);
		NumericExpression e5New = commonIdealFactory.cast(e5, real);
		NumericExpression realMinus = commonIdealFactory.cast(minus, real);
		//out.println("e5New: " + e5New);
		assertEquals(e5.argument(0), minus.argument(0));
		assertEquals(e5New.type(), minus.type());
		//assertEquals(e5, realMinus);
		
		//out.println("e6: " + e6);
		NumericExpression power = commonIdealFactory.expression(SymbolicOperator.POWER,  real, five, three);
		NumericExpression e6New = commonIdealFactory.cast(e6, real);
		NumericExpression realPower = commonIdealFactory.cast(power, real);
		//out.println("e6New: " + e6New);
		assertEquals(e6.argument(0), power.argument(0));
		assertEquals(e6New.type(), power.type());
		assertEquals(e6.argument(1), power.argument(1));
		//assertEquals(e6, realPower);
		
		//out.println("e7: " + e7);
		NumericExpression subtract = commonIdealFactory.expression(SymbolicOperator.SUBTRACT,  real,  five, three);
		NumericExpression e7New = commonIdealFactory.cast(e7, real);
		NumericExpression realSubtract = commonIdealFactory.cast(subtract, real);
		commonIdealFactory.cast(commonIdealFactory.expression(SymbolicOperator.POWER, integer, five, intThree), real);
		//out.println("e7New: " + e7New);
		assertEquals(e7.argument(0), subtract.argument(0));
		assertEquals(e7New.type(), subtract.type());
		assertEquals(e7.argument(1), subtract.argument(1));
		//BooleanExpression e7equalsRealSubtract = commonIdealFactory.equals(e7New, subtract);
		//assertTrue(e7equalsRealSubtract.isTrue());
		//assertEquals(e7New, realSubtract);

	}
	
	@Test
	public void addpolynomial(){
		NumericExpression p1 =idealFactory.add(idealFactory.multiply(x, x), idealFactory.intConstant(1));
		NumericExpression p2 =idealFactory.add(idealFactory.multiply(idealFactory.intConstant(2), idealFactory.multiply(x, x)), idealFactory.intConstant(1));
		NumericExpression p3 = idealFactory.multiply(c0, x);
		Polynomial poly1 = (Polynomial) p1;
		Polynomial poly2 = (Polynomial) p2;
		Polynomial poly3 = (Polynomial) p3;
		Polynomial b1 = commonIdealFactory.add(poly1, poly2);
		Polynomial b2 = commonIdealFactory.add(poly3, poly2);
		out.println("ADD_Polynomial=" + b1);
		NumericExpression p4 = idealFactory.add(idealFactory.multiply(idealFactory.intConstant(3), idealFactory.multiply(x, x)), idealFactory.intConstant(2));
		//Polynomial c = (Polynomial) idealFactory.intConstant(0);
		assertEquals(p4, b1);
		
	}
	
	@Test
	public void multiplypolynomial(){
		NumericExpression p1 =idealFactory.add(idealFactory.multiply(x, x), idealFactory.intConstant(1));
		NumericExpression p2 =idealFactory.add(idealFactory.multiply(idealFactory.intConstant(2), idealFactory.multiply(x, x)), idealFactory.intConstant(1));
		NumericExpression p3 = idealFactory.multiply(c0, x);
		Polynomial poly1 = (Polynomial) p1;
		Polynomial poly2 = (Polynomial) p2;
		Polynomial poly3 = (Polynomial) p3;
		Polynomial b1 = commonIdealFactory.multiply(poly1, poly2);
		Polynomial b2 = commonIdealFactory.multiply(poly1, poly3);
		out.println("Multiply_Polynomial=" + b1);
		NumericExpression x2 = idealFactory.multiply(x, x);
		NumericExpression x4 = idealFactory.multiply(x2, x2);
		NumericExpression p4 = idealFactory.add(idealFactory.multiply(idealFactory.intConstant(3), idealFactory.multiply(x, x)), idealFactory.intConstant(1));
		NumericExpression p5 = idealFactory.add(idealFactory.multiply(idealFactory.intConstant(2),x4), p4);
		assertEquals(p5, b1);
	}
	@Test
	public void constanttermsubtraction(){
		NumericExpression n = idealFactory.add(idealFactory.multiply(one, x), idealFactory.intConstant(1));
		Polynomial poly = (Polynomial) n;
		Polynomial b1 = commonIdealFactory.subtractConstantTerm(poly);
		Polynomial b2 = commonIdealFactory.subtractConstantTerm(c);
		//out.println("Constant Term Subtraction1=" + b2);
		//out.println("Constant Term Subtraction=" + b1);
		assertEquals(x, b1);
		assertEquals(c, b2);
	}
	
	@Test
	public void minus(){
		NumericExpression p1 =idealFactory.add(idealFactory.multiply(x, x), idealFactory.intConstant(1));
		NumericExpression ne = commonIdealFactory.minus(p1);
		NumericExpression n = idealFactory.minus(p1);
		//out.println("n=" +n);
		//out.println("ne=" +ne);
		assertEquals(n, ne);
	}
		
	@Test
	public void notlessthan(){
		NumericExpression n1 = idealFactory.subtract(x, y);
		NumericExpression n2 = idealFactory.add(x, y);
		BooleanExpression n = commonIdealFactory.notLessThan(n1, n2);
		out.println("Not Less Than=" +n);
	}
	@Test
	public void notlessthanequals(){
		NumericExpression n1 = idealFactory.subtract(x, idealFactory.intConstant(1));
		NumericExpression n2 = idealFactory.add(x, idealFactory.intConstant(1));
		BooleanExpression n = commonIdealFactory.notLessThanEquals(n1, n2);
		out.println("Not Less Than Equals=" +n);
	}
	@Test
	public void equals(){
		NumericExpression n1 = idealFactory.power(idealFactory.add(x,idealFactory.intConstant(1)), idealFactory.intConstant(2));
		NumericExpression n2 = idealFactory.subtract(idealFactory.multiply(x,x), idealFactory.intConstant(1));
		BooleanExpression n = commonIdealFactory.equals(n1, n2);
		out.println("Equals=" +n);
	}
	@Test
	public void intmoduluspolynomials(){
		NumericExpression n1 = idealFactory.add(idealFactory.multiply(x, x), idealFactory.intConstant(1));
		NumericExpression n2 = idealFactory.add(x, idealFactory.intConstant(1));
		NumericExpression n3 = idealFactory.intConstant(0);
		NumericExpression n4 = idealFactory.intConstant(1);
		NumericExpression n = commonIdealFactory.modulo(n1, n2);
		NumericExpression m = commonIdealFactory.modulo(n3, n2);
		NumericExpression p = commonIdealFactory.modulo(n1, n4);
		out.println("modulo=" +n);
	}
	@Test
	public void divide(){
		NumericExpression n = idealFactory.add(x, y);
		NumericExpression m = idealFactory.subtract(x, y);
		NumericExpression p = commonIdealFactory.divide(n, m);
		NumericExpression n1 = idealFactory.divide(n, m);
		assertEquals(n1, p);
	}
	@Test
	public void neq(){
		NumericExpression n1 = idealFactory.add(x, idealFactory.intConstant(1));
		NumericExpression n2 = idealFactory.add(x, idealFactory.intConstant(2));
		BooleanExpression n = commonIdealFactory.neq(n1, n2);
		out.println("neq=" +n);
	    //assertEquals(true, n);
	}
	
	@Test
	public void zero(){
		Constant c1 = commonIdealFactory.zero(real);
		assertEquals(c, c1);
		//out.println("zero=" +c);
	}
	@Test
	public void monomial(){
		Monic monic = (Monic) idealFactory.multiply(x, x);
		Monomial m = commonIdealFactory.monomial(c, monic);
		//out.println("monomial=" +m);
		assertEquals(c, m);
	}
	@Test
	public void rationalmultiply(){
		NumericExpression n1 = commonIdealFactory.multiply(three, five);
		NumericExpression n2 = commonIdealFactory.multiply(three, zero);
		NumericExpression n3 = commonIdealFactory.multiply(zero, five);
		NumericExpression n4 = commonIdealFactory.multiply(three, one);
	}
	
}
