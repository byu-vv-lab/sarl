package edu.udel.cis.vsl.sarl.universe.common;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.IF.prove.Simplifier;

public class IdentitySimplifier implements Simplifier {

	private SymbolicUniverse universe;

	private SymbolicExpression assumption;

	public IdentitySimplifier(SymbolicUniverse universe,
			SymbolicExpression assumption) {
		this.universe = universe;
		this.assumption = assumption;
	}

	@Override
	public SymbolicUniverse universe() {
		return universe;
	}

	@Override
	public SymbolicExpression newAssumption() {
		return assumption;
	}

	@Override
	public SymbolicExpression simplify(SymbolicExpression expression) {
		return expression;
	}

	@Override
	public Interval assumptionAsInterval(SymbolicConstant symbolicConstant) {
		return null;
	}

}
