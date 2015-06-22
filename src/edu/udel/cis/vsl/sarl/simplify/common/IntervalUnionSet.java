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
	 * integral or not: <code>true</code> - it is integral, <code>false</code> -
	 * it is rational.
	 */
	private boolean isInt;

	/**
	 * The number of {@link Interval}s in this {@link IntervalUnionSet} ensuring
	 * specified invariants of it.
	 */
	private int size;

	/**
	 * An sorted array of intervals; the value set is the union of these
	 * intervals.
	 */
	private Interval[] intervalArr;

	/**
	 * Constructs an {@link IntervalUnionSet} with defined type and size.
	 * 
	 * @param isIntegral
	 *            A boolean value to represent whether the set is integral.
	 * @param numIntervals
	 *            A natural number to represent the number of disjointed
	 *            intervals in the set. It would be 0, iff the set is empty.
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
	 * Constructs an interval union set representing an empty set
	 * 
	 * @param isIntegral
	 *            A boolean value to represent whether the set is integral.
	 */
	public IntervalUnionSet(boolean isIntegral) {
		isInt = isIntegral;
		size = 0;
		intervalArr = new Interval[0];
	}

	/**
	 * Constructs an interval set with exactly one interval containing exactly
	 * one number
	 * 
	 * @param number
	 *            a non-<code>null</code> number
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
	 * Constructs an interval union set with exactly one interval.
	 * 
	 * @param interval
	 *            a non-<code>null</code> interval.
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
	 * Constructs an interval union set with an ordered array of intervals.
	 * 
	 * @param intervals
	 *            an array of intervals (with at least one interval) with same
	 *            type (real/integer).
	 */
	public IntervalUnionSet(Interval... intervals) {
		assert intervals != null;
		assert intervals.length > 0;

		int inputIdx = 0, numOfInvalid = 0;
		int inputSize = intervals.length;

		while (inputIdx < inputSize) {
			Interval temp = intervals[inputIdx];

			if (temp != null && !temp.isEmpty()) {
				isInt = intervals[inputIdx].isIntegral();
				break;
			}
			inputIdx++;
		}
		numOfInvalid = inputIdx;
		while (inputIdx < inputSize) {
			Interval temp = intervals[inputIdx];

			if (temp != null && !temp.isEmpty()) {
				assert isInt == intervals[inputIdx].isIntegral();
			} else {
				numOfInvalid++;
			}
			inputIdx++;
		}
		inputSize = inputSize - numOfInvalid;

		int curIdx = inputSize - 1, step = 1;
		Interval[] inputArr = new Interval[inputSize];

		while (inputIdx > 0) {
			inputIdx--;

			Interval temp = intervals[inputIdx];

			if (temp != null && !temp.isEmpty()) {
				inputArr[curIdx] = temp;
				curIdx--;
			}
		}
		if (inputSize < 1) {
			size = 0;
			intervalArr = new Interval[0];
			return;
		} else if (inputSize == 1) {
			size = 1;
			intervalArr = new Interval[size];
			intervalArr[0] = intervals[0];
			return;
		}
		while (step < inputSize) {
			int tempSize = step * 2;
			int tempLeftIdx = 0;

			while (tempLeftIdx + step < inputSize) {
				int tempRightIdx = tempLeftIdx + step;
				Interval[] leftArr = Arrays.copyOfRange(inputArr, tempLeftIdx,
						tempLeftIdx + step);
				Interval[] rightArr = Arrays.copyOfRange(inputArr,
						tempRightIdx, Math.min(tempRightIdx + step, inputSize));
				Interval[] tempArr = merge(leftArr, rightArr);

				System.arraycopy(tempArr, 0, inputArr, tempLeftIdx,
						Math.min(tempArr.length, inputArr.length - tempLeftIdx));
				tempLeftIdx += tempSize;
			}
			step = tempSize;
		}
		while (inputSize > 0 && inputArr[inputSize - 1] == null) {
			inputSize--;
		}
		size = inputSize;
		intervalArr = new Interval[inputSize];
		System.arraycopy(inputArr, 0, intervalArr, 0, inputSize);
	}

	/**
	 * Constructs an interval union set being same with other one.
	 * 
	 * @param other
	 */
	public IntervalUnionSet(IntervalUnionSet other) {
		isInt = other.isInt;
		size = other.intervalArr.length;
		intervalArr = new Interval[size];
		System.arraycopy(other.intervalArr, 0, intervalArr, 0, size);
	}

	/**
	 * Add a single number to the union set
	 * 
	 * @param number
	 *            a single non-<code>null</code> number of the same type
	 *            (integer/real) as this set
	 */
	public IntervalUnionSet addNumber(Number number) {
		// TODO: Optimize (use full word instead of abbreviation)
		assert number != null;
		assert (number instanceof IntegerNumber) == isInt;

		Interval pointInterval = numberFactory.newInterval(
				number instanceof IntegerNumber, number, false, number, false);
		IntervalUnionSet pointSet = new IntervalUnionSet(pointInterval);
		IntervalUnionSet result = union(pointSet);

		return result;
	}// TODO: Testing

	@Override
	public boolean isIntegral() {
		return isInt;
	}// TODO: Testing

	@Override
	public boolean isEmpty() {
		return size == 0;
	}// TODO: Testing

	@Override
	public boolean containsNumber(Number number) {
		// TODO: Optimize : search the number directly.
		assert number != null;
		assert (number instanceof IntegerNumber) == isInt;

		int lIdx = 0;
		int rIdx = size - 1;

		while (lIdx <= rIdx) {
			int mIdx = (lIdx + rIdx) / 2;
			Interval cur = intervalArr[mIdx];
			boolean doesContain = cur.contains(number);

			if (doesContain || lIdx == rIdx) {
				return doesContain;
			} else {
				if (number.compareTo(cur.lower()) < 0) {
					rIdx = mIdx - 1;
				} else {
					lIdx = mIdx + 1;
				}
			}
		}
		return false;
	}// TODO: Testing

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
			curIdx --;
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
			//TODO: create var to store val reduce func call
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

	private Interval[] merge(Interval[] left, Interval[] right) {
		if (left == null) {
			return right;
		} else if (right == null) {
			return left;
		}

		boolean isChanged = false;
		int leftSize = left.length, rightSize = right.length;
		int tempSize = leftSize + rightSize;
		int leftIdx = 0, rightIdx = 0, curIdx = 0;
		Interval interval1 = null;
		Interval leftInterval = left[0];
		Interval rightInterval = right[0];
		Interval[] merged = new Interval[tempSize];

		if (leftInterval == null) {
			return merged;
		} else if (rightInterval == null) {
			System.arraycopy(left, 0, merged, 0, leftSize);
			return merged;
		}

		int compareLeft = compareLo(leftInterval, rightInterval);

		if (compareLeft > 0) {
			interval1 = rightInterval;
			rightIdx++;
		} else {
			interval1 = leftInterval;
			leftIdx++;
		}
		while (isChanged || (leftIdx < leftSize && left[leftIdx] != null)
				|| (rightIdx < rightSize && right[rightIdx] != null)) {
			isChanged = false;
			while (leftIdx < leftSize && left[leftIdx] != null) {
				Interval interval2 = left[leftIdx];
				int compareU1L2 = compareJoint(interval1, interval2);

				if (compareU1L2 < 0) {
					// i1 Left Disjoint i2
					break;
				} else {
					// i1 Left Jointed i2
					int compareRight = compareUp(interval1, interval2);

					if (compareRight < 0) {
						// i1 Left Intersect i2
						interval1 = numberFactory.newInterval(
								interval1.isIntegral(), interval1.lower(),
								interval1.strictLower(), interval2.upper(),
								interval2.strictUpper());
						isChanged = true;
						leftIdx++;
						break;
					}// else Left i1 contains i2
					leftIdx++;
				}
			}
			while (rightIdx < rightSize && right[rightIdx] != null) {
				Interval interval2 = right[rightIdx];
				int compareU1L2 = compareJoint(interval1, interval2);

				if (compareU1L2 < 0) {
					// i1 Left Disjoint i2
					break;
				} else {
					// i1 Left Jointed i2
					int compareRight = compareUp(interval1, interval2);

					if (compareRight < 0) {
						// i1 Left Intersect i2
						interval1 = numberFactory.newInterval(
								interval1.isIntegral(), interval1.lower(),
								interval1.strictLower(), interval2.upper(),
								interval2.strictUpper());
						isChanged = true;
						rightIdx++;
						break;
					}// else Left i1 contains i2
					rightIdx++;
				}
			}
			if (!isChanged) {
				merged[curIdx] = interval1;
				curIdx++;
				if ((leftIdx < leftSize && left[leftIdx] != null)
						&& (rightIdx < rightSize && right[rightIdx] != null)) {
					leftInterval = left[leftIdx];
					rightInterval = right[rightIdx];
					compareLeft = compareLo(leftInterval, rightInterval);
					if (compareLeft > 0) {
						interval1 = rightInterval;
						rightIdx++;
					} else {
						interval1 = leftInterval;
						leftIdx++;
					}
				} else {
					while (leftIdx < leftSize && left[leftIdx] != null) {
						merged[curIdx] = left[leftIdx];
						curIdx++;
						leftIdx++;
					}
					while (rightIdx < rightSize && right[rightIdx] != null) {
						merged[curIdx] = right[rightIdx];
						curIdx++;
						rightIdx++;
					}
				}
			}
		}
		return merged;
	}

	private int compareLo(Interval current, Interval target) {
		assert current != null && target != null;
		assert current.isIntegral() == target.isIntegral();

		boolean currentSL = current.strictLower();
		boolean targetSL = target.strictLower();
		Number currentLo = current.lower();
		Number targetLo = target.lower();

		if (currentLo == null && targetLo == null) {
			return 0;
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

	private int compareUp(Interval current, Interval target) {
		assert current != null && target != null;
		assert current.isIntegral() == target.isIntegral();

		boolean currentSU = current.strictUpper();
		boolean targetSU = target.strictUpper();
		Number currentUp = current.upper();
		Number targetUp = target.upper();

		if (currentUp == null && targetUp == null) {
			return 0;
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
					return -1;
				} else {
					return 1;
				}
			}
		}
	}
}
