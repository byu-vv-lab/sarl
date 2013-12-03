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
 * The class IdealSubtractTest tests methods found in the edu.udel.cis.vsl.sarl.ideal.common package 
 * using subtractions among various combinations such as:
 * 
 * <ul>
 * <li>Polynomial - Polynomial</li>
 * <li>Polynomial - Monomial</li>
 * <li>Monomial - Monomial</li>
 * <li>Monomial - Monic</li>
 * <li>PrimitivePower - Monomial</li>
 * <li>Monimc - Monic</li>
 * <li>Monic - PrimitivePower</li>
 * <li>PrimitivePower - PrimitivePower</li>
 * <li>PrimitivePower - Primitive</li>
 * <li>PrimitivePower - Constant</li>
 * <li>Constant - Primitive</li>
 * </ul>
 * 
 * This class also contains the following methods:
 * 
 * <ul>
 * <li>Primitive Subtract</li>
 * <li>Primitive Negative Subtract</li>
 * <li>minus</li>
 * <li>Constant term subtraction in the given expression</li>
 * </ul>
 *
 */
public class IdealSubtractTest {

	private static PrintStream out = System.out;
	private NumberFactory numberFactory;
	private ObjectFactory objectFactory;
	private SymbolicTypeFactory typeFactory;
	private IdealFactory idealFactory;

	private RationalNumber ratZero; // rational 0
	private Constant constZero; // real constant 0
	private Constant intNegOne; // int constant -1
	private Constant intZero; // int constant 0
	private Constant intOne; // int constant 1
	private Constant intTwo; // int constant 2
	private Constant intThree; // int constant 3
	private Constant intTen; // int constant 10
	StringObject Xobj; // "X"
	NumericSymbolicConstant x; // int symbolic constant "X"
	NumericSymbolicConstant y; // int symbolic constant "Y"
	private NumericExpression one; // real constant 1
	private RationalNumber realOne; // real 1
	
