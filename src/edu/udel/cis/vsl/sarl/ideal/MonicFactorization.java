package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;

/**
 * A MonicFactorizations is a factorization of a polynomial with no constant
 * factor, i.e., just a product of polynomial powers, f1^i1*...*fn^in.
 * 
 * @author siegel
 * 
 */
public interface MonicFactorization extends Factorization {

	/**
	 * Map from ReducedPolynomial to ReducedPolynomialPower.
	 * 
	 * @return
	 */
	SymbolicMap monicFactorizationMap(IdealFactory factory);

}
