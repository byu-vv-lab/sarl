package edu.udel.cis.vsl.sarl.simplify.common;

import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.number.Numbers;

public class LargeUnionIntervalUnionSetBench {

	public static void main(String[] args) {
		long start;
		long end;
		long mark;
		int tempInt1;
		int tempInt2;
		int numexpr;
		IntervalUnionSet[] intervalUnionSets;
		IntervalUnionSet intervalUnionSet;
		NumberFactory numberFactory = Numbers.REAL_FACTORY;

		//
		numexpr = 10000;
		intervalUnionSets = new IntervalUnionSet[numexpr];
		for (int i = 0; i < numexpr * 3; i += 3) {
			intervalUnionSets[i / 3] = new IntervalUnionSet(
					numberFactory.newInterval(true, numberFactory.integer(i),
							false, numberFactory.integer(i + 1), false));
		}
		intervalUnionSet = new IntervalUnionSet(true);
		start = System.currentTimeMillis();
		for (int i = 0; i < numexpr; i++) {
			intervalUnionSet = intervalUnionSet.union(intervalUnionSets[i]);

		}
		end = System.currentTimeMillis();
		mark = end - start;
		System.out
				.println("To union ("
						+ numexpr
						+ ") intervalUnionSets with ordered disjoint interval takes: ("
						+ mark + ") Millis");
		//
		intervalUnionSets = new IntervalUnionSet[numexpr];
		for (int i = 0; i < numexpr * 3; i += 3) {
			intervalUnionSets[i / 3] = new IntervalUnionSet(
					numberFactory.newInterval(true, numberFactory.integer(i),
							false, numberFactory.integer(i + 10), false));
		}
		intervalUnionSet = new IntervalUnionSet(true);
		start = System.currentTimeMillis();
		for (int i = 0; i < numexpr; i++) {
			intervalUnionSet = intervalUnionSet.union(intervalUnionSets[i]);

		}
		end = System.currentTimeMillis();
		mark = end - start;
		System.out.println("To union (" + numexpr
				+ ") intervalUnionSets with ordered jointed interval takes: ("
				+ mark + ") Millis");
		//
		intervalUnionSets = new IntervalUnionSet[numexpr];
		for (int i = 0; i < numexpr * 3; i += 3) {
			tempInt1 = (int) (Math.random() * 100);
			tempInt2 = (int) (Math.random() * 1000000);
			intervalUnionSets[i / 3] = new IntervalUnionSet(
					numberFactory.newInterval(true,
							numberFactory.integer(tempInt2), false,
							numberFactory.integer(tempInt2 + tempInt1), false));
		}
		intervalUnionSet = new IntervalUnionSet(true);
		start = System.currentTimeMillis();
		for (int i = 0; i < numexpr; i++) {
			intervalUnionSet = intervalUnionSet.union(intervalUnionSets[i]);

		}
		end = System.currentTimeMillis();
		mark = end - start;
		System.out
				.println("To union ("
						+ numexpr
						+ ") intervalUnionSets with random (mostly disjoint) interval takes: ("
						+ mark + ") Millis");
		//
		intervalUnionSets = new IntervalUnionSet[numexpr];
		for (int i = 0; i < numexpr * 3; i += 3) {
			tempInt1 = (int) (Math.random() * 1000000);
			tempInt2 = (int) (Math.random() * 100000);
			intervalUnionSets[i / 3] = new IntervalUnionSet(
					numberFactory.newInterval(true,
							numberFactory.integer(tempInt2), false,
							numberFactory.integer(tempInt2 + tempInt1), false));
		}
		intervalUnionSet = new IntervalUnionSet(true);
		start = System.currentTimeMillis();
		for (int i = 0; i < numexpr; i++) {
			intervalUnionSet = intervalUnionSet.union(intervalUnionSets[i]);

		}
		end = System.currentTimeMillis();
		mark = end - start;
		System.out
				.println("To union ("
						+ numexpr
						+ ") intervalUnionSets with random (mostly jointed) interval takes: ("
						+ mark + ") Millis");
	}
}
