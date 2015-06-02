package edu.udel.cis.vsl.sarl.numbers;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory.IntervalUnion;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.number.real.RealInteger;
import edu.udel.cis.vsl.sarl.number.real.RealRational;

public class NumberFactoryTest {

	private static NumberFactory factory = Numbers.REAL_FACTORY;

	private static BigInteger bigNegativeThirty = new BigInteger("-30");
	private static BigInteger bigNegativeTen = new BigInteger("-10");
	private static BigInteger bigNegativeThree = new BigInteger("-3");
	private static BigInteger bigNegativeOne = new BigInteger("-1");
	private static BigInteger bigZero = new BigInteger("0");
	private static BigInteger bigOne = BigInteger.ONE;
	private static BigInteger bigTwo = new BigInteger("2");
	private static BigInteger bigThree = new BigInteger("3");
	private static BigInteger bigFive = new BigInteger("5");
	private static BigInteger bigSix = new BigInteger("6");
	private static BigInteger bigEight = new BigInteger("8");
	private static BigInteger bigTen = new BigInteger("10");
	private static BigInteger bigFifteen = new BigInteger("15");
	private static BigInteger bigTwenty = new BigInteger("20");
	private static BigInteger bigThirty = new BigInteger("30");
	private static BigInteger bigThirtyOne = new BigInteger("31");

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Testing the multiply method with two IntegerNumbers.
	 * 
	 */
	@Test
	public void multiplyIntegers() {
		IntegerNumber a = factory.integer(bigThirty);
		IntegerNumber b = factory.integer(bigTen);
		IntegerNumber c = factory.multiply(a, b);
		IntegerNumber expected = factory.integer(new BigInteger("300"));

		// out.println(c);
		assertEquals(expected, c);
	}

	/**
	 * Testing that SARL finds a decimal value equivalent to its fraction form
	 */
	@Test
	public void decimalString() {
		RationalNumber a = factory.rational(".1");
		RationalNumber b = factory.rational(bigOne, bigTen);

		assertEquals(b, a);
	}

	/**
	 * @Exception ArithmeticException is thrown if the denominator (arg1) is
	 *            zero.
	 */
	@Test(expected = ArithmeticException.class)
	public void divideBy0() {
		factory.rational(bigOne, BigInteger.ZERO);
	}

	/**
	 * Testing the add method with two RationalNumbers.
	 */
	@Test
	public void addRat() {
		RationalNumber a = factory.rational(bigThirty, bigThirtyOne);
		RationalNumber b = factory.rational(bigTen, bigFifteen);
		RationalNumber c = factory.add(a, b);
		RationalNumber expected = factory.rational(new BigInteger("152"),
				new BigInteger("93"));

		// out.println(c);
		assertEquals(expected, c);
	}

	/**
	 * Testing the ceiling function ceil to ensure that ceilings are properly
	 * computed for rational numbers that, when simplified, stay in fraction
	 * form, and the case where they are integers.
	 */
	@Test
	public void rationalCeiling() {
		RationalNumber a = factory.rational(bigThirty, bigThirtyOne);
		RationalNumber b = factory.rational(bigTen, bigOne);
		IntegerNumber c = factory.ceil(a);
		IntegerNumber d = factory.ceil(b);
		IntegerNumber expectedC = factory.integer(bigOne);
		IntegerNumber expectedD = factory.integer(bigTen);
		// out.println(c);
		// out.println(d);
		assertEquals(expectedC, c);
		assertEquals(expectedD, d);
	}

	/**
	 * Testing the GCD function (GCD is computed with IntegerNumbers)
	 */
	@Test
	public void GCD() {
		IntegerNumber a = factory.integer(bigThirty);
		IntegerNumber b = factory.integer(bigTwenty);
		IntegerNumber c = factory.gcd(a, b);
		IntegerNumber expected = factory.integer(new BigInteger("10"));

		// out.println(c);
		assertEquals(expected, c);
	}

	/**
	 * Testing the LCM function (LCM is computed with IntegerNumbers)
	 */
	@Test
	public void LCM() {
		IntegerNumber a = factory.integer(bigThirty);
		IntegerNumber b = factory.integer(bigThirtyOne);
		IntegerNumber c = factory.lcm(a, b);
		IntegerNumber expected = factory.integer(new BigInteger("930"));

		// out.println(c);
		assertEquals(expected, c);
	}

	/**
	 * Testing the subtract method for two IntegerNumbers.
	 */
	@Test
	public void subInteger() {
		IntegerNumber a = factory.integer(bigThirty);
		IntegerNumber b = factory.integer(bigTen);
		IntegerNumber c = factory.subtract(a, b);
		IntegerNumber expected = factory.integer(new BigInteger("20"));

		// out.println(c);
		assertEquals(expected, c);
	}

	/**
	 * Testing the decrement function. This is covering the case of an
	 * IntegerNumber argument here, subtracting one and ensuring that it is
	 * computed correctly).
	 */
	@Test
	public void IntegerNumberDecrement() {
		IntegerNumber a = factory.integer(bigThirty);
		IntegerNumber c = factory.decrement(a);
		IntegerNumber expected = factory.integer(new BigInteger("29"));

		// out.println(c);
		assertEquals(expected, c);
	}

	/**
	 * Testing the increment method. This is covering the case of an
	 * IntegerNumber argument here, adding one and ensuring that is is computed
	 * correctly).
	 */
	@Test
	public void IntegerNumberIncrement() {
		IntegerNumber a = factory.integer(bigThirty);
		IntegerNumber c = factory.increment(a);
		IntegerNumber expected = factory.integer(new BigInteger("31"));

		// out.println(c);
		assertEquals(expected, c);
	}

	/**
	 * Testing the subtract method for two RationalNumbers.
	 */
	@Test
	public void subRat() {
		RationalNumber a = factory.rational(bigFive, bigTwo);
		RationalNumber b = factory.rational(bigTen, bigFifteen);
		RationalNumber c = factory.subtract(a, b);
		RationalNumber expected = factory.rational(new BigInteger("11"),
				new BigInteger("6"));

		// out.println(c);
		assertEquals(expected, c);
	}

	/**
	 * Testing integer method (converting a long to a RealInteger).
	 */
	@Test
	public void longInt() {
		long a = 30;
		RealInteger b = (RealInteger) factory.integer(a);
		RealInteger expectedB = (RealInteger) factory.integer(bigThirty);
		assertEquals(expectedB, b);

	}

	/**
	 * Testing that a positive numerator divided by a negative denominator is
	 * equivalent to a negative numerator divided by a positive denominator.
	 */
	@Test
	public void realRatRat() {
		RealRational a = (RealRational) factory.rational(bigThirty,
				bigNegativeOne);
		RealRational expectedA = (RealRational) factory.rational(
				bigNegativeThirty, bigOne);
		assertEquals(expectedA, a);
	}

	/**
	 * Ensuring that a RationalNumber with a denominator of one is considered
	 * integral.
	 */
	@Test
	public void realRatIsIntegral() {
		RealRational a = (RealRational) factory.rational(bigThirty, bigOne);
		boolean expected = true;
		boolean actual = factory.isIntegral(a);
		assertEquals(expected, actual);
	}

	/**
	 * Testing that the ceiling method works correctly for a negative
	 * RationalNumber
	 */
	@Test
	public void ceilingRatNumNegNum() {
		RationalNumber a = factory.rational(bigNegativeThree, bigOne);
		IntegerNumber b = factory.ceil(a);
		IntegerNumber expectedB = factory.integer(bigNegativeThree);
		assertEquals(expectedB, b);

	}

	/**
	 * Testing that the compare method works correctly with an input of two
	 * RationalNumber arguments.
	 */
	@Test
	public void ratNumCompare() {
		RationalNumber a = factory.rational(bigThirty, bigTen);
		RationalNumber b = factory.rational(bigTwenty, bigTen);
		int c = factory.compare(a, b);
		int expectedC = 1;
		assertEquals(expectedC, c);
	}

	/**
	 * Testing that the compare method works correctly with an input of two
	 * IntegerNumber arguments.
	 */
	@Test
	public void intNumCompare() {
		IntegerNumber a = factory.integer(bigThirty);
		IntegerNumber b = factory.integer(bigTwenty);
		int c = factory.compare(a, b);
		int expectedC = 1;
		assertEquals(expectedC, c);
	}

