package edu.udel.cis.vsl.sarl.preuniverse.common;
import org.pcollections.PVector;
import org.pcollections.TreePVector;

/**
 * @author Mohammad Alsulmi (malsulmi)
 * 
 * In this benchmark, we try to measure (evaluate) creating vectors by using one of the persistent java collection framework
 * which is PCollections framework
 * 
 * Here, we create vector of Object type and we append a null reference.

 */

public class PCollectionsBench {
    public static void main(String[] args) {
        PVector<String> vector;
        int maxSize = (int) Math.pow(2, 20);
        for (int i = 1; i <= maxSize; i = i * 2) {
            vector = TreePVector.empty();
            int size = i;
            long stime = System.nanoTime();

            
            for (int j = 0; j < size; j++) {
            	
                vector = vector.plus("hello");
            }
            System.out.println("Vector (vector) size: " + vector.size());
            long etime = System.nanoTime();

            double fTime = (etime - stime) / 1000000000.0;
            System.out.println(size+ "  "+fTime + " Sec");
        }
    }

}
