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
package edu.udel.cis.vsl.sarl.simplify.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
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
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection.SymbolicCollectionKind;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.simplify.IF.Simplifier;

/**
 * A partial implementation of Simplifer which can be extended.
 * 
 * @author siegel
 * 
 */
public abstract class CommonSimplifier implements Simplifier {

	protected PreUniverse universe;

	/** Cached simplifications. */
	protected Map<SymbolicObject, SymbolicObject> simplifyMap = new HashMap<SymbolicObject, SymbolicObject>();

	public CommonSimplifier(PreUniverse universe) {
		assert universe != null;
		this.universe = universe;
	}

	@Override
	public PreUniverse universe() {
		return universe;
	}

	/**
	 * Simplifies a symbolic expression. A concrete extension of this class must
	 * implement this method. The implementation may use
	 * {@link CommonSimplifier#simplifyGenericExpression}, a generic
	 * simplification method provided here.
	 * 
	 * Typically, an implementation of this method should not look in the cache
	 * for a simplified version of expression, because that is done already in
	 * the "main" simplify method, {@link #apply}, which then calls this method
	 * if it does not find it in the cache.
	 * 
	 * 
	 * @param expression
	 *            any symbolic expression.
	 * @return the simplified version of that expression
	 */
	protected abstract SymbolicExpression simplifyExpression(
			SymbolicExpression expression);

	protected SymbolicType simplifyTypeWork(SymbolicType type) {
		switch (type.typeKind()) {
		case BOOLEAN:
		case INTEGER:
		case REAL:
		case CHAR://add char type here, TODO need to check if this is correct
			return type;
		case ARRAY: {
			SymbolicArrayType arrayType = (SymbolicArrayType) type;
			SymbolicType elementType = arrayType.elementType();
			SymbolicType simplifiedElementType = simplifyType(elementType);

			if (arrayType.isComplete()) {
				NumericExpression extent = ((SymbolicCompleteArrayType) arrayType)
						.extent();
				NumericExpression simplifiedExtent = (NumericExpression) apply(extent);

				if (elementType != simplifiedElementType
						|| extent != simplifiedExtent)
					return universe.arrayType(simplifiedElementType,
							simplifiedExtent);
				return arrayType;
			} else {
				if (elementType != simplifiedElementType)
					return universe.arrayType(simplifiedElementType);
				return arrayType;
			}
		}
		case FUNCTION: {
			SymbolicFunctionType functionType = (SymbolicFunctionType) type;
			SymbolicTypeSequence inputs = functionType.inputTypes();
			SymbolicTypeSequence simplifiedInputs = simplifyTypeSequence(inputs);
			SymbolicType output = functionType.outputType();
			SymbolicType simplifiedOutput = simplifyType(output);

			if (inputs != simplifiedInputs || output != simplifiedOutput)
				return universe
						.functionType(simplifiedInputs, simplifiedOutput);
			return type;
		}
		case TUPLE: {
			SymbolicTypeSequence sequence = ((SymbolicTupleType) type)
					.sequence();
			SymbolicTypeSequence simplifiedSequence = simplifyTypeSequence(sequence);

			if (simplifiedSequence != sequence)
				return universe.tupleType(((SymbolicTupleType) type).name(),
						simplifiedSequence);
			return type;
		}
		case UNION: {
			SymbolicTypeSequence sequence = ((SymbolicUnionType) type)
					.sequence();
			SymbolicTypeSequence simplifiedSequence = simplifyTypeSequence(sequence);

			if (simplifiedSequence != sequence)
				return universe.unionType(((SymbolicUnionType) type).name(),
						simplifiedSequence);
			return type;
		}
		default:
			throw new SARLInternalException("unreachable");
		}
	}

	protected SymbolicType simplifyType(SymbolicType type) {
		SymbolicType result = (SymbolicType) simplifyMap.get(type);

		if (result == null) {
			result = simplifyTypeWork(type);
			simplifyMap.put(type, result);
		}
		return result;
	}

	protected Iterable<? extends SymbolicType> simplifyTypeSequenceWork(
			SymbolicTypeSequence sequence) {
		int size = sequence.numTypes();

		for (int i = 0; i < size; i++) {
			SymbolicType type = sequence.getType(i);
			SymbolicType simplifiedType = simplifyType(type);

			if (type != simplifiedType) {
				SymbolicType[] newTypes = new SymbolicType[size];

				for (int j = 0; j < i; j++)
					newTypes[j] = sequence.getType(j);
				newTypes[i] = simplifiedType;
				for (int j = i + 1; j < size; j++)
					newTypes[j] = simplifyType(sequence.getType(j));
				return Arrays.asList(newTypes);
			}
		}
		return sequence;
	}

	protected SymbolicTypeSequence simplifyTypeSequence(
			SymbolicTypeSequence sequence) {
		return universe.typeSequence(simplifyTypeSequenceWork(sequence));
	}

	protected SymbolicSequence<?> simplifySequenceWork(
			SymbolicSequence<?> sequence) {
		@SuppressWarnings("unchecked")
		SymbolicSequence<SymbolicExpression> theSequence = (SymbolicSequence<SymbolicExpression>) sequence;

		return theSequence.apply(this);
	}

