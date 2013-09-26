package edu.udel.cis.vsl.sarl;

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

	public final static SymbolicType realType = universe.realType();
	public final static SymbolicArrayType realArray = universe.arrayType(realType);
	public static SymbolicArrayType unionArray;
	
	public final static ExpressionFactory expressionFactory = system.expressionFactory();
	
	public static SymbolicUnionType myUnion;
					
	/**
	 * Runs the benchmarks for unions: unionInjectBenchmark()
	 * 
	 * @param args
	 *            ignored
	 */
	public static void main(String[] args) {
		
		unionInjectBenchmark();
		
		unionTestBenchmark();
		
	}
	
	public static void unionInjectBenchmark(){
		
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
			//System.out.println("Time (s): " + totalTime + "  for size: " + ARRAYSIZE);
			
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
	
	public static void unionTestBenchmark(){
		
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
			
			// Perform currSize unionInject() operations to show scalability
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
			
			//System.out.println("Time (s): " + totalTime + "  for size: " + currSize + " unionTest() operations");			
		}
	}

}

