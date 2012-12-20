package edu.udel.cis.vsl.sarl.symbolic.IF;

import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF;

/**
 * A "symbolic constant" is a symbol used in symbolic execution to represent an
 * input value. It is "constant" in the sense that its value does not change in
 * the course of an execution of the program. A symbolic constant is determined
 * by two things: a name (which is a String), and a type (instance of
 * SymbolicTypeIF). Two distinct symbolic constants may have the same name, but
 * different types. Two symbolic constants are considered equal if their names
 * are equal and their types are equal.
 * 
 * @author siegel
 * 
 */
public interface SymbolicConstantIF {

	/** Returns the name of this symbolic constant */
	String name();

	/** Returns the type of this symbolic constant */
	SymbolicTypeIF type();
}
