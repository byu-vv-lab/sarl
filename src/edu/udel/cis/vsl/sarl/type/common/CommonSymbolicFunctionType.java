package edu.udel.cis.vsl.sarl.type.common;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class CommonSymbolicFunctionType extends CommonSymbolicType implements
		SymbolicFunctionType {

	private final static int classCode = CommonSymbolicFunctionType.class
			.hashCode();

	private SymbolicTypeSequence inputTypes;

	private SymbolicType outputType;

	CommonSymbolicFunctionType(SymbolicTypeSequence inputTypes,
			SymbolicType outputType) {
		super(SymbolicTypeKind.FUNCTION);
		assert inputTypes != null;
		assert outputType != null;
		this.inputTypes = inputTypes;
		this.outputType = outputType;
	}

	@Override
	protected boolean typeEquals(CommonSymbolicType thatType) {
		CommonSymbolicFunctionType that = (CommonSymbolicFunctionType) thatType;

		return that.outputType.equals(outputType)
				&& that.inputTypes.equals(inputTypes);
	}

	@Override
	protected int computeHashCode() {
		return classCode ^ inputTypes.hashCode() ^ outputType.hashCode();
	}

	@Override
	public SymbolicType outputType() {
		return outputType;
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		StringBuffer result = inputTypes.toStringBuffer(true);

		result.append("->");
		result.append(outputType.toStringBuffer(true));
		if (atomize)
			atomize(result);
		return result;
	}

	@Override
	public SymbolicTypeSequence inputTypes() {
		return inputTypes;
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		if (!inputTypes.isCanonic())
			inputTypes = (SymbolicTypeSequence) factory.canonic(inputTypes);
		if (!outputType.isCanonic())
			outputType = factory.canonic(outputType);
	}

}
