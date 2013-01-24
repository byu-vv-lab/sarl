package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A non-trivial monic factorization. It is a product of monic polynomial
 * powers. The number of factors in this product is at least 2.
 * 
 * @author siegel
 * 
 */
public class NTMonicFactorization extends CommonSymbolicExpression implements
		MonicFactorization {

	/**
	 * In the factors map, a key is a monic polynomial and the value associated
	 * to that key is a power of that polynomial.
	 * 
	 * The keys are instances of Polynomial, but happen to have leading
	 * coefficient 1.
	 * 
	 * The values are instances of MonicPolynomialPower.
	 * 
	 * @param type
	 * @param factors
	 */
	protected NTMonicFactorization(SymbolicTypeIF type, SymbolicMap factors) {
		super(SymbolicOperator.MULTIPLY, type, factors);
	}

	public SymbolicMap factors() {
		return (SymbolicMap) argument(0);
	}

}
