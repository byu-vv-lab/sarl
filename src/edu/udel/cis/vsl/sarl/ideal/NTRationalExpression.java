package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

/**
 * A nontrivial rational expression. It consists of a numerator and denominator,
 * both factored polynomials.
 * 
 * @author siegel
 * 
 */
public class NTRationalExpression extends CommonSymbolicExpression implements
		RationalExpression {

	protected NTRationalExpression(Polynomial numerator, Polynomial denominator) {
		super(SymbolicOperator.DIVIDE, numerator.type(), numerator, denominator);
		assert !denominator.isOne();
		assert !denominator.isZero();
		assert !numerator.isZero();
		assert !numerator.equals(denominator);
	}

	public Polynomial numerator(IdealFactory factory) {
		return (Polynomial) argument(0);
	}

	public Polynomial denominator(IdealFactory factory) {
		return (Polynomial) argument(1);
	}

	@Override
	public NumericExpression plus(IdealFactory factory, NumericExpression expr) {
		// TODO Auto-generated method stub
		// MOVE ALL TO factory.  just too complicated here.
		if (expr instanceof RationalExpression) {
			RationalExpression that = (RationalExpression) expr;
			Polynomial num1 = this.numerator(factory);
			Polynomial num2 = that.numerator(factory);
			Polynomial den1 = this.denominator(factory);
			Polynomial den2 = that.denominator(factory);
			Monomial fact1 = den1.factorization(factory);
			Monomial fact2 = den2.factorization(factory);
			Monomial[] triple = factory.extractCommonality(fact1, fact2);
			// [a,g1,g2]: f1=a*g1, f2=a*g2
			// n1/d1+n2/d2=n1/rd1+n2/rd2=(n1d2+n2d1)/rd1d2
			Polynomial common = triple[0].expand(factory);
			Polynomial d1 = triple[1].expand(factory);
			Polynomial d2 = triple[2].expand(factory);
			Polynomial denominator = (Polynomial) common.times(factory,
					d1.times(factory, d2));
			Polynomial numerator = (Polynomial) num1.times(factory, d2).plus(
					factory, num2.times(factory, d1));

			return factory.divide(numerator, denominator);
		}
		return null;
	}

	@Override
	public NumericExpression times(IdealFactory factory, NumericExpression expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NumericExpression negate(IdealFactory factory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NumericExpression invert(IdealFactory factory) {
		// TODO Auto-generated method stub
		return null;
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
