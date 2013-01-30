package edu.udel.cis.vsl.sarl.ideal;

/**
 * A quotient of a two factored polynomials.
 * 
 * @author siegel
 * 
 */
public interface RationalExpression extends NumericExpression {

	FactoredPolynomial numerator(IdealFactory factory);

	FactoredPolynomial denominator(IdealFactory factory);

}
