package edu.udel.cis.vsl.sarl.collections;

import java.util.Iterator;

import org.junit.Test;

import com.github.krukow.clj_ds.PersistentVector;
import com.github.krukow.clj_ds.Persistents;

public class PersistentVectorTest {

	private PersistentVector<Integer> makeVec(int n) {
		PersistentVector<Integer> pv = Persistents.vector();

		for (int i = 0; i < n; i++)
			pv = pv.plus(i);
		return pv;
	}

	private void check(int n) {
		PersistentVector<Integer> pv = makeVec(n);
		Iterator<Integer> iter = pv.iterator();

		for (int i = 0; i < n; i++)
			System.out.println(iter.next());
	}

	@Test
	public void test() {
		check(33);
	}
}
