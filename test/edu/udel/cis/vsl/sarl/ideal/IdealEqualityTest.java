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
import static org.junit.Assert.assertFalse;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.RationalExpression;
import edu.udel.cis.vsl.sarl.ideal.common.CommonIdealFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
/**
 * IdealEqualityTest tests methods found in the edu.udel.cis.vsl.sarl.ideal.common package using equalities
 * @author cboucher
 *
 */
public class IdealEqualityTest {

	private static PrintStream out = System.out;
	private NumberFactory numberFactory;
	private ObjectFactory objectFactory;
	private SymbolicTypeFactory typeFactory;
	private CollectionFactory collectionFactory;
	private IdealFactory idealFactory;
	private BooleanExpressionFactory booleanFactory;
	private CommonIdealFactory commonIdealFactory;

	private Constant intTwo; // int constant 2
	StringObject Xobj; // "X"
	NumericSymbolicConstant x; // int symbolic constant "X"
	NumericSymbolicConstant y; // int symbolic constant "Y"
	private NumericExpression zero; // real constant 0
	private RationalNumber realZero; // real 0

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
		intTwo = idealFactory.intConstant(2);
		Xobj = objectFactory.stringObject("X");
		x = objectFactory.canonic(idealFactory.symbolicConstant(Xobj,
				typeFactory.integerType()));
		y = objectFactory.canonic(idealFactory.symbolicConstant(
				objectFactory.stringObject("Y"), typeFactory.integerType()));
		realZero = numberFactory.rational("0");
		zero = commonIdealFactory.constant(realZero);
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	/**
	 * Returns true or false if the two symbolic Expressions are not equal 
	 * or equal respectively
	 * 
	 * @param type
	 * 				NumericExpression
	 * 
	 * @return type
	 * 				BooleanExpression
	 * 
	 */
	@Test
	public void neq() {
		NumericExpression n11 = idealFactory.add(x, y);
		NumericExpression n22 = idealFactory.subtract(x, y);
		NumericExpression n1 = idealFactory.add(y, intTwo);
		NumericExpression n2 = idealFactory.subtract(y,	intTwo);
		NumericExpression n3 = idealFactory.add(y, intTwo);		
		BooleanExpression n = commonIdealFactory.neq(n1, n2);
		BooleanExpression n0 = commonIdealFactory.neq(n1, n3);
		BooleanExpression n1122 = commonIdealFactory.neq(n11, n22);
		BooleanExpression m1 = booleanFactory.symbolic(false);
		BooleanExpression m2 = booleanFactory.symbolic(true);
		
		out.println("neq2=" + n1122);		
		assertEquals(m1, n0);
		assertEquals(m2, n);
	}
	
	/**
	 * Compares two Rational Expressions. Does the less than, not less than, 
	 * not less than equals, less than equals comparisons
	 * 
	 * @param type
	 * 				Symbolic Expressions of same numeric type
	 */
	@Test
	public void comparingRationalExpressions() {
		NumericSymbolicConstant x2 = objectFactory.canonic(idealFactory
				.symbolicConstant(Xobj, typeFactory.realType())); //value 'X' of real type
		NumericSymbolicConstant y2 = objectFactory.canonic(idealFactory
				.symbolicConstant(objectFactory.stringObject("Y"),
						typeFactory.realType())); // value 'Y' of real type
		
		RationalExpression r1 = (RationalExpression) commonIdealFactory.divide(x2, y2);
		BooleanExpression b1 = booleanFactory.booleanExpression(
				SymbolicOperator.LESS_THAN_EQUALS, r1,
				commonIdealFactory.zeroReal());
		BooleanExpression b2 = booleanFactory.booleanExpression(
				SymbolicOperator.LESS_THAN, commonIdealFactory.zeroReal(), r1);
		BooleanExpression nb2 = idealFactory.notLessThan(commonIdealFactory.
				zeroReal(), r1);
		BooleanExpression nb3 = idealFactory.notLessThanEquals(commonIdealFactory.
				zeroReal(), r1);
		BooleanExpression nb4 = idealFactory.lessThanEquals(r1, commonIdealFactory.
				zeroReal());
		NumericExpression nb5 = commonIdealFactory.divide(
				commonIdealFactory.zeroReal(), r1);
		BooleanExpression nb6 = idealFactory.lessThan(r1, commonIdealFactory.
				zeroReal());
		
		out.println("b1=" +b1);
		out.println("b2=" +b2);
		assertEquals(zero, nb5);
		assertEquals(nb2, nb4);
		assertEquals(nb3, nb6);
	}
	
	/**
	 * Checks if two Symbolic Constants are equal. One is created globally 
	 * and the other locally.
	 * 
	 * @param type
	 * 				NumericSymbolicConstant
	 */
	@Test
	public void symbolicConstantEquality() {
		SymbolicConstant x2 = idealFactory.symbolicConstant(
				objectFactory.stringObject("X"), typeFactory.integerType());

		assertEquals(x, x2);
	}

	/**
	 * Checks if two Symbolic Constants are equal or not. 
	 * Here both have different values. So they are not equal. 
	 * Hence this method returns false.
	 * 
	 * @param type
	 * 				NumericSymbolicConstant
	 */
	@Test
	public void symbolicConstantInequality1() {
		assertFalse(x.equals(y));
	}
	
	/**
	 * Returns true or false if the two symbolic Expressions are equal or 
	 * not equal respectively
	 * 
	 * @param type
	 * 				NumericExpression
	 * 
	 * @return type
	 * 				BooleanExpression
	 * 
	 */
	@Test
	public void equals() {
		NumericExpression n11 = idealFactory.add(x, y);
		NumericExpression n22 = idealFactory.subtract(x, y);
		NumericExpression n1 = idealFactory.add(y, intTwo);
		NumericExpression n2 = idealFactory.subtract(y, intTwo);
		NumericExpression n3 = idealFactory.add(y, intTwo);		
		BooleanExpression n = commonIdealFactory.equals(n1, n2);
		BooleanExpression n0 = commonIdealFactory.equals(n1, n3);
		BooleanExpression n1122 = commonIdealFactory.equals(n11, n22);
		BooleanExpression m1 = booleanFactory.symbolic(false);
		BooleanExpression m2 = booleanFactory.symbolic(true);
		
		out.println("Equals=" +n1122);
		assertEquals(m1, n);
		assertEquals(m2, n0);
	}
}