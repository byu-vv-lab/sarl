package edu.udel.cis.vsl.sarl.IF.number;

/**
 * A number factory is used to produce concrete rational and integer numbers.
 * The rational and integer numbers live in two separate worlds. Different
 * implementations of this interface may make different chocies regarding
 * numerical precision, rounding policies, and other factors.
 */
public interface NumberFactory {

	/**
	 * Returns the integer number specified by the given string. The string
	 * could be something like "7583902" or "-1" or "0". There is no bound on
	 * the length of the string.
	 */
	IntegerNumber integer(String string);

	/** Returns an integer number corresponding to the given Java int. */
	IntegerNumber integer(int value);

	/**
	 * Returns the rational number specified by the given string, where the
	 * string is a decimal representation of the number. The string may be an
	 * integer string, such as "394" or "-1" or "0". Or it may contain a decimal
	 * point, as in "-3.1415" or "2." or ".234" or "-.234". There is no limit on
	 * the number of digits.
	 * 
	 */
	RationalNumber rational(String string);

	/** Makes best guest on type of number based on string. */
	Number number(String string);

	/** Returns absolute value of number, preserving type. */
	Number abs(Number number);

	/**
	 * Returns true iff the rational number is an integer, e.g., "3.0", or
	 * "4/2". If this method returns true, it is then safe to invoke method
	 * integerValue() on the number.
	 */
	boolean isIntegral(RationalNumber arg0);

	/** Returns the rational number which is the quotient of the two integers. */
	RationalNumber fraction(IntegerNumber numerator,
			IntegerNumber denominator);

	/** Casts an integer to a rational number. */
	RationalNumber integerToRational(IntegerNumber integer);

	/** The rational number zero. */
	RationalNumber zeroRational();

	/** The rational number one. */
	RationalNumber oneRational();

	/** The integer number zero. */
	IntegerNumber zeroInteger();

	/** The integer number zero. */
	IntegerNumber oneInteger();

	/**
	 * Adds two numbers and returns result. The numbers must be of same type
	 * (integer or real), which is also the type of the result.
	 */
	Number add(Number arg0, Number arg1);

	/**
	 * Subtracts two numbers and returns result. The numbers must be of same
	 * type (integer or real), which is also the type of the result.
	 */
	Number subtract(Number arg0, Number arg1);

	/**
	 * Multiplies two numbers and returns result. The numbers must be of same
	 * type (integer or real), which is also the type of the result.
	 */
	Number multiply(Number arg0, Number arg1);

	/**
	 * Divides two numbers and returns result. The numbers must be of same type
	 * (integer or real), which is also the type of the result.
	 */
	Number divide(Number arg0, Number arg1);

	/**
	 * Negates the number, preserving the type (IntegerNumberIF or
	 * RationalNumberIF).
	 */
	Number negate(Number arg0);

	/** Adds two rational numbers and returns the result. */
	RationalNumber add(RationalNumber arg0, RationalNumber arg1);

	/** Subtracts two rational numbers and returns the result. */
	RationalNumber subtract(RationalNumber arg0, RationalNumber arg1);

	/** Multiplies two rational numbers and returns the result. */
	RationalNumber multiply(RationalNumber arg0, RationalNumber arg1);

	/**
	 * Divides two rational numbers and returns the result. An
	 * ArithmeticException is thrown if arg1 is zero.
	 */
	RationalNumber divide(RationalNumber arg0, RationalNumber arg1);

	/** Returns the negation of the given rational number, i.e., -x. */
	RationalNumber negate(RationalNumber arg0);

	/** Adds two integer numbers and returns the result. */
	IntegerNumber add(IntegerNumber arg0, IntegerNumber arg1);

	/** Subtracts two integer numbers and returns the result. */
	IntegerNumber subtract(IntegerNumber arg0, IntegerNumber arg1);

	/** Multiplies two integer numbers and returns the result. */
	IntegerNumber multiply(IntegerNumber arg0, IntegerNumber arg1);

	/**
	 * Divides two integer numbers and returns the result. Note that this is
	 * integer division. The result is obtained by taking the real quotient and
	 * rounding towards zero. An ArithmeticException is thrown if the
	 * denominator is zero.
	 */
	IntegerNumber divide(IntegerNumber arg0, IntegerNumber arg1);

	/**
	 * Modulo operations. Returns the result of arg0 % arg1.
	 */
	IntegerNumber mod(IntegerNumber arg0, IntegerNumber arg1);

	/** Returns the negation of the given integer number, i.e., -x. */
	IntegerNumber negate(IntegerNumber arg0);

	/** add(arg, 1.0) */
	RationalNumber increment(RationalNumber arg);

	/** add(arg, 1) */
	IntegerNumber increment(IntegerNumber arg);

	/** adds 1 of proper type */
	Number increment(Number arg);

	/** arg-1.0 */
	RationalNumber decrement(RationalNumber arg);

	/** arg - 1 */
	IntegerNumber decrement(IntegerNumber arg);

	/** subtracts 1 of proper type */
	Number decrement(Number arg);

	/**
	 * Returns the greatest common divisor of two integers. The two integers
	 * must be positive.
	 */
	IntegerNumber gcd(IntegerNumber arg0, IntegerNumber arg1);

	/** Returns the least common multiple of the two positive integers. */
	IntegerNumber lcm(IntegerNumber arg0, IntegerNumber arg1);

	/**
	 * Returns the numerator in a representation of the rational number as the
	 * quotient of two integers. This method is coordinated with method
	 * denominator so that the quotient of the numerator and denominator give
	 * the original rational number.
	 */
	IntegerNumber numerator(RationalNumber arg0);

	/**
	 * Returns the denominator in a representation of the rational number as the
	 * quotient of two integers. This method is coordinated with method
	 * numerator so that the quotient of the numerator and denominator give the
	 * original rational number.
	 */
	IntegerNumber denominator(RationalNumber arg0);

	/**
	 * Returns the value of the rational number as an integer number. Applies
	 * only to a rational number which is integral. I.e., the method
	 * isIntegral() must return true.
	 * 
	 * @exception ArithmeticException
	 *                if arg0 is not integral
	 */
	IntegerNumber integerValue(RationalNumber arg0);

	/**
	 * Returns the greatest integer less than or equal to the given rational
	 * number.
	 */
	IntegerNumber floor(RationalNumber arg0);

	/**
	 * Returns the least integer greater than or equal to the given rational
	 * number.
	 */
	IntegerNumber ceil(RationalNumber arg0);

	/**
	 * Returns a positive value if arg0>arg1, 0 if arg0 equals arg1, -1 if
	 * arg0<arg1.
	 */
	int compare(RationalNumber arg0, RationalNumber arg1);

	/**
	 * Returns a positive value if arg0>arg1, 0 if arg0 equals arg1, -1 if
	 * arg0<arg1.
	 */
	int compare(IntegerNumber arg0, IntegerNumber arg1);

	/* "Mixed" operations... */

	/**
	 * Returns a rational representation of the number. If the number already is
	 * rational, returns the number. Else casts from integer to rational.
	 */
	RationalNumber rational(Number number);

	/**
	 * Returns a positive value if arg0>arg1, 0 if arg0 equals arg1, -1 if
	 * arg0<arg1.
	 */
	int compare(Number arg0, Number arg1);

	/**
	 * Performs Gaussian Elimination on a matrix of rationals, transforming the
	 * matrix to reduced row echelon form.
	 * 
	 * @param matrix
	 */
	void gaussianElimination(RationalNumber[][] matrix);

}
