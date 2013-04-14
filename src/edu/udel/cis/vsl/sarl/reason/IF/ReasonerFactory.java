package edu.udel.cis.vsl.sarl.reason.IF;

import edu.udel.cis.vsl.sarl.IF.Reasoner;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;

public interface ReasonerFactory {

	Reasoner newReasoner(BooleanExpression context);

}
