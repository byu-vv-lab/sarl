package edu.udel.cis.vsl.sarl.collections.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Comparator;

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
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionComparatorStub;
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
 * @author rwjones Test class for collections.common.Clj4SortedSymbolicMap
 */
public class CljSortedSymbolicMapTest {

	CommonObjectFactory fac;

	private static CommonObjectFactory objectFactory = new CommonObjectFactory(
			new RealNumberFactory());
	private static Comparator<SymbolicExpression> elementComparator = new ExpressionComparatorStub();

	private static SymbolicExpression x = new ExpressionStub("5");

	private static SymbolicExpression y = new ExpressionStub("y");

	private static SymbolicExpression a = new ExpressionStub("5");

	private static SymbolicExpression b = new ExpressionStub("9");

	private static SymbolicExpression z = new ExpressionStub("10");

	private static SymbolicExpression twenty = createExpression(20);
	private static SymbolicExpression forty = createExpression(40);
	private static SymbolicExpression sixty = createExpression(60);
	private static SymbolicExpression eighty = createExpression(80);
	private static SymbolicExpression hundred = createExpression(100);

	Collection<SymbolicExpression> set;
	private static CljSortedMap<SymbolicExpression, SymbolicExpression> test;
	private static CljSortedMap<SymbolicExpression, SymbolicExpression> test2;
	private static CljSortedMap<SymbolicExpression, SymbolicExpression> test3;
	private static CljSortedMap<SymbolicExpression, SymbolicExpression> test4;
	private static CljSortedMap<SymbolicExpression, SymbolicExpression> test5;
	private static CljSortedMap<SymbolicExpression, SymbolicExpression> canonicTest;

	public static SymbolicExpression createExpression(int expression) {
		SymbolicType symbolicType = new CommonSymbolicIntegerType(
				IntegerKind.IDEAL);
		NumberFactory numFact = Numbers.REAL_FACTORY;
		IntegerNumber expr = numFact.integer(expression);
		ObjectFactory objFact = Objects.newObjectFactory(numFact);
		SymbolicObject symObj = objFact.numberObject(expr);
		SymbolicTypeFactory typeFact = Types.newTypeFactory(objFact);
		CollectionFactory collectionFact = Collections
				.newCollectionFactory(objFact);
		ExpressionFactory exprFact = Expressions.newIdealExpressionFactory(
				numFact, objFact, typeFact, collectionFact);
		return exprFact.expression(SymbolicOperator.CONCRETE, symbolicType,
				symObj);
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		twenty = createExpression(20);
		forty = createExpression(40);
		sixty = createExpression(60);
		eighty = createExpression(80);
		eighty = objectFactory.canonic(eighty);
		hundred = createExpression(100);
		forty = objectFactory.canonic(forty);
		canonicTest = new CljSortedMap<SymbolicExpression, SymbolicExpression>(
				elementComparator);

		canonicTest = (CljSortedMap<SymbolicExpression, SymbolicExpression>) canonicTest
				.put(twenty, forty);
		canonicTest = (CljSortedMap<SymbolicExpression, SymbolicExpression>) canonicTest
				.put(sixty, forty);
		canonicTest = (CljSortedMap<SymbolicExpression, SymbolicExpression>) canonicTest
				.put(eighty, hundred);

		test = new CljSortedMap<SymbolicExpression, SymbolicExpression>(
				elementComparator);
		test3 = new CljSortedMap<SymbolicExpression, SymbolicExpression>(
				elementComparator);
		test4 = new CljSortedMap<SymbolicExpression, SymbolicExpression>(
				elementComparator);
		test5 = new CljSortedMap<SymbolicExpression, SymbolicExpression>(
				elementComparator);

		test = (CljSortedMap<SymbolicExpression, SymbolicExpression>) test.put(
				a, b);
		test = (CljSortedMap<SymbolicExpression, SymbolicExpression>) test.put(
				x, y);
		test = (CljSortedMap<SymbolicExpression, SymbolicExpression>) test.put(
				y, a);
		test = (CljSortedMap<SymbolicExpression, SymbolicExpression>) test.put(
				b, z);

		test2 = (CljSortedMap<SymbolicExpression, SymbolicExpression>) test
				.put(a, b);
		test2 = (CljSortedMap<SymbolicExpression, SymbolicExpression>) test
				.put(b, b);
		test2 = (CljSortedMap<SymbolicExpression, SymbolicExpression>) test
				.put(y, b);

		test5 = (CljSortedMap<SymbolicExpression, SymbolicExpression>) test5
				.put(a, b);
		test5 = (CljSortedMap<SymbolicExpression, SymbolicExpression>) test5
				.put(x, y);
		test5 = (CljSortedMap<SymbolicExpression, SymbolicExpression>) test5
				.put(y, a);
		test5 = (CljSortedMap<SymbolicExpression, SymbolicExpression>) test5
				.put(b, z);
		this.fac = null;
		this.fac = new CommonObjectFactory(new RealNumberFactory());

	}

