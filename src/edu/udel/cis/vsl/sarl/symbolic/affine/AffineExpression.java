package edu.udel.cis.vsl.sarl.symbolic.affine;

import edu.udel.cis.vsl.sarl.IF.NumberIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.factorpoly.FactoredPolynomial;

/**
 * Represents an expression of the form aX+b, where X is a
 * "pseudo primitive polynomial", and a and b are concrete numbers.
 * 
 * A FactoredPolynomial X is a pseudo-primitive polynomial if all of the
 * following hold:
 * 
 * <ol>
 * <li>X is a polynomial with no constant term
 * <li>the leading coefficient of X is positive
 * <li>if X is real: leading coefficient is 1
 * <li>if X is int: the gcd of the absolute values of the coefficients of X is 1
 * </ol>
 * 
 * If aX=0, then X is null and coefficient is 0.
 */
public class AffineExpression extends CommonSymbolicExpression {

	private FactoredPolynomial pseudo; /* maybe null */

	private NumberIF coefficient; /* not null */

	private NumberIF offset; /* not null */

	AffineExpression(FactoredPolynomial pseudo, NumberIF coefficient,
			NumberIF offset, SymbolicTypeIF type) {
		super(type);
		assert coefficient != null;
		assert offset != null;
		assert iff(pseudo == null, coefficient.signum() == 0);
		this.pseudo = pseudo;
		this.coefficient = coefficient;
		this.offset = offset;
	}

	public String toString() {
		String result = "";

		if (pseudo != null) {
			result = coefficient + "*" + pseudo;
		}
		if (offset.signum() != 0) {
			if (pseudo != null)
				result += "+";
			result += offset;
		}
		return result;
	}

	public FactoredPolynomial pseudo() {
		return pseudo;
	}

	public NumberIF coefficient() {
		return coefficient;
	}

	public NumberIF offset() {
		return offset;
	}

	boolean iff(boolean p, boolean q) {
		return (p && q) || ((!p) && !q);
	}


	@Override
	protected boolean intrinsicEquals(CommonSymbolicExpression expression) {
		if (expression instanceof AffineExpression) {
			AffineExpression that = (AffineExpression) expression;

			if (!coefficient.equals(that.coefficient))
				return false;
			if (!offset.equals(that.offset))
				return false;
			if (pseudo == null)
				return that.pseudo == null;
			return pseudo.equals(that.pseudo);
		}
		return false;
	}

	@Override
	protected int intrinsicHashCode() {
		int result = AffineExpression.class.hashCode() + offset.hashCode()
				+ coefficient.hashCode();

		if (pseudo != null)
			result += pseudo.hashCode();
		return result;
	}

	@Override
	public String atomString() {
		return "(" + toString() + ")";
	}
}
