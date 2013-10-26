package edu.udel.cis.vsl.sarl;

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


public class GCDBenchmark2 { 
	
	
	private static PrintStream out = System.out;

	private static NumberFactory factory = Numbers.REAL_FACTORY;

	private static BigInteger bigNegativeThirty = new BigInteger("-30");
	private static BigInteger bigNegativeTen = new BigInteger("-10");
	private static BigInteger bigNegativeThree = new BigInteger("-3");
	private static BigInteger bigNegativeOne = new BigInteger("-1");
	private static BigInteger bigZero = new BigInteger("0");
	private static BigInteger bigOne = BigInteger.ONE;
	private static BigInteger bigTwo = new BigInteger("2"); 
	private static BigInteger bigThirty = new BigInteger("30"); 
	private static BigInteger bigTwenty = new BigInteger("20"); 
	private static BigInteger bigThousand = new BigInteger("1000");  
	private static BigInteger bigTwoThousand = new BigInteger("2000"); 


	
	
	
	public void GCDBenchmark1() { 
		
		IntegerNumber a = factory.integer(bigThirty);
		IntegerNumber b = factory.integer(bigTwenty); 
		long x = System.currentTimeMillis() % 1000;
		factory.gcd(a, b); 
		long y = System.currentTimeMillis() % 1000;

		System.out.println(y-x);

	} 
	
	
public void GCDBenchmark2() { 
		
		IntegerNumber a = factory.integer(bigOne);
		IntegerNumber b = factory.integer(bigTwenty); 
		long x = System.currentTimeMillis() % 1000;
		factory.gcd(a, b); 
		long y = System.currentTimeMillis() % 1000;

		System.out.println( y-x);

	} 


public void GCDBenchmark3() { 
	
	IntegerNumber a = factory.integer(bigThousand);
	IntegerNumber b = factory.integer(bigTwoThousand); 
	long x = System.currentTimeMillis() % 1000;
	 factory.gcd(a, b); 
	long y = System.currentTimeMillis() % 1000;

	System.out.print(y-x);

}
}
