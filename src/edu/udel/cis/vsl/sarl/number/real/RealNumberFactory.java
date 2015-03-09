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
package edu.udel.cis.vsl.sarl.number.real;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.util.BinaryOperator;

/**
 * An implementation of number factory based on infinite precision real
 * arithmetic.
 */
public class RealNumberFactory implements NumberFactory {

	private Map<BigInteger, RealInteger> integerMap = new HashMap<BigInteger, RealInteger>();

	private Map<RationalKey, RealRational> rationalMap = new HashMap<RationalKey, RealRational>();

	private RealInteger zeroInteger, oneInteger, tenInteger;

	private RealRational zeroRational, oneRational;

	private BinaryOperator<IntegerNumber> multiplier;

	private Exponentiator<IntegerNumber> exponentiator;

	/**
	 * The empty integer interval: (0,0).
	 */
	private Interval emptyIntegerInterval;

	/**
	 * The empty real interval: (0.0, 0.0).
	 */
	private Interval emptyRealInterval;

	/**
	 * Uses a new factory to multiply two integer arguments.
	 */
	class IntMultiplier implements BinaryOperator<IntegerNumber> {
		private RealNumberFactory factory;

		IntMultiplier(RealNumberFactory factory) {
			this.factory = factory;
		}

		@Override
		public IntegerNumber apply(IntegerNumber arg0, IntegerNumber arg1) {
			return factory.multiply(arg0, arg1);
		}

	}

	public RealNumberFactory() {
		zeroInteger = integer(BigInteger.ZERO);
		oneInteger = integer(BigInteger.ONE);
		tenInteger = integer(BigInteger.TEN);
		zeroRational = fraction(zeroInteger, oneInteger);
		oneRational = fraction(oneInteger, oneInteger);
		multiplier = new IntMultiplier(this);
		emptyIntegerInterval = new CommonInterval(true, zeroInteger, true,
				zeroInteger, true);
		emptyRealInterval = new CommonInterval(false, zeroRational, true,
				zeroRational, true);
	}

	@Override
	/**
	 * See interface javadoc. Returns absolute value of a Number.
	 * 
	 */
	public Number abs(Number number) {
		if (number.signum() < 0) {
			return negate(number);
		} else {
			return number;
		}
	}

	@Override
	/**
	 * See interface. This takes in a BigInteger, and returns an IntegerNumber.
	 * 
	 */
	public RealInteger integer(BigInteger big) {
		RealInteger oldValue = integerMap.get(big);

		if (oldValue != null) {
			return oldValue;
		} else {
			RealInteger newValue = new RealInteger(big);

			integerMap.put(big, newValue);
			return newValue;
		}
	}

	@Override
	/**
	 * Returns the BigInteger interpretation of a long.
	 */
	public RealInteger integer(long value) {
		return integer(BigInteger.valueOf(value));
	}

	@Override
	/**
	 * Returns a RealRational formed from given BigInteger numerator and denominator.
	 * Detects and protects against zero valued denominators.
	 * Moves any negation to the numerator.
	 * Simplifies the RealRational
	 * If numerator equals zero, simplifies to 0/1 regardless of denominator.
	 */
	public RealRational rational(BigInteger numerator, BigInteger denominator) {
		int signum = denominator.signum();
		RationalKey key;
		RealRational oldValue;

		if (signum == 0) {
			throw new ArithmeticException("Division by 0");
		}
		// ensures any negation is in numerator
		// protects signum method in RealRational
		if (signum < 0) {
			numerator = numerator.negate();
			denominator = denominator.negate();
		}
		// interesting statement. replaces any denominator with one when
		// numerator is zero.
		if (numerator.signum() == 0) {
			denominator = BigInteger.ONE;
		} else {
			BigInteger gcd = numerator.gcd(denominator);

			numerator = numerator.divide(gcd);
			denominator = denominator.divide(gcd);
		}
		key = new RationalKey(numerator, denominator);
		oldValue = rationalMap.get(key);
		if (oldValue != null) {
			return oldValue;
		} else {
			RealRational newValue = new RealRational(numerator, denominator);

			rationalMap.put(key, newValue);
			return newValue;
		}
	}

	/**
	 * Returns true when a RealRational's denominator is equal to one.
	 * 
	 * @param arg0
	 * @return
	 */
	public boolean isIntegral(RealRational arg0) {
		return arg0.denominator().equals(BigInteger.ONE);
	}

