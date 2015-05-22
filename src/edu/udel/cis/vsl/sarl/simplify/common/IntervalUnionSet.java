package edu.udel.cis.vsl.sarl.simplify.common;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory.IntervalUnion;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.number.real.CommonInterval;
import edu.udel.cis.vsl.sarl.number.real.RealInteger;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.simplify.IF.Range;

/**
 * Implementation of {@link Range} in which a set is represented as a finite
 * union of intervals. This class is immutable. Under construction.
 * 
 * The following are invariants. They force there to be a unique representation
 * for each set of numbers:
 * 
 * 1. An empty interval cannot occur in the list.
 * 
 * 2. All of the intervals in the list are disjoint.
 * 
 * 3. The intervals in the list are ordered from least to greatest.
 * 
 * 4. If an interval has the form {a,+infty}, then it is open on the right. If
 * an interval has the form {-infty,a}, then it is open on the left.
 * 
 * 5. If {a,b} and {b,c} are two consecutive intervals in the list, the the
 * first one must be open on the right and the second one must be open on the
 * left.
 * 
 * 6. If the range set has integer type, all of the intervals are integer
 * intervals. If it has real type, all of the intervals are real intervals.
 * 
 * 7. If the range set has integer type, all of the intervals are closed, except
 * for +infty and -infty.
 * 
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

	public IntervalUnionSet(Interval... itvs) {
		// TODO: Test
		IntervalUnionSet res = new IntervalUnionSet(itvs[0]);

		intervals = new LinkedList<>();
		for (int i = 1; i < itvs.length; i++) {
			int oldSize = res.intervals.size();

			IntervalUnionSet tmp = new IntervalUnionSet(itvs[i]);
			res = (IntervalUnionSet) this.union(tmp);

			int newSize = res.intervals.size();

			if (newSize > oldSize) {
				intervals.add(res.intervals.get(oldSize));
			}
		}
	}

	public void addNumber(Number number) {
		if (number == null) {
			throw new NullPointerException(
					"The Number parameter number cannot be null.");
		}
		if (this.containsNumber(number)) {
			return;
		}

		Interval itv = numberFactory.newInterval(
				number instanceof IntegerNumber, number, false, number, false);
		IntervalUnionSet ius = new IntervalUnionSet(itv);
		IntervalUnionSet res = (IntervalUnionSet) this.union(ius);
		
		this.intervals = res.intervals;
	}

	@Override
	public boolean isIntegral() {
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
	}

	@Override
	public boolean containsNumber(Number number) {
		if (number == null) {
			throw new NullPointerException(
					"The Number parameter number cannot be null.");
		}
		if (isIntegral() != (number instanceof IntegerNumber)) {
			throw new IllegalArgumentException(
					"The Type of the input is mismatched with the set.");
		}

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
		if (set == null) {
			throw new NullPointerException(
					"The Range parameter set cannot be null.");
		}
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
		if (set == null) {
			throw new NullPointerException(
					"The Range parameter set cannot be null.");
		}

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
		if (set == null) {
			throw new NullPointerException(
					"The Range parameter set cannot be null.");
		}

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
			int cmpLo = 0;

			if (lo1 == null) {
				cmpLo = -1;
			} else if (lo2 == null) {
				cmpLo = 1;
			} else {
				cmpLo = lo1.compareTo(lo2);
			}
			if (cmpLo > 0) {
				temp = i2;
				lhs_iter.previous();
			} else if (cmpLo < 0) {
				temp = i1;
				rhs_iter.previous();
			} else {
				if (i1.strictLower() == i2.strictLower()) {
					temp = i1;
					rhs_iter.previous();
				} else if (i1.strictLower()) {
					temp = i2;
					lhs_iter.previous();
				} else {
					temp = i1;
					rhs_iter.previous();
				}
			}
		}
		while (lhs_iter.hasNext() || rhs_iter.hasNext() || isChanged) {
			isChanged = false;
			while (lhs_iter.hasNext()) {
				Interval nxt = lhs_iter.next();
				int hasIntersection = doesIntersect(temp, nxt);
				
				if (hasIntersection == -1) {
					if (temp.upper().compareTo(nxt.lower()) == 0
							&& !(temp.strictUpper() && nxt.strictLower())) {
						hasIntersection = 0;
					}
				}
				if (hasIntersection == 1) {
					if (temp.lower().compareTo(nxt.upper()) == 0
							&& !(temp.strictLower() && nxt.strictUpper())) {
						hasIntersection = 0;
					}
				}
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
					if (temp.upper().compareTo(nxt.lower()) == 0
							&& !(temp.strictUpper() && nxt.strictLower())) {
						hasIntersection = 0;
					}
				}
				if (hasIntersection == 1) {
					if (temp.lower().compareTo(nxt.upper()) == 0
							&& !(temp.strictLower() && nxt.strictUpper())) {
						hasIntersection = 0;
					}
				}
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
				rtn.intervals.add(temp);
				if (lhs_iter.hasNext() && rhs_iter.hasNext()) {
					Interval i1 = lhs_iter.next();
					Interval i2 = rhs_iter.next();
					Number lo1 = i1.lower();
					Number lo2 = i2.lower();
					int cmpLo = 0;

					if (lo1 == null) {
						cmpLo = -1;
					} else if (lo2 == null) {
						cmpLo = 1;
					} else {
						cmpLo = lo1.compareTo(lo2);
					}
					if (cmpLo > 0) {
						temp = i2;
						lhs_iter.previous();
					} else {
						temp = i1;
						rhs_iter.previous();
					}
				} else {
					while (lhs_iter.hasNext()) {
						rtn.intervals.add(lhs_iter.next());
					}
					while (rhs_iter.hasNext()) {
						rtn.intervals.add(rhs_iter.next());
					}
				}
			}
		}
		return rtn;
	}

	@Override
	public Range intersect(Range set) {
		if (set == null) {
			throw new NullPointerException(
					"The Range parameter set cannot be null.");
		}

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
		while (lhs_iter.hasNext() && rhs_iter.hasNext()) {
			while (lhs_iter.hasNext()) {
				temp = lhs_iter.next();
				while (rhs_iter.hasNext()) {
					Interval nxt = rhs_iter.next();
					int hasIntersection = doesIntersect(temp, nxt);

					if (hasIntersection == -1) {
						rhs_iter.previous();
						if (rhs_iter.hasPrevious()) {
							rhs_iter.previous();
						}
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
		if (set == null) {
			throw new NullPointerException(
					"The Range parameter set cannot be null.");
		}

		IntervalUnionSet rtn = new IntervalUnionSet();
		Interval temp = null;
		ListIterator<Interval> lhs_iter = intervals.listIterator();
		ListIterator<Interval> rhs_iter = ((IntervalUnionSet) set).intervals
				.listIterator();

		if (!lhs_iter.hasNext()) {
			rtn = this;
			return rtn;
		} else if (!rhs_iter.hasNext()) {
			rtn = this;
			return rtn;
		}
		while (lhs_iter.hasNext()) {
			while (lhs_iter.hasNext()) {
				temp = lhs_iter.next();
				while (rhs_iter.hasNext()) {
					Interval nxt = rhs_iter.next();
					int hasIntersection = doesIntersect(temp, nxt);

					if (hasIntersection == -1) {
						rtn.intervals.add(temp);
						rhs_iter.previous();
						if (rhs_iter.hasPrevious()) {
							rhs_iter.previous();
						}
						break;
					} else if (hasIntersection == 1) {
						if (rhs_iter.hasNext()) {
							continue;
						} else {
							rtn.intervals.add(temp);
						}
					} else {
						Interval dummyItv = numberFactory.intersection(temp,
								nxt);
						int cmpLo = 0;

						if (dummyItv.lower() == null) {
							cmpLo = -1;
						} else if (temp.lower() == null) {
							cmpLo = 1;
						} else {
							cmpLo = dummyItv.lower().compareTo(temp.lower());
						}

						Interval newItv = numberFactory.newInterval(
								temp.isIntegral(), temp.lower(),
								temp.strictLower(), dummyItv.lower(),
								!dummyItv.strictLower());

						if (cmpLo > 0) {
							rtn.intervals.add(newItv);
						} else if (cmpLo == 0) {
							if (!temp.strictLower() && dummyItv.strictLower()) {
								rtn.intervals.add(newItv);
							}
						}

						int cmpUp = 0;

						if (nxt.upper() == null) {
							cmpUp = 1;
						} else if (temp.upper() == null) {
							cmpUp = -1;
						} else {
							cmpUp = nxt.upper().compareTo(temp.upper());
						}
						temp = numberFactory.newInterval(temp.isIntegral(),
								dummyItv.upper(), !dummyItv.strictUpper(),
								temp.upper(), temp.strictUpper());
						if (cmpUp > 0) {
							break;
						} else if (cmpUp == 0
								&& ((!nxt.strictUpper()) || temp.strictUpper())) {
							break;
						} else {
							if (!rhs_iter.hasNext()) {
								rtn.intervals.add(temp);
							}
						}
					}
				}
			}
		}
		return rtn;
	}

	@Override
	public Range affineTransform(Number a, Number b) {
		Number lo = null, up = null;
		boolean sl = false, su = false;
		Interval tempItv = null, curItv = null;
		IntervalUnionSet resItv = new IntervalUnionSet();
		ListIterator<Interval> iter = intervals.listIterator();

		if (!(iter.hasNext())) {
			return resItv; // Return an empty set.
		}// if
		curItv = iter.next();
		if (curItv.lower() == null && curItv.upper() == null
				&& (curItv.strictLower() && curItv.strictUpper())
				&& (!iter.hasNext())) {
			// The set contains exactly one Univ-set
			lo = null;
			sl = true;
			up = null;
			su = true;
			tempItv = numberFactory.newInterval(this.isIntegral(), lo, sl, up,
					su);
			resItv.intervals.add(tempItv);
			return resItv;
		}// if
		iter.previous();
		if (a.signum() == 0) {
			lo = isIntegral() ? numberFactory.zeroInteger() : numberFactory
					.zeroRational();
			sl = curItv.strictLower();
			up = isIntegral() ? numberFactory.zeroInteger() : numberFactory
					.zeroRational();
			su = curItv.strictUpper();
			if (!sl && !su) {
				tempItv = numberFactory.newInterval(this.isIntegral(), lo,
						false, up, false);
				resItv.intervals.add(tempItv);
			}
		} else if (a.signum() > 0) {
			while (iter.hasNext()) {
				curItv = iter.next();
				tempItv = numberFactory.affineTransform(curItv, a, b);
				resItv.intervals.add(tempItv);
			}
		} else {
			while (iter.hasNext()) {
				curItv = iter.next();
				tempItv = numberFactory.affineTransform(curItv, a, b);
				resItv.intervals.add(0, tempItv);
			}
		}
		return resItv;
	}

	@Override
	public Range complement() {
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
					"The SymbolicConstant parameter x cannot be null.");
		} else if (universe == null) {
			throw new NullPointerException(
					"The PreUniverse parameter universe cannot be null.");
		}

		BooleanExpression rtn = universe.bool(false);
		NumericExpression loE = null, upE = null, xE = (NumericExpression) x;
		Interval curItv = null;
		ListIterator<Interval> iter = intervals.listIterator();

		if (intervals.isEmpty()) {
			return rtn;
		}
		while (iter.hasNext()) {
			curItv = iter.next();

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
			if (!i1su && !i2sl) {
				return 0;
			} else {
				return -1;
			}
		} else if (compare_i1Li2U > 0) {
			return 1;
		} else if (compare_i1Li2U == 0) {
			if (!i1sl && !i2su) {
				return 0;
			} else {
				return 1;
			}
		} else {
			return 0;
		}
	}

	public String toString() {
		String rtn = "";
		ListIterator<Interval> iter = intervals.listIterator();

		rtn += "{";
		if (intervals.isEmpty()) {
			rtn += "(0, 0)";
		}
		while (iter.hasNext()) {
			Interval temp = iter.next();
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
			if (iter.hasNext()) {
				rtn += " U ";
			}
		}
		rtn += "};";
		return rtn;
	}

	public boolean checkingInvariants() {
		// TODO : Test
		/*
		 * 1. An empty interval cannot occur in the list.
		 * 
		 * 2. All of the intervals in the list are disjoint.
		 * 
		 * 3. The intervals in the list are ordered from least to greatest.
		 * 
		 * 4. If an interval has the form {a,+infty}, then it is open on the
		 * right. If an interval has the form {-infty,a}, then it is open on the
		 * left.
		 * 
		 * 5. If {a,b} and {b,c} are two consecutive intervals in the list, the
		 * the first one must be open on the right and the second one must be
		 * open on the left.
		 * 
		 * 6. If the range set has integer type, all of the intervals are
		 * integer intervals. If it has real type, all of the intervals are real
		 * intervals.
		 * 
		 * 7. If the range set has integer type, all of the intervals are
		 * closed, except for +infty and -infty.
		 */
		ListIterator<Interval> iter = intervals.listIterator();
		Interval tmpItv = null, prevItv = null;

		while (iter.hasNext()) {
			if (tmpItv != null) {
				prevItv = tmpItv;
			}
			tmpItv = iter.next();
			// Check 1
			if (tmpItv.lower().compareTo(tmpItv.upper()) == 0
					&& (tmpItv.strictLower() || tmpItv.strictUpper())) {
				return false;
			}
			// Check 2
			if (doesIntersect(prevItv, tmpItv) == 0) {
				return false;
			}
			// check 3
			if (numberFactory.compare(prevItv, tmpItv) > 0) {
				return false;
			}
			// check 4
			if (tmpItv.lower() == null && !tmpItv.strictLower()) {
				return false;
			}
			if (tmpItv.upper() == null && !tmpItv.strictUpper()) {
				return false;
			}
			// check 5
			if (tmpItv.lower().compareTo(prevItv.upper()) == 0) {
				if (!(tmpItv.strictLower() && prevItv.strictUpper())) {
					return false;
				}
			}
			// check 6
			if (isIntegral() ? tmpItv.isReal() : tmpItv.isIntegral()) {
				return false;
			}
			// check 7
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
