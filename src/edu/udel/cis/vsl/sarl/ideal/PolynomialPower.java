package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.IntObject;

/**
 * A polynomial power is a power of a polynomial, i.e., a polynomial raised to
 * some concrete positive integer power.
 * 
 * @author siegel
 * 
 */
public interface PolynomialPower extends MonicFactorization {

	IntObject polynomialPowerExponent(IdealFactory factory);

	Polynomial polynomialPowerBase(IdealFactory factory);

}
