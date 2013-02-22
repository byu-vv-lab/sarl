package edu.udel.cis.vsl.sarl.expr.ideal;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

/**
 * A reduced polynomial is a polynomial satisfying: (1) if the type is real then
 * the leading coefficient of the polynomial is 1, (2) if the type is integer
 * then the leading coefficient is positive and the GCD of the absolute values
 * of the coefficients is 1, and (3) there is no known nontrivial factorization
 * of the polynomial.
 * 
 * A reduced polynomial is treated as a primitive, i.e., it may be used as a
 * "variable" in polynomial expressions. This is to express factorizations: a
 * factorization is a monomial in which the "variables" are reduced polynomials.
 * 
 * @author siegel
 * 
 */
public class ReducedPolynomial extends NumericPrimitive {

	public ReducedPolynomial(SymbolicType type,
			SymbolicMap<Monic, Monomial> termMap) {
		super(SymbolicOperator.ADD, type, termMap);
	}

	@SuppressWarnings("unchecked")
	public SymbolicMap<Monic, Monomial> termMap() {
		return (SymbolicMap<Monic, Monomial>) argument(0);
	}

	@Override
	public Polynomial expand(IdealFactory factory) {
		return factory.polynomial(termMap(), this);
	}

}
