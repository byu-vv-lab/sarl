package edu.udel.cis.vsl.sarl.simplify.common;

import java.math.BigInteger;
import java.util.ArrayList;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.simplify.IF.Range;

/**
 * Implementation of {@link Range} in which a set is represented as a finite
 * union of intervals. This class is immutable. Under construction.
 * 
 * The following are invariants. They force there to be a unique representation
 * for each set of numbers:
 * 
 * 1. All intervals in the array are non-<code>null</code> {@link Interval}s.
 * 
 * 2. An empty interval cannot occur in the array.
 * 
 * 3. All of the intervals in the array are disjoint.
 * 
 * 4. The intervals in the array are ordered from least to greatest.
 * 
 * 5. If an interval has the form {a,+infty}, then it is open on the right. If
 * an interval has the form {-infty,a}, then it is open on the left.
 * 
 * 6. If {a,b} and {b,c} are two consecutive intervals in the list, the first
 * one must be open on the right and the second one must be open on the left.
 * 
 * 7. If the range set has integer type, all of the intervals are integer
 * intervals. If it has real type, all of the intervals are real intervals.
 * 
 * 8. If the range set has integer type, all of the intervals are closed, except
 * for +infty and -infty.
 * 
 * @author siegel
 *
 */
public class IntervalUnionSet implements Range {

	private static NumberFactory numberFactory = Numbers.REAL_FACTORY;

	/**
	 * A boolean value to represent whether this {@link IntervalUnionSet} is
	 * integral or not: <code>true</code> - it is integral, or
	 * <code>false</code> - it is rational.
	 */
	private boolean isInt;

	/**
	 * An sorted array of {@link Interval}s; this {@link IntervalUnionSet} is
	 * the union of these {@link Interval}s.
	 */
	private Interval[] intervalArray;

	/**
	 * Constructs an {@link IntervalUnionSet} with defined type and size.
	 * 
	 * @param isIntegral
	 *            A boolean value to represent whether <code>this</code>
	 *            {@link IntervalUnionSet} is integral.
	 * @param size
	 *            A positive integer to represent the number of disjointed
	 *            {@link Interval}s in <code>this</code>
	 *            {@link IntervalUnionSet}. It would be 0, iff <code>this</code>
	 *            set is empty.
	 */
	private IntervalUnionSet(boolean isIntegral, int size) {
		// assert size > 0;
		isInt = isIntegral;
		intervalArray = new Interval[size];
	}

	/**
	 * Constructs an {@link IntervalUnionSet} representing an empty set
	 * 
	 * @param isIntegral
	 *            A boolean value to represent whether <code>this</code>
	 *            {@link IntervalUnionSet} is integral.
	 */
	public IntervalUnionSet(boolean isIntegral) {
		isInt = isIntegral;
		intervalArray = new Interval[0];
	}

	/**
	 * Constructs an {@link IntervalUnionSet} with exactly one {@link Interval}
	 * containing exactly one {@link Number}.
	 * 
	 * @param number
	 *            a non-<code>null</code> {@link Number}
	 */
	public IntervalUnionSet(Number number) {
		assert number != null;

		Interval interval = createIntervalwithInvariants(
				number instanceof IntegerNumber, number, false, number, false);

		isInt = number instanceof IntegerNumber;
		intervalArray = new Interval[1];
		intervalArray[0] = interval;
	}

	/**
	 * Constructs an {@link IntervalUnionSet} with exactly one {@link Interval}.
	 * 
	 * @param interval
	 *            a non-<code>null</code> {@link Interval}.
	 */
	public IntervalUnionSet(Interval interval) {
		// intervals are immutable, so re-use:
		assert interval != null;

		Interval temp = createIntervalwithInvariants(interval.isIntegral(),
				interval.lower(), interval.strictLower(), interval.upper(),
				interval.strictUpper());

		isInt = temp.isIntegral();
		if (temp.isEmpty()) {
			intervalArray = new Interval[0];
		} else {
			intervalArray = new Interval[1];
			intervalArray[0] = temp;
		}
	}

	/**
	 * Constructs an {@link IntervalUnionSet} with an ordered array of
	 * {@link Interval}s.
	 * 
	 * @param intervals
	 *            an array of {@link Interval}s (with at least one element) with
	 *            same type (real/integer).
	 */
	public IntervalUnionSet(Interval... intervals) {
		assert intervals != null;
		assert intervals.length > 0;

		int inputIndex = 0, size = 0;
		int inputSize = intervals.length;
		ArrayList<Interval> list = new ArrayList<Interval>();

		while (inputIndex < inputSize) {
			Interval temp = intervals[inputIndex];

			if (temp != null) {
				isInt = temp.isIntegral();
				if (!temp.isEmpty()) {
					isInt = temp.isIntegral();
					list.add(temp);
					inputIndex++;
					break;
				}
			}
			inputIndex++;
		}
		while (inputIndex < inputSize) {
			Interval temp = intervals[inputIndex];

			if (list.get(0).isUniversal()) {
				break;
			}
			if (temp != null && !temp.isEmpty()) {
				assert isInt == temp.isIntegral();
				addInterval(list, temp);
			}
			inputIndex++;
		}
		size = list.size();
		intervalArray = new Interval[size];
		list.toArray(intervalArray);
	}

	/**
	 * Constructs an {@link IntervalUnionSet} being same with <code>other</code>.
	 * 
	 * @param other
	 *            A non-<code>null</code> {@link IntervalUnionSet} would be
	 *            copied.
	 */
	public IntervalUnionSet(IntervalUnionSet other) {
		assert other != null;

		int size = other.intervalArray.length;

		isInt = other.isInt;
		intervalArray = new Interval[size];
		System.arraycopy(other.intervalArray, 0, intervalArray, 0, size);
	}

