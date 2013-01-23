package edu.udel.cis.vsl.sarl.IF;

import java.util.Collection;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.SymbolicExpressionIF.SymbolicOperator;

/**
 * A symbolic universe is used for the creation and manipulation of symbolic
 * objects and types. The symbolic objects and types created by this universe
 * are said to belong to this universe. Every symbolic object/type belongs to
 * one universe, though a reference to the universe is not necessarily stored in
 * the object/type.
 * 
 * Symbolic expressions are a kind of symbolic object. Every symbolic expression
 * has a symbolic type. Other symbolic objects include symbolic collections,
 * such as sequences, sets, and maps.
 * 
 * Symbolic objects implement the Immutable Pattern. All symbolic objects are
 * immutable, i.e., they cannot be modified after they are created.
 */
public interface SymbolicUniverseIF {

	// Types...

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
	 * Returns a type sequence based on the given array of types.
	 * 
	 * @param types
	 *            an array of types; must be non-null and each element must be
	 *            non-null
	 * @return a SymbolicTypeSequenceIF based on the array
	 */
	SymbolicTypeSequenceIF typeSequence(SymbolicTypeIF[] types);

	/**
	 * Returns a type sequence based on the given iterable object.
	 * 
	 * @param types
	 *            an iterable thing: must be non-null and all elements returned
	 *            by the iterator.
	 * @return a SymbolicTypeSequenceIF based on the iterator
	 */
	SymbolicTypeSequenceIF typeSequence(Iterable<SymbolicTypeIF> types);

	/**
	 * The tuple type defined by the given sequence of component types. The
	 * tuple type consists of all tuples of values (x_0,...,x_{n-1}), where
	 * x_{i} has type fieldsTypes[i]. A tuple type also has a name, and two
	 * tuple types are not equal if they have unequal names.
	 */
	SymbolicTupleTypeIF tupleType(StringObject name,
			SymbolicTypeSequenceIF fieldTypes);

	/**
	 * Returns the function type. A function type is specified by an array of
	 * inputs types, and an output type.
	 * 
	 * @param inputTypes
	 *            sequence of input types
	 * @param outputType
	 *            the output type of the function
	 * @return the function type
	 */
	SymbolicFunctionTypeIF functionType(SymbolicTypeSequenceIF inputTypes,
			SymbolicTypeIF outputType);

	/**
	 * The type which is the union of the given types.
	 * 
	 * @param name
	 * @param memberTypes
	 * @return
	 */
	SymbolicUnionTypeIF unionType(StringObject name,
			SymbolicTypeSequenceIF memberTypes);

	// Symbolic Objects...

	// General...

	/** Returns the number of symbolic objects controlled by this universe. */
	int numObjects();

	/**
	 * Returns the symbolic expression with the given ID (in the range
	 * 0..numExpressions-1).
	 */
	SymbolicObject objectWithId(int index);

	/** Returns the set of all symbolic objects controlled by this universe. */
	Collection<SymbolicObject> objects();

	/**
	 * Applies the given operator to the arguments and returns the resulting
	 * expression in the canonic form used by this universe. The arguments
	 * should have the form required by the operator; see the documentation in
	 * the SymbolicExpressionIF interface.
	 * 
	 * @param operator
	 *            a symbolic operator
	 * @param type
	 *            the type which the resulting expression should have (it may
	 *            not be unambiguous)
	 * @param arguments
	 *            arguments which should be appropriate for the specified
	 *            operator
	 */
	SymbolicExpressionIF make(SymbolicOperator operator, SymbolicTypeIF type,
			SymbolicObject[] arguments);

	/**
	 * Returns a simplifier object for the given assumption. The simplifier
	 * provides a method that takes a symbolic expression and returns an
	 * equivalent simplified expression, assuming the assumption holds. The
	 * assumption is any boolean-valued symbolic expression in this universe.
	 */
	SimplifierIF simplifier(SymbolicExpressionIF assumption);

	// Symbolic primitive objects: ints, boolean, reals, strings
	// Note: these are not symbolic expressions, just symbolic objects!

