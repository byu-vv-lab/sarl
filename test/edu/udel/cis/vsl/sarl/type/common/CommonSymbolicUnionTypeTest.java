package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.*;

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

/**
 * @author jthakkar
 *
 */
/**
 * @author jthakkar
 *
 */
public class CommonSymbolicUnionTypeTest {
	
	/**
	 * an ObjectFactory object that is used to instantiate the variables that is unionType and unionType2.
	 */
	ObjectFactory objectFactory;
	/**
	 * a NumboerFactory that is used to instantiate the objectFactory
	 */
	NumberFactory numberFactory;
	
	CommonSymbolicUnionType unionType, unionType2;
	
	/**
	 * an array of SymbolicTypes to be used in creating the SequenceType
	 */
	CommonSymbolicType typesArray[];
	
	/**
	 * integer types to be used in creating the SequenceType
	 */
	CommonSymbolicIntegerType idealIntKind, boundedIntKind;
	
	/**
	 * Real types to be used in creating the SequenceType
	 */
	CommonSymbolicRealType idealRealKind, floatRealKind;
	
	/**
	 typeSequence is used to assign the array of CommonSymbolicType to the variables that is unionType and unionType2. 
	 */
	CommonSymbolicTypeSequence typeSequence;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		numberFactory = Numbers.REAL_FACTORY;
		objectFactory = Objects.newObjectFactory(numberFactory);
		
		idealIntKind = new CommonSymbolicIntegerType(IntegerKind.IDEAL);
		boundedIntKind = new CommonSymbolicIntegerType(IntegerKind.BOUNDED);
		idealRealKind = new CommonSymbolicRealType(RealKind.IDEAL);
		floatRealKind = new CommonSymbolicRealType(RealKind.FLOAT);
		typesArray = new CommonSymbolicType[4];
				
		//an array of CommonSymbolicType
		typesArray[0] = idealRealKind;
		typesArray[1] = floatRealKind;
		typesArray[2] = idealIntKind;
		typesArray[3] = boundedIntKind;
		
		//initialization of all the variables.
		typeSequence = new CommonSymbolicTypeSequence(typesArray);
		unionType = new CommonSymbolicUnionType(objectFactory.stringObject("myUnion"),
				typeSequence);
		unionType2 = new CommonSymbolicUnionType(objectFactory.stringObject("myUnion2"),
				typeSequence);
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
		assertNotEquals(unionType.computeHashCode(), unionType2.computeHashCode());
	}
	
	/**
	 this test checks whether a variable of one type is the same or different from a variable of another type. 
	 */
	@Test
	public void testTypeEquals() 
	{
		assertFalse(unionType.typeEquals(unionType2));
	}
	
	/**
	 this test checks the name assigned to each variable with other variables. 
	 */
	@Test
	public void testName() 
	{
//		System.out.println(unionType.name());
//		System.out.println(unionType2.name());
		assertNotEquals(unionType.name(), unionType2.name());
	}
	
	/**
	 this test checks the typeSequence assigned to each variable. 
	 */
	@Test
	public void testSequence() 
	{
//		System.out.println(unionType.sequence());
//		System.out.println(unionType.sequence());
		assertEquals(unionType.sequence(), unionType2.sequence());
	}
	
	/**
	 here the pureType is always returned as null until something is not explicitly assigned.  
	 */
	@Test
	public void testPureType(){
		assertNull(unionType.getPureType());
		unionType.setPureType(unionType);
		assertNotNull(unionType.getPureType());
	}


	/**
	 this test checks the string output of a variable. The string output of variables of the same type should match but 
	 those of different types should not. 
	 */
	@Test
	public void testToStringBuffer() 
	{

//		System.out.println(unionType.toStringBuffer(true));
//		System.out.println(unionType.toStringBuffer(false));
//		System.out.println(unionType2.toStringBuffer(true));
//		System.out.println(unionType2.toStringBuffer(false));
		assertEquals(unionType.toStringBuffer(true).toString(),"Union[myUnion,<real,float,int,bounded>]");
		assertEquals(unionType.toStringBuffer(false).toString(),"Union[myUnion,<real,float,int,bounded>]");
		assertEquals(unionType2.toStringBuffer(false).toString(),"Union[myUnion2,<real,float,int,bounded>]");
		assertEquals(unionType2.toStringBuffer(true).toString(),"Union[myUnion2,<real,float,int,bounded>]");
		assertNotEquals(unionType.toStringBuffer(true).toString(),"Union[myUnion2,<real,float,int,bounded>]");
		assertNotEquals(unionType.toStringBuffer(false).toString(),"Union[myUnion2,<real,float,int,bounded>]");
		assertNotEquals(unionType2.toStringBuffer(true).toString(),"Union[myUnion,<real,float,int,bounded>]");
		assertNotEquals(unionType2.toStringBuffer(false).toString(),"Union[myUnion,<real,float,int,bounded>]");
	
	}

	/**
	 this test checks the index of CommonSymbolicType in the array.
	 */
	@Test
	public void testIndexOfType() 
	{
//		System.out.println(unionType.indexOfType(idealRealKind));
//		System.out.println(unionType.indexOfType(floatRealKind));
//		System.out.println(unionType.indexOfType(idealIntKind));
//		System.out.println(unionType.indexOfType(boundedIntKind));
//		System.out.println(unionType2.indexOfType(idealRealKind));
//		System.out.println(unionType2.indexOfType(floatRealKind));
//		System.out.println(unionType2.indexOfType(idealIntKind));
//		System.out.println(unionType2.indexOfType(boundedIntKind));
		assertEquals(unionType.indexOfType(idealRealKind), unionType2.indexOfType(idealRealKind));
		assertEquals(unionType.indexOfType(floatRealKind), unionType2.indexOfType(floatRealKind));
		assertEquals(unionType.indexOfType(idealIntKind), unionType2.indexOfType(idealIntKind));
		assertEquals(unionType.indexOfType(boundedIntKind), unionType2.indexOfType(boundedIntKind));
		assertNotEquals(unionType.indexOfType(idealIntKind), unionType2.indexOfType(idealRealKind));
		assertNotEquals(unionType.indexOfType(boundedIntKind), unionType2.indexOfType(floatRealKind));
		assertNotNull(unionType.indexOfType(boundedIntKind));
		
	}

}
