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
import java.util.HashMap;
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
import edu.udel.cis.vsl.sarl.util.SingletonMap;

/**
 * Replaces all bound variables in expressions with possible new ones so that
 * each has a unique name.
 * 
 * @author siegel
 * 
 */
public class BoundCleaner {

	private ExpressionSubstituter2 substituter;

	private CollectionFactory collectionFactory;

	private SymbolicTypeFactory typeFactory;

	private PreUniverse universe;

	private Map<SymbolicExpression, SymbolicExpression> cleanCache = new HashMap<>();

	/**
	 * For each string X, the number of quantified variables named X that have
	 * been encountered so far by this cleaner. If a variable does not occur in
	 * this map that means the number of times is "0".
	 */
	private Map<String, Integer> countMap = new HashMap<>();

	public BoundCleaner(PreUniverse universe,
			CollectionFactory collectionFactory,
			SymbolicTypeFactory typeFactory, ExpressionSubstituter2 substituter) {
		this.universe = universe;
		this.collectionFactory = collectionFactory;
		this.typeFactory = typeFactory;
		this.substituter = substituter;
	}

	private SymbolicCollection<?> cleanGenericCollection(
			SymbolicCollection<?> set) {
		Iterator<? extends SymbolicExpression> iter = set.iterator();

		while (iter.hasNext()) {
			SymbolicExpression oldElement = iter.next();
			SymbolicExpression newElement = cleanExpression(oldElement);

			if (newElement != oldElement) {
				Collection<SymbolicExpression> newSet = new LinkedList<SymbolicExpression>();

				for (SymbolicExpression e : set) {
					if (e == oldElement)
						break;
					newSet.add(e);
				}
				newSet.add(newElement);
				while (iter.hasNext())
					newSet.add(cleanExpression(iter.next()));
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
	private SymbolicSequence<?> cleanSequence(SymbolicSequence<?> sequence) {
		Iterator<? extends SymbolicExpression> iter = sequence.iterator();
		int count = 0;

		while (iter.hasNext()) {
			SymbolicExpression oldElement = iter.next();
			SymbolicExpression newElement = cleanExpression(oldElement);

			if (newElement != oldElement) {
				@SuppressWarnings("unchecked")
				SymbolicSequence<SymbolicExpression> newSequence = (SymbolicSequence<SymbolicExpression>) sequence
						.subSequence(0, count);

				newSequence = newSequence.add(newElement);
				while (iter.hasNext())
					newSequence = newSequence.add(cleanExpression(iter.next()));
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
	private SymbolicCollection<?> cleanCollection(
			SymbolicCollection<?> collection) {
		SymbolicCollectionKind kind = collection.collectionKind();

		if (kind == SymbolicCollectionKind.SEQUENCE)
			return cleanSequence((SymbolicSequence<?>) collection);
		return cleanGenericCollection(collection);
	}

	private SymbolicType cleanType(SymbolicType type) {
		switch (type.typeKind()) {
		case BOOLEAN:
		case INTEGER:
		case REAL:
			return type;
		case ARRAY: {
			SymbolicArrayType arrayType = (SymbolicArrayType) type;
			SymbolicType elementType = arrayType.elementType();
			SymbolicType newElementType = cleanType(elementType);

			if (arrayType.isComplete()) {
				NumericExpression extent = ((SymbolicCompleteArrayType) arrayType)
						.extent();
				NumericExpression newExtent = (NumericExpression) cleanExpression(extent);

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
			SymbolicTypeSequence newInputs = cleanTypeSequence(inputs);
			SymbolicType output = functionType.outputType();
			SymbolicType newOutput = cleanType(output);

			if (inputs != newInputs || output != newOutput)
				return typeFactory.functionType(newInputs, newOutput);
			return functionType;
		}
		case TUPLE: {
			SymbolicTupleType tupleType = (SymbolicTupleType) type;
			SymbolicTypeSequence fields = tupleType.sequence();
			SymbolicTypeSequence newFields = cleanTypeSequence(fields);

			if (fields != newFields)
				return typeFactory.tupleType(tupleType.name(), newFields);
			return tupleType;

		}
		case UNION: {
			SymbolicUnionType unionType = (SymbolicUnionType) type;
			SymbolicTypeSequence members = unionType.sequence();
			SymbolicTypeSequence newMembers = cleanTypeSequence(members);

			if (members != newMembers)
				return typeFactory.unionType(unionType.name(), newMembers);
			return unionType;
		}
		default:
			throw new SARLInternalException("unreachable");
		}
	}

	private SymbolicTypeSequence cleanTypeSequence(SymbolicTypeSequence sequence) {
		int count = 0;

		for (SymbolicType t : sequence) {
			SymbolicType newt = cleanType(t);

			if (t != newt) {
				int numTypes = sequence.numTypes();
				SymbolicType[] newTypes = new SymbolicType[numTypes];

				for (int i = 0; i < count; i++)
					newTypes[i] = sequence.getType(i);
				newTypes[count] = newt;
				for (int i = count + 1; i < numTypes; i++)
					newTypes[i] = cleanType(sequence.getType(i));
				return typeFactory.sequence(newTypes);
			}
			count++;
		}
		return sequence;
	}

	/**
	 * If no changes takes place, result returned will be == given expression.
	 */
	private SymbolicExpression cleanExpression(SymbolicExpression expression) {
		SymbolicOperator operator = expression.operator();
		SymbolicType type = expression.type();

		// TODO: problem can still have same name as free variable!
		// free variable may even be defined after this one.

		if (operator == SymbolicOperator.EXISTS
				|| operator == SymbolicOperator.FORALL
				|| operator == SymbolicOperator.LAMBDA
				|| operator == SymbolicOperator.ARRAY_LAMBDA) {
			SymbolicConstant boundVariable = (SymbolicConstant) expression
					.argument(0);
			SymbolicExpression arg1 = (SymbolicExpression) expression
					.argument(1);
			SymbolicExpression newArg1 = cleanExpression(arg1);
			String name = boundVariable.name().getString(), newName;
			Integer count = countMap.get(name);
			SymbolicConstant newBoundVariable;

			if (count == null) {
				count = 0;
			}
			{
				SingletonMap<SymbolicExpression, SymbolicExpression> substitutionMap;

				newName = name + "'" + count;
				count++;
				newBoundVariable = universe.symbolicConstant(
						universe.stringObject(newName), boundVariable.type());
				substitutionMap = new SingletonMap<SymbolicExpression, SymbolicExpression>(
						boundVariable, newBoundVariable);
				newArg1 = substituter.substitute(newArg1, substitutionMap);
			}
			countMap.put(name, count);
			if (arg1 == newArg1 && boundVariable == newBoundVariable)
				return expression;
			else
				return universe.make(operator, type, new SymbolicObject[] {
						newBoundVariable, newArg1 });
		} else {
			int numArgs = expression.numArguments();
			SymbolicObject[] newArgs = null;

			for (int i = 0; i < numArgs; i++) {
				SymbolicObject arg = expression.argument(i);
				SymbolicObjectKind kind = arg.symbolicObjectKind();

				switch (kind) {
				case BOOLEAN:
				case INT:
				case NUMBER:
				case STRING:
					if (newArgs != null)
						newArgs[i] = arg;
					break;
				default:
					SymbolicObject newArg;

					switch (kind) {
					case EXPRESSION:
						newArg = cleanExpression((SymbolicExpression) arg);
						break;
					case EXPRESSION_COLLECTION:
						newArg = cleanCollection((SymbolicCollection<?>) arg);
						break;
					case TYPE:
						throw new SARLInternalException("unreachable");
					case TYPE_SEQUENCE:
						newArg = cleanTypeSequence((SymbolicTypeSequence) arg);
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
			if (newArgs == null)
				return expression;
			return universe.make(operator, type, newArgs);
		}
	}

	public SymbolicExpression clean(SymbolicExpression expression) {
		SymbolicExpression result;

		expression = universe.canonic(expression);
		result = cleanCache.get(expression);
		if (result == null) {
			result = cleanExpression(expression);
			result = universe.canonic(result);
			cleanCache.put(expression, result);
		}
		return result;
	}

}
