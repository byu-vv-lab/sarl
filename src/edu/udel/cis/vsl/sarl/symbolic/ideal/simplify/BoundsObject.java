package edu.udel.cis.vsl.sarl.symbolic.ideal.simplify;

import edu.udel.cis.vsl.sarl.IF.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.IntervalIF;
import edu.udel.cis.vsl.sarl.IF.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.IF.NumberIF;
import edu.udel.cis.vsl.sarl.IF.RationalNumberIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicExpressionIF;
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
public class BoundsObject implements IntervalIF {

	protected NumberIF lower;

	protected boolean strictLower;

	protected NumberIF upper;

	protected boolean strictUpper;

	protected SymbolicExpressionIF expression;

	private NumberFactoryIF factory = Numbers.REAL_FACTORY;

	public static BoundsObject newLowerBound(SymbolicExpressionIF expression,
			NumberIF bound, boolean strict) {
		BoundsObject result = new BoundsObject(expression);

		result.setLower(bound, strict);
		return result;
	}

	public static BoundsObject newUpperBound(SymbolicExpressionIF expression,
			NumberIF bound, boolean strict) {
		BoundsObject result = new BoundsObject(expression);

		result.setUpper(bound, strict);
		return result;
	}

	public static BoundsObject newTightBound(SymbolicExpressionIF expression,
			NumberIF bound) {
		BoundsObject result = new BoundsObject(expression);

		result.makeConstant(bound);
		return result;
	}

	private BoundsObject(SymbolicExpressionIF symbolicConstant, NumberIF upper,
			boolean strictUpper, NumberIF lower, boolean strictLower) {
		assert symbolicConstant != null;
		this.expression = symbolicConstant;
		this.upper = upper;
		this.strictUpper = strictUpper;
		this.lower = lower;
		this.strictLower = strictLower;
	}

	private BoundsObject(SymbolicExpressionIF symbolicConstant) {
		this(symbolicConstant, null, true, null, true);
	}

	private void setLower(NumberIF bound, boolean strict) {
		if (bound == null && !strict)
			throw new RuntimeException(
					"Internal TASS error: infinite bound cannot be strict: "
							+ expression);
		if (isIntegral() && bound != null
				&& (strict || !(bound instanceof IntegerNumberIF))) {
			bound = (strict ? factory.add(factory.oneInteger(), factory
					.floor(factory.rational(bound))) : factory.ceil(factory
					.rational(bound)));
			strict = false;
		}
		this.lower = bound;
		this.strictLower = strict;
	}

	private void setUpper(NumberIF bound, boolean strict) {
		if (bound == null && !strict)
			throw new RuntimeException(
					"Internal TASS error: infinite bound cannot be strict: "
							+ expression);
		if (isIntegral() && bound != null
				&& (strict || !(bound instanceof IntegerNumberIF))) {
			bound = (strict ? factory.subtract(factory.ceil(factory
					.rational(bound)), factory.oneInteger()) : factory
					.floor(factory.rational(bound)));
			strict = false;
		}
		this.upper = bound;
		this.strictUpper = strict;
	}

	public BoundsObject clone() {
		return new BoundsObject(expression, upper, strictUpper, lower,
				strictLower);
	}

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

	public SymbolicExpressionIF symbolicConstant() {
		return expression;
	}

	public NumberIF lower() {
		return lower;
	}

	public NumberIF upper() {
		return upper;
	}

	public boolean strictLower() {
		return strictLower;
	}

	public boolean strictUpper() {
		return strictUpper;
	}

	public boolean isIntegral() {
		return expression.type().isInteger();
	}

	public boolean isReal() {
		return !isIntegral();
	}

	public void makeConstant(NumberIF value) {
		if (value == null)
			throw new RuntimeException(
					"Internal TASS error: tight bound cannot be null: "
							+ expression);
		if (isIntegral() && !(value instanceof IntegerNumberIF)) {
			if (!(value instanceof RationalNumberIF && factory
					.isIntegral((RationalNumberIF) value)))
				throw new RuntimeException(
						"TASS Internal error: attempt to set symbolic constant of integer type to non-integer value: "
								+ expression + " " + value);
			value = factory.integerValue((RationalNumberIF) value);
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
	public NumberIF constant() {
		if (lower != null && upper != null && lower.equals(upper)
				&& !strictLower && !strictUpper)
			return lower;
		return null;
	}

	private void enlargeLower(NumberIF thatLower, boolean thatStrict) {
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

	private void enlargeUpper(NumberIF thatUpper, boolean thatStrict) {
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

	public void restrictLower(NumberIF thatLower, boolean thatStrict) {
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

	public void restrictUpper(NumberIF thatUpper, boolean thatStrict) {
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
