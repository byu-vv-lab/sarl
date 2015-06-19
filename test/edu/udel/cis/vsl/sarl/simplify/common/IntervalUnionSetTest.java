/*
 * Copyright 2013 Stephen F. Siegel, University of Delaware
 */
package edu.udel.cis.vsl.sarl.simplify.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.number.real.RealInteger;
import edu.udel.cis.vsl.sarl.number.real.RealRational;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

/**
 * @author WenhaoWu
 *
 */
public class IntervalUnionSetTest {
	// Declaration:
	private static boolean showLog = true;
	private static int ARR_SIZE = 15;
	private static PreUniverse universe;
	private static NumberFactory numberFactory = Numbers.REAL_FACTORY;
	private static IntegerNumber INT_ZERO = numberFactory.zeroInteger();
	private static IntegerNumber INT_ONE = numberFactory.integer(1);
	private static IntegerNumber INT_TWO = numberFactory.integer(2);
	private static IntegerNumber INT_THREE = numberFactory.integer(3);
	private static IntegerNumber INT_FOUR = numberFactory.integer(4);
	private static IntegerNumber INT_FIVE = numberFactory.integer(5);
	private static IntegerNumber INT_SIX = numberFactory.integer(6);
	private static IntegerNumber INT_SEVEN = numberFactory.integer(7);
	private static IntegerNumber INT_EIGHT = numberFactory.integer(8);
	private static IntegerNumber INT_NINE = numberFactory.integer(9);
	private static IntegerNumber INT_TEN = numberFactory.integer(10);
	private static IntegerNumber INT_N_ONE = numberFactory.integer(-1);
	private static IntegerNumber INT_N_TWO = numberFactory.integer(-2);
	private static IntegerNumber INT_N_THREE = numberFactory.integer(-3);
	private static IntegerNumber INT_N_FOUR = numberFactory.integer(-4);
	private static IntegerNumber INT_N_FIVE = numberFactory.integer(-5);
	private static IntegerNumber INT_N_SIX = numberFactory.integer(-6);
	private static IntegerNumber INT_N_SEVEN = numberFactory.integer(-7);
	private static IntegerNumber INT_N_EIGHT = numberFactory.integer(-8);
	private static IntegerNumber INT_N_NINE = numberFactory.integer(-9);
	private static IntegerNumber INT_N_TEN = numberFactory.integer(-10);
	private static RationalNumber RAT_ZERO = numberFactory.rational(INT_ZERO);
	private static RationalNumber RAT_ONE = numberFactory.rational(INT_ONE);
	private static RationalNumber RAT_TWO = numberFactory.rational(INT_TWO);
	private static RationalNumber RAT_THREE = numberFactory.rational(INT_THREE);
	private static RationalNumber RAT_FOUR = numberFactory.rational(INT_FOUR);
	private static RationalNumber RAT_FIVE = numberFactory.rational(INT_FIVE);
	private static RationalNumber RAT_SIX = numberFactory.rational(INT_SIX);
	private static RationalNumber RAT_SEVEN = numberFactory.rational(INT_SEVEN);
	private static RationalNumber RAT_EIGHT = numberFactory.rational(INT_EIGHT);
	private static RationalNumber RAT_NINE = numberFactory.rational(INT_NINE);
	private static RationalNumber RAT_TEN = numberFactory.rational(INT_TEN);
	private static RationalNumber RAT_N_ONE = numberFactory.rational(INT_N_ONE);
	private static RationalNumber RAT_N_TWO = numberFactory.rational(INT_N_TWO);
	private static RationalNumber RAT_N_THREE = numberFactory.rational(INT_N_THREE);
	private static RationalNumber RAT_N_FOUR = numberFactory.rational(INT_N_FOUR);
	private static RationalNumber RAT_N_FIVE = numberFactory.rational(INT_N_FIVE);
	private static RationalNumber RAT_N_SIX = numberFactory.rational(INT_N_SIX);
	private static RationalNumber RAT_N_SEVEN = numberFactory.rational(INT_N_SEVEN);
	private static RationalNumber RAT_N_EIGHT = numberFactory.rational(INT_N_EIGHT);
	private static RationalNumber RAT_N_NINE = numberFactory.rational(INT_N_NINE);
	private static RationalNumber RAT_N_TEN = numberFactory.rational(INT_N_TEN);

