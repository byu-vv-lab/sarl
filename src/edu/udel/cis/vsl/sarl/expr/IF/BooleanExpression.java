package edu.udel.cis.vsl.sarl.expr.IF;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSet;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

public interface BooleanExpression extends SymbolicExpression {
	
	BooleanExpression booleanArg(int i);
	
	SymbolicCollection<BooleanExpression> booleanCollectionArg(int i);

	SymbolicSet<BooleanExpression> booleanSetArg(int i);
}
