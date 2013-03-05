package edu.udel.cis.vsl.sarl.simplify.IF;

import edu.udel.cis.vsl.sarl.IF.Simplifier;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpression;

public interface SimplifierFactory {

	Simplifier newSimplifier(BooleanExpression assumption); 
}