	/**
	 * Testing that the compare method works correctly with an input of two
	 * Number arguments. Covering cases of Number being a Number,
	 * RationalNumber, or Integer Number
	 */
	@Test
	public void numberCompare() {
		Number a = factory.number("20");
		Number b = factory.number("10");
		int c = factory.compare(a, b);
		int expectedC = 1;
		assertEquals(expectedC, c);
		Number d = factory.rational(bigTwenty, bigTen);
		Number e = factory.rational(bigTen, bigThree);
		int f = factory.compare(d, e);
		int expectedF = -1;
		assertEquals(expectedF, f);
		Number g = factory.integer(bigThirty);
		Number h = factory.integer(bigTen);
		int i = factory.compare(g, h);
		int expectedI = 1;
		assertEquals(expectedI, i);
	}

	/**
	 * Testing that the denominator method works correctly (returning the
	 * denominator of the input RationalNumber).
	 */
	@Test
	public void ratNumDenominator() {
		RationalNumber a = factory.rational(bigTen, bigThree);
		IntegerNumber b = factory.denominator(a);
		IntegerNumber expectedB = factory.integer(bigThree);
		assertEquals(expectedB, b);
	}

	/**
	 * Testing that the divide method works correctly with an input of two
	 * RationalNumber arguments.
	 */
	@Test
	public void ratNumDivide() {
		RationalNumber a = factory.rational(bigTen, bigThree);
		RationalNumber b = factory.rational(bigTwo, bigOne);
		RationalNumber c = factory.divide(a, b);
		RationalNumber expectedC = factory.rational(bigFive, bigThree);
		assertEquals(expectedC, c);
	}

	/**
	 * @Exception IllegalArgumentException is thrown if taking the modulus of
	 *            two IntegerNumber inputs where there is a negative
	 *            IntegerNumber as an argument.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void intNumModNegArg() {
		IntegerNumber a = factory.integer(bigTen);
		IntegerNumber b = factory.integer(bigNegativeOne);
		factory.mod(a, b);
	}

	/**
	 * Testing that the modulus method works correctly with an input of two
	 * IntegerNumber arguments.
	 */
	@Test
	public void intNumMod() {
		IntegerNumber a = factory.integer(bigTen);
		IntegerNumber b = factory.integer(bigThree);
		IntegerNumber c = factory.mod(a, b);
		IntegerNumber expectedC = factory.integer(bigOne);
		assertEquals(expectedC, c);
	}

	/**
	 * Testing that the floor method works correctly with an input of a
	 * RationalNumber argument. This covers the cases of both positive and
	 * negative RationalNumbers, as well as having a negative RationalNumber
	 * that is integral and not integral.
	 */
	@Test
	public void ratNumFloor() {
		RationalNumber a = factory.rational(bigTwenty, bigThree);
		IntegerNumber expectedB = factory.integer(bigSix);
		IntegerNumber b = factory.floor(a);
		assertEquals(expectedB, b);
		RationalNumber c = factory.rational(bigNegativeOne, bigThree);
		IntegerNumber expectedD = factory.integer(bigNegativeOne);
		IntegerNumber d = factory.floor(c);
		assertEquals(expectedD, d);
		RationalNumber e = factory.rational(bigNegativeTen, bigOne);
		IntegerNumber expectedF = factory.integer(bigNegativeTen);
		IntegerNumber f = factory.floor(e);
		assertEquals(expectedF, f);
	}

	/**
	 * Testing that the integer method works correctly with an input of a
	 * String.
	 */
	@Test
	public void intInt() {
		String a = "30";
		IntegerNumber b = factory.integer(a);
		IntegerNumber expectedB = factory.integer(bigThirty);
		assertEquals(expectedB, b);
	}

	/**
	 * Testing that the integerToRatioal method works correctly (taking an input
	 * argument of an IntegerNumber, and returning a RationalNumber).
	 */
	@Test
	public void intNumIntToRat() {
		IntegerNumber a = factory.integer(bigThirty);
		RationalNumber expectedB = factory.rational(bigThirty, bigOne);
		RationalNumber b = factory.integerToRational(a);
		assertEquals(expectedB, b);

	}

	/**
	 * @Exception ArithmeticException is thrown if trying to represent the
	 *            integer value of a RationalNumber if the RationalNumber
	 *            argument is not integral.
	 */
	@Test(expected = ArithmeticException.class)
	public void ratNumIntValueNotIntegral() {
		RationalNumber a = factory.rational(bigTen, bigThree);
		factory.integerValue(a);
	}

	/**
	 * Testing the integerValue method (with proper input of a RationalNumber
	 * argument where the RationalNumber is integral).
	 */
	@Test
	public void ratNumIntValue() {
		RationalNumber a = factory.rational(bigTen, bigOne);
		IntegerNumber expectedB = factory.integer(bigTen);
		IntegerNumber b = factory.integerValue(a);
		assertEquals(expectedB, b);
	}

	/**
	 * Testing the multiply method, taking an input of two RationalNumber
	 * arguments.
	 */
	@Test
	public void ratNumMultiply() {
		RationalNumber a = factory.rational(bigTen, bigOne);
		RationalNumber b = factory.rational(bigTwo, bigOne);
		RationalNumber expectedC = factory.rational(bigTwenty, bigOne);
		RationalNumber c = factory.multiply(a, b);
		assertEquals(expectedC, c);
	}

	/**
	 * Testing the negate method for an input of an IntegerNumber argument.
	 */
	@Test
	public void intNegate() {
		IntegerNumber a = factory.integer(bigOne);
		IntegerNumber expectedB = factory.integer(bigNegativeOne);
		IntegerNumber b = factory.negate(a);
		assertEquals(expectedB, b);
	}

	/**
	 * Testing the number method (covering the cases where an IntegerNumber
	 * String and a RationalNumber String are getting converted to Numbers.
	 */
	@Test
	public void stringToIntOrRat() {
		String a = "30";
		String b = "0.1";
		Number expectedC = factory.integer(bigThirty);
		Number c = factory.number(a);
		assertEquals(expectedC, c);
		Number expectedD = factory.rational(bigOne, bigTen);
		Number d = factory.number(b);
		assertEquals(expectedD, d);
	}

	/**
	 * Testing that the numerator method works correctly (taking an input of a
	 * RationalNumber argument, converting the fraction into lowest terms if
	 * necessary, and then returning the numerator).
	 */
	@Test
	public void ratNumNumerator() {
		RationalNumber a = factory.rational(bigTen, bigThirty);
		IntegerNumber expectedB = factory.integer(bigOne);
		IntegerNumber b = factory.numerator(a);
		assertEquals(expectedB, b);
	}

	/**
	 * Testing the oneInteger method, that it actually returns the IntegerNumber
	 * equivalent of 1.
	 */
	@Test
	public void testOneIntMethod() {
		IntegerNumber a = factory.oneInteger();
		IntegerNumber b = factory.integer(bigOne);
		assertEquals(b, a);
	}

	/**
	 * Testing the oneRational method, that it actually returns the
	 * RationalNumber equivalent of 1 (1/1).
	 */
	@Test
	public void testOneRatMethod() {
		RationalNumber a = factory.oneRational();
		RationalNumber b = factory.rational(bigOne, bigOne);
		assertEquals(b, a);
	}

	/**
	 * Testing the rational method where a RationalNumber is taken as an
	 * argument and a RationalNumber is returned.
	 */
	@Test
	public void numberInstOfRationalToRational() {
		Number a = factory.rational(bigThirty, bigOne);
		Number expectedC = factory.rational(bigThirty, bigOne);
		Number c = factory.rational(a);
		assertEquals(expectedC, c);
	}

	/**
	 * Testing the rational method where an IntegerNumber is taken in as an
	 * argument and a RationalNumber is returned.
	 */
	@Test
	public void numberInstOfIntNumtoRational() {
		Number a = (factory.integer(bigThirty));
		Number b = factory.rational(a);
		Number expectedB = (RealRational) factory.rational(bigThirty, bigOne);
		assertEquals(expectedB, b);
	}

	/**
	 * @Exception IllegalArgumentException is thrown in the number method when
	 *            an argument is passed that isn't compatible with the method.
	 *            (In this case, a String is getting passed, but not in the
	 *            correct form of a Number).
	 *
	 */
	@Test(expected = IllegalArgumentException.class)
	public void numberToRationalWrongArg() {
		Number a = factory.number("bigThree");
		factory.rational(a);
	}