	BooleanObject booleanObject(boolean value);

	IntObject intObject(int value);

	NumberObject numberObject(NumberIF value);

	StringObject stringObject(String string);

	// Symbolic constants...

	/**
	 * Returns the symbolic constant with the given name and type. Two symbolic
	 * constants are equal iff they have the same name and type. This method may
	 * use a Flyweight Pattern to return the same object if called twice with
	 * the same arguments. Or it may create a new object each time. These
	 * details are unimportant because symbolic constants are immutable.
	 */
	SymbolicConstantIF symbolicConstant(StringObject name, SymbolicTypeIF type);

	/**
	 * Returns the set of symbolic constants that have been instantiated in this
	 * universe.
	 * 
	 * @return a set of symbolic constants
	 */
	Collection<SymbolicConstantIF> symbolicConstants();

	/**
	 * Attempts to interpret the given symbolic expression as a symbolic
	 * constant. If this is not possible, returns null.
	 */
	SymbolicConstantIF extractSymbolicConstant(SymbolicExpressionIF expression);

	/**
	 * Substitutes symbolic expressions for symbolic constants in a symbolic
	 * expression.
	 * 
	 * Each occurrence of symbolic constant X in expression will be replaced
	 * with expression map(X), if map(X) is not null. The substitutions are all
	 * simultaneous.
	 * 
	 * @param expression
	 *            any symbolic expression
	 * @param map
	 *            a map from symbolic constants to symbolic expressions
	 * @return a symbolic expression in which the symbolic constants have been
	 *         replaced by the corresponding expressions
	 */
	SymbolicExpressionIF substitute(SymbolicExpressionIF expression,
			Map<SymbolicConstantIF, SymbolicExpressionIF> map);

	// Numbers...

	/** The symbolic expression wrapping the given concrete number */
	SymbolicExpressionIF symbolic(NumberObject numberObject);

	/**
	 * Creates the integer symbolic expression with given int value.
	 * 
	 * @param value
	 *            a Java int
	 * @return a symbolic expression representing that concrete integer value
	 */
	SymbolicExpressionIF symbolic(int value);

	/** The symbolic expression representing the 0 integer value. */
	SymbolicExpressionIF zeroInt();

	/** The symbolic expression representing the real number 0. */
	SymbolicExpressionIF zeroReal();

	/** The symbolic expression representing the integer 1. */
	SymbolicExpressionIF oneInt();

	/** The symbolic expression representing the real number 1. */
	SymbolicExpressionIF oneReal();

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
	 * Returns a symbolic expression representing the sum of the given argument
	 * sequence.
	 * 
	 * @param args
	 *            a sequence of symbolic expressions of numeric type. They must
	 *            all have the same type.
	 * @return expression representing the sum
	 */
	SymbolicExpressionIF add(SymbolicCollection args);

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
	 * Returns symbolic expression representing the product of the given
	 * sequence of expressions.
	 * 
	 * @param args
	 *            symbolic expression sequence; all elements have the same
	 *            numeric type
	 * @return a symbolic expression representing the product
	 */
	SymbolicExpressionIF multiply(SymbolicCollection args);

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
	 * Concrete power operator: e^b, where b is a concrete non-negative integer.
	 * This method might actually multiply out the expression, i.e., it does not
	 * necessarily return an expression with operator POWER.
	 * 
	 * @param base
	 *            the base expression in the power expression
	 * @param exponent
	 *            a non-negative concrete integer exponent
	 */
	SymbolicExpressionIF power(SymbolicExpressionIF base, IntObject exponent);

	/**
	 * General power operator: e^b. Both e and b are numeric expressions.
	 * 
	 * @param base
	 *            the base expression in the power expression
	 * @param exponent
	 *            the exponent in the power expression
	 */
	SymbolicExpressionIF power(SymbolicExpressionIF base,
			SymbolicExpressionIF exponent);

	/** Casts a real or integer type expression to an expression of real type. */
	SymbolicExpressionIF castToReal(SymbolicExpressionIF numericExpression);

