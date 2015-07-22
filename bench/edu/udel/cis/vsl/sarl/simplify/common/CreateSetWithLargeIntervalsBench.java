package edu.udel.cis.vsl.sarl.simplify.common;

import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.number.Numbers;

public class CreateSetWithLargeIntervalsBench {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		long start;
		long end;
		long mark;
		int tempInt1;
		int tempInt2;
		int numexpr;
		Interval[] intervals;
		IntervalUnionSet intervalUnionSet;
		NumberFactory numberFactory = Numbers.REAL_FACTORY;

		//
		numexpr = 10000;
		intervals = new Interval[numexpr];
		for (int i = 0; i < numexpr * 3; i += 3) {
			intervals[i / 3] = numberFactory.newInterval(true,
					numberFactory.integer(i), false,
					numberFactory.integer(i + 1), false);
		}
		start = System.currentTimeMillis();
		intervalUnionSet = new IntervalUnionSet(intervals);
		end = System.currentTimeMillis();
		mark = end - start;
		System.out
				.println("To add ("
						+ numexpr
						+ ") ordered disjointed intervals in to intervalUnionSet takes: ("
						+ mark + ") Millis");
		//
		intervals = new Interval[numexpr];
		for (int i = 0; i < numexpr * 3; i += 3) {
			intervals[i / 3] = numberFactory.newInterval(true,
					numberFactory.integer(i), false,
					numberFactory.integer(i + 10), false);
		}
		start = System.currentTimeMillis();
		intervalUnionSet = new IntervalUnionSet(intervals);
		end = System.currentTimeMillis();
		mark = end - start;
		System.out
				.println("To add ("
						+ numexpr
						+ ") ordered jointed intervals in to intervalUnionSet takes: ("
						+ mark + ") Millis");
		//
		intervals = new Interval[numexpr];
		for (int i = 0; i < numexpr * 3; i += 3) {
			tempInt1 = (int) (Math.random() * 100);
			tempInt2 = (int) (Math.random() * 1000000);
			intervals[i / 3] = numberFactory.newInterval(true,
					numberFactory.integer(tempInt2), false,
					numberFactory.integer(tempInt2 + tempInt1), false);
			//System.out.println(intervals[i / 3].toString());
		}
		start = System.currentTimeMillis();
		intervalUnionSet = new IntervalUnionSet(intervals);
		end = System.currentTimeMillis();
		mark = end - start;
		//System.out.println(intervalUnionSet.toString());
		System.out
				.println("To add ("
						+ numexpr
						+ ") random (most disjointed) intervals in to intervalUnionSet takes: ("
						+ mark + ") Millis");
		//
		intervals = new Interval[numexpr];
		for (int i = 0; i < numexpr * 3; i += 3) {
			tempInt1 = (int) (Math.random() * 1000000);
			tempInt2 = (int) (Math.random() * 100000);
			intervals[i / 3] = numberFactory.newInterval(true,
					numberFactory.integer(tempInt2), false,
					numberFactory.integer(tempInt2 + tempInt1), false);
			//System.out.println(intervals[i / 3].toString());
		}
		start = System.currentTimeMillis();
		intervalUnionSet = new IntervalUnionSet(intervals);
		end = System.currentTimeMillis();
		mark = end - start;
		//System.out.println(intervalUnionSet.toString());
		System.out
				.println("To add ("
						+ numexpr
						+ ") random (most jointed) intervals in to intervalUnionSet takes: ("
						+ mark + ") Millis");
		
	}
}
