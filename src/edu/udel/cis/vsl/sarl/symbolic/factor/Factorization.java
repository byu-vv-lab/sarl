package edu.udel.cis.vsl.sarl.symbolic.factor;

import java.util.Arrays;

import edu.udel.cis.vsl.sarl.IF.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.polynomial.Polynomial;
import edu.udel.cis.vsl.sarl.symbolic.power.PowerExpression;

/**
 * An instance of this class represents a factorization of a polynomial. The
 * polynomial is expressed as a product of a scalar and a (possibly empty) set
 * of polynomials of positive degree.
 * 
 * If this is a factorization over the reals, constant is the leading
 * coefficient of the polynomial, and the factors are all monic polynomials of
 * positive degree.
 * 
 * If this is a factorization over the integers, constant is an integer, and is
 * not necessarily the leading coefficient of the polynomial. Moreover, the
 * factors are not necessarily monic. For example, 4x+6y=2*(2x+3y) might be the
 * factorization for 4x+6y. Instead, each factor will have the property that the
 * GCD of the absolute values of its coefficients is 1. Moreover, the
 * coefficient of the leading coefficient of each factor will be positive.
 * 
 * The factors are guaranteed to be indexed in order of increasing id.
 */
public class Factorization extends CommonSymbolicExpression implements
		TreeExpressionIF {

	/**
	 * This is the leading coefficient of the polynomial that is being factored.
	 */
	private NumericConcreteExpressionIF constant;

	private PowerExpression[] factorPowers;

	/**
	 * Pre-requisite: the given factors must comply to the conditions described
	 * in the javadoc comment for this class. In particular, they must occur in
	 * increasing order by id. These facts are NOT checked by this method, so
	 * errors could result if the user does not guarantee this.
	 */
	public Factorization(NumericConcreteExpressionIF constant,
			PowerExpression[] factorPowers) {
		super(constant.type());

		assert constant != null;
		assert factorPowers != null;
		assert !(constant.isZero() && factorPowers.length != 0);

		for (PowerExpression factorPower : factorPowers) {
			assert factorPower.type().equals(type());
			assert factorPower.polynomialPowerBase() instanceof Polynomial;
			assert ((Polynomial) factorPower.polynomialPowerBase()).degree().signum() > 0;
		}
		this.constant = constant;
		this.factorPowers = factorPowers;
	}

	public NumericConcreteExpressionIF constant() {
		return constant;
	}

	public int numFactors() {
		return factorPowers.length;
	}

	public PowerExpression[] factorPowers() {
		return factorPowers;
	}

	public Polynomial factor(int index) {
		return (Polynomial) factorPowers[index].polynomialPowerBase();
	}

	public NumericConcreteExpressionIF exponent(int index) {
		return factorPowers[index].exponent();
	}

	/**
	 * A trivial factorization is one in which the there is at most one factor.
	 * By "at most one" we mean that the sum of the multiplicities is less that
	 * or equal to 1.
	 */
	public boolean isTrivial() {
		return factorPowers.length == 0
				|| (factorPowers.length == 1 && factorPowers[0].exponent()
						.isOne());
	}

	public PowerExpression factorPower(int index) {
		return factorPowers[index];
	}

	public NumericConcreteExpressionIF multiplicity(int index) {
		return factorPowers[index].exponent();
	}

	public String toString() {
		String result = constant.toString();
		int numFactors = factorPowers.length;

		for (int i = 0; i < numFactors; i++) {
			result += "*" + factorPowers[i].atomString();
		}
		return result;
	}

	public String atomString() {
		return toString();
	}

	@Override
	protected boolean intrinsicEquals(CommonSymbolicExpression expression) {
		if (expression instanceof Factorization) {
			Factorization that = (Factorization) expression;

			return constant.equals(that.constant)
					&& Arrays.equals(factorPowers, that.factorPowers);
		}
		return false;
	}

	@Override
	protected int intrinsicHashCode() {
		return Factorization.class.hashCode() + constant.hashCode()
				+ Arrays.hashCode(factorPowers);
	}

	public boolean isZero() {
		return constant.isZero();
	}

	public TreeExpressionIF argument(int index) {
		switch (index) {
		case 0:
			return constant;
		default:
			return factorPowers[index - 1];
		}
	}

	public SymbolicOperator operator() {
		return SymbolicOperator.MULTIPLY;
	}

	public int numArguments() {
		return numFactors() + 1;
	}

}
