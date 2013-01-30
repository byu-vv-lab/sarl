package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A non-trivial polynomial. It is the sum of monomials.
 * 
 * The set of monomials is represented as a map. A key in this map is a Monic.
 * The value associated to the Monic is a Monomial.
 * 
 * @author siegel
 * 
 */
public class NTPolynomial extends CommonSymbolicExpression implements
		Polynomial {

	/**
	 * The leading term should be the term corresponding to the maximal monic in
	 * the fixed total order on monics. Or null if the monomialMap is empty.
	 * 
	 * @param type
	 * @param monomialMap
	 * @param leadingTerm
	 */
	protected NTPolynomial(SymbolicTypeIF type, SymbolicMap monomialMap) {
		super(SymbolicOperator.ADD, type, monomialMap);
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
	public Polynomial polynomial(IdealFactory factory) {
		return this;
	}

	@Override
	public Factorization factorization(IdealFactory factory) {
		return factory.monicFactorization(type(),
				factory.singletonMap(this, this));
	}

	@Override
	public FactoredPolynomial numerator(IdealFactory factory) {
		return this;
	}

	@Override
	public FactoredPolynomial denominator(IdealFactory factory) {
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
				return (Polynomial) factory.canonic(new NTPolynomial(type(),
						newMap));
		}
		return expr.add(factory, this);
	}
}
