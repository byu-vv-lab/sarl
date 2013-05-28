/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.simplify.IF;

import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.Transform;
import edu.udel.cis.vsl.sarl.IF.UnaryOperator;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

/**
 * A simplifier is an object for simplifying symbolic expressions. It is created
 * with a given "context", which is a boolean-valued symbolic expression. The
 * context itself can be simplified; the simplified version of the context can
 * then be obtained via {@link #getFullContext} or {@link #getReducedContext}.
 * The former is an expression equivalent to the given context; the latter is
 * (possibly) weaker in that symbolic constants which have been "solved" are
 * removed. The solved values for those symbolic constants can be obtained from
 * method {@link #substitutionMap}. The contexts are all fixed at instantiation
 * and cannot be modified.
 * 
 * Any symbolic expression can then be simplified (subject to the context) using
 * the method {@link Transform#apply} (provided by the supertype
 * {@link Transform}).
 * 
 * Example:
 * 
 * <ul>
 * 
 * <li>context: <code>N>=0 && N>=1</code>
 * <ul>
 * <li>full context and reduced context: <code>N>=1</code></li>
 * <li>substitutionMap: <code>empty</code></li>
 * <li>result of simplifying <code>N>=1</code> : <code>true</code></li>
 * <li>result of simplifying <code>N>=2</code> : <code>N>=2</code></li>
 * <li>result of simplifying <code>N<0</code> : <code>false</code></li>
 * </ul>
 * </li>
 * 
 * <li>context: <code>x>=2 && x<=2</code>
 * <ul>
 * <li>full context: <code>x=2</code></li>
 * <li>reduced context: <code>true</code></li>
 * <li>subsitutionMap: <code>x:2</code></li>
 * <li>result of simplifying <code>x+y</code>: <code>2+y</code></li>
 * </ul>
 * </li>
 * 
 * </ul>
 */
public interface Simplifier extends UnaryOperator<SymbolicExpression> {

	/** Returns the pre-universe associated to this simplifier */
	PreUniverse universe();

	/**
	 * In the process of simplifying the initial context, this simplier may have
	 * "solved" for some of the symbolic constants occurring in the context.
	 * This method returns a map in which the keys are those symbolic constants
	 * and the value associated to a key is the "solved" value. The solved value
	 * will be substituted for the symbolic constants in any expression given to
	 * the {@link Transform#apply} method of this simplifier.
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
	 * {@link #getFullContext}). This context will not change after creation.
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
	 * {@link #getReducedContext}). Hence the expression returned is equivalent
	 * to the original given expression.
	 * 
	 * This context will not change after creation.
	 * 
	 * @return the reduced context associated to this Reasoner
	 * */
	BooleanExpression getFullContext();

	/**
	 * If the assumption can be represented as a simple interval constraint,
	 * i.e., an expression of the form <code>A <= x <= B</code>, where
	 * <code>A</code> and <code>B</code> are concrete numbers, <code>x</code> is
	 * the given symbolic constant, and <code><=</code> could be <code><</code>
	 * in either case, this returns the interval <code>[A,B]</code> (or
	 * <code>(A,B]</code>, or, ...). Else returns null.
	 * 
	 * @param symbolicConstant
	 *            the symbolic constant <code>x</code> around which the context
	 *            should be expressed as an interval
	 * @return the Interval bounding the symbolic constant or null if the
	 *         context does not have interval form
	 */
	Interval assumptionAsInterval(SymbolicConstant symbolicConstant);
}
