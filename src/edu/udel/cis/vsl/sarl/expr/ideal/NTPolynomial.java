package edu.udel.cis.vsl.sarl.expr.ideal;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

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
public class NTPolynomial extends IdealExpression implements Polynomial {

	/**
	 * The factorization is extrinsic data: not used in hashCode or equals.
	 */
	private Monomial factorization;

	private int degree = -1;

	/**
	 * The leading term should be the term corresponding to the maximal monic in
	 * the fixed total order on monics. Or null if the monomialMap is empty.
	 * 
	 * @param type
	 * @param monomialMap
	 * @param leadingTerm
	 */
	protected NTPolynomial(SymbolicMap<Monic, Monomial> termMap,
			Monomial factorization) {
		super(SymbolicOperator.ADD, factorization.type(), termMap);
		assert termMap.size() >= 2;
		this.factorization = factorization;
	}

	@Override
	public SymbolicMap<Monic, Monomial> termMap(IdealFactory factory) {
		return termMap();
	}

	@SuppressWarnings("unchecked")
	public SymbolicMap<Monic, Monomial> termMap() {
		return (SymbolicMap<Monic, Monomial>) argument(0);
	}

	@Override
	public Monomial leadingTerm() {
		SymbolicMap<Monic, Monomial> map = termMap();

		if (map.isEmpty())
			return null;
		return map.iterator().next();
	}

	@Override
	public Monomial factorization(IdealFactory factory) {
		return factorization;
	}

	@Override
	public Polynomial numerator(IdealFactory factory) {
		return this;
	}

	@Override
	public Polynomial denominator(IdealFactory factory) {
		return factory.one(type());
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

	public StringBuffer toStringBuffer() {
		StringBuffer buffer = new StringBuffer();
		boolean first = true;

		for (SymbolicExpression expr : termMap()) {
			if (first)
				first = false;
			else
				buffer.append("+");
			buffer.append(expr.toString());
		}
		return buffer;
	}

	@Override
	public String toString() {
		return toStringBuffer().toString();
	}

	@Override
	public int degree() {
		if (degree < 0) {
			degree = 0;
			for (SymbolicExpression expr : termMap().keys()) {
				int termDegree = ((Monic) expr).degree();

				if (termDegree > degree)
					degree = termDegree;
			}
		}
		return degree;
	}

	@Override
	public IdealKind idealKind() {
		return IdealKind.NTPolynomial;
	}

}
