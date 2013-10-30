package edu.udel.cis.vsl.sarl.collections.common;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.Collections;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.Expressions;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.number.real.RealNumberFactory;
import edu.udel.cis.vsl.sarl.object.Objects;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;
import edu.udel.cis.vsl.sarl.type.Types;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
import edu.udel.cis.vsl.sarl.type.common.CommonSymbolicIntegerType;




public class BasicCollectionTest {
	
	BasicCollection<SymbolicExpression> someCollection, someOtherCollection, someThirdCollection, someEmptyCollection;
	Collection<SymbolicExpression> collectionList1, collectionList2, emptyList;
	SymbolicExpression expr5, expr2, expr100;
	NumberFactory numFact;
	ObjectFactory objectFactory;
	ExpressionFactory exprFact;
	SymbolicTypeFactory typeFactory;
	CollectionFactory collectionFactory;
	CommonObjectFactory fac;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		SymbolicType symbolicType = new CommonSymbolicIntegerType(IntegerKind.IDEAL);
		numFact = Numbers.REAL_FACTORY;
		IntegerNumber FIVE = numFact.integer(5);
		objectFactory = Objects.newObjectFactory(numFact);
		SymbolicObject symObj =  objectFactory.numberObject(FIVE);
		typeFactory = Types.newTypeFactory(objectFactory);
		collectionFactory = Collections.newCollectionFactory(objectFactory);
		exprFact = Expressions.newIdealExpressionFactory(numFact, objectFactory, typeFactory, collectionFactory);
		expr5 = exprFact.expression(SymbolicOperator.CONCRETE, symbolicType, symObj);
		expr2 = exprFact.expression(SymbolicOperator.CONCRETE, symbolicType, objectFactory.numberObject(numFact.integer(2)));
		expr100 = exprFact.expression(SymbolicOperator.CONCRETE, symbolicType, objectFactory.numberObject(numFact.integer(100)));
		collectionList1 = new LinkedList<SymbolicExpression>();
		collectionList1.add(expr5);
		collectionList1.add(expr2);
		collectionList1.add(expr100);
		collectionList2 = new LinkedList<SymbolicExpression>();
		collectionList2.add(expr5);
		collectionList2.add(expr2);
		emptyList = new LinkedList<SymbolicExpression>();
		someCollection = new BasicCollection<SymbolicExpression>(collectionList1);
		someOtherCollection = new BasicCollection<SymbolicExpression>(collectionList1);
		someThirdCollection = new BasicCollection<SymbolicExpression>(collectionList2);
		someEmptyCollection = new BasicCollection<SymbolicExpression>(emptyList);
		this.fac = null;
		this.fac = new CommonObjectFactory(new RealNumberFactory());
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testComputeHashCode() {
		assertTrue(someCollection.hashCode()==someOtherCollection.hashCode());
	}
	
	@Test
	public void testCollectionEquals() {
		assertTrue(someCollection.collectionEquals(someOtherCollection));
		assertFalse(someCollection.collectionEquals(someThirdCollection));
	}

	@Test
	public void testSize() {
		assertTrue(someCollection.size()==3);
		assertFalse(someThirdCollection.size()==3);
	}
	
	@Test
	public void testIterator() {
		assertTrue(someCollection.iterator().hasNext());
		assertFalse(someEmptyCollection.iterator().hasNext());
	}
	
	@Test
	public void testToStringBuffer() {	
		assertEquals(someCollection.toStringBuffer(true).toString(),"{5, 2, 100}");
	}
	
	@Test
	public void testToStringBufferLong() {
		assertEquals(someCollection.toStringBufferLong().toString(),"Collection{5, 2, 100}");
	}

	@Test(expected=SARLInternalException.class)
	public void testCanonizeChildren(){
		someCollection.canonizeChildren(this.fac);
	}
	
}
