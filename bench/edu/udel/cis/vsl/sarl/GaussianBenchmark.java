package edu.udel.cis.vsl.sarl;




import java.util.Random;

import edu.udel.cis.vsl.sarl.IF.number.*;
import edu.udel.cis.vsl.sarl.number.Numbers;



public class GaussianBenchmark { 
	
	

	private static NumberFactory factory = Numbers.REAL_FACTORY;



	
	
public static void main(String args[]){	
		
		 
		Random rand = new Random();
	
		int row = 2; 
		int columns = 2;
		RationalNumber[][] matrix = new RationalNumber[2][2];
		for(int i=0;i<row;i++){ 
			for(int j=0;j<columns;j++){ 
				matrix[i][j] = factory.integerToRational(
								factory.integer(
								rand.nextInt(10))); 
						

			}
		}
 
		
		long x = System.nanoTime();
		factory.gaussianElimination(matrix);
		long y = System.nanoTime();
		System.out.print(y-x);

	 

		int row2 = 10; 
		int columns2 = 10;
		RationalNumber[][] matrix2 = new RationalNumber[10][10];
		for(int i=0;i<row2;i++){ 
			for(int j=0;j<columns2;j++){ 
				matrix2[i][j] = factory.integerToRational(
								factory.integer(
								rand.nextInt(10))); 
						

			}
		}
 
		
		long x2 = System.nanoTime();
		factory.gaussianElimination(matrix2);
		long y2 = System.nanoTime();
		System.out.print(y2-x2);
	
 



int row3 = 100; 
int columns3 = 100;
RationalNumber[][] matrix3 = new RationalNumber[100][100];
for(int i=0;i<row3;i++){ 
	for(int j=0;j<columns3;j++){ 
		matrix3[i][j] = factory.integerToRational(
						factory.integer(
						rand.nextInt(10))); 
				

	}
}


long x3 = System.nanoTime();
factory.gaussianElimination(matrix3);
long y3 = System.nanoTime();

System.out.println(y3-x3); 

//end of gaussian
}}