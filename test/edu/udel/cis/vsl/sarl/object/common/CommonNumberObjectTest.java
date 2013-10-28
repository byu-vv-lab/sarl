package edu.udel.cis.vsl.sarl.object.common;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.number.real.RealInteger;
import edu.udel.cis.vsl.sarl.number.real.RealNumberFactory;
import edu.udel.cis.vsl.sarl.number.real.RealRational;

/**
 * Test class for CommonNumberObject
 * @author jtirrell
 *
 */
public class CommonNumberObjectTest {

	/**
	 * Initialized to 12345678901234567890 upon setUp
	 */
	CommonNumberObject realint;
	/**
	 * Initialized to 12345678901234567890 upon setUp
	 */
	CommonNumberObject newrealint;
	/**
	 * Initialized to 1/2 upon setUp
	 */
	CommonNumberObject realrational;
	/**
	 * Initialized to 1/2 upon setUp
	 */
	CommonNumberObject newrealrational;
	/**
	 * Initialized to 0 upon setUp
	 */
	CommonNumberObject zero;
	/**
	 * Initialized to 1 upon setUp
	 */
	CommonNumberObject one;
	/**
	 * Initialized to -123 upon setUp
	 */
	CommonNumberObject negint;
	/**
	 * Initialized to a RealNumberFactory upon setUp
	 */
	NumberFactory realfactory;
	
	/**
	 * Initializes realint, newrealint, realreational, newrealrational, zero, one, negint, realfactory
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		//must create RealIntegers through factory for hash codes to be equal
		//should make RealInteger constructor private, or implement hash code.
		this.realint =    new CommonNumberObject(new RealInteger(new BigInteger("12345678901234567890")));
		this.newrealint = new CommonNumberObject(new RealInteger(new BigInteger("12345678901234567890")));
		this.realrational = new CommonNumberObject(new RealRational(new BigInteger("1"), new BigInteger("2")));
		this.newrealrational = new CommonNumberObject(new RealRational(new BigInteger("1"), new BigInteger("2")));
		this.zero = new CommonNumberObject(new RealInteger(BigInteger.ZERO));
		this.one = new CommonNumberObject(new RealInteger(BigInteger.ONE));
		this.negint = new CommonNumberObject(new RealInteger(new BigInteger("-123")));
		this.realfactory = new RealNumberFactory();
	}

//	@Test
//	public void testComputeHashCode() {
//		RationalNumber rat1 = this.realfactory.rational(new BigInteger("1"), new BigInteger("2"));
//		RationalNumber rat2 = this.realfactory.rational(new BigInteger("1"), new BigInteger("2"));
//		assertEquals(rat1., rat2.toString());
//	}

//	@Test
//	public void testIntrinsicEquals() {
//		assertTrue(this.realint.intrinsicEquals(this.newrealint));
//	}

	/**
	 * Verifies that toString returns the correct string representation of CommonNumberObjects
	 */
	@Test
	public void testToString() {
		assertEquals("12345678901234567890", this.realint.toString());
		assertEquals("1/2", this.realrational.toString());
	}

	/**
	 * Verifies that getNumber returns the correct Number representation of CommonNumberObjects
	 */
	@Test
	public void testGetNumber() {
		assertEquals("12345678901234567890", this.realint.getNumber().toString());
		assertEquals("1/2", this.realrational.getNumber().toString());
	}

	/**
	 * Verifies that signum returns 1 for CommonIntObjects with a positive value, -1 for negatives, 0 otherwise.
	 */
	@Test
	public void testSignum() {
		assertEquals(-1, this.negint.signum());
		assertEquals(0, this.zero.signum());
		assertEquals(1, this.realrational.signum());
	}

	/**
	 * Verifies that isZero returns true for CommonNumberObjects with a value of 0, and false otherwise
	 */
	@Test
	public void testIsZero() {
		assertTrue(this.zero.isZero());
		assertFalse(this.realint.isZero());
	}

	/**
	 * Verifies that isOne returns true for CommonNumberObjects with a value of 1, and false otherwise
	 */
	@Test
	public void testIsOne() {
		assertTrue(this.one.isOne());
		assertFalse(this.zero.isOne());
	}

	/**
	 * Verifies that isInteger returns true if the CommonNumberObject represents an integer, false otherwise
	 */
	@Test
	public void testIsInteger() {
		assertTrue(this.realint.isInteger());
		assertFalse(this.realrational.isInteger());
	}

	/**
	 * Verifies that isReal returns true if the CommonNumberObject represents a real(non-integer) number,
	 * false otherwise
	 */
	@Test
	public void testIsReal() {
		assertFalse(this.realint.isReal());
		assertTrue(this.realrational.isReal());
	}
	
	/**
	 * Verifies that compareTo returns 0 for equivalent CommonNumberObjects, <0 if the first is less than the 
	 * second, and >0 if the first is greater than the second.
	 */
	@Test
	public void testCompareTo() {
		assertEquals(0, this.realint.compareTo(this.newrealint));
		assertTrue(0<this.realint.compareTo(this.zero));
		assertTrue(0>this.zero.compareTo(this.realint));
		assertFalse(0<this.zero.compareTo(this.realint));
	}

	/**
	 * Verifies that toStringBuffer returns the CommonNumberObject's value as a string buffer
	 */
	@Test
	public void testToStringBuffer() {
		assertEquals("12345678901234567890", this.realint.toStringBuffer(false).toString());
		assertEquals("1/2", this.realrational.toStringBuffer(false).toString());
	}

	/**
	 * Verifies that toStringBufferLong returns the CommonNumberObject's value as a string buffer
	 */
	@Test
	public void testToStringBufferLong() {
		assertEquals("12345678901234567890", this.realint.toStringBufferLong().toString());
		assertEquals("1/2", this.realrational.toStringBufferLong().toString());
	}

}
