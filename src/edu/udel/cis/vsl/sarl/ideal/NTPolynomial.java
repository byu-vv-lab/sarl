package edu.udel.cis.vsl.sarl.ideal;

import java.util.Iterator;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;

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
	protected NTPolynomial(SymbolicMap termMap, Monomial factorization) {
		super(SymbolicOperator.ADD, factorization.type(), termMap);
		assert termMap.size() >= 2;
		this.factorization = factorization;
	}

	@Override
	public SymbolicMap termMap(IdealFactory factory) {
		return termMap();
	}

	public SymbolicMap termMap() {
		return (SymbolicMap) argument(0);
	}

	@Override
	public Monomial leadingTerm() {
		SymbolicMap map = termMap();

		if (map.isEmpty())
			return null;
		return (Monomial) map.iterator().next();
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

	@Override
	public boolean isZero() {
		return false;
	}

	@Override
	public boolean isOne() {
		return false;
	}

	public StringBuffer toStringBuffer() {
		StringBuffer buffer = new StringBuffer();
		boolean first = true;

		for (SymbolicExpressionIF expr : termMap()) {
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
			for (SymbolicExpressionIF expr : termMap().keys()) {
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

	@Override
	protected int compareIdeal(IdealExpression that) {
		NTPolynomial thatPoly = (NTPolynomial) that;
		int result = thatPoly.degree() - degree();

		if (result != 0)
			return result;

		Iterator<SymbolicExpressionIF> monomialIter1 = termMap().iterator();
		Iterator<SymbolicExpressionIF> monomialIter2 = thatPoly.termMap()
				.iterator();

		while (monomialIter1.hasNext()) {
			Monomial monomial1 = (Monomial) monomialIter1.next();

			if (monomialIter2.hasNext()) {
				Monomial monomial2 = (Monomial) monomialIter2.next();

				result = monomial1.compareTo(monomial2);
				if (result != 0)
					return result;
			} else {
				return -1;
			}
		}
		if (monomialIter2.hasNext())
			return 1;
		return 0;
	}

}