	@Override
	/**
	 * An efficient way of adding two RationalNumbers.
	 */
	public RationalNumber add(RationalNumber arg0, RationalNumber arg1) {
		RealRational x = (RealRational) arg0;
		RealRational y = (RealRational) arg1;

		return rational(
				x.numerator().multiply(y.denominator())
						.add(x.denominator().multiply(y.numerator())), x
						.denominator().multiply(y.denominator()));
	}

	@Override
	/**
	 * An override of the add function to add two integers with precision
	 */
	public IntegerNumber add(IntegerNumber arg0, IntegerNumber arg1) {
		RealInteger x = (RealInteger) arg0;
		RealInteger y = (RealInteger) arg1;

		return integer(x.value().add(y.value()));
	}

	@Override
	/**
	 * returns an Integer of the quotient of numerator and denominator
	 */
	public IntegerNumber ceil(RationalNumber arg0) {
		RealRational x = (RealRational) arg0;
		BigInteger numerator = x.numerator();
		BigInteger denominator = x.denominator();
		BigInteger quotient = numerator.divide(denominator);

		if (numerator.signum() <= 0) {
			return integer(quotient);
		} else {
			BigInteger modulus = numerator.mod(denominator);

			if (modulus.equals(BigInteger.ZERO)) {
				return integer(quotient);
			} else {
				return integer(quotient.add(BigInteger.ONE));
			}
		}
	}

	@Override
	/**
	 * Determines the larger of two rationals
	 * returns 1 when the first argument is greater
	 * returns 0 when the rationals are equal
	 * returns -1 when the second argument is greater
	 */
	public int compare(RationalNumber arg0, RationalNumber arg1) {
		return subtract(arg0, arg1).signum();
	}

	@Override
	/**
	 * Determines the larger of two integers
	 * returns 1 when first argument is greater
	 * returns 0 when arguments are equal
	 * returns -1 when second argument is greater
	 */
	public int compare(IntegerNumber arg0, IntegerNumber arg1) {
		return subtract(arg0, arg1).signum();
	}

	@Override
	/**
	 * Takes two numbers as arguments and determines how to compare them based on their more specific identities.
	 */
	public int compare(Number arg0, Number arg1) {
		if (arg0 instanceof IntegerNumber && arg1 instanceof IntegerNumber) {
			return compare((IntegerNumber) arg0, (IntegerNumber) arg1);
		} else if (arg0 instanceof RationalNumber
				&& arg1 instanceof RationalNumber) {
			return compare((RationalNumber) arg0, (RationalNumber) arg1);
		} else {
			return compare(rational(arg0), rational(arg1));
		}
	}

	@Override
	/**
	 * Returns the integer form of the denominator of a rational
	 */
	public IntegerNumber denominator(RationalNumber arg0) {
		return integer(((RealRational) arg0).denominator());
	}

	@Override
	/**
	 * An override of the divide method to accommodate rationals
	 */
	public RationalNumber divide(RationalNumber arg0, RationalNumber arg1) {
		RealRational x = (RealRational) arg0;
		RealRational y = (RealRational) arg1;

		return rational(x.numerator().multiply(y.denominator()), x
				.denominator().multiply(y.numerator()));
	}

	@Override
	/**
	 * An override of the divide method to maintain precision
	 */
	public IntegerNumber divide(IntegerNumber arg0, IntegerNumber arg1) {
		RealInteger x = (RealInteger) arg0;
		RealInteger y = (RealInteger) arg1;

		return integer(x.value().divide(y.value()));
	}

	@Override
	/**
	 * Modulates arguuument one by argument two and returns the modulated integer
	 * Protected against negative modulus
	 */
	public IntegerNumber mod(IntegerNumber arg0, IntegerNumber arg1) {
		RealInteger x = (RealInteger) arg0;
		RealInteger y = (RealInteger) arg1;

		if (y.signum() <= 0)
			throw new IllegalArgumentException("Modulus not positive: " + y);
		return integer(x.value().mod(y.value()));
	}

	@Override
	/**
	 * Calculates the mathematical floor of a rational number
	 */
	public IntegerNumber floor(RationalNumber arg0) {
		RealRational x = (RealRational) arg0;
		BigInteger numerator = x.numerator();
		BigInteger denominator = x.denominator();
		BigInteger quotient = numerator.divide(denominator);

		if (numerator.signum() >= 0) {
			return integer(quotient);
		} else {
			BigInteger modulus = numerator.mod(denominator);

			if (modulus.equals(BigInteger.ZERO)) {
				return integer(quotient);
			} else {
				return integer(quotient.subtract(BigInteger.ONE));
			}
		}
	}

