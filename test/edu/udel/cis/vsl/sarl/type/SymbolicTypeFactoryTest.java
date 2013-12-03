package edu.udel.cis.vsl.sarl.type;

import static org.junit.Assert.assertEquals;
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

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject.SymbolicObjectKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType.RealKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.collections.Collections;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionComparatorStub;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.cnf.CnfFactory;
import edu.udel.cis.vsl.sarl.expr.common.ExpressionComparator;
import edu.udel.cis.vsl.sarl.ideal.common.CommonIdealFactory;
import edu.udel.cis.vsl.sarl.ideal.common.NumericPrimitive;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.object.Objects;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
import edu.udel.cis.vsl.sarl.type.common.CommonSymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.type.common.CommonSymbolicFunctionType;
import edu.udel.cis.vsl.sarl.type.common.CommonSymbolicIntegerType;
import edu.udel.cis.vsl.sarl.type.common.CommonSymbolicPrimitiveType;
import edu.udel.cis.vsl.sarl.type.common.CommonSymbolicRealType;
import edu.udel.cis.vsl.sarl.type.common.CommonSymbolicTupleType;
import edu.udel.cis.vsl.sarl.type.common.CommonSymbolicTypeFactory;
import edu.udel.cis.vsl.sarl.type.common.CommonSymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.type.common.TypeSequenceComparator;

/**
 * Testing CommonSymbolicTypeFactory, which creates all types.
 * 
 * @author alali
 *
 */
public class SymbolicTypeFactoryTest {
	
	/**
	 * creating a new typeFactory in order to instantiate concrete type objects.
	 */
	SymbolicTypeFactory typeFactory, typeFactory2;
	
	/**
	 * used to create ExpressionComparator.
	 */
	NumericExpressionFactory numericFactory;
	/**
	 * an ObjectFactory object that is used to instantiate the typeFactory, and typeFactory2
	 */
	ObjectFactory objectFactory;
	/**
	 * a NumboerFactory that is used to instantiate the objectFactory
	 */
	NumberFactory numberFactory;
	
	/**
	 * a CollectionFactory to be used to instantiate NumericExpressionFactory
	 */
	CollectionFactory collectionFactory;
	
	/**
	 * a BooleanExpressionFactory to be used to instantiate NumericExpressionFacoty
	 */
	BooleanExpressionFactory booleanFactory;
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
	 * an ExpressionExpression to be used for comparing
	 * CompleteArrayType
	 */
	ExpressionComparator expressionComparator;
	
	/**
	 * integer types to be used in creating the SequenceType
	 */
	CommonSymbolicIntegerType idealIntKind, boundedIntKind;
	
	/**
	 * CompleteArrayTypes to be used in the factory testing
	 */
	SymbolicCompleteArrayType completeArrayType1, completeArrayType2, completeArrayType3;
	
	/**
	 * Real types to be used in creating the SequenceType
	 */
	CommonSymbolicRealType idealRealKind, floatRealKind;
	
	/**
	 * an ArrayList on SymbolicType to be used in creating the SequenceType
	 */
	ArrayList<SymbolicType> typesList;
	
	/**
	 * an array of SymbolicTypes to be used in creating the SequenceType
	 */
	SymbolicType typesArray[];
	
	/**
	 * creating unionType to be used for tests
	 */
	SymbolicUnionType unionType, unionType2, unionType3;
	
	/**
	 typeSequence is used to assign the array of CommonSymbolicType to the variables that is unionType. 
	 */
	SymbolicTypeSequence typeSequence, typeSequence2;
	
	CommonSymbolicFunctionType functionType;
	
	SymbolicFunctionType functionType2, functionType3;
	
	SymbolicTupleType tupleType, tupleType2, tupleType3;
	
	StringObject tupleStringObject, tupleStringObject3, unionStringObject, 
	unionStringObject2, unionStringObject3;

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
		collectionFactory = Collections.newCollectionFactory(objectFactory);
		booleanFactory = new CnfFactory(typeFactory, objectFactory, collectionFactory);
		numericFactory = new CommonIdealFactory(numberFactory, objectFactory, typeFactory, collectionFactory, booleanFactory);
		expressionComparator = new ExpressionComparator(numericFactory.comparator(), objectFactory.comparator(), typeFactory.typeComparator());
		boundedIntKind = new CommonSymbolicIntegerType(IntegerKind.BOUNDED);
		idealIntKind = new CommonSymbolicIntegerType(IntegerKind.IDEAL);
		idealRealKind = new CommonSymbolicRealType(RealKind.IDEAL);
		floatRealKind = new CommonSymbolicRealType(RealKind.FLOAT);
		symbolicObject = objectFactory.numberObject(numberFactory.integer(4));
		numericPrimitive = new NumericPrimitive(SymbolicOperator.CONCRETE, idealIntKind, symbolicObject);
		completeArrayType1 = typeFactory.arrayType(boundedIntKind, numericPrimitive);
		completeArrayType2 = typeFactory.arrayType(boundedIntKind, numericPrimitive);
		completeArrayType3 = typeFactory.arrayType(idealIntKind, numericPrimitive);
		typesList = new ArrayList<SymbolicType>();
		typesArray = new SymbolicType[5];
		
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
		typesArray[4] = completeArrayType2;
		
