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
package edu.udel.cis.vsl.sarl.preuniverse.common;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
//writen by Boyang Luo
public class MakeTest {
	// Universe
	private static PreUniverse universe;
	// SymbolicTypes
	private static SymbolicType integerType;
	private static SymbolicType realType;
	private static SymbolicType booleanType;
	private static SymbolicType  realArray;
	private static SymbolicType intArrayType;
	// SymbolicObjects
	private static SymbolicExpression nullExpression;
	private static SymbolicCompleteArrayType symbolicCompleteArrayType;


	private static SymbolicUnionType union1;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		universe = PreUniverses.newPreUniverse(system);

		// Types
		integerType = universe.integerType();
		booleanType = universe.booleanType();
		realType = universe.realType();
		//arrayType = universe.arrayType(integerType); //creates an array of ints
		realArray = universe.arrayType(realType);


		union1 = universe.unionType(
				universe.stringObject("union1"),
				Arrays.asList(new SymbolicType[] { integerType, realType,
						booleanType, realArray }));



	}

	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	//@Ignore
	@Test
	public void testMake(){
		NumericExpression zero,one,two,three,low,high;
		NumericSymbolicConstant x_var,y_var;
		BooleanExpression resultTrue, resultFalse;
		SymbolicExpression y_minus_x,x_minus_y;
		resultTrue=universe.bool(true);
		resultFalse=universe.bool(false);
		SymbolicType Integer,Bool;// For testing nullExpression() method
		StringObject name = universe.stringObject("name");
		SymbolicType type = universe.integerType(); 
		SymbolicConstant index = universe.symbolicConstant(name, type);
		Integer = universe.integerType();
		Bool = universe.booleanType();
		//Real = universe.realType();


		zero=universe.integer(0);
		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.integer(3);
		low = universe.integer(999);
		high = universe.integer(2000);
				//numberFactory.integer(1);
		
		x_var = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("x"), realType);
		y_var = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("y"), realType);

		//case ADD;
		SymbolicObject[] Args_ADD={x_var,y_var};
		assertEquals(universe.add(y_var,x_var),universe.make(SymbolicOperator.ADD,Integer,Args_ADD));
		//case AND;
		SymbolicObject[] Args_AND1={resultTrue,resultTrue};
		SymbolicObject[] Args_AND2={resultTrue,resultFalse};
		//SymbolicObject[] Args_AND3={resultTrue};
		assertEquals(universe.make(SymbolicOperator.AND,Bool,Args_AND2),resultFalse);
		assertEquals((universe.make(SymbolicOperator.AND,Bool,Args_AND1)),resultTrue);
		//assertEquals((universe.make(SymbolicOperator.AND,Bool,Args_AND3)),resultTrue);
		//case ARRAY_LAMBDA
		SymbolicObject[] Args_Array_Lambda={nullExpression};
		assertEquals(null,universe.make(SymbolicOperator.ARRAY_LAMBDA,symbolicCompleteArrayType,Args_Array_Lambda));
		//case ARRAY_WRITE
		intArrayType = universe.arrayType(integerType);
		SymbolicExpression intArrayTypeExpression = universe.symbolicConstant(
				universe.stringObject("intArrayTypeExpression"), intArrayType);
		SymbolicExpression write = universe.arrayWrite(
				intArrayTypeExpression, two,one);
		SymbolicExpression writeResult = universe.arrayWrite(write,two,one);		
		SymbolicObject[] Args_Array_Write={write,two,one};
		assertEquals(writeResult,universe.make(SymbolicOperator.ARRAY_WRITE,intArrayType,Args_Array_Write));
		
		
		
		//case CONCRETE
		// TODO: don't know how to do
		//SymbolicObject[] Args_Concrete={null};
		//SymbolicObject Concrete_result = universe.canonic();
		//System.out.println(Concrete_result);
		//assertEquals(universe.make(SymbolicOperator.CONCRETE, Integer, Args_Concrete),Concrete_result);
		
		
		
		//case COND
		SymbolicObject[] Args_COND={resultTrue,resultTrue,resultTrue};
		assertEquals(universe.make(SymbolicOperator.COND,Bool,Args_COND),resultTrue);
		//case DIVIDE:
		SymbolicObject[] Args_Divide={two,one};
		assertEquals(universe.make(SymbolicOperator.DIVIDE,Integer,Args_Divide),two);
		//case EXITS
		SymbolicObject[] Args_EXISTS={(NumericSymbolicConstant)index,resultFalse};
		assertEquals(universe.make(SymbolicOperator.EXISTS,Bool,Args_EXISTS),resultFalse);
		//case FORALL
		BooleanExpression testResult1 = 
				universe.forallInt((NumericSymbolicConstant)index, 
						low, high, resultTrue);
		SymbolicObject[] Args_FORALL={(NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("name"), integerType),resultTrue};
		assertEquals(universe.make(SymbolicOperator.FORALL,Bool,Args_FORALL),testResult1);
		//case MULTIPLY
		//SymbolicObject[] testList =new SymbolicObject[] {one};
		ArrayList<SymbolicObject> Args_MULTIPLY =new ArrayList<SymbolicObject>();
		Args_MULTIPLY.add(one);
		//assertEquals(universe.make(SymbolicOperator.MULTIPLY,Integer,testList),testList);
		//case MODULO
		SymbolicObject[] Args_Modulo= {three, one};
		assertEquals(universe.make(SymbolicOperator.MODULO,Integer,Args_Modulo), zero);
		//case NEGATIVE;
		y_minus_x = universe.subtract(y_var,x_var);
		x_minus_y = universe.subtract(x_var,y_var);
		SymbolicObject[] Args_Negative={x_minus_y};
		assertEquals(y_minus_x,universe.make(SymbolicOperator.NEGATIVE,Integer,Args_Negative));
		//case NEQ
		SymbolicObject[] Args_NEQ={resultTrue,resultTrue};
		assertEquals(universe.make(SymbolicOperator.NEQ,Bool,Args_NEQ),resultFalse);
		//case NOT:
		SymbolicObject[] Args_NOT={resultTrue};
		assertEquals(universe.make(SymbolicOperator.NOT,Bool,Args_NOT),resultFalse);
		//case OR;
		//TODO: Or case with one Args.
		SymbolicObject[] Args_OR1={resultTrue,resultTrue};
		SymbolicObject[] Args_OR2={resultFalse,resultFalse};
		//SymbolicObject[] Args_OR3={resultTrue};
		assertEquals(universe.make(SymbolicOperator.OR,Bool,Args_OR2),resultFalse);
		assertEquals((universe.make(SymbolicOperator.OR,Bool,Args_OR1)),resultTrue);
		//System.out.println(universe.make(SymbolicOperator.OR,Bool,Args_OR3));
		//assertEquals((universe.make(SymbolicOperator.OR,Bool,Args_OR3)),resultTrue);
		//case POWER:
		IntObject I1;
		I1 = universe.intObject(1);
		SymbolicObject[] Args_Power1= {one, one};
		SymbolicObject[] Args_Power2= {one, I1};
		SymbolicExpression result = universe.make(SymbolicOperator.POWER,Integer,Args_Power1);		
		assertEquals(universe.make(SymbolicOperator.POWER,Integer,Args_Power1), result);
		SymbolicExpression result1 = universe.make(SymbolicOperator.POWER,Integer,Args_Power2);		
		assertEquals(universe.make(SymbolicOperator.POWER,Integer,Args_Power2), result1);		
		//case SUBTRACT:
		SymbolicObject[] Args_Substract= {three, one};
		assertEquals(universe.make(SymbolicOperator.SUBTRACT,Integer,Args_Substract), two);
		//case TUPLE_WRITE
		SymbolicTupleType tupleType1;
		SymbolicExpression tuple, resultedTuple;
		IntObject i1;
		i1 = universe.intObject(1);
		tupleType1 = universe.tupleType(universe.stringObject("tupleType1"), Arrays.asList(new SymbolicType[]{integerType,integerType}));
		tuple = universe.tuple(tupleType1, Arrays.asList(new SymbolicExpression[]{universe.integer(1),universe.integer(2)}));
		resultedTuple = universe.tupleWrite(tuple, i1, universe.integer(2));
		SymbolicObject[] Args_TupleWrite={tuple,i1,two};
		assertEquals(universe.make(SymbolicOperator.TUPLE_WRITE,Integer,Args_TupleWrite),resultedTuple);
		//case UNION_INJECT
		// finished by Jeff DiMarco (jdimarco) 10/3/13. Not sure who started this
		LinkedList<SymbolicType> memberTypes;
		memberTypes = new LinkedList<SymbolicType>();
		memberTypes.add(integerType);
		memberTypes.add(realType);
		IntObject I0 = universe.intObject(0);
		union1 = universe.unionType(
				universe.stringObject("union1"),
				Arrays.asList(new SymbolicType[] { integerType, realType,
						booleanType, realArray }));

		SymbolicExpression symbolicExpr2 = universe.unionInject(union1, I0, universe.integer(5));
		SymbolicObject[] Args_Union_Inject={I0,universe.integer(5)};
		SymbolicExpression symbolicExpr3 =universe.make(SymbolicOperator.UNION_INJECT,union1,Args_Union_Inject);
		assertEquals(symbolicExpr2, symbolicExpr3);
		//case UNION_TEST
		// test written by Jeff DiMarco (jdimarco) 10/3/13. uses variables from UNION_INJECT case
		// TODO: failing test
		BooleanExpression ans1 = universe.unionTest(I0, symbolicExpr2);
		SymbolicObject[] Args_Union_Test = {I0, symbolicExpr2};
		symbolicExpr3 = universe.make(SymbolicOperator.UNION_TEST, union1, Args_Union_Test);
		assertEquals(ans1, universe.bool(true));
		assertEquals(symbolicExpr3, ans1);
		
	}

}
