package edu.udel.cis.vsl.sarl.symbolic.type;

public class SymbolicPrimitiveType extends SymbolicType {

	SymbolicPrimitiveType(SymbolicTypeKind kind) {
		super(kind);
	}

	public String toString() {
		return kind().toString();
	}

}
