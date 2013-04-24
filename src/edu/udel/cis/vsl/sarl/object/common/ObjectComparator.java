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
package edu.udel.cis.vsl.sarl.object.common;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject.SymbolicObjectKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;

public class ObjectComparator implements Comparator<SymbolicObject> {

	private Comparator<SymbolicExpression> expressionComparator;

	private Comparator<SymbolicCollection<?>> collectionComparator;

	private Comparator<SymbolicType> typeComparator;

	private Comparator<SymbolicTypeSequence> typeSequenceComparator;

	public ObjectComparator() {
	}

	public void setExpressionComparator(Comparator<SymbolicExpression> c) {
		expressionComparator = c;
	}

	public void setCollectionComparator(Comparator<SymbolicCollection<?>> c) {
		collectionComparator = c;
	}

	public void setTypeComparator(Comparator<SymbolicType> c) {
		typeComparator = c;
	}

	public void setTypeSequenceComparator(Comparator<SymbolicTypeSequence> c) {
		typeSequenceComparator = c;
	}

	public Comparator<SymbolicExpression> expressionComparator() {
		return expressionComparator;
	}

	public Comparator<SymbolicCollection<?>> collectionComparator() {
		return collectionComparator;
	}

	public Comparator<SymbolicType> typeComparator() {
		return typeComparator;
	}

	public Comparator<SymbolicTypeSequence> typeSequenceComparator() {
		return typeSequenceComparator;
	}

	// TODO: possibly. Stick another field in SymbolicObject:
	// RationalNumber order. Add a tree here?, which is sorted
	// (TreeMap?). Whenever you canonicalize a
	// SymbolicObject add it to the tree, and find the things
	// immediately preceding and after it. Take their orders
	// and average them and assign that to the new thing.
	// Modify compare map: if both objects are canonic,
	// just compare their orders.

	@Override
	public int compare(SymbolicObject o1, SymbolicObject o2) {
		if (o1 == o2)
			return 0;
		else {
			SymbolicObjectKind kind = o1.symbolicObjectKind();
			int result = kind.compareTo(o2.symbolicObjectKind());

			if (result != 0)
				return result;
			switch (kind) {
			case EXPRESSION:
				return expressionComparator.compare((SymbolicExpression) o1,
						(SymbolicExpression) o2);
			case EXPRESSION_COLLECTION:
				return collectionComparator.compare((SymbolicCollection<?>) o1,
						(SymbolicCollection<?>) o2);
			case TYPE:
				return typeComparator.compare((SymbolicType) o1,
						(SymbolicType) o2);
			case TYPE_SEQUENCE:
				return typeSequenceComparator.compare(
						(SymbolicTypeSequence) o1, (SymbolicTypeSequence) o2);
			case BOOLEAN:
				return ((BooleanObject) o1).getBoolean() ? (((BooleanObject) o2)
						.getBoolean() ? 0 : 1) : (((BooleanObject) o2)
						.getBoolean() ? -1 : 0);
			case INT:
				return ((IntObject) o1).getInt() - ((IntObject) o2).getInt();
			case NUMBER:
				return ((Number) o1).compareTo((Number) o2);
			case STRING:
				return ((StringObject) o1).getString().compareTo(
						((StringObject) o2).getString());
			default:
				throw new SARLInternalException("unreachable");
			}
		}
	}
}