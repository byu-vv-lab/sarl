/**
 * Implementation classes for the ideal module.
 * 
 * There are classes with names beginning with "NT" (e.g.,
 * {@link edu.udel.cis.vsl.sarl.ideal.common.NTConstant}). The "NT" stands for
 * "non-trivial". See the documentation for each class for the interpretation of
 * non-trivial, but in general the interpretation is that an element of that
 * class cannot be represented an an element of a "simpler" class. For example,
 * a non-trivial primitive power is a power of a primitive in which the exponent
 * is at least 2, as such an element cannot be represented as a primitive. (In
 * contrast, a primitive power with exponent 1 is equivalent to the primitive
 * power.) The idea is to have a canonical representation of each element.
 * 
 * Most of the arithemtic logic is contained in the class
 * {@link edu.udel.cis.vsl.sarl.ideal.common.CommonIdealFactory}. A number of
 * additional utility classes are defined simply to help the factory (e.g.,
 * {@link edu.udel.cis.vsl.sarl.ideal.common.MonomialAdder},
 * {@link edu.udel.cis.vsl.sarl.ideal.common.MonomialDivider}, etc.).
 */
package edu.udel.cis.vsl.sarl.ideal.common;