package edu.udel.cis.vsl.sarl.IF;

public interface SymbolicCollection extends SymbolicObject,
		Iterable<SymbolicExpressionIF> {

	public enum SymbolicCollectionKind {
		SET, SEQUENCE, MAP
	}

	int size();

	SymbolicCollectionKind collectionKind();

}