	@Override
	/**
	 * Creates and returns rationals from two integers
	 */
	public RealRational fraction(IntegerNumber numerator,
			IntegerNumber denominator) {
		RealInteger x = (RealInteger) numerator;
		RealInteger y = (RealInteger) denominator;

		return rational(x.value(), y.value());
	}

	@Override
	/**
	 * creates and returns integers from strings
	 */
	public IntegerNumber integer(String string) {
		return integer(new BigInteger(string));
	}

	@Override
	/**
	 * creates and returns rationals from integers by giving them one as a denominator
	 */
	public RationalNumber integerToRational(IntegerNumber integer) {
		RealInteger x = (RealInteger) integer;

		return rational(x.value(), BigInteger.ONE);
	}

	@Override
	/**
	 * Returns an integer from rationals that are integral
	 */
	public IntegerNumber integerValue(RationalNumber arg0) {
		RealRational x = (RealRational) arg0;

		if (!isIntegral(arg0)) {
			throw new ArithmeticException("Non-integral number: " + arg0);
		}
		return integer(x.numerator());
	}

	@Override
	/**
	 * Overrides the multiply class to deal with rationals
	 */
	public RationalNumber multiply(RationalNumber arg0, RationalNumber arg1) {
		RealRational x = (RealRational) arg0;
		RealRational y = (RealRational) arg1;

		return rational(x.numerator().multiply(y.numerator()), x.denominator()
				.multiply(y.denominator()));
	}

	@Override
	/**
	 * Overrides the multiply class to maintain precision
	 */
	public IntegerNumber multiply(IntegerNumber arg0, IntegerNumber arg1) {
		RealInteger x = (RealInteger) arg0;
		RealInteger y = (RealInteger) arg1;

		return integer(x.value().multiply(y.value()));
	}

	@Override
	/**
	 * negates the numerator of a rational number
	 */
	public RationalNumber negate(RationalNumber arg0) {
		RealRational x = (RealRational) arg0;

		return rational(x.numerator().negate(), x.denominator());
	}

	@Override
	/**
	 * negates an integer
	 */
	public IntegerNumber negate(IntegerNumber arg0) {
		RealInteger x = (RealInteger) arg0;

		return integer(x.value().negate());
	}

	@Override
	/**
	 * Determines how to represent a given string based on decimal point position
	 * returns an integer if a decimal point is not found
	 * returns a rational if a decimal point is found
	 */
	public Number number(String string) {
		int decimalPosition = string.indexOf('.');

		if (decimalPosition < 0) {
			return integer(string);
		} else {
			return rational(string);
		}
	}

	@Override
	/**
	 * Returns an integer from a rational number
	 */
	public IntegerNumber numerator(RationalNumber arg0) {
		return integer(((RealRational) arg0).numerator());
	}

	@Override
	/**
	 * returns an integer representation of one
	 */
	public IntegerNumber oneInteger() {
		return oneInteger;
	}

	@Override
	/**
	 * returns a rational representation of one
	 */
	public RationalNumber oneRational() {
		return oneRational;
	}

	@Override
	/**
	 * Returns a rationalNumber crafted from two string arguments
	 */
	public RationalNumber rational(String string) {
		int ePosition = string.indexOf('e');

		if (ePosition < 0) {
			return rationalWithoutE(string);
		} else {
			String left = string.substring(0, ePosition);
			RationalNumber result = rationalWithoutE(left);
			int length = string.length();
			boolean positive;
			String right;
			IntegerNumber exponent, power;
			RationalNumber powerReal;

			if (exponentiator == null)
				exponentiator = new Exponentiator<IntegerNumber>(multiplier,
						oneInteger);
			if (ePosition + 1 < length && string.charAt(ePosition + 1) == '+') {
				right = string.substring(ePosition + 2);
				positive = true;
			} else if (ePosition + 1 < length
					&& string.charAt(ePosition + 1) == '-') {
				right = string.substring(ePosition + 2);
				positive = false;
			} else {
				right = string.substring(ePosition + 1);
				positive = true;
			}
			exponent = integer(right);
			power = exponentiator.exp(tenInteger, exponent);
			powerReal = rational(power);
			if (!positive)
				result = divide(result, powerReal);
			else
				result = multiply(result, powerReal);
			return result;
		}

	}