	/**
	 * Testing the zeroInteger method, that it actually returns the
	 * IntegerNumber equivalent of 0.
	 */
	@Test
	public void testZeroIntMethod() {
		IntegerNumber a = factory.zeroInteger();
		IntegerNumber b = factory.integer(bigZero);
		assertEquals(b, a);
	}

	/**
	 * Testing the zeroRational method, that it actually returns the
	 * RationalNumber equivalent of 0 (0/1).
	 */
	@Test
	public void testZeroRatMethod() {
		RationalNumber a = factory.zeroRational();
		RationalNumber b = factory.rational(bigZero, bigOne);
		assertEquals(b, a);

	}

	/**
	 * Testing the abs method, with an input of a Number argument that is a
	 * positive number.
	 */
	@Test
	public void positiveNumberAbs() {
		Number a = factory.number("30");
		Number expectedB = factory.number("30");
		Number b = factory.abs(a);
		assertEquals(expectedB, b);
	}

	/**
	 * Testing the abs method, with an input of a Number argument that is a
	 * negative number.
	 */
	@Test
	public void negativeNumberAbs() {
		Number a = factory.number("-30");
		Number expectedB = factory.number("30");
		Number b = factory.abs(a);
		assertEquals(expectedB, b);
	}

	/**
	 * Testing the integer function, with an input of an int (ensuring that is
	 * is properly converted to an IntegerNumber).
	 */
	@Test
	public void intInteger() {
		int a = 30;
		IntegerNumber expectedB = factory.integer(bigThirty);
		IntegerNumber b = factory.integer(a);
		assertEquals(expectedB, b);
	}

	/**
	 * Testing the negate method for a Number argument that is an IntegerNumber.
	 */
	@Test
	public void numberNegateIntNum() {
		Number a = factory.number("30");
		IntegerNumber expectedB = (IntegerNumber) factory.number("-30");
		IntegerNumber b = (IntegerNumber) factory.negate(a);
		assertEquals(expectedB, b);
	}

	/**
	 * Testing the negate method for an input of a String argument, covering the
	 * cases of an integer and a rational number.
	 */
	@Test
	public void numberNegateRatNum() {
		Number a = factory.number("30");
		Number expectedB = factory.number("-30");
		Number b = factory.negate(a);
		assertEquals(expectedB, b);
		Number c = factory.number("30.1");
		Number expectedD = factory.number("-30.1");
		Number d = factory.negate(c);
		assertEquals(expectedD, d);
	}

	/**
	 * Testing the increment method for an input of a Number.
	 */
	@Test
	public void numIncrement() {
		Number a = factory.integer(bigThirty);
		Number expectedB = factory.integer(bigThirtyOne);
		Number b = factory.increment(a);
		assertEquals(expectedB, b);
	}

	/**
	 * Testing the increment method for an input of a RationalNumber.
	 */
	@Test
	public void ratNumIncrement() {
		RationalNumber a = factory.rational(bigFive, bigThree);
		RationalNumber expectedB = factory.rational(bigEight, bigThree);
		RationalNumber b = factory.increment(a);
		assertEquals(expectedB, b);
	}

	/**
	 * Testing the increment method for an input of a Number argument that is a
	 * RationalNumber.
	 */
	@Test
	public void numIncrementRatArg() {
		Number a = factory.rational(bigFive, bigThree);
		Number expectedB = factory.rational(bigEight, bigThree);
		Number b = factory.increment(a);
		assertEquals(expectedB, b);
	}

	/**
	 * Testing the decrement method with an input of a RationalNumber.
	 */
	@Test
	public void ratNumDecrement() {
		RationalNumber a = factory.rational(bigThirtyOne, bigOne);
		RationalNumber expectedB = factory.rational(bigThirty, bigOne);
		RationalNumber b = factory.decrement(a);
		assertEquals(expectedB, b);
	}

	/**
	 * Testing the decrement method with an input of a Number.
	 */
	@Test
	public void numDecrement() {
		Number a = factory.number("31");
		Number expectedB = factory.number("30");
		Number b = factory.decrement(a);
		assertEquals(expectedB, b);
	}

	/**
	 * Testing the decrement method with an input of a Number that is a
	 * RationalNumber.
	 */
	@Test
	public void numDecrementRatArg() {
		Number a = factory.rational(bigEight, bigThree);
		Number expectedB = factory.rational(bigFive, bigThree);
		Number b = factory.decrement(a);
		assertEquals(expectedB, b);
	}

	/**
	 * Testing the add method with an input of a Number, covering cases of
	 * adding both integer numbers and rational numbers. This method is only
	 * intended to work with both arguments being the same type of number.
	 */
	@Test
	public void numberAddition() {
		Number a = factory.number("10");
		Number b = factory.number("20");
		Number c = factory.number("1.1");
		Number d = factory.number("2.3");
		Number expectedE = factory.number("30");
		Number e = factory.add(a, b);
		assertEquals(expectedE, e);
		Number expectedF = factory.number("3.4");
		Number f = factory.add(c, d);
		assertEquals(expectedF, f);
	}

	/**
	 * @Exception IllegalArgumentException is thrown for the add method when
	 *            trying to add two numbers together that are not both the same
	 *            type of number (rational number or integer number).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void numberAdditionInvalArgs() {
		Number a = factory.number("10");
		Number b = factory.number("10.4");
		factory.add(a, b);
	}

	/**
	 * @Exception IllegalArgumentException is thrown for the add method when
	 *            trying to add two numbers together that are not both the same
	 *            type of number (rational number or integer number). This test
	 *            ensures that the throwing of the exception does not rely on
	 *            the order of the incompatible number arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void numberAdditionInvalArgsTwo() {
		Number a = factory.number("10.4");
		Number b = factory.number("10");
		factory.add(a, b);
	}

	/**
	 * Testing the subtract method with an input of a Number, covering cases of
	 * subtracting both integer numbers and rational numbers. This method is
	 * only intended to work with both arguments being the same type of number.
	 */
	@Test
	public void numberSubtraction() {
		Number a = factory.number("20");
		Number b = factory.number("10");
		Number c = factory.number("2.3");
		Number d = factory.number("1.1");
		Number expectedE = factory.number("10");
		Number e = factory.subtract(a, b);
		assertEquals(expectedE, e);
		Number expectedF = factory.number("1.2");
		Number f = factory.subtract(c, d);
		assertEquals(expectedF, f);
	}

	/**
	 * @Exception IllegalArgumentException is thrown for the subtract method
	 *            when trying to subtract two numbers together that are not both
	 *            the same type of number (rational number or integer number).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void numberSubtractionInvalArgs() {
		Number a = factory.number("10");
		Number b = factory.number("10.4");
		factory.subtract(a, b);
	}

	/**
	 * @Exception IllegalArgumentException is thrown for the subtract method
	 *            when trying to subtract two numbers together that are not both
	 *            the same type of number (rational number or integer number).
	 *            This test ensures that the throwing of the exception does not
	 *            rely on the order of the incompatible number arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void numberSubtractionInvalArgsTwo() {
		Number a = factory.number("10.4");
		Number b = factory.number("10");
		factory.subtract(a, b);
	}

	/**
	 * Testing the multiply method with an input of two Number arguments. This
	 * covers the cases of the arguments being integer numbers and rational
	 * numbers.
	 */
	@Test
	public void numberMultiply() {
		Number a = factory.number("2");
		Number b = factory.number("3");
		Number c = factory.number("1.1");
		Number d = factory.number("2.1");
		Number expectedE = factory.number("6");
		Number e = factory.multiply(a, b);
		assertEquals(expectedE, e);
		Number expectedF = factory.number("2.31");
		Number f = factory.multiply(c, d);
		assertEquals(expectedF, f);
	}

