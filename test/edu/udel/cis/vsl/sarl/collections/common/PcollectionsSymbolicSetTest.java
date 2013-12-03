package edu.udel.cis.vsl.sarl.collections.common;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pcollections.HashTreePSet;
import org.pcollections.PSet;

import edu.udel.cis.vsl.sarl.IF.Transform;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;
import edu.udel.cis.vsl.sarl.collections.Collections;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionStub;
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

/**
 * 
 * @author rwjones
 * Test class for collections.common.PcollectionsSymbolicSet
 */
public class PcollectionsSymbolicSetTest {

	CommonObjectFactory fac;
	private static CommonObjectFactory objectFactory = new CommonObjectFactory(new RealNumberFactory());
	
	private static PSet<SymbolicExpression> pTree;
	private static Collection<SymbolicExpression> collectionSet;
	
	private static PcollectionsSymbolicSet<SymbolicExpression> pSetHash;
	private static PcollectionsSymbolicSet<SymbolicExpression> pSetHash2;
	private static PcollectionsSymbolicSet<SymbolicExpression> pSetHash3;
	private static PcollectionsSymbolicSet<SymbolicExpression> pSetCollection;
	private static PcollectionsSymbolicSet<SymbolicExpression> pSetCollection2;
	private static PcollectionsSymbolicSet<SymbolicExpression> pSetCollection3;
	private static PcollectionsSymbolicSet<SymbolicExpression> pSetPlain;
	private static PcollectionsSymbolicSet<SymbolicExpression> pSetPlain2;
	private static PcollectionsSymbolicSet<SymbolicExpression> pSetPlain3;
	private static PcollectionsSymbolicSet<SymbolicExpression> emptyPSet;	
	private static SymbolicExpression three = new ExpressionStub("3");	
	private static SymbolicExpression four = new ExpressionStub("4");
	private static SymbolicExpression five = new ExpressionStub("5");
	private static SymbolicExpression otherfive = new ExpressionStub("5");
	private static SymbolicExpression nine = new ExpressionStub("9");
	private static SymbolicExpression ten = new ExpressionStub("10");
	
	private PcollectionsSymbolicSet<SymbolicExpression> canonicSet;
	private PcollectionsSymbolicSet<SymbolicExpression> canonicSet2;
	private PcollectionsSymbolicSet<SymbolicExpression> canonicSet3;
	private static SymbolicExpression twenty = createExpression(20);
	private static SymbolicExpression forty = createExpression(40);
	private static SymbolicExpression sixty = createExpression(60);
	private static SymbolicExpression eighty = createExpression(80);
	private static SymbolicExpression hundred = createExpression(100);
	
	public static SymbolicExpression createExpression(int expression){
		SymbolicType symbolicType = new CommonSymbolicIntegerType(IntegerKind.IDEAL);
		NumberFactory numFact = Numbers.REAL_FACTORY;
		IntegerNumber expr = numFact.integer(expression);
		ObjectFactory objFact = Objects.newObjectFactory(numFact);
		SymbolicObject symObj =  objFact.numberObject(expr);
		SymbolicTypeFactory typeFact = Types.newTypeFactory(objFact);
		CollectionFactory collectionFact = Collections.newCollectionFactory(objFact);
		ExpressionFactory exprFact = Expressions.newIdealExpressionFactory(numFact, objFact, typeFact, collectionFact);
		return exprFact.expression(SymbolicOperator.CONCRETE, symbolicType, symObj);
	}
	
    Transform<SymbolicExpression,SymbolicExpression> transform= new Transform<SymbolicExpression,SymbolicExpression>()
    {
		public ExpressionStub apply(SymbolicExpression x){
			String temp = x.toString() + "00";
			return new ExpressionStub(temp);
		}
	};
	
	Transform<SymbolicExpression,SymbolicExpression> transform2= new Transform<SymbolicExpression,SymbolicExpression>()
    {
		public ExpressionStub apply(SymbolicExpression x){
			String temp = x.toString();
			return new ExpressionStub(temp);
		}
	};
	
