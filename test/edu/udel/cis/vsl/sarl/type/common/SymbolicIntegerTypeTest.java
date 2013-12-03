package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;

/**
 * @author alali
 *
 *	Testing CommonSymbolicIntegerType:
 *	- IntegerKind integerKind()
 *	- typeEquals(...)
 *	- int computeHashCode()
 *	- StringBuffer toStringBuffer(...)
 *	- isHerbrand()
 *	- isIdeal()
 *	- SymbolicType getPureType()
 */
public class SymbolicIntegerTypeTest {
	
		CommonSymbolicIntegerType idealIntKind, idealIntKind2, boundedIntKind, herbrandIntKind;
		TypeComparator typeComparator;
		@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		typeComparator = new TypeComparator();
		idealIntKind = new CommonSymbolicIntegerType(IntegerKind.IDEAL);
		idealIntKind2 = new CommonSymbolicIntegerType(IntegerKind.IDEAL);
		boundedIntKind = new CommonSymbolicIntegerType(IntegerKind.BOUNDED);
		herbrandIntKind = new CommonSymbolicIntegerType(IntegerKind.HERBRAND);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * testing integer kind from created objects 
	 * with the enum type (IntegerKind)
	 */
	@Test
	public void testIntegerKind() {
		assertEquals(idealIntKind.integerKind(), IntegerKind.IDEAL);
		assertEquals(boundedIntKind.integerKind(), IntegerKind.BOUNDED);
		assertEquals(herbrandIntKind.integerKind(), IntegerKind.HERBRAND);
	}
	
	/**
	 * testing the same integer type and two identical integer types.
	 */
	@Test
	public void testTypeEquals() {
		assertTrue(idealIntKind.typeEquals(idealIntKind));
		assertTrue(idealIntKind.typeEquals(idealIntKind2));
		assertFalse(idealIntKind.typeEquals(boundedIntKind));
	}
	
	
	/**
	 * testing if the created herbrand integer types are really herbrand.
	 */
	@Test
	public void testIsHerbrand() {
		assertTrue(herbrandIntKind.isHerbrand());
		assertFalse(idealIntKind.isHerbrand());
		assertFalse(boundedIntKind.isHerbrand());
	}
	
	/**
	 * testing if the created ideal integer types are really Ideal!
	 */
	@Test
	public void testIsIdeal() {
		assertTrue(idealIntKind.isIdeal());
		assertFalse(boundedIntKind.isIdeal());
		assertFalse(herbrandIntKind.isIdeal());
	}
	
	/**
	 * testing the hash code for two integer types
	 * they've to have the same hash code if they're identical
	 */
	@Test
	public void testComputeHashCode() {
		assertEquals(idealIntKind.computeHashCode(),idealIntKind2.computeHashCode());
	}
	
	/**
	 * testing the string representation for the different integer types
	 */
	@Test
	public void testToStringBuffer() {
		assertEquals(idealIntKind.toStringBuffer(true).toString(), "int");
		assertEquals(idealIntKind2.toStringBuffer(true).toString(), "int");
		assertEquals(herbrandIntKind.toStringBuffer(true).toString(), "hint");
		assertEquals(boundedIntKind.toStringBuffer(true).toString(), "bounded");
	}
	
	/**
	 * comparing the integer types using TypeComparator.
	 */
	@Test
	public void testTypeComparator(){
		assertEquals(typeComparator.compare(idealIntKind, idealIntKind2), 0);
		assertNotEquals(typeComparator.compare(herbrandIntKind, boundedIntKind), 0);
	}
	
	/**
	 * checking the pure type of the integer type.
	 */
	@Test
	public void testGetPureType() {
		assertEquals(((CommonSymbolicIntegerType)idealIntKind.getPureType()).integerKind(), IntegerKind.IDEAL);
		
	}
	
}
