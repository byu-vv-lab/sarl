package edu.udel.cis.vsl.sarl.object;

import java.util.Random;

import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.number.real.RealNumberFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;

/**
 * 
 * @author justin
 * Benchmarks the ObjectComparator. Runs through an array and randomly compares elements
 */
public class ComparatorBenchmark {
	
	static NumberObject[] numberArray;
	static NumberFactory numberFactory;
	static ObjectFactory objectFactory;
	static int arrayLength = 10000;
	static int numberComparisons = 10000;
	
	public static void main(String args[])
	{
		
		long[] timeArray = new long[20];
		
		for (int j=0; j<20; j++) {
			
			//initialize some stuff
			numberFactory = new RealNumberFactory();
			objectFactory = Objects.newObjectFactory(numberFactory);
			Random generator = new Random();
			numberArray = new NumberObject[arrayLength];
	
			//create the array
			for (int i=0; i<arrayLength; i++) {
				numberArray[i] = objectFactory.numberObject(numberFactory.integer(Integer.toString(generator.nextInt(10000))));
			}
			
			long startTime = System.currentTimeMillis();
					
			//Run the comparator a bunch of times
			for (int i=0; i<numberComparisons; i++) {
				int index1 = generator.nextInt(arrayLength-1);
				int index2 = generator.nextInt(arrayLength-1);
				numberArray[index1].compareTo(numberArray[index2]);
			}
			
			long endTime = System.currentTimeMillis();
			timeArray[j] = endTime-startTime; 
					
		}
		
		//print out the array
		for (int i=0; i<20; i++) {
			System.out.println(timeArray[i]);
		}
		
		//calculate average
		long sum = 0;
		for(int j=0; j<20; j++) {
			sum+= timeArray[j];
		}
		long average = sum/20;
		
		System.out.println("Average is: " + average);
			
	}

}
