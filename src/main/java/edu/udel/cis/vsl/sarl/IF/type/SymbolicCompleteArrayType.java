/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.IF.type;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;

/**
 * A complete array type specifies not only the element type but also the extent
 * (length) of the array. The extent is a symbolic expression of integer type.
 * 
 * @author siegel
 * 
 */
public interface SymbolicCompleteArrayType extends SymbolicArrayType {

	/**
	 * Returns the extent (length) of any array of this type. This is a non-null
	 * integer-valued symbolic expression.
	 */
	NumericExpression extent();

}
