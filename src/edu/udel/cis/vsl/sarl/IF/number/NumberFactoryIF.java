package edu.udel.cis.vsl.sarl.IF.number;

/**
 * A number factory is used to produce concrete rational and integer numbers.
 * The rational and integer numbers live in two separate worlds. Different
 * implementations of this interface may make different chocies regarding
 * numerical precision, rounding policies, and other factors.
 */
public interface NumberFactoryIF {

	/**
	 * Returns the integer number specified by the given string. The string
	 * could be something like "7583902" or "-1" or "0". There is no bound on
	 * the length of the string.
	 */
	IntegerNumberIF integer(String string);

	/** Returns an integer number corresponding to the given Java int. */
	IntegerNumberIF integer(int value);

	/**
	 * Returns the rational number specified by the given string, where the
	 * string is a decimal representation of the number. The string may be an
	 * integer string, such as "394" or "-1" or "0". Or it may contain a decimal
	 * point, as in "-3.1415" or "2." or ".234" or "-.234". There is no limit on
	 * the number of digits.
	 * 
	 */
	RationalNumberIF rational(String string);

	/** Makes best guest on type of number based on string. */
	NumberIF number(String string);

	/** Returns absolute value of number, preserving type. */
	NumberIF abs(NumberIF number);

	/**
	 * Returns true iff the rational number is an integer, e.g., "3.0", or
	 * "4/2". If this method returns true, it is then safe to invoke method
	 * integerValue() on the number.
	 */
	boolean isIntegral(RationalNumberIF arg0);

	/** Returns the rational number which is the quotient of the two integers. */
	RationalNumberIF fraction(IntegerNumberIF numerator,
			IntegerNumberIF denominator);

	/** Casts an integer to a rational number. */
	RationalNumberIF integerToRational(IntegerNumberIF integer);

	/** The rational number zero. */
	RationalNumberIF zeroRational();

	/** The rational number one. */
	RationalNumberIF oneRational();

	/** The integer number zero. */
	IntegerNumberIF zeroInteger();

	/** The integer number zero. */
	IntegerNumberIF oneInteger();

	/**
	 * Adds two numbers and returns result. The numbers must be of same type
	 * (integer or real), which is also the type of the result.
	 */
	NumberIF add(NumberIF arg0, NumberIF arg1);

	/**
	 * Subtracts two numbers and returns result. The numbers must be of same
	 * type (integer or real), which is also the type of the result.
	 */
	NumberIF subtract(NumberIF arg0, NumberIF arg1);

	/**
	 * Multiplies two numbers and returns result. The numbers must be of same
	 * type (integer or real), which is also the type of the result.
	 */
	NumberIF multiply(NumberIF arg0, NumberIF arg1);

	/**
	 * Divides two numbers and returns result. The numbers must be of same type
	 * (integer or real), which is also the type of the result.
	 */
	NumberIF divide(NumberIF arg0, NumberIF arg1);

	/**
	 * Negates the number, preserving the type (IntegerNumberIF or
	 * RationalNumberIF).
	 */
	NumberIF negate(NumberIF arg0);

	/** Adds two rational numbers and returns the result. */
	RationalNumberIF add(RationalNumberIF arg0, RationalNumberIF arg1);

	/** Subtracts two rational numbers and returns the result. */
	RationalNumberIF subtract(RationalNumberIF arg0, RationalNumberIF arg1);

	/** Multiplies two rational numbers and returns the result. */
	RationalNumberIF multiply(RationalNumberIF arg0, RationalNumberIF arg1);

	/**
	 * Divides two rational numbers and returns the result. An
	 * ArithmeticException is thrown if arg1 is zero.
	 */
	RationalNumberIF divide(RationalNumberIF arg0, RationalNumberIF arg1);

	/** Returns the negation of the given rational number, i.e., -x. */
	RationalNumberIF negate(RationalNumberIF arg0);

	/** Adds two integer numbers and returns the result. */
	IntegerNumberIF add(IntegerNumberIF arg0, IntegerNumberIF arg1);

	/** Subtracts two integer numbers and returns the result. */
	IntegerNumberIF subtract(IntegerNumberIF arg0, IntegerNumberIF arg1);

