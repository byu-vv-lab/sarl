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
package edu.udel.cis.vsl.sarl.type.common;

import java.util.List;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;

/**
 * Implementation of {@link SymbolicCompleteArrayType} based on extending
 * {@link CommonSymbolicArrayType}.
 */
public class CommonSymbolicCompleteArrayType extends CommonSymbolicArrayType
		implements SymbolicCompleteArrayType {

	/**
	 * The length of the arrays in this type. Non-null.
	 */
	private NumericExpression extent;

	CommonSymbolicCompleteArrayType(SymbolicType elementType,
			NumericExpression extent) {
		super(elementType);
		assert extent != null;
		this.extent = extent;
	}

	@Override
	protected int computeHashCode() {
		return super.computeHashCode() ^ extent.hashCode();
	}

	@Override
	public String extentString() {
		return "[" + extent + "]";
	}

	@Override
	public NumericExpression extent() {
		return extent;
	}

	@Override
	public void canonizeChildren(ObjectFactory factory) {
		super.canonizeChildren(factory);
		if (!extent.isCanonic())
			extent = factory.canonic(extent);
	}

	@Override
	public boolean isComplete() {
		return true;
	}

	@Override
	protected List<SymbolicObject> getChildren() {
		List<SymbolicObject> result = super.getChildren();

		result.add(extent);
		return result;
	}

	@Override
	protected void nullifyFields() {
		super.nullifyFields();
		extent = null;
	}

	@Override
	public void uncommit() {
		if (extent != null) {
			extent.removeReferenceFrom(this);
			extent = null;
		}
		super.uncommit();
	}

}
