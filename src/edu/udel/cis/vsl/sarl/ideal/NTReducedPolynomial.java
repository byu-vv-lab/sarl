package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.IntObject;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;

public class NTReducedPolynomial extends NTPolynomial implements
		ReducedPolynomial {

	protected NTReducedPolynomial(SymbolicTypeIF type, SymbolicMap monomialMap) {
		super(type, monomialMap);
	}

	@Override
	public IntObject polynomialPowerExponent(IdealFactory factory) {
		return factory.oneIntObject();
	}

	@Override
	public ReducedPolynomial polynomialPowerBase(IdealFactory factory) {
		return this;
	}

	@Override
	public SymbolicMap monicFactorizationMap(IdealFactory factory) {
		return factory.singletonMap(this, this);
	}

	@Override
	public Constant factorizationConstant(IdealFactory factory) {
		return factory.one(type());
	}

	@Override
	public MonicFactorization monicFactorization(IdealFactory factory) {
		return this;
	}

}
