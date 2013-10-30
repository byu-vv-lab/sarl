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
		factory.multiply(numberZero, numberOne); 
		long y = System.nanoTime();

		System.out.println(y-x);

	  
	
	
	
		
		Number numberTwo = factory.integer("2");
		long x2 = System.nanoTime();
		factory.multiply(numberTwo, numberTwo); 
		long y2 = System.nanoTime();

		System.out.println(y2-x2);

	  

	
	Number numberTen = factory.integer("10");
	Number numberTwenty = factory.integer("20"); 
	long x3 = System.nanoTime();
	factory.multiply(numberTen, numberTwenty); 
	long y3 = System.nanoTime();

	System.out.println(y3-x3);
 
	
	Number numberTwoThousand = factory.integer("2000");
	Number numberThousand = factory.integer("1000"); 
	long x4 = System.nanoTime();
	factory.multiply(numberTwoThousand, numberThousand); 
	long y4 = System.nanoTime();

	System.out.println(y4-x4);

	Number numberTenThousand = factory.integer("10000");
	Number numberTwentyThousand = factory.integer("20000"); 
	long x5 = System.nanoTime();
	factory.multiply(numberTwentyThousand, numberTenThousand); 
	long y5 = System.nanoTime();

	System.out.println(y5-x5); 
	
	
	Number numberTwoMillion = factory.integer("2000000");
	Number numberOneMillion = factory.integer("1000000"); 
	long x6 = System.nanoTime();
	factory.multiply(numberTwoMillion, numberOneMillion); 
	long y6 = System.nanoTime();

	System.out.println(y6-x6); 
	
	
	
	Number numberTwoBillion = factory.integer("2000000000");
	Number numberOneBillion = factory.integer("1000000000"); 
	long x7 = System.nanoTime();
	factory.multiply(numberTwoBillion, numberOneBillion); 
	long y7 = System.nanoTime();

	System.out.println(y7-x7); 
	
	

	Number numberTwoTrillion = factory.integer("2000000000000");
	Number numberOneTrillion = factory.integer("1000000000000"); 
	long x8 = System.nanoTime();
	factory.multiply(numberTwoTrillion, numberOneTrillion); 
	long y8 = System.nanoTime();

	System.out.println(y8-x8); 
	
	
	Number numberTwoQuadrillion = factory.integer("2000000000000000");
	Number numberOneQuadrillion = factory.integer("1000000000000000"); 
	long x9 = System.nanoTime();
	factory.multiply(numberTwoQuadrillion, numberOneQuadrillion); 
	long y9 = System.nanoTime();

	System.out.println(y9-x9);

}  
	
	
	
}