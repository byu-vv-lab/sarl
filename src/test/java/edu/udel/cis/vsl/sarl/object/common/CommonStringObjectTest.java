package edu.udel.cis.vsl.sarl.object.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CommonStringObject
 * @author jtirrell
 *
 */
public class CommonStringObjectTest {

	/**
	 * CommonStringObject used for testing, instantiated in setUp
	 */
	CommonStringObject str1;
	/**
	 * CommonStringObject used for testing, instantiates in setUp
	 */
	CommonStringObject str2;
	
	/**
	 * Instantiates this.str1, this.str2
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.str1 = new CommonStringObject("TheString");
		this.str2 = new CommonStringObject("AnotherString");
	}

	/**
	 * Verifies that computeHashCode computes the same for two CommonStringObjects of the same value
	 */
	@Test
	public void testComputeHashCode() {
		CommonStringObject str3 = new CommonStringObject("TheString");
		assertEquals(this.str1.computeHashCode(), str3.computeHashCode());
	}

	/**
	 * Verifies that running intrinsicEquals on CommonStringObjects of the same value returns true 
	 */
	@Test
	public void testIntrinsicEquals() {
		CommonStringObject str3 = new CommonStringObject("TheString");
		assertTrue(this.str1.intrinsicEquals(str3));
		assertFalse(this.str1.intrinsicEquals(this.str2));
	}

	/**
	 * Verifies that toString returns the string representation of the CommonStringObject
	 */
	@Test
	public void testToString() {
		assertEquals("TheString", this.str1.toString());
	}

	/**
	 * Verifies that getString returns the string value of the CommonStringObject
	 */
	@Test
	public void testGetString() {
		assertEquals("TheString", this.str1.getString());
	}

	/**
	 * Verifies that comparing CommonStringObjects of the same values with compareTo returns 0, < or > otherwise
	 */
	@Test
	public void testCompareTo() {
		assertTrue(0<this.str1.compareTo(this.str2));
		assertTrue(0>this.str2.compareTo(this.str1));
		assertEquals(0, this.str1.compareTo(this.str1));
	}

	/**
	 * Verifies that toStringBuffer returns a string buffer containing the CommonStringObject's value
	 */
	@Test
	public void testToStringBuffer() {
		assertEquals("TheString", this.str1.toStringBuffer(false).toString());
	}

	/**
	 * Verifies that toStringBufferLong returns a string buffer containing the CommonStringObject's value
	 */
	@Test
	public void testToStringBufferLong() {
		assertEquals("TheString", this.str1.toStringBufferLong().toString());
	}

}
