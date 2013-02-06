package edu.udel.cis.vsl.sarl.symbolic.polynomial;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import edu.udel.cis.vsl.sarl.IF.Multiplier;
import edu.udel.cis.vsl.sarl.IF.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.IF.number.Exponentiator;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.NumericPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.concrete.ConcreteFactory;
import edu.udel.cis.vsl.sarl.symbolic.monic.MonicFactory;
import edu.udel.cis.vsl.sarl.symbolic.monic.MonicMonomial;
import edu.udel.cis.vsl.sarl.symbolic.monomial.Monomial;
import edu.udel.cis.vsl.sarl.symbolic.monomial.MonomialFactory;
import edu.udel.cis.vsl.sarl.symbolic.power.PowerExpressionFactory;
import edu.udel.cis.vsl.sarl.type.SymbolicTypeFactory;

public class PolynomialFactory implements Multiplier<Polynomial> {

	private Map<SymbolicExpressionKey<Polynomial>, Polynomial> map = new HashMap<SymbolicExpressionKey<Polynomial>, Polynomial>();

	private ConcreteFactory concreteFactory;

	private MonicFactory monicFactory;

	private MonomialFactory monomialFactory;

	private SymbolicTypeFactory typeFactory;

	private NumericConcreteExpressionIF oneIntExpression, oneRealExpression;

	private Exponentiator<Polynomial> intExponentiator, realExponentiator;

	private Polynomial zeroIntPolynomial, zeroRealPolynomial, oneIntPolynomial,
			oneRealPolynomial;

	public PolynomialFactory(MonomialFactory monomialFactory) {
		this.monomialFactory = monomialFactory;
		monicFactory = monomialFactory.monicFactory();
		concreteFactory = monomialFactory.concreteFactory();
		typeFactory = monomialFactory.typeFactory();
		oneIntExpression = concreteFactory.oneIntExpression();
		oneRealExpression = concreteFactory.oneRealExpression();
		zeroIntPolynomial = polynomial(typeFactory.integerType(),
				new Monomial[] {});
		zeroRealPolynomial = polynomial(typeFactory.realType(),
				new Monomial[] {});
		oneIntPolynomial = polynomial(typeFactory.integerType(),
				new Monomial[] { monomialFactory.oneIntMonomial() });
		oneRealPolynomial = polynomial(typeFactory.realType(),
				new Monomial[] { monomialFactory.oneRealMonomial() });
		intExponentiator = new Exponentiator<Polynomial>(this, oneIntPolynomial);
		realExponentiator = new Exponentiator<Polynomial>(this,
				oneRealPolynomial);
	}

	public PowerExpressionFactory powerExpressionFactory() {
		return monomialFactory.powerExpressionFactory();
	}

	public Polynomial polynomial(SymbolicTypeIF type, Monomial[] terms) {
		return CommonSymbolicExpression.flyweight(map, new Polynomial(type, terms));
	}

	public ConcreteFactory concreteFactory() {
		return concreteFactory;
	}

	public MonicFactory monicFactory() {
		return monicFactory;
	}

	public MonomialFactory monomialFactory() {
		return monomialFactory;
	}

	public SymbolicTypeFactory typeFactory() {
		return typeFactory;
	}

	public Polynomial zeroIntPolynomial() {
		return zeroIntPolynomial;
	}

	public Polynomial zeroRealPolynomial() {
		return zeroRealPolynomial;
	}

	public Polynomial oneIntPolynomial() {
		return oneIntPolynomial;
	}

	public Polynomial oneRealPolynomial() {
		return oneRealPolynomial;
	}

	public Polynomial polynomial(NumericPrimitive primitive) {
		SymbolicTypeIF type = primitive.type();
		NumericConcreteExpressionIF one = (type.isInteger() ? oneIntExpression
				: oneRealExpression);
		MonicMonomial monic = monicFactory.monicMonomial(primitive);
		Monomial monomial = monomialFactory.monomial(one, monic);
		Polynomial polynomial = polynomial(type, new Monomial[] { monomial });

		return polynomial;
	}

	public Polynomial polynomial(Monomial monomial) {
		if (monomial.isZero()) {
			return (monomial.type().isInteger() ? zeroIntPolynomial
					: zeroRealPolynomial);
		}
		return polynomial(monomial.type(), new Monomial[] { monomial });
	}

	public Polynomial polynomial(NumericConcreteExpressionIF constant) {
		if (constant.isZero()) {
			return (constant.type().isInteger() ? zeroIntPolynomial
					: zeroRealPolynomial);
		}
		return polynomial(monomialFactory.monomial(constant));
	}

