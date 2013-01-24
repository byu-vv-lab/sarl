package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

public class NTRationalExpression extends CommonSymbolicExpression implements
		RationalExpression {

	protected NTRationalExpression(FactoredPolynomial numerator,
			FactoredPolynomial denominator) {
		super(SymbolicOperator.DIVIDE, numerator.type(), numerator, denominator);
	}

	public FactoredPolynomial numerator() {
		return (FactoredPolynomial) argument(0);
	}

	public FactoredPolynomial denominator() {
		return (FactoredPolynomial) argument(1);
	}

}
