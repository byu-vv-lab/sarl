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
package edu.udel.cis.vsl.sarl.universe.common;

import edu.udel.cis.vsl.sarl.IF.Reasoner;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.common.CommonPreUniverse;
import edu.udel.cis.vsl.sarl.reason.IF.ReasonerFactory;

/**
 * A standard implementation of SymbolicUniverse, relying heavily on a given
 * NumericExpressionFactory for dealing with numeric issues and a
 * BooleanExpressionFactory for dealing with boolean expressions.
 * 
 * @author siegel
 */
public class CommonSymbolicUniverse extends CommonPreUniverse implements
		SymbolicUniverse {

	/**
	 * The factory for producing new Reasoner instances.
	 */
	private ReasonerFactory reasonerFactory;

	// Constructor...

	/**
	 * Constructs a new CommonSymbolicUniverse from the given system of
	 * factories.
	 * 
	 * @param system
	 *            a factory system
	 */
	public CommonSymbolicUniverse(FactorySystem system) {
		super(system);
	}

	// Helper methods...

	@Override
	public Reasoner reasoner(BooleanExpression context) {
		context = (BooleanExpression) canonic(context);
		return reasonerFactory.getReasoner(context);
	}

	public void setReasonerFactory(ReasonerFactory reasonerFactory) {
		this.reasonerFactory = reasonerFactory;
	}

	@Override
	public Number extractNumber(BooleanExpression assumption,
			NumericExpression expression) {
		Number result = extractNumber(expression);

		if (result != null)
			return result;
		return reasoner(assumption).extractNumber(expression);
	}

}
