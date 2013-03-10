package edu.udel.cis.vsl.sarl.IF;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.prove.TheoremProver;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;

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
public interface SymbolicUniverse {

	// Types...

	/** The boolean type. */
	SymbolicType booleanType();

	/** The integer type, representing the set of mathematical integers. */
	SymbolicType integerType();

	/** The real type, representing the set of mathematical real numbers. */
	SymbolicType realType();

	/**
	 * Returns the complete array type with the given element type and extent
	 * (array length). Neither argument can be null.
	 */
	SymbolicCompleteArrayType arrayType(SymbolicType elementType,
			NumericExpression extent);

	/**
	 * Returns the incomplete array type with the given element type. The
	 * element type cannot be null.
	 */
	SymbolicArrayType arrayType(SymbolicType elementType);

	/**
	 * The tuple type defined by the given sequence of component types. The
	 * tuple type consists of all tuples of values (x_0,...,x_{n-1}), where
	 * x_{i} has type fieldsTypes[i]. A tuple type also has a name, and two
	 * tuple types are not equal if they have unequal names.
	 */
	SymbolicTupleType tupleType(StringObject name,
			Iterable<? extends SymbolicType> fieldTypes);

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
	SymbolicFunctionType functionType(
			Iterable<? extends SymbolicType> inputTypes, SymbolicType outputType);

	/**
	 * The type which is the union of the given types.
	 * 
	 * @param name
	 * @param memberTypes
	 * @return
	 */
	SymbolicUnionType unionType(StringObject name,
			Iterable<? extends SymbolicType> memberTypes);

	// Symbolic Objects...

	// General...

	Comparator<SymbolicObject> comparator();

	/**
	 * Returns the unique representative instance from the given object's
	 * equivalence class, where the equivalence relation is determined by
	 * "equals".
	 * 
	 * @param object
	 *            a symbolic object
	 * @return canonical representative equal to object
	 */
	SymbolicObject canonic(SymbolicObject object);

	/** Returns the number of symbolic objects controlled by this universe. */
	int numObjects();

	/**
	 * Returns the symbolic expression with the given ID (in the range
	 * 0..numExpressions-1).
	 */
	SymbolicObject objectWithId(int index);

	/**
	 * Returns the set of all canonic symbolic objects controlled by this
	 * universe.
	 */
	Collection<SymbolicObject> objects();

	/**
	 * Applies the given operator to the arguments and returns the resulting
	 * expression in the canonic form used by this universe. The arguments
	 * should have the form required by the operator; see the documentation in
	 * the SymbolicExpression interface.
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
	SymbolicExpression make(SymbolicOperator operator, SymbolicType type,
			SymbolicObject[] arguments);

	/**
	 * Returns a simplifier object for the given assumption. The simplifier
	 * provides a method that takes a symbolic expression and returns an
	 * equivalent simplified expression, assuming the assumption holds. The
	 * assumption is any boolean-valued symbolic expression in this universe.
	 */
	Simplifier simplifier(BooleanExpression assumption);

	/**
	 * Returns the theorem prover associated to this universe.
	 * 
	 * @return the theorem prover
	 */
	TheoremProver prover();

	// Symbolic primitive objects: ints, boolean, reals, strings
	// Note: these are not symbolic expressions, just symbolic objects!

	BooleanObject booleanObject(boolean value);

	IntObject intObject(int value);

	NumberObject numberObject(Number value);

	StringObject stringObject(String string);

	// Symbolic constants...

	/**
	 * Returns the symbolic constant with the given name and type. Two symbolic
	 * constants are equal iff they have the same name and type. This method may
	 * use a Flyweight Pattern to return the same object if called twice with
	 * the same arguments. Or it may create a new object each time. These
	 * details are unimportant because symbolic constants are immutable.
	 */
	SymbolicConstant symbolicConstant(StringObject name, SymbolicType type);

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
	SymbolicExpression substitute(SymbolicExpression expression,
			Map<SymbolicConstant, SymbolicExpression> map);

