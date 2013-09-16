package edu.udel.cis.vsl.sarl.object.common;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CommonCharObjectTest {
	
	CommonCharObject charA;
	CommonCharObject char1;

	@Before
	public void setUp() throws Exception {
		this.charA = new CommonCharObject('A');
		this.char1 = new CommonCharObject('1');
	}

	@Test
	public void testComputeHashCode() {
		CommonCharObject newcharA = new CommonCharObject('A');
		CommonCharObject newchar1 = new CommonCharObject('1');
		assertEquals(this.charA.computeHashCode(), newcharA.computeHashCode());
		assertEquals(this.char1.computeHashCode(), newchar1.computeHashCode());
	}

	@Test
	public void testIntrinsicEquals() {
		CommonCharObject newcharA = new CommonCharObject('A');
		CommonCharObject newchar1 = new CommonCharObject('1');
		assertTrue(this.charA.intrinsicEquals(newcharA));
		assertTrue(this.char1.intrinsicEquals(newchar1));
		assertFalse(this.charA.intrinsicEquals(newchar1));
		assertFalse(this.char1.intrinsicEquals(newcharA));
	}

//	@Test
//	public void testCanonizeChildren() {
//	}

	@Test
	public void testToString() {
		assertEquals("A", this.charA.toString());
		assertEquals("1", this.char1.toString());
	}

	@Test
	public void testGetChar() {
		assertEquals('A', this.charA.getChar());
		assertEquals('1', this.char1.getChar());
	}

	@Test
	public void testCompareTo() {
		CommonCharObject newcharA = new CommonCharObject('A');
		CommonCharObject newchar1 = new CommonCharObject('1');
		assertEquals(0, this.charA.compareTo(newcharA));
		assertTrue(0 < this.charA.compareTo(newchar1));
		assertTrue(0 > this.char1.compareTo(newcharA));
		assertEquals(0, this.char1.compareTo(newchar1));
	}

	@Test
	public void testToStringBuffer() {
		assertEquals("(A)", this.charA.toStringBuffer(true).toString());
		assertEquals("A", this.charA.toStringBuffer(false).toString());
		assertEquals("(1)", this.char1.toStringBuffer(true).toString());
		assertEquals("1", this.char1.toStringBuffer(false).toString());
	}

	@Test
	public void testToStringBufferLong() {
		assertEquals("A", this.charA.toStringBufferLong().toString());
		assertEquals("1", this.char1.toStringBufferLong().toString());
	}

}