	/**
	 * @Exception IllegalArgumentException is thrown for the multiply method
	 *            when trying to multiply two numbers together that are not both
	 *            the same type of number (rational number or integer number).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void numberMultiplyInvalArgs() {
		Number a = factory.number("10");
		Number b = factory.number("10.4");
		factory.multiply(a, b);
	}

	/**
	 * @Exception IllegalArgumentException is thrown for the multiply method
	 *            when trying to multiply two numbers together that are not both
	 *            the same type of number (rational number or integer number).
	 *            This test ensures that the throwing of the exception does not
	 *            rely on the order of the incompatible number arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void numberMultiplyInvalArgsTwo() {
		Number a = factory.number("10.4");
		Number b = factory.number("10");
		factory.multiply(a, b);
	}

	/**
	 * Testing the divide method with an input of two Number arguments. This
	 * covers the cases of the arguments being integer numbers and rational
	 * numbers.
	 */
	@Test
	public void numberDivide() {
		Number a = factory.number("12");
		Number b = factory.number("3");
		Number c = factory.number("6.51");
		Number d = factory.number("2.1");
		Number expectedE = factory.number("4");
		Number e = factory.divide(a, b);
		assertEquals(expectedE, e);
		Number expectedF = factory.number("3.1");
		Number f = factory.divide(c, d);
		assertEquals(expectedF, f);
	}

