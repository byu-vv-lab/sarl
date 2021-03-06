/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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

	/**
	 * @param pseudo
	 *            Polynomial
	 * @param coefficient
	 *            leading coefficient number
	 * @param offset
	 *            offset number
	 */

	public AffineExpression(Polynomial pseudo, Number coefficient, Number offset) {
		assert coefficient != null;
		assert offset != null;
		assert iff(pseudo == null, coefficient.signum() == 0);
		this.pseudo = pseudo;
		this.coefficient = coefficient;
		this.offset = offset;
	}

	/**
	 * @return coefficient * pseudo
	 */
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

	/**
	 * 
	 * @return Polynomial
	 */

	public Polynomial pseudo() {
		return pseudo;
	}

	/**
	 * 
	 * @return coefficient number
	 */
	public Number coefficient() {
		return coefficient;
	}

	/**
	 * 
	 * @return offset number
	 */
	public Number offset() {
		return offset;
	}

	boolean iff(boolean p, boolean q) {
		return (p && q) || ((!p) && !q);
	}

	/**
	 * Equals compares an affineExpression to another
	 * 
	 * @return boolean true if equal false if not equal
	 * @param object
	 *            an AffineExpression
	 */

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
