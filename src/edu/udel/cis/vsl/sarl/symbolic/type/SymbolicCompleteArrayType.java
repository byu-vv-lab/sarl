package edu.udel.cis.vsl.sarl.symbolic.type;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;

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
	protected int computeHashCode() {
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
