package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

/**
 * Benchmark test which computes the polynomial (x+y)^10 and then raises that to 25
 * for comparison with BigPower
 * 
 * @author cboucher
 * 
 */
public class IdealPowerToPowerBenchmark {

	public final static SymbolicUniverse universe = SARL.newIdealUniverse();

	public final static SymbolicType realType = universe.realType();

	public final static NumericSymbolicConstant x = (NumericSymbolicConstant) universe
			.symbolicConstant(universe.stringObject("x"), realType);

	public final static NumericSymbolicConstant y = (NumericSymbolicConstant) universe
			.symbolicConstant(universe.stringObject("y"), realType);

	public final static NumericExpression xpy = universe.add(x, y);
	
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

		result = universe.power(xpy, 10);
		result = universe.power((NumericExpression)result, 25);
		stopTime = System.nanoTime();
		totalTime = ((double) (stopTime - startTime)) / 1000000000.0;
		System.out.println("Time (s): " + totalTime);
	}
}
