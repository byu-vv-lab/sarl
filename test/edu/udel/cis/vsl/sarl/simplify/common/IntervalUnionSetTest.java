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
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.number.real.RealInteger;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

/**
 * @author WenhaoWu
 *
 */
public class IntervalUnionSetTest {
	// Declaration:
	private static PreUniverse universe;
	private static boolean showLog = true;
	private static NumberFactory nf = Numbers.REAL_FACTORY;
	private static RealInteger INT_ZERO = (RealInteger) nf.zeroInteger();
	private static RealInteger INT_ONE = (RealInteger) nf.integer(1);
	private static RealInteger INT_TEN = (RealInteger) nf.integer(10);
	private static RealInteger INT_NEG_TEN = (RealInteger) nf.integer(-10);
	private static RationalNumber RAT_NEG_ONE_D_ZERO = nf.rational(nf
			.integer(-1));
	private static RationalNumber RAT_ZERO = nf.rational(nf.zeroInteger());
	private static RationalNumber RAT_ONE_D_ZERO = nf.rational(nf.integer(1));
	private static RationalNumber RAT_TWO_D_ZERO = nf.rational(nf.integer(2));
	private static RationalNumber RAT_THREE_D_ZERO = nf.rational(nf.integer(3));
	private static RationalNumber RAT_FOUR_D_ZERO = nf.rational(nf.integer(4));
	private static RationalNumber RAT_FIVE_D_ZERO = nf.rational(nf.integer(5));
	private static RationalNumber RAT_SIX_D_ZERO = nf.rational(nf.integer(6));
	private static RationalNumber RAT_SEVEN_D_ZERO = nf.rational(nf.integer(7));
	private static RationalNumber RAT_EIGHT_D_ZERO = nf.rational(nf.integer(8));
	private static RationalNumber RAT_NINE_D_ZERO = nf.rational(nf.integer(9));
	private static RationalNumber RAT_TEN_D_ZERO = nf.rational(nf.integer(10));
	private static RationalNumber RAT_ELEVEN_D_ZERO = nf.rational(nf
			.integer(11));
	private static RationalNumber RAT_THIRTY_D_ZERO = nf.rational(nf
			.integer(30));
	private static RationalNumber RAT_FIFTY_D_ZERO = nf
			.rational(nf.integer(50));
	private static RationalNumber RAT_SEVENTY_D_ZERO = nf.rational(nf
			.integer(70));

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
	// ConstructorTest
	@Test
	public void constructIUS() {
		IntervalUnionSet ius = new IntervalUnionSet();
		RealInteger n = (RealInteger) nf.zeroInteger();
		IntervalUnionSet iusInt = new IntervalUnionSet(n);
		RationalNumber lo = nf.rational(n);
		RationalNumber up = nf.rational(nf.rational(nf.integer(314)));
		boolean isInt = lo instanceof RealInteger;
		Interval itv = nf.newInterval(isInt, lo, true, up, false);
		IntervalUnionSet iusRat = new IntervalUnionSet(itv);
		IntervalUnionSet iusCp = new IntervalUnionSet(iusRat);

		assertTrue(ius.isEmpty());
		// p(ius.toString());
		assertFalse(iusInt.isEmpty());
		assertTrue(iusInt.isIntegral());
		// p(iusInt.toString());
		assertFalse(iusRat.isEmpty());
		assertFalse(iusRat.isIntegral());
		// p(iusRat.toString());
		assertFalse(iusCp.isEmpty());
		assertFalse(iusCp.isIntegral());
		// p(iusCp.toString());
	}

	// public boolean containsNumber(Number number);
	@Test(expected = NullPointerException.class)
	public void iusContainsNumNullInput() {
		IntervalUnionSet ius = new IntervalUnionSet();
		RealInteger num = null;
		ius.containsNumber(num);
	}

	@Test(expected = IllegalArgumentException.class)
	public void iusContainswithDiffType() {
		IntervalUnionSet ius = new IntervalUnionSet(INT_ZERO);

		assertFalse(ius.containsNumber(RAT_ZERO));
	}

	@Test
	public void iusContainswithEmptySet() {
		IntervalUnionSet ius = new IntervalUnionSet();

		assertFalse(ius.containsNumber(RAT_ZERO));
	}

	@Test
	public void iusContainsNumInt() {
		IntervalUnionSet ius = new IntervalUnionSet(INT_ONE);

		assertFalse(ius.containsNumber(INT_NEG_TEN));
		assertFalse(ius.containsNumber(INT_ZERO));
		ius.addNumber(INT_NEG_TEN);
		assertTrue(ius.containsNumber(INT_NEG_TEN));
		assertFalse(ius.containsNumber(INT_ZERO));
	}

	@Test
	public void iusContainsNumRat() {
		Interval itv = nf.newInterval(RAT_ZERO instanceof RealInteger,
				RAT_ZERO, true, RAT_TEN_D_ZERO, false);
		IntervalUnionSet ius = new IntervalUnionSet(itv);

		assertTrue(ius.containsNumber(RAT_TEN_D_ZERO));
		assertFalse(ius.containsNumber(RAT_ZERO));
	}

	// public boolean isIntegral();
	@Test
	public void iusIsIntegralEmptySet() {
		IntervalUnionSet ius = new IntervalUnionSet();

		assertFalse(ius.isIntegral());
	}

	@Test
	public void iusIsIntegralInt() {
		IntervalUnionSet ius = new IntervalUnionSet(INT_NEG_TEN);

		assertTrue(ius.isIntegral());
	}

	@Test
	public void iusIsIntegralRat() {
		Interval itv = nf.newInterval(RAT_ZERO instanceof RealInteger,
				RAT_ZERO, true, RAT_TEN_D_ZERO, false);
		IntervalUnionSet ius = new IntervalUnionSet(itv);

		assertFalse(ius.isIntegral());
	}

	// public Range union(Range set);
	@Test(expected = NullPointerException.class)
	public void iusUnionNullwithNull() {
		IntervalUnionSet src = new IntervalUnionSet();
		IntervalUnionSet element1 = null;
		src.union(element1);
	}

