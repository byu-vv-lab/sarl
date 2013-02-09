package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;

/**
 * A polynomial: an expression which is the sum of monomials.
 * 
 * 
 * @author siegel
 * 
 */
public interface Polynomial extends RationalExpression {

	/**
	 * Map from Monic to Monomial. The polynomial is sum of the monomials.
	 * 
	 * @return
	 */
	SymbolicMap termMap(IdealFactory factory);

	/**
	 * The leading term of this polynomial, or null if the polynomial is 0.
	 * 
	 * @return
	 */
	Monomial leadingTerm();

	/**
	 * Returns a factorization of this polynomial expressed as a Monomial in
	 * which the "variables" are ReducedPolynomials as well as other standard
	 * NumericPrimitives, such as symbolic constants, etc.
	 */
	Monomial factorization(IdealFactory factory);

	int degree();

}
