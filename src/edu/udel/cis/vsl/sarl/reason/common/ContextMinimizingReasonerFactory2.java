package edu.udel.cis.vsl.sarl.reason.common;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;
import edu.udel.cis.vsl.sarl.reason.IF.ReasonerFactory;
import edu.udel.cis.vsl.sarl.simplify.IF.SimplifierFactory;

/**
 * A context-minimizing reasoner factory wraps an underlying reasoner factory,
 * augmenting it with the ability to determine a reduced context for each
 * expression that is to be simplified or proved.
 * 
 * @author siegel
 *
 */
public class ContextMinimizingReasonerFactory2 implements ReasonerFactory {

	private TheoremProverFactory proverFactory;

	private SimplifierFactory simplifierFactory;

	private PreUniverse universe;

	private Map<BooleanExpression, ContextMinimizingReasoner2> reasonerMap = new HashMap<>();

	public ContextMinimizingReasonerFactory2(PreUniverse universe,
			TheoremProverFactory proverFactory,
			SimplifierFactory simplifierFactory) {
		this.universe = universe;
		this.proverFactory = proverFactory;
		this.simplifierFactory = simplifierFactory;
		this.universe = universe;
	}

	@Override
	public ContextMinimizingReasoner2 getReasoner(BooleanExpression context) {
		ContextMinimizingReasoner2 result = reasonerMap.get(context);

		if (result == null) {
			result = new ContextMinimizingReasoner2(this, context);
			reasonerMap.put(context, result);
		}
		return result;
	}

	PreUniverse getUniverse() {
		return universe;
	}

	SimplifierFactory getSimplifierFactory() {
		return simplifierFactory;
	}

	TheoremProverFactory getProverFactory() {
		return proverFactory;
	}

}
