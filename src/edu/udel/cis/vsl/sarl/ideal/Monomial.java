package edu.udel.cis.vsl.sarl.ideal;

/**
 * A Monomial is the product of a constant and a Monic.
 * 
 * @author siegel
 * 
 */
public interface Monomial extends Polynomial, Factorization {

	Constant monomialConstant(IdealFactory factory);

	Monic monic(IdealFactory factory);

}
