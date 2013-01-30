package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A nontrivial factorization of a polynomial. Has two parts: a constant and a
 * MonicFactorization.
 * 
 * @author siegel
 * 
 */
public class NTFactorization extends CommonSymbolicExpression implements
		Factorization {

	protected NTFactorization(Constant constant,
			MonicFactorization monicFactorization) {
		super(SymbolicOperator.MULTIPLY, constant.type(), constant,
				monicFactorization);
	}

	public MonicFactorization monicFactorization(IdealFactory factory) {
		return (MonicFactorization) argument(1);
	}

	@Override
	public Constant factorizationConstant(IdealFactory factory) {
		return (Constant) argument(0);
	}

}
