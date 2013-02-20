package edu.udel.cis.vsl.sarl.expr.ideal;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;

/**
 * A non-trivial monomial is the product of a constant and a monic. The constant
 * must not be 0 or 1 and the monic must not be empty.
 * 
 * @author siegel
 * 
 */
public class NTMonomial extends IdealExpression implements Monomial {

	private SymbolicMap termMap = null;

	protected NTMonomial(Constant constant, Monic monic) {
		super(SymbolicOperator.MULTIPLY, constant.type(), constant, monic);
		assert !constant.isZero();
		assert !constant.isOne();
		assert !monic.isOne();
	}

	@Override
	public Monic monic(IdealFactory factory) {
		return (Monic) argument(1);
	}

	public Monic monic() {
		return (Monic) argument(1);
	}

	@Override
	public SymbolicMap termMap(IdealFactory factory) {
		if (termMap == null)
			termMap = factory.singletonMap((Monic) argument(1), this);
		return termMap;
	}

	@Override
	public Constant monomialConstant(IdealFactory factory) {
		return (Constant) argument(0);
	}

	public Constant monomialConstant() {
		return (Constant) argument(0);
	}

	@Override
	public Monomial factorization(IdealFactory factory) {
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
	public Polynomial expand(IdealFactory factory) {
		Monic monic = this.monic();
		Polynomial expandedMonic = monic.expand(factory);

		if (monic.equals(expandedMonic))
			return this;
		return factory.multiply(monomialConstant(), expandedMonic);
	}

	// @Override
	// public boolean isZero() {
	// return false;
	// }
	//
	// @Override
	// public boolean isOne() {
	// return false;
	// }

	@Override
	public String toString() {
		return monomialConstant().toString() + monic().toString();
	}

	@Override
	public int degree() {
		return monic().degree();
	}

	@Override
	public IdealKind idealKind() {
		return IdealKind.NTMonomial;
	}

}
