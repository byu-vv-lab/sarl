/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.universe.Universes;
/**
 * Benchmarks for general Expressions
 * @author schivi
 *
 */
public class GenenralExpressionBenchmark {
	/**
	 * benchmark for computing a simple large boolean expression
	 * -checking for a close to linear relationship as the amount of expressions increase
	 */
	public static void main(String[] args) {
		SymbolicUniverse sUniverse;
		SymbolicType booleanType;
		long start;
		long end;
		long mark;
		int numexpr;
		Collection<BooleanExpression> col1;

		numexpr = 1000;
		sUniverse = Universes.newIdealUniverse();
		booleanType = sUniverse.booleanType();
		BooleanExpression[] ExpressionList1 = {};
		col1= new ArrayList<BooleanExpression>(Arrays.asList(ExpressionList1));
		for(int i = 0; i < numexpr; i++){
			col1.add((BooleanExpression) sUniverse.symbolicConstant(sUniverse.stringObject(Integer.toString(i)), booleanType));
		}
		start = System.currentTimeMillis();
			BooleanExpression s1 = sUniverse.and(col1);
		end = System.currentTimeMillis();
		mark = end - start;
		System.out.println(mark);	
			}
}
