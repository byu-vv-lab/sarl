package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.IntObject;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A non-trivial power of a monic polynomial.
 * 
 * @author siegel
 * 
 */
public class NTMonicPolynomialPower extends CommonSymbolicExpression implements
		MonicPolynomialPower {

	protected NTMonicPolynomialPower(Polynomial base, IntObject exponent) {
		super(SymbolicOperator.POWER, base.type(), base, exponent);
	}

	public Polynomial base() {
		return (Polynomial) argument(0);
	}

	public IntObject exponent() {
		return (IntObject) argument(1);
	}

}
