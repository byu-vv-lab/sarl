package edu.udel.cis.vsl.sarl.type.common;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class CommonSymbolicCompleteArrayType extends CommonSymbolicArrayType implements
		SymbolicCompleteArrayType {

	private SymbolicExpression extent;

	CommonSymbolicCompleteArrayType(SymbolicType elementType,
			SymbolicExpression extent) {
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
	public SymbolicExpression extent() {
		return extent;
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		super.canonizeChildren(factory);
		if (!extent.isCanonic())
			extent = factory.canonic(extent);
	}

}