	@Test
	public void iusUnionSimpleDisjointRat() {
		Interval itv1 = nf.newInterval(RAT_ZERO instanceof RealInteger, null,
				true, RAT_ZERO, false);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(
				RAT_THIRTY_D_ZERO instanceof RealInteger, RAT_THIRTY_D_ZERO,
				true, RAT_FIFTY_D_ZERO, false);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv2);
		Interval itv3 = nf.newInterval(
				RAT_SEVENTY_D_ZERO instanceof RealInteger, RAT_SEVENTY_D_ZERO,
				true, null, true);
		IntervalUnionSet tmp3 = new IntervalUnionSet(itv3);
		IntervalUnionSet res1 = (IntervalUnionSet) tmp1.union(tmp2);
		IntervalUnionSet res2 = (IntervalUnionSet) tmp2.union(tmp3);
		IntervalUnionSet res3 = (IntervalUnionSet) tmp3.union(tmp1);
		IntervalUnionSet res4 = (IntervalUnionSet) res1.union(tmp3);
		IntervalUnionSet res5 = (IntervalUnionSet) res2.union(tmp1);
		IntervalUnionSet res6 = (IntervalUnionSet) res3.union(tmp2);

		// Three simple set.
		p(tmp1.toString());
		p(tmp2.toString());
		p(tmp3.toString());
		// Result
		p(res1.toString());
		p(res2.toString());
		p(res3.toString());
		p(res4.toString());
		p(res5.toString());
		p(res6.toString());
	}

	@Test
	public void iusUnionSimpleAdjacentInt() {
		Interval itv1 = nf.newInterval(INT_ZERO instanceof RealInteger, null,
				true, INT_ZERO, true);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(INT_ZERO instanceof RealInteger,
				INT_ZERO, true, INT_ONE, true);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv2);
		Interval itv3 = nf.newInterval(INT_ONE instanceof RealInteger, INT_ONE,
				true, INT_TEN, true);
		IntervalUnionSet tmp3 = new IntervalUnionSet(itv3);
		IntervalUnionSet res1 = (IntervalUnionSet) tmp1.union(tmp2);
		IntervalUnionSet res2 = (IntervalUnionSet) tmp2.union(tmp3);
		IntervalUnionSet res3 = (IntervalUnionSet) tmp3.union(tmp1);
		IntervalUnionSet res4 = (IntervalUnionSet) res1.union(tmp3);
		IntervalUnionSet res5 = (IntervalUnionSet) res2.union(tmp1);
		IntervalUnionSet res6 = (IntervalUnionSet) res3.union(tmp2);

		// Three simple set.
		p(tmp1.toString());
		p(tmp2.toString());
		p(tmp3.toString());
		// Result
		p(res1.toString());
		p(res2.toString());
		p(res3.toString());
		p(res4.toString());
		p(res5.toString());
		p(res6.toString());
	}

	@Test
	public void iusUnionSimpleOverlapRat() {
		Interval itv1 = nf.newInterval(RAT_TEN_D_ZERO instanceof RealInteger,
				null, true, RAT_TEN_D_ZERO, false);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(RAT_ZERO instanceof RealInteger,
				RAT_ZERO, true, RAT_FIFTY_D_ZERO, true);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv2);
		Interval itv3 = nf.newInterval(
				RAT_THIRTY_D_ZERO instanceof RealInteger, RAT_THIRTY_D_ZERO,
				true, RAT_SEVENTY_D_ZERO, true);
		IntervalUnionSet tmp3 = new IntervalUnionSet(itv3);
		IntervalUnionSet res1 = (IntervalUnionSet) tmp1.union(tmp2);
		IntervalUnionSet res2 = (IntervalUnionSet) tmp2.union(tmp3);
		IntervalUnionSet res3 = (IntervalUnionSet) tmp3.union(tmp1);
		IntervalUnionSet res4 = (IntervalUnionSet) res1.union(tmp3);
		IntervalUnionSet res5 = (IntervalUnionSet) res2.union(tmp1);
		IntervalUnionSet res6 = (IntervalUnionSet) res3.union(tmp2);

		// Three simple set.
		p(tmp1.toString());
		p(tmp2.toString());
		p(tmp3.toString());
		// Result
		p(res1.toString());
		p(res2.toString());
		p(res3.toString());
		p(res4.toString());
		p(res5.toString());
		p(res6.toString());
	}

	@Test
	public void iusUnionComplexRat() {
		Interval itv1 = nf.newInterval(RAT_ZERO instanceof RealInteger,
				RAT_ZERO, true, RAT_SIX_D_ZERO, false);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(RAT_SEVEN_D_ZERO instanceof RealInteger,
				RAT_SEVEN_D_ZERO, true, RAT_EIGHT_D_ZERO, true);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv2);
		Interval itv3 = nf.newInterval(RAT_NINE_D_ZERO instanceof RealInteger,
				RAT_NINE_D_ZERO, false, RAT_ELEVEN_D_ZERO, false);
		IntervalUnionSet tmp3 = new IntervalUnionSet(itv3);
		IntervalUnionSet tmp4 = (IntervalUnionSet) tmp1.union(tmp2);
		IntervalUnionSet src1 = (IntervalUnionSet) tmp4.union(tmp3);
		Interval itv4 = nf.newInterval(RAT_ONE_D_ZERO instanceof RealInteger,
				RAT_ONE_D_ZERO, true, RAT_TWO_D_ZERO, true);
		IntervalUnionSet tmp5 = new IntervalUnionSet(itv4);
		Interval itv5 = nf.newInterval(RAT_THREE_D_ZERO instanceof RealInteger,
				RAT_THREE_D_ZERO, true, RAT_FOUR_D_ZERO, true);
		IntervalUnionSet tmp6 = new IntervalUnionSet(itv5);
		Interval itv6 = nf.newInterval(RAT_FIVE_D_ZERO instanceof RealInteger,
				RAT_FIVE_D_ZERO, true, RAT_NINE_D_ZERO, true);
		IntervalUnionSet tmp7 = new IntervalUnionSet(itv6);
		Interval itv7 = nf.newInterval(RAT_TEN_D_ZERO instanceof RealInteger,
				RAT_TEN_D_ZERO, true, null, true);
		IntervalUnionSet tmp8 = new IntervalUnionSet(itv7);
		IntervalUnionSet tmp9 = (IntervalUnionSet) tmp5.union(tmp6);
		IntervalUnionSet tmp10 = (IntervalUnionSet) tmp9.union(tmp7);
		IntervalUnionSet src2 = (IntervalUnionSet) tmp10.union(tmp8);
		IntervalUnionSet res1 = (IntervalUnionSet) src1.union(src2);
		Interval itv8 = nf.newInterval(RAT_TEN_D_ZERO instanceof RealInteger,
				null, true, RAT_ZERO, true);
		IntervalUnionSet src3 = new IntervalUnionSet(itv8);
		IntervalUnionSet res2 = (IntervalUnionSet) res1.union(src3);
		Interval itv9 = nf.newInterval(RAT_TEN_D_ZERO instanceof RealInteger,
				RAT_ZERO, false, RAT_ZERO, false);
		IntervalUnionSet src4 = new IntervalUnionSet(itv9);
		IntervalUnionSet res3 = (IntervalUnionSet) res2.union(src4);

		// TWO complex set.
		p(src1.toString());
		p(src2.toString());
		p(src3.toString());
		p(src4.toString());
		// Result
		p(res1.toString());
		p(res2.toString());
		p(res3.toString());
	}

	// public Range intersect(Range set);
	@Test(expected = NullPointerException.class)
	public void iusIntersectNullwithNull() {
		IntervalUnionSet src = new IntervalUnionSet();
		IntervalUnionSet element1 = null;
		src.intersect(element1);
	}

	@Test
	public void iusIntersectSimpleDisjointRat() {
		Interval itv1 = nf.newInterval(RAT_ZERO instanceof RealInteger, null,
				true, RAT_ZERO, false);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(RAT_ZERO instanceof RealInteger,
				RAT_ZERO, true, RAT_FIFTY_D_ZERO, false);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv2);
		Interval itv3 = nf.newInterval(
				RAT_SEVENTY_D_ZERO instanceof RealInteger, RAT_SEVENTY_D_ZERO,
				true, null, true);
		IntervalUnionSet tmp3 = new IntervalUnionSet(itv3);
		IntervalUnionSet res1 = (IntervalUnionSet) tmp1.intersect(tmp2);
		IntervalUnionSet res2 = (IntervalUnionSet) tmp2.intersect(tmp3);
		IntervalUnionSet res3 = (IntervalUnionSet) tmp3.intersect(tmp1);

		// Three simple set.
		p(tmp1.toString());
		p(tmp2.toString());
		p(tmp3.toString());
		// Result
		p(res1.toString());
		p(res2.toString());
		p(res3.toString());
	}

	@Test
	public void iusIntersectSimpleAdjacentInt() {
		Interval itv1 = nf.newInterval(INT_ZERO instanceof RealInteger, null,
				true, INT_ZERO, true);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(INT_ZERO instanceof RealInteger,
				INT_ZERO, true, INT_ONE, true);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv2);
		Interval itv3 = nf.newInterval(INT_ONE instanceof RealInteger, INT_ONE,
				true, INT_TEN, true);
		IntervalUnionSet tmp3 = new IntervalUnionSet(itv3);
		IntervalUnionSet res1 = (IntervalUnionSet) tmp1.intersect(tmp2);
		IntervalUnionSet res2 = (IntervalUnionSet) tmp2.intersect(tmp3);

		// Three simple set.
		p(tmp1.toString());
		p(tmp2.toString());
		p(tmp3.toString());
		// Result
		p(res1.toString());
		p(res2.toString());
	}

	@Test
	public void iusIntersectSimpleOverlapRat() {
		Interval itv1 = nf.newInterval(RAT_TEN_D_ZERO instanceof RealInteger,
				null, true, RAT_THIRTY_D_ZERO, false);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(RAT_ZERO instanceof RealInteger,
				RAT_ZERO, true, RAT_FIFTY_D_ZERO, true);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv2);
		Interval itv3 = nf.newInterval(
				RAT_THIRTY_D_ZERO instanceof RealInteger, RAT_THIRTY_D_ZERO,
				false, null, true);
		IntervalUnionSet tmp3 = new IntervalUnionSet(itv3);
		IntervalUnionSet res1 = (IntervalUnionSet) tmp1.intersect(tmp2);
		IntervalUnionSet res2 = (IntervalUnionSet) tmp2.intersect(tmp3);
		IntervalUnionSet res3 = (IntervalUnionSet) tmp3.intersect(tmp1);
		IntervalUnionSet res4 = (IntervalUnionSet) res1.intersect(tmp3);
		IntervalUnionSet res5 = (IntervalUnionSet) res2.intersect(tmp1);
		IntervalUnionSet res6 = (IntervalUnionSet) res3.intersect(tmp2);

		// Three simple set.
		p(tmp1.toString());
		p(tmp2.toString());
		p(tmp3.toString());
		// Result
		p(res1.toString());
		p(res2.toString());
		p(res3.toString());
		p(res4.toString());
		p(res5.toString());
		p(res6.toString());
	}

	@Test
	public void iusIntersectComplexRat() {
		Interval itv1 = nf.newInterval(RAT_ZERO instanceof RealInteger,
				RAT_ZERO, true, RAT_SIX_D_ZERO, false);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(RAT_SEVEN_D_ZERO instanceof RealInteger,
				RAT_SEVEN_D_ZERO, true, RAT_EIGHT_D_ZERO, true);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv2);
		Interval itv3 = nf.newInterval(RAT_NINE_D_ZERO instanceof RealInteger,
				RAT_NINE_D_ZERO, false, RAT_ELEVEN_D_ZERO, false);
		IntervalUnionSet tmp3 = new IntervalUnionSet(itv3);
		IntervalUnionSet tmp4 = (IntervalUnionSet) tmp1.union(tmp2);
		IntervalUnionSet src1 = (IntervalUnionSet) tmp4.union(tmp3);
		Interval itv4 = nf.newInterval(RAT_ONE_D_ZERO instanceof RealInteger,
				RAT_ONE_D_ZERO, true, RAT_TWO_D_ZERO, true);
		IntervalUnionSet tmp5 = new IntervalUnionSet(itv4);
		Interval itv5 = nf.newInterval(RAT_THREE_D_ZERO instanceof RealInteger,
				RAT_THREE_D_ZERO, true, RAT_FOUR_D_ZERO, true);
		IntervalUnionSet tmp6 = new IntervalUnionSet(itv5);
		Interval itv6 = nf.newInterval(RAT_FIVE_D_ZERO instanceof RealInteger,
				RAT_FIVE_D_ZERO, true, RAT_NINE_D_ZERO, true);
		IntervalUnionSet tmp7 = new IntervalUnionSet(itv6);
		Interval itv7 = nf.newInterval(RAT_TEN_D_ZERO instanceof RealInteger,
				RAT_TEN_D_ZERO, true, null, true);
		IntervalUnionSet tmp8 = new IntervalUnionSet(itv7);
		IntervalUnionSet tmp9 = (IntervalUnionSet) tmp5.union(tmp6);
		IntervalUnionSet tmp10 = (IntervalUnionSet) tmp9.union(tmp7);
		IntervalUnionSet src2 = (IntervalUnionSet) tmp10.union(tmp8);
		IntervalUnionSet res1 = (IntervalUnionSet) src1.intersect(src2);
		Interval itv8 = nf.newInterval(RAT_TEN_D_ZERO instanceof RealInteger,
				null, true, RAT_ZERO, true);
		IntervalUnionSet src3 = new IntervalUnionSet(itv8);
		IntervalUnionSet res2 = (IntervalUnionSet) res1.union(src3);
		Interval itv9 = nf.newInterval(RAT_TEN_D_ZERO instanceof RealInteger,
				RAT_ZERO, false, RAT_ZERO, false);
		IntervalUnionSet src4 = new IntervalUnionSet(itv9);
		IntervalUnionSet res3 = (IntervalUnionSet) res2.intersect(src4);

		// TWO complex set.
		p(src1.toString());
		p(src2.toString());
		p(src3.toString());
		p(src4.toString());
		// Result
		p(res1.toString());
		p(res2.toString());
		p(res3.toString());
	}

	// public Range minus(Range set);
	@Test(expected = NullPointerException.class)
	public void iusMinusNullwithNull() {
		IntervalUnionSet src = new IntervalUnionSet();
		IntervalUnionSet element1 = null;
		src.minus(element1);
	}

	@Test
	public void iusMinusSimpleDisjointRat() {
		Interval itv1 = nf.newInterval(RAT_ZERO instanceof RealInteger, null,
				true, RAT_ZERO, false);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(RAT_ZERO instanceof RealInteger,
				RAT_ZERO, true, RAT_FIFTY_D_ZERO, false);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv2);
		Interval itv3 = nf.newInterval(
				RAT_SEVENTY_D_ZERO instanceof RealInteger, RAT_SEVENTY_D_ZERO,
				true, null, true);
		IntervalUnionSet tmp3 = new IntervalUnionSet(itv3);
		IntervalUnionSet res1 = (IntervalUnionSet) tmp1.minus(tmp2);
		IntervalUnionSet res2 = (IntervalUnionSet) tmp2.minus(tmp3);
		IntervalUnionSet res3 = (IntervalUnionSet) tmp3.minus(tmp1);

		// Three simple set.
		p(tmp1.toString());
		p(tmp2.toString());
		p(tmp3.toString());
		// Result
		p(res1.toString());
		p(res2.toString());
		p(res3.toString());
	}

	@Test
	public void iusMinusSimpleAdjacentInt() {
		Interval itv1 = nf.newInterval(INT_ZERO instanceof RealInteger, null,
				true, INT_ZERO, true);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(INT_ZERO instanceof RealInteger,
				INT_ZERO, true, INT_ONE, true);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv2);
		Interval itv3 = nf.newInterval(INT_ONE instanceof RealInteger, INT_ONE,
				true, INT_TEN, true);
		IntervalUnionSet tmp3 = new IntervalUnionSet(itv3);
		IntervalUnionSet res1 = (IntervalUnionSet) tmp1.minus(tmp2);
		IntervalUnionSet res2 = (IntervalUnionSet) tmp2.minus(tmp3);

		// Three simple set.
		p(tmp1.toString());
		p(tmp2.toString());
		p(tmp3.toString());
		// Result
		p(res1.toString());
		p(res2.toString());
	}

	@Test
	public void iusMinusSimpleOverlapRat() {
		Interval itv1 = nf.newInterval(RAT_TEN_D_ZERO instanceof RealInteger,
				null, true, RAT_THIRTY_D_ZERO, false);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(RAT_ZERO instanceof RealInteger,
				RAT_ZERO, true, RAT_FIFTY_D_ZERO, true);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv2);
		Interval itv3 = nf.newInterval(
				RAT_THIRTY_D_ZERO instanceof RealInteger, RAT_THIRTY_D_ZERO,
				false, null, true);
		IntervalUnionSet tmp3 = new IntervalUnionSet(itv3);
		IntervalUnionSet res1 = (IntervalUnionSet) tmp1.minus(tmp2);
		IntervalUnionSet res2 = (IntervalUnionSet) tmp2.minus(tmp3);
		IntervalUnionSet res3 = (IntervalUnionSet) tmp3.minus(tmp1);
		IntervalUnionSet res4 = (IntervalUnionSet) tmp1.minus(res1);
		IntervalUnionSet res5 = (IntervalUnionSet) tmp2.minus(res2);
		IntervalUnionSet res6 = (IntervalUnionSet) tmp3.minus(res3);

		// Three simple set.
		p(tmp1.toString());
		p(tmp2.toString());
		p(tmp3.toString());
		// Result
		p(res1.toString());
		p(res2.toString());
		p(res3.toString());
		p(res4.toString());
		p(res5.toString());
		p(res6.toString());
	}

	@Test
	public void iusMinusComplexRat() {
		Interval itv1 = nf.newInterval(RAT_ZERO instanceof RealInteger,
				RAT_ZERO, true, RAT_SIX_D_ZERO, false);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(RAT_SEVEN_D_ZERO instanceof RealInteger,
				RAT_SEVEN_D_ZERO, true, RAT_EIGHT_D_ZERO, true);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv2);
		Interval itv3 = nf.newInterval(RAT_NINE_D_ZERO instanceof RealInteger,
				RAT_NINE_D_ZERO, false, RAT_ELEVEN_D_ZERO, false);
		IntervalUnionSet tmp3 = new IntervalUnionSet(itv3);
		IntervalUnionSet tmp4 = (IntervalUnionSet) tmp1.union(tmp2);
		IntervalUnionSet src1 = (IntervalUnionSet) tmp4.union(tmp3);
		Interval itv4 = nf.newInterval(RAT_ONE_D_ZERO instanceof RealInteger,
				RAT_ONE_D_ZERO, true, RAT_TWO_D_ZERO, true);
		IntervalUnionSet tmp5 = new IntervalUnionSet(itv4);
		Interval itv5 = nf.newInterval(RAT_THREE_D_ZERO instanceof RealInteger,
				RAT_THREE_D_ZERO, true, RAT_FOUR_D_ZERO, true);
		IntervalUnionSet tmp6 = new IntervalUnionSet(itv5);
		Interval itv6 = nf.newInterval(RAT_FIVE_D_ZERO instanceof RealInteger,
				RAT_FIVE_D_ZERO, true, RAT_NINE_D_ZERO, true);
		IntervalUnionSet tmp7 = new IntervalUnionSet(itv6);
		Interval itv7 = nf.newInterval(RAT_TEN_D_ZERO instanceof RealInteger,
				RAT_TEN_D_ZERO, true, null, true);
		IntervalUnionSet tmp8 = new IntervalUnionSet(itv7);
		IntervalUnionSet tmp9 = (IntervalUnionSet) tmp5.union(tmp6);
		IntervalUnionSet tmp10 = (IntervalUnionSet) tmp9.union(tmp7);
		IntervalUnionSet src2 = (IntervalUnionSet) tmp10.union(tmp8);
		IntervalUnionSet res1 = (IntervalUnionSet) src1.minus(src2);
		Interval itv8 = nf.newInterval(RAT_TEN_D_ZERO instanceof RealInteger,
				null, true, null, true);
		IntervalUnionSet src3 = new IntervalUnionSet(itv8);
		IntervalUnionSet res2 = (IntervalUnionSet) src3.minus(res1);
		IntervalUnionSet res3 = (IntervalUnionSet) res2.minus(res2);

		// TWO complex set.
		p(src1.toString());
		p(src2.toString());
		p(src3.toString());
		// Result
		p(res1.toString());
		p(res2.toString());
		p(res3.toString());
	}

	// public Range complement();
	@Test
	public void iusComplementwithUnivOrEmpty() {
		Interval univ = nf.newInterval(RAT_ZERO instanceof RealInteger, null,
				true, null, true);
		IntervalUnionSet univSet = new IntervalUnionSet(univ);
		IntervalUnionSet res = (IntervalUnionSet) univSet.complement();
		IntervalUnionSet res2 = (IntervalUnionSet) res.complement();

		p(res.toString());
		p(res2.toString());
	}

	@Test
	public void iusComplementSimpleDisjointRat() {
		Interval itv1 = nf.newInterval(RAT_ZERO instanceof RealInteger, null,
				true, RAT_ZERO, false);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(RAT_ZERO instanceof RealInteger,
				RAT_ZERO, true, RAT_FIFTY_D_ZERO, false);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv2);
		Interval itv3 = nf.newInterval(
				RAT_SEVENTY_D_ZERO instanceof RealInteger, RAT_SEVENTY_D_ZERO,
				true, null, true);
		IntervalUnionSet tmp3 = new IntervalUnionSet(itv3);
		IntervalUnionSet res1 = (IntervalUnionSet) tmp1.complement();
		IntervalUnionSet res2 = (IntervalUnionSet) tmp2.complement();
		IntervalUnionSet res3 = (IntervalUnionSet) tmp3.complement();

		// Three simple set.
		p(tmp1.toString());
		p(tmp2.toString());
		p(tmp3.toString());
		// Result
		p(res1.toString());
		p(res2.toString());
		p(res3.toString());
	}

	@Test
	public void iusComplementSimpleAdjacentInt() {
		Interval itv1 = nf.newInterval(INT_ZERO instanceof RealInteger, null,
				true, INT_ZERO, true);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(INT_ONE instanceof RealInteger, INT_ONE,
				false, INT_ONE, false);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv2);
		Interval itv3 = nf.newInterval(INT_ONE instanceof RealInteger, INT_ONE,
				true, INT_TEN, true);
		IntervalUnionSet tmp3 = new IntervalUnionSet(itv3);
		IntervalUnionSet res1 = (IntervalUnionSet) tmp1.complement();
		IntervalUnionSet res2 = (IntervalUnionSet) tmp2.complement();
		IntervalUnionSet res3 = (IntervalUnionSet) tmp3.complement();

		// Three simple set.
		p(tmp1.toString());
		p(tmp2.toString());
		p(tmp3.toString());
		// Result
		p(res1.toString());
		p(res2.toString());
		p(res3.toString());
	}

	@Test
	public void iusComplementSimpleOverlapRat() {
		Interval itv1 = nf.newInterval(RAT_TEN_D_ZERO instanceof RealInteger,
				null, true, RAT_THIRTY_D_ZERO, false);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(RAT_ZERO instanceof RealInteger,
				RAT_ZERO, true, RAT_FIFTY_D_ZERO, true);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv2);
		Interval itv3 = nf.newInterval(
				RAT_THIRTY_D_ZERO instanceof RealInteger, RAT_THIRTY_D_ZERO,
				false, null, true);
		IntervalUnionSet tmp3 = new IntervalUnionSet(itv3);
		IntervalUnionSet res1 = (IntervalUnionSet) tmp1.complement();
		IntervalUnionSet res2 = (IntervalUnionSet) tmp2.complement();
		IntervalUnionSet res3 = (IntervalUnionSet) tmp3.complement();
		IntervalUnionSet res4 = (IntervalUnionSet) res1.complement();
		IntervalUnionSet res5 = (IntervalUnionSet) res2.complement();
		IntervalUnionSet res6 = (IntervalUnionSet) res3.complement();

		// Three simple set.
		p(tmp1.toString());
		p(tmp2.toString());
		p(tmp3.toString());
		// Result
		p(res1.toString());
		p(res2.toString());
		p(res3.toString());
		p(res4.toString());
		p(res5.toString());
		p(res6.toString());
	}

	@Test
	public void iusComplementComplexRat() {
		Interval itv1 = nf.newInterval(RAT_ZERO instanceof RealInteger,
				RAT_ZERO, true, RAT_SIX_D_ZERO, false);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(RAT_SEVEN_D_ZERO instanceof RealInteger,
				RAT_SEVEN_D_ZERO, true, RAT_EIGHT_D_ZERO, true);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv2);
		Interval itv3 = nf.newInterval(RAT_NINE_D_ZERO instanceof RealInteger,
				RAT_NINE_D_ZERO, false, RAT_ELEVEN_D_ZERO, false);
		IntervalUnionSet tmp3 = new IntervalUnionSet(itv3);
		IntervalUnionSet tmp4 = (IntervalUnionSet) tmp1.union(tmp2);
		IntervalUnionSet src1 = (IntervalUnionSet) tmp4.union(tmp3);
		Interval itv4 = nf.newInterval(RAT_ONE_D_ZERO instanceof RealInteger,
				RAT_ONE_D_ZERO, true, RAT_TWO_D_ZERO, true);
		IntervalUnionSet tmp5 = new IntervalUnionSet(itv4);
		Interval itv5 = nf.newInterval(RAT_THREE_D_ZERO instanceof RealInteger,
				RAT_THREE_D_ZERO, true, RAT_FOUR_D_ZERO, true);
		IntervalUnionSet tmp6 = new IntervalUnionSet(itv5);
		Interval itv6 = nf.newInterval(RAT_FIVE_D_ZERO instanceof RealInteger,
				RAT_FIVE_D_ZERO, true, RAT_NINE_D_ZERO, true);
		IntervalUnionSet tmp7 = new IntervalUnionSet(itv6);
		Interval itv7 = nf.newInterval(RAT_TEN_D_ZERO instanceof RealInteger,
				RAT_TEN_D_ZERO, true, null, true);
		IntervalUnionSet tmp8 = new IntervalUnionSet(itv7);
		IntervalUnionSet tmp9 = (IntervalUnionSet) tmp5.union(tmp6);
		IntervalUnionSet tmp10 = (IntervalUnionSet) tmp9.union(tmp7);
		IntervalUnionSet src2 = (IntervalUnionSet) tmp10.union(tmp8);
		IntervalUnionSet res1 = (IntervalUnionSet) src1.complement();
		Interval itv8 = nf.newInterval(RAT_TEN_D_ZERO instanceof RealInteger,
				null, true, null, true);
		IntervalUnionSet src3 = new IntervalUnionSet(itv8);
		IntervalUnionSet res2 = (IntervalUnionSet) src2.complement();
		IntervalUnionSet res3 = (IntervalUnionSet) src3.complement();

		// TWO complex set.
		p(src1.toString());
		p(src2.toString());
		p(src3.toString());
		// Result
		p(res1.toString());
		p(res2.toString());
		p(res3.toString());
	}

	// public Range affineTransform(Number a, Number b)
	@Test(expected = NullPointerException.class)
	public void iusAffineTransformWithNullInputA() {
		Interval itv = nf.newInterval(INT_ZERO instanceof RealInteger,
				INT_ZERO, false, INT_TEN, false);
		IntervalUnionSet src = new IntervalUnionSet(itv);
		IntervalUnionSetTest res = (IntervalUnionSetTest) src.affineTransform(
				null, INT_NEG_TEN);

		p(res.toString());
	}

	@Test(expected = NullPointerException.class)
	public void iusAffineTransformWithNullInputB() {
		Interval itv = nf.newInterval(INT_ZERO instanceof RealInteger,
				INT_ZERO, false, INT_TEN, false);
		IntervalUnionSet src = new IntervalUnionSet(itv);
		IntervalUnionSetTest res = (IntervalUnionSetTest) src.affineTransform(
				INT_TEN, null);

		p(res.toString());
	}

	@Test
	public void iusAffineTransformWithSimpleSet() {
		Interval itv = nf.newInterval(INT_ZERO instanceof RealInteger,
				INT_ZERO, false, INT_TEN, false);
		IntervalUnionSet src = new IntervalUnionSet(itv);
		IntervalUnionSet res = (IntervalUnionSet) src.affineTransform(INT_TEN,
				INT_NEG_TEN);

		p(res.toString());
	}

	@Test
	public void iusAffineTransformWithSimpleSetAndNegA() {
		Interval itv = nf.newInterval(INT_ZERO instanceof RealInteger,
				INT_ZERO, false, INT_TEN, false);
		IntervalUnionSet src = new IntervalUnionSet(itv);
		IntervalUnionSet res = (IntervalUnionSet) src.affineTransform(
				INT_NEG_TEN, INT_TEN);

		p(res.toString());
	}

	@Test
	public void iusAffineTransformWithComplexSet() {
		Interval itv1 = nf.newInterval(RAT_ONE_D_ZERO instanceof RealInteger,
				RAT_ONE_D_ZERO, true, RAT_TWO_D_ZERO, true);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(RAT_THREE_D_ZERO instanceof RealInteger,
				RAT_THREE_D_ZERO, true, RAT_FOUR_D_ZERO, true);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv2);
		Interval itv3 = nf.newInterval(RAT_FIVE_D_ZERO instanceof RealInteger,
				RAT_FIVE_D_ZERO, true, RAT_NINE_D_ZERO, true);
		IntervalUnionSet tmp3 = new IntervalUnionSet(itv3);
		Interval itv4 = nf.newInterval(RAT_TEN_D_ZERO instanceof RealInteger,
				RAT_TEN_D_ZERO, true, null, true);
		IntervalUnionSet tmp4 = new IntervalUnionSet(itv4);
		IntervalUnionSet tmp5 = (IntervalUnionSet) tmp1.union(tmp2);
		IntervalUnionSet tmp6 = (IntervalUnionSet) tmp5.union(tmp3);
		IntervalUnionSet src = (IntervalUnionSet) tmp6.union(tmp4);
		IntervalUnionSet res = (IntervalUnionSet) src.affineTransform(
				RAT_NEG_ONE_D_ZERO, RAT_ZERO);

		p(src.toString());
		p(res.toString());
	}

	// public boolean intersects(Range set);
	@Test(expected = NullPointerException.class)
	public void iusIntersectsNullInput() {
		Interval itv1 = nf.newInterval(RAT_THREE_D_ZERO instanceof RealInteger,
				RAT_THREE_D_ZERO, true, RAT_NINE_D_ZERO, true);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv1);
		IntervalUnionSet tmp2 = null;
		tmp1.intersects(tmp2);
	}

	@Test
	public void iusIntersectsSimpleSetDisjoint() {
		Interval itv1 = nf.newInterval(INT_NEG_TEN instanceof RealInteger,
				INT_NEG_TEN, true, INT_ZERO, true);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(INT_ONE instanceof RealInteger, INT_ONE,
				true, INT_TEN, true);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv2);

		assertFalse(tmp1.intersects(tmp2));
	}

	@Test
	public void iusIntersectsSimpleSetAdjacent() {
		Interval itv1 = nf.newInterval(RAT_ONE_D_ZERO instanceof RealInteger,
				RAT_ONE_D_ZERO, true, RAT_TWO_D_ZERO, false);
		IntervalUnionSet src = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(RAT_TWO_D_ZERO instanceof RealInteger,
				RAT_TWO_D_ZERO, true, RAT_THREE_D_ZERO, true);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv2);
		Interval itv3 = nf.newInterval(RAT_TWO_D_ZERO instanceof RealInteger,
				RAT_TWO_D_ZERO, false, RAT_FOUR_D_ZERO, false);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv3);

		assertFalse(src.intersects(tmp1));
		assertTrue(src.intersects(tmp2));
	}

	@Test
	public void iusIntersectsSimpleSetOverlap() {
		Interval itv1 = nf.newInterval(RAT_ONE_D_ZERO instanceof RealInteger,
				RAT_ONE_D_ZERO, true, RAT_FIVE_D_ZERO, false);
		IntervalUnionSet src = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(RAT_TWO_D_ZERO instanceof RealInteger,
				RAT_TWO_D_ZERO, true, RAT_THREE_D_ZERO, true);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv2);
		Interval itv3 = nf.newInterval(RAT_TWO_D_ZERO instanceof RealInteger,
				RAT_TWO_D_ZERO, false, RAT_SEVEN_D_ZERO, false);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv3);

		assertTrue(src.intersects(tmp1));
		assertTrue(src.intersects(tmp2));
	}

	@Test
	public void iusIntersectsComplexSetDisjoint() {
		Interval itv4 = nf.newInterval(RAT_ONE_D_ZERO instanceof RealInteger,
				RAT_ONE_D_ZERO, true, RAT_TWO_D_ZERO, false);
		IntervalUnionSet tmp4 = new IntervalUnionSet(itv4);
		Interval itv5 = nf.newInterval(RAT_FIVE_D_ZERO instanceof RealInteger,
				RAT_FIVE_D_ZERO, true, RAT_SIX_D_ZERO, true);
		IntervalUnionSet tmp5 = new IntervalUnionSet(itv5);
		Interval itv6 = nf.newInterval(RAT_NINE_D_ZERO instanceof RealInteger,
				RAT_NINE_D_ZERO, false, RAT_TEN_D_ZERO, false);
		IntervalUnionSet tmp6 = new IntervalUnionSet(itv6);
		IntervalUnionSet src2 = (IntervalUnionSet) tmp4.union(tmp5).union(tmp6);
		Interval itv7 = nf.newInterval(RAT_THREE_D_ZERO instanceof RealInteger,
				RAT_THREE_D_ZERO, true, RAT_FOUR_D_ZERO, false);
		IntervalUnionSet tmp7 = new IntervalUnionSet(itv7);
		Interval itv8 = nf.newInterval(RAT_SEVEN_D_ZERO instanceof RealInteger,
				RAT_SEVEN_D_ZERO, true, RAT_EIGHT_D_ZERO, true);
		IntervalUnionSet tmp8 = new IntervalUnionSet(itv8);
		Interval itv9 = nf.newInterval(
				RAT_ELEVEN_D_ZERO instanceof RealInteger, RAT_ELEVEN_D_ZERO,
				false, RAT_FIFTY_D_ZERO, false);
		IntervalUnionSet tmp9 = new IntervalUnionSet(itv9);
		IntervalUnionSet src3 = (IntervalUnionSet) tmp7.union(tmp8).union(tmp9);

		assertFalse(src2.intersects(src3));
		assertFalse(src3.intersects(src2));
	}

	@Test
	public void iusIntersectsComplexSetOverlap() {
		Interval itv1 = nf.newInterval(RAT_ZERO instanceof RealInteger,
				RAT_ZERO, true, RAT_THREE_D_ZERO, false);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(RAT_FOUR_D_ZERO instanceof RealInteger,
				RAT_FOUR_D_ZERO, true, RAT_SEVEN_D_ZERO, true);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv2);
		Interval itv3 = nf.newInterval(RAT_EIGHT_D_ZERO instanceof RealInteger,
				RAT_EIGHT_D_ZERO, false, RAT_ELEVEN_D_ZERO, false);
		IntervalUnionSet tmp3 = new IntervalUnionSet(itv3);
		IntervalUnionSet src1 = (IntervalUnionSet) tmp1.union(tmp2).union(tmp3);
		Interval itv4 = nf.newInterval(RAT_ONE_D_ZERO instanceof RealInteger,
				RAT_ONE_D_ZERO, true, RAT_TWO_D_ZERO, false);
		IntervalUnionSet tmp4 = new IntervalUnionSet(itv4);
		Interval itv5 = nf.newInterval(RAT_FIVE_D_ZERO instanceof RealInteger,
				RAT_FIVE_D_ZERO, true, RAT_SIX_D_ZERO, true);
		IntervalUnionSet tmp5 = new IntervalUnionSet(itv5);
		Interval itv6 = nf.newInterval(RAT_NINE_D_ZERO instanceof RealInteger,
				RAT_NINE_D_ZERO, false, RAT_TEN_D_ZERO, false);
		IntervalUnionSet tmp6 = new IntervalUnionSet(itv6);
		IntervalUnionSet src2 = (IntervalUnionSet) tmp4.union(tmp5).union(tmp6);

		assertTrue(src1.intersects(src2));
		assertTrue(src2.intersects(src1));
	}

	// public boolean contains(Range set)
	@Test(expected = NullPointerException.class)
	public void iusContainsNullInput() {
		Interval itv1 = nf.newInterval(RAT_THREE_D_ZERO instanceof RealInteger,
				RAT_THREE_D_ZERO, true, RAT_NINE_D_ZERO, true);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv1);
		IntervalUnionSet tmp2 = null;
		tmp1.contains(tmp2);
	}

	@Test
	public void iusContainsSimpleSetDisjoint() {
		Interval itv1 = nf.newInterval(INT_NEG_TEN instanceof RealInteger,
				INT_NEG_TEN, true, INT_ZERO, true);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(INT_ONE instanceof RealInteger, INT_ONE,
				true, INT_TEN, true);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv2);

		assertFalse(tmp1.contains(tmp2));
	}

	@Test
	public void iusContainsSimpleSetAdjacent() {
		Interval itv1 = nf.newInterval(RAT_ONE_D_ZERO instanceof RealInteger,
				RAT_ONE_D_ZERO, true, RAT_TWO_D_ZERO, false);
		IntervalUnionSet src = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(RAT_TWO_D_ZERO instanceof RealInteger,
				RAT_TWO_D_ZERO, true, RAT_THREE_D_ZERO, true);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv2);
		Interval itv3 = nf.newInterval(RAT_TWO_D_ZERO instanceof RealInteger,
				RAT_TWO_D_ZERO, false, RAT_FOUR_D_ZERO, false);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv3);

		assertFalse(src.contains(tmp1));
		assertFalse(src.contains(tmp2));
	}

	@Test
	public void iusContainsSimpleSetOverlap() {
		Interval itv1 = nf.newInterval(RAT_ONE_D_ZERO instanceof RealInteger,
				RAT_ONE_D_ZERO, true, RAT_FIVE_D_ZERO, false);
		IntervalUnionSet src = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(RAT_TWO_D_ZERO instanceof RealInteger,
				RAT_TWO_D_ZERO, true, RAT_THREE_D_ZERO, true);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv2);
		Interval itv3 = nf.newInterval(RAT_TWO_D_ZERO instanceof RealInteger,
				RAT_TWO_D_ZERO, false, RAT_SEVEN_D_ZERO, false);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv3);

		assertTrue(src.contains(tmp1));
		assertFalse(src.contains(tmp2));
	}

	@Test
	public void iusContainsComplexSetDisjoint() {
		Interval itv4 = nf.newInterval(RAT_ONE_D_ZERO instanceof RealInteger,
				RAT_ONE_D_ZERO, true, RAT_TWO_D_ZERO, false);
		IntervalUnionSet tmp4 = new IntervalUnionSet(itv4);
		Interval itv5 = nf.newInterval(RAT_FIVE_D_ZERO instanceof RealInteger,
				RAT_FIVE_D_ZERO, true, RAT_SIX_D_ZERO, true);
		IntervalUnionSet tmp5 = new IntervalUnionSet(itv5);
		Interval itv6 = nf.newInterval(RAT_NINE_D_ZERO instanceof RealInteger,
				RAT_NINE_D_ZERO, false, RAT_TEN_D_ZERO, false);
		IntervalUnionSet tmp6 = new IntervalUnionSet(itv6);
		IntervalUnionSet src2 = (IntervalUnionSet) tmp4.union(tmp5).union(tmp6);
		Interval itv7 = nf.newInterval(RAT_THREE_D_ZERO instanceof RealInteger,
				RAT_THREE_D_ZERO, true, RAT_FOUR_D_ZERO, false);
		IntervalUnionSet tmp7 = new IntervalUnionSet(itv7);
		Interval itv8 = nf.newInterval(RAT_SEVEN_D_ZERO instanceof RealInteger,
				RAT_SEVEN_D_ZERO, true, RAT_EIGHT_D_ZERO, true);
		IntervalUnionSet tmp8 = new IntervalUnionSet(itv8);
		Interval itv9 = nf.newInterval(
				RAT_ELEVEN_D_ZERO instanceof RealInteger, RAT_ELEVEN_D_ZERO,
				false, RAT_FIFTY_D_ZERO, false);
		IntervalUnionSet tmp9 = new IntervalUnionSet(itv9);
		IntervalUnionSet src3 = (IntervalUnionSet) tmp7.union(tmp8).union(tmp9);

		assertFalse(src2.contains(src3));
		assertFalse(src3.contains(src2));
	}

	@Test
	public void iusContainsComplexSetOverlap() {
		Interval itv1 = nf.newInterval(RAT_ZERO instanceof RealInteger,
				RAT_ZERO, true, RAT_THREE_D_ZERO, false);
		IntervalUnionSet tmp1 = new IntervalUnionSet(itv1);
		Interval itv2 = nf.newInterval(RAT_FOUR_D_ZERO instanceof RealInteger,
				RAT_FOUR_D_ZERO, true, RAT_SEVEN_D_ZERO, true);
		IntervalUnionSet tmp2 = new IntervalUnionSet(itv2);
		Interval itv3 = nf.newInterval(RAT_EIGHT_D_ZERO instanceof RealInteger,
				RAT_EIGHT_D_ZERO, false, RAT_ELEVEN_D_ZERO, false);
		IntervalUnionSet tmp3 = new IntervalUnionSet(itv3);
		IntervalUnionSet src1 = (IntervalUnionSet) tmp1.union(tmp2).union(tmp3);
		Interval itv4 = nf.newInterval(RAT_ONE_D_ZERO instanceof RealInteger,
				RAT_ONE_D_ZERO, true, RAT_TWO_D_ZERO, false);
		IntervalUnionSet tmp4 = new IntervalUnionSet(itv4);
		Interval itv5 = nf.newInterval(RAT_FIVE_D_ZERO instanceof RealInteger,
				RAT_FIVE_D_ZERO, true, RAT_SIX_D_ZERO, true);
		IntervalUnionSet tmp5 = new IntervalUnionSet(itv5);
		Interval itv6 = nf.newInterval(RAT_NINE_D_ZERO instanceof RealInteger,
				RAT_NINE_D_ZERO, false, RAT_TEN_D_ZERO, false);
		IntervalUnionSet tmp6 = new IntervalUnionSet(itv6);
		IntervalUnionSet src2 = (IntervalUnionSet) tmp4.union(tmp5).union(tmp6);

		assertTrue(src1.contains(src2));
		assertFalse(src2.contains(src1));
	}

	// public void addNumber(Number number)
	@Test(expected = NullPointerException.class)
	public void iusAddNumberNullInput() {
		Interval itv = nf.newInterval(INT_ZERO instanceof RealInteger,
				INT_ZERO, false, INT_TEN, false);
		IntervalUnionSet src = new IntervalUnionSet(itv);
		RealInteger num = null;
		src.addNumber(num);
	}

	@Test
	public void iusAddNumberDisjoint() {
		Interval itv = nf.newInterval(INT_ZERO instanceof RealInteger,
				INT_ZERO, false, INT_TEN, false);
		IntervalUnionSet src = new IntervalUnionSet(itv);

		p(src.toString());
		src.addNumber(INT_NEG_TEN);
		p(src.toString());
	}

	@Test
	public void iusAddNumberAdjacent() {
		Interval itv = nf.newInterval(RAT_ONE_D_ZERO instanceof RealInteger,
				RAT_ONE_D_ZERO, true, RAT_TEN_D_ZERO, true);
		IntervalUnionSet src = new IntervalUnionSet(itv);

		p(src.toString());
		src.addNumber(RAT_ONE_D_ZERO);
		p(src.toString());
		src.addNumber(RAT_TEN_D_ZERO);
		p(src.toString());
	}

	@Test
	public void iusAddNumberIncluded() {
		Interval itv = nf.newInterval(RAT_ONE_D_ZERO instanceof RealInteger,
				RAT_ONE_D_ZERO, false, RAT_TEN_D_ZERO, false);
		IntervalUnionSet src = new IntervalUnionSet(itv);

		p(src.toString());
		src.addNumber(RAT_ONE_D_ZERO);
		p(src.toString());
		src.addNumber(RAT_TEN_D_ZERO);
		p(src.toString());
	}

	// public BooleanExpression symbolicRepresentation(SymbolicConstant
	// x,PreUniverse universe)
	@Test
	public void iusSymbolicRepresentationTest() {
		NumericSymbolicConstant x = (NumericSymbolicConstant) universe
				.symbolicConstant(universe.stringObject("X"),
						universe.realType());
		SymbolicExpression expected = universe.and(
				universe.lessThan(universe.zeroReal(), x),
				universe.lessThan(x, universe.rational(10.00)));
		Interval itv = nf.newInterval(false, RAT_ZERO, true, RAT_TEN_D_ZERO,
				true);
		IntervalUnionSet ius = new IntervalUnionSet(itv);
		SymbolicExpression actual = ius.symbolicRepresentation(x, universe);

		System.out.println(expected.toString());
		System.out.println(actual.toString());
		assertEquals(expected, actual);
	}

	private void p(String s) {
		if (showLog) {
			System.out.println(s);
		}
	}
}
