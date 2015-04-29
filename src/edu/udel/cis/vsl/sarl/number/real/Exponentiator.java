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

import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.util.BinaryOperator;

/**
 * An efficient implementation of raising something to some power. Given an
 * associative, commutative multiplication operator *, with identity 1, on some
 * set T, you can form an Exponentiator. This provides a method exp which takes
 * an element x of T, and a nonnegative IntegerNumberIF m, and returns the
 * result of multiplying x by itself m times (x*x*...*x). If m=0, 1 is returned.
 * 
 * @author siegel
 * 
 * @param <T>
 */
public class Exponentiator<T> {

	private BinaryOperator<T> multiplier;

	private T one;

	private static NumberFactory numberFactory = Numbers.REAL_FACTORY;

	private static IntegerNumber two = numberFactory.integer(2);

	/**
	 * Constructor for the exponentiator class
	 * 
	 * @param multiplier
	 * @param one
	 */
	public Exponentiator(BinaryOperator<T> multiplier, T one) {
		this.multiplier = multiplier;
		this.one = one;
	}

	// while result, binaryPower
	// e = b0*1 + b1*2 + b2*4 + b3*8 + b4*16 + ...
	// binaryPower = a^1, a^2, a^4, a^8, a^16,...
	// a^e = ...

	/**
	 * Calculates the binary expansion of an exponential
	 * 
	 * @param base
	 * @param exponent
	 * @return
	 */
	public T exp(T base, IntegerNumber exponent) {
		int signum = exponent.signum();

		if (signum == 0) {
			return one;
		} else if (exponent.isOne()) {
			return base;
		} else if (signum > 0) {
			int numBinaryDigits = 0;
			boolean[] binaryExpansion;
			IntegerNumber powerOf2 = numberFactory.oneInteger();

			for (IntegerNumber e = exponent; e.signum() > 0; e = numberFactory
					.divide(e, two)) {
				numBinaryDigits++;
				powerOf2 = numberFactory.multiply(powerOf2, two);
			}

			// at this point numBinaryDigits should be the number of binary
			// digits
			// necessary for the base 2 expansion of exponent. Example:
			// exponent=1:
			// numBinaryDigits=1 (expansion:1). exponent=2: numBinaryDigits=2
			// (10).
			// powerOf2 will equal 2^numBinaryDigits

			assert numBinaryDigits >= 1;
			binaryExpansion = new boolean[numBinaryDigits];

			// binaryExpansion will be the base-2 expansion of exponent.
			// binaryExpansion[i] will hold the digit for the 2^i term in the
			// expansion.

			powerOf2 = numberFactory.divide(powerOf2, two);
			for (int i = numBinaryDigits - 1; i >= 0; i--, powerOf2 = numberFactory
					.divide(powerOf2, two)) {
				IntegerNumber difference = numberFactory.subtract(exponent,
						powerOf2);

				if (difference.signum() >= 0) {
					binaryExpansion[i] = true;
					exponent = difference;
				}
			}

			// binaryExpansion now holds the binary expansion of (the original
			// value of) exponent and
			// exponent is now 0.

			assert exponent.signum() == 0;

			T binaryPower = base; // a^1
			T result = null;

			for (int i = 0; i < numBinaryDigits; i++) {
				if (i > 0)
					binaryPower = multiplier.apply(binaryPower, binaryPower);
				if (binaryExpansion[i])
					result = result == null ? binaryPower : multiplier.apply(
							result, binaryPower);
			}
			return result;
		} else {
			throw new IllegalArgumentException(
					"Negative exponent not allowed: " + exponent);
		}
	}
}
