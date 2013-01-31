package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.IntObject;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A power of a polynomial. The polynomial should be reduced (monic if real
 * type, ....).
 * 
 * @author siegel
 * 
 */
public class NTReducedPolynomialPower extends CommonSymbolicExpression
		implements ReducedPolynomialPower {

	private SymbolicMap monicFactorizationMap = null;

	private Constant factorizationConstant = null;

	protected NTReducedPolynomialPower(Polynomial base, IntObject exponent) {
		super(SymbolicOperator.POWER, base.type(), base, exponent);
	}

	@Override
	public ReducedPolynomial polynomialPowerBase(IdealFactory factory) {
		return (ReducedPolynomial) argument(0);
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
