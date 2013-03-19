/**
 * The prove package provides an abstract interface for an automated
 * theorem prover.  Such a prover must implement the {@link TheoremProver}
 * interface.  The main method provided by that interface is {@link TheoremProver#valid()},
 * which is used to determine whether a given boolean-valued symbolic expression
 * is valid.  Such a query can result in one of three possible outcomes:
 * yes, no, or "I don't know."
 */
package edu.udel.cis.vsl.sarl.IF.prove;