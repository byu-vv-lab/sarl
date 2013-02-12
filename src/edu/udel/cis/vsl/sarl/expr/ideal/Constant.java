package edu.udel.cis.vsl.sarl.expr.ideal;

import edu.udel.cis.vsl.sarl.IF.number.NumberIF;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;

/**
 * A constant, i.e., a concrete number.
 * 
 * Implemented interfaces:
 * 
 * FactoredPolynomial
 * 
 * @author siegel
 * 
 */
public interface Constant extends Monomial {

	NumberObject value();

	NumberIF number();

}
