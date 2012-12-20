package edu.udel.cis.vsl.sarl.symbolic.IF;

import java.util.Collection;

import edu.udel.cis.vsl.tass.config.RunConfiguration;
import edu.udel.cis.vsl.sarl.number.IF.NumberIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF.SymbolicKind;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicArrayTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicCompleteArrayTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicFunctionTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTupleTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.constant.SymbolicConstant;

/**
 * A symbolic universe is used for the creation and manipulation of symbolic
 * expressions. The symbolic expressions created by this universe are said to
 * belong to this universe. Every symbolic expression belongs to one universe,
 * though a reference to the universe is not necessarily stored in the
 * expression.
 * 
 * Symbolic expressions are typed. The types are also produced by this universe.
 * 
 * Symbolic expressions are for the most part opaque objects. There are not many
 * methods defined in the SymbolicExpressionIF interface itself. This is to give
 * the implementations of SymbolicUniverseIF as much leeway as possible in the
 * implementation details, form of internal representation, etc.
 * 
 * Many applications, however, need to "walk" through a symbolic expression,
 * treating it as a tree in which the nodes are operators and the leaves are
 * symbolic constants or concrete values. This interface does not make any
 * assumptions that symbolic expressions are represented in such a form.
 * Instead, there is a method "tree" defined here which takes a
 * SymbolicExpressionIF and returns an instance of TreeExpressionIF. The
 * TreeExpressionIF has the convenient tree form.
 */
public interface SymbolicUniverseIF {

	/* Information */

	/** The run configuration associated to this run of TASS. */
	RunConfiguration configuration();

	/** Returns the number of symbolic expressions controlled by this universe. */
	int numExpression();

	/* Types */

	/** The boolean type. */
	SymbolicTypeIF booleanType();

	/** The integer type, representing the set of mathematical integers. */
	SymbolicTypeIF integerType();

	/** The real type, representing the set of mathematical real numbers. */
	SymbolicTypeIF realType();

	/**
	 * Returns the complete array type with the given element type and extent
	 * (array length). Neither argument can be null.
	 */
	SymbolicCompleteArrayTypeIF arrayType(SymbolicTypeIF elementType,
			SymbolicExpressionIF extent);

	/**
	 * Returns the incomplete array type with the given element type. The
	 * element type cannot be null.
	 */
	SymbolicArrayTypeIF arrayType(SymbolicTypeIF elementType);

	/**
	 * The tuple type defined by the given sequence of component types. The
	 * tuple type consists of all tuples of values (x_0,...,x_{n-1}), where
	 * x_{i} has type fieldsTypes[i]. A tuple type also has a name, and two
	 * tuple types are not equal if they have unequal names.
	 */
	SymbolicTupleTypeIF tupleType(String name, SymbolicTypeIF[] fieldTypes);

	/**
	 * Returns the function type. A function type is specified by an array of
	 * inputs types, and an output type.
	 * 
	 * @param inputTypes
	 *            array of input types
	 * @param outputType
	 *            the output type of the function
	 * @return the function type
	 */
	SymbolicFunctionTypeIF functionType(SymbolicTypeIF[] inputTypes,
			SymbolicTypeIF outputType);

	/* Symbolic Constants */

	// TODO: add method to substitute symbolic constants into an expression:
	// SymbolicExpressionIF substitute(SymbolicExpressionIF oldExpression,
	// Map<SymbolicConstantIF,SymbolicConstantIF> map);

	/**
	 * Creates and returns a new symbolic constant with given name and type.
	 * Throws exception if there already exists a symbolic constant with that
	 * name and type. Note that it is allowed to have two distinct symbolic
	 * constants with the same name, as long as they have different types.
	 */
	SymbolicConstantIF newSymbolicConstant(String name, SymbolicTypeIF type);

	/**
	 * Finds the symbolic constant with the given name and type and returns it,
	 * if it exists. Otherwise, returns null.
	 */
	SymbolicConstantIF getSymbolicConstant(String name, SymbolicTypeIF type);

	/**
	 * Looks for the symbolic constant with the given name and type. If this
	 * constant is found, it is returned. If no symbolic constant with that name
	 * is found, a new one is created and returned.
	 */
	SymbolicConstantIF getOrCreateSymbolicConstant(String name,
			SymbolicTypeIF type);

	/**
	 * Returns the set of symbolic constants that have been instantiated in this
	 * universe.
	 * 
	 * @return a set of symbolic constants
	 */
	Collection<SymbolicConstant> symbolicConstants();

	/* Symbolic Expressions */

