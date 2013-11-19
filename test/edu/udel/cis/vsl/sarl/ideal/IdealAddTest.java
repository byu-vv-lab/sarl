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
 * The class IdealAddTest tests methods found in the edu.udel.cis.vsl.sarl.ideal.common package 
 * using additions among various combinations such as:
 * 
 * <ul>
 * <li>Polynomial + Polynomial</li>
 * <li>Polynomial + Monomial</li>
 * <li>Monomial + Monomial</li>
 * <li>Monomial + Monic</li>
 * <li>Monimc + Monic</li>
 * <li>Monic + PrimitivePower</li>
 * <li>PrimitivePower + PrimitivePower</li>
 * <li>Primitive + PrimitivePower</li>
 * <li>Constant + Primitive</li>
 * <li>RationalExpression + Polynomial</li>
 * <li>RationalExpression + Monomial</li>
 * <li>RationalExpression + Monic</li>
 * <li>RationalExpression + Primitive</li>
 * <li>RationalExpression + PrimitivePower</li>
 * </ul>
 * 
 */
public class IdealAddTest {

	private NumberFactory numberFactory;
	private ObjectFactory objectFactory;
	private SymbolicTypeFactory typeFactory;
	private CollectionFactory collectionFactory;
	private IdealFactory idealFactory;
	private BooleanExpressionFactory booleanFactory;
	private CommonIdealFactory commonIdealFactory;

	private RationalNumber ratNegPointTwoFive; // -0.25 (-1/4)
	private RationalNumber ratOnePointFive; // 1.5 (3/2)
	private RationalNumber ratOnePointTwoFive; // 1.25 (5/4)
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
		realThree = numberFactory.rational("3");
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	/**
	 * Adds two constants of real type.
	 * 
	 * @param type
	 * 				Real Constants
	 */
	@Test
	public void constantAdd() {
		Constant c3 = (Constant) idealFactory.add(constOnePointFive, constNegPointTwoFive);
				
		assertEquals(ratOnePointTwoFive, c3.number());
	}
	
	/**
	 * Shows that the commutative property holds for two Numeric Symbolic 
	 * Constants
	 * 
	 * @param type
	 * 				NumericSymbolicConstant
	 */
	@Test
	public void commutativity1() {
		SymbolicExpression xpy = idealFactory.add(x, y); // x + y
		SymbolicExpression ypx = idealFactory.add(y, x); // y + x
		
		assertEquals(xpy, ypx);
	}
	
	/**
	 * Adds two polynomials by forming the factorization and by factoring out 
	 * the common factors that are produced from the two factorizations.
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
	 * 				a polynomial of type Polynomial which is the sum of two 
	 * 				polynomials (passed as arguments).
	 */
	@Test
	public void addPolyToPoly(){
		NumericExpression p1 = idealFactory.add(idealFactory.multiply(x, x), intOne);
		NumericExpression p2 = idealFactory.add(idealFactory.multiply(intTwo, 
				idealFactory.multiply(x, x)), intOne);
		NumericExpression p3 = idealFactory.multiply(intZero, x);
		NumericExpression p4 = idealFactory.add(idealFactory.multiply(intThree, 
				idealFactory.multiply(x, x)), intTwo);
		Polynomial poly1 = (Polynomial) p1;
		Polynomial poly2 = (Polynomial) p2;
		Polynomial poly3 = (Polynomial) p3;
		
		Polynomial b1 = commonIdealFactory.add(poly1, poly2);
		Polynomial b2 = commonIdealFactory.add(poly3, poly2);
		
		assertEquals(p4, b1);
		assertEquals(p2, b2);
	}
	
