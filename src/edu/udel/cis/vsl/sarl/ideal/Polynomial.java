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
	 * Map from Monic to Monomial. Polynomial is sum of the monomials.
	 * 
	 * @return
	 */
	SymbolicMap polynomialMap(IdealFactory factory);

	/**
	 * The leading term of this polynomial, or null if the polynomial is 0.
	 * 
	 * @return
	 */
	Monomial leadingTerm();

	Monomial factorization(IdealFactory factory);

}
