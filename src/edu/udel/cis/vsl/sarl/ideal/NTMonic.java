package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A non-trivial monic is the product of at least two primitive powers. The set
 * of primitive powers comprising this product is represented as a map.
 * 
 * A key in the map is primitive. The value associated to that key is a
 * PrimitivePower.
 * 
 * @author siegel
 * 
 */
public class NTMonic extends CommonSymbolicExpression implements Monic {

	private SymbolicMap polynomialMap = null;

	protected NTMonic(SymbolicTypeIF type, SymbolicMap factorMap) {
		super(SymbolicOperator.MULTIPLY, type, factorMap);
		assert factorMap.size() >= 2;
	}

	@Override
	public Constant monomialConstant(IdealFactory factory) {
		return factory.one(type());
	}

	@Override
	public Monic monic(IdealFactory factory) {
		return this;
	}

	@Override
	public SymbolicMap termMap(IdealFactory factory) {
		if (polynomialMap == null)
			polynomialMap = factory.singletonMap(this, this);
		return polynomialMap;
	}

	@Override
	public SymbolicMap monicFactors(IdealFactory factory) {
		return monicFactors();
	}

	public SymbolicMap monicFactors() {
		return (SymbolicMap) argument(0);
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
	public boolean isTrivialMonic() {
		return false;
	}

	@Override
	public Polynomial expand(IdealFactory factory) {
		Polynomial result = factory.one(type());

		for (SymbolicExpressionIF expr : monicFactors()) {
			PrimitivePower ppower = (PrimitivePower) expr;

			result = factory.multiply(result, ppower.expand(factory));
		}
		return result;
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

		for (SymbolicExpressionIF expr : monicFactors())
			buffer.append(expr.atomString());
		return buffer;
	}

	@Override
	public String toString() {
		return toStringBuffer().toString();
	}

}