	/**
	 * Adds a monomial and a polynomial by forming the factorization and by 
	 * factoring out the common factors that are produced from the two factorizations.
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
	 * 				a polynomial of type Polynomial which is the sum of a 
	 * 				monomial and a polynomial (passed as arguments).
	 */
	@Test
	public void addPolyToMonomial() {
		NumericExpression p1 = idealFactory.add(idealFactory.multiply(intTwo, 
				idealFactory.multiply(x, x)), intOne);
		NumericExpression p2 = idealFactory.multiply(intTen, x);
		NumericExpression p3 = idealFactory.add(idealFactory.multiply(
				intTen, x), idealFactory.add(idealFactory.multiply(
						intTwo, idealFactory.multiply(x, x)), intOne));
		Polynomial poly1 = (Polynomial) p1;
		Polynomial poly2 = (Polynomial) p2;
		
		Polynomial b1 = commonIdealFactory.add(poly1, poly2);
		
		assertEquals(p3, b1);
	}
	
	/**
	 * Adds a monomial and a monic by forming the factorization and by 
	 * factoring out the common factors that are produced from the two factorizations.
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
	 * 				a polynomial of type Polynomial which is the sum of a 
	 * 				monomial and a monic (passed as arguments).
	 */
	@Test
	public void addMonomialToMonic() {
		NumericExpression p1 = idealFactory.multiply(intTen, x);
		NumericExpression p2 = idealFactory.multiply(x, y);
		NumericExpression p3 = idealFactory.multiply(x, idealFactory.
				add(intTen, y));
		Polynomial poly1 = (Polynomial) p1;
		Polynomial poly2 = (Polynomial) p2;
		
		Polynomial b1 = commonIdealFactory.add(poly1, poly2);
		
		assertEquals(p3, b1);
	}
	
	/**
	 * Adds two monics by forming the factorization and by factoring out the 
	 * common factors that are produced from the two factorizations.
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
	 * 				a polynomial of type Polynomial which is the sum of two 
	 * 				monics (passed as arguments).
	 */
	@Test
	public void addMonicToMonic() {
		NumericExpression p1 = idealFactory.multiply(x, y);
		NumericExpression p2 = idealFactory.multiply(intTwo, 
				idealFactory.multiply(x, y));
		Polynomial poly1 = (Polynomial) p1;
		
		Polynomial b1 = commonIdealFactory.add(poly1, poly1);
		
		assertEquals(p2, b1);
	}
	
	/**
	 * Adds a primitive and a monic by forming the factorization and by 
	 * factoring out the common factors that are produced from the two factorizations.
	 * 
	 * @param p1
	 *            a Monic
	 * @param p2
	 *            a Primitive
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a polynomial of type Polynomial which is the sum of a 
	 * 				monic and a primitive (passed as arguments).
	 */
	@Test
	public void addPrimitiveToMonic() {
		NumericExpression p1 = idealFactory.multiply(x, y);
		NumericExpression p2 = idealFactory.multiply(idealFactory.
				add(intOne, y), x);
		Polynomial poly1 = (Polynomial) p1;
		Polynomial poly2 = (Polynomial) x;
		
		Polynomial b1 = commonIdealFactory.add(poly1, poly2);
		
		assertEquals(p2, b1);
	}
	
	/**
	 * Adds a constant to a monic by forming the factorization and by 
	 * factoring out the common factors that are produced from the two factorizations.
	 * 
	 * @param p1
	 *            a Monic
	 * @param p2
	 *            a Constant
	 * 
	 * @param type
	 * 				Polynomial and an Integer Constant
	 * 
	 * @return
	 * 				a polynomial of type Polynomial which is the sum of a 
	 * 				constant and a monic (passed as arguments).
	 */
	@Test
	public void addConstantToMonic() {
		NumericExpression p1 = idealFactory.multiply(x, y);
		NumericExpression p2 = idealFactory.add(idealFactory.
				multiply(x, y), intOne);
		Polynomial poly1 = (Polynomial) p1;
		
		Polynomial b1 = commonIdealFactory.add(poly1, intOne);
		
		assertEquals(p2, b1);
	}
	
