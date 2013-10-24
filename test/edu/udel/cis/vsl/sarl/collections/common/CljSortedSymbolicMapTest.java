package edu.udel.cis.vsl.sarl.collections.common;

import static org.junit.Assert.*;


import java.util.Collection;
import java.util.Comparator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionComparatorStub;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionStub;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.number.real.RealNumberFactory;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class CljSortedSymbolicMapTest {

	CommonObjectFactory fac;
	
	//private static ObjectFactory objectFactory = new ObjectFactoryStub();

	private static Comparator<SymbolicExpression> elementComparator = new ExpressionComparatorStub();

	//private static CollectionFactory collectionFactory = Collections.newCollectionFactory(objectFactory);

	private static SymbolicExpression x = new ExpressionStub("5");

	private static SymbolicExpression y = new ExpressionStub("y");
	
	private static SymbolicExpression a = new ExpressionStub("5");

	private static SymbolicExpression b = new ExpressionStub("9");
	
	private static SymbolicExpression z = new ExpressionStub("10");
	
	Collection<SymbolicExpression> set;
	private static SymbolicMap<SymbolicExpression, SymbolicExpression> collectionMap1;
	private static CljSortedSymbolicMap<SymbolicExpression,SymbolicExpression> test;
	private static CljSortedSymbolicMap<SymbolicExpression,SymbolicExpression> test2;
	private static CljSortedSymbolicMap<SymbolicExpression,SymbolicExpression> test3;
	private static CljSortedSymbolicMap<SymbolicExpression,SymbolicExpression> test4;
	private static CljSortedSymbolicMap<SymbolicExpression,SymbolicExpression> test5;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		test = new CljSortedSymbolicMap<SymbolicExpression,SymbolicExpression>(elementComparator);
		test3 = new CljSortedSymbolicMap<SymbolicExpression,SymbolicExpression>(elementComparator);
		test4 = new CljSortedSymbolicMap<SymbolicExpression,SymbolicExpression>(elementComparator);
		test5 = new CljSortedSymbolicMap<SymbolicExpression,SymbolicExpression>(elementComparator);
		
		test = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) test.put(a, b);
		test = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) test.put(x, y);
		test = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) test.put(y, a);
		test = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) test.put(b, z);
		
		test2 = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) test.put(a, b);
		test2 = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) test.put(b, b);
		test2 = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) test.put(y, b);
		
		test5 = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) test5.put(a, b);
		test5 = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) test5.put(x, y);
		test5 = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) test5.put(y, a);
		test5 = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) test5.put(b, z);
		
		this.fac = null;
		this.fac = new CommonObjectFactory(new RealNumberFactory());
		
	}

	@After
	public void tearDown() throws Exception {
		
	}


	@Test
	public void testComputeHashCode() {
		
		int testHash = test.computeHashCode();
		assertEquals(test.hashCode(),testHash);
		
	}

	@Test
	public void testCanonizeChildren() {
		assertFalse(test.isCanonic());
		
	}

	@Test
	public void testRestrict() {
		assertEquals(test.restrict(elementComparator).compare(a, b), -4);
		assertEquals(test.restrict(elementComparator).compare(a, x), 0);
	}

	@Test
	public void testSize() {
		assertEquals(test.size(),3);
		assertEquals(test2.size(),3);
	}

	@Test
	public void testIterator() {
		java.util.Iterator<SymbolicExpression> temp = test.iterator();
		String testString = "[";
		while(temp.hasNext())
		{
			//temp.next().toString();
			testString = testString + temp.next().toString() + ",";			
		}
		testString = testString + "]";
		assertEquals(testString, "[y,10,5,]");
	}

	@Test
	public void testGet() {
		assertEquals(test.get(a),test.get(x));
	}

	@Test
	public void testKeys() {
		assertEquals(test.keys().toString(),"[5, 9, y]");
	}

	@Test
	public void testValues() {
		assertEquals(test.values().toString(),"[y, 10, 5]");
	}

	@Test
	public void testEntries() {
		assertEquals(test.entries().toString(),"[[5 y], [9 10], [y 5]]");
	}

	@Test
	public void testIsEmpty() {
		assertFalse(test.isEmpty());
		assertTrue(test3.isEmpty());
	}

	@Test
	public void testCollectionEqualsSymbolicCollectionOfV() {
		assertTrue(test.collectionEquals(test5));
		assertTrue(test3.collectionEquals(test4));
		assertFalse(test3.collectionEquals(test5));
		assertFalse(test.collectionEquals(test2));
		assertFalse(test.collectionEquals(test3));
		assertFalse(test.collectionEquals(collectionMap1));
	}

	@Test
	public void testIsSorted() {
		assertTrue(test.isSorted());
	}

	@Test
	public void testPut() {
		assertEquals(test.put(z, a).keys().toString(), "[10, 5, 9, y]");
	}

	@Test
	public void testRemove() {
		assertEquals(test.remove(a).keys().toString(), "[9, y]");
		assertEquals(test3.remove(a).keys().toString(), "[]");
	}

	@Test
	public void testComparator() {
		assertEquals(test.comparator().compare(test.get(a), test.get(x)), 0);
		assertEquals(test.comparator().compare(test.get(a), test.get(y)), 68);
	}

	@Test
	public void testToStringBufferBoolean() {
		assertEquals(test3.toStringBuffer(true).toString(), "{}");
		assertEquals(test.toStringBuffer(true).toString(), "{5->y, 9->10, y->5}");
	}

	@Test
	public void testToStringBufferLong() {
		assertEquals(test3.toStringBufferLong().toString(), "SortedMap{}");
		assertEquals(test.toStringBufferLong().toString(), "SortedMap{5->y, 9->10, y->5}");
	}

}
