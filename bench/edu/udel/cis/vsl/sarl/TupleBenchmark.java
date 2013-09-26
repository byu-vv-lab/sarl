package edu.udel.cis.vsl.sarl;

import java.util.Arrays;
import java.util.LinkedList;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
/**
 * 
 * @author Mohammad Alsulmi (malsulmi)
 * 
 * The main purpose of the benchmark is to show which is better to be used in creating tuples
 * the linked lists or the arrays?
 * 
 * Here, we measure the time for creating tuples from both linked list and array
 * 
 *
 */
public class TupleBenchmark {
	
	public final static SymbolicUniverse universe = SARL.newIdealUniverse();
	public final static FactorySystem system = PreUniverses.newIdealFactorySystem();

	public final static SymbolicType integerType = universe.integerType();
	
	public final static ExpressionFactory expressionFactory = system.expressionFactory();
	
	public static SymbolicTupleType tupleType;
	
	public static SymbolicExpression tuple;
	


	public static void main(String[] args) {
	
		LinkedList<SymbolicType> members = null;			// list of types
		LinkedList<SymbolicExpression> elements = null;		// list of elements to be added to the tuple
		SymbolicType membersArray[];						// array of types
		NumericExpression elementsArray[];					// array of elements to be added to the tuple
		int size;
		double totalTime;
		
		int maxSize =(int) Math.pow(2,20);
		System.out.println("Testing of tuple creation using linked lists");
		for(int i = 1; i<=maxSize; i = i*2){
			members = new LinkedList<>();
			elements = new LinkedList<>();
			size = i;
			long startTime, stopTime;
			// we start measuring time for creating tuples from linked list
			startTime = System.nanoTime();
			
			for(int j = 0;j < size ; j++){
				members.add(integerType);
				elements.add(universe.integer(j));
			}
			tupleType = universe.tupleType(universe.stringObject("type1"), members);
			tuple = universe.tuple(tupleType, elements);
			// stopping time
			stopTime = System.nanoTime();
			// total time calculation
			totalTime =((double) (stopTime - startTime))/1000000000.0;
			
			System.out.println("Total Time in seconds: " + totalTime + " for size: "+size);
			
		}
		
		System.out.println("Testing of tuple creation using arrays");
		
		for(int i = 1; i<=maxSize; i = i*2){
						
			size = i;
			membersArray = new SymbolicType[size];
			elementsArray = new NumericExpression[size];
			long startTime, stopTime;

			// we start measuring time for creating tuples from arrays 

			startTime = System.nanoTime();
			
			for(int j = 0;j < size ; j++){
				membersArray[j] = integerType;
				elementsArray[j] = universe.integer(j);
			}
			tupleType = universe.tupleType(universe.stringObject("type1"), Arrays.asList(membersArray));
			tuple = universe.tuple(tupleType, Arrays.asList(elementsArray));
			// stopping time
			stopTime = System.nanoTime();

			// total time calculation
			totalTime =((double) (stopTime - startTime))/1000000000.0;
			
			System.out.println("Total Time in seconds: " + totalTime + " for size: "+size);
			
		}


		
	}

}
