package edu.udel.cis.vsl.sarl.number.real;

import java.math.BigInteger;

import edu.udel.cis.vsl.sarl.number.IF.IntegerNumberIF;

/**
 * An infinite precision representation of integer numbers, based on Java's
 * BigInteger class. There is no bound on such an integer.
 * 
 * Because we are using the flyweight pattern, two RealIntegers will represent
 * the same integer iff they are the same (==) object. Hence we keep the equals
 * and hashCode methods inherited from Object.
 */
public class RealInteger implements IntegerNumberIF {

	private BigInteger value;

	public RealInteger(BigInteger value) {
		assert value != null;
		this.value = value;
	}

	public int signum() {
		return value.signum();
	}

	public String toString() {
		return value.toString();
	}

	public BigInteger value() {
		return value;
	}

	public String atomString() {
		return toString();
	}

	public boolean isZero() {
		return value == BigInteger.ZERO;
	}

	public boolean isOne() {
		return value == BigInteger.ONE;
	}

	public int intValue() {
		return value.intValue();
	}
}
