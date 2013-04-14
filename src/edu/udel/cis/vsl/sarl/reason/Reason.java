package edu.udel.cis.vsl.sarl.reason;

import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;
import edu.udel.cis.vsl.sarl.reason.IF.ReasonerFactory;
import edu.udel.cis.vsl.sarl.reason.common.CommonReasonerFactory;
import edu.udel.cis.vsl.sarl.simplify.IF.SimplifierFactory;

public class Reason {

	public static ReasonerFactory newReasonerFactory(
			SimplifierFactory simplifierFactory,
			TheoremProverFactory proverFactory) {
		return new CommonReasonerFactory(simplifierFactory, proverFactory);
	}
}