	/**
	 * Attempts to interpret the given symbolic expression as a concrete number.
	 * If this is not possible, returns null.
	 */
	NumberIF extractNumber(SymbolicExpressionIF expression);

	// Booleans...

	/**
	 * The symbolic expression wrapping the given boolean object (true or
	 * false).
	 */
	SymbolicExpressionIF symbolic(BooleanObject object);

	/**
	 * Short cut for symbolic(booleanObject(value)).
	 * 
	 * @param value
	 * @return symbolic expression wrapping boolean value
	 */
	SymbolicExpressionIF symbolic(boolean value);

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
	 * Returns a symbolic expression which represents the conjunction of the
	 * expressions in the given array args. Each expression in args must have
	 * boolean type. args must be non-null, and may have any length, including
	 * 0. If the length of args is 0, the resulting expression is equivalent to
	 * "true".
	 * 
	 * @param args
	 *            a sequence of expressions of boolean type
	 * @return the conjunction of the expressions in args
	 */
	SymbolicExpressionIF and(SymbolicCollection args);

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
	SymbolicExpressionIF or(SymbolicCollection args);

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
	 * Attempts to interpret the given symbolic expression as a concrete boolean
	 * value. If this is not possible, returns null.
	 */
	Boolean extractBoolean(SymbolicExpressionIF expression);

	// Functions...

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

	/**
	 * The result of applying an uninterpreted function to a sequence of
	 * arguments. The number and types of arguments must match the function's
	 * input signature.
	 */
	SymbolicExpressionIF apply(SymbolicExpressionIF function,
			SymbolicSequence argumentSequence);

	// Union type operations...

	/**
	 * Casts an object belonging to one of the members of a union type to the
	 * union type.
	 * 
	 * @param unionType
	 *            the union type
	 * @param memberIndex
	 *            the index of the member type of the object in the list of
	 *            member types of the union type
	 * @param object
	 *            an expression whose type is the member type with the given
	 *            index
	 * @return an expression whose type is the union type representing the same
	 *         object as the given object
	 */
	SymbolicExpressionIF unionInject(SymbolicUnionTypeIF unionType,
			IntObject memberIndex, SymbolicExpressionIF object);

	/**
	 * Tests whether an object of a union type belongs to the member type of the
	 * given index.
	 * 
	 * @param unionType
	 *            the union type
	 * @param memberIndex
	 *            an integer in range [0,n-1], where n is the number of member
	 *            types of the union type
	 * @param object
	 *            an expression of the union type
	 * @return a boolean expression telling whether the object belongs to the
	 *         specified member type
	 */
	SymbolicExpressionIF unionTest(SymbolicUnionTypeIF unionType,
			IntObject memberIndex, SymbolicExpressionIF object);

	/**
	 * Casts an object whose type is a union type to a representation whose type
	 * is the appropriate member type of the union type. The behavior is
	 * undefined if the object does not belong to the specified member type.
	 * 
	 * @param unionType
	 *            a union type
	 * @param memberIndex
	 *            an integer in range [0,n-1], where n is the number of member
	 *            types of the union types
	 * @param object
	 *            an object whose type is the union type and for which
	 *            unionTest(unionType, memberIndex, object) holds.
	 * @return a representation of the object with type the member type
	 */
	SymbolicExpressionIF unionExtract(SymbolicUnionTypeIF unionType,
			IntObject memberIndex, SymbolicExpressionIF object);

	// Arrays...

	/**
	 * Returns the concrete array consisting of given sequence of elements.
	 * 
	 * @param elements
	 *            sequence of symbolic expressions
	 * @return array consisting of those elements
	 */
	SymbolicExpressionIF array(SymbolicSequence elements);

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

	SymbolicExpressionIF denseArrayWrite(SymbolicExpressionIF array,
			SymbolicSequence values);

	/**
	 * Returns an expression representing an array with element type T defined
	 * by a function f from int to T.
	 */
	SymbolicExpressionIF arrayLambda(SymbolicCompleteArrayTypeIF arrayType,
			SymbolicExpressionIF function);

	// Tuples...

