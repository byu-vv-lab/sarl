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
 * necessary if the inputs do not satify these constraints.
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

	public static BoundsObject newLowerBound(SymbolicExpression expression,
			Number bound, boolean strict) {
		BoundsObject result = new BoundsObject(expression);

		result.setLower(bound, strict);
		return result;
	}

	public static BoundsObject newUpperBound(SymbolicExpression expression,
			Number bound, boolean strict) {
		BoundsObject result = new BoundsObject(expression);

		result.setUpper(bound, strict);
		return result;
	}

	public static BoundsObject newTightBound(SymbolicExpression expression,
			Number bound) {
		BoundsObject result = new BoundsObject(expression);

		result.makeConstant(bound);
		return result;
	}

	private BoundsObject(SymbolicExpression symbolicConstant, Number upper,
			boolean strictUpper, Number lower, boolean strictLower) {
		assert symbolicConstant != null;
		this.expression = symbolicConstant;
		this.upper = upper;
		this.strictUpper = strictUpper;
		this.lower = lower;
		this.strictLower = strictLower;
	}

	private BoundsObject(SymbolicExpression symbolicConstant) {
		this(symbolicConstant, null, true, null, true);
	}

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
