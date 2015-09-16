package edu.udel.cis.vsl.sarl.IF;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

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
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicMapType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicSetType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;

public interface CoreUniverse {

	// General...

	/**
	 * Gets the show queries flag: if true, SARL theorem prover queries will be
	 * printed to the output stream.
	 * 
	 * @return current value of the show queries flag
	 */
	boolean getShowQueries();

	/**
	 * Sets the show queries flag. If this is set to true, SARL theorem prover
	 * queries will be printed to the output stream.
	 * 
	 * @param value
	 *            new value for the show queries flag.
	 */
	void setShowQueries(boolean value);

	/**
	 * Gets the show prover queries flag: if true, the theorem prover queries
	 * processed by the underlying theorem prover(s) will be printed to the
	 * output stream.
	 * 
	 * @return current value of the show prover queries flag
	 */
	boolean getShowProverQueries();

	/**
	 * Sets the show prover queries flag. If set to true, the theorem prover
	 * queries processed by the underlying theorem prover(s) will be printed to
	 * the output stream.
	 * 
	 * @param value
	 *            new value for the show theorem prover queries flag
	 */
	void setShowProverQueries(boolean value);

	/**
	 * Returns the output stream to which information (such as queries) will be
	 * printed. By default, standard out.
	 * 
	 * @return current output stream
	 */
	PrintStream getOutputStream();

	/**
	 * Sets the output stream, the stream to which information (such as queries)
	 * will be printed. By default, standard out.
	 * 
	 * @param out
	 *            new value for output stream
	 */
	void setOutputStream(PrintStream out);

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
	 * Returns the "pure" version of the type, i.e., the type obtained by making
	 * every array type incomplete. This is applied recusively down all
	 * components of the type tree.
	 */
	SymbolicType pureType(SymbolicType type);

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
	 * Note: to create a concrete number of Herbrand integer type, create an
	 * ideal concrete integer then cast it to the Herbrand type.
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

	/**
	 * Returns the character type.
	 * 
	 * @return the character type
	 */
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
	 * Under construction.
	 * 
	 * @param elementType
	 * @return
	 */
	SymbolicSetType setType(SymbolicType elementType);

	/**
	 * Under construction.
	 * 
	 * @param keyType
	 * @param valueType
	 * @return
	 */
	SymbolicMapType mapType(SymbolicType keyType, SymbolicType valueType);

	/**
	 * Returns a tuple type which has two components: component 0 is the key
	 * type of the map type; component 1 is the value type of the map type. This
	 * is the type of an "entry" in the map. This type is used by the method
	 * {@link #entrySet(SymbolicExpression)}, which returns the set of entries
	 * of a map.
	 * 
	 * @param mapType
	 *            a map type
	 * @return the type of an entry in the map
	 */
	SymbolicTupleType entryType(SymbolicMapType mapType);

	// Other...

	/**
	 * Returns a substituter for which the base substitutions are specified by
	 * an explicit Java {@link Map}. The map specifies a set of key-value pairs.
	 * The substituter will replace and key with its corresponding value; all
	 * other substitutions are determined from those "base" cases by recursive
	 * application of substitution.
	 * 
	 * @param map
	 *            a map which specifies that a key should be replaced by the
	 *            corresponding value
	 * @return a substituter based on the given map
	 */
	UnaryOperator<SymbolicExpression> mapSubstituter(
			Map<SymbolicExpression, SymbolicExpression> map);

	/**
	 * Returns a substituter specified by a mapping of old names to new names
	 * for symbolic constants. Any symbolic constant appearing as a key in the
	 * map will be replaced by a similar one with name the corresponding value.
	 * This includes bound symbolic constants.
	 * 
	 * @param nameMap
	 *            mapping of old to new names for symbolic constants
	 * @return a substituter which replaces symbolic constants as specified by
	 *         <code>nameMap</code>
	 */
	UnaryOperator<SymbolicExpression> nameSubstituter(
			Map<StringObject, StringObject> nameMap);

