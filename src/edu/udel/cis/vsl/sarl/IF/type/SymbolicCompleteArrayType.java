package edu.udel.cis.vsl.sarl.IF.type;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;

/**
 * A complete array type specifies not only the element type but also the extent
 * (length) of the array. The extent is a symbolic expression of integer type.
 * 
 * @author siegel
 * 
 */
public interface SymbolicCompleteArrayType extends SymbolicArrayType {

	/**
	 * Returns the extent (length) of any array of this type. This is a non-null
	 * integer-valued symbolic expression.
	 */
	NumericExpression extent();

}
