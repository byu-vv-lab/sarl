package edu.udel.cis.vsl.sarl.prove.z3;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class Z3TheoremProverFactory implements TheoremProverFactory {

	private PreUniverse universe;

	public Z3TheoremProverFactory(PreUniverse universe) {
		this.universe = universe;
	}

	@Override
	public TheoremProver newProver(BooleanExpression context) {
		return new Z3TheoremProver(universe, context);
	}

}
