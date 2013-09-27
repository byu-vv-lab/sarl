/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.ideal.simplify;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.number.Numbers;

/**
 * An instance of BoundsObject gives concrete upper and lower bounds on a
 * symbolic expression. Each bound can be either strict ("<") or not strict
 * ("<="). The lower bound can be -infty, represented by null, in which case the
 * lower bound must be strict. The upper bound can be +infty, represented by
 * null, in which case the upper bound must be strict.
 * 
 * A BoundsObject has one of two types: integral or real, which is 
 * determined by the type of the symbolic constant.
 * 
 * For an integer type bounds object, a non-null lower or upper 
 * bound must be a non-strict integer.
 * The static constructors guarantee this and take care of any conversions 
 * necessary if the inputs do not satisfy these constraints.
 * 
 * A BoundsObject instance is not immutable.   There are methods for modifying the object.
 * However the symbolic expression (and therefore type) cannot change.
 */
public class BoundsObject implements Interval {

	protected Number lower;

	protected boolean strictLower;

	protected Number upper;

	protected boolean strictUpper;

	protected SymbolicExpression expression;

	private NumberFactory factory = Numbers.REAL_FACTORY;

	/**
	 * Modifies the lower bound of a BoundsObject, and whether or not this lower bound is strict.
	 * 
	 * @param expression SARL Symbolic Expression being bounded below
	 * @param bound SARL Number providing the value of the bound being set
	 * @param strict Boolean value: True ("<") or False ("<=")
	 * @return BoundObject with a lower bound and strictness provided as arguments when method is called
	 * @see BoundsObject for associated constraints on real/integer type and strictness
	 */
	public static BoundsObject newLowerBound(SymbolicExpression expression,
			Number bound, boolean strict) {
		BoundsObject result = new BoundsObject(expression);

		result.setLower(bound, strict);
		return result;
	}

	/**
	 * Modifies the upper bound of a BoundsObject, and whether or not this upper bound is strict.
	 * 
	 * @param expression SARL Symbolic Expression being bounded above
	 * @param bound SARL Number providing the value of the bound being set
	 * @param strict Boolean value: True ("<") or False ("<=")
	 * @return BoundObject with an upper bound and strictness provided as arguments when method is called
	 * @see BoundsObject for associated constraints on real/integer type and strictness
	 */
	public static BoundsObject newUpperBound(SymbolicExpression expression,
			Number bound, boolean strict) {
		BoundsObject result = new BoundsObject(expression);

		result.setUpper(bound, strict);
		return result;
	}

	/**
	 * Modifies a BoundsObject to constrain the bound at a single value (value <= Symbolic Expression <= value).
	 * 
	 * @param expression SARL Symbolic Expression being tightly bounded (upper and lower)
	 * @param bound SARL Number providing the value of the bound being set
	 * @return BoundsObject with the provided Symbolic Expression with a upper and lower bound value of the 
	 * provided SARL Number
	 * @see BoundsObject for associated constraints on real/integer type and strictness
	 */
	public static BoundsObject newTightBound(SymbolicExpression expression,
			Number bound) {
		BoundsObject result = new BoundsObject(expression);

		result.makeConstant(bound);
		return result;
	}

	/**
	 * Private default constructor with the complete set of constructor arguments for a BoundsObject.
	 * 
	 * @param symbolicConstant SARL Symbolic Expression being being bounded
	 * @param upper SARL Number providing the value of the upper bound being set
	 * @param strictUpper Strictness of upper bound. Boolean value: True ("<") or False ("<=")
	 * @param lower SARL Number providing the value of the lower bound being set
	 * @param strictLower Strictness of lower bound. Boolean value: True ("<") or False ("<=")
	 * @see BoundsObject for associated constraints on real/integer type and strictness
	 */
	private BoundsObject(SymbolicExpression symbolicConstant, Number upper,
			boolean strictUpper, Number lower, boolean strictLower) {
		assert symbolicConstant != null;
		this.expression = symbolicConstant;
		this.upper = upper;
		this.strictUpper = strictUpper;
		this.lower = lower;
		this.strictLower = strictLower;
	}

