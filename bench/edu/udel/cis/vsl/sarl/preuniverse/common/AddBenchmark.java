package edu.udel.cis.vsl.sarl.preuniverse.common;

import java.util.LinkedList;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;

public class AddBenchmark {
	
	public final static SymbolicUniverse universe = SARL.newIdealUniverse();
	public final static FactorySystem system = PreUniverses.newIdealFactorySystem();
	public final static NumericExpressionFactory numericExpressionFactory = system.numericFactory();
	
	public static void main(String[] args) {
		
		addBenchmark();
		
	}
	
	public static void addBenchmark(){
		
		final int SIZE = 1000000;
		final int ITERATIONS = 10;
		final boolean PRINT = true;
		LinkedList<NumericExpression> numericExpressionList;
		
		
		numericExpressionList = new LinkedList<>();
		for(int i = 0; i < SIZE; ++i){
			numericExpressionList.addLast(universe.integer(i));
		}
		
		double totalTime = 0.0f;
		
		for(int i = 0; i < ITERATIONS; ++i){
			long startTime = System.nanoTime(), stopTime;
			// TIMING STARTS HERE
		
			universe.add(numericExpressionList);
		
			// TIMING ENDS HERE
		
			stopTime = System.nanoTime();
			totalTime += ((double) (stopTime - startTime)) / 1000000000.0;
		}
		
		if(PRINT)
			System.out.println("Average time (s): " + totalTime/ITERATIONS + " for iters: " + ITERATIONS + "  for size: " + SIZE);
	}
}
