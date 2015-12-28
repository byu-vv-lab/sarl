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
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

/**
 * The class IdealDivideTest tests methods found in the edu.udel.cis.vsl.sarl.ideal.common package 
 * using divisions among various combinations such as:
 * 
 * <ul>
 * <li>Polynomial / Polynomial</li>
 * <li>Polynomial / Monomial</li>
 * <li>Monomial / Monomial</li>
 * <li>Monomial / Monic</li>
 * <li>Monomial / PrimitivePower</li>
 * <li>Monomial / Primitive</li>
 * <li>Monimc / Monic</li>
 * <li>Monic / PrimitivePower</li>
 * <li>PrimitivePower / PrimitivePower</li>
 * <li>Primitive / PrimitivePower</li>
 * <li>Constant / Primitive</li>
 * </ul>
 */
public class IdealDivideTest {

	private NumberFactory numberFactory;
	private ObjectFactory objectFactory;
	private SymbolicTypeFactory typeFactory;
	private IdealFactory idealFactory;

	/**
	 * int constant 0
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
	 * int constant 3
	 */
	private Constant intThree; 
	/**
	 * int constant 5
	 */
	private Constant intFive; 
	/**
	 * int constant 10
	 */
	private Constant intTen; 
	/**
	 *  "X"
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
	 * real constant 0
	 */
	private NumericExpression zero;
	/**
	 * real 0
	 */
	private RationalNumber realZero; 
	/**
	 * real 15
	 */
	private RationalNumber realFifteen;
	/**
	 * real constant 5
	 */
	private NumericExpression five;
	/**
	 * real 5
	 */
	private RationalNumber realFive;
	/**
	 * real  constant 3
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
		realFifteen = numberFactory.rational("15");
		zero = idealFactory.constant(realZero);
		fifteen = idealFactory.constant(realFifteen);
		realFive = numberFactory.rational("5");
		five = idealFactory.constant(realFive);
		realThree = numberFactory.rational("3");
		three = idealFactory.constant(realThree);
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	/**
	 * a function - divide() which divides two polynomials
	 * 
	 * @param a - Polynomial
	 * @param b - Polynomial
	 * 
	 * @return
	 * 			the value of an expression consisting of division of two polynomials
	 */
	public Polynomial divide(Polynomial a, Polynomial b){
		Polynomial p = (Polynomial) idealFactory.divide(a, b);
		return p;
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
	 * Divides two polynomials, a polynomial with monic, monomial, 
	 * a monomial with a monic, a monic with a primitivepower and a constant
	 * 
	 * @param type
	 * 				the SymbolicExpression of numeric type of a Polynomial
	 */
	@Test
	public void divide() {
		Polynomial poly1 = (Polynomial) x;
		Polynomial poly2 = (Polynomial) y;
		
		Polynomial n1 = multiply(intTen, poly1);
		Polynomial n2 = multiply(intOne, poly1);
		Polynomial n3 = multiply(intZero, poly1);
		Polynomial n4 = multiply(intOne, intOne);
		NumericExpression n5 = idealFactory.multiply(fifteen, three);
		NumericExpression n6 = idealFactory.multiply(five, three);
		NumericExpression n7 = idealFactory.multiply(zero, three);
		Polynomial n = add(poly1, poly2);
		NumericExpression m = idealFactory.subtract(x, y);
		NumericExpression np = idealFactory.divide(n, m);		
		Polynomial b1 = divide(n1, n2);
		Polynomial b2 = divide(n3, n2);
		Polynomial b3 = divide(n1, n4);
		NumericExpression b4 = idealFactory.divide(n5, n6);
		NumericExpression b5 = idealFactory.divide(n7, n6);
		NumericExpression p1 = idealFactory.divide(n, m);
		
		assertEquals(np, p1);
		assertEquals(intTen, b1);
		assertEquals(intZero, b2);
		assertEquals(n1, b3);
		assertEquals(three, b4);
		assertEquals(zero, b5);
	}
	
	/**
	 * Divides two polynomials by forming the factorization and by factoring 
	 * out the common factors that are produced from the two factorizations.
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
	 * 				a polynomial of type Polynomial which is the division of 
	 * 				two polynomials (passed as arguments).
	 */
	@Test
	public void dividePolyByPoly() {
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial p01 = add(intTen, multiply(intTen, poly1));
		Polynomial p02 = add(intTwo, multiply(intTwo, poly1));
		
		Polynomial b1 = divide(p01, p02);
		
		assertEquals(intFive, b1);
	}
	
	/**
	 * Divides a polynomial with a monomial by forming the factorization and 
	 * by factoring out the common factors that are produced from the two factorizations.
	 * 
	 * @param p1
	 *            a Polynomial
	 * @param p2
	 *            a Monomial
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a polynomial of type Polynomial which is the division of 
	 * 				a polynomial with a monomial (passed as arguments).
	 */
	@Test
	public void dividePolyToMonomial() {
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial p01 = multiply(poly1, poly1);
		Polynomial p02 = multiply(intTen, poly1);
		Polynomial p03 = add(p02, multiply(intTen, p01));
		NumericExpression p04 = add(poly1, intOne);
		
		Polynomial b1 = divide(p03, p02);
		
		assertEquals(p04, b1);
	}
	
	/**
	 * Divides two monomials by forming the factorization 
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
	 * 				a polynomial of type Polynomial which is the division of 
	 * 				two monomials (passed as arguments).
	 */
	@Test
	public void divideMonomialToMonomial() {
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial p01 = multiply(intTen, poly1);
		Polynomial p02 = multiply(intTwo, poly1);
		
		Polynomial b1 = divide(p01, p02);
		
		assertEquals(intFive, b1);
	}
	
	/**
	 * Divides a monomial with a monic by forming the factorization 
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
	 * 				a polynomial of type Polynomial which is the division of 
	 * 				a monomial with a monic (passed as arguments).
	 */
	@Test
	public void divideMonomialToMonic() {
		Polynomial poly1 = (Polynomial) x;
		Polynomial poly2 = (Polynomial) y;
		
		Polynomial p01 = multiply(intTen, multiply(poly1, poly2));
		Polynomial p02 = multiply(poly1, poly2);
		
		Polynomial b1 = divide(p01, p02);
		
		assertEquals(intTen, b1);
	}
	
	/**
	 * Divides two monics by forming the factorization 
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
	 * 				a polynomial of type Polynomial which is the division of 
	 * 				two monics (passed as arguments).
	 */
	@Test
	public void divideMonicToMonic() {
		Polynomial poly1 = (Polynomial) x;
		Polynomial poly2 = (Polynomial) y;
		
		Polynomial p01 = multiply(poly1, multiply(poly1, poly2));
		Polynomial p02 = multiply(poly1, poly2);
				
		Polynomial b1 = divide(p01, p02);
		
		assertEquals(x, b1);
	}
	
	/**
	 * Divides a monomial with a primitive power by forming the factorization 
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
	 * 				a polynomial of type Polynomial which is the division of 
	 * 				a monomial with a primitive power (passed as arguments).
	 */
	@Test
	public void divideMonomialToPrimitivePower() {
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial p01 = multiply(poly1, poly1);
		Polynomial p02 = multiply(intTen, poly1);
		NumericExpression p03 = divide(intTen, poly1);
		
		Polynomial b1 = divide(p02, p01);
		
		assertEquals(p03, b1);
	}
	
	/**
	 * Divides a monomial with a primitive power by forming the factorization 
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
	 * 				a polynomial of type Polynomial which is the division of 
	 * 				a monomial with a primitive power (passed as arguments).
	 */
	@Test
	public void divideMonicToPrimitivePower() {
		Polynomial poly1 = (Polynomial) x;
		Polynomial poly2 = (Polynomial) y;
		
		Polynomial p01 = multiply(poly1, poly1);
		Polynomial p02 = multiply(multiply(poly1, poly1), poly2);
		
		Polynomial b1 = divide(p02, p01);
		
		assertEquals(y, b1);
	}
	
	/**
	 * Divides a monomial with a primitive by forming the factorization and 
	 * by factoring out the common factors that are produced from the two factorizations.
	 * 
	 * @param p1
	 *            a Monomial
	 * @param p2
	 *            a Primitive
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a polynomial of type Polynomial which is the division of 
	 * 				a monomial with a primitive (passed as arguments).
	 */
	@Test
	public void divideMonomialToPrimitive() {
		Polynomial poly1 = (Polynomial) x;
		Polynomial p01 = multiply(intTen, poly1);
		
		Polynomial b1 = divide(p01, poly1);
		
		assertEquals(intTen, b1);
	}
	
	/**
	 * Divides two primitive powers by forming the factorization and by 
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
	 * 				a polynomial of type Polynomial which is the division of 
	 * 				two primitive powers (passed as arguments).
	 */
	@Test
	public void dividePrimitivePowerToItself() {
		Polynomial poly1 = (Polynomial) x;
		Polynomial p01 = multiply(poly1, poly1);
		
		Polynomial b1 = divide(p01, p01);
		
		assertEquals(intOne, b1);
	}
	
	/**
	 * Divides a primitive with a constant by forming the factorization and 
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
	 * 				a polynomial of type Polynomial which is the division of 
	 * 				a primitive with a constant (passed as arguments).
	 */
	@Test
	public void dividePrimitiveToConstant() {
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial p01 = multiply(poly1, poly1);
		Polynomial p02 = multiply(intTen, poly1);
		NumericExpression p03 = divide(p01, p02);
		
		Polynomial b3 = divide(poly1, intTen);
		
		assertEquals(p03, b3);
	}
	
	/**
	 * Divides two polynomials by removing the common factors between them
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
	 * 				a polynomial of type Polynomial by removing the common factors.
	 */
	@Test
	public void factorization() {
		StringObject Zobj;
		Zobj = objectFactory.stringObject("Z"); // string object 'Z'
		NumericSymbolicConstant z = objectFactory.canonic(idealFactory.symbolicConstant(Zobj,
				typeFactory.integerType()));
		Polynomial poly1 = (Polynomial) x;
		Polynomial poly2 = (Polynomial) y;
		Polynomial poly3 = (Polynomial) z;
		
		Polynomial p01 = multiply(multiply((Polynomial) idealFactory.
				subtract(poly1, intOne),add(poly1, intOne)), add(multiply(
						poly1, poly2), intTwo));
		Polynomial p02 = multiply(multiply((Polynomial) idealFactory.
				subtract(poly1, intOne), poly3), add(multiply(poly1, 
						poly2), intThree));
		Polynomial p03 = multiply(add(poly1, intOne), add(multiply(
				poly1, poly2), intTwo));
		Polynomial p04 = multiply(poly3, add(multiply(poly1, poly2), 
				intThree));
		NumericExpression p05 = divide(p03, p04);
		
		Polynomial b1 = divide(p01, p02);
		
		assertEquals(p05, b1);
	}
}