package edu.udel.cis.vsl.sarl.number.real;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.BinaryOperator;
import edu.udel.cis.vsl.sarl.IF.number.Exponentiator;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;

public class RealNumberFactory implements NumberFactory {

	private Map<BigInteger, RealInteger> integerMap = new HashMap<BigInteger, RealInteger>();

	private Map<RationalKey, RealRational> rationalMap = new HashMap<RationalKey, RealRational>();

	private RealInteger zeroInteger, oneInteger, tenInteger;

	private RealRational zeroRational, oneRational;

	private BinaryOperator<IntegerNumber> multiplier;

	private Exponentiator<IntegerNumber> exponentiator;

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
	}

	@Override
	public Number abs(Number number) {
		if (number.signum() < 0) {
			return negate(number);
		} else {
			return number;
		}
	}

	@Override
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
	public RealInteger integer(long value) {
		return integer(BigInteger.valueOf(value));
	}

	@Override
	public RealRational rational(BigInteger numerator, BigInteger denominator) {
		int signum = denominator.signum();
		RationalKey key;
		RealRational oldValue;

		if (signum == 0) {
			throw new ArithmeticException("Division by 0");
		}
		if (signum < 0) {
			numerator = numerator.negate();
			denominator = denominator.negate();
		}
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

	public boolean isIntegral(RealRational arg0) {
		return arg0.denominator().equals(BigInteger.ONE);
	}

	@Override
	public RationalNumber add(RationalNumber arg0, RationalNumber arg1) {
		RealRational x = (RealRational) arg0;
		RealRational y = (RealRational) arg1;

		return rational(
				x.numerator().multiply(y.denominator())
						.add(x.denominator().multiply(y.numerator())), x
						.denominator().multiply(y.denominator()));
	}

	@Override
	public IntegerNumber add(IntegerNumber arg0, IntegerNumber arg1) {
		RealInteger x = (RealInteger) arg0;
		RealInteger y = (RealInteger) arg1;

		return integer(x.value().add(y.value()));
	}

	@Override
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
	public int compare(RationalNumber arg0, RationalNumber arg1) {
		return subtract(arg0, arg1).signum();
	}

	@Override
	public int compare(IntegerNumber arg0, IntegerNumber arg1) {
		return subtract(arg0, arg1).signum();
	}

	@Override
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
	public IntegerNumber denominator(RationalNumber arg0) {
		return integer(((RealRational) arg0).denominator());
	}

	@Override
	public RationalNumber divide(RationalNumber arg0, RationalNumber arg1) {
		RealRational x = (RealRational) arg0;
		RealRational y = (RealRational) arg1;

		return rational(x.numerator().multiply(y.denominator()), x
				.denominator().multiply(y.numerator()));
	}

	@Override
	public IntegerNumber divide(IntegerNumber arg0, IntegerNumber arg1) {
		RealInteger x = (RealInteger) arg0;
		RealInteger y = (RealInteger) arg1;

		return integer(x.value().divide(y.value()));
	}

	@Override
	public IntegerNumber mod(IntegerNumber arg0, IntegerNumber arg1) {
		RealInteger x = (RealInteger) arg0;
		RealInteger y = (RealInteger) arg1;

		if (y.signum() <= 0)
			throw new IllegalArgumentException("Modulus not positive: " + y);
		return integer(x.value().mod(y.value()));
	}

	@Override
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
	public RealRational fraction(IntegerNumber numerator,
			IntegerNumber denominator) {
		RealInteger x = (RealInteger) numerator;
		RealInteger y = (RealInteger) denominator;

		return rational(x.value(), y.value());
	}

	@Override
	public IntegerNumber integer(String string) {
		return integer(new BigInteger(string));
	}

	@Override
	public RationalNumber integerToRational(IntegerNumber integer) {
		RealInteger x = (RealInteger) integer;

		return rational(x.value(), BigInteger.ONE);
	}

	@Override
	public IntegerNumber integerValue(RationalNumber arg0) {
		RealRational x = (RealRational) arg0;

		if (!isIntegral(arg0)) {
			throw new ArithmeticException("Non-integral number: " + arg0);
		}
		return integer(x.numerator());
	}

	@Override
	public RationalNumber multiply(RationalNumber arg0, RationalNumber arg1) {
		RealRational x = (RealRational) arg0;
		RealRational y = (RealRational) arg1;

		return rational(x.numerator().multiply(y.numerator()), x.denominator()
				.multiply(y.denominator()));
	}

	@Override
	public IntegerNumber multiply(IntegerNumber arg0, IntegerNumber arg1) {
		RealInteger x = (RealInteger) arg0;
		RealInteger y = (RealInteger) arg1;

		return integer(x.value().multiply(y.value()));
	}

	@Override
	public RationalNumber negate(RationalNumber arg0) {
		RealRational x = (RealRational) arg0;

		return rational(x.numerator().negate(), x.denominator());
	}

	@Override
	public IntegerNumber negate(IntegerNumber arg0) {
		RealInteger x = (RealInteger) arg0;

		return integer(x.value().negate());
	}

	@Override
	public Number number(String string) {
		int decimalPosition = string.indexOf('.');

		if (decimalPosition < 0) {
			return integer(string);
		} else {
			return rational(string);
		}
	}

	@Override
	public IntegerNumber numerator(RationalNumber arg0) {
		return integer(((RealRational) arg0).numerator());
	}

	@Override
	public IntegerNumber oneInteger() {
		return oneInteger;
	}

	@Override
	public RationalNumber oneRational() {
		return oneRational;
	}

	@Override
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
	public RationalNumber rational(Number number) {
		if (number instanceof RationalNumber) {
			return (RationalNumber) number;
		} else if (number instanceof IntegerNumber) {
			return integerToRational((IntegerNumber) number);
		}
		throw new IllegalArgumentException("Unknown type of number: " + number);
	}

	@Override
	public RationalNumber subtract(RationalNumber arg0, RationalNumber arg1) {
		return add(arg0, negate(arg1));
	}

	@Override
	public IntegerNumber subtract(IntegerNumber arg0, IntegerNumber arg1) {
		RealInteger x = (RealInteger) arg0;
		RealInteger y = (RealInteger) arg1;

		return integer(x.value().subtract(y.value()));
	}

	@Override
	public IntegerNumber zeroInteger() {
		return zeroInteger;
	}

	@Override
	public RationalNumber zeroRational() {
		return zeroRational;
	}

	@Override
	public boolean isIntegral(RationalNumber arg0) {
		RealRational x = (RealRational) arg0;

		return x.denominator().equals(BigInteger.ONE);
	}

	@Override
	public IntegerNumber integer(int value) {
		return integer("" + value);
	}

	@Override
	public Number negate(Number arg0) {
		if (arg0 instanceof IntegerNumber)
			return negate((IntegerNumber) arg0);
		else
			return negate((RationalNumber) arg0);
	}

	@Override
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
	public RationalNumber increment(RationalNumber arg) {
		return add(arg, oneRational);
	}

	@Override
	public IntegerNumber increment(IntegerNumber arg) {
		return add(arg, oneInteger);
	}

	@Override
	public Number increment(Number arg) {
		if (arg instanceof IntegerNumber)
			return add((IntegerNumber) arg, oneInteger);
		return add((RationalNumber) arg, oneRational);
	}

	@Override
	public RationalNumber decrement(RationalNumber arg) {
		return subtract(arg, oneRational);
	}

	@Override
	public IntegerNumber decrement(IntegerNumber arg) {
		return subtract(arg, oneInteger);
	}

	@Override
	public Number decrement(Number arg) {
		if (arg instanceof IntegerNumber)
			return subtract((IntegerNumber) arg, oneInteger);
		return subtract((RationalNumber) arg, oneRational);
	}

	@Override
	public IntegerNumber gcd(IntegerNumber arg0, IntegerNumber arg1) {
		BigInteger value0 = ((RealInteger) arg0).value();
		BigInteger value1 = ((RealInteger) arg1).value();

		return integer(value0.gcd(value1));
	}

	@Override
	public IntegerNumber lcm(IntegerNumber arg0, IntegerNumber arg1) {
		return divide(multiply(arg0, arg1), gcd(arg0, arg1));
	}

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
}
