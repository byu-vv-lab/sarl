package edu.udel.cis.vsl.sarl.preuniverse.IF;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.expr.ArrayElementReference;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.OffsetReference;
import edu.udel.cis.vsl.sarl.IF.expr.ReferenceExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.expr.TupleComponentReference;
import edu.udel.cis.vsl.sarl.IF.expr.UnionMemberReference;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.CharObject;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;

public interface PreUniverse {

	// General...

	/**
	 * Returns a comparator on the set of all symbolic objects. This defines a
	 * total order on all symbolic objects.
	 * 
	 * @return a comparator on all symbolic objects
	 */
	Comparator<SymbolicObject> comparator();

	/**
	 * Returns the unique representative instance from the given object's
	 * equivalence class, where the equivalence relation is determined by
	 * "equals". (Refer to the "Flyweight Pattern".)
	 * 
	 * A symbolic universe does not necessarily use the Flyweight Pattern on the
	 * symbolic objects it creates, but this method gives the user the option to
	 * use that pattern, in full or in part.
	 * 
	 * The methods in this universe which return symbolic expressions may return
	 * distinct instances which are equivalent (under "equals"). However,
	 * canonic is guaranteed to return a unique instance from each equivalence
	 * class, i.e., if a.equals(b) then canonic(a)==canonic(b).
	 * 
	 * @param object
	 *            a symbolic object
	 * @return canonical representative equal to object
	 */
	SymbolicObject canonic(SymbolicObject object);

	/**
	 * The result is the same as that of canonic on objects, but since the
	 * result of canonic on a SymbolicExpression must be a SymbolicExpression,
	 * this method is provided so that users don't have to cast.
	 * 
	 * @param expression
	 *            any symbolic expression belonging to this universe
	 * @return the canonic representative equal to that expression
	 */
	SymbolicExpression canonic(SymbolicExpression expression);

	/**
	 * Returns the number of canonic symbolic objects controlled by this
	 * universe.
	 * 
	 * @return the number of canonic symbolic objects
	 */
	int numObjects();

	/**
	 * Each canonic symbolic object is assigned a unique ID number. The numbers
	 * start from 0 and there are no gaps, i.e., they are in the range
	 * 0..numExpressions-1.
	 * 
	 * @return the canonic symbolic object with the given ID number.
	 */
	SymbolicObject objectWithId(int id);

	/**
	 * Returns the set of all canonic symbolic objects controlled by this
	 * universe as a Java Collection.
	 * 
	 * @return the set of all canonic symbolic objects
	 */
	Collection<SymbolicObject> objects();

	// Types...

	/**
	 * Returns a boolean expression which holds iff the two types are
	 * compatible. Two types are compatible if it is possible for them to have a
	 * value in common. For the most part, this is the same as saying they are
	 * the same type. The exception is that an incomplete array type and a
	 * complete array type with compatible element types are compatible.
	 * 
	 * @param type0
	 *            a type
	 * @param type1
	 *            a type
	 * @return a boolean expression which holds iff the two types are compatible
	 */
	BooleanExpression compatible(SymbolicType type0, SymbolicType type1);

	/**
	 * The boolean type.
	 * 
	 * @return the boolean type
	 * */
	SymbolicType booleanType();

	/**
	 * The "ideal" integer type, representing the set of mathematical integers.
	 * 
	 * @return the integer type
	 * */
	SymbolicIntegerType integerType();

	/**
	 * Returns the Herbrand integer type. All operations in which at least one
	 * argument has Herbrand integer type will be treated as uninterpreted
	 * functions: no simplifications or other transformations will be performed.
	 * 
	 * Note: to create a concrete number of herbrand integer type, create an
	 * ideal concrete integer then cast it to the herbrand type.
	 * 
	 * @return the Herbrand integer type
	 */
	SymbolicIntegerType herbrandIntegerType();

	/**
	 * Returns the bounded integer types with specified upper and lower bounds.
	 * Either of the bounds may be null, indication no bound (i.e., + or -
	 * infinity). If cyclic is true, then all operations treat the domain
	 * cyclically (i.e., max+1 = min).
	 * 
	 * NOTE: NOT YET IMPLEMENTED
	 * 
	 * @param min
	 *            smallest integer value in the domain or null
	 * @param max
	 *            largest integer value in the domain or null
	 * @param cyclic
	 *            should operations treat the domain cyclically?
	 * @return the bounded integer type as specified
	 */
	SymbolicIntegerType boundedIntegerType(NumericExpression min,
			NumericExpression max, boolean cyclic);

