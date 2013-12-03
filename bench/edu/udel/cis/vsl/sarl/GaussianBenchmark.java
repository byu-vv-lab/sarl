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
								rand.nextInt(1))); 
						

			}
		}
 
		
		long x = System.nanoTime();
		factory.gaussianElimination(matrix);
		long y = System.nanoTime();
		System.out.println(y-x);

	 

		int row2 = 10; 
		int columns2 = 10;
		RationalNumber[][] matrix2 = new RationalNumber[10][10];
		for(int i=0;i<row2;i++){ 
			for(int j=0;j<columns2;j++){ 
				matrix2[i][j] = factory.integerToRational(
								factory.integer(
								rand.nextInt(1))); 
						

			}
		}
 
		
		long x2 = System.nanoTime();
		factory.gaussianElimination(matrix2);
		long y2 = System.nanoTime();
		System.out.println(y2-x2);
	
 



int row3 = 100; 
int columns3 = 100;
RationalNumber[][] matrix3 = new RationalNumber[100][100];
for(int i=0;i<row3;i++){ 
	for(int j=0;j<columns3;j++){ 
		matrix3[i][j] = factory.integerToRational(
						factory.integer(
						rand.nextInt(1))); 
				

	}
}


long x3 = System.nanoTime();
factory.gaussianElimination(matrix3);
long y3 = System.nanoTime();

System.out.println(y3-x3);   






int row4 = 150; 
int columns4 = 150;
RationalNumber[][] matrix4 = new RationalNumber[150][150];
for(int i=0;i<row4;i++){ 
	for(int j=0;j<columns4;j++){ 
		matrix4[i][j] = factory.integerToRational(
						factory.integer(
						rand.nextInt(1))); 
				

	}
}


long x4 = System.nanoTime();
factory.gaussianElimination(matrix4);
long y4 = System.nanoTime();

System.out.println(y4-x4);  




int row5 = 300; 
int columns5 = 300;
RationalNumber[][] matrix5 = new RationalNumber[300][300];
for(int i=0;i<row5;i++){ 
	for(int j=0;j<columns5;j++){ 
		matrix5[i][j] = factory.integerToRational(
						factory.integer(
						rand.nextInt(1))); 
				

	}
}


long x5 = System.nanoTime();
factory.gaussianElimination(matrix5);
long y5 = System.nanoTime();

System.out.println(y5-x5); 




int row6 = 1000; 
int columns6 = 1000;
RationalNumber[][] matrix6 = new RationalNumber[1000][1000];
for(int i=0;i<row6;i++){ 
	for(int j=0;j<columns6;j++){ 
		matrix6[i][j] = factory.integerToRational(
						factory.integer(
						rand.nextInt(1))); 
				

	}
}


long x6 = System.nanoTime();
factory.gaussianElimination(matrix6);
long y6 = System.nanoTime();

System.out.println(y6-x6); 




int row7 = 10000; 
int columns7 = 10000;
RationalNumber[][] matrix7 = new RationalNumber[10000][10000];
for(int i=0;i<row7;i++){ 
	for(int j=0;j<columns7;j++){ 
		matrix7[i][j] = factory.integerToRational(
						factory.integer(
						rand.nextInt(1))); 
				

	}
}


long x7 = System.nanoTime();
factory.gaussianElimination(matrix7);
long y7 = System.nanoTime();

System.out.println(y7-x7); 




int row8 = 100000; 
int columns8 = 100000;
RationalNumber[][] matrix8 = new RationalNumber[100000][100000];
for(int i=0;i<row8;i++){ 
	for(int j=0;j<columns8;j++){ 
		matrix8[i][j] = factory.integerToRational(
						factory.integer(
						rand.nextInt(1))); 
				

	}
}


long x8 = System.nanoTime();
factory.gaussianElimination(matrix8);
long y8 = System.nanoTime();

System.out.println(y8-x8);



int row9 = 50000; 
int columns9 = 500000;
RationalNumber[][] matrix9 = new RationalNumber[50000][50000];
for(int i=0;i<row9;i++){ 
	for(int j=0;j<columns9;j++){ 
		matrix9[i][j] = factory.integerToRational(
						factory.integer(
						rand.nextInt(1))); 
				

	}
}





				

	





//end of gaussian
}}