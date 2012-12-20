package edu.udel.cis.vsl.sarl.symbolic.type;

import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicArrayTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF;

public class SymbolicArrayType extends SymbolicType implements
		SymbolicArrayTypeIF {

	public static final int classHashCode = SymbolicArrayType.class.hashCode();

	private SymbolicTypeIF elementType;

	/**
	 * Creates new symbolic array type with given elementType. *
	 * 
	 * @param elementType
	 *            any non-null type
	 */
	SymbolicArrayType(SymbolicTypeIF elementType) {
		super(SymbolicTypeKind.ARRAY);
		assert elementType != null;
		this.elementType = elementType;
	}

	@Override
	boolean intrinsicEquals(SymbolicType that) {
		if (that instanceof SymbolicArrayType) {
			SymbolicArrayType arrayType = (SymbolicArrayType) that;

			return elementType.equals(arrayType.elementType);
		}
		return false;
	}

	@Override
	int intrinsicHashCode() {
		return classHashCode + elementType.hashCode();
	}

	public SymbolicTypeIF elementType() {
		return elementType;
	}

	/**
	 * Used by toString() method. Complete array type subclass can override this
	 * by putting the extent in the brackets.
	 * 
	 * @return "[]"
	 */
	public String extentString() {
		return "[]";
	}

	/**
	 * Nice human-readable representation of the array type. Example
	 * "int[2][][4]". elementType: "int[][4]". extent: 2.
	 */
	public String toString() {
		String result = "";
		SymbolicTypeIF type;

		for (type = this; type instanceof SymbolicArrayType; type = ((SymbolicArrayType) type)
				.elementType())
			result += ((SymbolicArrayType) type).extentString();
		return type + result;
	}

}
