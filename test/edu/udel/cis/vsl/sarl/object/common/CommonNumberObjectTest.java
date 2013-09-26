package edu.udel.cis.vsl.sarl.object.common;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.number.real.RealInteger;
import edu.udel.cis.vsl.sarl.number.real.RealRational;

public class CommonNumberObjectTest {

	CommonNumberObject realint;
	CommonNumberObject newrealint;
	CommonNumberObject realrational;
	CommonNumberObject newrealrational;
	CommonNumberObject zero;
	CommonNumberObject one;
	CommonNumberObject negint;
	
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
	}

	@Test
	public void testComputeHashCode() {
		//System.out.println(this.realint.computeHashCode());
		//System.out.println(this.newrealint.computeHashCode());
		//assertEquals(this.realint.computeHashCode(), this.newrealint.computeHashCode());
		//assertEquals(this.realrational.computeHashCode(), this.newrealrational.computeHashCode());
	}

	@Test
	public void testIntrinsicEquals() {
		//System.out.println(this.newrealint.getNumber());
		//System.out.println(this.realint.getNumber());
		//System.out.println(this.realint.getNumber() == this.newrealint.getNumber());
		//assertTrue(this.realint.intrinsicEquals(this.newrealint));
		//assertTrue(this.realrational.intrinsicEquals(this.newrealrational));
	}

	@Test
	public void testToString() {
		assertEquals("12345678901234567890", this.realint.toString());
		assertEquals("1/2", this.realrational.toString());
	}

	@Test
	public void testGetNumber() {
		assertEquals("12345678901234567890", this.realint.getNumber().toString());
		assertEquals("1/2", this.realrational.getNumber().toString());
	}

	@Test
	public void testSignum() {
		assertEquals(-1, this.negint.signum());
		assertEquals(0, this.zero.signum());
		assertEquals(1, this.realrational.signum());
	}

	@Test
	public void testIsZero() {
		assertTrue(this.zero.isZero());
		assertFalse(this.realint.isZero());
	}

	@Test
	public void testIsOne() {
		assertTrue(this.one.isOne());
		assertFalse(this.zero.isOne());
	}

	@Test
	public void testIsInteger() {
		assertTrue(this.realint.isInteger());
		assertFalse(this.realrational.isInteger());
	}

	@Test
	public void testIsReal() {
		assertFalse(this.realint.isReal());
		assertTrue(this.realrational.isReal());
	}

	@Test
	public void testCompareTo() {
		assertTrue(0==this.realint.compareTo(this.newrealint));
		assertTrue(0<this.realint.compareTo(this.zero));
		assertTrue(0>this.zero.compareTo(this.realint));
		assertFalse(0<this.zero.compareTo(this.realint));
	}

	@Test
	public void testToStringBuffer() {
		assertEquals("(12345678901234567890)", this.realint.toStringBuffer(true).toString());
		assertEquals("(1/2)", this.realrational.toStringBuffer(true).toString());
		assertEquals("12345678901234567890", this.realint.toStringBuffer(false).toString());
		assertEquals("1/2", this.realrational.toStringBuffer(false).toString());
	}

	@Test
	public void testToStringBufferLong() {
		assertEquals("12345678901234567890", this.realint.toStringBufferLong().toString());
		assertEquals("1/2", this.realrational.toStringBufferLong().toString());
	}

}
