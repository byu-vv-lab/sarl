package edu.udel.cis.vsl.sarl.prove;

import java.util.ArrayList;
import java.util.List;

import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;
import edu.udel.cis.vsl.sarl.prove.cvc.CVC3TheoremProver;

public class TheoremProverBenchmarkEquality {

	public final static int N = 1000;

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

	public static CVC3TheoremProver cvcProver = (CVC3TheoremProver) proverFactory
			.newProver(booleanExprTrue);

	public final static ExpressionFactory expressionFactory = factorySystem.expressionFactory();

	public final static ValidityChecker vc = cvcProver.validityChecker();

	public static void main(String[] args) {

		long startTime = System.nanoTime(), stopTime;
		double totalTime;

		System.out.println("Building constants...");
		List<NumericSymbolicConstant> expressions = new ArrayList<NumericSymbolicConstant>();
		for(int i = 0; i < N; i++) {
			NumericSymbolicConstant x = (NumericSymbolicConstant)universe
					.symbolicConstant(universe.stringObject("x" + i), universe.integerType());
			expressions.add(x);
//			double percentDone = (i / N) * 100;
//			System.out.println(percentDone + "%");
		}
		System.out.println("Building context expression...");
		List<BooleanExpression> boolExpressions = new ArrayList<BooleanExpression>();
		for(int i = 1; i < expressions.size(); i++) {
			BooleanExpression xEqualsx = (BooleanExpression)
					expressionFactory.expression(SymbolicOperator.EQUALS,
							universe.booleanType(),expressions.get(i), expressions.get(i));
			boolExpressions.add(xEqualsx);
//			double percentDone = (i / N) * 100;
//			System.out.println(percentDone + "%");;
		}

		BooleanExpression reallyBigExpression = universe.and(boolExpressions);

		System.out.println("Constructing a new prover with the context");
		long constructorStartTime = System.nanoTime();
		cvcProver = (CVC3TheoremProver) proverFactory.newProver(reallyBigExpression);
		long constructorEndTime = System.nanoTime();
		System.out.println("Done.");

		long totalConstructorTime = constructorEndTime - constructorStartTime;

		long totalQueryTime = 0;
		System.out.println("Querying CVC3...");
		for(int i = 1; i < expressions.size(); i++) {
			BooleanExpression EqualsPrevious = universe.equals(expressions.get(i),expressions.get(i - 1));
					

			long queryStartTime = System.nanoTime();
			cvcProver.valid(EqualsPrevious);
			long queryEndTime = System.nanoTime();
			
//			System.out.println(((i / N) * 100) + "%");
			totalQueryTime += (queryEndTime - queryStartTime);
		}

		stopTime = System.nanoTime();
		System.out.println("Time(s):");
		System.out.println("Total: " + (((double) (stopTime - startTime)) / 1000000000.0) + " seconds");
		System.out.println("Constructor: " + (((double) totalConstructorTime) / 1000000000.0) + " seconds");
		System.out.println("CVC3 Queries: " + (((double) totalQueryTime) / 1000000000.0) + " seconds");
	}
}
