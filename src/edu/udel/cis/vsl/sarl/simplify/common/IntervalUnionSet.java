package edu.udel.cis.vsl.sarl.simplify.common;

import java.util.Arrays;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory.IntervalUnion;
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
	 * The number of {@link Interval}s in this {@link IntervalUnionSet}
	 */
	private int size;

	/**
	 * An sorted array of {@link Interval}s; this {@link IntervalUnionSet} is
	 * the union of these {@link Interval}s.
	 */
	private Interval[] intervalArr;

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
	private IntervalUnionSet(boolean isIntegral, int numIntervals) {
		isInt = isIntegral;
		size = numIntervals;
		if (size > 0) {
			intervalArr = new Interval[size];
		} else {
			intervalArr = new Interval[0];
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
		size = 0;
		intervalArr = new Interval[0];
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
		size = 1;
		intervalArr = new Interval[size];
		intervalArr[0] = interval;
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
			size = 0;
			intervalArr = new Interval[size];
		} else {
			size = 1;
			intervalArr = new Interval[size];
			intervalArr[0] = interval;
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

		int inputIdx = 0, numOfInvalid = 0;
		int inputSize = intervals.length;

		while (inputIdx < inputSize) {
			Interval tmpInterval = intervals[inputIdx];

			if (tmpInterval != null && !tmpInterval.isEmpty()) {
				// To use the type of first valid interval
				isInt = intervals[inputIdx].isIntegral();
				break;
			}
			inputIdx++;
		}
		numOfInvalid = inputIdx;
		while (inputIdx < inputSize) {
			Interval tmpInterval = intervals[inputIdx];

			if (tmpInterval != null && !tmpInterval.isEmpty()) {
				// To check others have a same type with the 1st
				assert isInt == intervals[inputIdx].isIntegral();
			} else {
				numOfInvalid++;
			}
			inputIdx++;
		}
		inputSize = inputSize - numOfInvalid;

		int tmpIdx = inputSize - 1, step = 1;
		Interval[] inputArr = new Interval[inputSize];

		while (inputIdx > 0) {
			inputIdx--;

			Interval tmpInterval = intervals[inputIdx];

			if (tmpInterval != null && !tmpInterval.isEmpty()) {
				inputArr[tmpIdx] = tmpInterval;
				tmpIdx--;
			}
		}// To get the refined input intervals.
		if (inputSize < 1) {
			size = 0;
			intervalArr = new Interval[0];
			return; // As an empty set
		} else if (inputSize == 1) {
			size = 1;
			intervalArr = new Interval[size];
			intervalArr[0] = inputArr[0];
			return; // As a set with a single interval
		}
		/*
		 * To perform a iterative merge-sort on the interval array. That
		 * algorithm would combine jointed intervals, then it would leave
		 * results and a corresponding number of null-intervals.
		 */
		while (step < inputSize) {
			int mergedSize = step * 2;
			int leftArrIdx = 0;

			while (leftArrIdx + step < inputSize) {
				int rightArrIdx = leftArrIdx + step;
				Interval[] leftArr = Arrays.copyOfRange(inputArr, leftArrIdx,
						leftArrIdx + step);
				Interval[] rightArr = Arrays.copyOfRange(inputArr, rightArrIdx,
						Math.min(rightArrIdx + step, inputSize));
				Interval[] mergedArr = merge(leftArr, rightArr);

				System.arraycopy(mergedArr, 0, inputArr, leftArrIdx, Math.min(
						mergedArr.length, inputArr.length - leftArrIdx));
				leftArrIdx += mergedSize;
			}
			step = mergedSize;
		}
		while (inputSize > 0 && inputArr[inputSize - 1] == null) {
			inputSize--;
		}// To counter the number of non-null intervals.
		size = inputSize;
		intervalArr = new Interval[inputSize];
		System.arraycopy(inputArr, 0, intervalArr, 0, inputSize);
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
		isInt = other.isInt;
		size = other.intervalArr.length;
		intervalArr = new Interval[size];
		System.arraycopy(other.intervalArr, 0, intervalArr, 0, size);
	}

	/**
	 * To merge two sorted {@link Interval} arrays into one. All jointed
	 * {@link Interval}s would be combined into one, and fill corresponding
	 * position with null {@link Interval}s. Eventually, all null elements would
	 * be sorted to the tail of the whole array.
	 * 
	 * @param leftArr
	 *            The sorted non-<code>null</code> {@link Interval}s in the left
	 *            side.
	 * @param rightArr
	 *            The sorted non-<code>null</code> {@link Interval}s in the
	 *            right side has the same type(real/integer) with
	 *            <code>left</code>
	 * @return the merged {@link Interval}s
	 * 
	 */
	private Interval[] merge(Interval[] leftArr, Interval[] rightArr) {
		assert leftArr != null && rightArr != null;

		boolean isChanged = false;
		int leftSize = leftArr.length, rightSize = rightArr.length;
		int mergedSize = leftSize + rightSize;
		int leftIdx = 0, rightIdx = 0, mergedIdx = 0;
		Interval tmpInterval = null;
		Interval leftInterval = leftArr[0];
		Interval rightInterval = rightArr[0];
		Interval[] mergedArr = new Interval[mergedSize];

		if (leftInterval == null) {
			return mergedArr;
		} else if (rightInterval == null) {
			System.arraycopy(leftArr, 0, mergedArr, 0, leftSize);
			return mergedArr;
		} // If the 1st interval is null, the array is empty set

		int compareLower = compareLo(leftInterval, rightInterval);

		if (compareLower > 0) {
			tmpInterval = rightInterval;
			rightIdx++;
		} else {
			tmpInterval = leftInterval;
			leftIdx++;
		} // To find the left-most interval as the 1st interval of mergedArr
		while (isChanged || (leftIdx < leftSize && leftArr[leftIdx] != null)
				|| (rightIdx < rightSize && rightArr[rightIdx] != null)) {
			isChanged = false;
			while (leftIdx < leftSize && leftArr[leftIdx] != null) {
				Interval nxtInterval = leftArr[leftIdx];
				int compareU1L2 = compareJoint(tmpInterval, nxtInterval);

				if (compareU1L2 < 0) {
					// tmp Left Disjoint nxt, then stop
					break;
				} else {
					int compareUpper = compareUp(tmpInterval, nxtInterval);

					if (compareUpper < 0) {
						// tmp Left Intersect nxt, then combine and stop
						tmpInterval = numberFactory.newInterval(
								tmpInterval.isIntegral(), tmpInterval.lower(),
								tmpInterval.strictLower(), nxtInterval.upper(),
								nxtInterval.strictUpper());
						isChanged = true;
						leftIdx++;
						break;
					}// else tmp Left Contains nxt, then skip
					leftIdx++;
				}
			}
			while (rightIdx < rightSize && rightArr[rightIdx] != null) {
				Interval nxtInterval = rightArr[rightIdx];
				int compareU1L2 = compareJoint(tmpInterval, nxtInterval);

				if (compareU1L2 < 0) {
					// tmp Left Disjoint nxt, then stop
					break;
				} else {
					int compareRight = compareUp(tmpInterval, nxtInterval);

					if (compareRight < 0) {
						// tmp Left Intersect nxt, then combine and stop
						tmpInterval = numberFactory.newInterval(
								tmpInterval.isIntegral(), tmpInterval.lower(),
								tmpInterval.strictLower(), nxtInterval.upper(),
								nxtInterval.strictUpper());
						isChanged = true;
						rightIdx++;
						break;
					}// else tmp Left Contains nxt, then skip
					rightIdx++;
				}
			}
			if (!isChanged) {
				mergedArr[mergedIdx] = tmpInterval;
				mergedIdx++;
				if ((leftIdx < leftSize && leftArr[leftIdx] != null)
						&& (rightIdx < rightSize && rightArr[rightIdx] != null)) {
					// To find next left-most interval in both arrays
					leftInterval = leftArr[leftIdx];
					rightInterval = rightArr[rightIdx];
					compareLower = compareLo(leftInterval, rightInterval);
					if (compareLower > 0) {
						tmpInterval = rightInterval;
						rightIdx++;
					} else {
						tmpInterval = leftInterval;
						leftIdx++;
					}
				} else {
					// To concatenate the rest elements
					while (leftIdx < leftSize && leftArr[leftIdx] != null) {
						mergedArr[mergedIdx] = leftArr[leftIdx];
						mergedIdx++;
						leftIdx++;
					}
					while (rightIdx < rightSize && rightArr[rightIdx] != null) {
						mergedArr[mergedIdx] = rightArr[rightIdx];
						mergedIdx++;
						rightIdx++;
					}
				}
			}
		}
		return mergedArr;
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
	 *            type(real/integer) with <code>left</code>
	 * @return a negative integer iff they are NOT jointed, or a positive
	 *         integer iff they are jointed.
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
			if (difference.signum() > 0 && !difference.isOne()) {
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
				} else {
					return 1;
				}
			}
		}
	}

	/**
	 * Add a single number to the union set
	 * 
	 * @param number
	 *            a single non-<code>null</code> number of the same type
	 *            (integer/real) with this set
	 */
	public IntervalUnionSet addNumber(Number number) {
		assert number != null;
		assert (number instanceof IntegerNumber) == isInt;

		int leftIdx = 0;
		int rightIdx = size - 1;

		while (leftIdx <= rightIdx) {
			int midIdx = (leftIdx + rightIdx) / 2;
			int compareNumber = intervalArr[midIdx].compare(number);

			if (compareNumber > 0 && leftIdx != rightIdx) {
				rightIdx = midIdx - 1;
			} else if (compareNumber < 0 && leftIdx != rightIdx) {
				leftIdx = midIdx + 1;
			} else if (compareNumber == 0) {
				// The set contains the number.
				return new IntervalUnionSet(this);
			} else {
				int pointIdx = midIdx;
				IntervalUnionSet tempSet = new IntervalUnionSet(isInt, size + 1);

				if (compareNumber > 0) { // leftIdx == rightIdx
					// The number is on the left side.
					System.arraycopy(intervalArr, 0, tempSet.intervalArr, 0,
							pointIdx);
					tempSet.intervalArr[pointIdx] = numberFactory.newInterval(
							isInt, number, false, number, false);
					System.arraycopy(intervalArr, pointIdx,
							tempSet.intervalArr, pointIdx + 1, size - pointIdx);
				} else {
					// The number is on the right side.
					pointIdx++;
					System.arraycopy(intervalArr, 0, tempSet.intervalArr, 0,
							pointIdx);
					tempSet.intervalArr[pointIdx] = numberFactory.newInterval(
							isInt, number, false, number, false);
					System.arraycopy(intervalArr, pointIdx,
							tempSet.intervalArr, pointIdx + 1, size - pointIdx);
				}
				return tempSet;
			}
		} // Using binary searching to compare the number with intervals
			// To add a number to an empty set.
		return new IntervalUnionSet(number);
	}// TODO: Testing

	@Override
	public boolean isIntegral() {
		return isInt;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean containsNumber(Number number) {
		assert number != null;
		assert (number instanceof IntegerNumber) == isInt;

		int leftIdx = 0;
		int rightIdx = size - 1;

		while (leftIdx <= rightIdx) {
			int midIdx = (leftIdx + rightIdx) / 2;
			int compareNumber = intervalArr[midIdx].compare(number);

			if (compareNumber > 0 && leftIdx != rightIdx) {
				rightIdx = midIdx - 1;
			} else if (compareNumber < 0 && leftIdx != rightIdx) {
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
	 * 
	 * @param interval
	 * @return
	 */
	public boolean contains(Interval interval) {
		assert interval != null;
		assert interval.isIntegral() == isInt;

		int lIdx = 0;
		int rIdx = size - 1;

		if (interval.isEmpty()) {
			return true;
		}
		while (lIdx <= rIdx) {
			int mIdx = (lIdx + rIdx) / 2;
			Interval cur = intervalArr[mIdx];
			int compareInterval = numberFactory.compare(cur, interval);

			if (compareInterval % 3 == 0) {
				return true;
			} else if (lIdx == rIdx) {
				return false;
			} else {
				if (compareInterval < -3) {
					rIdx = mIdx - 1;
				} else if (compareInterval > 3) {
					lIdx = mIdx + 1;
				} else {
					return false;
				}
			}
		}
		return false;
	}// TODO: Testing

	@Override
	public boolean contains(Range set) {
		assert set != null;
		assert set.isIntegral() == isInt;

		IntervalUnionSet tar = (IntervalUnionSet) set;
		int tarSize = tar.size;
		int curIdx = 0;
		int tarIdx = 0;

		while (curIdx < size) {
			Interval curItv = intervalArr[curIdx];

			while (tarIdx < tarSize) {
				Interval tarItv = tar.intervalArr[tarIdx];
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

		IntervalUnionSet tar = (IntervalUnionSet) set;
		int tarSize = tar.size;
		int curIdx = 0;
		int tarIdx = 0;

		while (curIdx < size) {
			Interval curItv = intervalArr[curIdx];

			while (tarIdx < tarSize) {
				Interval tarItv = tar.intervalArr[tarIdx];
				int compareIterval = numberFactory.compare(curItv, tarItv);

				if (compareIterval == 4) {
					tarIdx++;
				} else {
					if (compareIterval < 4 && compareIterval > -4) {
						return true;
					}
					break;
				}
			}
			if (tarIdx >= tarSize) {
				return false;
			}
			curIdx++;
		}
		return false;
	}// TODO: Testing

	@Override
	public IntervalUnionSet union(Range set) {
		// TODO: union return new Obj for other func return new Obj in same
		// class should be same!
		assert set != null;
		assert set.isIntegral() == isInt;

		IntervalUnionSet tar = (IntervalUnionSet) set;
		int tarSize = tar.size;
		int tempSize = size + tarSize;
		Interval[] tempArr = new Interval[tempSize];
		Interval[] tarArr = tar.intervalArr;
		Interval temp = null;
		boolean isChanged = false;
		int curIdx = tempSize, numOfInvalid = 0;

		if (size <= 0) {
			IntervalUnionSet result = new IntervalUnionSet(tar.isInt, tarSize);

			System.arraycopy(tar.intervalArr, 0, result.intervalArr, 0, tarSize);
			return result;
		} else if (tarSize <= 0) {
			IntervalUnionSet result = new IntervalUnionSet(isInt, size);

			System.arraycopy(intervalArr, 0, result.intervalArr, 0, size);
			return result;
		}
		tempArr = merge(intervalArr, tarArr);
		while (curIdx > 0) {
			curIdx--;
			if (tempArr[curIdx] == null) {
				numOfInvalid++;
			}
		}
		tempSize = tempSize - numOfInvalid;

		IntervalUnionSet result = new IntervalUnionSet(tar.isInt, tempSize);

		System.arraycopy(tempArr, 0, result.intervalArr, 0, tempSize);
		return result;
	}// TODO:Testing

	@Override
	public IntervalUnionSet intersect(Range set) {
		assert set != null;
		assert set.isIntegral() == isInt;

		IntervalUnionSet tar = (IntervalUnionSet) set;
		int tarSize = tar.size;
		int curIdx = 0;
		int tarIdx = 0;
		int ctr = 0;
		int tempSize = Math.max(size, tarSize);
		Interval[] tempArr = new Interval[tempSize];
		Interval temp = null;

		if (curIdx >= size) {
			IntervalUnionSet rtn = new IntervalUnionSet(isInt, size);

			System.arraycopy(intervalArr, 0, rtn.intervalArr, 0, size);
			return rtn;
		} else if (tarIdx >= tarSize) {
			IntervalUnionSet rtn = new IntervalUnionSet(tar.isInt, tarSize);

			System.arraycopy(tar.intervalArr, 0, rtn.intervalArr, 0, tarSize);
			return rtn;
		}
		while (curIdx < size && tarIdx < tarSize) {
			while (curIdx < size) {
				temp = intervalArr[curIdx];
				while (tarIdx < tarSize) {
					Interval nxt = tar.intervalArr[tarIdx];
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

		System.arraycopy(tempArr, 0, rtn.intervalArr, 0, ctr);
		return rtn;
	}// TODO: Testing

	@Override
	public IntervalUnionSet minus(Range set) {
		assert set != null;
		assert set.isIntegral() == isInt;

		IntervalUnionSet tar = (IntervalUnionSet) set;
		int tarSize = tar.size;
		int curIdx = 0;
		int tarIdx = 0;
		int ctr = 0;
		int tempSize = Math.max(size * 2, tarSize * 2);
		Interval[] tempArr = new Interval[tempSize];
		Interval temp = null;

		if (curIdx >= size || tarIdx >= tarSize) {
			IntervalUnionSet rtn = new IntervalUnionSet(isInt, size);

			System.arraycopy(intervalArr, 0, rtn.intervalArr, 0, size);
			return rtn;
		}
		while (curIdx < size) {
			temp = intervalArr[curIdx];
			curIdx++;

			while (tarIdx < tarSize) {
				Interval nxt = tar.intervalArr[tarIdx];
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

		System.arraycopy(tempArr, 0, rtn.intervalArr, 0, ctr);
		return rtn;
	}// TODO: Testing

	@Override
	public IntervalUnionSet affineTransform(Number a, Number b) {
		assert a != null && b != null;
		assert (a instanceof IntegerNumber == b instanceof IntegerNumber) == isInt;

		int curIdx = 0;
		Interval[] tempArr = new Interval[size];
		Interval temp = null, cur = null;

		if (curIdx >= size) {
			IntervalUnionSet rtn = new IntervalUnionSet(isInt, size);

			System.arraycopy(intervalArr, 0, rtn.intervalArr, 0, size);
			return rtn;
		}
		cur = intervalArr[curIdx];
		if (cur.lower() == null && cur.upper() == null) {
			assert size == 1;

			IntervalUnionSet rtn = new IntervalUnionSet(isInt, size);

			System.arraycopy(intervalArr, 0, rtn.intervalArr, 0, size);
			return rtn;
		}
		if (a.signum() == 0) {
			return new IntervalUnionSet(numberFactory.newInterval(isInt, b,
					false, b, false));
		} else if (a.signum() > 0) {
			while (curIdx < size) {
				cur = intervalArr[curIdx];
				temp = numberFactory.affineTransform(cur, a, b);
				tempArr[curIdx] = temp;
				curIdx++;
			}
		} else if (a.signum() < 0) {
			while (curIdx < size) {
				cur = intervalArr[curIdx];
				temp = numberFactory.affineTransform(cur, a, b);
				curIdx++;
				tempArr[size - curIdx] = temp;

			}
		}
		IntervalUnionSet rtn = new IntervalUnionSet(isInt, size);

		System.arraycopy(tempArr, 0, rtn.intervalArr, 0, size);
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

		if (curIdx >= size) {
			return rtn;
		}
		while (curIdx < size) {
			curItv = intervalArr[curIdx];
			curIdx++;

			boolean sl = curItv.strictLower();
			boolean su = curItv.strictUpper();
			BooleanExpression tmpE, tmpE2, resE;
			// TODO: create var to store val reduce func call
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
		String rtn = "";
		int curIdx = 0;

		rtn += "{";
		if (curIdx >= size) {
			rtn += "(0, 0)";
		}
		while (curIdx < size) {
			Interval temp = intervalArr[curIdx];
			Number lo = temp.lower();
			Number up = temp.upper();
			boolean sl = temp.strictLower();
			boolean su = temp.strictUpper();

			if (sl) {
				rtn += "(";
			} else {
				rtn += "[";
			}
			if (lo == null) {
				rtn += "-inf";
			} else {
				rtn += lo.toString();
			}
			rtn += ", ";
			if (up == null) {
				rtn += "+inf";
			} else {
				rtn += up.toString();
			}
			if (su) {
				rtn += ")";
			} else {
				rtn += "]";
			}
			curIdx++;
			if (curIdx < size) {
				rtn += " U ";
			}
		}
		rtn += "};";
		return rtn;
	}// TODO: Testing

	public boolean checkInvariants() {
		// TODO : Test
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
		int curIdx = 0;
		Interval tmpItv = null, prevItv = null;

		if (curIdx >= size) {
			return intervalArr == null;
		}
		while (curIdx < size) {
			if (tmpItv != null) {
				prevItv = tmpItv;
			}
			tmpItv = intervalArr[curIdx];
			curIdx++;
			// Check 1
			if (tmpItv == null) {
				return false;
			}
			// Check 2
			if (tmpItv.lower().compareTo(tmpItv.upper()) == 0
					&& (tmpItv.strictLower() || tmpItv.strictUpper())) {
				return false;
			}
			// Check 3
			int compareInterval = numberFactory.compare(prevItv, tmpItv);
			if (compareInterval < 4 && compareInterval > -4) {
				return false;
			}
			// check 4
			if (numberFactory.compare(prevItv, tmpItv) > 0) {
				return false;
			}
			// check 5
			if (tmpItv.lower() == null && !tmpItv.strictLower()) {
				return false;
			}
			if (tmpItv.upper() == null && !tmpItv.strictUpper()) {
				return false;
			}
			// check 6
			if (tmpItv.lower().compareTo(prevItv.upper()) == 0) {
				if (!(tmpItv.strictLower() && prevItv.strictUpper())) {
					return false;
				}
			}
			// check 7
			if (isIntegral() ? tmpItv.isReal() : tmpItv.isIntegral()) {
				return false;
			}
			// check 8
			if (isIntegral()) {
				if (tmpItv.lower() != null && tmpItv.strictLower()) {
					return false;
				}
				if (tmpItv.upper() != null && tmpItv.strictUpper()) {
					return false;
				}
			}
		}
		return true;
	}
}
