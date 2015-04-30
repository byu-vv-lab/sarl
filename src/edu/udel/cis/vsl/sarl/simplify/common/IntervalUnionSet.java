package edu.udel.cis.vsl.sarl.simplify.common;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory.IntervalUnion;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.number.real.CommonInterval;
import edu.udel.cis.vsl.sarl.simplify.IF.Range;

/**
 * Implementation of {@link Range} in which a set is represented as a finite
 * union of intervals. This class is immutable. Under construction.
 * 
 * @author siegel
 *
 */
public class IntervalUnionSet implements Range {

	private static NumberFactory numberFactory = Numbers.REAL_FACTORY;

	/**
	 * Ordered list of intervals; the value set is the union of these intervals.
	 */
	private List<Interval> intervals;

	/**
	 * Constructs new empty set.
	 */
	public IntervalUnionSet() {
		intervals = new LinkedList<>();
	}

	public IntervalUnionSet(Number number) {
		Interval interval = numberFactory.newInterval(
				number instanceof IntegerNumber, number, false, number, false);

		intervals = new LinkedList<>();
		intervals.add(interval);
	}

	public IntervalUnionSet(Interval interval) {
		// intervals are immutable, so re-use:
		intervals = new LinkedList<>();
		intervals.add(interval);
	}

	public IntervalUnionSet(IntervalUnionSet other) {
		intervals = new LinkedList<>(other.intervals);
	}

	public void addNumber(Number number) {
		// TODO: Testing *
		ListIterator<Interval> iter = intervals.listIterator();
		int index = 0;

		while (iter.hasNext()) {
			Interval curr = iter.next();
			Interval interval = numberFactory.newInterval(
					number instanceof IntegerNumber, number, false, number,
					false);
			// cursor now just after curr
			int compare = curr.compare(number);

			if (compare < 0) { // number > curr
				if (!iter.hasNext()) {
					intervals.add(interval);
					return; // Append number to tail
				}// if
			} else if (compare == 0) {
				return; // number in curr
			} else { // number < curr
				intervals.add(index, interval);
				return; // Insert number before curr
			}// if-else if-else

			index++;
		}// while
	}

	@Override
	public boolean isIntegral() {
		// TODO Testing

		// To return true iff there exists at least 1 interval and all intervals
		// are integral.
		boolean areNotIntegral = false;
		ListIterator<Interval> iter = intervals.listIterator();

		if (!(iter.hasNext())) {
			return areNotIntegral; // When this set is empty, return false
		}// if

		while (iter.hasNext()) {
			Interval curItv = iter.next();

			areNotIntegral = areNotIntegral || (!curItv.isIntegral());
		}// while

		return (!areNotIntegral); // When this set is not empty and each
									// interval is integral.
	}

	@Override
	public boolean isEmpty() {
		ListIterator<Interval> iter = intervals.listIterator();

		return !(iter.hasNext());
	}// public boolean isEmpty();

	@Override
	public boolean containsNumber(Number number) {
		// TODO Testing
		ListIterator<Interval> iter = intervals.listIterator();

		while (iter.hasNext()) {
			Interval curItv = iter.next();

			if (curItv.contains(number)) {
				return true; // The 'number' is found
			}
		}

		return false; // The 'number' is not found.
	}

	@Override
	public boolean contains(Range set) {
		// TODO Testing

		// To check the type of set
		assert (set.isIntegral() == this.isIntegral());

		ListIterator<Interval> lhs_iter = intervals.listIterator();
		ListIterator<Interval> rhs_iter = ((IntervalUnionSet) set).intervals
				.listIterator();

		while (lhs_iter.hasNext()) {
			Interval lhs_itv = lhs_iter.next();

			while (rhs_iter.hasNext()) {
				Interval rhs_itv = rhs_iter.next();

				if (doesContain(lhs_itv, rhs_itv) != 1) {
					if (!lhs_iter.hasNext()) {
						return false;
					}
					rhs_iter.previous();
					break;
				}

			}// inner while

			if (!rhs_iter.hasNext()) {
				return true;
			}

		}// outer while

		return true;
	}

