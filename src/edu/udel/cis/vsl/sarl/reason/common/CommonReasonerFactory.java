package edu.udel.cis.vsl.sarl.reason.common;

import edu.udel.cis.vsl.sarl.IF.Reasoner;
import edu.udel.cis.vsl.sarl.IF.Simplifier;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;
import edu.udel.cis.vsl.sarl.reason.IF.ReasonerFactory;
import edu.udel.cis.vsl.sarl.simplify.IF.SimplifierFactory;

public class CommonReasonerFactory implements ReasonerFactory {

	private TheoremProverFactory proverFactory;

	private SimplifierFactory simplifierFactory;

	public CommonReasonerFactory(SimplifierFactory simplifierFactory,
			TheoremProverFactory proverFactory) {
		this.proverFactory = proverFactory;
		this.simplifierFactory = simplifierFactory;
	}

	@Override
	public Reasoner newReasoner(BooleanExpression context) {
		Simplifier simplifier = simplifierFactory.newSimplifier(context);

		return new CommonReasoner(simplifier, proverFactory);
	}

}