	/**
	 * Single-parameter, private constructor called by newLowerBound, newUpperBound, newTightBound
	 * that sets the Symbolic Expression of a newly-created BoundsObject.
	 * 
	 * @param symbolicConstant SARL Symbolic Expression to have a bound applied
	 * @see BoundsObject for associated constraints on real/integer type and strictness
	 * @see newLowerBound for call of this method in modifying the lower bound of a BoundsObject
	 * @see newUpperBound for call of this method in modifying the upper bound of a BoundsObject
	 * @see newTightBound for call of this method in modifying the tight bound of a BoundsObject
	 */
	private BoundsObject(SymbolicExpression symbolicConstant) {
		this(symbolicConstant, null, true, null, true);
	}

	/**
	 * Private setter that modifies the lower bound of a BoundsObject
	 * 
	 * @param bound SARL Number providing the value of the lower bound being set
	 * @param strict Boolean value: True ("<") or False ("<=")
	 */
	private void setLower(Number bound, boolean strict) {
		if (bound == null && !strict)
			throw new RuntimeException(
					"Internal TASS error: infinite bound cannot be strict: "
							+ expression);
		if (isIntegral() && bound != null
				&& (strict || !(bound instanceof IntegerNumber))) {
			bound = (strict ? factory.add(factory.oneInteger(),
					factory.floor(factory.rational(bound))) : factory
					.ceil(factory.rational(bound)));
			strict = false;
		}
		this.lower = bound;
		this.strictLower = strict;
	}

	/**
	 * Private setter that modifies the upper bound of a BoundsObject
	 * 
	 * @param bound SARL Number providing the value of the upper bound being set
	 * @param strict Boolean value: True ("<") or False ("<=")
	 */
	private void setUpper(Number bound, boolean strict) {
		if (bound == null && !strict)
			throw new RuntimeException(
					"Internal TASS error: infinite bound cannot be strict: "
							+ expression);
		if (isIntegral() && bound != null
				&& (strict || !(bound instanceof IntegerNumber))) {
			bound = (strict ? factory
					.subtract(factory.ceil(factory.rational(bound)),
							factory.oneInteger()) : factory.floor(factory
					.rational(bound)));
			strict = false;
		}
		this.upper = bound;
		this.strictUpper = strict;
	}

