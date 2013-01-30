package edu.udel.cis.vsl.sarl.IF.type;


public interface SymbolicFunctionTypeIF extends SymbolicTypeIF {

	SymbolicTypeSequenceIF inputTypes();

	SymbolicTypeIF outputType();

}
