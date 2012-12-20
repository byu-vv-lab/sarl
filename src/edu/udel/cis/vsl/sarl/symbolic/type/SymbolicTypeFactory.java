package edu.udel.cis.vsl.sarl.symbolic.type;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import edu.udel.cis.vsl.sarl.symbolic.IF.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF.SymbolicTypeKind;

public class SymbolicTypeFactory {

	private int count = 0;

	private SymbolicPrimitiveType booleanType, integerType, realType;

	private Map<SymbolicTypeKey, SymbolicType> map = new LinkedHashMap<SymbolicTypeKey, SymbolicType>();

	private Vector<SymbolicType> typeVector = new Vector<SymbolicType>();

	public SymbolicTypeFactory() {
		booleanType = (SymbolicPrimitiveType) canonicalize(new SymbolicPrimitiveType(
				SymbolicTypeKind.BOOLEAN));
		integerType = (SymbolicPrimitiveType) canonicalize(new SymbolicPrimitiveType(
				SymbolicTypeKind.INTEGER));
		realType = (SymbolicPrimitiveType) canonicalize(new SymbolicPrimitiveType(
				SymbolicTypeKind.REAL));
	}

	private SymbolicType canonicalize(SymbolicType type) {
		assert type != null;

		SymbolicTypeKey key = new SymbolicTypeKey(type);
		SymbolicType old = map.get(key);

		if (old != null) {
			return old;
		} else {
			type.setId(count);
			count++;
			map.put(key, type);
			typeVector.add(type);
			return type;
		}
	}

	public int numTypes() {
		return count;
	}

	public SymbolicType typeWithId(int id) {
		return typeVector.elementAt(id);

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

	public SymbolicArrayType arrayType(SymbolicTypeIF elementType) {
		return (SymbolicArrayType) canonicalize(new SymbolicArrayType(
				elementType));
	}

	public SymbolicCompleteArrayType arrayType(SymbolicTypeIF elementType,
			SymbolicExpressionIF extent) {
		return (SymbolicCompleteArrayType) canonicalize(new SymbolicCompleteArrayType(
				elementType, extent));
	}

	public SymbolicTupleType tupleType(String name, SymbolicTypeIF[] fieldTypes) {
		return (SymbolicTupleType) canonicalize(new SymbolicTupleType(name,
				fieldTypes));
	}

	public SymbolicFunctionType functionType(SymbolicTypeIF[] inputTypes,
			SymbolicTypeIF outputType) {
		return (SymbolicFunctionType) canonicalize(new SymbolicFunctionType(
				inputTypes, outputType));
	}

}