	@Override
	public boolean intersects(Range set) {
		// TODO Testing

		// To check the type of set
		assert (set.isIntegral() == this.isIntegral());

		ListIterator<Interval> lhs_iter = intervals.listIterator();
		ListIterator<Interval> rhs_iter = ((IntervalUnionSet) set).intervals
				.listIterator();

		while (lhs_iter.hasNext()) {
			Interval lhs_itv = lhs_iter.next();

			while (rhs_iter.hasNext()) {
				Interval rhs_itv = rhs_iter.next();

				if (doesIntersect(lhs_itv, rhs_itv) == 0) {
					return true;
				} else if (doesIntersect(lhs_itv, rhs_itv) == -1) {
					lhs_iter.previous();
					break;
				}
			}// inner while

		}// outer while

		return false;
	}

	@Override
	public Range union(Range set) {
		// TODO Testing
		assert (set.isIntegral() == this.isIntegral());

		IntervalUnionSet rtn = new IntervalUnionSet();
		Interval temp = null;
		ListIterator<Interval> lhs_iter = intervals.listIterator();
		ListIterator<Interval> rhs_iter = ((IntervalUnionSet) set).intervals
				.listIterator();
		boolean isChanged = false;

		if (!lhs_iter.hasNext()) {
			rtn = (IntervalUnionSet) set;
			return rtn;
		} else if (!rhs_iter.hasNext()) {
			rtn = this;
			return rtn;
		} else {
			Interval i1 = lhs_iter.next();
			Interval i2 = rhs_iter.next();
			Number lo1 = i1.lower();
			Number lo2 = i2.lower();

			if (lo1.compareTo(lo2) > 0) {
				temp = i2;
				lhs_iter.previous();
			} else {
				temp = i1;
				rhs_iter.previous();
			}

		}

		while (lhs_iter.hasNext() || rhs_iter.hasNext()) {
			isChanged = false;

			while (lhs_iter.hasNext()) {
				Interval nxt = lhs_iter.next();
				int hasIntersection = doesIntersect(temp, nxt);

				if (hasIntersection == -1) {
					lhs_iter.previous();
					break;
				} else if (hasIntersection == 0) {
					IntervalUnion itvU = new IntervalUnion();
					numberFactory.union(temp, nxt, itvU);
					temp = itvU.union;
					isChanged = true;
				} else {
					rtn.intervals.add(nxt);
				}
			}

			while (rhs_iter.hasNext()) {
				Interval nxt = rhs_iter.next();
				int hasIntersection = doesIntersect(temp, nxt);

				if (hasIntersection == -1) {
					rhs_iter.previous();
					break;
				} else if (hasIntersection == 0) {
					IntervalUnion itvU = new IntervalUnion();
					numberFactory.union(temp, nxt, itvU);
					temp = itvU.union;
					isChanged = true;
				} else {
					rtn.intervals.add(nxt);
				}
			}

			if (!isChanged) {
				if (lhs_iter.hasNext() && rhs_iter.hasNext()) {
					rtn.intervals.add(temp);
					Interval i1 = lhs_iter.next();
					Interval i2 = rhs_iter.next();
					Number lo1 = i1.lower();
					Number lo2 = i2.lower();

					if (lo1.compareTo(lo2) > 0) {
						temp = i2;
						lhs_iter.previous();
					} else {
						temp = i1;
						rhs_iter.previous();
					}
				} else if (lhs_iter.hasNext()) {
					rtn.intervals.add(temp);
					rtn.intervals.add(lhs_iter.next());
				} else if (rhs_iter.hasNext()) {
					rtn.intervals.add(temp);
					rtn.intervals.add(rhs_iter.next());
				}
			}
		}
		return rtn;
	}

	@Override
	public Range intersect(Range set) {
		// TODO Testing
		assert (set.isIntegral() == this.isIntegral());

		IntervalUnionSet rtn = new IntervalUnionSet();
		Interval temp = null;
		ListIterator<Interval> lhs_iter = intervals.listIterator();
		ListIterator<Interval> rhs_iter = ((IntervalUnionSet) set).intervals
				.listIterator();

		if (!lhs_iter.hasNext()) {
			rtn = this;
			return rtn;
		} else if (!rhs_iter.hasNext()) {
			rtn = (IntervalUnionSet) set;
			return rtn;
		}

		while (lhs_iter.hasNext() || rhs_iter.hasNext()) {

			while (lhs_iter.hasNext()) {
				temp = lhs_iter.next();

				while (rhs_iter.hasNext()) {
					Interval nxt = rhs_iter.next();
					int hasIntersection = doesIntersect(temp, nxt);

					if (hasIntersection == -1) {
						rhs_iter.previous();
						break;
					} else if (hasIntersection == 1) {
						continue;
					} else {
						Interval newItv = numberFactory.intersection(temp, nxt);
						rtn.intervals.add(newItv);
					}
				}
			}
		}

		return rtn;
	}