	/**
	 * @Exception IllegalArgumentException is thrown for the divide method when
	 *            trying to divide two numbers together that are not both the
	 *            same type of number (rational number or integer number).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void numberDivideInvalArgs() {
		Number a = factory.number("10");
		Number b = factory.number("10.4");
		factory.divide(a, b);
	}

	/**
	 * @Exception IllegalArgumentException is thrown for the divide method when
	 *            trying to divide two numbers together that are not both the
	 *            same type of number (rational number or integer number). This
	 *            test ensures that the throwing of the exception does not rely
	 *            on the order of the incompatible number arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void numberDivideInvalArgsTwo() {
		Number a = factory.number("10.4");
		Number b = factory.number("10");
		factory.divide(a, b);
	}

	// TODO: To add comm for Intersection part
	@Test(expected = NullPointerException.class)
	public void intervalIntersectionNullInputI1() {
		IntegerNumber lo2 = factory.integer(-314);
		IntegerNumber up2 = factory.integer(427);
		boolean isIntegral2 = lo2 instanceof RealInteger;
		boolean sl2 = true;
		boolean su2 = true;
		Interval i1 = null;
		Interval i2 = factory.newInterval(isIntegral2, lo2, sl2, up2, su2);

		assert (lo2 instanceof RealInteger) == (up2 instanceof RealInteger);
		factory.intersection(i1, i2);
	}

	@Test(expected = NullPointerException.class)
	public void intervalIntersectionNullInputI2() {
		IntegerNumber lo1 = factory.integer(-314);
		IntegerNumber up1 = factory.integer(427);
		boolean isIntegral1 = lo1 instanceof RealInteger;
		boolean sl1 = true;
		boolean su1 = true;
		Interval i1 = factory.newInterval(isIntegral1, lo1, sl1, up1, su1);
		Interval i2 = null;

		assert (lo1 instanceof RealInteger) == (up1 instanceof RealInteger);
		factory.intersection(i1, i2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void intervalIntersectionInvalInputI1IntI2Rat() {
		IntegerNumber lo1 = factory.integer(1);
		IntegerNumber up1 = factory.integer(1);
		RationalNumber lo2 = factory.rational(lo1);
		RationalNumber up2 = factory.rational(up1);
		boolean isIntegral1 = lo1 instanceof RealInteger;
		boolean isIntegral2 = lo2 instanceof RealInteger;
		boolean sl1 = true;
		boolean su1 = true;
		boolean sl2 = true;
		boolean su2 = true;
		Interval i1 = factory.newInterval(isIntegral1, lo1, sl1, up1, su1);
		Interval i2 = factory.newInterval(isIntegral2, lo2, sl2, up2, su2);

		assert (lo1 instanceof RealInteger) == (up1 instanceof RealInteger);
		assert (lo2 instanceof RealRational) == (up2 instanceof RealRational);

		factory.intersection(i1, i2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void intervalIntersectionInvalInputI1RatI2Int() {
		IntegerNumber lo2 = factory.integer(1);
		IntegerNumber up2 = factory.integer(1);
		RationalNumber lo1 = factory.rational(lo2);
		RationalNumber up1 = factory.rational(up2);
		boolean isIntegral1 = lo1 instanceof RealInteger;
		boolean isIntegral2 = lo2 instanceof RealInteger;
		boolean sl1 = true;
		boolean su1 = true;
		boolean sl2 = true;
		boolean su2 = true;
		Interval i1 = factory.newInterval(isIntegral1, lo1, sl1, up1, su1);
		Interval i2 = factory.newInterval(isIntegral2, lo2, sl2, up2, su2);

		assert (lo1 instanceof RealRational) == (up1 instanceof RealRational);
		assert (lo2 instanceof RealInteger) == (up2 instanceof RealInteger);

		factory.intersection(i1, i2);
	}

	@Test
	public void intervalIntersectionI1DisjointI2Int() {
		// i1: (-infi, -314] i2: [314, +infi) expected: (0, 0)
		IntegerNumber lo1 = null;
		IntegerNumber up1 = factory.integer(-314);
		IntegerNumber lo2 = factory.integer(314);
		IntegerNumber up2 = null;
		IntegerNumber loR = factory.zeroInteger();
		IntegerNumber upR = factory.zeroInteger();
		boolean isIntegral1 = up1 instanceof RealInteger;
		boolean isIntegral2 = lo2 instanceof RealInteger;
		boolean isIntegralR = loR instanceof RealInteger;
		boolean sl1 = true;
		boolean su1 = false;
		boolean sl2 = false;
		boolean su2 = true;
		boolean slR = true;
		boolean suR = true;
		Interval i1 = factory.newInterval(isIntegral1, lo1, sl1, up1, su1);
		Interval i2 = factory.newInterval(isIntegral2, lo2, sl2, up2, su2);
		Interval iR = factory.newInterval(isIntegralR, loR, slR, upR, suR);

		assert (loR instanceof RealInteger) == (upR instanceof RealInteger);

		Interval iRes = factory.intersection(i1, i2);

		assertTrue(factory.compare(iRes, iR) == 0);
	}

	@Test
	public void intervalIntersectionI1DisjointI2Rat() {
		// i1: (314.0, 427.0) i2: [-314.0, 314.0] expected: (0.0, 0.0)
		RationalNumber lo1 = factory.rational(factory.integer(314));
		RationalNumber up1 = factory.rational(factory.integer(427));
		RationalNumber lo2 = factory.rational(factory.integer(-314));
		RationalNumber up2 = factory.rational(factory.integer(314));
		RationalNumber loR = factory.rational(factory.zeroInteger());
		RationalNumber upR = factory.rational(factory.zeroInteger());
		boolean isIntegral1 = lo1 instanceof RealInteger;
		boolean isIntegral2 = lo2 instanceof RealInteger;
		boolean isIntegralR = loR instanceof RealInteger;
		boolean sl1 = true;
		boolean su1 = true;
		boolean sl2 = false;
		boolean su2 = false;
		boolean slR = true;
		boolean suR = true;
		Interval i1 = factory.newInterval(isIntegral1, lo1, sl1, up1, su1);
		Interval i2 = factory.newInterval(isIntegral2, lo2, sl2, up2, su2);
		Interval iR = factory.newInterval(isIntegralR, loR, slR, upR, suR);

		assert (lo1 instanceof RealRational) == (up1 instanceof RealRational);
		assert (lo2 instanceof RealRational) == (up2 instanceof RealRational);
		assert (loR instanceof RealRational) == (upR instanceof RealRational);

		Interval iRes = factory.intersection(i1, i2);

		assertTrue(factory.compare(iRes, iR) == 0);
	}

	@Test
	public void intervalIntersectionI1IntersectI2withExactlyOneInt() {
		// i1: [-427, 314] i2: [314, 427] expected: [314, 314]
		IntegerNumber lo1 = factory.integer(-427);
		IntegerNumber up1 = factory.integer(314);
		IntegerNumber lo2 = factory.integer(314);
		IntegerNumber up2 = factory.integer(427);
		IntegerNumber loR = factory.integer(314);
		IntegerNumber upR = factory.integer(314);
		boolean isIntegral1 = lo1 instanceof RealInteger;
		boolean isIntegral2 = lo2 instanceof RealInteger;
		boolean isIntegralR = loR instanceof RealInteger;
		boolean sl1 = false;
		boolean su1 = false;
		boolean sl2 = false;
		boolean su2 = false;
		boolean slR = false;
		boolean suR = false;
		Interval i1 = factory.newInterval(isIntegral1, lo1, sl1, up1, su1);
		Interval i2 = factory.newInterval(isIntegral2, lo2, sl2, up2, su2);
		Interval iR = factory.newInterval(isIntegralR, loR, slR, upR, suR);

		assert (lo1 instanceof RealInteger) == (up1 instanceof RealInteger);
		assert (lo2 instanceof RealInteger) == (up2 instanceof RealInteger);
		assert (loR instanceof RealInteger) == (upR instanceof RealInteger);

		Interval iRes = factory.intersection(i1, i2);

		assertTrue(factory.compare(iRes, iR) == 0);
	}

	@Test
	public void intervalIntersectionI1IntersectI2withExactlyRangeOfOneRat() {
		// i1: [314.0, +infi) i2: (-infi, 315) expected: [314.0, 315.0)
		RationalNumber lo1 = factory.rational(factory.integer(314));
		RationalNumber up1 = null;
		RationalNumber lo2 = null;
		RationalNumber up2 = factory.rational(factory.integer(315));
		RationalNumber loR = factory.rational(factory.integer(314));
		RationalNumber upR = factory.rational(factory.integer(315));
		boolean isIntegral1 = lo1 instanceof RealInteger;
		boolean isIntegral2 = lo2 instanceof RealInteger;
		boolean isIntegralR = loR instanceof RealInteger;
		boolean sl1 = false;
		boolean su1 = true;
		boolean sl2 = true;
		boolean su2 = true;
		boolean slR = false;
		boolean suR = true;
		Interval i1 = factory.newInterval(isIntegral1, lo1, sl1, up1, su1);
		Interval i2 = factory.newInterval(isIntegral2, lo2, sl2, up2, su2);
		Interval iR = factory.newInterval(isIntegralR, loR, slR, upR, suR);

		assert (lo1 instanceof RealInteger) == (up1 instanceof RealInteger);
		assert (lo2 instanceof RealInteger) == (up2 instanceof RealInteger);
		assert (loR instanceof RealInteger) == (upR instanceof RealInteger);

		Interval iRes = factory.intersection(i1, i2);

		assertTrue(factory.compare(iRes, iR) == 0);
	}

	@Test
	public void intervalIntersectionI1IntersectI2withMoreThanOneInt() {
		IntegerNumber lo1 = factory.integer(-427);
		IntegerNumber up1 = factory.integer(427);
		IntegerNumber lo2 = factory.integer(-314);
		IntegerNumber up2 = factory.integer(314);
		IntegerNumber loR = factory.integer(-314);
		IntegerNumber upR = factory.integer(314);
		boolean isIntegral1 = lo1 instanceof RealInteger;
		boolean isIntegral2 = lo2 instanceof RealInteger;
		boolean isIntegralR = loR instanceof RealInteger;
		boolean sl1 = true;
		boolean su1 = true;
		boolean sl2 = true;
		boolean su2 = true;
		boolean slR = false;
		boolean suR = false;
		Interval i1 = factory.newInterval(isIntegral1, lo1, sl1, up1, su1);
		Interval i2 = factory.newInterval(isIntegral2, lo2, sl2, up2, su2);
		Interval iR = factory.newInterval(isIntegralR, loR, slR, upR, suR);

		assert (lo1 instanceof RealInteger) == (up1 instanceof RealInteger);
		assert (lo2 instanceof RealInteger) == (up2 instanceof RealInteger);
		assert (loR instanceof RealInteger) == (upR instanceof RealInteger);

		Interval iRes = factory.intersection(i1, i2);

		assertTrue(factory.compare(iRes, iR) == 0);
	}

	@Test
	public void intervalIntersectionI1IntersectI2withLargerRangeOfOneRat() {
		// i1: (-infi, +infi) i2: (-infi, 314) expected: (-infi, 314.0)
		RationalNumber lo1 = null;
		RationalNumber up1 = null;
		RationalNumber lo2 = null;
		RationalNumber up2 = factory.rational(factory.integer(314));
		RationalNumber loR = null;
		RationalNumber upR = factory.rational(factory.integer(314));
		boolean isIntegral1 = lo1 instanceof RealInteger;
		boolean isIntegral2 = lo2 instanceof RealInteger;
		boolean isIntegralR = loR instanceof RealInteger;
		boolean sl1 = true;
		boolean su1 = true;
		boolean sl2 = true;
		boolean su2 = true;
		boolean slR = true;
		boolean suR = true;
		Interval i1 = factory.newInterval(isIntegral1, lo1, sl1, up1, su1);
		Interval i2 = factory.newInterval(isIntegral2, lo2, sl2, up2, su2);
		Interval iR = factory.newInterval(isIntegralR, loR, slR, upR, suR);
		Interval iRes = factory.intersection(i1, i2);

		assert (lo1 instanceof RealInteger) == (up1 instanceof RealInteger);
		assert (lo2 instanceof RealInteger) == (up2 instanceof RealInteger);
		assert (loR instanceof RealInteger) == (upR instanceof RealInteger);

		assertTrue(factory.compare(iRes, iR) == 0);
	}

	@Test
	public void intervalIntersectionI1UnivI2EmptyRat() {
		RationalNumber lo1 = null;
		RationalNumber up1 = null;
		RationalNumber lo2 = factory.rational(factory.zeroInteger());
		RationalNumber up2 = factory.rational(factory.zeroInteger());
		RationalNumber loR = factory.rational(factory.zeroInteger());
		RationalNumber upR = factory.rational(factory.zeroInteger());
		boolean isIntegral1 = lo1 instanceof RealInteger;
		boolean isIntegral2 = lo2 instanceof RealInteger;
		boolean isIntegralR = loR instanceof RealInteger;
		boolean sl1 = true;
		boolean su1 = true;
		boolean sl2 = true;
		boolean su2 = true;
		boolean slR = true;
		boolean suR = true;
		Interval i1 = factory.newInterval(isIntegral1, lo1, sl1, up1, su1);
		Interval i2 = factory.newInterval(isIntegral2, lo2, sl2, up2, su2);
		Interval iR = factory.newInterval(isIntegralR, loR, slR, upR, suR);

		assert (lo1 instanceof RealRational) == (up1 instanceof RealRational);
		assert (lo2 instanceof RealRational) == (up2 instanceof RealRational);
		assert (loR instanceof RealRational) == (upR instanceof RealRational);

		Interval iRes = factory.intersection(i1, i2);

		assertTrue(factory.compare(iRes, iR) == 0);
	}

	// TODO: To add comm for Union part
	@Test(expected = NullPointerException.class)
	public void intervalUnionNullInputI1() {
		IntegerNumber lo2 = factory.integer(-314);
		IntegerNumber up2 = factory.integer(427);
		boolean isIntegral2 = lo2 instanceof RealInteger;
		boolean sl2 = true;
		boolean su2 = true;
		Interval i1 = null;
		Interval i2 = factory.newInterval(isIntegral2, lo2, sl2, up2, su2);
		IntervalUnion iuRes = new IntervalUnion();
		;

		assert (lo2 instanceof RealInteger) == (up2 instanceof RealInteger);
		factory.union(i1, i2, iuRes);
	}

	@Test(expected = NullPointerException.class)
	public void intervalUnionNullInputI2() {
		IntegerNumber lo1 = factory.integer(-314);
		IntegerNumber up1 = factory.integer(427);
		boolean isIntegral1 = lo1 instanceof RealInteger;
		boolean sl1 = true;
		boolean su1 = true;
		Interval i1 = factory.newInterval(isIntegral1, lo1, sl1, up1, su1);
		Interval i2 = null;
		IntervalUnion iuRes = new IntervalUnion();
		;

		assert (lo1 instanceof RealInteger) == (up1 instanceof RealInteger);
		factory.union(i1, i2, iuRes);
	}

	@Test(expected = NullPointerException.class)
	public void intervalUnionNullInputIU() {
		IntegerNumber lo1 = factory.integer(-314);
		IntegerNumber up1 = factory.integer(427);
		boolean isIntegral1 = lo1 instanceof RealInteger;
		boolean sl1 = true;
		boolean su1 = true;
		Interval i1 = factory.newInterval(isIntegral1, lo1, sl1, up1, su1);
		Interval i2 = i1;
		IntervalUnion iuRes = null;

		assert (lo1 instanceof RealInteger) == (up1 instanceof RealInteger);
		factory.union(i1, i2, iuRes);
	}

	@Test(expected = IllegalArgumentException.class)
	public void intervalUnionInvalInputI1IntI2Rat() {
		IntegerNumber lo1 = factory.integer(1);
		IntegerNumber up1 = factory.integer(1);
		RationalNumber lo2 = factory.rational(lo1);
		RationalNumber up2 = factory.rational(up1);
		boolean isIntegral1 = lo1 instanceof RealInteger;
		boolean isIntegral2 = lo2 instanceof RealInteger;
		boolean sl1 = true;
		boolean su1 = true;
		boolean sl2 = true;
		boolean su2 = true;
		Interval i1 = factory.newInterval(isIntegral1, lo1, sl1, up1, su1);
		Interval i2 = factory.newInterval(isIntegral2, lo2, sl2, up2, su2);
		IntervalUnion iuRes = new IntervalUnion();
		;

		assert (lo1 instanceof RealInteger) == (up1 instanceof RealInteger);
		assert (lo2 instanceof RealRational) == (up2 instanceof RealRational);

		factory.union(i1, i2, iuRes);
	}

	@Test(expected = IllegalArgumentException.class)
	public void intervalUnionInvalInputI1RatI2Int() {
		IntegerNumber lo2 = factory.integer(1);
		IntegerNumber up2 = factory.integer(1);
		RationalNumber lo1 = factory.rational(lo2);
		RationalNumber up1 = factory.rational(up2);
		boolean isIntegral1 = lo1 instanceof RealInteger;
		boolean isIntegral2 = lo2 instanceof RealInteger;
		boolean sl1 = true;
		boolean su1 = true;
		boolean sl2 = true;
		boolean su2 = true;
		Interval i1 = factory.newInterval(isIntegral1, lo1, sl1, up1, su1);
		Interval i2 = factory.newInterval(isIntegral2, lo2, sl2, up2, su2);
		IntervalUnion iuRes = new IntervalUnion();
		;

		assert (lo1 instanceof RealRational) == (up1 instanceof RealRational);
		assert (lo2 instanceof RealInteger) == (up2 instanceof RealInteger);

		factory.union(i1, i2, iuRes);
	}

	@Test
	public void intervalUnionI1DisjointI2Int() {
		// i1: (-infi, -314] i2: [314, +infi) expected: {0, null}
		IntegerNumber lo1 = null;
		IntegerNumber up1 = factory.integer(-314);
		IntegerNumber lo2 = factory.integer(314);
		IntegerNumber up2 = null;
		boolean isIntegral1 = up1 instanceof RealInteger;
		boolean isIntegral2 = lo2 instanceof RealInteger;
		boolean sl1 = true;
		boolean su1 = false;
		boolean sl2 = false;
		boolean su2 = true;
		Interval i1 = factory.newInterval(isIntegral1, lo1, sl1, up1, su1);
		Interval i2 = factory.newInterval(isIntegral2, lo2, sl2, up2, su2);
		IntervalUnion iuRes = new IntervalUnion();

		assert up1 instanceof RealInteger;
		assert lo2 instanceof RealInteger;

		factory.union(i1, i2, iuRes);

		assertTrue(iuRes.status == -1);
		assertTrue(iuRes.union == null);
	}

	@Test
	public void intervalUnionI1DisjointI2Rat() {
		// i1: (314.0, 427.0) i2: [-314.0, 314.0] expected: {1, null}
		RationalNumber lo1 = factory.rational(factory.integer(314));
		RationalNumber up1 = factory.rational(factory.integer(427));
		RationalNumber lo2 = factory.rational(factory.integer(-314));
		RationalNumber up2 = factory.rational(factory.integer(314));
		boolean isIntegral1 = lo1 instanceof RealInteger;
		boolean isIntegral2 = lo2 instanceof RealInteger;
		boolean sl1 = true;
		boolean su1 = true;
		boolean sl2 = true;
		boolean su2 = true;
		Interval i1 = factory.newInterval(isIntegral1, lo1, sl1, up1, su1);
		Interval i2 = factory.newInterval(isIntegral2, lo2, sl2, up2, su2);
		IntervalUnion iuRes = new IntervalUnion();

		assert (lo1 instanceof RealRational) == (up1 instanceof RealRational);
		assert (lo2 instanceof RealRational) == (up2 instanceof RealRational);

		factory.union(i1, i2, iuRes);

		assertTrue(iuRes.status == 1);
		assertTrue(iuRes.union == null);
	}

	@Test
	public void intervalUnionI1IntersectI2withExactlyOneIntDisjoint() {
		// i1: [-427, 314] i2: [314, 427] expected: [-427, 427]
		IntegerNumber lo1 = factory.integer(-427);
		IntegerNumber up1 = factory.integer(314);
		IntegerNumber lo2 = factory.integer(314);
		IntegerNumber up2 = factory.integer(427);
		IntegerNumber loR = factory.integer(-427);
		IntegerNumber upR = factory.integer(427);
		boolean isIntegral1 = lo1 instanceof RealInteger;
		boolean isIntegral2 = lo2 instanceof RealInteger;
		boolean isIntegralR = loR instanceof RealInteger;
		boolean sl1 = false;
		boolean su1 = false;
		boolean sl2 = false;
		boolean su2 = false;
		boolean slR = false;
		boolean suR = false;
		Interval i1 = factory.newInterval(isIntegral1, lo1, sl1, up1, su1);
		Interval i2 = factory.newInterval(isIntegral2, lo2, sl2, up2, su2);
		Interval iR = factory.newInterval(isIntegralR, loR, slR, upR, suR);
		IntervalUnion iuRes = new IntervalUnion();

		assert (lo1 instanceof RealInteger) == (up1 instanceof RealInteger);
		assert (lo2 instanceof RealInteger) == (up2 instanceof RealInteger);
		assert (loR instanceof RealInteger) == (upR instanceof RealInteger);

		factory.union(i1, i2, iuRes);

		assertTrue(iuRes.status == 0);
		assertTrue(factory.compare(iuRes.union, iR) == 0);
	}

	@Test
	public void intervalIntersectionI1IntersectI2withExactlyAdjacent() {
		// i1: [314.0, +infi) i2: (-infi, 314.0) expected: (-infi, +infi)
		RationalNumber lo1 = factory.rational(factory.integer(314));
		RationalNumber up1 = null;
		RationalNumber lo2 = null;
		RationalNumber up2 = factory.rational(factory.integer(314));
		RationalNumber loR = null;
		RationalNumber upR = null;
		boolean isIntegral1 = lo1 instanceof RealInteger;
		boolean isIntegral2 = up2 instanceof RealInteger;
		boolean isIntegralR = loR instanceof RealInteger;
		boolean sl1 = false;
		boolean su1 = true;
		boolean sl2 = true;
		boolean su2 = true;
		boolean slR = true;
		boolean suR = true;
		Interval i1 = factory.newInterval(isIntegral1, lo1, sl1, up1, su1);
		Interval i2 = factory.newInterval(isIntegral2, lo2, sl2, up2, su2);
		Interval iR = factory.newInterval(isIntegralR, loR, slR, upR, suR);
		IntervalUnion iuRes = new IntervalUnion();

		assert (lo1 instanceof RealInteger) == (up1 instanceof RealInteger);
		assert (lo2 instanceof RealInteger) == (up2 instanceof RealInteger);
		assert (loR instanceof RealInteger) == (upR instanceof RealInteger);

		factory.union(i1, i2, iuRes);

		assertTrue(iuRes.status == 0);
		assertTrue(factory.compare(iuRes.union, iR) == 0);
	}

	@Test
	public void intervalUnionI1IntersectI2withMoreThanOneInt() {
		// i1: [-427, 427] i2: [-314, 314] expected: [-427, 427]
		IntegerNumber lo1 = factory.integer(-427);
		IntegerNumber up1 = factory.integer(427);
		IntegerNumber lo2 = factory.integer(-314);
		IntegerNumber up2 = factory.integer(314);
		IntegerNumber loR = factory.integer(-427);
		IntegerNumber upR = factory.integer(427);
		boolean isIntegral1 = lo1 instanceof RealInteger;
		boolean isIntegral2 = lo2 instanceof RealInteger;
		boolean isIntegralR = loR instanceof RealInteger;
		boolean sl1 = false;
		boolean su1 = false;
		boolean sl2 = false;
		boolean su2 = false;
		boolean slR = false;
		boolean suR = false;
		Interval i1 = factory.newInterval(isIntegral1, lo1, sl1, up1, su1);
		Interval i2 = factory.newInterval(isIntegral2, lo2, sl2, up2, su2);
		Interval iR = factory.newInterval(isIntegralR, loR, slR, upR, suR);
		IntervalUnion iuRes = new IntervalUnion();

		assert (lo1 instanceof RealInteger) == (up1 instanceof RealInteger);
		assert (lo2 instanceof RealInteger) == (up2 instanceof RealInteger);
		assert (loR instanceof RealInteger) == (upR instanceof RealInteger);

		factory.union(i1, i2, iuRes);

		assertTrue(iuRes.status == 0);
		assertTrue(factory.compare(iuRes.union, iR) == 0);
	}

	@Test
	public void intervalUnionI1IntersectI2withLargerRangeOfOneRat() {
		// i1: (-infi, 314] i2: (-427.0, 314.0) expected: (-infi, 314.0]
		RationalNumber lo1 = null;
		RationalNumber up1 = factory.rational(factory.integer(314));
		RationalNumber lo2 = factory.rational(factory.integer(-427));
		RationalNumber up2 = factory.rational(factory.integer(314));
		RationalNumber loR = null;
		RationalNumber upR = factory.rational(factory.integer(314));
		boolean isIntegral1 = lo1 instanceof RealInteger;
		boolean isIntegral2 = lo2 instanceof RealInteger;
		boolean isIntegralR = loR instanceof RealInteger;
		boolean sl1 = true;
		boolean su1 = false;
		boolean sl2 = true;
		boolean su2 = true;
		boolean slR = true;
		boolean suR = false;
		Interval i1 = factory.newInterval(isIntegral1, lo1, sl1, up1, su1);
		Interval i2 = factory.newInterval(isIntegral2, lo2, sl2, up2, su2);
		Interval iR = factory.newInterval(isIntegralR, loR, slR, upR, suR);
		IntervalUnion iuRes = new IntervalUnion();

		assert (lo1 instanceof RealInteger) == (up1 instanceof RealInteger);
		assert (lo2 instanceof RealInteger) == (up2 instanceof RealInteger);
		assert (loR instanceof RealInteger) == (upR instanceof RealInteger);

		factory.union(i1, i2, iuRes);

		assertTrue(iuRes.status == 0);
		assertTrue(factory.compare(iuRes.union, iR) == 0);
	}

	@Test
	public void intervalUnionI1EmptyI2UnivRat() {
		RationalNumber lo1 = factory.rational(factory.zeroInteger());
		RationalNumber up1 = factory.rational(factory.zeroInteger());
		RationalNumber lo2 = null;
		RationalNumber up2 = null;
		RationalNumber loR = null;
		RationalNumber upR = null;
		boolean isIntegral1 = lo1 instanceof RealInteger;
		boolean isIntegral2 = lo2 instanceof RealInteger;
		boolean isIntegralR = loR instanceof RealInteger;
		boolean sl1 = true;
		boolean su1 = true;
		boolean sl2 = true;
		boolean su2 = true;
		boolean slR = true;
		boolean suR = true;
		Interval i1 = factory.newInterval(isIntegral1, lo1, sl1, up1, su1);
		Interval i2 = factory.newInterval(isIntegral2, lo2, sl2, up2, su2);
		Interval iR = factory.newInterval(isIntegralR, loR, slR, upR, suR);
		IntervalUnion iuRes = new IntervalUnion();

		assert (lo1 instanceof RealInteger) == (up1 instanceof RealInteger);
		assert (lo2 instanceof RealInteger) == (up2 instanceof RealInteger);
		assert (loR instanceof RealInteger) == (upR instanceof RealInteger);

		factory.union(i1, i2, iuRes);

		assertTrue(iuRes.status == 0);
		assertTrue(factory.compare(iuRes.union, iR) == 0);
	}

	/**
	 * @Exception NullPointerException is thrown for the affineTransform method
	 *            when trying to affineTransform an interval and two numbers
	 *            that all of those should be not null. This test ensures that
	 *            the throwing of the exception when any of those three
	 *            parameters is null.
	 */
	@Test(expected = NullPointerException.class)
	public void intervalAffineTransformNullInputOfInterval_itv() {
		IntegerNumber a = factory.integer(1);
		IntegerNumber b = factory.integer(1);
		Interval itv = null;

		factory.affineTransform(itv, a, b);
	}

