package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

public class IdealDivideBenchmark {
	public final static int N = 50;
	
	public final static SymbolicUniverse universe = SARL.newIdealUniverse();

	public final static SymbolicType realType = universe.realType();

	public final static NumericSymbolicConstant x = (NumericSymbolicConstant) universe
			.symbolicConstant(universe.stringObject("x"), realType);

	public final static NumericSymbolicConstant y = (NumericSymbolicConstant) universe
			.symbolicConstant(universe.stringObject("y"), realType);
	
	public final static SymbolicExpression s = universe.add(x, y);

	public static NumericExpression xpy = universe.power(universe.add(x, y), N);
	
	public static SymbolicExpression result = (NumericSymbolicConstant) universe
			.symbolicConstant(universe.stringObject("result"), realType);
	
	
	public static void main(String[] args) {
		long startTime = System.nanoTime(), stopTime;
		double totalTime;
		//universe.power(xpy, N);
		//result = universe.add(x, y);
		for (int i = 0; i < N; i++) {
			xpy = universe.divide((NumericExpression) xpy, (NumericExpression) s);
		}
		stopTime = System.nanoTime();
		totalTime = ((double) (stopTime - startTime)) / 1000000000.0;
		System.out.println("Time (s): " + totalTime);
		System.out.println(xpy);
	}
}
