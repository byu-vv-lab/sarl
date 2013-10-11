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

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;

/**
 * @author alali
 *	
 *	This class is to compare different types in the Type module.
 *	It compare those types: Boolean, Integer, Real, Array, Function, Tuple, and Union.
 */
public class TypeComparator implements Comparator<SymbolicType> {

	private Comparator<SymbolicTypeSequence> typeSequenceComparator;

	private Comparator<SymbolicExpression> expressionComparator;

	public TypeComparator() {

	}

	
	/**
	 * @param c Comparator of type SymbolicTypeSequence
	 */
	public void setTypeSequenceComparator(Comparator<SymbolicTypeSequence> c) {
		typeSequenceComparator = c;
	}

	
	/**
	 * @param c , a comparator of type symbolic expressions
	 * sets expressionComparator to be used in comparison
	 */
	public void setExpressionComparator(Comparator<SymbolicExpression> c) {
		expressionComparator = c;
		
	}

	
	/**
	 * @return expressionComparator that has the way to compare two expressions 
	 * and also used when comparing two complete array types
	 */
	public Comparator<SymbolicExpression> expressionComparator() {
		return expressionComparator;
	}

	
	/* (non-Javadoc)
	 * comparing two types according to their types.
	 * if the two types aren't equal
	 * @return 0
	 * 
	 * otherwise, it checks if the two similar types are of the same kinds
	 * @returns 0 if similar kind. Otherwise, returns -1, 1.
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	 @Override
	public int compare(SymbolicType o1, SymbolicType o2) {
		SymbolicTypeKind kind = o1.typeKind();
		int result = kind.compareTo(o2.typeKind());

		if (result != 0)
			return result;
		switch (kind) {
		case BOOLEAN:
			return 0;
		case INTEGER:
			return ((SymbolicIntegerType) o1).integerKind().compareTo(
					((SymbolicIntegerType) o2).integerKind());
		case REAL:
			return ((SymbolicRealType) o1).realKind().compareTo(
					((SymbolicRealType) o2).realKind());
		case ARRAY: {
			CommonSymbolicArrayType t1 = (CommonSymbolicArrayType) o1;
			CommonSymbolicArrayType t2 = (CommonSymbolicArrayType) o2;

			result = compare(t1.elementType(), t2.elementType());
			if (result != 0)
				return result;
			else {
				if (t1.isComplete())
					return t2.isComplete() ? expressionComparator.compare(
							((CommonSymbolicCompleteArrayType) t1).extent(),
							((CommonSymbolicCompleteArrayType) t2).extent())
							: -1;
				else
					return t2.isComplete() ? 1 : 0;
			}
		}
		case FUNCTION: {
			CommonSymbolicFunctionType t1 = (CommonSymbolicFunctionType) o1;
			CommonSymbolicFunctionType t2 = (CommonSymbolicFunctionType) o2;

			result = typeSequenceComparator.compare(t1.inputTypes(),
					t2.inputTypes());
			if (result != 0)
				return result;
			return compare(t1.outputType(), t2.outputType());
		}
		case TUPLE: {
			CommonSymbolicTupleType t1 = (CommonSymbolicTupleType) o1;
			CommonSymbolicTupleType t2 = (CommonSymbolicTupleType) o2;

			result = t1.name().compareTo(t2.name());
			if (result != 0)
				return result;
			return typeSequenceComparator.compare(t1.sequence(), t2.sequence());
		}
		case UNION: {
			CommonSymbolicUnionType t1 = (CommonSymbolicUnionType) o1;
			CommonSymbolicUnionType t2 = (CommonSymbolicUnionType) o2;

			result = t1.name().compareTo(t2.name());
			if (result != 0)
				return result;
			return typeSequenceComparator.compare(t1.sequence(), t2.sequence());
		}
		default:
			throw new SARLInternalException("unreachable");
		}
	}
}
