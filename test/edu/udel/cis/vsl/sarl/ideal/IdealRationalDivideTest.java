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
 * The class IdealRationalDivideTest consists of methods which returns a rational expression which is the
 * result of dividing a rational number to each and every one of the following expressions
 * 
 * This class tests methods found in the edu.udel.cis.vsl.sarl.ideal.common package 
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
 * Example:
 * 				RationalExpression: x/y
 * 				Monomial: 3x^2 * y
 * 
 * 				Now dividing the rational expression with the given monomial gives a rational expression as the result
 * 								(x*y^2)
 *
 */
public class IdealRationalDivideTest {
	private NumberFactory numberFactory;
	private ObjectFactory objectFactory;
	private SymbolicTypeFactory typeFactory;
	private IdealFactory idealFactory;

	private RationalNumber realThree; // real 3
	private RationalNumber realOne; // real 1
	private RationalNumber realFive; // real 5
	private RationalNumber realSeven; // real 7
	private RationalNumber realTwentyOne; // real 21
	private RationalNumber realThirtyFive; // real 35
	private NumericExpression three; // real constant 3
	private NumericExpression five; // real constant 5
	private NumericExpression seven; // real constant 7
	private NumericExpression twentyOne; // real constant 21
	private NumericExpression thirtyFive; // real constant 35
	RationalExpression r1; // Rational Expression x/y
	NumericSymbolicConstant x; // int symbolic constant "X"
	NumericSymbolicConstant y; // int symbolic constant "Y"
		
