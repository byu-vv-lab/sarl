package edu.udel.cis.vsl.sarl.IF.expr;

import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

/**
 * A "symbolic constant" is a symbol used in symbolic execution to represent an
 * input value. It is "constant" in the sense that its value does not change in
 * the course of an execution of the program. A symbolic constant is determined
 * by two things: a name (which is a String), and a type (instance of
 * SymbolicTypeIF). Two distinct symbolic constants may have the same name, but
 * different types. Two symbolic constants are considered equal iff their names
 * are equal and their types are equal.
 * 
 * Symbolic constants are symbolic expressions and are therefore immutable.
 * 
 * @author siegel
 * 
 */
public interface SymbolicConstant extends SymbolicExpression {

	/** Returns the name of this symbolic constant */
	StringObject name();

	/** Returns the type of this symbolic constant */
	SymbolicType type();

}
