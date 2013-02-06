package edu.udel.cis.vsl.sarl.ideal;

/**
 * A quotient of a two polynomials. The second polynomial (the denominator) must
 * be monic-like: if real, the leading coefficient is 1; if integer, the leading
 * coefficient is positive and the GCD of the absolute values of the
 * coefficients is 1.
 * 
 * @author siegel
 * 
 */
public interface RationalExpression extends NumericExpression {

	Polynomial numerator(IdealFactory factory);

	Polynomial denominator(IdealFactory factory);

}
