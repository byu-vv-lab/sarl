package edu.udel.cis.vsl.sarl;


import java.math.BigInteger;

import edu.udel.cis.vsl.sarl.IF.number.*;
import edu.udel.cis.vsl.sarl.number.Numbers;



public class MultiplyIntegerNumberBenchmark { 
	
	

	private static NumberFactory factory = Numbers.REAL_FACTORY;


	
	private static BigInteger bigTwo = new BigInteger("2"); 
	

	



	
public static void main(String args[]) {
	
		
		
	IntegerNumber numberX = factory.integer(bigTwo);
	IntegerNumber numberTen = factory.integer(bigTwo); 
	long x = System.nanoTime();
	for(int i =0; i <99999;i++){
		factory.multiply(numberX, numberTen); 
	}
	long y = System.nanoTime();

	System.out.println(y-x);
  
	
	
	
	
		IntegerNumber numberX2 = factory.integer(bigTwo);
		IntegerNumber numberTwo = factory.integer(bigTwo); 
		long x2 = System.nanoTime();
		for(int i =0; i <999999;i++){
			factory.multiply(numberX2, numberTwo); 
		}
		long y2 = System.nanoTime();

		System.out.println(y2-x2);
	  
	
		IntegerNumber numberX3 = factory.integer(bigTwo);
		long x3 = System.nanoTime();
		for(int i =0; i <9999999;i++){
			factory.multiply(numberX3, numberTwo); 
		}
		long y3 = System.nanoTime();

		System.out.println(y3-x3);
	  

 
		IntegerNumber numberX4 = factory.integer(bigTwo);
		long x4 = System.nanoTime();
		for(int i =0; i <99999999;i++){
			factory.multiply(numberX4, numberTwo); 
		}
		long y4 = System.nanoTime();

		System.out.println(y4-x4);
	  

		
		IntegerNumber numberX5 = factory.integer(bigTwo);
		long x5 = System.nanoTime();
		for(int i =0; i <999999999;i++){
			factory.multiply(numberX5, numberTwo); 
		}
		long y5 = System.nanoTime();

		System.out.println(y5-x5);

		IntegerNumber numberX6 = factory.integer(bigTwo);
		long x6 = System.nanoTime();
		for(int i =0; i <999999999;i++){
			factory.multiply(numberX6, numberTwo); 
		}
		long y6 = System.nanoTime();

		System.out.println(y6-x6);
	

}  
	
	
	
}