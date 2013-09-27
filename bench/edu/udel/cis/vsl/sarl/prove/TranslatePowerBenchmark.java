package edu.udel.cis.vsl.sarl.prove;

import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;
import edu.udel.cis.vsl.sarl.prove.cvc.CVC3TheoremProver;

public class TranslatePowerBenchmark {

	public final static int N = 175;
	
	public final static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();

	public final static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);

	public final static SymbolicRealType realType = universe.realType();

	private static BooleanExpression booleanExprTrue = universe
			.trueExpression();
	
	public final static NumericSymbolicConstant a = (NumericSymbolicConstant) universe
			.symbolicConstant(universe.stringObject("a"), realType);

	public final static NumericSymbolicConstant b = (NumericSymbolicConstant) universe
			.symbolicConstant(universe.stringObject("b"), realType);

	public final static TheoremProverFactory proverFactory = Prove
			.newCVC3TheoremProverFactory(universe);

	public final static CVC3TheoremProver cvcProver = (CVC3TheoremProver) proverFactory
			.newProver(booleanExprTrue);

	public final static ValidityChecker vc = cvcProver.validityChecker();

	public static void main(String[] args) {

		long startTime = System.nanoTime(), stopTime;
		double totalTime;
		NumericExpression addExpression = universe.add(a, b);
		
		NumericExpression p = universe.power(addExpression, N);
		cvcProver.translate(p);
		stopTime = System.nanoTime();
		totalTime = ((double) (stopTime - startTime)) / 1000000000.0;
		System.out.println("Time (s): " + totalTime);
	}
}
