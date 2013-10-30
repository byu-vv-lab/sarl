package edu.udel.cis.vsl.sarl.preuniverse.common;

import static org.junit.Assert.assertEquals;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.ReferenceExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

/**
 * 
 * @author Julian Piane (jpiane)
 * 
 * This benchmark tests the preuniverse method referencedType()
 * 
 * The purpose of this benchmark is to analyze the effect of the recursive
 * structure of this method in the hopes that a loop-based modification
 * may be applied to allow for performance enhancements.
 * 
 * we will be simulating a symbolic expression with many embedded sub-expressions
 * by creating n-dimensional arrays where each sub-array is a sub-expression.
 *
 */

public class ReferencedTypeBenchmark {
	private static PreUniverse universe;
	private static SymbolicType arrayType, integerType;
	private static int size, runs;
	private static long startingTime, endingTime;
	private static double totalTime;
	
	public static void main(String args[])
	{
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		universe = PreUniverses.newPreUniverse(system);
		integerType = universe.integerType();
		arrayType = universe.arrayType(integerType);
		size = 0;
		runs = 10;
		
		ReferenceExpression identityReference, nDimensionalArrayReference;
		NumericExpression zero;
		SymbolicArrayType ArrayType;
		
		zero = universe.integer(0);
		
		
		identityReference = universe.identityReference();

		for(int i = 1; i <=7; i++)
		{
			nDimensionalArrayReference = universe.arrayElementReference(identityReference, zero);
			ArrayType = universe.arrayType(arrayType);
			int summer = 200;
			size += summer;
			totalTime = 0;
			
			for(int n = 0; n < size; n++)
			{
				ArrayType = universe.arrayType(ArrayType);
				nDimensionalArrayReference = universe.arrayElementReference(nDimensionalArrayReference, zero);
			}
			
			for(int r = 0; r < runs; r++)
			{
				startingTime = System.nanoTime();
				universe.referencedType(ArrayType, nDimensionalArrayReference);
				endingTime = System.nanoTime();
				totalTime += ((double)(endingTime-startingTime))/1000000000.0;
			}
			
			System.out.println("Total Time in seconds: " + totalTime/runs + " for size: "+size);
		}

		
	}
}
