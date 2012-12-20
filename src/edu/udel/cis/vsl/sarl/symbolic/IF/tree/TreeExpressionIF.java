package edu.udel.cis.vsl.sarl.symbolic.IF.tree;

import edu.udel.cis.vsl.sarl.symbolic.IF.SymbolicExpressionIF;

/**
 * A tree symbolic expression has a "kind" and some number of arguments. Some
 * might have additional fields. The essential leaf nodes in the tree are those
 * with kind CONCRETE_BOOLEAN, CONCRETE_NUMBER, or SYMBOLIC_CONSTANT. All other
 * nodes are operators; some of these might also have 0 arguments (such as AND,
 * OR, ADD, ...) and so can also, technically be considered leaves.
 * 
 * TO POSSIBLY DO: add IFF (<=>), let quantifiers take multiple variables, add
 * =>.
 * 
 * The kinds are:
 * 
 * ADD: an expression representing addition of its arguments. Can take any
 * number of arguments (0, 1, 2, ...). If there are 0 arguments, it represents
 * the number 0 of the appropriate (integer or real) type.
 * 
 * AND: boolean conjunction. Takes any number of arguments. (same with OR). If
 * there are 0 arguments, same as TRUE.
 * 
 * APPLY: an expression representing a value of the from f(x1,...,xn). The
 * number of arguments varies, depending on the type of f. Arg 0 is f, a
 * symbolic expression of function type. Arg 1 is x1, the first argument to f,
 * and so on through arg n. * ARRAY_LAMBDA: an array expression of type T[]
 * formed by providing a function from integers to T.
 * 
 * ARRAY_READ: reading an element of an array. 2 arguments. Argument 0 is the
 * array expression (a symbolic expression of array type), Argument 1 is the
 * index expression (a symbolic expression of integer type).
 * 
 * ARRAY_WRITE: the result of writing to an array. 3 Arguments. Arg 0 is the
 * original array expression, arg 1 is the index expression, arg 2 is the new
 * value being assigned to that position in the array.
 * 
 * CAST: changing value from one type to another
 * 
 * CONCRETE_BOOLEAN: a concrete boolean value (true or false). 0 arguments. The
 * expression must be an instance of ConcreteBooleanExpressionIF, which has a
 * method to get the concrete boolean value: value(), which returns a Boolean.
 * 
 * CONCRETE_NUMBER: a concrete numerical value (instance of NumberIF). 0
 * arguments, but then expression must be an instance of
 * NumericConcreteExpressionIF, which has a method to get the concrete value:
 * value() which returns a NumberIF.
 * 
 * CONCRETE_TUPLE: a concrete tuple value, i.e., an n-tuple of the form
 * <arg0,arg1,...,arg(n-1)>. The number and types of the arguments depend on the
 * tuple type.
 * 
 * COND: a conditional expression, as in C's ternary expression (arg0 ? arg1 :
 * arg2). 3 arguments. Arg 0 is the boolean predicate expression, arg1 the
 * expression which is the result if arg0 evaluates to true, arg2 the expression
 * which is the result if arg0 evaluates to false.
 * 
 * DIVIDE: real division: 2 arguments: arg 0 the numerator, arg 1 the
 * denominator. Both must have real type.
 * 
 * EQUALS: comparison of two values. Two arguments.
 * 
 * EXISTS: existential quantification: exists x.e. arg0 is an instance of
 * SymbolicConstantExpressionIF, which wraps the bound variable x, and arg1 is
 * e, a boolean-valued symbolic expression.
 * 
 * FORALL: universal quantification: forall x.e. arg0 is an instance of
 * SymbolicConstantExpressionIF, which wraps the bound variable x, and arg1 is
 * e, a boolean-valued symbolic expression.
 * 
 * INT_DIVIDE: integer division: 2 arguments: arg 0 numerator, arg 1
 * denominator.
 * 
 * LAMBDA: a lambda expression, as in the lambda calculus: lambda x.e. arg0 is
 * an instance of SymbolicConstantExpressionIF, which wraps the bound variable
 * x, and arg1 is e, a symbolic expression. Functional type.
 * 
 * LENGTH: operator for getting the length of an array. Has 1 argument, arg0,
 * which is the array expression. Integer type.
 * 
 * LESS_THAN: 2 arguments: expression of the from arg0 < arg1.
 * 
 * LESS_THAN_EQUALS: 2 arguments: expression of the form arg0 <= arg1.
 * 
 * MODULO: integer modulus operator: arg0 % arg1.
 * 
 * MULTIPLY: an expression representing the numerical product of its arguments.
 * Can take any number of arguments (0, 1, 2, ...). If there are 0 arguments, it
 * represents the number 1 of the appropriate (integer or real) type.
 * 
 * NEGATIVE: numerical negation, - arg0.
 * 
 * NEQ: arg0 != arg1
 * 
 * NOT: logical negation, !arg0.
 * 
 * OR: boolean disjunction. Takes any number of arguments. (same with AND). If
 * there are 0 arguments, same as FALSE.
 * 
 * POWER: exponentiation: arg0^arg1 (arg0 raised to the arg1 power).
 * 
 * SUBTRACT: numerical subtraction: arg0 - arg1
 * 
 * SYMBOLIC_CONSTANT: an expression wrapping a symbolic constant. 0 arguments.
 * Expression of this kind must be an instance of SymbolicConstantExpressionIF,
 * which provides a method symbolicConstant() to get the underlying symbolic
 * constant.
 * 
 * TUPLE_READ: 2 arguments: arg0 is the tuple expression. arg1 is a
 * NumericConcreteExpressionIF of integer type giving the index in the tuple.
 * 
 * TUPLE_WRITE: 3 arguments: arg0 is the original tuple expression, arg1 is a
 * NumericConcreteExpressionIF of integer type giving the index, arg2 is the new
 * value to write into the tuple.
 * 
 * @author siegel
 */
public interface TreeExpressionIF extends SymbolicExpressionIF {

	public enum SymbolicKind {
		ADD,
		AND,
		APPLY,
		ARRAY_LAMBDA,
		ARRAY_READ,
		ARRAY_WRITE,
		CAST,
		CONCRETE_BOOLEAN,
		CONCRETE_NUMBER,
		CONCRETE_TUPLE,
		COND,
		DIVIDE,
		EQUALS,
		EXISTS,
		FORALL,
		INT_DIVIDE,
		LAMBDA,
		LENGTH,
		LESS_THAN,
		LESS_THAN_EQUALS,
		MODULO,
		MULTIPLY,
		NEGATIVE,
		NEQ,
		NOT,
		OR,
		POWER,
		SUBTRACT,
		SYMBOLIC_CONSTANT,
		TUPLE_READ,
		TUPLE_WRITE
	}

	/**
	 * The kind of symbolic expression, one of the elements of the enumerated
	 * type SymbolicKind.
	 * 
	 * @return the kind of the symbolic expression
	 */
	SymbolicKind kind();

	/**
	 * Returns the i-th argument (child) of the operator.
	 * 
	 * @param index
	 *            the index i
	 * @return the i-th argument
	 */
	TreeExpressionIF argument(int index);

	/**
	 * The number of arguments (children) of this symbolic expression.
	 * 
	 * @return number of arguments
	 */
	int numArguments();

}
