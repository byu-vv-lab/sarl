package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType.RealKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.preuniverse.common.PureTypeTest;

public class CommonSymbolicFunctionTypeTest 
{
	CommonSymbolicFunctionType function, function1, function2;
	CommonSymbolicTypeSequence typeSequence;
	CommonSymbolicIntegerType idealIntKind, boundedIntKind;
	CommonSymbolicRealType idealRealKind, floatRealKind;
	TypeComparator typeComparator;
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

	@Test
	public void testOutputType() 
	{
		assertTrue(function.outputType().isInteger());
		assertFalse(function2.outputType().isInteger());
	}

	@Test
	public void testToStringBuffer() 
	{
//		System.out.print(function.toStringBuffer(true));
//		System.out.print(function1.toStringBuffer(true));
//		System.out.print(function2.toStringBuffer(true));
		assertEquals(function.toStringBuffer(true).toString(),"(<int,bounded,real,float>->int)");
		assertEquals(function1.toStringBuffer(false).toString(),"<int,bounded,real,float>->int");
		assertNotEquals(function2.toStringBuffer(true).toString(),"<int,bounded,real,float>->int");
	}

	@Test
	public void testInputTypes() {
		assertEquals(function.inputTypes().numTypes(), 4);
		assertEquals(function1.inputTypes().numTypes(), 4);
		assertEquals(function2.inputTypes().numTypes(), 4);
		assertNotEquals(function.inputTypes().numTypes(), 5);
	}
	
//	@Test
//	public void testGetPureType() 
//	{
//		assertTrue((function.getPureType().inputTypes()) instanceof SymbolicTypeSequence);
//		assertNotEquals(function1.getPureType(), function2.getPureType());
//
//	}

//	@Test
//	public void testSetPureType() 
//	{
//	
//	}

}