	/**
	 * Returns a RationalNumber generated from two strings while simultaneously
	 * eliminating the value E from the strings
	 */
	public RationalNumber rationalWithoutE(String string) {
		String left, right; // substrings to left/right of decimal point
		int decimalPosition = string.indexOf('.');
		int rightLength;
		String powerOfTen = "1";

		if (decimalPosition < 0) { // no decimal
			left = string;
			right = "";
		} else if (decimalPosition == 0) {
			left = "";
			right = string.substring(1, string.length());
		} else {
			left = string.substring(0, decimalPosition);
			right = string.substring(decimalPosition + 1, string.length());
		}
		rightLength = right.length();
		for (int j = 0; j < rightLength; j++)
			powerOfTen += "0";
		return rational(new BigInteger(left + right),
				new BigInteger(powerOfTen));
	}

	@Override
	/**
	 * Determines how to represent two numbers as a RationalNumber based on their more specific classes
	 */
	public RationalNumber rational(Number number) {
		if (number instanceof RationalNumber) {
			return (RationalNumber) number;
		} else if (number instanceof IntegerNumber) {
			return integerToRational((IntegerNumber) number);
		}
		throw new IllegalArgumentException("Unknown type of number: " + number);
	}

	@Override
	/**
	 * An override of the subtract method to deal with RationalNumbers
	 */
	public RationalNumber subtract(RationalNumber arg0, RationalNumber arg1) {
		return add(arg0, negate(arg1));
	}

	@Override
	/**
	 * An override of the subtract method to maintain precision
	 */
	public IntegerNumber subtract(IntegerNumber arg0, IntegerNumber arg1) {
		RealInteger x = (RealInteger) arg0;
		RealInteger y = (RealInteger) arg1;

		return integer(x.value().subtract(y.value()));
	}

	@Override
	/**
	 * Returns an integer representation of zero
	 */
	public IntegerNumber zeroInteger() {
		return zeroInteger;
	}

	@Override
	/**
	 * Returns a rational representation of zero
	 */
	public RationalNumber zeroRational() {
		return zeroRational;
	}

	@Override
	/**
	 * Determines if a rational is integral by seeing if its denominator equates to one
	 */
	public boolean isIntegral(RationalNumber arg0) {
		RealRational x = (RealRational) arg0;

		return x.denominator().equals(BigInteger.ONE);
	}

	@Override
	/**
	 * Returns an integer representation of a value
	 */
	public IntegerNumber integer(int value) {
		return integer("" + value);
	}

	@Override
	/**
	 * Determines how to properly negate a number based on its more specific class
	 */
	public Number negate(Number arg0) {
		if (arg0 instanceof IntegerNumber)
			return negate((IntegerNumber) arg0);
		else
			return negate((RationalNumber) arg0);
	}

	@Override
	/**
	 * Determines how to properly add two numbers based on their more specific classes
	 */
	public Number add(Number arg0, Number arg1) {
		if (arg0 instanceof IntegerNumber) {
			if (!(arg1 instanceof IntegerNumber))
				throw new IllegalArgumentException(
						"Mixed numeric types not allowed:\n" + arg0 + "\n"
								+ arg1);
			return add((IntegerNumber) arg0, (IntegerNumber) arg1);
		} else if (arg0 instanceof RationalNumber) {
			if (!(arg1 instanceof RationalNumber))
				throw new IllegalArgumentException(
						"Mixed numeric types not allowed:\n" + arg0 + "\n"
								+ arg1);
			return add((RationalNumber) arg0, (RationalNumber) arg1);
		} else {
			throw new IllegalArgumentException("Unknown type of number: "
					+ arg0);
		}
	}

	@Override
	/**
	 * Determines how to properly divide two numbers based on their more specific classes
	 */
	public Number divide(Number arg0, Number arg1) {
		if (arg0 instanceof IntegerNumber) {
			if (!(arg1 instanceof IntegerNumber))
				throw new IllegalArgumentException(
						"Mixed numeric types not allowed:\n" + arg0 + "\n"
								+ arg1);
			return divide((IntegerNumber) arg0, (IntegerNumber) arg1);
		} else if (arg0 instanceof RationalNumber) {
			if (!(arg1 instanceof RationalNumber))
				throw new IllegalArgumentException(
						"Mixed numeric types not allowed:\n" + arg0 + "\n"
								+ arg1);
			return divide((RationalNumber) arg0, (RationalNumber) arg1);
		} else {
			throw new IllegalArgumentException("Unknown type of number: "
					+ arg0);
		}
	}

