package edu.udel.cis.vsl.sarl;


import java.math.BigInteger;





import edu.udel.cis.vsl.sarl.IF.number.*;
import edu.udel.cis.vsl.sarl.number.Numbers;



public class MultiplyRationalNumberBenchmark { 
	
	

	private static NumberFactory factory = Numbers.REAL_FACTORY;

	private static BigInteger bigOne = BigInteger.ONE;
	private static BigInteger bigTwo = new BigInteger("2");  
	private static BigInteger bigThree = new BigInteger("3"); 

	

	
	







	
	
public static void  main(String args []){ 
	

		RationalNumber rationalOneHalf = factory.rational(bigOne, bigTwo);
		RationalNumber rationalOneThird = factory.rational(bigOne,bigThree); 
		long x = System.nanoTime();
		for(int i =0; i <999;i++){
			factory.multiply(rationalOneHalf, rationalOneThird); 
		}
		long y = System.nanoTime();

		System.out.println(y-x); 
		
		
		
		long x2 = System.nanoTime();
		for(int i =0; i <999;i++){
			factory.multiply(rationalOneHalf, rationalOneThird); 
		}
		long y2 = System.nanoTime();

		System.out.println(y2-x2); 
		
		
		long x3 = System.nanoTime();
		for(int i =0; i <9999;i++){
			factory.multiply(rationalOneHalf, rationalOneThird); 
		}
		long y3 = System.nanoTime();

		System.out.println(y3-x3);  
		
		
		long x4 = System.nanoTime();
		for(int i =0; i <99999;i++){
			factory.multiply(rationalOneHalf, rationalOneThird); 
		}
		long y4 = System.nanoTime();

		System.out.println(y4-x4); 
		
		long x5 = System.nanoTime();
		for(int i =0; i <999999;i++){
			factory.multiply(rationalOneHalf, rationalOneThird); 
		}
		long y5 = System.nanoTime();

		System.out.println(y5-x5);  
		
		
		
		long x6 = System.nanoTime();
		for(int i =0; i <9999999;i++){
			factory.multiply(rationalOneHalf, rationalOneThird); 
		}
		long y6 = System.nanoTime();

		System.out.println(y6-x6); 
		

		
		
	  
	  
		
		
		
		
	
		
		

} 





}

 


	

	
	
	
