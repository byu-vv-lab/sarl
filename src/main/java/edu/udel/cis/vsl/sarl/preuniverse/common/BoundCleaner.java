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
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
import edu.udel.cis.vsl.sarl.util.Pair;

/**
 * Replaces all bound variables in expressions with new ones so that each has a
 * unique name and a name different from any unbound symbolic constant (assuming
 * no one else use the special string).
 * 
 * @author Stephen F. Siegel
 */
public class BoundCleaner extends ExpressionSubstituter {

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

	/**
	 * State of search: stack of pairs of symbolic constants. Left component of
	 * a pair is the original bound symbolic constant, right component is the
	 * new bound symbolic constant that will be substituted for the old one. An
	 * entry is pushed onto the stack whenever a quantified expression is
	 * reached, then the body of the expression is searched, then the stack is
	 * popped.
	 * 
	 * @author siegel
	 */
	class BoundStack implements SubstituterState {
		Deque<Pair<SymbolicConstant, SymbolicConstant>> stack = new ArrayDeque<>();

		SymbolicConstant get(SymbolicConstant key) {
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

		@Override
		public boolean isInitial() {
			return stack.isEmpty();
		}
	}

	public BoundCleaner(PreUniverse universe,
			CollectionFactory collectionFactory, SymbolicTypeFactory typeFactory) {
		super(universe, collectionFactory, typeFactory);
	}

	@Override
	protected SubstituterState newState() {
		return new BoundStack();
	}

	@Override
	protected SymbolicExpression substituteQuantifiedExpression(
			SymbolicExpression expression, SubstituterState state) {
		SymbolicConstant oldBoundVariable = (SymbolicConstant) expression
				.argument(0);
		String oldName = oldBoundVariable.name().getString();
		Integer count = countMap.get(oldName);
		SymbolicType newType = substituteType(expression.type(), state);
		SymbolicType newBoundVariableType = substituteType(
				oldBoundVariable.type(), state);

		if (count == null)
			count = 0;

		String newName = oldName + specialString + count;

		count++;
		countMap.put(oldName, count);

		SymbolicConstant newBoundVariable = universe.symbolicConstant(
				universe.stringObject(newName), newBoundVariableType);

		((BoundStack) state).push(oldBoundVariable, newBoundVariable);

		SymbolicExpression newBody = substituteExpression(
				(SymbolicExpression) expression.argument(1), state);

		((BoundStack) state).pop();

		SymbolicExpression result = universe.make(expression.operator(),
				newType, new SymbolicObject[] { newBoundVariable, newBody });

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

}
