package edu.udel.cis.vsl.sarl.preuniverse.common;

import java.util.Arrays;
import java.util.LinkedList;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;

public class Appending {
	public final static SymbolicUniverse universe = SARL.newIdealUniverse();
	public final static FactorySystem system = PreUniverses
			.newIdealFactorySystem();

	public final static SymbolicType integerType = universe.integerType();

	public final static ExpressionFactory expressionFactory = system
			.expressionFactory();

	public static SymbolicExpression array1, array2;

	public static void main(String[] args) {

		NumericExpression elementsArray[];
		LinkedList<NumericExpression> elementsList;
		int maxSize = (int) Math.pow(2, 15);
		int size;
		long startingTime, endingTime;
		double totalTime;
				
		// the case of new append
		System.out.println("Testing of array creation using new array append");
		
		for (int i = 1; i <= maxSize; i = i * 2) {
			size = i;
			// starting the time
			elementsList = new LinkedList<>();

			elementsArray = new NumericExpression[0];
			//elementsArray[0] = universe.integer(7);
			//elementsArray[1] = universe.integer(7);
			array1 = universe.array(integerType, Arrays.asList(elementsArray));
			//elementsList = new LinkedList<>();
			elementsArray = new NumericExpression[size];
			for (int j = 0; j < size; j++) {
				elementsArray[j] = universe.integer(j);
			}
			
			array2 = universe.array(integerType, Arrays.asList(elementsArray));
			startingTime = System.nanoTime();
			universe.append(array1, array2);
			// stopping the time
			endingTime = System.nanoTime();

			// total time calculation
			totalTime = ((double) (endingTime - startingTime)) / 1000000000.0;
			System.out.println("Total Time in seconds: " + totalTime
					+ " for size: " + size);

		}
		
/*
		System.out.println("Testing of array creation using old array append");
		
		// the case of append to an empty array

		for (int i = 1; i <= maxSize; i = i * 2) {
			size = i;
			// starting the time
			elementsArray = new NumericExpression[0];
			NumericExpression a = universe.integer(100);
			array1 = universe.array(integerType, Arrays.asList(elementsArray));
			startingTime = System.nanoTime();
			for (int j = 0; j < size; j++) {
				array1 = universe.append(array1, a);
			}
			// stopping the time
			endingTime = System.nanoTime();

			// total time calculation
			totalTime = ((double) (endingTime - startingTime)) / 1000000000.0;
			System.out.println("Total Time in seconds: " + totalTime
					+ " for size: " + size);

		}


*/
	}

}
