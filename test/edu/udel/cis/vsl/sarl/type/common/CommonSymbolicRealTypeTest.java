package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType.RealKind;

import static edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType.RealKind.*;

public class CommonSymbolicRealTypeTest {
	/* Testing CommonSymbolicRealType by Team3
	 * Three realKinds: ideal, herbrand, and float;
	 */
		CommonSymbolicRealType idealRealKind, idealRealKind2, herbrandRealKind, floatRealKind;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		idealRealKind = new CommonSymbolicRealType(RealKind.IDEAL);
		idealRealKind2 = new CommonSymbolicRealType(RealKind.IDEAL);
		floatRealKind = new CommonSymbolicRealType(RealKind.FLOAT);
		herbrandRealKind = new CommonSymbolicRealType(RealKind.HERBRAND);
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testRealKind() {
		assertEquals(herbrandRealKind.realKind(), RealKind.HERBRAND);
		assertEquals(floatRealKind.realKind(), RealKind.FLOAT);
		assertEquals(idealRealKind.realKind(), RealKind.IDEAL);
	}
	
	@Test
	public void testTypeEquals() {
		assertTrue(idealRealKind.typeEquals(idealRealKind2));
	}
	
	@Test
	public void testComputeHashCode() {
		assertEquals(idealRealKind.computeHashCode(), idealRealKind2.computeHashCode());
	}

	

	@Test
	public void testIsHerbrand() {
		assertTrue(herbrandRealKind.isHerbrand());
		
	}
	
	@Test
	public void testIsIdeal() {
		assertTrue(idealRealKind.isIdeal());
		assertTrue(idealRealKind2.isIdeal());
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
	

	
*/

	@Test
	public void testToStringBuffer() {
        CommonSymbolicRealType ideal1 = new CommonSymbolicRealType(RealKind.IDEAL);
        assertEquals(
            "IDEAL type should be 'real'",
            ideal1.toStringBuffer(true).toString(),
            "real");

        CommonSymbolicRealType herbrand1 = new CommonSymbolicRealType(RealKind.HERBRAND);
        assertEquals(
            "HERBRAND type should be 'hreal'",
            herbrand1.toStringBuffer(true).toString(),
            "hreal");

        CommonSymbolicRealType float1 = new CommonSymbolicRealType(RealKind.FLOAT);
        assertEquals(
            "FLOAT type should be 'float'",
            float1.toStringBuffer(true).toString(),
            "float");
	}


}
