package edu.udel.cis.vsl.sarl.ideal.common;

import edu.udel.cis.vsl.sarl.IF.UnaryOperator;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Monomial;

class MonomialMultiplier implements UnaryOperator<Monomial> {
	private CommonIdealFactory factory;
	private Number scalar;
	private NumberFactory numberFactory;

	public MonomialMultiplier(CommonIdealFactory factory, Number scalar) {
		this.factory = factory;
		this.scalar = scalar;
		this.numberFactory = factory.numberFactory();
	}

	@Override
	public Monomial apply(Monomial arg) {
		return factory.monomial(
				factory.constant(numberFactory.multiply(
						arg.monomialConstant(factory).number(), scalar)),
				arg.monic(factory));
	}
}