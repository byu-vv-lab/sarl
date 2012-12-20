package edu.udel.cis.vsl.sarl.symbolic.type;

import java.util.Arrays;

import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicFunctionTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF;

public class SymbolicFunctionType extends SymbolicType implements
		SymbolicFunctionTypeIF {

	SymbolicTypeIF[] inputTypes;

	SymbolicTypeIF outputType;

	SymbolicFunctionType(SymbolicTypeIF[] inputTypes, SymbolicTypeIF outputType) {
		super(SymbolicTypeKind.FUNCTION);
		assert inputTypes != null;
		assert outputType != null;

		int numInputs = inputTypes.length;

		this.inputTypes = new SymbolicTypeIF[numInputs];
		for (int i = 0; i < numInputs; i++) {
			assert inputTypes[i] != null;
			this.inputTypes[i] = inputTypes[i];
		}
		this.outputType = outputType;
	}

	@Override
	boolean intrinsicEquals(SymbolicType thatType) {
		if (thatType instanceof SymbolicFunctionType) {
			SymbolicFunctionType that = (SymbolicFunctionType) thatType;

			return that.outputType.equals(outputType)
					&& Arrays.equals(inputTypes, that.inputTypes);
		}
		return false;
	}

	@Override
	int intrinsicHashCode() {
		return SymbolicFunctionType.class.hashCode() + outputType.hashCode()
				+ Arrays.hashCode(inputTypes);
	}

	public SymbolicTypeIF inputType(int index) {
		return inputTypes[index];
	}

	public int numInputs() {
		return inputTypes.length;
	}

	public SymbolicTypeIF outputType() {
		return outputType;
	}

	public String toString() {
		String result = "(";
		int numInputs = numInputs();

		for (int i = 0; i < numInputs; i++) {
			if (i > 0)
				result += ",";
			result += inputType(i);
		}
		result += ")->" + outputType;
		return result;
	}

}
