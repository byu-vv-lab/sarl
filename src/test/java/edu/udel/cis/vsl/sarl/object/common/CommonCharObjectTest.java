package edu.udel.cis.vsl.sarl.object.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CommonCharObject
 * @author jtirrell
 *
 */
public class CommonCharObjectTest {
	
	/**
	 * A char object containing 'A', instantiated during setUp
	 */
	CommonCharObject charA;
	/**
	 * a char object containing '1', instantiated during setUp
	 */
	CommonCharObject char1;

	/**
	 * Instantiates this.charA and this.char1
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.charA = new CommonCharObject('A');
		this.char1 = new CommonCharObject('1');
	}

	/**
	 * Verifies that computeHashCode returns the same hash when called on CommonCharObjects with the same value
	 */
	@Test
	public void testComputeHashCode() {
		CommonCharObject newcharA = new CommonCharObject('A');
		CommonCharObject newchar1 = new CommonCharObject('1');
		
		assertEquals(this.charA.computeHashCode(), newcharA.computeHashCode());
		assertEquals(this.char1.computeHashCode(), newchar1.computeHashCode());
	}

	/**
	 * Verifies that intrinisicEquals returns true for CommonCharObjects of the same value
	 * and false for those of different values
	 */
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

	/**
	 * Verifies that the toString() method returns a string representation of CommonCharObjects
	 */
	@Test
	public void testToString() {
		assertEquals("A", this.charA.toString());
		assertEquals("1", this.char1.toString());
	}

	/**
	 * Verifies that getChar() returns the character that the ComonCharObject represents
	 */
	@Test
	public void testGetChar() {
		assertEquals('A', this.charA.getChar());
		assertEquals('1', this.char1.getChar());
	}

	/**
	 * Verifies that compareTo returns 0 on CommonCharObjects of the same value, and a value > or < 0 otherwise 
	 */
	@Test
	public void testCompareTo() {
		CommonCharObject newcharA = new CommonCharObject('A');
		CommonCharObject newchar1 = new CommonCharObject('1');
		
		assertEquals(0, this.charA.compareTo(newcharA));
		assertTrue(0 < this.charA.compareTo(newchar1));
		assertTrue(0 > this.char1.compareTo(newcharA));
		assertEquals(0, this.char1.compareTo(newchar1));
	}

	/**
	 * Verifies that toStringBuffer returns a string buffer containing the character 
	 * that CommonCharObject represents
	 */
	@Test
	public void testToStringBuffer() {
		assertEquals("A", this.charA.toStringBuffer(false).toString());
		assertEquals("1", this.char1.toStringBuffer(false).toString());
	}

	/**
	 * Verifies that toStringBuffer returns a string buffer containing the character 
	 * that CommonCharObject represents
	 */
	@Test
	public void testToStringBufferLong() {
		assertEquals("A", this.charA.toStringBufferLong().toString());
		assertEquals("1", this.char1.toStringBufferLong().toString());
	}

}
