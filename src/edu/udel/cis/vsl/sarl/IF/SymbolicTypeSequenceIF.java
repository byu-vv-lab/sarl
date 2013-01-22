package edu.udel.cis.vsl.sarl.IF;

public interface SymbolicTypeSequenceIF extends Iterable<SymbolicTypeIF>,
		Comparable<SymbolicTypeSequenceIF> {

	int numTypes();

	SymbolicTypeIF getType(int index);

}
