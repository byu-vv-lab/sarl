package edu.udel.cis.vsl.sarl.universe.IF;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.reason.IF.ReasonerFactory;

public interface ExtendedUniverse extends SymbolicUniverse {
	
	void incrementValidCount();
	
	void incrementProverValidCount();

	void setReasonerFactory(ReasonerFactory reasonerFactory);
}
