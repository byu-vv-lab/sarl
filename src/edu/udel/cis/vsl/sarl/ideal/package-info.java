/**
 * <p>
 * The ideal module supports reasoning about numerical expressions using "ideal"
 * mathematical reals and integers. In particular, addition and multiplication
 * are commutative and associative, there are no finite bounds on these sets,
 * there is no rounding, etc.
 * </p>
 * 
 * <p>
 * Expressions of real type are represented as rational functions, i.e.,
 * quotients of two multivariate polynomials. The "variables" in the polynomials
 * are referred to as "primitives". They may be symbolic constants, array-read
 * expressions, tuple-read expressions, or generally any expression of real type
 * not produced by a numeric operation.
 * </p>
 * 
 * <p>
 * Expressions of integer type are represented as polynomials in integer
 * primitives.
 * </p>
 * 
 * <p>
 * This module is big and complex.
 * </p>
 * 
 * <p>
 * The entry point is {@link edu.udel.cis.vsl.sarl.ideal.Ideal}. That class
 * provides static methods to get a new
 * {@link edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory}, an implementation of
 * {@link edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory}. It also
 * provides a method to get a simplifier factory.
 * </p>
 * 
 * <p>
 * The interface for this module is the sub-package
 * {@link edu.udel.cis.vsl.sarl.ideal.IF}. All code outside of this module
 * should use only elements provided in that sub-package (in addition to the
 * entry point).
 * </p>
 * 
 * <p>
 * The implementation classes for ideal symbolic expressions and their
 * arithmetic are in the sub-package {@link edu.udel.cis.vsl.sarl.ideal.common}.
 * </p>
 * 
 * <p>
 * The sub-package {edu.udel.cis.vsl.sarl.ideal.simplify} contains the classes
 * implementing a simplifier for ideal expressions.
 * </p>
 */
package edu.udel.cis.vsl.sarl.ideal;