	@Override
	/**
	 * Determines how to properly multiply two numbers based on their more specific classes
	 */
	public Number multiply(Number arg0, Number arg1) {
		if (arg0 instanceof IntegerNumber) {
			if (!(arg1 instanceof IntegerNumber))
				throw new IllegalArgumentException(
						"Mixed numeric types not allowed:\n" + arg0 + "\n"
								+ arg1);
			return multiply((IntegerNumber) arg0, (IntegerNumber) arg1);
		} else if (arg0 instanceof RationalNumber) {
			if (!(arg1 instanceof RationalNumber))
				throw new IllegalArgumentException(
						"Mixed numeric types not allowed:\n" + arg0 + "\n"
								+ arg1);
			return multiply((RationalNumber) arg0, (RationalNumber) arg1);
		} else {
			throw new IllegalArgumentException("Unknown type of number: "
					+ arg0);
		}
	}

	@Override
	/**
	 * Determines how to properly subtract two numbers based on their more specific classes
	 */
	public Number subtract(Number arg0, Number arg1) {
		if (arg0 instanceof IntegerNumber) {
			if (!(arg1 instanceof IntegerNumber))
				throw new IllegalArgumentException(
						"Mixed numeric types not allowed:\n" + arg0 + "\n"
								+ arg1);
			return subtract((IntegerNumber) arg0, (IntegerNumber) arg1);
		} else if (arg0 instanceof RationalNumber) {
			if (!(arg1 instanceof RationalNumber))
				throw new IllegalArgumentException(
						"Mixed numeric types not allowed:\n" + arg0 + "\n"
								+ arg1);
			return subtract((RationalNumber) arg0, (RationalNumber) arg1);
		} else {
			throw new IllegalArgumentException("Unknown type of number: "
					+ arg0);
		}
	}

	@Override
	/**
	 * Returns a RationalNumber incremented by one
	 */
	public RationalNumber increment(RationalNumber arg) {
		return add(arg, oneRational);
	}

	@Override
	/**
	 * Returns an IntegerNumber incremented by one
	 */
	public IntegerNumber increment(IntegerNumber arg) {
		return add(arg, oneInteger);
	}

	@Override
	/**
	 * Determines how to properly increment a number based on its more specific class
	 */
	public Number increment(Number arg) {
		if (arg instanceof IntegerNumber)
			return add((IntegerNumber) arg, oneInteger);
		return add((RationalNumber) arg, oneRational);
	}

	@Override
	/**
	 * Returns a RationalNumber decremented by one
	 */
	public RationalNumber decrement(RationalNumber arg) {
		return subtract(arg, oneRational);
	}

	@Override
	/**
	 * Returns an IntegerNumber decremented by one
	 */
	public IntegerNumber decrement(IntegerNumber arg) {
		return subtract(arg, oneInteger);
	}

	@Override
	/**
	 * Determines how to properly decrement a number based on its more specific class
	 */
	public Number decrement(Number arg) {
		if (arg instanceof IntegerNumber)
			return subtract((IntegerNumber) arg, oneInteger);
		return subtract((RationalNumber) arg, oneRational);
	}

	@Override
	/**
	 * Sends BigInteger representations of given IntegerNumbers to the gcd function
	 */
	public IntegerNumber gcd(IntegerNumber arg0, IntegerNumber arg1) {
		BigInteger value0 = ((RealInteger) arg0).value();
		BigInteger value1 = ((RealInteger) arg1).value();

		return integer(value0.gcd(value1));
	}

	@Override
	/**
	 * Determines and returns the lcm of two IntegerNumbers by dividing their product by their gcd
	 */
	public IntegerNumber lcm(IntegerNumber arg0, IntegerNumber arg1) {
		return divide(multiply(arg0, arg1), gcd(arg0, arg1));
	}

