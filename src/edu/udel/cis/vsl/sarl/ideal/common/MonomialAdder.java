package edu.udel.cis.vsl.sarl.ideal.common;

import edu.udel.cis.vsl.sarl.IF.BinaryOperator;
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.Monomial;

/**
 * Add c0*m + c1*m, where m is a monic and c0 and c1 are constants. The answer
 * is (c0+c1)*m, or null if c0+c1=0.
 * 
 * @author siegel
 * 
 */
class MonomialAdder implements BinaryOperator<Monomial> {
	private CommonIdealFactory factory;

	public MonomialAdder(CommonIdealFactory factory) {
		this.factory = factory;
	}

	@Override
	public Monomial apply(Monomial arg0, Monomial arg1) {
		Constant c = factory.add(arg0.monomialConstant(factory),
				arg1.monomialConstant(factory));

		if (c.isZero())
			return null;
		return factory.monomial(c, arg0.monic(factory));
	}
}