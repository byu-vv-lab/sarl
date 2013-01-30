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

	protected NTRationalExpression(FactoredPolynomial numerator,
			FactoredPolynomial denominator) {
		super(SymbolicOperator.DIVIDE, numerator.type(), numerator, denominator);
	}

	public FactoredPolynomial numerator(IdealFactory factory) {
		return (FactoredPolynomial) argument(0);
	}

	public FactoredPolynomial denominator(IdealFactory factory) {
		return (FactoredPolynomial) argument(1);
	}

	@Override
	public NumericExpression add(IdealFactory factory, NumericExpression expr) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
