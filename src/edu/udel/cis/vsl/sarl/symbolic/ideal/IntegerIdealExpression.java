package edu.udel.cis.vsl.sarl.symbolic.ideal;

import edu.udel.cis.vsl.sarl.symbolic.factorpoly.FactoredPolynomial;

/**
 * The symnbolic expression class in the ideal symbolic universe used to
 * represent integer-valued symbolic expressions. Wraps an instance of
 * FactoredPolynomial, the canonical form which is multivariate polynomial
 * together with a factorization of the polynomial.
 * 
 * @author siegel
 * 
 */
public class IntegerIdealExpression extends IdealExpression {

	protected IntegerIdealExpression(FactoredPolynomial fp) {
		super(fp);
		assert type().isInteger();
	}

	public FactoredPolynomial factoredPolynomial() {
		return (FactoredPolynomial) expression;
	}
}
