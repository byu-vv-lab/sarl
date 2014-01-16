package edu.udel.cis.vsl.sarl.collections;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.krukow.clj_ds.PersistentVector;
import com.github.krukow.clj_ds.Persistents;

public class PersistentVectorTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private PersistentVector<Integer> makeVec(int n) {
		PersistentVector<Integer> pv = Persistents.vector();

		for (int i = 0; i < n; i++) {
			pv = pv.plus(i);
		}
		return pv;
	}

	private void check(int n) {
		PersistentVector<Integer> pv1 = makeVec(n), pv2 = makeVec(n);
		Iterator<Integer> iter1 = pv1.iterator(), iter2 = pv2.iterator();

		assertEquals(pv1.size(), pv2.size());
		while (iter1.hasNext()) {
			Integer a = iter1.next();
			Integer b = iter2.next();

			assertEquals(a, b);
		}
	}

	@Test
	public void test() {
		for (int i = 0; i < 35; i++) {
			check(i);
		}
	}
}
