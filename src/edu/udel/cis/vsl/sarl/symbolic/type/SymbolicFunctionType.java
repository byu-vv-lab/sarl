package edu.udel.cis.vsl.sarl.symbolic.type;

import edu.udel.cis.vsl.sarl.IF.SymbolicFunctionTypeIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicTypeIF;

public class SymbolicFunctionType extends SymbolicType implements
		SymbolicFunctionTypeIF {

	private SymbolicTypeSequence inputTypes;

	private SymbolicTypeIF outputType;

	SymbolicFunctionType(SymbolicTypeSequence inputTypes,
			SymbolicTypeIF outputType) {
		super(SymbolicTypeKind.FUNCTION);
		assert inputTypes != null;
		assert outputType != null;
		this.inputTypes = inputTypes;
		this.outputType = outputType;
	}

	@Override
	protected boolean intrinsicEquals(SymbolicType thatType) {
		SymbolicFunctionType that = (SymbolicFunctionType) thatType;

		return that.outputType.equals(outputType)
				&& that.inputTypes.equals(inputTypes);
	}

	@Override
	protected int computeHashCode() {
		return kind().hashCode() + inputTypes.hashCode()
				+ outputType.hashCode();
	}

	@Override
	public SymbolicTypeIF outputType() {
		return outputType;
	}

	@Override
	public String toString() {
		return inputTypes + "->" + outputType;
	}

	@Override
	public SymbolicTypeSequence inputTypes() {
		return inputTypes;
	}

	@Override
	protected int intrinsicCompare(SymbolicType thatType) {
		SymbolicFunctionType that = (SymbolicFunctionType) thatType;
		int result = inputTypes.compareTo(that.inputTypes);

		if (result != 0)
			return result;
		return outputType.compareTo(that.outputType);
	}

}
