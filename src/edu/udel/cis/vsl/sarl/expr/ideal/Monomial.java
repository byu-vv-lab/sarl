package edu.udel.cis.vsl.sarl.expr.ideal;

/**
 * A Monomial is the product of a constant and a Monic.
 * 
 * @author siegel
 * 
 */
public interface Monomial extends Polynomial {

	Constant monomialConstant(IdealFactory factory);

	Monic monic(IdealFactory factory);

	/**
	 * If the monomial contains any primitives which are ReducedPolynomials,
	 * they are multiplied out (expanded) into polynomials involving only
	 * ordinary primitives.
	 * 
	 * @param factory
	 * @return equivalent polynomial with no ReducedPolynomial primitives in
	 *         terms
	 */
	Polynomial expand(IdealFactory factory);

}
