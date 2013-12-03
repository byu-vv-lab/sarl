package edu.udel.cis.vsl.sarl.collections.common;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionComparatorStub;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionStub;
import edu.udel.cis.vsl.sarl.number.real.RealNumberFactory;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;


/**
 * 
 * @author rwjones
 * Test class for collections.common.CommonCollectionFactory
 */
public class CommonCollectionFactoryTest {
	
	private static CommonObjectFactory objectFactory = new CommonObjectFactory(new RealNumberFactory());
	private static Comparator<SymbolicExpression> elementComparator = new ExpressionComparatorStub();
	CommonCollectionFactory collectionFactory;
	
	private static SymbolicExpression three = new ExpressionStub("3");
	private static SymbolicExpression five = new ExpressionStub("5");
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		collectionFactory = new CommonCollectionFactory(objectFactory);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for setElementComparator()
	 */
	@Test
	public void testSetElementComparator() {
		collectionFactory.setElementComparator(elementComparator);
		assertTrue(collectionFactory.comparator()  instanceof Comparator);
	}

	/**
	 * Test method for basicCollection()
	 */
	@Test
	public void testBasicCollection() {
		Collection<SymbolicExpression> testCollection = new LinkedList<SymbolicExpression>();
		assertTrue(collectionFactory.basicCollection(testCollection) instanceof BasicCollection<?>);
	}

	/**
	 * Test method for emptyHashSet()
	 */
	@Test
	public void testEmptyHashSet() {
		assertEquals(collectionFactory.emptyHashSet().size(), 0);
	}

	/**
	 * Test method for emptySortedSet(), expects thrown exception
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void testEmptySortedSet() {
		collectionFactory.emptySortedSet();
	}

	/**
	 * Test method for singletoHashSet()
	 */
	@Test
	public void testSingletonHashSet() {
		assertEquals(collectionFactory.singletonHashSet(five).toString(), "{5}");
	}

	/**
	 * Test method for singletonSortedSet(), expects thrown exception
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void testSingletonSortedSet() {
		collectionFactory.singletonSortedSet(five);
	}

	/**
	 * Test method for iterableSequence()
	 */
	@Test
	public void testIterableSequence() {
		Iterable<SymbolicExpression> iterableList = new LinkedList<SymbolicExpression>();
		assertEquals(collectionFactory.sequence(iterableList).add(three).toString(),"<3>");
	}

	/**
	 * Test method for arraySequence()
	 */
	@Test
	public void testArraySequence() {
		SymbolicExpression[] arrayList = new SymbolicExpression[1];
		arrayList[0] = three;
		assertEquals(collectionFactory.sequence(arrayList).toString(),"<3>");
	}

	/**
	 * Test method for singleSequence()
	 */
	@Test
	public void testSingletonSequence() {
		assertEquals(collectionFactory.singletonSequence(three).toString(),"<3>");
	}

	/**
	 * Test method for singletonHashMap()
	 */
	@Test
	public void testSingletonHashMap() {
		
		assertEquals(collectionFactory.singletonHashMap(five, three).toString(),"{5->3}");
	}

	/**
	 * Test method for sortedMap(), one argument
	 */
	@Test
	public void testSortedMap() {
		HashMap<SymbolicExpression,SymbolicExpression> testMap = new HashMap<SymbolicExpression, SymbolicExpression>();
		assertEquals(collectionFactory.sortedMap(elementComparator,testMap).put(five, three).toString(),"{5->3}");
	}

	/**
	 * Test method for sortedMap(), two arguments
	 */
	@Test
	public void testSortedMap2() {
		HashMap<SymbolicExpression,SymbolicExpression> testMap = new HashMap<SymbolicExpression, SymbolicExpression>();
		assertEquals(collectionFactory.sortedMap(testMap).put(five, three).toString(),"{5->3}");
	}

	/**
	 * Test method for hashMap()
	 */
	@Test
	public void testHashMap() {
		HashMap<SymbolicExpression,SymbolicExpression> testMap = new HashMap<SymbolicExpression, SymbolicExpression>();
		assertEquals(collectionFactory.hashMap(testMap).put(five, three).toString(),"{5->3}");
	}

	/**
	 * Test method for emptySortedSetComparator()
	 */
	@Test
	public void testEmptySortedSetComparator()
	{
		assertEquals(collectionFactory.emptySortedSet(elementComparator),null);
	}

	/**
	 * Test method for singletonSortedSetComparator()
	 */
	@Test
	public void testSingletonSortedSetComparator()
	{
		assertEquals(collectionFactory.singletonSortedSet(five, elementComparator),null);
	}
	
	/**
	 * Test method for singletonSortedMap()
	 */
	@Test
	public void testSingletonSortedMap()
	{
		collectionFactory.setElementComparator(elementComparator);
		collectionFactory.init();
		assertEquals(collectionFactory.singletonSortedMap(five,three).toString(),"{5->3}");
	}
	
}
