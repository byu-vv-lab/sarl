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
package edu.udel.cis.vsl.sarl.type.common;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;

/**
 * compares two {@link SymbolicTypeSequence}
 * 
 * @author mohammedalali
 *
 */
public class TypeSequenceComparator implements
		Comparator<SymbolicTypeSequence> {
	/**
	 * holds the way to compare two SymbolicTypeSequences
	 */
	private Comparator<SymbolicType> typeComparator;

	public TypeSequenceComparator() {

	}
	
	/**
	 * must be used to set the typeComparator before comparing by compare()
	 * 
	 * @param c
	 */
	public void setTypeComparator(Comparator<SymbolicType> c) {
		typeComparator = c;
	}

	@Override
	public int compare(SymbolicTypeSequence o1, SymbolicTypeSequence o2) {
		int size = o1.numTypes();
		int result = size - o2.numTypes();

		if (result != 0)
			return result;
		for (int i = 0; i < size; i++) {
			result = typeComparator.compare(o1.getType(i), o2.getType(i));
			if (result != 0)
				return result;
		}
		return 0;
	}

}
