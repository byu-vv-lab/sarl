package edu.udel.cis.vsl.sarl;




import edu.udel.cis.vsl.sarl.IF.number.*;
import edu.udel.cis.vsl.sarl.number.Numbers;



public class GaussianBenchmark { 
	
	

	private static NumberFactory factory = Numbers.REAL_FACTORY;

	

	
	
	
	public void GaussianBenchmark1() { 
		
		int a = 30;
		int b = 20;  
		RationalNumber[][] matrix = new RationalNumber[a][b];

		long x = System.nanoTime();
		factory.gaussianElimination(matrix);
		long y = System.nanoTime();

		System.out.println(y-x);

	} 
	
	
public void GaussianBenchmark2() { 
		
		int a = 2;
		int b = 2; 
		RationalNumber[][] matrix = new RationalNumber[a][b];

		long x = System.nanoTime();
		factory.gaussianElimination(matrix);
		long y = System.nanoTime();

		System.out.println(y-x);

	} 

public void GaussianBenchmark3() { 
	
	int a = 10;
	int b = 10;  
	RationalNumber[][] matrix = new RationalNumber[a][b];

	long x = System.nanoTime();
	factory.gaussianElimination(matrix);
	long y = System.nanoTime();

	System.out.println(y-x);

} 


public void GaussianBenchmar4() { 
	
	int a = 100;
	int b = 100;  
	RationalNumber[][] matrix = new RationalNumber[a][b];

	long x = System.nanoTime();
	factory.gaussianElimination(matrix);
	long y = System.nanoTime();

	System.out.println(y-x);

} 


public void GaussianBenchmark5() { 
	
	int a = 200;
	int b = 400;  
	RationalNumber[][] matrix = new RationalNumber[a][b];

	long x = System.nanoTime();
	factory.gaussianElimination(matrix);
	long y = System.nanoTime();

	System.out.println(y-x);

} 


public void GaussianBenchmark6() { 
	
	int a = 1000;
	int b = 1000;  
	RationalNumber[][] matrix = new RationalNumber[a][b];

	long x = System.nanoTime();
	factory.gaussianElimination(matrix);
	long y = System.nanoTime();

	System.out.println(y-x);

} 

public void GaussianBenchmark7() { 
	
	int a = 10000;
	int b = 10000;  
	RationalNumber[][] matrix = new RationalNumber[a][b];

	long x = System.nanoTime();
	factory.gaussianElimination(matrix);
	long y = System.nanoTime();

	System.out.println(y-x);

} 
}
