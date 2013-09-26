package edu.udel.cis.vsl.sarl.numbers;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
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
	
	
	
}
