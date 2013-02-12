package edu.udel.cis.vsl.sarl.expr.ideal;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;

/**
 * A Monic is a product of powers of primitive expressions
 * x_1^{i_1}*...*x_n^{i_n}, where the x_i are primitives and the i_j are
 * positive concrete ints.
 * 
 * @author siegel
 * 
 */
public interface Monic extends Monomial {

	/**
	 * Map from Primitive to PrimitivePower.
	 * 
	 * @return
	 */
	SymbolicMap monicFactors(IdealFactory factory);

	/**
	 * Is this the trivial monic, i.e., the monic consisting of 0 factors (and
	 * therefore equivalent to 1)?
	 * 
	 * @return true iff this monic is trivial
	 */
	boolean isTrivialMonic();

}
