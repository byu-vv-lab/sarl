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

	private SymbolicMap polynomialMap = null;

	protected NTMonic(SymbolicTypeIF type, SymbolicMap factorMap) {
		super(SymbolicOperator.MULTIPLY, type, factorMap);
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
	public SymbolicMap monicFactorizationMap(IdealFactory factory) {
		return (SymbolicMap) argument(0);
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
	public Monomial leadingTerm() {
		return this;
	}

	@Override
	public NumericExpression add(IdealFactory factory, NumericExpression expr) {
		// TODO Auto-generated method stub
		return null;
	}

}