	public static String iteratorString(PcollectionsSymbolicSet<SymbolicExpression> T)
	{
		Iterator<SymbolicExpression> someIterator = T.iterator();
		String iteratorString = "[";
		while(someIterator.hasNext())
		{
			iteratorString = iteratorString + someIterator.next().toString() + ",";
		}
		iteratorString = iteratorString + "]";
		return iteratorString;
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		pTree = HashTreePSet.empty();
		collectionSet = new LinkedList<SymbolicExpression>();
		
		pSetHash = new PcollectionsSymbolicSet<SymbolicExpression>(pTree);
		pSetHash = (PcollectionsSymbolicSet<SymbolicExpression>) pSetHash.add(five);
		pSetHash = (PcollectionsSymbolicSet<SymbolicExpression>) pSetHash.add(three);
		pSetHash = (PcollectionsSymbolicSet<SymbolicExpression>) pSetHash.add(otherfive);
		pSetHash2 = new PcollectionsSymbolicSet<SymbolicExpression>(pTree);
		pSetHash2 = (PcollectionsSymbolicSet<SymbolicExpression>) pSetHash2.add(five);
		pSetHash2 = (PcollectionsSymbolicSet<SymbolicExpression>) pSetHash2.add(three);
		pSetHash2 = (PcollectionsSymbolicSet<SymbolicExpression>) pSetHash2.add(otherfive);
		pSetHash3 = new PcollectionsSymbolicSet<SymbolicExpression>(pTree);

					
		pSetCollection = new PcollectionsSymbolicSet<SymbolicExpression>(collectionSet);
		pSetCollection = (PcollectionsSymbolicSet<SymbolicExpression>) pSetCollection.add(five);
		pSetCollection = (PcollectionsSymbolicSet<SymbolicExpression>) pSetCollection.add(three);
		pSetCollection = (PcollectionsSymbolicSet<SymbolicExpression>) pSetCollection.add(otherfive);
		pSetCollection2 = new PcollectionsSymbolicSet<SymbolicExpression>(collectionSet);
		pSetCollection2 = (PcollectionsSymbolicSet<SymbolicExpression>) pSetCollection2.add(five);
		pSetCollection2 = (PcollectionsSymbolicSet<SymbolicExpression>) pSetCollection2.add(three);
		pSetCollection2 = (PcollectionsSymbolicSet<SymbolicExpression>) pSetCollection2.add(otherfive);
		pSetCollection3 = new PcollectionsSymbolicSet<SymbolicExpression>(collectionSet);
		
		pSetPlain = new PcollectionsSymbolicSet<SymbolicExpression>();
		pSetPlain = (PcollectionsSymbolicSet<SymbolicExpression>) pSetPlain.add(five);
		pSetPlain = (PcollectionsSymbolicSet<SymbolicExpression>) pSetPlain.add(three);
		pSetPlain = (PcollectionsSymbolicSet<SymbolicExpression>) pSetPlain.add(otherfive);
		pSetPlain2 = new PcollectionsSymbolicSet<SymbolicExpression>();
		pSetPlain2 = (PcollectionsSymbolicSet<SymbolicExpression>) pSetPlain2.add(five);
		pSetPlain2 = (PcollectionsSymbolicSet<SymbolicExpression>) pSetPlain2.add(three);
		pSetPlain2 = (PcollectionsSymbolicSet<SymbolicExpression>) pSetPlain2.add(otherfive);
		pSetPlain3 = new PcollectionsSymbolicSet<SymbolicExpression>();
		emptyPSet = new PcollectionsSymbolicSet<SymbolicExpression>();
		
		twenty = createExpression(20);
		forty = createExpression(40);
		sixty = createExpression(60);
		eighty = createExpression(80);
		eighty = objectFactory.canonic(eighty);
		hundred = createExpression(100);
		forty = objectFactory.canonic(forty);
		sixty = objectFactory.canonic(sixty);

		
		canonicSet = new PcollectionsSymbolicSet<SymbolicExpression>(collectionSet);
		canonicSet2 = new PcollectionsSymbolicSet<SymbolicExpression>(collectionSet);
		canonicSet3 = new PcollectionsSymbolicSet<SymbolicExpression>(collectionSet);
		
		canonicSet3 = (PcollectionsSymbolicSet<SymbolicExpression>) canonicSet3.add(twenty);
		canonicSet3 = (PcollectionsSymbolicSet<SymbolicExpression>) canonicSet3.add(hundred);

		
		canonicSet = (PcollectionsSymbolicSet<SymbolicExpression>) canonicSet.add(twenty);
		canonicSet = (PcollectionsSymbolicSet<SymbolicExpression>) canonicSet.add(eighty);
		canonicSet = (PcollectionsSymbolicSet<SymbolicExpression>) canonicSet.add(forty);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for computeHashCode()
	 */
	@Test
	public void testComputeHashCode() {
		int hashCodeHash = pSetHash.computeHashCode();
		assertEquals(pSetHash.hashCode(),hashCodeHash);
		int hashCodeCollection = pSetCollection.computeHashCode();
		assertEquals(pSetCollection.hashCode(),hashCodeCollection);
		int hashCodePlain = pSetPlain.computeHashCode();
		assertEquals(pSetPlain.hashCode(),hashCodePlain);
	}

	/**
	 * Test method for canonizeChildren()
	 */
	@Test
	public void testCanonizeChildren() {
		
		assertFalse(canonicSet.isCanonic());
		canonicSet = objectFactory.canonic(canonicSet);
		assertTrue(canonicSet.isCanonic());		
		assertFalse(canonicSet2.isCanonic());
		canonicSet2 = objectFactory.canonic(canonicSet2);
		assertTrue(canonicSet2.isCanonic());
		canonicSet3 = objectFactory.canonic(canonicSet3);
	}

	/**
	 * Test method for collectionEquals()
	 */
	@Test
	public void testCollectionEquals() {
		assertTrue(pSetHash.collectionEquals(pSetHash));
		assertTrue(pSetHash.collectionEquals(pSetHash2));
		assertFalse(pSetHash.collectionEquals(pSetHash3));
		assertTrue(pSetHash.collectionEquals(pSetPlain));
		assertTrue(pSetHash.collectionEquals(pSetCollection));
		assertFalse(pSetHash.collectionEquals(pSetHash3));
		assertFalse(pSetHash.collectionEquals(pSetPlain3));
		
		assertTrue(pSetCollection.collectionEquals(pSetCollection));
		assertTrue(pSetCollection.collectionEquals(pSetCollection2));
		assertFalse(pSetCollection.collectionEquals(pSetCollection3));
		assertTrue(pSetCollection.collectionEquals(pSetPlain));
		assertTrue(pSetCollection.collectionEquals(pSetCollection));
		assertFalse(pSetCollection.collectionEquals(pSetHash3));
		assertFalse(pSetCollection.collectionEquals(pSetPlain3));
		
		assertTrue(pSetPlain.collectionEquals(pSetPlain));
		assertTrue(pSetPlain.collectionEquals(pSetPlain2));
		assertFalse(pSetPlain.collectionEquals(pSetPlain3));
		assertTrue(pSetPlain.collectionEquals(pSetHash));
		assertTrue(pSetPlain.collectionEquals(pSetCollection));
		assertFalse(pSetPlain.collectionEquals(pSetHash3));
		assertFalse(pSetPlain.collectionEquals(pSetCollection3));
		
	}

	/**
	 * Test method for size()
	 */
	@Test
	public void testSize() {
		assertEquals(pSetHash.size(),2);
		assertEquals(pSetCollection.size(),2);
		assertEquals(pSetPlain.size(),2);
	}

	/**
	 * Test method for iterator()
	 */
	@Test
	public void testIterator() {
		assertEquals(iteratorString(pSetHash),"[3,5,]");
		assertEquals(iteratorString(pSetHash3),"[]");
		assertEquals(iteratorString(pSetCollection),"[3,5,]");
		assertEquals(iteratorString(pSetCollection3),"[]");
		assertEquals(iteratorString(pSetPlain),"[3,5,]");
		assertEquals(iteratorString(pSetPlain3),"[]");
	}

	/**
	 * Test method for contains()
	 */
	@Test
	public void testContains() {
		assertTrue(pSetHash.contains(five));
		assertFalse(pSetHash.contains(ten));
		assertFalse(pSetHash3.contains(five));
		
		assertTrue(pSetCollection.contains(five));
		assertFalse(pSetCollection.contains(ten));
		assertFalse(pSetCollection3.contains(five));
		
		assertTrue(pSetPlain.contains(five));
		assertFalse(pSetPlain.contains(ten));
		assertFalse(pSetPlain3.contains(five));
	}

	/**
	 * Test method for toStringBuffer()
	 */
	@Test
	public void testToStringBuffer() {
		assertEquals(pSetHash.toStringBuffer(true).toString(),"{3,5}");
		assertEquals(pSetHash.toStringBuffer(false).toString(),"3,5");
		assertEquals(pSetHash3.toStringBuffer(true).toString(),"{}");
		assertEquals(pSetHash3.toStringBuffer(false).toString(),"");
		
		assertEquals(pSetCollection.toStringBuffer(true).toString(),"{3,5}");
		assertEquals(pSetCollection.toStringBuffer(false).toString(),"3,5");
		assertEquals(pSetCollection3.toStringBuffer(true).toString(),"{}");
		assertEquals(pSetCollection3.toStringBuffer(false).toString(),"");
		
		assertEquals(pSetPlain.toStringBuffer(true).toString(),"{3,5}");
		assertEquals(pSetPlain.toStringBuffer(false).toString(),"3,5");
		assertEquals(pSetPlain3.toStringBuffer(true).toString(),"{}");
		assertEquals(pSetPlain3.toStringBuffer(false).toString(),"");
	}

	/**
	 * Test method for toStringBufferLong()
	 */
	@Test
	public void testToStringBufferLong() {
		assertEquals(pSetHash.toStringBufferLong().toString(),"UnsortedSet{3,5}");
		assertEquals(pSetHash3.toStringBufferLong().toString(),"UnsortedSet{}");
		
		assertEquals(pSetCollection.toStringBufferLong().toString(),"UnsortedSet{3,5}");
		assertEquals(pSetCollection3.toStringBufferLong().toString(),"UnsortedSet{}");
		
		assertEquals(pSetPlain.toStringBufferLong().toString(),"UnsortedSet{3,5}");
		assertEquals(pSetPlain3.toStringBufferLong().toString(),"UnsortedSet{}");
	}

	/**
	 * Test method for isSorted()
	 */
	@Test
	public void testIsSorted() {
		assertFalse(pSetHash.isSorted());
		assertFalse(pSetCollection.isSorted());
		assertFalse(pSetPlain.isSorted());
	}

	/**
	 * Test method for add()
	 */
	@Test
	public void testAdd() {
		assertEquals(pSetHash.toString(),"{3,5}");
		assertEquals(pSetHash.add(ten).toString(),"{3,5,10}");
		
		assertEquals(pSetCollection.toString(),"{3,5}");
		assertEquals(pSetCollection.add(ten).toString(),"{3,5,10}");
		
		assertEquals(pSetPlain.toString(),"{3,5}");
		assertEquals(pSetPlain.add(ten).toString(),"{3,5,10}");
	}

	/**
	 * Test method for addAll()
	 */
	@Test
	public void testAddAll() {
		assertEquals(pSetHash.toString(),"{3,5}");
		assertEquals(pSetHash.addAll(pSetHash3.add(ten).add(four).add(nine)).toString(),"{3,4,5,9,10}");
		
		assertEquals(pSetCollection.toString(),"{3,5}");
		assertEquals(pSetCollection.addAll(pSetCollection3.add(ten).add(four).add(nine)).toString(),"{3,4,5,9,10}");
		
		assertEquals(pSetPlain.toString(),"{3,5}");
		assertEquals(pSetPlain.addAll(pSetPlain3.add(ten).add(four).add(nine)).toString(),"{3,4,5,9,10}");
	}

	/**
	 * Test method for remove() 
	 */
	@Test
	public void testRemove() {
		assertEquals(pSetHash.remove(three).toString(), "{5}");
		assertEquals(pSetHash3.remove(three).toString(), "{}");
		
		assertEquals(pSetCollection.remove(three).toString(), "{5}");
		assertEquals(pSetCollection3.remove(three).toString(), "{}");
		
		assertEquals(pSetPlain.remove(three).toString(), "{5}");
		assertEquals(pSetPlain3.remove(three).toString(), "{}");
	}

	/**
	 * Test method for removeAll()
	 */
	@Test
	public void testRemoveAll() {
		assertEquals(pSetHash.addAll(pSetHash3.add(ten).add(four).add(nine)).removeAll(pSetHash2).toString(), "{4,9,10}");
		assertEquals(pSetCollection.addAll(pSetCollection3.add(ten).add(four).add(nine)).removeAll(pSetCollection2).toString(), "{4,9,10}");
		assertEquals(pSetPlain.addAll(pSetPlain3.add(ten).add(four).add(nine)).removeAll(pSetCollection2).toString(), "{4,9,10}");
		
		assertEquals(pSetHash3.removeAll(pSetHash2).toString(), "{}");
		assertEquals(pSetCollection3.removeAll(pSetCollection2).toString(), "{}");
		assertEquals(pSetPlain3.removeAll(pSetCollection2).toString(), "{}");
	}

	/**
	 * Test method for keepOnly(), expects a thrown exception
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void testKeepOnly() {
		pSetPlain.keepOnly(pSetCollection);
	}

	/**
	 * Test method for comparator()
	 */
	@Test
	public void testComparator() {
		assertEquals(pSetHash.comparator(),null);
		assertEquals(pSetCollection.comparator(),null);
		assertEquals(pSetPlain.comparator(),null);
	}

	/**
	 * Test method for apply()
	 */
	@Test
	public void testApply() {
		assertEquals(pSetPlain.apply(transform).toString(),"{300,500}");
		assertEquals(pSetPlain.apply(transform2).toString(), "{3,5}");
		assertEquals(emptyPSet.apply(transform).toString(), "{}");
	}

}
