package edu.udel.cis.vsl.sarl.IF.config;

import edu.udel.cis.vsl.sarl.IF.config.ProverInfo.ProverKind;

/**
 * A SARL configuration encapsulates information about the complete set of
 * external theorem provers available to SARL.
 * 
 * @author siegel
 *
 */
public interface SARLConfig {

	int getNumProvers();

	ProverInfo getProver(int index);

	Iterable<ProverInfo> getProvers();

	ProverInfo getProverWithAlias(String alias);

	ProverInfo getProverWithKind(ProverKind kind);

}
