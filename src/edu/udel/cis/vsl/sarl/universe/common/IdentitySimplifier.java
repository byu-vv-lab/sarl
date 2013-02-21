package edu.udel.cis.vsl.sarl.universe.common;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverseIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstantIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.number.IntervalIF;
import edu.udel.cis.vsl.sarl.IF.prove.SimplifierIF;

public class IdentitySimplifier implements SimplifierIF {

	private SymbolicUniverseIF universe;

	private SymbolicExpressionIF assumption;

	public IdentitySimplifier(SymbolicUniverseIF universe,
			SymbolicExpressionIF assumption) {
		this.universe = universe;
		this.assumption = assumption;
	}

	@Override
	public SymbolicUniverseIF universe() {
		return universe;
	}

	@Override
	public SymbolicExpressionIF newAssumption() {
		return assumption;
	}

	@Override
	public SymbolicExpressionIF simplify(SymbolicExpressionIF expression) {
		return expression;
	}

	@Override
	public IntervalIF assumptionAsInterval(SymbolicConstantIF symbolicConstant) {
		return null;
	}

}