	@After
	public void tearDown() throws Exception {

	}

	/**
	 * Test method for computeHashCode()
	 */
	@Test
	public void testComputeHashCode() {

		int testHash = test.computeHashCode();
		assertEquals(test.hashCode(), testHash);

	}

	/**
	 * Test method for canonizeChildren()
	 */
	@Test
	public void testCanonizeChildren() {
		assertFalse(canonicTest.isCanonic());
		assertFalse(twenty.isCanonic());
		assertTrue(forty.isCanonic());
		canonicTest = objectFactory.canonic(canonicTest);
		assertTrue(canonicTest.isCanonic());
		assertTrue(twenty.isCanonic());
		assertTrue(forty.isCanonic());

	}

	/**
	 * Test method for restrict()
	 */
	@Test
	public void testRestrict() {
		assertEquals(test.restrict(elementComparator).compare(a, b), -4);
		assertEquals(test.restrict(elementComparator).compare(a, x), 0);
	}

	/**
	 * Test method for size()
	 */
	@Test
	public void testSize() {
		assertEquals(test.size(), 3);
		assertEquals(test2.size(), 3);
	}

	/**
	 * Test method for iterator()
	 */
	@Test
	public void testIterator() {
		java.util.Iterator<SymbolicExpression> temp = test.iterator();
		String testString = "[";
		while (temp.hasNext()) {
			testString = testString + temp.next().toString() + ",";
		}
		testString = testString + "]";
		assertEquals(testString, "[y,10,5,]");
	}

	/**
	 * Test method for get()
	 */
	@Test
	public void testGet() {
		assertEquals(test.get(a), test.get(x));
	}

	/**
	 * Test method for keys()
	 */
	@Test
	public void testKeys() {
		assertEquals(test.keys().toString(), "[5, 9, y]");
	}

	/**
	 * Test method for values()
	 */
	@Test
	public void testValues() {
		assertEquals(test.values().toString(), "[y, 10, 5]");
	}

	/**
	 * Test method for entries()
	 */
	@Test
	public void testEntries() {
		assertEquals(test.entries().toString(), "[[5 y], [9 10], [y 5]]");
	}

	/**
	 * Test method for isEmpty()
	 */
	@Test
	public void testIsEmpty() {
		assertFalse(test.isEmpty());
		assertTrue(test3.isEmpty());
	}

	/**
	 * Test method for collectionEqualsSymbolicCollectionOfV()
	 */
	@Test
	public void testCollectionEqualsSymbolicCollectionOfV() {
		assertTrue(test.collectionEquals(test5));
		assertTrue(test3.collectionEquals(test4));
		assertFalse(test3.collectionEquals(test5));
		assertFalse(test.collectionEquals(test2));
		assertFalse(test.collectionEquals(test3));
	}

	/**
	 * Test method for put()
	 */
	@Test
	public void testPut() {
		assertEquals(test.put(z, a).keys().toString(), "[10, 5, 9, y]");
	}

	/**
	 * Test method for remove()
	 */
	@Test
	public void testRemove() {
		assertEquals(test.remove(a).keys().toString(), "[9, y]");
		assertEquals(test3.remove(a).keys().toString(), "[]");
	}

	/**
	 * Test method for comparator()
	 */
	@Test
	public void testComparator() {
		assertEquals(
				canonicTest.comparator().compare(canonicTest.get(twenty),
						canonicTest.get(sixty)), 0);
		assertEquals(
				canonicTest.comparator().compare(canonicTest.get(twenty),
						canonicTest.get(eighty)), 3);
	}

	/**
	 * Test method for toStringBufferBoolean()
	 */
	@Test
	public void testToStringBufferBoolean() {
		assertEquals(test3.toStringBuffer(true).toString(), "{}");
		assertEquals(test.toStringBuffer(true).toString(),
				"{5->y, 9->10, y->5}");
	}

	/**
	 * Test method for toStringBufferLong()
	 */
	@Test
	public void testToStringBufferLong() {
		assertEquals(test3.toStringBufferLong().toString(), "SortedMap{}");
		assertEquals(test.toStringBufferLong().toString(),
				"SortedMap{5->y, 9->10, y->5}");
	}

}
