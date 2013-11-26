package edu.udel.cis.vsl.sarl.preuniverse.common;

import java.util.LinkedList;

import com.trifork.clj_ds.PersistentVector;
import com.trifork.clj_ds.IPersistentVector;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;



public class ClojureBenchmark {
	public static void main(String[] args){
		
		LinkedList<Integer> list1 = new LinkedList<>();
		list1.add(0);
		PersistentVector<Integer> pVec = PersistentVector.create(list1);
		
		
		final int SIZE = 1000000;
		//final int ITERATIONS = 10;
		final boolean PRINT = true;
	
		double totalTime = 0.0f;
		
		for(int i = 0; i < SIZE; ++i){
			long startTime = System.nanoTime(), stopTime;
			// TIMING STARTS HERE
		
			//pVec.add(i);
		
			// TIMING ENDS HERE
		
			stopTime = System.nanoTime();
			totalTime += ((double) (stopTime - startTime)) / 1000000000.0;
		}
		
		if(PRINT)
			System.out.println("ATime (s): " + totalTime + "  for size: " + SIZE);
		
		
	}
}