	/**
	 * Returns the symbolic expression with the given ID (in the range
	 * 0..numExpressions-1).
	 */
	SymbolicExpressionIF expressionWithId(int index);

	/** Returns the set of all symbolic expressions controlled by this universe. */
	Collection<SymbolicExpressionIF> expressions();

	/** The symbolic expression which wraps the symbolic constant. */
	SymbolicExpressionIF symbolicConstantExpression(
			SymbolicConstantIF symbolicConstant);

	/** The symbolic tree expression which wraps the symbolic constant. */
	TreeExpressionIF symbolicConstantTreeExpression(
			SymbolicConstantIF symbolicConstant);

	/** The symbolic expression wrapping the given concrete number */
	SymbolicExpressionIF concreteExpression(NumberIF value);

	/** The symbolic expression wrapping the given concrete Java int */
	SymbolicExpressionIF concreteExpression(int value);

	/** The symbolic expression representing the 0 integer value. */
	SymbolicExpressionIF zeroInt();

	/** The symbolic expression representing the real number 0. */
	SymbolicExpressionIF zeroReal();

	/** The symbolic expression representing the integer 1. */
	SymbolicExpressionIF oneInt();

	/** The symbolic expression representing the real number 1. */
	SymbolicExpressionIF oneReal();

	/**
	 * The symbolic expression wrapping the given boolean value (true or false).
	 */
	SymbolicExpressionIF concreteExpression(boolean value);

	/**
	 * The result of applying an uninterpreted function to a sequence of
	 * arguments. The number and types of arguments must match the function's
	 * input signature.
	 */
	SymbolicExpressionIF apply(SymbolicExpressionIF function,
			SymbolicExpressionIF[] arguments);

	/**
	 * Attempts to interpret the given symbolic expression as a symbolic
	 * constant. If this is not possible, returns null.
	 */
	SymbolicConstantIF extractSymbolicConstant(SymbolicExpressionIF expression);

	/**
	 * Attempts to interpret the given symbolic expression as a concrete number.
	 * If this is not possible, returns null.
	 */
	NumberIF extractNumber(SymbolicExpressionIF expression);

	/**
	 * Attempts to interpret the given symbolic expression as a concrete boolean
	 * value. If this is not possible, returns null.
	 */
	Boolean extractBoolean(SymbolicExpressionIF expression);

	/**
	 * Given any symbolic expression in tree form, returns an equivalent one in
	 * the universe's internal canonical form.
	 */
	SymbolicExpressionIF canonicalize(TreeExpressionIF standard);

	/**
	 * Given a tree expression that was returned by the tree method in this
	 * universe, returns the equivalent expression in the universe's internal
	 * canonical form. This can be more efficient than the canonicalize method,
	 * but is not as general, as it is only guaranteed to work for tree
	 * expressions that were returned by tree.
	 */
	SymbolicExpressionIF canonicalizeTree(TreeExpressionIF tree);

	/**
	 * 
	 * Given a symbolic expression in the universe's internal canonical form,
	 * returns an equivalent symbolic expression in tree form. Note that the
	 * expression in tree form cannot be used as an argument to any of the
	 * methods in the universe, other than those that explicitly call for
	 * TreeExpressionIF, namely canonicalize. So you can manipulate the tree
	 * expression however you want, but when you want to get it back into the
	 * universe, you need to go through canonicalize.
	 */
	TreeExpressionIF tree(SymbolicExpressionIF expression);

	/**
	 * Takes an operator and an array of expressions in internal canonical form,
	 * and returns expression in internal canonical form that is the expression
	 * you would get by invoking the appropriate operator method on the
	 * arguments.
	 */
	SymbolicExpressionIF make(SymbolicKind operator, SymbolicTypeIF type,
			SymbolicExpressionIF[] arguments);

	/**
	 * Returns a simplifier object for the given assumption. The simplifier
	 * provides a method that takes a symbolic expression and returns an
	 * equivalent simplified expression, assuming the assumption holds. The
	 * assumption is any boolean-valued symbolic expression in this universe.
	 */
	SimplifierIF simplifier(SymbolicExpressionIF assumption);

	/* Numeric Operations */

	/**
	 * Returns a symbolic expression which is the result of adding the two given
	 * symbolic exprssions. The two given expressions must have the same
	 * (numeric) type: either both integers, or both real.
	 * 
	 * @param arg0
	 *            a symbolic expression of a numeric type
	 * @param arg1
	 *            a symbolic expression of the same numeric type
	 * @return arg0+arg1
	 */
	SymbolicExpressionIF add(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1);

