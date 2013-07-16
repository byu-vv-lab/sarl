package edu.udel.cis.vsl.sarl.expr.common;

import edu.udel.cis.vsl.sarl.IF.expr.ArrayElementReference;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;

public class CommonArrayElementReference extends CommonNTReference implements
		ArrayElementReference {

	public CommonArrayElementReference(SymbolicType referenceType,
			SymbolicConstant arrayElementReferenceFunction,
			SymbolicSequence<SymbolicExpression> parentIndexSequence) {
		super(referenceType, arrayElementReferenceFunction, parentIndexSequence);
	}

	@Override
	public NumericExpression getIndex() {
		return getIndexExpression();
	}

	@Override
	public boolean isArrayElementReference() {
		return true;
	}

	@Override
	public ReferenceKind referenceKind() {
		return ReferenceKind.ARRAY_ELEMENT;
	}
}
