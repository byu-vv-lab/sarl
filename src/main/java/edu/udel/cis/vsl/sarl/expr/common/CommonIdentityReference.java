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
 * The identity reference I, which is characterized by the property that
 * dereference(value, I)==value for any symbolic expression value.
 * 
 * @author siegel
 * 
 */
public class CommonIdentityReference extends CommonReferenceExpression {

	/**
	 * Constructs the identity reference.
	 * 
	 * @param referenceType
	 *            the symbolic reference type
	 * @param oneSequence
	 *            the singleton sequence whose sole element is the concrete
	 *            integer 1
	 */
	CommonIdentityReference(SymbolicType referenceType,
			SymbolicSequence<NumericExpression> oneSequence) {
		super(referenceType, oneSequence);
	}

	/**
	 * Returns true, as this is the identity reference, overriding the default
	 * false.
	 */
	@Override
	public boolean isIdentityReference() {
		return true;
	}

	/**
	 * Method that returns ReferenceKind.
	 */
	@Override
	public ReferenceKind referenceKind() {
		return ReferenceKind.IDENTITY;
	}

}
