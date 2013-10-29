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

/**
 * @author alali
 * 
 * Testing CommonSymbolicTypeSequence
 * - computeHashCode()
 * - numTypes()
 * - getType()
 * - intrinsicEquals()
 * - typeComparator()
 * - canonizeChildren() - not tested yet
 *
 */
public class CommonSymbolicTypeSequenceTest {

	CommonSymbolicTypeSequence typeSequence, typeSequence2;
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
	public void setUp() throws Exception {
		
		typeComparator = new TypeComparator();
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
		typeSequence2 = new CommonSymbolicTypeSequence(types);
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testComputeHashCode() {
		assertEquals(typeSequence.computeHashCode(), typeSequence2.computeHashCode());
	}
	
	@Test
	public void testNumTypes() {
		assertEquals(typeSequence.numTypes(), 4);
	}
	
	@Test
	public void testGetType() {
		assertTrue(typeSequence.getType(1) instanceof CommonSymbolicIntegerType);
		assertTrue(typeSequence.getType(0) instanceof CommonSymbolicIntegerType);
		assertTrue(typeSequence.getType(2) instanceof CommonSymbolicRealType);
		assertTrue(typeSequence.getType(3) instanceof CommonSymbolicRealType);
	}
	

	@Test
	public void testIntrinsicEquals() {
		assertTrue(typeSequence.intrinsicEquals(typeSequence2));
		//System.out.println(typeSequence.toString());
	}
	

	
	/*


	@Test
	public void testToStringBuffer() {
		fail("Not yet implemented");
	}

	@Test
	public void testToStringBufferLong() {
		fail("Not yet implemented");
	}
*/
}
