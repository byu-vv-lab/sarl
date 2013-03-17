package edu.udel.cis.vsl.sarl.type.common;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class CommonSymbolicArrayType extends CommonSymbolicType implements
		SymbolicArrayType {

	private final static int classCode = CommonSymbolicArrayType.class
			.hashCode();

	private SymbolicType elementType;

	/**
	 * Creates new symbolic array type with given elementType. *
	 * 
	 * @param elementType
	 *            any non-null type
	 */
	CommonSymbolicArrayType(SymbolicType elementType) {
		super(SymbolicTypeKind.ARRAY);
		assert elementType != null;
		this.elementType = elementType;
	}

	/**
	 * Both this and that have kind ARRAY. However, neither, either or both may
	 * be complete.
	 */
	@Override
	protected boolean typeEquals(CommonSymbolicType that) {
		if (!elementType.equals(((CommonSymbolicArrayType) that).elementType))
			return false;
		if (isComplete()) {
			if (((CommonSymbolicArrayType) that).isComplete()) {
				return ((CommonSymbolicCompleteArrayType) this).extent()
						.equals(((CommonSymbolicCompleteArrayType) that)
								.extent());
			}
			return false;
		} else {
			return !((CommonSymbolicArrayType) that).isComplete();
		}
	}

	@Override
	protected int computeHashCode() {
		return classCode ^ elementType.hashCode();
	}

	@Override
	public SymbolicType elementType() {
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
	@Override
	public String toString() {
		String result = "";
		SymbolicType type;

		for (type = this; type instanceof CommonSymbolicArrayType; type = ((CommonSymbolicArrayType) type)
				.elementType())
			result += ((CommonSymbolicArrayType) type).extentString();
		return type + result;
	}

	@Override
	public boolean isComplete() {
		return false;
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		if (!elementType.isCanonic())
			elementType = factory.canonic(elementType);
	}

}
