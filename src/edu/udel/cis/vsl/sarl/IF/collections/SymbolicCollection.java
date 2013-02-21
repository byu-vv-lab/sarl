package edu.udel.cis.vsl.sarl.IF.collections;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

public interface SymbolicCollection extends SymbolicObject,
		Iterable<SymbolicExpression> {

	public enum SymbolicCollectionKind {
		SET, SEQUENCE, MAP
	}

	int size();

	SymbolicCollectionKind collectionKind();

	SymbolicExpression getFirst();

}
