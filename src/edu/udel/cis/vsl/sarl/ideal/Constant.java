package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;

/**
 * A constant, i.e., a concrete number. Wraps a NumberObject, which wraps a
 * Number.
 * 
 * @author siegel
 * 
 */
public interface Constant extends Monomial {

	/**
	 * The NumberObject wrapped by this Constant.
	 * 
	 * @return the underlying NumberObject
	 */
	NumberObject value();

	/**
	 * Convenience method, equivalent to value().getNumber().
	 * 
	 * @return value().getNumber()
	 */
	Number number();

}