	@Override
	public Range minus(Range set) {
		// TODO Testing
		assert (set.isIntegral() == this.isIntegral());

		IntervalUnionSet rtn = new IntervalUnionSet();
		Interval temp = null;
		ListIterator<Interval> lhs_iter = intervals.listIterator();
		ListIterator<Interval> rhs_iter = ((IntervalUnionSet) set).intervals
				.listIterator();

		if (!lhs_iter.hasNext()) {
			rtn = this;
			return rtn;
		} else if (!rhs_iter.hasNext()) {
			rtn = (IntervalUnionSet) set;
			return rtn;
		}

		while (lhs_iter.hasNext() || rhs_iter.hasNext()) {

			while (lhs_iter.hasNext()) {
				temp = lhs_iter.next();

				while (rhs_iter.hasNext()) {
					Interval nxt = rhs_iter.next();
					int hasIntersection = doesIntersect(temp, nxt);

					if (hasIntersection == -1) {
						rtn.intervals.add(temp);
						rhs_iter.previous();
						break;
					} else if (hasIntersection == 1) {
						continue;
					} else {
						Interval dummyItv = numberFactory.intersection(temp,
								nxt);

						if (dummyItv.lower().compareTo(temp.lower()) > 0) {
							Interval newItv = new CommonInterval(
									temp.isIntegral(), temp.lower(),
									temp.strictLower(), dummyItv.lower(),
									!dummyItv.strictLower());
							rtn.intervals.add(newItv);
						} else if (dummyItv.upper().compareTo(temp.upper()) == 0) {
							break;
						}
						temp = new CommonInterval(temp.isIntegral(),
								dummyItv.upper(), !dummyItv.strictUpper(),
								temp.upper(), temp.strictUpper());
					}
				}
			}
		}

		return rtn;
	}

	@Override
	public Range affineTransform(Number a, Number b) {
		// TODO Testing
		Number lo = null, up = null;
		boolean sl = false, su = false;
		Interval tempItv = null;
		Interval curItv = null, nextItv = null;
		IntervalUnionSet resItv = new IntervalUnionSet();
		ListIterator<Interval> origItvIter = intervals.listIterator();

		if (!(origItvIter.hasNext())) {
			return resItv; // Return an empty set.
		}// if

		curItv = origItvIter.next();

		if (curItv.lower() == null && curItv.upper() == null
				&& (curItv.strictLower() || curItv.strictUpper()) == false
				&& (!origItvIter.hasNext())) { // The set contains exactly one
												// Univ-set
			lo = null;
			sl = false;
			up = null;
			su = false;
			tempItv = numberFactory.newInterval(this.isIntegral(), lo, sl, up,
					su); // [-infi, +infi]
			resItv.intervals.add(tempItv);
			return resItv;
		}// if

		if (curItv.lower().compareTo(curItv.upper()) == 0
				&& (curItv.strictLower() && curItv.strictUpper()) == true
				&& (!origItvIter.hasNext())) { // The set contains exactly one
												// empty-set.
			// In fact, the interval should be (b,b) which is same to (0, 0)
			lo = this.isIntegral() ? numberFactory.number("0") : numberFactory
					.number("0.0");
			sl = curItv.strictLower();
			up = this.isIntegral() ? numberFactory.number("0") : numberFactory
					.number("0.0");
			su = curItv.strictUpper();
			tempItv = numberFactory.newInterval(this.isIntegral(), lo, sl, up,
					su); // (0,0) or (0.0 , 0.0)
			resItv.intervals.add(tempItv);
			return resItv;
		}// if

		if (a.signum() == 0) {
			lo = this.isIntegral() ? numberFactory.number("0") : numberFactory
					.number("0.0");
			sl = true;
			up = this.isIntegral() ? numberFactory.number("0") : numberFactory
					.number("0.0");
			su = true;
			tempItv = numberFactory.newInterval(this.isIntegral(), lo, sl, up,
					su); // (0,0) or (0.0 , 0.0)
			resItv.intervals.add(tempItv);
			return resItv;
		}

		tempItv = numberFactory.affineTransform(curItv, a, b);

		while (origItvIter.hasNext()) {
			nextItv = numberFactory.affineTransform(origItvIter.next(), a, b);

			if ((tempItv.upper().compareTo(nextItv.lower()) < 0)
					|| (tempItv.upper().compareTo(nextItv.lower()) == 0 && (tempItv
							.strictUpper() && nextItv.strictLower() == true))) {
				if (a.signum() < 0) {
					resItv.intervals.add(0, tempItv);
				} else {
					resItv.intervals.add(tempItv);
				}
				tempItv = nextItv;
			} else {
				lo = (tempItv.lower().compareTo(nextItv.lower()) < 0) ? tempItv
						.lower() : nextItv.lower();
				sl = (tempItv.lower().compareTo(nextItv.lower()) < 0) ? tempItv
						.strictLower() : nextItv.strictLower();
				up = (tempItv.upper().compareTo(nextItv.upper()) < 0) ? tempItv
						.upper() : nextItv.upper();
				su = (tempItv.upper().compareTo(nextItv.upper()) < 0) ? tempItv
						.strictUpper() : nextItv.strictUpper();

				tempItv = numberFactory.newInterval(this.isIntegral(), lo, sl,
						up, su);
			}
		}

		return resItv;
	}

