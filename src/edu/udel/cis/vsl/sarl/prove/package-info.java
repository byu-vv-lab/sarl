/**
 * This package constitutes the interface between SARL and (possibly external)
 * theorem provers. It provides an abstract interface
 * {@link edu.udel.cis.vsl.sarl.prove.IF.TheoremProver} representing an
 * automated theorem prover as well as implementations for specific provers
 * (such as CVC3).
 * 
 * The entry-point for using this module is class
 * {@link edu.udel.cis.vsl.sarl.prove.Prove}. That class provides static methods
 * for producing instances of
 * {@link edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory}. Those factories
 * are used to produce instances of
 * {@link edu.udel.cis.vsl.sarl.prove.IF.TheoremProver}.
 * 
 * @author Stephen F. Siegel
 */
package edu.udel.cis.vsl.sarl.prove;