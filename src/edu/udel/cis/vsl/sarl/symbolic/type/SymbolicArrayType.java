package edu.udel.cis.vsl.sarl.symbolic.type;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;

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

	/**
	 * Both this and that have kind ARRAY. However, neither, either or both may
	 * be complete.
	 */
	@Override
	protected boolean intrinsicEquals(SymbolicType that) {
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
		return kind().hashCode() + elementType.hashCode();
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

	/**
	 * Can assume that has kind ARRAY. Order: first compare element types. Among
	 * those with same element type, all completes come first, then incompletes.
	 * Among completes with same element type, order by extent.
	 */
	@Override
	protected int intrinsicCompare(SymbolicType that) {
		int result = elementType
				.compareTo(((SymbolicArrayType) that).elementType);

		if (result != 0)
			return result;
		else {
			boolean thisComplete = isComplete();
			boolean thatComplete = ((SymbolicArrayType) that).isComplete();

			if (thisComplete) {
				if (thatComplete) {
					return ((SymbolicCompleteArrayType) this)
							.extent()
							.compareTo(
									((SymbolicCompleteArrayType) that).extent());
				}
				return -1; // this complete; that not
			} else {
				if (thatComplete)
					return 1; // this not complete, that is
				return 0; // both incomplete
			}
		}
	}

	@Override
	public boolean isComplete() {
		return false;
	}

}
