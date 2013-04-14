package edu.udel.cis.vsl.sarl.prove.IF;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;

public interface TheoremProverFactory {

	TheoremProver newProver(BooleanExpression context);

}