	/**
	 * The "ideal" real type, representing the set of mathematical real numbers.
	 * 
	 * @return the real type
	 * */
	SymbolicRealType realType();

	/**
	 * Returns the Herbrand real type. All operations in which at least one
	 * argument has Herbrand real type will be treated as uninterpreted
	 * functions: no simplifications or other transformations will be performed.
	 * Operations may involve mixed real and Herbrand real types, but the result
	 * will always be a Herbrand expression as long as at least one argument is
	 * Herbrand.
	 * 
	 * A Herbrand value and non-Herbrand value are always considered to be not
	 * equal, even if they are concrete expressions.
	 * 
	 * Note: to create a concrete number of herbrand real type, create an ideal
	 * concrete real then cast it to the herbrand type.
	 * 
	 * @return the Herbrand real type
	 */
	SymbolicRealType herbrandRealType();

	// TODO: floating point types

	SymbolicType characterType();

	/**
	 * Returns the complete array type with the given element type and extent
	 * (array length). Neither argument can be null.
	 * 
	 * @return the complete array type
	 */
	SymbolicCompleteArrayType arrayType(SymbolicType elementType,
			NumericExpression extent);

	/**
	 * Returns the incomplete array type with the given element type. The
	 * element type cannot be null.
	 * 
	 * @return the incomplete array type
	 */
	SymbolicArrayType arrayType(SymbolicType elementType);

	/**
	 * The tuple type defined by the given sequence of component types. The
	 * tuple type consists of all tuples of values (x_0,...,x_{n-1}), where
	 * x_{i} has type fieldsTypes_i. A tuple type also has a name, and two tuple
	 * types are not equal if they have unequal names.
	 * 
	 * @return the tuple type specified by the given name and field types
	 */
	SymbolicTupleType tupleType(StringObject name,
			Iterable<? extends SymbolicType> fieldTypes);

	/**
	 * Returns the specified function type. A function type is specified by a
	 * sequence of inputs types, and an output type.
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
	 * The type which is the union of the given member types. Say the member
	 * types are t_0,...,t_{n-1} and call the union type u. For 0<=i<n, there
	 * are functions inject_i: t_i -> u, extract_i: u -> t_i, and test_i: u ->
	 * {true,false}. The domain of u consists of all expressions of the form
	 * inject_i(x_i). extract_i(inject_i(x))=x and extract_i is undefined on any
	 * element of u that is not in the image of inject_i. test_i(x) is true iff
	 * x=inject_i(x_i) for some x_i in t_i.
	 * 
	 * @param name
	 *            the name of the union type
	 * @param memberTypes
	 *            the sequence of member types
	 * @return the specified union type
	 */
	SymbolicUnionType unionType(StringObject name,
			Iterable<? extends SymbolicType> memberTypes);

	/**
	 * Applies the given operator to the arguments and returns the resulting
	 * expression in the form used by this universe. The arguments should have
	 * the form required by the operator; see the documentation in the
	 * SymbolicExpression interface. The result returned should be identical to
	 * what would be returned by calling the specific methods (e.g., add(...)).
	 * 
	 * @param operator
	 *            a symbolic operator
	 * @param type
	 *            the type which the resulting expression should have (since it
	 *            may not be unambiguous)
	 * @param arguments
	 *            arguments which should be appropriate for the specified
	 *            operator
	 */
	SymbolicExpression make(SymbolicOperator operator, SymbolicType type,
			SymbolicObject[] arguments);

	/**
	 * The total number of valid calls made.
	 * 
	 * @return the total number of validity calls
	 */
	int numValidCalls();

	/**
	 * The total number of calls made to the validity method in the underlying
	 * automated theorem prover. This is in general smaller than that returned
	 * by {@link #numValidCalls}, as not every valid call requires a call to the
	 * prover.
	 * 
	 * @return the total number of theorem prover validity calls
	 */
	int numProverValidCalls();

	// Symbolic primitive objects: ints, boolean, reals, strings
	// Note: these are not symbolic expressions, just symbolic objects!

