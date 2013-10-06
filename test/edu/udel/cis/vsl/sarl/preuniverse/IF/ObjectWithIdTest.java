/* @author Gunjan Majmudar */

package edu.udel.cis.vsl.sarl.preuniverse.IF;


import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
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
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.collections.Collections;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;


public class ObjectWithIdTest {
	
	
	private static PreUniverse universe;
	
	private static SymbolicObject stringObject, objectWithId;
	
	private static Collection test;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FactorySystem test = PreUniverses.newIdealFactorySystem();
		universe =  PreUniverses.newPreUniverse(test);
		stringObject = universe.stringObject("Hello");

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
	public void objectWithIdTest(){
		
		test = universe.objects();
		System.out.print(test);
		objectWithId = universe.objectWithId(40);
		
		assertEquals(universe.stringObject("Hello"), objectWithId);
		
		
	}
}
