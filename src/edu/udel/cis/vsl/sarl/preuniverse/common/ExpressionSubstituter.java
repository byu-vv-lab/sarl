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

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
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
			Map<SymbolicExpression, SymbolicExpression> map,
			Deque<SymbolicConstant> boundStack) {
		Iterator<? extends SymbolicExpression> iter = set.iterator();

		while (iter.hasNext()) {
			SymbolicExpression oldElement = iter.next();
			SymbolicExpression newElement = substituteExpression(oldElement,
					map, boundStack);

			if (newElement != oldElement) {
				Collection<SymbolicExpression> newSet = new LinkedList<SymbolicExpression>();

				for (SymbolicExpression e : set) {
					if (e == oldElement)
						break;
					newSet.add(e);
				}
				newSet.add(newElement);
				while (iter.hasNext())
					newSet.add(substituteExpression(iter.next(), map,
							boundStack));
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
			Map<SymbolicExpression, SymbolicExpression> map,
			Deque<SymbolicConstant> boundStack) {
		Iterator<? extends SymbolicExpression> iter = sequence.iterator();
		int count = 0;

		while (iter.hasNext()) {
			SymbolicExpression oldElement = iter.next();
			SymbolicExpression newElement = substituteExpression(oldElement,
					map, boundStack);

			if (newElement != oldElement) {
				@SuppressWarnings("unchecked")
				SymbolicSequence<SymbolicExpression> newSequence = (SymbolicSequence<SymbolicExpression>) sequence
						.subSequence(0, count);

				newSequence = newSequence.add(newElement);
				while (iter.hasNext())
					newSequence = newSequence.add(substituteExpression(
							iter.next(), map, boundStack));
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
	private SymbolicCollection<?> substituteCollection(
			SymbolicCollection<?> collection,
			Map<SymbolicExpression, SymbolicExpression> map,
			Deque<SymbolicConstant> boundStack) {
		SymbolicCollectionKind kind = collection.collectionKind();

		if (kind == SymbolicCollectionKind.SEQUENCE)
			return substituteSequence((SymbolicSequence<?>) collection, map,
					boundStack);
		return substituteGenericCollection(collection, map, boundStack);
	}

	private SymbolicType substituteType(SymbolicType type,
			Map<SymbolicExpression, SymbolicExpression> map,
			Deque<SymbolicConstant> boundStack) {
		switch (type.typeKind()) {
		case BOOLEAN:
		case INTEGER:
		case REAL:
		case CHAR: // add char here, because printf in MPI programs will make char part of bundle type.
			return type;
		case ARRAY: {
			SymbolicArrayType arrayType = (SymbolicArrayType) type;
			SymbolicType elementType = arrayType.elementType();
			SymbolicType newElementType = substituteType(elementType, map,
					boundStack);

			if (arrayType.isComplete()) {
				NumericExpression extent = ((SymbolicCompleteArrayType) arrayType)
						.extent();
				NumericExpression newExtent = (NumericExpression) substituteExpression(
						extent, map, boundStack);

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
			SymbolicTypeSequence newInputs = substituteTypeSequence(inputs,
					map, boundStack);
			SymbolicType output = functionType.outputType();
			SymbolicType newOutput = substituteType(output, map, boundStack);

			if (inputs != newInputs || output != newOutput)
				return typeFactory.functionType(newInputs, newOutput);
			return functionType;
		}
		case TUPLE: {
			SymbolicTupleType tupleType = (SymbolicTupleType) type;
			SymbolicTypeSequence fields = tupleType.sequence();
			SymbolicTypeSequence newFields = substituteTypeSequence(fields,
					map, boundStack);

			if (fields != newFields)
				return typeFactory.tupleType(tupleType.name(), newFields);
			return tupleType;

		}
		case UNION: {
			SymbolicUnionType unionType = (SymbolicUnionType) type;
			SymbolicTypeSequence members = unionType.sequence();
			SymbolicTypeSequence newMembers = substituteTypeSequence(members,
					map, boundStack);

			if (members != newMembers)
				return typeFactory.unionType(unionType.name(), newMembers);
			return unionType;
		}
		default:
			throw new SARLInternalException("unreachable");
		}
	}

	private SymbolicTypeSequence substituteTypeSequence(
			SymbolicTypeSequence sequence,
			Map<SymbolicExpression, SymbolicExpression> map,
			Deque<SymbolicConstant> boundStack) {
		int count = 0;

		for (SymbolicType t : sequence) {
			SymbolicType newt = substituteType(t, map, boundStack);

			if (t != newt) {
				int numTypes = sequence.numTypes();
				SymbolicType[] newTypes = new SymbolicType[numTypes];

				for (int i = 0; i < count; i++)
					newTypes[i] = sequence.getType(i);
				newTypes[count] = newt;
				for (int i = count + 1; i < numTypes; i++)
					newTypes[i] = substituteType(sequence.getType(i), map,
							boundStack);
				return typeFactory.sequence(newTypes);
			}
			count++;
		}
		return sequence;
	}

	/**
	 * If no changes takes place, result returned will be == given expression.
	 */
	private SymbolicExpression substituteExpression(
			SymbolicExpression expression,
			Map<SymbolicExpression, SymbolicExpression> map,
			Deque<SymbolicConstant> boundStack) {
		SymbolicOperator operator = expression.operator();

		// no substitution into bound variables...
		if (operator == SymbolicOperator.SYMBOLIC_CONSTANT
				&& boundStack.contains(expression))
			return expression;
		if (operator == SymbolicOperator.EXISTS
				|| operator == SymbolicOperator.FORALL
				|| operator == SymbolicOperator.LAMBDA
				|| operator == SymbolicOperator.ARRAY_LAMBDA) {
			SymbolicType type = expression.type();
			SymbolicType newType = substituteType(type, map, boundStack);
			SymbolicConstant arg0 = (SymbolicConstant) expression.argument(0);
			SymbolicExpression arg1 = (SymbolicExpression) expression
					.argument(1);
			SymbolicExpression newArg1;

			boundStack.push(arg0);
			newArg1 = substituteExpression(arg1, map, boundStack);
			boundStack.pop();
			if (type == newType && arg1 == newArg1)
				return expression;
			else
				return universe.make(operator, newType, new SymbolicObject[] {
						arg0, newArg1 });
		} else {
			SymbolicExpression newValue = map.get(expression);

			if (newValue != null)
				return newValue;
			if (expression.isNull())
				return expression;
			else {
				int numArgs = expression.numArguments();
				SymbolicType type = expression.type();
				SymbolicType newType = substituteType(type, map, boundStack);
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
							newArg = substituteExpression(
									(SymbolicExpression) arg, map, boundStack);
							break;
						case EXPRESSION_COLLECTION:
							newArg = substituteCollection(
									(SymbolicCollection<?>) arg, map,
									boundStack);
							break;
						case TYPE: // currently unreachable, no expressions have
									// TYPE arg
							newArg = substituteType((SymbolicType) arg, map,
									boundStack);
							break;
						case TYPE_SEQUENCE:
							newArg = substituteTypeSequence(
									(SymbolicTypeSequence) arg, map, boundStack);
							break;
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
				return universe.make(operator, newType, newArgs);
			}
		}
	}

	public SymbolicExpression substitute(SymbolicExpression expression,
			Map<SymbolicExpression, SymbolicExpression> map) {
		return substituteExpression(expression, map,
				new ArrayDeque<SymbolicConstant>());
	}

}
