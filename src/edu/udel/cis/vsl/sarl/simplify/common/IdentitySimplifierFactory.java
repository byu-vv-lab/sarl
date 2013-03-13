package edu.udel.cis.vsl.sarl.simplify.common;

import edu.udel.cis.vsl.sarl.IF.Simplifier;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.simplify.IF.SimplifierFactory;

public class IdentitySimplifierFactory implements SimplifierFactory {

	private SymbolicUniverse universe;

	public IdentitySimplifierFactory(SymbolicUniverse universe) {
		this.universe = universe;
	}

	@Override
	public Simplifier newSimplifier(BooleanExpression assumption) {
		return new IdentitySimplifier(universe, assumption);
	}

}
