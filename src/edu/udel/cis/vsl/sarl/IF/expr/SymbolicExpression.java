package edu.udel.cis.vsl.sarl.IF.expr;

import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

/**
 * An instance of this type represents a symbolic expression. This is the root
 * of the symbolic expression type hierarchy.
 * 
 * Symbolic expressions are immutable: they cannot be modified after they are
 * instantiated. (Or at least, not in a way visible to the user.)
 * 
 * A symbolic expression has an operator, a type, and some number of arguments,
 * which together fully specify the expression. The arguments implement the
 * SymbolicObject interface. SymbolicExpressionIF extends SymbolicObject, i.e.,
 * a symbolic expression can be used as an argument (but so can other types of
 * objects).
 * 
 * The difference between symbolic expressions and symbolic objects which are
 * not symbolic expressions is that the latter may have essential fields that
 * are not arguments. (An essential field is used in the "equals" method.) In
 * contrast, a symbolic expression is completely determined by its kind, type,
 * and arguments.
 * 
 * Types implementing SymbolicObject include
 * <ul>
 * <li>SymbolicExpressionIF</li>
 * <li>SymbolicCollection (including SymbolicMap and SymbolicSequence)</li>
 * <li>NumberObject</li>
 * <li>IntObject</li>
 * <li>BooleanObject</li>
 * <li>StringObject</li>
 * </ul>
 * 
 * Every symbolic expression has a type, which is an instance of SymbolicTypeIF.
 * 
 * The symbolic expression operators are as follows:
 * 
 * <ul>
 * <li>ADD: an expression representing the sum of symbolic expressions. This has
 * 1 or 2 arguments. If 1, the argument is a SymbolicCollection with at least
 * one element; the ADD expression represents the sum of the elements in the
 * collection. If 2, then both arguments are symbolic expressions and have the
 * same numeric (integer or real) type, and the ADD expression represents the
 * sum of the two arguments.</li>
 * 
 * <li>AND: boolean conjunction. Has 1 or 2 arguments, similar to ADD. If 1, the
 * argument is a collection with at least one element. All symbolic expressions
 * in the collection have boolean type. If there are 2 arguments, they are both
 * symbolic expressions of boolean type.</li>
 * 
 * <li>APPLY: an expression representing a value of the form f(x). Takes 2
 * arguments. Arg 0 is f, a symbolic expression of function type. Arg 1 is a
 * SymbolicSequence containing the arguments to f in order.</li>
 * 
 * <li>ARRAY_LAMBDA: an array expression of type T[] formed by providing a
 * function f from integers to T. 1 argument: a symbolic expression f of
 * functional type.</li>
 * 
 * <li>ARRAY_READ: an expression representing the result of reading an element
 * from an array. 2 arguments. Argument 0 is the array expression (a symbolic
 * expression of array type), Argument 1 is the index expression (a symbolic
 * expression of integer type).</li>
 * 
 * <li>ARRAY_WRITE: an expression representing the array resulting from
 * modifying a single element of an array. 3 Arguments. Arg 0 is the original
 * array expression, arg 1 is the index expression, arg 2 is the new value being
 * assigned to that position in the array.</li>
 * 
 * <li>CAST: an expression representing the resulting of converting a value from
 * one type to another. 1 argument: the value being cast. The type() method in
 * this expression yields the new type to which the element is being cast.</li>
 * 
 * <li>CONCRETE: a concrete value acting as a symbolic expression. One argument,
 * which is the concrete value. Argument may be BooleanObject, NumberObject, or
 * SymbolicCollection. The latter serves as concrete values for arrays, tuples.</li>
 * 
 * <li>COND: a conditional expression, as in C's ternary expression (arg0 ? arg1
 * : arg2). 3 arguments. Arg 0 is the boolean predicate expression, arg1 the
 * expression which is the result if arg0 evaluates to true, arg2 the expression
 * which is the result if arg0 evaluates to false. arg1 and arg2 must have the
 * same type, which is the type of this expression.</li>
 * 
 * <li>DENSE_ARRAY_WRITE: Represents the result of multiple writes to an array.
 * 2 arguments. arg0 is an expression of array type T[]. Arg 2 is a
 * SymbolicSequence, say v0,...,v(n-1). Each element of the sequence is either
 * NULL (i.e., the expression with operator NULL; see below) or an expression of
 * type T. The dense array write expression represents the result of starting
 * with arg0 and then for each i for which v(i) is non-NULL, setting the array
 * element in position i to v(i). It is thus equivalent to a sequence of array
 * write operations. It is included here to allow a dense representation of the
 * array, which can have performance benefits, in particular constant-time
 * lookup and modification (just like for regular concrete arrays)</li>
 * 
 * <li>DIVIDE: real division: 2 arguments: arg 0 the numerator, arg 1 the
 * denominator. Both must be symbolic expressions of real type. Has real type.</li>
 * 
 * <li>EQUALS: comparison of two values. Two arguments, both symbolic
 * expressions. Has boolean type.</li>
 * 
 * <li>EXISTS: existential quantification: exists x.e. 2 arguments. arg0 is a
 * symbolic constant x, and arg1 is e, a boolean-valued symbolic expression. Has
 * boolean type.</li>
 * 
 * <li>FORALL: universal quantification: forall x.e. 2 arguments. arg0 is a
 * symbolic constant x, and arg1 is e, a boolean-valued symbolic expression. Has
 * boolean type.</li>
 * 
 * <li>INT_DIVIDE: integer division: 2 arguments, both symbolic expressions: arg
 * 0 numerator, arg 1 denominator. Has integer type.</li>
 * 
 * <li>LAMBDA: a lambda expression, as in the lambda calculus: lambda x.e. 2
 * arguments. arg0 is a symbolic constant x, and arg1 is e, a symbolic
 * expression. Functional type.</li>
 * 
 * <li>LENGTH: operator for getting the length of an array. Has 1 argument,
 * arg0, which is the array expression. Integer type.</li>
 * 
 * <li>LESS_THAN: 2 arguments, both symbolic expressions of the same numeric
 * type: expression of the form arg0 < arg1. Has boolean type.</li>
 * 
 * <li>LESS_THAN_EQUALS: 2 arguments, both symbolic expressions of the same
 * numeric type: expression of the form arg0 <= arg1. Has boolean type.</li>
 * 
 * <li>MODULO: integer modulus operator: 2 arguments. arg0 % arg1. Has integer
 * type.</li>
 * 
 * <li>MULTIPLY: an expression representing the numerical product of symbolic
 * expressions. Can have 1 or 2 arguments, like ADD. If 1 argument, the argument
 * is a collection with at least one element, and the elements of the collection
 * are all of the same numeric type. If the expression has 2 arguments, they
 * both have the same numeric type.</li>
 * 
 * <li>NEGATIVE: numerical negation, - arg0. 1 argument. Type is same as that of
 * argument.</li>
 * 
 * <li>NEQ: arg0 != arg1. 2 arguments. Boolean type.</li>
 * 
 * <li>NOT: logical negation, !arg0. 1 argument. Boolean type.</li>
 * 
 * <li>NULL: used to represent no symbolic expression in case where Java's null
 * is not acceptable</li>
 * 
 * <li>OR: boolean disjunction. Like AND, takes 1 or 2 arguments. If 1 argument,
 * the argument is a collection with at least one element. If 2 arguments, both
 * have boolean type.</li>
 * 
 * <li>POWER: exponentiation: arg0^arg1 (arg0 raised to the arg1-th power). 2
 * arguments: the base (symbolic expression of numeric type) and the exponent.
 * The exponent can be either a symbolic expression of numeric type or an
 * IntObject. In the latter case, the int is non-negative.</li>
 * 
 * <li>SUBTRACT: numerical subtraction: arg0 - arg1. 2 symbolic expression
 * arguments of same numeric type.</li>
 * 
 * <li>SYMBOLIC_CONSTANT: a symbolic constant. 1 argument, a StringObject, which
 * givens the name of the symbolic constant.</li>
 * 
 * <li>TUPLE_READ: 2 arguments: arg0 is the tuple expression. arg1 is an
 * IntObject giving the index in the tuple.</li>
 * 
 * <li>TUPLE_WRITE: 3 arguments: arg0 is the original tuple expression, arg1 is
 * an IntObject giving the index, arg2 is the new value to write into the tuple.
 * </li>
 * 
 * <li>UNION_INJECT: injects an element of a member type into a union type that
 * inclues that member type. 2 arguments: arg0 is an IntObject giving the index
 * of the member type of the union type; arg1 is a symbolic expression whose
 * type is the member type. The union type itself is the type of the
 * UNION_INJECT expression.</li>
 * 
 * <li>UNION_TEST: 2 arguments: arg0 is an IntObject giving the index of a
 * member type of the union type; arg1 is a symbolic expression whose type is
 * the union type. This is a boolean-valued expression whose value is true iff
 * arg1 belongs to the specified member type of the union type.</li>
 * 
 * <li>UNION_EXTRACT: 2 arguments: arg0 is an IntObject giving the index of a
 * member type of a union type; arg1 is a symbolic expression whose type is the
 * union type. The resulting expression has type the specified member type. This
 * essentially pulls the expression out of the union and casts it to the member
 * type. If arg1 does not belong to the member type (as determined by a
 * UNION_TEST expression), the value of this expression is undefined.</li>
 * 
 * </ul>
 * 
 * TO POSSIBLY DO: add IFF (<=>), let quantifiers take multiple variables, add
 * =>.
 * 
 * @author siegel
 */
