package edu.udel.cis.vsl.sarl.type;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequenceIF;

public class TypeComparator implements Comparator<SymbolicTypeIF> {

	private Comparator<SymbolicTypeSequenceIF> typeSequenceComparator;

	private Comparator<SymbolicExpressionIF> expressionComparator;

	private Comparator<SymbolicObject> objectComparator;

	public TypeComparator() {

	}

	public void setObjectComparator(Comparator<SymbolicObject> c) {
		objectComparator = c;
	}

	public void setTypeSequenceComparator(Comparator<SymbolicTypeSequenceIF> c) {
		typeSequenceComparator = c;
	}

	public void setExpressionComparator(Comparator<SymbolicExpressionIF> c) {
		expressionComparator = c;
	}

	@Override
	public int compare(SymbolicTypeIF o1, SymbolicTypeIF o2) {
		SymbolicTypeKind kind = o1.typeKind();
		int result = kind.compareTo(o2.typeKind());

		if (result != 0)
			return result;
		switch (kind) {
		case BOOLEAN:
		case INTEGER:
		case REAL:
			return 0;
		case ARRAY: {
			SymbolicArrayType t1 = (SymbolicArrayType) o1;
			SymbolicArrayType t2 = (SymbolicArrayType) o2;

			result = compare(t1.elementType(), t2.elementType());
			if (result != 0)
				return result;
			else {
				if (t1.isComplete())
					return t2.isComplete() ? expressionComparator.compare(
							((SymbolicCompleteArrayType) t1).extent(),
							((SymbolicCompleteArrayType) t2).extent()) : -1;
				else
					return t2.isComplete() ? 1 : 0;
			}
		}
		case FUNCTION: {
			SymbolicFunctionType t1 = (SymbolicFunctionType) o1;
			SymbolicFunctionType t2 = (SymbolicFunctionType) o2;

			result = typeSequenceComparator.compare(t1.inputTypes(),
					t2.inputTypes());
			if (result != 0)
				return result;
			return compare(t1.outputType(), t2.outputType());
		}
		case TUPLE: {
			SymbolicTupleType t1 = (SymbolicTupleType) o1;
			SymbolicTupleType t2 = (SymbolicTupleType) o2;

			result = objectComparator.compare(t1.name(), t2.name());
			if (result != 0)
				return result;
			return typeSequenceComparator.compare(t1.sequence(), t2.sequence());
		}
		case UNION: {
			SymbolicUnionType t1 = (SymbolicUnionType) o1;
			SymbolicUnionType t2 = (SymbolicUnionType) o2;

			result = objectComparator.compare(t1.name(), t2.name());
			if (result != 0)
				return result;
			return typeSequenceComparator.compare(t1.sequence(), t2.sequence());
		}
		default:
			throw new SARLInternalException("unreachable");
		}
	}
}
