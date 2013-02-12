package edu.udel.cis.vsl.sarl.IF.object;

public interface IntObject extends SymbolicObject, Comparable<IntObject> {

	int getInt();

	IntObject minWith(IntObject that);

	IntObject maxWith(IntObject that);

	IntObject minus(IntObject that);

	IntObject plus(IntObject that);

	int signum();

	boolean isZero();

	boolean isOne();

	boolean isPositive();

	boolean isNegative();

}
