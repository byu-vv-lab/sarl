package edu.udel.cis.vsl.sarl.IF.expr;

import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSet;

public interface BooleanExpression extends SymbolicExpression {
	
	BooleanExpression booleanArg(int i);
	
	SymbolicCollection<BooleanExpression> booleanCollectionArg(int i);

	SymbolicSet<BooleanExpression> booleanSetArg(int i);
}
