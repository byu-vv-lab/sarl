package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject.SymbolicObjectKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType.RealKind;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionComparatorStub;
import edu.udel.cis.vsl.sarl.ideal.common.NumericPrimitive;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.object.Objects;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.Types;

/**
 * Testing CommonSymbolicTypeFactory, which creates all types.
 * 
 * @author alali
 *
 */
public class CommonSymbolicTypeFactoryTest {
	
	/**
	 * creating a new typeFactory in order to instantiate concrete type objects.
	 */
	CommonSymbolicTypeFactory typeFactory, typeFactory2;
	/**
	 * an ObjectFactory object that is used to instantiate the typeFactory, and typeFactory2
	 */
	ObjectFactory objectFactory;
	/**
	 * a NumboerFactory that is used to instantiate the objectFactory
	 */
	NumberFactory numberFactory;
	
	
	/**
	 * NumericPrimitive is an implementation of NumericExpression
	 * we need this object to test CompleteArrayTypes
	 */
	NumericPrimitive numericPrimitive;
	
	/**
	 * a SymblicObject object to be used in creating a NumericPrimitive
	 */
	SymbolicObject symbolicObject;
	
	/**
	 * integer types to be used in creating the SequenceType
	 */
	CommonSymbolicIntegerType idealIntKind, boundedIntKind;
	
	/**
	 * Real types to be used in creating the SequenceType
	 */
	CommonSymbolicRealType idealRealKind, floatRealKind;
	
	/**
	 * an ArrayList on SymbolicType to be used in creating the SequenceType
	 */
	ArrayList<CommonSymbolicType> typesList;
	
	/**
	 * an array of SymbolicTypes to be used in creating the SequenceType
	 */
	CommonSymbolicType typesArray[];

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Instantiation of objects that are needed for testing
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		numberFactory = Numbers.REAL_FACTORY;
		objectFactory = Objects.newObjectFactory(numberFactory);
		typeFactory = new CommonSymbolicTypeFactory(objectFactory);
		typeFactory2 = (CommonSymbolicTypeFactory)Types.newTypeFactory(objectFactory);
		idealIntKind = new CommonSymbolicIntegerType(IntegerKind.IDEAL);
		boundedIntKind = new CommonSymbolicIntegerType(IntegerKind.BOUNDED);
		idealRealKind = new CommonSymbolicRealType(RealKind.IDEAL);
		floatRealKind = new CommonSymbolicRealType(RealKind.FLOAT);
		typesList = new ArrayList<CommonSymbolicType>();
		typesArray = new CommonSymbolicType[4];
		//to be used in CommonSymblicCompleteArrayType
		symbolicObject = objectFactory.numberObject(numberFactory.integer(4));
		numericPrimitive = new NumericPrimitive(SymbolicOperator.CONCRETE, idealIntKind, symbolicObject);
		
		//a list of CommonSymbolicType 
		typesList.add(idealRealKind);
		typesList.add(floatRealKind);
		typesList.add(idealIntKind);
		typesList.add(boundedIntKind);
		
