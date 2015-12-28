package edu.udel.cis.vsl.sarl.prove;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.udel.cis.vsl.sarl.IF.config.Configurations;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class TheoremProverBenchmark {

	public final static int N = 1000;
	public static List<String> benchmarkResults = new ArrayList<String>();

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
			.newMultiProverFactory(universe,
					Configurations.getDefaultConfiguration());

	public static TheoremProver cvcProver = proverFactory
			.newProver(booleanExprTrue);

	public final static ExpressionFactory expressionFactory = factorySystem
			.expressionFactory();

	public static void main(String[] args) {

		long startTime = System.nanoTime(), stopTime;
		int scaleN = 0;
		int scaleFactor = 50;
		while (scaleN < N) {
			scaleN += scaleFactor;
			System.out.println("Building constants...");
			List<NumericSymbolicConstant> expressions = new ArrayList<NumericSymbolicConstant>();
			for (int i = 0; i < scaleN; i++) {
				NumericSymbolicConstant x = (NumericSymbolicConstant) universe
						.symbolicConstant(universe.stringObject("x" + i),
								universe.integerType());
				expressions.add(x);
				// double percentDone = (i / N) * 100;
				// System.out.println(percentDone + "%");
			}
			System.out.println("Building context expression...");
			List<BooleanExpression> boolExpressions = new ArrayList<BooleanExpression>();
			for (int i = 1; i < expressions.size(); i++) {
				BooleanExpression xGreaterThanXminusOne = (BooleanExpression) expressionFactory
						.expression(SymbolicOperator.LESS_THAN,
								universe.booleanType(), expressions.get(i - 1),
								expressions.get(i));
				boolExpressions.add(xGreaterThanXminusOne);
				// double percentDone = (i / N) * 100;
				// System.out.println(percentDone + "%");;
			}

			BooleanExpression reallyBigExpression = universe
					.and(boolExpressions);

			System.out.println("Constructing a new prover with the context");
			long constructorStartTime = System.nanoTime();
			cvcProver = proverFactory.newProver(reallyBigExpression);
			long constructorEndTime = System.nanoTime();
			System.out.println("Done.");

			long totalConstructorTime = constructorEndTime
					- constructorStartTime;

			long totalQueryTime = 0;
			System.out.println("Querying CVC3...");
			for (int i = 1; i < expressions.size(); i++) {
				BooleanExpression lessThanPrevious = universe.lessThan(
						expressions.get(i - 1), expressions.get(i));

				long queryStartTime = System.nanoTime();
				cvcProver.valid(lessThanPrevious);
				long queryEndTime = System.nanoTime();

				// System.out.println(((i / N) * 100) + "%");
				totalQueryTime += (queryEndTime - queryStartTime);
			}

			stopTime = System.nanoTime();
			double totalTime = (double) (stopTime - startTime) / 1000000000.0;
			double constructorTime = (double) totalConstructorTime / 1000000000.0;
			double queryTime = (double) totalQueryTime / 1000000000.0;
			System.out.println("Time(s):");
			System.out.println("Total: " + totalTime + " seconds");
			System.out.println("Constructor: " + constructorTime + " seconds");
			System.out.println("CVC3 Queries: " + queryTime + " seconds");

			benchmarkResults.add(scaleN + "," + totalTime + ","
					+ constructorTime + "," + queryTime);
		}
		try {
			FileWriter f = new FileWriter(System.getProperty("user.home")
					+ "/benchmarkResults.csv");
			for (String result : benchmarkResults)
				f.append(result + "\n");
			f.flush();
			f.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
