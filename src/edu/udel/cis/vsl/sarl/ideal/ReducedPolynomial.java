package edu.udel.cis.vsl.sarl.ideal;

/**
 * A reduced polynomial is a polynomial satisfying: (1) if the type is real then
 * the leading coefficient of the polynomial is 1, and (2) if the type is
 * integer then the leading coefficient is positive and the GCD of the absolute
 * values of the coefficients is 1.
 * 
 * @author siegel
 * 
 */
public interface ReducedPolynomial extends Polynomial, ReducedPolynomialPower {

}
