package edu.udel.cis.vsl.sarl.simplify.common;

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
	 * @param numIntervals
	 *            A natural number to represent the number of disjointed
	 *            {@link Interval}s in <code>this</code>
	 *            {@link IntervalUnionSet}. It would be 0, iff <code>this</code>
	 *            set is empty.
	 */
	private IntervalUnionSet(boolean isIntegral, int size) {
		isInt = isIntegral;
		if (size > 0) {
			intervalArray = new Interval[size];
		} else {
			intervalArray = new Interval[0];
		}
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

		Interval interval = numberFactory.newInterval(
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
		isInt = interval.isIntegral();
		if (interval.isEmpty()) {
			intervalArray = new Interval[0];
		} else {
			intervalArray = new Interval[1];
			intervalArray[0] = interval;
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

			if (temp != null && !temp.isEmpty()) {
				// To use the type of first valid interval
				isInt = temp.isIntegral();
				list.add(temp);
				inputIndex++;
				break;
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
	 * Constructs an {@link IntervalnionSet} being same with <code>other</code>.
	 * 
	 * @param other
	 *            A non-<code>null</code> {@link IntervalnionSet} would be
	 *            copied.
	 */
	public IntervalUnionSet(IntervalUnionSet other) {
		assert other != null;

		int size = other.intervalArray.length;

		isInt = other.isInt;
		intervalArray = new Interval[size];
		System.arraycopy(other.intervalArray, 0, intervalArray, 0, size);
	}

	private void addInterval(ArrayList<Interval> list, Interval interval) {
		assert isInt == interval.isIntegral();

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

		if (lower == null && upper == null) {
			list.clear();
			list.add(interval);
		} else if (lower == null) {
			start = -1;
		} else if (upper == null) {
			end = size;
		}
		while (left < right && start == -2) {
			int mid = (left + right) / 2;
			int compare = list.get(mid).compare(lower);

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
			} else if (right == left) {
				int compareJoint = 0;
				int compare = list.get(right).compare(lower);

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
		assert start != -2;
		if (start == size) {
			list.add(interval);
			return;
		}
		left = 0;
		right = size - 1;
		while (left < right && end == -2) {
			int mid = (left + right) / 2;
			int compare = list.get(mid).compare(upper);

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
			} else if (right == left) {
				int compareJoint = 0;
				int compare = list.get(right).compare(upper);

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
		assert end != -2;
		if (noIntersection) {
			assert start >= end;
			start = end == -1 ? 0 : start;
			list.add(start, interval);
		} else {
			start = start < 0 ? 0 : start;
			end = end < size ? end : (size - 1);
			list.subList(start, end + 1).clear();
			list.add(start, numberFactory.newInterval(isInt, lower,
					strictLower, upper, strictUpper));

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
		assert current != null && target != null;
		assert current.isIntegral() == target.isIntegral();

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
		assert current != null && target != null;
		assert current.isIntegral() == target.isIntegral();

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
		assert left != null && right != null;
		assert left.isIntegral() == right.isIntegral();

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
		int leftIdx = 0;
		int rightIdx = size - 1;

		while (leftIdx <= rightIdx) {
			int midIdx = (leftIdx + rightIdx) / 2;
			int compareNumber = intervalArray[midIdx].compare(number);

			if (compareNumber > 0 && leftIdx != rightIdx) {
				// The number is on the left part of the current set.
				rightIdx = midIdx - 1;
			} else if (compareNumber < 0 && leftIdx != rightIdx) {
				// The number is on the right part of the current set.
				leftIdx = midIdx + 1;
			} else if (compareNumber == 0) {
				// The set contains the number.
				return new IntervalUnionSet(this);
			} else {
				// The set does NOT contain the number
				leftIdx = compareNumber < 0 ? midIdx : midIdx - 1;
				rightIdx = leftIdx + 1;
				leftIdx = Math.max(leftIdx, 0);
				rightIdx = Math.min(rightIdx, size - 1);

				boolean leftSl = intervalArray[leftIdx].strictLower();
				boolean rightSu = intervalArray[rightIdx].strictUpper();
				Number leftLo = intervalArray[leftIdx].lower();
				Number leftUp = intervalArray[leftIdx].upper();
				Number rightLo = intervalArray[rightIdx].lower();
				Number rightUp = intervalArray[rightIdx].upper();
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
							leftIdx);
					result.intervalArray[leftIdx] = numberFactory.newInterval(
							isInt, leftLo, leftSl, rightUp, rightSu);
					System.arraycopy(intervalArray, rightIdx + 1,
							result.intervalArray, rightIdx, size - rightIdx - 1);
					return result;
				} else if (leftJoint) {
					// The number changes an interval's lower condition
					IntervalUnionSet result = new IntervalUnionSet(this);

					if (isInt) {
						result.intervalArray[leftIdx] = numberFactory
								.newInterval(true, leftLo, false, number, false);
					} else {
						result.intervalArray[leftIdx] = numberFactory
								.newInterval(false, leftLo, leftSl, number,
										false);
					}
					return result;
				} else if (rightJoint) {
					// The number changes an interval's upper condition
					IntervalUnionSet result = new IntervalUnionSet(this);

					if (isInt) {
						result.intervalArray[rightIdx] = numberFactory
								.newInterval(true, number, false, rightUp,
										false);
					} else {
						result.intervalArray[rightIdx] = numberFactory
								.newInterval(false, number, false, rightUp,
										rightSu);
					}
					return result;
				} else {
					// The number becomes a new point interval
					IntervalUnionSet result = new IntervalUnionSet(isInt,
							size + 1);

					if (leftIdx == rightIdx) {
						if (leftIdx == 0) {
							// To add the number to the head
							result.intervalArray[0] = numberFactory
									.newInterval(isInt, number, false, number,
											false);
							System.arraycopy(intervalArray, 0,
									result.intervalArray, 1, size);
						} else {
							// To add the number to the tail
							result.intervalArray[size] = numberFactory
									.newInterval(isInt, number, false, number,
											false);
							System.arraycopy(intervalArray, 0,
									result.intervalArray, 0, size);
						}
					} else {
						// To insert the number to the body
						System.arraycopy(intervalArray, 0,
								result.intervalArray, 0, rightIdx);
						result.intervalArray[rightIdx] = numberFactory
								.newInterval(isInt, number, false, number,
										false);
						System.arraycopy(intervalArray, rightIdx,
								result.intervalArray, rightIdx + 1, size
										- rightIdx);
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

	/**
	 * Does this {@link IntervalUnionSet} contain the given {@link Interval} as
	 * a member?
	 * 
	 * @param interval
	 *            any non-<code>null</code> {@link Interval} of the same type
	 *            (integer/real) as this {@link IntervalUnionSet}
	 * @return <code>true</code> iff this {@link IntervalUnionSet} contains the
	 *         given {@link Interval}
	 */
	public boolean contains(Interval interval) {
		assert interval != null;
		assert interval.isIntegral() == isInt;

		int size = intervalArray.length;
		int leftIdx = 0;
		int rightIdx = size - 1;

		if (interval.isEmpty()) {
			return true;
		}// Any sets would contain an empty set
		while (leftIdx <= rightIdx) {
			int midIdx = (leftIdx + rightIdx) / 2;
			Interval midInterval = intervalArray[midIdx];
			int compareLo = compareLo(midInterval, interval);
			int compareUp = compareUp(midInterval, interval);

			if (compareLo > 0) {
				if (compareUp > 0) {
					int compareJoint = compareJoint(interval, midInterval);

					if (compareJoint <= 0) { // No intersection
						rightIdx = midIdx - 1;
						continue;
					}
				}
				return false;
			} else if (compareLo < 0) {
				if (compareUp < 0) {
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
				if (compareUp < 0) {
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean contains(Range set) {
		assert set != null;
		assert set.isIntegral() == isInt;

		IntervalUnionSet tar = (IntervalUnionSet) set;
		int size = intervalArray.length;
		int tarSize = tar.intervalArray.length;
		int curIdx = 0;
		int tarIdx = 0;

		while (curIdx < size) {
			Interval curItv = intervalArray[curIdx];

			while (tarIdx < tarSize) {
				Interval tarItv = tar.intervalArray[tarIdx];
				int compareIterval = numberFactory.compare(curItv, tarItv);

				if (compareIterval % 3 == 0) {
					tarIdx++;
				} else {
					if (curIdx >= size) {
						return false;
					}
					break;
				}
			}
			if (tarIdx >= tarSize) {
				// The target set may be empty.
				return true;
			}
			curIdx++;
		}
		// This set may be empty.
		return size == 0 && tarSize == 0;
	}// TODO:Testing

	@Override
	public boolean intersects(Range set) {
		assert set != null;
		assert set.isIntegral() == isInt;

		IntervalUnionSet other = (IntervalUnionSet) set;
		int size = intervalArray.length;
		int otherSize = other.intervalArray.length;
		int thisIdx = 0;
		int otherIdx = 0;

		if (size == 0 || otherSize == 0) {
			return false;
		}// An empty set could not intersects with any sets.
		while (thisIdx < size && otherIdx < otherSize) {
			Interval thisInterval = intervalArray[thisIdx];
			Interval otherInterval = other.intervalArray[otherIdx];
			int compareLo = compareLo(otherInterval, thisInterval);
			int compareUp = compareUp(otherInterval, thisInterval);

			if (compareLo > 0) {
				if (compareUp > 0) {
					int compareJoint = compareJoint(thisInterval, otherInterval);

					if (compareJoint <= 0) { // No intersection
						thisIdx++;
						break;
					}
				}
				return true;
			} else if (compareLo < 0) {
				if (compareUp < 0) {
					int compareJoint = compareJoint(otherInterval, thisInterval);

					if (compareJoint <= 0) { // No intersection
						otherIdx++;
						continue;
					}
				}
				return true;
			} else {
				return true;
			}
		}
		return false;
	}// TODO: Testing

	@Override
	public IntervalUnionSet union(Range set) {
		assert set != null;
		assert set.isIntegral() == isInt;

		IntervalUnionSet rightSet = (IntervalUnionSet) set;
		Interval[] leftArray = intervalArray;
		Interval[] rightArray = rightSet.intervalArray;
		int leftSize = leftArray.length;
		int rightSize = rightArray.length;

		if (leftSize <= 0 && rightSize <= 0) {
			return new IntervalUnionSet(isInt);
		} else if (leftSize <= 0) {
			return new IntervalUnionSet(rightSet);
		} else if (rightSize <= 0) {
			return new IntervalUnionSet(this);
		} else if (leftArray[0].isUniversal()) {
			return new IntervalUnionSet(this);
		} else if (rightArray[0].isUniversal()) {
			return new IntervalUnionSet(rightSet);
		}

		int leftIndex = 0, rightIndex = 0;
		boolean isChanged = false;
		Interval left = leftArray[0];
		Interval right = rightArray[0];
		Interval temp = null;
		ArrayList<Interval> list = new ArrayList<Interval>();
		int compareLeft = compareLo(left, right);

		if (compareLeft > 0) {
			temp = right;
			rightIndex++;
		} else {
			temp = left;
			leftIndex++;
		}// To find the left-most interval in two sets.
		while (isChanged || leftIndex < leftSize || rightIndex < rightSize) {
			isChanged = false;
			while (leftIndex < leftSize) {
				Interval next = leftArray[leftIndex];
				int compareTempNext = compareJoint(temp, next);

				if (compareTempNext < 0) {
					// temp Left-disjoint next, then stop
					break;
				} else {
					int compareRight = compareUp(temp, next);

					if (compareRight < 0) {
						temp = numberFactory.newInterval(isInt, temp.lower(),
								temp.strictLower(), next.upper(),
								next.strictUpper());
						isChanged = true;
						leftIndex++;
						break;
					}// else temp Contains next, then skip
					leftIndex++;
				}
			}
			while (rightIndex < rightSize) {
				Interval next = rightArray[rightIndex];
				int compareTempNext = compareJoint(temp, next);

				if (compareTempNext < 0) {
					// Temp Left-disjoint next. then stop
					break;
				} else {
					int compareRight = compareUp(temp, next);

					if (compareRight < 0) {
						temp = numberFactory.newInterval(isInt, temp.lower(),
								temp.strictLower(), next.upper(),
								next.strictUpper());
						isChanged = true;
						rightIndex++;
						break;
					}// else temp Contains next, then skip
					rightIndex++;
				}
			}
			if (!isChanged) {
				list.add(temp);
				if (leftIndex < leftSize && rightIndex < rightSize) {
					left = leftArray[leftIndex];
					right = rightArray[rightIndex];
					compareLeft = compareLo(left, right);
					if (compareLeft > 0) {
						temp = right;
						rightIndex++;
					} else {
						temp = left;
						leftIndex++;
					}
				} else {
					// To concatenate
					while (leftIndex < leftSize) {
						list.add(leftArray[leftIndex]);
						leftIndex++;
					}
					while (rightIndex < rightSize) {
						list.add(rightArray[rightIndex]);
						rightIndex++;
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

		IntervalUnionSet tar = (IntervalUnionSet) set;
		int size = intervalArray.length;
		int tarSize = tar.intervalArray.length;
		int curIdx = 0;
		int tarIdx = 0;
		int ctr = 0;
		int tempSize = Math.max(size, tarSize);
		Interval[] tempArr = new Interval[tempSize];
		Interval temp = null;

		if (curIdx >= size) {
			IntervalUnionSet rtn = new IntervalUnionSet(isInt, size);

			System.arraycopy(intervalArray, 0, rtn.intervalArray, 0, size);
			return rtn;
		} else if (tarIdx >= tarSize) {
			IntervalUnionSet rtn = new IntervalUnionSet(tar.isInt, tarSize);

			System.arraycopy(tar.intervalArray, 0, rtn.intervalArray, 0,
					tarSize);
			return rtn;
		}
		while (curIdx < size && tarIdx < tarSize) {
			while (curIdx < size) {
				temp = intervalArray[curIdx];
				while (tarIdx < tarSize) {
					Interval nxt = tar.intervalArray[tarIdx];
					int compareIterval = numberFactory.compare(temp, nxt);

					if (compareIterval == -4) {
						if (tarIdx > 0) {
							tarIdx--;
						}
						break;
					} else if (compareIterval == 4) {
						tarIdx++;
						continue;
					} else {
						Interval newItv = numberFactory.intersection(temp, nxt);

						tempArr[ctr] = newItv;
						ctr++;
						tarIdx++;
					}
				}
				curIdx++;
			}
		}

		IntervalUnionSet rtn = new IntervalUnionSet(tar.isInt, ctr);

		System.arraycopy(tempArr, 0, rtn.intervalArray, 0, ctr);
		return rtn;
	}// TODO: Testing

	@Override
	public IntervalUnionSet minus(Range set) {
		assert set != null;
		assert set.isIntegral() == isInt;

		IntervalUnionSet tar = (IntervalUnionSet) set;
		int size = intervalArray.length;
		int tarSize = tar.intervalArray.length;
		int curIdx = 0;
		int tarIdx = 0;
		int ctr = 0;
		int tempSize = Math.max(size * 2, tarSize * 2);
		Interval[] tempArr = new Interval[tempSize];
		Interval temp = null;

		if (curIdx >= size || tarIdx >= tarSize) {
			IntervalUnionSet rtn = new IntervalUnionSet(isInt, size);

			System.arraycopy(intervalArray, 0, rtn.intervalArray, 0, size);
			return rtn;
		}
		while (curIdx < size) {
			temp = intervalArray[curIdx];
			curIdx++;

			while (tarIdx < tarSize) {
				Interval nxt = tar.intervalArray[tarIdx];
				tarIdx++;
				int compareIterval = numberFactory.compare(temp, nxt);

				if (compareIterval == -4) {
					tempArr[ctr] = temp;
					ctr++;
					tarIdx--;
					if (tarIdx > 0) {
						tarIdx--;
					}
					break;
				} else if (compareIterval == 4) {
					if (tarIdx < tarSize) {
						continue;
					} else {
						tempArr[ctr] = temp;
						ctr++;
					}
				} else {
					Interval dummy = numberFactory.intersection(temp, nxt);
					int cmpLo = 0;
					int cmpUp = 0;

					if (dummy.lower() == null) {
						cmpLo = -1;
					} else if (temp.lower() == null) {
						cmpLo = 1;
					} else {
						cmpLo = dummy.lower().compareTo(temp.lower());
					}
					if (cmpLo > 0) {
						Interval newItv = numberFactory.newInterval(
								temp.isIntegral(), temp.lower(),
								temp.strictLower(), dummy.lower(),
								!dummy.strictUpper());

						tempArr[ctr] = newItv;
						ctr++;
					} else if (cmpLo == 0) {
						if (!temp.strictLower() && dummy.strictLower()) {
							Interval newItv = numberFactory.newInterval(
									temp.isIntegral(), temp.lower(),
									temp.strictLower(), dummy.lower(),
									!dummy.strictUpper());

							tempArr[ctr] = newItv;
							ctr++;
						}
					}
					if (nxt.upper() == null) {
						cmpUp = 1;
					} else if (temp.upper() == null) {
						cmpUp = -1;
					} else {
						cmpUp = nxt.upper().compareTo(temp.upper());
					}
					temp = numberFactory.newInterval(temp.isIntegral(),
							dummy.upper(), !dummy.strictUpper(), temp.upper(),
							temp.strictUpper());
					if (cmpUp > 0) {
						break;
					} else if (cmpUp == 0
							&& ((!nxt.strictUpper()) || temp.strictUpper())) {
						break;
					} else {
						if (tarIdx >= tarSize) {
							tempArr[ctr] = temp;
							ctr++;
						}
					}
				}
			}
		}

		IntervalUnionSet rtn = new IntervalUnionSet(tar.isInt, ctr);

		System.arraycopy(tempArr, 0, rtn.intervalArray, 0, ctr);
		return rtn;
	}// TODO: Testing

	@Override
	public IntervalUnionSet affineTransform(Number a, Number b) {
		assert a != null && b != null;
		assert (a instanceof IntegerNumber == b instanceof IntegerNumber) == isInt;

		int curIdx = 0;
		int size = intervalArray.length;
		Interval[] tempArr = new Interval[size];
		Interval temp = null, cur = null;

		if (curIdx >= size) {
			IntervalUnionSet rtn = new IntervalUnionSet(isInt, size);

			System.arraycopy(intervalArray, 0, rtn.intervalArray, 0, size);
			return rtn;
		}
		cur = intervalArray[curIdx];
		if (cur.lower() == null && cur.upper() == null) {
			assert size == 1;

			IntervalUnionSet rtn = new IntervalUnionSet(isInt, size);

			System.arraycopy(intervalArray, 0, rtn.intervalArray, 0, size);
			return rtn;
		}
		if (a.signum() == 0) {
			return new IntervalUnionSet(numberFactory.newInterval(isInt, b,
					false, b, false));
		} else if (a.signum() > 0) {
			while (curIdx < size) {
				cur = intervalArray[curIdx];
				temp = numberFactory.affineTransform(cur, a, b);
				tempArr[curIdx] = temp;
				curIdx++;
			}
		} else if (a.signum() < 0) {
			while (curIdx < size) {
				cur = intervalArray[curIdx];
				temp = numberFactory.affineTransform(cur, a, b);
				curIdx++;
				tempArr[size - curIdx] = temp;

			}
		}
		IntervalUnionSet rtn = new IntervalUnionSet(isInt, size);

		System.arraycopy(tempArr, 0, rtn.intervalArray, 0, size);
		return rtn;
	}// TODO: Testing

	@Override
	public IntervalUnionSet complement() {
		Interval univ = numberFactory.newInterval(isIntegral(), null, true,
				null, true);
		IntervalUnionSet univSet = new IntervalUnionSet(univ);
		IntervalUnionSet rtn = (IntervalUnionSet) univSet.minus(this);

		return rtn;
	}

	@Override
	public BooleanExpression symbolicRepresentation(SymbolicConstant x,
			PreUniverse universe) {
		if (x == null) {
			throw new NullPointerException(
					"[Error] The SymbolicConstant x cannot be null.");
		} else if (universe == null) {
			throw new NullPointerException(
					"[Error] The PreUniverse universe cannot be null.");
		}

		BooleanExpression rtn = universe.bool(false);
		NumericExpression loE = null, upE = null, xE = (NumericExpression) x;
		Interval curItv = null;
		int curIdx = 0;
		int size = intervalArray.length;

		if (curIdx >= size) {
			return rtn;
		}
		while (curIdx < size) {
			curItv = intervalArray[curIdx];
			curIdx++;

			boolean sl = curItv.strictLower();
			boolean su = curItv.strictUpper();
			BooleanExpression tmpE, tmpE2, resE;
			// use "==" isted of cmpTo

			if (curItv.lower() == null && curItv.upper() == null) {
				rtn = universe.bool(true);
				return rtn;
			} else if (curItv.lower() == null) {
				upE = universe.number(curItv.upper());
				resE = su ? universe.lessThan(xE, upE) : universe
						.lessThanEquals(xE, upE);
			} else if (curItv.upper() == null) {
				loE = universe.number(curItv.lower());
				resE = sl ? universe.lessThan(loE, xE) : universe
						.lessThanEquals(loE, xE);
			} else {
				loE = universe.number(curItv.lower());
				upE = universe.number(curItv.upper());
				tmpE = sl ? universe.lessThan(loE, xE) : universe
						.lessThanEquals(loE, xE);
				tmpE2 = su ? universe.lessThan(xE, upE) : universe
						.lessThanEquals(xE, upE);
				resE = universe.and(tmpE, tmpE2);
				if (curItv.lower().compareTo(curItv.upper()) == 0
						&& (!sl && !su)) {
					resE = universe.equals(xE, loE);
				}
			}
			rtn = universe.or(rtn, resE);
		}
		return rtn;
	}// TODO: Testing

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

	public boolean checkInvariants() {
		/*
		 * 1. All of intervals in the array are non-<code>null</code> intervals.
		 * 
		 * 2. An empty interval cannot occur in the array.
		 * 
		 * 3. All of the intervals in the array are disjoint.
		 * 
		 * 4. The intervals in the array are ordered from least to greatest.
		 * 
		 * 5. If an interval has the form {a,+infty}, then it is open on the
		 * right. If an interval has the form {-infty,a}, then it is open on the
		 * left.
		 * 
		 * 6. If {a,b} and {b,c} are two consecutive intervals in the list, the
		 * the first one must be open on the right and the second one must be
		 * open on the left.
		 * 
		 * 7. If the range set has integer type, all of the intervals are
		 * integer intervals. If it has real type, all of the intervals are real
		 * intervals.
		 * 
		 * 8. If the range set has integer type, all of the intervals are
		 * closed, except for +infty and -infty.
		 */
		int index = 0;
		int size = intervalArray.length;
		Interval temp = null, prior = null;

		if (index >= size) {
			return intervalArray == null;
		}
		while (index < size) {
			if (temp != null) {
				prior = temp;
			}
			temp = intervalArray[index];
			index++;
			// Check 1
			if (temp == null) {
				return false;
			}
			// Check 2
			if (temp.lower().compareTo(temp.upper()) == 0
					&& (temp.strictLower() || temp.strictUpper())) {
				return false;
			}
			// Check 3
			int compare = numberFactory.compare(prior, temp);
			if (compare < 4 && compare > -4) {
				return false;
			}
			// check 4
			if (numberFactory.compare(prior, temp) > 0) {
				return false;
			}
			// check 5
			if (temp.lower() == null && !temp.strictLower()) {
				return false;
			}
			if (temp.upper() == null && !temp.strictUpper()) {
				return false;
			}
			// check 6
			if (temp.lower().compareTo(prior.upper()) == 0) {
				if (!(temp.strictLower() && prior.strictUpper())) {
					return false;
				}
			}
			// check 7
			if (isInt ? temp.isReal() : temp.isIntegral()) {
				return false;
			}
			// check 8
			if (isIntegral()) {
				if (temp.lower() != null && temp.strictLower()) {
					return false;
				}
				if (temp.upper() != null && temp.strictUpper()) {
					return false;
				}
			}
		}
		return true;
	}
}
