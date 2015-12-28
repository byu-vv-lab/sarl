/**
 * Interface for the ideal module. All code outside of this module should use
 * only elements contained in this sub-pacakge, as well as the module entry
 * point {@link edu.udel.cis.vsl.sarl.ideal.Ideal} and the interfaces defined in
 * {@link edu.udel.cis.vsl.sarl.IF} and its sub-packages.
 * 
 * These interfaces define a simple hierarchy of numeric expressions:
 * <ul>
 * <li>{@link edu.udel.cis.vsl.sarl.ideal.IF.Constant}. As you would expect, a
 * constant is a concrete number, like "5" or "3.1415".</li>
 * <li>{@link edu.udel.cis.vsl.sarl.ideal.IF.Primitive}. A primitive is a
 * numeric symbolic constant or any other expression which will not be
 * decomposed and therefore plays the role of a single "variable" in a
 * polynomial</li>
 * <li>{@link edu.udel.cis.vsl.sarl.ideal.IF.PrimitivePower}. A power of a
 * primitive. Note that a primitive p is a primitive power, since it can be
 * expressed as p^1.</li>
 * <li>{@link edu.udel.cis.vsl.sarl.ideal.IF.Monic}. A monic is a product of
 * primitive powers. Note that a primitive power is a monic. The number "1" is
 * also a monic: it is the empty monic (empty product).</li>
 * <li>{@link edu.udel.cis.vsl.sarl.ideal.IF.Monomial}. A monomial is a product
 * of a constant and a monic. A monic is a monomial (with constant 1). A
 * constant is a monomial (with empty monic).</li>
 * <li>{@link edu.udel.cis.vsl.sarl.ideal.IF.Polynomial}. A polynomial is the
 * sum of monomials. A monomial is a polynomial.</li>
 * <li>{@link edu.udel.cis.vsl.sarl.ideal.IF.RationalExpression}. A rational
 * expression is the quotient of two polynomials. A polynomial is a rational
 * expressions (with denominator 1). A rational expression must have real type.
 * All expressions of integer type are polynomials.</li>
 * </ul>
 */
package edu.udel.cis.vsl.sarl.ideal.IF;