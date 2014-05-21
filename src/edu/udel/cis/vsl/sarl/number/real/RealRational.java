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

import java.math.BigInteger;

import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;

/**
 * An infinite precision representation of the mathematical rational numbers. A
 * rational number is represented as a quotient of two integers whose gcd is 1.
 * The denominator must be positive. The rational number 0 is represented as
 * 0/1. This specifies a unique numerator/denominator representation of each
 * rational number. The integers are instances of Java's BigInteger class.
 * 
 * Because we are using the flyweight pattern, two RealRationals will represent
 * the same rational number iff they are the same (==) object. Hence we keep the
 * equals and hashCode methods inherited from Object.
 * 
 * @author siegel
 * 
 */
public class RealRational extends RealNumber implements RationalNumber {

	private BigInteger numerator;

	private BigInteger denominator;

	/**
	 * Creates a rational number from supplied integer values Includes testing
	 * to detect null values and denominators of zero
	 * 
	 * @param numerator
	 * @param denominator
	 */
	public RealRational(BigInteger numerator, BigInteger denominator) {
		assert numerator != null;
		assert denominator != null;
		assert denominator.signum() != 0;
		this.numerator = numerator;
		this.denominator = denominator;
	}

	@Override
	/**
	 * Overrides the method signum.
	 * Determines signum solely based on the value of the numerator.
	 */
	public int signum() {
		return numerator.signum();
	}

	@Override
	public BigInteger numerator() {
		return numerator;
	}

	@Override
	public BigInteger denominator() {
		return denominator;
	}

	/**
	 * Overrides the method toString. Returns a string representation of a
	 * RealRational and prints only the numerator for rationals with denominator
	 * equal to one.
	 */
	@Override
	public String toString() {
		if (denominator.equals(BigInteger.ONE)) {
			return numerator.toString();
		} else {
			return numerator.toString() + "/" + denominator.toString();
		}
	}

	/**
	 * Overrides the atomString method. Returns a toString representation
	 * enclosed in parentheses.
	 */
	@Override
	public String atomString() {
		return "(" + toString() + ")";
	}

	/**
	 * Overrides the isZero method. A rational is only zero when its numerator
	 * is zero. This method simplifies isZero on RealRationals by only
	 * evaluating the numerator.
	 */
	@Override
	public boolean isZero() {
		return numerator == BigInteger.ZERO;
	}

	/**
	 * Overrides the isOne method. A rational is only one when its numerator and
	 * denominator are equal. This method compares the two values with .equals
	 */
	@Override
	public boolean isOne() {
		return numerator.equals(denominator);
	}

}
