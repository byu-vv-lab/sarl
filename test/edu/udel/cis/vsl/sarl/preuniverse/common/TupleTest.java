package edu.udel.cis.vsl.sarl.preuniverse.common;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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

public class TupleTest {
	private static PreUniverse universe;

	private static SymbolicType integerType;

	private static SymbolicType realType;
	private static NumericExpression one, two, three, five;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FactorySystem system = PreUniverses.newIdealFactorySystem();

		universe = PreUniverses.newPreUniverse(system);
		integerType = universe.integerType();
		realType = universe.realType();
		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.rational(3);
		five = universe.rational(5);

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
	public void tupleTypeTest(){
		
		SymbolicTupleType tupleType1 = universe.tupleType(universe.stringObject("tupleType1"), Arrays.asList(new SymbolicType[]{integerType,integerType,realType}));
		SymbolicTupleType tupleType2 = universe.tupleType(universe.stringObject("tupleType1"), Arrays.asList(new SymbolicType[]{integerType,integerType,realType}));
		SymbolicTupleType tupleType3;
		SymbolicTypeSequence sequence;
		LinkedList<SymbolicType> members = new LinkedList<>();
		members.add(integerType);
		members.add(integerType);
		members.add(realType);
		tupleType3 = universe.tupleType(universe.stringObject("tupleType1"), members);
	
		assertEquals(SymbolicTypeKind.TUPLE,tupleType1.typeKind());

		sequence = tupleType1.sequence();
		assertEquals(integerType, sequence.getType(0));
		assertEquals(integerType, sequence.getType(1));
		assertEquals(realType, sequence.getType(2));
		assertEquals(universe.stringObject("tupleType1"), tupleType1.name());
		
		assertEquals(tupleType1, tupleType2);
		assertEquals(3,sequence.numTypes());
		
		assertEquals(tupleType1,tupleType3);
		
		members.remove();
		members = null;
		assertEquals(tupleType1,tupleType3);
		
	
	
	}
	
	@Test(expected= SARLException.class)
	public void tupleTest1(){
		
		SymbolicTupleType tupleType1 = universe.tupleType(universe.stringObject("tupleType1"), Arrays.asList(new SymbolicType[]{integerType,integerType,realType}));
		SymbolicExpression tuple = universe.tuple(tupleType1, Arrays.asList(new SymbolicExpression[]{universe.integer(1),universe.integer(2)}));
	}
	@Test(expected= SARLException.class)
	public void tupleTest2(){
		SymbolicTupleType tupleType1 = universe.tupleType(universe.stringObject("tupleType1"), Arrays.asList(new SymbolicType[]{integerType,integerType,realType}));
		
		SymbolicExpression tuple = universe.tuple(tupleType1, Arrays.asList(new SymbolicExpression[]{universe.rational(1),universe.integer(2),universe.integer(2)}));

		
	}
		
	
	
	


}
