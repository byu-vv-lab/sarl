/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.IF.number;

import java.math.BigInteger;

/**
 * A number factory is used to produce concrete rational and integer numbers.
 * The rational and integer numbers live in two separate worlds. Different
 * implementations of this interface may make different choices regarding
 * numerical precision, rounding policies, and other factors.
 * 
 * TODO: add methods for producing and manipulating intervals here.
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
	RationalNumber fraction(IntegerNumber numerator, IntegerNumber denominator);

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
	 * Returns a positive value if arg0 is greater than arg1, 0 if arg0 equals
	 * arg1, a negative value if arg0 is less than arg1.
	 */
	int compare(Number arg0, Number arg1);

	/**
	 * Performs Gaussian Elimination on a matrix of rationals, transforming the
	 * matrix to reduced row echelon form.
	 * 
	 * @param matrix
	 */
	void gaussianElimination(RationalNumber[][] matrix);

	/**
	 * Returns the rational number which is the quotient of the two given
	 * integers. It can of course be simplified.
	 * 
	 * @param numerator
	 *            any BigInteger
	 * @param denominator
	 *            any BigInteger
	 * @exception ArithmeticException
	 *                if denominator is zero
	 * @return numerator/denominator
	 */
	RationalNumber rational(BigInteger numerator, BigInteger denominator);

	/**
	 * Returns the IntegerNumber with value specified by the BigInteger. No
	 * precision is lost
	 * 
	 * @param big
	 *            any BigInteger
	 * @return the corresponding IntegerNumber
	 */
	IntegerNumber integer(BigInteger big);

	/**
	 * Returns the IntegerNumber with value specified by the long. No precision
	 * is lost.
	 * 
	 * @param value
	 *            any long
	 * @return the corresponding IntegerNumber
	 */
	IntegerNumber integer(long value);

	// Intervals...

	/**
	 * Returns the empty integer interval: (0,0).
	 * 
	 * @return empty integer interval
	 */
	Interval emptyIntegerInterval();

	/**
	 * Returns the empty real interval: (0.0, 0.0).
	 * 
	 * @return empty real interval
	 */
	Interval emptyRealInterval();

	/**
	 * Returns a new {@link Interval} as specified. A value of <code>null</code>
	 * for the lower bound represents negative infinity; for the upper bound it
	 * represents positive infinity.
	 * 
	 * Preconditions: the parameters must specify an interval in "normal form",
	 * i.e., the following must all hold:
	 * 
	 * <ul>
	 * <li>if the type is integral, then the upper and lower bounds must be
	 * instances of {@link IntegerNumber}, else they must be instances of
	 * {@link RationalNumber}</li>
	 * 
	 * <li>if the lower bound is <code>null</code>, <code>strictLower</code>
	 * must be <code>true</code>; if the upper bound is <code>null</code>,
	 * <code>strictUpper</code> must be <code>true</code></li>
	 * 
	 * <li>if both bounds are non-<code>null</code>, the lower bound must be
	 * less than or equal to the upper bound</li>
	 * 
	 * <li>if the bounds are non-<code>null</code> and equal: either (1) both
	 * <code>strictLower</code> and <code>strictUpper</code> will be
	 * <code>false</code>, or (2) <code>strictLower</code> and
	 * <code>strictUpper</code> will be <code>true</code> and the upper and
	 * lower bounds will be 0. The first case represents an interval consisting
	 * of a single point; the second case represents the empty interval.</li>
	 * 
	 * <li>if the type is integral: if the lower bound is non-<code>null</code>
	 * then <code>strictLower</code> will be <code>false</code>; if the upper
	 * bound is non-<code>null</code> then <code>strictUpper</code> will be
	 * <code>false</code>.</li>
	 * </ul>
	 * 
	 * @param isIntegral
	 *            does the interval have integer type (as opposed to real type)?
	 * @param lower
	 *            the lower bound of the interval
	 * @param strictLower
	 *            is the lower bound strict, i.e., "(", as opposed to "["?
	 * @param upper
	 *            the upper bound of the interval
	 * @param strictUpper
	 *            is the upper bound strict, i.e., ")", as opposed to "]"?
	 * @return a new interval object as specified
	 */
	Interval newInterval(boolean isIntegral, Number lower, boolean strictLower,
			Number upper, boolean strictUpper);

	/**
	 * Returns the interval which is the intersection of the two given
	 * intervals. The two given intervals must have the same type.
	 * 
	 * @param i1
	 *            an interval
	 * @param i2
	 *            an interval with same type (integer or real) as
	 *            <code>i1</code>
	 * @return the interval representing the intersection of <code>i1</code> and
	 *         <code>i2</code>
	 */
	Interval intersection(Interval i1, Interval i2);

	/**
	 * A simple type for recording the result of attempting to take the union of
	 * two intervals i1 and i2. There are three possibilities:
	 * <ol>
	 * <li>the union of the two intervals is an interval. In this case,
	 * <code>status=0</code> and <code>union</code> is the union of the two
	 * intervals.</li>
	 * <li>i1 is strictly less than i2 and the union is not an interval. In this
	 * case, <code>status<0</code> and <code>union</code> is <code>null</code>.</li>
	 * <li>i1 is strictly greater than i2 and the union is not an interval. In
	 * this case, <code>status>0</code> and <code>union</code> is
	 * <code>null</code>.
	 * </ol>
	 * 
	 * @author siegel
	 *
	 */
	public class IntervalUnion {
		public int status;
		public Interval union;
	};

	/**
	 * Computes the union of two intervals or reports that the union is not an
	 * interval and why. The result is stored in the <code>result</code> object.
	 * If the union of the two intervals is an interval,
	 * <code>result.status</code> will be set to 0 and <code>result.union</code>
	 * will hold the union interval. Otherwise, the status will be set to either
	 * a negative or positive integer and <code>result.union</code> will be set
	 * to <code>null</code>. A positive status indicates that every element of
	 * i1 is greater than every element of i2; a negative status indicates every
	 * element of i1 is less than every element of i2.
	 *
	 * @param i1
	 *            an interval
	 * @param i2
	 *            an interval of same type as <code>i1</code>
	 * @return an interval which is the union of <code>i1</code> and
	 *         <code>i2</code> or <code>null</code>
	 */
	void union(Interval i1, Interval i2, IntervalUnion result);

	/**
	 * Computes the affineTransform of the input interval <code>itv</code> with
	 * two numbers: <code>a</code> and <code>b</code> as parameters.
	 * 
	 * @param itv
	 *            an interval
	 * @param a
	 *            a number used to multiply with both upper and lower of
	 *            <code>itv</code>
	 * @param b
	 *            a number used to add to both upper and lower of
	 *            <code>itv</code>, after multiplying <code>a</code>.
	 * @return an interval which is affineTransform of <code>itv</code>
	 */
	Interval affineTransform(Interval itv, Number a, Number b);

	/**
	 * Returns 1 if <code>arg0</code> on the right side of <code>arg1</code>, 0
	 * if <code>arg0</code> is same with <code>arg1</code>, -1 if
	 * <code>arg0</code> on the left side of <code>arg1</code>.
	 * 
	 * @param arg0
	 *            an Interval
	 * @param arg1
	 *            an Interval
	 */
	int compare(Interval arg0, Interval arg1);
}
