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
 * The class IdealAddTest tests methods found in the edu.udel.cis.vsl.sarl.ideal.common package 
 * using additions among various combinations such as:
 * 
 * <ul>
 * <li>Polynomial + Polynomial</li>
 * <li>Polynomial + Monomial</li>
 * <li>Polynomial + PrimitvePower</li>
 * <li>Monomial + Monomial</li>
 * <li>Monomial + PrimitivePower</li>
 * <li>Monomial + Monic</li>
 * <li>Monic + Monic</li>
 * <li>Monic + PrimitvePower</li>
 * <li>Monic + Primitve</li>
 * <li>Monic + Constant</li>
 * <li>Monic + PrimitivePower</li>
 * <li>PrimitivePower + PrimitivePower</li>
 * <li>Primitive + PrimitivePower</li>
 * <li>Constant + Primitive</li>
 * </ul>
 */
public class IdealAddTest {

	private NumberFactory numberFactory;
	private ObjectFactory objectFactory;
	private SymbolicTypeFactory typeFactory;
	private IdealFactory idealFactory;

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
			
	@Before
	public void setUp() throws Exception {
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		numberFactory = system.numberFactory();
		objectFactory = system.objectFactory();
		typeFactory = system.typeFactory();
		idealFactory = (IdealFactory) system.numericFactory();
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
	}

	@After
	public void tearDown() throws Exception {
		
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
		Polynomial poly1 = (Polynomial) x;
		Polynomial poly2 = (Polynomial) y;
		
		SymbolicExpression xpy = add(poly1, poly2); // x + y
		SymbolicExpression ypx = add(poly2, poly1); // y + x
		
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
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial p1 = add(multiply(poly1, poly1), intOne);
		Polynomial p2 = add(multiply(intTwo, multiply(poly1, poly1)), intOne);
		Polynomial p3 = multiply(intZero, poly1);
		Polynomial p4 = add(multiply(intThree, multiply(poly1, poly1)), intTwo);
		
		Polynomial b1 = add(p1, p2);
		Polynomial b2 = add(p3, p2);
		
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
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial p1 = add(multiply(intTwo, multiply(poly1, poly1)), intOne);
		Polynomial p2 = multiply(intTen, poly1);
		Polynomial p3 = add(multiply(intTen, poly1), add(multiply(
						intTwo, multiply(poly1, poly1)), intOne));
				
		Polynomial b1 = add(p1, p2);
		
		assertEquals(p3, b1);
	}
	
	/**
	 * Adds two monomials by forming the factorization and by 
	 * factoring out the common factors that are produced from the two factorizations.
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
	 * 				a polynomial of type Polynomial which is the sum of two 
	 * 				monomials (passed as arguments).
	 */
	@Test
	public void addMonomialToMonomial() {
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial p1 = multiply(intTen, poly1);
		Polynomial p2 = multiply(idealFactory.intConstant(20), poly1);
				
		Polynomial b1 = add(p1, p1);
		
		assertEquals(p2, b1);
	}
	
	/**
	 * Adds a monomial and a primitive power by forming the factorization and by 
	 * factoring out the common factors that are produced from the two factorizations.
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
	 * 				a polynomial of type Polynomial which is the sum of a 
	 * 				monomial and a primitive power (passed as arguments).
	 */
	@Test
	public void addMonomialToPrimitivePower() {
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial p1 = multiply(intTen, poly1);
		Polynomial p2 = multiply(poly1, poly1);
		Polynomial p3 = multiply(poly1, add(intTen, poly1));
		
		Polynomial b1 = add(p1, p2);
		
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
		Polynomial poly1 = (Polynomial) x;
		Polynomial poly2 = (Polynomial) y;
		
		Polynomial p1 = multiply(intTen, poly1);
		Polynomial p2 = multiply(poly1, poly2);
		Polynomial p3 = multiply(poly1, add(intTen, poly2));
		
		Polynomial b1 = add(p1, p2);
		
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
		Polynomial poly1 = (Polynomial) x;
		Polynomial poly2 = (Polynomial) y;
		
		Polynomial p1 = multiply(poly1, poly2);
		Polynomial p2 = multiply(intTwo, multiply(poly1, poly2));
		
		Polynomial b1 = add(p1, p1);
		
		assertEquals(p2, b1);
	}
	
	/**
	 * Adds a primitive power and a monic by forming the factorization and by 
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
	 * 				a polynomial of type Polynomial which is the sum of a 
	 * 				monic and a primitive power (passed as arguments).
	 */
	@Test
	public void addPrimitivePowerToMonic() {
		Polynomial poly1 = (Polynomial) x;
		Polynomial poly2 = (Polynomial) y;
		
		Polynomial p1 = multiply(poly1, poly2);
		Polynomial p2 = multiply(poly1, poly1);
		Polynomial p3 = multiply(add(poly1, poly2), poly1);
		
		Polynomial b1 = add(p1, p2);
		
		assertEquals(p3, b1);
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
		Polynomial poly1 = (Polynomial) x;
		Polynomial poly2 = (Polynomial) y;
		
		Polynomial p1 = multiply(poly1, poly2);
		Polynomial p2 = multiply(add(intOne, poly2), poly1);
		
		Polynomial b1 = add(p1, poly1);
		
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
		Polynomial poly1 = (Polynomial) x;
		Polynomial poly2 = (Polynomial) y;
		
		Polynomial p1 = multiply(poly1, poly2);
		Polynomial p2 = add(multiply(poly1, poly2), intOne);
	
		Polynomial b1 = add(p1, intOne);
		
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
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial p1 = multiply(poly1, poly1);
		Polynomial p2 = multiply(multiply(poly1, poly1), intTwo);
		Polynomial p3 = multiply(multiply(poly1, poly1), intThree);
		
		Polynomial b1 = add(p1, p2);
		
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
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial p1 = multiply(poly1, poly1);
		Polynomial p2 = multiply(multiply(poly1, poly1), intTwo);
				
		Polynomial b1 = add(p1, p1);
		
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
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial p1 = multiply(poly1, poly1);
		Polynomial p2 = add(poly1, multiply(poly1, poly1));
		
		Polynomial b1 = add(p1, poly1);
		
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
		Polynomial poly1 = (Polynomial) x;
		
		Polynomial p1 = add(poly1, intOne);
		
		Polynomial b1 = add(poly1, intOne);
		
		assertEquals(p1, b1);
	}
}