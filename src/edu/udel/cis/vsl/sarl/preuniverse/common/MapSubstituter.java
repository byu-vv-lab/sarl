package edu.udel.cis.vsl.sarl.preuniverse.common;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

/**
 * A substituter specified by giving an explicit Java {@link Map} from
 * {@link SymbolicExpression} to {@link SymbolicExpression} to specify the base
 * substitutions. Bound variables will not be modified.
 * 
 * @author siegel
 *
 */
public class MapSubstituter extends ExpressionSubstituter {

	public class BoundStack implements SubstituterState {
		Deque<SymbolicConstant> stack = new ArrayDeque<>();
	}

	private Map<SymbolicExpression, SymbolicExpression> map;

	public MapSubstituter(PreUniverse universe,
			CollectionFactory collectionFactory,
			SymbolicTypeFactory typeFactory,
			Map<SymbolicExpression, SymbolicExpression> map) {
		super(universe, collectionFactory, typeFactory);
		this.map = map;
	}

	@Override
	protected SymbolicExpression substituteQuantifiedExpression(
			SymbolicExpression expression, SubstituterState state) {
		SymbolicType type = expression.type();
		SymbolicType newType = substituteType(type, state);
		SymbolicConstant arg0 = (SymbolicConstant) expression.argument(0);
		SymbolicExpression arg1 = (SymbolicExpression) expression.argument(1);

		((BoundStack) state).stack.push(arg0);

		SymbolicExpression newArg1 = substituteExpression(arg1, state);

		((BoundStack) state).stack.pop();

		if (type == newType && arg1 == newArg1)
			return expression;
		else
			return universe.make(expression.operator(), newType,
					new SymbolicObject[] { arg0, newArg1 });
	}

	@Override
	protected SymbolicExpression substituteNonquantifiedExpression(
			SymbolicExpression expr, SubstituterState state) {
		// no substitution into bound vars
		if (expr instanceof SymbolicConstant
				&& ((BoundStack) state).stack.contains(expr))
			return expr;

		SymbolicExpression result = map.get(expr);

		if (result != null)
			return result;
		return super.substituteNonquantifiedExpression(expr, state);
	}

	@Override
	protected SubstituterState newState() {
		return new BoundStack();
	}

}
