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
package edu.udel.cis.vsl.sarl.expr.IF;

import java.util.Collection;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

public interface BooleanExpressionFactory {

	/**
	 * Allows CnfFactory to simplify expensive (p || !p) expressions as they are
	 * created. Default is false. Setting to true will decrease performance in
	 * certain CnfFactory methods.
	 * 
	 * @param boolean value: false = default
	 * 
	 */
	void setBooleanExpressionSimplification(boolean value);

	/**
	 * Whether or not CnfFactory methods are allowed to simplify expensive (p ||
	 * !p) expressions.
	 * 
	 * @return boolean value: false = default
	 */
	boolean getBooleanExpressionSimplification();

	/**
	 * Returns an expression, given the operator and collection of arguments
	 * 
	 * @param operator
	 *            A SymbolicOperator
	 * @param args
	 *            Collection of arguments
	 * @return Returns a BooleanExpression
	 */
	BooleanExpression booleanExpression(SymbolicOperator operator,
			Collection<SymbolicObject> args);

	/**
	 * Returns an expression, given the operator and an array of arguments
	 * 
	 * @param operator
	 *            A SymbolicOperator
	 * @param args
	 *            Array of arguments
	 * @return Returns a BooleanExpression
	 */
	BooleanExpression booleanExpression(SymbolicOperator operator,
			SymbolicObject[] args);

	/**
	 * Returns an expression, given the operator and an argument
	 * 
	 * @param operator
	 *            A SymbolicOperator
	 * @param arg0
	 *            A SymbolicObject
	 * @return Returns a BooleanExpression
	 */
	BooleanExpression booleanExpression(SymbolicOperator operator,
			SymbolicObject arg0);

	/**
	 * Returns an expression, given the operator and two arguments
	 * 
	 * @param operator
	 *            A SymbolicOperator
	 * @param arg0
	 *            A SymbolicObject
	 * @param arg1
	 *            A SymbolicObject
	 * @return Returns a BooleanExpression
	 */
	BooleanExpression booleanExpression(SymbolicOperator operator,
			SymbolicObject arg0, SymbolicObject arg1);

	/**
	 * Returns an expression, given the operator and three arguments
	 * 
	 * @param operator
	 *            A SymbolicOperator
	 * @param arg0
	 *            A SymbolicObject
	 * @param arg1
	 *            A SymbolicObject
	 * @param arg2
	 *            A SymbolicObject
	 * @return Returns a BooleanExpression
	 */
	BooleanExpression booleanExpression(SymbolicOperator operator,
			SymbolicObject arg0, SymbolicObject arg1, SymbolicObject arg2);

	/**
	 * Returns a true BooleanExpression
	 * 
	 * @return BooleanExpression
	 */
	BooleanExpression trueExpr();

	/**
	 * Returns a false BooleanExpression
	 * 
	 * @return BooleanExpression
	 */
	BooleanExpression falseExpr();

	/**
	 * The symbolic expression wrapping the given boolean object (true or
	 * false).
	 */
	BooleanExpression symbolic(BooleanObject object);

	/**
	 * Short cut for symbolic(booleanObject(value)).
	 * 
	 * @param value
	 * @return symbolic expression wrapping boolean value
	 */
	BooleanExpression symbolic(boolean value);

	/**
	 * Constructs new boolean symbolic constant with given name.
	 * 
	 * @param name
	 *            the name for the new symbolic constant
	 * @return the new symbolic constant
	 */
	BooleanSymbolicConstant booleanSymbolicConstant(StringObject name);

	/**
	 * Returns a symbolic expression representing the conjunction of the two
	 * given arguments. Each argument must be non-null and have boolean type.
	 * Does not modify anything.
	 * 
	 * @param arg0
	 *            a symbolic expression of boolean type
	 * @param arg1
	 *            a symbolic expression of boolean type
	 * @return conjunction of arg0 and arg1
	 */
	BooleanExpression and(BooleanExpression arg0, BooleanExpression arg1);

