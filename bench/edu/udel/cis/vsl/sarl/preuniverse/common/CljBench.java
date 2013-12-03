package edu.udel.cis.vsl.sarl.preuniverse.common;


/**
 * @author Mohammad Alsulmi (malsulmi)
 * 
 * In this benchmark, we try to measure (evaluate) creating vectors by using one of the persistent java collection framework
 * which is CJS framework
 * 
 *  some of the code has been commented since the library is not added to the SARL project.
 */

public class CljBench {
       public static void main(String[] args) {
//        PersistentVector<String> vector;
        int maxSize = (int) Math.pow(2, 21);
        for (int i = 1; i <= maxSize; i = i * 2) {
  //      	vector = Persistents.vector();
            int size = i;
            long stime = System.nanoTime();

            
            for (int j = 0; j < size; j++) {
    //        	 vector = vector.plus("hello");
            }
      //      System.out.println("Vector (vector) size: " + vector.size());
            long etime = System.nanoTime();

            double fTime = (etime - stime) / 1000000000.0;
            System.out.println(size+ "  "+fTime + " Sec");
        }
    }


}
