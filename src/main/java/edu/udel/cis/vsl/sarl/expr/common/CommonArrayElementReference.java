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

import edu.udel.cis.vsl.sarl.IF.expr.ArrayElementReference;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;

/** 
 * CommonArrayElement Reference extends the CommonNTReference superclass and
 * implements the ArrayElementReference interface. It is used to get the index
 * of the CommonArrayElementReference and the kind of reference.
 * 
 * @author siegel
 */
public class CommonArrayElementReference extends CommonNTReference implements
		ArrayElementReference {
	
	/** 
	 * Constructor that builds a CommonArrayElementReference.
	 * 
	 * @param referenceType 
	 * @param arrayElementReferenceFunction
	 * @param parentIndexSequence
	 */
	public CommonArrayElementReference(SymbolicType referenceType,
			SymbolicConstant arrayElementReferenceFunction,
			SymbolicSequence<SymbolicExpression> parentIndexSequence) {
		super(referenceType, arrayElementReferenceFunction, parentIndexSequence);
	}

	/** Method that returns NumericExpression.
	 * 
	 * @return NumericExpression
	 */
	@Override
	public NumericExpression getIndex() {
		return getIndexExpression();
	}

	/**
	 * Method that always returns true.
	 * 
	 * @return boolean
	 */
	@Override
	public boolean isArrayElementReference() {
		return true;
	}

	/**
	 * Getter method that returns the ReferenceKind.
	 * 
	 * @return ReferenceKind
	 */
	@Override
	public ReferenceKind referenceKind() {
		return ReferenceKind.ARRAY_ELEMENT;
	}
}