	@Before
	public void setUp() throws Exception {
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		numberFactory = system.numberFactory();
		objectFactory = system.objectFactory();
		typeFactory = system.typeFactory();
		idealFactory = (IdealFactory) system.numericFactory();
		realThree = numberFactory.rational("3");
		realOne = numberFactory.rational("1");
		realThree = numberFactory.rational("3");
		realFive = numberFactory.rational("5");
		realSeven = numberFactory.rational("7");
		realTwentyOne = numberFactory.rational("21");
		realThirtyFive = numberFactory.rational("35");
		five = idealFactory.constant(realFive);
		three = idealFactory.constant(realThree);
		seven = idealFactory.constant(realSeven);
		twentyOne = idealFactory.constant(realTwentyOne);
		thirtyFive = idealFactory.constant(realThirtyFive);
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
	 * 				a rational expression of type RationalExpression which is the division of a rational expression 
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
	 * 				a rational expression of type RationalExpression which is the division of a rational expression 
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
	
	/**
	 * Adds a rational expression and a monic by forming the factorization and by factoring out 
	 * the common factors that are produced from the two factorizations.
	 * 
	 * @param p1 - RationalExpression
	 * @param p2 - Monic
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a rational expression of type RationalExpression which is the division of a rational expression 
	 * 				and a monic (passed as arguments).
	 */
	@Test
	public void divideRationalToMonic() {
		Polynomial x2 = (Polynomial) idealFactory.multiply(x, x); //x^2
		NumericExpression y2 = idealFactory.multiply(y, y); //y^2
		Polynomial monic = (Polynomial) idealFactory.multiply(x2, y); //x^2 * y
		NumericExpression result = idealFactory.multiply(y2, x); //(x*y^2) 
				
		RationalExpression divMonic = divideRational(monic, r1); //(x*y^2) 
		
		assertEquals(result, divMonic);
	}
	
	/**
	 * Adds a rational expression and a primitive power by forming the factorization and by factoring out 
	 * the common factors that are produced from the two factorizations.
	 * 
	 * @param p1 - RationalExpression
	 * @param p2 - PrimitivePower
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a rational expression of type RationalExpression which is the division of a rational expression 
	 * 				and a primitive power (passed as arguments).
	 */
	@Test
	public void divideRationalToPrimitivePower() {
		Polynomial x2 = (Polynomial) idealFactory.multiply(x, x); //x^2
		NumericExpression result = idealFactory.multiply(x,y); //(x*y)  
				
		RationalExpression divPrimitivePower = divideRational(x2, r1); //(x*y)
		
		assertEquals(result, divPrimitivePower);
	}
	
	/**
	 * Adds a rational expression and a primitive by forming the factorization and by factoring out 
	 * the common factors that are produced from the two factorizations.
	 * 
	 * @param p1 - RationalExpression
	 * @param p2 - Primitive
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * @return
	 * 				a rational expression of type RationalExpression which is the division of a rational expression 
	 * 				and a primitive (passed as arguments).
	 */
	@Test
	public void divideRationalToPrimitive() {
		Polynomial poly1 = (Polynomial) x;
		NumericExpression y2 = idealFactory.multiply(y, y); //y^2
		NumericExpression result = idealFactory.divide(y, y2); //1/y 		
				
		RationalExpression divPrimitive = (RationalExpression) 
				idealFactory.divide(r1, poly1); //1/y
		
		assertEquals(result, divPrimitive);
	}
	
	/**
	 * Returns a rational expression by canceling out the common factors that 
	 * are present in both numerator and denominator.
	 * 
	 * @param type
	 * 				Symbolic Expressions of same numeric type
	 * 
	 * the test below does this:
	 * 		(21x^3 - 35x^2) / (7x) -------> 3x^2 - 5x
	 */
	@Test
	public void complexRational() {
		StringObject Xobj = objectFactory.stringObject("X");
		NumericSymbolicConstant x = objectFactory.canonic(idealFactory.symbolicConstant(Xobj,
				typeFactory.realType())); // value 'X' of type real
		IntObject exp3 = objectFactory.intObject(3); // integer object '3'
		IntObject exp2 = objectFactory.intObject(2); // integer object '2'
		
		NumericExpression complex1 = idealFactory.multiply(twentyOne, idealFactory.
				power(x, exp3));
		NumericExpression complex2 = idealFactory.multiply(thirtyFive, idealFactory.
				power(x, exp2));
		NumericExpression numer = idealFactory.subtract(complex1, complex2);		
		NumericExpression denom = idealFactory.multiply(x, seven);
		NumericExpression complex = idealFactory.divide(numer, denom);
		NumericExpression result1 = idealFactory.multiply(idealFactory.power(x, 
				exp2), three);
		NumericExpression result2 = idealFactory.multiply(x, five);
		NumericExpression result = idealFactory.subtract(result1, result2);
		
		assertEquals(result, complex);
	}
	
	/**
	 * Returns a rational expression by canceling out the common factors that 
	 * are present in both numerator and denominator.
	 * 
	 * @param type
	 * 				Symbolic Expressions of same numeric type
	 * 
	 *  [(x+1)(x-1)(xy+3)]/[(x-1)*(xy+5)*z] = [(x+1)(xy+3)]/[(xy+5)*z]?
	 */
	@Test
	public void factoringRational() {
		NumericSymbolicConstant x = objectFactory.canonic(idealFactory
				.symbolicConstant(objectFactory.stringObject("x"),
						typeFactory.realType())); // value 'X' of type real
		NumericSymbolicConstant y = objectFactory.canonic(idealFactory
				.symbolicConstant(objectFactory.stringObject("y"),
						typeFactory.realType())); // value 'Y' of type real
		NumericSymbolicConstant z = objectFactory.canonic(idealFactory
				.symbolicConstant(objectFactory.stringObject("z"),
						typeFactory.realType())); // value 'Z' of type real
		
		NumericExpression xPlus1 = idealFactory.add(x, 
				idealFactory.constant(realOne)); //(x+1)
		NumericExpression xMinus1 = idealFactory.subtract(x, 
				idealFactory.constant(realOne)); //(x-1)
		NumericExpression xy = idealFactory.multiply(x, y); //xy
		NumericExpression xyPlusThree = idealFactory.add(xy, 
				idealFactory.constant(realThree)); //xy+3
		NumericExpression xyPlusFive = idealFactory.add(xy, 
				idealFactory.constant(realFive)); //xy+5
		NumericExpression numerator = idealFactory.multiply(
				idealFactory.multiply(xPlus1, xMinus1), xyPlusThree); //(x+1)(x-1)(xy+3)
		NumericExpression denominator = idealFactory.multiply(
				idealFactory.multiply(xMinus1, xyPlusFive), z); //(x-1)*(xy+5)*z		
		NumericExpression resultNumer = idealFactory.multiply(xPlus1, 
				xyPlusThree); //(x+1)(xy+3)
		NumericExpression resultDenom = idealFactory.multiply(xyPlusFive, 
				z); //(xy+5)*z
		NumericExpression r1 = idealFactory.divide(numerator, denominator);
		NumericExpression result = idealFactory.divide(resultNumer, resultDenom);
		
		assertEquals(result, r1);
	}
}
