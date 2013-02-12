package edu.udel.cis.vsl.sarl.type.common;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequenceIF;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class SymbolicFunctionType extends SymbolicType implements
		SymbolicFunctionTypeIF {

	private SymbolicTypeSequenceIF inputTypes;

	private SymbolicTypeIF outputType;

	SymbolicFunctionType(SymbolicTypeSequenceIF inputTypes,
			SymbolicTypeIF outputType) {
		super(SymbolicTypeKind.FUNCTION);
		assert inputTypes != null;
		assert outputType != null;
		this.inputTypes = inputTypes;
		this.outputType = outputType;
	}

	@Override
	protected boolean typeEquals(SymbolicType thatType) {
		SymbolicFunctionType that = (SymbolicFunctionType) thatType;

		return that.outputType.equals(outputType)
				&& that.inputTypes.equals(inputTypes);
	}

	@Override
	protected int computeHashCode() {
		return typeKind().hashCode() + inputTypes.hashCode()
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
	public SymbolicTypeSequenceIF inputTypes() {
		return inputTypes;
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		if (!inputTypes.isCanonic())
			inputTypes = (SymbolicTypeSequenceIF) factory.canonic(inputTypes);
		if (!outputType.isCanonic())
			outputType = factory.canonic(outputType);
	}

}
