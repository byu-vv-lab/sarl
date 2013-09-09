package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;

public class CommonSymbolicIntegerTypeTest {
	/*Testing SymbolicIntegerType
	 * 
	 */
		SymbolicIntegerType idealIntType, boundedIntType, herbrandIntType;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		idealIntType = new CommonSymbolicIntegerType(IntegerKind.IDEAL);
		boundedIntType = new CommonSymbolicIntegerType(IntegerKind.BOUNDED);
		herbrandIntType = new CommonSymbolicIntegerType(IntegerKind.HERBRAND);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testIntegerKind() {
		assertEquals(idealIntType.integerKind(), IntegerKind.IDEAL);
		assertEquals(boundedIntType.integerKind(), IntegerKind.BOUNDED);
		assertEquals(herbrandIntType.integerKind(), IntegerKind.HERBRAND);
	}
	
	/*@Test no need to test the types unless we know they're
	 * the same type.
	public void testTypeEquals() {
		assertFalse(idealIntType.typeEquals(boundedIntType));
	}
	*/
	
	@Test
	public void testIsHerbrand() {
		assertTrue(herbrandIntType.isHerbrand());
		assertFalse(idealIntType.isHerbrand());
		assertFalse(boundedIntType.isHerbrand());
	}
	
	@Test
	public void testIsIdeal() {
		assertTrue(idealIntType.isIdeal());
		assertFalse(boundedIntType.isIdeal());
		assertFalse(herbrandIntType.isIdeal());
	}
	
	/*

	@Test
	public void testComputeHashCode() {
		fail("Not yet implemented");
	}

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

	@Test
	public void testToStringBuffer() {
		fail("Not yet implemented");
	}
*/
}
