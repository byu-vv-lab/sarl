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




	
	
public static void  main(String args []){ 
	
		RationalNumber rationalOneHalf = factory.rational(bigOne, bigTwo);
		RationalNumber rationalOneThird = factory.rational(bigOne,bigThree); 
		long x = System.nanoTime();
		factory.multiply(rationalOneThird, rationalOneHalf) ;
		long y = System.nanoTime();

		System.out.println(y-x);

	 
	
	
		
		RationalNumber rationalOneTwenty = factory.rational(bigOne,bigTwenty);
		RationalNumber rationalOneThirty = factory.rational(bigOne,bigThirty); 
		long x2 = System.nanoTime();
		factory.multiply(rationalOneTwenty, rationalOneThirty) ;
		long y2 = System.nanoTime();

		System.out.println(y2-x2); 
		
		

	  



	
	RationalNumber rationalTwentyOverThousand = factory.rational(bigTwenty,bigThousand);
	RationalNumber rationalThirtyOverTwoThousand= factory.rational(bigThirty,bigTwoThousand); 
	long x3 = System.nanoTime();
	factory.multiply(rationalTwentyOverThousand,rationalThirtyOverTwoThousand) ;
	long y3 = System.nanoTime();

	System.out.println(y3-x3);





	
	RationalNumber rationalA1overA2 = factory.rational(bigA1,bigA2);
	RationalNumber rationalA3overA4 = factory.rational(bigA3,bigA4); 
	long x4 = System.nanoTime();
	factory.multiply(rationalA1overA2,rationalA3overA4) ;
	long y4 = System.nanoTime();

	System.out.println(y4-x4);

} 





}

 


	

	
	
	
