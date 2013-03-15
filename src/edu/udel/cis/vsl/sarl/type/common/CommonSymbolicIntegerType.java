package edu.udel.cis.vsl.sarl.type.common;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class CommonSymbolicIntegerType extends CommonSymbolicType implements
		SymbolicIntegerType {

	public final static int classCode = CommonSymbolicIntegerType.class
			.hashCode();

	private IntegerKind integerKind;

	public CommonSymbolicIntegerType(IntegerKind kind) {
		super(SymbolicTypeKind.INTEGER);
		this.integerKind = kind;
	}

	@Override
	public IntegerKind integerKind() {
		return integerKind;
	}

	@Override
	protected boolean typeEquals(CommonSymbolicType that) {
		return integerKind == ((CommonSymbolicIntegerType) that).integerKind;
	}

	@Override
	protected int computeHashCode() {
		return classCode ^ integerKind.hashCode();
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
	}

	@Override
	public String toString() {
		return integerKind.toString();
	}

	@Override
	public boolean isHerbrand() {
		return integerKind == IntegerKind.HERBRAND;
	}

	@Override
	public boolean isIdeal() {
		return integerKind == IntegerKind.IDEAL;
	}

}
