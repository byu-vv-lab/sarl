package edu.udel.cis.vsl.sarl.symbolic;

import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;

/**
 * A primitive expression is an expression not formed by numerical or logical
 * operators, hence it cannot be broken down any further in the canonical
 * representation. Classes implementing this interface: ArrayRead, ArrayWrite,
 * TupleRead, TupleWrite, SymbolicConstantExpression, RelationalExpression,
 * EvaluatedFunctionExpression, ConditionalExpression,
 * IntegerDivisionExpression, IntegerModulusExpression.
 * 
 * This is a marker interface.
 */
public interface NumericPrimitive extends TreeExpressionIF {

	public enum NumericPrimitiveKind {
		APPLY,
		ARRAY_READ,
		CAST,
		COND,
		INT_DIV,
		INT_MOD,
		LAMBDA,
		LENGTH,
		SYMBOLIC_CONSTANT,
		TUPLE_READ
	}

	NumericPrimitiveKind numericPrimitiveKind();

}
