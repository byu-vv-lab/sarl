package edu.udel.cis.vsl.sarl.type.common;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class CommonSymbolicIntegerType extends CommonSymbolicType implements
		SymbolicIntegerType {

	private final static int classCode = CommonSymbolicIntegerType.class
			.hashCode();

	private IntegerKind integerKind;

	private StringBuffer name;

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
	public StringBuffer toStringBuffer(boolean atomize) {
		if (name == null) {
			String shortName;

			switch (integerKind) {
			case IDEAL:
				shortName = "int";
				break;
			case HERBRAND:
				shortName = "hint";
				break;
			case BOUNDED:
				shortName = "bounded";
				break;
			default:
				throw new SARLInternalException("unreachable");
			}
			name = new StringBuffer(shortName);
		}
		return name;
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
