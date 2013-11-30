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
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;
import edu.udel.cis.vsl.sarl.ideal.IF.RationalExpression;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

/**
 * The class IdealDivideTest tests methods found in the edu.udel.cis.vsl.sarl.ideal.common package 
 * using divisions among various combinations such as:
 * 
 * <ul>
 * <li>RationalExpression / Polynomial</li>
 * <li>RationalExpression / Monomial</li>
 * <li>RationalExpression / Monic</li>
 * <li>RationalExpression / Primitive</li>
 * <li>RationalExpression / PrimitivePower</li>
 * </ul>
 *
 */
public class IdealRationalDivideTest {
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
	 * a function - divideRational() which divides a rational expression with a polynomial
	 * 
	 * @param a - Polynomial
	 * @param b - RationalExpression
	 * 
	 * @return
	 * 			the value of an expression consisting of division of a rational expression and a polynomial
	 */
	public RationalExpression divideRational(Polynomial a, RationalExpression b){
		RationalExpression r = (RationalExpression) idealFactory.divide(a, b);
		return r;
	}
	
	/**
	 * Divide various levels of numbers (primitive, monic, poly, etc.) with 
	 * a rational number
	 * 
	 * @return type
	 * 				RationalExpression
	 */
	@Test
	public void divideToRational() {
		NumericSymbolicConstant x = objectFactory.canonic(idealFactory
				.symbolicConstant(objectFactory.stringObject("x"),
						typeFactory.realType())); // value 'X' of type real
		NumericSymbolicConstant y = objectFactory.canonic(idealFactory
				.symbolicConstant(objectFactory.stringObject("Y"),
						typeFactory.realType())); // value 'Y' of type real
		
		RationalExpression r1 = (RationalExpression) idealFactory.divide(x, y);	// x/y	
		Polynomial x2 = (Polynomial) idealFactory.multiply(x, x); //x^2
		NumericExpression y2 = idealFactory.multiply(y, y); //y^2
		Polynomial monic = (Polynomial) idealFactory.multiply(x2, y); //x^2 * y
		Polynomial monomial = (Polynomial) idealFactory.multiply(idealFactory.constant(realThree), 
				monic); //3x^2 * y
		Polynomial polynomial = (Polynomial) idealFactory.add(idealFactory.
				divide(monomial, idealFactory.constant(realThree)), x2); //x^2 * y + x^2
		RationalExpression divPrimitive = (RationalExpression) 
				idealFactory.divide(r1, x); //1/y 
		RationalExpression divPrimitivePower = divideRational(x2, r1); //(x*y)
		RationalExpression divMonic = divideRational(monic, r1); //(x*y^2) 
		RationalExpression divMonomial = divideRational(idealFactory.divide(monomial, 
						idealFactory.constant(realThree)), r1); //(x*y^2) 
		RationalExpression divPolynomial = divideRational(polynomial, r1); //x^3 + (x^3/y)
		
		NumericExpression result1 = idealFactory.divide(y, y2); //1/y 
		NumericExpression result2 = idealFactory.multiply(x,y); //(x*y) 
		NumericExpression result3 = idealFactory.multiply(y2, x); //(x*y^2) 
		NumericExpression result4 = idealFactory.add(result3, 
				idealFactory.multiply(x, y)); //(x*y^2) + (x*y) 
		
		assertEquals(result1, divPrimitive);	
		assertEquals(result2, divPrimitivePower);	
		assertEquals(result3, divMonic);	
		assertEquals(result3, divMonomial);
		assertEquals(result4, divPolynomial);
	}
	
	/**
	 * Adds a rational expression and a polynomial by forming the factorization and by factoring out 
	 * the common factors that are produced from the two factorizations.
	 * 
	 * @param p1 - RationalExpression
	 * @param p2 - Polynomial
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a rational expression of type RationalExpression which is the sum of a rational expression 
	 * 				and a polynomial (passed as arguments).
	 */
	@Test
	public void divideRationalToPolynomial() {
		Polynomial x2 = (Polynomial) idealFactory.multiply(x, x); //x^2
		NumericExpression y2 = idealFactory.multiply(y, y); //y^2
		Polynomial monic = (Polynomial) idealFactory.multiply(x2, y); //x^2 * y
		Polynomial monomial = (Polynomial) idealFactory.multiply(idealFactory.constant(realThree), 
				monic); //3x^2 * y
		Polynomial polynomial = (Polynomial) idealFactory.add(idealFactory.
				divide(monomial, idealFactory.constant(realThree)), x2); //x^2 * y + x^2
		NumericExpression result1 = idealFactory.multiply(y2, x); //(x*y^2) 
		NumericExpression result2 = idealFactory.add(result1, 
				idealFactory.multiply(x, y)); //(x*y^2) + (x*y) 
		
		RationalExpression divPolynomial = divideRational(polynomial, r1); //x^3 + (x^3/y)
		
		assertEquals(result2, divPolynomial);
	}
	
	/**
	 * Adds a rational expression and a monomial by forming the factorization and by factoring out 
	 * the common factors that are produced from the two factorizations.
	 * 
	 * @param p1 - RationalExpression
	 * @param p2 - Monomial
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a rational expression of type RationalExpression which is the sum of a rational expression 
	 * 				and a monomial (passed as arguments).
	 */
	@Test
	public void divideRationalToMonomial() {
		Polynomial x2 = (Polynomial) idealFactory.multiply(x, x); //x^2
		NumericExpression y2 = idealFactory.multiply(y, y); //y^2
		Polynomial monic = (Polynomial) idealFactory.multiply(x2, y); //x^2 * y
		Polynomial monomial = (Polynomial) idealFactory.multiply(idealFactory.constant(realThree), 
				monic); //3x^2 * y
		NumericExpression result = idealFactory.multiply(y2, x); //(x*y^2) 
				
		RationalExpression divMonomial = divideRational(idealFactory.divide(monomial, 
				idealFactory.constant(realThree)), r1); //(x*y^2) 
		
		assertEquals(result, divMonomial);
	}
}
