package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A non-trivial polynomial. It is the sum of monomials.
 * 
 * The set of monomials is represented as a map. A key in this map is a Monic.
 * The value associated to the Monic is a Monomial.
 * 
 * @author siegel
 * 
 */
public class NTPolynomial extends CommonSymbolicExpression implements
		Polynomial {

	protected NTPolynomial(SymbolicTypeIF type, SymbolicMap monomialMap) {
		super(SymbolicOperator.ADD, type, monomialMap);
	}

	public SymbolicMap monomialMap() {
		return (SymbolicMap) argument(0);
	}

}
