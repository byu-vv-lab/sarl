package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A non-trivial monic factorization is a product of at least 2 polynomial
 * powers.
 * 
 * @author siegel
 * 
 */
public class NTMonicFactorization extends CommonSymbolicExpression implements
		MonicFactorization {

	/**
	 * In the factors map, a key is a polynomial and the value associated to
	 * that key is a power of that polynomial.
	 * 
	 * The keys are instances of Polynomial.
	 * 
	 * The values are instances of MonicPolynomialPower.
	 * 
	 * @param type
	 *            integer or real type
	 * @param factors
	 *            map from Polynomial to PolynomialPower
	 */
	protected NTMonicFactorization(SymbolicTypeIF type, SymbolicMap factors) {
		super(SymbolicOperator.MULTIPLY, type, factors);
		assert factors.size() >= 2;
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
	public SymbolicMap monicFactorizationMap(IdealFactory factory) {
		return (SymbolicMap) argument(0);
	}

}