	/**
	 * @Exception NullPointerException is thrown for the affineTransform method
	 *            when trying to affineTransform an interval and two numbers
	 *            that all of those should be not null. This test ensures that
	 *            the throwing of the exception when any of those three
	 *            parameters is null.
	 */
	@Test(expected = NullPointerException.class)
	public void intervalAffineTransformNullInputOfNumber_a() {
		IntegerNumber a = null;
		IntegerNumber b = factory.integer(1);
		IntegerNumber lo = factory.integer(-10);
		Number up = factory.number("10");
		boolean sl = true;
		boolean su = true;
		Interval itv = factory.newInterval(true, lo, sl, up, su);

		factory.affineTransform(itv, a, b);
	}

	/**
	 * @Exception NullPointerException is thrown for the affineTransform method
	 *            when trying to affineTransform an interval and two numbers
	 *            that all of those should be not null. This test ensures that
	 *            the throwing of the exception when any of those three
	 *            parameters is null.
	 */
	@Test(expected = NullPointerException.class)
	public void intervalAffineTransformNullInputOfNumber_b() {
		RationalNumber a = factory.rational(factory.integer(1));
		RationalNumber b = null;
		RationalNumber lo = factory.rational(factory.integer(1));
		RationalNumber up = factory.rational(factory.integer(1));
		boolean isIntegral = lo instanceof RealInteger;
		boolean sl = true;
		boolean su = true;
		Interval itv = factory.newInterval(isIntegral, lo, sl, up, su);

		assert (lo instanceof RealInteger) == (up instanceof RealInteger);
		assert (lo instanceof RealRational) == (up instanceof RealRational);

		factory.affineTransform(itv, a, b);
	}

