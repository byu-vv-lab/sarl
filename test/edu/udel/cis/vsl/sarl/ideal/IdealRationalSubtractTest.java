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
 * The class IdealSubtractTest tests methods found in the edu.udel.cis.vsl.sarl.ideal.common package 
 * using subtractions among various combinations such as:
 * 
 * <ul>
 * <li>RationalExpression - Polynomial</li>
 * <li>RationalExpression - Monomial</li>
 * <li>RationalExpression - Monic</li>
 * <li>RationalExpression - Primitive</li>
 * <li>RationalExpression - PrimitivePower</li>
 * </ul>
 * 
 */
public class IdealRationalSubtractTest {
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
	 * a function - subRational() which subtracts a rational expression with a polynomial
	 * 
	 * @param a - RationalExpression
	 * @param b - Polynomial
	 * 
	 * @return
	 * 			the value of an expression consisting of subtraction of a rational expression and a polynomial
	 */
	public RationalExpression subRational(RationalExpression a, Polynomial b){
		RationalExpression r = (RationalExpression) idealFactory.subtract(a, b);
		return r;
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
		Polynomial x2 = (Polynomial) idealFactory.multiply(x, x); //x^2
		Polynomial monic = (Polynomial) idealFactory.multiply(x2, y); //x^2 * y
		Polynomial monomial = (Polynomial) idealFactory.multiply(idealFactory.constant(realThree), 
				monic); //3x^2 * y
		Polynomial polynomial = (Polynomial) idealFactory.add(monomial, x2); //3x^2 * y + x^2
		RationalExpression subPrimitive = (RationalExpression) 
				idealFactory.subtract(r1, x); //(x - x*y)/y 
		RationalExpression subPrimitivePower = subRational(r1, x2); //(x - x^2*y)/y 
		RationalExpression subMonic = subRational(r1, monic); //(x - x^2*y^2)/y 
		RationalExpression subMonomial = subRational(r1, monomial); //(x - 3*x^2*y^2)/y 
		RationalExpression subPolynomial = subRational(r1, polynomial); //(x - 3*x^2*y^2 - x^2 * y)/y
		
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
	
	/**
	 * Subtracts a rational expression and a polynomial by forming the factorization and by factoring out 
	 * the common factors that are produced from the two factorizations.
	 * 
	 * @param p1 - RationalExpression
	 * @param p2 - Polynomial
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a rational expression of type RationalExpression which is the subtraction of a rational expression 
	 * 				and a polynomial (passed as arguments).
	 */
	@Test
	public void subRationalToPolynomial() {
		Polynomial x2 = (Polynomial) idealFactory.multiply(x, x); //x^2
		Polynomial monic = (Polynomial) idealFactory.multiply(x2, y); //x^2 * y
		Polynomial monomial = (Polynomial) idealFactory.multiply(idealFactory.constant(realThree), 
				monic); //3x^2 * y
		Polynomial polynomial = (Polynomial) idealFactory.add(monomial, x2); //3x^2 * y + x^2
		NumericExpression result = idealFactory.divide(idealFactory.
				add(idealFactory.minus(idealFactory.multiply(polynomial, y)),
						x), y); //(3*x^2*y^2 + x^2 * y + x)/y  
		
		RationalExpression subPolynomial = subRational(r1, polynomial); //(x - 3*x^2*y^2 - x^2 * y)/y
		
		assertEquals(result, subPolynomial);
	}
	
	/**
	 * Subtracts a rational expression and a monomial by forming the factorization and by factoring out 
	 * the common factors that are produced from the two factorizations.
	 * 
	 * @param p1 - RationalExpression
	 * @param p2 - monomial
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a rational expression of type RationalExpression which is the subtraction of a rational expression 
	 * 				and a monomial (passed as arguments).
	 */
	@Test
	public void subRationalToMonomial() {
		Polynomial x2 = (Polynomial) idealFactory.multiply(x, x); //x^2
		Polynomial monic = (Polynomial) idealFactory.multiply(x2, y); //x^2 * y
		Polynomial monomial = (Polynomial) idealFactory.multiply(idealFactory.constant(realThree), 
				monic); //3x^2 * y
		NumericExpression result = idealFactory.divide(idealFactory.
				subtract(x, idealFactory.multiply(monomial, y)), y); //(3*x^2*y^2 + x)/y  
		
		RationalExpression subMonomial = subRational(r1, monomial); //(x - 3*x^2*y^2)/y
		
		assertEquals(result, subMonomial);
	}
	
	/**
	 * Subtracts a rational expression and a monic by forming the factorization and by factoring out 
	 * the common factors that are produced from the two factorizations.
	 * 
	 * @param p1 - RationalExpression
	 * @param p2 - monic
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a rational expression of type RationalExpression which is the subtraction of a rational expression 
	 * 				and a monic (passed as arguments).
	 */
	@Test
	public void subRationalToMonic() {
		Polynomial x2 = (Polynomial) idealFactory.multiply(x, x); //x^2
		Polynomial monic = (Polynomial) idealFactory.multiply(x2, y); //x^2 * y
		NumericExpression result = idealFactory.divide(idealFactory.
				subtract(x, idealFactory.multiply(monic, y)), y); //(x^2*y^2 + x)/y 
		
		RationalExpression subMonic = subRational(r1, monic); //(x - x^2*y^2)/y 
		
		assertEquals(result, subMonic);
	}
}
