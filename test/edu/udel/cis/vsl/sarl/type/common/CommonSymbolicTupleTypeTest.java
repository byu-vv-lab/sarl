package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType.RealKind;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.object.Objects;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;

public class CommonSymbolicTupleTypeTest 
{
	CommonSymbolicTupleType tuple1, tuple2, tuple3;
	
	ObjectFactory objectFactory;
	
	NumberFactory numberFactory;

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
		numberFactory = Numbers.REAL_FACTORY;
		objectFactory = Objects.newObjectFactory(numberFactory);
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
		tuple1 = new CommonSymbolicTupleType(objectFactory.stringObject("Tuple"), typeSequence);
		tuple2 = new CommonSymbolicTupleType(objectFactory.stringObject("Tuple"), typeSequence);
		tuple3 = new CommonSymbolicTupleType(objectFactory.stringObject("Tuple3"), typeSequence);
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testComputeHashCode() 
	{
		assertEquals(tuple1.computeHashCode(), tuple2.computeHashCode());
		assertNotEquals(tuple1.computeHashCode(), tuple3.computeHashCode());
	}

//	@Test
//	public void testCanonizeChildren() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testTypeEquals() 
	{
		assertTrue(tuple1.typeEquals(tuple2));
		assertFalse(tuple2.typeEquals(tuple3));
	}

//	@Test
//	public void testCommonSymbolicTupleType() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testToStringBuffer() 
	{
//		System.out.println(tuple1.toStringBuffer(true));
//		System.out.println(tuple2.toStringBuffer(false));
//		System.out.println(tuple3.toStringBuffer(true));
//		System.out.println(tuple3.toStringBuffer(false));
		assertEquals(tuple1.toStringBuffer(true).toString(),"Tuple<int,bounded,real,float>");
		assertEquals(tuple2.toStringBuffer(false).toString(),"Tuple<int,bounded,real,float>");
		assertEquals(tuple3.toStringBuffer(false).toString(),"Tuple3<int,bounded,real,float>");
		assertEquals(tuple3.toStringBuffer(true).toString(),"Tuple3<int,bounded,real,float>");
		assertNotEquals(tuple1.toStringBuffer(true).toString(),"Tuple3<int,bounded,real,float>");
		assertNotEquals(tuple2.toStringBuffer(false).toString(),"Tuple3<int,bounded,real,float>");
		assertNotEquals(tuple3.toStringBuffer(true).toString(),"Tuple<int,bounded,real,float>");
		assertNotEquals(tuple3.toStringBuffer(false).toString(),"Tuple<int,bounded,real,float>");
	}

//	@Test
//	public void testName() 
//	{
//		fail("Not yet implemented");
//	}

//	@Test
//	public void testSequence() 
//	{
//		fail("Not yet implemented");
//	}

	@Test
	public void testGetPureType() 
	{
//		System.out.println(tuple1.getPureType());
//		System.out.println(tuple2.getPureType());
//		System.out.println(tuple3.getPureType());
		assertEquals(tuple1.getPureType(), tuple2.getPureType());
		assertEquals(tuple2.getPureType(), tuple3.getPureType());
	}

	@Test
	public void testSetPureType() 
	{
//		System.out.println(tuple1.getPureType());
		assertNull(tuple1.getPureType());
		tuple1.setPureType(tuple3);
		assertNotNull(tuple1.getPureType());
//		System.out.println(tuple1.getPureType());
	}

}
