package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.NumberObject;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
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

	public boolean isZero() {
		return value().isZero();
	}

	public boolean isOne() {
		return value().isOne();
	}

	@Override
	public SymbolicMap polynomialMap(IdealFactory factory) {
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
	public NumericExpression add(IdealFactory factory, NumericExpression expr) {
		if (expr instanceof Constant) {
			Constant that = (Constant) expr;
			SymbolicTypeIF type = type();

			assert that.type().equals(type);
			return (Constant) factory.canonic(new Constant(type, factory
					.universe().numberObject(
							factory.numberFactory().add(value().getNumber(),
									that.value().getNumber()))));
		} else {
			return expr.add(factory, this);
		}
	}

}