	/**
	 * A simple method to print a matrix of RationalNumbers to screen
	 * 
	 * @param out
	 * @param msg
	 * @param matrix
	 */
	public void printMatrix(PrintWriter out, String msg,
			RationalNumber[][] matrix) {
		out.println(msg);
		for (int i = 0; i < matrix.length; i++) {
			RationalNumber[] row = matrix[i];

			for (int j = 0; j < row.length; j++) {
				out.print(row[j] + "  ");
			}
			out.println();
		}
		out.println();
		out.flush();
	}

	@Override
	/**
	 * Performs a gaussian elimination on the given RationalNumber matrix
	 * Maintains a boolean 'debug' for easy troubleshooting due to its complex nature
	 */
	public void gaussianElimination(RationalNumber[][] matrix) {
		int numRows = matrix.length;
		int numCols;
		int top = 0; // index of current top row
		int col = 0; // index of current left column
		int pivotRow = 0; // index of row containing the pivot
		RationalNumber pivot = zeroRational; // the value of the pivot
		int i = 0; // loop variable over rows of matrix
		int j = 0; // loop variable over columns of matrix
		boolean debug = false;
		PrintWriter out = new PrintWriter(System.out);

		if (numRows == 0)
			return;
		numCols = matrix[0].length;

		for (top = col = 0; top < numRows && col < numCols; top++, col++) {
			/*
			 * At this point we know that the submatarix consisting of the first
			 * top rows of A is in reduced row-echelon form. We will now
			 * consider the submatrix B consisting of the remaining rows. We
			 * know, additionally, that the first col columns of B are all zero.
			 */

			if (debug)
				out.println("Top: " + top + "\n");

			/*
			 * Step 1: Locate the leftmost column of B that does not consist
			 * entirely of zeros, if one exists. The top nonzero entry of this
			 * column is the pivot.
			 */

			pivot = zeroRational;
			pivotSearch: for (; col < numCols; col++) {
				for (pivotRow = top; pivotRow < numRows; pivotRow++) {
					pivot = matrix[pivotRow][col];
					if (!pivot.isZero())
						break pivotSearch;
				}
			}

			if (col >= numCols) {
				break;
			}

			/*
			 * At this point we are guaranteed that pivot = A[pivotRow,col] is
			 * nonzero. We also know that all the columns of B to the left of
			 * col consist entirely of zeros.
			 */

			if (debug) {
				out.println("Step 1 result: col=" + col + ", pivotRow="
						+ pivotRow + ", pivot=" + pivot + "\n");
			}

			/*
			 * Step 2: Interchange the top row with the pivot row, if necessary,
			 * so that the entry at the top of the column found in Step 1 is
			 * nonzero.
			 */

			if (pivotRow != top) {
				RationalNumber[] tmpRow = matrix[top];

				matrix[top] = matrix[pivotRow];
				matrix[pivotRow] = tmpRow;
			}

			if (debug) {
				printMatrix(out, "Step 2 result:\n", matrix);
			}

			/*
			 * At this point we are guaranteed that A[top,col] = pivot is
			 * nonzero. Also, we know that (i>=top and j<col) implies A[i,j] =
			 * 0.
			 */

			/*
			 * Step 3: Divide the top row by pivot in order to introduce a
			 * leading 1.
			 */

			if (!pivot.isOne())
				for (j = col; j < numCols; j++) {
					matrix[top][j] = divide(matrix[top][j], pivot);
				}

			if (debug) {
				printMatrix(out, "Step 3 result:\n", matrix);
			}

			/*
			 * At this point we are guaranteed that A[top,col] is 1.0, assuming
			 * that floating point arithmetic guarantees that a/a equals 1.0 for
			 * any nonzero double a.
			 */

			/*
			 * Step 4: Add suitable multiples of the top row to all other rows
			 * so that all entries above and below the leading 1 become zero.
			 */

			for (i = 0; i < numRows; i++) {
				if (i != top) {
					RationalNumber tmp = matrix[i][col];
					for (j = col; j < numCols; j++) {
						matrix[i][j] = subtract(matrix[i][j],
								multiply(tmp, matrix[top][j]));
					}
				}
			}

			if (debug) {
				printMatrix(out, "Step 4 result:\n", matrix);
			}
		}
	}

	@Override
	public Interval emptyIntegerInterval() {
		return emptyIntegerInterval;
	}

	@Override
	public Interval emptyRealInterval() {
		return emptyRealInterval;
	}

	@Override
	public Interval newInterval(boolean isIntegral, Number lower,
			boolean strictLower, Number upper, boolean strictUpper) {
		return new CommonInterval(isIntegral, lower, strictLower, upper,
				strictUpper);
	}