		//an array of CommonSymbolicType
		typesArray[0] = idealRealKind;
		typesArray[1] = floatRealKind;
		typesArray[2] = idealIntKind;
		typesArray[3] = boundedIntKind;
		
	}

	@After
	public void tearDown() throws Exception {
		
	}



	/**
	 * Checks the return of objectFactory() if it is really an ObjectFactory
	 */
	@Test
	public void testObjectFactory() {
		assertTrue(typeFactory.objectFactory() instanceof ObjectFactory);
	}
	
	/**
	 * Checks the return of integerType() if it is really a SymbolicIntegerType
	 */
	@Test
	public void testIntegerType() {
		assertTrue(typeFactory.integerType() instanceof SymbolicIntegerType);
	}
	
	/**
	 * tests the return of herbrandIntegerType
	 * if its kind is Integer.herbrand
	 */
	@Test
	public void testHerbrandIntegerType() {
		assertEquals(typeFactory.herbrandIntegerType().integerKind(), SymbolicIntegerType.IntegerKind.HERBRAND);
	}
	
	/**
	 * Checks the return of realType() if it is really a SymbolicRealType
	 */
	@Test
	public void testRealType() {
		assertTrue(typeFactory.realType() instanceof SymbolicRealType);
	}

	/**
	 * tests the return of herbrandRealType
	 * if its type is Real.herbrand
	 */
	@Test
	public void testHerbrandRealType() {
		assertEquals(typeFactory.herbrandRealType().realKind(), SymbolicRealType.RealKind.HERBRAND);
	}
	
	/**
	 * checks the return of characterType() if it is a PrimitiveType of kind Char.
	 */
	@Test
	public void testCharacterType() {
		assertEquals(((CommonSymbolicPrimitiveType)typeFactory.characterType()).typeKind(), SymbolicTypeKind.CHAR);
	}
	
	/**
	 * Checks the construction of a SymblicSequenceType from a list
	 */
	@Test
	public void testSequence(){
		assertTrue(typeFactory.sequence(typesList) instanceof SymbolicTypeSequence);
	}
	
	
	/**
	 * Checks the construction of a SymbolicSequenceType from an array
	 * also test if the Kind of the created sequence is SymbolicObjectKind.Type_Sequence.
	 */
	@Test
	public void testSequence2(){
		assertTrue(
				typeFactory.sequence(typesArray) instanceof SymbolicTypeSequence);
		assertEquals(
				typeFactory.sequence(typesArray).symbolicObjectKind(), SymbolicObjectKind.TYPE_SEQUENCE);
	}
	
	/**
	 * Tests the construction of a single element sequence
	 * if its length is 1.
	 * 
	 * Also, test if the constructed sequence is of type Sequence.
	 */
	@Test
	public void testsingletonSequence(){
		assertEquals(typeFactory.singletonSequence(idealIntKind).numTypes(), 1);
		assertEquals(typeFactory.singletonSequence(idealIntKind).symbolicObjectKind(), SymbolicObjectKind.TYPE_SEQUENCE);

	}
	
	
	/**
	 * Tests the constructed boolean type object if it's a CommonSymbolicPrimitveType
	 * and is a Boolean kind
	 */
	@Test
	public void testBooleanType() {
		assertTrue(typeFactory.booleanType() instanceof CommonSymbolicPrimitiveType);
		assertTrue(typeFactory.booleanType().isBoolean());
		
		//Failure
		//assertEquals(typeFactory.booleanType().symbolicObjectKind(), SymbolicObjectKind.BOOLEAN);
	}	
	
	/**
	 * testing the arrayType() method that takes only one parameter 
	 * if it returns an (incomplete) array type
	 */
	@Test
	public void testArrayType() {
		assertTrue(typeFactory.arrayType(idealIntKind) instanceof SymbolicArrayType);
		}
	
	/**
	 * testing the return of a arrayType() with a length if it is an instance of
	 * SymbolicCompleteArrayType
	 */
	@Test
	public void testArrayType2(){
		assertTrue(typeFactory.arrayType(idealIntKind, numericPrimitive) instanceof SymbolicCompleteArrayType);
	}
	
	/**
	 * testing two different types using compare() from typeComparator
	 * the compare() number shouldn't be equal to 0 because they're different types
	 */
	@Test
	public void testTypeComparator(){
		assertNotEquals(typeFactory.typeComparator().compare(idealIntKind, idealRealKind), 0);
	}
	
	/**
	 * testing the construction of a typeSequenceComparator()
	 */
	@Test
	public void testTypeSequenceComparator() {
		assertTrue(typeFactory.typeSequenceComparator() instanceof TypeSequenceComparator);
	}
	

	/**
	 * testing setting an expression comparator from typeComparator
	 */
	@Test
	public void testSetExpressionComparator() {
		assertNull(typeFactory.typeComparator().expressionComparator());
		typeFactory.setExpressionComparator(new ExpressionComparatorStub());
		typeFactory.init();
		assertNotNull(typeFactory.typeComparator().expressionComparator());
	}
	
	/**
	 * Testing the construction of a FunctionType that has a sequence and an outputType
	 */
	@Test
	public void testFunctionType() {
		assertTrue((typeFactory.functionType(typeFactory.sequence(typesList), 
				idealIntKind)) instanceof CommonSymbolicFunctionType);
	}
	
	@Test
	public void testTupleType() {
		StringObject stringObject = objectFactory.stringObject("myTuple");
		
		assertTrue((typeFactory.tupleType(stringObject, 
				typeFactory.sequence(typesArray))) instanceof CommonSymbolicTupleType);
	}
	
	@Test
	public void testUnionType() {
		StringObject stringObject = objectFactory.stringObject("myUnion");
		
		assertTrue((typeFactory.unionType(stringObject, 
				typeFactory.sequence(typesList))) instanceof CommonSymbolicUnionType);
	}

	/*
	@Test
	public void testArrayTypeSymbolicTypeNumericExpression() {
		fail("Not yet implemented");
	}

	

	@Test
	public void testPureType() {
		fail("Not yet implemented");
	}
*/
}
