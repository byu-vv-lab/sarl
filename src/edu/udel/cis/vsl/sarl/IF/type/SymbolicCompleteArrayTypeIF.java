package edu.udel.cis.vsl.sarl.IF.type;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;


/**
 * A complete array type specifies not only the elementn type but also the
 * extent (length) of the array. This is a symbolic expression of integer type.
 * 
 * @author siegel
 * 
 */
public interface SymbolicCompleteArrayTypeIF extends SymbolicArrayTypeIF {

	/**
	 * The extent of arrays in this type. Non-null integer-valued symbolic
	 * expressions.
	 */
	SymbolicExpressionIF extent();

}