	/**
	 * Performs substitution of a single symbolic constant. This is for
	 * convenience; it is a special case of the method that takes an entire map.
	 * 
	 * @param expression
	 *            the expression in which substitution should take place
	 * @param variable
	 *            the symbolic constant which should be replaced
	 * @param value
	 *            the thing that replaces the variable
	 * @return the expression that results from performing the substitution on
	 *         the given expression
	 */
	SymbolicExpression substitute(SymbolicExpression expression,
			SymbolicConstant variable, SymbolicExpression value);

	// Integers...

	/** The symbolic expression representing the 0 integer value. */
	NumericExpression zeroInt();

	/** The symbolic expression representing the integer 1. */
	NumericExpression oneInt();

	/**
	 * Creates the integer symbolic expression with given int value.
	 * 
	 * @param value
	 *            a Java int
	 * @return a symbolic expression representing that concrete integer value
	 */
	NumericExpression integer(int value);

	NumericExpression integer(long value);

	NumericExpression integer(BigInteger value);

	// Rationals...

	/** The symbolic expression representing the real number 0. */
	NumericExpression zeroReal();

	/** The symbolic expression representing the real number 1. */
	NumericExpression oneReal();

	NumericExpression rational(int value);

	NumericExpression rational(long value);

	NumericExpression rational(BigInteger value);

	NumericExpression rational(float value);

	/**
	 * Creates a concrete expression from the given double based on the string
	 * representation of the double. Since the double is a finite-precision
	 * floating-point value, be aware that this translation might not yield the
	 * exact real number you expect.
	 * 
	 * @param value
	 *            a Java double
	 * @return a concrete symbolic expression of real type representing the
	 *         double
	 */
	NumericExpression rational(double value);

	/**
	 * Returns the symbolic concrete real number numerator/denominator (real
	 * division).
	 * 
	 * @param numerator
	 *            a Java int
	 * @param denominator
	 *            a Java int
	 * @return the real number formed by dividing numerator by denominator, as a
	 *         symbolic expression
	 */
	NumericExpression rational(int numerator, int denominator);

	NumericExpression rational(long numerator, long denominator);

	NumericExpression rational(BigInteger numerator, BigInteger denominator);

	// General Numbers...

	/** The number factory used by this universe. */
	NumberFactory numberFactory();

	NumericExpression number(Number number);

	/** The symbolic expression wrapping the given concrete number */
	NumericExpression number(NumberObject numberObject);

	/**
	 * Returns the Number value if the given symbolic expression has a concrete
	 * numerical value, else returns null.
	 * 
	 * @param expression
	 *            a symbolic expression
	 * @return Number value or null
	 */
	Number extractNumber(NumericExpression expression);

	// Numeric operations...

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
	NumericExpression add(NumericExpression arg0, NumericExpression arg1);

	/**
	 * Returns a symbolic expression representing the sum of the given argument
	 * sequence.
	 * 
	 * @param args
	 *            a sequence of symbolic expressions of numeric type. They must
	 *            all have the same type.
	 * @return expression representing the sum
	 */
	NumericExpression add(Iterable<? extends NumericExpression> args);

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
	NumericExpression subtract(NumericExpression arg0, NumericExpression arg1);

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
	NumericExpression multiply(NumericExpression arg0, NumericExpression arg1);

	/**
	 * Returns symbolic expression representing the product of the given
	 * sequence of expressions.
	 * 
	 * @param args
	 *            symbolic expression sequence; all elements have the same
	 *            numeric type
	 * @return a symbolic expression representing the product
	 */
	NumericExpression multiply(Iterable<? extends NumericExpression> args);

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
	NumericExpression divide(NumericExpression arg0, NumericExpression arg1);

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
	NumericExpression modulo(NumericExpression arg0, NumericExpression arg1);

	/**
	 * Returns a symbolic expression which is the negative of the given
	 * numerical expression. The given expression must be non-null and have
	 * either integer or real type.
	 * 
	 * @param arg
	 *            a symbolic expression of integer or real type
	 * @return -arg
	 */
	NumericExpression minus(NumericExpression arg);

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
	NumericExpression power(NumericExpression base, IntObject exponent);

