/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.preuniverse;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.common.CommonPreUniverse;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
import edu.udel.cis.vsl.sarl.universe.Universes;

public class UnionTest {

	private SymbolicUniverse universe;
	private SymbolicType intType, realType, booleanType, integerType;
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
	
	private SymbolicTypeFactory typeFactory;

	@Before
	public void setUp() throws Exception {
		universe = Universes.newIdealUniverse();
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		
		typeFactory = system.typeFactory();
		intType = universe.integerType();
		integerType = universe.integerType();
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

	@Test (expected = SARLException.class)
	public void injectErrorsTest(){
		SymbolicExpression ten = universe.integer(10);
		universe.unionInject(union1, universe.intObject(-1), ten);
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
	public void abstractUnionTestTest(){
		
		SymbolicExpression x = universe.symbolicConstant(universe.stringObject("x"), union1);
		
		SymbolicExpression test0 = universe.unionTest(universe.intObject(0), x);
		
		assertEquals(booleanType, test0.type());
		assertEquals(SymbolicExpression.SymbolicOperator.UNION_TEST, test0.operator());
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
	
	@Test (expected = SARLException.class)
	// Test written by Jeff DiMarco (jdimarco) 9/24/13
	public void injectErrorNegativeIndex(){
		SymbolicExpression ten = universe.integer(10);
		universe.unionInject(union1, universe.intObject(-1), ten);
	}
	
	@Test (expected = SARLException.class)
	// Test written by Jeff DiMarco (jdimarco) 9/24/13
	public void injectErrorLargerIndex(){
		SymbolicExpression ten = universe.integer(10);
		// union1 has 4 elements, try index 107
		universe.unionInject(union1, universe.intObject(107), ten);
	}
	
	@Test (expected = SARLException.class)
	// Test written by Jeff DiMarco (jdimarco) 9/24/13
	public void injectErrorIncompatibleTypes(){
		SymbolicExpression trueExpr = universe.bool(true);
		// union1 has type intType for index 0, try injecct booleanType
		universe.unionInject(union1, universe.intObject(0), trueExpr);
	}
	// written by Mohammad Alsulmi
	@Test
	public void testCompatibleWithUnion(){
		
		// here we test compatible with tuple types 
		SymbolicUnionType type1, type2, type3, type5;
		SymbolicType type4;
		BooleanExpression result, expected;
		
		
		type1 = universe.unionType(universe.stringObject("Type1"), Arrays.asList(new SymbolicType[]{integerType,realType}));
		type2 = universe.unionType(universe.stringObject("Type1"), Arrays.asList(new SymbolicType[]{integerType,realType}));		
		type3 = universe.unionType(universe.stringObject("type3"),Arrays.asList(new SymbolicType[]{realType, integerType}));
		type5 = universe.unionType(universe.stringObject("Type1"), Arrays.asList(new SymbolicType[]{integerType,universe.booleanType()}));
		type4 = universe.booleanType();
		
		// here we compare two identical unions types (type1, type2)
		// the expected compatible call should return true
		expected = universe.bool(true);
		result = universe.compatible(type1, type2);
		assertEquals(expected, result);
		
		// here we compare two different unions types (type1, type3)
		// the expected compatible call should return false
		expected = universe.bool(false);
		result  = universe.compatible(type1, type3);
		assertEquals(expected, result);
		
		// here we compare a union type with boolean type (type1, type4)
		// the expected compatible call should return true
		expected = universe.bool(false);
		result  = universe.compatible(type1, type4);
		assertEquals(expected, result);
		
		// here we compare two different tuple types (type1, type5), but they have the same name
		// the expected compatible call should return false
		expected = universe.bool(false);
		result  = universe.compatible(type1, type5);
		assertEquals(expected, result);

	}
	
	@Test
	// Test written by Jeff DiMarco (jdimarco) 9/24/13
	public void typeSequenceSymbolicTypeArray() {
		SymbolicType[] typeArray = {typeFactory.booleanType(), typeFactory.integerType()};
		SymbolicTypeSequence typeSequence;
		SymbolicTypeSequence expectedTypeSequence;
		
		CommonPreUniverse commonUniverse = (CommonPreUniverse)universe;
		typeSequence = commonUniverse.typeSequence(typeArray);
		expectedTypeSequence = typeFactory.sequence(typeArray);
		
		assertEquals(expectedTypeSequence.numTypes(), typeSequence.numTypes());
		assertEquals(expectedTypeSequence.getType(0), typeSequence.getType(0));
		assertEquals(expectedTypeSequence.getType(1), typeSequence.getType(1));
	}

	@Test
	// Written by Jeff DiMarco(jdimarco) 9/20/13
	public void unionTypeStringObjectSymbolicTypeSequence() {
		LinkedList<SymbolicType> memberTypes = new LinkedList<SymbolicType>();
		SymbolicUnionType unionType;
		SymbolicTypeSequence sequence;
		CommonPreUniverse commonUniverse = (CommonPreUniverse)universe;

		memberTypes.add(integerType);
		memberTypes.add(realType);
		sequence = ((CommonPreUniverse) universe).typeSequence(memberTypes);
		
		unionType = commonUniverse.unionType(universe.stringObject("MyUnion"),
				sequence);
		
		assertEquals(SymbolicTypeKind.UNION, unionType.typeKind());
		sequence = unionType.sequence();
		assertEquals(integerType, sequence.getType(0));
		assertEquals(realType, sequence.getType(1));
		assertEquals(2, sequence.numTypes());
		assertEquals(universe.stringObject("MyUnion"), unionType.name());
	}

	@Test
	public void unionTypeTest() {
		LinkedList<SymbolicType> memberTypes = new LinkedList<SymbolicType>();
		SymbolicUnionType unionType;
		SymbolicTypeSequence sequence;

		memberTypes.add(integerType);
		memberTypes.add(realType);
		unionType = universe.unionType(universe.stringObject("MyUnion"),
				memberTypes);
		assertEquals(SymbolicTypeKind.UNION, unionType.typeKind());
		sequence = unionType.sequence();
		assertEquals(integerType, sequence.getType(0));
		assertEquals(realType, sequence.getType(1));
		assertEquals(2, sequence.numTypes());
		assertEquals(universe.stringObject("MyUnion"), unionType.name());
	}

}
