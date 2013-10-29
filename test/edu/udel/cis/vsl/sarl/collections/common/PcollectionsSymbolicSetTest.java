package edu.udel.cis.vsl.sarl.collections.common;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pcollections.HashTreePMap;
import org.pcollections.HashTreePSet;
import org.pcollections.PSet;

import edu.udel.cis.vsl.sarl.IF.Transform;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionComparatorStub;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionStub;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.number.real.RealNumberFactory;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class PcollectionsSymbolicSetTest {

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
	
	private static SymbolicExpression three = new ExpressionStub("3");	
	private static SymbolicExpression four = new ExpressionStub("4");
	private static SymbolicExpression five = new ExpressionStub("5");
	private static SymbolicExpression otherfive = new ExpressionStub("5");
	private static SymbolicExpression nine = new ExpressionStub("9");
	private static SymbolicExpression ten = new ExpressionStub("10");
	
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
		//pSetHash3 = (PcollectionsSymbolicSet<SymbolicExpression>) pSetHash3.add(five);

					
		pSetCollection = new PcollectionsSymbolicSet<SymbolicExpression>(collectionSet);
		pSetCollection = (PcollectionsSymbolicSet<SymbolicExpression>) pSetCollection.add(five);
		pSetCollection = (PcollectionsSymbolicSet<SymbolicExpression>) pSetCollection.add(three);
		pSetCollection = (PcollectionsSymbolicSet<SymbolicExpression>) pSetCollection.add(otherfive);
		pSetCollection2 = new PcollectionsSymbolicSet<SymbolicExpression>(collectionSet);
		pSetCollection2 = (PcollectionsSymbolicSet<SymbolicExpression>) pSetCollection2.add(five);
		pSetCollection2 = (PcollectionsSymbolicSet<SymbolicExpression>) pSetCollection2.add(three);
		pSetCollection2 = (PcollectionsSymbolicSet<SymbolicExpression>) pSetCollection2.add(otherfive);
		pSetCollection3 = new PcollectionsSymbolicSet<SymbolicExpression>(collectionSet);
		//pSetCollection3 = (PcollectionsSymbolicSet<SymbolicExpression>) pSetCollection3.add(five);
		
		pSetPlain = new PcollectionsSymbolicSet<SymbolicExpression>();
		pSetPlain = (PcollectionsSymbolicSet<SymbolicExpression>) pSetPlain.add(five);
		pSetPlain = (PcollectionsSymbolicSet<SymbolicExpression>) pSetPlain.add(three);
		pSetPlain = (PcollectionsSymbolicSet<SymbolicExpression>) pSetPlain.add(otherfive);
		pSetPlain2 = new PcollectionsSymbolicSet<SymbolicExpression>();
		pSetPlain2 = (PcollectionsSymbolicSet<SymbolicExpression>) pSetPlain2.add(five);
		pSetPlain2 = (PcollectionsSymbolicSet<SymbolicExpression>) pSetPlain2.add(three);
		pSetPlain2 = (PcollectionsSymbolicSet<SymbolicExpression>) pSetPlain2.add(otherfive);
		pSetPlain3 = new PcollectionsSymbolicSet<SymbolicExpression>();
		//pSetPlain3 = (PcollectionsSymbolicSet<SymbolicExpression>) pSetPlain3.add(five);
		
		/*Transform<SymbolicExpression,SymbolicExpression> tform = new Transform<SymbolicExpression,SymbolicExpression>{
			
			public SymbolicExpression apply(SymbolicExpression x){
				return new SymbolicExpression("x * 5");
			}
		}*/
		
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testComputeHashCode() {
		int hashCodeHash = pSetHash.computeHashCode();
		assertEquals(pSetHash.hashCode(),hashCodeHash);
		int hashCodeCollection = pSetCollection.computeHashCode();
		assertEquals(pSetCollection.hashCode(),hashCodeCollection);
		int hashCodePlain = pSetPlain.computeHashCode();
		assertEquals(pSetPlain.hashCode(),hashCodePlain);
	}
/*
	@Test
	public void testCanonizeChildren() {
		fail("Not yet implemented");
	}*/

	@Test
	public void testCollectionEquals() {
		assertTrue(pSetHash.equals(pSetHash));
		assertTrue(pSetHash.equals(pSetHash2));
		assertFalse(pSetHash.equals(pSetHash3));
		assertTrue(pSetHash.equals(pSetPlain));
		assertTrue(pSetHash.equals(pSetCollection));
		assertFalse(pSetHash.equals(pSetHash3));
		assertFalse(pSetHash.equals(pSetPlain3));
		
		assertTrue(pSetCollection.equals(pSetCollection));
		assertTrue(pSetCollection.equals(pSetCollection2));
		assertFalse(pSetCollection.equals(pSetCollection3));
		assertTrue(pSetCollection.equals(pSetPlain));
		assertTrue(pSetCollection.equals(pSetCollection));
		assertFalse(pSetCollection.equals(pSetHash3));
		assertFalse(pSetCollection.equals(pSetPlain3));
		
		assertTrue(pSetPlain.equals(pSetPlain));
		assertTrue(pSetPlain.equals(pSetPlain2));
		assertFalse(pSetPlain.equals(pSetPlain3));
		assertTrue(pSetPlain.equals(pSetHash));
		assertTrue(pSetPlain.equals(pSetCollection));
		assertFalse(pSetPlain.equals(pSetHash3));
		assertFalse(pSetPlain.equals(pSetCollection3));
	}

	@Test
	public void testSize() {
		assertEquals(pSetHash.size(),2);
		assertEquals(pSetCollection.size(),2);
		assertEquals(pSetPlain.size(),2);
	}

	@Test
	public void testIterator() {
		assertEquals(iteratorString(pSetHash),"[3,5,]");
		assertEquals(iteratorString(pSetHash3),"[]");
		assertEquals(iteratorString(pSetCollection),"[3,5,]");
		assertEquals(iteratorString(pSetCollection3),"[]");
		assertEquals(iteratorString(pSetPlain),"[3,5,]");
		assertEquals(iteratorString(pSetPlain3),"[]");
	}

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

	@Test
	public void testToStringBufferLong() {
		assertEquals(pSetHash.toStringBufferLong().toString(),"UnsortedSet{3,5}");
		assertEquals(pSetHash3.toStringBufferLong().toString(),"UnsortedSet{}");
		
		assertEquals(pSetCollection.toStringBufferLong().toString(),"UnsortedSet{3,5}");
		assertEquals(pSetCollection3.toStringBufferLong().toString(),"UnsortedSet{}");
		
		assertEquals(pSetPlain.toStringBufferLong().toString(),"UnsortedSet{3,5}");
		assertEquals(pSetPlain3.toStringBufferLong().toString(),"UnsortedSet{}");
	}

	@Test
	public void testIsSorted() {
		assertTrue(pSetHash.isSorted());
		assertTrue(pSetCollection.isSorted());
		assertTrue(pSetPlain.isSorted());
	}

	@Test
	public void testAdd() {
		assertEquals(pSetHash.toString(),"{3,5}");
		assertEquals(pSetHash.add(ten).toString(),"{3,5,10}");
		
		assertEquals(pSetCollection.toString(),"{3,5}");
		assertEquals(pSetCollection.add(ten).toString(),"{3,5,10}");
		
		assertEquals(pSetPlain.toString(),"{3,5}");
		assertEquals(pSetPlain.add(ten).toString(),"{3,5,10}");
	}

	@Test
	public void testAddAll() {
		assertEquals(pSetHash.toString(),"{3,5}");
		assertEquals(pSetHash.addAll(pSetHash3.add(ten).add(four).add(nine)).toString(),"{3,4,5,9,10}");
		
		assertEquals(pSetCollection.toString(),"{3,5}");
		assertEquals(pSetCollection.addAll(pSetCollection3.add(ten).add(four).add(nine)).toString(),"{3,4,5,9,10}");
		
		assertEquals(pSetPlain.toString(),"{3,5}");
		assertEquals(pSetPlain.addAll(pSetPlain3.add(ten).add(four).add(nine)).toString(),"{3,4,5,9,10}");
	}

	@Test
	public void testRemove() {
		assertEquals(pSetHash.remove(three).toString(), "{5}");
		assertEquals(pSetHash3.remove(three).toString(), "{}");
		
		assertEquals(pSetCollection.remove(three).toString(), "{5}");
		assertEquals(pSetCollection3.remove(three).toString(), "{}");
		
		assertEquals(pSetPlain.remove(three).toString(), "{5}");
		assertEquals(pSetPlain3.remove(three).toString(), "{}");
	}

	@Test
	public void testRemoveAll() {
		assertEquals(pSetHash.addAll(pSetHash3.add(ten).add(four).add(nine)).removeAll(pSetHash2).toString(), "{4,9,10}");
		assertEquals(pSetCollection.addAll(pSetCollection3.add(ten).add(four).add(nine)).removeAll(pSetCollection2).toString(), "{4,9,10}");
		assertEquals(pSetPlain.addAll(pSetPlain3.add(ten).add(four).add(nine)).removeAll(pSetCollection2).toString(), "{4,9,10}");
		
		assertEquals(pSetHash3.removeAll(pSetHash2).toString(), "{}");
		assertEquals(pSetCollection3.removeAll(pSetCollection2).toString(), "{}");
		assertEquals(pSetPlain3.removeAll(pSetCollection2).toString(), "{}");
	}

	/*@Test
	public void testKeepOnly() {
		
	}*/

	@Test
	public void testComparator() {
		assertEquals(pSetHash.comparator(),null);
		assertEquals(pSetCollection.comparator(),null);
		assertEquals(pSetPlain.comparator(),null);
	}

	/*@Test
	public void testApply() {
		
	}*/

}
