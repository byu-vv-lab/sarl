package edu.udel.cis.vsl.sarl.numbers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.number.Numbers;

public class RealNumberTest {
	
	private static NumberFactory factory = Numbers.REAL_FACTORY;
	private static BigInteger bigTen = new BigInteger("10");
	private static BigInteger bigFifteen = new BigInteger("15");
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void compareInts() {
		IntegerNumber a = factory.integer(bigFifteen);
		IntegerNumber b = factory.integer(bigTen);
		int result = a.compareTo(b);
		assertEquals(1, result);
	}
	
	@Test
	public void compareIntAndRat() {
		IntegerNumber a = factory.integer(bigFifteen);
		RationalNumber b = factory.rational(bigFifteen, bigTen);
		int result = a.compareTo(b);
		assertEquals(-1, result);
	}
	
	@Test
	public void compareRatAndInt() {
		IntegerNumber b = factory.integer(bigTen);
		RationalNumber a = factory.rational(bigFifteen, bigTen);
		int result = a.compareTo(b);
		assertEquals(1, result);
	}
	
	@Test
	public void compareDiffNumeratorRats() {
		RationalNumber a = factory.rational(bigFifteen, bigTen);
		RationalNumber b = factory.rational(bigTen, bigFifteen);
		int result = a.compareTo(b);
		assertEquals(1, result);
	}
	
	@Test
	public void compareSameRats() {
		RationalNumber a = factory.rational(bigTen, bigTen);
		RationalNumber b = factory.rational(bigTen, bigTen);
		int result = a.compareTo(b);
		assertEquals(0, result);
	}
	
	@Test
	public void compareRationals(){
		RationalNumber a = factory.rational(new BigInteger("3"), new BigInteger("4"));
		RationalNumber b = factory.rational("1");
		
		assertFalse(a.compareTo(b) < 0);
		assertTrue(factory.compare(a, b) < 0);
	}
}
