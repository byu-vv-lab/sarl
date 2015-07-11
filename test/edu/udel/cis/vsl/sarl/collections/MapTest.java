package edu.udel.cis.vsl.sarl.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.UnaryOperator;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.IF.SortedSymbolicMap;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.util.BinaryOperator;
import edu.udel.cis.vsl.sarl.util.Pair;

public class MapTest {

	// Static fields: created once for the entire suite:

	private static PreUniverse universe = (PreUniverse) SARL
			.newStandardUniverse();

	private static CollectionFactory cf = universe.collectionFactory();

	private static Comparator<SymbolicObject> comparator = universe
			.comparator();

	/**
	 * A comparator on arrays of length 2 of symbolic expressions. The array of
	 * length 2 represents an entry in a map (key, value). An array of such
	 * pairs represents a map. It is used to easily specify the expected value
	 * of a map after performing some test.
	 */
	private static Comparator<SymbolicExpression[]> pairComparator = new Comparator<SymbolicExpression[]>() {

		@Override
		public int compare(SymbolicExpression[] o1, SymbolicExpression[] o2) {
			int result = comparator.compare(o1[0], o2[0]);

			if (result != 0)
				return result;
			return comparator.compare(o1[1], o2[1]);
		}

	};

	/**
	 * Binary operator which maps (x,y) to null if x+y=0, else to x+y.
	 */
	private static BinaryOperator<SymbolicExpression> binOp = new BinaryOperator<SymbolicExpression>() {
		@Override
		public SymbolicExpression apply(SymbolicExpression x,
				SymbolicExpression y) {
			NumericExpression result = universe.add((NumericExpression) x,
					(NumericExpression) y);

			if (result.isZero())
				result = null;
			return result;
		}
	};

	/**
	 * Unary operator. Maps 1 to 0. Map 0 to null. Map all other x to x.
	 */
	private static UnaryOperator<SymbolicExpression> unOp = new UnaryOperator<SymbolicExpression>() {
		@Override
		public SymbolicExpression apply(SymbolicExpression x) {
			if (x.isOne())
				return universe.zeroInt();
			if (x.isZero())
				return null;
			return x;
		}
	};

	// Instances fields: created once before each test:

	private SymbolicExpression zero = universe.integer(0), one = universe
			.integer(1), two = universe.integer(2),
			three = universe.integer(3), four = universe.integer(4),
			five = universe.integer(5);

	SymbolicMap<SymbolicExpression, SymbolicExpression> map1 = cf
			.emptySortedMap(), map2 = cf.emptySortedMap();

	// SymbolicExpression[][] blah = { { one, two }, { three, four } };

	// Static methods:

	private static void checkMap(SymbolicExpression[][] expected,
			SymbolicMap<SymbolicExpression, SymbolicExpression> actual) {
		Collection<Pair<SymbolicExpression, SymbolicExpression>> expectedPairs, actualPairs;

		if (actual instanceof SortedSymbolicMap<?, ?>) {
			Arrays.sort(expected, pairComparator);
			expectedPairs = new LinkedList<>();
			actualPairs = new LinkedList<>();
		} else {
			expectedPairs = new HashSet<>();
			actualPairs = new HashSet<>();
		}
		for (Entry<SymbolicExpression, SymbolicExpression> entry : actual
				.entries()) {
			actualPairs.add(new Pair<SymbolicExpression, SymbolicExpression>(
					entry.getKey(), entry.getValue()));
		}
		for (SymbolicExpression[] keyValue : expected) {
			expectedPairs.add(new Pair<SymbolicExpression, SymbolicExpression>(
					keyValue[0], keyValue[1]));
		}
		assertEquals(expectedPairs, actualPairs);
	}

	@Test
	public void empty() {
		assertTrue(map1.isEmpty());
		map1 = map1.put(zero, one);
		assertFalse(map1.isEmpty());
	}

	@Test
	public void put1() {
		map1 = map1.put(zero, one);
		checkMap(new SymbolicExpression[][] { { zero, one } }, map1);
	}

	@Test
	public void putOver() {
		map1 = map1.put(zero, one);
		map1 = map1.put(one, two);
		checkMap(new SymbolicExpression[][] { { zero, one }, { one, two } },
				map1);
		map1 = map1.put(zero, three);
		checkMap(new SymbolicExpression[][] { { zero, three }, { one, two } },
				map1);
		map1 = map1.put(zero, three);
		checkMap(new SymbolicExpression[][] { { zero, three }, { one, two } },
				map1);
	}

