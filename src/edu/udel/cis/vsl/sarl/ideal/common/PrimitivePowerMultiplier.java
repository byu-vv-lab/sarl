package edu.udel.cis.vsl.sarl.ideal.common;

import edu.udel.cis.vsl.sarl.IF.BinaryOperator;
import edu.udel.cis.vsl.sarl.ideal.IF.PrimitivePower;

/**
 * Multiply p^i*p^j, where p is a NumericPrimitive and i and j are positive
 * IntObjects. The answer is p^{i+j}.
 * 
 * @author siegel
 * 
 */
class PrimitivePowerMultiplier implements BinaryOperator<PrimitivePower> {
	private CommonIdealFactory factory;

	public PrimitivePowerMultiplier(CommonIdealFactory factory) {
		this.factory = factory;
	}

	@Override
	public PrimitivePower apply(PrimitivePower arg0, PrimitivePower arg1) {
		return factory.primitivePower(
				arg0.primitive(factory),
				arg0.primitivePowerExponent(factory).plus(
						arg1.primitivePowerExponent(factory)));
	}
}