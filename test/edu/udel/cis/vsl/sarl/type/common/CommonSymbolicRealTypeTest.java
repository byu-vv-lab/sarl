package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType.RealKind.*;

public class CommonSymbolicRealTypeTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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
	public void testComputeHashCode() {
		//fail("Not yet implemented");
	}
/*
	@Test
	public void testCanonizeChildren() {
		fail("Not yet implemented");
	}

	@Test
	public void testTypeEquals() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsHerbrand() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsIdeal() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPureType() {
		fail("Not yet implemented");
	}

	@Test
	public void testCommonSymbolicRealType() {
		fail("Not yet implemented");
	}

	@Test
	public void testRealKind() {
		fail("Not yet implemented");
	}
*/

	@Test
	public void testToStringBuffer() {
        CommonSymbolicRealType ideal1 = new CommonSymbolicRealType(IDEAL);
        assertEquals(
            "IDEAL type should be 'real'",
            ideal1.toStringBuffer(true).toString(),
            "real");

        CommonSymbolicRealType herbrand1 = new CommonSymbolicRealType(HERBRAND);
        assertEquals(
            "HERBRAND type should be 'hreal'",
            herbrand1.toStringBuffer(true).toString(),
            "hreal");

        CommonSymbolicRealType float1 = new CommonSymbolicRealType(FLOAT);
        assertEquals(
            "FLOAT type should be 'float'",
            float1.toStringBuffer(true).toString(),
            "float");
	}
}
