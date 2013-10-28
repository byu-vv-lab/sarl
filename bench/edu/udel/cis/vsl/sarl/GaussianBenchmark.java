package edu.udel.cis.vsl.sarl;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.*;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.number.real.RealInteger;
import edu.udel.cis.vsl.sarl.number.real.RealRational;


public class GaussianBenchmark { 
	
	
	private static PrintStream out = System.out;

	private static NumberFactory factory = Numbers.REAL_FACTORY;

	

	
	
	
	public void GaussianBenchmark1() { 
		
		int a = 30;
		int b = 20; 
		long x = System.currentTimeMillis() % 1000;
		RationalNumber[][] matrix = new RationalNumber[a][b];
		factory.gaussianElimination(matrix);
		long y = System.currentTimeMillis() % 1000;

		System.out.println(y-x);

	} 
	
	
public void GaussianBenchmark2() { 
		
		int a = 2;
		int b = 2; 
		long x = System.currentTimeMillis() % 1000;
		RationalNumber[][] matrix = new RationalNumber[a][b];
		factory.gaussianElimination(matrix);
		long y = System.currentTimeMillis() % 1000;

		System.out.println(y-x);

	} 

public void GaussianBenchmark3() { 
	
	int a = 10;
	int b = 10; 
	long x = System.currentTimeMillis() % 1000;
	RationalNumber[][] matrix = new RationalNumber[a][b];
	factory.gaussianElimination(matrix);
	long y = System.currentTimeMillis() % 1000;

	System.out.println(y-x);

} 


public void GaussianBenchmar4() { 
	
	int a = 100;
	int b = 100; 
	long x = System.currentTimeMillis() % 1000;
	RationalNumber[][] matrix = new RationalNumber[a][b];
	factory.gaussianElimination(matrix);
	long y = System.currentTimeMillis() % 1000;

	System.out.println(y-x);

} 


public void GaussianBenchmark5() { 
	
	int a = 200;
	int b = 400; 
	long x = System.currentTimeMillis() % 1000;
	RationalNumber[][] matrix = new RationalNumber[a][b];
	factory.gaussianElimination(matrix);
	long y = System.currentTimeMillis() % 1000;

	System.out.println(y-x);

} 


public void GaussianBenchmark6() { 
	
	int a = 1000;
	int b = 1000; 
	long x = System.currentTimeMillis() % 1000;
	RationalNumber[][] matrix = new RationalNumber[a][b];
	factory.gaussianElimination(matrix);
	long y = System.currentTimeMillis() % 1000;

	System.out.println(y-x);

} 

public void GaussianBenchmark7() { 
	
	int a = 10000;
	int b = 10000; 
	long x = System.currentTimeMillis() % 1000;
	RationalNumber[][] matrix = new RationalNumber[a][b];
	factory.gaussianElimination(matrix);
	long y = System.currentTimeMillis() % 1000;

	System.out.println(y-x);

} 
}
