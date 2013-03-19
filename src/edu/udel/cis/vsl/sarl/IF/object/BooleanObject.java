package edu.udel.cis.vsl.sarl.IF.object;

/**
 * A symbolic object wrapping a single boolean value.
 * 
 * @author siegel
 * 
 */
public interface BooleanObject extends SymbolicObject,
		Comparable<BooleanObject> {

	boolean getBoolean();

}
