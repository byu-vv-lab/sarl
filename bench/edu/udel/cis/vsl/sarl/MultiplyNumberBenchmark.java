package edu.udel.cis.vsl.sarl;


import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.*;
import edu.udel.cis.vsl.sarl.number.Numbers;




public class MultiplyNumberBenchmark { 
	
	

	private static NumberFactory factory = Numbers.REAL_FACTORY;


	



	
public static void main(String args[]) {
	
		
		Number numberZero = factory.number("0");
		Number numberOne = factory.integer("1"); 
		long x = System.nanoTime(); 
		for(int i=0; i<999;i++){ 
			factory.multiply(numberZero, numberOne); 

		}
		long y = System.nanoTime();

		System.out.println(y-x);

	  
	
	
	
		
		long x2 = System.nanoTime(); 
		for(int i=0; i<9999;i++){ 
			factory.multiply(numberZero, numberOne); 

		}
		long y2 = System.nanoTime();

		System.out.println(y2-x2);

		long x3 = System.nanoTime(); 
		for(int i=0; i<99999;i++){ 
			factory.multiply(numberZero, numberOne); 

		}
		long y3 = System.nanoTime();

		System.out.println(y3-x3);

		
		
		long x4 = System.nanoTime(); 
		for(int i=0; i<99999;i++){ 
			factory.multiply(numberZero, numberOne); 

		}
		long y4 = System.nanoTime();

		System.out.println(y4-x4); 
		
		
		
		long x5 = System.nanoTime(); 
		for(int i=0; i<99999;i++){ 
			factory.multiply(numberZero, numberOne); 

		}
		long y5 = System.nanoTime();

		System.out.println(y5-x5); 
		
		
		long x6 = System.nanoTime(); 
		for(int i=0; i<99999;i++){ 
			factory.multiply(numberZero, numberOne); 

		}
		long y6 = System.nanoTime();

		System.out.println(y6-x6);
	
	
	
	

}  
	
	
	
}