package edu.udel.cis.vsl.sarl.collections;

import static edu.udel.cis.vsl.sarl.collections.SetTest.BinaryOpKind.ADD_ALL;
import static edu.udel.cis.vsl.sarl.collections.SetTest.BinaryOpKind.KEEP_ONLY;
import static edu.udel.cis.vsl.sarl.collections.SetTest.BinaryOpKind.REMOVE_ALL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.IF.SortedSymbolicSet;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSet;
import edu.udel.cis.vsl.sarl.collections.common.CommonSymbolicCollection;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

/**
 * Test of type {@link SymbolicSet}.
 * 
 * @author siegel
 *
 */
public class SetTest {

	public static enum BinaryOpKind {
		ADD_ALL, REMOVE_ALL, KEEP_ONLY
	}

	private static PreUniverse universe = (PreUniverse) SARL
			.newStandardUniverse();

	private static CollectionFactory cf = universe.collectionFactory();

	private Collection<SymbolicExpression> emptyList = new LinkedList<>();

	private SymbolicExpression zero = universe.integer(0), one = universe
			.integer(1), two = universe.integer(2),
			three = universe.integer(3), four = universe.integer(4),
			five = universe.integer(5);

	SymbolicSet<SymbolicExpression> set1 = cf.emptySortedSet(), set2 = cf
			.emptySortedSet();

	private void checkSet(Collection<? extends SymbolicExpression> expected,
			SymbolicSet<SymbolicExpression> actual) {
		if (actual instanceof SortedSymbolicSet<?>) {
			SymbolicExpression[] expectedArray = expected
					.toArray(new SymbolicExpression[0]);
			Comparator<? super SymbolicExpression> c = ((SortedSymbolicSet<SymbolicExpression>) actual)
					.comparator();
			List<SymbolicExpression> list1 = new LinkedList<SymbolicExpression>();
			List<SymbolicExpression> list2 = new LinkedList<SymbolicExpression>();

			Arrays.sort(expectedArray, c);
			for (SymbolicExpression x : expectedArray)
				list1.add(x);
			for (SymbolicExpression x : actual)
				list2.add(x);
			assertEquals(list1, list2);
		} else {
			Set<SymbolicExpression> set1 = new HashSet<SymbolicExpression>(
					expected);
			Set<SymbolicExpression> set2 = new HashSet<SymbolicExpression>();

			for (SymbolicExpression x : actual)
				set2.add(x);
			assertEquals(set1, set2);
		}
	}

	private void binaryOpTestAux(BinaryOpKind op,
			Collection<SymbolicExpression> elements1,
			Collection<SymbolicExpression> elements2, boolean commit) {
		SymbolicSet<SymbolicExpression> set1 = cf.emptySortedSet(), set2 = cf
				.emptySortedSet();

		for (SymbolicExpression x : elements1)
			set1 = set1.add(x);
		for (SymbolicExpression y : elements2)
			set2 = set2.add(y);
		if (commit)
			set1.commit();

		SymbolicSet<SymbolicExpression> result;

		switch (op) {
		case ADD_ALL:
			result = set1.addAll(set2);
			break;
		case KEEP_ONLY:
			result = set1.keepOnly(set2);
			break;
		case REMOVE_ALL:
			result = set1.removeAll(set2);
			break;
		default:
			throw new RuntimeException("Unknown Operator: " + op);
		}

		Set<SymbolicExpression> expected = new HashSet<>(elements1);

		switch (op) {
		case ADD_ALL:
			expected.addAll(elements2);
			break;
		case KEEP_ONLY:
			expected.retainAll(elements2);
			break;
		case REMOVE_ALL:
			expected.removeAll(elements2);
			break;
		default:
			throw new RuntimeException("Unknown Operator: " + op);
		}
		checkSet(expected, result);
		checkSet(elements2, set2);
		if (commit)
			checkSet(elements1, set1);
	}

	private void binaryOpTest(BinaryOpKind op,
			Collection<SymbolicExpression> elements1,
			Collection<SymbolicExpression> elements2) {
		binaryOpTestAux(op, elements1, elements2, true);
		binaryOpTestAux(op, elements1, elements2, false);
	}

	private void addAllTest(Collection<SymbolicExpression> elements1,
			Collection<SymbolicExpression> elements2) {
		binaryOpTest(ADD_ALL, elements1, elements2);
	}

	private void keepOnlyTest(Collection<SymbolicExpression> elements1,
			Collection<SymbolicExpression> elements2) {
		binaryOpTest(KEEP_ONLY, elements1, elements2);
	}

