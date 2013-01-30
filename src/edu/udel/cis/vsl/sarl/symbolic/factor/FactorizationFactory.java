package edu.udel.cis.vsl.sarl.symbolic.factor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.Exponentiator;
import edu.udel.cis.vsl.sarl.IF.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.Multiplier;
import edu.udel.cis.vsl.sarl.IF.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.IF.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.IF.RationalNumberIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.NumericPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.concrete.ConcreteFactory;
import edu.udel.cis.vsl.sarl.symbolic.monic.MonicMonomial;
import edu.udel.cis.vsl.sarl.symbolic.monomial.Monomial;
import edu.udel.cis.vsl.sarl.symbolic.monomial.MonomialFactory;
import edu.udel.cis.vsl.sarl.symbolic.polynomial.Polynomial;
import edu.udel.cis.vsl.sarl.symbolic.polynomial.PolynomialFactory;
import edu.udel.cis.vsl.sarl.symbolic.power.PowerExpression;
import edu.udel.cis.vsl.sarl.symbolic.power.PowerExpressionFactory;

public class FactorizationFactory implements Multiplier<Factorization> {

	private Map<SymbolicExpressionKey<Factorization>, Factorization> map = new HashMap<SymbolicExpressionKey<Factorization>, Factorization>();

	private PowerExpressionFactory powerExpressionFactory;

	private PolynomialFactory polynomialFactory;

	private MonomialFactory monomialFactory;

	private ConcreteFactory concreteFactory;

	private NumberFactoryIF numberFactory;

	private NumericConcreteExpressionIF oneIntExpression, oneRealExpression;

	private Factorization zeroIntFactorization, zeroRealFactorization,
			oneIntFactorization, oneRealFactorization;

	private Exponentiator<Factorization> intExponentiator, realExponentiator;

	public FactorizationFactory(PolynomialFactory polynomialFactory) {
		this.polynomialFactory = polynomialFactory;
		powerExpressionFactory = polynomialFactory.powerExpressionFactory();
		monomialFactory = polynomialFactory.monomialFactory();
		concreteFactory = polynomialFactory.concreteFactory();
		numberFactory = concreteFactory.numberFactory();
		oneIntExpression = concreteFactory.oneIntExpression();
		oneRealExpression = concreteFactory.oneRealExpression();
		zeroIntFactorization = factorization(concreteFactory
				.zeroIntExpression(), new PowerExpression[] {});
		zeroRealFactorization = factorization(concreteFactory
				.zeroRealExpression(), new PowerExpression[] {});
		oneIntFactorization = factorization(concreteFactory.oneIntExpression(),
				new PowerExpression[] {});
		oneRealFactorization = factorization(concreteFactory
				.oneRealExpression(), new PowerExpression[] {});
		intExponentiator = new Exponentiator<Factorization>(this,
				oneIntFactorization);
		realExponentiator = new Exponentiator<Factorization>(this,
				oneRealFactorization);
	}

	/**
	 * Returns the factorization c*f1^i1*...*fn^in. Each f is a polynomial and
	 * each i is a concrete integer.
	 * 
	 * Pre-requisite: the given factors must comply to the conditions described
	 * in the javadoc comment for this class. In particular, they must occur in
	 * increasing order by id. These facts are NOT checked by this method, so
	 * errors could result if the user does not guarantee this.
	 */
	Factorization factorization(NumericConcreteExpressionIF constant,
			PowerExpression[] factorPowers) {
		return CommonSymbolicExpression.flyweight(map, new Factorization(constant,
				factorPowers));
	}

	public Factorization factorization(NumericConcreteExpressionIF constant) {
		return factorization(constant, new PowerExpression[] {});
	}

	private Factorization factorization(NumericConcreteExpressionIF constant,
			PowerExpression factorPower) {
		return factorization(constant, new PowerExpression[] { factorPower });
	}

