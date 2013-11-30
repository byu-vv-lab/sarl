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
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;
import edu.udel.cis.vsl.sarl.ideal.IF.RationalExpression;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

/**
 * The class IdealRationalAddTest tests methods found in the edu.udel.cis.vsl.sarl.ideal.common package 
 * using additions among various combinations such as:
 * 
 * <ul>
 * <li>RationalExpression + Polynomial</li>
 * <li>RationalExpression + Monomial</li>
 * <li>RationalExpression + Monic</li>
 * <li>RationalExpression + Primitive</li>
 * <li>RationalExpression + PrimitivePower</li>
 * </ul>
 * 
 */
public class IdealRationalAddTest {
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
	private RationalNumber realThree; // real 3
	RationalExpression r1;
	NumericSymbolicConstant x;
	NumericSymbolicConstant y;
		
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
		realThree = numberFactory.rational("3");
		x = objectFactory.canonic(idealFactory
				.symbolicConstant(objectFactory.stringObject("x"),
						typeFactory.realType())); // value 'X' of type real
		y = objectFactory.canonic(idealFactory
				.symbolicConstant(objectFactory.stringObject("Y"),
						typeFactory.realType())); // value 'Y' of type real
		r1 = (RationalExpression) idealFactory.divide(x, y);	// x/y
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	/**
	 * a function - addRational() which adds a rational expression with a polynomial
	 * 
	 * @param a - RationalExpression
	 * @param b - Polynomial
	 * 
	 * @return
	 * 			the value of an expression consisting of addition of a rational expression and a polynomial
	 */
	public RationalExpression addRational(RationalExpression a, Polynomial b){
		RationalExpression r = (RationalExpression) idealFactory.add(a, b);
		return r;
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
		Polynomial x2 = (Polynomial) idealFactory.multiply(x, x); //x^2
		Polynomial monic = (Polynomial) idealFactory.multiply(x2, y); //x^2 * y
		Polynomial monomial = (Polynomial) idealFactory.multiply(idealFactory.constant(realThree), 
				monic); //3x^2 * y
		Polynomial polynomial = (Polynomial) idealFactory.add(monomial, x2); //3x^2 * y + x^2
		RationalExpression plusPrimitive = (RationalExpression) 
				idealFactory.add(r1, x); //(x*y + x)/y 
		RationalExpression plusPrimitivePower = addRational(r1, x2); //(x^2*y + x)/y 
		RationalExpression plusMonic = addRational(r1, monic); //(x^2*y^2 + x)/y 
		RationalExpression plusMonomial = addRational(r1, monomial); //(3*x^2*y^2 + x)/y 
		RationalExpression plusPolynomial = addRational(r1, polynomial); //(3*x^2*y^2 + x^2 * y + x)/y
		
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
	public void addRationalToPolynomial() {
		Polynomial x2 = (Polynomial) idealFactory.multiply(x, x); //x^2
		Polynomial monic = (Polynomial) idealFactory.multiply(x2, y); //x^2 * y
		Polynomial monomial = (Polynomial) idealFactory.multiply(idealFactory.constant(realThree), 
				monic); //3x^2 * y
		Polynomial polynomial = (Polynomial) idealFactory.add(monomial, x2); //3x^2 * y + x^2
		NumericExpression result5 = idealFactory.divide(idealFactory.
				add(idealFactory.multiply(polynomial, y), x), y); //(3*x^2*y^2 + x^2 * y + x)/y
		
		RationalExpression plusPolynomial = addRational(r1, polynomial); //(3*x^2*y^2 + x^2 * y + x)/y
		
		assertEquals(result5, plusPolynomial);
	}
}
