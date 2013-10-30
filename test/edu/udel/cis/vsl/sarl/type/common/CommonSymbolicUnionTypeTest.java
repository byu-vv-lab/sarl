package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType.RealKind;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.object.Objects;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;

public class CommonSymbolicUnionTypeTest {
	
	/**
	 * an ObjectFactory object that is used to instantiate the typeFactory, and typeFactory2
	 */
	ObjectFactory objectFactory;
	/**
	 * a NumboerFactory that is used to instantiate the objectFactory
	 */
	NumberFactory numberFactory;
	
	CommonSymbolicUnionType unionType, unionType2;
	
	/**
	 * an array of SymbolicTypes to be used in creating the SequenceType
	 */
	CommonSymbolicType typesArray[];
	
	/**
	 * integer types to be used in creating the SequenceType
	 */
	CommonSymbolicIntegerType idealIntKind, boundedIntKind;
	
	/**
	 * Real types to be used in creating the SequenceType
	 */
	CommonSymbolicRealType idealRealKind, floatRealKind;
	
	CommonSymbolicTypeSequence typeSequence;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		numberFactory = Numbers.REAL_FACTORY;
		objectFactory = Objects.newObjectFactory(numberFactory);
		
		idealIntKind = new CommonSymbolicIntegerType(IntegerKind.IDEAL);
		boundedIntKind = new CommonSymbolicIntegerType(IntegerKind.BOUNDED);
		idealRealKind = new CommonSymbolicRealType(RealKind.IDEAL);
		floatRealKind = new CommonSymbolicRealType(RealKind.FLOAT);
		typesArray = new CommonSymbolicType[4];
				
		//an array of CommonSymbolicType
		typesArray[0] = idealRealKind;
		typesArray[1] = floatRealKind;
		typesArray[2] = idealIntKind;
		typesArray[3] = boundedIntKind;
				
		typeSequence = new CommonSymbolicTypeSequence(typesArray);
		unionType = new CommonSymbolicUnionType(objectFactory.stringObject("myUnion"),
				typeSequence);
		unionType2 = new CommonSymbolicUnionType(objectFactory.stringObject("myUnion2"),
				typeSequence);
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testComputeHashCode() {
		assertNotEquals(unionType.computeHashCode(), unionType2.computeHashCode());
	}
	
	@Test
	public void testTypeEquals() {
		assertFalse(unionType.typeEquals(unionType2));
	}
	
	@Test
	public void testName() {
		assertNotEquals(unionType.name(), unionType2.name());
	}
	
	@Test
	public void testSequence() {
		assertEquals(unionType.sequence(), unionType2.sequence());
	}
	
	@Test
	public void testPureType(){
		assertNull(unionType.getPureType());
		unionType.setPureType(unionType);
		assertNotNull(unionType.getPureType());
	}
/*

	@Test
	public void testToStringBuffer() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndexOfType() {
		fail("Not yet implemented");
	}
*/
}
