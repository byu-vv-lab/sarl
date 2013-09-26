package edu.udel.cis.vsl.sarl.numbers;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.*;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.number.real.RealInteger;
import edu.udel.cis.vsl.sarl.number.real.RealRational;

public class NumberFactoryTest {

	private static PrintStream out = System.out;

	private static NumberFactory factory = Numbers.REAL_FACTORY;

	private static BigInteger bigNegativeOne = new BigInteger("-1");
	private static BigInteger bigOne = BigInteger.ONE;
	private static BigInteger bigNegativeThirty = new BigInteger("-30");
	private static BigInteger bigThirty = new BigInteger("30");

	private static BigInteger bigThirtyOne = new BigInteger("31");

	private static BigInteger bigTen = new BigInteger("10");

	private static BigInteger bigFifteen = new BigInteger("15"); 
	
	private static BigInteger bigFive = new BigInteger("5"); 
	
	private static BigInteger bigTwo = new BigInteger("2");  
	
	private static BigInteger bigZero = new BigInteger("0"); 
	
	private static BigInteger bigSix = new BigInteger("6");
	
	private static BigInteger bigTwenty = new BigInteger("20");
	private static BigInteger bigNegativeThree = new BigInteger("-3");
	private static BigInteger bigThree = new BigInteger("3");
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void multiplyIntegers() {
		IntegerNumber a = factory.integer(bigThirty);
		IntegerNumber b = factory.integer(bigTen);
		IntegerNumber c = factory.multiply(a, b);
		IntegerNumber expected = factory.integer(new BigInteger("300"));

		//out.println(c);
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

	@Test
	public void addRat() {
		RationalNumber a = factory.rational(bigThirty, bigThirtyOne);
		RationalNumber b = factory.rational(bigTen, bigFifteen);
		RationalNumber c = factory.add(a, b);
		RationalNumber expected = factory.rational(new BigInteger("152"),
				new BigInteger("93"));

		//out.println(c);
		assertEquals(expected, c);
	}
	
	@Test
	public void rationalCeiling() {
		RationalNumber a = factory.rational(bigThirty, bigThirtyOne);
		RationalNumber b = factory.rational(bigTen, bigOne);
		IntegerNumber c = factory.ceil(a);
		IntegerNumber d = factory.ceil(b);
		IntegerNumber expectedC = factory.integer(bigOne);
		IntegerNumber expectedD = factory.integer(bigTen);
		//out.println(c);
		//out.println(d);
		assertEquals(expectedC, c);
		assertEquals(expectedD, d);
	} 
	
	@Test
	public void GCD() { 
		IntegerNumber a = factory.integer(bigThirty);
		IntegerNumber b = factory.integer(bigTwenty);
		IntegerNumber c = factory.gcd(a, b);
		IntegerNumber expected = factory.integer(new BigInteger("10"));

		//out.println(c);
		assertEquals(expected, c);
	} 
	
	@Test
	public void LCM() { 
		IntegerNumber a = factory.integer(bigThirty);
		IntegerNumber b = factory.integer(bigThirtyOne);
		IntegerNumber c = factory.lcm(a, b);
		IntegerNumber expected = factory.integer(new BigInteger("930")); 
		

		//out.println(c);
		assertEquals(expected, c);
	} 
	
	@Test
	public void subInteger() { 
		IntegerNumber a = factory.integer(bigThirty);
		IntegerNumber b = factory.integer(bigTen);
		IntegerNumber c = factory.subtract(a, b);
		IntegerNumber  expected = factory.integer(new BigInteger("20"));

		//out.println(c);
		assertEquals(expected, c);
	} 
	
	@Test
	public void IntegerNumberDecrement() { 
		IntegerNumber a = factory.integer(bigThirty); 
		IntegerNumber c = factory.decrement(a); 
		IntegerNumber expected = factory.integer(new BigInteger("29")); 
		
		//out.println(c); 
		assertEquals(expected, c);
	} 
	
	@Test
	public void IntegerNumberIncrement() { 
		IntegerNumber a = factory.integer(bigThirty); 
		IntegerNumber c = factory.increment(a); 
		IntegerNumber expected = factory.integer(new BigInteger("31")); 		
		
		//out.println(c); 
		assertEquals(expected, c);
	}  	
	
	@Test
	public void subRat() {
		RationalNumber a = factory.rational(bigFive, bigTwo);
		RationalNumber b = factory.rational(bigTen, bigFifteen);
		RationalNumber c = factory.subtract(a, b);
		RationalNumber expected = factory.rational(new BigInteger("11"),
				new BigInteger("6"));

		//out.println(c);
		assertEquals(expected, c);
	}		
	@Test
	public void longInt(){
		long a = 30;
		RealInteger b = (RealInteger) factory.integer(a);
		assertEquals(bigThirty, b);
		
	}
	/*@Test
	public void intApply() {
		IntegerNumber a = factory.integer(bigTen);
		IntegerNumber b = factory.integer(bigThree);
		IntegerNumber expectedC = factory.integer(bigThirty);
		IntegerNumber c = factory.apply(a,b);
		assertEquals(expectedC, c);
	}*/
	
	@Test
	public void realRatRat(){
		RealRational a = (RealRational) factory.rational(bigThirty, bigNegativeOne);
		RealRational expectedA = (RealRational) factory.rational(bigNegativeThirty, bigOne);
		assertEquals(expectedA, a);
	}
	
	@Test
	public void realRatIsIntegral(){
		RealRational a = (RealRational) factory.rational(bigThirty, bigOne);
		boolean expected = true;
		boolean actual = factory.isIntegral(a); 
		assertEquals(expected, actual);
	}
	
	@Test
	public void ceilingRatNumNegNum(){
		RationalNumber a = factory.rational(bigNegativeThree, bigOne);
		IntegerNumber b = factory.ceil(a);
		IntegerNumber expectedB = factory.integer(bigNegativeThree);
		assertEquals(expectedB, b);
		
	}
	
	@Test
	public void ratNumCompare(){
		RationalNumber a = factory.rational(bigThirty, bigTen);
		RationalNumber b = factory.rational(bigTwenty, bigTen);
		int c = factory.compare(a, b);
		int expectedC = 1;
		assertEquals(expectedC, c);
	}
	
	@Test
	public void intNumCompare(){
		IntegerNumber a = factory.integer(bigThirty);
		IntegerNumber b = factory.integer(bigTwenty);
		int c = factory.compare(a, b);
		int expectedC = 1;
		assertEquals(expectedC, c);
	}
	
	@Test
	public void numberCompare(){
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
	
	@Test
	public void ratNumDenominator(){
		RationalNumber a = factory.rational(bigTen, bigThree);
		IntegerNumber b = factory.denominator(a);
		IntegerNumber expectedB = factory.integer(bigThree);
		assertEquals(expectedB, b);
	}
	
	@Test
	public void ratNumDivide(){
		RationalNumber a = factory.rational(bigTen, bigThree);
		RationalNumber b = factory.rational(bigTwo, bigOne);
		RationalNumber c = factory.divide(a, b);
		RationalNumber expectedC = factory.rational(bigFive, bigThree);
		assertEquals(expectedC, c);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void intNumModNegArg(){
		IntegerNumber a = factory.integer(bigTen);
		IntegerNumber b = factory.integer(bigNegativeOne);
		factory.mod(a, b);		
	}
	@Test
	public void intNumMod(){
		IntegerNumber a = factory.integer(bigTen);
		IntegerNumber b = factory.integer(bigThree);
		IntegerNumber c = factory.mod(a, b);
		IntegerNumber expectedC = factory.integer(bigOne);
		assertEquals(expectedC, c);
	}
	@Test
	public void ratNumFloor(){
		RationalNumber a = factory.rational(bigTwenty, bigThree);
		IntegerNumber expectedB = factory.integer(bigSix);
		IntegerNumber b = factory.floor(a);
		assertEquals(expectedB, b);
		RationalNumber c = factory.rational(bigNegativeOne, bigThree);
		IntegerNumber expectedD = factory.integer(bigZero);
		IntegerNumber d = factory.floor(c);
		assertEquals(expectedD, d);
		RationalNumber e = factory.rational(bigTen, bigOne);
		IntegerNumber expectedF = factory.integer(bigTen);
		IntegerNumber f = factory.floor(e);
		assertEquals(expectedF, f);	
	}
	@Test
	public void intInt(){
		String a = "30";
		IntegerNumber b = factory.integer(a);
		IntegerNumber expectedB = factory.integer(bigThirty);
		assertEquals(expectedB, b);
	}
	
	@Test
	public void intNumIntToRat(){
		IntegerNumber a = factory.integer(bigThirty);
		RationalNumber expectedB = factory.rational(bigThirty, bigOne);
		RationalNumber b = factory.integerToRational(a);
		assertEquals(expectedB, b);
				
	}
	
	@Test(expected=ArithmeticException.class)
	public void ratNumIntValueNotIntegral(){
		RationalNumber a = factory.rational(bigTen, bigThree);
		factory.integerValue(a);
	}
	
	@Test
	public void ratNumIntValue(){
		RationalNumber a = factory.rational(bigTen, bigOne);
		IntegerNumber expectedB = factory.integer(bigTen);
		IntegerNumber b = factory.integerValue(a);
		assertEquals(expectedB, b);
	}
	
	@Test
	public void ratNumMultiply(){
		RationalNumber a = factory.rational(bigTen, bigOne);
		RationalNumber b = factory.rational(bigTwo, bigOne);
		RationalNumber expectedC = factory.rational(bigTwenty, bigOne);
		RationalNumber c = factory.multiply(a, b);
		assertEquals(expectedC, c);
	}
	@Test
	public void intNegate(){
		IntegerNumber a = factory.integer(bigOne);
		IntegerNumber expectedB = factory.integer(bigNegativeOne);
		IntegerNumber b = factory.negate(a);
		assertEquals(expectedB, b);
	}
	@Test
	public void stringToIntOrRat(){
		String a = "30";
		String b = "0.1";
		Number expectedC = factory.integer(bigThirty);
		Number c = factory.number(a);
		Number expectedD = factory.rational(bigOne, bigTen);
		Number d = factory.number(b);
	}
	
	@Test
	public void ratNumNumerator(){
		RationalNumber a = factory.rational(bigTen, bigThirty);
		IntegerNumber expectedB = factory.integer(bigTen);
		IntegerNumber b = factory.numerator(a);
		assertEquals(expectedB, b);
	}
	
	@Test
	public void testOneIntMethod(){
		IntegerNumber a = factory.oneInteger();
		IntegerNumber b = factory.integer(bigOne);
		assertEquals(b, a);
	}
	
	@Test
	public void testOneRatMethod(){
		RationalNumber a = factory.oneRational();
		RationalNumber b = factory.rational(bigOne, bigOne);
		assertEquals(b, a);
	}
	
	
}
