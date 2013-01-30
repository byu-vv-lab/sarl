package edu.udel.cis.vsl.sarl.symbolic.polynomial;

import java.util.Arrays;

import edu.udel.cis.vsl.sarl.IF.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.symbolic.NumericPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.monic.MonicMonomial;
import edu.udel.cis.vsl.sarl.symbolic.monomial.Monomial;

/**
 * A Polynomial is an expression that is a polynomial in primitive expressions.
 * In other words, it is the sum of monomials in primitive expressions. The
 * Polynomial is unqiuely represented by an ordered set of Monomials.
 * 
 * The polynomial can be either real or integer type. If real, all monomials
 * have real type. If integer, all monomials have integer type.
 * 
 * Two polynomials are considered equal iff their term sets are equal.
 */
public class Polynomial extends CommonSymbolicExpression implements TreeExpressionIF {

	private static NumberFactoryIF numberFactory = Numbers.REAL_FACTORY;

	private static IntegerNumberIF NEG_ONE = numberFactory.integer(-1);

	private Monomial[] terms;

	/**
	 * Construct a new polynomial of either integer or real type, representing
	 * the sum of the given list of terms. The terms should be ordered from
	 * least to greatest monic monomial (see the natural order in
	 * MonicMonomial). This is assumed and is not checked. Erroneous behavior
	 * may result if this pre-condition is not met.
	 */
	// this is a problem: a lot of defects arise if this condition is
	// not checked. So checking it now.
	Polynomial(SymbolicTypeIF numericType, Monomial[] terms) {
		super(numericType);
		assert terms != null;

		for (int i = 0; i < terms.length; i++) {
			Monomial monomial = terms[i];

			assert numericType.equals(monomial.type());
			if (i > 0)
				assert monomial.monicMonomial().compareTo(
						terms[i - 1].monicMonomial()) > 0;
		}
		this.terms = terms;
	}

	public Monomial[] terms() {
		return terms;
	}

	public int numTerms() {
		return terms.length;
	}

	public NumericPrimitive extractPrimitive() {
		if (numTerms() == 1 && degree().isOne()) {
			Monomial monomial = terms[0];
			NumericConcreteExpressionIF constant = monomial.coefficient();

			if (constant.isOne()) {
				MonicMonomial monic = monomial.monicMonomial();

				return monic.factor(0);
			}
		}
		return null;
	}

	protected int intrinsicHashCode() {
		return type().hashCode() + Arrays.hashCode(terms);
	}

	protected boolean intrinsicEquals(CommonSymbolicExpression expression) {
		return expression instanceof Polynomial
				&& type().equals(expression.type())
				&& Arrays.equals(terms, ((Polynomial) expression).terms);
	}

	public String toString() {
		if (terms.length == 0)
			return "0";

		String result = "";

		for (int i = 0; i < terms.length; i++) {
			if (i > 0)
				result += " + ";
			result += terms[i];
		}
		return result;
	}

	public String atomString() {
		return "(" + toString() + ")";
	}

	public boolean isZero() {
		return terms.length == 0;
	}

	public boolean isOne() {
		return terms.length == 1 && terms[0].isOne();
	}

	public IntegerNumberIF degree() {
		/*
		 * since monomials are ordered, the degree of the lead monomial is the
		 * degree of the polynomial
		 */
		if (isZero())
			return NEG_ONE;
		return terms[0].degree();
	}

	public TreeExpressionIF argument(int index) {
		return terms[index];
	}

	public SymbolicOperator operator() {
		return SymbolicOperator.ADD;
	}

	public int numArguments() {
		return terms.length;
	}

	private NumberIF zero() {
		return (type().isInteger() ? numberFactory.zeroInteger()
				: numberFactory.zeroRational());
	}

	/**
	 * Returns the constant term as a NumberIF. This will always return a
	 * non-null value. I.e., will return 0 if there is no constant term.
	 */
	public NumberIF constantTerm() {
		if (terms.length == 0) {
			return zero();
		} else {
			Monomial lastMonomial = terms[terms.length - 1];

			if (lastMonomial.degree().signum() == 0) {
				return lastMonomial.coefficient().value();
			} else {
				return zero();
			}
		}
	}

}