	/**
	 * Returns a symbolic expression which is the result of subtracting arg1
	 * from arg0. The two given expressions must have the same (numeric) type:
	 * either both integers, or both real.
	 * 
	 * @param arg0
	 *            a symbolic expression of a numeric type
	 * @param arg1
	 *            a symbolic expression of the same numeric type
	 * @return arg0-arg1
	 */
	SymbolicExpressionIF subtract(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1);

	/**
	 * Returns a symbolic expression which is the result of multiplying the two
	 * given symbolic exprssions. The two given expressions must have the same
	 * (numeric) type: either both integers, or both real.
	 * 
	 * @param arg0
	 *            a symbolic expression of a numeric type
	 * @param arg1
	 *            a symbolic expression of the same numeric type
	 * @return arg0 * arg1, the product of arg0 and arg1.
	 */
	SymbolicExpressionIF multiply(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1);

	/**
	 * Returns a symbolic expression which is the result of dividing arg0 by
	 * arg1. The two given expressions must have the same (numeric) type: either
	 * both integers, or both real. In the integer case, division is interpreted
	 * as "integer division", which rounds towards 0.
	 * 
	 * @param arg0
	 *            a symbolic expression of a numeric type
	 * @param arg1
	 *            a symbolic expression of the same numeric type
	 * @return arg0 / arg1
	 */
	SymbolicExpressionIF divide(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1);

	/**
	 * Returns a symbolic expression which represents arg0 modulo arg1. The two
	 * given expressions must have the integer type. What happens for negative
	 * integers is unspecified.
	 * 
	 * @param arg0
	 *            a symbolic expression of integer type
	 * @param arg1
	 *            a symbolic expression of integer type
	 * @return arg0 % arg1
	 */
	SymbolicExpressionIF modulo(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1);

	/**
	 * Returns a symbolic expression which is the negative of the given
	 * numerical expression. The given expression must be non-null and have
	 * either integer or real type.
	 * 
	 * @param arg
	 *            a symbolic expression of integer or real type
	 * @return -arg
	 */
	SymbolicExpressionIF minus(SymbolicExpressionIF arg);

	/**
	 * Power operator: e^b. Here e must be a numeric expression (either integer
	 * or real type) and b must be a concretizeable integer expression whose
	 * concrete value is non-negative.
	 */
	SymbolicExpressionIF power(SymbolicExpressionIF base,
			SymbolicExpressionIF exponent);

	/** Casts a real or integer type expression to an expression of real type. */
	SymbolicExpressionIF castToReal(SymbolicExpressionIF numericExpression);

	/* Boolean Operations */

	/**
	 * Returns a symbolic expression which represents the conjunction of the
	 * expressions in the given array args. Each expression in args must have
	 * boolean type. args must be non-null, and may have any length, including
	 * 0. If the length of args is 0, the resulting expression is equivalent to
	 * "true".
	 * 
	 * @param args
	 *            an array of expressions of boolean type
	 * @return the conjunction of the expressions in args
	 */
	SymbolicExpressionIF and(SymbolicExpressionIF[] args);

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
	SymbolicExpressionIF and(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1);

	/**
	 * Returns a symbolic expression which represents the disjunction of the
	 * expressions in the given array args. Each expression in args must have
	 * boolean type. args must be non-null, and may have any length, including
	 * 0. If the length of args is 0, the resulting expression is equivalent to
	 * "false".
	 * 
	 * @param args
	 *            an array of expressions of boolean type
	 * @return the disjunction of the expressions in args
	 */
	SymbolicExpressionIF or(SymbolicExpressionIF[] args);

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
	SymbolicExpressionIF or(SymbolicExpressionIF arg0, SymbolicExpressionIF arg1);

	/**
	 * Returns a symbolic expression representing the logical negation of the
	 * given expression arg. arg must be non-null and have boolean type.
	 * 
	 * @param arg
	 *            a symbolic expression of boolean type
	 * @return negation of arg
	 */
	SymbolicExpressionIF not(SymbolicExpressionIF arg);

	/**
	 * Given three symbolic expressions of boolean type, returns a symbolic
	 * expression that is equivalent to
	 * "if predicate then trueValue else falseValue". Equivalent to (p -> q_t)
	 * && (!p -> q_f).
	 * 
	 * @param predicate
	 *            the test condition p
	 * @param trueValue
	 *            the value q_t if condition is true
	 * @param falseValue
	 *            the value q_f if condition is false
	 * @return symbolic expression equivalent to (p -> q_t) && (!p -> q_f).
	 */
	SymbolicExpressionIF cond(SymbolicExpressionIF predicate,
			SymbolicExpressionIF trueValue, SymbolicExpressionIF falseValue);