	@Override
	public Range complement() {
		// TODO Testing
		Number lo = null, up = null;
		boolean sl = false, su = false;
		Interval tempItv = null;
		Interval curItv = null, nextItv = null;
		IntervalUnionSet resItv = new IntervalUnionSet();
		ListIterator<Interval> origItvIter = intervals.listIterator();

		if (!(origItvIter.hasNext())) { // The set is empty
			tempItv = numberFactory.newInterval(this.isIntegral(), null, false,
					null, false);
			resItv.intervals.add(0, tempItv);
			return resItv; // Return Univ [null, null]
		}// if

		curItv = origItvIter.next();

		if (curItv.lower() == null && curItv.upper() == null
				&& (curItv.strictLower() || curItv.strictUpper()) == false
				&& (!origItvIter.hasNext())) { // The set contains exactly one
												// Univ-set.
			lo = this.isIntegral() ? numberFactory.number("0") : numberFactory
					.number("0.0");
			sl = true;
			up = this.isIntegral() ? numberFactory.number("0") : numberFactory
					.number("0.0");
			su = true;
			tempItv = numberFactory.newInterval(this.isIntegral(), lo, sl, up,
					su); // (0,0) or (0.0 , 0.0)
			resItv.intervals.add(0, tempItv);
			return resItv;
		}// if

		if (curItv.lower() == curItv.upper()
				&& (curItv.strictLower() && curItv.strictUpper()) == true
				&& (!origItvIter.hasNext())) {
			// The set contains exactly one empty-set.
			lo = null;
			sl = false;
			up = null;
			su = false;
			tempItv = numberFactory.newInterval(this.isIntegral(), lo, sl, up,
					su);
			resItv.intervals.add(0, tempItv);
			return resItv;
		}// if

		if (curItv.lower() != null) {
			// The original set starts with [.., .. OR (.., ..
			// To generate the possible first Interval starting with -infinity
			lo = null;
			sl = false;
			up = curItv.lower();
			su = !curItv.strictLower();
			tempItv = numberFactory.newInterval(this.isIntegral(), lo, sl, up,
					su); // [-infi, ..
			resItv.intervals.add(tempItv);
		}// if

		while (origItvIter.hasNext()) {
			nextItv = origItvIter.next();

			lo = curItv.upper();
			sl = !curItv.strictUpper();
			up = nextItv.lower();
			su = !nextItv.strictLower();
			tempItv = numberFactory.newInterval(this.isIntegral(), lo, sl, up,
					su);
			resItv.intervals.add(tempItv);

			curItv = nextItv;
		}// while

		if (curItv.upper() == null) {
			// The original set is end with [.., +infi] or (.. , +infi]
			return resItv;
		} else {
			// The original set ends with [ | ( .., .. ) | ] or [-infi, ..) | ]
			// To generate the possible last Interval ending with +infinity
			lo = curItv.upper();
			sl = !curItv.strictUpper();
			up = null;
			su = false;
			tempItv = numberFactory.newInterval(this.isIntegral(), lo, sl, up,
					su);
			resItv.intervals.add(tempItv);
			return resItv;
		}// if-else
	}

	@Override
	public BooleanExpression symbolicRepresentation(SymbolicConstant x) {
		// TODO Auto-generated method stub

		return null;
	}

