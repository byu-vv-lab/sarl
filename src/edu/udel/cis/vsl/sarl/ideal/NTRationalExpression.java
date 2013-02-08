package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A nontrivial rational expression. It consists of a numerator and denominator,
 * both factored polynomials.
 * 
 * @author siegel
 * 
 */
public class NTRationalExpression extends CommonSymbolicExpression implements
		RationalExpression {

	protected NTRationalExpression(Polynomial numerator, Polynomial denominator) {
		super(SymbolicOperator.DIVIDE, numerator.type(), numerator, denominator);
		assert !denominator.isOne();
		assert !denominator.isZero();
		assert !numerator.isZero();
		assert !numerator.equals(denominator);
	}

	public Polynomial numerator(IdealFactory factory) {
		return (Polynomial) argument(0);
	}

	public Polynomial denominator(IdealFactory factory) {
		return (Polynomial) argument(1);
	}

	@Override
	public boolean isZero() {
		return false;
	}

	@Override
	public boolean isOne() {
		return false;
	}

}
