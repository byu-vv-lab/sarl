package edu.udel.cis.vsl.sarl;


import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

public class ObjectBenchmark {
	static FactorySystem system = PreUniverses.newIdealFactorySystem();
	private static PreUniverse universe = PreUniverses.newPreUniverse(system);
	
	
	public static void main(String[] args) {
		long startTime = System.nanoTime(), stopTime;
		double totalTime; 
		// Benchmark for numObjects()
		System.out.println("Benchmark for 'numObjects()");
		universe.numObjects();
		stopTime = System.nanoTime();
		totalTime = ((double) (stopTime - startTime)) / 1000000000.0;
		System.out.println("Time (s): " + totalTime);
		
		System.out.println("\nBenchmark for 'objects()");
		universe.objects();
		stopTime = System.nanoTime();
		totalTime = ((double) (stopTime - startTime)) / 1000000000.0;
		System.out.println("Time (s): " + totalTime);
		

	}

}