	/** Multiplies two integer numbers and returns the result. */
	IntegerNumberIF multiply(IntegerNumberIF arg0, IntegerNumberIF arg1);

	/**
	 * Divides two integer numbers and returns the result. Note that this is
	 * integer division. The result is obtained by taking the real quotient and
	 * rounding towards zero. An ArithmeticException is thrown if the
	 * denominator is zero.
	 */
	IntegerNumberIF divide(IntegerNumberIF arg0, IntegerNumberIF arg1);

	/**
	 * Modulo operations. Returns the result of arg0 % arg1.
	 */
	IntegerNumberIF mod(IntegerNumberIF arg0, IntegerNumberIF arg1);

	/** Returns the negation of the given integer number, i.e., -x. */
	IntegerNumberIF negate(IntegerNumberIF arg0);

	/** add(arg, 1.0) */
	RationalNumberIF increment(RationalNumberIF arg);

	/** add(arg, 1) */
	IntegerNumberIF increment(IntegerNumberIF arg);

	/** adds 1 of proper type */
	NumberIF increment(NumberIF arg);

	/** arg-1.0 */
	RationalNumberIF decrement(RationalNumberIF arg);

	/** arg - 1 */
	IntegerNumberIF decrement(IntegerNumberIF arg);

	/** subtracts 1 of proper type */
	NumberIF decrement(NumberIF arg);

	/**
	 * Returns the greatest common divisor of two integers. The two integers
	 * must be positive.
	 */
	IntegerNumberIF gcd(IntegerNumberIF arg0, IntegerNumberIF arg1);

	/** Returns the least common multiple of the two positive integers. */
	IntegerNumberIF lcm(IntegerNumberIF arg0, IntegerNumberIF arg1);

	/**
	 * Returns the numerator in a representation of the rational number as the
	 * quotient of two integers. This method is coordinated with method
	 * denominator so that the quotient of the numerator and denominator give
	 * the original rational number.
	 */
	IntegerNumberIF numerator(RationalNumberIF arg0);

	/**
	 * Returns the denominator in a representation of the rational number as the
	 * quotient of two integers. This method is coordinated with method
	 * numerator so that the quotient of the numerator and denominator give the
	 * original rational number.
	 */
	IntegerNumberIF denominator(RationalNumberIF arg0);

	/**
	 * Returns the value of the rational number as an integer number. Applies
	 * only to a rational number which is integral. I.e., the method
	 * isIntegral() must return true.
	 * 
	 * @exception ArithmeticException
	 *                if arg0 is not integral
	 */
	IntegerNumberIF integerValue(RationalNumberIF arg0);

	/**
	 * Returns the greatest integer less than or equal to the given rational
	 * number.
	 */
	IntegerNumberIF floor(RationalNumberIF arg0);

	/**
	 * Returns the least integer greater than or equal to the given rational
	 * number.
	 */
	IntegerNumberIF ceil(RationalNumberIF arg0);

	/**
	 * Returns a positive value if arg0>arg1, 0 if arg0 equals arg1, -1 if
	 * arg0<arg1.
	 */
	int compare(RationalNumberIF arg0, RationalNumberIF arg1);

	/**
	 * Returns a positive value if arg0>arg1, 0 if arg0 equals arg1, -1 if
	 * arg0<arg1.
	 */
	int compare(IntegerNumberIF arg0, IntegerNumberIF arg1);

	/* "Mixed" operations... */

	/**
	 * Returns a rational representation of the number. If the number already is
	 * rational, returns the number. Else casts from integer to rational.
	 */
	RationalNumberIF rational(NumberIF number);

	/**
	 * Returns a positive value if arg0>arg1, 0 if arg0 equals arg1, -1 if
	 * arg0<arg1.
	 */
	int compare(NumberIF arg0, NumberIF arg1);

	/**
	 * Performs Gaussian Elimination on a matrix of rationals, transforming the
	 * matrix to reduced row echelon form.
	 * 
	 * @param matrix
	 */
	void gaussianElimination(RationalNumberIF[][] matrix);

}
