package edu.udel.cis.vsl.sarl.IF.config;

import edu.udel.cis.vsl.sarl.IF.config.Prover.ProverKind;

/**
 * A SARL configuration encapsulates information about the complete set of
 * external theorem provers available to SARL.
 * 
 * @author siegel
 *
 */
public interface SARLConfig {

	int getNumProvers();

	Prover getProver(int index);

	Iterable<Prover> getProvers();

	Prover getProverWithAlias(String alias);

	Prover getProverWithKind(ProverKind kind);

}
