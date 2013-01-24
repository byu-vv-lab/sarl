package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A non-trivial monomial. It is the product of a constant and a monic.
 * 
 * @author siegel
 * 
 */
public class NTMonomial extends CommonSymbolicExpression implements Monomial {

	protected NTMonomial(Constant constant, Monic monic) {
		super(SymbolicOperator.MULTIPLY, constant.type(), constant, monic);
	}

	public Constant constant() {
		return (Constant) argument(0);
	}

	public Monic monic() {
		return (Monic) argument(1);
	}

}
