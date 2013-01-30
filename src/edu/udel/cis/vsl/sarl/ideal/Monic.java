package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.SymbolicMap;

/**
 * A Monic is a product of powers of primitive expressions
 * x_1^{i_1}*...*x_n^{i_n}, where the x_i are primitives and the i_j are
 * positive concrete ints.
 * 
 * @author siegel
 * 
 */
public interface Monic extends Monomial, MonicFactorization {

	/**
	 * Map from Primitive to PrimitivePower.
	 * 
	 * @return
	 */
	SymbolicMap monicFactors(IdealFactory factory);

}
