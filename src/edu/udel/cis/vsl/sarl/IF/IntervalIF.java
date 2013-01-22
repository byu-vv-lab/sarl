package edu.udel.cis.vsl.sarl.IF;



/**
 * An instance of IntervalIF represents a numeric interval. Can be real or
 * integral.
 */
public interface IntervalIF {

	boolean isReal();

	boolean isIntegral();

	NumberIF lower();

	NumberIF upper();

	boolean strictLower();

	boolean strictUpper();

}