	@Test
	public void putOverImmut() {
		map1 = map1.put(zero, one);
		map1.commit();
		map1 = map1.put(one, two);
		checkMap(new SymbolicExpression[][] { { zero, one }, { one, two } },
				map1);
		map1.commit();
		map1 = map1.put(zero, three);
		checkMap(new SymbolicExpression[][] { { zero, three }, { one, two } },
				map1);
		map1.commit();
		map1 = map1.put(zero, three);
		checkMap(new SymbolicExpression[][] { { zero, three }, { one, two } },
				map1);
	}

	@Test
	public void get() {
		map1 = map1.put(two, three);
		map1 = map1.put(three, four);
		map1 = map1.put(four, five);
		assertEquals(three, map1.get(two));
		assertEquals(four, map1.get(three));
		assertEquals(five, map1.get(four));
		assertNull(map1.get(zero));
		assertNull(map1.get(one));
		assertNull(map1.get(five));
	}

	@Test
	public void remove() {
		map1 = map1.put(two, three);
		map1 = map1.put(three, four);
		map1 = map1.put(four, five);
		map1 = map1.remove(three);
		checkMap(new SymbolicExpression[][] { { two, three }, { four, five } },
				map1);
		map1 = map1.remove(four);
		checkMap(new SymbolicExpression[][] { { two, three } }, map1);
		map1 = map1.remove(four);
		checkMap(new SymbolicExpression[][] { { two, three } }, map1);
		map1 = map1.remove(zero);
		checkMap(new SymbolicExpression[][] { { two, three } }, map1);
		map1 = map1.remove(two);
		checkMap(new SymbolicExpression[][] {}, map1);
	}

