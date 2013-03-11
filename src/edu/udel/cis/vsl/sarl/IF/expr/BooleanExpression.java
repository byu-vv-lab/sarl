package edu.udel.cis.vsl.sarl.IF.expr;

/**
 * A symbolic expression of boolean type.
 * 
 * @author siegel
 * 
 */
public interface BooleanExpression extends SymbolicExpression {

	/**
	 * Returns the i-th argument of this expression in the case where the i-th
	 * argument should be an instance of BooleanExpression. A SARLException is
	 * thrown if that argument is not an instance of BooleanExpression, or if i
	 * is out of range.
	 * 
	 * @param i
	 *            integer in range [0,numArgs-1]
	 * @return the i-th argument of this expression
	 */
	BooleanExpression booleanArg(int i);

	/**
	 * Returns the i-th argument of this expression in the case where the i-th
	 * argument should be an instance of Iterable<? extends BooleanExpression>.
	 * A SARLException is thrown if that argument is not an instance of
	 * Iterable<? extends BooleanExpression>, or if i is out of range.
	 * 
	 * @param i
	 *            integer in range [0,numArgs-1]
	 * @return the i-th argument of this expression
	 */
	Iterable<? extends BooleanExpression> booleanCollectionArg(int i);
}
