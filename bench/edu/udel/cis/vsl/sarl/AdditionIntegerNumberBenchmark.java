package edu.udel.cis.vsl.sarl;


import java.math.BigInteger;


import edu.udel.cis.vsl.sarl.IF.number.*;
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

  
	
	
}	
	
}