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
	 * Singular bench for a large Expression of Or Expressions
	 * Using Simplification it should be fast than previous Or logic
	 * 
	 *
	 */

	public class LargeOrSimplificationBench {
		/**
		 * benchmark for large boolean operation on large boolean expressions
		 * does not include time to construct individual expressions
		 * 
		 * @param args
		 *            ignored
		 */
		
		public static void main(String[] args) {
			SymbolicUniverse sUniverse;
			SymbolicType booleanType;
			long start;
			long end;
			long mark;
			int numexpr;
			BooleanExpression s1, s3, d1, d2;
			Collection<BooleanExpression> col1;
		numexpr = 7000;
			sUniverse = Universes.newIdealUniverse();
			booleanType = sUniverse.booleanType();
			BooleanExpression[] ExpressionList1 = {};
			
			BooleanExpression tester5 = (sUniverse.not((BooleanExpression) sUniverse.symbolicConstant(sUniverse.stringObject(Integer.toString(-1)), booleanType)));
			//part1 -- similar expressions are at the end of the expression
			col1= new ArrayList<BooleanExpression>(Arrays.asList(ExpressionList1));
			for(int i = 0; i < numexpr; i++){
				col1.add((BooleanExpression) sUniverse.symbolicConstant(sUniverse.stringObject(Integer.toString(i)), booleanType));
			}
			col1.add(tester5);
			 s1 = sUniverse.or(col1);
			start = System.currentTimeMillis();
			 s3 = sUniverse.or(sUniverse.not(tester5),s1);
			end = System.currentTimeMillis();
			mark = end - start;
			System.out.println(mark);
			System.out.println(s3);
			
			//part2 --  expressions are at the beginning of the collections
			col1= new ArrayList<BooleanExpression>(Arrays.asList(ExpressionList1));
			col1.add(tester5);

			for(int i = 0; i < numexpr; i++){
				col1.add((BooleanExpression) sUniverse.symbolicConstant(sUniverse.stringObject(Integer.toString(i)), booleanType));
			}
			d1 = sUniverse.or(col1);
			start = System.currentTimeMillis();
			d2= sUniverse.or(sUniverse.not(tester5),d1);

			end = System.currentTimeMillis();
			mark = end - start;
			System.out.println(mark);
			System.out.println(d2);
			
				}
	}
