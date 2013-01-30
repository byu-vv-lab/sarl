package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.IntObject;
import edu.udel.cis.vsl.sarl.IF.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
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
	public SymbolicMap polynomialMap(IdealFactory factory) {
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
	public Polynomial polynomial(IdealFactory factory) {
		return this;
	}

	@Override
	public Factorization factorization(IdealFactory factory) {
		return this;
	}

	@Override
	public FactoredPolynomial numerator(IdealFactory factory) {
		return this;
	}

	@Override
	public FactoredPolynomial denominator(IdealFactory factory) {
		return factory.one(type());
	}

	@Override
	public SymbolicMap monicFactorizationMap(IdealFactory factory) {
		return monicFactors(factory);
	}

	@Override
	public Constant factorizationConstant(IdealFactory factory) {
		return factory.one(type());
	}

	@Override
	public MonicFactorization monicFactorization(IdealFactory factory) {
		return this;
	}

	@Override
	public IntObject polynomialPowerExponent(IdealFactory factory) {
		return factory.oneIntObject();
	}

	@Override
	public Polynomial polynomialPowerBase(IdealFactory factory) {
		return this;
	}

	@Override
	public Monomial leadingTerm() {
		return this;
	}

	@Override
	public NumericExpression add(IdealFactory factory, NumericExpression expr) {
		if (expr instanceof Constant) { // X+C: polynomial
			Constant that = (Constant) expr;

			if (that.isZero())
				return this;
			else
				return factory.polynomial(
						type(),
						polynomialMap(factory).put(factory.emptyIntMonic(),
								that));
		} else if (expr instanceof NumericPrimitive) {
			NumericPrimitive that = (NumericPrimitive) expr;

			if (this.equals(that)) // 2X
				return factory.monomial(factory.two(type()), this);
			else
				// X+Y
				return factory.polynomial(type(),
						polynomialMap(factory).put(that, that));
		} else {
			return expr.add(factory, this);
		}
	}

}
