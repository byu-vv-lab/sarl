package edu.udel.cis.vsl.sarl.ideal;

/**
 * A quotient of a two factored polynomials.
 * 
 * @author siegel
 * 
 */
public interface RationalExpression extends NumericExpression {

	Polynomial numerator(IdealFactory factory);

	Polynomial denominator(IdealFactory factory);

}
