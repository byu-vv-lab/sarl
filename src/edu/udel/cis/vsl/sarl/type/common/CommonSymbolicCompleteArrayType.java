package edu.udel.cis.vsl.sarl.type.common;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class CommonSymbolicCompleteArrayType extends CommonSymbolicArrayType
		implements SymbolicCompleteArrayType {

	private NumericExpression extent;

	CommonSymbolicCompleteArrayType(SymbolicType elementType,
			NumericExpression extent) {
		super(elementType);
		assert extent != null;
		this.extent = extent;
	}

	@Override
	protected int computeHashCode() {
		return super.computeHashCode() ^ extent.hashCode();
	}

	@Override
	public String extentString() {
		return "[" + extent + "]";
	}

	@Override
	public NumericExpression extent() {
		return extent;
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		super.canonizeChildren(factory);
		if (!extent.isCanonic())
			extent = factory.canonic(extent);
	}

	@Override
	public boolean isComplete() {
		return true;
	}

}
