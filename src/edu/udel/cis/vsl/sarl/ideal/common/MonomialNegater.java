package edu.udel.cis.vsl.sarl.ideal.common;

import edu.udel.cis.vsl.sarl.IF.UnaryOperator;
import edu.udel.cis.vsl.sarl.ideal.IF.Monomial;

class MonomialNegater implements UnaryOperator<Monomial> {
	private CommonIdealFactory factory;

	public MonomialNegater(CommonIdealFactory factory) {
		this.factory = factory;
	}

	@Override
	public Monomial apply(Monomial arg) {
		return factory.negate(arg);
	}
}