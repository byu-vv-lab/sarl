/*Author: Gunjan Majmudar */

package edu.udel.cis.vsl.sarl.preuniverse.common;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.expr.common.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

public class FunctionCaseEqualsTest {
	
	private static PreUniverse universe;

	private static SymbolicType realType, integerType;
	
	private static SymbolicExpression addition1, addition2;
	
	private static NumericSymbolicConstant x, y, z;

	private static SymbolicType functionType1, functionType2, functionType3;
	
	private static SymbolicTypeSequence sequence1, sequence2, sequence3;
	
	private static BooleanExpression value1, value2, value, trueExpr, falseExpr;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FactorySystem test = PreUniverses.newIdealFactorySystem();
		universe = new CommonPreUniverse(test);
		integerType = universe.integerType();
		realType = universe.realType();
		trueExpr = universe.trueExpression();
		falseExpr = universe.falseExpression();
		addition1 = universe.add(universe.add(x,y),z);
		addition2 = universe.add(universe.add(y,z), x);
		
		
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	
	@Test
	public void FunctionCaseEqualsTest1(){
		SymbolicTupleType tupleType1 = universe.tupleType(universe.stringObject("SequenceofInteger"), Arrays.asList(new SymbolicType[]{integerType,integerType,integerType}));
		SymbolicTupleType tupleType2 = universe.tupleType(universe.stringObject("Sequenceofreals"), Arrays.asList(new SymbolicType[]{realType,realType,realType}));
		SymbolicTupleType tupleType3 = universe.tupleType(universe.stringObject("SequenceofInteger"), Arrays.asList(new SymbolicType[]{integerType,integerType,integerType}));
		sequence1 = tupleType1.sequence();
		sequence2 = tupleType2.sequence();
		sequence2 = tupleType3.sequence();
		
		functionType1 = universe.functionType(sequence1, realType);
		functionType2 = universe.functionType(sequence2, integerType);
		functionType3 = universe.functionType(sequence3, realType);
		value1 = universe.compatible(functionType1, functionType2);
		value2 = universe.compatible(functionType1, functionType3);
		
		assertEquals(falseExpr, value1);
		assertEquals(trueExpr, value2);
	}
	
	@Test
	public void FunctionCaseEqualsTest2(){
		
		addition1 = universe.add(universe.add(x,y),z);
		addition2 = universe.add(universe.add(y,z),x);
		value = universe.equals(addition1, addition2);
		assertEquals (trueExpr, value);
		
	}
}


