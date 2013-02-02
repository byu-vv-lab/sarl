package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;

/**
 * A reduced polynomial is a polynomial satisfying: (1) if the type is real then
 * the leading coefficient of the polynomial is 1, and (2) if the type is
 * integer then the leading coefficient is positive and the GCD of the absolute
 * values of the coefficients is 1.
 * 
 * @author siegel
 * 
 */
public class ReducedPolynomial extends NumericPrimitive {

	public ReducedPolynomial(SymbolicTypeIF type, SymbolicMap termMap) {
		super(SymbolicOperator.ADD, type, termMap);
	}

}