	/**
	 * Returns expression equivalent to arg0 < arg1. The arguments must be
	 * numeric of the same type (i.e., both are of integer type or both are of
	 * real type).
	 * 
	 * @param arg0
	 *            symbolic expression of numeric type
	 * @param arg1
	 *            symbolic expression of same numeric type
	 * @return symbolic expression of boolean type arg0 < arg1
	 */
	SymbolicExpressionIF lessThan(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1);

	/**
	 * Returns expression equivalent to arg0 <= arg1 ("less than or equal to").
	 * The arguments must be numeric of the same type (i.e., both are of integer
	 * type or both are of real type).
	 * 
	 * @param arg0
	 *            symbolic expression of numeric type
	 * @param arg1
	 *            symbolic expression of same numeric type
	 * @return symbolic expression of boolean type arg0 <= arg1
	 */
	SymbolicExpressionIF lessThanEquals(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1);

	/**
	 * Returns expression equivalent to arg0 = arg1 ("equals"). The arguments
	 * must be numeric of the same type (i.e., both are of integer type or both
	 * are of real type).
	 * 
	 * @param arg0
	 *            symbolic expression of numeric type
	 * @param arg1
	 *            symbolic expression of same numeric type
	 * @return symbolic expression of boolean type arg0 = arg1
	 */
	SymbolicExpressionIF equals(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1);

	/**
	 * Returns expression equivalent to arg0 != arg1 ("not equals"). The
	 * arguments must be numeric of the same type (i.e., both are of integer
	 * type or both are of real type).
	 * 
	 * @param arg0
	 *            symbolic expression of numeric type
	 * @param arg1
	 *            symbolic expression of same numeric type
	 * @return symbolic expression of boolean type arg0 != arg1
	 */
	SymbolicExpressionIF neq(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1);

	/* Array Operations */

	/**
	 * Returns the length of any symbolic expression of array type. This is a
	 * symbolic expression of integer type.
	 * 
	 * @param array
	 *            a symbolic expression of array type
	 * @return a symbolic expression of integer type representing the length of
	 *         the array
	 */
	SymbolicExpressionIF length(SymbolicExpressionIF array);

	/**
	 * Returns an expression representing the value of the element of the array
	 * at position index. Arrays are indexed from 0. The expression returned has
	 * type the element type of the array.
	 * 
	 * @param array
	 *            the given array
	 * @param index
	 *            symbolic expression of integer type
	 * @return expression representing value of index-th element of the array
	 */
	SymbolicExpressionIF arrayRead(SymbolicExpressionIF array,
			SymbolicExpressionIF index);

	/**
	 * Returns an expression representing the result of modifying an array by
	 * changing the value at position index. Arrays are indexed from 0. The
	 * expression returned has the same (array) type as the original array.
	 * 
	 * @param array
	 *            the given array
	 * @param index
	 *            symbolic expression of integer type
	 * @param value
	 *            the new value for the element at position index
	 * @return expression representing the result of changing the index-th
	 *         element to value
	 */
	SymbolicExpressionIF arrayWrite(SymbolicExpressionIF array,
			SymbolicExpressionIF index, SymbolicExpressionIF value);

	/**
	 * Returns an expression representing an array with element type T defined
	 * by a function f from int to T.
	 */
	SymbolicExpressionIF arrayLambda(SymbolicCompleteArrayTypeIF arrayType,
			SymbolicExpressionIF function);

	/**
	 * Returns an expression representing the result obtained by starting with
	 * the array origin and issuing a sequence of concrete assignments. For each
	 * i for which elements[i] is non-null, the assignment of element[i] to
	 * position i in the array is carried out.
	 */
	SymbolicExpressionIF arrayExpression(SymbolicExpressionIF origin,
			SymbolicExpressionIF[] elements);

	/**
	 * Every array value can be represented (in more than one way) as an origin
	 * array modified with a concrete array of elements. This method works
	 * together with method getArrayElements. For some origin-elements
	 * representation, this method will return the origin; the other method
	 * returns the concrete array of elements. It is guaranteed that
	 * arrayExpression(getArrayOrigin (array), getArrayElements(array)) returns
	 * something equivalent to the original array.
	 * 
	 * @param array
	 *            expression of array type
	 * @return the array origin, an expression of the same array type as the
	 *         original array
	 */
	SymbolicExpressionIF getArrayOrigin(SymbolicExpressionIF array);

