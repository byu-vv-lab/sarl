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
package edu.udel.cis.vsl.sarl.IF;

import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.universe.IF.ExtendedUniverse;

/**
 * A simplifier is an object for simplifying symbolic expressions. It is created
 * with a given "assumption", which is a boolean-valued symbolic expression. The
 * assumption itself can be simplified; the simplified version of the assumption
 * can then be obtained by the method newAssumption. Any other symbolic
 * expression can be simplified (subject to the assumption) using the method
 * "apply" (provided by the supertype Transform).
 * 
 * Example:
 * 
 * <pre>
 * assumption: N>=0 && N>=1
 * new assumption: N>=1
 * result of simplify (N>=1) : true
 * result of simplify (N>=2) : N>=2
 * result of simplify (N<0)  : false
 * </pre>
 * 
 * Etc.
 */
public interface Simplifier extends
		Transform<SymbolicExpression, SymbolicExpression> {

	/** Returns the symbolic universe associated to this simplifier */
	ExtendedUniverse universe();

	/**
	 * In the process of simplifying the initial context, this simplier may have
	 * "solved" for some of the symbolic constants occurring in the context.
	 * This method returns a map in which the keys are those symbolic constants
	 * and the value associated to a key is the "solved" value. The solved value
	 * will be substituted for the symbolic constants in any expression given to
	 * the {@link simplify} method of this simplifier.
	 * 
	 * @return a mapping from some symbolic constants occurring in original
	 *         context to their solved values
	 */
	Map<SymbolicConstant, SymbolicExpression> substitutionMap();

	/**
	 * Returns the reduced context associated to this Reasoner. This expression
	 * may differ from the original one used to create the Reasoner because it
	 * was simplified or put into a canonical form. Moreover, symbolic constants
	 * which have been "solved" may be removed from the context. (For the
	 * context with addtional equations giving those solved values, use method
	 * {@link getFullContext}). This context will not change after creation.
	 * 
	 * @return the reduced context associated to this Reasoner
	 * */
	BooleanExpression getReducedContext();

	/**
	 * Returns the full context associated to this Reasoner. This expression may
	 * differ from the original one used to create the Reasoner because it was
	 * simplified or put into a canonical form. The full context includes
	 * equations where one side is a symbolic constant and the other is the
	 * solved value. (For the context without those equations, use method
	 * {@link getReducedContext}). Hence the expression returned is equivalent
	 * to the original given expression.
	 * 
	 * This context will not change after creation.
	 * 
	 * @return the reduced context associated to this Reasoner
	 * */
	BooleanExpression getFullContext();

	/**
	 * If the assumption can be represented as a simple interval constraint,
	 * i.e., an expression of the form A <= x <= B, where A and B are concrete
	 * numbers, x is the given symbolic constant, and <= could be < in either
	 * case, this returns the interval [A,B] (or (A,B], or, ...). Else returns
	 * null.
	 */
	Interval assumptionAsInterval(SymbolicConstant symbolicConstant);
}
