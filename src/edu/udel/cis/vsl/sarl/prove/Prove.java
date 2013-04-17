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
package edu.udel.cis.vsl.sarl.prove;

import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.ModelResult;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;
import edu.udel.cis.vsl.sarl.prove.common.CommonModelResult;
import edu.udel.cis.vsl.sarl.prove.common.CommonValidityResult;
import edu.udel.cis.vsl.sarl.prove.cvc.CVC3TheoremProverFactory;

public class Prove {

	public final static ValidityResult RESULT_YES = new CommonValidityResult(
			ResultType.YES);

	public final static ValidityResult RESULT_NO = new CommonValidityResult(
			ResultType.NO);

	public final static ValidityResult RESULT_MAYBE = new CommonValidityResult(
			ResultType.MAYBE);

	public static TheoremProverFactory newCVC3TheoremProverFactory(
			PreUniverse universe) {
		return new CVC3TheoremProverFactory(universe);
	}

	public static ValidityResult validityResult(ResultType type) {
		switch (type) {
		case YES:
			return RESULT_YES;
		case NO:
			return RESULT_NO;
		case MAYBE:
			return RESULT_MAYBE;
		default:
			throw new SARLInternalException("unreachable");
		}
	}

	public static ModelResult modelResult(
			Map<SymbolicConstant, SymbolicExpression> model) {
		return new CommonModelResult(model);
	}

}
