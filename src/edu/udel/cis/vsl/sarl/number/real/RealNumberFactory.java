package edu.udel.cis.vsl.sarl.number.real;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.Multiplier;
import edu.udel.cis.vsl.sarl.IF.number.Exponentiator;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberIF;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumberIF;

public class RealNumberFactory implements NumberFactoryIF {

	private Map<BigInteger, RealInteger> integerMap = new HashMap<BigInteger, RealInteger>();

	private Map<RationalKey, RealRational> rationalMap = new HashMap<RationalKey, RealRational>();

	private RealInteger zeroInteger, oneInteger, tenInteger;

	private RealRational zeroRational, oneRational;

	private Multiplier<IntegerNumberIF> multiplier;

	private Exponentiator<IntegerNumberIF> exponentiator;

	class IntMultiplier implements Multiplier<IntegerNumberIF> {
		private RealNumberFactory factory;

		IntMultiplier(RealNumberFactory factory) {
			this.factory = factory;
		}

		@Override
		public IntegerNumberIF multiply(IntegerNumberIF arg0,
				IntegerNumberIF arg1) {
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

	public NumberIF abs(NumberIF number) {
		if (number.signum() < 0) {
			return negate(number);
		} else {
			return number;
		}
	}

	private RealInteger integer(BigInteger big) {
		RealInteger oldValue = integerMap.get(big);

		if (oldValue != null) {
			return oldValue;
		} else {
			RealInteger newValue = new RealInteger(big);

			integerMap.put(big, newValue);
			return newValue;
		}
	}

	private RealRational rational(BigInteger numerator, BigInteger denominator) {
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

	public RationalNumberIF add(RationalNumberIF arg0, RationalNumberIF arg1) {
		RealRational x = (RealRational) arg0;
		RealRational y = (RealRational) arg1;

		return rational(x.numerator().multiply(y.denominator()).add(
				x.denominator().multiply(y.numerator())), x.denominator()
				.multiply(y.denominator()));
	}

	public IntegerNumberIF add(IntegerNumberIF arg0, IntegerNumberIF arg1) {
		RealInteger x = (RealInteger) arg0;
		RealInteger y = (RealInteger) arg1;

		return integer(x.value().add(y.value()));
	}

	public IntegerNumberIF ceil(RationalNumberIF arg0) {
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

	public int compare(RationalNumberIF arg0, RationalNumberIF arg1) {
		return subtract(arg0, arg1).signum();
	}

	public int compare(IntegerNumberIF arg0, IntegerNumberIF arg1) {
		return subtract(arg0, arg1).signum();
	}

	public int compare(NumberIF arg0, NumberIF arg1) {
		if (arg0 instanceof IntegerNumberIF && arg1 instanceof IntegerNumberIF) {
			return compare((IntegerNumberIF) arg0, (IntegerNumberIF) arg1);
		} else if (arg0 instanceof RationalNumberIF
				&& arg1 instanceof RationalNumberIF) {
			return compare((RationalNumberIF) arg0, (RationalNumberIF) arg1);
		} else {
			return compare(rational(arg0), rational(arg1));
		}
	}

	public IntegerNumberIF denominator(RationalNumberIF arg0) {
		return integer(((RealRational) arg0).denominator());
	}

	public RationalNumberIF divide(RationalNumberIF arg0, RationalNumberIF arg1) {
		RealRational x = (RealRational) arg0;
		RealRational y = (RealRational) arg1;

		return rational(x.numerator().multiply(y.denominator()), x
				.denominator().multiply(y.numerator()));
	}

	public IntegerNumberIF divide(IntegerNumberIF arg0, IntegerNumberIF arg1) {
		RealInteger x = (RealInteger) arg0;
		RealInteger y = (RealInteger) arg1;

		return integer(x.value().divide(y.value()));
	}

	public IntegerNumberIF mod(IntegerNumberIF arg0, IntegerNumberIF arg1) {
		RealInteger x = (RealInteger) arg0;
		RealInteger y = (RealInteger) arg1;

		if (y.signum() <= 0)
			throw new IllegalArgumentException("Modulus not positive: " + y);
		return integer(x.value().mod(y.value()));
	}

	public IntegerNumberIF floor(RationalNumberIF arg0) {
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

	public RealRational fraction(IntegerNumberIF numerator,
			IntegerNumberIF denominator) {
		RealInteger x = (RealInteger) numerator;
		RealInteger y = (RealInteger) denominator;

		return rational(x.value(), y.value());
	}

	public IntegerNumberIF integer(String string) {
		return integer(new BigInteger(string));
	}

	public RationalNumberIF integerToRational(IntegerNumberIF integer) {
		RealInteger x = (RealInteger) integer;

		return rational(x.value(), BigInteger.ONE);
	}

	public IntegerNumberIF integerValue(RationalNumberIF arg0) {
		RealRational x = (RealRational) arg0;

		if (!isIntegral(arg0)) {
			throw new ArithmeticException("Non-integral number: " + arg0);
		}
		return integer(x.numerator());
	}

	public RationalNumberIF multiply(RationalNumberIF arg0,
			RationalNumberIF arg1) {
		RealRational x = (RealRational) arg0;
		RealRational y = (RealRational) arg1;

		return rational(x.numerator().multiply(y.numerator()), x.denominator()
				.multiply(y.denominator()));
	}

	public IntegerNumberIF multiply(IntegerNumberIF arg0, IntegerNumberIF arg1) {
		RealInteger x = (RealInteger) arg0;
		RealInteger y = (RealInteger) arg1;

		return integer(x.value().multiply(y.value()));
	}

	public RationalNumberIF negate(RationalNumberIF arg0) {
		RealRational x = (RealRational) arg0;

		return rational(x.numerator().negate(), x.denominator());
	}

	public IntegerNumberIF negate(IntegerNumberIF arg0) {
		RealInteger x = (RealInteger) arg0;

		return integer(x.value().negate());
	}

	public NumberIF number(String string) {
		int decimalPosition = string.indexOf('.');

		if (decimalPosition < 0) {
			return integer(string);
		} else {
			return rational(string);
		}
	}

	public IntegerNumberIF numerator(RationalNumberIF arg0) {
		return integer(((RealRational) arg0).numerator());
	}

	public IntegerNumberIF oneInteger() {
		return oneInteger;
	}

	public RationalNumberIF oneRational() {
		return oneRational;
	}

	public RationalNumberIF rational(String string) {
		int ePosition = string.indexOf('e');

		if (ePosition < 0) {
			return rationalWithoutE(string);
		} else {
			String left = string.substring(0, ePosition);
			RationalNumberIF result = rationalWithoutE(left);
			int length = string.length();
			boolean positive;
			String right;
			IntegerNumberIF exponent, power;
			RationalNumberIF powerReal;

			if (exponentiator == null)
				exponentiator = new Exponentiator<IntegerNumberIF>(multiplier,
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

	public RationalNumberIF rationalWithoutE(String string) {
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

	public RationalNumberIF rational(NumberIF number) {
		if (number instanceof RationalNumberIF) {
			return (RationalNumberIF) number;
		} else if (number instanceof IntegerNumberIF) {
			return integerToRational((IntegerNumberIF) number);
		}
		throw new IllegalArgumentException("Unknown type of number: " + number);
	}

	public RationalNumberIF subtract(RationalNumberIF arg0,
			RationalNumberIF arg1) {
		return add(arg0, negate(arg1));
	}

	public IntegerNumberIF subtract(IntegerNumberIF arg0, IntegerNumberIF arg1) {
		RealInteger x = (RealInteger) arg0;
		RealInteger y = (RealInteger) arg1;

		return integer(x.value().subtract(y.value()));
	}

	public IntegerNumberIF zeroInteger() {
		return zeroInteger;
	}

	public RationalNumberIF zeroRational() {
		return zeroRational;
	}

	public boolean isIntegral(RationalNumberIF arg0) {
		RealRational x = (RealRational) arg0;

		return x.denominator().equals(BigInteger.ONE);
	}

	public IntegerNumberIF integer(int value) {
		return integer("" + value);
	}

	public NumberIF negate(NumberIF arg0) {
		if (arg0 instanceof IntegerNumberIF)
			return negate((IntegerNumberIF) arg0);
		else
			return negate((RationalNumberIF) arg0);
	}

	public NumberIF add(NumberIF arg0, NumberIF arg1) {
		if (arg0 instanceof IntegerNumberIF) {
			if (!(arg1 instanceof IntegerNumberIF))
				throw new IllegalArgumentException(
						"Mixed numeric types not allowed:\n" + arg0 + "\n"
								+ arg1);
			return add((IntegerNumberIF) arg0, (IntegerNumberIF) arg1);
		} else if (arg0 instanceof RationalNumberIF) {
			if (!(arg1 instanceof RationalNumberIF))
				throw new IllegalArgumentException(
						"Mixed numeric types not allowed:\n" + arg0 + "\n"
								+ arg1);
			return add((RationalNumberIF) arg0, (RationalNumberIF) arg1);
		} else {
			throw new IllegalArgumentException("Unknown type of number: "
					+ arg0);
		}
	}

	public NumberIF divide(NumberIF arg0, NumberIF arg1) {
		if (arg0 instanceof IntegerNumberIF) {
			if (!(arg1 instanceof IntegerNumberIF))
				throw new IllegalArgumentException(
						"Mixed numeric types not allowed:\n" + arg0 + "\n"
								+ arg1);
			return divide((IntegerNumberIF) arg0, (IntegerNumberIF) arg1);
		} else if (arg0 instanceof RationalNumberIF) {
			if (!(arg1 instanceof RationalNumberIF))
				throw new IllegalArgumentException(
						"Mixed numeric types not allowed:\n" + arg0 + "\n"
								+ arg1);
			return divide((RationalNumberIF) arg0, (RationalNumberIF) arg1);
		} else {
			throw new IllegalArgumentException("Unknown type of number: "
					+ arg0);
		}
	}

	public NumberIF multiply(NumberIF arg0, NumberIF arg1) {
		if (arg0 instanceof IntegerNumberIF) {
			if (!(arg1 instanceof IntegerNumberIF))
				throw new IllegalArgumentException(
						"Mixed numeric types not allowed:\n" + arg0 + "\n"
								+ arg1);
			return multiply((IntegerNumberIF) arg0, (IntegerNumberIF) arg1);
		} else if (arg0 instanceof RationalNumberIF) {
			if (!(arg1 instanceof RationalNumberIF))
				throw new IllegalArgumentException(
						"Mixed numeric types not allowed:\n" + arg0 + "\n"
								+ arg1);
			return multiply((RationalNumberIF) arg0, (RationalNumberIF) arg1);
		} else {
			throw new IllegalArgumentException("Unknown type of number: "
					+ arg0);
		}
	}

	public NumberIF subtract(NumberIF arg0, NumberIF arg1) {
		if (arg0 instanceof IntegerNumberIF) {
			if (!(arg1 instanceof IntegerNumberIF))
				throw new IllegalArgumentException(
						"Mixed numeric types not allowed:\n" + arg0 + "\n"
								+ arg1);
			return subtract((IntegerNumberIF) arg0, (IntegerNumberIF) arg1);
		} else if (arg0 instanceof RationalNumberIF) {
			if (!(arg1 instanceof RationalNumberIF))
				throw new IllegalArgumentException(
						"Mixed numeric types not allowed:\n" + arg0 + "\n"
								+ arg1);
			return subtract((RationalNumberIF) arg0, (RationalNumberIF) arg1);
		} else {
			throw new IllegalArgumentException("Unknown type of number: "
					+ arg0);
		}
	}

	public IntegerNumberIF gcd(IntegerNumberIF arg0, IntegerNumberIF arg1) {
		BigInteger value0 = ((RealInteger) arg0).value();
		BigInteger value1 = ((RealInteger) arg1).value();

		return integer(value0.gcd(value1));
	}

	public IntegerNumberIF lcm(IntegerNumberIF arg0, IntegerNumberIF arg1) {
		return divide(multiply(arg0, arg1), gcd(arg0, arg1));
	}

	public void printMatrix(PrintWriter out, String msg,
			RationalNumberIF[][] matrix) {
		out.println(msg);
		for (int i = 0; i < matrix.length; i++) {
			RationalNumberIF[] row = matrix[i];

			for (int j = 0; j < row.length; j++) {
				out.print(row[j] + "  ");
			}
			out.println();
		}
		out.println();
		out.flush();
	}

	public void gaussianElimination(RationalNumberIF[][] matrix) {
		int numRows = matrix.length;
		int numCols;
		int top = 0; // index of current top row
		int col = 0; // index of current left column
		int pivotRow = 0; // index of row containing the pivot
		RationalNumberIF pivot = zeroRational; // the value of the pivot
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
				RationalNumberIF[] tmpRow = matrix[top];

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
					RationalNumberIF tmp = matrix[i][col];
					for (j = col; j < numCols; j++) {
						matrix[i][j] = subtract(matrix[i][j], multiply(tmp,
								matrix[top][j]));
					}
				}
			}

			if (debug) {
				printMatrix(out, "Step 4 result:\n", matrix);
			}
		}
	}
}
