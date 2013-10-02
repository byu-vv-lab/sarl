package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType.RealKind;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionComparatorStub;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.object.Objects;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.Types;

public class CommonSymbolicTypeFactoryTest {
	
	//creating a new typeFactory in order to create concrete type objects.
	CommonSymbolicTypeFactory typeFactory, typeFactory2;
	ObjectFactory objectFactory;
	NumberFactory numberFactory;
	NumericExpression numberExpr;
	
	CommonSymbolicIntegerType idealIntKind, boundedIntKind;
	CommonSymbolicRealType idealRealKind, floatRealKind;
	ArrayList<CommonSymbolicType> typesList;
	CommonSymbolicType typesArray[];

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
		typeFactory = new CommonSymbolicTypeFactory(objectFactory);
		typeFactory2 = (CommonSymbolicTypeFactory)Types.newTypeFactory(objectFactory);
		idealIntKind = new CommonSymbolicIntegerType(IntegerKind.IDEAL);
		boundedIntKind = new CommonSymbolicIntegerType(IntegerKind.BOUNDED);
		idealRealKind = new CommonSymbolicRealType(RealKind.IDEAL);
		floatRealKind = new CommonSymbolicRealType(RealKind.FLOAT);
		typesList = new ArrayList<CommonSymbolicType>();
		typesList.add(idealRealKind);
		typesList.add(floatRealKind);
		typesList.add(idealIntKind);
		typesList.add(boundedIntKind);
		
		typesArray = new CommonSymbolicType[4];
		typesArray[0] = idealRealKind;
		typesArray[1] = floatRealKind;
		typesArray[2] = idealIntKind;
		typesArray[3] = boundedIntKind;
	}

	@After
	public void tearDown() throws Exception {
		
	}



	@Test
	public void testObjectFactory() {
		assertTrue(typeFactory.objectFactory() instanceof ObjectFactory);
	}
	
	@Test
	public void testIntegerType() {
		assertTrue(typeFactory.integerType() instanceof SymbolicIntegerType);
	}
	
	@Test
	public void testHerbrandIntegerType() {
		assertEquals(typeFactory.herbrandIntegerType().integerKind(), SymbolicIntegerType.IntegerKind.HERBRAND);
	}
	
	@Test
	public void testRealType() {
		assertTrue(typeFactory.realType() instanceof SymbolicRealType);
	}

	@Test
	public void testHerbrandRealType() {
		assertEquals(typeFactory.herbrandRealType().realKind(), SymbolicRealType.RealKind.HERBRAND);
	}
	
	@Test
	public void testCharacterType() {
		assertTrue(typeFactory.characterType() instanceof SymbolicType);
	}
	
	@Test
	public void testSequence(){
		//System.out.println(types.toString());
		assertTrue(typeFactory.sequence(typesList) instanceof SymbolicTypeSequence);
	}
	
	
	@Test
	public void testSequence2(){
		//List<CommonSymbolicType> a = Arrays.asList(idealIntKind, boundedIntKind, idealRealKind, floatRealKind);
		assertTrue(
				typeFactory.sequence(typesArray) instanceof SymbolicTypeSequence);
	}
	
	@Test
	public void testsingletonSequence(){
		assertEquals(typeFactory.singletonSequence(idealIntKind).numTypes(), 1);
	}
	
	
	@Test
	public void testBooleanType() {
		assertTrue(typeFactory.booleanType() instanceof CommonSymbolicPrimitiveType);
	}
	
	@Test
	public void testArrayType() {
		assertTrue(typeFactory.arrayType(idealIntKind) instanceof SymbolicArrayType);
	}
	
	@Test
	public void testTypeComparator(){
		assertNotEquals(typeFactory.typeComparator().compare(idealIntKind, idealRealKind), 0);
	}
	
	@Test
	public void testTypeSequenceComparator() {
		assertTrue(typeFactory.typeSequenceComparator() instanceof TypeSequenceComparator);
	}
	

	@Test
	public void testSetExpressionComparator() {
		assertNull(typeFactory.typeComparator().expressionComparator());
		typeFactory.setExpressionComparator(new ExpressionComparatorStub());
		typeFactory.init();
		assertNotNull(typeFactory.typeComparator().expressionComparator());
	}

	/*
	@Test
	public void testArrayTypeSymbolicTypeNumericExpression() {
		fail("Not yet implemented");
	}

	@Test
	public void testTupleType() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnionType() {
		fail("Not yet implemented");
	}

	@Test
	public void testFunctionType() {
		fail("Not yet implemented");
	}

	@Test
	public void testPureType() {
		fail("Not yet implemented");
	}
*/
}
