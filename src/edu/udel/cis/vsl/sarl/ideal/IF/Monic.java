package edu.udel.cis.vsl.sarl.ideal.IF;

import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.ideal.common.NumericPrimitive;

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
	 * Returns the factors of this monic as a map from
	 * {@link edu.udel.cis.vsl.sarl.ideal.common.NumericPrimitive} to
	 * {@link PrimitivePower}. A key in the map is a primitive x and the value
	 * associated to x will be a primitive power x^i (x raised to the i-th
	 * power) for some positive integer i.
	 * 
	 * @return the factors of this monic as a map
	 */
	SymbolicMap<NumericPrimitive, PrimitivePower> monicFactors(
			IdealFactory factory);

	/**
	 * Is this the trivial monic, i.e., the monic consisting of 0 factors (and
	 * therefore equivalent to 1)?
	 * 
	 * @return true iff this monic is trivial
	 */
	boolean isTrivialMonic();

}
