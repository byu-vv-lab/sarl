package edu.udel.cis.vsl.sarl.reason.common;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.Reasoner;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.reason.IF.ReasonerFactory;

/**
 * A context-minimizing reasoner factory wraps an underlying reasoner factory,
 * augmenting it with the ability to determine a reduced context for each
 * expression that is to be simplified or proved.
 * 
 * @author siegel
 *
 */
public class ContextMinimizingReasonerFactory implements ReasonerFactory {

	private ReasonerFactory underlyingFactory;

	private PreUniverse universe;

	private Map<BooleanExpression, ContextMinimizingReasoner> reasonerMap = new HashMap<>();

	public ContextMinimizingReasonerFactory(PreUniverse universe,
			ReasonerFactory underlyingFactory) {
		this.universe = universe;
		this.underlyingFactory = underlyingFactory;
	}

	@Override
	public Reasoner getReasoner(BooleanExpression context) {
		if (context.isTrue())
			return underlyingFactory.getReasoner(context);

		ContextMinimizingReasoner result = reasonerMap.get(context);

		if (result == null) {
			result = new ContextMinimizingReasoner(this, context);
			reasonerMap.put(context, result);
		}
		return result;
	}

	ReasonerFactory getUnderlyingFactory() {
		return underlyingFactory;
	}

	PreUniverse getUniverse() {
		return universe;
	}

}
