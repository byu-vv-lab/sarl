package edu.udel.cis.vsl.sarl.expr.common;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.OffsetReference;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;

public class CommonOffsetReference extends CommonNTReference implements
		OffsetReference {

	public CommonOffsetReference(SymbolicType referenceType,
			SymbolicConstant offsetReferenceFunction,
			SymbolicSequence<SymbolicExpression> parentIndexSequence) {
		super(referenceType, offsetReferenceFunction, parentIndexSequence);
	}

	@Override
	public NumericExpression getOffset() {
		return getIndexExpression();
	}

	@Override
	public boolean isOffsetReference() {
		return true;
	}

	@Override
	public ReferenceKind referenceKind() {
		return ReferenceKind.OFFSET;
	}
}