	/**
	 * Returns a substituter that replaces a specific symbolic constant with
	 * some specific value. The value must have a type that is compatible with
	 * that of the symbolic constant.
	 * 
	 * @param var
	 *            the symbolic constant
	 * @param value
	 *            the value that will replace the symbolic constant
	 * @return a substituter that will replace any occurrence of
	 *         <code>var</code> with <code>value</code>
	 */
	UnaryOperator<SymbolicExpression> simpleSubstituter(SymbolicConstant var,
			SymbolicExpression value);

	/**
	 * <p>
	 * Returns an operator on {@link SymbolicExpression}s that replaces all
	 * symbolic constants with (including bound ones) with symbolic constants
	 * with unique canonical names. The names are formed by appending the
	 * integers 0, 1, ..., to <code>root</code>. The renamer has state, so it
	 * can be used repeatedly (applied to multiple symbolic expressions) and
	 * will continue to generate new names for the new symbolic constants it
	 * encounters if they have not been encountered before. Every fresh binding
	 * of a bound variable is considered to be new, so is given a unique new
	 * name.
	 * </p>
	 * 
	 * <p>
	 * The parameter <code>ignore</code> also provides a way to specify that
	 * certain symbolic constants should be ignored, i.e., they should be
	 * renamed.
	 * </p>
	 * 
	 * @param root
	 *            the string that forms the root of the names of the new
	 *            symbolic constants
	 * @param ignore
	 *            a predicate providing a method that takes a
	 *            {@link SymbolicConstant} and returns <code>true</code> or
	 *            <code>false</code>; if it returns <code>true</code>, then that
	 *            symbolic constant should <strong>not</strong> be renamed
	 * @return a unary operator which take a symbolic expression and returns a
	 *         symbolic expression in which the symbolic constants have been
	 *         assigned canonical names
	 */
	UnaryOperator<SymbolicExpression> canonicalRenamer(String root,
			Predicate<SymbolicConstant> ignore);

