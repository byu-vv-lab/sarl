package edu.udel.cis.vsl.sarl.symbolic.rational;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.number.IF.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.number.IF.NumberIF;
import edu.udel.cis.vsl.sarl.number.IF.RationalNumberIF;
import edu.udel.cis.vsl.sarl.symbolic.NumericPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.concrete.ConcreteFactory;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpressionKey;
import edu.udel.cis.vsl.sarl.symbolic.factor.Factorization;
import edu.udel.cis.vsl.sarl.symbolic.factor.FactorizationFactory;
import edu.udel.cis.vsl.sarl.symbolic.factorpoly.FactoredPolynomial;
import edu.udel.cis.vsl.sarl.symbolic.factorpoly.FactoredPolynomialFactory;

/**
 * A factory used to produce and perform basic manipulations on
 * RationalExpressions.
 * 
 * @author siegel
 * 
 */
public class RationalFactory {

	private Map<SymbolicExpressionKey<RationalExpression>, RationalExpression> map = new HashMap<SymbolicExpressionKey<RationalExpression>, RationalExpression>();

	private FactoredPolynomialFactory fpFactory;

	private FactorizationFactory factorizationFactory;

	private ConcreteFactory concreteFactory;

	private NumberFactoryIF numberFactory;

	private NumericConcreteExpressionIF one;

	private RationalExpression zeroRational, oneRational;

	private FactoredPolynomial oneFp;

	public RationalFactory(FactoredPolynomialFactory fpFactory) {
		this.fpFactory = fpFactory;
		factorizationFactory = fpFactory.factorizationFactory();
		concreteFactory = factorizationFactory.concreteFactory();
		numberFactory = concreteFactory.numberFactory();
		oneFp = fpFactory.oneRealFactoredPolynomial();
		one = concreteFactory.oneRealExpression();
		oneRational = rational(oneFp, oneFp);
		zeroRational = rational(fpFactory.zeroRealFactoredPolynomial(), oneFp);
	}

	/**
	 * Numerator and denominator must be real. Denominator will be made monic by
	 * factoring out constant putting in numerator.
	 * */
	public RationalExpression rational(FactoredPolynomial numerator,
			FactoredPolynomial denominator) {
		Factorization fact1 = numerator.factorization();
		Factorization fact2 = denominator.factorization();
		Factorization[] triple = factorizationFactory.extractCommonality(fact1,
				fact2);
		NumericConcreteExpressionIF constant = triple[2].constant();
		Factorization denominatorFactorization = factorizationFactory
				.withConstant(triple[2], one);
		Factorization numeratorFactorization = factorizationFactory
				.withConstant(triple[1],
						concreteFactory.divide(triple[1].constant(), constant));
		FactoredPolynomial newNumerator = fpFactory
				.factoredPolynomial(numeratorFactorization);
		FactoredPolynomial newDenominator = fpFactory
				.factoredPolynomial(denominatorFactorization);

		return SymbolicExpression.flyweight(map, new RationalExpression(
				newNumerator, newDenominator));
	}

	public RationalExpression rational(NumericPrimitive primitive) {
		assert primitive.type().isReal();
		return rational(fpFactory.factoredPolynomial(primitive),
				fpFactory.oneRealFactoredPolynomial());
	}

	public RationalExpression rational(NumericConcreteExpressionIF concrete) {
		assert concrete.type().isReal();
		return rational(fpFactory.factoredPolynomial(concrete),
				fpFactory.oneRealFactoredPolynomial());
	}

	public RationalExpression rational(RationalNumberIF number) {
		return rational(concreteFactory.concrete(number));
	}

	public RationalExpression rational(NumberIF number) {
		return rational(numberFactory.rational(number));
	}

	public RationalExpression rational(FactoredPolynomial fp) {
		return rational(fp, oneFp);
	}

	public RationalExpression add(RationalExpression arg0,
			RationalExpression arg1) {
		return rational(
				fpFactory.add(fpFactory.multiply(arg0.numerator(),
						arg1.denominator()), fpFactory.multiply(
						arg0.denominator(), arg1.numerator())),
				fpFactory.multiply(arg0.denominator(), arg1.denominator()));
	}

	public RationalExpression multiply(RationalExpression arg0,
			RationalExpression arg1) {
		return rational(fpFactory.multiply(arg0.numerator(), arg1.numerator()),
				fpFactory.multiply(arg0.denominator(), arg1.denominator()));
	}

	public RationalExpression negate(RationalExpression arg0) {
		return rational(fpFactory.negate(arg0.numerator()), arg0.denominator());
	}

	public RationalExpression divide(RationalExpression arg0,
			RationalExpression arg1) {
		return rational(
				fpFactory.multiply(arg0.numerator(), arg1.denominator()),
				fpFactory.multiply(arg0.denominator(), arg1.numerator()));
	}

	public RationalExpression subtract(RationalExpression arg0,
			RationalExpression arg1) {
		return add(arg0, negate(arg1));
	}

	public RationalExpression zero() {
		return zeroRational;
	}

	public RationalExpression one() {
		return oneRational;
	}
}