	private void removeAllTest(Collection<SymbolicExpression> elements1,
			Collection<SymbolicExpression> elements2) {
		binaryOpTest(REMOVE_ALL, elements1, elements2);
	}

	@Test
	public void add0() {
		assertEquals(0, set1.size());
		assertTrue(two.isFree());
		set1 = set1.add(two);
		assertFalse(two.isFree());
		assertFalse(two.isImmutable());
		assertEquals(1, set1.size());
		assertEquals(two, set1.getFirst());
	}

	@Test
	public void add1() {
		SymbolicSet<SymbolicExpression> set = cf.singletonSortedSet(three);

		set.add(four);
		checkSet(Arrays.asList(three, four), set);
		set.add(two);
		checkSet(Arrays.asList(two, three, four), set);
		checkSet(Arrays.asList(two, three, four), set.add(three));
	}

	@Test
	public void addBig() {
		int n = 100;
		List<SymbolicExpression> expected = new LinkedList<>();

		for (int i = 1; i <= n; i++) {
			set1 = set1.add(universe.integer(n + 1 - i));
		}
		for (int i = 1; i <= n; i++) {
			expected.add(universe.integer(i));
		}
		checkSet(expected, set1);
	}

	@Test
	public void addAll_31_2() {
		assertFalse(two.isImmutable());
		addAllTest(Arrays.asList(three, one), Arrays.asList(two));
		assertTrue(one.isImmutable());
		assertTrue(three.isImmutable());
	}

	@Test
	public void addAllNothing() {
		addAllTest(Arrays.asList(five), emptyList);
	}

	@Test
	public void addAllBig() {
		int n = 100;
		List<SymbolicExpression> big = new LinkedList<>();

		for (int i = 0; i < n; i++)
			big.add(universe.integer(i));
		addAllTest(Arrays.asList(zero), big);
	}

	@Test
	public void addAll_3_2() {
		addAllTest(Arrays.asList(three), Arrays.asList(two));
	}

	@Test
	public void addAll_235_345() {
		addAllTest(Arrays.asList(two, three, five),
				Arrays.asList(three, four, five));
	}

	@Test
	public void addAll_1_2() {
		addAllTest(Arrays.asList(one), Arrays.asList(two));
	}

	@Test
	public void addAll_234_2() {
		addAllTest(Arrays.asList(two, three, four), Arrays.asList(two));
	}

	@Test
	public void addAll_2_34() {
		addAllTest(Arrays.asList(two), Arrays.asList(three, four));
	}

	@Test(expected = SARLException.class)
	public void addAllFake() {
		set2 = new FakeSet();
		set1 = set1.add(zero);
		set1.addAll(set2);
	}

	@Test
	public void remove1() {
		set1 = set1.add(three);
		set1 = set1.add(two);
		set1 = set1.add(one);
		set1 = set1.remove(three);
		checkSet(Arrays.asList(one, two), set1);
	}

	@Test
	public void removeNonmember() {
		set1 = set1.add(two);
		set1 = set1.add(one);
		set1 = set1.remove(three);
		checkSet(Arrays.asList(one, two), set1);
	}

	@Test
	public void removeImmut() {
		set1 = set1.add(two);
		set1 = set1.add(one);
		set1 = set1.add(three);
		set1.commit();
		set2 = set1.remove(three);
		checkSet(Arrays.asList(one, two), set2);
		set2 = set1.remove(two);
		checkSet(Arrays.asList(one, three), set2);
		set2 = set1.remove(one);
		checkSet(Arrays.asList(two, three), set2);
		set2 = set2.remove(three);
		checkSet(Arrays.asList(two), set2);
		set2 = set2.remove(two);
		checkSet(new LinkedList<SymbolicExpression>(), set2);
		checkSet(Arrays.asList(one, two, three), set1);
	}

	@Test
	public void removeAll_213_13() {
		removeAllTest(Arrays.asList(two, one, three), Arrays.asList(one, three));
	}

	@Test
	public void removeAll_2_3() {
		removeAllTest(Arrays.asList(two), Arrays.asList(three));
	}

	@Test
	public void removeAll_345_2() {
		removeAllTest(Arrays.asList(three, four, five), Arrays.asList(two));
	}

	@Test
	public void removeAll_4_23() {
		removeAllTest(Arrays.asList(four), Arrays.asList(two, three));
	}

	@Test
	public void removeAllNothing() {
		removeAllTest(emptyList, emptyList);
	}

	@Test
	public void removeAllNothing2() {
		removeAllTest(Arrays.asList(zero), emptyList);
	}

	@Test(expected = SARLException.class)
	public void removeAllFake() {
		set2 = new FakeSet();
		set1 = set1.add(zero);
		set1.removeAll(set2);
	}