	/**
	 * Every array value can be represented (in more than one way) as an origin
	 * array modified with a concrete array of elements. This method works
	 * together with method getArrayOrigin. For some origin-elements
	 * representation, this method will return the concrete array of elements;
	 * the other method returns the origin. It is guaranteed that
	 * arrayExpression(getArrayOrigin (array), getArrayElements(array)) returns
	 * something equivalent to the original array.
	 * 
	 * @param array
	 *            expression of array type
	 * @return a concrete array of elements, some of which may be null; those
	 *         that are non-null have type the element type of the array.
	 */
	SymbolicExpressionIF[] getArrayElements(SymbolicExpressionIF array);

	/* Tuple Operations */

	/**
	 * Returns the concrete tuple expression with the given tuple components.
	 * 
	 * @param type
	 *            the tuple type
	 * @param components
	 *            the component expressions
	 * @return the tuple formed from the components
	 */
	SymbolicExpressionIF tupleExpression(SymbolicTupleTypeIF type,
			SymbolicExpressionIF[] components);

	/**
	 * Returns an expression that represents the result of reading a component
	 * in a tuple value. The index should be an integer-valued expression. The
	 * components are numbered starting from 0.
	 * 
	 * @param tuple
	 *            the tuple value being read
	 * @param index
	 *            symbolic expression for the index of the component to read
	 * @return a symbolic expression representing the component at that index
	 */
	SymbolicExpressionIF tupleRead(SymbolicExpressionIF tuple,
			SymbolicExpressionIF index);

	/**
	 * Also returns an expression representing the result of reading a component
	 * of a tuple value, in the special case where the index is a concrete
	 * integer (rather than just a symbolic expression). This is a "convenience"
	 * method, because it is often the case that the component index is known
	 * concretely, and it becomes tiresome to construct a symbolic expression
	 * wrapping the concrete int.
	 * 
	 * @param tuple
	 *            the tuple value being read
	 * @param index
	 *            concrete int index of the component to read
	 * @return a symbolic expression representing the component at that index
	 */
	SymbolicExpressionIF tupleRead(SymbolicExpressionIF tuple, int index);

	/**
	 * Returns an expression representing the result of modifying a tuple by
	 * changing the value of one component. The component is specified by its
	 * index. The components are indexed starting from 0. In this method, the
	 * index is specified by a concrete Java int.
	 * 
	 * @param tuple
	 *            the original tuple
	 * @param index
	 *            the index of the component to modify
	 * @param value
	 *            the new value for the component
	 * @return an expression representing the new tuple
	 */
	SymbolicExpressionIF tupleWrite(SymbolicExpressionIF tuple, int index,
			SymbolicExpressionIF value);

	/**
	 * Returns an expression representing the result of modifying a tuple by
	 * changing the value of one component. The component is specified by its
	 * index. The components are indexed starting from 0. In this method, the
	 * index is specified by symbolic expression of integer type.
	 * 
	 * @param tuple
	 *            the original tuple
	 * @param index
	 *            the index of the component to modify
	 * @param value
	 *            the new value for the component
	 * @return an expression representing the new tuple
	 */
	SymbolicExpressionIF tupleWrite(SymbolicExpressionIF tuple,
			SymbolicExpressionIF index, SymbolicExpressionIF value);

	/* First-order expressions */

	/**
	 * Returns the universally quantified expression forall(x).e.
	 * 
	 * @param boundVariable
	 *            the bound variable x
	 * @param predicate
	 *            the expression e (of boolean type)
	 * @return the expression forall(x).e
	 */
	SymbolicExpressionIF forall(SymbolicConstantIF boundVariable,
			SymbolicExpressionIF predicate);

	/**
	 * Returns the existenially quantified expression exists(x).e.
	 * 
	 * @param boundVariable
	 *            the bound variable x
	 * @param predicate
	 *            the expression e (of boolean type)
	 * @return the expression exists(x).e
	 */
	SymbolicExpressionIF exists(SymbolicConstantIF boundVariable,
			SymbolicExpressionIF predicate);

	/**
	 * Returns the lambda expression lambda(x).e, i.e., the expression
	 * representing the function which given x returns e, where e might possibly
	 * involve the variable x. Note that x is a symbolic constant.
	 * 
	 * @param boundVariable
	 *            the bound variable x
	 * @param expression
	 *            the expression e
	 * @return lambda(x).e
	 */
	SymbolicExpressionIF lambda(SymbolicConstantIF boundVariable,
			SymbolicExpressionIF expression);

}
