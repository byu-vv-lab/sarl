package edu.udel.cis.vsl.sarl.IF.type;

/**
 * A function type is specified by the number and types of inputs, and a single
 * output type. It represents an abstract (mathematical) function from the
 * Cartesian product of the input sets to the output set.
 * 
 * @author siegel
 * 
 */
public interface SymbolicFunctionType extends SymbolicType {

	/**
	 * The ordered sequence of input types. These can be any number (including
	 * 0) of non-null types.
	 * 
	 * @return the inputs type sequence
	 */
	SymbolicTypeSequence inputTypes();

	/**
	 * The output type, a non-null symbolic type.
	 * 
	 * @return the output type
	 */
	SymbolicType outputType();

}