	@Test
	public void keepOnly_21_23() {
		keepOnlyTest(Arrays.asList(two, one), Arrays.asList(two, three));
	}

	@Test
	public void keepOnly_3_2() {
		keepOnlyTest(Arrays.asList(three), Arrays.asList(two));
	}

	@Test
	public void keepOnly_43_2() {
		keepOnlyTest(Arrays.asList(four, three), Arrays.asList(two));
	}

	@Test
	public void keepOnly_3_24() {
		keepOnlyTest(Arrays.asList(three), Arrays.asList(two, four));
	}

	@Test
	public void keepOnly_2_2() {
		keepOnlyTest(Arrays.asList(two), Arrays.asList(two));
	}

	@Test
	public void keepOnly_23_2() {
		keepOnlyTest(Arrays.asList(two, three), Arrays.asList(two));
	}

	@Test
	public void keepOnly_2_3() {
		keepOnlyTest(Arrays.asList(two), Arrays.asList(three));
	}

	@Test
	public void keepOnlyEmpty1() {
		keepOnlyTest(emptyList, emptyList);
	}

	@Test
	public void keepOnlyEmpty2() {
		keepOnlyTest(Arrays.asList(zero), emptyList);
	}

	@Test(expected = SARLException.class)
	public void keepOnlyFake() {
		set2 = new FakeSet();
		set1 = set1.add(zero);
		set1.keepOnly(set2);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void canonic() {
		set1.commit();
		set2 = set2.add(universe.integer(10));
		set2 = set2.add(universe.integer(20));
		set1 = set1.addAll(set2);
		set1 = (SymbolicSet<SymbolicExpression>) universe.canonic(set1);
		assertTrue(set1.isCanonic());
		for (SymbolicExpression x : set1)
			assertTrue(x.isCanonic());
	}

	@Test(expected = SARLException.class)
	public void removeIter() {
		set1 = set1.add(universe.integer(10));

		Iterator<SymbolicExpression> iter = set1.iterator();

		iter.next();
		iter.remove();
	}

	@Test
	public void contains() {
		set1 = set1.add(universe.integer(10));
		assertTrue(set1.contains(universe.integer(10)));
		assertFalse(set1.contains(universe.integer(11)));
	}

	@Test
	public void equalsYes() {
		set1 = set1.add(one);
		set2 = set2.add(one);
		assertEquals(set1, set2);
	}

	@Test
	public void equalsNo() {
		set1 = set1.add(one);
		set1 = set1.add(two);
		set2 = set2.add(one);
		set2 = set2.add(three);
		assertFalse(set1.equals(set2));
	}

	@Test
	public void toStringBufferAtom() {
		set1 = set1.add(two);
		set1 = set1.add(three);
		assertEquals("{2,3}", set1.toStringBuffer(true).toString());
	}

	@Test
	public void toStringBuffer() {
		set1 = set1.add(two);
		set1 = set1.add(three);
		assertEquals("2,3", set1.toStringBuffer(false).toString());
	}

	@Test
	public void toStringBufferLong() {
		set1 = set1.add(one);
		assertEquals("SortedSet{1}", set1.toStringBufferLong().toString());
	}
}

class FakeSet extends CommonSymbolicCollection<SymbolicExpression> implements
		SymbolicSet<SymbolicExpression> {

	FakeSet() {
		super(SymbolicCollectionKind.UNSORTED_SET);
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public Iterator<SymbolicExpression> iterator() {
		return null;
	}

	@Override
	public SortedSymbolicSet<SymbolicExpression> add(SymbolicExpression element) {
		return null;
	}

	@Override
	public SortedSymbolicSet<SymbolicExpression> remove(
			SymbolicExpression element) {
		return null;
	}

	@Override
	public SortedSymbolicSet<SymbolicExpression> addAll(
			SymbolicSet<? extends SymbolicExpression> set) {
		return null;
	}

	@Override
	public SortedSymbolicSet<SymbolicExpression> removeAll(
			SymbolicSet<? extends SymbolicExpression> set) {
		return null;
	}

	@Override
	public SortedSymbolicSet<SymbolicExpression> keepOnly(
			SymbolicSet<? extends SymbolicExpression> set) {
		return null;
	}

	@Override
	public boolean contains(SymbolicExpression element) {
		return false;
	}

	@Override
	protected void commitChildren() {
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		return null;
	}

	@Override
	public StringBuffer toStringBufferLong() {
		return null;
	}

	@Override
	protected boolean collectionEquals(SymbolicCollection<SymbolicExpression> o) {
		return false;
	}

	@Override
	protected int computeHashCode() {
		return 0;
	}

}
