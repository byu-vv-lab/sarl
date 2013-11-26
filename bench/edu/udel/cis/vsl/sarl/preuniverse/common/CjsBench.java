package edu.udel.cis.vsl.sarl.preuniverse.common;


public class CjsBench {
    /**
     * @author Mohammad Alsulmi (malsulmi)
     */
    public static void main(String[] args) {
        //PersistentVector<String> vector;
        int maxSize = (int) Math.pow(2, 28);
        for (int i = 1; i <= maxSize; i = i * 2) {
        	//vector = Persistents.vector();
            int size = i;
            long stime = System.nanoTime();

            
            for (int j = 0; j < size; j++) {
                //String xx = Integer.toString(j);
            	//vector.plus(null);
            }
            long etime = System.nanoTime();

            double fTime = (etime - stime) / 1000000000.0;
            System.out.println(size+ "  "+fTime + " Sec");
        }
    }


}
