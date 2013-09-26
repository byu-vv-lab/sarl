package edu.udel.cis.vsl.sarl;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

public class IntegerBenchmark {
	static FactorySystem system = PreUniverses.newIdealFactorySystem();
	private static PreUniverse universe = PreUniverses.newPreUniverse(system);
	
	static SymbolicConstant index = universe.symbolicConstant(
			universe.stringObject("name"), universe.integerType());
	static NumericExpression low = universe.integer(999);
	static NumericExpression high = universe.integer(2000);
	static BooleanExpression trueExp = universe.bool(true);

	public static void main(String[] args) {
		long startTime = System.nanoTime(), stopTime;
		double totalTime;
		
		// This is a benchmark for the 'forallInt()' method
		System.out.println("Benchmark for 'forallInt()'");
		universe.forallInt((NumericSymbolicConstant)index, 
				low, high, trueExp);
		stopTime = System.nanoTime();
		totalTime = ((double) (stopTime - startTime)) / 1000000000.0;
		System.out.println("Time (s): " + totalTime);
		
		// This is a benchmark for the 'existsInt()' method
		System.out.println("\nBenchmark for 'existsInt()");
		universe.existsInt((NumericSymbolicConstant)index,
				low, high, trueExp);
		stopTime = System.nanoTime();
		totalTime = ((double) (stopTime - startTime)) / 1000000000.0;
		System.out.println("Time (s): " + totalTime);
	}

}
