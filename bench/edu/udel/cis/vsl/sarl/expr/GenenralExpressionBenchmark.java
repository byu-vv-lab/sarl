package edu.udel.cis.vsl.sarl.expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.universe.Universes;
public class GenenralExpressionBenchmark {

	
	private static SymbolicUniverse sUniverse;
	private static SymbolicType booleanType;
	static long start;
	static long end;
	static long mark;
	

	/**
	 * benchmark for computing a simple large boolean expression
	 * -checking for a close to'; linear relationship as the amount of expressions increase
	 */
	
	public static void main(String[] args) {

		
		sUniverse = Universes.newIdealUniverse();
		
		booleanType = sUniverse.booleanType();
				
		
		BooleanExpression[] ExpressionList1 = {};
		Collection<BooleanExpression> col1= new ArrayList<BooleanExpression>(Arrays.asList(ExpressionList1));
		for(int i = 0; i < 1000; i++){
	
			col1.add((BooleanExpression) sUniverse.symbolicConstant(sUniverse.stringObject(Integer.toString(i)), booleanType));
	
		}

		
		
		
		start = System.currentTimeMillis();
			BooleanExpression s1 = sUniverse.and(col1);

	
		end = System.currentTimeMillis();
		mark = end - start;
		System.out.println(mark);
		
		
				
			}
	
	
}
