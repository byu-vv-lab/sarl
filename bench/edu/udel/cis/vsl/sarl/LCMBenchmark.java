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

	
public static void  main(String args[]){ 
	
		
		IntegerNumber numberThirty = factory.integer(bigThirty);
		IntegerNumber numberTwenty = factory.integer(bigTwenty); 
		long x = System.nanoTime();
		factory.lcm(numberTwenty, numberThirty); 
		long y = System.nanoTime();
		
		System.out.println(y-x);

	
	
	

	IntegerNumber numberThousand = factory.integer(bigThousand);
	IntegerNumber numberTwoThousand = factory.integer(bigTwoThousand); 
	long x2 = System.nanoTime();
	 factory.lcm(numberThousand, numberTwoThousand); 
	long y2 = System.nanoTime();

	System.out.print(y2-x2);



	
	IntegerNumber numberOneMillion = factory.integer(bigOneMillion);
	IntegerNumber numberTenMillion = factory.integer(bigTenMillion); 
	long x3 = System.nanoTime();
	
	factory.lcm(numberOneMillion,numberTenMillion);
	long y3 = System.nanoTime();

	System.out.print(y3-x3);



	
	IntegerNumber numberFiftyMillion = factory.integer(bigFiftyMillion);
	IntegerNumber numberHundredMillion = factory.integer(bigOneHundredMillion); 
	long x4 = System.nanoTime();
	
	factory.lcm(numberFiftyMillion,numberHundredMillion);
	long y4 = System.nanoTime();

	System.out.print(y4-x4);



	
	IntegerNumber numberOneBillion = factory.integer(bigOneBillion);
	IntegerNumber numberFiveBillion = factory.integer(bigFiveBillion); 
	long x5 = System.nanoTime();
	
	factory.lcm(numberOneBillion,numberFiveBillion);
	long y5 = System.nanoTime();

	System.out.print(y5-x5);



	
	IntegerNumber numberOneTrillion = factory.integer(bigOneTrillion);
	IntegerNumber numberTenTrillion = factory.integer(bigTenTrillion); 
	long x6 = System.nanoTime();
	
	factory.lcm(numberOneTrillion,numberTenTrillion);
	long y6 = System.nanoTime();

	System.out.print(y6-x6);



	
	IntegerNumber numberOneQuadrillion = factory.integer(bigOneQuadrillion);
	IntegerNumber numberTwoQuadrillion = factory.integer(bigTwoQuadrillion); 
	long x7 = System.nanoTime();
	
	factory.lcm(numberOneQuadrillion,numberTwoQuadrillion);
	long y7 = System.nanoTime();

	System.out.print(y7-x7);

}
}
