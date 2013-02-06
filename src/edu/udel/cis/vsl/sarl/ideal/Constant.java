package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberIF;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A constant, i.e., a concrete number.
 * 
 * Implemented interfaces:
 * 
 * FactoredPolynomial
 * 
 * @author siegel
 * 
 */
public class Constant extends CommonSymbolicExpression implements Monomial {

	private SymbolicMap polynomialMap = null;

	protected Constant(SymbolicTypeIF type, NumberObject value) {
		super(SymbolicOperator.CONCRETE, type, value);
	}

	public NumberObject value() {
		return (NumberObject) argument(0);
	}

	public NumberIF number() {
		return value().getNumber();
	}

	public boolean isZero() {
		return value().isZero();
	}

	public boolean isOne() {
		return value().isOne();
	}

	@Override
	public SymbolicMap termMap(IdealFactory factory) {
		if (polynomialMap == null)
			polynomialMap = factory.singletonMap(factory.emptyMonic(type()),
					this);
		return polynomialMap;
	}

	@Override
	public Constant monomialConstant(IdealFactory factory) {
		return this;
	}

	@Override
	public Monic monic(IdealFactory factory) {
		return factory.emptyMonic(type());
	}

	@Override
	public Monomial factorization(IdealFactory factory) {
		return this;
	}

	@Override
	public Polynomial numerator(IdealFactory factory) {
		return this;
	}

	@Override
	public Polynomial denominator(IdealFactory factory) {
		return factory.one(type());
	}

	@Override
	public Monomial leadingTerm() {
		return this;
	}

	@Override
	public NumericExpression plus(IdealFactory factory, NumericExpression expr) {
		if (expr instanceof Constant) {
			Constant that = (Constant) expr;
			SymbolicTypeIF type = type();

			assert that.type().equals(type);
			return factory.constant(factory.objectFactory().numberObject(
					factory.numberFactory().add(value().getNumber(),
							that.value().getNumber())));
		} else {
			return expr.plus(factory, this);
		}
	}

	@Override
	public NumericExpression times(IdealFactory factory, NumericExpression expr) {
		if (expr instanceof Constant)
			return factory
					.constant(factory.numberFactory().multiply(
							value().getNumber(),
							((Constant) expr).value().getNumber()));
		else
			return expr.plus(factory, this);
	}

	@Override
	public Polynomial expand(IdealFactory factory) {
		return this;
	}

	@Override
	public NumericExpression negate(IdealFactory factory) {
		return factory.constant(factory.numberFactory().negate(
				value().getNumber()));
	}

	@Override
	public NumericExpression invert(IdealFactory factory) {
		return factory.constant(factory.numberFactory().divide(
				factory.numberFactory().oneRational(), value().getNumber()));
	}

	@Override
	public Polynomial intDivide(IdealFactory factory, Polynomial expr) {
		if (expr instanceof Constant)
			return factory.constant(factory.numberFactory().divide(
					(IntegerNumberIF) value().getNumber(),
					(IntegerNumberIF) ((Constant) expr).value().getNumber()));
		else
			return expr.intDivide(factory, this);
	}

	@Override
	public Polynomial modulo(IdealFactory factory, Polynomial expr) {
		if (expr instanceof Constant)
			return factory.constant(factory.numberFactory().mod(
					(IntegerNumberIF) value().getNumber(),
					(IntegerNumberIF) ((Constant) expr).value().getNumber()));
		else
			return expr.modulo(factory, this);
	}

}
