package edu.udel.cis.vsl.sarl.preuniverse.common;

import java.util.Arrays;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
/**
 * 
 * @author Mohammad Alsulmi (malsulmi) 
 * 
 * The purpose of this benchmark is to evaluate append method in PreUniverse.
 * 
 * Here, we try to measure the time required for performing append() on two arrays.
 *
 */

public class AppendingBenchmark {
	public final static SymbolicUniverse universe = SARL.newIdealUniverse();
	public final static FactorySystem system = PreUniverses
			.newIdealFactorySystem();

	public final static SymbolicType integerType = universe.integerType();

	public final static ExpressionFactory expressionFactory = system
			.expressionFactory();

	public static SymbolicExpression array1, array2;

	public static void main(String[] args) {

		NumericExpression elementsArray[];
		int maxSize = (int) Math.pow(2, 13);
		int size;
		long startingTime, endingTime;
		double totalTime;

		// the case of appending one array into another
		System.out.println("Evaluating the time for appending one array into another");

		for (int i = 1; i <= maxSize; i = i * 2) {
			size = i;
			elementsArray = new NumericExpression[0];
			// creating the 1st array
			array1 = universe.array(integerType, Arrays.asList(elementsArray));
			
			elementsArray = new NumericExpression[size];
			for (int j = 0; j < size; j++) {
				elementsArray[j] = universe.integer(j);
			}
			// creating the 2nd array
			array2 = universe.array(integerType, Arrays.asList(elementsArray));
			// starting the time
			startingTime = System.nanoTime();
			// appending array2 into array1
			universe.append(array1, array2);
			// stopping the time
			endingTime = System.nanoTime();

			// total time calculation
			totalTime = ((double) (endingTime - startingTime)) / 1000000000.0;
			System.out.println("Total Time in seconds: " + totalTime
					+ " for size: " + size);

		}

		System.out.println("Testing of array creation using old array append");

		// the case of appending one element into one array

		for (int i = 1; i <= maxSize; i = i * 2) {
			size = i;
			elementsArray = new NumericExpression[0];
			NumericExpression a = universe.integer(100);
			// creating the array
			array1 = universe.array(integerType, Arrays.asList(elementsArray));
			// starting the time
			startingTime = System.nanoTime();
			for (int j = 0; j < size; j++) {
				// appending one element
				array1 = universe.append(array1, a);
			}
			// stopping the time
			endingTime = System.nanoTime();

			// total time calculation
			totalTime = ((double) (endingTime - startingTime)) / 1000000000.0;
			System.out.println("Total Time in seconds: " + totalTime
					+ " for size: " + size);

		}

	}

}
