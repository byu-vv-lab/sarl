/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.reason.common;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.Reasoner;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;
import edu.udel.cis.vsl.sarl.reason.IF.ReasonerFactory;
import edu.udel.cis.vsl.sarl.simplify.IF.Simplifier;
import edu.udel.cis.vsl.sarl.simplify.IF.SimplifierFactory;

/**
 * Factory for producing instances of {@link CommonReasoner}.
 * 
 * @author Stephen F. Siegel
 */
public class CommonReasonerFactory implements ReasonerFactory {

	private Map<BooleanExpression, Reasoner> reasonerCache = new HashMap<>();

	private TheoremProverFactory proverFactory;

	private SimplifierFactory simplifierFactory;

	public CommonReasonerFactory(SimplifierFactory simplifierFactory,
			TheoremProverFactory proverFactory) {
		this.proverFactory = proverFactory;
		this.simplifierFactory = simplifierFactory;
	}

	@Override
	public Reasoner getReasoner(BooleanExpression context) {
		Reasoner result = reasonerCache.get(context);

		if (result == null) {
			Simplifier simplifier = simplifierFactory.newSimplifier(context);

			result = new CommonReasoner(simplifier, proverFactory);
			reasonerCache.put(context, result);
		}
		return result;
	}
}
