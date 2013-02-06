package edu.udel.cis.vsl.sarl.symbolic.monomial;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.concrete.ConcreteFactory;
import edu.udel.cis.vsl.sarl.symbolic.monic.MonicFactory;
import edu.udel.cis.vsl.sarl.symbolic.monic.MonicMonomial;
import edu.udel.cis.vsl.sarl.symbolic.power.PowerExpressionFactory;
import edu.udel.cis.vsl.sarl.type.SymbolicTypeFactory;

public class MonomialFactory {

	private MonicFactory monicFactory;

	private ConcreteFactory concreteFactory;

	private SymbolicTypeFactory typeFactory;

	private Monomial zeroIntMonomial, zeroRealMonomial, oneIntMonomial,
			oneRealMonomial;

	private NumericConcreteExpressionIF oneIntExpression, oneRealExpression;

	private Map<SymbolicExpressionKey<Monomial>, Monomial> map = new HashMap<SymbolicExpressionKey<Monomial>, Monomial>();

	public MonomialFactory(MonicFactory monicFactory,
			ConcreteFactory concreteFactory) {
		this.typeFactory = monicFactory.typeFactory();
		this.monicFactory = monicFactory;
		this.concreteFactory = concreteFactory;
		zeroIntMonomial = theMonomial(concreteFactory.zeroIntExpression(),
				monicFactory.emptyIntMonic());
		oneIntMonomial = theMonomial(concreteFactory.oneIntExpression(),
				monicFactory.emptyIntMonic());
		zeroRealMonomial = theMonomial(concreteFactory.zeroRealExpression(),
				monicFactory.emptyRealMonic());
		oneRealMonomial = theMonomial(concreteFactory.oneRealExpression(),
				monicFactory.emptyRealMonic());
		oneIntExpression = concreteFactory.oneIntExpression();
		oneRealExpression = concreteFactory.oneRealExpression();
	}

	public PowerExpressionFactory powerExpressionFactory() {
		return monicFactory.powerExpressionFactory();
	}

	public SymbolicTypeFactory typeFactory() {
		return typeFactory;
	}

	public ConcreteFactory concreteFactory() {
		return concreteFactory;
	}

	public MonicFactory monicFactory() {
		return monicFactory;
	}

	public Monomial zeroIntMonomial() {
		return zeroIntMonomial;
	}

	public Monomial zeroRealMonomial() {
		return zeroRealMonomial;
	}

	public Monomial oneIntMonomial() {
		return oneIntMonomial;
	}

	public Monomial oneRealMonomial() {
		return oneRealMonomial;
	}

	private Monomial theMonomial(NumericConcreteExpressionIF coefficient,
			MonicMonomial monic) {
		return CommonSymbolicExpression.flyweight(map, new Monomial(coefficient,
				monic));
	}

	public Monomial monomial(NumericConcreteExpressionIF constant,
			MonicMonomial monic) {
		Monomial result;

		assert constant != null;
		assert monic != null;
		assert constant.type().equals(monic.type());
		if (constant.isZero()) {
			result = (constant.isInteger() ? zeroIntMonomial : zeroRealMonomial);
		} else {
			result = theMonomial(constant, monic);
		}
		return result;
	}

	public Monomial monomial(MonicMonomial monic) {
		assert monic != null;
		if (monic.type().isInteger()) {
			return monomial(oneIntExpression, monic);
		} else {
			return monomial(oneRealExpression, monic);
		}
	}

	public Monomial monomial(NumericConcreteExpressionIF constant) {
		assert constant != null;
		if (constant.isZero()) {
			return (constant.isInteger() ? zeroIntMonomial : zeroRealMonomial);
		} else {
			return monomial(constant, (constant.isInteger() ? monicFactory
					.emptyIntMonic() : monicFactory.emptyRealMonic()));
		}
	}

	public Monomial negate(Monomial monomial) {
		return monomial(concreteFactory.negate(monomial.coefficient()),
				monomial.monicMonomial());
	}

	public Monomial multiply(Monomial monomial0, Monomial monomial1) {
		assert monomial0 != null;
		assert monomial1 != null;

		NumericConcreteExpressionIF constant = concreteFactory.multiply(
				monomial0.coefficient(), monomial1.coefficient());
		MonicMonomial monic = monicFactory.multiply(monomial0.monicMonomial(),
				monomial1.monicMonomial());

		return monomial(constant, monic);
	}

	public Monomial castToReal(Monomial monomial) {
		NumericConcreteExpressionIF realCoefficient = concreteFactory
				.castToReal(monomial.coefficient());
		MonicMonomial realMonic = monicFactory.castToReal(monomial
				.monicMonomial());

		return monomial(realCoefficient, realMonic);
	}

}
