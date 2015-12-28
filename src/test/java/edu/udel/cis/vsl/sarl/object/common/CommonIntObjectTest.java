package edu.udel.cis.vsl.sarl.object.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CommonIntObject
 * @author jtirrell
 *
 */
public class CommonIntObjectTest {

	/**
	 * Initialized to a CommonIntObject of value 1 upon setUp
	 */
	CommonIntObject shortint;
	/**
	 * Initialized to a CommonIntObject of value 1234567890 upon setUp
	 */
	CommonIntObject longint;
	/**
	 * Initialized to a CommonIntObject of value -1234567890 upon setUp
	 */
	CommonIntObject negint;
	/**
	 * Initialized to a CommonIntObject of value 0 upon setUp
	 */
	CommonIntObject zeroint;

	/**
	 * Initializes shortint, longint, negint, zeroint
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.shortint = new CommonIntObject(1);
		this.longint = new CommonIntObject(1234567890);
		this.negint = new CommonIntObject(-1234567890);
		this.zeroint = new CommonIntObject(0);
	}
	
	/**
	 * Verifies that computeHashCode returns the same hash code for objects of the same value
	 */
	@Test
	public void testComputeHashCode() {
		CommonIntObject newshortint = new CommonIntObject(1);
		CommonIntObject newlongint = new CommonIntObject(1234567890);
		
		assertEquals(this.shortint.computeHashCode(), newshortint.computeHashCode());
		assertEquals(this.longint.computeHashCode(), newlongint.computeHashCode());
	}

	/**
	 * Verifies that intrinsicEquals returns true for objects of the same value, false otherwise
	 */
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
	 * Verifies that toString returns the object's correct value as a string
	 */
	@Test
	public void testToString() {
		assertEquals("1", this.shortint.toString());
		assertEquals("1234567890", this.longint.toString());
	}

	/**
	 * Verifies that getInt returns the objects value as an int.
	 */
	@Test
	public void testGetInt() {
		assertEquals(1, this.shortint.getInt());
		assertEquals(1234567890, this.longint.getInt());
	}

	/**
	 * Verifies that minWith returns the correct minimum of CommonIntObjects
	 */
	@Test
	public void testMinWith() {
		assertEquals(1, this.shortint.minWith(this.longint).getInt());
		assertEquals(1, this.longint.minWith(this.shortint).getInt());
	}

	/**
	 * Verifies that maxWith returns the correct maximum of CommonIntObjects
	 */
	@Test
	public void testMaxWith() {
		assertEquals(1234567890, this.shortint.maxWith(this.longint).getInt());
		assertEquals(1234567890, this.longint.maxWith(this.shortint).getInt());
	}

	/**
	 * Verifies that minus performs a correct subtraction on two CommonIntObjects
	 */
	@Test
	public void testMinus() {
		assertEquals(1234567889 , this.longint.minus(this.shortint).getInt());
		assertEquals(-1234567889, this.shortint.minus(this.longint).getInt());
	}

	/**
	 * Verifies that plus performs a correct addition on two CommonIntObjects
	 */
	@Test
	public void testPlus() {
		assertEquals(1234567891, this.shortint.plus(this.longint).getInt());
		assertEquals(0, this.longint.plus(this.negint).getInt());
	}

	/**
	 * Verifies that signum returns 1 for CommonIntObjects with a positive value, -1 for negatives, 0 otherwise.
	 */
	@Test
	public void testSignum() {
		assertEquals(0, this.zeroint.signum());
		assertEquals(1, this.longint.signum());
		assertEquals(-1, this.negint.signum());
	}

	/**
	 * Verifies that isZero returns true for CommonIntObjects with a value of 0, and false otherwise
	 */
	@Test
	public void testIsZero() {
		assertTrue(this.zeroint.isZero());
		assertFalse(this.longint.isZero());
		assertFalse(this.negint.isZero());
	}

	/**
	 * Verifies that isPositive returns true for CommonIntObjects with a positive value, and false otherwise
	 */
	@Test
	public void testIsPositive() {
		assertTrue(this.longint.isPositive());
		assertFalse(this.zeroint.isPositive());
		assertFalse(this.negint.isPositive());
	}

	/**
	 * Verifies that isNegative returns true for CommonIntObjects with a negative value, and false otherwise
	 */
	@Test
	public void testIsNegative() {
		assertTrue(this.negint.isNegative());
		assertFalse(this.zeroint.isNegative());
		assertFalse(this.longint.isNegative());
	}

	/**
	 * Verifies that isOne returns true for CommonIntObjects with a value of 1, and false otherwise
	 */
	@Test
	public void testIsOne() {
		assertTrue(this.shortint.isOne());
		assertFalse(this.zeroint.isOne());
		assertFalse(this.negint.isOne());
	}

	/**
	 * Verifies that compareTo returns 0 if two CommonIntObjects have the same value, and another value otherwise
	 */
	@Test
	public void testCompareTo() {
		assertEquals(0, this.shortint.compareTo(this.shortint));
		assertTrue(0>this.shortint.compareTo(this.longint));
		assertTrue(0<this.shortint.compareTo(this.negint));
	}

	/**
	 * Verifies that toStringBuffer returns the CommonIntObject's value as a string buffer
	 */
	@Test
	public void testToStringBuffer() {
		assertEquals("1", this.shortint.toStringBuffer(false).toString());
		assertEquals("-1234567890", this.negint.toStringBuffer(false).toString());
	}

	/**
	 * Verifies that toStringBufferLong returns the CommonIntObject's value as a string buffer
	 */
	@Test
	public void testToStringBufferLong() {
		assertEquals("1", this.shortint.toStringBufferLong().toString());
		assertEquals("-1234567890", this.negint.toStringBufferLong().toString());
	}

}
