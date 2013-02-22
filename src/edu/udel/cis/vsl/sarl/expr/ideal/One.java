package edu.udel.cis.vsl.sarl.expr.ideal;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

/**
 * Empty monic: equivalent to 1.
 * 
 * @author siegel
 * 
 */
public class One extends IdealExpression implements Constant, Monic {

	protected One(SymbolicType type, NumberObject oneObj) {
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
	public SymbolicMap<Monic, Monomial> termMap(IdealFactory factory) {
		return factory.singletonMap((Monic) this, (Monomial) this);
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
	public SymbolicMap<NumericPrimitive, PrimitivePower> monicFactors(
			IdealFactory factory) {
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

	// @Override
	// public boolean isZero() {
	// return false;
	// }

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
		return (NumberObject) argument(0);
	}

	@Override
	public Number number() {
		return value().getNumber();
	}

	@Override
	public int degree() {
		return 0;
	}

	@Override
	public IdealKind idealKind() {
		return IdealKind.Constant;
	}

}
