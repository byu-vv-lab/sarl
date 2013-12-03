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

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.universe.Universes;

public class UnionTest {

	private SymbolicUniverse universe;
	private SymbolicType intType, realType, booleanType;
	private SymbolicArrayType realArray, unionArray;

	/**
	 * union1 is union of intType, realType, booleanType, realArray
	 */
	private SymbolicUnionType union1;

	/**
	 * a has type array-of-union1. Its elements are: 2/3, true, and 10. Or more
	 * precisely: inject(1, 2/3), inject(2, true), and inject(0, 10).
	 */
	SymbolicExpression a;

	// private BooleanExpressionFactory booleanFactory;

	@Before
	public void setUp() throws Exception {
		universe = Universes.newIdealUniverse();
		// FactorySystem system = PreUniverses.newIdealFactorySystem();

		// booleanFactory = system.booleanFactory();
		intType = universe.integerType();
		realType = universe.realType();
		booleanType = universe.booleanType();
		realArray = universe.arrayType(realType);
		union1 = universe.unionType(
				universe.stringObject("union1"),
				Arrays.asList(new SymbolicType[] { intType, realType,
						booleanType, realArray }));
		unionArray = universe.arrayType(union1);
		a = universe.symbolicConstant(universe.stringObject("a"), unionArray);
		a = universe.arrayWrite(
				a,
				universe.integer(0),
				universe.unionInject(union1, universe.intObject(1),
						universe.rational(2, 3)));
		a = universe.arrayWrite(
				a,
				universe.integer(1),
				universe.unionInject(union1, universe.intObject(2),
						universe.bool(true)));
		a = universe.arrayWrite(
				a,
				universe.integer(2),
				universe.unionInject(union1, universe.intObject(0),
						universe.integer(10)));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void injectExtract1() {
		SymbolicExpression ten = universe.integer(10);
		SymbolicExpression u_ten = universe.unionInject(union1,
				universe.intObject(0), ten);

		assertEquals(union1, u_ten.type());
		assertEquals(ten, universe.unionExtract(universe.intObject(0), u_ten));
	}

	@Test
	public void extractInject() {
		SymbolicConstant x = universe.symbolicConstant(
				universe.stringObject("x"), union1);
		SymbolicExpression x_real = universe.unionExtract(
				universe.intObject(1), x);

		assertEquals(union1, x.type());
		assertEquals(realType, x_real.type());
		assertEquals(x,
				universe.unionInject(union1, universe.intObject(1), x_real));
	}

	@Test
	public void arrayOfUnion() {

		System.out.println(a);
		assertEquals(
				universe.rational(2, 3),
				universe.unionExtract(universe.intObject(1),
						universe.arrayRead(a, universe.integer(0))));
		assertEquals(
				universe.bool(true),
				universe.unionExtract(universe.intObject(2),
						universe.arrayRead(a, universe.integer(1))));
		assertEquals(
				universe.integer(10),
				universe.unionExtract(universe.intObject(0),
						universe.arrayRead(a, universe.integer(2))));
	}

	@Test
	public void unionTestTest() {

		SymbolicExpression ten = universe.integer(10);
		SymbolicExpression u_ten = universe.unionInject(union1,
				universe.intObject(0), ten);

		SymbolicExpression test0 = universe.unionTest(universe.intObject(0),
				u_ten);
		SymbolicExpression test1 = universe.unionTest(universe.intObject(1),
				u_ten);
		SymbolicExpression test2 = universe.unionTest(universe.intObject(2),
				u_ten);

		assertEquals(universe.bool(true), test0);
		assertEquals(universe.bool(false), test1);
		assertEquals(universe.bool(false), test2);
	}

	@Test
	public void abstractUnionTestTest() {

		SymbolicExpression x = universe.symbolicConstant(
				universe.stringObject("x"), union1);

		SymbolicExpression test0 = universe.unionTest(universe.intObject(0), x);

		assertEquals(booleanType, test0.type());
		assertEquals(SymbolicExpression.SymbolicOperator.UNION_TEST,
				test0.operator());
		assertEquals(universe.intObject(0), test0.argument(0));
		assertEquals(x, test0.argument(1));

	}

	@Test
	public void castInject() {
		NumericExpression x = universe.rational(5, 6);
		SymbolicExpression u_x = universe.unionInject(union1,
				universe.intObject(1), x);
		SymbolicExpression cast_x = universe.cast(union1, x);

		assertEquals(union1, u_x.type());
		assertEquals(union1, cast_x.type());
		assertEquals(u_x, cast_x);
	}
}
