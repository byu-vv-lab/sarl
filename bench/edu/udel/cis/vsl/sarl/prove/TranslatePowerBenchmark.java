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

public class TranslatePowerBenchmark {

	public final static int N = 50;
	
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
		
		List<NumericSymbolicConstant> expressions = new ArrayList<NumericSymbolicConstant>();
		for(int i = 0; i < N; i++) {
			NumericSymbolicConstant x = (NumericSymbolicConstant)universe.symbolicConstant(universe.stringObject("x" + i), universe.integerType());
			expressions.add(x);
			System.out.println(i);
		}
		
		List<BooleanExpression> boolExpressions = new ArrayList<BooleanExpression>();
		for(int i = 1; i < expressions.size(); i ++) {
				BooleanExpression xGreaterThanXminusOne = (BooleanExpression)
						expressionFactory.expression(SymbolicOperator.LESS_THAN,
						universe.booleanType(),expressions.get(i-1), expressions.get(i));
				boolExpressions.add(xGreaterThanXminusOne);
			System.out.println(i);
		}
		long subtractMe = System.nanoTime();
		
		BooleanExpression reallyBigExpression = universe.and(boolExpressions);
		
		cvcProver = (CVC3TheoremProver) proverFactory.newProver(reallyBigExpression);
		long fromMe = System.nanoTime();
		
		long buildingUniverseAndProver = fromMe - subtractMe;
		
		for(int i = 1; i < expressions.size(); i++) {
				BooleanExpression lessThanPrevious = universe.lessThan(expressions.get(i-1),
						expressions.get(i));
				cvcProver.valid(lessThanPrevious);
				System.out.println(i);
		}

		stopTime = System.nanoTime();
		totalTime = ((double) (stopTime - startTime - buildingUniverseAndProver)) / 1000000000.0;
		System.out.println("Time (s): " + totalTime);
		System.out.println("Building Universe and Prover Time: " + ((double)buildingUniverseAndProver)/1000000000.0);
	}
}
