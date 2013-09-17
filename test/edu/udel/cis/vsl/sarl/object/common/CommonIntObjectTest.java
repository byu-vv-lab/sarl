package edu.udel.cis.vsl.sarl.object.common;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CommonIntObjectTest {

	CommonIntObject shortint;
	CommonIntObject longint;
	CommonIntObject negint;
	CommonIntObject zeroint;

	@Before
	public void setUp() throws Exception {
		this.shortint = new CommonIntObject(1);
		this.longint = new CommonIntObject(1234567890);
		this.negint = new CommonIntObject(-1234567890);
		this.zeroint = new CommonIntObject(0);
	}
	
	@Test
	public void testComputeHashCode() {
		CommonIntObject newshortint = new CommonIntObject(1);
		CommonIntObject newlongint = new CommonIntObject(1234567890);
		assertEquals(this.shortint.computeHashCode(), newshortint.computeHashCode());
		assertEquals(this.longint.computeHashCode(), newlongint.computeHashCode());
	}

	@Test
	public void testIntrinsicEquals() {
		CommonIntObject newshortint = new CommonIntObject(1);
		CommonIntObject newlongint = new CommonIntObject(1234567890);
		assertTrue(this.shortint.intrinsicEquals(newshortint));
		assertTrue(this.longint.intrinsicEquals(newlongint));
		assertFalse(this.shortint.intrinsicEquals(newlongint));
		assertFalse(this.longint.intrinsicEquals(newshortint));
	}

	/**
	 * Method not implemented
	 */
	@Test
	public void testCanonizeChildren() {
	}

	@Test
	public void testToString() {
		assertEquals("1", this.shortint.toString());
		assertEquals("1234567890", this.longint.toString());
	}

	@Test
	public void testGetInt() {
		assertEquals(1, this.shortint.getInt());
		assertEquals(1234567890, this.longint.getInt());
	}

	@Test
	public void testMinWith() {
		assertEquals(1, this.shortint.minWith(this.longint).getInt());
		assertEquals(1, this.longint.minWith(this.shortint).getInt());
	}

	@Test
	public void testMaxWith() {
		assertEquals(1234567890, this.shortint.maxWith(this.longint).getInt());
		assertEquals(1234567890, this.longint.maxWith(this.shortint).getInt());
	}

	@Test
	public void testMinus() {
		assertEquals(1234567889 , this.longint.minus(this.shortint).getInt());
		assertEquals(-1234567889, this.shortint.minus(this.longint).getInt());
	}

	@Test
	public void testPlus() {
		assertEquals(1234567891, this.shortint.plus(this.longint).getInt());
		assertEquals(0, this.longint.plus(this.negint).getInt());
	}

	@Test
	public void testSignum() {
		assertEquals(0, this.zeroint.signum());
		assertEquals(1, this.longint.signum());
		assertEquals(-1, this.negint.signum());
	}

	@Test
	public void testIsZero() {
		assertTrue(this.zeroint.isZero());
		assertFalse(this.longint.isZero());
		assertFalse(this.negint.isZero());
	}

	@Test
	public void testIsPositive() {
		assertTrue(this.longint.isPositive());
		assertFalse(this.zeroint.isPositive());
		assertFalse(this.negint.isPositive());
	}

	@Test
	public void testIsNegative() {
		assertTrue(this.negint.isNegative());
		assertFalse(this.zeroint.isNegative());
		assertFalse(this.longint.isNegative());
	}

	@Test
	public void testIsOne() {
		assertTrue(this.shortint.isOne());
		assertFalse(this.zeroint.isOne());
		assertFalse(this.negint.isOne());
	}

	@Test
	public void testCompareTo() {
		assertTrue(0==this.shortint.compareTo(this.shortint));
		assertTrue(0>this.shortint.compareTo(this.longint));
		assertTrue(0<this.shortint.compareTo(this.negint));
	}

	@Test
	public void testToStringBuffer() {
		assertEquals("(1)", this.shortint.toStringBuffer(true).toString());
		assertEquals("1", this.shortint.toStringBuffer(false).toString());
		assertEquals("(-1234567890)", this.negint.toStringBuffer(true).toString());
		assertEquals("-1234567890", this.negint.toStringBuffer(false).toString());
	}

	@Test
	public void testToStringBufferLong() {
		assertEquals("1", this.shortint.toStringBufferLong().toString());
		assertEquals("-1234567890", this.negint.toStringBufferLong().toString());
	}

}
