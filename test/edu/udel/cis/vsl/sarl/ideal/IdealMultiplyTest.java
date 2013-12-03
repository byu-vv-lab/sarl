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
 * </ul>
 *
 */
public class IdealMultiplyTest {

	private static PrintStream out = System.out;
	private NumberFactory numberFactory;
	private ObjectFactory objectFactory;
	private SymbolicTypeFactory typeFactory;
	private IdealFactory idealFactory;
	
	/**
	 * rational -1/4
	 */
	private RationalNumber ratNegPointTwoFive; 
	/**
	 * rational 3/2
	 */
	private RationalNumber ratOnePointFive;
	/**
	 * real constant 3/2
	 */
	private Constant constOnePointFive;
	/**
	 * real constant -1/4
	 */
	private Constant constNegPointTwoFive;
	/**
	 * int constant 
	 */
	private Constant intZero;
	/**
	 * int constant 1
	 */
	private Constant intOne; 
	/**
	 * int constant 2
	 */
	private Constant intTwo;
	/**
	 * int constant 10
	 */
	private Constant intTen;
	/**
	 * "X"
	 */
	StringObject Xobj; 
	/**
	 * int symbolic constant "X"
	 */
	NumericSymbolicConstant x;
	/**
	 * int symbolic constant "Y"
	 */
	NumericSymbolicConstant y;
	/**
	 * real constant 15
	 */
	private NumericExpression fifteen;
	/**
	 * real constant 5
	 */
	private NumericExpression five; 
	/**
	 * real constant 0
	 */
	private NumericExpression zero; 
	/**
	 * real constant 1
	 */
	private NumericExpression one; 
	/**
	 * real 0
	 */
	private RationalNumber realZero;
	/**
	 * real 1
	 */
	private RationalNumber realOne;
	/**
	 * real 15
	 */
	private RationalNumber realFifteen; 
	/**
	 * real 5
	 */
	private RationalNumber realFive;
	/**
	 * real constant 3
	 */
	private NumericExpression three;
	/**
	 * real 3
	 */
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
	 * a function - add() which adds two polynomials
	 * 
	 * @param a - Polynomial
	 * @param b - Polynomial
	 * 
	 * @return
	 * 			the value of an expression consisting of addition of two polynomials
	 */
	public Polynomial add(Polynomial a, Polynomial b){
		Polynomial p = idealFactory.add(a, b);
		return p;
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
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial p1 = add(multiply(poly1, poly1), intOne);
        Polynomial p2 = add(multiply(intTwo,
                        multiply(poly1, poly1)), intOne);
        Polynomial p3 = multiply(intZero, poly1);
        NumericExpression x2 = multiply(poly1, poly1);
        NumericExpression x4 = idealFactory.multiply(x2, x2);
        NumericExpression p4 = idealFactory.add(multiply(idealFactory.
                intConstant(3), multiply(poly1, poly1)), intOne);
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
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial p1 = add(multiply(poly1, poly1), intOne);
		Polynomial p2 = multiply(intTen, poly1);
		NumericExpression x2 = multiply(poly1, poly1);
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
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial p1 = multiply(intTen, poly1);
		NumericExpression p2 = multiply(idealFactory.intConstant(100), 
				multiply(poly1, poly1));
		
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
		Polynomial poly1 = (Polynomial) x;
		Polynomial poly2 = (Polynomial) y;
		
		Polynomial p1 = multiply(intTen, poly1);
		Polynomial p2 = multiply(poly1, poly2);
		NumericExpression p3 = multiply(multiply(poly1, intTen), 
				multiply(poly1, poly2));
		
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
		Polynomial poly1 = (Polynomial) x;
		Polynomial poly2 = (Polynomial) y;
		
		Polynomial p1 = multiply(multiply(poly1, poly2), poly1);
		Polynomial p2 = multiply(poly1, poly2);
		NumericExpression p3 = multiply(multiply(poly1, 
				multiply(poly1, poly1)), multiply(poly2, poly2));
		
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
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial p1 = multiply(intTen, poly1);
		Polynomial x2 = multiply(poly1, poly1);
		NumericExpression p2 = multiply(intTen, multiply(x2, poly1));
		
		Polynomial b1 = multiply(p1, x2);
		
		assertEquals(p2, b1);
	}
	
	/**
	 * Multiplies a monic with a primitive power by forming the factorization 
	 * and by factoring out the common factors that are produced from the two factorizations.
	 * 
	 * @param p1
	 *            a Monic
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
		Polynomial poly1 = (Polynomial) x;
		Polynomial poly2 = (Polynomial) y;
		
		Polynomial p1 = multiply(poly1, poly2);
		Polynomial x2 = multiply(poly1, poly1);
		NumericExpression p2 = multiply(multiply(poly1, multiply(poly1, 
				poly1)), poly2);
		
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
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial x2 = multiply(poly1, poly1);
		NumericExpression x4 = multiply(x2, x2);
		
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
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial x2 = multiply(poly1, poly1);
		NumericExpression p1 = multiply(poly1, x2);
				
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
		
		Polynomial b1 = multiply(poly1, intTen);
		
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
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial xp1 = add(poly1, intOne);
		Polynomial xm1 = add(poly1, (Polynomial) idealFactory.minus(intOne));
		Polynomial xp1xm1 = multiply(xp1, xm1);		
		SymbolicExpression x2m1 = idealFactory.subtract(multiply(poly1, poly1),
				idealFactory.multiply(intOne,intOne));
		
		out.println("xp1xm1=" + xp1xm1);
		out.println("x2m1=" + x2m1);
		
		assertEquals(x2m1, xp1xm1);
	}
}