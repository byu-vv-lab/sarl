package edu.udel.cis.vsl.sarl;

import java.util.Arrays;
import java.util.LinkedList;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;

/**
 * Benchmark test which runs unionInject() on unions of scaling sizes
 * 
 * @author jdimarco
 * 
 */
public class UnionBenchmark {

	public final static SymbolicUniverse universe = SARL.newIdealUniverse();
	public final static FactorySystem system = PreUniverses.newIdealFactorySystem();

	public final static SymbolicType integerType = universe.integerType();
	public final static SymbolicType realType = universe.realType();
	public final static SymbolicType booleanType = universe.booleanType();
	public final static SymbolicArrayType realArray = universe.arrayType(realType);
	public static SymbolicArrayType unionArray;
	
	public final static ExpressionFactory expressionFactory = system.expressionFactory();
	
	public static SymbolicUnionType myUnion;
	
	public final static boolean PRINT = true;
					
	/**
	 * Runs the benchmarks for unions: unionInjectBenchmark(), unionTestBenchmark()
	 * 
	 * @param args
	 *            ignored
	 */
	public static void main(String[] args) {
		
		unionInjectBenchmark();
		
		unionTestBenchmark();
		
		unionArrayBenchmark();
		
	}
	
	/**
	 * Tests the runtime of creating an array of type UNION.  The size of the
	 * unions are, small (4), medium (10), and large (25)
	 */
	public static void unionArrayBenchmark(){
		
		if(PRINT)
			System.out.println("Running unionArrayBenchmark...");
		// Maximum size to benchmark, starts at 16 and doubles until this ceiling
		int MAXSIZE = (int)Math.pow(2,19); // 512k
						
		for(int currSize = 16; currSize <= MAXSIZE; currSize *= 2){
						
			LinkedList<SymbolicType> symbolicTypeList = new LinkedList<SymbolicType>();
			for(int i = 0; i < currSize; ++i){
				symbolicTypeList.push(universe.arrayType(realType, universe.integer(i)));
			}
							
			myUnion = universe.unionType(universe.stringObject("myUnion"), symbolicTypeList);
				
			long startTime = System.nanoTime(), stopTime;
			double totalTime;
					
			// Perform currSize unionInject() operations to show scalability
			LinkedList<SymbolicExpression> unionList = new LinkedList<>();
			for(int i = 0; i < currSize; ++i){
					SymbolicCompleteArrayType tempArray = universe.arrayType(realType, universe.integer(i));
					SymbolicExpression tempExpr = expressionFactory.expression(SymbolicOperator.UNION_INJECT, realArray, tempArray);
					@SuppressWarnings("unused")
					SymbolicExpression tempInject = universe.unionInject(myUnion, universe.intObject(i), tempExpr);
					unionList.push(tempInject);
			}
			
			@SuppressWarnings("unused")
			SymbolicExpression arrayOfUnion = universe.array(myUnion, unionList);
					
			stopTime = System.nanoTime();
			totalTime = ((double) (stopTime - startTime)) / 1000000000.0;
			if(PRINT)
				System.out.println("Time (s): " + totalTime + "  for size: " + currSize);
					
		}
	}
	
	/**
	 * Runs many unionInject() operations on large scale unions (2^20) and times them
	 * as well as a single unionInject() operation
	 */
	public static void unionInjectBenchmark(){
		
		if(PRINT)
			System.out.println("Running unionInjectBenchmark()...");
		// Maximum size to benchmark, starts at 16 and doubles until this ceiling
		int MAXSIZE = (int)Math.pow(2,20); // 1M
				
		for(int currSize = 16; currSize <= MAXSIZE; currSize *= 2){
				
			LinkedList<SymbolicType> symbolicTypeList = new LinkedList<SymbolicType>();
			for(int i = 0; i < currSize; ++i){
				symbolicTypeList.push(universe.arrayType(realType, universe.integer(i)));
			}
					
			myUnion = universe.unionType(universe.stringObject("myUnion"), symbolicTypeList);
		
			long startTime = System.nanoTime(), stopTime;
			double totalTime;
			
			// Perform currSize unionInject() operations to show scalability
			for(int i = 0; i < currSize; ++i){
					SymbolicCompleteArrayType tempArray = universe.arrayType(realType, universe.integer(i));
					SymbolicExpression tempExpr = expressionFactory.expression(SymbolicOperator.UNION_INJECT, realArray, tempArray);
					@SuppressWarnings("unused")
					SymbolicExpression tempInject = universe.unionInject(myUnion, universe.intObject(i), tempExpr);
			}
			
			stopTime = System.nanoTime();
			totalTime = ((double) (stopTime - startTime)) / 1000000000.0;
			if(PRINT)
				System.out.println("Time (s): " + totalTime + "  for size: " + currSize);
			
			startTime = System.nanoTime();
			
			// Perform 1 unionInject() operation
			SymbolicCompleteArrayType tempArray = universe.arrayType(realType, universe.integer(currSize-1));
			SymbolicExpression tempExpr = expressionFactory.expression(SymbolicOperator.UNION_INJECT, realArray, tempArray);
			@SuppressWarnings("unused")
			SymbolicExpression tempInject = universe.unionInject(myUnion, universe.intObject(currSize-1), tempExpr);
			
			stopTime = System.nanoTime();
			totalTime = ((double) (stopTime - startTime)) / 1000000000.0;
		}
	}
	
	/**
	 * Creates large sized (2^20) unions and runs unionTest() on each type within the union
	 */
	public static void unionTestBenchmark(){
		
		if(PRINT)
			System.out.println("Running unionTestBenchmark()...");
		// Maximum size to benchmark, starts at 16 and doubles until this ceiling
		int MAXSIZE = (int)Math.pow(2,20); // 1M
				
		for(int currSize = 16; currSize <= MAXSIZE; currSize *= 2){
				
			LinkedList<SymbolicType> symbolicTypeList = new LinkedList<SymbolicType>();
			for(int i = 0; i < currSize; ++i){
				symbolicTypeList.push(universe.arrayType(realType, universe.integer(i)));
			}
					
			myUnion = universe.unionType(universe.stringObject("myUnion"), symbolicTypeList);
		
			long startTime, stopTime;
			double totalTime = 0.0;
			
			// Create each union, but only time the unionTest() time
			for(int i = 0; i < currSize; ++i){
					SymbolicCompleteArrayType tempArray = universe.arrayType(realType, universe.integer(i));
					SymbolicExpression tempExpr = expressionFactory.expression(SymbolicOperator.UNION_INJECT, realArray, tempArray);
					@SuppressWarnings("unused")
					SymbolicExpression tempInject = universe.unionInject(myUnion, universe.intObject(i), tempExpr);
					startTime = System.nanoTime();
					universe.unionTest(universe.intObject(i), tempExpr);
					stopTime = System.nanoTime();
					totalTime += (((double)(stopTime - startTime)) / 100000000.0);
			}
			
			if(PRINT)
				System.out.println("Time (s): " + totalTime + "  for size: " + currSize + " unionTest() operations");			
		}
	}

}

