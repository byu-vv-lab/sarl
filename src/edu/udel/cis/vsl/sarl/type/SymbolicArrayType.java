package edu.udel.cis.vsl.sarl.type;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.object.ObjectFactory;

public class SymbolicArrayType extends SymbolicType implements
		SymbolicArrayTypeIF {

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

	/**
	 * Both this and that have kind ARRAY. However, neither, either or both may
	 * be complete.
	 */
	@Override
	protected boolean typeEquals(SymbolicType that) {
		if (!elementType.equals(((SymbolicArrayType) that).elementType))
			return false;
		if (isComplete()) {
			if (((SymbolicArrayType) that).isComplete()) {
				return ((SymbolicCompleteArrayType) this).extent().equals(
						((SymbolicCompleteArrayType) that).extent());
			}
			return false;
		} else {
			return !((SymbolicArrayType) that).isComplete();
		}
	}

	@Override
	protected int computeHashCode() {
		return SymbolicTypeKind.ARRAY.hashCode() ^ elementType.hashCode();
	}

	@Override
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
	@Override
	public String toString() {
		String result = "";
		SymbolicTypeIF type;

		for (type = this; type instanceof SymbolicArrayType; type = ((SymbolicArrayType) type)
				.elementType())
			result += ((SymbolicArrayType) type).extentString();
		return type + result;
	}

	@Override
	public boolean isComplete() {
		return false;
	}

	@Override
	public void canonizeChildren(ObjectFactory factory) {
		if (!elementType.isCanonic())
			elementType = factory.canonic(elementType);
	}

}
