package edu.udel.cis.vsl.sarl.IF.object;

/**
 * A symbolic object wrapping a single String.
 * 
 * @author siegel
 * 
 */
public interface StringObject extends SymbolicObject, Comparable<StringObject> {

	/**
	 * Gets the string.
	 * 
	 * @return the string
	 */
	String getString();

}