	/** A monomial can be factored in the obvious way. */
	public Factorization factorization(Monomial monomial) {
		SymbolicTypeIF type = monomial.type();
		MonicMonomial monic = monomial.monicMonomial();
		PowerExpression[] monicPowers = monic.factorPowers();
		int numFactorPowers = monicPowers.length;
		Factorization result = factorization(monomial.coefficient());
		NumericConcreteExpressionIF one = (type.isInteger() ? oneIntExpression
				: oneRealExpression);

		for (int i = 0; i < numFactorPowers; i++) {
			PowerExpression monicPower = monicPowers[i];
			NumericPrimitive primitive = (NumericPrimitive) monicPower.polynomialPowerBase();
			Polynomial polynomialFactor = polynomialFactory
					.polynomial(primitive);
			PowerExpression polynomialPower = powerExpressionFactory
					.powerExpression(polynomialFactor, monicPower.exponent());

			result = multiply(result, factorization(one, polynomialPower));
		}
		return result;
	}

	public Factorization multiply(Factorization f1, Factorization f2) {
		NumericConcreteExpressionIF newConstant = concreteFactory.multiply(f1
				.constant(), f2.constant());

		if (newConstant.isZero()) {
			return (newConstant.type().isInteger() ? zeroIntFactorization
					: zeroRealFactorization);
		}

		int numFactors1 = f1.numFactors();
		int numFactors2 = f2.numFactors();
		int index1 = 0, index2 = 0;
		LinkedList<PowerExpression> newFactorPowers = new LinkedList<PowerExpression>();

		while (index1 < numFactors1 && index2 < numFactors2) {
			PowerExpression fp1 = f1.factorPower(index1);
			PowerExpression fp2 = f2.factorPower(index2);
			Polynomial p1 = (Polynomial) fp1.polynomialPowerBase();
			Polynomial p2 = (Polynomial) fp2.polynomialPowerBase();
			int compare = CommonSymbolicExpression.compare(p1, p2);

			if (compare == 0) {
				newFactorPowers.add(powerExpressionFactory.powerExpression(p1,
						concreteFactory.addRational(fp1.exponent(), fp2.exponent())));
				index1++;
				index2++;
			} else if (compare > 0) {
				newFactorPowers.add(fp2);
				index2++;
			} else {
				newFactorPowers.add(fp1);
				index1++;
			}
		}
		while (index1 < numFactors1) {
			newFactorPowers.add(f1.factorPower(index1));
			index1++;
		}
		while (index2 < numFactors2) {
			newFactorPowers.add(f2.factorPower(index2));
			index2++;
		}
		return factorization(newConstant, (PowerExpression[]) newFactorPowers
				.toArray(new PowerExpression[newFactorPowers.size()]));
	}

	public Factorization exp(Factorization factorization,
			IntegerNumberIF exponent) {
		return (factorization.type().isInteger() ? intExponentiator.exp(
				factorization, exponent) : realExponentiator.exp(factorization,
				exponent));
	}

	public Polynomial expand(Factorization factorization) {
		// cache these? could add field to factorization object
		int numFactors = factorization.numFactors();
		Polynomial result = polynomialFactory.polynomial(factorization
				.constant());

		for (int i = 0; i < numFactors; i++) {
			result = polynomialFactory.multiply(result, polynomialFactory.exp(
					factorization.factor(i), (IntegerNumberIF) factorization
							.exponent(i).value()));

		}
		return result;
	}

