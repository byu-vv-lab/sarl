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
package edu.udel.cis.vsl.sarl.IF;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.universe.Universes;

public class ArrayTest {

	private static PrintStream out = System.out;
	private SymbolicUniverse universe;
	// private NumberFactory numberFactory;
	private StringObject a_obj; // "a"
	// private StringObject b_obj; // "b"
	// private SymbolicType realType;
	private SymbolicType integerType;
	private NumericExpression zero, one, two, five, six, seventeen; // integer
																	// constants
	private List<NumericExpression> list1; // {5,6}
	private List<NumericExpression> list2; // {17}
	private boolean debug = false;

	@Before
	public void setUp() throws Exception {
		universe = Universes.newIdealUniverse();
		// realType = universe.realType();
		integerType = universe.integerType();
		a_obj = universe.stringObject("a");
		// b_obj = universe.stringObject("b");
		zero = universe.integer(0);
		one = universe.integer(1);
		two = universe.integer(2);
		five = universe.integer(5);
		six = universe.integer(6);
		seventeen = universe.integer(17);
		list1 = Arrays.asList(new NumericExpression[] { five, six });
		list2 = Arrays.asList(new NumericExpression[] { seventeen });
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void arrayRead1() {
		SymbolicExpression a = universe.array(integerType, list1);

		if (debug) out.println("arrayRead1: a = " + a);
		assertEquals(universe.arrayType(integerType, two), a.type());
		assertEquals(five, universe.arrayRead(a, zero));
		assertEquals(six, universe.arrayRead(a, one));
	}

	@Test
	public void jagged1() {
		SymbolicArrayType t1 = universe.arrayType(integerType);
		SymbolicArrayType t2 = universe.arrayType(t1, universe.integer(2));
		SymbolicExpression a1 = universe.array(integerType, list1);
		SymbolicExpression a2 = universe.array(integerType, list2);
		SymbolicExpression a = universe.symbolicConstant(a_obj, t2);

		assertTrue(t2.isComplete());
		a = universe.arrayWrite(a, zero, a1);
		a = universe.arrayWrite(a, one, a2);
		if (debug) out.println("jagged1: a = " + a);
		// a={{5,6},{17}}
		assertEquals(five,
				universe.arrayRead(universe.arrayRead(a, zero), zero));
		assertEquals(six, universe.arrayRead(universe.arrayRead(a, zero), one));
		assertEquals(seventeen,
				universe.arrayRead(universe.arrayRead(a, one), zero));
		assertTrue(((SymbolicArrayType) a.type()).isComplete());
		assertEquals(two, universe.length(a));
		assertEquals(two, universe.length(universe.arrayRead(a, zero)));
		assertEquals(one, universe.length(universe.arrayRead(a, one)));
	}

	private SymbolicExpression write2d(SymbolicExpression array,
			NumericExpression i, NumericExpression j, SymbolicExpression value) {
		SymbolicExpression row = universe.arrayRead(array, i);
		SymbolicExpression newRow = universe.arrayWrite(row, j, value);

		return universe.arrayWrite(array, i, newRow);
	}

	private SymbolicExpression read2d(SymbolicExpression array,
			NumericExpression i, NumericExpression j) {
		SymbolicExpression row = universe.arrayRead(array, i);

		return universe.arrayRead(row, j);
	}

	/**
	 * Write and read a 2d array.
	 */
	@Test
	public void array2d() {
		SymbolicArrayType t = universe.arrayType(universe
				.arrayType(integerType));
		SymbolicExpression a = universe.symbolicConstant(a_obj, t);
		NumericExpression zero = universe.integer(0);
		NumericExpression twoInt = universe.integer(2);
		SymbolicExpression read;

		a = write2d(a, zero, zero, twoInt);
		read = read2d(a, zero, zero);
		assertEquals(twoInt, read);
		// for the heck of it...
		if (debug) out.println("array2d: new row is: " + universe.arrayRead(a, zero));
	}

	@Test
	public void canonic1() {
		SymbolicArrayType t1 = universe.arrayType(integerType,
				universe.integer(3));
		SymbolicArrayType t2 = universe.arrayType(integerType,
				universe.integer(3));

		assertEquals(t1, t2);
		t1 = (SymbolicArrayType) universe.canonic(t1);
		t2 = (SymbolicArrayType) universe.canonic(t2);
		assertSame(t1, t2);
	}

	@Test
	public void denseTest() {
		SymbolicArrayType t = universe.arrayType(integerType);
		SymbolicExpression a = universe.symbolicConstant(
				universe.stringObject("a"), t);
		SymbolicExpression b1 = universe.denseArrayWrite(
				a,
				Arrays.asList(new SymbolicExpression[] { null, null, two, null,
						two, null, null }));
		SymbolicExpression b2 = universe.arrayWrite(a, two, two);

		b2 = universe.arrayWrite(b2, universe.integer(4), two);
		if (debug) {
			out.println("b1 = " + b1);
			out.println("b2 = " + b2);
		}
		assertEquals(b2, b1);
	}

}