	/**
	 * Returns the concrete tuple expression with the given tuple components.
	 * 
	 * @param type
	 *            the tuple type
	 * @param components
	 *            the component expressions
	 * @return the tuple formed from the components
	 */
	SymbolicExpressionIF tuple(SymbolicTupleTypeIF type,
			SymbolicSequence components);

	/**
	 * Returns an expression that represents the result of reading a component
	 * in a tuple value. The index should be an integer-valued expression. The
	 * components are numbered starting from 0.
	 * 
	 * @param tuple
	 *            the tuple value being read
	 * @param index
	 *            index of the component to read
	 * @return a symbolic expression representing the component at that index
	 */
	SymbolicExpressionIF tupleRead(SymbolicExpressionIF tuple, IntObject index);

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
	SymbolicExpressionIF tupleWrite(SymbolicExpressionIF tuple,
			IntObject index, SymbolicExpressionIF value);

	// Misc. expressions...

	/**
	 * Casts expression to new type.
	 * 
	 * @param newType
	 *            a symbolic type
	 * @param expression
	 *            a symbolic expression
	 * @return symbolic expression cast to new type
	 * @exception
	 */
	SymbolicExpressionIF cast(SymbolicTypeIF newType,
			SymbolicExpressionIF expression);

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

	// Collections...

	/**
	 * Returns the empty set.
	 * 
	 * @return the empty set
	 */
	SymbolicSet emptySet();

	/**
	 * Returns the collection obtained by adding the given element to the given
	 * collection. If the given collection already contains that element, the
	 * collection returned will equal the given one.
	 * 
	 * @param set
	 *            any collection of symbolic expressions
	 * @param element
	 *            any symbolic expression
	 * @return collection obtained by adding element
	 */
	SymbolicSet add(SymbolicSet set, SymbolicExpressionIF element);

	/**
	 * Returns the union of the two sets.
	 * 
	 * @param set0
	 *            a set of symbolic expressions
	 * @param set1
	 *            a set of symbolic expressions
	 * @return their union
	 */
	SymbolicSet union(SymbolicSet set0, SymbolicSet set1);

	/**
	 * Returns the singleton set containing the one element.
	 * 
	 * @param element
	 *            a symbolic expression
	 * @return the set consisting of that one element
	 */
	SymbolicSet singleton(SymbolicExpressionIF element);

	/**
	 * Returns the set obtained by removing the given element from a set. If the
	 * given set does not contain the element, the set returns equals the given
	 * one.
	 * 
	 * @param set
	 *            a set of symbolic expressions
	 * @param element
	 *            a symbolic expressions
	 * @return set-{element}
	 */
	SymbolicSet remove(SymbolicSet set, SymbolicExpressionIF element);

	/**
	 * Returns the intersections of the sets.
	 * 
	 * @param set1
	 *            a set of symbolic expressions
	 * @param set2
	 *            a set of symbolic expressions
	 * @return the intersection of the two sets
	 */
	SymbolicSet intersection(SymbolicSet set1, SymbolicSet set2);

	/**
	 * Returns the set set1-set2, i.e., the set consisting of all x in set1 such
	 * that x is not in set2.
	 * 
	 * @param set1
	 *            a set of symbolic expressions
	 * @param set2
	 *            a set of symbolic expressions
	 * @return the set difference, set1-set2
	 */
	SymbolicSet removeAll(SymbolicSet set1, SymbolicSet set2);

	/**
	 * Returns a SymbolicExpressionSequenceIF comprising the given sequence of
	 * elements.
	 * 
	 * @param elements
	 *            any object providing an iterator over SymbolicExpressionIF
	 * @return a single SymbolicExpressionSequenceIF which wraps the given list
	 *         of elements
	 */
	SymbolicSequence sequence(Iterable<? extends SymbolicExpressionIF> elements);

	/**
	 * Returns a SymbolicExpressionSequenceIF comprising the sequence of
	 * elements specified as an array.
	 * 
	 * @param elements
	 *            any array of SymbolicExpressionIF
	 * @return a single SymbolicExpressionSequenceIF which wraps the given list
	 *         of elements
	 */
	SymbolicSequence sequence(SymbolicExpressionIF[] elements);

