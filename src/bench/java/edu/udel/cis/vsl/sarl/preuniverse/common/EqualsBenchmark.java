package edu.udel.cis.vsl.sarl.preuniverse.common;

import java.util.LinkedList;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;

public class EqualsBenchmark {

	public final static SymbolicUniverse universe = SARL.newIdealUniverse();
	public final static FactorySystem system = PreUniverses.newIdealFactorySystem();
	
	public final static ExpressionFactory expressionFactory = system.expressionFactory();
	
	public static void main(String[] args) {
		
		final int NUM = 22;
		
		long startingTime, endingTime;
		double totalTime;
		
		SymbolicExpression five = universe.integer(5);
		LinkedList<SymbolicObject> symbolicExpressionList1 = new LinkedList<>();
		LinkedList<SymbolicObject> symbolicExpressionList2 = new LinkedList<>();
		for(int iter = 10; iter < NUM; ++iter){
			for(int i = 0; i < Math.pow(2, iter); ++i){
				symbolicExpressionList1.push(five);
				symbolicExpressionList2.push(five);
			}
			
			
			SymbolicExpression symbolicExpression1 = expressionFactory.expression(SymbolicOperator.EQUALS, universe.integerType(), symbolicExpressionList1);
			SymbolicExpression symbolicExpression2 = expressionFactory.expression(SymbolicOperator.EQUALS, universe.integerType(), symbolicExpressionList2);
			
			startingTime = System.nanoTime();
			
			symbolicExpression1.equals(symbolicExpression2);
			
			endingTime = System.nanoTime();
			totalTime = ((double) (endingTime - startingTime)) / 1000000000.0;
			System.out.println("Time for equals(): " + totalTime + "  , size: " + Math.pow(2,iter));
			
		}
		
	}
	
}
