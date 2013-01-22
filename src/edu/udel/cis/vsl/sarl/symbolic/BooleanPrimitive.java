package edu.udel.cis.vsl.sarl.symbolic;

import edu.udel.cis.vsl.sarl.IF.SymbolicExpressionIF;

/**
 * A primitive expression is an expression not formed by numerical or logical
 * operators, hence it cannot be broken down any further in the canonical
 * representation. Classes implementing this interface: ArrayRead, ArrayWrite,
 * TupleRead, TupleWrite, SymbolicConstantExpression, RelationalExpression,
 * EvaluatedFunctionExpression, ConditionalExpression,
 * IntegerDivisionExpression, IntegerModulusExpression, LambdaExpression.
 * 
 * This is a marker interface.
 */
public interface BooleanPrimitive extends SymbolicExpressionIF {

	public enum BooleanPrimitiveKind {
		APPLY, ARRAY_READ, LAMBDA, SYMBOLIC_CONSTANT, TUPLE_READ
	}

	BooleanPrimitiveKind booleanPrimitiveKind();

}