	public Polynomial add(Polynomial polynomial0, Polynomial polynomial1) {
		Monomial[] terms0, terms1;
		int numTerms0, numTerms1, index0 = 0, index1 = 0;
		Vector<Monomial> termVector = new Vector<Monomial>();
		Monomial[] terms;
		int numTerms;
		SymbolicTypeIF type;

		assert polynomial0 != null;
		assert polynomial1 != null;
		assert polynomial0.type().equals(polynomial1.type());
		type = polynomial0.type();
		if (!type.equals(polynomial1.type())) {
			throw new IllegalArgumentException("Incompatible types:\n"
					+ polynomial0.type() + "\n" + polynomial1.type());
		}
		terms0 = polynomial0.terms();
		terms1 = polynomial1.terms();
		numTerms0 = terms0.length;
		numTerms1 = terms1.length;
		while (index0 < numTerms0 && index1 < numTerms1) {
			Monomial term0 = terms0[index0];
			Monomial term1 = terms1[index1];
			MonicMonomial monic0 = term0.monicMonomial();
			MonicMonomial monic1 = term1.monicMonomial();
			int compare = monic0.compareTo(monic1);

			if (compare == 0) {
				NumericConcreteExpressionIF coefficient = concreteFactory.addRational(
						term0.coefficient(), term1.coefficient());

				if (!coefficient.isZero())
					termVector.add(monomialFactory
							.monomial(coefficient, monic0));
				index0++;
				index1++;
			} else if (compare > 0) {
				termVector.add(term1);
				index1++;
			} else {
				termVector.add(term0);
				index0++;
			}
		}
		while (index0 < numTerms0) {
			termVector.add(terms0[index0]);
			index0++;
		}
		while (index1 < numTerms1) {
			termVector.add(terms1[index1]);
			index1++;
		}
		numTerms = termVector.size();
		terms = new Monomial[numTerms];
		for (int i = 0; i < numTerms; i++)
			terms[i] = termVector.elementAt(i);
		return polynomial(type, terms);
	}

	private Polynomial multiply(Monomial monomial, Polynomial polynomial) {
		Monomial[] oldTerms = polynomial.terms();
		int numTerms = oldTerms.length;
		Monomial[] newTerms = new Monomial[numTerms];

		for (int i = 0; i < numTerms; i++)
			newTerms[i] = monomialFactory.multiply(monomial, oldTerms[i]);
		return polynomial(polynomial.type(), newTerms);
	}

	public Polynomial multiply(Polynomial p1, Polynomial p2) {
		SymbolicTypeIF type = p1.type();
		Polynomial result = (type.isInteger() ? zeroIntPolynomial
				: zeroRealPolynomial);
		Monomial[] terms = p1.terms();

		for (Monomial term : terms)
			result = add(result, multiply(term, p2));
		return result;
	}

	public Polynomial exp(Polynomial polynomial, IntegerNumberIF exponent) {
		return (polynomial.type().isInteger() ? intExponentiator.exp(
				polynomial, exponent) : realExponentiator.exp(polynomial,
				exponent));
	}

	public Polynomial negate(Polynomial polynomial) {
		Monomial[] oldTerms = polynomial.terms();
		int numTerms = oldTerms.length;
		Monomial[] newTerms = new Monomial[numTerms];

		for (int i = 0; i < numTerms; i++) {
			newTerms[i] = monomialFactory.negate(oldTerms[i]);
		}
		return polynomial(polynomial.type(), newTerms);
	}

	public Polynomial castToReal(Polynomial polynomial) {
		// cast all monomials to real and add them up.
		Polynomial result = zeroRealPolynomial;

		for (Monomial monomial : polynomial.terms()) {
			result = add(result, polynomial(monomialFactory
					.castToReal(monomial)));
		}
		return result;
	}

	/**
	 * Divides the polynomial by the constant and returns the resulting
	 * polynomial. If polynomial has real type, every coeffcient is divided by
	 * the constant. If polynomial has integer type: the operation is only
	 * defined if the constant divides each coefficient. Otherwise an exception
	 * is thrown.
	 */
	public Polynomial divide(Polynomial polynomial,
			NumericConcreteExpressionIF constant) {
		int numTerms = polynomial.numTerms();
		Monomial[] oldTerms = polynomial.terms();
		Monomial[] newTerms = new Monomial[numTerms];

		for (int i = 0; i < numTerms; i++) {
			Monomial oldTerm = oldTerms[i];
			NumericConcreteExpressionIF oldCoefficient = oldTerm.coefficient();
			NumericConcreteExpressionIF newCoefficient = concreteFactory
					.divide(oldCoefficient, constant);

			if (polynomial.type().isInteger()
					&& !concreteFactory.mod(oldCoefficient,
							concreteFactory.abs(constant)).isZero()) {
				throw new IllegalArgumentException("Term " + oldTerm
						+ " is not divisible by " + constant);
			}
			newTerms[i] = monomialFactory.monomial(newCoefficient, oldTerm
					.monicMonomial());
		}
		return polynomial(polynomial.type(), newTerms);
	}

}
