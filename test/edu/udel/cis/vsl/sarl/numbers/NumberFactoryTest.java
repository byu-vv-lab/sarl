package edu.udel.cis.vsl.sarl.numbers;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.number.Numbers;

public class NumberFactoryTest {

	private static PrintStream out = System.out;

	private static NumberFactory factory = Numbers.REAL_FACTORY;

	private static BigInteger bigOne = BigInteger.ONE;

	private static BigInteger bigThirty = new BigInteger("30");

	private static BigInteger bigThirtyOne = new BigInteger("31");

	private static BigInteger bigTen = new BigInteger("10");

	private static BigInteger bigFifteen = new BigInteger("15");

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void addRat() {
		RationalNumber a = factory.rational(bigThirty, bigThirtyOne);
		RationalNumber b = factory.rational(bigTen, bigFifteen);
		RationalNumber c = factory.add(a, b);
		RationalNumber expected = factory.rational(new BigInteger("152"),
				new BigInteger("93"));

		out.println(c);
		assertEquals(expected, c);
	}

	@Test
	public void decimalString() {
		RationalNumber a = factory.rational(".1");
		RationalNumber b = factory.rational(bigOne, bigTen);

		assertEquals(b, a);
	}

	@Test(expected=ArithmeticException.class)
	public void divideBy0() {
		factory.rational(bigOne, BigInteger.ZERO);
	}

}