	/**
	 * Returns an operator on {@link SymbolicExpression}s that replaces all
	 * symbolic constants with (including bound ones) with symbolic constants
	 * with unique canonical names. The names are formed by appending the
	 * integers 0, 1, ..., to <code>root</code>. The renamer has state, so it
	 * can be used repeatedly (applied to multiple symbolic expressions) and
	 * will continue to generate new names for the new symbolic constants it
	 * encounters if they have not been encountered before. Every fresh binding
	 * of a bound variable is considered to be new, so is given a unique new
	 * name.
	 * 
	 * <p>
	 * Equivalent to invoking {@link #canonicalRenamer(String, Predicate)} with
	 * <code>ignore</code> the constant predicate <code>false</code>.
	 * </p>
	 * 
	 * @param root
	 *            the string that forms the root of the names of the new
	 *            symbolic constants
	 * @return a unary operator which take a symbolic expression and returns a
	 *         symbolic expression in which the symbolic constants have been
	 *         assigned canonial names
	 */
	UnaryOperator<SymbolicExpression> canonicalRenamer(String root);

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
	 * The total number of calls made to methods
	 * {@link Reasoner#valid(BooleanExpression)} and
	 * {@link Reasoner#validOrModel(BooleanExpression)}.
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
	 * 
	 * @param value
	 *            an Java int
	 * @return a concrete rational representation of this integer value.
	 *         basically returns (value/1), which is a rational
	 */
	NumericExpression rational(int value);

	/**
	 * 
	 * @param value
	 *            a Java long
	 * @return a concrete rational representation of this long value. basically
	 *         returns (value/1), which is a rational
	 */
	NumericExpression rational(long value);

	/**
	 * 
	 * @param value
	 *            a Java BigInteger
	 * @return a concrete rational representation of this BigInteger value.
	 *         basically returns (value/1), which is a rational
	 */
	NumericExpression rational(BigInteger value);

	/**
	 * 
	 * @param value
	 *            a Java float
	 * @return a concrete rational representation of this float value. basically
	 *         returns (value/1), which is a rational
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

	/**
	 * Returns the symbolic concrete real number numerator/denominator (real
	 * division).
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
	 * Returns the symbolic concrete real number numerator/denominator (real
	 * division).
	 * 
	 * @param numerator
	 *            a Java BigInteger
	 * @param denominator
	 *            a Java BigInteger
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

	// Characters and string expressions:

	/**
	 * Returns a concrete symbolic expression of character type which wraps the
	 * given Java char.
	 * 
	 * @param theChar
	 *            the Java char
	 * @return symbolic expression wrapping theChar
	 */
	SymbolicExpression character(char theChar);

	/**
	 * If the given expression is a concrete character expression, this returns
	 * the character value, else it returns null.
	 * 
	 * @param expression
	 *            a symbolic expression
	 * @return the character is wraps or null
	 */
	Character extractCharacter(SymbolicExpression expression);

	/**
	 * Returns a symbolic expression of type array of char which is a literal
	 * array consisting of the sequence of characters in the given string.
	 * 
	 * @param theString
	 *            a Java string
	 * @return the string represented as a symbolic expression of type
	 *         array-of-char
	 */
	SymbolicExpression stringExpression(String theString);

	// Numeric operations...

	/**
	 * Returns a symbolic expression which is the result of adding the two given
	 * symbolic expressions. The two given expressions must have the same
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
	 * given symbolic expressions. The two given expressions must have the same
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
	 * Allows CnfFactory to simplify expensive (p || !p) expressions as they are
	 * created. Default is false. Setting to true will decrease performance in
	 * certain CnfFactory methods.
	 * 
	 * @param boolean value: false = default
	 * 
	 * @return void
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
	 * Returns array of length 0.
	 * 
	 * @param elementType
	 *            the type of the non-existent elements of this array
	 * @return array of length 0 of given type
	 */
	SymbolicExpression emptyArray(SymbolicType elementType);

	/**
	 * Returns an array in which every element has the same value.
	 * 
	 * @param elementType
	 *            the element type of the array
	 * @param length
	 *            the length of the array
	 * @param value
	 *            the value of each element
	 * @return an array of specified length in which every element is value
	 */
	SymbolicExpression constantArray(SymbolicType elementType,
			NumericExpression length, SymbolicExpression value);

	/**
	 * Appends an element to the end of a concrete symbolic array. Returns a new
	 * array expression which is same as old with new element appended to end.
	 * 
	 * TODO: extend to arbitrary arrays, not just symbolic
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
	 * Removes an element in a specified position in a concrete symbolic array.
	 * Returns a new array which is same as old except the element has been
	 * removed and the remaining elements have been shifted down to remove the
	 * gap. The resulting array has length 1 less than the original one.
	 * 
	 * TODO: extend to arbitrary arrays, not just concrete
	 * 
	 * @param concreteArray
	 *            a concrete array
	 * @param index
	 *            an int index
	 * @return array obtained by removing element at specified index
	 * @throws SARLException
	 *             if index is negative or greater than or equal to the length
	 *             of the given concrete array
	 */
	SymbolicExpression removeElementAt(SymbolicExpression concreteArray,
			int index);

	/**
	 * Inserts value an position index in array, shifting subsequence elements
	 * "up".
	 * 
	 * @param concreteArray
	 *            a concrete array
	 * @param index
	 *            an int index in the range [0,length], where length is the
	 *            length of the original array. If index=length, this is the
	 *            same as append.
	 * @param value
	 *            expression to insert
	 * @return array obtained by inserting the element at specified index
	 */
	SymbolicExpression insertElementAt(SymbolicExpression concreteArray,
			int index, SymbolicExpression value);

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

	// Sets...
	// Under construction...

	SymbolicExpression emptySet(SymbolicSetType setType);

	SymbolicExpression singletonSet(SymbolicSetType setType,
			SymbolicExpression value);

	BooleanExpression isElementOf(SymbolicExpression value,
			SymbolicExpression set);

	BooleanExpression isSubsetOf(SymbolicExpression set1,
			SymbolicExpression set2);

	SymbolicExpression setAdd(SymbolicExpression set, SymbolicExpression value);

	SymbolicExpression setRemove(SymbolicExpression set,
			SymbolicExpression value);

	SymbolicExpression setUnion(SymbolicExpression set1, SymbolicExpression set2);

	SymbolicExpression setIntersection(SymbolicExpression set1,
			SymbolicExpression set2);

	SymbolicExpression setDifference(SymbolicExpression set1,
			SymbolicExpression set2);

	NumericExpression cardinality(SymbolicExpression set);

	// Maps...

	SymbolicExpression emptyMap(SymbolicMapType mapType);

	SymbolicExpression put(SymbolicExpression map, SymbolicExpression key,
			SymbolicExpression value);

	SymbolicExpression get(SymbolicExpression map, SymbolicExpression key);

	SymbolicExpression removeEntryWithKey(SymbolicExpression map,
			SymbolicExpression key);

	SymbolicExpression keySet(SymbolicExpression map);

	NumericExpression mapSize(SymbolicExpression map);

	/**
	 * Returns the entry set of the map. This is the set consisting of all
	 * ordered pairs (key,value) for each entry in the map. Each entry is a
	 * symbolic expression which has a tuple type. The tuple type has two
	 * components: component 0 is the key type, component 1 the value type.
	 * 
	 * @param map
	 * @return
	 */
	SymbolicExpression entrySet(SymbolicExpression map);

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

	// References

	/**
	 * <p>
	 * Returns the type of all reference expressions. A reference expression is
	 * a kind of symbolic expression used to represent a reference to a
	 * subexpression of other expressions. It may be thought of as a sequence of
	 * directions for navigating to a particular node in a tree, starting from
	 * the root. For example, a reference expression <i>r</i> might encode
	 * "the 3rd element of the 2nd component". Given an expression <i>e</i> of
	 * tuple type in which the 2nd component has array type, that <i>r</i>
	 * specifies a particular element of a particular component of <i>e</i>.
	 * </p>
	 * 
	 * <p>
	 * A reference may also be thought of as a <i>function</i> which takes a
	 * symbolic expression (of a compatible type) and returns a sub-expression
	 * of that expression.
	 * </p>
	 * 
	 * @return the type of all reference expressions
	 */
	SymbolicType referenceType();

	/**
	 * Returns the "null reference", a symbolic expression of reference type
	 * which is not equal to a reference value returned by any of the other
	 * methods, and which cannot be dereferenced.
	 * 
	 * @return the null reference
	 */
	ReferenceExpression nullReference();

	/**
	 * Given a <code>reference</code> and a <code>value</code>, returns the
	 * sub-expression of <code>value</code> specified by the reference. Throws
	 * exception if the reference is not of the correct form for the type of
	 * <code>value</code>.
	 * 
	 * @param value
	 *            a non-<code>null</code> symbolic expression
	 * @param reference
	 *            a non-<code>null</code> reference into a sub-expression of
	 *            <code>value</code>
	 * @return the sub-expression of <code>value</code> specified by the
	 *         reference
	 */
	SymbolicExpression dereference(SymbolicExpression value,
			ReferenceExpression reference);

	/**
	 * Returns the type referenced by a reference into an expression of the
	 * given type. Example: if <code>type</code> is <i>array-of-integer</i> and
	 * <code>reference</code> is an array element reference, this method returns
	 * <i>integer</i>.
	 * 
	 * @param type
	 *            a non-<code>null</code> symbolic type
	 * @param reference
	 *            a reference that is compatible with <code>type</code>, i.e.,
	 *            can reference into an expression of that type
	 * @return the component of the given type which is referenced by the given
	 *         reference
	 */
	SymbolicType referencedType(SymbolicType type, ReferenceExpression reference);

	/**
	 * Returns the identity (or "trivial") reference <code>I</code>. This is the
	 * reference characterized by the property that
	 * <code>dereference(I,v)</code> returns <code>v</code> for any symbolic
	 * expression <code>v</code>.
	 * 
	 * @return the identity reference
	 */
	ReferenceExpression identityReference();

	/**
	 * Given a reference to an array and an <code>index</code> (integer),
	 * returns a reference to the element of the array at that index. Think of
	 * this as tacking on one more instruction to the sequence of directions
	 * specified by a reference. For example, if <code>arrayReference</code>
	 * encodes "2nd component of element 3" and <code>index</code> is
	 * <code>X+Y</code>, the result returned specifies "element <code>X+Y</code>
	 * of the 2nd component of element 3".
	 * 
	 * @param arrayReference
	 *            a non-<code>null</code> reference for which the referenced
	 *            sub-expression has array type
	 * @param index
	 *            a non-<code>null</code> expression of integer type
	 * @return a reference to the <code>index</code>-th element of the
	 *         referenced array
	 */
	ArrayElementReference arrayElementReference(
			ReferenceExpression arrayReference, NumericExpression index);

	/**
	 * Given a reference to a tuple, and a field index, returns a reference to
	 * that component of the tuple. Think of this as tacking on one more
	 * instruction to the sequence of directions specified by a reference. For
	 * example, if <code>tupleReference</code> encodes
	 * "2nd component of element 3" and <code>fieldIndex</code> is 15, the
	 * result returned specifies "the 15-th component of the 2nd component of
	 * element 3".
	 * 
	 * @param tupleReference
	 *            a non-<code>null</code> reference for which the referenced
	 *            sub-expression has tuple type
	 * @param fieldIndex
	 *            a non-<code>null</code> concrete integer object specifying the
	 *            component of the tuple (indexed from 0)
	 * @return a reference to the <code>fieldIndex</code>-th element of the
	 *         referenced tuple
	 */
	TupleComponentReference tupleComponentReference(
			ReferenceExpression tupleReference, IntObject fieldIndex);

	/**
	 * Given a reference to a union (expression of union type) and an index of a
	 * member type of that union, returns a reference to the underlying element.
	 */
	UnionMemberReference unionMemberReference(
			ReferenceExpression unionReference, IntObject memberIndex);

	OffsetReference offsetReference(ReferenceExpression reference,
			NumericExpression offset);

	/**
	 * Given a symbolic expression value, a reference to a point within that
	 * value, and a subValue, returns the symbolic expression obtained by
	 * replacing the referenced part of value with subValue.
	 * 
	 * @param value
	 *            a non-<code>null</code> symbolic expression
	 * @param reference
	 *            a non-<code>null</code> reference to a subexpression of
	 *            <code>value</code>
	 * @param subValue
	 *            a non-<code>null</code> expression with type compatible with
	 *            that of the referenced sub-expression of <code>value</code>
	 * @return the expression that results from taking <code>value</code> and
	 *         replacing the referenced sub-expression with
	 *         <code>subValue</code>
	 */
	SymbolicExpression assign(SymbolicExpression value,
			ReferenceExpression reference, SymbolicExpression subValue);

	/**
	 * Returns the set of unbound symbolic constants occurring in an expression.
	 * Each symbolic constant will occur at most once in the collection
	 * returned. This includes symbolic constants that occur in types (for
	 * example, array lengths).
	 * 
	 * @param expr
	 *            a non-<code>null</code> symbolic expression
	 * @return set of unbound symbolic constants occurring in <code>expr</code>
	 */
	Set<SymbolicConstant> getFreeSymbolicConstants(SymbolicExpression expr);
}
