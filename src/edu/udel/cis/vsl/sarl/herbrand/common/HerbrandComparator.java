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
package edu.udel.cis.vsl.sarl.herbrand.common;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

public class HerbrandComparator implements Comparator<NumericExpression> {

	private Comparator<SymbolicObject> objectComparator;

	private Comparator<SymbolicType> typeComparator;

	HerbrandComparator(Comparator<SymbolicObject> objectComparator,
			Comparator<SymbolicType> typeComparator) {
		this.objectComparator = objectComparator;
		this.typeComparator = typeComparator;
	}

	public int compare(NumericExpression o1, NumericExpression o2) {
		SymbolicType t1 = o1.type();
		SymbolicType t2 = o2.type();

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
			return 0;
		}
	}

}
