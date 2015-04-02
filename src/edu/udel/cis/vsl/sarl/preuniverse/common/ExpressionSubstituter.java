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
import edu.udel.cis.vsl.sarl.IF.UnaryOperator;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
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

/**
 * A generic, abstract class for performing substitutions on symbolic
 * expressions. Concrete classes must implement the interface
 * {@link SubstituterState}, which is used store anything about the state of the
 * search through the expression. They must also implement the method
 * {@link #newState()} to produce the initial {@link SubstituterState}. They
 * should also override any or all of the <code>protected</code> methods here.
 *
 * @author siegel
 */
public abstract class ExpressionSubstituter implements
		UnaryOperator<SymbolicExpression> {

	/**
	 * An object for storing some information about the state of the search
	 * through a symbolic expression. Used as the type of one of the arguments
	 * to all the methods.
	 * 
	 * @author siegel
	 */
	public interface SubstituterState {

	}

	/**
	 * Factory used for producing {@link SymbolicCollection}s.
	 */
	protected CollectionFactory collectionFactory;

	/**
	 * Factory used for producing {@link SymbolicType}s.
	 */
	protected SymbolicTypeFactory typeFactory;

	/**
	 * Universe for producing new symbolic expressions; will be used especially
	 * for method
	 * {@link PreUniverse#make(SymbolicOperator, SymbolicType, SymbolicObject[])}
	 * .
	 */
	protected PreUniverse universe;

	/**
	 * Cached results of previous substitutions.
	 */
	protected Map<SymbolicExpression, SymbolicExpression> cache = new HashMap<>();

	/**
	 * Constructs new substituter based on given factories.
	 * 
	 * @param universe
	 *            Universe for producing new symbolic expressions;
	 * @param collectionFactory
	 *            Factory used for producing {@link SymbolicCollection}s.
	 * @param typeFactory
	 *            Factory used for producing {@link SymbolicType}s.
	 */
	public ExpressionSubstituter(PreUniverse universe,
			CollectionFactory collectionFactory, SymbolicTypeFactory typeFactory) {
		this.universe = universe;
		this.collectionFactory = collectionFactory;
		this.typeFactory = typeFactory;
	}

	/**
	 * Produce the initial instance of {@link SubstituterState} to start off a
	 * search.
	 * 
	 * @return new initial state
	 */
	protected abstract SubstituterState newState();

	/**
	 * Performs substitution on each symbolic expression in any kind of
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
	 * @return a collection consisting of the substituted expressions
	 */
	private SymbolicCollection<?> substituteGenericCollection(
			SymbolicCollection<?> set, SubstituterState state) {
		Iterator<? extends SymbolicExpression> iter = set.iterator();

		while (iter.hasNext()) {
			SymbolicExpression oldElement = iter.next();
			SymbolicExpression newElement = substituteExpression(oldElement,
					state);

			if (newElement != oldElement) {
				Collection<SymbolicExpression> newSet = new LinkedList<SymbolicExpression>();

				for (SymbolicExpression e : set) {
					if (e == oldElement)
						break;
					newSet.add(e);
				}
				newSet.add(newElement);
				while (iter.hasNext())
					newSet.add(substituteExpression(iter.next(), state));
				return collectionFactory.basicCollection(newSet);
			}
		}
		return set;
	}

	/**
	 * Returns a SymbolicSequence obtained by substituting values in a given
	 * SymbolicSequence.
	 * 
	 * @param sequence
	 *            a SymbolicSequence
	 * @return sequence consisting of substituted expression, in same order as
	 *         original sequence
	 */
	private SymbolicSequence<?> substituteSequence(
			SymbolicSequence<?> sequence, SubstituterState state) {
		Iterator<? extends SymbolicExpression> iter = sequence.iterator();
		int count = 0;

		while (iter.hasNext()) {
			SymbolicExpression oldElement = iter.next();
			SymbolicExpression newElement = substituteExpression(oldElement,
					state);

			if (newElement != oldElement) {
				@SuppressWarnings("unchecked")
				SymbolicSequence<SymbolicExpression> newSequence = (SymbolicSequence<SymbolicExpression>) sequence
						.subSequence(0, count);

				newSequence = newSequence.add(newElement);
				while (iter.hasNext())
					newSequence = newSequence.add(substituteExpression(
							iter.next(), state));
				return newSequence;
			}
			count++;
		}
		return sequence;
	}

	/**
	 * Performs substitution on a {@link SymbolicCollection}. The kind of
	 * collection returned is not necessarily the same as the given one. Only
	 * sequences need to be preserved because other collections are all
	 * processed through method
	 * {@link PreUniverse#make(SymbolicOperator, SymbolicType, SymbolicObject[])}
	 * anyway.
	 * 
	 * @param collection
	 *            any kind of symbolic collection
	 * @return a collection in which substitution has been applied to each
	 *         element using the given map
	 */
	protected SymbolicCollection<?> substituteCollection(
			SymbolicCollection<?> collection, SubstituterState state) {
		SymbolicCollectionKind kind = collection.collectionKind();

		if (kind == SymbolicCollectionKind.SEQUENCE)
			return substituteSequence((SymbolicSequence<?>) collection, state);
		return substituteGenericCollection(collection, state);
	}

	protected SymbolicType substituteType(SymbolicType type,
			SubstituterState state) {
		switch (type.typeKind()) {
		case BOOLEAN:
		case INTEGER:
		case REAL:
		case CHAR:
			return type;
		case ARRAY: {
			SymbolicArrayType arrayType = (SymbolicArrayType) type;
			SymbolicType elementType = arrayType.elementType();
			SymbolicType newElementType = substituteType(elementType, state);

			if (arrayType.isComplete()) {
				NumericExpression extent = ((SymbolicCompleteArrayType) arrayType)
						.extent();
				NumericExpression newExtent = (NumericExpression) substituteExpression(
						extent, state);

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
					state);
			SymbolicType output = functionType.outputType();
			SymbolicType newOutput = substituteType(output, state);

			if (inputs != newInputs || output != newOutput)
				return typeFactory.functionType(newInputs, newOutput);
			return functionType;
		}
		case TUPLE: {
			SymbolicTupleType tupleType = (SymbolicTupleType) type;
			SymbolicTypeSequence fields = tupleType.sequence();
			SymbolicTypeSequence newFields = substituteTypeSequence(fields,
					state);

			if (fields != newFields)
				return typeFactory.tupleType(tupleType.name(), newFields);
			return tupleType;

		}
		case UNION: {
			SymbolicUnionType unionType = (SymbolicUnionType) type;
			SymbolicTypeSequence members = unionType.sequence();
			SymbolicTypeSequence newMembers = substituteTypeSequence(members,
					state);

			if (members != newMembers)
				return typeFactory.unionType(unionType.name(), newMembers);
			return unionType;
		}
		default:
			throw new SARLInternalException("unreachable");
		}
	}

	protected SymbolicTypeSequence substituteTypeSequence(
			SymbolicTypeSequence sequence, SubstituterState state) {
		int count = 0;

		for (SymbolicType t : sequence) {
			SymbolicType newt = substituteType(t, state);

			if (t != newt) {
				int numTypes = sequence.numTypes();
				SymbolicType[] newTypes = new SymbolicType[numTypes];

				for (int i = 0; i < count; i++)
					newTypes[i] = sequence.getType(i);
				newTypes[count] = newt;
				for (int i = count + 1; i < numTypes; i++)
					newTypes[i] = substituteType(sequence.getType(i), state);
				return typeFactory.sequence(newTypes);
			}
			count++;
		}
		return sequence;
	}

	protected SymbolicObject substituteObject(SymbolicObject obj,
			SubstituterState state) {
		switch (obj.symbolicObjectKind()) {
		case BOOLEAN:
		case INT:
		case NUMBER:
		case STRING:
		case CHAR:
			return obj;
		case EXPRESSION:
			return substituteExpression((SymbolicExpression) obj, state);
		case EXPRESSION_COLLECTION:
			return substituteCollection((SymbolicCollection<?>) obj, state);
		case TYPE:
			return substituteType((SymbolicType) obj, state);
		case TYPE_SEQUENCE:
			return substituteTypeSequence((SymbolicTypeSequence) obj, state);
		default:
			throw new SARLInternalException("unreachable");
		}

	}

	/**
	 * Substitutes into a quantified expression. By default, this is same as for
	 * a non-quantified expression, but this method can be overridden.
	 * 
	 * @param expression
	 *            the expression to which substitution should be applied; must
	 *            be a non-<code>null</code> quantified expression
	 * @param state
	 *            the current state of the search
	 * @return the result of applying substitution to <code>expression</code>
	 */
	protected SymbolicExpression substituteQuantifiedExpression(
			SymbolicExpression expression, SubstituterState state) {
		return substituteNonquantifiedExpression(expression, state);
	}

	/**
	 * Determines if the given expression is a quantified expression.
	 * 
	 * @param expr
	 *            a non-<code>null</code> symbolic expression
	 * @return <code>true</code> iff the expression is quantified
	 */
	protected boolean isQuantified(SymbolicExpression expr) {
		SymbolicOperator operator = expr.operator();

		return operator == SymbolicOperator.EXISTS
				|| operator == SymbolicOperator.FORALL
				|| operator == SymbolicOperator.LAMBDA;
	}

	/**
	 * Substitutes into a non-quantified expression. The <code>expression</code>
	 * must be non-<code>null</code>. If it is the SARL "NULL" expression, it is
	 * returned without change. Otherwise, substitution is performed recursively
	 * on the arguments of the expression, the and the new expression is put
	 * back together using
	 * {@link PreUniverse#make(SymbolicOperator, SymbolicType, SymbolicObject[])}
	 * .
	 * 
	 * @param expression
	 *            the expression to which substitution should be applied; must
	 *            be a non-<code>null</code> non-quantified expression
	 * @param state
	 *            the current state of the search
	 * @return the result of applying substitution to <code>expression</code>
	 */
	protected SymbolicExpression substituteNonquantifiedExpression(
			SymbolicExpression expression, SubstituterState state) {
		if (expression.isNull())
			return expression;
		else {
			int numArgs = expression.numArguments();
			SymbolicType type = expression.type();
			SymbolicType newType = substituteType(type, state);
			SymbolicObject[] newArgs = type == newType ? null
					: new SymbolicObject[numArgs];

			for (int i = 0; i < numArgs; i++) {
				SymbolicObject arg = expression.argument(i);
				SymbolicObject newArg = substituteObject(arg, state);

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
			if (newType == type && newArgs == null)
				return expression;
			return universe.make(expression.operator(), newType, newArgs);
		}
	}

	/**
	 * Performs substitution on a symbolic expression. Determines whether the
	 * expression is quantified or not, can invokes the appropriate method:
	 * {@link #substituteQuantifiedExpression(SymbolicExpression, SubstituterState)}
	 * or
	 * {@link #substituteNonquantifiedExpression(SymbolicExpression, SubstituterState)}
	 * 
	 * @param expression
	 *            the expression to which substitution should be applied; must
	 *            be non-<code>null</code>
	 * @param state
	 *            the current state of the search .
	 */
	protected SymbolicExpression substituteExpression(
			SymbolicExpression expression, SubstituterState state) {
		if (isQuantified(expression)) {
			return substituteQuantifiedExpression(expression, state);
		} else {
			return substituteNonquantifiedExpression(expression, state);
		}
	}

	/**
	 * Performs substitution on the given symbolic expression. First looks in
	 * cache to see if substitution was already applied and returns old result
	 * if so. Otherwise, applies the substitution algorithm to the expression,
	 * caches, and returns the result.
	 * 
	 * @param expression
	 *            a non-<code>null</code> symbolic expression
	 * @return the result of performing substitution on <code>expression</code>
	 */
	@Override
	public SymbolicExpression apply(SymbolicExpression expression) {
		SymbolicExpression result = cache.get(expression);

		if (result == null) {
			result = substituteExpression(expression, newState());
			cache.put(expression, result);
		} else {
			// performance debugging experiments:
			// System.out.println("* Substitution cache hit! *");
			// if (!expression.isCanonic())
			// System.out.println("$$$$$$$$$$$$ NOT CANONIC $$$$$$$$$$$$$$$");
		}
		return result;
	}

}
