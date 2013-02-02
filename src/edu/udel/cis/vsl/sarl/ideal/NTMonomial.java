package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A non-trivial monomial is the product of a constant and a monic. The constant
 * must not be 1 and the monic must not be empty.
 * 
 * @author siegel
 * 
 */
public class NTMonomial extends CommonSymbolicExpression implements Monomial {

	private SymbolicMap polynomialMap = null;

	protected NTMonomial(Constant constant, Monic monic) {
		super(SymbolicOperator.MULTIPLY, constant.type(), constant, monic);
	}

	public Monic monic(IdealFactory factory) {
		return (Monic) argument(1);
	}

	@Override
	public SymbolicMap polynomialMap(IdealFactory factory) {
		if (polynomialMap == null)
			polynomialMap = factory.singletonMap((Monic) argument(1), this);
		return polynomialMap;
	}

	@Override
	public Constant monomialConstant(IdealFactory factory) {
		return (Constant) argument(0);
	}

	@Override
	public Factorization factorization(IdealFactory factory) {
		return this;
	}

	@Override
	public Polynomial numerator(IdealFactory factory) {
		return this;
	}

	@Override
	public Polynomial denominator(IdealFactory factory) {
		return factory.one(type());
	}

	@Override
	public Monomial leadingTerm() {
		return this;
	}

	@Override
	public NumericExpression add(IdealFactory factory, NumericExpression expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Constant factorizationConstant(IdealFactory factory) {
		return monomialConstant(factory);
	}

	@Override
	public MonicFactorization monicFactorization(IdealFactory factory) {
		return monic(factory);
	}

}
