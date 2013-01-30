package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A non-trivial monic factorization. It is a product of polynomial powers. The
 * number of factors in this product is at least 2.
 * 
 * @author siegel
 * 
 */
public class NTMonicFactorization extends CommonSymbolicExpression implements
		MonicFactorization {

	private Constant factorizationConstant = null;

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
	public SymbolicMap monicFactorizationMap(IdealFactory factory) {
		return (SymbolicMap) argument(0);
	}

}