	/**
	 * Returns the BooleanObject wrapping the given boolean value. A
	 * BooleanObject is a SymbolicObject so can be used as an argument of a
	 * SymbolicExpression.
	 * 
	 * @param value
	 *            true or false
	 * @return the corresponding BooleanObject
	 */
	BooleanObject booleanObject(boolean value);

	/**
	 * Returns the IntObject wrapping the given Java int value. An IntObject is
	 * a SymbolicObject so can be used as an argument of a symbolic expression.
	 * It is used in cases where a "small" concrete integer is needed. For
	 * concrete integers of arbitrary size, use IntegerNumber instead and create
	 * a NumberObject.
	 * 
	 * @param value
	 *            any Java int
	 * @return an IntObject wrapping that value
	 */
	IntObject intObject(int value);

	/**
	 * Returns the NumberObject wrapping the given Number value. These are SARL
	 * Numbers, not Java Numbers. They are used to represent infinite precision,
	 * unbounded integers and rational numbers.
	 * 
	 * @param value
	 *            a SARL Number
	 * @return the NumberObject wrapping that Number
	 */
	NumberObject numberObject(Number value);
	/**
	 * Returns the charObject wrapping the given char value. These are SARL
	 * char, not Java char. 
	 * 
	 * @param value
	 *            a SARL char
	 * @return the CharObject wrapping that char
	 */
	CharObject charObject(char value);

	/**
	 * Returns the StringObject wrapping the given String value. A StringObject
	 * is a SymbolicObject so can be used as an argument to a SymbolicExpression
	 * 
	 * @param string
	 *            any Java String
	 * @return the StringObject wrapping that string
	 */
	StringObject stringObject(String string);

	// The "null" expression...

	/**
	 * Returns the "null" expression. This is a non-null (in the Java sense of
	 * "null") symbolic expression for which the method "isNull()" returns true.
	 * Its type is null.
	 * 
	 * @return the null expression
	 */
	SymbolicExpression nullExpression();

	// Symbolic constants...

	/**
	 * Returns the symbolic constant with the given name and type. Two symbolic
	 * constants are equal iff they have the same name and type. This method may
	 * use a Flyweight Pattern to return the same object if called twice with
	 * the same arguments. Or it may create a new object each time. These
	 * details are unimportant because symbolic constants are immutable.
	 * 
	 * This method will return the right kind of SymbolicConstant based on the
	 * type: if the type is numeric (integer or real), an instance of
	 * NumericSymbolicConstant will be returned. If the type is boolean, a
	 * BooleanSymbolicConstant will be returned.
	 * 
	 * @param name
	 *            the name to give to this symbolic constant; it will be used to
	 *            identify the object and for printing
	 * @param type
	 *            the type of this symbolic constant
	 */
	SymbolicConstant symbolicConstant(StringObject name, SymbolicType type);

	SymbolicExpression substitute(SymbolicExpression expression,
			Map<SymbolicExpression, SymbolicExpression> map);

