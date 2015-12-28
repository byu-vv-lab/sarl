package edu.udel.cis.vsl.sarl.prove.common;

import java.util.ArrayList;

import edu.udel.cis.vsl.sarl.IF.TheoremProverException;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

/**
 * A factory for producing instances of {@link MultiProver}.
 * 
 * @author Stephen F. Siegel
 *
 */
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
		ArrayList<TheoremProver> provers = new ArrayList<>();

		for (int i = 0; i < numProvers; i++) {
			try {
				TheoremProver prover = factories[i].newProver(context);

				provers.add(prover);
			} catch (TheoremProverException e) {
				// thrown if the context contained something that class
				// of theorem prover just can't handle
				// ignore this prover.
			}
		}
		return new MultiProver(universe, provers.toArray(new TheoremProver[0]));
	}

}