	// ----Tools----
	/**
	 * This function is a private function used to check whether
	 * {@link Interval} itv1 contains {@link Interval} itv2
	 * 
	 * @param itv1
	 *            any non-<code>null</code> {@link Interval}
	 * @param itv2
	 *            any non-<code>null</code> {@link Interval} having the type
	 *            with itv1
	 * 
	 * @return <code>1</code> iff the itv1 contains the itv2 <code>0</code> iff
	 *         the itv1 does NOT contain the itv2 <code>-1</code> iff the itv2
	 *         contains the itv1
	 */
	private int doesContain(Interval i1, Interval i2) {
		assert !(i1.isEmpty()) && !(i2.isEmpty()); // Both are not empty
		assert i1.isIntegral() == i2.isIntegral(); // Both have same type
		assert i1.isReal() == i2.isReal();

		Number i1lo = i1.lower(), i1up = i1.upper(), i2lo = i2.lower(), i2up = i2
				.upper();
		boolean i1sl = i1.strictLower(), i1su = i1.strictUpper(), i2sl = i2
				.strictLower(), i2su = i2.strictUpper();
		int compareLo, compareUp;

		if (i1lo == null && i2lo == null) {
			compareLo = 0;
		} else if (i1lo == null) {
			compareLo = -1;
		} else if (i2lo == null) {
			compareLo = 1;
		} else {
			compareLo = i1lo.compareTo(i2lo);
		}
		if (i1up == null && i2up == null) {
			compareUp = 0;
		} else if (i1up == null) {
			compareUp = 1;
		} else if (i2up == null) {
			compareUp = -1;
		} else {
			compareUp = i1up.compareTo(i2up);
		}

		if (compareLo < 0) {
			if (compareUp < 0) {
				return 0;
			} else if (compareUp > 0) {
				return 1;
			} else {
				if (i1su && !i2su) {
					return 0;
				} else {
					return 1;
				}
			}
		} else if (compareLo > 0) {
			if (compareUp < 0) {
				return -1;
			} else if (compareUp > 0) {
				return 0;
			} else {
				if (!i1su && i2su) {
					return 0;
				} else {
					return -1;
				}
			}
		} else {
			if (compareUp < 0) {
				if (!i1sl && i2sl) {
					return 0;
				} else {
					return -1;
				}
			} else if (compareUp > 0) {
				if (i1sl && !i2sl) {
					return 0;
				} else {
					return 1;
				}
			} else {
				if (i1sl == i2sl) {
					if (i1su == i2su) {
						return 1;
					} else {
						return i1su ? -1 : 1;
					}
				} else if (i1su) {
					if (i1su == i2su) {
						return -1;
					} else {
						return i1su ? -1 : 0;
					}
				} else {
					if (i1su == i2su) {
						return 1;
					} else {
						return i1su ? 0 : 1;
					}
				}

			}
		}
	}

	/**
	 * This function is a private function used to check whether
	 * {@link Interval} itv1 intersects {@link Interval} itv2
	 * 
	 * @param itv1
	 *            any non-<code>null</code> {@link Interval}
	 * @param itv2
	 *            any non-<code>null</code> {@link Interval} having the type
	 *            with itv1
	 * 
	 * @return <code>1</code> iff the itv1 on the right side of the itv2 without
	 *         intersection <code>0</code> iff the itv1 intersects with the itv2
	 *         <code>-1</code> iff the itv1 on the left side of the itv2 without
	 *         intersection
	 */
	private int doesIntersect(Interval i1, Interval i2) {
		assert !(i1.isEmpty()) && !(i2.isEmpty()); // Both are not empty
		assert i1.isIntegral() == i2.isIntegral(); // Both have same type
		assert i1.isReal() == i2.isReal();

		Number i1lo = i1.lower(), i1up = i1.upper(), i2lo = i2.lower(), i2up = i2
				.upper();
		boolean i1sl = i1.strictLower(), i1su = i1.strictUpper(), i2sl = i2
				.strictLower(), i2su = i2.strictUpper();
		int compare_i1Ui2L, compare_i1Li2U;

		if (i1up == null || i2lo == null) {
			compare_i1Ui2L = 1;
		} else {
			compare_i1Ui2L = i1up.compareTo(i2lo);
		}
		if (i1lo == null || i2up == null) {
			compare_i1Li2U = -1;
		} else {
			compare_i1Li2U = i1lo.compareTo(i2up);
		}

		if (compare_i1Ui2L < 0) {
			return -1;
		} else if (compare_i1Ui2L == 0) {
			if (i1su && i2sl) {
				return -1;
			} else {
				return 0;
			}
		} else if (compare_i1Li2U > 0) {
			return 1;
		} else if (compare_i1Li2U == 0) {
			if (i1sl && i2su) {
				return 1;
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}
}
