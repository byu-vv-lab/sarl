package edu.udel.cis.vsl.sarl.IF.expr;

/**
 * A non-trivial reference expression, i.e., one which is not the null reference
 * or the identity reference.
 * 
 * @author siegel
 * 
 */
public interface NTReferenceExpression extends ReferenceExpression {

	/**
	 * As this is a reference to an array element, tuple component, union
	 * member, or an offset reference, returns the reference to the parent,
	 * i.e., the array, tuple, union, or the other reference, resp.
	 */
	ReferenceExpression getParent();

}
