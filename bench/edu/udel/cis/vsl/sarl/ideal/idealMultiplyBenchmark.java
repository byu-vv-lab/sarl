package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

public class idealMultiplyBenchmark {
	public final static int N = 150;

	public final static SymbolicUniverse universe = SARL.newIdealUniverse();

	public final static SymbolicType realType = universe.realType();

	public final static NumericExpression x = (NumericSymbolicConstant) universe
			.symbolicConstant(universe.stringObject("x"), realType);
	
	public final static SymbolicExpression s = universe.add(universe.multiply(x, x), universe
			.multiply(universe.rational(2), x));
		
	public static SymbolicExpression result = (NumericSymbolicConstant) universe
			.symbolicConstant(universe.stringObject("result"), realType);
	
	
	public static void main(String[] args) {
		long startTime = System.nanoTime(), stopTime;
		double totalTime;
		result = universe.rational(1);
		for (int i = 0; i < N; i++) {
			result = universe.multiply((NumericExpression) s, (NumericExpression) result);
		}
		stopTime = System.nanoTime();
		totalTime = ((double) (stopTime - startTime)) / 1000000000.0;
		System.out.println("Time (s): " + totalTime);
		System.out.println(result);
	}
}
