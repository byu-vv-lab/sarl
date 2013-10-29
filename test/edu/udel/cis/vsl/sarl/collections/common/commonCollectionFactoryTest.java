package edu.udel.cis.vsl.sarl.collections.common;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pcollections.HashTreePSet;

import edu.udel.cis.vsl.sarl.IF.Transform;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionComparatorStub;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionStub;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.number.real.RealNumberFactory;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;
public class commonCollectionFactoryTest {
	
	private static CommonObjectFactory objectFactory = new CommonObjectFactory(new RealNumberFactory());
	private static Comparator<SymbolicExpression> elementComparator = new ExpressionComparatorStub();
	CommonCollectionFactory collectionFactory;
	
	private static SymbolicExpression three = new ExpressionStub("3");	
	private static SymbolicExpression four = new ExpressionStub("4");
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

	@Test
	public void testSetElementComparator() {
		collectionFactory.setElementComparator(elementComparator);
		assertTrue(collectionFactory.comparator()  instanceof Comparator);
	}
	
	@Test
	public void testBasicCollection() {
		Collection<SymbolicExpression> testCollection = new LinkedList<SymbolicExpression>();
		assertTrue(collectionFactory.basicCollection(testCollection) instanceof BasicCollection<?>);
	}
	
	@Test
	public void testEmptyHashSet() {
		assertEquals(collectionFactory.emptyHashSet().size(), 0);
	}
	
	/*@Test
	public void testEmptySortedSet() {
		assertEquals(collectionFactory.emptySortedSet().size(), 0);
	}*/
	
	@Test
	public void testSingletonHashSet() {
		assertEquals(collectionFactory.singletonHashSet(five).toString(), "{5}");
	}
	
	/*@Test
	public void testSingletonSortedSet() {
		assertEquals(collectionFactory.singletonSortedSet(five).toString(), "{5}");
	}*/
	
	@Test
	public void testIterableSequence() {
		Iterable<SymbolicExpression> iterableList = new LinkedList<SymbolicExpression>();
		assertEquals(collectionFactory.sequence(iterableList).add(three).toString(),"<3>");
	}
	
	@Test
	public void testArraySequence() {
		SymbolicExpression[] arrayList = new SymbolicExpression[1];
		arrayList[0] = three;
		assertEquals(collectionFactory.sequence(arrayList).toString(),"<3>");
	}
	
	@Test
	public void testSingletonSequence() {
		assertEquals(collectionFactory.singletonSequence(three).toString(),"<3>");
	}
	
	/*@Test
	public void testEmptySequence() {
		//collectionFactory.setElementComparator(elementComparator);
		//collectionFactory.init();
		//assertEquals(collectionFactory.emptySequence().size(),0);
	}
	
	@Test
	public void testEmptySortedMap() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testEmptyHashMap() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testSingletonSortedMap() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testSingletonHashMap() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testSortedMap() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testHashMap() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testEmptySortedSet() {
		fail("Not yet implemented");
	}
	*/

	
}
