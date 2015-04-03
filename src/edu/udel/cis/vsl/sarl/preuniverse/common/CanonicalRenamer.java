package edu.udel.cis.vsl.sarl.preuniverse.common;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
import edu.udel.cis.vsl.sarl.util.Pair;

/**
 * A substituter used to assign new, canonical names to all symbolic constants
 * occurring in a sequence of expressions. This class is provided with a root
 * {@link String}, e.g., "X". Then, as it encounters symbolic constants, it
 * renames them X0, X1, X2, ...., in that order.
 * 
 * @author Stephen F. Siegel
 */
public class CanonicalRenamer extends ExpressionSubstituter {

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
	}

	/**
	 * The root {@link String} to use for the new names. The integer 0, 1, ...,
	 * will be appended to {@link #root} to form the names of the new symbolic
	 * constants.
	 */
	private String root;

	/**
	 * Map from original free (not bound) symbolic constants to their newly
	 * named versions.
	 */
	private Map<SymbolicConstant, SymbolicConstant> freeMap = new HashMap<>();

	/**
	 * The number of symbolic constants encountered so far. This includes both
	 * free and bound symbolic constants. Each declaration of a bound symbolic
	 * constant (i.e., its binding occurrence in a quantified expression) is
	 * considered a totally new symbolic constant, so will be given a unique
	 * name.
	 */
	private int varCount = 0;

	/**
	 * Should this not change the names of symbolic constants of functional
	 * type?
	 */
	private boolean ignoreFunctions;

	/**
	 * Creates new renamer.
	 * 
	 * @param universe
	 *            symbolic universe for producing new {@link SymbolicExpression}
	 *            s
	 * @param collectionFactory
	 *            factory for producing new {@link SymbolicCollection}s
	 * @param typeFactory
	 *            factory for producing new {@link SymbolicType}s
	 * @param root
	 *            root of new names
	 * @param ignoreFunctions
	 *            should this not change the names of symbolic constants of
	 *            functional type?
	 */
	public CanonicalRenamer(PreUniverse universe,
			CollectionFactory collectionFactory,
			SymbolicTypeFactory typeFactory, String root,
			boolean ignoreFunctions) {
		super(universe, collectionFactory, typeFactory);
		this.root = root;
		this.ignoreFunctions = ignoreFunctions;
	}

	private boolean ignore(SymbolicConstant x) {
		return ignoreFunctions
				&& x.type().typeKind() == SymbolicTypeKind.FUNCTION;
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
		SymbolicType newType = substituteType(expression.type(), state);
		String newName = root + varCount;

		varCount++;

		SymbolicType newBoundVariableType = substituteType(
				oldBoundVariable.type(), state);
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
			SymbolicExpression expr, SubstituterState state) {
		if (expr instanceof SymbolicConstant
				&& !ignore((SymbolicConstant) expr)) {
			SymbolicConstant newVar = freeMap.get((SymbolicConstant) expr);

			if (newVar == null) {
				SymbolicType oldType = expr.type();
				SymbolicType newType = substituteType(oldType, state);

				newVar = universe.symbolicConstant(
						universe.stringObject(root + varCount), newType);
				varCount++;
				freeMap.put((SymbolicConstant) expr, newVar);
			}
			return newVar;
		} else {
			return super.substituteNonquantifiedExpression(expr, state);
		}
	}
}