	/**
	 * <p>
	 * Returns a symbolic expression representing the conjunction of the two
	 * given arguments, possibly modifying <code>arg0</code>. Each argument must
	 * be non-null and have boolean type.
	 * </p>
	 * 
	 * <p>
	 * If <code>arg0</code> is mutable, this method will modify
	 * <code>arg0</code> so that it becomes the conjunction of the original
	 * value of <code>arg0</code> and <code>arg1</code>, and it returns
	 * <code>arg0</code>. Otherwise, it behaves exactly as
	 * {@link #and(BooleanExpression, BooleanExpression)}. <code>arg1</code> is
	 * not modified in any case.
	 * </p>
	 * 
	 * @param arg0
	 *            a symbolic expression of boolean type
	 * @param arg1
	 *            a symbolic expression of boolean type
	 * @return conjunction of arg0 and arg1
	 */
	BooleanExpression andMut(BooleanExpression arg0, BooleanExpression arg1);

	/**
	 * Returns a symbolic expression representing the disjunction of the two
	 * given arguments. Each argument must be non-null and have boolean type.
	 * 
	 * @param arg0
	 *            a symbolic expression of boolean type
	 * @param arg1
	 *            a symbolic expression of boolean type
	 * @return disjunction of arg0 and arg1
	 */
	BooleanExpression or(BooleanExpression arg0, BooleanExpression arg1);

	BooleanExpression orMut(BooleanExpression arg0, BooleanExpression arg1);

	/**
	 * Returns a symbolic expression which represents the disjunction of the
	 * expressions in the given array args. Each expression in args must have
	 * boolean type. args must be non-null, and may have any length, including
	 * 0. If the length of args is 0, the resulting expression is equivalent to
	 * "false".
	 * 
	 * @param args
	 *            a sequence of expressions of boolean type
	 * @return the disjunction of the expressions in args
	 */
	BooleanExpression or(Iterable<? extends BooleanExpression> args);

	BooleanExpression orMut(BooleanExpression arg0,
			Iterable<? extends BooleanExpression> args);

	/**
	 * Returns a symbolic expression representing the logical negation of the
	 * given expression arg. arg must be non-null and have boolean type.
	 * 
	 * @param arg
	 *            a symbolic expression of boolean type
	 * @return negation of arg
	 */
	BooleanExpression not(BooleanExpression arg);

	BooleanExpression notMut(BooleanExpression arg);

	/**
	 * Returns a symbolic expression representing "p implies q", i.e., p=>q.
	 * 
	 * @param arg0
	 *            a symbolic expression of boolean type (p)
	 * @param arg1
	 *            a symbolic expression of boolean type (q)
	 * @return p=>q
	 */
	BooleanExpression implies(BooleanExpression arg0, BooleanExpression arg1);

	/**
	 * Returns a symbolic expression representing "p is equivalent to q", i.e.,
	 * p<=>q.
	 * 
	 * @param arg0
	 *            a symbolic expression of boolean type (p)
	 * @param arg1
	 *            a symbolic expression of boolean type (q)
	 * @return p<=>q
	 */
	BooleanExpression equiv(BooleanExpression arg0, BooleanExpression arg1);

	/**
	 * Returns the universally quantified expression forall(x).e.
	 * 
	 * @param boundVariable
	 *            the bound variable x
	 * @param predicate
	 *            the expression e (of boolean type)
	 * @return the expression forall(x).e
	 */
	BooleanExpression forall(SymbolicConstant boundVariable,
			BooleanExpression predicate);

	/**
	 * Returns the existentially quantified expression exists(x).e.
	 * 
	 * @param boundVariable
	 *            the bound variable x
	 * @param predicate
	 *            the expression e (of boolean type)
	 * @return the expression exists(x).e
	 */
	BooleanExpression exists(SymbolicConstant boundVariable,
			BooleanExpression predicate);

	// Think about this..

	// SymbolicSet<BooleanExpression> getConjunctiveClauses(BooleanExpression
	// predicate)

	// BooleanExpression and(SymbolicSet<BooleanExpression> clauses)
	// a possibly faster version of and.
	// Cnf will have to check all members are instances of correct class
	// or
	//
	// BooleanExpression[] getConjunctiveClauses(BooleanExpression predicate);
	// BooleanExpression conjunctiveWeakending(BooleanExpression predicate,
	// BitSet clausesToKeep);

}
