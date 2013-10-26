package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;

public class CommonSymbolicPrimitiveTypeTest {
	CommonSymbolicPrimitiveType bool1, bool2, char1;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		bool1 = new CommonSymbolicPrimitiveType(SymbolicTypeKind.BOOLEAN);
		bool2 = new CommonSymbolicPrimitiveType(SymbolicTypeKind.BOOLEAN);
		char1 = new CommonSymbolicPrimitiveType(SymbolicTypeKind.CHAR);
	}

	@After
	public void tearDown() throws Exception {
	}
	

	@Test
	public void testComputeHashCode() 
	{
		assertEquals(bool1.computeHashCode(), bool2.computeHashCode());
		assertNotEquals(bool1.computeHashCode(), char1.computeHashCode());
	}
	
	@Test
	public void testTypeEquals() 
	{
		assertTrue(bool1.typeEquals(bool2));
		assertEquals(bool1.typeEquals(char1), char1.typeEquals(bool2));
	}
	
/*
	@Test
	public void testCanonizeChildren() 
	{
		
	}
*/
	

	@Test
	public void testGetPureType() 
	{
		assertEquals(bool1.getPureType(), bool2.getPureType());
		assertNotEquals(bool1.getPureType(), char1.getPureType());
	}

//	@Test
//	public void testCommonSymbolicPrimitiveType() 
//	{
//		fail("Not yet implemented");
//	}

	/**
	 * 
	 */
	@Test
	public void testToStringBuffer() 
	{
		//System.out.print(bool1.toStringBuffer(true));
		assertEquals(bool1.toStringBuffer(true).toString(),"boolean");
		assertEquals(bool2.toStringBuffer(false).toString(),"boolean");
		assertNotEquals(char1.toStringBuffer(true).toString(),"boolean");
	}

}
