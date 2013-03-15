package edu.udel.cis.vsl.sarl.type.common;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class CommonSymbolicRealType extends CommonSymbolicType implements
		SymbolicRealType {

	public final static int classCode = CommonSymbolicRealType.class.hashCode();

	private RealKind realKind;

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
	public String toString() {
		return realKind.toString();
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