	/**
	 * A wrapper function for function "numberFactory.newInterval()" to ensure
	 * invariants.
	 * 
	 * @param isIntegral
	 * @param lower
	 * @param strictLower
	 * @param upper
	 * @param strictUpper
	 * @return Interval
	 */
	private Interval createIntervalwithInvariants(boolean isIntegral,
			Number lower, boolean strictLower, Number upper, boolean strictUpper) {
		Number zeroRational = numberFactory.rational(BigInteger.ZERO,
				BigInteger.ONE);
		Number zeroInteger = numberFactory.integer(BigInteger.ZERO);
		Number oneInteger = numberFactory.integer(BigInteger.ONE);
		boolean sl = strictLower, su = strictUpper;
		Number lo = lower, up = upper;
		int compare = 0;

		if (lo == null && up == null) {
			sl = true;
			su = true;
		} else if (lo == null) {
			sl = true;
			if (isIntegral && su) {
				up = numberFactory.subtract(up, oneInteger);
				su = false;
			}
		} else if (up == null) {
			su = true;
			if (isIntegral && sl) {
				lo = numberFactory.add(lo, oneInteger);
				sl = false;
			}
		} else {
			if (isIntegral && sl) {
				lo = numberFactory.add(lo, oneInteger);
				sl = false;
			}
			if (isIntegral && su) {
				up = numberFactory.subtract(up, oneInteger);
				su = false;
			}
			compare = numberFactory.subtract(up, lo).signum();
			if (isIntegral) {
				if (compare < 0) {
					return numberFactory.newInterval(true, zeroInteger, true,
							zeroInteger, true);
				}
			} else {
				if (!sl && !su) {
					if (compare < 0) {
						return numberFactory.newInterval(false, zeroRational,
								true, zeroRational, true);
					}
				} else {
					if (compare <= 0) {
						return numberFactory.newInterval(false, zeroRational,
								true, zeroRational, true);
					}
				}
			}

		}
		return numberFactory.newInterval(isIntegral, lo, sl, up, su);
	}

	/**
	 * To union a single non-<code>null</code> {@link Interval} into given list.
	 * 
	 * @param list
	 * @param interval
	 *            A non-<code>null</code> {@link Interval} with the same type of
	 *            list.
	 */
	private void addInterval(ArrayList<Interval> list, Interval interval) {
		// assert list != null;
		// assert interval != null;
		// assert isInt == interval.isIntegral();

		int size = list.size();
		int start = -2;
		int end = -2;
		int left = 0;
		int right = size - 1;
		Number lower = interval.lower();
		Number upper = interval.upper();
		boolean strictLower = interval.strictLower();
		boolean strictUpper = interval.strictUpper();
		boolean noIntersection = true;

		if (interval.isUniversal()) {
			list.clear();
			list.add(interval);
		} else if (lower == null) {
			start = -1;
		} else if (upper == null) {
			end = size;
		}
		while (left < right && start == -2) {
			int mid = (left + right) / 2;
			Interval temp = list.get(mid);
			int compare = temp.compare(lower);

			if (lower == temp.lower() && strictLower && temp.strictLower()) {
				compare = 0;
			}
			if (compare == 0) {
				start = mid;
				lower = list.get(start).lower();
				strictLower = list.get(start).strictLower();
				noIntersection = false;
				break;
			} else if (compare > 0) {
				right = mid - 1;
			} else {
				left = mid + 1;
			}
		}
		if (start == -2) {
			if (right < left) {
				if (right < 0) {
					start = -1;
				} else {
					int compareJoint = compareJoint(list.get(right), interval);

					if (compareJoint < 0) {
						start = left;
					} else {
						start = right;
						lower = list.get(start).lower();
						strictLower = list.get(start).strictLower();
						noIntersection = false;
					}
				}
			} else { // right == left
				Interval temp = list.get(right);
				int compareJoint = 0;
				int compare = temp.compare(lower);

				if (lower == temp.lower() && strictLower && temp.strictLower()) {
					compare = 0;
				}
				if (compare > 0) {
					if (right < 1) {
						start = -1;
					} else {
						compareJoint = compareJoint(list.get(right - 1),
								interval);
						if (compareJoint < 0) {
							start = right;
						} else {
							start = right - 1;
							lower = list.get(start).lower();
							strictLower = list.get(start).strictLower();
							noIntersection = false;
						}
					}
				} else if (compare < 0) {
					compareJoint = compareJoint(list.get(right), interval);
					if (compareJoint < 0) {
						start = right + 1;
					} else {
						start = right;
						lower = list.get(start).lower();
						strictLower = list.get(start).strictLower();
						noIntersection = false;
					}
				} else {
					start = right;
					lower = list.get(start).lower();
					strictLower = list.get(start).strictLower();
					noIntersection = false;
				}
			}
		}
		if (start == size) {
			list.add(interval);
			return;
		}
		left = 0;
		right = size - 1;
		while (left < right && end == -2) {
			int mid = (left + right) / 2;
			Interval temp = list.get(mid);
			int compare = temp.compare(upper);

			if (upper == temp.upper() && strictUpper && temp.strictUpper()) {
				compare = 0;
			}
			if (compare == 0) {
				end = mid;
				upper = list.get(end).upper();
				strictUpper = list.get(end).strictUpper();
				noIntersection = false;
				break;
			} else if (compare > 0) {
				right = mid - 1;
			} else {
				left = mid + 1;
			}
		}
		if (end == -2) {
			if (right < left) {
				if (right < 0) {
					end = -1;
				} else {
					int compareJoint = compareJoint(interval, list.get(left));

					if (compareJoint < 0) {
						end = right;
					} else {
						end = left;
						upper = list.get(end).upper();
						strictUpper = list.get(end).strictUpper();
						noIntersection = false;
					}
				}
			} else { // right == left
				Interval temp = list.get(right);
				int compareJoint = 0;
				int compare = temp.compare(upper);

				if (upper == temp.upper() && strictUpper && temp.strictUpper()) {
					compare = 0;
				}
				if (compare > 0) {
					compareJoint = compareJoint(interval, list.get(right));
					if (compareJoint < 0) {
						end = right - 1;
					} else {
						end = right;
						upper = list.get(end).upper();
						strictUpper = list.get(end).strictUpper();
						noIntersection = false;
					}
				} else if (compare < 0) {
					if (right >= size - 1) {
						end = size - 1;
					} else {
						compareJoint = compareJoint(interval,
								list.get(right + 1));
						if (compareJoint < 0) {
							end = right;
						} else {
							end = right + 1;
							upper = list.get(end).upper();
							strictUpper = list.get(end).strictUpper();
							noIntersection = false;
						}
					}
				} else {
					end = right;
					upper = list.get(end).upper();
					strictUpper = list.get(end).strictUpper();
					noIntersection = false;
				}
			}
		}
		if (noIntersection) {
			// assert start >= end;
			start = end == -1 ? 0 : start;
			start = start == -1 ? 0 : start;
			list.add(start, interval);
		} else {
			start = start < 0 ? 0 : start;
			end = end < size ? end : (size - 1);
			list.subList(start, end + 1).clear();
			list.add(
					start,
					createIntervalwithInvariants(isInt, lower, strictLower,
							upper, strictUpper));

		}
	}

