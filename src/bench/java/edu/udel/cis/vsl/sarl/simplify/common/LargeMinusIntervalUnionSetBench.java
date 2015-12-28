package edu.udel.cis.vsl.sarl.simplify.common;

import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.number.Numbers;

public class LargeMinusIntervalUnionSetBench {

	public static void main(String[] args) {
		long start;
		long end;
		long mark;
		int tempInt1;
		int tempInt2;
		int numexpr;
		NumberFactory numberFactory = Numbers.REAL_FACTORY;
		Interval int_univ = numberFactory.newInterval(true, null, true, null,
				true);
		IntervalUnionSet[] intervalUnionSets;
		IntervalUnionSet intervalUnionSet;

		//
		numexpr = 10000;
		intervalUnionSets = new IntervalUnionSet[numexpr];
		for (int i = 0; i < numexpr * 3; i += 3) {
			intervalUnionSets[i / 3] = new IntervalUnionSet(
					numberFactory.newInterval(true, numberFactory.integer(i),
							false, numberFactory.integer(i + 1), false));
		}
		intervalUnionSet = new IntervalUnionSet(int_univ);
		start = System.currentTimeMillis();
		for (int i = 0; i < numexpr; i++) {
			intervalUnionSet = intervalUnionSet.minus(intervalUnionSets[i]);

		}
		end = System.currentTimeMillis();
		mark = end - start;
		// System.out.println(intervalUnionSet.toString());
		System.out
				.println("To minus ("
						+ numexpr
						+ ") intervalUnionSets with ordered disjointed interval takes: ("
						+ mark + ") Millis");
		//
		intervalUnionSets = new IntervalUnionSet[numexpr];
		for (int i = 0; i < numexpr * 3; i += 3) {
			intervalUnionSets[i / 3] = new IntervalUnionSet(
					numberFactory.newInterval(true, numberFactory.integer(i),
							false, numberFactory.integer(i + 10), false));
		}
		intervalUnionSet = new IntervalUnionSet(int_univ);
		start = System.currentTimeMillis();
		for (int i = 0; i < numexpr; i++) {
			intervalUnionSet = intervalUnionSet.minus(intervalUnionSets[i]);

		}
		end = System.currentTimeMillis();
		mark = end - start;
		// System.out.println(intervalUnionSet.toString());
		System.out.println("To minus (" + numexpr
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
		intervalUnionSet = new IntervalUnionSet(int_univ);
		start = System.currentTimeMillis();
		for (int i = 0; i < numexpr; i++) {
			intervalUnionSet = intervalUnionSet.minus(intervalUnionSets[i]);

		}
		end = System.currentTimeMillis();
		mark = end - start;
		// System.out.println(intervalUnionSet.toString());
		System.out
				.println("To minus ("
						+ numexpr
						+ ") intervalUnionSets with random (mostly disjointed) interval takes: ("
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
		intervalUnionSet = new IntervalUnionSet(int_univ);
		start = System.currentTimeMillis();
		for (int i = 0; i < numexpr; i++) {
			intervalUnionSet = intervalUnionSet.minus(intervalUnionSets[i]);

		}
		end = System.currentTimeMillis();
		mark = end - start;
		// System.out.println(intervalUnionSet.toString());
		System.out
				.println("To minus ("
						+ numexpr
						+ ") intervalUnionSets with random (mostly jointed) interval takes: ("
						+ mark + ") Millis");
	}

}
