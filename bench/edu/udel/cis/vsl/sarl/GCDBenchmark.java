package edu.udel.cis.vsl.sarl;



import java.math.BigInteger;



import edu.udel.cis.vsl.sarl.IF.number.*;
import edu.udel.cis.vsl.sarl.number.Numbers;



public class GCDBenchmark { 
	
	

	private static NumberFactory factory = Numbers.REAL_FACTORY;

	private static BigInteger bigOne = BigInteger.ONE;
	private static BigInteger bigThirty = new BigInteger("30"); 
	private static BigInteger bigTwenty = new BigInteger("20"); 
	private static BigInteger bigThousand = new BigInteger("1000");  
	private static BigInteger bigTwoThousand = new BigInteger("2000"); 


	
	
	
	public void GCDBenchmark1() { 
		
		IntegerNumber a = factory.integer(bigThirty);
		IntegerNumber b = factory.integer(bigTwenty); 
		long x = System.nanoTime();
		factory.gcd(a, b); 
		long y = System.nanoTime();

		System.out.println(y-x);

	} 
	
	
public void GCDBenchmark2() { 
		
		IntegerNumber a = factory.integer(bigOne);
		IntegerNumber b = factory.integer(bigTwenty); 
		long x = System.nanoTime();
		factory.gcd(a, b); 
		long y = System.nanoTime();

		System.out.println( y-x);

	} 


public void GCDBenchmark3() { 
	
	IntegerNumber a = factory.integer(bigThousand);
	IntegerNumber b = factory.integer(bigTwoThousand); 
	long x = System.nanoTime();
	 factory.gcd(a, b); 
	long y = System.nanoTime();

	System.out.print(y-x);

}
}