	/**
	 * Adds a polynomial with a primitive power by forming the factorization 
	 * and by factoring out the common factors that are produced from the two factorizations.
	 * 
	 * @param p1
	 *            a PrimitivePower
	 * @param p2
	 *            a Polynomial
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a polynomial of type Polynomial which is the sum of a 
	 * 				polynomial and a primitive power (passed as arguments).
	 */
	@Test
	public void addPolyToPrimitivePower() {
		NumericExpression p1 = idealFactory.multiply(x, x);
		NumericExpression p2 = idealFactory.multiply(idealFactory.
				multiply(x, x), intTwo);
		NumericExpression p3 = idealFactory.multiply(idealFactory.
				multiply(x, x), intThree);
		Polynomial poly1 = (Polynomial) p1;
		Polynomial poly2 = (Polynomial) p2;
		
		Polynomial b1 = commonIdealFactory.add(poly1, poly2);
		
		assertEquals(p3, b1);
	}
	
	/**
	 * Adds two primitive powers by forming the factorization and by factoring 
	 * out the common factors that are produced from the two factorizations.
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
	 * 				a polynomial of type Polynomial which is the sum of two 
	 * 				primitive powers (passed as arguments).
	 */
	@Test
	public void addPrimitivePowerToItself() {
		NumericExpression p1 = idealFactory.multiply(x, x);
		NumericExpression p2 = idealFactory.multiply(idealFactory.
				multiply(x, x), intTwo);
		Polynomial poly1 = (Polynomial) p1;
				
		Polynomial b1 = commonIdealFactory.add(poly1, poly1);
		
		assertEquals(p2, b1);
	}
	
	/**
	 * Adds a primitive power with a primitive by forming the factorization 
	 * and by factoring out the common factors that are produced from the two factorizations.
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
	 * 				a polynomial of type Polynomial which is the sum of a 
	 * 				primitive power and a primitive (passed as arguments).
	 */
	@Test
	public void addPrimitivePowerToPrimitive() {
		NumericExpression p1 = idealFactory.multiply(x, x);
		NumericExpression p2 = idealFactory.add(x, idealFactory.
				multiply(x, x));
		Polynomial poly1 = (Polynomial) p1;
		Polynomial poly2 = (Polynomial) x;
		
		Polynomial b1 = commonIdealFactory.add(poly1, poly2);
		
		assertEquals(p2, b1);
	}
	
	/**
	 * Adds a primitive power with a constant by forming the factorization 
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
	 * 				a polynomial of type Polynomial which is the sum of a 
	 * 				primitive power and a constant (passed as arguments).
	 */
	@Test
	public void addPrimitiveToConstant() {
		NumericExpression p1 = idealFactory.add(x, intOne);
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial b1 = commonIdealFactory.add(poly1, intOne);
		
		assertEquals(p1, b1);
	}
	
	/**
	 * Adds various levels of numbers (primitive, monic, poly, etc.) with a 
	 * rational number
	 * 
	 * @return type
	 * 				RationalExpression
	 */
	@Test
	public void addToRational() {
		NumericSymbolicConstant x = objectFactory.canonic(idealFactory
				.symbolicConstant(objectFactory.stringObject("x"),
						typeFactory.realType())); // value 'X' of type real
		NumericSymbolicConstant y = objectFactory.canonic(idealFactory
				.symbolicConstant(objectFactory.stringObject("Y"),
						typeFactory.realType())); // value 'Y' of type real
		
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
		
		NumericExpression result1 = idealFactory.divide(idealFactory.
				add(idealFactory.multiply(x, y), x), y); //(x*y + x)/y 
		NumericExpression result2 = idealFactory.divide(idealFactory.
				add(idealFactory.multiply(x2, y), x), y); //(x^2*y + x)/y 
		NumericExpression result3 = idealFactory.divide(idealFactory.
				add(idealFactory.multiply(monic, y), x), y); //(x^2*y^2 + x)/y 
		NumericExpression result4 = idealFactory.divide(idealFactory.
				add(idealFactory.multiply(monomial, y), x), y); //(3*x^2*y^2 + x)/y 
		NumericExpression result5 = idealFactory.divide(idealFactory.
				add(idealFactory.multiply(polynomial, y), x), y); //(3*x^2*y^2 + x^2 * y + x)/y 
		
		assertEquals(result1, plusPrimitive);	
		assertEquals(result2, plusPrimitivePower);	
		assertEquals(result3, plusMonic);	
		assertEquals(result4, plusMonomial);
		assertEquals(result5, plusPolynomial);
	}
}