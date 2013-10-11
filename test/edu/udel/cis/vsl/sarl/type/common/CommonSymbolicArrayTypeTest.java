package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType.RealKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

/**
 * @author alali
 * Testing CommonSymbolicArrayType
 *  - typeEquals()
 *  - computeHashCode()
 *  - elementType()
 *  - toStringBuffer()
 *  - isComplete()
 *  - canonizedChildren() - not yet tested
 *  - getPureType() - not yet tested
 *  - setPureType() - not yet tested.
 */

public class CommonSymbolicArrayTypeTest {
		
	CommonSymbolicArrayType intArrayType1, intArrayType11, intArrayType2, realArrayType1, realArrayType2;
	CommonSymbolicIntegerType idealIntKind, idealIntKind2, boundedIntKind;
	CommonSymbolicRealType idealRealKind, floatRealKind;
	TypeComparator typeComparator;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Initiating the variables.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		typeComparator = new TypeComparator();
		idealIntKind = new CommonSymbolicIntegerType(IntegerKind.IDEAL);
		idealIntKind2 = new CommonSymbolicIntegerType(IntegerKind.IDEAL);
		boundedIntKind = new CommonSymbolicIntegerType(IntegerKind.BOUNDED);
		idealRealKind = new CommonSymbolicRealType(RealKind.IDEAL);
		floatRealKind = new CommonSymbolicRealType(RealKind.FLOAT);
		intArrayType1 = new CommonSymbolicArrayType(idealIntKind);
		intArrayType11 = new CommonSymbolicArrayType(idealIntKind2);
		intArrayType2 = new CommonSymbolicArrayType(boundedIntKind);
		realArrayType1 = new CommonSymbolicArrayType(idealRealKind);
		realArrayType2 = new CommonSymbolicArrayType(floatRealKind);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Testing the hash code of two array type
	 * They should have the same Hash Code if they're identical
	 */
	@Test
	public void testComputeHashCode() {
		assertEquals(intArrayType1.computeHashCode(), intArrayType11.computeHashCode());
		assertNotEquals(intArrayType1.computeHashCode(), intArrayType2.computeHashCode());
	}
	
	/**
	 * Testing if two types are equals, i.e. the have the same type and kins.
	 */
	@Test
	public void testTypeEquals() {
		
		assertTrue(intArrayType1.typeEquals(intArrayType11));
		assertFalse(intArrayType1.typeEquals(intArrayType2));
	}
	
	/**
	 * Testing the Kinds of the types of the different array types
	 * making sure that they've the same type as the enumerated predefined Kinds.
	 */
	@Test
	public void testElementType() {
		
		//assertEquals(intArrayType1.elementType(), new CommonSymbolicIntegerType(IntegerKind.IDEAL));
		assertTrue(intArrayType2.elementType() instanceof SymbolicType);
		//assertEquals(realArrayType1.elementType(), new CommonSymbolicRealType(RealKind.IDEAL));
		assertEquals(((CommonSymbolicIntegerType)intArrayType11.elementType()).integerKind(), IntegerKind.IDEAL);
		assertEquals(((CommonSymbolicIntegerType)intArrayType1.elementType()).integerKind(), IntegerKind.IDEAL);
		assertEquals(((CommonSymbolicIntegerType)intArrayType2.elementType()).integerKind(), IntegerKind.BOUNDED);
		assertEquals(((CommonSymbolicRealType)realArrayType1.elementType()).realKind(), RealKind.IDEAL);
		assertEquals(((CommonSymbolicRealType)realArrayType2.elementType()).realKind(), RealKind.FLOAT);
	}
	
	/**
	 * Testing toStringBuffer for two identical array types.
	 */
	@Test
	public void testToStringBuffer() {
		assertEquals(intArrayType1.toStringBuffer(true).toString(), intArrayType11.toStringBuffer(true).toString());
	}
	
	
	/**
	 *  Testing to make sure that this array type isn't complete.
	 */
	@Test
	public void testIsComplete() {
		assertFalse(intArrayType1.isComplete());
	}
	

	/**
	 * testing if two array types are equal using
	 * compareTo in TypeComparator
	 */
	@Test
	public void testTypeComparator(){
		assertEquals(typeComparator.compare(intArrayType1, intArrayType11), 0);
	}
}
