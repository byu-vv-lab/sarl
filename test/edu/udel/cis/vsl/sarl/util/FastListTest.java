package edu.udel.cis.vsl.sarl.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FastListTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void emptyTest() {
		FastList<String> list = new FastList<String>();

		assertTrue(list.isEmpty());
	}

	@Test
	public void addFront1() {
		FastList<String> list = new FastList<String>();

		list.addFront("a");

		FastNode<String> first = list.getFirst(), last = list.getLast();

		assertEquals("a", first.getData());
		assertSame(first, last);
		assertNull(first.getPrev());
		assertNull(first.getNext());
	}

	@Test
	public void addBack1() {
		FastList<String> list = new FastList<String>();

		list.add("a");

		FastNode<String> first = list.getFirst(), last = list.getLast();

		assertEquals("a", first.getData());
		assertSame(first, last);
		assertNull(first.getPrev());
		assertNull(last.getNext());
	}

	@Test
	public void addBack2() {
		FastList<String> list = new FastList<String>();

		list.add("a");
		list.add("b");

		FastNode<String> first = list.getFirst(), last = list.getLast();

		assertEquals("a", first.getData());
		assertEquals("b", last.getData());
		assertSame(last, first.getNext());
		assertSame(first, last.getPrev());
		assertNull(first.getPrev());
		assertNull(last.getNext());
	}

	@Test
	public void addFront2() {
		FastList<String> list = new FastList<String>();

		list.addFront("b");
		list.addFront("a");

		FastNode<String> first = list.getFirst(), last = list.getLast();

		assertEquals("a", first.getData());
		assertEquals("b", last.getData());
		assertSame(last, first.getNext());
		assertSame(first, last.getPrev());
		assertNull(first.getPrev());
		assertNull(last.getNext());
	}

	@Test
	public void add3() {
		FastList<String> list = new FastList<String>();

		list.addFront("b");
		list.addFront("a");
		list.add("c");

		FastNode<String> first = list.getFirst(), second = first.getNext(), third = second
				.getNext();

		assertEquals("a", first.getData());
		assertEquals("b", second.getData());
		assertEquals("c", third.getData());
		assertNull(first.getPrev());
		assertSame(second, first.getNext());
		assertSame(first, second.getPrev());
		assertSame(third, second.getNext());
		assertSame(second, third.getPrev());
		assertNull(third.getNext());
		assertSame(third, list.getLast());
	}

	@Test
	public void append1() {
		FastList<String> list1 = new FastList<String>();
		FastList<String> list2 = new FastList<String>();

		list1.add("a");
		list1.add("b");
		list2.add("1");
		list2.add("2");
		list1.append(list2);

		FastNode<String> u1 = list1.getFirst(), u2 = u1.getNext(), u3 = u2
				.getNext(), u4 = u3.getNext();

		assertEquals("a", u1.getData());
		assertEquals("b", u2.getData());
		assertEquals("1", u3.getData());
		assertEquals("2", u4.getData());
		assertNull(u4.getNext());
		assertSame(u3, u4.getPrev());
		assertSame(u2, u3.getPrev());
		assertSame(u1, u2.getPrev());
		assertNull(u1.getPrev());
		assertTrue(list2.isEmpty());
	}

	@Test
	public void prepend1() {
		FastList<String> list1 = new FastList<String>();
		FastList<String> list2 = new FastList<String>();

		list1.add("1");
		list1.add("2");
		list2.add("a");
		list2.add("b");
		list1.prepend(list2);

		FastNode<String> u1 = list1.getFirst(), u2 = u1.getNext(), u3 = u2
				.getNext(), u4 = u3.getNext();

		assertEquals("a", u1.getData());
		assertEquals("b", u2.getData());
		assertEquals("1", u3.getData());
		assertEquals("2", u4.getData());
		assertNull(u4.getNext());
		assertSame(u3, u4.getPrev());
		assertSame(u2, u3.getPrev());
		assertSame(u1, u2.getPrev());
		assertNull(u1.getPrev());
		assertTrue(list2.isEmpty());
	}

}
