package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
/**
 * Benchmark test which adds x to itself n times (x*n)
 * 
 * @author aepstein
 * 
 */
public class IdealAddBenchmark {
	
	/** The number of times to add X */
	public final static int N = 100000;

	public final static SymbolicUniverse universe = SARL.newIdealUniverse();

	public final static SymbolicType realType = universe.realType();

	public final static SymbolicExpression x = (NumericSymbolicConstant) universe
			.symbolicConstant(universe.stringObject("x"), realType);
	
	public static SymbolicExpression result = (NumericSymbolicConstant) universe
			.symbolicConstant(universe.stringObject("result"), realType);
	
	
	/**
	 * Runs the test, prints the total time, takes no arguments.
	 * 
	 * @param args
	 *            ignored
	 */
	public static void main(String[] args) {
		long startTime = System.nanoTime(), stopTime;
		double totalTime;
		
		result = x;
		for (int i = 0; i < N; i++) {
			result = universe.add((NumericExpression) x, (NumericExpression) result);
		}
		stopTime = System.nanoTime();
		totalTime = ((double) (stopTime - startTime)) / 1000000000.0;
		System.out.println("Time (s): " + totalTime);
		System.out.println(result);
	}

}
