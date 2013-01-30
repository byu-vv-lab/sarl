package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.IntObject;
import edu.udel.cis.vsl.sarl.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A power of a polynomial. The polynomial should be reduced (monic if real
 * type, ....).
 * 
 * @author siegel
 * 
 */
public class NTPolynomialPower extends CommonSymbolicExpression implements
		PolynomialPower {

	private SymbolicMap monicFactorizationMap = null;

	private Constant factorizationConstant = null;

	protected NTPolynomialPower(Polynomial base, IntObject exponent) {
		super(SymbolicOperator.POWER, base.type(), base, exponent);
	}

	@Override
	public Polynomial polynomialPowerBase(IdealFactory factory) {
		return (Polynomial) argument(0);
	}

	@Override
	public IntObject polynomialPowerExponent(IdealFactory factory) {
		return (IntObject) argument(1);
	}

	@Override
	public SymbolicMap monicFactorizationMap(IdealFactory factory) {
		if (monicFactorizationMap == null)
			monicFactorizationMap = factory.singletonMap(
					(Polynomial) argument(0), this);
		return monicFactorizationMap;
	}

	@Override
	public Constant factorizationConstant(IdealFactory factory) {
		if (factorizationConstant == null)
			factorizationConstant = factory.one(type());
		return factorizationConstant;
	}

	@Override
	public MonicFactorization monicFactorization(IdealFactory factory) {
		return this;
	}

}
