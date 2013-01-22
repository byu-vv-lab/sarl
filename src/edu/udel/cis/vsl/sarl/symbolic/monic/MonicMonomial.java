package edu.udel.cis.vsl.sarl.symbolic.monic;

import java.util.Arrays;

import edu.udel.cis.vsl.sarl.IF.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.IF.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.symbolic.NumericPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.power.PowerExpression;

/**
 * A monic monomial is a product of primitive expressions.
 * 
 * It is structured as a sequence of primitive expressions and exponents:
 * (x_0^n_0)...(x_r^n_r).
 * 
 * The primitive expressions are ordered from least to greatest by id.
 * 
 * The primitive expressions are unique (x_i=x_j => i=j).
 * 
 * The exponents are all positive integers.
 * 
 * It is possible for r=0. This represents the empty product, i.e., 1.
 * 
 * The degree of the monomial is the sum of the exponents. (If r=0, the degree
 * is 0.)
 * 
 * The set of monomials has an order. In this order, a monomial of higher degree
 * always comes before a monomial of lower degree. For two monomials of the same
 * degree, the id numbers of the primitive factors are used; see the comments
 * for the compareTo method below.
 * 
 * Note that a MonicMonomial cannot be 0. (On the other hand, a monomial can be
 * 0: it can be the product of the constant 0 and the MonicMonomial 1.)
 */
public class MonicMonomial extends CommonSymbolicExpression implements
		TreeExpressionIF, Comparable<MonicMonomial> {

	private static NumberFactoryIF numberFactory = Numbers.REAL_FACTORY;

	/**
	 * The factors are listed in increasing order. Factors are ordered by
	 * increasing id. Each factorPower is of the form e^n where e is a
	 * NumericPrimitive expression and n is ConcreteNumericExpressionIF
	 * (wrapping a non-negative concrete integer).
	 */
	private PowerExpression[] factorPowers;

	MonicMonomial(SymbolicTypeIF numericType, PowerExpression[] factorPowers) {
		super(numericType);
		assert factorPowers != null;
		for (PowerExpression factorPower : factorPowers) {
			assert numericType.equals(factorPower.type());
			assert factorPower.exponent().value().signum() > 0;
			assert factorPower.base() instanceof NumericPrimitive;

		}
		this.factorPowers = factorPowers;
	}

	public PowerExpression[] factorPowers() {
		return factorPowers;
	}

	public PowerExpression factorPower(int index) {
		return factorPowers[index];
	}

	public NumericPrimitive factor(int index) {
		return (NumericPrimitive) factorPowers[index].base();
	}

	public NumericConcreteExpressionIF exponent(int index) {
		return factorPowers[index].exponent();
	}

	protected int intrinsicHashCode() {
		return MonicMonomial.class.hashCode() + type().hashCode()
				+ Arrays.hashCode(factorPowers);
	}

	protected boolean intrinsicEquals(CommonSymbolicExpression expression) {
		if (expression instanceof MonicMonomial) {
			MonicMonomial that = (MonicMonomial) expression;

			return type().equals(that.type())
					&& Arrays.equals(factorPowers, that.factorPowers);
		}
		return false;
	}

	public String toString() {
		String result = "";

		for (int i = 0; i < factorPowers.length; i++) {
			if (i > 0)
				result += "*";
			result += factorPowers[i].atomString();
		}
		return result;
	}

	public String atomString() {
		return "(" + toString() + ")";
	}

	/**
	 * Order: (1) A monomial of higher degree comes before a monomial of lower
	 * degree. (2) If X and Y are primitives and X comes before Y, and m1 = pXq1
	 * (where p and q1 are some monomials) and m2 = pYq2 (where q2 is some
	 * monomial), then m1 comes before m2.
	 * 
	 * This assumes that the factors are already ordered in increasing order.
	 * 
	 * + : this-that>0, i.e. this>that, i.e. that comes first in list of
	 * increasing order
	 */
	public int compareTo(MonicMonomial that) {
		if (equals(that))
			return 0;

		int compare = numberFactory.compare(that.degree(), degree());

		if (compare != 0)
			return compare;

		int numFactors1 = factorPowers.length;
		int numFactors2 = that.factorPowers.length;
		int i = 0;

		while (true) {
			if (i >= numFactors1 || i >= numFactors2) {
				throw new RuntimeException(
						"TASS Internal Error on monomial ordering\n" + this
								+ "\n" + that);
			}
			compare = CommonSymbolicExpression.compare(
					(CommonSymbolicExpression) factorPowers[i].base(),
					(CommonSymbolicExpression) that.factorPowers[i].base());
			if (compare != 0)
				return compare;
			compare = numberFactory.compare(that.factorPowers[i].exponent()
					.value(), factorPowers[i].exponent().value());
			if (compare != 0)
				return compare;
			i++;
		}

	}

	public IntegerNumberIF degree() {
		IntegerNumberIF sum = numberFactory.zeroInteger();

		for (int i = 0; i < factorPowers.length; i++)
			sum = numberFactory.add(sum, (IntegerNumberIF) factorPowers[i]
					.exponent().value());
		return sum;
	}

	public boolean isOne() {
		return factorPowers.length == 0;
	}

	public TreeExpressionIF argument(int index) {
		return factorPowers[index];
	}

	public SymbolicOperator operator() {
		return SymbolicOperator.MULTIPLY;
	}

	public int numArguments() {
		return factorPowers.length;
	}
}
