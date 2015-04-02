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
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
import edu.udel.cis.vsl.sarl.util.Pair;

/**
 * Replaces all bound variables in expressions with new ones so that each has a
 * unique name and a name different from any unbound symbolic constant (assuming
 * no one else use the special string).
 * 
 * @author siegel
 */
public class BoundCleaner2 extends ExpressionSubstituter {

	/**
	 * Special string which will be used to give unique name to new variables.
	 * "'" is good but not for Z3.
	 */
	private final static String specialString = "__";

	/**
	 * For each string X, the number of quantified variables named X that have
	 * been encountered so far by this cleaner. If a variable does not occur in
	 * this map that means the number of times is "0".
	 */
	private Map<String, Integer> countMap = new HashMap<>();

	class BoundStack implements SubstituterState {
		Deque<Pair<SymbolicConstant, SymbolicConstant>> stack = new ArrayDeque<>();

		SymbolicConstant get(SymbolicConstant key) {
			// ((BoundStack)state).stack.iterator();
			for (Pair<SymbolicConstant, SymbolicConstant> pair : stack) {
				if (pair.left.equals(key))
					return pair.right;
			}
			return null;
		}

		void push(SymbolicConstant key, SymbolicConstant value) {
			stack.push(new Pair<SymbolicConstant, SymbolicConstant>(key, value));
		}

		void pop() {
			stack.pop();
		}
	}

	public BoundCleaner2(PreUniverse universe,
			CollectionFactory collectionFactory, SymbolicTypeFactory typeFactory) {
		super(universe, collectionFactory, typeFactory);
	}

	@Override
	protected SymbolicExpression substituteQuantifiedExpression(
			SymbolicExpression expression, SubstituterState state) {
		SymbolicConstant boundVariable = (SymbolicConstant) expression
				.argument(0);
		SymbolicExpression arg1 = (SymbolicExpression) expression.argument(1);
		String name = boundVariable.name().getString(), newName;
		Integer count = countMap.get(name);
		SymbolicConstant newBoundVariable;

		if (count == null) {
			count = 0;
		}
		newName = name + specialString + count;
		count++;
		newBoundVariable = universe.symbolicConstant(
				universe.stringObject(newName), boundVariable.type());
		countMap.put(name, count);
		((BoundStack) state).push(boundVariable, newBoundVariable);

		SymbolicExpression newBody = substituteExpression(arg1, state);

		((BoundStack) state).pop();

		SymbolicExpression result = universe.make(expression.operator(),
				expression.type(), new SymbolicObject[] { newBoundVariable,
						newBody });

		return result;
	}

	@Override
	protected SymbolicExpression substituteNonquantifiedExpression(
			SymbolicExpression expression, SubstituterState state) {

		if (expression instanceof SymbolicConstant) {
			SymbolicConstant newConstant = ((BoundStack) state)
					.get(((SymbolicConstant) expression));

			if (newConstant != null)
				return newConstant;
		}
		return super.substituteNonquantifiedExpression(expression, state);
	}

	@Override
	protected SubstituterState newState() {
		return new BoundStack();
	}
}
