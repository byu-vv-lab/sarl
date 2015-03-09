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
package edu.udel.cis.vsl.sarl.number.real;

import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;

/**
 * Immutable implementation of {@link Interval}. Under construction.
 */
public class CommonInterval implements Interval {

	private boolean isIntegral;

	protected Number lower;

	protected boolean strictLower;

	protected Number upper;

	protected boolean strictUpper;

	// private NumberFactory factory = Numbers.REAL_FACTORY;

	public CommonInterval(boolean isIntegral, Number lower,
			boolean strictLower, Number upper, boolean strictUpper) {
		if (isIntegral) {
			assert (lower == null || lower instanceof IntegerNumber)
					&& (upper == null || upper instanceof IntegerNumber);
			assert (lower == null && !strictLower)
					|| (lower != null && strictLower);
			assert (upper == null && !strictUpper)
					|| (upper != null && strictUpper);
		} else {
			assert (lower == null || lower instanceof RationalNumber)
					&& (upper == null || upper instanceof RationalNumber);
			assert lower != null || !strictLower;
			assert upper != null || !strictUpper;
		}
		if (lower != null && upper != null) {
			int compare = lower.compareTo(upper);

			// <a,b> with a>b is unacceptable
			// (0,0) is fine: the unique representation of the empty set
			// [a,a] is fine, but not (a,a), [a,a), or (a,a]
			assert compare < 0
					|| (compare == 0 && ((!strictLower && !strictUpper) || lower
							.isZero()));
		}
		this.isIntegral = isIntegral;
		this.lower = lower;
		this.strictLower = strictLower;
		this.upper = upper;
		this.strictUpper = strictUpper;
	}

	@Override
	public CommonInterval clone() {
		return new CommonInterval(isIntegral, lower, strictLower, upper,
				strictUpper);
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof CommonInterval) {
			CommonInterval that = (CommonInterval) object;

			if (isIntegral != that.isIntegral
					|| strictLower != that.strictLower
					|| strictUpper != that.strictUpper)
				return false;
			if (upper == null) {
				if (that.upper != null)
					return false;
			} else {
				if (that.upper == null || !upper.equals(that.upper))
					return false;
			}
			if (lower == null) {
				if (that.lower != null)
					return false;
			} else {
				if (that.lower == null || !lower.equals(that.lower))
					return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public Number lower() {
		return lower;
	}

	@Override
	public Number upper() {
		return upper;
	}

	@Override
	public boolean strictLower() {
		return strictLower;
	}

	@Override
	public boolean strictUpper() {
		return strictUpper;
	}

	@Override
	public boolean isIntegral() {
		return isIntegral;
	}

	@Override
	public boolean isReal() {
		return !isIntegral;
	}

	@Override
	public String toString() {
		String result;

		result = strictLower ? "(" : "[";
		result += lower == null ? "-infty" : lower.toString();
		result += ",";
		result += upper == null ? "+infty" : upper.toString();
		result += strictUpper ? ")" : "]";
		return result;
	}

	@Override
	public boolean isEmpty() {
		return strictLower && strictUpper && lower != null && upper != null
				&& lower.equals(upper);
	}

	@Override
	public boolean contains(Number number) {
		if (lower != null) {
			int compare = lower.compareTo(number);

			if (compare > 0 || (compare == 0 && strictLower))
				return false;
		}
		if (upper != null) {
			int compare = upper.compareTo(number);

			if (compare < 0 || (compare == 0 && strictUpper))
				return false;
		}
		return true;
	}

	@Override
	public int compare(Number number) {
		if (lower != null) {
			int compare = lower.compareTo(number);

			if (compare > 0 || (compare == 0 && strictLower))
				return 1;
		}
		if (upper != null) {
			int compare = upper.compareTo(number);

			if (compare < 0 || (compare == 0 && strictUpper))
				return -1;
		}
		return 0;
	}

}
