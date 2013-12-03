package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;

public class SymbolicPrimitiveTypeTest {
	
	/**
	 declaring variables for CommonSymbolicPrimitiveType 
	 */
	CommonSymbolicPrimitiveType bool1, bool2, char1;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 initializing the declared variables.
	 */
	@Before
	public void setUp() throws Exception {
		
		bool1 = new CommonSymbolicPrimitiveType(SymbolicTypeKind.BOOLEAN);
		bool2 = new CommonSymbolicPrimitiveType(SymbolicTypeKind.BOOLEAN);
		char1 = new CommonSymbolicPrimitiveType(SymbolicTypeKind.CHAR);
	}

	@After
	public void tearDown() throws Exception {
	}
	

	/**
	 in this test the hash code of boolean is compared to boolean which should be the same and 
	 hash code of boolean is compared to character which should be different. 
	 */
	@Test
	public void testComputeHashCode() 
	{
		assertEquals(bool1.computeHashCode(), bool2.computeHashCode());
		assertNotEquals(bool1.computeHashCode(), char1.computeHashCode());
	}
	
	/**
	 this test checks whether a variable of one type is the same or different from a variable of another type.  
	 */
	@Test
	public void testTypeEquals() 
	{
		assertTrue(bool1.typeEquals(bool2));
		assertEquals(bool1.typeEquals(char1), char1.typeEquals(bool2));
	}
	

	/**
	 * this test checks whether the variables are canonizable or not,
	 * here they are not and return false.  
	 */
	@Test
	public void testCanonizeChildren() 
	{
//		System.out.println(bool1.isCanonic());
//		System.out.println(bool2.isCanonic());
//		System.out.println(char1.isCanonic());
		assertEquals(bool1.isCanonic(), bool2.isCanonic());
		assertEquals(bool2.isCanonic(), char1.isCanonic());
	}

	

	/**
	  this test aims check the output for the method getPureType and the PureType for boolean and character should be different.
	 */
	@Test
	public void testGetPureType() 
	{
//		System.out.println(bool1.getPureType());
//		System.out.println(bool2.getPureType());
//		System.out.println(char1.getPureType());
		assertEquals(bool1.getPureType(), bool2.getPureType());
		assertNotEquals(bool1.getPureType(), char1.getPureType());
	}

//	@Test
//	public void testCommonSymbolicPrimitiveType() 
//	{
//		fail("Not yet implemented");
//	}

	/**
	 this test checks the string output of a variable. The string output of variables of the same type should match but 
	 those of different types should not. 
	 */
	@Test
	public void testToStringBuffer() 
	{
//		System.out.println(bool1.toStringBuffer(true));
//		System.out.println(bool2.toStringBuffer(true));
//		System.out.println(bool1.toStringBuffer(false));
//		System.out.println(char1.toStringBuffer(true));
//		System.out.println(char1.toStringBuffer(false));
		assertEquals(bool1.toStringBuffer(true).toString(),"boolean");
		assertEquals(bool2.toStringBuffer(false).toString(),"boolean");
		assertNotEquals(bool1.toStringBuffer(true).toString(),"char");
		assertNotEquals(bool1.toStringBuffer(false).toString(),"char");
		assertNotEquals(bool2.toStringBuffer(true).toString(),"CHAR");
		assertNotEquals(bool2.toStringBuffer(false).toString(),"CHAR");
		assertEquals(char1.toStringBuffer(true).toString(),"CHAR");
		assertEquals(char1.toStringBuffer(true).toString(),"CHAR");
		assertEquals(char1.toStringBuffer(false).toString(),"CHAR");
		assertNotEquals(char1.toStringBuffer(true).toString(),"char");
		assertNotEquals(char1.toStringBuffer(false).toString(),"char");
		assertNotEquals(char1.toStringBuffer(true).toString(),"boolean");
	}

}
