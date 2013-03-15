package edu.udel.cis.vsl.sarl.simplify;

import edu.udel.cis.vsl.sarl.IF.Simplifier;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.simplify.IF.SimplifierFactory;
import edu.udel.cis.vsl.sarl.simplify.common.IdentitySimplifier;
import edu.udel.cis.vsl.sarl.simplify.common.IdentitySimplifierFactory;

public class Simplify {

	public static Simplifier identitySimplifier(SymbolicUniverse universe,
			SymbolicExpression assumption) {
		return new IdentitySimplifier(universe, assumption);
	}

	public static SimplifierFactory newIdentitySimplifierFactory(
			SymbolicUniverse universe) {
		return new IdentitySimplifierFactory(universe);
	}

}
