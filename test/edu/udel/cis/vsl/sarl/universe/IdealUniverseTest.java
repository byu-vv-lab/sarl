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
package edu.udel.cis.vsl.sarl.universe;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.util.SingletonMap;
import edu.udel.cis.vsl.sarl.universe.common.MathUniverse; 
import edu.udel.cis.vsl.sarl.universe.common.MathSimplifier;
public class IdealUniverseTest {

	private static PrintStream out = System.out;
	private SymbolicUniverse universe;
	// private NumberFactory numberFactory;
	private StringObject Xobj; // "X"
	private StringObject Yobj; // "Y"
	private SymbolicType realType, integerType;
	private NumericSymbolicConstant x; // real symbolic constant "X"
	private NumericSymbolicConstant y; // real symbolic constant "Y"
	private NumericExpression two; // real 2.0
	private NumericExpression three; // real 3.0
	private MathUniverse mathUniverse; 
	private SymbolicExpression symbol;  
	private MathSimplifier trigIdentity;
	@Before
	public void setUp() throws Exception {
		universe = Universes.newIdealUniverse();
		// numberFactory = universe.numberFactory();
		Xobj = universe.stringObject("X");
		Yobj = universe.stringObject("Y");
		realType = universe.realType();
		integerType = universe.integerType();
		x = (NumericSymbolicConstant) universe.symbolicConstant(Xobj, realType);
		y = (NumericSymbolicConstant) universe.symbolicConstant(Yobj, realType);
		two = (NumericExpression) universe.cast(realType, universe.integer(2));
		three = (NumericExpression) universe
				.cast(realType, universe.integer(3));

		out.println("    x = " + x);
		out.println("    y = " + y);
		out.println("  two = " + two);
		out.println("three = " + three);
		out.println();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Map: x->y. x+2y -> 3y
	 */
	@Test
	public void sub1() {
		SymbolicExpression u = universe.add(x, universe.multiply(two, y));
		// SymbolicExpression v = universe.add(y, universe.multiply(two, x));
		SymbolicExpression expected = universe.multiply(three, y);
		Map<SymbolicConstant, SymbolicExpression> map = new SingletonMap<SymbolicConstant, SymbolicExpression>(
				x, y);
		SymbolicExpression newU = universe.substituteSymbolicConstants(u, map);

		out.println("sub1:    u = " + u);
		out.println("sub1:  map = " + map);
		out.println("sub1: newU = " + newU);
		assertEquals(expected, newU);
	}

	/**
	 * Map: x->y, y->x. x+2y -> 2x+y
	 */
	@Test
	public void sub2() {
		SymbolicExpression u = universe.add(x, universe.multiply(two, y));
		SymbolicExpression expected = universe
				.add(y, universe.multiply(two, x));
		Map<SymbolicConstant, SymbolicExpression> map = new HashMap<SymbolicConstant, SymbolicExpression>();
		SymbolicExpression newU;

		map.put(x, y);
		map.put(y, x);
		newU = universe.substituteSymbolicConstants(u, map);
		out.println("sub2:    u = " + u);
		out.println("sub2:  map = " + map);
		out.println("sub2: newU = " + newU);
		assertEquals(expected, newU);
	}

	/**
	 * Tests substitution of symbolic constants applied to an array of symbolic
	 * constants.
	 */
	@Test
	public void subArray1() {
		int n = 3;
		SymbolicConstant[] sc = new SymbolicConstant[n];
		Map<SymbolicConstant, SymbolicExpression> map = new HashMap<SymbolicConstant, SymbolicExpression>();
		SymbolicExpression array1 = universe.symbolicConstant(
				universe.stringObject("a"), universe.arrayType(integerType));
		SymbolicExpression array2;

		for (int i = 0; i < n; i++)
			sc[i] = universe.symbolicConstant(universe.stringObject("x" + i),
					integerType);
		for (int i = 0; i < n; i++)
			map.put(sc[i], sc[n - 1 - i]);
		for (int i = 0; i < n; i++)
			array1 = universe.arrayWrite(array1, universe.integer(i), sc[i]);
		array2 = universe.substituteSymbolicConstants(array1, map);
		out.println("subArray1: array1 = " + array1);
		out.println("subArray1: array2 = " + array2);
		for (int i = 0; i < n; i++)
			assertEquals(sc[n - 1 - i],
					universe.arrayRead(array2, universe.integer(i)));
	}

	@Test
	public void subExpression() {
		SymbolicTupleType tupleType = universe.tupleType(
				universe.stringObject("tup"),
				Arrays.asList(new SymbolicType[] { integerType, realType }));
		SymbolicExpression tuple1 = universe.tuple(
				tupleType,
				Arrays.asList(new SymbolicExpression[] { universe.integer(1),
						universe.rational(3.14) }));
		SymbolicExpression tuple2 = universe.tuple(
				tupleType,
				Arrays.asList(new SymbolicExpression[] { universe.integer(-2),
						universe.rational(2.718) }));
		SymbolicExpression expr = universe.array(tupleType, Arrays
				.asList(new SymbolicExpression[] { tuple1, tuple1, tuple2 }));
		SymbolicExpression expected = universe.array(tupleType, Arrays
				.asList(new SymbolicExpression[] { tuple2, tuple2, tuple2 }));
		Map<SymbolicExpression, SymbolicExpression> map = new HashMap<SymbolicExpression, SymbolicExpression>();
		SymbolicExpression actual;

		map.put(tuple1, tuple2);
		out.println("subExpression:     expr = " + expr);
		out.println("subExpression:     map  = " + map);
		out.println("subExpression: expected = " + expected); 
		actual = universe.substitute(expr, map);
		assertEquals(expected, actual);
	} 
	@Test
	public void newMathTest() {   
		NumericExpression one = mathUniverse.oneReal();
		SymbolicExpression cos = mathUniverse.cosFunction; 
		SymbolicExpression cos2 = mathUniverse.multiply((NumericExpression) cos, (NumericExpression) cos);  
		SymbolicExpression sin = mathUniverse.sinFunction; 
		SymbolicExpression sin2 = mathUniverse.multiply((NumericExpression) sin, (NumericExpression) sin);   
		SymbolicExpression expected = mathUniverse.subtract(one, (NumericExpression) sin2);
		
		out.println("hello");
		out.println(expected);
		SymbolicExpression answer = trigIdentity.simplifyExpression(cos2); 
		assertEquals(expected,answer);
		
	}

}
