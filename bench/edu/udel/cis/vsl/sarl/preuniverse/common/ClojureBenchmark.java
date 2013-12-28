package edu.udel.cis.vsl.sarl.preuniverse.common;

import com.github.krukow.clj_lang.IPersistentCollection;
import com.github.krukow.clj_lang.PersistentVector;

public class ClojureBenchmark {
	public static void main(String[] args) {

		IPersistentCollection<Integer> pList = PersistentVector.emptyVector();

		final int SIZE = 100000;
		// final int ITERATIONS = 10;
		final boolean PRINT = true;

		double totalTime = 0.0f;
		long startTime = System.nanoTime(), stopTime;

		for (int i = 0; i < SIZE; ++i) {
			pList = pList.cons(i);
		}

		stopTime = System.nanoTime();
		totalTime += ((double) (stopTime - startTime)) / 1000000000.0;

		if (PRINT)
			System.out.println(pList.count());

		if (PRINT)
			System.out
					.println("Time (s): " + totalTime + "  for size: " + SIZE);

	}
}