	@Test
	public void removeImmut() {
		map1 = map1.put(two, three);
		map1 = map1.put(three, four);
		map1 = map1.put(four, five);
		map1.commit();
		map2 = map1.remove(three);
		checkMap(new SymbolicExpression[][] { { two, three }, { three, four },
				{ four, five } }, map1);
		checkMap(new SymbolicExpression[][] { { two, three }, { four, five } },
				map2);
		map2.commit();
		map1 = map2.remove(four);
		checkMap(new SymbolicExpression[][] { { two, three }, { four, five } },
				map2);
		checkMap(new SymbolicExpression[][] { { two, three } }, map1);
		map1.commit();
		map2 = map1.remove(four);
		checkMap(new SymbolicExpression[][] { { two, three } }, map2);
		checkMap(new SymbolicExpression[][] { { two, three } }, map1);
		map2.commit();
		map1 = map2.remove(zero);
		checkMap(new SymbolicExpression[][] { { two, three } }, map1);
		checkMap(new SymbolicExpression[][] { { two, three } }, map2);
		map1.commit();
		map2 = map1.remove(two);
		checkMap(new SymbolicExpression[][] { { two, three } }, map1);
		checkMap(new SymbolicExpression[][] {}, map2);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void canonic() {
		// make some other 3 canonic so that our "three" is not:
		universe.canonic(universe.integer(3));
		map1 = map1.put(two, three);
		map1 = map1.put(three, four);
		map1 = map1.put(four, five);
		map2 = map2.put(two, three);
		map2 = map2.put(three, four);
		map2 = map2.put(four, five);
		assertEquals(map2, map1);
		map1 = (SymbolicMap<SymbolicExpression, SymbolicExpression>) universe
				.canonic(map1);
		assertEquals(map2, map1);
		assertTrue(map1.isCanonic());
		for (Entry<SymbolicExpression, SymbolicExpression> entry : map1
				.entries()) {
			assertTrue(entry.getKey().isCanonic());
			assertTrue(entry.getValue().isCanonic());
		}
	}

	@Test
	public void keySet() {
		map1 = map1.put(two, three);
		map1 = map1.put(three, four);

		Iterator<SymbolicExpression> iter = map1.keys().iterator();

		assertTrue(iter.hasNext());
		assertEquals(two, iter.next());
		assertTrue(iter.hasNext());
		assertEquals(three, iter.next());
		assertFalse(iter.hasNext());
	}

	@Test(expected = SARLException.class)
	public void keySetRemoveBad() {
		map1 = map1.put(two, three);

		Iterator<SymbolicExpression> iter = map1.keys().iterator();

		iter.next();
		iter.remove();
	}

	@Test(expected = SARLException.class)
	public void valueSetRemoveBad() {
		map1 = map1.put(two, three);

		Iterator<SymbolicExpression> iter = map1.values().iterator();

		iter.next();
		iter.remove();
	}

	@Test(expected = SARLException.class)
	public void entriesRemoveBad() {
		map1 = map1.put(two, three);

		Iterator<Entry<SymbolicExpression, SymbolicExpression>> iter = map1
				.entries().iterator();

		iter.next();
		iter.remove();
	}

	@Test
	public void javaMap() {
		Map<SymbolicExpression, SymbolicExpression> javaMap = new HashMap<>();

		javaMap.put(one, two);
		javaMap.put(two, three);
		javaMap.put(three, four);
		map1 = cf.sortedMap(javaMap);
		checkMap(new SymbolicExpression[][] { { one, two }, { two, three },
				{ three, four } }, map1);
	}

	@Test
	public void comparator() {
		SortedSymbolicMap<SymbolicExpression, SymbolicExpression> map = cf
				.emptySortedMap();
		Comparator<? super SymbolicExpression> c = map.comparator();

		assertEquals(comparator.compare(zero, one), c.compare(zero, one));
		assertEquals(comparator.compare(one, two), c.compare(one, two));
		assertEquals(comparator.compare(five, three), c.compare(five, three));
	}

	@Test
	public void combine() {
		// map1 = {{1,2}, {2,3}, {3,4}}
		// map2 = {{1,2}, {2,-3}, {5,3}
		// expected = {1,4}, {3,4}, {5,3}
		map1 = map1.put(one, two);
		map1 = map1.put(two, three);
		map1 = map1.put(three, four);
		map2 = map2.put(one, two);
		map2 = map2.put(two, universe.integer(-3));
		map2 = map2.put(five, three);
		map1.combine(binOp, map2);
		checkMap(new SymbolicExpression[][] { { one, four }, { three, four },
				{ five, three } }, map1);
		checkMap(
				new SymbolicExpression[][] { { one, two },
						{ two, universe.integer(-3) }, { five, three } }, map2);
	}

	@Test
	public void combineImmut() {
		// map1 = {{1,2}}
		// map2 = {{1,2}, {2,3}}
		// expected = {1,4}, {2,3}
		map1 = map1.put(one, two);
		map2 = map2.put(one, two);
		map2 = map2.put(two, three);
		map1.commit();
		map2.commit();
		map1 = map1.combine(binOp, map2);
		checkMap(new SymbolicExpression[][] { { one, four }, { two, three } },
				map1);
	}

	@Test
	public void combine2() {
		// map1 = {{1,2}}
		// map2 = {{1,2}, {2,3}}
		// expected = {1,4}, {2,3}
		map1 = map1.put(one, two);
		map2 = map2.put(one, two);
		map2 = map2.put(two, three);
		map1.commit();
		map1 = map1.combine(binOp, map2);
		checkMap(new SymbolicExpression[][] { { one, four }, { two, three } },
				map1);
	}

	@Test
	public void combine3() {
		map1 = map1.combine(binOp, map2);
		checkMap(new SymbolicExpression[][] {}, map1);
	}

	@Test
	public void apply() {
		map1 = map1.put(one, one);
		map1 = map1.put(two, zero);
		map1 = map1.put(three, four);
		map1 = map1.apply(unOp);
		checkMap(new SymbolicExpression[][] { { one, zero }, { three, four } },
				map1);
	}
	
	@Test
	public void applyImmutable() {
		map1 = map1.put(one, one);
		map1 = map1.put(two, zero);
		map1 = map1.put(three, four);
		map1.commit();
		map1 = map1.apply(unOp);
		checkMap(new SymbolicExpression[][] { { one, zero }, { three, four } },
				map1);
	}

	@Test
	public void nequals() {
		map1 = map1.put(one, two);
		map2 = map2.put(one, three);
		assertFalse(map1.equals(map2));
	}

	@Test
	public void string() {
		map1 = map1.put(two, three);
		map1 = map1.put(three, four);
		assertEquals("{2->3, 3->4}", map1.toString());
	}
	
	@Test
	public void stringBuf() {
		map1 = map1.put(two, three);
		map1 = map1.put(three, four);
		assertEquals("Map{2->3, 3->4}", map1.toStringBufferLong().toString());
	}
}
