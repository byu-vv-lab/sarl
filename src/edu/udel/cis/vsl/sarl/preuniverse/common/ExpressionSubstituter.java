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
package edu.udel.cis.vsl.sarl.preuniverse.common;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject.SymbolicObjectKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection.SymbolicCollectionKind;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class ExpressionSubstituter {

	private CollectionFactory collectionFactory;

	private SymbolicTypeFactory typeFactory;

	private PreUniverse universe;

	public ExpressionSubstituter(PreUniverse universe,
			CollectionFactory collectionFactory, SymbolicTypeFactory typeFactory) {
		this.universe = universe;
		this.collectionFactory = collectionFactory;
		this.typeFactory = typeFactory;
	}

	/**
	 * Performs subsitution on each symbolic expression in any kind of
	 * SymbolicCollection (set, map, ...). The result will be a
	 * SymbolicCollection that is not necessarily the same kind as the original.
	 * For most cases, this is OK: e.g., if this is going to be used as an
	 * argument to make() where the operator is AND, OR, ADD, or MULTIPLY, since
	 * that will only use collection to iterate over its contents. For other
	 * purposes, a SymbolicSequence is required, and the method
	 * substituteSequence should be used.
	 * 
	 * @param set
	 *            a symbolic collection of any kind
	 * @param map
	 *            map of symbolic expressions to the expressions that should
	 *            replace them
	 * @return a collection consisting of the substituted expressions
	 */
	private SymbolicCollection<?> substituteGenericCollection(
			SymbolicCollection<?> set,
			Map<SymbolicExpression, SymbolicExpression> map) {
		Iterator<? extends SymbolicExpression> iter = set.iterator();

		while (iter.hasNext()) {
			SymbolicExpression oldElement = iter.next();
			SymbolicExpression newElement = substitute(oldElement, map);

			if (newElement != oldElement) {
				Collection<SymbolicExpression> newSet = new LinkedList<SymbolicExpression>();

				for (SymbolicExpression e : set) {
					if (e == oldElement)
						break;
					newSet.add(e);
				}
				newSet.add(newElement);
				while (iter.hasNext())
					newSet.add(substitute(iter.next(), map));
				return collectionFactory.basicCollection(newSet);
			}
		}
		return set;
	}

	/**
	 * Returns a SymbolicSequence consisting obtained by substituting values in
	 * a given SymbolicSequence.
	 * 
	 * @param sequence
	 *            a SymbolicSequence
	 * @param map
	 *            map of symbolic expressions to the expressions that should
	 *            replace them
	 * @return sequence consisting of substituted expression, in same order as
	 *         original sequence
	 */
	private SymbolicSequence<?> substituteSequence(
			SymbolicSequence<?> sequence,
			Map<SymbolicExpression, SymbolicExpression> map) {
		Iterator<? extends SymbolicExpression> iter = sequence.iterator();
		int count = 0;

		while (iter.hasNext()) {
			SymbolicExpression oldElement = iter.next();
			SymbolicExpression newElement = substitute(oldElement, map);

			if (newElement != oldElement) {
				@SuppressWarnings("unchecked")
				SymbolicSequence<SymbolicExpression> newSequence = (SymbolicSequence<SymbolicExpression>) sequence
						.subSequence(0, count);

				newSequence = newSequence.add(newElement);
				while (iter.hasNext())
					newSequence = newSequence.add(substitute(iter.next(), map));
				return newSequence;
			}
			count++;
		}
		return sequence;
	}

	/**
	 * Performs subsitution on a SymbolicCollection. The kind of collection
	 * returned is not necessarily the same as the given one. Only sequences
	 * need to be preserved because other collections are all processed through
	 * method make anyway.
	 * 
	 * @param collection
	 *            any kind of symbolic collection
	 * @param map
	 *            a symbolic map from symbolic expressions to symbolic
	 *            expressions
	 * @return a collection in which substitution has been applied to each
	 *         element using the given map
	 */
	private SymbolicCollection<?> substitute(SymbolicCollection<?> collection,
			Map<SymbolicExpression, SymbolicExpression> map) {
		SymbolicCollectionKind kind = collection.collectionKind();

		if (kind == SymbolicCollectionKind.SEQUENCE)
			return substituteSequence((SymbolicSequence<?>) collection, map);
		return substituteGenericCollection(collection, map);
	}

	private SymbolicType substitute(SymbolicType type,
			Map<SymbolicExpression, SymbolicExpression> map) {
		switch (type.typeKind()) {
		case BOOLEAN:
		case INTEGER:
		case REAL:
			return type;
		case ARRAY: {
			SymbolicArrayType arrayType = (SymbolicArrayType) type;
			SymbolicType elementType = arrayType.elementType();
			SymbolicType newElementType = substitute(elementType, map);

			if (arrayType.isComplete()) {
				NumericExpression extent = ((SymbolicCompleteArrayType) arrayType)
						.extent();
				NumericExpression newExtent = (NumericExpression) substitute(
						extent, map);

				if (elementType != newElementType || extent != newExtent)
					return typeFactory.arrayType(newElementType, newExtent);
				return arrayType;
			}
			if (elementType != newElementType)
				return typeFactory.arrayType(newElementType);
			return arrayType;
		}
		case FUNCTION: {
			SymbolicFunctionType functionType = (SymbolicFunctionType) type;
			SymbolicTypeSequence inputs = functionType.inputTypes();
			SymbolicTypeSequence newInputs = substitute(inputs, map);
			SymbolicType output = functionType.outputType();
			SymbolicType newOutput = substitute(output, map);

			if (inputs != newInputs || output != newOutput)
				return typeFactory.functionType(newInputs, newOutput);
			return functionType;
		}
		case TUPLE: {
			SymbolicTupleType tupleType = (SymbolicTupleType) type;
			SymbolicTypeSequence fields = tupleType.sequence();
			SymbolicTypeSequence newFields = substitute(fields, map);

			if (fields != newFields)
				return typeFactory.tupleType(tupleType.name(), newFields);
			return tupleType;

		}
		case UNION: {
			SymbolicUnionType unionType = (SymbolicUnionType) type;
			SymbolicTypeSequence members = unionType.sequence();
			SymbolicTypeSequence newMembers = substitute(members, map);

			if (members != newMembers)
				return typeFactory.unionType(unionType.name(), newMembers);
			return unionType;
		}
		default:
			throw new SARLInternalException("unreachable");
		}
	}

	private SymbolicTypeSequence substitute(SymbolicTypeSequence sequence,
			Map<SymbolicExpression, SymbolicExpression> map) {
		int count = 0;

		for (SymbolicType t : sequence) {
			SymbolicType newt = substitute(t, map);

			if (t != newt) {
				int numTypes = sequence.numTypes();
				SymbolicType[] newTypes = new SymbolicType[numTypes];

				for (int i = 0; i < count; i++)
					newTypes[i] = sequence.getType(i);
				newTypes[count] = newt;
				for (int i = count + 1; i < numTypes; i++)
					newTypes[i] = substitute(sequence.getType(i), map);
				return typeFactory.sequence(newTypes);
			}
			count++;
		}
		return sequence;
	}

	/**
	 * If no changes takes place, result returned will be == given expression.
	 */
	public SymbolicExpression substitute(SymbolicExpression expression,
			Map<SymbolicExpression, SymbolicExpression> map) {
		SymbolicExpression newValue = map.get(expression);

		if (newValue != null)
			return newValue;
		if (expression.isNull())
			return expression;
		else {
			int numArgs = expression.numArguments();
			SymbolicType type = expression.type();
			SymbolicType newType = substitute(type, map);
			SymbolicObject[] newArgs = type == newType ? null
					: new SymbolicObject[numArgs];

			for (int i = 0; i < numArgs; i++) {
				SymbolicObject arg = expression.argument(i);
				SymbolicObjectKind kind = arg.symbolicObjectKind();

				switch (kind) {
				case BOOLEAN:
				case INT:
				case NUMBER:
				case STRING:
					// no substitutions into those primitive objects
					if (newArgs != null)
						newArgs[i] = arg;
					break;
				default:
					SymbolicObject newArg;

					switch (kind) {
					case EXPRESSION:
						newArg = substitute((SymbolicExpression) arg, map);
						break;
					case EXPRESSION_COLLECTION:
						newArg = substitute((SymbolicCollection<?>) arg, map);
						break;
					case TYPE: // currently unreachable, no expressions have TYPE arg
						newArg = substitute((SymbolicType) arg, map);
						break;
					case TYPE_SEQUENCE:
						newArg = substitute((SymbolicTypeSequence) arg, map);
					default:
						throw new SARLInternalException("unreachable");
					}
					if (newArg != arg) {
						if (newArgs == null) {
							newArgs = new SymbolicObject[numArgs];

							for (int j = 0; j < i; j++)
								newArgs[j] = expression.argument(j);
						}
					}
					if (newArgs != null)
						newArgs[i] = newArg;
				}
			}
			if (newType == type && newArgs == null)
				return expression;
			return universe.make(expression.operator(), newType, newArgs);
		}
	}
}
