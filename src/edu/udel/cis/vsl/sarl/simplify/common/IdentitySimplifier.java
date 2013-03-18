package edu.udel.cis.vsl.sarl.simplify.common;

import edu.udel.cis.vsl.sarl.IF.Simplifier;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.Interval;

public class IdentitySimplifier implements Simplifier {

	private SymbolicUniverse universe;

	private BooleanExpression assumption;

	public IdentitySimplifier(SymbolicUniverse universe,
			BooleanExpression assumption) {
		this.universe = universe;
		this.assumption = assumption;
	}

	@Override
	public SymbolicUniverse universe() {
		return universe;
	}

	@Override
	public BooleanExpression newAssumption() {
		return assumption;
	}

	@Override
	public SymbolicExpression apply(SymbolicExpression expression) {
		return expression;
	}

	@Override
	public Interval assumptionAsInterval(SymbolicConstant symbolicConstant) {
		return null;
	}

}