	/**
	 * Appends an element to the end of a sequence.
	 * 
	 * @param sequence
	 *            a symbolic sequence
	 * @param element
	 *            a symbolic expression
	 * @return a sequence identical to the given one except with the given
	 *         element added to the end
	 */
	SymbolicSequence add(SymbolicSequence sequence, SymbolicExpressionIF element);

	/**
	 * Sets the element at specified position. Sequence must have length at
	 * least index+1.
	 * 
	 * @param sequence
	 *            a symbolic sequence
	 * @param index
	 *            integer in range [0,n-1], where n is length of sequence
	 * @param element
	 *            a symbolic expression
	 * @return a sequence identical to old except that element in position index
	 *         now has value element
	 */
	SymbolicSequence set(SymbolicSequence sequence, int index,
			SymbolicExpressionIF element);

	/**
	 * Removes the element at position index, shifting all subsequent elements
	 * down one.
	 * 
	 * @param sequence
	 *            a symoblic sequence
	 * @param index
	 *            integer in range [0,n-1], where n is length of sequence
	 * @return a sequence obtained from given one by removing the element and
	 *         shifting remaining element down one in index
	 */
	SymbolicSequence remove(SymbolicSequence sequence, int index);

	/**
	 * Returns an empty symbolic map.
	 * 
	 * @return an empty symbolic map
	 */
	SymbolicMap emptyMap();

	/**
	 * Returns a symbolic map equivalent to the given one except that the entry
	 * for the given key is modified or created so to use the given value. An
	 * entry for the given key may or may not exist in the old map.
	 * 
	 * @param map
	 *            a symbolic map
	 * @param key
	 *            a symbolic expression key
	 * @param value
	 *            a symbolic expression value to associate to that key
	 * @return a map based on the original map but with the given value
	 *         associated to the given key
	 */
	SymbolicMap put(SymbolicMap map, SymbolicExpressionIF key,
			SymbolicExpressionIF value);

	/**
	 * Returns a symbolic map based on the given Java Map. The Java map should
	 * not be modified after this method is invoked.
	 * 
	 * @param javaMap
	 * @return a symbolic map based on the given Java map
	 */
	SymbolicMap map(Map<SymbolicExpressionIF, SymbolicExpressionIF> javaMap);

	/**
	 * Returns a map obtained by applying the given unary operator to the values
	 * of the given map, without changing the keys. If the unary operator
	 * returns null on an element, that entry is removed from the map.
	 * 
	 * @param map
	 *            a symbolic map
	 * @param operator
	 *            a unary operator on values
	 * @return a map obtained from the given one by applying operator to values
	 */
	SymbolicMap apply(SymbolicMap map, UnaryOperatorIF operator);

	/**
	 * Combines the two maps into a single map using the given binary operator.
	 * Iterates over the union of the key sets of the two maps. If a given key
	 * exists in only one map, the value associated to it in the new map is the
	 * same as the old value. If the key exists in two maps, the value is
	 * determined by applying the binary operator to the two old values. If the
	 * result of applying the binary operator is null, the element is removed
	 * from the map.
	 * 
	 * Examples:
	 * <ul>
	 * <li>adding polynomials: apply takes two monomials with same monic. adds
	 * coefficients. if coefficient is 0, returns null, else returns monomial
	 * with new coefficient and same monic</li>
	 * 
	 * <li>multiplying monics: apply takes two primitive powers with same
	 * primitive base. adds their exponents. Not possible to get null.</li>
	 * 
	 * <li>multiplying polynomial factorizations: like above</li>
	 * </ul>
	 * 
	 * Eventually would like efficient persistent implementation as in Clojure.
	 * 
	 * @param operator
	 *            a binary operator which can be applied to the values in the
	 *            maps
	 * @param map1
	 *            a symbolic map
	 * @param map2
	 *            a symbolic map
	 * @return a map obtained by combining the two given maps
	 */
	SymbolicMap combine(BinaryOperatorIF operator, SymbolicMap map1,
			SymbolicMap map2);
}
