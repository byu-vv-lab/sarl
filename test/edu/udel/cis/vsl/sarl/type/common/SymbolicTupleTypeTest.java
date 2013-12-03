package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

public class SymbolicTupleTypeTest 
{
	/**
	 declaring all the required variables.
	 */
	CommonSymbolicTupleType tuple1, tuple2, tuple3;
	
	/**
	 an ObjectFactory object that is used to instantiate the variables that is tuple1, tuple2, tuple3. 
	 */
	ObjectFactory objectFactory;
	
	/**
	 a numberFactory is used to instantiate the objectFactory 
	 */
	NumberFactory numberFactory;

	/**
	 typeSequence is used to assign the array of CommonSymbolicType to the variables that is tuple1, tuple2, tuple3. 
	 */
	CommonSymbolicTypeSequence typeSequence;
	
	/**
	 integer types to be used in creating the typeSequence 
	 */
	CommonSymbolicIntegerType idealIntKind, boundedIntKind;
	
	/**
	 real types to be used in creating the typeSequence 
	 */
	CommonSymbolicRealType idealRealKind, floatRealKind;
	
	/**
	 types is an ArrayList which is used in creating typeSequence 
	 */
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
		
		//initializing the ArrayList
		types = new ArrayList<CommonSymbolicType>();
		
		//adding the real or integer type to the list
		types.add(idealIntKind);
		types.add(boundedIntKind);
		types.add(idealRealKind);
		types.add(floatRealKind);
		
		//assigning the ArrayList to another variable
		typeSequence = new CommonSymbolicTypeSequence(types);
		
		//initializing the variables
		tuple1 = new CommonSymbolicTupleType(objectFactory.stringObject("Tuple"), typeSequence);
		tuple2 = new CommonSymbolicTupleType(objectFactory.stringObject("Tuple"), typeSequence);
		tuple3 = new CommonSymbolicTupleType(objectFactory.stringObject("Tuple3"), typeSequence);
	}

	@After
	public void tearDown() throws Exception {
	}


	/**
	 in this test the hash code for two variables is compared with each other and 
	 depending upon the variable they should be either similar or different. 
	 */
	@Test
	public void testComputeHashCode() 
	{
		assertEquals(tuple1.computeHashCode(), tuple2.computeHashCode());
		assertNotEquals(tuple1.computeHashCode(), tuple3.computeHashCode());
	}

	/**
	 * this test checks whether the variables are canonizable or not,
	 * here they are not and return false.
	 */
	@Test
	public void testCanonizeChildren() 
	{
//		System.out.println(tuple1.isCanonic());
//		System.out.println(tuple2.isCanonic());
//		System.out.println(tuple3.isCanonic());
		assertEquals(tuple1.isCanonic(), tuple2.isCanonic());
		assertEquals(tuple2.isCanonic(), tuple3.isCanonic());
	}

	/**
	 this test checks whether a variable of one type is the same or different from a variable of another type. 
	 */
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

	/**
	 this test checks the string output of a variable. The string output of variables of the same type should match but 
	 those of different types should not.
	 */
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

	/**
	 this test checks the name assigned to each variable with other variables. 
	 */
	@Test
	public void testName() 
	{
//		System.out.println(tuple1.name());
//		System.out.println(tuple2.name());
//		System.out.println(tuple3.name());
		assertEquals(tuple1.name(), tuple2.name());
		assertNotEquals(tuple2.name(), tuple3.name());
	}

	/**
	 this test checks the typeSequence assigned to each variable. 
	 */
	@Test
	public void testSequence() 
	{
//		System.out.println(tuple1.sequence());
//		System.out.println(tuple2.sequence());
//		System.out.println(tuple3.sequence());
		assertEquals(tuple1.sequence(), tuple2.sequence());
		assertEquals(tuple2.sequence(), tuple3.sequence());
	}

	/**
	 here the pureType is always returned as null thus the pureType of each variable is the same. 
	 */
	@Test
	public void testGetPureType() 
	{
//		System.out.println(tuple1.getPureType());
//		System.out.println(tuple2.getPureType());
//		System.out.println(tuple3.getPureType());
		assertEquals(tuple1.getPureType(), tuple2.getPureType());
		assertEquals(tuple2.getPureType(), tuple3.getPureType());
	}

	/**
	 here the method getPureType always returns null therefore something is assigned to the variable and checked whether 
	 the method getPureType still returns null or not.  
	 */
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