		typeSequence = new CommonSymbolicTypeSequence(typesArray);
		typeSequence2 = new CommonSymbolicTypeSequence(typesList);
		tupleStringObject = objectFactory.stringObject("myTuple");
		tupleStringObject3 = objectFactory.stringObject("myTuple3");
		unionStringObject = objectFactory.stringObject("myUnion");
		unionStringObject2 = objectFactory.stringObject("myUnion2");
		unionStringObject3 = objectFactory.stringObject("myUnion3");
		tupleType = typeFactory.tupleType(tupleStringObject, typeFactory.sequence(typesArray));
		tupleType2 = typeFactory.tupleType(tupleStringObject, typeFactory.sequence(typesArray));
		tupleType3 = typeFactory.tupleType(tupleStringObject3, typeFactory.sequence(typesList));
		unionType = typeFactory.unionType(unionStringObject, typeSequence);
		unionType2 = typeFactory.unionType(unionStringObject2, typeSequence);
		unionType3 = typeFactory.unionType(unionStringObject3, typeSequence2);
		functionType = (CommonSymbolicFunctionType)typeFactory.functionType(typeSequence, floatRealKind);
		functionType2 = typeFactory.functionType(typeSequence, boundedIntKind);
		functionType3 = typeFactory.functionType(typeSequence2, boundedIntKind);
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
		assertNull(typeFactory.typeComparator().expressionComparator());
		typeFactory.typeComparator().setExpressionComparator(expressionComparator);
		assertNotNull(typeFactory.typeComparator().expressionComparator());		
		assertEquals(typeFactory.typeComparator().compare(completeArrayType1, completeArrayType2), 0);
		assertNotEquals(typeFactory.typeComparator().compare(completeArrayType1, completeArrayType3), 0);
		assertNotEquals(typeFactory.typeComparator().compare(functionType, functionType2), 0);
		assertNotEquals(typeFactory.typeComparator().compare(functionType, functionType3), 0);
		assertNotEquals(typeFactory.typeComparator().compare(unionType, unionType2), 0);
		assertNotEquals(typeFactory.typeComparator().compare(unionType, unionType3), 0);
		//testing two different types
		assertNotEquals(typeFactory.typeComparator().compare(completeArrayType1, functionType), 0);
		//testing two identical boolean types
		assertEquals(typeFactory.typeComparator().compare(typeFactory.booleanType(), typeFactory.booleanType()), 0);
		//testing two identical TupleTypes
		assertEquals(typeFactory.typeComparator().compare(tupleType, tupleType2), 0);
		assertNotEquals(typeFactory.typeComparator().compare(tupleType, tupleType3), 0);

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
	
	/**
	 * testing the return of unionType() if it is of type SymbolicUnionType.
	 */
	@Test
	public void testUnionType() {
		//SymbolicTypeSequence nullSequence = null;
		
		assertTrue((typeFactory.unionType(unionStringObject, 
				typeFactory.sequence(typesList))) instanceof SymbolicUnionType);
		//assertNull(typeFactory.unionType(unionStringObject2, nullSequence));
				
	}
	
	/**
	 * Testing the pureType of different SymbolicTypes.
	 * PureType returns the same type after removing the length.
	 * So a SymbolicCompleteArrayType(t1) and SymbolicArrayType(t1)
	 * should have the same pureType 
	 */
	@Test
	public void testPureType() {
		CommonSymbolicCompleteArrayType cArray = (CommonSymbolicCompleteArrayType) typeFactory.arrayType(boundedIntKind, numericPrimitive);
				
		assertEquals(typeFactory.pureType(boundedIntKind), boundedIntKind.getPureType());
		assertEquals(typeFactory.pureType(floatRealKind), floatRealKind.getPureType());
		assertEquals(typeFactory.pureType(cArray), cArray.getPureType());
		assertNull(functionType.getPureType());
		functionType.setPureType(functionType);
		assertNotNull(functionType.getPureType());
		assertEquals(functionType.getPureType(), (CommonSymbolicFunctionType)typeFactory.pureType(functionType));
		assertEquals(typeFactory.pureType(unionType).symbolicObjectKind(), SymbolicObjectKind.TYPE);
		assertTrue(typeFactory.pureType(functionType2) instanceof SymbolicFunctionType);
		assertTrue((SymbolicTupleType)typeFactory.pureType(tupleType) instanceof SymbolicTupleType);
	}
}