	/**
	 * To compare the lower of two given {@link Interval}s
	 * 
	 * @param current
	 *            a non-<code>null</code> {@link Interval}.
	 * @param target
	 *            a non-<code>null</code> {@link Interval} has the same
	 *            type(real/integer) with <code>current</code>
	 * @return a negative integer iff <code>current</code> is left-most, a
	 *         positive integer iff <code>target</code> is left-most, or a zero
	 *         integer iff their lower and strictLower are exactly same.
	 */
	private int compareLo(Interval current, Interval target) {
		// assert current != null && target != null;
		// assert current.isIntegral() == target.isIntegral();

		boolean currentSL = current.strictLower();
		boolean targetSL = target.strictLower();
		Number currentLo = current.lower();
		Number targetLo = target.lower();

		if (currentLo == null && targetLo == null) {
			return 0; // Both are negative infinity
		} else if (currentLo == null) {
			return -1;
		} else if (targetLo == null) {
			return 1;
		} else {
			int compare = numberFactory.compare(currentLo, targetLo);

			if (compare == 0) {
				if (!currentSL && targetSL) {
					return -1;
				} else if (currentSL && !targetSL) {
					return 1;
				} else {
					return 0;
				}
			} else {
				return compare;
			}
		}
	}

	/**
	 * To compare the upper of two given {@link Interval}s
	 * 
	 * @param current
	 *            a non-<code>null</code> {@link Interval}.
	 * @param target
	 *            a non-<code>null</code> {@link Interval} has the same
	 *            type(real/integer) with <code>current</code>
	 * @return a negative integer iff <code>target</code> is right-most, a
	 *         positive integer iff <code>current</code> is right-most, or a
	 *         zero integer iff their upper and strictUpper are exactly same.
	 */
	private int compareUp(Interval current, Interval target) {
		// assert current != null && target != null;
		// assert current.isIntegral() == target.isIntegral();

		boolean currentSU = current.strictUpper();
		boolean targetSU = target.strictUpper();
		Number currentUp = current.upper();
		Number targetUp = target.upper();

		if (currentUp == null && targetUp == null) {
			return 0; // Both are positive infinity
		} else if (currentUp == null) {
			return 1;
		} else if (targetUp == null) {
			return -1;
		} else {
			int compare = numberFactory.compare(currentUp, targetUp);

			if (compare == 0) {
				if (!currentSU && targetSU) {
					return 1;
				} else if (currentSU && !targetSU) {
					return -1;
				} else {
					return 0;
				}
			} else {
				return compare;
			}
		}
	}

	/**
	 * To determine whether two given {@link Interval}s are jointed, it means
	 * that those two {@link Interval}s could be combined as a single one, by
	 * comparing <code>left</code>'s upper with <code>right</code>'s lower.
	 * 
	 * @param left
	 *            a non-<code>null</code> {@link Interval}.
	 * @param right
	 *            a non-<code>null</code> {@link Interval} has the same
	 *            type(real/integer) with <code>left</code>, and its lower
	 *            should greater than or equal to <code>left</code>'s lower.
	 * @return a negative integer iff they are NOT jointed, a zero integer iff
	 *         they are adjacent but no intersected, or a positive integer iff
	 *         they are intersected.
	 */
	private int compareJoint(Interval left, Interval right) {
		// assert left != null && right != null;
		// assert left.isIntegral() == right.isIntegral();

		boolean isIntegral = left.isIntegral();
		boolean leftSU = left.strictUpper();
		boolean rightSL = right.strictLower();
		Number leftUp = left.upper();
		Number rightLo = right.lower();

		if (leftUp == null || rightLo == null) {
			return 1;
		}

		Number difference = numberFactory.subtract(rightLo, leftUp);

		if (isIntegral) {
			/*
			 * For integral intervals even if the difference of their adjacent
			 * bound are 1, they are considered jointed.
			 */
			// e.g. [1, 1] U [2, 2] == [1, 2]
			if (difference.isOne()) {
				return 0;
			} else if (difference.signum() > 0) {
				return -1;
			} else {
				return 1;
			}
		} else {
			if (difference.signum() < 0) {
				return 1;
			} else if (difference.signum() > 0) {
				return -1;
			} else {
				if (leftSU && rightSL) {
					/*
					 * For rational intervals if the difference of their
					 * adjacent bound are 0 but both strict values are true,
					 * they are considered disjointed.
					 */
					// e.g. Both [0, 1) and (1, 2] excludes [1, 1]
					return -1;
				} else if (!leftSU && !rightSL) {
					return 1;
				} else {
					return 0;
				}
			}
		}
	}

