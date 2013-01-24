package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

public class NTFactorization extends CommonSymbolicExpression implements
		Factorization {

	protected NTFactorization(Constant constant,
			MonicFactorization monicFactorization) {
		super(SymbolicOperator.MULTIPLY, constant.type(), constant,
				monicFactorization);
	}

	public Constant constant() {
		return (Constant) argument(0);
	}

	public MonicFactorization monicFactorizatio() {
		return (MonicFactorization) argument(1);
	}

}
