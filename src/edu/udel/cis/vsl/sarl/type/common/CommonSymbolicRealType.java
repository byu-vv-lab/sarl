package edu.udel.cis.vsl.sarl.type.common;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class CommonSymbolicRealType extends CommonSymbolicType implements
		SymbolicRealType {

	private final static int classCode = CommonSymbolicRealType.class
			.hashCode();

	private RealKind realKind;

	private StringBuffer name;

	public CommonSymbolicRealType(RealKind kind) {
		super(SymbolicTypeKind.REAL);
		this.realKind = kind;
	}

	@Override
	public RealKind realKind() {
		return realKind;
	}

	@Override
	protected boolean typeEquals(CommonSymbolicType that) {
		return realKind == ((CommonSymbolicRealType) that).realKind;
	}

	@Override
	protected int computeHashCode() {
		return classCode ^ realKind.hashCode();
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		if (name == null) {
			String shortName;

			switch (realKind) {
			case IDEAL:
				shortName = "real";
				break;
			case HERBRAND:
				shortName = "hreal";
				break;
			case FLOAT:
				shortName = "float";
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
		return realKind == RealKind.HERBRAND;
	}

	@Override
	public boolean isIdeal() {
		return realKind == RealKind.IDEAL;
	}
}
