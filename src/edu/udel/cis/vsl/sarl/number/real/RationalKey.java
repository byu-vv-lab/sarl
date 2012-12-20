package edu.udel.cis.vsl.sarl.number.real;

import java.math.BigInteger;

/**
 * A key used in the rational map. This is needed in order to implement the
 * flyweight pattern on RealRational.
 */
public class RationalKey {

	BigInteger numerator;

	BigInteger denominator;

	RationalKey(BigInteger numerator, BigInteger denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}

	public boolean equals(Object object) {
		if (object instanceof RationalKey) {
			RationalKey that = (RationalKey) object;

			return numerator.equals(that.numerator)
					&& denominator.equals(that.denominator);
		}
		return false;
	}

	public int hashCode() {
		return numerator.hashCode() + denominator.hashCode();
	}
}