	public Factorization trivialFactorization(Polynomial polynomial) {
		SymbolicTypeIF type = polynomial.type();
		Monomial[] terms;
		int numTerms;

		if (polynomial.isZero())
			return (type.isInteger() ? zeroIntFactorization
					: zeroRealFactorization);
		terms = polynomial.terms();
		numTerms = terms.length;
		assert numTerms >= 1;
		if (type.isInteger()) {
			IntegerNumberIF leadNumber = (IntegerNumberIF) terms[0]
					.coefficient().value();
			IntegerNumberIF gcd = leadNumber;

			for (int i = 1; i < numTerms; i++)
				gcd = numberFactory.gcd(gcd, (IntegerNumberIF) terms[i]
						.coefficient().value());
			if (gcd.isOne() && leadNumber.signum() > 0) {
				return factorization(concreteFactory.oneIntExpression(),
						powerExpressionFactory.powerExpression(polynomial,
								oneIntExpression));
			} else {
				Monomial[] newTerms = new Monomial[numTerms];
				Polynomial factor;

				if (gcd.signum() != leadNumber.signum()) {
					gcd = numberFactory.negate(gcd);
				}
				for (int i = 0; i < numTerms; i++) {
					IntegerNumberIF newNumber = numberFactory.divide(
							(IntegerNumberIF) terms[i].coefficient().value(),
							gcd);

					newTerms[i] = monomialFactory.monomial(concreteFactory
							.concrete(newNumber), terms[i].monicMonomial());
				}
				factor = polynomialFactory.polynomial(type, newTerms);
				if (factor.isOne()) {
					return factorization(concreteFactory.concrete(gcd));
				} else {
					assert factor.degree().signum() > 0;
					return factorization(concreteFactory.concrete(gcd),
							powerExpressionFactory.powerExpression(factor,
									oneIntExpression));
				}
			}
		} else {
			RationalNumberIF leadNumber = (RationalNumberIF) terms[0]
					.coefficient().value();

			assert leadNumber.signum() != 0;

			if (leadNumber.isOne()) {
				return factorization(concreteFactory.oneRealExpression(),
						powerExpressionFactory.powerExpression(polynomial,
								oneIntExpression));
			} else {
				Monomial[] newTerms = new Monomial[numTerms];
				Polynomial factor;

				for (int i = 0; i < numTerms; i++) {
					RationalNumberIF newNumber = numberFactory.divide(
							(RationalNumberIF) terms[i].coefficient().value(),
							leadNumber);

					newTerms[i] = monomialFactory.monomial(concreteFactory
							.concrete(newNumber), terms[i].monicMonomial());
				}
				factor = polynomialFactory.polynomial(type, newTerms);
				if (factor.isOne()) {
					return factorization(concreteFactory.concrete(leadNumber));
				} else {
					assert factor.degree().signum() > 0;
					return factorization(concreteFactory.concrete(leadNumber),
							powerExpressionFactory.powerExpression(factor,
									oneIntExpression));
				}
			}
		}
	}

	public Factorization negate(Factorization factorization) {
		return factorization(concreteFactory.negate(factorization.constant()),
				factorization.factorPowers());
	}

	public PolynomialFactory polynomialFactory() {
		return polynomialFactory;
	}

	public MonomialFactory monomialFactory() {
		return monomialFactory;
	}

	public ConcreteFactory concreteFactory() {
		return concreteFactory;
	}

	public NumberFactoryIF numberFactory() {
		return numberFactory;
	}

	public Factorization zeroIntFactorization() {
		return zeroIntFactorization;
	}

	public Factorization zeroRealFactorization() {
		return zeroRealFactorization;
	}

	public Factorization oneIntFactorization() {
		return oneIntFactorization;
	}

	public Factorization oneRealFactorization() {
		return oneRealFactorization;
	}

