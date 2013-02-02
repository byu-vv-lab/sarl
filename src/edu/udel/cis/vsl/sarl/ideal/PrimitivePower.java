package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.IntObject;

/**
 * A power of a Primitive expression, x^i, where x is a Primitive and i is a
 * concrete nonnegative int.
 * 
 * @author siegel
 * 
 */
public interface PrimitivePower extends Monic {

	NumericPrimitive primitive(IdealFactory factory);

	IntObject primitivePowerExponent(IdealFactory factory);

}
