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
package edu.udel.cis.vsl.sarl.IF;

import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

/**
 * A result to a validity query which also requested a model in case the answer
 * was "NO", and for which the answer was "NO". The model may nevertheless be
 * null, indicating the model could not be constructed for some reason (even
 * though the query is known to be invalid).
 * 
 * @author Stephen F. Siegel
 * 
 */
public interface ModelResult extends ValidityResult {

	/**
	 * Returns the model, a map assigning a concrete symbolic expression value
	 * to each symbolic constant occurring in the query. May be null.
	 * 
	 * @return the model or null
	 */
	public Map<SymbolicConstant, SymbolicExpression> getModel();
}
