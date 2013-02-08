package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A numeric primitive expression. Other class may want to extend this.
 * Examples: symbolic constant, array read, tuple read, function application,
 * when those have numeric type.
 * 
 * Note: might want NumericSymbolicConstant extends NumericPrimitive, as opposed
 * to other kinds of symbolic constants.
 * 
 * @author siegel
 * 
 */
public class NumericPrimitive extends CommonSymbolicExpression implements
		PrimitivePower {

	/**
	 * Singleton map from this to this.
	 */
	private SymbolicMap monicFactors = null;

	public NumericPrimitive(SymbolicOperator operator, SymbolicTypeIF type,
			SymbolicObject[] arguments) {
		super(operator, type, arguments);
	}

	public NumericPrimitive(SymbolicOperator operator, SymbolicTypeIF type,
			SymbolicObject arg0) {
		super(operator, type, arg0);
	}

	public NumericPrimitive(SymbolicOperator operator, SymbolicTypeIF type,
			SymbolicObject arg0, SymbolicObject arg1) {
		super(operator, type, arg0, arg1);
	}

	public NumericPrimitive(SymbolicOperator operator, SymbolicTypeIF type,
			SymbolicObject arg0, SymbolicObject arg1, SymbolicObject arg2) {
		super(operator, type, arg0, arg1, arg2);
	}

	@Override
	public SymbolicMap monicFactors(IdealFactory factory) {
		if (monicFactors == null)
			monicFactors = factory.singletonMap(this, this);
		return monicFactors;
	}

	@Override
	public Constant monomialConstant(IdealFactory factory) {
		return factory.one(type());
	}

	@Override
	public Monic monic(IdealFactory factory) {
		return this;
	}

	@Override
	public SymbolicMap termMap(IdealFactory factory) {
		return monicFactors(factory);
	}

	@Override
	public NumericPrimitive primitive(IdealFactory factory) {
		return this;
	}

	@Override
	public IntObject primitivePowerExponent(IdealFactory factory) {
		return factory.oneIntObject();
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
	public boolean isTrivialMonic() {
		return false;
	}

	/**
	 * Note this might need to be overridden for certain kinds of numeric
	 * primitives, e.g., ReducedPolynomials.
	 */
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
		return false;
	}

}
