package edu.udel.cis.vsl.sarl.IF.collections;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

public interface SymbolicCollection<T extends SymbolicExpression> extends
		SymbolicObject, Iterable<T> {

	public enum SymbolicCollectionKind {
		BASIC, SET, SEQUENCE, MAP
	}

	int size();

	SymbolicCollectionKind collectionKind();

	T getFirst();

}
