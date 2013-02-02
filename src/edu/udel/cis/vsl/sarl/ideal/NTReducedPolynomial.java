package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.IntObject;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

public class NTReducedPolynomial extends CommonSymbolicExpression implements
		ReducedPolynomial {

	protected NTReducedPolynomial(SymbolicTypeIF type, SymbolicMap monomialMap) {
		super(SymbolicOperator.ADD, type, monomialMap);
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

	@Override
	public SymbolicMap polynomialMap(IdealFactory factory) {
		return (SymbolicMap) argument(0);
	}

	@Override
	public Monomial leadingTerm() {
		SymbolicMap map = (SymbolicMap) argument(0);

		if (map.isEmpty())
			return null;
		return (Monomial) map.iterator().next();
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
	public NumericExpression add(IdealFactory factory, NumericExpression expr) {
		if (expr instanceof Polynomial) {
			Polynomial that = (Polynomial) expr;
			SymbolicMap thatMap = that.polynomialMap(factory);
			SymbolicMap thisMap = this.polynomialMap(factory);
			MonomialAdder monomialAdder = factory.newMonomialAdder();
			SymbolicMap newMap = thisMap.combine(monomialAdder, thatMap);

			if (newMap.isEmpty())
				return factory.zero(type());
			if (newMap.size() == 1) // return the monomial
				return (Monomial) newMap.iterator().next();
			else
				return factory.reducedPolynomial(type(), newMap);
		}
		return expr.add(factory, this);
	}

}
