package edu.udel.cis.vsl.sarl.IF.number;




/**
 * An instance of IntervalIF represents a numeric interval. Can be real or
 * integral.
 */
public interface Interval {

	boolean isReal();

	boolean isIntegral();

	Number lower();

	Number upper();

	boolean strictLower();

	boolean strictUpper();

}
