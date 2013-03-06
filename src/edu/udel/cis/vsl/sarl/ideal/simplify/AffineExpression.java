package edu.udel.cis.vsl.sarl.ideal.simplify;

import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;

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
public class AffineExpression {

	private final static int classCode = AffineExpression.class.hashCode();

	private Polynomial pseudo; /* maybe null */

	private Number coefficient; /* not null */

	private Number offset; /* not null */

	public AffineExpression(Polynomial pseudo, Number coefficient, Number offset) {
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

	public Polynomial pseudo() {
		return pseudo;
	}

	public Number coefficient() {
		return coefficient;
	}

	public Number offset() {
		return offset;
	}

	boolean iff(boolean p, boolean q) {
		return (p && q) || ((!p) && !q);
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof AffineExpression) {
			AffineExpression that = (AffineExpression) object;

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
	public int hashCode() {
		int result = classCode ^ offset.hashCode() ^ coefficient.hashCode();

		if (pseudo != null)
			result ^= pseudo.hashCode();
		return result;
	}

}