	/**
	 * @Exception IllegalArgumentException is thrown for the affineTransform
	 *            method when trying to affineTransform an interval and two
	 *            numbers that either of two numbers has different type with the
	 *            interval (rational number or integer number). This test
	 *            ensures that the throwing of the exception does not rely on
	 *            the order of the incompatible number arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void numberAffineTransformInvalInputOfIntegerNumber_a() {
		IntegerNumber a = factory.integer(3);
		RationalNumber b = factory.rational(factory.integer(3));
		RationalNumber lo = factory.rational(factory.integer(-4));
		RationalNumber up = factory.rational(factory.integer(4));
		boolean isIntegral = lo instanceof RealInteger;
		boolean sl = true;
		boolean su = true;
		Interval itv = factory.newInterval(isIntegral, lo, sl, up, su);

		assert (lo instanceof RealInteger) == (up instanceof RealInteger);
		assert (lo instanceof RealRational) == (up instanceof RealRational);

		factory.affineTransform(itv, a, b);
	}

	/**
	 * @Exception IllegalArgumentException is thrown for the affineTransform
	 *            method when trying to affineTransform an interval and two
	 *            numbers that either of two numbers has different type with the
	 *            interval (rational number or integer number). This test
	 *            ensures that the throwing of the exception does not rely on
	 *            the order of the incompatible number arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void intervalAffineTransformInvalInputOfRationalNumber_a() {
		RationalNumber a = factory.rational(factory.integer(3));
		IntegerNumber b = factory.integer(3);
		IntegerNumber lo = factory.integer(-4);
		IntegerNumber up = factory.integer(4);
		boolean isIntegral = lo instanceof RealInteger;
		boolean sl = true;
		boolean su = true;
		Interval itv = factory.newInterval(isIntegral, lo, sl, up, su);

		assert (lo instanceof RealInteger) == (up instanceof RealInteger);
		assert (lo instanceof RealRational) == (up instanceof RealRational);

		factory.affineTransform(itv, a, b);
	}

	/**
	 * @Exception IllegalArgumentException is thrown for the affineTransform
	 *            method when trying to affineTransform an interval and two
	 *            numbers that either of two numbers has different type with the
	 *            interval (rational number or integer number). This test
	 *            ensures that the throwing of the exception does not rely on
	 *            the order of the incompatible number arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void intervalAffineTransformInvalInputOfIntegerNumber_b() {
		RationalNumber a = factory.rational(factory.integer(3));
		IntegerNumber b = factory.integer(3);
		RationalNumber lo = factory.rational(factory.integer(-4));
		RationalNumber up = factory.rational(factory.integer(4));
		boolean isIntegral = lo instanceof RealInteger;
		boolean sl = true;
		boolean su = true;
		Interval itv = factory.newInterval(isIntegral, lo, sl, up, su);

		assert (lo instanceof RealInteger) == (up instanceof RealInteger);
		assert (lo instanceof RealRational) == (up instanceof RealRational);

		factory.affineTransform(itv, a, b);
	}

	/**
	 * @Exception IllegalArgumentException is thrown for the affineTransform
	 *            method when trying to affineTransform an interval and two
	 *            numbers that either of two numbers has different type with the
	 *            interval (rational number or integer number). This test
	 *            ensures that the throwing of the exception does not rely on
	 *            the order of the incompatible number arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void intervalAffineTransformInvalInputOfRationalNumber_b() {
		IntegerNumber a = factory.integer(3);
		RationalNumber b = factory.rational(factory.integer(3));
		IntegerNumber lo = factory.integer(-4);
		IntegerNumber up = factory.integer(4);
		boolean isIntegral = lo instanceof RealInteger;
		boolean sl = true;
		boolean su = true;
		Interval itv = factory.newInterval(isIntegral, lo, sl, up, su);

		assert (lo instanceof RealInteger) == (up instanceof RealInteger);
		assert (lo instanceof RealRational) == (up instanceof RealRational);

		factory.affineTransform(itv, a, b);
	}

	/**
	 * Testing the affineTransform method with an input of one Interval and two
	 * Number arguments. This covers the cases of the interval argument with a
	 * negative infinity as its lower boundary value. The result interval should
	 * keep its lower boundary value as negative infinity.
	 */
	@Test
	public void intervalAffineTransformIntervalWithLowerValue_NegInfinity() {
		IntegerNumber a = factory.integer(314);
		IntegerNumber b = factory.integer(314);
		IntegerNumber lo = null;
		IntegerNumber up = factory.integer(1);
		boolean isIntegral = up instanceof RealInteger;
		boolean sl = true;
		boolean su = true;
		Interval itv = factory.newInterval(isIntegral, lo, sl, up, su);
		Interval result = factory.affineTransform(itv, a, b);

		assert result.lower() == null;
		assert ((IntegerNumber) result.upper()).intValue() == 628;
	}

