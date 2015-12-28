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
package edu.udel.cis.vsl.sarl.expr.common;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;

/**
 * The "null reference" is a useless reference value not equal to any of the
 * other reference values and which cannot be dereferenced. It does have
 * reference type, and can be used in places where an expression of reference
 * type is needed, but no valid reference exists.
 * 
 * @author siegel
 * 
 */
public class CommonNullReference extends CommonReferenceExpression {

	/**
	 * Constructs the null reference.
	 * 
	 * @param referenceType
	 *            the symbolic reference type
	 * @param zeroSequence
	 *            the singleton sequence whose sole element is the conrete
	 *            integer 0
	 */
	public CommonNullReference(SymbolicType referenceType,
			SymbolicSequence<NumericExpression> zeroSequence) {
		super(referenceType, zeroSequence);
	}

	/**
	 * Returns true, overriding the default false, since this is the null
	 * reference.
	 */
	@Override
	public boolean isNullReference() {
		return true;
	}

	/**
	 * Method that returns NULL ReferenceKind.
	 * 
	 * @return ReferenceKind
	 * 	NULL ReferenceKind
	 */
	@Override
	public ReferenceKind referenceKind() {
		return ReferenceKind.NULL;
	}

}
