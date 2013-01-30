package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A non-trivial factored polynomial. Has two arguments: a polynomial and a
 * Factorization of that polynomial. This kind of expression is unusual in that
 * the value of the expression is determined by either argument, not by
 * combining the arguments.
 * 
 * @author siegel
 * 
 */
public class NTFactoredPolynomial extends CommonSymbolicExpression implements
		FactoredPolynomial {

	private Constant denominator = null;

	protected NTFactoredPolynomial(Polynomial polynomial,
			Factorization factorization) {
		super(SymbolicOperator.CHOICE, polynomial.type(), polynomial,
				factorization);
	}

	public Polynomial polynomial(IdealFactory factory) {
		return (Polynomial) argument(0);
	}

	public Factorization factorization(IdealFactory factory) {
		return (Factorization) argument(1);
	}

	@Override
	public FactoredPolynomial numerator(IdealFactory factory) {
		return this;
	}

	@Override
	public FactoredPolynomial denominator(IdealFactory factory) {
		if (denominator == null)
			denominator = factory.one(type());
		return denominator;
	}

	@Override
	public NumericExpression add(IdealFactory factory, NumericExpression expr) {
		// TODO Auto-generated method stub
		return null;
	}

}
