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

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

/**
 * Comparator of symbolic expressions.
 * 
 * Creation order: first create ObjectComparator oc. Then create
 * NumericComparator nc using oc. Then create ExpressionComparator ec using oc
 * and nc.
 * 
 * @author siegel
 * 
 */
public class ExpressionComparator implements Comparator<SymbolicExpression> {

	private Comparator<SymbolicObject> objectComparator;

	private Comparator<SymbolicType> typeComparator;

	private Comparator<NumericExpression> numericComparator;

	/**
	 * Constructor that takes a three Comparators of types NumericExpression,SymbolicObject,
	 * and SymbolicType.
	 * 
	 * @param numericComparator 
	 * @param objectComparator
	 * @param typeComparator
	 */
	public ExpressionComparator(
			Comparator<NumericExpression> numericComparator,
			Comparator<SymbolicObject> objectComparator,
			Comparator<SymbolicType> typeComparator) {
		this.numericComparator = numericComparator;
		this.objectComparator = objectComparator;
		this.typeComparator = typeComparator;
	}
	
	/**
	 * Returns a comparator of the type SymbolicObject
	 * @return Comparator<SymbolicObject>
	 */
	public Comparator<SymbolicObject> objectComparator() {
		return objectComparator;
	}
	
	/**
	 * Returns a comparator of the type SymbolicType
	 * @return Comparator<SymbolicType>
	 */
	public Comparator<SymbolicType> typeComparator() {
		return typeComparator;
	}

	/**
	 * Returns a comparator of the type numericExpression
	 * @return Comparator<NumericExpression>
	 */
	public Comparator<NumericExpression> numericComparator() {
		return numericComparator;
	}

	/**
	 * Numerics first, then everything else. For everything else: first compare
	 * types, then operator, then number of arguments, the compare arguments.
	 * 
	 * TODO: don't you have to compare classes, to be consistent with equals?
	 */
	@Override
	public int compare(SymbolicExpression o1, SymbolicExpression o2) {
		SymbolicType t1 = o1.type();
		SymbolicType t2 = o2.type();

		if (t1.isNumeric()) {
			if (t2.isNumeric())
				return numericComparator.compare((NumericExpression) o1,
						(NumericExpression) o2);
			else
				return -1;
		} else if (t2.isNumeric())
			return 1;
		else { // neither is numeric
			int result = typeComparator.compare(t1, t2);

			if (result != 0)
				return result;
			result = o1.operator().compareTo(o2.operator());
			if (result != 0)
				return result;
			else {
				int numArgs = o1.numArguments();

				result = numArgs - o2.numArguments();
				if (result != 0)
					return result;
				for (int i = 0; i < numArgs; i++) {
					result = objectComparator.compare(o1.argument(i),
							o2.argument(i));
					if (result != 0)
						return result;
				}
				{
					Class<? extends SymbolicExpression> class1 = o1.getClass();
					Class<? extends SymbolicExpression> class2 = o2.getClass();

					if (class1.equals(class2))
						return 0;
					return class1.getCanonicalName().compareTo(
							class2.getCanonicalName());

				}
			}
		}
	}
}
