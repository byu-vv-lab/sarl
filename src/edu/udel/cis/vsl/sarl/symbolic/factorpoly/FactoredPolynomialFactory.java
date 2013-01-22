package edu.udel.cis.vsl.sarl.symbolic.factorpoly;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.Exponentiator;
import edu.udel.cis.vsl.sarl.IF.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.Multiplier;
import edu.udel.cis.vsl.sarl.IF.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.IF.NumberIF;
import edu.udel.cis.vsl.sarl.IF.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.NumericPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.concrete.ConcreteFactory;
import edu.udel.cis.vsl.sarl.symbolic.factor.Factorization;
import edu.udel.cis.vsl.sarl.symbolic.factor.FactorizationFactory;
import edu.udel.cis.vsl.sarl.symbolic.integer.IntegerOperationFactory;
import edu.udel.cis.vsl.sarl.symbolic.monomial.Monomial;
import edu.udel.cis.vsl.sarl.symbolic.polynomial.Polynomial;
import edu.udel.cis.vsl.sarl.symbolic.polynomial.PolynomialFactory;

/**
 * A factored polynomial adds to a polnomial a factorization of that polynomial.
 * It simply wraps a polynomial object with one more piece of information. The
 * factorization is not considered part of the state and is not used in the
 * equals or hash code methods. The relationship between polynomials and
 * factored polynomials is 1-1.
 * 
 * If a factorization for a polynomial P is introduced that differs from a
 * previously existing factorization, a heuristic is used to select the "best"
 * one and the factorization is updated. This could lead to some
 * non-deterministic behavior in the transition system as the result of division
 * (for example) depends on the factorization. However, any two possible next
 * states are equivalent under the equivalence relation which identifies two
 * symbolic expressions that evaluate to the same concrete value for any
 * possible assignment of concrete values to symbolic constants.
 */