	private void p(String s) {
		if (showLog) {
			System.out.println(s);
		}
	}

	private void p(Interval... intervals) {
		if (showLog) {
			if (intervals != null) {
				System.out.print("{");
				for (int i = 0; i < intervals.length; i++) {
					if (intervals[i] != null) {
						System.out.print(intervals[i].toString());
					} else {
						System.out.print("null");
					}
					if (i == intervals.length - 1) {
						System.out.print("}\n");
					} else {
						System.out.print(", ");
					}
				}
			}
		}
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FactorySystem system = PreUniverses.newIdealFactorySystem();

		universe = PreUniverses.newPreUniverse(system);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	// Tests
	// Constructor Testings
	@Test
	public void constructIntervalUnionSet_Int_Empty() {
		IntervalUnionSet actual = new IntervalUnionSet(true);

		assertTrue(actual.isEmpty());
		assertTrue(actual.isIntegral());
		p(actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_Rat_Empty() {
		IntervalUnionSet actual = new IntervalUnionSet(false);

		assertTrue(actual.isEmpty());
		assertTrue(!actual.isIntegral());
		p(actual.toString());
	}

	@Test(expected = AssertionError.class)
	public void constructIntervalUnionSet_Number_Int_Null() {
		IntegerNumber nullNum = null;
		IntervalUnionSet actual = new IntervalUnionSet(nullNum);
	}

	@Test(expected = AssertionError.class)
	public void constructIntervalUnionSet_Number_Rat_Null() {
		RationalNumber nullNum = null;
		IntervalUnionSet actual = new IntervalUnionSet(nullNum);
	}

	@Test
	public void constructIntervalUnionSet_Number_Int_Zero() {
		IntervalUnionSet actual = new IntervalUnionSet(INT_ZERO);

		assertTrue(!actual.isEmpty());
		assertTrue(actual.isIntegral());
		p(actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_Number_Rat_Zero() {
		IntervalUnionSet actual = new IntervalUnionSet(RAT_ZERO);

		assertTrue(!actual.isEmpty());
		assertTrue(!actual.isIntegral());
		p(actual.toString());
	}

	@Test(expected = AssertionError.class)
	public void constructIntervalUnionSet_Interval_Null() {
		Interval nullInterval = null;
		IntervalUnionSet actual = new IntervalUnionSet(nullInterval);
	}

	@Test
	public void constructIntervalUnionSet_Interval_Rat_Empty() {
		IntervalUnionSet expected = new IntervalUnionSet(false);
		Interval emptyInterval = numberFactory.newInterval(false, RAT_ZERO, true,
				RAT_ZERO, true);
		IntervalUnionSet actual = new IntervalUnionSet(emptyInterval);

		assertTrue(actual.isEmpty());
		assertTrue(!actual.isIntegral());
		assertEquals(expected.toString(), actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_Interval_Int_Single() {
		IntervalUnionSet expected = new IntervalUnionSet(INT_TEN);
		Interval singleInterval = numberFactory.newInterval(true, INT_TEN, false, INT_TEN,
				false);
		IntervalUnionSet actual = new IntervalUnionSet(singleInterval);

		assertTrue(!actual.isEmpty());
		assertTrue(actual.isIntegral());
		assertEquals(expected.toString(), actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_Interval_Rat_Single() {
		Interval singleInterval = numberFactory.newInterval(false, RAT_N_ONE, false,
				RAT_ONE, true);
		IntervalUnionSet actual = new IntervalUnionSet(singleInterval);

		assertTrue(!actual.isEmpty());
		assertTrue(!actual.isIntegral());
		p(actual.toString());
	}

	@Test(expected = AssertionError.class)
	public void constructIntervalUnionSet_IntervalList_NullList() {
		Interval[] nullArr = null;
		IntervalUnionSet actual = new IntervalUnionSet(nullArr);
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_NullIntervals() {
		// All of intervals in the array are non-<code>null</code> intervals.
		IntervalUnionSet expected = new IntervalUnionSet(false);
		Interval[] nullIntervalArr = new Interval[ARR_SIZE];
		IntervalUnionSet actual = new IntervalUnionSet(nullIntervalArr);

		assertTrue(actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Rat_SomeNull() {
		// All of intervals in the array are non-<code>null</code> intervals.
		Interval[] expectedArr = new Interval[ARR_SIZE];
		Interval[] actualArr = new Interval[ARR_SIZE];

		for (int i = 0; i * 3 < ARR_SIZE && i < 7; i += 2) {
			IntegerNumber int_i = numberFactory.integer(i);
			IntegerNumber int_j = numberFactory.integer(i + 1);

			expectedArr[i] = numberFactory.newInterval(false, int_i, true, int_j, true);
			actualArr[i * 3] = numberFactory.newInterval(false, int_i, true, int_j, true);
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedArr);
		IntervalUnionSet actual = new IntervalUnionSet(actualArr);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(actualArr);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Int_SomeEmpty() {
		// An empty interval cannot occur in the array.
		Interval[] expectedArr = new Interval[ARR_SIZE];
		Interval[] actualArr = new Interval[ARR_SIZE];

		for (int i = 0; i < ARR_SIZE; i++) {
			if (i % 5 == 0) {
				IntegerNumber int_i = numberFactory.integer(i);
				IntegerNumber int_j = numberFactory.integer(i + 2);

				expectedArr[i / 5] = numberFactory.newInterval(true, int_i, false, int_j,
						false);
				actualArr[i] = numberFactory.newInterval(true, int_i, false, int_j, false);
			} else {
				actualArr[i] = numberFactory.emptyIntegerInterval();
			}
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedArr);
		IntervalUnionSet actual = new IntervalUnionSet(actualArr);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(actualArr);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Int_SomeOverlapped() {
		// All of the intervals in the array are disjoint.
		Interval[] actualArr = new Interval[ARR_SIZE];

		actualArr[0] = numberFactory.newInterval(true, null, true, INT_ZERO, false);
		actualArr[Math.min(ARR_SIZE, 4)] = numberFactory.newInterval(true, INT_NINE,
				false, null, true);
		for (int i = 1; i < ARR_SIZE && i < 4; i++) {
			IntegerNumber int_i = numberFactory.integer(i);
			IntegerNumber int_j = numberFactory.integer(i + 5);
			actualArr[i] = numberFactory.newInterval(true, int_i, false, int_j, false);
		}

		IntervalUnionSet expected = new IntervalUnionSet(numberFactory.newInterval(true,
				null, true, null, true));
		IntervalUnionSet actual = new IntervalUnionSet(actualArr);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(actualArr);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Rat_descOrdered() {
		// The intervals in the array are ordered from least to greatest.
		Interval[] expectedArr = new Interval[ARR_SIZE];
		Interval[] actualArr = new Interval[ARR_SIZE];

		for (int i = 0; i < ARR_SIZE; i++) {
			RationalNumber rat_i = numberFactory.rational(numberFactory.integer(i));
			RationalNumber rat_j = numberFactory.rational(numberFactory.integer(i + 1));

			expectedArr[i] = numberFactory.newInterval(false, rat_i, true, rat_j, true);
			actualArr[ARR_SIZE - 1 - i] = numberFactory.newInterval(false, rat_i, true,
					rat_j, true);
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedArr);
		IntervalUnionSet actual = new IntervalUnionSet(actualArr);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(actualArr);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Rat_descOrdered2() {
		// The intervals in the array are ordered from least to greatest.
		Interval[] expectedArr = new Interval[ARR_SIZE];
		Interval[] actualArr = new Interval[ARR_SIZE];

		for (int i = 0; i < ARR_SIZE; i++) {
			RationalNumber rat_i = numberFactory.rational(numberFactory.integer(i));
			RationalNumber rat_j = numberFactory.rational(numberFactory.integer(i + 1));

			expectedArr[i] = numberFactory.newInterval(false, rat_i, false, rat_j, false);
			actualArr[ARR_SIZE - 1 - i] = numberFactory.newInterval(false, rat_i, false,
					rat_j, false);
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedArr);
		IntervalUnionSet actual = new IntervalUnionSet(actualArr);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(actualArr);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Rat_descOrdered3() {
		// The intervals in the array are ordered from least to greatest.
		Interval[] expectedArr = new Interval[ARR_SIZE];
		Interval[] actualArr = new Interval[ARR_SIZE];

		for (int i = 0; i < ARR_SIZE; i++) {
			RationalNumber rat_i = numberFactory.rational(numberFactory.integer(i));
			RationalNumber rat_j = numberFactory.rational(numberFactory.integer(i + 1));

			expectedArr[i] = numberFactory.newInterval(false, rat_i, true, rat_j, false);
			actualArr[ARR_SIZE - 1 - i] = numberFactory.newInterval(false, rat_i, true,
					rat_j, false);
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedArr);
		IntervalUnionSet actual = new IntervalUnionSet(actualArr);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(actualArr);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Rat_descOrdered4() {
		// The intervals in the array are ordered from least to greatest.
		Interval[] expectedArr = new Interval[ARR_SIZE];
		Interval[] actualArr = new Interval[ARR_SIZE];

		for (int i = 0; i < ARR_SIZE; i++) {
			IntegerNumber int_i = numberFactory.integer(i);
			IntegerNumber int_j = numberFactory.integer(i);

			expectedArr[i] = numberFactory.newInterval(true, int_i, false, int_j, false);
			actualArr[ARR_SIZE - 1 - i] = numberFactory.newInterval(true, int_i, false,
					int_j, false);
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedArr);
		IntervalUnionSet actual = new IntervalUnionSet(actualArr);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(actualArr);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Rat_Adjacent() {
		/*
		 * If {a,b} and {b,c} are two consecutive intervals in the list, the the
		 * first one must be open on the right and the second one must be open
		 * on the left.
		 */
		Interval[] expectedArr = new Interval[ARR_SIZE];
		Interval[] actualArr = new Interval[ARR_SIZE];

		expectedArr[0] = numberFactory.newInterval(false, RAT_ZERO, false, RAT_TEN, true);
		for (int i = 0; i < ARR_SIZE && i < 10; i++) {
			RationalNumber rat_i = numberFactory.rational(numberFactory.integer(i));
			RationalNumber rat_j = numberFactory.rational(numberFactory.integer(i + 1));

			actualArr[i] = numberFactory.newInterval(false, rat_i, false, rat_j, true);
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedArr);
		IntervalUnionSet actual = new IntervalUnionSet(actualArr);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(actualArr);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test(expected = AssertionError.class)
	public void constructIntervalUnionSet_IntervalList_AssortedType() {
		/*
		 * If the range set has integer type, all of the intervals are integer
		 * intervals. If it has real type, all of the intervals are real
		 * intervals.
		 */
		Interval[] expectedArr = new Interval[ARR_SIZE];
		Interval[] actualArr = new Interval[ARR_SIZE];

		for (int i = 0; i < ARR_SIZE; i++) {
			if (i % 2 == 0) {
				IntegerNumber int_i = numberFactory.integer(i);
				IntegerNumber int_j = numberFactory.integer(i + 1);

				expectedArr[i / 2] = numberFactory.newInterval(true, int_i, false, int_j,
						false);
				actualArr[i] = numberFactory.newInterval(true, int_i, false, int_j, false);
			} else {
				RationalNumber rat_ni = numberFactory.rational(numberFactory.integer(-i));
				RationalNumber rat_nj = numberFactory.rational(numberFactory.integer(-i + 1));

				actualArr[i] = numberFactory
						.newInterval(false, rat_ni, true, rat_nj, true);
			}
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedArr);
		IntervalUnionSet actual = new IntervalUnionSet(actualArr);
	}

	@Test
	public void constructIntervalUnionSet_Int_ManyIntervals_HoldInvariants() {
		Interval[] expectedArr = new Interval[ARR_SIZE];
		Interval[] actualArr = new Interval[ARR_SIZE];
		IntegerNumber int_max = numberFactory.integer(ARR_SIZE - 1);

		expectedArr[0] = numberFactory.newInterval(true, null, true, null, true);
		actualArr[0] = numberFactory.newInterval(true, null, true, INT_ZERO, false);
		actualArr[ARR_SIZE - 1] = numberFactory.newInterval(true, int_max, false, null,
				true);
		for (int i = 1; i < ARR_SIZE - 1; i++) {
			IntegerNumber int_i = numberFactory.integer(i);

			actualArr[i] = numberFactory.newInterval(true, int_i, false, int_i, false);
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedArr);
		IntervalUnionSet actual = new IntervalUnionSet(actualArr);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(actualArr);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Complicated1() {
		Interval[] expectedArr = new Interval[ARR_SIZE];
		Interval[] actualArr = new Interval[ARR_SIZE];
		IntegerNumber int_a = numberFactory.integer(ARR_SIZE);
		IntegerNumber int_b = numberFactory.integer(ARR_SIZE + 2);
		IntegerNumber int_c = numberFactory.integer(ARR_SIZE + 6);

		expectedArr[0] = numberFactory.newInterval(true, INT_ZERO, false, int_a, false);
		expectedArr[1] = numberFactory.newInterval(true, int_b, false, int_c, false);
		actualArr[0] = numberFactory.newInterval(true, INT_ZERO, false, int_a, false);
		actualArr[ARR_SIZE - 1] = numberFactory.newInterval(true, int_b, false, int_c,
				true);
		for (int i = 1; i < ARR_SIZE - 1; i++) {
			IntegerNumber int_i = numberFactory.integer(i);

			actualArr[i] = numberFactory.newInterval(true, int_i, false, int_i, false);
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedArr);
		IntervalUnionSet actual = new IntervalUnionSet(actualArr);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(actualArr);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Complicated2() {
		Interval[] expectedArr = new Interval[ARR_SIZE];
		Interval[] actualArr = new Interval[ARR_SIZE];
		IntegerNumber int_x = numberFactory.integer(ARR_SIZE + 2);
		IntegerNumber int_y = numberFactory.integer(ARR_SIZE + 4);
		IntegerNumber int_z = numberFactory.integer(ARR_SIZE + 6);

		expectedArr[0] = numberFactory.newInterval(true, INT_ZERO, false, int_y, false);
		expectedArr[1] = numberFactory.newInterval(true, int_x, false, int_z, false);
		actualArr[0] = numberFactory.newInterval(true, INT_ZERO, false, int_y, false);
		actualArr[ARR_SIZE - 1] = numberFactory.newInterval(true, int_x, false, int_z,
				true);
		for (int i = 1; i < ARR_SIZE - 1; i++) {
			IntegerNumber int_i = numberFactory.integer(i);

			actualArr[i] = numberFactory.newInterval(true, int_i, false, int_i, false);
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedArr);
		IntervalUnionSet actual = new IntervalUnionSet(actualArr);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(actualArr);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Complicated3() {
		Interval[] expectedArr = new Interval[ARR_SIZE];
		Interval[] actualArr = new Interval[ARR_SIZE];
		IntegerNumber int_x = numberFactory.integer(ARR_SIZE + 5);
		IntegerNumber int_y = numberFactory.integer(ARR_SIZE + 11);
		IntegerNumber int_z = numberFactory.integer(ARR_SIZE + 20);
		IntegerNumber int_a = numberFactory.integer(ARR_SIZE + 22);
		IntegerNumber int_b = numberFactory.integer(ARR_SIZE + 24);
		IntegerNumber int_c = numberFactory.integer(ARR_SIZE + 25);

		expectedArr[0] = numberFactory
				.newInterval(true, INT_THREE, false, INT_FOUR, false);
		expectedArr[1] = numberFactory.newInterval(true, INT_SIX, false, int_a, false);
		expectedArr[2] = numberFactory.newInterval(true, int_b, false, int_c, false);

		actualArr[0] = numberFactory.newInterval(true, INT_EIGHT, false, int_y, false);
		actualArr[ARR_SIZE - 1] = numberFactory.newInterval(true, int_x, false, int_z,
				false);
		for (int i = 3; i / 3 < ARR_SIZE - 1; i += 3) {
			IntegerNumber int_i = numberFactory.integer(i);
			IntegerNumber int_j = numberFactory.integer(i + 1);

			actualArr[i / 3] = numberFactory.newInterval(true, int_i, false, int_j, false);
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedArr);
		IntervalUnionSet actual = new IntervalUnionSet(actualArr);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(actualArr);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Complicated4() {
		Interval[] expectedArr = new Interval[ARR_SIZE];
		Interval[] actualArr = new Interval[ARR_SIZE];
		RationalNumber rat_x = numberFactory.rational(numberFactory.integer(ARR_SIZE + 24));
		RationalNumber rat_a = numberFactory.rational(numberFactory.integer(ARR_SIZE + 25));

		expectedArr[0] = numberFactory.newInterval(false, RAT_ZERO, false, rat_a, false);

		actualArr[ARR_SIZE - 1] = numberFactory.newInterval(false, RAT_ONE, true, rat_x,
				true);
		for (int i = 0; i / 3 < ARR_SIZE - 1; i += 3) {
			RationalNumber rat_i = numberFactory.rational(numberFactory.integer(i));
			RationalNumber rat_j = numberFactory.rational(numberFactory.integer(i + 1));

			actualArr[i / 3] = numberFactory
					.newInterval(false, rat_i, false, rat_j, false);
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedArr);
		IntervalUnionSet actual = new IntervalUnionSet(actualArr);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(actualArr);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test(expected = NullPointerException.class)
	public void constructIntervalUnionSet_IntervalUnionSet_Null() {
		IntervalUnionSet nullIntervalUnionSet = null;
		IntervalUnionSet actual = new IntervalUnionSet(nullIntervalUnionSet);
	}

	@Test
	public void constructIntervalUnionSet_IntervalUnionSet_Empty() {
		IntervalUnionSet expected = new IntervalUnionSet(false);
		IntervalUnionSet actual = new IntervalUnionSet(expected);

		assertTrue(actual.isEmpty());
		assertTrue(!actual.isIntegral());
		assertEquals(expected.toString(), actual.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalUnionSet_Int_Simple() {
		Interval intInterval = numberFactory.newInterval(true, INT_N_TEN, false, INT_TEN,
				false);
		IntervalUnionSet expected = new IntervalUnionSet(intInterval);
		IntervalUnionSet actual = new IntervalUnionSet(expected);

		assertTrue(!actual.isEmpty());
		assertTrue(actual.isIntegral());
		assertEquals(expected.toString(), actual.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_Rat_ComplicatedIntervalUnionSet() {
		Interval[] expectedArr = new Interval[ARR_SIZE];

		for (int i = 1; i < ARR_SIZE - 1; i += 3) {
			IntegerNumber int_i = numberFactory.integer(i);
			IntegerNumber int_j = numberFactory.integer(i + 1);
			expectedArr[i] = numberFactory.newInterval(true, int_i, false, int_j, false);
		}
		IntervalUnionSet expected = new IntervalUnionSet(expectedArr);
		IntervalUnionSet actual = new IntervalUnionSet(expected);

		assertEquals(expected.toString(), actual.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void addNumber() {

	}

	@Test(expected = AssertionError.class)
	public void union_IntervalUnionSet_Null() {
		IntervalUnionSet nullIntervalUnionSet = null;
		IntervalUnionSet original = new IntervalUnionSet(true);
		IntervalUnionSet actual = original.union(nullIntervalUnionSet);
	}

	@Test
	public void union_IntervalUnionSet_Empty() {
		IntervalUnionSet emptyIntervalUnionSet = new IntervalUnionSet(false);
		IntervalUnionSet original = new IntervalUnionSet(numberFactory.newInterval(false,
				RAT_N_ONE, true, RAT_ONE, true));
		IntervalUnionSet expected = original;
		IntervalUnionSet actual = original.union(emptyIntervalUnionSet);

		assertEquals(expected.toString(), actual.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void union_IntervalUnionSet_Univ() {
		IntervalUnionSet univIntervalUnionSet = new IntervalUnionSet(numberFactory.newInterval(true,
				null, true, null, true));
		IntervalUnionSet original = new IntervalUnionSet(numberFactory.newInterval(true,
				INT_N_ONE, false, INT_ONE, false));
		IntervalUnionSet expected = univIntervalUnionSet;
		IntervalUnionSet actual = original.union(univIntervalUnionSet);

		assertEquals(expected.toString(), actual.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void union_IntervalUnionSet_Self() {
		IntervalUnionSet original = new IntervalUnionSet(numberFactory.newInterval(false,
				RAT_N_ONE, true, RAT_ONE, true));
		IntervalUnionSet expected = original;
		IntervalUnionSet actual = original.union(original);

		assertEquals(expected.toString(), actual.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void union_IntervalUnionSet_Self_Empty() {
		IntervalUnionSet original = new IntervalUnionSet(
				numberFactory.emptyIntegerInterval());
		IntervalUnionSet expected = original;
		IntervalUnionSet actual = original.union(original);

		assertEquals(expected.toString(), actual.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void union_IntervalUnionSet_Self_Univ() {
		IntervalUnionSet original = new IntervalUnionSet(numberFactory.newInterval(true,
				null, true, null, true));
		IntervalUnionSet expected = original;
		IntervalUnionSet actual = original.union(original);

		assertEquals(expected.toString(), actual.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}
}
