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
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
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
 * <li>RationalExpression * Polynomial</li>
 * <li>RationalExpression * Monomial</li>
 * <li>RationalExpression * Monic</li>
 * <li>RationalExpression * Primitive</li>
 * <li>RationalExpression * PrimitivePower</li>
 * </ul>
 *
 */
public class IdealRationalMultiplyTest {
	private NumberFactory numberFactory;
	private ObjectFactory objectFactory;
	private SymbolicTypeFactory typeFactory;
	private IdealFactory idealFactory;

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
	
	/**
	 * Multiplies a rational expression and a polynomial by forming the factorization and by factoring out 
	 * the common factors that are produced from the two factorizations.
	 * 
	 * @param p1 - RationalExpression
	 * @param p2 - Polynomial
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a rational expression of type RationalExpression which is the multiplication of a rational expression 
	 * 				and a polynomial (passed as arguments).
	 */
	@Test
	public void mulRationalToPolynomial() {
		Polynomial x2 = (Polynomial) idealFactory.multiply(x, x); //x^2
		Polynomial monic = (Polynomial) idealFactory.multiply(x2, y); //x^2 * y
		Polynomial monomial = (Polynomial) idealFactory.multiply(idealFactory.constant(realThree), 
				monic); //3x^2 * y
		Polynomial polynomial = (Polynomial) idealFactory.add(idealFactory.
				divide(monomial, idealFactory.constant(realThree)), x2); //x^2 * y + x^2
		NumericExpression result = idealFactory.add(idealFactory.
				multiply(x2, x), idealFactory.divide(idealFactory.
						multiply(x2, x), y)); //3*x^3 + (x^3/y) 
		
		RationalExpression mulPolynomial = multiplyRational(r1, polynomial); //x^3 + (x^3/y)
		
		assertEquals(result, mulPolynomial);
	}
	
	/**
	 * Multiplies a rational expression and a monomial by forming the factorization and by factoring out 
	 * the common factors that are produced from the two factorizations.
	 * 
	 * @param p1 - RationalExpression
	 * @param p2 - Monomial
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a rational expression of type RationalExpression which is the multiplication of a rational expression 
	 * 				and a monomial (passed as arguments).
	 */
	@Test
	public void mulRationalToMonomial() {
		Polynomial x2 = (Polynomial) idealFactory.multiply(x, x); //x^2
		Polynomial monic = (Polynomial) idealFactory.multiply(x2, y); //x^2 * y
		Polynomial monomial = (Polynomial) idealFactory.multiply(idealFactory.constant(realThree), 
				monic); //3x^2 * y
		NumericExpression result = idealFactory.multiply(x2, x); //(x^3)  
		
		RationalExpression mulMonomial = multiplyRational(r1, idealFactory.divide(monomial, 
				idealFactory.constant(realThree))); //(x^3)
		
		assertEquals(result, mulMonomial);
	}
	
	/**
	 * Multiplies a rational expression and a monic by forming the factorization and by factoring out 
	 * the common factors that are produced from the two factorizations.
	 * 
	 * @param p1 - RationalExpression
	 * @param p2 - Monic
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a rational expression of type RationalExpression which is the multiplication of a rational expression 
	 * 				and a monic (passed as arguments).
	 */
	@Test
	public void mulRationalToMonic() {
		Polynomial x2 = (Polynomial) idealFactory.multiply(x, x); //x^2
		Polynomial monic = (Polynomial) idealFactory.multiply(x2, y); //x^2 * y
		NumericExpression result1 = idealFactory.multiply(x2, x); //(x^3)   
		
		RationalExpression mulMonic = multiplyRational(r1, monic); //(x^3)
		
		assertEquals(result1, mulMonic);
	}
	
	/**
	 * Multiplies a rational expression and a primitive power by forming the factorization and by factoring out 
	 * the common factors that are produced from the two factorizations.
	 * 
	 * @param p1 - RationalExpression
	 * @param p2 - PrimitivePower
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a rational expression of type RationalExpression which is the multiplication of a rational expression 
	 * 				and a primitive power (passed as arguments).
	 */
	@Test
	public void mulRationalToPrimitivePower() {
		Polynomial x2 = (Polynomial) idealFactory.multiply(x, x); //x^2
		NumericExpression result2 = idealFactory.divide(idealFactory.
				multiply(x2, x), y); //(x^2*x)/y 
		
		RationalExpression mulPrimitivePower = multiplyRational(r1, x2); //(x*x^2)/y 
		
		assertEquals(result2, mulPrimitivePower);
	}
	
	/**
	 * Multiplies a rational expression and a primitive by forming the factorization and by factoring out 
	 * the common factors that are produced from the two factorizations.
	 * 
	 * @param p1 - RationalExpression
	 * @param p2 - Primitive
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a rational expression of type RationalExpression which is the multiplication of a rational expression 
	 * 				and a primitive (passed as arguments).
	 */
	@Test
	public void mulRationalToPrimitive() {
		NumericExpression result = idealFactory.divide(idealFactory.
				multiply(x, x), y); //(x*x)/y 
		
		RationalExpression mulPrimitive = (RationalExpression) 
				idealFactory.multiply(r1, x); //(x*x)/y
		
		assertEquals(result, mulPrimitive);
	}
}