public class FactoredPolynomialFactory implements
		Multiplier<FactoredPolynomial> {

	private Map<Polynomial, FactoredPolynomial> map = new HashMap<Polynomial, FactoredPolynomial>();

	private FactorizationFactory factorizationFactory;

	private IntegerOperationFactory integerFactory;

	private PolynomialFactory polynomialFactory;

	private ConcreteFactory concreteFactory;

	private NumberFactoryIF numberFactory;

	private FactoredPolynomial zeroIntFactoredPolynomial,
			zeroRealFactoredPolynomial, oneIntFactoredPolynomial,
			oneRealFactoredPolynomial;

	private Exponentiator<FactoredPolynomial> intExponentiator,
			realExponentiator;

	public FactoredPolynomialFactory(FactorizationFactory factorizationFactory,
			IntegerOperationFactory integerFactory) {
		this.factorizationFactory = factorizationFactory;
		this.integerFactory = integerFactory;
		polynomialFactory = factorizationFactory.polynomialFactory();
		concreteFactory = factorizationFactory.concreteFactory();
		numberFactory = concreteFactory.numberFactory();

		zeroIntFactoredPolynomial = factoredPolynomial(polynomialFactory
				.zeroIntPolynomial(), factorizationFactory
				.zeroIntFactorization());
		zeroRealFactoredPolynomial = factoredPolynomial(polynomialFactory
				.zeroRealPolynomial(), factorizationFactory
				.zeroRealFactorization());
		oneIntFactoredPolynomial = factoredPolynomial(polynomialFactory
				.oneIntPolynomial(), factorizationFactory.oneIntFactorization());
		oneRealFactoredPolynomial = factoredPolynomial(polynomialFactory
				.oneRealPolynomial(), factorizationFactory
				.oneRealFactorization());
		intExponentiator = new Exponentiator<FactoredPolynomial>(this,
				oneIntFactoredPolynomial);
		realExponentiator = new Exponentiator<FactoredPolynomial>(this,
				oneRealFactoredPolynomial);
	}

	public FactoredPolynomial factoredPolynomial(Polynomial polynomial,
			Factorization factorization) {
		FactoredPolynomial fp = map.get(polynomial);

		if (fp == null) {
			fp = new FactoredPolynomial(polynomial, factorization);
			map.put(polynomial, fp);
		} else {
			Factorization oldFactorization = fp.factorization();

			if (!factorization.equals(oldFactorization)) {
				IntegerNumberIF granularity = factorizationFactory
						.granularity(factorization);
				IntegerNumberIF oldGranularity = factorizationFactory
						.granularity(oldFactorization);
				int compare = numberFactory
						.compare(granularity, oldGranularity);

				if (compare > 0) {
					fp.setFactorization(factorization);
				}
			}
		}
		return fp;
	}

	public FactoredPolynomial factoredPolynomial(Polynomial polynomial) {
		FactoredPolynomial fp = map.get(polynomial);

		if (fp == null) {
			fp = new FactoredPolynomial(polynomial, factorizationFactory
					.trivialFactorization(polynomial));
			map.put(polynomial, fp);
		}
		return fp;
	}

	public FactoredPolynomial factoredPolynomial(Factorization factorization) {
		return factoredPolynomial(factorizationFactory.expand(factorization),
				factorization);
	}

	public FactoredPolynomial factoredPolynomial(
			NumericConcreteExpressionIF constant) {
		return factoredPolynomial(factorizationFactory.factorization(constant));
	}

	public FactoredPolynomial factoredPolynomial(NumberIF number) {
		return factoredPolynomial(concreteFactory.concrete(number));
	}

	public FactoredPolynomial factoredPolynomial(NumericPrimitive primitive) {
		Polynomial polynomial = polynomialFactory.polynomial(primitive);

		return factoredPolynomial(polynomial);
	}

	public FactoredPolynomial factoredPolynomial(Monomial monomial) {
		Polynomial polynomial = polynomialFactory.polynomial(monomial);
		Factorization factorization = factorizationFactory
				.factorization(monomial);

		return factoredPolynomial(polynomial, factorization);
	}

	public FactoredPolynomial add(FactoredPolynomial f1, FactoredPolynomial f2) {
		Polynomial polynomial = polynomialFactory.add(f1.polynomial(), f2
				.polynomial());
		Factorization[] triple = factorizationFactory.extractCommonality(f1
				.factorization(), f2.factorization());

		if (triple[0].numFactors() == 0) {
			return factoredPolynomial(polynomial);
		} else {
			// factorization is product of factorization of sum and triple[0]
			Polynomial sum = polynomialFactory.add(factorizationFactory
					.expand(triple[1]), factorizationFactory.expand(triple[2]));
			Factorization factorization = factorizationFactory.multiply(
					triple[0], factoredPolynomial(sum).factorization());

			return factoredPolynomial(polynomial, factorization);
		}

	}

	public FactoredPolynomial multiply(FactoredPolynomial f1,
			FactoredPolynomial f2) {
		Polynomial polynomial = polynomialFactory.multiply(f1.polynomial(), f2
				.polynomial());
		Factorization factorization = factorizationFactory.multiply(f1
				.factorization(), f2.factorization());

		return factoredPolynomial(polynomial, factorization);
	}

	public FactoredPolynomial exp(FactoredPolynomial f1,
			IntegerNumberIF exponent) {
		return (f1.type().isInteger() ? intExponentiator.exp(f1, exponent)
				: realExponentiator.exp(f1, exponent));
	}

	public FactoredPolynomial negate(FactoredPolynomial f1) {
		return factoredPolynomial(polynomialFactory.negate(f1.polynomial()),
				factorizationFactory.negate(f1.factorization()));
	}

	public FactoredPolynomial subtract(FactoredPolynomial f1,
			FactoredPolynomial f2) {
		return add(f1, negate(f2));
	}

	public FactoredPolynomial zeroIntFactoredPolynomial() {
		return zeroIntFactoredPolynomial;
	}

	public FactoredPolynomial zeroRealFactoredPolynomial() {
		return zeroRealFactoredPolynomial;
	}

	public FactoredPolynomial oneIntFactoredPolynomial() {
		return oneIntFactoredPolynomial;
	}

	public FactoredPolynomial oneRealFactoredPolynomial() {
		return oneRealFactoredPolynomial;
	}

	public FactoredPolynomial castToReal(FactoredPolynomial f1) {
		Factorization factorization = f1.factorization();
		FactoredPolynomial result = factoredPolynomial(concreteFactory
				.castToReal(factorization.constant()));
		int numFactors = factorization.numFactors();

		for (int i = 0; i < numFactors; i++) {
			NumericConcreteExpressionIF exponent = factorization
					.multiplicity(i);
			Polynomial factor = factorization.factor(i);
			Polynomial realFactor = polynomialFactory.castToReal(factor);
			FactoredPolynomial fp = factoredPolynomial(realFactor);

			result = multiply(result, exp(fp, (IntegerNumberIF) exponent
					.value()));
		}
		return result;
	}

	public FactorizationFactory factorizationFactory() {
		return factorizationFactory;
	}

	public PolynomialFactory polynomialFactory() {
		return polynomialFactory;
	}

	public ConcreteFactory concreteFactory() {
		return concreteFactory;
	}

	/**
	 * Returns a sybmolic expression representing the "integer division" of the
	 * two given expressions.
	 * 
	 * Cancellation will be performed to the extent possible.
	 */
	public FactoredPolynomial intDivision(FactoredPolynomial numerator,
			FactoredPolynomial denominator) {
		assert numerator != null;
		assert numerator.type().isInteger();
		assert denominator != null;
		assert denominator.type().isInteger();
		assert !denominator.isZero();

		if (numerator.isZero())
			return zeroIntFactoredPolynomial;
		
		Factorization fact1 = numerator.factorization();
		Factorization fact2 = denominator.factorization();
		Factorization[] triple = factorizationFactory.extractCommonality(fact1,
				fact2);
		FactoredPolynomial newNumerator = factoredPolynomial(triple[1]);
		FactoredPolynomial newDenominator = factoredPolynomial(triple[2]);
		Factorization newNumeratorFact = newNumerator.factorization();
		Factorization newDenominatorFact = newDenominator.factorization();
		IntegerNumberIF numeratorConstant = (IntegerNumberIF) newNumeratorFact
				.constant().value();
		IntegerNumberIF denominatorConstant = (IntegerNumberIF) newDenominatorFact
				.constant().value();
		IntegerNumberIF gcd = numberFactory.gcd(numeratorConstant,
				denominatorConstant);

		numeratorConstant = numberFactory.divide(numeratorConstant, gcd);
		denominatorConstant = numberFactory.divide(denominatorConstant, gcd);
		if (denominatorConstant.signum() < 0) {
			numeratorConstant = numberFactory.negate(numeratorConstant);
			denominatorConstant = numberFactory.negate(denominatorConstant);
		}
		newNumerator = factoredPolynomial(factorizationFactory.withConstant(
				newNumeratorFact, concreteFactory.concrete(numeratorConstant)));
		newDenominator = factoredPolynomial(factorizationFactory.withConstant(
				newDenominatorFact, concreteFactory
						.concrete(denominatorConstant)));
		if (newDenominator.degree().signum() == 0) {
			if (newDenominator.isOne())
				return newNumerator;
			if (newNumerator.degree().signum() == 0)
				return factoredPolynomial(concreteFactory
						.concrete(numberFactory.divide(numeratorConstant,
								denominatorConstant)));
		}
		return factoredPolynomial(integerFactory.integerDivision(newNumerator,
				newDenominator));
	}

	/**
	 * (ad)%(bd) = (a%b)d
	 * 
	 * 
	 * */
	public FactoredPolynomial modulo(FactoredPolynomial numerator,
			FactoredPolynomial denominator) {
		assert numerator.type().isInteger();
		assert denominator.type().isInteger();
		if (denominator.isZero())
			throw new IllegalArgumentException("Denominator is zero");
		if (numerator.isZero())
			return zeroIntFactoredPolynomial;

		Factorization fact1 = numerator.factorization();
		Factorization fact2 = denominator.factorization();
		Factorization[] triple = factorizationFactory.extractCommonality(fact1,
				fact2);
		FactoredPolynomial newNumerator = factoredPolynomial(triple[1]);
		FactoredPolynomial newDenominator = factoredPolynomial(triple[2]);
		Factorization newNumeratorFact = newNumerator.factorization();
		Factorization newDenominatorFact = newDenominator.factorization();

		FactoredPolynomial common = factoredPolynomial(triple[0]);
		FactoredPolynomial modulus;

		if (newNumerator.degree().signum() <= 0
				&& newDenominator.degree().signum() <= 0) {
			assert newNumerator.degree().signum() == 0;
			assert newDenominator.degree().signum() == 0;

			NumericConcreteExpressionIF numeratorConstant = newNumeratorFact
					.constant();
			NumericConcreteExpressionIF denominatorConstant = newDenominatorFact
					.constant();

			modulus = factoredPolynomial(concreteFactory.mod(numeratorConstant,
					denominatorConstant));
		} else {
			modulus = factoredPolynomial(integerFactory.integerModulus(
					newNumerator, newDenominator));
		}
		return multiply(common, modulus);
	}

	/**
	 * Divides the factored polynomial by the constant and returns the resulting
	 * factored polynomial. If factored polynomial has real type, every
	 * coeffcient is divided by the constant. If it has integer type: the
	 * operation is only defined if the constant divides each coefficient.
	 * Otherwise an exception is thrown.
	 */
	public FactoredPolynomial divide(FactoredPolynomial fp,
			NumericConcreteExpressionIF constant) {
		Polynomial newPolynomial = polynomialFactory.divide(fp.polynomial(),
				constant);
		Factorization newFactorization = factorizationFactory.divide(fp
				.factorization(), constant);

		return factoredPolynomial(newPolynomial, newFactorization);
	}

}
