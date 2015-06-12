package edu.udel.cis.vsl.sarl.simplify.common;

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
	 * @param itvs
	 *            an array of intervals (with at least one interval) satisfying
	 *            all invariants specified for {@link IntervalUnionSet}.
	 */
	public IntervalUnionSet(Interval... itvs) {
		assert itvs != null;
		assert itvs.length > 0;

		boolean isIntegral = false;
		int inputIdx = 0, tmpIdx = 0, resSize = 0;
		int inputSize = itvs.length;
		Interval[] resArr = new Interval[inputSize];

		while (inputIdx < inputSize) {
			if (itvs[inputIdx] == null || itvs[inputIdx].isEmpty()) {
				inputIdx++;
			} else {
				resArr[resSize] = itvs[inputIdx];
				isIntegral = itvs[inputIdx].isIntegral();
				resSize++;
				inputIdx++;
				break;
			}
		}
		while (inputIdx < inputSize) {
			if (itvs[inputIdx] != null
					&& itvs[inputIdx].isIntegral() == isIntegral
					&& !itvs[inputIdx].isEmpty()) {
				tmpIdx = 0;
				while (tmpIdx < resSize) {
					int cmp = numberFactory.compare(itvs[inputIdx],
							resArr[tmpIdx]);

					if (cmp < 2 && cmp > -2) {
						break;
					}
					if (cmp == 4) {
						Number diff = numberFactory.subtract(
								itvs[inputIdx].lower(), resArr[tmpIdx].upper());
						if (diff.isZero()
								&& (!itvs[inputIdx].strictLower() || !resArr[tmpIdx]
										.strictUpper())) {
							cmp = 2;
						} else if (isIntegral && diff.isOne()) {
							cmp = 2;
						} else {
							if (tmpIdx < resSize - 1) {
								tmpIdx++;
								continue;
							} else {
								resArr[resSize] = itvs[inputIdx];
								resSize++;
								break;
							}
						}
					} else if (cmp == -4) {
						Number diff = numberFactory.subtract(
								resArr[tmpIdx].lower(), itvs[inputIdx].upper());
						if (diff.isZero()
								&& (!itvs[inputIdx].strictUpper() || !resArr[tmpIdx]
										.strictLower())) {
							cmp = -2;
						} else if (isIntegral && diff.isOne()) {
							cmp = -2;
						} else {
							System.arraycopy(resArr, tmpIdx, resArr,
									tmpIdx + 1, resSize - tmpIdx);
							resArr[tmpIdx] = itvs[inputIdx];
							resSize++;
							break;
						}
					}
					if (cmp == -2) {
						resArr[tmpIdx] = numberFactory.newInterval(isIntegral,
								itvs[inputIdx].lower(),
								itvs[inputIdx].strictLower(),
								resArr[tmpIdx].upper(),
								resArr[tmpIdx].strictUpper());
						break;
					} else {
						int ctr = 1;
						Number lo = itvs[inputIdx].lower();
						Number up = itvs[inputIdx].upper();
						boolean sl = itvs[inputIdx].strictLower();
						boolean su = itvs[inputIdx].strictUpper();

						if (cmp == 2) {
							lo = resArr[tmpIdx].lower();
							sl = resArr[tmpIdx].strictLower();
						}
						while (tmpIdx + ctr < resSize) {
							int cmp2 = numberFactory.compare(itvs[inputIdx],
									resArr[tmpIdx + ctr]);

							if (cmp2 == -4) {
								Number diff = numberFactory.subtract(
										resArr[tmpIdx + ctr].lower(),
										itvs[inputIdx].upper());
								if (diff.isZero()
										&& (!itvs[inputIdx].strictUpper() || !resArr[tmpIdx
												+ ctr].strictLower())) {
									cmp2 = -2;
								} else if (isIntegral && diff.isOne()) {
									up = resArr[tmpIdx + ctr].upper();
									su = resArr[tmpIdx + ctr].strictUpper();
									cmp2 = -2;
								}
							}
							if (cmp2 == -3) {
								ctr++;
							} else {
								if (cmp2 == -2) {
									up = resArr[tmpIdx + ctr].upper();
									su = resArr[tmpIdx + ctr].strictUpper();
									ctr++;
								}
								break;
							}
						}
						resArr[tmpIdx] = numberFactory.newInterval(isIntegral,
								lo, sl, up, su);
						if (ctr > 1 && (tmpIdx + ctr < resSize)) {
							System.arraycopy(resArr, tmpIdx + ctr, resArr,
									tmpIdx + 1, resSize - tmpIdx - ctr);
						}
						resSize -= (ctr - 1);
					}

				}
			}
			inputIdx++;
		}

		isInt = isIntegral;
		size = resSize;
		if (size > 0) {
			intervalArr = new Interval[resSize];
			System.arraycopy(resArr, 0, intervalArr, 0, size);
		}
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
		int curIdx = 0;
		int tarIdx = 0;
		int ctr = 0;
		int tempSize = size + tarSize;
		Interval[] tempArr = new Interval[tempSize];
		Interval temp = null;
		boolean isChanged = false;

		if (curIdx >= size) {
			IntervalUnionSet rtn = new IntervalUnionSet(tar.isInt, tarSize);

			System.arraycopy(tar.intervalArr, 0, rtn.intervalArr, 0, tarSize);
			return rtn;
		} else if (tarIdx >= tarSize) {
			IntervalUnionSet rtn = new IntervalUnionSet(isInt, size);

			System.arraycopy(intervalArr, 0, rtn.intervalArr, 0, size);
			return rtn;
		} else {
			Interval i1 = intervalArr[curIdx];
			Interval i2 = tar.intervalArr[tarIdx];
			int cmp = numberFactory.compare(i1, i2);

			if (cmp <= 0) {
				temp = i1;
				curIdx++;
			} else {
				temp = i2;
				tarIdx++;
			}
		}
		while (curIdx < size || tarIdx < tarSize || isChanged) {
			isChanged = false;
			while (curIdx < size) {
				Interval nxt = intervalArr[curIdx];
				int compareIterval = numberFactory.compare(temp, nxt);
				/*
				 * if (its == -1) { if (temp.upper().compareTo(nxt.lower()) == 0
				 * && !(temp.strictUpper() && nxt.strictLower())) { its = 0; } }
				 * if (its == 1) { if (temp.lower().compareTo(nxt.upper()) == 0
				 * && !(temp.strictLower() && nxt.strictUpper())) { its = 0; } }
				 */

				if (compareIterval == -4) {
					break;
				} else {
					if (compareIterval < 4 && compareIterval > -4) {
						IntervalUnion tmpU = new IntervalUnion();

						numberFactory.union(temp, nxt, tmpU);
						temp = tmpU.union;
						isChanged = true;
					} else {
						tempArr[ctr] = nxt;
						ctr++;
					}
					curIdx++;
				}
			}
			while (tarIdx < tarSize) {
				Interval nxt = intervalArr[curIdx];
				int compareIterval = numberFactory.compare(temp, nxt);

				/*
				 * if (its == -1) { if (temp.upper().compareTo(nxt.lower()) == 0
				 * && !(temp.strictUpper() && nxt.strictLower())) { its = 0; } }
				 * if (its == 1) { if (temp.lower().compareTo(nxt.upper()) == 0
				 * && !(temp.strictLower() && nxt.strictUpper())) { its = 0; } }
				 */
				if (compareIterval == -4) {
					break;
				} else {
					if (compareIterval < 4 && compareIterval > -4) {
						IntervalUnion tmpU = new IntervalUnion();

						numberFactory.union(temp, nxt, tmpU);
						temp = tmpU.union;
						isChanged = true;
					} else {
						tempArr[ctr] = nxt;
						ctr++;
					}
					tarIdx++;
				}
			}
			if (!isChanged) {
				tempArr[ctr] = temp;
				ctr++;
				if (curIdx < size && tarIdx < tarSize) {
					Interval i1 = intervalArr[curIdx];
					Interval i2 = tar.intervalArr[tarIdx];
					int cmp = numberFactory.compare(i1, i2);

					if (cmp <= 0) {
						temp = i1;
						curIdx++;
					} else {
						temp = i2;
						tarIdx++;
					}
				} else {
					while (curIdx < size) {
						tempArr[ctr] = intervalArr[curIdx];
						ctr++;
						curIdx++;
					}
					while (tarIdx < tarSize) {
						tempArr[ctr] = tar.intervalArr[tarIdx];
						ctr++;
						tarIdx++;
					}
				}
			}
		}

		IntervalUnionSet rtn = new IntervalUnionSet(tar.isInt, ctr);

		System.arraycopy(tempArr, 0, rtn.intervalArr, 0, ctr);
		return rtn;
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
						&& (!curItv.strictLower() && !curItv.strictUpper())) {
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
