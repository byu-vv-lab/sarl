package edu.udel.cis.vsl.sarl.object.common;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CommonStringObjectTest {

	CommonStringObject str1;
	CommonStringObject str2;
	
	@Before
	public void setUp() throws Exception {
		this.str1 = new CommonStringObject("TheString");
		this.str2 = new CommonStringObject("AnotherString");
	}

	@Test
	public void testComputeHashCode() {
		CommonStringObject str3 = new CommonStringObject("TheString");
		assertEquals(this.str1.computeHashCode(), str3.computeHashCode());
	}

	@Test
	public void testIntrinsicEquals() {
		CommonStringObject str3 = new CommonStringObject("TheString");
		assertTrue(this.str1.intrinsicEquals(str3));
		assertFalse(this.str1.intrinsicEquals(this.str2));
	}

	@Test
	public void testToString() {
		assertEquals("TheString", this.str1.toString());
	}

	@Test
	public void testGetString() {
		assertEquals("TheString", this.str1.getString());
	}

	@Test
	public void testCompareTo() {
		assertTrue(0<this.str1.compareTo(this.str2));
		assertTrue(0>this.str2.compareTo(this.str1));
		assertTrue(0==this.str1.compareTo(this.str1));
	}

	@Test
	public void testToStringBuffer() {
		assertEquals("(TheString)", this.str1.toStringBuffer(true).toString());
		assertEquals("TheString", this.str1.toStringBuffer(false).toString());
	}

	@Test
	public void testToStringBufferLong() {
		assertEquals("TheString", this.str1.toStringBufferLong().toString());
	}

}
