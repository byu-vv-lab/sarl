package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A non-trivial monic is the product of primitive powers. The set of primitive
 * powers comprising this product is represented as a map.
 * 
 * A key in the map is primitive. The value associated to that key is a
 * PrimitivePower.
 * 
 * @author siegel
 * 
 */
public class NTMonic extends CommonSymbolicExpression implements Monic {

	private Constant monomialConstant = null;

	private SymbolicMap polynomialMap = null;

	private Constant denominator = null;

	private Constant factorizationConstant = null;

	protected NTMonic(SymbolicTypeIF type, SymbolicMap factorMap) {
		super(SymbolicOperator.MULTIPLY, type, factorMap);
	}

	@Override
	public Constant monomialConstant(IdealFactory factory) {
		if (monomialConstant == null)
			monomialConstant = factory.one(type());
		return monomialConstant;
	}

	@Override
	public Monic monic(IdealFactory factory) {
		return this;
	}

	@Override
	public SymbolicMap polynomialMap(IdealFactory factory) {
		if (polynomialMap == null)
			polynomialMap = factory.singletonMap(this, this);
		return polynomialMap;
	}

	@Override
	public SymbolicMap monicFactors(IdealFactory factory) {
		return (SymbolicMap) argument(0);
	}

	@Override
	public Polynomial polynomial(IdealFactory factory) {
		return this;
	}

	@Override
	public Factorization factorization(IdealFactory factory) {
		return this;
	}

	@Override
	public FactoredPolynomial numerator(IdealFactory factory) {
		return this;
	}

	@Override
	public FactoredPolynomial denominator(IdealFactory factory) {
		if (denominator == null)
			denominator = factory.one(type());
		return denominator;
	}

	@Override
	public SymbolicMap monicFactorizationMap(IdealFactory factory) {
		return (SymbolicMap) argument(0);
	}

	@Override
	public Constant factorizationConstant(IdealFactory factory) {
		if (factorizationConstant == null)
			factorizationConstant = factory.one(type());
		return factorizationConstant;
	}

	@Override
	public MonicFactorization monicFactorization(IdealFactory factory) {
		return this;
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

}