	/**
	 * Substitutes symbolic expressions for symbolic constants in a symbolic
	 * expression.
	 * 
	 * Each occurrence of symbolic constant X in expression will be replaced
	 * with expression map(X), if map(X) is not null. The substitutions are all
	 * simultaneous.
	 * 
	 * Note that substitutions are not recursive, i.e., no substitutions are
	 * applied to the resulting expression after the first substitution.
	 * 
	 * Example: map={X->Y, Y->X}. expression=X/Y. result of substitution is Y/X.
	 * 
	 * @param expression
	 *            any symbolic expression
	 * @param map
	 *            a map from symbolic constants to symbolic expressions
	 * @return a symbolic expression in which the symbolic constants have been
	 *         replaced by the corresponding expressions
	 */
	SymbolicExpression substituteSymbolicConstants(
			SymbolicExpression expression,
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

	/**
	 * The symbolic expression representing the 0 integer value.
	 * 
	 * @return the integer 0 as a numeric symbolic expression
	 */
	NumericExpression zeroInt();

	/**
	 * The symbolic expression representing the integer 1.
	 * 
	 * @return the integer 1 as a numeric symbolic expression
	 */
	NumericExpression oneInt();

	/**
	 * Returns the integer symbolic expression with given int value.
	 * 
	 * @param value
	 *            a Java int
	 * @return the symbolic expression of integer type representing with that
	 *         value
	 */
	NumericExpression integer(int value);

	/**
	 * Returns the numeric symbolic expression with the given long value.
	 * 
	 * @param value
	 *            any Java long
	 * @return the symbolic expression of integer type with that value
	 */
	NumericExpression integer(long value);

	/**
	 * Returns the numeric symbolic expression with the given BigIntger value.
	 * The BigInteger class is a standard Java class for represeting integers of
	 * any size.
	 * 
	 * @param value
	 *            any BigInteger
	 * @return the symbolic expression of integer type with that value
	 */
	NumericExpression integer(BigInteger value);

	// Rationals...

	/**
	 * The symbolic expression representing the real number 0. Note that this is
	 * NOT equal to the integer number 0, since they have different types.
	 */
	NumericExpression zeroReal();

	/** The symbolic expression representing the real number 1. */
	NumericExpression oneReal();

	/**
	 * Creates a concrete expression from the given integer.
	 * 
	 * @param value
	 *            a Java integer type
	 * @return a concrete symbolic expression of real type representing the
	 *         integer
	 */

	NumericExpression rational(int value);

	/**
	 * Creates a concrete expression from the given long.
	 * 
	 * @param value
	 *            a Java long type
	 * @return a concrete symbolic expression of real type representing the long
	 */

	NumericExpression rational(long value);

	/**
	 * Creates a concrete expression from the given BigInteger.
	 * 
	 * @param value
	 *            a Java.Math BigInteger type
	 * @return a concrete symbolic expression of real type representing the
	 *         BigInteger
	 */

	NumericExpression rational(BigInteger value);

	/**
	 * Creates a concrete expression from the given float.
	 * 
	 * @param value
	 *            a Java float type
	 * @return a concrete symbolic expression of real type representing the
	 *         float
	 */

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
	 * Returns the symbolic concrete real number (numerator/denominator) by
	 * performing real division.
	 * 
	 * @param numerator
	 *            a Java int
	 * @param denominator
	 *            a Java int
	 * @return the real number formed by dividing numerator by denominator, as a
	 *         symbolic expression
	 */
	NumericExpression rational(int numerator, int denominator);

	/**
	 * Returns the symbolic concrete real number (numerator/denominator) by
	 * performing real division.
	 * 
	 * @param numerator
	 *            a Java long
	 * @param denominator
	 *            a Java long
	 * @return the real number formed by dividing numerator by denominator, as a
	 *         symbolic expression
	 */

	NumericExpression rational(long numerator, long denominator);

	/**
	 * Returns the symbolic concrete real number (numerator/denominator) by
	 * performing real division.
	 * 
	 * @param numerator
	 *            a Java.Math.BigInteger
	 * @param denominator
	 *            a Java.Math.BigInteger
	 * @return the real number formed by dividing numerator by denominator, as a
	 *         symbolic expression
	 */

	NumericExpression rational(BigInteger numerator, BigInteger denominator);

	// General Numbers...

	/** The number factory used by this universe. */
	NumberFactory numberFactory();

	/**
	 * The concrete symbolic expression wrapping the given number. The type of
	 * the expression will be the ideal integer or real type, determined by the
	 * kind of number (IntegerNumber or RationalNumber).
	 * 
	 * @param number
	 *            any sarl Number
	 * @return the symbolic expression wrapping that number
	 */
	NumericExpression number(Number number);

	/** The symbolic expression wrapping the given concrete number object. */
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

	// Character and string...
	/**
	 * 
	 * @param char
	 *            
	 * @return SymbolicExpression char by given char type theChar
	 */
	SymbolicExpression character(char theChar);
	/**
	 * 
	 * @param String
	 *            
	 * @return SymbolicExpression String by given String type theString
	 */
	SymbolicExpression stringExpression(String theString);

	Character extractCharacter(SymbolicExpression expression);

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
	 * @return expression representing the sum
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
	 * @return an expression representing arg0 % arg1
	 */
	NumericExpression modulo(NumericExpression arg0, NumericExpression arg1);

	/**
	 * Returns a symbolic expression which is the result of subtracting the
	 * given two numerical expressions. The given expression must be non-null
	 * and have either integer or real type.
	 * 
	 * @param arg0
	 *            a symbolic expression of integer or real type
	 * @param arg1
	 *            a symbolic expression of integer or real type
	 * @return an expression representing ar0 - arg1
	 */
	NumericExpression minus(NumericExpression arg);

	/**
	 * Concrete power operator: e^b, where b is a concrete non-negative integer.
	 * This method might actually multiply out the expression, i.e., it does not
	 * necessarily return an expression with operator POWER.
	 * 
	 * @param base
	 *            the base expression
	 * @param exponent
	 *            a non-negative concrete IntObject exponent
	 * @return the expression after applying power operation
	 */
	NumericExpression power(NumericExpression base, IntObject exponent);

	/**
	 * Concrete power operator: e^b, where b is a concrete non-negative integer.
	 * This method might actually multiply out the expression, i.e., it does not
	 * necessarily return an expression with operator POWER.
	 * 
	 * @param base
	 *            the base expression
	 * @param exponent
	 *            a non-negative concrete integer exponent
	 * @return the expression after applying power operation
	 * 
	 */
	NumericExpression power(NumericExpression base, int exponent);

	/**
	 * Concrete power operator: e^b, where b is a concrete non-negative integer.
	 * This method might actually multiply out the expression, i.e., it does not
	 * necessarily return an expression with operator POWER.
	 * 
	 * @param base
	 *            the base expression
	 * @param exponent
	 *            the exponent expression
	 * @return the expression after applying power operation
	 */
	NumericExpression power(NumericExpression base, NumericExpression exponent);

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

	/**
	 * Returns the boolean literal "true".
	 * 
	 * @return the symbolic expression "true"
	 */
	BooleanExpression trueExpression();

	/**
	 * Returns the boolean literal "false".
	 * 
	 * @return the symbolic expression "false"
	 */
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
	 * Does the integer a divide the integer b evenly? I.e, does there exist an
	 * integer n such that b=a*n?
	 * 
	 * @param a
	 *            a symbolic expression of integer type
	 * @param b
	 *            a symbolic expression of integer type
	 * @return a symbolic expression of boolean type holding iff a divides b
	 */
	BooleanExpression divides(NumericExpression a, NumericExpression b);

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
	 * Appends an element to the end of a concrete symbolic array. Returns a new
	 * array expression which is same as old with new element appended to end.
	 * 
	 * @param concreteArray
	 *            a concrete array
	 * @param element
	 *            a symbolic expression whose type is compatible with element
	 *            type of the array
	 * @return an array obtained by appending element to given array
	 */
	SymbolicExpression append(SymbolicExpression concreteArray,
			SymbolicExpression element);

	/**
	 * Remove an element from array at the given position. Returns a new array
	 * expression which is same as old except the removed element won't be
	 * included
	 * 
	 * @param concreteArray
	 *            the given concrete array
	 * @param index
	 *            the position where the element will be deleted
	 * 
	 * 
	 * @return an array obtained by removing element from given array
	 */

	SymbolicExpression removeElementAt(SymbolicExpression concreteArray,
			int index);

	/**
	 * Returns an array of length 0.
	 * 
	 * @param elementType
	 *            the type of the non-existent elements of this array
	 * @return an empty array with length 0 of given type
	 */
	SymbolicExpression emptyArray(SymbolicType elementType);

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
	 *            symbolic expression of integer type representing the position
	 *            of the element within the array
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
	 *            symbolic expression of integer type representing the position
	 *            of the element within the array
	 * @param value
	 *            the new value for the element at position index
	 * @return expression representing the result of changing the index-th
	 *         element to value
	 */
	SymbolicExpression arrayWrite(SymbolicExpression array,
			NumericExpression index, SymbolicExpression value);

	/**
	 * Returns an array obtained by performing a sequence of writes, given in a
	 * "dense" format, to an array. The sequence of values are used to write to
	 * the indexes 0, 1, .... A null element in the sequence is simply ignored.
	 * 
	 * @param array
	 *            a symbolic expression of array type
	 * @param values
	 *            a sequence of symbolic expressions, each of which is either
	 *            null or a symbolic expression of the appropriate type
	 * @return the array resulting from writing to the given array in position
	 *         0,...,n-1, where n is the length of the sequence.
	 */
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
	 */
	SymbolicExpression cast(SymbolicType newType, SymbolicExpression expression);

	/**
	 * "If-then-else" expression. Note that trueCase and falseCase must have the
	 * same type, which becomes the type of this expression.
	 * 
	 * @param predicate
	 *            the test condition p
	 * @param trueCase
	 *            the value if condition is true
	 * @param falseCase
	 *            the value if condition is false
	 * @return symbolic expression whose values is trueCase if predicate holds,
	 *         falseCase if predicate is false
	 */
	SymbolicExpression cond(BooleanExpression predicate,
			SymbolicExpression trueCase, SymbolicExpression falseCase);

	// References...

	/** Returns the type of all reference expressions. */
	SymbolicType referenceType();

	/**
	 * Returns the "null reference", a symbolic expression of reference type
	 * which is not equal to a reference value returned by any of the other
	 * methods, and which cannot be dereferenced.
	 */
	ReferenceExpression nullReference();

	/**
	 * Given a reference and a value, returns the sub-expression of value
	 * specified by the reference. Throws exception if the reference is not of
	 * the correct form for the type of value.
	 *  @param value
	 *  		a SymbolicExpression which you would like to dereference from
	 *  @param reference
	 *  		a ReferenceExpression which is compatible with the value
	 *  		and points to the sub-expression you would like to dereference.
	 */
	SymbolicExpression dereference(SymbolicExpression value,
			ReferenceExpression reference);

	/**
	 * Returns the type referenced by a reference into an object of the given
	 * type. You can only use this method to get the type referenced into a
	 * SymbolicType, not a SymbolicExpression. Example: if type is array-of-int
	 * and the reference is an array element reference, this method returns int.
	 * 
	 * @param type
	 *            a symbolic type
	 * @param reference
	 *            a reference that is compatible with that type, i.e., can
	 *            reference into a value of that type
	 * @return the component of the given type which is referenced by the given
	 *         reference
	 */
	SymbolicType referencedType(SymbolicType type, ReferenceExpression reference);

	/**
	 * Returns the identity (or "trivial") reference I. This is the reference
	 * characterized by the property that dereference(I,v) returns v for any
	 * symbolic expression v.
	 */
	ReferenceExpression identityReference();

	/**
	 * Given a reference to an array and an index (integer), returns a reference
	 * to the element of the array at that index
	 */
	ArrayElementReference arrayElementReference(
			ReferenceExpression arrayReference, NumericExpression index);

	/**
	 * Given a reference to a tuple, and a field index, returns a reference to
	 * that component of the tuple
	 */
	TupleComponentReference tupleComponentReference(
			ReferenceExpression tupleReference, IntObject fieldIndex);

	/**
	 * Given a reference to a union (expression of union type) and an index of a
	 * member type of that union, returns a reference to the underlying element
	 */
	UnionMemberReference unionMemberReference(
			ReferenceExpression unionReference, IntObject memberIndex);

	OffsetReference offsetReference(ReferenceExpression reference,
			NumericExpression offset);

	/**
	 * Given a symbolic expression value, a reference to a point within that
	 * value, and a subValue, returns the symbolic expression obtained by
	 * replacing the referenced part of value with subValue.
	 */
	SymbolicExpression assign(SymbolicExpression value,
			ReferenceExpression reference, SymbolicExpression subValue);

	void incrementValidCount();

	void incrementProverValidCount();

	/**
	 * Given an iterable collection of SymbolicTypes, returns a
	 * SymbolicTypeSequence conatining those SymbolicTypes
	 * 
	 * @param SymbolicType
	 *            - types
	 * @return SymbolicTypeSequence of SymbolicType - types
	 */
	SymbolicTypeSequence typeSequence(Iterable<? extends SymbolicType> types);

	<T extends SymbolicExpression> SymbolicCollection<T> basicCollection(
			Collection<T> javaCollection);

	SymbolicType pureType(SymbolicType type);

	/**
	 * Changes the names of the bound variables in the expression so that every
	 * bound variable has a unique name. The names will be unique among ALL
	 * bound variables ever encountered by this method in this preuniverse.
	 * 
	 * @param expr
	 *            a symbolic expressions
	 * @return a symbolic expression equivalent to expr but with the names of
	 *         the bound variables possibly changed to be unique
	 */
	SymbolicExpression cleanBoundVariables(SymbolicExpression expr);

}
