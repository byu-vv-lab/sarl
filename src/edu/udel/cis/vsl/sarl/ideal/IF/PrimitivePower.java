package edu.udel.cis.vsl.sarl.ideal.IF;

import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.ideal.common.NumericPrimitive;

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
