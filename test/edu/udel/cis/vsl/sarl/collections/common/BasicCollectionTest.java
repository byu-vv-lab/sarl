package edu.udel.cis.vsl.sarl.collections.common;

import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
import edu.udel.cis.vsl.sarl.expr.common.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.object.Objects;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.Types;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
import edu.udel.cis.vsl.sarl.type.common.CommonSymbolicIntegerType;




public class BasicCollectionTest {
	
	BasicCollection<SymbolicExpression> someCollection;
	LinkedList<Integer> intList;
	SymbolicExpression expr5, expr2, expr100;
	NumberFactory numFact;
	ObjectFactory objectFactory;
	ExpressionFactory exprFact;
	SymbolicTypeFactory typeFactory;
	CollectionFactory collectionFactory;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		intList.add(5);
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
		
	}

	@After
	public void tearDown() throws Exception {
	}


	/*@Test
	public void testComputeHashCode() {
		
	}*/
/*
	@Test
	public void testCanonizeChildren() {
		fail("Not yet implemented");
	}

	@Test
	public void testCollectionEquals() {
		fail("Not yet implemented");
	}

	@Test
	public void testBasicCollection() {
		fail("Not yet implemented");
	}

	@Test
	public void testSize() {
		fail("Not yet implemented");
	}

	@Test
	public void testIterator() {
		fail("Not yet implemented");
	}

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