	@Override
	public Interval intersection(Interval i1, Interval i2) {
		boolean isIntegral = i1.isIntegral();

		assert isIntegral == i2.isIntegral();

		Number lo1 = i1.lower(), lo2 = i2.lower(), hi1 = i1.upper(), hi2 = i2
				.upper();
		boolean sl1 = i1.strictLower(), sl2 = i2.strictLower(), su1 = i1
				.strictUpper(), su2 = i2.strictUpper();
		Number lo, hi;
		boolean sl, su;

		if (lo1 == null) {
			lo = lo2;
			sl = sl2;
		} else {
			if (lo2 == null) {
				lo = lo1;
				sl = sl1;
			} else {
				int compare = lo1.compareTo(lo2);

				if (compare < 0) {
					lo = lo2;
					sl = sl2;
				} else if (compare == 0) {
					lo = lo1;
					sl = sl1 || sl2;
				} else {
					lo = lo1;
					sl = sl1;
				}
			}
		}
		if (hi1 == null) {
			hi = hi2;
			su = su2;
		} else {
			if (hi2 == null) {
				hi = hi1;
				su = su1;
			} else {
				int compare = hi1.compareTo(hi2);

				if (compare > 0) {
					hi = hi2;
					su = su2;
				} else if (compare == 0) {
					hi = hi1;
					su = su1 || su2;
				} else {
					hi = hi1;
					su = su1;
				}
			}
		}
		if (lo != null && hi != null) {
			int compare = hi.compareTo(lo);

			if (compare < 0) {
				return isIntegral ? emptyIntegerInterval : emptyRealInterval;
			} else if (compare == 0) {
				if (sl || su) {
					return isIntegral ? emptyIntegerInterval
							: emptyRealInterval;
				}
			}
		}
		return new CommonInterval(i1.isIntegral(), lo, sl, hi, su);
	}

	@Override
	public void union(Interval i1, Interval i2, IntervalUnion result) {
		// under construction...
		if (i1.isEmpty()) {
			result.status = 0;
			result.union = i2;
			return;
		} else if (i2.isEmpty()) {
			result.status = 0;
			result.union = i1;
			return;
		} else {
			boolean isIntegral = i1.isIntegral();

			assert isIntegral == i2.isIntegral();

			Number lo1 = i1.lower(), lo2 = i2.lower(), hi1 = i1.upper(), hi2 = i2
					.upper();
			boolean sl1 = i1.strictLower(), sl2 = i2.strictLower(), su1 = i1
					.strictUpper(), su2 = i2.strictUpper();
			Number lo, hi;
			boolean sl, su;

			int compare1 = hi1.compareTo(lo2);

			if (compare1 < 0) { // hi1<lo2
				result.status = -1;
			} else if (compare1 == 0) { // hi1=lo2
				if (!su1 || !sl2) { // <...)[...>
					lo = lo1;
					hi = hi2;
					sl = sl1;
					su = su2;
					result.status = 0;
				} else { // <...)(...>
					result.status = -1;
				}
			} else { // hi1>lo2
				int compare2 = lo1.compareTo(hi2);

				if (compare2 < 0) { // lo1<hi2
					int compareLo = lo1.compareTo(lo2);

					if (compareLo < 0) {
						lo = lo1;
						sl = sl1;
					} else if (compareLo == 0) {
						lo = lo1;
						sl = sl1 && sl2;
					} else {
						lo = lo2;
						sl = sl2;
					}

					int compareHi = hi1.compareTo(hi2);

					if (compareHi < 0) {
						hi = hi2;
						su = su2;
					} else if (compareHi == 0) {
						hi = hi1;
						su = su1 && su2;
					} else {
						hi = hi1;
						su = su1;
					}
					result.status = 0;
				} else if (compare2 == 0) { // lo1=hi2
					if (!sl1 || !su2) {
						lo = lo2;
						hi = hi1;
						sl = sl2;
						su = su1;
						result.status = 0;
					} else {
						result.status = 1;
					}
				} else { // lo1>hi2
					result.status = 1;
				}
			}
			if (result.status != 0) {
				result.union = null;
			} else {
				// keep working....
				// result.union = new CommonInterval(i1.isIntegral(), lo, sl,
				// hi,
				// su);
			}
		}
	}

}