	/**
	 * Does this {@link IntervalUnionSet} contain the given {@link Interval} as
	 * a sub-set?
	 * 
	 * @param interval
	 *            any non-<code>null</code> {@link Interval} of the same type
	 *            (integer/real) as this {@link IntervalUnionSet}
	 * @return <code>true</code> iff this {@link IntervalUnionSet} contains the
	 *         given {@link Interval}
	 */
	private boolean contains(Interval interval) {
		// assert interval != null;
		// assert interval.isIntegral() == isInt;

		int thisSize = intervalArray.length;
		int leftIdx = 0;
		int rightIdx = thisSize - 1;

		/*
		 * Currently used in contains(set), the interval could not be empty.
		 */
		// if (interval.isEmpty()) {
		// return true;
		// }// Any sets would contain an empty set
		while (leftIdx <= rightIdx) {
			int midIdx = (leftIdx + rightIdx) / 2;
			Interval midInterval = intervalArray[midIdx];
			int compareLower = compareLo(midInterval, interval);
			int compareUpper = compareUp(midInterval, interval);

			if (compareLower > 0) {
				if (compareUpper > 0) {
					int compareJoint = compareJoint(interval, midInterval);

					if (compareJoint <= 0) { // No intersection
						rightIdx = midIdx - 1;
						continue;
					}
				}
				return false;
			} else if (compareLower < 0) {
				if (compareUpper < 0) {
					int compareJoint = compareJoint(midInterval, interval);

					if (compareJoint > 0) {
						return false;
					} else { // No intersection
						leftIdx = midIdx + 1;
						continue;
					}
				} else { // compareUp >= 0
					return true;
				}
			} else {
				if (compareUpper < 0) {
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Add a single {@link Number} to this {@link IntervalUnionSet}
	 * 
	 * @param number
	 *            a single non-<code>null</code> {@link Number} of the same type
	 *            (integer/real) with this {@link IntervalUnionSet}
	 */
	public IntervalUnionSet addNumber(Number number) {
		assert number != null;
		assert (number instanceof IntegerNumber) == isInt;

		int size = intervalArray.length;
		int leftIndex = 0;
		int rightIndex = size - 1;

		while (leftIndex <= rightIndex) {
			int midIndex = (leftIndex + rightIndex) / 2;
			int compareNumber = intervalArray[midIndex].compare(number);

			if (compareNumber > 0 && leftIndex != rightIndex) {
				// The number is on the left part of the current set.
				rightIndex = midIndex - 1;
			} else if (compareNumber < 0 && leftIndex != rightIndex) {
				// The number is on the right part of the current set.
				leftIndex = midIndex + 1;
			} else if (compareNumber == 0) {
				// The set contains the number.
				return new IntervalUnionSet(this);
			} else {
				// The set does NOT contain the number
				leftIndex = compareNumber < 0 ? midIndex : midIndex - 1;
				rightIndex = leftIndex + 1;
				leftIndex = Math.max(leftIndex, 0);
				rightIndex = Math.min(rightIndex, size - 1);

				boolean leftSl = intervalArray[leftIndex].strictLower();
				boolean rightSu = intervalArray[rightIndex].strictUpper();
				Number leftLo = intervalArray[leftIndex].lower();
				Number leftUp = intervalArray[leftIndex].upper();
				Number rightLo = intervalArray[rightIndex].lower();
				Number rightUp = intervalArray[rightIndex].upper();
				Number leftDiff = numberFactory.subtract(number, leftUp);
				Number rightDiff = numberFactory.subtract(rightLo, number);
				boolean leftJoint = isInt ? leftDiff.isOne() : leftDiff
						.isZero();
				boolean rightJoint = isInt ? rightDiff.isOne() : rightDiff
						.isZero();

				if (leftJoint && rightJoint) {
					// The number connects two disjointed interval
					IntervalUnionSet result = new IntervalUnionSet(isInt,
							size - 1);

					System.arraycopy(intervalArray, 0, result.intervalArray, 0,
							leftIndex);
					result.intervalArray[leftIndex] = createIntervalwithInvariants(
							isInt, leftLo, leftSl, rightUp, rightSu);
					System.arraycopy(intervalArray, rightIndex + 1,
							result.intervalArray, rightIndex, size - rightIndex
									- 1);
					return result;
				} else if (leftJoint) {
					// The number changes an interval's lower condition
					IntervalUnionSet result = new IntervalUnionSet(this);

					if (isInt) {
						result.intervalArray[leftIndex] = createIntervalwithInvariants(
								true, leftLo, false, number, false);
					} else {
						result.intervalArray[leftIndex] = createIntervalwithInvariants(
								false, leftLo, leftSl, number, false);
					}
					return result;
				} else if (rightJoint) {
					// The number changes an interval's upper condition
					IntervalUnionSet result = new IntervalUnionSet(this);

					if (isInt) {
						result.intervalArray[rightIndex] = createIntervalwithInvariants(
								true, number, false, rightUp, false);
					} else {
						result.intervalArray[rightIndex] = createIntervalwithInvariants(
								false, number, false, rightUp, rightSu);
					}
					return result;
				} else {
					// The number becomes a new point interval
					IntervalUnionSet result = new IntervalUnionSet(isInt,
							size + 1);

					if (leftIndex == rightIndex) {
						if (leftIndex == 0) {
							// To add the number to the head
							result.intervalArray[0] = createIntervalwithInvariants(
									isInt, number, false, number, false);
							System.arraycopy(intervalArray, 0,
									result.intervalArray, 1, size);
						} else {
							// To add the number to the tail
							result.intervalArray[size] = createIntervalwithInvariants(
									isInt, number, false, number, false);
							System.arraycopy(intervalArray, 0,
									result.intervalArray, 0, size);
						}
					} else {
						// To insert the number to the body
						System.arraycopy(intervalArray, 0,
								result.intervalArray, 0, rightIndex);
						result.intervalArray[rightIndex] = createIntervalwithInvariants(
								isInt, number, false, number, false);
						System.arraycopy(intervalArray, rightIndex,
								result.intervalArray, rightIndex + 1, size
										- rightIndex);
					}
					return result;
				}
			}
		} // Using binary searching to compare the number with intervals
		return new IntervalUnionSet(number);// To add a number to an empty set.
	}

	@Override
	public boolean isIntegral() {
		return isInt;
	}

	@Override
	public boolean isEmpty() {
		return intervalArray.length == 0;
	}

	@Override
	public boolean containsNumber(Number number) {
		assert number != null;
		assert (number instanceof IntegerNumber) == isInt;

		int size = intervalArray.length;
		int leftIdx = 0;
		int rightIdx = size - 1;

		while (leftIdx <= rightIdx) {
			int midIdx = (leftIdx + rightIdx) / 2;
			int compareNumber = intervalArray[midIdx].compare(number);

			if (compareNumber > 0) {
				rightIdx = midIdx - 1;
			} else if (compareNumber < 0) {
				leftIdx = midIdx + 1;
			} else if (compareNumber == 0) {
				return true;
			} else {
				break;
			}
		} // Using binary searching to compare the number with intervals
		return false;
	}

	@Override
	public boolean contains(Range set) {
		assert set != null;
		assert set.isIntegral() == isInt;

		IntervalUnionSet other = (IntervalUnionSet) set;
		Interval[] thisArray = intervalArray;
		Interval[] otherArray = other.intervalArray;
		int thisSize = thisArray.length;
		int otherSize = otherArray.length;
		int thisIndex = 0;
		int otherIndex = 0;

		if (otherSize < 1) {
			// Any set contains an empty set
			return true;
		} else if (thisSize < 1) {
			// An empty set does NOT contain any non-empty set.
			return false;
		} else if (thisArray[0].isUniversal()) {
			// A universal set contains any set.
			return true;
		} else if (otherArray[0].isUniversal()) {
			// Only a universal set could contain itself
			return false;
		} else if (otherSize == 1) {
			return contains(otherArray[0]);
		}
		while (thisIndex < thisSize) {
			Interval thisInterval = thisArray[thisIndex];

			while (otherIndex < otherSize) {
				Interval otherInterval = otherArray[otherIndex];
				int compareLower = compareLo(otherInterval, thisInterval);

				if (compareLower < 0) {
					return false;
				} else if (compareLower > 0) {
					int compareJoint = compareJoint(thisInterval, otherInterval);

					if (compareJoint > 0) {
						int compareUpper = compareUp(otherInterval,
								thisInterval);

						if (compareUpper < 0) {
							otherIndex++;
						} else if (compareUpper > 0) {
							return false;
						} else {
							otherIndex++;
							thisIndex++;
							break;
						}
					} else {
						if (thisIndex >= thisSize - 1) {
							return false;
						}
						thisIndex++;
						break;
					}
				} else {
					int compareUpper = compareUp(otherInterval, thisInterval);

					if (compareUpper > 0) {
						return false;
					} else if (compareUpper < 0) {
						otherIndex++;
					} else {
						otherIndex++;
						thisIndex++;
						break;
					}
				}
			}
			if (otherIndex == otherSize) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean intersects(Range set) {
		assert set != null;
		assert set.isIntegral() == isInt;

		IntervalUnionSet other = (IntervalUnionSet) set;
		Interval[] thisArray = intervalArray;
		Interval[] otherArray = other.intervalArray;
		int thisSize = thisArray.length;
		int otherSize = otherArray.length;
		int thisIndex = 0;
		int otherIndex = 0;

		if (thisSize == 0 || otherSize == 0) {
			return false;
		}// An empty set could not intersect with any sets.
		while (thisIndex < thisSize && otherIndex < otherSize) {
			Interval thisInterval = thisArray[thisIndex];
			Interval otherInterval = otherArray[otherIndex];
			int compareLower = compareLo(otherInterval, thisInterval);
			int compareUpper = compareUp(otherInterval, thisInterval);

			if (compareLower > 0) {
				if (compareUpper > 0) {
					int compareJoint = compareJoint(thisInterval, otherInterval);

					if (compareJoint <= 0) { // No intersection
						thisIndex++;
						break;
					}
				}
				return true;
			} else if (compareLower < 0) {
				if (compareUpper < 0) {
					int compareJoint = compareJoint(otherInterval, thisInterval);

					if (compareJoint <= 0) { // No intersection
						otherIndex++;
						continue;
					}
				}
				return true;
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public IntervalUnionSet union(Range set) {
		assert set != null;
		assert set.isIntegral() == isInt;

		IntervalUnionSet other = (IntervalUnionSet) set;
		Interval[] thisArray = intervalArray;
		Interval[] otherArray = other.intervalArray;
		int thisSize = thisArray.length;
		int otherSize = otherArray.length;

		if (thisSize <= 0) {
			return new IntervalUnionSet(other);
		} else if (otherSize <= 0) {
			return new IntervalUnionSet(this);
		} else if (thisArray[0].isUniversal()) {
			return new IntervalUnionSet(this);
		} else if (otherArray[0].isUniversal()) {
			return new IntervalUnionSet(other);
		}
		// Using binary method to union a set with single interval
		if (thisSize == 1) {
			thisArray = otherArray;
			otherArray = intervalArray;
			thisSize = otherSize;
			otherSize = 1;
		}
		if (otherSize == 1) {
			ArrayList<Interval> list = new ArrayList<Interval>();

			for (Interval i : thisArray) {
				list.add(i);
			}
			addInterval(list, otherArray[0]);

			IntervalUnionSet result = new IntervalUnionSet(isInt, list.size());

			list.toArray(result.intervalArray);
			return result;
		}

		int thisIndex = 0, otherIndex = 0;
		boolean isChanged = false;
		Interval thisInterval = thisArray[0];
		Interval otherInterval = otherArray[0];
		Interval temp = null;
		ArrayList<Interval> list = new ArrayList<Interval>();
		int compareLower = compareLo(thisInterval, otherInterval);

		if (compareLower > 0) {
			temp = otherInterval;
			otherIndex++;
		} else {
			temp = thisInterval;
			thisIndex++;
		}// To find the left-most interval in two sets.
		while (isChanged || thisIndex < thisSize || otherIndex < otherSize) {
			isChanged = false;
			while (thisIndex < thisSize) {
				Interval next = thisArray[thisIndex];
				int compareTempNext = compareJoint(temp, next);

				if (compareTempNext < 0) {
					// temp Left-disjoint next, then stop
					break;
				} else {
					int compareUpper = compareUp(temp, next);

					if (compareUpper < 0) {
						temp = createIntervalwithInvariants(isInt,
								temp.lower(), temp.strictLower(), next.upper(),
								next.strictUpper());
						isChanged = true;
						thisIndex++;
						break;
					}// else temp Contains next, then skip
					thisIndex++;
				}
			}
			while (otherIndex < otherSize) {
				Interval next = otherArray[otherIndex];
				int compareTempNext = compareJoint(temp, next);

				if (compareTempNext < 0) {
					// Temp Left-disjoint next. then stop
					break;
				} else {
					int compareUpper = compareUp(temp, next);

					if (compareUpper < 0) {
						temp = createIntervalwithInvariants(isInt,
								temp.lower(), temp.strictLower(), next.upper(),
								next.strictUpper());
						isChanged = true;
						otherIndex++;
						break;
					}// else temp Contains next, then skip
					otherIndex++;
				}
			}
			if (!isChanged) {
				list.add(temp);
				if (thisIndex < thisSize && otherIndex < otherSize) {
					thisInterval = thisArray[thisIndex];
					otherInterval = otherArray[otherIndex];
					compareLower = compareLo(thisInterval, otherInterval);
					if (compareLower > 0) {
						temp = otherInterval;
						otherIndex++;
					} else {
						temp = thisInterval;
						thisIndex++;
					}
				} else {
					// To concatenate
					while (thisIndex < thisSize) {
						list.add(thisArray[thisIndex]);
						thisIndex++;
					}
					while (otherIndex < otherSize) {
						list.add(otherArray[otherIndex]);
						otherIndex++;
					}
				}
			}
		}

		IntervalUnionSet result = new IntervalUnionSet(isInt, list.size());

		list.toArray(result.intervalArray);
		return result;
	}

	@Override
	public IntervalUnionSet intersect(Range set) {
		assert set != null;
		assert set.isIntegral() == isInt;

		ArrayList<Interval> list = new ArrayList<Interval>();
		IntervalUnionSet other = (IntervalUnionSet) set;
		Interval[] thisArray = intervalArray;
		Interval[] otherArray = other.intervalArray;
		Interval thisInterval = null;
		int thisSize = thisArray.length;
		int otherSize = otherArray.length;
		int thisIndex = 0;
		int otherIndex = 0;

		if (otherSize < 1 || thisSize < 1) {
			return new IntervalUnionSet(isInt);
		} else if (thisArray[0].isUniversal()) {
			return new IntervalUnionSet(other);
		} else if (otherArray[0].isUniversal()) {
			return new IntervalUnionSet(this);
		}

		while (thisIndex < thisSize && otherIndex < otherSize) {
			thisInterval = thisArray[thisIndex];
			Interval otherInterval = otherArray[otherIndex];
			int compareLower = compareLo(otherInterval, thisInterval);
			int compareUpper = compareUp(otherInterval, thisInterval);

			if (compareLower < 0) {
				if (compareUpper < 0) {
					int compareJoint = compareJoint(otherInterval, thisInterval);

					if (compareJoint > 0) {
						list.add(createIntervalwithInvariants(isInt,
								thisInterval.lower(),
								thisInterval.strictLower(),
								otherInterval.upper(),
								otherInterval.strictUpper()));
						thisInterval = createIntervalwithInvariants(isInt,
								otherInterval.upper(),
								!otherInterval.strictUpper(),
								thisInterval.upper(),
								thisInterval.strictUpper());
					}
					otherIndex++;
				} else if (compareUpper > 0) {
					list.add(thisInterval);
					thisIndex++;
				} else {
					list.add(thisInterval);
					thisIndex++;
					otherIndex++;
				}
			} else if (compareLower > 0) {
				if (compareUpper < 0) {
					list.add(otherInterval);
					thisInterval = createIntervalwithInvariants(isInt,
							otherInterval.upper(),
							!otherInterval.strictUpper(), thisInterval.upper(),
							thisInterval.strictUpper());
					otherIndex++;
				} else if (compareUpper > 0) {
					int compareJoint = compareJoint(thisInterval, otherInterval);

					if (compareJoint > 0) {
						list.add(createIntervalwithInvariants(isInt,
								otherInterval.lower(),
								otherInterval.strictLower(),
								thisInterval.upper(),
								thisInterval.strictUpper()));
					}
					thisIndex++;
				} else {
					list.add(otherInterval);
					otherIndex++;
					thisIndex++;
				}
			} else {
				if (compareUpper < 0) {
					list.add(otherInterval);
					thisInterval = createIntervalwithInvariants(isInt,
							otherInterval.upper(),
							!otherInterval.strictUpper(), thisInterval.upper(),
							thisInterval.strictUpper());
					otherIndex++;
				} else if (compareUpper > 0) {
					list.add(thisInterval);
					thisIndex++;
				} else {
					list.add(thisInterval);
					thisIndex++;
					otherIndex++;
				}
			}
		}

		int newSize = list.size();
		Interval[] newArray = new Interval[newSize];
		IntervalUnionSet result = new IntervalUnionSet(isInt, newSize);

		list.toArray(newArray);
		System.arraycopy(newArray, 0, result.intervalArray, 0, newSize);
		return result;
	}

	@Override
	public IntervalUnionSet minus(Range set) {
		assert set != null;
		assert set.isIntegral() == isInt;

		ArrayList<Interval> list = new ArrayList<Interval>();
		IntervalUnionSet other = (IntervalUnionSet) set;
		Interval[] thisArray = intervalArray;
		Interval[] otherArray = other.intervalArray;
		Interval thisInterval = null;
		int thisSize = thisArray.length;
		int otherSize = otherArray.length;
		int thisIndex = 0;
		int otherIndex = 0;

		if (otherSize < 1 || thisSize < 1) {
			return new IntervalUnionSet(this);
		} else if (otherArray[0].isUniversal()) {
			return new IntervalUnionSet(isInt);
		}
		while (thisIndex < thisSize) {
			thisInterval = thisArray[thisIndex];
			while (otherIndex < otherSize) {
				Interval otherInterval = otherArray[otherIndex];
				int compareLower = compareLo(otherInterval, thisInterval);
				int compareUpper = compareUp(otherInterval, thisInterval);

				if (compareLower < 0) {
					if (compareUpper < 0) {
						int compareJoint = compareJoint(otherInterval,
								thisInterval);

						if (compareJoint > 0) {
							thisInterval = createIntervalwithInvariants(isInt,
									otherInterval.upper(),
									!otherInterval.strictUpper(),
									thisInterval.upper(),
									thisInterval.strictUpper());
						}
						otherIndex++;
					} else if (compareUpper > 0) {
						thisIndex++;
						break;
					} else {
						thisIndex++;
						otherIndex++;
						break;
					}
				} else if (compareLower > 0) {
					if (compareUpper < 0) {
						list.add(createIntervalwithInvariants(isInt,
								thisInterval.lower(),
								thisInterval.strictLower(),
								otherInterval.lower(),
								!otherInterval.strictLower()));
						thisInterval = createIntervalwithInvariants(isInt,
								otherInterval.upper(),
								!otherInterval.strictUpper(),
								thisInterval.upper(),
								thisInterval.strictUpper());
						otherIndex++;
					} else if (compareUpper > 0) {
						int compareJoint = compareJoint(thisInterval,
								otherInterval);

						if (compareJoint > 0) {
							list.add(createIntervalwithInvariants(isInt,
									thisInterval.lower(),
									thisInterval.strictLower(),
									otherInterval.lower(),
									!otherInterval.strictLower()));
						} else {
							list.add(thisInterval);
						}
						thisIndex++;
						break;
					} else {
						list.add(createIntervalwithInvariants(isInt,
								thisInterval.lower(),
								thisInterval.strictLower(),
								otherInterval.lower(),
								!otherInterval.strictLower()));
						thisIndex++;
						otherIndex++;
						break;
					}
				} else {
					if (compareUpper < 0) {
						thisInterval = createIntervalwithInvariants(isInt,
								otherInterval.upper(),
								!otherInterval.strictUpper(),
								thisInterval.upper(),
								thisInterval.strictUpper());
						otherIndex++;
					} else if (compareUpper > 0) {
						thisIndex++;
						break;
					} else {
						thisIndex++;
						otherIndex++;
						break;
					}
				}
			}
			if (otherIndex == otherSize) {
				if (thisIndex < thisSize) {
					int compareUp = compareUp(thisInterval,
							thisArray[thisIndex]);

					if (compareUp == 0) {
						list.add(thisInterval);
						thisIndex++;
					}
				}
				while (thisIndex < thisSize) {
					list.add(thisArray[thisIndex]);
					thisIndex++;
				}
				break;
			}
		}

		int newSize = list.size();
		IntervalUnionSet result = null;

		if (newSize < 1) {
			result = new IntervalUnionSet(isInt);
		} else {
			Interval[] newArray = new Interval[newSize];

			result = new IntervalUnionSet(isInt, newSize);
			list.toArray(newArray);
			System.arraycopy(newArray, 0, result.intervalArray, 0, newSize);
		}
		return result;
	}

	@Override
	public IntervalUnionSet affineTransform(Number a, Number b) {
		assert a != null && b != null;
		assert a instanceof IntegerNumber == isInt;
		assert b instanceof IntegerNumber == isInt;

		int index = 0;
		int size = intervalArray.length;
		Interval[] tempArray = new Interval[size];
		Interval temp = null, current = null;

		if (0 == size) {
			return new IntervalUnionSet(isInt);
		}
		current = intervalArray[index];
		if (current.isUniversal()) {
			return new IntervalUnionSet(this);
		}
		if (a.signum() == 0) {
			return new IntervalUnionSet(createIntervalwithInvariants(isInt, b,
					false, b, false));
		} else if (a.signum() > 0) {
			while (index < size) {
				current = intervalArray[index];
				temp = numberFactory.affineTransform(current, a, b);
				tempArray[index] = temp;
				index++;
			}
		} else if (a.signum() < 0) {
			while (index < size) {
				current = intervalArray[index];
				temp = numberFactory.affineTransform(current, a, b);
				index++;
				tempArray[size - index] = temp;
			}
		}

		IntervalUnionSet result = new IntervalUnionSet(isInt, size);

		System.arraycopy(tempArray, 0, result.intervalArray, 0, size);
		return result;
	}

	@Override
	public IntervalUnionSet complement() {
		Interval univ = createIntervalwithInvariants(isIntegral(), null, true,
				null, true);
		IntervalUnionSet univSet = new IntervalUnionSet(univ);
		IntervalUnionSet rtn = (IntervalUnionSet) univSet.minus(this);

		return rtn;
	}

	@Override
	public BooleanExpression symbolicRepresentation(SymbolicConstant x,
			PreUniverse universe) {
		assert x != null;
		assert universe != null;

		BooleanExpression result = universe.bool(false);
		NumericExpression lowerExpression = null;
		NumericExpression upperExpression = null;
		NumericExpression symbolX = (NumericExpression) x;
		Interval interval = null;
		int index = 0;
		int size = intervalArray.length;

		if (0 >= size) {
			return result;
		}
		while (index < size) {
			interval = intervalArray[index];
			index++;

			boolean strictLower = interval.strictLower();
			boolean strictUpper = interval.strictUpper();
			Number lower = interval.lower();
			Number upper = interval.upper();
			BooleanExpression temp1, temp2, resultExpression;

			if (lower == null && upper == null) {
				result = universe.bool(true);
				return result;
			} else if (lower == null) {
				upperExpression = universe.number(upper);
				resultExpression = strictUpper ? universe.lessThan(symbolX,
						upperExpression) : universe.lessThanEquals(symbolX,
						upperExpression);
			} else if (upper == null) {
				lowerExpression = universe.number(lower);
				resultExpression = strictLower ? universe.lessThan(
						lowerExpression, symbolX) : universe.lessThanEquals(
						lowerExpression, symbolX);
			} else {
				lowerExpression = universe.number(lower);
				upperExpression = universe.number(upper);
				temp1 = strictLower ? universe.lessThan(lowerExpression,
						symbolX) : universe.lessThanEquals(lowerExpression,
						symbolX);
				temp2 = strictUpper ? universe.lessThan(symbolX,
						upperExpression) : universe.lessThanEquals(symbolX,
						upperExpression);
				resultExpression = universe.and(temp1, temp2);
				if (lower.compareTo(upper) == 0) {
					// assert !strictLower && !strictUpper;
					resultExpression = universe
							.equals(symbolX, lowerExpression);
				}
			}
			result = universe.or(result, resultExpression);
		}
		return result;
	}

	public String toString() {
		String result = "";
		int index = 0;
		int size = intervalArray.length;

		result += "{";
		if (index >= size) {
			result += "(0, 0)";
		}
		while (index < size) {
			Interval temp = intervalArray[index];
			Number lo = temp.lower();
			Number up = temp.upper();
			boolean sl = temp.strictLower();
			boolean su = temp.strictUpper();

			if (sl) {
				result += "(";
			} else {
				result += "[";
			}
			if (lo == null) {
				result += "-inf";
			} else {
				result += lo.toString();
			}
			result += ", ";
			if (up == null) {
				result += "+inf";
			} else {
				result += up.toString();
			}
			if (su) {
				result += ")";
			} else {
				result += "]";
			}
			index++;
			if (index < size) {
				result += " U ";
			}
		}
		result += "};";
		return result;
	}

	/**
	 * To check all invariants of <code>this</code> {@link IntervalUnionSet}
	 * Those invariants are:
	 * 
	 * 1. All of intervals in the array are non-<code>null</code> intervals.
	 * 
	 * 2. An empty interval cannot occur in the array.
	 * 
	 * 3. All of the intervals in the array are disjoint.
	 * 
	 * 4. The intervals in the array are ordered from least to greatest.
	 * 
	 * 5. If an interval has the form {a,+infty}, then it is open on the right.
	 * If an interval has the form {-infty,a}, then it is open on the left.
	 * 
	 * 6. If {a,b} and {b,c} are two consecutive intervals in the list, the the
	 * first one must be open on the right and the second one must be open on
	 * the left.
	 * 
	 * 7. If the range set has integer type, all of the intervals are integer
	 * intervals. If it has real type, all of the intervals are real intervals.
	 * 
	 * 8. If the range set has integer type, all of the intervals are closed,
	 * except for +infty and -infty.
	 * 
	 * @return <code>true</code> iff every {@link Interval} in <code>this</code>
	 *         set ensures invariants
	 */
	public boolean checkInvariants() {
		int index = 0;
		int size = intervalArray.length;
		Interval temp = null, previous = null;

		if (index >= size) {
			return size == 0;
		}
		while (index < size) {
			if (temp != null) {
				previous = temp;
			}
			temp = intervalArray[index];
			index++;
			// Check 1
			assert temp != null;
			// Check 2
			assert !temp.isEmpty();
			if (previous != null && temp != null) {
				// Check 3
				assert compareJoint(previous, temp) < 0;
				// check 4
				assert compareLo(previous, temp) < 0
						&& compareUp(previous, temp) < 0;
				// check 6
				if (temp.lower().compareTo(previous.upper()) == 0) {
					assert temp.strictLower() && previous.strictUpper();
				}
			}
			// check 5
			assert temp.lower() != null || temp.strictLower();
			assert temp.upper() != null || temp.strictUpper();
			// check 7
			assert isInt == temp.isIntegral();
			// check 8
			if (isInt) {
				assert temp.lower() == null || !temp.strictLower();
				assert temp.upper() == null || !temp.strictUpper();
			}
		}
		return true;
	}
	// TODO: Do a performance testing for small intervals by looping it several
	// times And put them into the "bench".
}