	@Before
	public void setUp() throws Exception {
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		numberFactory = system.numberFactory();
		objectFactory = system.objectFactory();
		typeFactory = system.typeFactory();
		idealFactory = (IdealFactory) system.numericFactory();
		ratZero = numberFactory.rational("0");
		constZero = idealFactory.constant(ratZero);
		intZero = idealFactory.intConstant(0);
		intNegOne = idealFactory.intConstant(-1);
		intOne = idealFactory.intConstant(1);
		intTwo = idealFactory.intConstant(2);
		intThree = idealFactory.intConstant(3);
		intTen = idealFactory.intConstant(10);
		Xobj = objectFactory.stringObject("X");
		x = objectFactory.canonic(idealFactory.symbolicConstant(Xobj,
				typeFactory.integerType()));
		y = objectFactory.canonic(idealFactory.symbolicConstant(
				objectFactory.stringObject("Y"), typeFactory.integerType()));
		realOne = numberFactory.rational("1");
		one = idealFactory.constant(realOne);
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	/**
	 * a function - sub() which subtracts two polynomials
	 * 
	 * @param a - Polynomial
	 * @param b - Polynomial
	 * 
	 * @return
	 * 			the value of an expression consisting of subtraction of two polynomials
	 */
	public Polynomial sub(Polynomial a, Polynomial b){
		Polynomial p = (Polynomial) idealFactory.subtract(a, b);
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
	 * Subtracts two polynomials by forming the factorization and by factoring 
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
	 * 				a polynomial of type Polynomial which is the subtraction 
	 * 				of two polynomials (passed as arguments).
	 */
	@Test
	public void subPolyToPoly() {
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial p1 = add(multiply(poly1, poly1), intOne);
		Polynomial p2 = add(multiply(intTwo, multiply(poly1, poly1)), intOne);
		Polynomial p3 = add(multiply(intThree, 
				multiply(poly1, poly1)), intTwo);
		NumericExpression p4 = multiply(poly1, poly1);
		
		Polynomial b1 = sub(p2, p1);
		Polynomial b2 = sub(p3, p2);
		
		assertEquals(p4, b1);
		assertEquals(p1, b2);
	}
	
	/**
	 * Subtracts a polynomial with a monomial by forming the factorization and 
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
	 * 				a polynomial of type Polynomial which is the subtraction 
	 * 				of a polynomial with a monomial (passed as arguments).
	 */
	@Test
	public void subPolyToMonomial() {
		Polynomial poly1 = (Polynomial) x;
		
		NumericExpression p1 = add(multiply(intTwo, multiply(poly1, poly1)), intOne);
		Polynomial p2 = multiply(intTen, poly1);
		Polynomial p3 = add(multiply(intTen, poly1), add(multiply(
						intTwo, multiply(poly1, poly1)), intOne));
		
		Polynomial b1 = sub(p3, p2);
		
		assertEquals(p1, b1);
	}
	
	/**
	 * Subtracts two monomials by forming the factorization and by factoring 
	 * out the common factors that are produced from the two factorizations.
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
	 * 				a polynomial of type Polynomial which is the subtraction 
	 * 				of two monomials (passed as arguments).
	 */
	@Test
	public void subMonomialToMonomial() {
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial p1 = multiply(intTen, poly1);
		Polynomial p2 = multiply(intOne, poly1);
		NumericExpression p3 = multiply(idealFactory.intConstant(9), poly1);
		
		Polynomial b1 = sub(p1, p2);
		
		assertEquals(p3, b1);
	}
	
	/**
	 * Subtracts a monomial with a monic by forming the factorization and by factoring 
	 * out the common factors that are produced from the two factorizations.
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
	 * 				a polynomial of type Polynomial which is the subtraction 
	 * 				of a monomial with a monic (passed as arguments).
	 */
	@Test
	public void subMonomialToMonic() {
		Polynomial poly1 = (Polynomial) x;
		Polynomial poly2 = (Polynomial) y;
		
		Polynomial p1 = multiply(intTen, multiply(poly1, poly2));
		Polynomial p2 = multiply(poly1, poly2);
		NumericExpression p3 = multiply(multiply(idealFactory.
				intConstant(9), poly1), poly2);
		
		Polynomial b1 = sub(p1, p2);
		
		assertEquals(p3, b1);
	}
	
	/**
	 * Subtracts two monics by forming the factorization and by factoring 
	 * out the common factors that are produced from the two factorizations.
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
	 * 				a polynomial of type Polynomial which is the subtraction 
	 * 				of two monics (passed as arguments).
	 */
	@Test
	public void subMonicToMonic() {
		Polynomial poly1 = (Polynomial) x;
		Polynomial poly2 = (Polynomial) y;
		
		Polynomial p1 = multiply(poly1, multiply(poly1, poly2));
		Polynomial p2 = multiply(poly1, poly2);
		NumericExpression p3 = idealFactory.multiply(multiply(poly1, poly2), idealFactory.
				subtract(poly1, intOne));
		
		Polynomial b1 = sub(p1, p2);
		
		assertEquals(p3, b1);
	}
	
	/**
	 * Subtracts a primitive power with a monomial by forming the factorization 
	 * and by factoring out the common factors that are produced from the two factorizations.
	 * 
	 * @param p1
	 *            a PrimitivePower
	 * @param p2
	 *            a Monomial
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a polynomial of type Polynomial which is the subtraction 
	 * 				of a primitive power with a monomial (passed as arguments).
	 */
	@Test
	public void subPrimitivePowerToMonomial() {
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial p1 = multiply(intTen, poly1);
		Polynomial p2 = multiply(poly1, poly1);
		NumericExpression p3 = idealFactory.subtract(multiply(poly1, poly1), multiply(intTen, poly1));
		
		Polynomial b1 = sub(p2, p1);
		
		assertEquals(p3, b1);
	}
	
	/**
	 * Subtracts a monic with a primitive power by forming the factorization and by 
	 * factoring out the common factors that are produced from the two factorizations.
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
	 * 				a polynomial of type Polynomial which is the subtraction 
	 * 				of a monic with a primitive power (passed as arguments).
	 */
	@Test
	public void subMonicToPrimitivePower() {
		Polynomial poly1 = (Polynomial) x;
		Polynomial poly2 = (Polynomial) y;
		
		Polynomial p1 = multiply(poly1, poly2);
		Polynomial p2 = multiply(poly2, poly2);
		NumericExpression p3 = idealFactory.multiply(poly2, idealFactory.subtract(poly1, poly2));
		
		Polynomial b1 = sub(p1, p2);
		
		assertEquals(p3, b1);
	}
	
	/**
	 * Subtracts two primitive powers by forming the factorization and by 
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
	 * 				a polynomial of type Polynomial which is the subtraction 
	 * 				of two primitive powers (passed as arguments).
	 */
	@Test
	public void subPrimitivePowerToItself() {
		Polynomial poly1 = (Polynomial) x;
		Polynomial poly2 = (Polynomial) y;
		
		Polynomial p1 = multiply(poly1, poly1);
		Polynomial p2 = multiply(poly2, poly2);
		NumericExpression p3 = idealFactory.multiply(add(poly1, poly2), idealFactory.
				subtract(poly1, poly2));
		
		Polynomial b1 = sub(p1, p2);
		
		assertEquals(p3, b1);
	}
	
	/**
	 * Subtracts a primitive power with a constant by forming the factorization 
	 * and by factoring out the common factors that are produced from the two factorizations.
	 * 
	 * @param p1
	 *            a PrimitivePower
	 * @param p2
	 *            a Constant
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a polynomial of type Polynomial which is the subtraction 
	 * 				of a primitive power with a constant (passed as arguments).
	 */
	@Test
	public void subPrimitivePowerToConstant() {
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial p1 = multiply(poly1, poly1);
		NumericExpression p2 = idealFactory.subtract(multiply(poly1, poly1), intOne);
		
		NumericExpression b1 = idealFactory.subtract(p1, intOne);
		
		assertEquals(p2, b1);
	}
	
	/**
	 * Subtracts a primitive power with a primitive by forming the factorization 
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
	 * 				a polynomial of type Polynomial which is the subtraction 
	 * 				of a primitive power with a primitive (passed as arguments).
	 */
	@Test
	public void subPrimitivePowerToPrimitive() {
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial p1 = multiply(poly1, poly1);
		NumericExpression p2 = idealFactory.subtract(multiply(poly1, poly1), poly1);
		
		Polynomial b2 = sub(p1, poly1);
		
		assertEquals(p2, b2);
	}
	
	/**
	 * Subtracts a constant with a primitive by forming the factorization 
	 * and by factoring out the common factors that are produced from the two factorizations.
	 * 
	 * @param p1
	 *            a Constant
	 * @param p2
	 *            a Primitive
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a polynomial of type Polynomial which is the subtraction 
	 * 				of a constant with a primitive (passed as arguments).
	 */
	@Test
	public void subConstantToPrimitive() {
		NumericExpression p1 = idealFactory.subtract(intOne, x);
				
		NumericExpression b2 = idealFactory.subtract(intOne, x);
		
		assertEquals(p1, b2);
	}
	
	/**
	 * Returns the subtraction of symbolic expression of same numeric type
	 * 
	 * @param type
	 * 				Symbolic Expression of Numeric type
	 */
	@Test
	public void primitiveSubtract() {
		NumericExpression subNine = idealFactory.subtract(intTen, intOne);
		Constant nine = idealFactory.intConstant(9);
		
		assertEquals(subNine, nine);
	}

	/**
	 * Returns the subtraction of symbolic expression of same numeric type
	 * 
	 * @param type
	 * 				Symbolic Expressions of same Numeric type
	 */
	@Test
	public void primitiveNegSubtract() {
		NumericExpression subEleven = idealFactory.subtract(intTen, intNegOne);
		Constant eleven = idealFactory.intConstant(11);
		
		assertEquals(subEleven, eleven);
	}
	
	/**
	 * Returns the expression by removing the constant term present in the 
	 * passed argument.
	 * 
	 * @param type
	 * 				Polynomial
	 */
	@Test
	public void constantTermSubtraction() {
		Polynomial n = (Polynomial) idealFactory.add(idealFactory.multiply(one, x), intOne);
		Polynomial m = (Polynomial) idealFactory.add(idealFactory.multiply(one, x), intZero);
		Polynomial o = (Polynomial) idealFactory.add(idealFactory.multiply(intTen, 
				idealFactory.multiply(x, x)),(idealFactory.add(
						idealFactory.multiply(intTen, x), intOne)));
		NumericExpression p = idealFactory.add(idealFactory.multiply(intTen, 
				idealFactory.multiply(x, x)), idealFactory.multiply(intTen, x));
				
		Polynomial b1 = idealFactory.subtractConstantTerm(n);
		Polynomial b2 = idealFactory.subtractConstantTerm(m);
		Polynomial b3 = idealFactory.subtractConstantTerm(o);
		Polynomial b4 = idealFactory.subtractConstantTerm(constZero);
		out.println("Constant Term Subtraction1=" + b3);
		
		assertEquals(x, b1);
		assertEquals(x, b2);
		assertEquals(p, b3);
		assertEquals(constZero, b4);
	}

	/**
	 * Returns the negation for the given argument
	 * 
	 * @param type
	 * 				SymbolicExpression of integer or real type
	 * 
	 */
	@Test
	public void minus() {
		NumericExpression p1 = idealFactory.subtract(idealFactory.multiply(x, x),intOne);
		NumericExpression p2 = idealFactory.subtract(intOne, idealFactory.multiply(x, x));
		NumericExpression m1 = idealFactory.minus(intZero);
		NumericExpression n1 = idealFactory.minus(p1);
		
		assertEquals(p2, n1);
		assertEquals(intZero, m1);
	}
}