package edu.udel.cis.vsl.sarl.expr.IF;

import java.util.Collection;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

public interface BooleanExpressionFactory {

	BooleanExpression booleanExpression(SymbolicOperator operator,
			Collection<SymbolicObject> args);

	BooleanExpression booleanExpression(SymbolicOperator operator,
			SymbolicObject[] args);

	BooleanExpression booleanExpression(SymbolicOperator operator,
			SymbolicObject arg0);

	BooleanExpression booleanExpression(SymbolicOperator operator,
			SymbolicObject arg0, SymbolicObject arg1);

	BooleanExpression booleanExpression(SymbolicOperator operator,
			SymbolicObject arg0, SymbolicObject arg1, SymbolicObject arg2);

	BooleanExpression trueExpr();

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

	BooleanSymbolicConstant booleanSymbolicConstant(StringObject name);

	/**
	 * Returns a symbolic expression representing the conjunction of the two
	 * given arguments. Each argument must be non-null and have boolean type.
	 * 
	 * @param arg0
	 *            a symbolic expression of boolean type
	 * @param arg1
	 *            a symbolic expression of boolean type
	 * @return conjunction of arg0 and arg1
	 */
	BooleanExpression and(BooleanExpression arg0, BooleanExpression arg1);

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

	/**
	 * Returns a symbolic expression representing the logical negation of the
	 * given expression arg. arg must be non-null and have boolean type.
	 * 
	 * @param arg
	 *            a symbolic expression of boolean type
	 * @return negation of arg
	 */
	BooleanExpression not(BooleanExpression arg);

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
	 * Returns the existenially quantified expression exists(x).e.
	 * 
	 * @param boundVariable
	 *            the bound variable x
	 * @param predicate
	 *            the expression e (of boolean type)
	 * @return the expression exists(x).e
	 */
	BooleanExpression exists(SymbolicConstant boundVariable,
			BooleanExpression predicate);

}
