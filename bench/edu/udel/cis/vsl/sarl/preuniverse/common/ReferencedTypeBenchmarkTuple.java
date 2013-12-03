package edu.udel.cis.vsl.sarl.preuniverse.common;

import java.util.Arrays;

import edu.udel.cis.vsl.sarl.IF.expr.ReferenceExpression;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
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
 * This benchmark class also serves the purpose of comparing the results to the other
 * referencedType benchmarks, ReferencedTypeBenchmark and ReferencedTypeBenchmarkUnion.
 * The result of this study will shine light on certain inefficiencies that may be a
 * result of the different SymbolicTypes.
 *
 */

public class ReferencedTypeBenchmarkTuple {
	private static PreUniverse universe;
	private static SymbolicType tupleType, integerType;
	private static int size, runs;
	private static long startingTime, endingTime;
	private static double totalTime;
	
	public static void main(String args[])
	{
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		universe = PreUniverses.newPreUniverse(system);
		integerType = universe.integerType();
		tupleType = universe.tupleType(
				universe.stringObject("tupleType"),
				Arrays.asList(new SymbolicType[] { integerType }));
		size = 0;
		runs = 10;
		
		ReferenceExpression identityReference, nDimensionalTupleReference;
		SymbolicTupleType TupleType;
		
		IntObject zeroInt = universe.intObject(0);
		
		
		identityReference = universe.identityReference();

		for(int i = 1; i <=7; i++)
		{
			nDimensionalTupleReference = universe.tupleComponentReference(identityReference, zeroInt);
			TupleType = universe.tupleType(
					universe.stringObject("tupleType"),
					Arrays.asList(new SymbolicType[] { tupleType }));
			int summer = 200000;
			size += summer;
			totalTime = 0;
			
			for(int n = 0; n < size; n++)
			{
				TupleType = universe.tupleType(
						universe.stringObject("tupleType"),
						Arrays.asList(new SymbolicType[] { TupleType }));
				nDimensionalTupleReference = universe.tupleComponentReference(nDimensionalTupleReference, zeroInt);
			}
			
			for(int r = 0; r < runs; r++)
			{
				startingTime = System.nanoTime();
				universe.referencedType(TupleType, nDimensionalTupleReference);
				endingTime = System.nanoTime();
				totalTime += ((double)(endingTime-startingTime))/1000000000.0;
			}
			
			System.out.println("Total Time in seconds: " + totalTime/runs + " for size: "+size);
		}

		
	}
}
