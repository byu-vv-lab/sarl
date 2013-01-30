package edu.udel.cis.vsl.sarl.ideal;

/**
 * A factored polynomial is a polynomial together with a factorization of that
 * polynomial.
 * 
 * @author siegel
 * 
 */
public interface FactoredPolynomial extends RationalExpression {

	Polynomial polynomial(IdealFactory factory);

	Factorization factorization(IdealFactory factory);

}
