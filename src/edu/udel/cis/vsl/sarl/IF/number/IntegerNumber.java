package edu.udel.cis.vsl.sarl.IF.number;

/** An instance of this class represents an integer number. */
public interface IntegerNumber extends Number {

	/**
	 * Attempts to extract a Java int value from the IntegerNumberIF. The answer
	 * could be wrong if the integer value is outside of the range of the Java
	 * int type.
	 */
	int intValue();
}
