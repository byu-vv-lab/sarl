package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.number.NumberIF;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A constant which is not 1.
 * 
 * @author siegel
 * 
 */
public class NTConstant extends CommonSymbolicExpression implements Constant {

	protected NTConstant(SymbolicTypeIF type, NumberObject value) {
		super(SymbolicOperator.CONCRETE, type, value);
		assert !value.isOne();
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
		return false;
	}

	@Override
	public SymbolicMap termMap(IdealFactory factory) {
		return factory.singletonMap(factory.one(type()), this);
	}

	@Override
	public Constant monomialConstant(IdealFactory factory) {
		return this;
	}

	@Override
	public Monic monic(IdealFactory factory) {
		return factory.one(type());
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
	public Polynomial expand(IdealFactory factory) {
		return this;
	}

	@Override
	public String toString() {
		return number().toString();
	}

}
