/* @author Gunjan Majmudar */


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

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

public class PureTypeTest {
	
	private static PreUniverse universe;

	private static SymbolicType realType, integerType;

	private static SymbolicType pureType;

	SymbolicTypeKind three;

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
	public void pureTypeKindTest(){
		
		SymbolicType pureType1 = universe.pureType(integerType);
		SymbolicType pureType2 = universe.pureType(integerType);
		assertEquals(pureType1.typeKind(), pureType2.typeKind());
		
	}
	
}