package edu.udel.cis.vsl.sarl.simplify;

import edu.udel.cis.vsl.sarl.IF.Simplifier;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.simplify.common.IdentitySimplifier;

public class Simplify {

	Simplifier identitySimplifier(SymbolicUniverse universe,
			SymbolicExpression assumption) {
		return new IdentitySimplifier(universe, assumption);
	}

}
