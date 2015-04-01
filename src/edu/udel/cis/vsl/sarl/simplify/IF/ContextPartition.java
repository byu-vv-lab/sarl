package edu.udel.cis.vsl.sarl.simplify.IF;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

public interface ContextPartition {

	BooleanExpression minimizeFor(SymbolicExpression expr, PreUniverse universe);

}
