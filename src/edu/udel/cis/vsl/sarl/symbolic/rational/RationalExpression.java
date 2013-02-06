package edu.udel.cis.vsl.sarl.symbolic.rational;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.factorpoly.FactoredPolynomial;

/**
 * A rational expression is the quotient of two polynomials (or instances of
 * FactoredPolynomial). To put into canonical form, the denominator is made
 * monic by dividing numerator and denominator by the leading coefficient of the
 * denominator.
 */
public class RationalExpression extends CommonSymbolicExpression implements
		TreeExpressionIF {

	private FactoredPolynomial numerator;

	private FactoredPolynomial denominator;

	RationalExpression(FactoredPolynomial numerator,
			FactoredPolynomial denominator) {
		super(numerator.type());
		assert numerator.type().typeKind() == SymbolicTypeKind.REAL;
		assert numerator.type().equals(denominator.type());
		this.numerator = numerator;
		this.denominator = denominator;
	}

	public FactoredPolynomial numerator() {
		return numerator;
	}

	public FactoredPolynomial denominator() {
		return denominator;
	}

	protected int intrinsicHashCode() {
		return RationalExpression.class.hashCode() + numerator.hashCode()
				+ denominator.hashCode();
	}

	protected boolean intrinsicEquals(CommonSymbolicExpression expression) {
		if (expression instanceof RationalExpression) {
			RationalExpression that = (RationalExpression) expression;

			return numerator.equals(that.numerator)
					&& denominator.equals(that.denominator);
		}
		return false;
	}

	public String toString() {
		String denominatorString = denominator.toString();

		if (denominatorString.equals("1") || denominatorString.equals("1.0")) {
			return numerator.toString();
		} else {
			return numerator.atomString() + "/" + denominator.atomString();
		}
	}

	public String atomString() {
		String denominatorString = denominator.toString();

		if (denominatorString.equals("1") || denominatorString.equals("1.0")) {
			return numerator.atomString();
		} else {
			return "(" + numerator.atomString() + "/"
					+ denominator.atomString() + ")";
		}
	}

	public boolean isZero() {
		return numerator.isZero();
	}

	public boolean isOne() {
		return numerator.equals(denominator);
	}

	public TreeExpressionIF argument(int index) {
		switch (index) {
		case 0:
			return numerator;
		case 1:
			return denominator;
		default:
			throw new IllegalArgumentException("numArguments=" + 2 + ", index="
					+ index);
		}
	}

	public SymbolicOperator operator() {
		return SymbolicOperator.DIVIDE;
	}

	public int numArguments() {
		return 2;
	}

}
