package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.SymbolicTypeIF;
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

	protected NTMonic(SymbolicTypeIF type, SymbolicMap factorMap) {
		super(SymbolicOperator.MULTIPLY, type, factorMap);
	}

	public SymbolicMap factorMap() {
		return (SymbolicMap) argument(0);
	}

}
