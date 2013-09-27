package edu.udel.cis.vsl.sarl.expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.universe.Universes;

public class ExpressionBenchmark {
	private SymbolicUniverse sUniverse;
	private BooleanSymbolicConstant x,y;
	private SymbolicType booleanType;
	private StringObject Xobj,Yobj; // "X" , "Y"
	long start;
	long end;
	long mark;
	
	//benchmark for large boolean operation on large boolean expressions
	//does not include time to construct individual expressions
	@Test
	public void mark1(){

		
		sUniverse = Universes.newIdealUniverse();
		
		
		Xobj = sUniverse.stringObject("X");
		Yobj = sUniverse.stringObject("Y");
		booleanType = sUniverse.booleanType();
		 x = (BooleanSymbolicConstant) sUniverse.symbolicConstant(Xobj,booleanType );
		 y = (BooleanSymbolicConstant) sUniverse.symbolicConstant(Yobj,booleanType );
			
	
		
		BooleanExpression[] ExpressionList1 = {};
		Collection<BooleanExpression> col1= new ArrayList<BooleanExpression>(Arrays.asList(ExpressionList1));
		BooleanExpression[] ExpressionList2 = {};
		Collection<BooleanExpression> col2= new ArrayList<BooleanExpression>(Arrays.asList(ExpressionList2));
		for(int i = 0; i < 200; i++){
	
			col1.add((BooleanExpression) sUniverse.symbolicConstant(sUniverse.stringObject(Integer.toString(i)), booleanType));
	
		}
		for(int i = 0; i < 200; i++){
			col2.add((BooleanExpression) sUniverse.symbolicConstant(sUniverse.stringObject(Integer.toString(-i)), booleanType));
			
		}
		
		
		
		
		start = System.currentTimeMillis();
		
		BooleanExpression s1 = sUniverse.and(col1);
		BooleanExpression s2 = sUniverse.and(col2);
		BooleanExpression s3 = sUniverse.or(s1,s2);
		
		end = System.currentTimeMillis();
		mark = end - start;
		System.out.println(mark);
		
		//After ten runs with a total of 400 expressions.. average of 942ms
		
				
			}
}