	protected SymbolicCollection<?> simplifyGenericCollectionOLD(
			SymbolicCollection<?> collection) {
		int count = 0;
		Iterator<? extends SymbolicExpression> iter = collection.iterator();

		while (iter.hasNext()) {
			SymbolicExpression x = iter.next();
			SymbolicExpression y = apply(x);

			if (x != y) {
				// this assumes iterators will always iterate in same order
				Iterator<? extends SymbolicExpression> iter2 = collection
						.iterator();
				List<SymbolicExpression> list = new LinkedList<SymbolicExpression>();

				for (int i = 0; i < count; i++)
					list.add(iter2.next());
				list.add(y);
				while (iter.hasNext())
					list.add(apply(iter.next()));
				return universe.basicCollection(list);
			}
			count++;
		}
		return collection;
	}

	protected SymbolicCollection<?> simplifyGenericCollection(
			SymbolicCollection<?> collection) {
		Iterator<? extends SymbolicExpression> iter = collection.iterator();
		boolean change = false;
		List<SymbolicExpression> list = new LinkedList<SymbolicExpression>();

		while (iter.hasNext()) {
			SymbolicExpression x = iter.next();
			SymbolicExpression y = apply(x);

			change = change || x != y;
			list.add(y);
		}
		if (change)
			return universe.basicCollection(list);
		return collection;
	}

	protected SymbolicCollection<?> simplifyCollectionWork(
			SymbolicCollection<?> collection) {
		SymbolicCollectionKind kind = collection.collectionKind();

		if (kind == SymbolicCollectionKind.SEQUENCE)
			return simplifySequenceWork((SymbolicSequence<?>) collection);
		return simplifyGenericCollection(collection);
	}

	protected SymbolicCollection<?> simplifyCollection(
			SymbolicCollection<?> collection) {
		SymbolicCollection<?> result = (SymbolicCollection<?>) simplifyMap
				.get(collection);

		if (result == null) {
			result = simplifyCollectionWork(collection);
			simplifyMap.put(collection, result);
		}
		return result;
	}

	protected SymbolicObject simplifyObject(SymbolicObject object) {
		switch (object.symbolicObjectKind()) {
		case BOOLEAN:
		case INT:
		case NUMBER:
		case STRING:
		case CHAR:
			return object;
		case EXPRESSION:
			return apply((SymbolicExpression) object);
		case EXPRESSION_COLLECTION:
			return simplifyCollection((SymbolicCollection<?>) object);
		case TYPE:
			return simplifyType((SymbolicType) object);
		case TYPE_SEQUENCE:
			return simplifyTypeSequence((SymbolicTypeSequence) object);
		default:
			throw new SARLInternalException("unreachable");
		}
	}

	/**
	 * This method simplifies an expression in a generic way that should work
	 * correctly on any symbolic expression: it simplifies the type and the
	 * arguments of the expression, and then rebuilds the expression using
	 * method {@link PreUniverse@make}.
	 * 
	 * This method does <strong>not</strong> look in the table of cached
	 * simplification results for expression. However, the recursive calls to
	 * the arguments may invoke the method {@link apply}, which will look for
	 * cached results on those arguments.
	 * 
	 * @param expression
	 *            any symbolic expression
	 * @return a simplified version of that expression
	 */
	protected SymbolicExpression simplifyGenericExpression(
			SymbolicExpression expression) {
		if (expression.isNull())
			return expression;
		else {
			SymbolicOperator operator = expression.operator();

			if (operator == SymbolicOperator.CONCRETE) {
				SymbolicObject object = (SymbolicObject) expression.argument(0);
				SymbolicObjectKind kind = object.symbolicObjectKind();

				switch (kind) {
				case BOOLEAN:
				case INT:
				case NUMBER:
				case STRING:
					return expression;
				default:
				}
			}
			{
				SymbolicType type = expression.type();
				SymbolicType simplifiedType = simplifyType(type);
				int numArgs = expression.numArguments();
				SymbolicObject[] simplifiedArgs = null;

				if (type == simplifiedType) {
					for (int i = 0; i < numArgs; i++) {
						SymbolicObject arg = expression.argument(i);
						SymbolicObject simplifiedArg = simplifyObject(arg);

						assert simplifiedArg != null;
						if (simplifiedArg != arg) {
							simplifiedArgs = new SymbolicObject[numArgs];
							for (int j = 0; j < i; j++)
								simplifiedArgs[j] = expression.argument(j);
							simplifiedArgs[i] = simplifiedArg;
							for (int j = i + 1; j < numArgs; j++)
								simplifiedArgs[j] = simplifyObject(expression
										.argument(j));
							break;
						}
					}
				} else {
					simplifiedArgs = new SymbolicObject[numArgs];
					for (int i = 0; i < numArgs; i++)
						simplifiedArgs[i] = simplifyObject(expression
								.argument(i));
				}
				if (simplifiedArgs == null)
					return expression;
				return universe.make(operator, simplifiedType, simplifiedArgs);
			}
		}
	}

	// also need to simplify numeric relational expressions like
	// 0<a, 0<=a, 0==a, 0!=a
	// !(0<=a) <=> a<0 <=> -a>0 <=> 0<-a

	// also might be able to simplify symbolic constants such as booleans

	public static int simplifyCount = 0;

	@Override
	public SymbolicExpression apply(SymbolicExpression expression) {
		SymbolicExpression result = (SymbolicExpression) simplifyMap
				.get(expression);

		if (result == null) {
			result = simplifyExpression(expression);
			simplifyMap.put(expression, result);
		}
		return result;
	}

}