public interface SymbolicExpression extends SymbolicObject {

	/**
	 * An enumerated type for the different kinds of symbolic expressions.
	 */
	public enum SymbolicOperator {
		ADD,
		AND,
		APPLY,
		ARRAY_LAMBDA,
		ARRAY_READ,
		ARRAY_WRITE,
		CAST,
		CONCRETE,
		COND,
		DENSE_ARRAY_WRITE,
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
		NULL,
		OR,
		POWER,
		SUBTRACT,
		SYMBOLIC_CONSTANT,
		TUPLE_READ,
		TUPLE_WRITE,
		UNION_EXTRACT,
		UNION_INJECT,
		UNION_TEST
	}

	/**
	 * Returns the i-th argument (child) of the operator.
	 * 
	 * @param index
	 *            the index i
	 * @return the i-th argument
	 */
	SymbolicObject argument(int index);

	/**
	 * A string representation appropriate for nesting in other expressions,
	 * typically by surrounding the normal string version with parentheses if
	 * necessary.
	 */
	String atomString();

	/**
	 * The kind of this symbolic expression, one of the elements of the
	 * enumerated type SymbolicKind.
	 * 
	 * @return the kind of the symbolic expression
	 */
	SymbolicOperator operator();

	/**
	 * The number of arguments (children) of this symbolic expression.
	 * 
	 * @return number of arguments
	 */
	int numArguments();

	/**
	 * A nice human-readable stand-alone representation of the symbolic
	 * expression.
	 */
	@Override
	String toString();

	/** Returns the type of this symbolic expression. */
	SymbolicType type();

	/** Is this the "NULL" symbolic expression? */
	boolean isNull();

	boolean isFalse();

	boolean isTrue();

	boolean isZero();

	boolean isOne();

	boolean isNumeric();

}
