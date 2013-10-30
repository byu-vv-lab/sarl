package edu.udel.cis.vsl.sarl;



import java.math.BigInteger;



import edu.udel.cis.vsl.sarl.IF.number.*;
import edu.udel.cis.vsl.sarl.number.Numbers;



public class GCDBenchmark2 { 
	
	

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
	
public static void main(String args[]){
		
		IntegerNumber integerThirty = factory.integer(bigThirty);
		IntegerNumber integerTwenty = factory.integer(bigTwenty); 
		long x = System.nanoTime();
		factory.gcd(integerThirty, integerTwenty); 
		long y = System.nanoTime();

		System.out.println(y-x);

	
	
	
		
		IntegerNumber integerOne = factory.integer(bigOne);
		long x2 = System.nanoTime();
		factory.gcd(integerOne, integerTwenty); 
		long y2 = System.nanoTime();

		System.out.println( y2-x2);

	 



	
	IntegerNumber integerThousand = factory.integer(bigThousand);
	IntegerNumber integerTwoThousand = factory.integer(bigTwoThousand); 
	long x3 = System.nanoTime();
	 factory.gcd(integerThousand, integerTwoThousand); 
	long y3 = System.nanoTime();

	System.out.println(y3-x3);



	
	IntegerNumber integerOneMillion = factory.integer(bigOneMillion);
	IntegerNumber integerTenMillion = factory.integer(bigTenMillion); 
	long x4 = System.nanoTime();
	 factory.gcd(integerOneMillion, integerTenMillion); 
	long y4 = System.nanoTime();

	System.out.println(y4-x4);



	
	IntegerNumber integerFiftyMillion = factory.integer(bigFiftyMillion);
	IntegerNumber integerOneHundredMillion= factory.integer(bigOneHundredMillion); 
	long x5 = System.nanoTime();
	factory.gcd(integerFiftyMillion, integerOneHundredMillion); 

	long y5 = System.nanoTime();

	System.out.println(y5-x5);



	
	IntegerNumber integerOneBillion = factory.integer(bigOneBillion);
	IntegerNumber integerFiveBillion = factory.integer(bigFiveBillion); 
	long x6 = System.nanoTime();
	 factory.gcd(integerOneBillion, integerFiveBillion); 
	long y6 = System.nanoTime();

	System.out.print(y6-x6);



	
	IntegerNumber integerOneTrillion = factory.integer(bigOneTrillion);
	IntegerNumber integerTenTrillion = factory.integer(bigTenTrillion); 
	long x7 = System.nanoTime();
	 factory.gcd(integerOneTrillion,  integerTenTrillion); 
	long y7 = System.nanoTime();

	System.out.print(y7-x7);



	
	IntegerNumber integerOneQuadrillion = factory.integer(bigOneQuadrillion);
	IntegerNumber integerTwoQuadrillion = factory.integer(bigTwoQuadrillion); 
	long x8 = System.nanoTime();
	 factory.gcd(integerOneQuadrillion, integerTwoQuadrillion); 
	long y8 = System.nanoTime();

	System.out.print(y8-x8);

 
	}
}
