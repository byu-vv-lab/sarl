package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.NumberObject;
import edu.udel.cis.vsl.sarl.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.SymbolicTypeIF;
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

	private Monic emptyMonic = null;

	private SymbolicMap polynomialMap = null;

	private Factorization factorization = null;

	private Constant denominator = null;

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
		if (emptyMonic == null)
			emptyMonic = factory.emptyMonic(type());
		return emptyMonic;
	}

	@Override
	public Polynomial polynomial(IdealFactory factory) {
		return this;
	}

	@Override
	public Factorization factorization(IdealFactory factory) {
		if (factorization == null)
			factorization = factory.factorization(this,
					factory.emptyMonicFactorization(type()));
		return factorization;
	}

	@Override
	public FactoredPolynomial numerator(IdealFactory factory) {
		return this;
	}

	@Override
	public FactoredPolynomial denominator(IdealFactory factory) {
		if (denominator == null)
			denominator = factory.one(type());
		return denominator;
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
