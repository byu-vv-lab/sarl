/**
 * An implementation of the <code>number</code> module.
 * External users of this module should not import elements from here.
 * Instead, import elements of the <code>IF</code> package.<p>
 * 
 * This implementation uses a representation of integers that uses Java's
 * <code>BigInteger</code> class.  Rational numbers are represented as an ordered
 * pair (numerator,denominator) of big integers, with GCD 1.
 * 
 * @author siegel
 */
package edu.udel.cis.vsl.sarl.number.real;