package edu.udel.cis.vsl.sarl;


import java.math.BigInteger;


import edu.udel.cis.vsl.sarl.IF.number.*;
import edu.udel.cis.vsl.sarl.number.Numbers;



public class MultiplyIntegerNumberBenchmark { 
	
	

	private static NumberFactory factory = Numbers.REAL_FACTORY;


	private static BigInteger bigZero = new BigInteger("0");
	private static BigInteger bigOne = BigInteger.ONE;
	private static BigInteger bigTwo = new BigInteger("2"); 
	private static BigInteger bigThousand = new BigInteger("1000");  
	private static BigInteger bigTwoThousand = new BigInteger("2000"); 
	private static BigInteger bigTen = new BigInteger("10");  
	private static BigInteger bigTwenty = new BigInteger("20");
	private static BigInteger bigA1 = new BigInteger("21220"); 
	private static BigInteger bigA2 = new BigInteger("43784"); 
	private static BigInteger bigA3 = new BigInteger("452222");  
	private static BigInteger bigA4 = new BigInteger("48273"); 




	
public static void main(String args[]) {
	
		
		IntegerNumber numberZero = factory.integer(bigZero);
		IntegerNumber numberOne = factory.integer(bigOne); 
		long x = System.nanoTime();
		factory.multiply(numberZero, numberOne); 
		long y = System.nanoTime();

		System.out.println(y-x);

	  
	
	
	
		
		IntegerNumber numberTwo = factory.integer(bigTwo);
		long x2 = System.nanoTime();
		factory.multiply(numberTwo, numberTwo); 
		long y2 = System.nanoTime();

		System.out.println(y2-x2);

	  

	
	IntegerNumber numberTen = factory.integer(bigTen);
	IntegerNumber numberTwenty = factory.integer(bigTwenty); 
	long x3 = System.nanoTime();
	factory.multiply(numberTen, numberTwenty); 
	long y3 = System.nanoTime();

	System.out.println(y3-x3);
 
	
	IntegerNumber numberTwoThousand = factory.integer(bigTwoThousand);
	IntegerNumber numberThousand = factory.integer(bigThousand); 
	long x4 = System.nanoTime();
	factory.multiply(numberTwoThousand, numberThousand); 
	long y4 = System.nanoTime();

	System.out.println(y4-x4);

 



	
	
 

	
	IntegerNumber numberA1 = factory.integer(bigA1);
	IntegerNumber numberA2 = factory.integer(bigA2); 
	long x5 = System.nanoTime();
	factory.multiply(numberA1, numberA2); 
	long y5 = System.nanoTime();

	System.out.println(y5-x5);

 


	
	IntegerNumber numberA3 = factory.integer(bigA3);
	long x6 = System.nanoTime();
	factory.multiply(numberA3, numberA2); 
	long y6 = System.nanoTime();

	System.out.println(y6-x6);



	
	IntegerNumber numberA4 = factory.integer(bigA4); 
	long x7 = System.nanoTime();
	factory.multiply(numberA3, numberA4); 
	long y7 = System.nanoTime();

	System.out.println(y7-x7);

}  
	
	
	
}