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
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

public class FunctionTypeTest {
	
	private static PreUniverse universe;

	private static SymbolicType realType, integerType,tupleType;

	private static SymbolicType functionType1, functionType2;

	private static SymbolicTypeKind three;
	
	private static SymbolicTypeSequence sequence1, sequence2;

	private static PrintStream out = System.out; 
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FactorySystem test = PreUniverses.newIdealFactorySystem();
		universe = new CommonPreUniverse(test);
		integerType = universe.integerType();
		realType = universe.realType();
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
	public void FunctionTypeTest1(){
		SymbolicTupleType tupleType1 = universe.tupleType(universe.stringObject("tupleType1"), Arrays.asList(new SymbolicType[]{integerType,integerType,integerType}));
		SymbolicTupleType tupleType2 = universe.tupleType(universe.stringObject("tupleType2"), Arrays.asList(new SymbolicType[]{integerType,integerType,integerType}));
		sequence1 = tupleType1.sequence();
		sequence2 = tupleType2.sequence();
		functionType1 = universe.functionType(sequence1, realType);
		functionType2 = universe.functionType(sequence1, realType);
		assertEquals(functionType1.typeKind(),functionType2.typeKind());
	}
}
