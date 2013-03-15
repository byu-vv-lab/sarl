package edu.udel.cis.vsl.sarl.IF.type;

public interface SymbolicRealType extends SymbolicType {
	
	public enum RealKind {
		HERBRAND,
		FLOAT,
		IDEAL
	};
	
	RealKind realKind();

}
