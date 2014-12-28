package edu.udel.cis.vsl.sarl.prove.common;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class MultiProverFactory implements TheoremProverFactory {

	private PreUniverse universe;

	private TheoremProverFactory[] factories;

	public MultiProverFactory(PreUniverse universe,
			TheoremProverFactory[] factories) {
		this.universe = universe;
		this.factories = factories;
	}

	@Override
	public TheoremProver newProver(BooleanExpression context) {
		int numProvers = factories.length;
		TheoremProver[] provers = new TheoremProver[numProvers];

		for (int i = 0; i < numProvers; i++) {
			provers[i] = factories[i].newProver(context);
		}
		return new MultiProver(universe, provers);
	}

}
