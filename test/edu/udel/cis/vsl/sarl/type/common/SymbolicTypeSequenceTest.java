package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType.RealKind;
/**
 * @author alali
 * 
 * Testing CommonSymbolicTypeSequence
 * - computeHashCode()
 * - numTypes()
 * - getType()
 * - intrinsicEquals()
 * - typeComparator()
 */
public class SymbolicTypeSequenceTest {

	/**
	 * Declaring two TypeSequences to be used for testing
	 */
	CommonSymbolicTypeSequence typeSequence, typeSequence2, typeSequence3, typeSequence4;
	
	/**
	 * Declaring two IntegerTypes to be added in the typeSequence
	 */
	CommonSymbolicIntegerType idealIntKind, boundedIntKind;
	
	/**
	 * Declaring two realTypes to be added in the typeSequence
	 */
	CommonSymbolicRealType idealRealKind, floatRealKind;
	
	/**
	 * lists to be filled with different SymbolicTypes 
	 * to create SymbolicTypeSequences.
	 */
	ArrayList<CommonSymbolicType> typesList, typesList2;
	
	/**
	 * arrays of SymbolicTypes to be used
	 * in creating TypeSequences
	 */
	CommonSymbolicType[] typesArray, typesArray2;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Instantiating different objects to be used for testing
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		//System.out.println(1);
		typesList = new ArrayList<CommonSymbolicType>();
		typesList2 = new ArrayList<CommonSymbolicType>();
		typesArray = new CommonSymbolicType[4];
		typesArray2 = new CommonSymbolicType[4];
		idealIntKind = new CommonSymbolicIntegerType(IntegerKind.IDEAL);
		boundedIntKind = new CommonSymbolicIntegerType(IntegerKind.BOUNDED);
		idealRealKind = new CommonSymbolicRealType(RealKind.IDEAL);
		floatRealKind = new CommonSymbolicRealType(RealKind.FLOAT);
		
		typesList.add(idealIntKind);
		typesList.add(boundedIntKind);
		typesList.add(idealRealKind);
		typesList.add(floatRealKind);
		
		typesList2.add(idealRealKind);
		typesList2.add(floatRealKind);
		typesList2.add(idealIntKind);
		typesList2.add(boundedIntKind);
		
		typesArray[0] = idealIntKind;
		typesArray[1] = boundedIntKind;
		typesArray[2] = idealRealKind;
		typesArray[3] = floatRealKind;
		
		typesArray2[0] = idealIntKind;
		typesArray2[1] = idealIntKind;
		typesArray2[2] = idealRealKind;
		typesArray2[3] = floatRealKind;
		
		/*
			I have to put those instantiations at the end of the method
		 	after adding element to the list. Otherwise the list will be empty
		 	when creating the TypeSequences.
		 * 
		 */
		typeSequence = new CommonSymbolicTypeSequence(typesList);
		typeSequence2 = new CommonSymbolicTypeSequence(typesList2);
		typeSequence3 = new CommonSymbolicTypeSequence(typesArray);
		typeSequence4 = new CommonSymbolicTypeSequence(typesArray2);
		//System.out.println(2);
	}

	@After
	public void tearDown() throws Exception {
	}


	/**
	 * testing the hash code for different type sequences
	 * two type sequences have to be identical to have the same hash code.
	 */
	@Test
	public void testComputeHashCode() {
		assertNotEquals(typeSequence.computeHashCode(), typeSequence2.computeHashCode());
		assertEquals(typeSequence.computeHashCode(), typeSequence3.computeHashCode());
	}
	
	/**
	 * testing the size of the type sequence that we've created above
	 */
	@Test
	public void testNumTypes() {
		//System.out.println(3);
		assertEquals(typeSequence.numTypes(), 4);
		assertEquals(typeSequence3.numTypes(), 4);
	}
	
	/**
	 * testing the types of the elements of the typeSequence
	 */
	@Test
	public void testGetType() {
		//System.out.println(3);		
		assertTrue(typeSequence.getType(0) instanceof CommonSymbolicIntegerType);
		assertTrue(typeSequence.getType(1) instanceof CommonSymbolicIntegerType);
		assertTrue(typeSequence.getType(2) instanceof CommonSymbolicRealType);
		assertTrue(typeSequence.getType(3) instanceof CommonSymbolicRealType);
		assertTrue(typeSequence3.getType(0) instanceof CommonSymbolicIntegerType);
	}
	

	/**
	 * testing if two typeSequences are "equal", i.e. having the same elements
	 * and are in the same order, i.e having the same hash code
	 */
	@Test
	public void testIntrinsicEquals() {
		//System.out.println(3);
		assertFalse(typeSequence.intrinsicEquals(typeSequence2));
		assertTrue(typeSequence.intrinsicEquals(typeSequence3));
		assertFalse(typeSequence4.intrinsicEquals(typeSequence));
		assertFalse(typeSequence4.intrinsicEquals(boundedIntKind));
	}
	
	/**
	 * testing the string representation of the typeSequecne
	 */
	@Test
	public void testToString() {
		assertEquals(typeSequence.toString(), typeSequence3.toString());
	}
	
	
	/**
	 * this test checks the type of the first element in the sequence 
	 */
	@Test
	public void testIterator()
	{
//		System.out.println(typeSequence.iterator().next());
//		System.out.println(typeSequence2.iterator().next());
//		System.out.println(typeSequence3.iterator().next());
//		System.out.println(typeSequence4.iterator().next());
		assertEquals(typeSequence.iterator().next(), typeSequence3.iterator().next());
		assertEquals(typeSequence3.iterator().next(), typeSequence4.iterator().next());
		assertNotEquals(typeSequence.iterator().next(), typeSequence2.iterator().next());
	}
}
