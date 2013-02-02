package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
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
	protected NTPolynomial(SymbolicTypeIF type, SymbolicMap monomialMap,
			Factorization factorization) {
		super(SymbolicOperator.ADD, type, monomialMap, factorization);
		assert monomialMap.size() >= 2;
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
		return (Factorization) argument(1);
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
		// TODO: deal with factorization here.
		return null;
	}

}
