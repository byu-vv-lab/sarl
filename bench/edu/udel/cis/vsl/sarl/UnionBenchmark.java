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
	
	public final static ExpressionFactory expressionFactory = system.expressionFactory();
	
	public static SymbolicUnionType myUnion;
					
	/**
	 * Runs the test, prints the total time, takes no arguments.
	 * 
	 * @param args
	 *            ignored
	 */
	public static void main(String[] args) {
		
		// Maximum size to benchmark, starts at 16 and doubles until this ceiling
		int MAXSIZE = (int)Math.pow(2,20); // 1M
		
		for(int currSize = 16; currSize <= MAXSIZE; currSize *= 2){
		
			int ARRAYSIZE = currSize;
			LinkedList<SymbolicType> symbolicTypeList = new LinkedList<SymbolicType>();
			for(int i = 0; i < ARRAYSIZE; ++i){
				symbolicTypeList.push(universe.arrayType(realType, universe.integer(i)));
			}
			
			myUnion = universe.unionType(universe.stringObject("myUnion"), symbolicTypeList);
		
			long startTime = System.nanoTime(), stopTime;
			double totalTime;
			
			for(int i = 0; i < ARRAYSIZE; ++i){
					SymbolicCompleteArrayType tempArray = universe.arrayType(realType, universe.integer(i));
					SymbolicExpression tempExpr = expressionFactory.expression(SymbolicOperator.UNION_INJECT, realArray, tempArray);
					@SuppressWarnings("unused")
					SymbolicExpression tempInject = universe.unionInject(myUnion, universe.intObject(i), tempExpr);
			}
			
			stopTime = System.nanoTime();
			totalTime = ((double) (stopTime - startTime)) / 1000000000.0;
			System.out.println("Time (s): " + totalTime + "  for size: " + ARRAYSIZE);
		}
	}

}

