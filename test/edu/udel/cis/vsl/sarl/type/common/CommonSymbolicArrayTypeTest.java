package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType.RealKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;

public class CommonSymbolicArrayTypeTest {
	
	
	private List<CommonSymbolicArrayType> listArrayType;
	CommonSymbolicArrayType intType1, intType11, intType2, realType1, realType2;
	CommonSymbolicIntegerType idealIntKind, idealIntKind2, boundedIntKind;
	CommonSymbolicRealType idealRealKind, floatRealKind;
	

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
		idealRealKind = new CommonSymbolicRealType(RealKind.IDEAL);
		floatRealKind = new CommonSymbolicRealType(RealKind.FLOAT);
		intType1 = new CommonSymbolicArrayType(idealIntKind);
		intType11 = new CommonSymbolicArrayType(idealIntKind2);
		intType2 = new CommonSymbolicArrayType(boundedIntKind);
		realType1 = new CommonSymbolicArrayType(idealRealKind);
		realType2 = new CommonSymbolicArrayType(floatRealKind);
		listArrayType = Arrays.asList(new CommonSymbolicArrayType[] {intType1, intType11, intType2, realType1, realType2});
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testComputeHashCode() {
		//fail("Not yet implemented");
	}
	
	@Test
	public void testTypeEquals() {
		//System.out.println("intType1: " + intType1.elementType().toString());
		//System.out.println("intType11: " + intType11.elementType().toString());
		//System.out.println("intType1: " + listArrayType.get(0).elementType().toString());

		assertTrue(listArrayType.get(0).typeEquals(listArrayType.get(1)));
	}
	
	@Test
	public void testElementType() {
		/* SymbolicType.SymbolicTypeKind.INTEGER = "INTEGER"
		 * elementType() = "int"
		 */
		//System.out.println(SymbolicType.SymbolicTypeKind.INTEGER);
		//System.out.println(listArrayType.get(2).elementType().toString());
		//assertEquals(listArrayType.get(0).elementType(), SymbolicType.SymbolicTypeKind.INTEGER);
		//assertEquals(listArrayType.get(1).elementType(), IntegerKind.BOUNDED);
		//assertEquals(listArrayType.get(2).elementType(), RealKind.IDEAL);
		//assertEquals(listArrayType.get(3).elementType(), RealKind.FLOAT);
	}
	
	@Test
	public void testGetPureType() {
		//assertEqual(listArrayType.get(0).getPureType(),)
		
		assertEquals(listArrayType.get(0).typeKind(), SymbolicTypeKind.ARRAY);
		//assertEquals(listArrayType.get(0).typeKind(), Null);
	}
	
	
	
	/*
	@Test
	public void testCanonizeChildren() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testToStringBuffer() {
		
	}
	

	@Test
	public void testExtentString() {
		fail("Not yet implemented");
	}


	@Test
	public void testIsComplete() {
		fail("Not yet implemented");
	}

	

	@Test
	public void testSetPureType() {
		fail("Not yet implemented");
	}
*/
}
