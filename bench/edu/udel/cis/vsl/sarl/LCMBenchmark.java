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
	private static BigInteger bigOneMillion = new BigInteger("1000000");
	private static BigInteger bigTenMillion = new BigInteger("10000000");
	private static BigInteger bigFiftyMillion = new BigInteger("50000000");
	private static BigInteger bigOneHundredMillion = new BigInteger("100000000");
	private static BigInteger bigOneBillion = new BigInteger("1000000000");
	private static BigInteger bigFiveBillion = new BigInteger("5000000000");
	private static BigInteger bigOneTrillion = new BigInteger("1000000000000");
	private static BigInteger bigTenTrillion = new BigInteger("10000000000000");
	private static BigInteger bigOneQuadrillion = new BigInteger("1000000000000000");
	private static BigInteger bigTwoQuadrillion = new BigInteger("2000000000000000");

	
	
	
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

public void LCMBenchmark5() { 
	
	IntegerNumber a = factory.integer(bigOneMillion);
	IntegerNumber b = factory.integer(bigTenMillion); 
	long x = System.nanoTime();
	
	factory.lcm(a,b);
	long y = System.nanoTime();

	System.out.print(y-x);

}

public void LCMBenchmark6() { 
	
	IntegerNumber a = factory.integer(bigFiftyMillion);
	IntegerNumber b = factory.integer(bigOneHundredMillion); 
	long x = System.nanoTime();
	
	factory.lcm(a,b);
	long y = System.nanoTime();

	System.out.print(y-x);

}

public void LCMBenchmark7() { 
	
	IntegerNumber a = factory.integer(bigOneBillion);
	IntegerNumber b = factory.integer(bigFiveBillion); 
	long x = System.nanoTime();
	
	factory.lcm(a,b);
	long y = System.nanoTime();

	System.out.print(y-x);

}

public void LCMBenchmark8() { 
	
	IntegerNumber a = factory.integer(bigOneTrillion);
	IntegerNumber b = factory.integer(bigTenTrillion); 
	long x = System.nanoTime();
	
	factory.lcm(a,b);
	long y = System.nanoTime();

	System.out.print(y-x);

}

public void LCMBenchmark9() { 
	
	IntegerNumber a = factory.integer(bigOneQuadrillion);
	IntegerNumber b = factory.integer(bigTwoQuadrillion); 
	long x = System.nanoTime();
	
	factory.lcm(a,b);
	long y = System.nanoTime();

	System.out.print(y-x);

}
}
