package edu.udel.cis.vsl.sarl.symbolic.type;

import edu.udel.cis.vsl.sarl.symbolic.IF.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicCompleteArrayTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF;

public class SymbolicCompleteArrayType extends SymbolicArrayType implements
		SymbolicCompleteArrayTypeIF {

	private SymbolicExpressionIF extent;

	SymbolicCompleteArrayType(SymbolicTypeIF elementType,
			SymbolicExpressionIF extent) {
		super(elementType);
		assert extent != null;
		this.extent = extent;
	}

	@Override
	boolean intrinsicEquals(SymbolicType that) {
		if (that instanceof SymbolicCompleteArrayType) {
			SymbolicCompleteArrayType arrayType = (SymbolicCompleteArrayType) that;

			return super.equals(arrayType) && extent.equals(arrayType.extent);
		}
		return false;
	}

	@Override
	int intrinsicHashCode() {
		return super.hashCode() + extent.hashCode();
	}

	@Override
	public String extentString() {
		return "[" + extent + "]";
	}

	@Override
	public SymbolicExpressionIF extent() {
		return extent;
	}

}
