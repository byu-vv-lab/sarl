package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A non-trivial monomial is the product of a constant and a monic. The constant
 * must not be 0 or 1 and the monic must not be empty.
 * 
 * @author siegel
 * 
 */
public class NTMonomial extends CommonSymbolicExpression implements Monomial {

	private SymbolicMap polynomialMap = null;

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
		if (polynomialMap == null)
			polynomialMap = factory.singletonMap((Monic) argument(1), this);
		return polynomialMap;
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
	public NumericExpression plus(IdealFactory factory, NumericExpression expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Polynomial expand(IdealFactory factory) {
		Monic monic = this.monic();
		Polynomial expandedMonic = monic.expand(factory);

		if (monic.equals(expandedMonic))
			return this;
		return (Polynomial) monomialConstant().times(factory, expandedMonic);
	}

	@Override
	public NumericExpression times(IdealFactory factory, NumericExpression expr) {
		if (expr instanceof Constant) {
			Constant that = (Constant) expr;

			if (that.isZero())
				return that;
			if (that.isOne())
				return this;
			return factory
					.monomial(
							(Constant) that.times(factory, monomialConstant()),
							monic());
		} else if (expr instanceof Monic) {
			Monic that = (Monic) expr;

			return factory.monomial(monomialConstant(),
					(Monic) that.times(factory, monic()));

		} else if (expr instanceof Monomial) {
			Monomial that = (Monomial) expr;

			return factory.monomial((Constant) (that.monomialConstant(factory)
					.times(factory, monomialConstant())), (Monic) (that
					.monic(factory).times(factory, monic())));
		}
		return expr.times(factory, this);
	}

	@Override
	public NumericExpression negate(IdealFactory factory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Polynomial intDivide(IdealFactory factory, Polynomial expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Polynomial modulo(IdealFactory factory, Polynomial expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NumericExpression invert(IdealFactory factory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isZero() {
		return false;
	}

	@Override
	public boolean isOne() {
		return false;
	}
}