	/**
	 * Given two factorizations f1 and f2, this returns an array of length 3
	 * containing 3 factorizations a, g1, g2 (in that order), satisfying
	 * f1=a*g1, f2=a*g2, g1 and g2 have no factors in common, a is a monic
	 * factorization (its constant is 1).
	 */
	public Factorization[] extractCommonality(Factorization fact1,
			Factorization fact2) {
		Factorization[] triple = new Factorization[3];
		LinkedList<PowerExpression> commonFactors = new LinkedList<PowerExpression>();
		LinkedList<PowerExpression> newFactors1 = new LinkedList<PowerExpression>();
		LinkedList<PowerExpression> newFactors2 = new LinkedList<PowerExpression>();
		int n1 = fact1.numFactors();
		int n2 = fact2.numFactors();
		int index1 = 0, index2 = 0;

		while (index1 < n1 && index2 < n2) {
			PowerExpression fp1 = fact1.factorPower(index1);
			PowerExpression fp2 = fact2.factorPower(index2);
			Polynomial factor1 = (Polynomial) fp1.polynomialPowerBase();
			Polynomial factor2 = (Polynomial) fp2.polynomialPowerBase();
			int compare = CommonSymbolicExpression.compare(factor1, factor2);

			if (compare == 0) {
				NumericConcreteExpressionIF exponent1 = fp1.exponent();
				NumericConcreteExpressionIF exponent2 = fp2.exponent();
				NumericConcreteExpressionIF difference = concreteFactory
						.subtract(exponent1, exponent2);

				if (difference.signum() <= 0) {
					commonFactors.add(powerExpressionFactory.powerExpression(
							factor1, exponent1));
					if (difference.signum() != 0) {
						newFactors2.add(powerExpressionFactory.powerExpression(
								factor2, concreteFactory.negate(difference)));
					}
				} else {
					commonFactors.add(powerExpressionFactory.powerExpression(
							factor1, exponent2));
					newFactors1.add(powerExpressionFactory.powerExpression(
							factor1, difference));
				}
				index1++;
				index2++;
			} else if (compare > 0) {
				// factor1 has greater id, so factor2 comes first
				newFactors2.add(fp2);
				index2++;
			} else {
				newFactors1.add(fp1);
				index1++;
			}
		}
		while (index1 < n1) {
			newFactors1.add(fact1.factorPower(index1));
			index1++;
		}
		while (index2 < n2) {
			newFactors2.add(fact2.factorPower(index2));
			index2++;
		}

		triple[0] = factorization((fact1.type().isInteger() ? oneIntExpression
				: oneRealExpression), commonFactors
				.toArray(new PowerExpression[commonFactors.size()]));
		triple[1] = factorization(fact1.constant(), newFactors1
				.toArray(new PowerExpression[newFactors1.size()]));
		triple[2] = factorization(fact2.constant(), newFactors2
				.toArray(new PowerExpression[newFactors2.size()]));
		return triple;
	}

	public Factorization withConstant(Factorization factorization,
			NumericConcreteExpressionIF newConstant) {
		return factorization(newConstant, factorization.factorPowers());
	}

	/**
	 * Returns the factorization which is the result of dividing the given
	 * factorization by the concrete value. The types of the arguments must be
	 * the same. For integer type, this is only defined if the given constant
	 * divides the constant of the given factorization.
	 */
	public Factorization divide(Factorization factorization,
			NumericConcreteExpressionIF constant) {
		NumericConcreteExpressionIF oldConstant = factorization.constant();
		SymbolicTypeIF type = factorization.type();
		NumericConcreteExpressionIF newConstant;

		assert !type.isInteger()
				|| concreteFactory.mod(oldConstant,
						concreteFactory.abs(constant)).isZero();
		newConstant = concreteFactory.divide(oldConstant, constant);
		return factorization(newConstant, factorization.factorPowers());
	}

	/**
	 * Returns the sum of the exponents of the factor powers in the
	 * factorization. This gives an estimate on how fine the factorization is. A
	 * factorization with a higher granularity is likely (but not guaranteed) to
	 * be a better factorization.
	 */
	public IntegerNumberIF granularity(Factorization factorization) {
		IntegerNumberIF result = numberFactory.zeroInteger();

		for (PowerExpression power : factorization.factorPowers()) {
			result = numberFactory.add(result, (IntegerNumberIF) power
					.exponent().value());
		}
		return result;
	}

}
