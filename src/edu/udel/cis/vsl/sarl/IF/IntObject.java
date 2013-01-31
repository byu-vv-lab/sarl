package edu.udel.cis.vsl.sarl.IF;

public interface IntObject extends SymbolicObject {

	int getInt();

	IntObject minWith(IntObject that);

	IntObject maxWith(IntObject that);

	IntObject minus(IntObject that);

	IntObject plus(IntObject that);

	int signum();

	boolean isZero();

	boolean isPositive();

	boolean isNegative();

}