	/**
	 * Shorthand for power(base, intObject(exponent)).
	 * 
	 * @param base
	 *            he base expression in the power expression
	 * 
	 * @param exponent
	 *            a non-negative concrete integer exponent
	 * @return power(base, intObject(exponent))
	 */
	NumericExpression power(NumericExpression base, int exponent);

	/**
	 * General power operator: e^b. Both e and b are numeric expressions.
	 * 
	 * @param base
	 *            the base expression in the power expression
	 * @param exponent
	 *            the exponent in the power expression
	 */
	NumericExpression power(NumericExpression base, NumericExpression exponent);

	/** Casts a real or integer type expression to an expression of real type. */
	NumericExpression castToReal(NumericExpression numericExpression);

	// Booleans...

	/**
	 * If the given expression has a concrete boolean value, this returns it,
	 * else it returns null.
	 * 
	 * @param expression
	 *            any BooleanExpression
	 * @return one of the two concrete Boolean values, if possible, else false
	 */
	Boolean extractBoolean(BooleanExpression expression);

	BooleanExpression trueExpression();

	BooleanExpression falseExpression();

	/**
	 * The symbolic expression wrapping the given boolean object (true or
	 * false).
	 */
	BooleanExpression bool(BooleanObject object);

	/**
	 * Short cut for symbolic(booleanObject(value)).
	 * 
	 * @param value
	 * @return symbolic expression wrapping boolean value
	 */
	BooleanExpression bool(boolean value);

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
	BooleanExpression and(Iterable<? extends BooleanExpression> args);

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
	BooleanExpression lessThan(NumericExpression arg0, NumericExpression arg1);

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
	BooleanExpression lessThanEquals(NumericExpression arg0,
			NumericExpression arg1);

	/**
	 * Returns expression equivalent to arg0 = arg1 ("equals"). This is a
	 * general equals operator (not just for numeric expressions). To be equal,
	 * the arguments must have equal types. The notion of equals then depends on
	 * the particular type.
	 * 
	 * @param arg0
	 *            a symbolic expression
	 * @param arg1
	 *            a symbolic expression
	 * @return symbolic expression of boolean type arg0 = arg1
	 */
	BooleanExpression equals(SymbolicExpression arg0, SymbolicExpression arg1);

