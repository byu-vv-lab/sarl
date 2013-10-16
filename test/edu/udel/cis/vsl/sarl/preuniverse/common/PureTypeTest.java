/* @author Gunjan Majmudar */


package edu.udel.cis.vsl.sarl.preuniverse.common;


import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

public class PureTypeTest {
	
	private static PreUniverse universe;

	private static SymbolicType realType, integerType, booleanType;
	
	private static SymbolicArrayType array1;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FactorySystem test = PreUniverses.newIdealFactorySystem();
		universe = new CommonPreUniverse(test);
		integerType = universe.integerType();
		realType = universe.realType();
		booleanType = universe.booleanType();
		array1 = universe.arrayType(integerType);
		
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
	
	@Test
	public void pureTypeObjectKindTest(){
		
		SymbolicType pureType2 = universe.pureType(array1);
		SymbolicType pureType4 = universe.pureType(booleanType);
		assertEquals(pureType2.symbolicObjectKind(), pureType4.symbolicObjectKind());
		
	}
	
}