	/**
	 * Testing the affineTransform method with an input of one Interval and two
	 * Number arguments. This covers the cases of the interval argument with a
	 * positive infinity as its upper boundary value. The result interval should
	 * keep its upper boundary value as positive infinity.
	 */
	@Test
	public void intervalAffineTransformIntervalWithUpperValue_PosInfinity() {
		RationalNumber a = factory.rational(factory.integer(314));
		RationalNumber b = factory.rational(factory.integer(314));
		RationalNumber lo = factory.rational(factory.integer(-1));
		RationalNumber up = null;
		boolean isIntegral = lo instanceof RealInteger;
		boolean sl = true;
		boolean su = true;
		Interval itv = factory.newInterval(isIntegral, lo, sl, up, su);
		Interval result = factory.affineTransform(itv, a, b);

		assert (result.upper() == null);
	}

	/**
	 * Testing the affineTransform method with an input of one Interval and two
	 * Number arguments. This covers the cases of the first Number argument a
	 * with a value of "-1" and the second Number argument b with a value of
	 * "0". The both boundaries' absolute value of result interval should be
	 * reversed.
	 */
	@Test
	public void intervalAffineTransformWithNumber_a_LessThanZero() {
		IntegerNumber a = factory.integer(-1);
		IntegerNumber b = factory.integer(0);
		IntegerNumber lo = factory.integer(314);
		IntegerNumber up = factory.integer(427);
		IntegerNumber reslo = factory.integer(-427);
		IntegerNumber resup = factory.integer(-314);
		boolean isIntegral = lo instanceof RealInteger;
		boolean sl = true;
		boolean su = true;
		Interval itv = factory.newInterval(isIntegral, lo, sl, up, su);
		Interval result = factory.affineTransform(itv, a, b);

		assert (result.lower().compareTo(reslo) == 0);
		assert (result.upper().compareTo(resup) == 0);
	}

	/**
	 * Testing the affineTransform method with an input of one Interval and two
	 * Number arguments. This covers the cases of the first Number argument a
	 * with a value of "0" and both boundaries not strict. The result interval
	 * should be "[b, b]".
	 */
	@Test
	public void intervalAffineTransformIntervalWithBothBoundNotStrictAndNumber_a_isZero() {
		IntegerNumber a = factory.integer(0);
		IntegerNumber b = factory.integer(3);
		IntegerNumber lo = null;
		IntegerNumber up = factory.integer(427);
		boolean isIntegral = up instanceof RealInteger;
		boolean sl = true;
		boolean su = false;
		Interval itv = factory.newInterval(isIntegral, lo, sl, up, su);
		Interval expectedRes = factory.newInterval(isIntegral, b, false, b,
				false);
		Interval result = factory.affineTransform(itv, a, b);

		System.out.println(result.toString());
		assert (result.equals(expectedRes));
	}

	/**
	 * Testing the affineTransform method with an input of one Interval and two
	 * Number arguments. This covers the cases of the first Number argument a
	 * with a value of "0" and lower boundary strict. The result interval should
	 * be "[b, b]".
	 */
	@Test
	public void intervalAffineTransformIntervalWithLowerBoundStrictAndNumber_a_isZero() {
		RationalNumber a = factory.rational(factory.integer(0));
		RationalNumber b = factory.rational(factory.integer(314));
		RationalNumber lo = factory.rational(factory.integer(427));
		RationalNumber up = null;
		boolean isIntegral = up instanceof RealInteger;
		boolean sl = true;
		boolean su = false;
		Interval itv = factory.newInterval(isIntegral, lo, sl, up, su);
		Interval expectedRes = factory.newInterval(isIntegral, b, false, b, false);
		Interval result = factory.affineTransform(itv, a, b);

		assert (result.equals(expectedRes));
	}

	/**
	 * Testing the affineTransform method with an input of one Interval and two
	 * Number arguments. This covers the cases of the first Number argument a
	 * with a value of "0" and upper boundary strict. The result interval should
	 * be "[b, b]".
	 */
	@Test
	public void intervalAffineTransformIntervalWithUpperBoundStrictAndNumber_a_isZero() {
		RationalNumber a = factory.rational(factory.integer(0));
		RationalNumber b = factory.rational(factory.integer(314));
		RationalNumber lo = null;
		RationalNumber up = factory.rational(factory.integer(427));
		boolean isIntegral = up instanceof RealInteger;
		boolean sl = false;
		boolean su = true;
		Interval itv = factory.newInterval(isIntegral, lo, sl, up, su);
		Interval expectedRes = factory.newInterval(isIntegral, b, false, b, false);
		Interval result = factory.affineTransform(itv, a, b);

		assert (result.equals(expectedRes));
	}

	/**
	 * Testing the affineTransform method with an input of one Interval and two
	 * Number arguments. This covers the cases of the first Number argument a
	 * with a value of "0" and both boundaries strict. The result interval
	 * should be "[b, b]".
	 */
	@Test
	public void intervalAffineTransformIntervalWithBothBoundStrictAndNumber_a_isZero() {
		RationalNumber a = factory.rational(factory.integer(0));
		RationalNumber b = factory.rational(factory.integer(314));
		RationalNumber lo = null;
		RationalNumber up = null;
		boolean isIntegral = lo instanceof RealInteger;
		boolean sl = true;
		boolean su = true;
		Interval itv = factory.newInterval(isIntegral, lo, sl, up, su);
		Interval expectedRes = factory.newInterval(isIntegral, b, false, b, false);
		Interval result = factory.affineTransform(itv, a, b);

		assert (result.equals(expectedRes));
	}
}