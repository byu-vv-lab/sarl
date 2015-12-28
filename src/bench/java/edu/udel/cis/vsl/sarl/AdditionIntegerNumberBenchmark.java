package edu.udel.cis.vsl.sarl;


import java.math.BigInteger;

import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.number.Numbers;



public class AdditionIntegerNumberBenchmark { 
	
	

	private static NumberFactory factory = Numbers.REAL_FACTORY;


	private static BigInteger bigZero = new BigInteger("0");
	private static BigInteger bigOne = BigInteger.ONE;
	private static BigInteger bigTwo = new BigInteger("2"); 
	private static BigInteger bigThousand = new BigInteger("1000");  
	private static BigInteger bigTwoThousand = new BigInteger("2000"); 
	private static BigInteger bigThirtyOne = new BigInteger("31"); 
	private static BigInteger bigTen = new BigInteger("10"); 
	private static BigInteger bigA1 = new BigInteger("21220"); 
	private static BigInteger bigA2 = new BigInteger("43784"); 
	private static BigInteger bigA3 = new BigInteger("452222");  
	private static BigInteger bigA4 = new BigInteger("48273"); 
	private static BigInteger bigA5 = new BigInteger("48273786");  
	private static BigInteger bigA6 = new BigInteger("23456782"); 
	private static BigInteger bigA7 = new BigInteger("48456782345678"); 
	private static BigInteger bigA8 = new BigInteger("23456782345678"); 
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
	
	
		
		IntegerNumber integerZero = factory.integer(bigZero);
		IntegerNumber integerOne = factory.integer(bigOne); 
		long x = System.nanoTime();
		factory.add(integerZero, integerOne); 
		long y = System.nanoTime();

		System.out.println(y-x);

	 
	
	
	
		
		IntegerNumber integerTwo = factory.integer(bigTwo);
		long x2 = System.nanoTime();
		factory.add(integerTwo, integerTwo); 
		long y2 = System.nanoTime();

		System.out.println(y2-x2);

	 

	
	IntegerNumber integerTen = factory.integer(bigTen);
	long x3 = System.nanoTime();
	factory.add(integerTen, integerTwo); 
	long y3 = System.nanoTime();

	System.out.println(y3-x3);

  
	
	IntegerNumber integerTwoThousand = factory.integer(bigTwoThousand);
	IntegerNumber integerThirtyOne = factory.integer(bigThirtyOne); 
	long x4 = System.nanoTime();
	factory.add(integerTwoThousand, integerThirtyOne); 
	long y4 = System.nanoTime();

	System.out.println(y4-x4);

  



	
	IntegerNumber integerThousand = factory.integer(bigThousand); 
	long x5 = System.nanoTime();
	factory.add(integerTwoThousand, integerThousand); 
	long y5 = System.nanoTime();

	System.out.println(y5-x5);

  

	
	IntegerNumber integerA1 = factory.integer(bigA1);
	IntegerNumber integerA2 = factory.integer(bigA2); 
	long x6 = System.nanoTime();
	factory.add(integerA1, integerA2); 
	long y6 = System.nanoTime();

	System.out.println(y6-x6);

  


	
	IntegerNumber integerA3 = factory.integer(bigA3);
	long x7 = System.nanoTime();
	factory.add(integerA3, integerA2); 
	long y7 = System.nanoTime();

	System.out.println(y7-x7);

 


	
	IntegerNumber integerA4 = factory.integer(bigA4); 
	long x8 = System.nanoTime();
	factory.multiply(integerA4, integerA3); 
	long y8 = System.nanoTime();

	System.out.println(y8-x8);

   


	
	IntegerNumber integerA5 = factory.integer(bigA5);
	IntegerNumber integerA6 = factory.integer(bigA6); 
	long x9 = System.nanoTime();
	factory.add(integerA5, integerA6); 
	long y9 = System.nanoTime();

	System.out.println(y9-x9);

 

	
	IntegerNumber integerA7 = factory.integer(bigA7);
	IntegerNumber integerA8 = factory.integer(bigA8); 
	long x10 = System.nanoTime();
	factory.add(integerA7, integerA8); 
	long y10 = System.nanoTime();

	System.out.println(y10-x10); 
	
	
	
	IntegerNumber numberOneMillion = factory.integer(bigOneMillion);
	IntegerNumber numberTenMillion = factory.integer(bigTenMillion); 
	long x11 = System.nanoTime();
	
	factory.add(numberOneMillion,numberTenMillion);
	long y11 = System.nanoTime();

	System.out.print(y11-x11);



	
	IntegerNumber numberFiftyMillion = factory.integer(bigFiftyMillion);
	IntegerNumber numberHundredMillion = factory.integer(bigOneHundredMillion); 
	long x12 = System.nanoTime();
	
	factory.add(numberFiftyMillion,numberHundredMillion);
	long y12 = System.nanoTime();

	System.out.print(y12-x12);



	
	IntegerNumber numberOneBillion = factory.integer(bigOneBillion);
	IntegerNumber numberFiveBillion = factory.integer(bigFiveBillion); 
	long x13 = System.nanoTime();
	
	factory.add(numberOneBillion,numberFiveBillion);
	long y13 = System.nanoTime();

	System.out.print(y13-x13);



	
	IntegerNumber numberOneTrillion = factory.integer(bigOneTrillion);
	IntegerNumber numberTenTrillion = factory.integer(bigTenTrillion); 
	long x14 = System.nanoTime();
	
	factory.add(numberOneTrillion,numberTenTrillion);
	long y14 = System.nanoTime();

	System.out.print(y14-x14);



	
	IntegerNumber numberOneQuadrillion = factory.integer(bigOneQuadrillion);
	IntegerNumber numberTwoQuadrillion = factory.integer(bigTwoQuadrillion); 
	long x15 = System.nanoTime();
	
	factory.add(numberOneQuadrillion,numberTwoQuadrillion);
	long y15 = System.nanoTime();

	System.out.print(y15-x15);




  
	
	
}	
	
}