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

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

/**
 * The class IdealBooleanTest tests methods found in the edu.udel.cis.vsl.sarl.ideal.common package 
 * using booleans
 * 
 * This class usually compares two expressions and returns a boolean value. The expressions
 * can be polynomials, rational expressions, monomials, monics, primitive powers, primitives etc.
 * 
 * The comparisons usually comprises of:
 * 
 * <ul>
 * <li>less than</li>
 * <li>less than equals</li>
 * <li>equals</li>
 * <li>not equals</li>
 * <li>not less than</li>
 * <li>not less than equals</li>
 * </ul>
 *
 */
public class IdealBooleanTest {

	private static PrintStream out = System.out;
	private ObjectFactory objectFactory;
	private SymbolicTypeFactory typeFactory;
	private IdealFactory idealFactory;
	private BooleanExpressionFactory booleanFactory;
	
	/**
	 * int constant 1
	 */
	private Constant intOne;
	/**
	 * int constant 2
	 */
	private Constant intTwo; 
	/**
	 * "X"
	 */
	StringObject Xobj; 
	/**
	 * int symbolic constant "X"
	 */
	NumericSymbolicConstant x; 
	/**
	 * "Y"
	 */
	StringObject Yobj;
	/**
	 * int symbolic constant "Y"
	 */
	NumericSymbolicConstant y;
		
	@Before
	public void setUp() throws Exception {
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		objectFactory = system.objectFactory();
		typeFactory = system.typeFactory();
		idealFactory = (IdealFactory) system.numericFactory();
		booleanFactory = system.booleanFactory();
		intOne = idealFactory.intConstant(1);
		intTwo = idealFactory.intConstant(2);
		Xobj = objectFactory.stringObject("X");
		x = objectFactory.canonic(idealFactory.symbolicConstant(Xobj,
				typeFactory.integerType()));
		Yobj = objectFactory.stringObject("Y");
		y = objectFactory.canonic(idealFactory.symbolicConstant(Yobj,
				typeFactory.integerType()));
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	/**
	 * Returns true if the first argument is 'not less than' the second 
	 * argument and vice-versa.
	 * 
	 * @param type
	 * 				NumericExpression
	 * 
	 * @return type
	 * 				BooleanExpression
	 */
	@Test
	public void notLessThan() {
		NumericExpression n1 = idealFactory.subtract(x,intOne);
		NumericExpression n2 = idealFactory.add(x, intOne);
		BooleanExpression m1 = booleanFactory.symbolic(false);
		BooleanExpression m2 = booleanFactory.symbolic(true);
		
		BooleanExpression n = idealFactory.notLessThan(n2, n1);
		BooleanExpression nn = idealFactory.notLessThan(n1, n2);
		
		assertEquals(m2, n);
		assertEquals(m1, nn);
	}

	/**
	 * Returns true if the first argument is 'not less than or equal' 
	 * to the second argument and vice-versa.
	 * 
	 * @param type
	 * 				NumericExpression
	 * 
	 * @return type
	 * 				BooleanExpression
	 */
	@Test
	public void notLessThanEquals() {		
		NumericExpression n1 = idealFactory.subtract(x, intOne);
		NumericExpression n2 = idealFactory.add(x, intOne);
		BooleanExpression m = booleanFactory.symbolic(true);
		BooleanExpression n = booleanFactory.symbolic(false);
		
		BooleanExpression n01 = idealFactory.notLessThanEquals(n1, n2);
		BooleanExpression n02 = idealFactory.notLessThanEquals(n2, n1);
		
		assertEquals(n, n01);
		assertEquals(m, n02);
	}
	
	/**
	 * Returns true if the first argument is 'equal' 
	 * to the second argument and returns false otherwise.
	 * @param type
	 * 				NumericExpression
	 * 
	 * @return type
	 * 				BooleanExpression
	 */
	@Test
	public void equals() {
		NumericExpression n1 = idealFactory.add(x, intOne);
		NumericExpression n2 = idealFactory.add(idealFactory.
				multiply(intOne, idealFactory.multiply(x, x)), x);
		NumericExpression n3 = idealFactory.add(idealFactory.
				multiply(intTwo, idealFactory.multiply(x, y)), x);
		NumericExpression r1 = idealFactory.
				divide(idealFactory.add(x, y), x);	// (x-y)/y	
		BooleanExpression m = booleanFactory.symbolic(true);
		BooleanExpression n = booleanFactory.symbolic(false);
		
		BooleanExpression b0 = idealFactory.equals(x, n1);
		BooleanExpression b1 = idealFactory.equals(x, n2);
		BooleanExpression b2 = idealFactory.equals(x, n3);
		BooleanExpression b3 = idealFactory.equals(x, x);
		BooleanExpression b4 = idealFactory.equals(intOne, r1);
		
		out.println("b1=" +b1);
		out.println("b2=" +b2);
		out.println("b4=" +b4);
		assertEquals(n, b0);
		assertEquals(m, b3);
	}
	
}