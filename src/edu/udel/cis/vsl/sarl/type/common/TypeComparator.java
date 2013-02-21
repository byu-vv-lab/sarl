package edu.udel.cis.vsl.sarl.type.common;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;

public class TypeComparator implements Comparator<SymbolicType> {

	private Comparator<SymbolicTypeSequence> typeSequenceComparator;

	private Comparator<SymbolicExpression> expressionComparator;

	public TypeComparator() {

	}

	public void setTypeSequenceComparator(Comparator<SymbolicTypeSequence> c) {
		typeSequenceComparator = c;
	}

	public void setExpressionComparator(Comparator<SymbolicExpression> c) {
		expressionComparator = c;
	}

	public Comparator<SymbolicExpression> expressionComparator() {
		return expressionComparator;
	}

	@Override
	public int compare(SymbolicType o1, SymbolicType o2) {
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
			CommonSymbolicArrayType t1 = (CommonSymbolicArrayType) o1;
			CommonSymbolicArrayType t2 = (CommonSymbolicArrayType) o2;

			result = compare(t1.elementType(), t2.elementType());
			if (result != 0)
				return result;
			else {
				if (t1.isComplete())
					return t2.isComplete() ? expressionComparator.compare(
							((CommonSymbolicCompleteArrayType) t1).extent(),
							((CommonSymbolicCompleteArrayType) t2).extent()) : -1;
				else
					return t2.isComplete() ? 1 : 0;
			}
		}
		case FUNCTION: {
			CommonSymbolicFunctionType t1 = (CommonSymbolicFunctionType) o1;
			CommonSymbolicFunctionType t2 = (CommonSymbolicFunctionType) o2;

			result = typeSequenceComparator.compare(t1.inputTypes(),
					t2.inputTypes());
			if (result != 0)
				return result;
			return compare(t1.outputType(), t2.outputType());
		}
		case TUPLE: {
			CommonSymbolicTupleType t1 = (CommonSymbolicTupleType) o1;
			CommonSymbolicTupleType t2 = (CommonSymbolicTupleType) o2;

			result = t1.name().compareTo(t2.name());
			if (result != 0)
				return result;
			return typeSequenceComparator.compare(t1.sequence(), t2.sequence());
		}
		case UNION: {
			CommonSymbolicUnionType t1 = (CommonSymbolicUnionType) o1;
			CommonSymbolicUnionType t2 = (CommonSymbolicUnionType) o2;

			result = t1.name().compareTo(t2.name());
			if (result != 0)
				return result;
			return typeSequenceComparator.compare(t1.sequence(), t2.sequence());
		}
		default:
			throw new SARLInternalException("unreachable");
		}
	}
}
