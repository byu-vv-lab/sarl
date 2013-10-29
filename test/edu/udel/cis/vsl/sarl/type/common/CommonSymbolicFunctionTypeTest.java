package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType.RealKind;

public class CommonSymbolicFunctionTypeTest 
{
	CommonSymbolicFunctionType function, function1, function2;
	
	CommonSymbolicTypeSequence typeSequence;
	
	CommonSymbolicIntegerType idealIntKind, boundedIntKind;
	
	CommonSymbolicRealType idealRealKind, floatRealKind;
	
	ArrayList<CommonSymbolicType> types;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception 
	{
		idealIntKind = new CommonSymbolicIntegerType(IntegerKind.IDEAL);
		boundedIntKind = new CommonSymbolicIntegerType(IntegerKind.BOUNDED);
		idealRealKind = new CommonSymbolicRealType(RealKind.IDEAL);
		floatRealKind = new CommonSymbolicRealType(RealKind.FLOAT);
		types = new ArrayList<CommonSymbolicType>();
		types.add(idealIntKind);
		types.add(boundedIntKind);
		types.add(idealRealKind);
		types.add(floatRealKind);
		typeSequence = new CommonSymbolicTypeSequence(types);
		function = new CommonSymbolicFunctionType(typeSequence, idealIntKind);
		function1 = new CommonSymbolicFunctionType(typeSequence, idealIntKind);
		function2 = new CommonSymbolicFunctionType(typeSequence, floatRealKind);
	}

	@After
	public void tearDown() throws Exception {
	}
	

	/**
	 in this test the hash code for two functions is compared with each other and depending upon the type of function i.e. if 
	 they are similar than the hash codes should be equal and at the same time the hash code for two different functions should be different. 
	 */
	@Test
	public void testComputeHashCode() 
	{
		assertEquals(function.computeHashCode(), function1.computeHashCode());
		assertNotEquals(function1.computeHashCode(), function2.computeHashCode());
				
	}

//	@Test
//	public void testCanonizeChildren() {
//		fail("Not yet implemented");
//	}

	/**
	 this test checks whether one type of function is similar to another type of function or not.  
	 */
	@Test
	public void testTypeEquals() 
	{
		assertTrue(function.typeEquals(function1));
		assertFalse(function2.typeEquals(function1));
		assertNotEquals(function.typeEquals(function1), function1.typeEquals(function2));
	}

//	@Test
//	public void testCommonSymbolicFunctionType() {
//		fail("Not yet implemented");
//	}

	/**
	 this test aims to check the output type of each variable of the ArrayList of the CommonSymbolicType. 
	 */
	@Test
	public void testOutputType() 
	{
		assertTrue(function.outputType().isInteger());
		assertFalse(function2.outputType().isInteger());
	}

	/**
	 this test checks the string output of each variable of the ArrayList of the CommonSymbolicType. 
	 */
	@Test
	public void testToStringBuffer() 
	{
//		System.out.println(function.toStringBuffer(true));
//		System.out.println(function1.toStringBuffer(true));
//		System.out.println(function1.toStringBuffer(false));
//		System.out.println(function2.toStringBuffer(true));
		assertEquals(function.toStringBuffer(true).toString(),"(<int,bounded,real,float>->int)");
		assertEquals(function1.toStringBuffer(false).toString(),"<int,bounded,real,float>->int");
		assertNotEquals(function1.toStringBuffer(false).toString(),"(<int,bounded,real,float>->int)");
		assertNotEquals(function2.toStringBuffer(true).toString(),"(<int,bounded,real,float>->int)");
	}

	/**
	 this test check the how many input types the function has, in this particular test the expected value is 4.   
	 */
	@Test
	public void testInputTypes() 
	{
//		System.out.println(function.inputTypes().numTypes());
//		System.out.println(function1.inputTypes().numTypes());
//		System.out.println(function2.inputTypes().numTypes());
		assertEquals(function.inputTypes().numTypes(), 4);
		assertEquals(function1.inputTypes().numTypes(), 4);
		assertEquals(function2.inputTypes().numTypes(), 4);
		assertNotEquals(function.inputTypes().numTypes(), 5);
	}
	
	@Test
	public void testGetPureType() 
	{
//		System.out.println(function.getPureType());
//		System.out.println(function1.getPureType());
//		System.out.println(function2.getPureType());
		assertEquals(function.getPureType(), function1.getPureType());
		assertEquals(function1.getPureType(), function2.getPureType());
	}

	@Test
	public void testSetPureType() 
	{
//		System.out.println(function.getPureType());
		assertNull(function.getPureType());
		function.setPureType(function2);
		assertNotNull(function.getPureType());
//		System.out.println(function.getPureType());
	}

}
