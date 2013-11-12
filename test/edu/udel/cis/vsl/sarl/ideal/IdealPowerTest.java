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
import org.junit.Ignore;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class IdealPowerTest {

	private static PrintStream out = System.out;
	private ObjectFactory objectFactory;
	private SymbolicTypeFactory typeFactory;
	private IdealFactory idealFactory;
		
	private Constant intOne; // int constant 1
	private Constant intTwo; // int constant 2
	StringObject Xobj; // "X"
	NumericSymbolicConstant x; // int symbolic constant "X"
	NumericSymbolicConstant y; // int symbolic constant "Y"
		
	@Before
	public void setUp() throws Exception {
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		objectFactory = system.objectFactory();
		typeFactory = system.typeFactory();
		idealFactory = (IdealFactory) system.numericFactory();
		intOne = idealFactory.intConstant(1);
		intTwo = idealFactory.intConstant(2);
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
	 * Asserts true if (x+1)^2 = x^2 + 2*x + 1
	 * 
	 * @param type
	 * 				SymbolicExpression of numeric type
	 */
	@Test
	public void xPlus1Squared() {
		NumericExpression xp1 = idealFactory.add(x, intOne);
		SymbolicExpression xp1squared = idealFactory.multiply(xp1, xp1);
		SymbolicExpression x2p2xp1 = idealFactory.add(idealFactory.multiply(x,
				x), idealFactory.add(idealFactory.multiply(intTwo, x), intOne));

		out.println("xplus1squared: " + xp1squared + " vs. " + x2p2xp1);
		
		assertEquals(xp1squared, x2p2xp1);
	}

	/**
	 * gives the result for [(x+y)^100] / [(x+y)^99] as (x+y)
	 * 
	 * @param type
	 * 				SymbolicExpression of numeric type
	 */
	@Test
	public void bigPower() {
		int exponent = 100;
		IntObject n = objectFactory.intObject(exponent);
		IntObject m = objectFactory.intObject(exponent - 1);
		NumericExpression xpy = idealFactory.add(x, y);
		NumericExpression xpyen = idealFactory.power(xpy, n);
		NumericExpression xpyem = idealFactory.power(xpy, m);
		NumericExpression quotient = idealFactory.divide(xpyen, xpyem);

		out.println("bigPower: (X+Y)^" + n + " = " + xpyen);
		out.println("bigPower: (X+Y)^" + m + " = " + xpyem);
		out.println("bigPower: quotient : " + quotient);
		
		assertEquals(xpy, quotient);
	}
}