package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

/**
 * Benchmark test which computes the polynomial (x+y)^N for a big integer N.
 * 
 * @author siegel
 * 
 */
public class BigPowerBenchmark {

	/** The exponent N */
	public final static int N = 250;

	public final static SymbolicUniverse universe = SARL.newIdealUniverse();

	public final static SymbolicType realType = universe.realType();

	public final static NumericSymbolicConstant x = (NumericSymbolicConstant) universe
			.symbolicConstant(universe.stringObject("x"), realType);

	public final static NumericSymbolicConstant y = (NumericSymbolicConstant) universe
			.symbolicConstant(universe.stringObject("y"), realType);

	public final static NumericExpression xpy = universe.add(x, y);

	/**
	 * Runs the test, prints the total time, takes no arguments.
	 * 
	 * @param args
	 *            ignored
	 */
	public static void main(String[] args) {
		long startTime = System.nanoTime(), stopTime;
		double totalTime;

		universe.power(xpy, N);
		stopTime = System.nanoTime();
		totalTime = ((double) (stopTime - startTime)) / 1000000000.0;
		System.out.println("Time (s): " + totalTime);
	}

}
