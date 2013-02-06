package edu.udel.cis.vsl.sarl.IF.collections;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

public interface SymbolicCollection extends SymbolicObject,
		Iterable<SymbolicExpressionIF> {

	public enum SymbolicCollectionKind {
		SET, SEQUENCE, MAP
	}

	int size();

	SymbolicCollectionKind collectionKind();

}
