package edu.udel.cis.vsl.sarl.number.real;

import java.math.BigInteger;

import edu.udel.cis.vsl.sarl.IF.RationalNumberIF;

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
public class RealRational extends RealNumber implements RationalNumberIF {

	private BigInteger numerator;

	private BigInteger denominator;

	public RealRational(BigInteger numerator, BigInteger denominator) {
		assert numerator != null;
		assert denominator != null;
		assert denominator.signum() != 0;
		this.numerator = numerator;
		this.denominator = denominator;
	}

	@Override
	public int signum() {
		return numerator.signum();
	}

	BigInteger numerator() {
		return numerator;
	}

	BigInteger denominator() {
		return denominator;
	}

	@Override
	public String toString() {
		if (denominator.equals(BigInteger.ONE)) {
			return numerator.toString();
		} else {
			return numerator.toString() + "/" + denominator.toString();
		}
	}

	@Override
	public String atomString() {
		return "(" + toString() + ")";
	}

	@Override
	public boolean isZero() {
		return numerator == BigInteger.ZERO;
	}

	@Override
	public boolean isOne() {
		return numerator.equals(denominator);
	}

}
