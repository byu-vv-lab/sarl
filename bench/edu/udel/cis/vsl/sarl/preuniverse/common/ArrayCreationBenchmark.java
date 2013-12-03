package edu.udel.cis.vsl.sarl.preuniverse.common;

import java.util.LinkedList;

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
 * @author malsulmi
 * 
 * The purpose of this benchmark is to evaluate the performance of creating arrays and appending
 * 
 * Here, we try to measure the time required for creating arrays by either appending or regular creation
 *
 */

public class ArrayCreationBenchmark {

	public final static SymbolicUniverse universe = SARL.newIdealUniverse();
	public final static FactorySystem system = PreUniverses
			.newIdealFactorySystem();

	public final static SymbolicType integerType = universe.integerType();

	public final static ExpressionFactory expressionFactory = system
			.expressionFactory();

	public static SymbolicExpression array;

	public static void main(String[] args) {
		LinkedList<NumericExpression> elementsList;
		int maxSize = (int) Math.pow(2, 20);
		int size;
		long startingTime, endingTime;
		double totalTime;
		System.out.println("Testing of array creation using array method");
		// the case of array creation
		
		for (int i = 1; i <= maxSize; i = i * 2) {
			size = i;
			// starting the time
			
			elementsList = new LinkedList<NumericExpression>();
			for (int j = 0; j < size; j++) {
				elementsList.add(universe.integer(j));
			}
			startingTime = System.nanoTime();
			array = universe.array(integerType, (elementsList));
			endingTime = System.nanoTime();

			// total time calculation
			totalTime = ((double) (endingTime - startingTime)) / 1000000000.0;
			System.out.println("Total Time in seconds: " + totalTime
					+ " for size: " + size);

		}
		
		System.out.println("Testing of array creation using append method");

		// the case of append to an empty array
		SymbolicExpression element = universe.integer(1000);
		for (int i = 1; i <= maxSize; i = i * 2) {
			size = i;
			// starting the time
			
			elementsList = new LinkedList<>();
			startingTime = System.nanoTime();
			array = universe.array(integerType, elementsList);

			for (int j = 0; j < size; j++) {
				array = universe.append(array, element);
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
