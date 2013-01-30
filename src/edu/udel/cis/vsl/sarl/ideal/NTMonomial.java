package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A non-trivial monomial. It is the product of a constant and a monic.
 * 
 * @author siegel
 * 
 */
public class NTMonomial extends CommonSymbolicExpression implements Monomial {

	private SymbolicMap polynomialMap = null;

	private Constant denominator = null;

	private Factorization factorization = null;

	protected NTMonomial(Constant constant, Monic monic) {
		super(SymbolicOperator.MULTIPLY, constant.type(), constant, monic);
	}

	public Monic monic(IdealFactory factory) {
		return (Monic) argument(1);
	}

	@Override
	public SymbolicMap polynomialMap(IdealFactory factory) {
		if (polynomialMap == null)
			polynomialMap = factory.singletonMap((Monic) argument(1), this);
		return polynomialMap;
	}

	@Override
	public Constant monomialConstant(IdealFactory factory) {
		return (Constant) argument(0);
	}

	@Override
	public Polynomial polynomial(IdealFactory factory) {
		return this;
	}

	@Override
	public Factorization factorization(IdealFactory factory) {
		if (factorization == null) {
			SymbolicMap monicMap = monic(factory)
					.monicFactorizationMap(factory);
			MonicFactorization monicFactorization = factory.monicFactorization(
					type(), monicMap);

			factorization = factory.factorization(factory.one(type()),
					monicFactorization);
		}
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
		// TODO Auto-generated method stub
		return null;
	}

}