	/**
	 * Returns expression equivalent to arg0 != arg1 ("not equals").
	 * 
	 * @param arg0
	 *            a symbolic expression
	 * @param arg1
	 *            a symbolic expression
	 * @return symbolic expression of boolean type arg0 != arg1
	 */
	BooleanExpression neq(SymbolicExpression arg0, SymbolicExpression arg1);

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
	 * A special case of "forall" that is very common: forall integers i such
	 * that low<=i<high, p(i).
	 * 
	 * @param index
	 *            i, a symbolic constant of integer type
	 * @param low
	 *            a symbolic expression of integer type, lower bound of i
	 *            (inclusive)
	 * @param high
	 *            a symbolic expression of integer type, upper bound of i
	 *            (exclusive)
	 * @param predicate
	 *            some boolean symbolic expression, usually involving i
	 * @return an expression equivalent to "forall int i. low<=i<high -> p(i)".
	 */
	BooleanExpression forallInt(NumericSymbolicConstant index,
			NumericExpression low, NumericExpression high,
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

	/**
	 * A special case of "exists" that is very common: exists integer i such
	 * that low<=i<high and p(i).
	 * 
	 * @param index
	 *            i, a symbolic constant of integer type
	 * @param low
	 *            a symbolic expression of integer type, lower bound of i
	 *            (inclusive)
	 * @param high
	 *            a symbolic expression of integer type, upper bound of i
	 *            (exclusive)
	 * @param predicate
	 *            some boolean symbolic expression, usually involving i
	 * @return an expression equivalent to "exists int i. low<=i<high && p(i)".
	 */
	BooleanExpression existsInt(NumericSymbolicConstant index,
			NumericExpression low, NumericExpression high,
			BooleanExpression predicate);

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
	SymbolicExpression lambda(SymbolicConstant boundVariable,
			SymbolicExpression expression);

	/**
	 * The result of applying an uninterpreted function to a sequence of
	 * arguments. The number and types of arguments must match the function's
	 * input signature.
	 */
	SymbolicExpression apply(SymbolicExpression function,
			Iterable<? extends SymbolicExpression> argumentSequence);

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
	SymbolicExpression unionInject(SymbolicUnionType unionType,
			IntObject memberIndex, SymbolicExpression object);

	/**
	 * Tests whether an object of a union type is in the image of injection from
	 * the member type of the given index.
	 * 
	 * @param memberIndex
	 *            an integer in range [0,n-1], where n is the number of member
	 *            types of the union type
	 * @param object
	 *            an expression of the union type
	 * @return a boolean expression telling whether the object belongs to the
	 *         specified member type
	 */
	BooleanExpression unionTest(IntObject memberIndex, SymbolicExpression object);

	/**
	 * Casts an object whose type is a union type to a representation whose type
	 * is the appropriate member type of the union type. The behavior is
	 * undefined if the object does not belong to the specified member type.
	 * 
	 * @param memberIndex
	 *            an integer in range [0,n-1], where n is the number of member
	 *            types of the union types
	 * @param object
	 *            an object whose type is the union type and for which
	 *            unionTest(unionType, memberIndex, object) holds.
	 * @return a representation of the object with type the member type
	 */
	SymbolicExpression unionExtract(IntObject memberIndex,
			SymbolicExpression object);

	// Arrays...

	/**
	 * Returns the concrete array consisting of given sequence of elements. The
	 * type of the array will be the complete array type determined by the
	 * element type and the number of elements.
	 * 
	 * @param elementType
	 *            the type of each element of the array
	 * @param elements
	 *            sequence of symbolic expressions
	 * @return array consisting of those elements
	 */
	SymbolicExpression array(SymbolicType elementType,
			Iterable<? extends SymbolicExpression> elements);

	/**
	 * Returns the length of any symbolic expression of array type. This is a
	 * symbolic expression of integer type.
	 * 
	 * @param array
	 *            a symbolic expression of array type
	 * @return a symbolic expression of integer type representing the length of
	 *         the array
	 */
	NumericExpression length(SymbolicExpression array);

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
	SymbolicExpression arrayRead(SymbolicExpression array,
			NumericExpression index);

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
	SymbolicExpression arrayWrite(SymbolicExpression array,
			NumericExpression index, SymbolicExpression value);

	SymbolicExpression denseArrayWrite(SymbolicExpression array,
			Iterable<? extends SymbolicExpression> values);

	/**
	 * Returns an expression representing an array with element type T defined
	 * by a function f from int to T.
	 */
	SymbolicExpression arrayLambda(SymbolicCompleteArrayType arrayType,
			SymbolicExpression function);

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
	SymbolicExpression tuple(SymbolicTupleType type,
			Iterable<? extends SymbolicExpression> components);

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
	SymbolicExpression tupleRead(SymbolicExpression tuple, IntObject index);

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
	SymbolicExpression tupleWrite(SymbolicExpression tuple, IntObject index,
			SymbolicExpression value);

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
	SymbolicExpression cast(SymbolicType newType, SymbolicExpression expression);

	/**
	 * "If-then-else" expression. Note that trueCase and falseCase must have the
	 * same type, which becomes the type of this expression.
	 * 
	 * @param predicate
	 *            the test condition p
	 * @param trueValue
	 *            the value if condition is true
	 * @param falseValue
	 *            the value if condition is false
	 * @return symbolic expression whose values is trueCase if predicate holds,
	 *         falseCase if predicate is false
	 */
	SymbolicExpression cond(BooleanExpression predicate,
			SymbolicExpression trueCase, SymbolicExpression falseCase);

}
