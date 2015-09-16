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
package edu.udel.cis.vsl.sarl.object;

import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class Objects {

	public static ObjectFactory newObjectFactory(NumberFactory numberFactory) {
		return new CommonObjectFactory(numberFactory);
	}

	/**
	 * Modifies reference counts of two objects when they are used in a context
	 * where <code>obj2</code> is replacing <code>obj1</code> Example:
	 * 
	 * <pre>
	 * x = Objects.replace(x, and(x, y));
	 * </pre>
	 * 
	 * This is the same as <code>x=and(x,y)</code> but updates the reference
	 * counts of the original and final values of <code>x</code> appropriately.
	 * 
	 * @param obj1
	 *            the original value of some variable
	 * @param obj2
	 *            the new value that will replace the original
	 * @return <code>obj2</code>
	 */
	public static <T extends SymbolicObject> T replace(T obj1, T obj2) {
		if (obj1 != obj2) {
			if (obj1 != null && !obj1.isCanonic())
				obj1.decrementReferenceCount();
			if (obj2 != null && !obj2.isCanonic())
				obj2.incrementReferenceCount();
		}
		return obj2;
	}
}
