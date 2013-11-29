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
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;
import edu.udel.cis.vsl.sarl.ideal.IF.RationalExpression;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
/**
 * The class IdealMultiplyTest tests methods found in the edu.udel.cis.vsl.sarl.ideal.common package 
 * using multiplications among various combinations such as:
 * 
 * <ul>
 * <li>Polynomial * Polynomial</li>
 * <li>Polynomial * Monomial</li>
 * <li>Monomial * Monomial</li>
 * <li>Monomial * Monic</li>
 * <li>Monomial * PrimitivePower</li>
 * <li>Monic * Monic</li>
 * <li>Monic * PrimitivePower</li>
 * <li>PrimitivePower * PrimitivePower</li>
 * <li>Primitive * PrimitivePower</li>
 * <li>Constant * Primitive</li>
 * <li>RationalExpression * Polynomial</li>
 * <li>RationalExpression * Monomial</li>
 * <li>RationalExpression * Monic</li>
 * <li>RationalExpression * Primitive</li>
 * <li>RationalExpression * PrimitivePower</li>
 * </ul>
 *
 */
public class IdealMultiplyTest {

	private static PrintStream out = System.out;
	private NumberFactory numberFactory;
	private ObjectFactory objectFactory;
	private SymbolicTypeFactory typeFactory;
	private IdealFactory idealFactory;

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
		idealFactory = (IdealFactory) system.numericFactory();
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
		zero = idealFactory.constant(realZero);
		one = idealFactory.constant(realOne);
		fifteen = idealFactory.constant(realFifteen);
		five = idealFactory.constant(realFive);
		realThree = numberFactory.rational("3");
		three = idealFactory.constant(realThree);
	}

	@After
	public void tearDown() throws Exception {
		
	}

	/**
	 * a function - multiply() which multiplies two polynomials
	 * 
	 * @param a - Polynomial
	 * @param b - Polynomial
	 * 
	 * @return
	 * 			the value of an expression consisting of multiplication of two polynomials
	 */
	public Polynomial multiply(Polynomial a, Polynomial b){
		Polynomial p = idealFactory.multiply(a, b);
		return p;
	}
	
	/**
	 * a function - multiplyRational() which multiplies a rational expression with a polynomial
	 * 
	 * @param a - RationalExpression
	 * @param b - Polynomial
	 * 
	 * @return
	 * 			the value of an expression consisting of multiplication of a rational expression and a polynomial
	 */
	public RationalExpression multiplyRational(RationalExpression a, Polynomial b){
		RationalExpression r = (RationalExpression) idealFactory.multiply(a, b);
		return r;
	}
	
	/**
	 * Multiplies two Constants of type real and returns a Constant with 
	 * the same type
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
	 * Multiplies two polynomials by forming the factorization and by 
	 * factoring out the common factors that are produced from the two factorizations.
	 * 
	 * @param p1
	 *            a Polynomial
	 * @param p2
	 *            a Polynomial
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a polynomial of type Polynomial which is the multiplication 
	 * 				of two polynomials (passed as arguments).
	 */
	@Test
	public void mulPolyToPoly() {
		Polynomial p1 = (Polynomial) idealFactory.add(idealFactory.multiply(x, x), intOne);
		Polynomial p2 = (Polynomial) idealFactory.add(idealFactory.multiply(intTwo,
						idealFactory.multiply(x, x)), intOne);
		Polynomial p3 = (Polynomial) idealFactory.multiply(intZero, x);
		NumericExpression x2 = idealFactory.multiply(x, x);
		NumericExpression x4 = idealFactory.multiply(x2, x2);
		NumericExpression p4 = idealFactory.add(idealFactory.multiply(idealFactory.
				intConstant(3), idealFactory.multiply(x, x)), intOne);
		NumericExpression p5 = idealFactory.add(idealFactory.
				multiply(intTwo, x4), p4);
				
		Polynomial b1 = multiply(p1, p2);
		Polynomial b2 = multiply(p1, p3);
		
		assertEquals(p5, b1);
		assertEquals(intZero, b2);
	}
	
	/**
	 * Multiplies a polynomial with a monomial by forming the factorization 
	 * and by factoring out the common factors that are produced from the two factorizations.
	 * 
	 * @param p1
	 *            a Monomial
	 * @param p2
	 *            a Polynomial
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a polynomial of type Polynomial which is the multiplication 
	 * 				of a polynomial and a monomial (passed as arguments).
	 */
	@Test
	public void mulPolyToMonomial() {
		Polynomial p1 = (Polynomial) idealFactory.add(idealFactory.multiply(x, x), intOne);
		Polynomial p2 = (Polynomial) idealFactory.multiply(intTen, x);
		NumericExpression x2 = idealFactory.multiply(x, x);
		NumericExpression p3 = idealFactory.add(idealFactory.multiply(intTen, 
				idealFactory.multiply(x2, x)), p2);
		
		Polynomial b1 = multiply(p2, p1);
		
		assertEquals(p3, b1);
	}
	
	/**
	 * Multiplies two monomials by forming the factorization 
	 * and by factoring out the common factors that are produced from the two factorizations.
	 * 
	 * @param p1
	 *            a Monomial
	 * @param p2
	 *            a Monomial
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a polynomial of type Polynomial which is the multiplication 
	 * 				of two monomials (passed as arguments).
	 */
	@Test
	public void mulMonomialToMonomial() {
		Polynomial p1 = (Polynomial) idealFactory.multiply(intTen, x);
		NumericExpression p2 = idealFactory.multiply(idealFactory.intConstant(100), 
				idealFactory.multiply(x, x));
		
		Polynomial b1 = multiply(p1, p1);
		
		assertEquals(p2, b1);
	}
	
	/**
	 * Multiplies a monomial to a monic by forming the factorization 
	 * and by factoring out the common factors that are produced from the two factorizations.
	 * 
	 * @param p1
	 *            a Monomial
	 * @param p2
	 *            a Monic
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a polynomial of type Polynomial which is the multiplication 
	 * 				of a monomial to a monic (passed as arguments).
	 */
	@Test
	public void mulMonomialToMonic() {
		Polynomial p1 = (Polynomial) idealFactory.multiply(intTen, x);
		Polynomial p2 = (Polynomial) idealFactory.multiply(x, y);
		NumericExpression p3 = idealFactory.multiply(idealFactory.multiply(x, intTen), 
				idealFactory.multiply(x, y));
		
		Polynomial b1 = multiply(p1, p2);
		
		assertEquals(p3, b1);
	}
	
	/**
	 * Multiplies two monics by forming the factorization 
	 * and by factoring out the common factors that are produced from the two factorizations.
	 * 
	 * @param p1
	 *            a Monic
	 * @param p2
	 *            a Monic
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a polynomial of type Polynomial which is the multiplication 
	 * 				of two monics (passed as arguments).
	 */
	@Test
	public void mulMonicToMonic() {
		Polynomial p1 = (Polynomial) idealFactory.multiply(idealFactory.multiply(x, y), x);
		Polynomial p2 = (Polynomial) idealFactory.multiply(x, y);
		NumericExpression p3 = idealFactory.multiply(idealFactory.multiply(x, 
				idealFactory.multiply(x, x)), idealFactory.multiply(y, y));
		
		Polynomial b1 = multiply(p1, p2);
		
		assertEquals(p3, b1);
	}
	
	/**
	 * Multiplies a monomial with a primitive power by forming the factorization 
	 * and by factoring out the common factors that are produced from the two factorizations.
	 * 
	 * @param p1
	 *            a Monomial
	 * @param p2
	 *            a PrimitivePower
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a polynomial of type Polynomial which is the multiplication 
	 * 				of a monomial and a primitive power (passed as arguments).
	 */
	@Test
	public void mulMonomialToPrimitivePower() {
		Polynomial p1 = (Polynomial) idealFactory.multiply(intTen, x);
		Polynomial x2 = (Polynomial) idealFactory.multiply(x, x);
		NumericExpression p2 = idealFactory.multiply(intTen, idealFactory.
				multiply(x2, x));
		
		Polynomial b1 = multiply(p1, x2);
		
		assertEquals(p2, b1);
	}
	
	/**
	 * Multiplies a monic with a primitive power by forming the factorization 
	 * and by factoring out the common factors that are produced from the two factorizations.
	 * 
	 * @param p1
	 *            a Monoic
	 * @param p2
	 *            a PrimitivePower
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a polynomial of type Polynomial which is the multiplication 
	 * 				of a monic and a primitive power (passed as arguments).
	 */
	@Test
	public void mulMonicToPrimitivePower() {
		Polynomial p1 = (Polynomial) idealFactory.multiply(x, y);
		Polynomial x2 = (Polynomial) idealFactory.multiply(x, x);
		NumericExpression p2 = idealFactory.multiply(idealFactory.multiply(x, idealFactory.
				multiply(x, x)), y);
		
		Polynomial b1 = multiply(p1, x2);
		
		assertEquals(p2, b1);
	}
	
	/**
	 * Multiplies two primitive powers by forming the factorization and by 
	 * factoring out the common factors that are produced from the two factorizations.
	 * 
	 * @param p1
	 *            a PrimitivePower
	 * @param p2
	 *            a PrimitivePower
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a polynomial of type Polynomial which is the multiplication 
	 * 				of two primitive powers (passed as arguments).
	 */
	@Test
	public void mulPrimitivePowerToItself() {
		Polynomial x2 = (Polynomial) idealFactory.multiply(x, x);
		NumericExpression x4 = idealFactory.multiply(x2, x2);
		
		Polynomial b1 = multiply(x2, x2);
		
		assertEquals(x4, b1);
	}
	
	/**
	 * Multiplies a primitive power and a primitive by forming the factorization 
	 * and by factoring out the common factors that are produced from the two factorizations.
	 * 
	 * @param p1
	 *            a PrimitivePower
	 * @param p2
	 *            a Primitive
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a polynomial of type Polynomial which is the multiplication 
	 * 				of a primitive power and a primitive (passed as arguments).
	 */
	@Test
	public void mulPrimitivePowerToPrimitive() {
		Polynomial x2 = (Polynomial) idealFactory.multiply(x, x);
		NumericExpression p1 = idealFactory.multiply(x, x2);
		Polynomial poly1 = (Polynomial) x;
				
		Polynomial b1 = multiply(x2,poly1);
		
		assertEquals(p1, b1);
	}
	
	/**
	 * Multiplies a Constant and a Primitive by forming the factorization and 
	 * by factoring out the common factors that are produced from the two factorizations.
	 * 
	 * @param p1
	 *            a Primitive
	 * @param p2
	 *            a Constant
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a polynomial of type Polynomial which is the multiplication 
	 * 				of a constant and a primitive (passed as arguments).
	 */
	@Test
	public void mulConstantToPrimitive() {
		Polynomial poly1 = (Polynomial) x;
		NumericExpression p1 = idealFactory.multiply(intTen, x);
		
		Polynomial b1 = idealFactory.multiply(poly1, intTen);
		
		assertEquals(p1, b1);
	}
	
	/**
	 * Multiplies two rational numbers. 
	 * Also checks if the first or second argument is zero or one.
	 * 
	 * @param type
	 * 				SymbolicExpression of numeric type
	 */
	@Test
	public void rationalMultiply() {
		NumericExpression n1 = idealFactory.multiply(three, five);
		NumericExpression n2 = idealFactory.multiply(three, zero);
		NumericExpression n3 = idealFactory.multiply(zero, five);
		NumericExpression n4 = idealFactory.multiply(three, one);
		
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
		Polynomial xp1 = (Polynomial) idealFactory.add(x, intOne);
		Polynomial xm1 = (Polynomial) idealFactory.add(x,
				idealFactory.minus(intOne));
		Polynomial xp1xm1 = multiply(xp1, xm1);		
		SymbolicExpression x2m1 = idealFactory.subtract(idealFactory.multiply(x, x),
				idealFactory.multiply(intOne,intOne));
		
		out.println("xp1xm1=" + xp1xm1);
		out.println("x2m1=" + x2m1);
		
		assertEquals(x2m1, xp1xm1);
	}
	
	/**
	 * Multiply various levels of numbers (primitive, monic, poly, etc.) with 
	 * a rational number
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
		Polynomial x2 = (Polynomial) idealFactory.multiply(x, x); //x^2
		Polynomial monic = (Polynomial) idealFactory.multiply(x2, y); //x^2 * y
		Polynomial monomial = (Polynomial) idealFactory.multiply(idealFactory.constant(realThree), 
				monic); //3x^2 * y
		Polynomial polynomial = (Polynomial) idealFactory.add(idealFactory.
				divide(monomial, idealFactory.constant(realThree)), x2); //x^2 * y + x^2
		RationalExpression mulPrimitive = (RationalExpression) 
				idealFactory.multiply(r1, x); //(x*x)/y 
		RationalExpression mulPrimitivePower = multiplyRational(r1, x2); //(x*x^2)/y 
		RationalExpression mulMonic = multiplyRational(r1, monic); //(x^3) 
		RationalExpression mulMonomial = multiplyRational(r1, idealFactory.divide(monomial, 
						idealFactory.constant(realThree))); //(x^3) 
		RationalExpression mulPolynomial = multiplyRational(r1, polynomial); //x^3 + (x^3/y)
		
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