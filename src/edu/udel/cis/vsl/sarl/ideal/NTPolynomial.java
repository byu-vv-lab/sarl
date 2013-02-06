package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A non-trivial polynomial is the sum of at least 2 monomials with different
 * underlying monics, e.g., 1+x^2, x+y, or x+xy.
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
	protected NTPolynomial(MonomialSum monomialSum, Monomial factorization) {
		super(SymbolicOperator.CHOICE, monomialSum.type(), monomialSum,
				factorization);
		assert monomialSum.numTerms() >= 2;
	}

	@Override
	public SymbolicMap termMap(IdealFactory factory) {
		return polynomialMap();
	}

	public SymbolicMap polynomialMap() {
		return (SymbolicMap) ((SymbolicExpressionIF) argument(0)).argument(0);
	}

	@Override
	public Monomial leadingTerm() {
		SymbolicMap map = polynomialMap();

		if (map.isEmpty())
			return null;
		return (Monomial) map.iterator().next();
	}

	@Override
	public Monomial factorization(IdealFactory factory) {
		return (Monomial) argument(1);
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
	public NumericExpression plus(IdealFactory factory, NumericExpression expr) {
		// TODO: deal with factorization here.
		return null;
	}

	@Override
	public NumericExpression times(IdealFactory factory, NumericExpression expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NumericExpression negate(IdealFactory factory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NumericExpression invert(IdealFactory factory) {
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
	public boolean isZero() {
		return false;
	}

	@Override
	public boolean isOne() {
		return false;
	}

	// public NumericExpression add(IdealFactory factory, NumericExpression
	// expr) {
	// if (expr instanceof Polynomial) {
	// Polynomial that = (Polynomial) expr;
	// SymbolicMap thatMap = that.polynomialMap(factory);
	// SymbolicMap thisMap = this.polynomialMap(factory);
	// MonomialAdder monomialAdder = factory.newMonomialAdder();
	// SymbolicMap newMap = thisMap.combine(monomialAdder, thatMap);
	//
	// if (newMap.isEmpty())
	// return factory.zero(type());
	// if (newMap.size() == 1) // return the monomial
	// return (Monomial) newMap.iterator().next();
	// else
	// return factory.reducedPolynomial(type(), newMap);
	// }
	// return expr.add(factory, this);
	// }

}