	@Override
	public BoundsObject clone() {
		return new BoundsObject(expression, upper, strictUpper, lower,
				strictLower);
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof BoundsObject) {
			BoundsObject that = (BoundsObject) object;

			return expression.equals(that.expression)
					&& ((upper == null && that.upper == null) || upper
							.equals(that.upper))
					&& strictUpper == that.strictUpper
					&& ((lower == null && that.lower == null) || lower
							.equals(that.lower))
					&& strictLower == that.strictLower;
		}
		return false;
	}

	/**
	 * Getter method for retrieving the underlying Symbolic Expression of a BoundsObject
	 * 
	 * @return The Symbolic Expression of BoundsObject this method is called by
	 */
	public SymbolicExpression symbolicConstant() {
		return expression;
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
		return expression.type().isInteger();
	}

	@Override
	public boolean isReal() {
		return !isIntegral();
	}

	/**
	 * Setter to make a BoundsObject tightly bounded about the underlying Symbolic Expression of the BoundsObject
	 * (value <= Symbolic Expression <= value).
	 * <p>
	 * Automatically sets the tight bound (upper, lower) to non-strict.
	 * 
	 * @param value SARL Number providing the value of the bound being set
	 * @see BoundsObject for associated constraints on real/integer type and strictness
	 */
	public void makeConstant(Number value) {
		if (value == null)
			throw new RuntimeException(
					"Internal TASS error: tight bound cannot be null: "
							+ expression);
		if (isIntegral() && !(value instanceof IntegerNumber)) {
			if (!(value instanceof RationalNumber && factory
					.isIntegral((RationalNumber) value)))
				throw new RuntimeException(
						"TASS Internal error: attempt to set symbolic constant of integer type to non-integer value: "
								+ expression + " " + value);
			value = factory.integerValue((RationalNumber) value);
		}
		lower = value;
		upper = value;
		strictLower = false;
		strictUpper = false;
	}

	/**
	 * Indicates if the bounds of a BoundsObject are in the form "lower <= Symbolic Expression <= upper".
	 * 
	 * @return (Boolean value) True if a lower bound is not of a greater value than the upper bound, 
	 * false otherwise
	 */
	public boolean isConsistent() {
		int compare;

		if (lower == null || upper == null)
			return true;
		compare = factory.compare(lower, upper);
		if (compare > 0)
			return false;
		if (compare == 0 && (strictLower || strictUpper))
			return false;
		return true;
	}

	/**
	 * If the bounds are tight and define a single constant, i.e., a<=X<=a, this
	 * returns that constant a. Otherwise returns null.
	 */
	public Number constant() {
		if (lower != null && upper != null && lower.equals(upper)
				&& !strictLower && !strictUpper)
			return lower;
		return null;
	}

	private void enlargeLower(Number thatLower, boolean thatStrict) {
		if (lower == null)
			return;
		if (thatLower == null) {
			setLower(null, true);
		} else {
			int compare = factory.compare(lower, thatLower);

			if (compare > 0) {
				setLower(thatLower, thatStrict);
			} else if (compare == 0 && strictLower && !thatStrict) {
				setLower(lower, false);
			}
		}
	}

	private void enlargeUpper(Number thatUpper, boolean thatStrict) {
		if (upper == null)
			return;
		if (thatUpper == null) {
			setUpper(null, true);
		} else {
			int compare = factory.compare(thatUpper, upper);

			if (compare > 0) {
				setUpper(thatUpper, thatStrict);
			} else if (compare == 0 && strictUpper && !thatStrict) {
				setUpper(upper, false);
			}
		}
	}

	/**
	 * Modifies the lower bound of a BoundsObject if the desired new lower bound is of a lower absolute value.
	 * 
	 * @param thatLower The desired, new, lower bound for the BoundsObject calling this method
	 * @param thatStrict Strictness of new, more restrictive bound.
	 * @return If a new lower bound of -infty (value of null) is provided, no changes to the BoundsObject are made.
	 */
	public void restrictLower(Number thatLower, boolean thatStrict) {
		if (thatLower == null) {
			return;
		} else {
			if (lower == null) {
				setLower(thatLower, thatStrict);
			} else {
				int compare = factory.compare(thatLower, lower);

				if (compare > 0) {
					setLower(thatLower, thatStrict);
				} else if (compare == 0 && thatStrict && !strictLower) {
					setLower(lower, true);
				}
			}
		}
	}

	public void restrictUpper(Number thatUpper, boolean thatStrict) {
		if (thatUpper == null) {
			return;
		} else {
			if (upper == null) {
				setUpper(thatUpper, thatStrict);
			} else {
				int compare = factory.compare(upper, thatUpper);

				if (compare > 0) {
					setUpper(thatUpper, thatStrict);
				} else if (compare == 0 && thatStrict && !strictUpper) {
					setUpper(upper, true);
				}
			}
		}
	}

	public void enlargeTo(BoundsObject that) {
		enlargeLower(that.lower, that.strictLower);
		enlargeUpper(that.upper, that.strictUpper);
	}

	public void restrictTo(BoundsObject that) {
		restrictLower(that.lower, that.strictLower);
		restrictUpper(that.upper, that.strictUpper);
	}

	@Override
	public String toString() {
		String result;

		result = (lower == null ? "-infty" : lower.toString());
		result += " " + (strictLower ? "<" : "<=") + " ";
		result += expression;
		result += " " + (strictUpper ? "<" : "<=") + " ";
		result += (upper == null ? "+infty" : upper.toString());
		return result;
	}
}
