package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.IntObject;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A non-trivial primitive power. It represents a Primitive expression raised to
 * some concrete integer exponent; the exponent is at least 2.
 * 
 * @author siegel
 * 
 */
public class NTPrimitivePower extends CommonSymbolicExpression implements
		PrimitivePower {

	protected NTPrimitivePower(NumericPrimitive primitive, IntObject exponent) {
		super(SymbolicOperator.POWER, primitive.type(), primitive, exponent);
	}

	public NumericPrimitive primitive() {
		return (NumericPrimitive) argument(0);
	}

	@Override
	public SymbolicMap monicFactors(IdealFactory factory) {
		return factory.singletonMap(this, this);
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
		return factory.singletonMap(this, this);
	}

	@Override
	public IntObject primitivePowerExponent(IdealFactory factory) {
		return (IntObject) argument(1);
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
	public IntObject polynomialPowerExponent(IdealFactory factory) {
		return primitivePowerExponent(factory);
	}

	@Override
	public Polynomial polynomialPowerBase(IdealFactory factory) {
		return primitive();
	}

	@Override
	public SymbolicMap monicFactorizationMap(IdealFactory factory) {
		return factory.singletonMap(primitive(), this);
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
	public Monomial leadingTerm() {
		return this;
	}

	@Override
	public NumericExpression add(IdealFactory factory, NumericExpression expr) {
		// TODO Auto-generated method stub
		if (expr instanceof Constant) { // X^i + C

		} else if (expr instanceof NumericPrimitive) { // X^i+Y or X^i+X

		} else if (expr instanceof PrimitivePower) { // X^i+Y^j or X^i+X^j
			
		}
		return expr.add(factory, this);
	}

	@Override
	public NumericPrimitive primitive(IdealFactory factory) {
		return (NumericPrimitive) argument(0);
	}

}
