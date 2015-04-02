package edu.udel.cis.vsl.sarl.preuniverse.common;

import java.util.ArrayDeque;
import java.util.Deque;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

/**
 * A substituter specified by a single symbolic constant and a value that is to
 * replace that symbolic constant. Bound variables are ignored.
 * 
 * @author siegel
 *
 */
public class SimpleSubstituter extends ExpressionSubstituter {

	private SymbolicConstant var;

	private SymbolicExpression value;

	class BoundStack implements SubstituterState {
		Deque<SymbolicConstant> stack = new ArrayDeque<>();
	}

	public SimpleSubstituter(PreUniverse universe,
			CollectionFactory collectionFactory,
			SymbolicTypeFactory typeFactory, SymbolicConstant var,
			SymbolicExpression value) {
		super(universe, collectionFactory, typeFactory);
		this.var = var;
		this.value = value;
	}

	@Override
	protected SymbolicExpression substituteQuantifiedExpression(
			SymbolicExpression expression, SubstituterState state) {
		SymbolicConstant boundVariable = (SymbolicConstant) expression
				.argument(0);
		SymbolicExpression arg1 = (SymbolicExpression) expression.argument(1);

		((BoundStack) state).stack.push(boundVariable);

		SymbolicExpression newBody = substituteExpression(arg1, state);

		((BoundStack) state).stack.pop();

		SymbolicExpression result = universe.make(expression.operator(),
				expression.type(), new SymbolicObject[] { boundVariable,
						newBody });

		return result;
	}

	@Override
	protected SymbolicExpression substituteNonquantifiedExpression(
			SymbolicExpression expr, SubstituterState state) {
		if (expr instanceof SymbolicConstant
				&& !((BoundStack) state).stack.contains(expr)
				&& var.equals(expr)) {
			return value;
		}
		return super.substituteNonquantifiedExpression(expr, state);
	}

	@Override
	protected SubstituterState newState() {
		return new BoundStack();
	}
}
