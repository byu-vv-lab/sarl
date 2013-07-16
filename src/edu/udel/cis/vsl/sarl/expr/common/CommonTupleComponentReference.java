package edu.udel.cis.vsl.sarl.expr.common;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.TupleComponentReference;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;

public class CommonTupleComponentReference extends CommonNTReference implements
		TupleComponentReference {

	/**
	 * The fieldIndex duplicated the information in one of the arguments, but
	 * there is no obvious way to translate from a NumberObject to an IntObject
	 * so cache it here.
	 */
	private IntObject fieldIndex;

	public CommonTupleComponentReference(SymbolicType referenceType,
			SymbolicConstant tupleComponentReferenceFunction,
			SymbolicSequence<SymbolicExpression> parentIndexSequence,
			IntObject fieldIndex) {
		super(referenceType, tupleComponentReferenceFunction,
				parentIndexSequence);
		assert parentIndexSequence.get(1).operator() == SymbolicOperator.CONCRETE
				&& parentIndexSequence.get(1).argument(0) instanceof NumberObject
				&& ((IntegerNumber) ((NumberObject) parentIndexSequence.get(1)
						.argument(0)).getNumber()).intValue() == fieldIndex
						.getInt();
		this.fieldIndex = fieldIndex;
	}

	@Override
	public IntObject getIndex() {
		return fieldIndex;
	}

	@Override
	public boolean isTupleComponentReference() {
		return true;
	}

	@Override
	public ReferenceKind referenceKind() {
		return ReferenceKind.TUPLE_COMPONENT;
	}
}
