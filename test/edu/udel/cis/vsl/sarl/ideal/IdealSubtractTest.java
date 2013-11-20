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
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;
import edu.udel.cis.vsl.sarl.ideal.IF.RationalExpression;
import edu.udel.cis.vsl.sarl.ideal.common.CommonIdealFactory;
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
 * <li>RationalExpression - Polynomial</li>
 * <li>RationalExpression - Monomial</li>
 * <li>RationalExpression - Monic</li>
 * <li>RationalExpression - Primitive</li>
 * <li>RationalExpression - PrimitivePower</li>
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
	private CollectionFactory collectionFactory;
	private IdealFactory idealFactory;
	private BooleanExpressionFactory booleanFactory;
	private CommonIdealFactory commonIdealFactory;

	private RationalNumber ratZero; // 0
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
	private RationalNumber realThree; // real 3

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
		realThree = numberFactory.rational("3");
		one = commonIdealFactory.constant(realOne);
	}

	@After
	public void tearDown() throws Exception {
		
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
		NumericExpression p1 = idealFactory.add(idealFactory.multiply(x, x), intOne);
		NumericExpression p2 = idealFactory.add(idealFactory.multiply(intTwo, 
				idealFactory.multiply(x, x)), intOne);
		NumericExpression p3 = idealFactory.add(idealFactory.multiply(intThree, 
				idealFactory.multiply(x, x)), intTwo);
		NumericExpression p4 = idealFactory.multiply(x, x);
		Polynomial poly1 = (Polynomial) p2;
		Polynomial poly2 = (Polynomial) p3;
		
		NumericExpression b1 = commonIdealFactory.subtract(p2, p1);
		NumericExpression b2 = commonIdealFactory.subtract(poly2, poly1);
		
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
		NumericExpression p1 = idealFactory.add(idealFactory.multiply(intTwo, 
				idealFactory.multiply(x, x)), intOne);
		NumericExpression p2 = idealFactory.multiply(intTen, x);
		NumericExpression p3 = idealFactory.add(idealFactory.multiply(
				intTen, x), idealFactory.add(idealFactory.multiply(
						intTwo, idealFactory.multiply(x, x)), intOne));
		Polynomial poly1 = (Polynomial) p2;
		Polynomial poly2 = (Polynomial) p3;
		
		NumericExpression b1 = commonIdealFactory.subtract(poly2, poly1);
		
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
		NumericExpression p1 = idealFactory.multiply(intTen, x);
		NumericExpression p2 = idealFactory.multiply(intOne, x);
		NumericExpression p3 = idealFactory.multiply(idealFactory.intConstant(9), x);
		Polynomial poly1 = (Polynomial) p1;
		Polynomial poly2 = (Polynomial) p2;
		
		NumericExpression b1 = commonIdealFactory.subtract(poly1, poly2);
		
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
		NumericExpression p1 = idealFactory.multiply(intTen, idealFactory.multiply(x, y));
		NumericExpression p2 = idealFactory.multiply(x, y);
		NumericExpression p3 = idealFactory.multiply(idealFactory.multiply(idealFactory.
				intConstant(9), x), y);
		Polynomial poly1 = (Polynomial) p1;
		Polynomial poly2 = (Polynomial) p2;
		
		NumericExpression b1 = commonIdealFactory.subtract(poly1, poly2);
		
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
		NumericExpression p1 = idealFactory.multiply(x, idealFactory.multiply(x, y));
		NumericExpression p2 = idealFactory.multiply(x, y);
		NumericExpression p3 = idealFactory.multiply(idealFactory.multiply(x, y), idealFactory.
				subtract(x, intOne));
		Polynomial poly1 = (Polynomial) p1;
		Polynomial poly2 = (Polynomial) p2;
		
		NumericExpression b1 = commonIdealFactory.subtract(poly1, poly2);
		
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
		NumericExpression p1 = idealFactory.multiply(intTen, x);
		NumericExpression p2 = idealFactory.multiply(x, x);
		NumericExpression p3 = idealFactory.subtract(idealFactory.
				multiply(x, x), idealFactory.multiply(intTen, x));
		Polynomial poly1 = (Polynomial) p1;
		Polynomial poly2 = (Polynomial) p2;
		
		NumericExpression b1 = commonIdealFactory.subtract(poly2, poly1);
		
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
		NumericExpression p1 = idealFactory.multiply(x, x);
		NumericExpression p2 = idealFactory.multiply(y, y);
		NumericExpression p3 = idealFactory.multiply(idealFactory.add(x, y), idealFactory.subtract(x, y));
		Polynomial poly1 = (Polynomial) p1;
		Polynomial poly2 = (Polynomial) p2;
		
		NumericExpression b1 = commonIdealFactory.subtract(poly1, poly2);
		
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
		NumericExpression p1 = idealFactory.multiply(x, x);
		NumericExpression p2 = idealFactory.subtract(idealFactory.
				multiply(x, x), intOne);
		Polynomial poly1 = (Polynomial) p1;
		
		NumericExpression b1 = commonIdealFactory.subtract(poly1, intOne);
		
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
		NumericExpression p1 = idealFactory.multiply(x, x);
		NumericExpression p2 = idealFactory.subtract(idealFactory.
				multiply(x, x), x);
		Polynomial poly1 = (Polynomial) p1;
		
		NumericExpression b2 = commonIdealFactory.subtract(poly1, x);
		
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
		Polynomial poly1 = (Polynomial) x;
		
		NumericExpression b2 = commonIdealFactory.subtract(intOne, poly1);
		
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
		NumericExpression subNine = commonIdealFactory.subtract(intTen, intOne);
		Constant nine = commonIdealFactory.intConstant(9);
		
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
		NumericExpression subEleven = commonIdealFactory.subtract(intTen, intNegOne);
		Constant eleven = commonIdealFactory.intConstant(11);
		
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
		NumericExpression n = idealFactory.add(idealFactory.multiply(one, x), intOne);
		NumericExpression m = idealFactory.add(idealFactory.multiply(one, x), intZero);
		NumericExpression o = idealFactory.add(idealFactory.multiply(intTen, 
				idealFactory.multiply(x, x)),(idealFactory.add(
						idealFactory.multiply(intTen, x), intOne)));
		NumericExpression p = idealFactory.add(idealFactory.multiply(intTen, 
				idealFactory.multiply(x, x)), idealFactory.multiply(intTen, x));
		Polynomial poly1 = (Polynomial) n;
		Polynomial poly2 = (Polynomial) m;
		Polynomial poly3 = (Polynomial) o;		
		Polynomial b1 = commonIdealFactory.subtractConstantTerm(poly1);
		Polynomial b2 = commonIdealFactory.subtractConstantTerm(poly2);
		Polynomial b3 = commonIdealFactory.subtractConstantTerm(poly3);
		Polynomial b4 = commonIdealFactory.subtractConstantTerm(constZero);
		
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
		NumericExpression m1 = commonIdealFactory.minus(intZero);
		NumericExpression n1 = commonIdealFactory.minus(p1);
		
		assertEquals(p2, n1);
		assertEquals(intZero, m1);
	}
	
	/**
	 * Subtract various levels of numbers (primitive, monic, poly, etc.) with 
	 * a rational number
	 * 
	 * @return type
	 * 				RationalExpression
	 */
	@Test
	public void subToRational() {
		NumericSymbolicConstant x = objectFactory.canonic(idealFactory
				.symbolicConstant(objectFactory.stringObject("x"),
						typeFactory.realType())); // value 'X' of real type
		NumericSymbolicConstant y = objectFactory.canonic(idealFactory
				.symbolicConstant(objectFactory.stringObject("Y"),
						typeFactory.realType())); // value 'Y' of real type
		
		RationalExpression r1 = (RationalExpression) idealFactory.divide(x, y);	// x/y	
		NumericExpression x2 = idealFactory.multiply(x, x); //x^2
		NumericExpression monic = idealFactory.multiply(x2, y); //x^2 * y
		NumericExpression monomial = idealFactory.multiply(idealFactory.constant(realThree), 
				monic); //3x^2 * y
		NumericExpression polynomial = idealFactory.add(monomial, x2); //3x^2 * y + x^2
		RationalExpression subPrimitive = (RationalExpression) 
				idealFactory.subtract(r1, x); //(x - x*y)/y 
		RationalExpression subPrimitivePower = (RationalExpression) 
				idealFactory.subtract(r1, x2); //(x - x^2*y)/y 
		RationalExpression subMonic = (RationalExpression) 
				idealFactory.subtract(r1, monic); //(x - x^2*y^2)/y 
		RationalExpression subMonomial = (RationalExpression) 
				idealFactory.subtract(r1, monomial); //(x - 3*x^2*y^2)/y 
		RationalExpression subPolynomial = (RationalExpression) 
				idealFactory.subtract(r1, polynomial); //(x - 3*x^2*y^2 - x^2 * y)/y
		
		NumericExpression result1 = idealFactory.divide(idealFactory.
				subtract(x, idealFactory.multiply(x, y)), y); //(x*y + x)/y 
		NumericExpression result2 = idealFactory.divide(idealFactory.
				subtract(x, idealFactory.multiply(x2, y)), y); //(x^2*y + x)/y 
		NumericExpression result3 = idealFactory.divide(idealFactory.
				subtract(x, idealFactory.multiply(monic, y)), y); //(x^2*y^2 + x)/y 
		NumericExpression result4 = idealFactory.divide(idealFactory.
				subtract(x, idealFactory.multiply(monomial, y)), y); //(3*x^2*y^2 + x)/y 
		NumericExpression result5 = idealFactory.divide(idealFactory.
				add(idealFactory.minus(idealFactory.multiply(polynomial, y)),
						x), y); //(3*x^2*y^2 + x^2 * y + x)/y 
		
		assertEquals(result1, subPrimitive);	
		assertEquals(result2, subPrimitivePower);	
		assertEquals(result3, subMonic);	
		assertEquals(result4, subMonomial);
		assertEquals(result5, subPolynomial);
	}
}