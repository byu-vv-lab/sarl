package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.IntObject;

/**
 * A reduced polynomial power is a positive power of a reduced polynomial, p^n,
 * where n>=1.
 * 
 * @author siegel
 * 
 */
public interface ReducedPolynomialPower extends MonicFactorization {

	/**
	 * Returns the (positive) exponent in the power expression.
	 * 
	 * @param factory
	 *            the IdealFactory responsible for this expression
	 * @return the exponent
	 */
	IntObject polynomialPowerExponent(IdealFactory factory);

	/**
	 * Returns the reduced polynomial base in this power expression.
	 * 
	 * @param factory
	 *            the IdealFactory responsible for this expression
	 * 
	 * @return the base
	 */
	ReducedPolynomial polynomialPowerBase(IdealFactory factory);

}
