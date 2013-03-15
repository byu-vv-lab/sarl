package edu.udel.cis.vsl.sarl.IF.type;

public interface SymbolicIntegerType extends SymbolicType {

	public enum IntegerKind {
		HERBRAND, IDEAL, BOUNDED
	}

	IntegerKind integerKind();

}
