package edu.udel.cis.vsl.sarl;


import java.math.BigInteger;



import edu.udel.cis.vsl.sarl.IF.number.*;
import edu.udel.cis.vsl.sarl.number.Numbers;



public class MultiplyRationalNumberBenchmark { 
	
	

	private static NumberFactory factory = Numbers.REAL_FACTORY;

	private static BigInteger bigOne = BigInteger.ONE;
	private static BigInteger bigTwo = new BigInteger("2");  
	private static BigInteger bigThree = new BigInteger("3"); 

	private static BigInteger bigThirty = new BigInteger("30"); 
	private static BigInteger bigTwenty = new BigInteger("20"); 
	private static BigInteger bigThousand = new BigInteger("1000");  
	private static BigInteger bigTwoThousand = new BigInteger("2000"); 
	private static BigInteger bigA1 = new BigInteger("21220"); 
	private static BigInteger bigA2 = new BigInteger("43784"); 
	private static BigInteger bigA3 = new BigInteger("452222");  
	private static BigInteger bigA4 = new BigInteger("48273"); 




	
	
	
	public void MultiplyRationalNumberBenchmark1() { 
		
		RationalNumber a = factory.rational(bigOne,bigTwo);
		RationalNumber b = factory.rational(bigOne,bigThree); 
		long x = System.nanoTime();
		factory.multiply(a, b) ;
		long y = System.nanoTime();

		System.out.println(y-x);

	}  
	
	
public void MultiplyRationalNumberBenchmark2() { 
		
		RationalNumber a = factory.rational(bigOne,bigTwenty);
		RationalNumber b = factory.rational(bigOne,bigThirty); 
		long x = System.nanoTime();
		factory.multiply(a, b) ;
		long y = System.nanoTime();

		System.out.println(y-x); 
		
		

	}   



public void MultiplyRationalNumberBenchmark3() { 
	
	RationalNumber a = factory.rational(bigTwenty,bigThousand);
	RationalNumber b = factory.rational(bigThirty,bigTwoThousand); 
	long x = System.nanoTime();
	factory.multiply(a, b) ;
	long y = System.nanoTime();

	System.out.println(y-x);

}  



public void MultiplyRationalNumberBenchmark4() { 
	
	RationalNumber a = factory.rational(bigA1,bigA2);
	RationalNumber b = factory.rational(bigA3,bigA4); 
	long x = System.nanoTime();
	factory.multiply(a, b) ;
	long y = System.nanoTime();

	System.out.println(y-x);

}  






 


	

	
	
	
}