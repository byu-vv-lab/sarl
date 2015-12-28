/**
 * The number package supports infinite-precision integer and rational 
 * numbers. 
 * 
 * All numbers are subtypes of the interface
 * {@link edu.udel.cis.vsl.sarl.IF.number.Number} (not
 * to be confused with Java's Number class).  All instances
 * of Number are immutable.
 * 
 * An integer number must implement the
 * {@link edu.udel.cis.vsl.sarl.IF.number.IntegerNumber} interface.
 * A rational number must implement the
 * {@link edu.udel.cis.vsl.sarl.IF.number.RationalNumber} interface.
 * The integers and rationals are disjoint sets.
 * 
 * A {@link edu.udel.cis.vsl.sarl.IF.number.NumberFactory}
 * provides methods to create numbers, as well
 * as operations such as addition, subtraction, and so on.  
 */
package edu.udel.cis.vsl.sarl.IF.number;