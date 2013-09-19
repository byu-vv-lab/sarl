package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;

public class CommonSymbolicIntegerTypeTest {
	/*Testing SymbolicIntegerType
	 * 
	 */
	
		CommonSymbolicIntegerType idealIntKind, idealIntKind2, boundedIntKind, herbrandIntKind;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		idealIntKind = new CommonSymbolicIntegerType(IntegerKind.IDEAL);
		idealIntKind2 = new CommonSymbolicIntegerType(IntegerKind.IDEAL);
		boundedIntKind = new CommonSymbolicIntegerType(IntegerKind.BOUNDED);
		herbrandIntKind = new CommonSymbolicIntegerType(IntegerKind.HERBRAND);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testIntegerKind() {
		assertEquals(idealIntKind.integerKind(), IntegerKind.IDEAL);
		assertEquals(boundedIntKind.integerKind(), IntegerKind.BOUNDED);
		assertEquals(herbrandIntKind.integerKind(), IntegerKind.HERBRAND);
	}
	
	@Test
	public void testTypeEquals() {
		assertTrue(idealIntKind.typeEquals(idealIntKind));
		assertFalse(idealIntKind.typeEquals(boundedIntKind));
	}
	
	
	@Test
	public void testIsHerbrand() {
		assertTrue(herbrandIntKind.isHerbrand());
		assertFalse(idealIntKind.isHerbrand());
		assertFalse(boundedIntKind.isHerbrand());
	}
	
	@Test
	public void testIsIdeal() {
		assertTrue(idealIntKind.isIdeal());
		assertFalse(boundedIntKind.isIdeal());
		assertFalse(herbrandIntKind.isIdeal());
	}
	
	@Test
	public void testComputeHashCode() {
		assertEquals(idealIntKind.computeHashCode(),idealIntKind2.computeHashCode());
	}
	
	@Test
	public void testToStringBuffer() {
		assertEquals(idealIntKind.toStringBuffer(true).toString(), "int");
		assertEquals(idealIntKind2.toStringBuffer(true).toString(), "int");
		assertEquals(herbrandIntKind.toStringBuffer(true).toString(), "hint");
		assertEquals(boundedIntKind.toStringBuffer(true).toString(), "bounded");
	}
	
	/*

	@Test
	public void testCanonizeChildren() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPureType() {
		fail("Not yet implemented");
	}

	@Test
	public void testCommonSymbolicIntegerType() {
		fail("Not yet implemented");
	}
*/
}
