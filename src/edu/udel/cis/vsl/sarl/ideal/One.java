package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.number.NumberIF;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * Empty monic: equivalent to 1.
 * 
 * @author siegel
 * 
 */
public class One extends CommonSymbolicExpression implements Constant, Monic {

	protected One(SymbolicTypeIF type, NumberObject oneObj) {
		super(SymbolicOperator.CONCRETE, type, oneObj);
		assert oneObj.isOne();
	}

	@Override
	public Constant monomialConstant(IdealFactory factory) {
		return this;
	}

	@Override
	public Monic monic(IdealFactory factory) {
		return this;
	}

	@Override
	public SymbolicMap termMap(IdealFactory factory) {
		return factory.singletonMap(this, this);
	}

	@Override
	public Monomial leadingTerm() {
		return this;
	}

	@Override
	public Polynomial numerator(IdealFactory factory) {
		return this;
	}

	@Override
	public Polynomial denominator(IdealFactory factory) {
		return this;
	}

	@Override
	public SymbolicMap monicFactors(IdealFactory factory) {
		return factory.emptyMap();
	}

	@Override
	public boolean isTrivialMonic() {
		return true;
	}

	@Override
	public Monomial factorization(IdealFactory factory) {
		return this;
	}

	@Override
	public Polynomial expand(IdealFactory factory) {
		return this;
	}

	@Override
	public boolean isZero() {
		return false;
	}

	@Override
	public boolean isOne() {
		return true;
	}

	@Override
	public String toString() {
		return "1";
	}

	@Override
	public NumberObject value() {
		return (NumberObject) argument(1);
	}

	@Override
	public NumberIF number() {
		return value().getNumber();
	}
}
