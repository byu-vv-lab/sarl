package edu.udel.cis.vsl.sarl.symbolic.type;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.StringObject;
import edu.udel.cis.vsl.sarl.IF.SymbolicArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicCompleteArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicFunctionTypeIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicTupleTypeIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicTypeIF.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.SymbolicTypeSequenceIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicUnionTypeIF;

public class SymbolicTypeFactory {

	private SymbolicPrimitiveType booleanType, integerType, realType;

	private Map<SymbolicTypeIF, SymbolicTypeIF> map = new LinkedHashMap<SymbolicTypeIF, SymbolicTypeIF>();

	private Map<SymbolicTypeSequenceIF, SymbolicTypeSequenceIF> sequenceMap = new LinkedHashMap<SymbolicTypeSequenceIF, SymbolicTypeSequenceIF>();

	private ArrayList<SymbolicTypeIF> typeVector = new ArrayList<SymbolicTypeIF>();

	public SymbolicTypeFactory() {
		booleanType = (SymbolicPrimitiveType) canonicalize(new SymbolicPrimitiveType(
				SymbolicTypeKind.BOOLEAN));
		integerType = (SymbolicPrimitiveType) canonicalize(new SymbolicPrimitiveType(
				SymbolicTypeKind.INTEGER));
		realType = (SymbolicPrimitiveType) canonicalize(new SymbolicPrimitiveType(
				SymbolicTypeKind.REAL));
	}

	private SymbolicTypeIF canonicalize(SymbolicTypeIF type) {
		assert type != null;

		SymbolicTypeIF old = map.get(type);

		if (old != null) {
			return old;
		} else {
			((SymbolicType) type).setId(typeVector.size());
			map.put(type, type);
			typeVector.add(type);
			return type;
		}
	}

	private SymbolicTypeSequenceIF canonicalize(SymbolicTypeSequenceIF sequence) {
		assert sequence != null;

		SymbolicTypeSequenceIF old = sequenceMap.get(sequence);

		if (old != null) {
			return old;
		} else {
			sequenceMap.put(sequence, sequence);
			return sequence;
		}
	}

	public int numTypes() {
		return typeVector.size();
	}

	public SymbolicTypeIF typeWithId(int id) {
		return typeVector.get(id);

	}

	public SymbolicPrimitiveType booleanType() {
		return booleanType;
	}

	public SymbolicPrimitiveType integerType() {
		return integerType;
	}

	public SymbolicPrimitiveType realType() {
		return realType;
	}

	public SymbolicTypeSequenceIF sequence(Iterable<SymbolicTypeIF> elements) {
		return canonicalize(new SymbolicTypeSequence(elements));
	}

	public SymbolicTypeSequenceIF sequence(SymbolicTypeIF[] elements) {
		return canonicalize(new SymbolicTypeSequence(elements));
	}

	public SymbolicArrayTypeIF arrayType(SymbolicTypeIF elementType) {
		return (SymbolicArrayTypeIF) canonicalize(new SymbolicArrayType(
				elementType));
	}

	public SymbolicCompleteArrayTypeIF arrayType(SymbolicTypeIF elementType,
			SymbolicExpressionIF extent) {
		return (SymbolicCompleteArrayTypeIF) canonicalize(new SymbolicCompleteArrayType(
				elementType, extent));
	}

	public SymbolicTupleTypeIF tupleType(StringObject name,
			SymbolicTypeSequenceIF fieldTypes) {
		return (SymbolicTupleTypeIF) canonicalize(new SymbolicTupleType(name,
				fieldTypes));
	}

	public SymbolicUnionTypeIF unionType(StringObject name,
			SymbolicTypeSequenceIF memberTypes) {
		return (SymbolicUnionTypeIF) canonicalize(new SymbolicUnionType(name,
				memberTypes));
	}

	public SymbolicFunctionTypeIF functionType(SymbolicTypeSequence inputTypes,
			SymbolicTypeIF outputType) {
		return (SymbolicFunctionTypeIF) canonicalize(new SymbolicFunctionType(
				inputTypes, outputType));
	}

}
