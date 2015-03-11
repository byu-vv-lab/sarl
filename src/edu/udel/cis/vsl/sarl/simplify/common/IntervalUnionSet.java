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
import edu.udel.cis.vsl.sarl.number.Numbers;
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
		ListIterator<Interval> iter = intervals.listIterator();

		while (iter.hasNext()) {
			Interval curr = iter.next();
			// cursor now just after curr
			int compare = curr.compare(number);

			if (compare < 0) { // number > curr
			} else if (compare == 0) {
				return; // number in curr
			} else { // number < curr

			}
		}
	}

	@Override
	public boolean isIntegral() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsNumber(Number number) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(Range set) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(Range set) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Range union(Range set) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Range intersect(Range set) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Range minus(Range set) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Range affineTransform(Number a, Number b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Range complement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BooleanExpression symbolicRepresentation(SymbolicConstant x) {
		// TODO Auto-generated method stub
		return null;
	}

}
