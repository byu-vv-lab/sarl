package edu.udel.cis.vsl.sarl.expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.universe.Universes;

public class LargeExpressionBenchesWithCanonic {

	public static void main(String[] args) {
		SymbolicUniverse sUniverse;
		SymbolicType booleanType;
		long start;
		long end;
		long mark;
		int numexpr;
		ObjectFactory of;
		
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		
		
		of = system.objectFactory();
		
		
		Collection<BooleanExpression> col1;
		Collection<BooleanExpression> col2;
		
		numexpr = 700;
		sUniverse = Universes.newIdealUniverse();
		booleanType = sUniverse.booleanType();
		BooleanExpression[] ExpressionList1 = {};
		col1= new ArrayList<BooleanExpression>(Arrays.asList(ExpressionList1));
		BooleanExpression[] ExpressionList2 = {};
		col2= new ArrayList<BooleanExpression>(Arrays.asList(ExpressionList2));
		for(int i = 0; i < numexpr; i++){
			col1.add(of.canonic((BooleanExpression) sUniverse.symbolicConstant(sUniverse.stringObject(Integer.toString(i)), booleanType)));
		}
		for(int i = 0; i < numexpr; i++){
			col2.add(of.canonic((BooleanExpression) sUniverse.symbolicConstant(sUniverse.stringObject(Integer.toString(-i)), booleanType)));
		}
		start = System.currentTimeMillis();
			BooleanExpression s1 = sUniverse.and(col1);
			BooleanExpression s2 = sUniverse.and(col2);
			@SuppressWarnings("unused")
			BooleanExpression s3 = sUniverse.or(s1,s2);
		end = System.currentTimeMillis();
		mark = end - start;
		System.out.println(mark);
			}
}
