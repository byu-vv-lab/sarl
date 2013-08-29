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
package edu.udel.cis.vsl.sarl.ideal; //

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class IdealTest {

	private static PrintStream out = System.out;
	private NumberFactory numberFactory;
	private ObjectFactory objectFactory;
	private SymbolicTypeFactory typeFactory;
	// private CollectionFactory collectionFactory;
	private IdealFactory idealFactory;

	private RationalNumber n1; // 3/2
	private RationalNumber n2; // -1/4
	private RationalNumber n3; // 5/4
	private Constant c1; // real constant 3/2
	private Constant c2; // real constant -1/4
	private Constant c10; // int constant 10
	StringObject Xobj; // "X"
	NumericSymbolicConstant x; // int symbolic constant "X"
	NumericSymbolicConstant y; // int symbolic constant "Y"

	@Before
	public void setUp() throws Exception {
		FactorySystem system = PreUniverses.newIdealFactorySystem();

		objectFactory = system.objectFactory();
		numberFactory = system.numberFactory();
		typeFactory = system.typeFactory();
		// collectionFactory = system.collectionFactory();
		idealFactory = (IdealFactory) system.numericFactory();
		n1 = numberFactory.rational("1.5");
		n2 = numberFactory.rational("-.25");
		n3 = numberFactory.rational("1.25");
		c1 = idealFactory.constant(n1);
		c2 = idealFactory.constant(n2);
		c10 = idealFactory.intConstant(10);
		Xobj = objectFactory.stringObject("X");
		x = objectFactory.canonic(idealFactory.symbolicConstant(Xobj,
				typeFactory.integerType()));
		y = objectFactory.canonic(idealFactory.symbolicConstant(
				objectFactory.stringObject("Y"), typeFactory.integerType()));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void constantCreation() {
		out.println("constantCreation: " + c10);
		assertEquals(10, ((IntegerNumber) c10.number()).intValue());
	}

	@Test
	public void constantAdd() {
		Constant c3 = (Constant) idealFactory.add(c1, c2);

		out.println("constantAdd: " + c1 + " + " + c2 + " = " + c3);
		assertEquals(n3, c3.number());
	}

	@Test
	public void constantMultiply() {
		Constant result = (Constant) idealFactory.multiply(c1, c2);
		RationalNumber expected = numberFactory.rational("-.375");

		out.println("constantMultiply: " + c1 + " * " + c2 + " = " + result);
		assertEquals(expected, result.number());
	}

	@Test
	public void symbolicConstantCreate() {
		out.println("symbolicConstantCreate: " + x);
		assertEquals("X", x.name().getString());
		assertEquals(typeFactory.integerType(), x.type());
	}

	@Test
	public void symbolicConstantEquality() {
		SymbolicConstant x2 = idealFactory.symbolicConstant(
				objectFactory.stringObject("X"), typeFactory.integerType());

		assertEquals(x, x2);
	}

	@Test
	public void symbolicConstantInequality1() {
		assertFalse(x.equals(y));
	}

	@Test
	public void symbolicConstantInequality2() {
		SymbolicConstant x2 = idealFactory.symbolicConstant(
				objectFactory.stringObject("X"), typeFactory.realType());

		assertFalse(x.equals(x2));
	}

	@Test
	public void commutativity1() {
		SymbolicExpression xpy = idealFactory.add(x, y);
		SymbolicExpression ypx = idealFactory.add(y, x);

		out.println("commutativity1: " + xpy + " vs. " + ypx);
		assertEquals(xpy, ypx);
	}

	@Test
	public void xplus1squared() {
		NumericExpression xp1 = idealFactory
				.add(x, idealFactory.intConstant(1));
		SymbolicExpression xp1squared = idealFactory.multiply(xp1, xp1);
		SymbolicExpression x2p2xp1 = idealFactory.add(idealFactory.multiply(x,
				x), idealFactory.add(
				idealFactory.multiply(idealFactory.intConstant(2), x),
				idealFactory.intConstant(1)));

		out.println("xplus1squared: " + xp1squared + " vs. " + x2p2xp1);
		assertEquals(xp1squared, x2p2xp1);
	}

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
