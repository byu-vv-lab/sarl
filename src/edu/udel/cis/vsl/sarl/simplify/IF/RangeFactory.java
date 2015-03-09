package edu.udel.cis.vsl.sarl.simplify.IF;

import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;

/**
 * Factory for producing instances of {@link Range}.
 * 
 * @author siegel
 *
 */
public interface RangeFactory {

	/**
	 * Returns an empty number set of the specified type.
	 * 
	 * @param isIntegral
	 *            is this integer type (not real type)?
	 * @return empty set of integer type if <code>isIntegral</code> is
	 *         <code>true</code>, else empty set of real type
	 */
	Range emptySet(boolean isIntegral);

	/**
	 * Returns the number set consisting of the single specified element. The
	 * type of <code>number</code> determines the type of the set.
	 * 
	 * @param number
	 *            a non-<code>null</code> {@link Number}
	 * @return singleton set containing <code>number</code>
	 */
	Range singletonSet(Number number);

	/**
	 * <p>
	 * Returns the set consisting of all x between <code>left</code> and
	 * <code>right</code>.
	 * </p>
	 * 
	 * <p>
	 * Either end-point may be <code>null</code>. A <code>null</code> left
	 * end-point is interpreted as negative infinity. A <code>null</code> right
	 * end-point is interpreted as positive infinity. If an end-point is
	 * <code>null</code>, the corresponding "strict" argument must be
	 * <code>true</code>.
	 * </p>
	 * 
	 * <p>
	 * If <code>isIntegral</code> is <code>true</code>, then both
	 * <code>left</code> and <code>right</code> must be instances of
	 * {@link IntegerNumber} (this includes the possibility of <code>null</code>
	 * ). If <code>isIntegral</code> is <code>false</code>, both must be
	 * instances of {@link RationalNumber}.
	 * </p>
	 * 
	 * @param left
	 *            the left end-point of the interval
	 * @param strictLeft
	 *            is the left inequality strict? If <code>true</code> then only
	 *            x strictly greater than the left end-point may be included in
	 *            the set
	 * @param right
	 *            the right end-point of the interval
	 * @param strictRight
	 *            is the right inequality strict? If <code>true</code>, then
	 *            only x strictly less than the right end-point may be included
	 *            in the set
	 * @param isIntegral
	 *            does the set have integer type (not real type)?
	 * @return the set representing the interval of numbers
	 */
	Range interval(Number left, boolean strictLeft, Number right,
			boolean strictRight, boolean isIntegral);
}
