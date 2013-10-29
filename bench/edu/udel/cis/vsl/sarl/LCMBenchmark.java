package edu.udel.cis.vsl.sarl;


import java.math.BigInteger;



import edu.udel.cis.vsl.sarl.IF.number.*;
import edu.udel.cis.vsl.sarl.number.Numbers;



public class LCMBenchmark { 
	
	

	private static NumberFactory factory = Numbers.REAL_FACTORY;

	
	private static BigInteger bigOne = BigInteger.ONE;
	private static BigInteger bigThirty = new BigInteger("30"); 
	private static BigInteger bigTwenty = new BigInteger("20"); 
	private static BigInteger bigThousand = new BigInteger("1000");  
	private static BigInteger bigTwoThousand = new BigInteger("2000"); 


	
	
	
	public void LCMBenchmark1() { 
		
		IntegerNumber a = factory.integer(bigThirty);
		IntegerNumber b = factory.integer(bigTwenty); 
		long x = System.nanoTime();
		factory.lcm(a, b); 
		long y = System.nanoTime();
		System.out.print("LCM");
		System.out.println(y-x);

	} 
	
	
public void LCMBenchmark2() { 
		
		IntegerNumber a = factory.integer(bigOne);
		IntegerNumber b = factory.integer(bigTwenty); 
		long x = System.nanoTime();
		factory.lcm(a, b); 
		long y = System.nanoTime();

		System.out.println( y-x);

	} 


public void LCMBenchmark3() { 
	
	IntegerNumber a = factory.integer(bigThousand);
	IntegerNumber b = factory.integer(bigTwoThousand); 
	long x = System.nanoTime();
	 factory.lcm(a, b); 
	long y = System.nanoTime();

	System.out.print(y-x);

} 

public void LCMBenchmark4() { 
	
	IntegerNumber a = factory.integer(bigThousand);
	IntegerNumber b = factory.integer(bigTwoThousand); 
	long x = System.nanoTime();
	
	factory.lcm(a,b);
	long y = System.nanoTime();

	System.out.print(y-x);

}
}
