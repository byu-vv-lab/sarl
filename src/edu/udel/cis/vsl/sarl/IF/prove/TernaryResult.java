package edu.udel.cis.vsl.sarl.IF.prove;

/**
 * Defines an enumerated Type "ResultType" with three possible values: YES, NO,
 * MAYBE. This is used to represent the result returned by an automated theorem
 * prover.
 * 
 * @author siegel
 * 
 */
public class TernaryResult {

	public enum ResultType {
		YES, NO, MAYBE
	};

}
