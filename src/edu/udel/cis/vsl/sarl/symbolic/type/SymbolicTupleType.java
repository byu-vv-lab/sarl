package edu.udel.cis.vsl.sarl.symbolic.type;

import java.util.Arrays;

import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTupleTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF;

public class SymbolicTupleType extends SymbolicType implements
		SymbolicTupleTypeIF {

	private SymbolicTypeIF[] fieldTypes;

	private String name;

	SymbolicTupleType(String name, SymbolicTypeIF[] fieldTypes) {
		super(SymbolicTypeKind.TUPLE);
		assert name != null;
		assert fieldTypes != null;

		int numFields = fieldTypes.length;

		this.name = name;
		this.fieldTypes = new SymbolicTypeIF[numFields];
		for (int i = 0; i < numFields; i++) {
			assert fieldTypes[i] != null;
			this.fieldTypes[i] = fieldTypes[i];
		}
	}

	@Override
	boolean intrinsicEquals(SymbolicType that) {
		if (that instanceof SymbolicTupleType) {
			SymbolicTupleType thatType = (SymbolicTupleType) that;

			return name.equals(thatType.name)
					&& Arrays.equals(fieldTypes, thatType.fieldTypes);
		}
		return false;
	}

	@Override
	int intrinsicHashCode() {
		return SymbolicTupleType.class.hashCode() + name.hashCode()
				+ Arrays.hashCode(fieldTypes);
	}

	public SymbolicTypeIF fieldType(int index) {
		return fieldTypes[index];
	}

	public int numFields() {
		return fieldTypes.length;
	}

	public String toString() {
		String result = name + "<";
		int numFields = numFields();

		for (int i = 0; i < numFields; i++) {
			if (i > 0)
				result += ",";
			result += fieldType(i);
		}
		result += ">";
		return result;
	}

	public String name() {
		return name;
	}

}
