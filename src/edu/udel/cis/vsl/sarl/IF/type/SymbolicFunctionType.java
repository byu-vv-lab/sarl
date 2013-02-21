package edu.udel.cis.vsl.sarl.IF.type;


public interface SymbolicFunctionType extends SymbolicType {

	SymbolicTypeSequence inputTypes();

	SymbolicType outputType();

}
