package edu.udel.cis.vsl.sarl.IF;

public interface SymbolicCollection extends SymbolicObject {

	public enum SymbolicCollectionKind {
		SEQUENCE, MAP
	}
	
	int size();

	SymbolicCollectionKind collectionKind();

	Iterable<SymbolicExpressionIF> elements();

}
