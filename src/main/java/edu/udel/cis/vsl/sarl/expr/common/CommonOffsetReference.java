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
import edu.udel.cis.vsl.sarl.IF.expr.OffsetReference;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;

/**
 * CommonOfsetReference extends the CommonNTReference superclass and implements
 * the OffsetReference interface. 
 * Implementation of a non-trivial Reference that is offset by a Numeric value.
 */
public class CommonOffsetReference extends CommonNTReference implements
		OffsetReference {

	/**
	 * Constructor that creates a CommonOffsetReference.
	 * 
	 * @param referenceType
	 * @param offsetReferenceFunction
	 * @param parentIndexSequence
	 */
	public CommonOffsetReference(SymbolicType referenceType,
			SymbolicConstant offsetReferenceFunction,
			SymbolicSequence<SymbolicExpression> parentIndexSequence) {
		super(referenceType, offsetReferenceFunction, parentIndexSequence);
	}

	/**
	 * Method that returns NumericExpression.
	 * 
	 * @return NumericExpression
	 */
	@Override
	public NumericExpression getOffset() {
		return getIndexExpression();
	}

	/**
	 * Method that always returns true.
	 * 
	 * @return boolean
	 */
	@Override
	public boolean isOffsetReference() {
		return true;
	}

	/**
	 * Method that returns the ReferenceKind
	 * 
	 * @return ReferenceKind
	 */
	@Override
	public ReferenceKind referenceKind() {
		return ReferenceKind.OFFSET;
	}
}
