package edu.udel.cis.vsl.sarl.symbolic.util;

import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverseIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstantIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.number.IntervalIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.SymbolicConstantExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF.SymbolicKind;

/**
 * Substitutes symbolic expressions for symbolic constants.
 */
public class Substituter extends Simplifier {

	private Map<SymbolicConstantIF, SymbolicExpressionIF> substitutionMap;

	private SymbolicExpressionIF newAssumption;

	public Substituter(SymbolicUniverseIF universe,
			Map<SymbolicConstantIF, SymbolicExpressionIF> substitutionMap,
			SymbolicExpressionIF assumption) {
		super(universe);
		this.substitutionMap = substitutionMap;
		if (assumption != null)
			this.newAssumption = simplify(assumption);
	}

	/**
	 * Returns either SymbolicExpressionIF which is non-trivial result of
	 * simplification, or null if no non-trivial simplification is possible.
	 */
	private SymbolicExpressionIF substituteTree(TreeExpressionIF tree) {
		int numArgs = tree.numArguments();
		SymbolicOperator kind = tree.operator();
		SymbolicExpressionIF result;

		if (numArgs == 0) {
			if (kind == SymbolicOperator.SYMBOLIC_CONSTANT) {
				SymbolicConstantIF oldSymbolicConstant = ((SymbolicConstantExpressionIF) tree)
						.symbolicConstant();

				result = substitutionMap.get(oldSymbolicConstant);
				if (result == null) {
					SymbolicTypeIF oldType = tree.type();
					SymbolicTypeIF newType = simplifyType(oldType);

					if (oldType.equals(newType)) {
						result = null;
					} else {
						SymbolicConstantIF newSymbolicConstant = universe
								.getOrCreateSymbolicConstant(
										oldSymbolicConstant.name(), newType);

						result = universe
								.symbolicConstantExpression(newSymbolicConstant);
					}
				}
			} else
				result = null;
		} else {
			SymbolicExpressionIF[] newArgs = new SymbolicExpressionIF[numArgs];
			SymbolicTypeIF oldType = tree.type();
			SymbolicTypeIF newType = simplifyType(oldType);
			boolean change = newType != oldType;

			for (int i = 0; i < numArgs; i++) {
				TreeExpressionIF oldArg = tree.argument(i);
				SymbolicExpressionIF newArg = substituteTree(oldArg);

				if (newArg != null) {
					change = true;
					newArgs[i] = newArg;
				}
			}
			if (change) {
				for (int i = 0; i < numArgs; i++) {
					if (newArgs[i] == null)
						newArgs[i] = universe
								.canonicalizeTree(tree.argument(i));
				}
				result = universe.make(kind, newType, newArgs);
			} else {
				result = null;
			}
		}
		return result;
	}

	public SymbolicExpressionIF simplify(SymbolicExpressionIF expression) {
		if (expression == null)
			return null;

		TreeExpressionIF tree = universe.tree(expression);
		SymbolicExpressionIF result = substituteTree(tree);

		if (result == null)
			return expression;
		else
			return result;
	}

	@Override
	public SymbolicExpressionIF newAssumption() {
		return newAssumption;
	}

	@Override
	public IntervalIF assumptionAsInterval(SymbolicConstantIF symbolicConstant) {
		throw new RuntimeException("Not implemented for substitution.");
	}

}
