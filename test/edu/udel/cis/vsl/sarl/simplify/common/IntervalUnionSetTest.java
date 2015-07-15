/*
 * Copyright 2013 Stephen F. Siegel, University of Delaware
 */
package edu.udel.cis.vsl.sarl.simplify.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.number.Numbers;
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
	private static Interval INT_EMPTY = numberFactory.emptyIntegerInterval();
	private static Interval RAT_EMPTY = numberFactory.emptyRealInterval();
	private static Interval INT_UNIV = numberFactory.newInterval(true, null,
			true, null, true);
	private static Interval RAT_UNIV = numberFactory.newInterval(false, null,
			true, null, true);
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
	private static RationalNumber RAT_N_THREE = numberFactory
			.rational(INT_N_THREE);
	private static RationalNumber RAT_N_FOUR = numberFactory
			.rational(INT_N_FOUR);
	private static RationalNumber RAT_N_FIVE = numberFactory
			.rational(INT_N_FIVE);
	private static RationalNumber RAT_N_SIX = numberFactory.rational(INT_N_SIX);
	private static RationalNumber RAT_N_SEVEN = numberFactory
			.rational(INT_N_SEVEN);
	private static RationalNumber RAT_N_EIGHT = numberFactory
			.rational(INT_N_EIGHT);
	private static RationalNumber RAT_N_NINE = numberFactory
			.rational(INT_N_NINE);
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
		Interval emptyInterval = RAT_EMPTY;
		IntervalUnionSet actual = new IntervalUnionSet(emptyInterval);

		assertTrue(actual.isEmpty());
		assertTrue(!actual.isIntegral());
		assertEquals(expected.toString(), actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_Interval_Int_Single() {
		IntervalUnionSet expected = new IntervalUnionSet(INT_TEN);
		Interval singleInterval = numberFactory.newInterval(true, INT_TEN,
				false, INT_TEN, false);
		IntervalUnionSet actual = new IntervalUnionSet(singleInterval);

		assertTrue(!actual.isEmpty());
		assertTrue(actual.isIntegral());
		assertEquals(expected.toString(), actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_Interval_Rat_Single() {
		Interval singleInterval = numberFactory.newInterval(false, RAT_N_ONE,
				false, RAT_ONE, true);
		IntervalUnionSet actual = new IntervalUnionSet(singleInterval);

		assertTrue(!actual.isEmpty());
		assertTrue(!actual.isIntegral());
		p(actual.toString());
	}

	@Test(expected = AssertionError.class)
	public void constructIntervalUnionSet_IntervalList_NullList() {
		Interval[] nullList = null;
		IntervalUnionSet actual = new IntervalUnionSet(nullList);
	}

	@Test(expected = AssertionError.class)
	public void constructIntervalUnionSet_IntervalList_EmptyList() {
		Interval[] emptyList = new Interval[0];
		IntervalUnionSet actual = new IntervalUnionSet(emptyList);
	}

	@Test(expected = AssertionError.class)
	public void constructIntervalUnionSet_IntervalList_Int_mismatchedType() {
		Interval int_zeroInterval = numberFactory.newInterval(true, INT_ZERO,
				false, INT_ZERO, false);
		Interval rat_zeroInterval = numberFactory.newInterval(false, INT_ZERO,
				false, INT_ZERO, false);

		Interval[] intList = { int_zeroInterval, rat_zeroInterval };
		IntervalUnionSet actual = new IntervalUnionSet(intList);
	}

	@Test(expected = AssertionError.class)
	public void constructIntervalUnionSet_IntervalList_Rat_mismatchedType() {
		Interval int_zeroInterval = numberFactory.newInterval(true, INT_ZERO,
				false, INT_ZERO, false);
		Interval rat_zeroInterval = numberFactory.newInterval(false, INT_ZERO,
				false, INT_ZERO, false);

		Interval[] intList = { rat_zeroInterval, int_zeroInterval };
		IntervalUnionSet actual = new IntervalUnionSet(intList);
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_NullIntervals() {
		// All of intervals in the array are non-<code>null</code> intervals.
		IntervalUnionSet expected = new IntervalUnionSet(false);
		Interval[] nullIntervalList = new Interval[ARR_SIZE];
		IntervalUnionSet actual = new IntervalUnionSet(nullIntervalList);

		assertTrue(actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_EpmtyIntervals() {
		// All of intervals in the array are non-<code>null</code> intervals.
		IntervalUnionSet expected = new IntervalUnionSet(true);
		Interval[] emptyIntervalList = new Interval[ARR_SIZE];
		for (int i = 0; i < ARR_SIZE; i++) {
			emptyIntervalList[i] = INT_EMPTY;
		}
		IntervalUnionSet actual = new IntervalUnionSet(emptyIntervalList);

		assertTrue(actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_UnivIntervals() {
		// All of intervals in the array are non-<code>null</code> intervals.
		IntervalUnionSet expected = new IntervalUnionSet(RAT_UNIV);
		Interval[] univIntervalList = new Interval[ARR_SIZE];
		for (int i = 0; i < ARR_SIZE; i++) {
			univIntervalList[i] = RAT_UNIV;
		}
		IntervalUnionSet actual = new IntervalUnionSet(univIntervalList);

		// assertFalse(actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_SomeUnivIntervals() {
		// All of intervals in the array are non-<code>null</code> intervals.
		IntervalUnionSet expected = new IntervalUnionSet(RAT_UNIV);
		Interval[] univIntervalList = new Interval[ARR_SIZE];
		for (int i = 0; i < ARR_SIZE; i++) {
			if (i % 5 == 3) {
				univIntervalList[i] = RAT_UNIV;
			} else {
				univIntervalList[i] = numberFactory.newInterval(false,
						RAT_N_ONE, true, RAT_ONE, true);
			}
		}
		IntervalUnionSet actual = new IntervalUnionSet(univIntervalList);

		// assertFalse(actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Rat_SomeNull() {
		// All of intervals in the array are non-<code>null</code> intervals.
		Interval[] expectedList = new Interval[ARR_SIZE];
		Interval[] list = new Interval[ARR_SIZE];

		for (int i = 0; i * 3 < ARR_SIZE && i < 7; i += 2) {
			RationalNumber rat_i = numberFactory.rational(numberFactory
					.integer(i));
			RationalNumber rat_j = numberFactory.rational(numberFactory
					.integer(i + 1));

			expectedList[i] = numberFactory.newInterval(false, rat_i, true,
					rat_j, true);
			list[i * 3] = numberFactory.newInterval(false, rat_i, true, rat_j,
					true);
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedList);
		IntervalUnionSet actual = new IntervalUnionSet(list);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(list);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Int_SomeNull() {
		// All of intervals in the array are non-<code>null</code> intervals.
		Interval[] expectedList = new Interval[ARR_SIZE];
		Interval[] list = new Interval[ARR_SIZE];

		for (int i = 0; i * 3 < ARR_SIZE && i < 7; i += 2) {
			IntegerNumber int_i = numberFactory.integer(i);
			IntegerNumber int_j = numberFactory.integer(i + 1);

			expectedList[i] = numberFactory.newInterval(true, int_i, false,
					int_j, false);
			list[i * 3] = numberFactory.newInterval(true, int_i, false, int_j,
					false);
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedList);
		IntervalUnionSet actual = new IntervalUnionSet(list);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(list);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Int_SomeEmpty() {
		// An empty interval cannot occur in the array.
		Interval[] expectedList = new Interval[ARR_SIZE];
		Interval[] list = new Interval[ARR_SIZE];

		for (int i = 0; i < ARR_SIZE; i++) {
			if (i % 5 == 0) {
				IntegerNumber int_i = numberFactory.integer(i);
				IntegerNumber int_j = numberFactory.integer(i + 2);

				expectedList[i / 5] = numberFactory.newInterval(true, int_i,
						false, int_j, false);
				list[i] = numberFactory.newInterval(true, int_i, false, int_j,
						false);
			} else {
				list[i] = INT_EMPTY;
			}
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedList);
		IntervalUnionSet actual = new IntervalUnionSet(list);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(list);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Int_SomeOverlapped() {
		// All of the intervals in the array are disjoint.
		Interval[] list = new Interval[ARR_SIZE];

		list[0] = numberFactory.newInterval(true, null, true, INT_ZERO, false);
		list[Math.min(ARR_SIZE, 4)] = numberFactory.newInterval(true, INT_NINE,
				false, null, true);
		for (int i = 1; i < ARR_SIZE && i < 4; i++) {
			IntegerNumber int_i = numberFactory.integer(i);
			IntegerNumber int_j = numberFactory.integer(i + 5);
			list[i] = numberFactory.newInterval(true, int_i, false, int_j,
					false);
		}

		IntervalUnionSet expected = new IntervalUnionSet(INT_UNIV);
		IntervalUnionSet actual = new IntervalUnionSet(list);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(list);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Rat_descOrdered() {
		// The intervals in the array are ordered from least to greatest.
		Interval[] expectedList = new Interval[ARR_SIZE];
		Interval[] list = new Interval[ARR_SIZE];

		for (int i = 0; i < ARR_SIZE; i++) {
			RationalNumber rat_i = numberFactory.rational(numberFactory
					.integer(i));
			RationalNumber rat_j = numberFactory.rational(numberFactory
					.integer(i + 1));

			expectedList[i] = numberFactory.newInterval(false, rat_i, true,
					rat_j, true);
			list[ARR_SIZE - 1 - i] = numberFactory.newInterval(false, rat_i,
					true, rat_j, true);
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedList);
		IntervalUnionSet actual = new IntervalUnionSet(list);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(list);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Rat_descOrdered2() {
		// The intervals in the array are ordered from least to greatest.
		Interval[] expectedList = new Interval[ARR_SIZE];
		Interval[] list = new Interval[ARR_SIZE];

		for (int i = 0; i < ARR_SIZE; i++) {
			RationalNumber rat_i = numberFactory.rational(numberFactory
					.integer(i));
			RationalNumber rat_j = numberFactory.rational(numberFactory
					.integer(i + 1));

			expectedList[i] = numberFactory.newInterval(false, rat_i, false,
					rat_j, false);
			list[ARR_SIZE - 1 - i] = numberFactory.newInterval(false, rat_i,
					false, rat_j, false);
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedList);
		IntervalUnionSet actual = new IntervalUnionSet(list);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(list);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Rat_descOrdered3() {
		// The intervals in the array are ordered from least to greatest.
		Interval[] expectedList = new Interval[ARR_SIZE];
		Interval[] list = new Interval[ARR_SIZE];

		for (int i = 0; i < ARR_SIZE; i++) {
			RationalNumber rat_i = numberFactory.rational(numberFactory
					.integer(i));
			RationalNumber rat_j = numberFactory.rational(numberFactory
					.integer(i + 1));

			expectedList[i] = numberFactory.newInterval(false, rat_i, true,
					rat_j, false);
			list[ARR_SIZE - 1 - i] = numberFactory.newInterval(false, rat_i,
					true, rat_j, false);
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedList);
		IntervalUnionSet actual = new IntervalUnionSet(list);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(list);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Rat_descOrdered4() {
		// The intervals in the array are ordered from least to greatest.
		Interval[] expectedList = new Interval[ARR_SIZE];
		Interval[] list = new Interval[ARR_SIZE];

		for (int i = 0; i < ARR_SIZE; i++) {
			IntegerNumber int_i = numberFactory.integer(i);
			IntegerNumber int_j = numberFactory.integer(i);

			expectedList[i] = numberFactory.newInterval(true, int_i, false,
					int_j, false);
			list[ARR_SIZE - 1 - i] = numberFactory.newInterval(true, int_i,
					false, int_j, false);
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedList);
		IntervalUnionSet actual = new IntervalUnionSet(list);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(list);
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
		Interval[] expectedList = new Interval[ARR_SIZE];
		Interval[] list = new Interval[ARR_SIZE];

		expectedList[0] = numberFactory.newInterval(false, RAT_ZERO, false,
				RAT_TEN, true);
		for (int i = 0; i < ARR_SIZE && i < 10; i++) {
			RationalNumber rat_i = numberFactory.rational(numberFactory
					.integer(i));
			RationalNumber rat_j = numberFactory.rational(numberFactory
					.integer(i + 1));

			list[i] = numberFactory.newInterval(false, rat_i, false, rat_j,
					true);
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedList);
		IntervalUnionSet actual = new IntervalUnionSet(list);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(list);
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
		Interval[] expectedList = new Interval[ARR_SIZE];
		Interval[] list = new Interval[ARR_SIZE];

		for (int i = 0; i < ARR_SIZE; i++) {
			if (i % 2 == 0) {
				IntegerNumber int_i = numberFactory.integer(i);
				IntegerNumber int_j = numberFactory.integer(i + 1);

				expectedList[i / 2] = numberFactory.newInterval(true, int_i,
						false, int_j, false);
				list[i] = numberFactory.newInterval(true, int_i, false, int_j,
						false);
			} else {
				RationalNumber rat_ni = numberFactory.rational(numberFactory
						.integer(-i));
				RationalNumber rat_nj = numberFactory.rational(numberFactory
						.integer(-i + 1));

				list[i] = numberFactory.newInterval(false, rat_ni, true,
						rat_nj, true);
			}
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedList);
		IntervalUnionSet actual = new IntervalUnionSet(list);
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_HoldInvariants() {
		Interval[] expectedList = new Interval[ARR_SIZE];
		Interval[] list = new Interval[ARR_SIZE];
		IntegerNumber int_max = numberFactory.integer(ARR_SIZE - 1);

		expectedList[0] = INT_UNIV;
		list[0] = numberFactory.newInterval(true, null, true, INT_ZERO, false);
		list[ARR_SIZE - 1] = numberFactory.newInterval(true, int_max, false,
				null, true);
		for (int i = 1; i < ARR_SIZE - 1; i++) {
			IntegerNumber int_i = numberFactory.integer(i);

			list[i] = numberFactory.newInterval(true, int_i, false, int_i,
					false);
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedList);
		IntervalUnionSet actual = new IntervalUnionSet(list);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(list);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Complicated1() {
		Interval[] expectedList = new Interval[ARR_SIZE];
		Interval[] list = new Interval[ARR_SIZE];
		IntegerNumber int_a = numberFactory.integer(ARR_SIZE);
		IntegerNumber int_b = numberFactory.integer(ARR_SIZE + 2);
		IntegerNumber int_c = numberFactory.integer(ARR_SIZE + 6);

		expectedList[0] = numberFactory.newInterval(true, INT_ZERO, false,
				int_a, false);
		expectedList[1] = numberFactory.newInterval(true, int_b, false, int_c,
				false);
		list[0] = numberFactory
				.newInterval(true, INT_ZERO, false, int_a, false);
		list[ARR_SIZE - 1] = numberFactory.newInterval(true, int_b, false,
				int_c, false);
		for (int i = 1; i < ARR_SIZE - 1; i++) {
			IntegerNumber int_i = numberFactory.integer(i);

			list[i] = numberFactory.newInterval(true, int_i, false, int_i,
					false);
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedList);
		IntervalUnionSet actual = new IntervalUnionSet(list);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(list);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Complicated2() {
		Interval[] expectedList = new Interval[ARR_SIZE];
		Interval[] list = new Interval[ARR_SIZE];
		IntegerNumber int_x = numberFactory.integer(ARR_SIZE + 2);
		IntegerNumber int_y = numberFactory.integer(ARR_SIZE + 4);
		IntegerNumber int_z = numberFactory.integer(ARR_SIZE + 6);

		expectedList[0] = numberFactory.newInterval(true, INT_ZERO, false,
				int_y, false);
		expectedList[1] = numberFactory.newInterval(true, int_x, false, int_z,
				false);
		list[0] = numberFactory
				.newInterval(true, INT_ZERO, false, int_y, false);
		list[ARR_SIZE - 1] = numberFactory.newInterval(true, int_x, false,
				int_z, false);
		for (int i = 1; i < ARR_SIZE - 1; i++) {
			IntegerNumber int_i = numberFactory.integer(i);

			list[i] = numberFactory.newInterval(true, int_i, false, int_i,
					false);
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedList);
		IntervalUnionSet actual = new IntervalUnionSet(list);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(list);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Complicated3() {
		Interval[] expectedList = new Interval[ARR_SIZE];
		Interval[] list = new Interval[ARR_SIZE];
		IntegerNumber int_x = numberFactory.integer(ARR_SIZE + 5);
		IntegerNumber int_y = numberFactory.integer(ARR_SIZE + 11);
		IntegerNumber int_z = numberFactory.integer(ARR_SIZE + 20);
		IntegerNumber int_a = numberFactory.integer(ARR_SIZE + 22);
		IntegerNumber int_b = numberFactory.integer(ARR_SIZE + 24);
		IntegerNumber int_c = numberFactory.integer(ARR_SIZE + 25);

		expectedList[0] = numberFactory.newInterval(true, INT_THREE, false,
				INT_FOUR, false);
		expectedList[1] = numberFactory.newInterval(true, INT_SIX, false,
				int_a, false);
		expectedList[2] = numberFactory.newInterval(true, int_b, false, int_c,
				false);

		list[0] = numberFactory.newInterval(true, INT_EIGHT, false, int_y,
				false);
		list[ARR_SIZE - 1] = numberFactory.newInterval(true, int_x, false,
				int_z, false);
		for (int i = 3; i / 3 < ARR_SIZE - 1; i += 3) {
			IntegerNumber int_i = numberFactory.integer(i);
			IntegerNumber int_j = numberFactory.integer(i + 1);

			list[i / 3] = numberFactory.newInterval(true, int_i, false, int_j,
					false);
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedList);
		IntervalUnionSet actual = new IntervalUnionSet(list);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(list);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Complicated4() {
		Interval[] expectedList = new Interval[ARR_SIZE];
		Interval[] list = new Interval[ARR_SIZE];
		RationalNumber rat_x = numberFactory.rational(numberFactory
				.integer(ARR_SIZE + 24));
		RationalNumber rat_a = numberFactory.rational(numberFactory
				.integer(ARR_SIZE + 25));

		expectedList[0] = numberFactory.newInterval(false, RAT_ZERO, false,
				rat_a, false);

		list[ARR_SIZE - 1] = numberFactory.newInterval(false, RAT_ONE, true,
				rat_x, true);
		for (int i = 0; i / 3 < ARR_SIZE - 1; i += 3) {
			RationalNumber rat_i = numberFactory.rational(numberFactory
					.integer(i));
			RationalNumber rat_j = numberFactory.rational(numberFactory
					.integer(i + 1));

			list[i / 3] = numberFactory.newInterval(false, rat_i, false, rat_j,
					false);
		}

		IntervalUnionSet expected = new IntervalUnionSet(expectedList);
		IntervalUnionSet actual = new IntervalUnionSet(list);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(list);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Complicated5() {
		Interval[] list = new Interval[ARR_SIZE];
		RationalNumber rat_x = numberFactory.rational(numberFactory
				.integer(ARR_SIZE - 1));

		list[0] = numberFactory.newInterval(false, null, true, RAT_ONE, false);

		list[ARR_SIZE - 1] = numberFactory.newInterval(false, rat_x, false,
				null, true);
		for (int i = 1; i < ARR_SIZE - 1; i++) {
			RationalNumber rat_i = numberFactory.rational(numberFactory
					.integer(i));
			RationalNumber rat_j = numberFactory.rational(numberFactory
					.integer(i + 1));

			list[i] = numberFactory.newInterval(false, rat_i, true, rat_j,
					false);
		}

		IntervalUnionSet expected = new IntervalUnionSet(RAT_UNIV);
		IntervalUnionSet actual = new IntervalUnionSet(list);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(list);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Complicated6() {
		Interval[] list = new Interval[ARR_SIZE];
		RationalNumber rat_x = numberFactory.rational(numberFactory
				.integer(ARR_SIZE - 1));

		list[ARR_SIZE - 1] = numberFactory.newInterval(false, null, true,
				RAT_ONE, false);
		list[0] = numberFactory.newInterval(false, rat_x, false, null, true);
		for (int i = 1; i < ARR_SIZE - 1; i++) {
			RationalNumber rat_i = numberFactory.rational(numberFactory
					.integer(i));
			RationalNumber rat_j = numberFactory.rational(numberFactory
					.integer(i + 1));

			list[i] = numberFactory.newInterval(false, rat_i, true, rat_j,
					false);
		}

		IntervalUnionSet expected = new IntervalUnionSet(RAT_UNIV);
		IntervalUnionSet actual = new IntervalUnionSet(list);

		assertTrue(!actual.isEmpty());
		assertEquals(expected.toString(), actual.toString());
		p("The list is :");
		p(list);
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_SameBoundary1() {
		Interval first = numberFactory.newInterval(false, RAT_N_ONE, true,
				RAT_ONE, true);
		Interval second = numberFactory.newInterval(false, RAT_N_ONE, false,
				RAT_ONE, true);
		Interval third = numberFactory.newInterval(false, RAT_N_ONE, true,
				RAT_ONE, false);
		Interval fourth = numberFactory.newInterval(false, RAT_N_ONE, false,
				RAT_ONE, false);
		IntervalUnionSet expected = new IntervalUnionSet(fourth);
		IntervalUnionSet actual1 = new IntervalUnionSet(first, first, second,
				second, third);
		IntervalUnionSet actual2 = new IntervalUnionSet(first, third, third,
				second, fourth);

		assertTrue(!actual1.isEmpty());
		assertTrue(!actual2.isEmpty());
		assertEquals(expected.toString(), actual1.toString());
		assertEquals(expected.toString(), actual2.toString());
		p("expected: " + expected.toString());
		p(" actual1: " + actual1.toString());
		p(" actual2: " + actual2.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_SameBoundary2() {
		Interval before = numberFactory.newInterval(false, RAT_N_NINE, true,
				RAT_N_FOUR, true);
		Interval after = numberFactory.newInterval(false, RAT_FOUR, true,
				RAT_NINE, true);
		Interval first = numberFactory.newInterval(false, RAT_N_ONE, true,
				RAT_ONE, true);
		Interval second = numberFactory.newInterval(false, RAT_N_ONE, false,
				RAT_ONE, true);
		Interval third = numberFactory.newInterval(false, RAT_N_ONE, true,
				RAT_ONE, false);
		Interval fourth = numberFactory.newInterval(false, RAT_N_ONE, false,
				RAT_ONE, false);
		IntervalUnionSet expected = new IntervalUnionSet(before, after, fourth);
		IntervalUnionSet actual1 = new IntervalUnionSet(before, after, first,
				first, second, second, third);
		IntervalUnionSet actual2 = new IntervalUnionSet(before, after, first,
				third, third, second, fourth);

		assertTrue(!actual1.isEmpty());
		assertTrue(!actual2.isEmpty());
		assertEquals(expected.toString(), actual1.toString());
		assertEquals(expected.toString(), actual2.toString());
		p("expected: " + expected.toString());
		p(" actual1: " + actual1.toString());
		p(" actual2: " + actual2.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Coverage1() {
		Interval first = numberFactory.newInterval(false, RAT_N_EIGHT, true,
				RAT_N_SEVEN, true);
		Interval second = numberFactory.newInterval(false, RAT_N_FOUR, true,
				RAT_N_THREE, true);
		Interval third = numberFactory.newInterval(false, RAT_N_ONE, true,
				RAT_ONE, true);
		Interval fourth = numberFactory.newInterval(false, RAT_THREE, true,
				RAT_FOUR, true);
		Interval fourth2 = numberFactory.newInterval(false, RAT_TWO, true,
				RAT_FIVE, true);
		Interval fifth = numberFactory.newInterval(false, RAT_SEVEN, true,
				RAT_EIGHT, true);
		Interval target1 = numberFactory.newInterval(false, RAT_TWO, true,
				RAT_THREE, false);
		Interval target2 = numberFactory.newInterval(false, RAT_FOUR, false,
				RAT_FIVE, true);
		Interval target3 = numberFactory.newInterval(false, RAT_ONE, true,
				RAT_TWO, true);
		Interval[] list = { first, second, third, fourth, fifth, target1,
				target2, target3 };
		IntervalUnionSet expected = new IntervalUnionSet(first, second, third,
				fourth2, target3, fifth);
		IntervalUnionSet actual1 = new IntervalUnionSet(list);

		assertTrue(!actual1.isEmpty());
		assertEquals(expected.toString(), actual1.toString());
		p("The list is :");
		p(list);
		p("expected: " + expected.toString());
		p(" actual1: " + actual1.toString());
	}

	@Test
	public void constructIntervalUnionSet_IntervalList_Coverage2() {
		Interval first = numberFactory.newInterval(false, RAT_N_EIGHT, true,
				RAT_N_SEVEN, true);
		Interval second = numberFactory.newInterval(false, RAT_N_FOUR, true,
				RAT_N_THREE, true);
		Interval third = numberFactory.newInterval(false, RAT_N_ONE, true,
				RAT_ONE, true);
		Interval third2 = numberFactory.newInterval(false, RAT_N_TWO, true,
				RAT_TWO, true);
		Interval fourth = numberFactory.newInterval(false, RAT_THREE, true,
				RAT_FOUR, true);
		Interval fifth = numberFactory.newInterval(false, RAT_SEVEN, true,
				RAT_EIGHT, true);
		Interval target1 = numberFactory.newInterval(false, RAT_N_TWO, true,
				RAT_N_ONE, false);
		Interval target2 = numberFactory.newInterval(false, RAT_ONE, false,
				RAT_TWO, true);
		Interval target3 = numberFactory.newInterval(false, RAT_N_THREE, true,
				RAT_N_TWO, true);
		Interval[] list = { first, second, third, fourth, fifth, target1,
				target2, target3 };
		IntervalUnionSet expected = new IntervalUnionSet(first, second,
				target3, third2, fourth, fifth);
		IntervalUnionSet actual1 = new IntervalUnionSet(list);

		assertTrue(!actual1.isEmpty());
		assertEquals(expected.toString(), actual1.toString());
		p("The list is :");
		p(list);
		p("expected: " + expected.toString());
		p(" actual1: " + actual1.toString());
	}

	@Test(expected = AssertionError.class)
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
		Interval intInterval = numberFactory.newInterval(true, INT_N_TEN,
				false, INT_TEN, false);
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
		Interval[] expectedList = new Interval[ARR_SIZE];

		for (int i = 1; i < ARR_SIZE - 1; i += 3) {
			IntegerNumber int_i = numberFactory.integer(i);
			IntegerNumber int_j = numberFactory.integer(i + 1);
			expectedList[i] = numberFactory.newInterval(true, int_i, false,
					int_j, false);
		}
		IntervalUnionSet expected = new IntervalUnionSet(expectedList);
		IntervalUnionSet actual = new IntervalUnionSet(expected);

		assertEquals(expected.toString(), actual.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test(expected = AssertionError.class)
	public void union_IntervalUnionSet_Null() {
		IntervalUnionSet nullIntervalUnionSet = null;
		IntervalUnionSet original = new IntervalUnionSet(true);
		IntervalUnionSet actual = original.union(nullIntervalUnionSet);
	}

	@Test(expected = AssertionError.class)
	public void union_IntervalUnionSet_Mismatched() {
		IntervalUnionSet rational = new IntervalUnionSet(false);
		IntervalUnionSet integral = new IntervalUnionSet(true);
		IntervalUnionSet actual = integral.union(rational);
	}

	@Test
	public void union_IntervalUnionSet_Empty() {
		IntervalUnionSet emptyRatSet = new IntervalUnionSet(false);
		IntervalUnionSet nonemptyRatSet = new IntervalUnionSet(
				numberFactory
						.newInterval(false, RAT_N_ONE, true, RAT_ONE, true));
		IntervalUnionSet expected = nonemptyRatSet;
		IntervalUnionSet actual1 = nonemptyRatSet.union(emptyRatSet);
		IntervalUnionSet actual2 = emptyRatSet.union(nonemptyRatSet);

		assertEquals(expected.toString(), actual1.toString());
		assertEquals(expected.toString(), actual2.toString());
		p("expected: " + expected.toString());
		p(" actual1: " + actual1.toString());
		p(" actual2: " + actual2.toString());
	}

	@Test
	public void union_IntervalUnionSet_Univ() {
		IntervalUnionSet univIntSet = new IntervalUnionSet(INT_UNIV);
		IntervalUnionSet nonunivIntSet = new IntervalUnionSet(
				numberFactory.newInterval(true, INT_N_ONE, false, INT_ONE,
						false));
		IntervalUnionSet expected = univIntSet;
		IntervalUnionSet actual1 = nonunivIntSet.union(univIntSet);
		IntervalUnionSet actual2 = univIntSet.union(nonunivIntSet);

		assertEquals(expected.toString(), actual1.toString());
		assertEquals(expected.toString(), actual2.toString());
		p("expected: " + expected.toString());
		p(" actual1: " + actual1.toString());
		p(" actual2: " + actual2.toString());
	}

	@Test
	public void union_IntervalUnionSet_Self() {
		IntervalUnionSet original = new IntervalUnionSet(
				numberFactory
						.newInterval(false, RAT_N_ONE, true, RAT_ONE, true));
		IntervalUnionSet expected = original;
		IntervalUnionSet actual = original.union(original);

		assertEquals(expected.toString(), actual.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void union_IntervalUnionSet_Simple_Disjoint_Rat() {
		Interval first1 = numberFactory.newInterval(false, RAT_N_TEN, true,
				RAT_N_EIGHT, true);
		Interval first2 = numberFactory.newInterval(false, RAT_N_EIGHT, true,
				RAT_N_SIX, true);
		Interval second1 = numberFactory.newInterval(false, RAT_N_SIX, true,
				RAT_N_FOUR, true);
		Interval second2 = numberFactory.newInterval(false, RAT_N_FOUR, true,
				RAT_N_TWO, true);
		Interval third1 = numberFactory.newInterval(false, RAT_N_TWO, true,
				RAT_ZERO, true);
		Interval third2 = numberFactory.newInterval(false, RAT_ZERO, true,
				RAT_TWO, true);
		Interval fourth1 = numberFactory.newInterval(false, RAT_TWO, true,
				RAT_FOUR, true);
		Interval fourth2 = numberFactory.newInterval(false, RAT_FOUR, true,
				RAT_SIX, true);
		Interval fifth1 = numberFactory.newInterval(false, RAT_SIX, true,
				RAT_EIGHT, true);
		Interval fifth2 = numberFactory.newInterval(false, RAT_EIGHT, true,
				RAT_TEN, true);
		Interval[] list1 = { first1, second1, third1, fourth1, fifth1 };
		Interval[] list2 = { first2, second2, third2, fourth2, fifth2 };
		Interval[] expectedList = { first1, second1, third1, fourth1, fifth1,
				first2, second2, third2, fourth2, fifth2 };

		IntervalUnionSet set1 = new IntervalUnionSet(list1);
		IntervalUnionSet set2 = new IntervalUnionSet(list2);
		IntervalUnionSet expected = new IntervalUnionSet(expectedList);
		IntervalUnionSet actual = set1.union(set2);

		assertEquals(expected.toString(), actual.toString());
		p("    set1: " + set1.toString());
		p("    set2: " + set2.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void union_IntervalUnionSet_Simple_Adjacent_Rat() {
		Interval first1 = numberFactory.newInterval(false, null, true,
				RAT_N_EIGHT, false);
		Interval first2 = numberFactory.newInterval(false, RAT_N_EIGHT, true,
				RAT_N_SIX, false);
		Interval second1 = numberFactory.newInterval(false, RAT_N_SIX, true,
				RAT_N_FOUR, false);
		Interval second2 = numberFactory.newInterval(false, RAT_N_FOUR, true,
				RAT_N_TWO, false);
		Interval third1 = numberFactory.newInterval(false, RAT_N_TWO, true,
				RAT_ZERO, false);
		Interval third2 = numberFactory.newInterval(false, RAT_ZERO, true,
				RAT_TWO, false);
		Interval fourth1 = numberFactory.newInterval(false, RAT_TWO, true,
				RAT_FOUR, false);
		Interval fourth2 = numberFactory.newInterval(false, RAT_FOUR, true,
				RAT_SIX, false);
		Interval fifth1 = numberFactory.newInterval(false, RAT_SIX, true,
				RAT_EIGHT, false);
		Interval fifth2 = numberFactory.newInterval(false, RAT_EIGHT, true,
				null, true);
		Interval[] list1 = { first1, second1, third1, fourth1, fifth1 };
		Interval[] list2 = { first2, second2, third2, fourth2, fifth2 };

		IntervalUnionSet set1 = new IntervalUnionSet(list1);
		IntervalUnionSet set2 = new IntervalUnionSet(list2);
		IntervalUnionSet expected = new IntervalUnionSet(RAT_UNIV);
		IntervalUnionSet actual = set1.union(set2);

		assertEquals(expected.toString(), actual.toString());
		p("    set1: " + set1.toString());
		p("    set2: " + set2.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void union_IntervalUnionSet_Simple_Adjacent_Int() {
		Interval first1 = numberFactory.newInterval(true, null, true,
				INT_N_TEN, false);
		Interval first2 = numberFactory.newInterval(true, INT_N_NINE, false,
				INT_N_EIGHT, false);
		Interval second1 = numberFactory.newInterval(true, INT_N_SEVEN, false,
				INT_N_FIVE, false);
		Interval second2 = numberFactory.newInterval(true, INT_N_FOUR, false,
				INT_N_TWO, false);
		Interval third1 = numberFactory.newInterval(true, INT_N_ONE, false,
				INT_ZERO, false);
		Interval third2 = numberFactory.newInterval(true, INT_ONE, false,
				INT_ONE, false);
		Interval fourth1 = numberFactory.newInterval(true, INT_TWO, false,
				INT_FOUR, false);
		Interval fourth2 = numberFactory.newInterval(true, INT_FIVE, false,
				INT_SEVEN, false);
		Interval fifth1 = numberFactory.newInterval(true, INT_EIGHT, false,
				INT_NINE, false);
		Interval fifth2 = numberFactory.newInterval(true, INT_TEN, false, null,
				true);
		Interval[] list1 = { first1, second1, third1, fourth1, fifth1 };
		Interval[] list2 = { first2, second2, third2, fourth2, fifth2 };

		IntervalUnionSet set1 = new IntervalUnionSet(list1);
		IntervalUnionSet set2 = new IntervalUnionSet(list2);
		IntervalUnionSet expected = new IntervalUnionSet(INT_UNIV);
		IntervalUnionSet actual = set1.union(set2);

		assertEquals(expected.toString(), actual.toString());
		p("    set1: " + set1.toString());
		p("    set2: " + set2.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void union_IntervalUnionSet_Simple_Overlapped_Rat() {
		Interval first1 = numberFactory.newInterval(false, RAT_N_NINE, false,
				RAT_N_SEVEN, true);
		Interval first2 = numberFactory.newInterval(false, RAT_N_EIGHT, true,
				RAT_N_FIVE, true);
		Interval second1 = numberFactory.newInterval(false, RAT_N_SIX, true,
				RAT_N_THREE, true);
		Interval second2 = numberFactory.newInterval(false, RAT_N_FOUR, true,
				RAT_N_ONE, true);
		Interval third1 = numberFactory.newInterval(false, RAT_N_TWO, true,
				RAT_ONE, true);
		Interval third2 = numberFactory.newInterval(false, RAT_ZERO, true,
				RAT_THREE, true);
		Interval fourth1 = numberFactory.newInterval(false, RAT_TWO, true,
				RAT_FIVE, true);
		Interval fourth2 = numberFactory.newInterval(false, RAT_FOUR, true,
				RAT_SEVEN, true);
		Interval fifth1 = numberFactory.newInterval(false, RAT_SIX, true,
				RAT_NINE, true);
		Interval fifth2 = numberFactory.newInterval(false, RAT_EIGHT, true,
				RAT_NINE, false);
		Interval result = numberFactory.newInterval(false, RAT_N_NINE, false,
				RAT_NINE, false);
		Interval[] list1 = { first1, second1, third1, fourth1, fifth1 };
		Interval[] list2 = { first2, second2, third2, fourth2, fifth2 };

		IntervalUnionSet set1 = new IntervalUnionSet(list1);
		IntervalUnionSet set2 = new IntervalUnionSet(list2);
		IntervalUnionSet expected = new IntervalUnionSet(result);
		IntervalUnionSet actual = set1.union(set2);

		assertEquals(expected.toString(), actual.toString());
		p("    set1: " + set1.toString());
		p("    set2: " + set2.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void union_IntervalUnionSet_Simple_Complicated1_Int() {
		Interval first1 = numberFactory.newInterval(true, INT_N_NINE, false,
				INT_N_ONE, false);
		Interval first2 = numberFactory.newInterval(true, INT_N_TEN, false,
				INT_N_TEN, false);
		Interval second1 = numberFactory.newInterval(true, INT_THREE, false,
				INT_THREE, false);
		Interval second2 = numberFactory.newInterval(true, INT_N_EIGHT, false,
				INT_N_EIGHT, false);
		Interval third1 = numberFactory.newInterval(true, INT_FIVE, false,
				INT_FIVE, false);
		Interval third2 = numberFactory.newInterval(true, INT_N_SIX, false,
				INT_N_SIX, false);
		Interval fourth1 = numberFactory.newInterval(true, INT_SEVEN, false,
				INT_SEVEN, false);
		Interval fourth2 = numberFactory.newInterval(true, INT_N_FOUR, false,
				INT_N_TWO, false);
		Interval fifth1 = numberFactory.newInterval(true, INT_TEN, false,
				INT_TEN, false);
		Interval fifth2 = numberFactory.newInterval(true, INT_ZERO, false,
				INT_NINE, false);
		Interval result = numberFactory.newInterval(true, INT_N_TEN, false,
				INT_TEN, false);
		Interval[] list1 = { first1, second1, third1, fourth1, fifth1 };
		Interval[] list2 = { first2, second2, third2, fourth2, fifth2 };

		IntervalUnionSet set1 = new IntervalUnionSet(list1);
		IntervalUnionSet set2 = new IntervalUnionSet(list2);
		IntervalUnionSet expected = new IntervalUnionSet(result);
		IntervalUnionSet actual = set1.union(set2);

		assertEquals(expected.toString(), actual.toString());
		p("    set1: " + set1.toString());
		p("    set2: " + set2.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void union_IntervalUnionSet_Simple_Complicated2_Int() {
		Interval first1 = numberFactory.newInterval(true, INT_ZERO, false,
				INT_TWO, false);
		Interval first2 = numberFactory.newInterval(true, INT_N_TWO, false,
				INT_N_ONE, false);
		Interval second1 = numberFactory.newInterval(true, INT_FOUR, false,
				INT_FOUR, false);
		Interval second2 = numberFactory.newInterval(true, INT_ONE, false,
				INT_ONE, false);
		Interval third1 = numberFactory.newInterval(true, INT_SIX, false,
				INT_SIX, false);
		Interval third2 = numberFactory.newInterval(true, INT_THREE, false,
				INT_THREE, false);
		Interval fourth1 = numberFactory.newInterval(true, INT_EIGHT, false,
				INT_EIGHT, false);
		Interval fourth2 = numberFactory.newInterval(true, INT_FIVE, false,
				INT_FIVE, false);
		Interval fifth1 = numberFactory.newInterval(true, INT_TEN, false,
				INT_TEN, false);
		Interval result = numberFactory.newInterval(true, INT_N_TWO, false,
				INT_SIX, false);
		Interval[] list1 = { first1, second1, third1, fourth1, fifth1 };
		Interval[] list2 = { first2, second2, third2, fourth2 };

		IntervalUnionSet set1 = new IntervalUnionSet(list1);
		IntervalUnionSet set2 = new IntervalUnionSet(list2);
		IntervalUnionSet expected = new IntervalUnionSet(result, fourth1,
				fifth1);
		IntervalUnionSet actual = set1.union(set2);

		assertEquals(expected.toString(), actual.toString());
		p("    set1: " + set1.toString());
		p("    set2: " + set2.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void union_IntervalUnionSet_Simple_Complicated3_Rat() {
		Interval first1 = numberFactory.newInterval(false, null, true,
				RAT_N_SEVEN, true);
		Interval first2 = numberFactory.newInterval(false, null, true,
				RAT_N_FIVE, true);
		Interval second1 = numberFactory.newInterval(false, RAT_N_SIX, true,
				RAT_N_THREE, true);
		Interval second2 = numberFactory.newInterval(false, RAT_N_FOUR, true,
				RAT_N_ONE, true);
		Interval third1 = numberFactory.newInterval(false, RAT_N_TWO, true,
				null, true);
		Interval third2 = numberFactory.newInterval(false, RAT_ZERO, true,
				null, true);
		Interval[] list1 = { first1, second1, third1 };
		Interval[] list2 = { first2, second2, third2 };

		IntervalUnionSet set1 = new IntervalUnionSet(list1);
		IntervalUnionSet set2 = new IntervalUnionSet(list2);
		IntervalUnionSet expected = new IntervalUnionSet(RAT_UNIV);
		IntervalUnionSet actual = set1.union(set2);

		assertEquals(expected.toString(), actual.toString());
		p("    set1: " + set1.toString());
		p("    set2: " + set2.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void union_IntervalUnionSet_Simple_Complicated4_Rat() {
		Interval first1 = numberFactory.newInterval(false, RAT_N_TEN, true,
				RAT_N_SEVEN, true);
		Interval first2 = numberFactory.newInterval(false, null, true,
				RAT_N_FIVE, true);
		Interval second1 = numberFactory.newInterval(false, RAT_N_SIX, true,
				RAT_N_THREE, true);
		Interval second2 = numberFactory.newInterval(false, RAT_N_FOUR, true,
				RAT_N_ONE, true);
		Interval third1 = numberFactory.newInterval(false, RAT_N_TWO, true,
				null, true);
		Interval third2 = numberFactory.newInterval(false, RAT_ZERO, true,
				RAT_TEN, true);
		Interval[] list1 = { first1, second1, third1 };
		Interval[] list2 = { first2, second2, third2 };

		IntervalUnionSet set1 = new IntervalUnionSet(list1);
		IntervalUnionSet set2 = new IntervalUnionSet(list2);
		IntervalUnionSet expected = new IntervalUnionSet(RAT_UNIV);
		IntervalUnionSet actual = set1.union(set2);

		assertEquals(expected.toString(), actual.toString());
		p("    set1: " + set1.toString());
		p("    set2: " + set2.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test(expected = AssertionError.class)
	public void containsNumber_Int_Null() {
		IntegerNumber nullIntNum = null;
		IntervalUnionSet univIntSet = new IntervalUnionSet(INT_UNIV);

		univIntSet.containsNumber(nullIntNum);
	}

	@Test(expected = AssertionError.class)
	public void containsNumber_Rat_mismatchedType() {
		IntegerNumber intNum = INT_ZERO;
		IntervalUnionSet univRatSet = new IntervalUnionSet(RAT_UNIV);

		univRatSet.containsNumber(intNum);
	}

	@Test(expected = AssertionError.class)
	public void containsNumber_Int_mismatchedType() {
		RationalNumber ratNum = RAT_ZERO;
		IntervalUnionSet univIntSet = new IntervalUnionSet(INT_UNIV);

		univIntSet.containsNumber(ratNum);
	}

	@Test
	public void containsNumber_Int_withEmptySet() {
		IntervalUnionSet emptyIntSet = new IntervalUnionSet(INT_EMPTY);
		boolean actual = emptyIntSet.containsNumber(INT_ONE);

		assert emptyIntSet.isIntegral();
		assert emptyIntSet.isEmpty();
		assertFalse(actual);
		p("   Set: " + emptyIntSet.toString());
		p("Number: " + INT_ONE.toString());
		p("expected: " + "false");
		p("  actual: " + actual);
	}

	@Test
	public void containsNumber_Rat_withEmptySet() {
		IntervalUnionSet emptyRatSet = new IntervalUnionSet(RAT_EMPTY);
		boolean actual = emptyRatSet.containsNumber(RAT_ONE);

		assert !emptyRatSet.isIntegral();
		assert emptyRatSet.isEmpty();
		assertFalse(actual);
		p("   Set: " + emptyRatSet.toString());
		p("Number: " + RAT_ONE.toString());
		p("expected: " + "false");
		p("  actual: " + actual);
	}

	@Test
	public void containsNumber_Int_withUnivSet() {
		IntervalUnionSet univIntSet = new IntervalUnionSet(INT_UNIV);
		boolean actual = univIntSet.containsNumber(INT_ONE);

		assert univIntSet.isIntegral();
		assert !univIntSet.isEmpty();
		assertTrue(actual);
		p("   Set: " + univIntSet.toString());
		p("Number: " + INT_ONE.toString());
		p("expected: " + "true");
		p("  actual: " + actual);
	}

	@Test
	public void containsNumber_Rat_withUnivSet() {
		IntervalUnionSet univRatSet = new IntervalUnionSet(RAT_UNIV);
		boolean actual = univRatSet.containsNumber(RAT_ONE);

		assert !univRatSet.isIntegral();
		assert !univRatSet.isEmpty();
		assertTrue(actual);
		p("   Set: " + univRatSet.toString());
		p("Number: " + RAT_ONE.toString());
		p("expected: " + "true");
		p("  actual: " + actual);
	}

	@Test
	public void containsNumber_Int_Num_LeftDisjoint() {
		Interval first = numberFactory.newInterval(true, INT_N_FIVE, false,
				INT_N_TWO, false);
		Interval second = numberFactory.newInterval(true, INT_ZERO, false,
				INT_ZERO, false);
		Interval third = numberFactory.newInterval(true, INT_TWO, false,
				INT_FIVE, false);
		IntervalUnionSet intervalSet = new IntervalUnionSet(first, second,
				third);
		boolean actual = intervalSet.containsNumber(INT_N_TEN);

		assertFalse(actual);
		p("   Set: " + intervalSet.toString());
		p("Number: " + INT_N_TEN.toString());
		p("expected: " + "false");
		p("  actual: " + actual);
	}

	@Test
	public void containsNumber_Rat_Num_RightDisjoint() {
		Interval first = numberFactory.newInterval(false, RAT_N_FIVE, true,
				RAT_N_TWO, true);
		Interval second = numberFactory.newInterval(false, RAT_ZERO, false,
				RAT_ZERO, false);
		Interval third = numberFactory.newInterval(false, RAT_TWO, true,
				RAT_FIVE, true);
		IntervalUnionSet intervalSet = new IntervalUnionSet(first, second,
				third);
		boolean actual = intervalSet.containsNumber(RAT_TEN);

		assertFalse(actual);
		p("   Set: " + intervalSet.toString());
		p("Number: " + RAT_TEN.toString());
		p("expected: " + "false");
		p("  actual: " + actual);
	}

	@Test
	public void containsNumber_Int_Num_NotContained1() {
		Interval first = numberFactory.newInterval(true, INT_N_FIVE, false,
				INT_N_TWO, false);
		Interval second = numberFactory.newInterval(true, INT_ZERO, false,
				INT_ZERO, false);
		Interval third = numberFactory.newInterval(true, INT_TWO, false,
				INT_FIVE, false);
		IntervalUnionSet intervalSet = new IntervalUnionSet(first, second,
				third);
		boolean actual = intervalSet.containsNumber(INT_ONE);

		assertFalse(actual);
		p("   Set: " + intervalSet.toString());
		p("Number: " + INT_ONE.toString());
		p("expected: " + "false");
		p("  actual: " + actual);
	}

	@Test
	public void containsNumber_Rat_Num_NotContained1() {
		Interval first = numberFactory.newInterval(false, RAT_N_FIVE, true,
				RAT_N_TWO, true);
		Interval second = numberFactory.newInterval(false, RAT_ZERO, false,
				RAT_ZERO, false);
		Interval third = numberFactory.newInterval(false, RAT_TWO, true,
				RAT_FIVE, true);
		IntervalUnionSet intervalSet = new IntervalUnionSet(first, second,
				third);
		boolean actual = intervalSet.containsNumber(RAT_N_FIVE);

		assertFalse(actual);
		p("   Set: " + intervalSet.toString());
		p("Number: " + RAT_N_FIVE.toString());
		p("expected: " + "false");
		p("  actual: " + actual);
	}

	@Test
	public void containsNumber_Rat_Num_NotContained2() {
		Interval first = numberFactory.newInterval(false, RAT_N_FIVE, true,
				RAT_N_TWO, true);
		Interval second = numberFactory.newInterval(false, RAT_ZERO, false,
				RAT_ZERO, false);
		Interval third = numberFactory.newInterval(false, RAT_TWO, true,
				RAT_FIVE, true);
		IntervalUnionSet intervalSet = new IntervalUnionSet(first, second,
				third);
		boolean actual = intervalSet.containsNumber(RAT_FIVE);

		assertFalse(actual);
		p("   Set: " + intervalSet.toString());
		p("Number: " + RAT_FIVE.toString());
		p("expected: " + "false");
		p("  actual: " + actual);
	}

	@Test
	public void containsNumber_Rat_Num_NotContained3() {
		RationalNumber ratNum = numberFactory.divide(RAT_THREE, RAT_TWO);
		Interval first = numberFactory.newInterval(false, RAT_N_FIVE, true,
				RAT_N_TWO, true);
		Interval second = numberFactory.newInterval(false, RAT_ZERO, false,
				RAT_ZERO, false);
		Interval third = numberFactory.newInterval(false, RAT_TWO, true,
				RAT_FIVE, true);
		IntervalUnionSet intervalSet = new IntervalUnionSet(first, second,
				third);
		boolean actual = intervalSet.containsNumber(ratNum);

		assertFalse(actual);
		p("   Set: " + intervalSet.toString());
		p("Number: " + ratNum.toString());
		p("expected: " + "false");
		p("  actual: " + actual);
	}

	@Test
	public void containsNumber_Int_Num_Contained1() {
		Interval first = numberFactory.newInterval(true, null, true,
				INT_N_SEVEN, false);
		Interval second = numberFactory.newInterval(true, INT_N_FIVE, false,
				INT_N_TWO, false);
		Interval third = numberFactory.newInterval(true, INT_ZERO, false,
				INT_ZERO, false);
		Interval fourth = numberFactory.newInterval(true, INT_TWO, false,
				INT_FIVE, false);
		Interval fifth = numberFactory.newInterval(true, INT_SEVEN, false,
				null, true);
		IntervalUnionSet intervalSet = new IntervalUnionSet(first, second,
				third, fourth, fifth);
		boolean actual = intervalSet.containsNumber(INT_N_TEN);

		assertTrue(actual);
		p("   Set: " + intervalSet.toString());
		p("Number: " + INT_N_TEN.toString());
		p("expected: " + "true");
		p("  actual: " + actual);
	}

	@Test
	public void containsNumber_Int_Num_Contained2() {
		Interval first = numberFactory.newInterval(true, null, true,
				INT_N_SEVEN, false);
		Interval second = numberFactory.newInterval(true, INT_N_FIVE, false,
				INT_N_TWO, false);
		Interval third = numberFactory.newInterval(true, INT_ZERO, false,
				INT_ZERO, false);
		Interval fourth = numberFactory.newInterval(true, INT_TWO, false,
				INT_FIVE, false);
		Interval fifth = numberFactory.newInterval(true, INT_SEVEN, false,
				null, true);
		IntervalUnionSet intervalSet = new IntervalUnionSet(first, second,
				third, fourth, fifth);
		boolean actual = intervalSet.containsNumber(INT_ZERO);

		assertTrue(actual);
		p("   Set: " + intervalSet.toString());
		p("Number: " + INT_ZERO.toString());
		p("expected: " + "true");
		p("  actual: " + actual);
	}

	@Test
	public void containsNumber_Rat_Num_Contained1() {
		Interval first = numberFactory.newInterval(false, null, true,
				RAT_N_SEVEN, false);
		Interval second = numberFactory.newInterval(false, RAT_N_FIVE, true,
				RAT_N_TWO, true);
		Interval third = numberFactory.newInterval(false, RAT_ZERO, false,
				RAT_ZERO, false);
		Interval fourth = numberFactory.newInterval(false, RAT_TWO, true,
				RAT_FIVE, true);
		Interval fifth = numberFactory.newInterval(false, RAT_SEVEN, true,
				null, true);
		IntervalUnionSet intervalSet = new IntervalUnionSet(first, second,
				third, fourth, fifth);
		boolean actual = intervalSet.containsNumber(RAT_N_SEVEN);

		assertTrue(actual);
		p("   Set: " + intervalSet.toString());
		p("Number: " + RAT_N_SEVEN.toString());
		p("expected: " + "true");
		p("  actual: " + actual);
	}

	@Test
	public void containsNumber_Rat_Num_Contained2() {
		RationalNumber ratNum = numberFactory.divide(RAT_SEVEN, RAT_TWO);
		Interval first = numberFactory.newInterval(false, null, true,
				RAT_N_SEVEN, false);
		Interval second = numberFactory.newInterval(false, RAT_N_FIVE, true,
				RAT_N_TWO, true);
		Interval third = numberFactory.newInterval(false, RAT_ZERO, false,
				RAT_ZERO, false);
		Interval fourth = numberFactory.newInterval(false, RAT_TWO, true,
				RAT_FIVE, true);
		Interval fifth = numberFactory.newInterval(false, RAT_SEVEN, true,
				null, true);
		IntervalUnionSet intervalSet = new IntervalUnionSet(first, second,
				third, fourth, fifth);
		boolean actual = intervalSet.containsNumber(ratNum);

		assertTrue(actual);
		p("   Set: " + intervalSet.toString());
		p("Number: " + ratNum.toString());
		p("expected: " + "true");
		p("  actual: " + actual);
	}

	@Test(expected = AssertionError.class)
	public void addNumber_Rat_Null() {
		RationalNumber nullRatNum = null;
		IntervalUnionSet univRatSet = new IntervalUnionSet(RAT_UNIV);

		univRatSet.addNumber(nullRatNum);
	}

	@Test(expected = AssertionError.class)
	public void addNumber_Rat_mismatchedType() {
		IntegerNumber intNum = INT_ZERO;
		IntervalUnionSet univRatSet = new IntervalUnionSet(RAT_UNIV);

		univRatSet.addNumber(intNum);
	}

	@Test(expected = AssertionError.class)
	public void addNumber_Int_mismatchedType() {
		RationalNumber ratNum = RAT_ZERO;
		IntervalUnionSet univIntSet = new IntervalUnionSet(INT_UNIV);

		univIntSet.addNumber(ratNum);
	}

	@Test
	public void addNumber_Int_withEmptySet() {
		IntervalUnionSet emptyIntSet = new IntervalUnionSet(INT_EMPTY);
		IntervalUnionSet actual = emptyIntSet.addNumber(INT_ONE);
		IntervalUnionSet expected = new IntervalUnionSet(INT_ONE);

		assertEquals(expected.toString(), actual.toString());
		p("      Set: " + emptyIntSet.toString());
		p("addNumber: " + INT_ONE.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void addNumber_Rat_withEmptySet() {
		IntervalUnionSet emptyRatSet = new IntervalUnionSet(RAT_EMPTY);
		IntervalUnionSet actual = emptyRatSet.addNumber(RAT_ONE);
		IntervalUnionSet expected = new IntervalUnionSet(RAT_ONE);

		assert !actual.isIntegral();
		assertEquals(expected.toString(), actual.toString());
		p("      Set: " + emptyRatSet.toString());
		p("addNumber: " + RAT_ONE.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void addNumber_Int_withUnivSet() {
		IntervalUnionSet univIntSet = new IntervalUnionSet(INT_UNIV);
		IntervalUnionSet actual = univIntSet.addNumber(INT_ONE);
		IntervalUnionSet expected = univIntSet;

		assertEquals(expected.toString(), actual.toString());
		p("      Set: " + univIntSet.toString());
		p("addNumber: " + INT_ONE.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void addNumber_Rat_withUnivSet() {
		IntervalUnionSet univRatSet = new IntervalUnionSet(RAT_UNIV);
		IntervalUnionSet actual = univRatSet.addNumber(RAT_ONE);
		IntervalUnionSet expected = univRatSet;

		assertEquals(expected.toString(), actual.toString());
		p("      Set: " + univRatSet.toString());
		p("addNumber: " + RAT_ONE.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void addNumber_Int_Num_LeftDisjoint() {
		Interval first = numberFactory.newInterval(true, INT_N_FIVE, false,
				INT_N_TWO, false);
		Interval second = numberFactory.newInterval(true, INT_ZERO, false,
				INT_ZERO, false);
		Interval third = numberFactory.newInterval(true, INT_TWO, false,
				INT_FIVE, false);
		Interval target = numberFactory.newInterval(true, INT_N_TEN, false,
				INT_N_TEN, false);
		IntervalUnionSet intervalSet = new IntervalUnionSet(first, second,
				third);
		IntervalUnionSet expected = new IntervalUnionSet(first, second, third,
				target);
		IntervalUnionSet actual = intervalSet.addNumber(INT_N_TEN);

		assertEquals(expected.toString(), actual.toString());
		p("      Set: " + intervalSet.toString());
		p("addNumber: " + INT_N_TEN.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void addNumber_Int_Num_LeftJoint() {
		Interval first = numberFactory.newInterval(true, INT_N_FIVE, false,
				INT_N_TWO, false);
		Interval second = numberFactory.newInterval(true, INT_ZERO, false,
				INT_ZERO, false);
		Interval third = numberFactory.newInterval(true, INT_TWO, false,
				INT_FIVE, false);
		Interval target = numberFactory.newInterval(true, INT_N_SIX, false,
				INT_N_SIX, false);
		IntervalUnionSet intervalSet = new IntervalUnionSet(first, second,
				third);
		IntervalUnionSet expected = new IntervalUnionSet(first, second, third,
				target);
		IntervalUnionSet actual = intervalSet.addNumber(INT_N_SIX);

		assertEquals(expected.toString(), actual.toString());
		p("      Set: " + intervalSet.toString());
		p("addNumber: " + INT_N_SIX.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void addNumber_Int_Num_LeftRightJoint() {
		Interval first = numberFactory.newInterval(true, INT_N_FIVE, false,
				INT_N_TWO, false);
		Interval second = numberFactory.newInterval(true, INT_ZERO, false,
				INT_ZERO, false);
		Interval third = numberFactory.newInterval(true, INT_TWO, false,
				INT_FIVE, false);
		Interval target = numberFactory.newInterval(true, INT_N_ONE, false,
				INT_N_ONE, false);
		IntervalUnionSet intervalSet = new IntervalUnionSet(first, second,
				third);
		IntervalUnionSet expected = new IntervalUnionSet(first, second, third,
				target);
		IntervalUnionSet actual = intervalSet.addNumber(INT_N_ONE);

		assertEquals(expected.toString(), actual.toString());
		p("      Set: " + intervalSet.toString());
		p("addNumber: " + INT_N_ONE.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void addNumber_Int_Num_RightJoint() {
		Interval first = numberFactory.newInterval(true, INT_N_FIVE, false,
				INT_N_TWO, false);
		Interval second = numberFactory.newInterval(true, INT_ZERO, false,
				INT_ZERO, false);
		Interval third = numberFactory.newInterval(true, INT_TWO, false,
				INT_FIVE, false);
		Interval target = numberFactory.newInterval(true, INT_SIX, false,
				INT_SIX, false);
		IntervalUnionSet intervalSet = new IntervalUnionSet(first, second,
				third);
		IntervalUnionSet expected = new IntervalUnionSet(first, second, third,
				target);
		IntervalUnionSet actual = intervalSet.addNumber(INT_SIX);

		assertEquals(expected.toString(), actual.toString());
		p("      Set: " + intervalSet.toString());
		p("addNumber: " + INT_SIX.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void addNumber_Int_Num_DisJoint() {
		Interval first = numberFactory.newInterval(true, INT_N_FIVE, false,
				INT_N_FOUR, false);
		Interval second = numberFactory.newInterval(true, INT_ZERO, false,
				INT_ZERO, false);
		Interval third = numberFactory.newInterval(true, INT_FOUR, false,
				INT_FIVE, false);
		Interval target = numberFactory.newInterval(true, INT_TWO, false,
				INT_TWO, false);
		IntervalUnionSet intervalSet = new IntervalUnionSet(first, second,
				third);
		IntervalUnionSet expected = new IntervalUnionSet(first, second, third,
				target);
		IntervalUnionSet actual = intervalSet.addNumber(INT_TWO);

		assertEquals(expected.toString(), actual.toString());
		p("      Set: " + intervalSet.toString());
		p("addNumber: " + INT_TWO.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void addNumber_Rat_Num_RightDisjoint() {
		Interval first = numberFactory.newInterval(false, RAT_N_FIVE, true,
				RAT_N_TWO, true);
		Interval second = numberFactory.newInterval(false, RAT_ZERO, false,
				RAT_ZERO, false);
		Interval third = numberFactory.newInterval(false, RAT_TWO, true,
				RAT_FIVE, true);
		Interval target = numberFactory.newInterval(false, RAT_TEN, false,
				RAT_TEN, false);
		IntervalUnionSet intervalSet = new IntervalUnionSet(first, second,
				third);
		IntervalUnionSet expected = new IntervalUnionSet(first, second, third,
				target);
		IntervalUnionSet actual = intervalSet.addNumber(RAT_TEN);

		assertEquals(expected.toString(), actual.toString());
		p("      Set: " + intervalSet.toString());
		p("addNumber: " + RAT_TEN.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void addNumber_Rat_Num_LeftJoint() {
		Interval first = numberFactory.newInterval(false, RAT_N_FIVE, true,
				RAT_N_TWO, true);
		Interval second = numberFactory.newInterval(false, RAT_ZERO, false,
				RAT_ZERO, false);
		Interval third = numberFactory.newInterval(false, RAT_TWO, true,
				RAT_FIVE, true);
		Interval target = numberFactory.newInterval(false, RAT_N_FIVE, false,
				RAT_N_FIVE, false);
		IntervalUnionSet intervalSet = new IntervalUnionSet(first, second,
				third);
		IntervalUnionSet expected = new IntervalUnionSet(first, second, third,
				target);
		IntervalUnionSet actual = intervalSet.addNumber(RAT_N_FIVE);

		assertEquals(expected.toString(), actual.toString());
		p("      Set: " + intervalSet.toString());
		p("addNumber: " + RAT_N_FIVE.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void addNumber_Rat_Num_LeftRightJoint() {
		Interval first = numberFactory.newInterval(false, RAT_N_FIVE, true,
				RAT_N_TWO, true);
		Interval second = numberFactory.newInterval(false, RAT_N_TWO, true,
				RAT_TWO, true);
		Interval third = numberFactory.newInterval(false, RAT_TWO, true,
				RAT_FIVE, true);
		Interval target1 = numberFactory.newInterval(false, RAT_N_TWO, false,
				RAT_N_TWO, false);
		IntervalUnionSet intervalSet = new IntervalUnionSet(first, second,
				third);
		IntervalUnionSet expected1 = new IntervalUnionSet(first, second, third,
				target1);
		IntervalUnionSet expected2 = new IntervalUnionSet(
				numberFactory.newInterval(false, RAT_N_FIVE, true, RAT_FIVE,
						true));
		IntervalUnionSet actual1 = intervalSet.addNumber(RAT_N_TWO);
		IntervalUnionSet actual2 = actual1.addNumber(RAT_TWO);

		assertEquals(expected1.toString(), actual1.toString());
		p("      Set: " + intervalSet.toString());
		p("addNumber: " + RAT_N_TWO.toString());
		p("expected: " + expected1.toString());
		p("  actual: " + actual1.toString());
		assertEquals(expected2.toString(), actual2.toString());
		p("      Set: " + actual1.toString());
		p("addNumber: " + RAT_TWO.toString());
		p("expected: " + expected2.toString());
		p("  actual: " + actual2.toString());
	}

	@Test
	public void addNumber_Rat_Num_RightJoint() {
		Interval first = numberFactory.newInterval(false, RAT_N_FIVE, true,
				RAT_N_TWO, true);
		Interval second = numberFactory.newInterval(false, RAT_ZERO, false,
				RAT_ZERO, false);
		Interval third = numberFactory.newInterval(false, RAT_TWO, true,
				RAT_FIVE, true);
		Interval target = numberFactory.newInterval(false, RAT_FIVE, false,
				RAT_FIVE, false);
		IntervalUnionSet intervalSet = new IntervalUnionSet(first, second,
				third);
		IntervalUnionSet expected = new IntervalUnionSet(first, second, third,
				target);
		IntervalUnionSet actual = intervalSet.addNumber(RAT_FIVE);

		assertEquals(expected.toString(), actual.toString());
		p("      Set: " + intervalSet.toString());
		p("addNumber: " + RAT_FIVE.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void addNumber_Rat_Num_DisJoint() {
		Interval first = numberFactory.newInterval(false, RAT_N_FIVE, true,
				RAT_N_TWO, true);
		Interval second = numberFactory.newInterval(false, RAT_ZERO, false,
				RAT_ZERO, false);
		Interval third = numberFactory.newInterval(false, RAT_TWO, true,
				RAT_FIVE, true);
		Interval target = numberFactory.newInterval(false, RAT_ONE, false,
				RAT_ONE, false);
		IntervalUnionSet intervalSet = new IntervalUnionSet(first, second,
				third);
		IntervalUnionSet expected = new IntervalUnionSet(first, second, third,
				target);
		IntervalUnionSet actual = intervalSet.addNumber(RAT_ONE);

		assertEquals(expected.toString(), actual.toString());
		p("      Set: " + intervalSet.toString());
		p("addNumber: " + RAT_ONE.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test(expected = AssertionError.class)
	public void contains_IntervalUnionSet_Null() {
		IntervalUnionSet target = null;
		IntervalUnionSet intervalSet = new IntervalUnionSet(INT_UNIV);

		intervalSet.contains(target);
	}

	@Test(expected = AssertionError.class)
	public void contains_IntervalUnionSet_Mismatch() {
		IntervalUnionSet target = new IntervalUnionSet(INT_EMPTY);
		IntervalUnionSet intervalSet = new IntervalUnionSet(RAT_UNIV);

		intervalSet.contains(target);
	}

	@Test
	public void contains_IntervalUnionSet_EmptySet() {
		IntervalUnionSet emptySet = new IntervalUnionSet(INT_EMPTY);
		IntervalUnionSet nonemptySet = new IntervalUnionSet(INT_ONE);
		boolean actual1 = emptySet.contains(nonemptySet);
		boolean actual2 = nonemptySet.contains(emptySet);

		assertFalse(actual1);
		assertTrue(actual2);
	}

	@Test
	public void contains_IntervalUnionSet_UnivSet() {
		IntervalUnionSet univSet = new IntervalUnionSet(INT_UNIV);
		IntervalUnionSet nonunivSet = new IntervalUnionSet(INT_ONE);
		boolean actual1 = univSet.contains(nonunivSet);
		boolean actual2 = nonunivSet.contains(univSet);

		assertTrue(actual1);
		assertFalse(actual2);
	}

	@Test
	public void contains_IntervalUnionSet_Interval_Int_LeftDisjoint() {
		Interval current = numberFactory.newInterval(true, INT_N_ONE, false,
				INT_ONE, false);
		Interval target = numberFactory.newInterval(true, INT_N_THREE, false,
				INT_N_TWO, false);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.contains(intervalSet);
		boolean actual1 = intervalSet.contains(targetSet);

		assertTrue(actual0);
		assertFalse(actual1);
	}

	@Test
	public void contains_IntervalUnionSet_Interval_Rat_LeftDisjoint() {
		Interval current = numberFactory.newInterval(false, RAT_N_ONE, true,
				RAT_ONE, true);
		Interval target = numberFactory.newInterval(false, RAT_N_ONE, false,
				RAT_N_ONE, false);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.contains(intervalSet);
		boolean actual1 = intervalSet.contains(targetSet);

		assertTrue(actual0);
		assertFalse(actual1);
	}

	@Test
	public void contains_IntervalUnionSet_Interval_Int_LeftIntersect() {
		Interval current = numberFactory.newInterval(true, INT_N_ONE, false,
				INT_ONE, false);
		Interval target = numberFactory.newInterval(true, INT_N_THREE, false,
				INT_N_ONE, false);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.contains(intervalSet);
		boolean actual1 = intervalSet.contains(targetSet);

		assertTrue(actual0);
		assertFalse(actual1);
	}

	@Test
	public void contains_IntervalUnionSet_Interval_Rat_LeftIntersect() {
		Interval current = numberFactory.newInterval(false, RAT_N_ONE, false,
				RAT_ONE, true);
		Interval target = numberFactory.newInterval(false, RAT_N_THREE, true,
				RAT_N_ONE, false);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.contains(intervalSet);
		boolean actual1 = intervalSet.contains(targetSet);

		assertTrue(actual0);
		assertFalse(actual1);
	}

	@Test
	public void contains_IntervalUnionSet_Interval_Int_Target_contains_Current() {
		Interval current = numberFactory.newInterval(true, INT_N_ONE, false,
				INT_ONE, false);
		Interval target = numberFactory.newInterval(true, INT_N_THREE, false,
				INT_THREE, false);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.contains(intervalSet);
		boolean actual1 = intervalSet.contains(targetSet);

		assertTrue(actual0);
		assertFalse(actual1);
	}

	@Test
	public void contains_IntervalUnionSet_Interval_Rat_Target_contains_Current() {
		Interval current = numberFactory.newInterval(false, RAT_N_ONE, true,
				RAT_ONE, true);
		Interval target = numberFactory.newInterval(false, RAT_N_ONE, false,
				RAT_ONE, false);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.contains(intervalSet);
		boolean actual1 = intervalSet.contains(targetSet);

		assertTrue(actual0);
		assertFalse(actual1);
	}

	@Test
	public void contains_IntervalUnionSet_Interval_Int_RightIntersect() {
		Interval current = numberFactory.newInterval(true, INT_N_ONE, false,
				INT_ONE, false);
		Interval target = numberFactory.newInterval(true, INT_ONE, false,
				INT_THREE, false);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.contains(intervalSet);
		boolean actual1 = intervalSet.contains(targetSet);

		assertTrue(actual0);
		assertFalse(actual1);
	}

	@Test
	public void contains_IntervalUnionSet_Interval_Rat_RightIntersect() {
		Interval current = numberFactory.newInterval(false, RAT_N_ONE, true,
				RAT_ONE, false);
		Interval target = numberFactory.newInterval(false, RAT_ONE, false,
				RAT_THREE, true);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.contains(intervalSet);
		boolean actual1 = intervalSet.contains(targetSet);

		assertTrue(actual0);
		assertFalse(actual1);
	}

	@Test
	public void contains_IntervalUnionSet_Interval_Int_RightDisjoint() {
		Interval current = numberFactory.newInterval(true, INT_N_ONE, false,
				INT_ONE, false);
		Interval target = numberFactory.newInterval(true, INT_TWO, false,
				INT_THREE, false);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.contains(intervalSet);
		boolean actual1 = intervalSet.contains(targetSet);

		assertTrue(actual0);
		assertFalse(actual1);
	}

	@Test
	public void contains_IntervalUnionSet_Interval_Rat_RightDisjoint() {
		Interval current = numberFactory.newInterval(false, RAT_N_ONE, true,
				RAT_ONE, true);
		Interval target = numberFactory.newInterval(false, RAT_ONE, false,
				RAT_ONE, false);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.contains(intervalSet);
		boolean actual1 = intervalSet.contains(targetSet);

		assertTrue(actual0);
		assertFalse(actual1);
	}

	@Test
	public void contains_IntervalUnionSet_Interval_Int_Contains() {
		Interval current = numberFactory.newInterval(true, INT_N_ONE, false,
				INT_ONE, false);
		Interval target = numberFactory.newInterval(true, INT_ZERO, false,
				INT_ZERO, false);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.contains(intervalSet);
		boolean actual1 = intervalSet.contains(targetSet);

		assertTrue(actual0);
		assertTrue(actual1);
	}

	@Test
	public void contains_IntervalUnionSet_Interval_Rat_Contains() {
		Interval current = numberFactory.newInterval(false, RAT_N_ONE, false,
				RAT_ONE, false);
		Interval target = numberFactory.newInterval(false, RAT_N_ONE, true,
				RAT_ONE, true);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.contains(intervalSet);
		boolean actual1 = intervalSet.contains(targetSet);

		assertTrue(actual0);
		assertTrue(actual1);
	}

	@Test
	public void contains_IntervalUnionSet_Interval_Int_Multiple() {
		Interval first = numberFactory.newInterval(true, INT_N_TEN, false,
				INT_N_SIX, false);
		Interval second = numberFactory.newInterval(true, INT_N_TWO, false,
				INT_TWO, false);
		Interval third = numberFactory.newInterval(true, INT_SIX, false,
				INT_TEN, false);
		Interval target = numberFactory.newInterval(true, INT_N_TEN, false,
				INT_N_FOUR, false);
		IntervalUnionSet firstSet = new IntervalUnionSet(first);
		IntervalUnionSet secondSet = new IntervalUnionSet(second);
		IntervalUnionSet thirdSet = new IntervalUnionSet(third);
		IntervalUnionSet intervalSet = new IntervalUnionSet(first, second,
				third);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		boolean actual0a = intervalSet.contains(firstSet);
		boolean actual0b = intervalSet.contains(secondSet);
		boolean actual0c = intervalSet.contains(thirdSet);
		boolean actual1 = intervalSet.contains(targetSet);

		assertTrue(actual0a);
		assertTrue(actual0b);
		assertTrue(actual0c);
		assertFalse(actual1);
	}

	@Test
	public void contains_IntervalUnionSet_Interval_Rat_Multiple() {
		Interval first = numberFactory.newInterval(false, RAT_N_TEN, true,
				RAT_N_SIX, true);
		Interval second = numberFactory.newInterval(false, RAT_N_TWO, true,
				RAT_TWO, true);
		Interval third = numberFactory.newInterval(false, RAT_SIX, true,
				RAT_TEN, true);
		Interval target = numberFactory.newInterval(false, RAT_SIX, true,
				RAT_SEVEN, false);
		IntervalUnionSet firstSet = new IntervalUnionSet(first);
		IntervalUnionSet secondSet = new IntervalUnionSet(second);
		IntervalUnionSet thirdSet = new IntervalUnionSet(third);
		IntervalUnionSet intervalSet = new IntervalUnionSet(first, second,
				third);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		boolean actual0a = intervalSet.contains(firstSet);
		boolean actual0b = intervalSet.contains(secondSet);
		boolean actual0c = intervalSet.contains(thirdSet);
		boolean actual1 = intervalSet.contains(targetSet);

		assertTrue(actual0a);
		assertTrue(actual0b);
		assertTrue(actual0c);
		assertTrue(actual1);
	}

	@Test
	public void contains_IntervalUnionSet_Disjoint_Rat() {
		Interval first1 = numberFactory.newInterval(false, null, true,
				RAT_N_EIGHT, true);
		Interval first2 = numberFactory.newInterval(false, RAT_N_SIX, true,
				RAT_N_FIVE, true);
		Interval second1 = numberFactory.newInterval(false, RAT_N_EIGHT, true,
				RAT_N_SEVEN, true);
		Interval second2 = numberFactory.newInterval(false, RAT_N_FIVE, true,
				RAT_N_FOUR, true);
		Interval third1 = numberFactory.newInterval(false, RAT_N_FIVE, false,
				RAT_N_FIVE, false);
		Interval third2 = numberFactory.newInterval(false, RAT_N_FOUR, true,
				RAT_N_THREE, true);
		Interval fourth1 = numberFactory.newInterval(false, RAT_N_TWO, true,
				RAT_N_ONE, true);
		Interval fourth2 = numberFactory.newInterval(false, RAT_ZERO, true,
				RAT_ONE, true);
		Interval fifth1 = numberFactory.newInterval(false, RAT_N_ONE, true,
				RAT_ZERO, true);
		Interval fifth2 = numberFactory.newInterval(false, RAT_TWO, true, null,
				true);
		Interval[] list1 = { first1, second1, third1, fourth1, fifth1 };
		Interval[] list2 = { first2, second2, third2, fourth2, fifth2 };
		IntervalUnionSet set1 = new IntervalUnionSet(list1);
		IntervalUnionSet set2 = new IntervalUnionSet(list2);
		boolean actual = set1.contains(set2);

		assertFalse(actual);
		p("    set1: " + set1.toString());
		p("    set2: " + set2.toString());
	}

	@Test
	public void contains_IntervalUnionSet_Intersection1_Rat() {
		Interval first1 = numberFactory.newInterval(false, RAT_N_NINE, false,
				RAT_N_EIGHT, true);
		Interval first2 = numberFactory.newInterval(false, null, true,
				RAT_N_NINE, false);
		Interval second1 = numberFactory.newInterval(false, RAT_N_EIGHT, true,
				RAT_N_SEVEN, true);
		Interval second2 = numberFactory.newInterval(false, RAT_N_EIGHT, false,
				RAT_N_SEVEN, true);
		Interval third1 = numberFactory.newInterval(false, RAT_N_SIX, true,
				RAT_N_FIVE, true);
		Interval third2 = numberFactory.newInterval(false, RAT_N_SIX, false,
				RAT_N_FIVE, false);
		Interval fourth1 = numberFactory.newInterval(false, RAT_N_FOUR, true,
				RAT_N_THREE, true);
		Interval fourth2 = numberFactory.newInterval(false, RAT_N_FOUR, true,
				RAT_N_THREE, false);
		Interval fifth1 = numberFactory.newInterval(false, RAT_N_TWO, true,
				RAT_N_ONE, false);
		Interval fifth2 = numberFactory.newInterval(false, RAT_N_ONE, false,
				null, true);
		Interval[] list1 = { first1, second1, third1, fourth1, fifth1 };
		Interval[] list2 = { first2, second2, third2, fourth2, fifth2 };
		IntervalUnionSet set1 = new IntervalUnionSet(list1);
		IntervalUnionSet set2 = new IntervalUnionSet(list2);
		boolean actual = set1.contains(set2);

		assertFalse(actual);
		p("    set1: " + set1.toString());
		p("    set2: " + set2.toString());
	}

	@Test
	public void contains_IntervalUnionSet_Intersection2_Rat() {
		Interval first1 = numberFactory.newInterval(false, RAT_N_NINE, false,
				RAT_N_EIGHT, true);
		Interval first2 = first1;
		Interval second1 = numberFactory.newInterval(false, RAT_N_EIGHT, true,
				RAT_N_SEVEN, true);
		Interval second2 = second1;
		Interval third1 = numberFactory.newInterval(false, RAT_N_SIX, true,
				RAT_N_FOUR, true);
		Interval third2 = numberFactory.newInterval(false, RAT_N_FIVE, true,
				RAT_N_FOUR, true);
		Interval fourth1 = numberFactory.newInterval(false, RAT_N_FOUR, true,
				RAT_N_THREE, false);
		Interval fourth2 = numberFactory.newInterval(false, RAT_N_THREE, false,
				RAT_N_TWO, false);
		Interval fifth1 = numberFactory.newInterval(false, RAT_N_TWO, true,
				RAT_N_ONE, false);
		Interval fifth2 = numberFactory.newInterval(false, RAT_N_ONE, false,
				null, true);
		Interval[] list1 = { first1, second1, third1, fourth1, fifth1 };
		Interval[] list2 = { first2, second2, third2, fourth2, fifth2 };
		IntervalUnionSet set1 = new IntervalUnionSet(list1);
		IntervalUnionSet set2 = new IntervalUnionSet(list2);
		boolean actual = set1.contains(set2);

		assertFalse(actual);
		p("    set1: " + set1.toString());
		p("    set2: " + set2.toString());
	}

	@Test
	public void contains_IntervalUnionSet_Intersection3_Rat() {
		Interval first1 = numberFactory.newInterval(false, RAT_N_NINE, false,
				RAT_N_EIGHT, true);
		Interval first2 = first1;
		Interval second1 = numberFactory.newInterval(false, RAT_N_EIGHT, true,
				RAT_N_SEVEN, true);
		Interval second2 = second1;
		Interval third1 = numberFactory.newInterval(false, RAT_N_SIX, true,
				RAT_N_FOUR, true);
		Interval third2 = third1;
		Interval fourth1 = numberFactory.newInterval(false, RAT_N_FOUR, true,
				RAT_N_THREE, true);
		Interval fourth2 = numberFactory.newInterval(false, RAT_ZERO, true,
				RAT_ONE, false);
		Interval fifth1 = numberFactory.newInterval(false, RAT_N_TWO, true,
				RAT_N_ONE, false);
		Interval fifth2 = numberFactory.newInterval(false, RAT_TWO, false,
				null, true);
		Interval[] list1 = { first1, second1, third1, fourth1, fifth1 };
		Interval[] list2 = { first2, second2, third2, fourth2, fifth2 };
		IntervalUnionSet set1 = new IntervalUnionSet(list1);
		IntervalUnionSet set2 = new IntervalUnionSet(list2);
		boolean actual = set1.contains(set2);

		assertFalse(actual);
		p("    set1: " + set1.toString());
		p("    set2: " + set2.toString());
	}

	@Test
	public void contains_IntervalUnionSet_Intersection4_Rat() {
		Interval first1 = numberFactory.newInterval(false, RAT_N_NINE, false,
				RAT_N_EIGHT, true);
		Interval first2 = first1;
		Interval second1 = numberFactory.newInterval(false, RAT_N_EIGHT, true,
				RAT_N_SEVEN, true);
		Interval second2 = second1;
		Interval third1 = numberFactory.newInterval(false, RAT_N_SIX, true,
				RAT_N_FOUR, true);
		Interval third2 = third1;
		Interval fourth1 = numberFactory.newInterval(false, RAT_N_FOUR, true,
				RAT_N_THREE, true);
		Interval fourth2 = numberFactory.newInterval(false, RAT_N_FOUR, true,
				RAT_ZERO, true);
		Interval fifth1 = numberFactory.newInterval(false, RAT_N_TWO, true,
				RAT_N_ONE, false);
		Interval fifth2 = numberFactory.newInterval(false, RAT_N_TWO, false,
				null, true);
		Interval[] list1 = { first1, second1, third1, fourth1, fifth1 };
		Interval[] list2 = { first2, second2, third2, fourth2, fifth2 };
		IntervalUnionSet set1 = new IntervalUnionSet(list1);
		IntervalUnionSet set2 = new IntervalUnionSet(list2);
		boolean actual = set1.contains(set2);

		assertFalse(actual);
		p("    set1: " + set1.toString());
		p("    set2: " + set2.toString());
	}

	@Test
	public void contains_IntervalUnionSet_Contains1_Rat() {
		Interval first1 = numberFactory.newInterval(false, null, true,
				RAT_N_SEVEN, false);
		Interval first2 = numberFactory.newInterval(false, null, true,
				RAT_N_SEVEN, true);
		Interval second1 = numberFactory.newInterval(false, RAT_N_SIX, false,
				RAT_N_FIVE, false);
		Interval second2 = numberFactory.newInterval(false, RAT_N_SIX, true,
				RAT_N_FIVE, true);
		Interval third1 = numberFactory.newInterval(false, RAT_N_FOUR, true,
				RAT_N_THREE, true);
		Interval third2 = third1;
		Interval fourth1 = numberFactory.newInterval(false, RAT_N_TWO, true,
				RAT_TWO, true);
		Interval fourth2 = numberFactory.newInterval(false, RAT_N_ONE, false,
				RAT_ONE, false);
		Interval fifth1 = numberFactory.newInterval(false, RAT_FIVE, true,
				null, true);
		Interval fifth2 = numberFactory.newInterval(false, RAT_FIVE, true,
				null, true);
		Interval[] list1 = { first1, second1, third1, fourth1, fifth1 };
		Interval[] list2 = { first2, second2, third2, fourth2, fifth2 };
		IntervalUnionSet set1 = new IntervalUnionSet(list1);
		IntervalUnionSet set2 = new IntervalUnionSet(list2);
		boolean actual = set1.contains(set2);

		assertTrue(actual);
		p("    set1: " + set1.toString());
		p("    set2: " + set2.toString());
	}

	@Test
	public void contains_IntervalUnionSet_Contains2_Rat() {
		Interval first1 = numberFactory.newInterval(false, RAT_N_TEN, true,
				RAT_ZERO, true);
		Interval first2 = numberFactory.newInterval(false, RAT_N_NINE, true,
				RAT_N_EIGHT, true);
		Interval second1 = numberFactory.newInterval(false, RAT_TWO, true,
				RAT_FOUR, true);
		Interval second2 = numberFactory.newInterval(false, RAT_N_SEVEN, true,
				RAT_N_SIX, true);
		Interval third2 = numberFactory.newInterval(false, RAT_N_FIVE, true,
				RAT_N_FOUR, true);
		Interval fourth2 = numberFactory.newInterval(false, RAT_N_THREE, true,
				RAT_N_TWO, true);
		Interval[] list1 = { first1, second1 };
		Interval[] list2 = { first2, second2, third2, fourth2 };
		IntervalUnionSet set1 = new IntervalUnionSet(list1);
		IntervalUnionSet set2 = new IntervalUnionSet(list2);
		boolean actual = set1.contains(set2);

		assertTrue(actual);
		p("    set1: " + set1.toString());
		p("    set2: " + set2.toString());
	}

	@Test
	public void contains_IntervalUnionSet_Contains3_Rat() {
		Interval first1 = numberFactory.newInterval(false, null, true,
				RAT_N_SEVEN, false);
		Interval first2 = numberFactory.newInterval(false, null, true,
				RAT_N_SEVEN, true);
		Interval second1 = numberFactory.newInterval(false, RAT_N_SIX, false,
				RAT_N_FIVE, false);
		Interval second2 = numberFactory.newInterval(false, RAT_N_SIX, true,
				RAT_N_FIVE, true);
		Interval third1 = numberFactory.newInterval(false, RAT_N_FOUR, true,
				RAT_N_THREE, true);
		Interval third2 = third1;
		Interval fourth1 = numberFactory.newInterval(false, RAT_N_TWO, true,
				RAT_TWO, true);
		Interval fourth2 = numberFactory.newInterval(false, RAT_N_TWO, true,
				RAT_TWO, true);
		Interval[] list1 = { first1, second1, third1, fourth1 };
		Interval[] list2 = { first2, second2, third2, fourth2 };
		IntervalUnionSet set1 = new IntervalUnionSet(list1);
		IntervalUnionSet set2 = new IntervalUnionSet(list2);
		boolean actual = set1.contains(set2);

		assertTrue(actual);
		p("    set1: " + set1.toString());
		p("    set2: " + set2.toString());
	}

	@Test
	public void contains_IntervalUnionSet_NotContains1_Rat() {
		Interval first1 = numberFactory.newInterval(false, null, true,
				RAT_N_SEVEN, false);
		Interval first2 = numberFactory.newInterval(false, null, true,
				RAT_N_SEVEN, true);
		Interval second1 = numberFactory.newInterval(false, RAT_N_SIX, false,
				RAT_N_FIVE, false);
		Interval second2 = numberFactory.newInterval(false, RAT_N_SIX, true,
				RAT_N_FIVE, true);
		Interval third1 = numberFactory.newInterval(false, RAT_N_FOUR, true,
				RAT_N_THREE, true);
		Interval third2 = third1;
		Interval fourth1 = numberFactory.newInterval(false, RAT_N_TWO, true,
				RAT_TWO, true);
		Interval fourth2 = numberFactory.newInterval(false, RAT_N_TWO, true,
				RAT_TWO, true);
		Interval fifth2 = numberFactory.newInterval(false, RAT_FIVE, true,
				RAT_TEN, true);
		Interval[] list1 = { first1, second1, third1, fourth1 };
		Interval[] list2 = { first2, second2, third2, fourth2, fifth2 };
		IntervalUnionSet set1 = new IntervalUnionSet(list1);
		IntervalUnionSet set2 = new IntervalUnionSet(list2);
		boolean actual = set1.contains(set2);

		assertFalse(actual);
		p("    set1: " + set1.toString());
		p("    set2: " + set2.toString());
	}

	@Test(expected = AssertionError.class)
	public void intersects_Null() {
		IntervalUnionSet target = null;
		IntervalUnionSet intervalSet = new IntervalUnionSet(INT_UNIV);

		intervalSet.intersects(target);
	}

	@Test(expected = AssertionError.class)
	public void intersects_Mismatch() {
		IntervalUnionSet target = new IntervalUnionSet(INT_UNIV);
		IntervalUnionSet intervalSet = new IntervalUnionSet(RAT_UNIV);

		intervalSet.intersects(target);
	}

	@Test
	public void intersects_Int_emptySet_In_Univ() {
		IntervalUnionSet target = new IntervalUnionSet(INT_EMPTY);
		IntervalUnionSet intervalSet = new IntervalUnionSet(INT_UNIV);
		boolean actual = intervalSet.intersects(target);

		assertFalse(actual);
	}

	@Test
	public void intersects_Rat_emptySet_In_Univ() {
		IntervalUnionSet target = new IntervalUnionSet(RAT_EMPTY);
		IntervalUnionSet intervalSet = new IntervalUnionSet(RAT_UNIV);
		boolean actual = intervalSet.intersects(target);

		assertFalse(actual);
	}

	@Test
	public void intersects_Int_univSet_In_empty() {
		IntervalUnionSet target = new IntervalUnionSet(INT_UNIV);
		IntervalUnionSet intervalSet = new IntervalUnionSet(INT_EMPTY);
		boolean actual = intervalSet.intersects(target);

		assertFalse(actual);
	}

	@Test
	public void intersects_Rat_univSet_In_empty() {
		IntervalUnionSet target = new IntervalUnionSet(RAT_UNIV);
		IntervalUnionSet intervalSet = new IntervalUnionSet(RAT_EMPTY);
		boolean actual = intervalSet.intersects(target);

		assertFalse(actual);
	}

	@Test
	public void intersects_Int_LeftDisjoint() {
		Interval current = numberFactory.newInterval(true, INT_N_ONE, false,
				INT_ONE, false);
		Interval target = numberFactory.newInterval(true, INT_N_THREE, false,
				INT_N_TWO, false);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.intersects(intervalSet);
		boolean actual1 = intervalSet.intersects(targetSet);

		assertTrue(actual0);
		assertFalse(actual1);
	}

	//
	@Test
	public void intersects_Rat_LeftDisjoint() {
		Interval current = numberFactory.newInterval(false, RAT_N_ONE, true,
				RAT_ONE, true);
		Interval target = numberFactory.newInterval(false, RAT_N_ONE, false,
				RAT_N_ONE, false);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.intersects(intervalSet);
		boolean actual1 = intervalSet.intersects(targetSet);

		assertTrue(actual0);
		assertFalse(actual1);
	}

	@Test
	public void intersects_Int_LeftIntersect() {
		Interval current = numberFactory.newInterval(true, INT_N_ONE, false,
				INT_ONE, false);
		Interval target = numberFactory.newInterval(true, INT_N_THREE, false,
				INT_N_ONE, false);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.intersects(intervalSet);
		boolean actual1 = intervalSet.intersects(targetSet);

		assertTrue(actual0);
		assertTrue(actual1);
	}

	@Test
	public void intersects_Rat_LeftIntersect() {
		Interval current = numberFactory.newInterval(false, RAT_N_ONE, false,
				RAT_ONE, true);
		Interval target = numberFactory.newInterval(false, RAT_N_THREE, true,
				RAT_N_ONE, false);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.intersects(intervalSet);
		boolean actual1 = intervalSet.intersects(targetSet);

		assertTrue(actual0);
		assertTrue(actual1);
	}

	@Test
	public void intersects_Int_Target_contains_Current() {
		Interval current = numberFactory.newInterval(true, INT_N_ONE, false,
				INT_ONE, false);
		Interval target = numberFactory.newInterval(true, INT_N_THREE, false,
				INT_THREE, false);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.intersects(intervalSet);
		boolean actual1 = intervalSet.intersects(targetSet);

		assertTrue(actual0);
		assertTrue(actual1);
	}

	@Test
	public void intersects_Rat_Target_contains_Current() {
		Interval current = numberFactory.newInterval(false, RAT_N_ONE, true,
				RAT_ONE, true);
		Interval target = numberFactory.newInterval(false, RAT_N_ONE, false,
				RAT_ONE, false);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.intersects(intervalSet);
		boolean actual1 = intervalSet.intersects(targetSet);

		assertTrue(actual0);
		assertTrue(actual1);
	}

	@Test
	public void intersects_Int_RightIntersect() {
		Interval current = numberFactory.newInterval(true, INT_N_ONE, false,
				INT_ONE, false);
		Interval target = numberFactory.newInterval(true, INT_ONE, false,
				INT_THREE, false);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.intersects(intervalSet);
		boolean actual1 = intervalSet.intersects(targetSet);

		assertTrue(actual0);
		assertTrue(actual1);
	}

	@Test
	public void intersects_Rat_RightIntersect() {
		Interval current = numberFactory.newInterval(false, RAT_N_ONE, true,
				RAT_ONE, false);
		Interval target = numberFactory.newInterval(false, RAT_ONE, false,
				RAT_THREE, true);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.intersects(intervalSet);
		boolean actual1 = intervalSet.intersects(targetSet);

		assertTrue(actual0);
		assertTrue(actual1);
	}

	@Test
	public void intersects_Int_RightDisjoint() {
		Interval current = numberFactory.newInterval(true, INT_N_ONE, false,
				INT_ONE, false);
		Interval target = numberFactory.newInterval(true, INT_TWO, false,
				INT_THREE, false);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.intersects(intervalSet);
		boolean actual1 = intervalSet.intersects(targetSet);

		assertTrue(actual0);
		assertFalse(actual1);
	}

	@Test
	public void intersects_Rat_RightDisjoint() {
		Interval current = numberFactory.newInterval(false, RAT_N_ONE, true,
				RAT_ONE, true);
		Interval target = numberFactory.newInterval(false, RAT_ONE, false,
				RAT_ONE, false);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.intersects(intervalSet);
		boolean actual1 = intervalSet.intersects(targetSet);

		assertTrue(actual0);
		assertFalse(actual1);
	}

	@Test
	public void intersects_Int_Contains() {
		Interval current = numberFactory.newInterval(true, INT_N_ONE, false,
				INT_ONE, false);
		Interval target = numberFactory.newInterval(true, INT_ZERO, false,
				INT_ZERO, false);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.intersects(intervalSet);
		boolean actual1 = intervalSet.intersects(targetSet);

		assertTrue(actual0);
		assertTrue(actual1);
	}

	@Test
	public void intersects_Rat_Contains() {
		Interval current = numberFactory.newInterval(false, RAT_N_ONE, false,
				RAT_ONE, false);
		Interval target = numberFactory.newInterval(false, RAT_N_ONE, true,
				RAT_ONE, true);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(current);
		boolean actual0 = intervalSet.intersects(intervalSet);
		boolean actual1 = intervalSet.intersects(targetSet);

		assertTrue(actual0);
		assertTrue(actual1);
	}

	@Test
	public void intersects_Int_Multiple() {
		Interval first = numberFactory.newInterval(true, INT_N_TEN, false,
				INT_N_SIX, false);
		Interval second = numberFactory.newInterval(true, INT_N_TWO, false,
				INT_TWO, false);
		Interval third = numberFactory.newInterval(true, INT_SIX, false,
				INT_TEN, false);
		Interval target = numberFactory.newInterval(true, INT_N_TEN, false,
				INT_N_FOUR, false);
		IntervalUnionSet targetSet = new IntervalUnionSet(target);
		IntervalUnionSet intervalSet = new IntervalUnionSet(first, second,
				third);
		boolean actual0 = intervalSet.intersects(intervalSet);
		boolean actual1 = intervalSet.intersects(targetSet);

		assertTrue(actual0);
		assertTrue(actual1);
	}

	@Test(expected = AssertionError.class)
	public void intersect_IntervalUnionSet_Null() {
		IntervalUnionSet nullSet = null;
		IntervalUnionSet currentSet = new IntervalUnionSet(RAT_ZERO);
		IntervalUnionSet actual = currentSet.intersect(nullSet);
	}

	@Test(expected = AssertionError.class)
	public void intersect_IntervalUnionSet_MismatchedType() {
		IntervalUnionSet intSet = new IntervalUnionSet(INT_ZERO);
		IntervalUnionSet ratSet = new IntervalUnionSet(RAT_ZERO);
		IntervalUnionSet actual = ratSet.intersect(intSet);
	}

	@Test
	public void intersect_IntervalUnionSet_Empty() {
		IntervalUnionSet emptyRatSet = new IntervalUnionSet(false);
		IntervalUnionSet nonemptyRatSet = new IntervalUnionSet(
				numberFactory
						.newInterval(false, RAT_N_ONE, true, RAT_ONE, true));
		IntervalUnionSet expected = emptyRatSet;
		IntervalUnionSet actual1 = nonemptyRatSet.intersect(emptyRatSet);
		IntervalUnionSet actual2 = emptyRatSet.intersect(nonemptyRatSet);

		assertEquals(expected.toString(), actual1.toString());
		assertEquals(expected.toString(), actual2.toString());
		p("expected: " + expected.toString());
		p(" actual1: " + actual1.toString());
		p(" actual2: " + actual2.toString());
	}

	@Test
	public void intersect_IntervalUnionSet_Univ() {
		IntervalUnionSet univIntSet = new IntervalUnionSet(INT_UNIV);
		IntervalUnionSet nonunivIntSet = new IntervalUnionSet(
				numberFactory.newInterval(true, INT_N_ONE, false, INT_ONE,
						false));
		IntervalUnionSet expected = nonunivIntSet;
		IntervalUnionSet actual1 = nonunivIntSet.intersect(univIntSet);
		IntervalUnionSet actual2 = univIntSet.intersect(nonunivIntSet);

		assertEquals(expected.toString(), actual1.toString());
		assertEquals(expected.toString(), actual2.toString());
		p("expected: " + expected.toString());
		p(" actual1: " + actual1.toString());
		p(" actual2: " + actual2.toString());
	}

	@Test
	public void intersect_IntervalUnionSet_Self() {
		IntervalUnionSet original = new IntervalUnionSet(
				numberFactory
						.newInterval(false, RAT_N_ONE, true, RAT_ONE, true));
		IntervalUnionSet expected = original;
		IntervalUnionSet actual = original.intersect(original);

		assertEquals(expected.toString(), actual.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	@Test
	public void intersect_IntervalUnionSet_Simple_Disjoint_Rat() {
		Interval first1 = numberFactory.newInterval(false, RAT_N_TEN, true,
				RAT_N_EIGHT, true);
		Interval first2 = numberFactory.newInterval(false, RAT_N_EIGHT, true,
				RAT_N_SIX, true);
		Interval second1 = numberFactory.newInterval(false, RAT_N_SIX, true,
				RAT_N_FOUR, true);
		Interval second2 = numberFactory.newInterval(false, RAT_N_FOUR, true,
				RAT_N_TWO, true);
		Interval third1 = numberFactory.newInterval(false, RAT_N_TWO, true,
				RAT_ZERO, true);
		Interval third2 = numberFactory.newInterval(false, RAT_ZERO, true,
				RAT_TWO, true);
		Interval fourth1 = numberFactory.newInterval(false, RAT_TWO, true,
				RAT_FOUR, true);
		Interval fourth2 = numberFactory.newInterval(false, RAT_FOUR, true,
				RAT_SIX, true);
		Interval fifth1 = numberFactory.newInterval(false, RAT_SIX, true,
				RAT_EIGHT, true);
		Interval fifth2 = numberFactory.newInterval(false, RAT_EIGHT, true,
				RAT_TEN, true);
		Interval[] list1 = { first1, second1, third1, fourth1, fifth1 };
		Interval[] list2 = { first2, second2, third2, fourth2, fifth2 };
		IntervalUnionSet set1 = new IntervalUnionSet(list1);
		IntervalUnionSet set2 = new IntervalUnionSet(list2);
		IntervalUnionSet expected = new IntervalUnionSet(false);
		IntervalUnionSet actual1 = set1.intersect(set2);
		IntervalUnionSet actual2 = set2.intersect(set1);

		assertEquals(expected.toString(), actual1.toString());
		assertEquals(expected.toString(), actual2.toString());
		p("    set1: " + set1.toString());
		p("    set2: " + set2.toString());
		p("expected: " + expected.toString());
		p(" actua1l: " + actual1.toString());
		p(" actual2: " + actual2.toString());
	}

	@Test
	public void intersect_IntervalUnionSet_Simple_Adjacent_Rat() {
		Interval first1 = numberFactory.newInterval(false, null, true,
				RAT_N_EIGHT, false);
		Interval first2 = numberFactory.newInterval(false, RAT_N_EIGHT, true,
				RAT_N_SIX, false);
		Interval second1 = numberFactory.newInterval(false, RAT_N_SIX, true,
				RAT_N_FOUR, false);
		Interval second2 = numberFactory.newInterval(false, RAT_N_FOUR, true,
				RAT_N_TWO, false);
		Interval third1 = numberFactory.newInterval(false, RAT_N_TWO, true,
				RAT_ZERO, false);
		Interval third2 = numberFactory.newInterval(false, RAT_ZERO, true,
				RAT_TWO, false);
		Interval fourth1 = numberFactory.newInterval(false, RAT_TWO, true,
				RAT_FOUR, false);
		Interval fourth2 = numberFactory.newInterval(false, RAT_FOUR, true,
				RAT_SIX, false);
		Interval fifth1 = numberFactory.newInterval(false, RAT_SIX, true,
				RAT_EIGHT, false);
		Interval fifth2 = numberFactory.newInterval(false, RAT_EIGHT, true,
				null, true);
		Interval[] list1 = { first1, second1, third1, fourth1, fifth1 };
		Interval[] list2 = { first2, second2, third2, fourth2, fifth2 };
		IntervalUnionSet set1 = new IntervalUnionSet(list1);
		IntervalUnionSet set2 = new IntervalUnionSet(list2);
		IntervalUnionSet expected = new IntervalUnionSet(false);
		IntervalUnionSet actual1 = set1.intersect(set2);
		IntervalUnionSet actual2 = set2.intersect(set1);

		assertEquals(expected.toString(), actual1.toString());
		assertEquals(expected.toString(), actual2.toString());
		p("    set1: " + set1.toString());
		p("    set2: " + set2.toString());
		p("expected: " + expected.toString());
		p(" actua1l: " + actual1.toString());
		p(" actual2: " + actual2.toString());
	}

	@Test
	public void intersect_IntervalUnionSet_Simple_Adjacent_Int() {
		Interval first1 = numberFactory.newInterval(true, null, true,
				INT_N_TEN, false);
		Interval first2 = numberFactory.newInterval(true, INT_N_NINE, false,
				INT_N_EIGHT, false);
		Interval second1 = numberFactory.newInterval(true, INT_N_SEVEN, false,
				INT_N_FIVE, false);
		Interval second2 = numberFactory.newInterval(true, INT_N_FOUR, false,
				INT_N_TWO, false);
		Interval third1 = numberFactory.newInterval(true, INT_N_ONE, false,
				INT_ZERO, false);
		Interval third2 = numberFactory.newInterval(true, INT_ONE, false,
				INT_ONE, false);
		Interval fourth1 = numberFactory.newInterval(true, INT_TWO, false,
				INT_FOUR, false);
		Interval fourth2 = numberFactory.newInterval(true, INT_FIVE, false,
				INT_SEVEN, false);
		Interval fifth1 = numberFactory.newInterval(true, INT_EIGHT, false,
				INT_NINE, false);
		Interval fifth2 = numberFactory.newInterval(true, INT_TEN, false, null,
				true);
		Interval[] list1 = { first1, second1, third1, fourth1, fifth1 };
		Interval[] list2 = { first2, second2, third2, fourth2, fifth2 };

		IntervalUnionSet set1 = new IntervalUnionSet(list1);
		IntervalUnionSet set2 = new IntervalUnionSet(list2);
		IntervalUnionSet expected = new IntervalUnionSet(true);
		IntervalUnionSet actual1 = set1.intersect(set2);
		IntervalUnionSet actual2 = set2.intersect(set1);

		assertEquals(expected.toString(), actual1.toString());
		assertEquals(expected.toString(), actual2.toString());
		p("    set1: " + set1.toString());
		p("    set2: " + set2.toString());
		p("expected: " + expected.toString());
		p(" actua1l: " + actual1.toString());
		p(" actual2: " + actual2.toString());
	}

	@Test
	public void intersect_IntervalUnionSet_Simple_Overlapped_Rat() {
		Interval first1 = numberFactory.newInterval(false, RAT_N_NINE, true,
				RAT_N_SEVEN, true);
		Interval first2 = numberFactory.newInterval(false, RAT_N_EIGHT, true,
				RAT_N_SEVEN, true);
		Interval second1 = numberFactory.newInterval(false, RAT_N_SIX, false,
				RAT_N_FIVE, true);
		Interval second2 = numberFactory.newInterval(false, RAT_N_SIX, true,
				RAT_N_FIVE, true);
		Interval third1 = numberFactory.newInterval(false, RAT_N_FIVE, true,
				RAT_N_FOUR, true);
		Interval third2 = third1;
		Interval fourth1 = numberFactory.newInterval(false, RAT_N_THREE, false,
				RAT_N_TWO, false);
		Interval fourth2 = numberFactory.newInterval(false, RAT_N_THREE, false,
				RAT_N_TWO, true);
		Interval fifth1 = numberFactory.newInterval(false, RAT_N_ONE, true,
				RAT_ONE, true);
		Interval fifth2 = numberFactory.newInterval(false, RAT_N_ONE, true,
				RAT_ZERO, false);
		Interval sixth1 = numberFactory.newInterval(false, RAT_THREE, false,
				RAT_FIVE, false);
		Interval sixth2 = numberFactory.newInterval(false, RAT_TWO, false,
				RAT_FOUR, false);
		Interval sixth3 = numberFactory.newInterval(false, RAT_THREE, false,
				RAT_FOUR, false);
		Interval seventh1 = numberFactory.newInterval(false, RAT_SIX, false,
				RAT_EIGHT, true);
		Interval seventh2 = numberFactory.newInterval(false, RAT_SIX, true,
				RAT_EIGHT, false);
		Interval seventh3 = numberFactory.newInterval(false, RAT_SIX, true,
				RAT_EIGHT, true);

		Interval[] list1 = { first1, second1, third1, fourth1, fifth1, sixth1,
				seventh1 };
		Interval[] list2 = { first2, second2, third2, fourth2, fifth2, sixth2,
				seventh2 };
		Interval[] list3 = { first2, second2, third2, fourth2, fifth2, sixth3,
				seventh3 };

		IntervalUnionSet set1 = new IntervalUnionSet(list1);
		IntervalUnionSet set2 = new IntervalUnionSet(list2);
		IntervalUnionSet expected = new IntervalUnionSet(list3);
		IntervalUnionSet actual1 = set1.intersect(set2);
		IntervalUnionSet actual2 = set2.intersect(set1);

		assertEquals(expected.toString(), actual1.toString());
		assertEquals(expected.toString(), actual2.toString());
		p("    set1: " + set1.toString());
		p("    set2: " + set2.toString());
		p("expected: " + expected.toString());
		p(" actua1l: " + actual1.toString());
		p(" actual2: " + actual2.toString());
	}

	@Test
	public void intersect_IntervalUnionSet_Complicated1_Int() {
		Interval first1 = numberFactory.newInterval(true, INT_N_TEN, false,
				INT_N_TWO, false);
		Interval first2 = numberFactory.newInterval(true, INT_N_NINE, false,
				INT_N_SEVEN, false);
		Interval second1 = numberFactory.newInterval(true, INT_ZERO, false,
				INT_ZERO, false);
		Interval second2 = numberFactory.newInterval(true, INT_N_FIVE, false,
				INT_N_FIVE, false);
		Interval third1 = numberFactory.newInterval(true, INT_THREE, false,
				INT_THREE, false);
		Interval third2 = numberFactory.newInterval(true, INT_N_THREE, false,
				INT_N_TWO, false);
		Interval fourth1 = numberFactory.newInterval(true, INT_SIX, false,
				INT_SEVEN, false);
		Interval fourth2 = numberFactory.newInterval(true, INT_ZERO, false,
				INT_ZERO, false);
		Interval fifth1 = numberFactory.newInterval(true, INT_NINE, false,
				INT_NINE, false);
		Interval fifth2 = numberFactory.newInterval(true, INT_TWO, false,
				INT_TEN, false);

		Interval[] list1 = { first1, second1, third1, fourth1, fifth1 };
		Interval[] list2 = { first2, second2, third2, fourth2, fifth2 };
		Interval[] list3 = { first2, second2, third2, fourth2, third1, fourth1,
				fifth1 };

		IntervalUnionSet set1 = new IntervalUnionSet(list1);
		IntervalUnionSet set2 = new IntervalUnionSet(list2);
		IntervalUnionSet expected = new IntervalUnionSet(list3);
		IntervalUnionSet actual1 = set1.intersect(set2);
		IntervalUnionSet actual2 = set2.intersect(set1);

		assertEquals(expected.toString(), actual1.toString());
		assertEquals(expected.toString(), actual2.toString());
		p("    set1: " + set1.toString());
		p("    set2: " + set2.toString());
		p("expected: " + expected.toString());
		p(" actua1l: " + actual1.toString());
		p(" actual2: " + actual2.toString());
	}

	@Test
	public void intersect_IntervalUnionSet_Complicated2_Rat() {
		Interval first1 = numberFactory.newInterval(false, null, true,
				RAT_N_TWO, true);
		Interval first2 = numberFactory.newInterval(false, RAT_N_NINE, true,
				RAT_N_SEVEN, true);
		Interval second1 = numberFactory.newInterval(false, RAT_ZERO, true,
				RAT_ONE, true);
		Interval second2 = numberFactory.newInterval(false, RAT_N_FIVE, true,
				RAT_N_FOUR, true);
		Interval third1 = numberFactory.newInterval(false, RAT_THREE, true,
				RAT_FOUR, true);
		Interval third2 = numberFactory.newInterval(false, RAT_N_THREE, true,
				RAT_N_TWO, true);
		Interval fourth1 = numberFactory.newInterval(false, RAT_SIX, true,
				RAT_SEVEN, true);
		Interval fourth2 = numberFactory.newInterval(false, RAT_ZERO, true,
				RAT_ONE, true);
		Interval fifth1 = numberFactory.newInterval(false, RAT_NINE, true,
				RAT_TEN, true);
		Interval fifth2 = numberFactory.newInterval(false, RAT_TWO, true, null,
				true);

		Interval[] list1 = { first1, second1, third1, fourth1, fifth1 };
		Interval[] list2 = { first2, second2, third2, fourth2, fifth2 };
		Interval[] list3 = { first2, second2, third2, fourth2, third1, fourth1,
				fifth1 };

		IntervalUnionSet set1 = new IntervalUnionSet(list1);
		IntervalUnionSet set2 = new IntervalUnionSet(list2);
		IntervalUnionSet expected = new IntervalUnionSet(list3);
		IntervalUnionSet actual1 = set1.intersect(set2);
		IntervalUnionSet actual2 = set2.intersect(set1);

		assertEquals(expected.toString(), actual1.toString());
		assertEquals(expected.toString(), actual2.toString());
		p("    set1: " + set1.toString());
		p("    set2: " + set2.toString());
		p("expected: " + expected.toString());
		p(" actua1l: " + actual1.toString());
		p(" actual2: " + actual2.toString());
	}

	// TODO:
	@Test(expected = AssertionError.class)
	public void minus_IntervalUnionSet_Null() {
		IntervalUnionSet nullSet = null;
		IntervalUnionSet currentSet = new IntervalUnionSet(RAT_ZERO);
		IntervalUnionSet actual = currentSet.minus(nullSet);
	}

	@Test(expected = AssertionError.class)
	public void minus_IntervalUnionSet_MismatchedType() {
		IntervalUnionSet intSet = new IntervalUnionSet(INT_ZERO);
		IntervalUnionSet ratSet = new IntervalUnionSet(RAT_ZERO);
		IntervalUnionSet actual = ratSet.minus(intSet);
	}

	@Test
	public void minus_IntervalUnionSet_Empty() {
		IntervalUnionSet emptyRatSet = new IntervalUnionSet(false);
		IntervalUnionSet nonemptyRatSet = new IntervalUnionSet(
				numberFactory
						.newInterval(false, RAT_N_ONE, true, RAT_ONE, true));
		IntervalUnionSet expected1 = nonemptyRatSet;
		IntervalUnionSet expected2 = emptyRatSet;
		IntervalUnionSet actual1 = nonemptyRatSet.minus(emptyRatSet);
		IntervalUnionSet actual2 = emptyRatSet.minus(nonemptyRatSet);

		assertEquals(expected1.toString(), actual1.toString());
		assertEquals(expected2.toString(), actual2.toString());
		p("expected: " + expected1.toString());
		p(" actual1: " + actual1.toString());
		p("expected: " + expected2.toString());
		p(" actual2: " + actual2.toString());
	}

	@Test
	public void minus_IntervalUnionSet_Univ() {
		IntervalUnionSet univIntSet = new IntervalUnionSet(INT_UNIV);
		IntervalUnionSet nonunivIntSet = new IntervalUnionSet(
				numberFactory.newInterval(true, INT_N_ONE, false, INT_ONE,
						false));
		IntervalUnionSet expected1 = new IntervalUnionSet(true);
		IntervalUnionSet expected2 = new IntervalUnionSet(
				numberFactory.newInterval(true, null, true, INT_N_TWO,
						false), numberFactory.newInterval(true, INT_TWO,
						false, null, true));
		IntervalUnionSet actual1 = nonunivIntSet.minus(univIntSet);
		IntervalUnionSet actual2 = univIntSet.minus(nonunivIntSet);

		//(expected1.toString(), actual1.toString());
		//assertEquals(expected2.toString(), actual2.toString());
		p("expected: " + expected1.toString());
		p(" actual1: " + actual1.toString());
		p("expected: " + expected2.toString());
		p(" actual2: " + actual2.toString());
	}

	@Test
	public void minus_IntervalUnionSet_Self() {
		IntervalUnionSet original = new IntervalUnionSet(
				numberFactory
						.newInterval(false, RAT_N_ONE, true, RAT_ONE, true));
		IntervalUnionSet expected = new IntervalUnionSet(false);
		IntervalUnionSet actual = original.minus(original);

		assertEquals(expected.toString(), actual.toString());
		p("expected: " + expected.toString());
		p("  actual: " + actual.toString());
	}

	/*
	 * @Test public void intersects_Rat_Multiple() { Interval first =
	 * numberFactory.newInterval(false, RAT_N_TEN, true, RAT_N_SIX, true);
	 * Interval second = numberFactory.newInterval(false, RAT_N_TWO, true,
	 * RAT_TWO, true); Interval third = numberFactory.newInterval(false,
	 * RAT_SIX, true, RAT_TEN, true); Interval target =
	 * numberFactory.newInterval(false, RAT_SIX, true, RAT_SEVEN, false);
	 * IntervalUnionSet target = new IntervalUnionSet(target); IntervalUnionSet
	 * intervalSet = new IntervalUnionSet(first, second, third); boolean actual0
	 * = intervalSet.intersects(intervalSet); boolean actual1 =
	 * intervalSet.intersects(target);
	 * 
	 * assertTrue(actual0); assertTrue(actual1); }
	 */
}
