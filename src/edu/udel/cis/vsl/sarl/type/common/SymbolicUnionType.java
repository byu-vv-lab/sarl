package edu.udel.cis.vsl.sarl.type.common;

import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequenceIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionTypeIF;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class SymbolicUnionType extends SymbolicType implements
		SymbolicUnionTypeIF {

	private SymbolicTypeSequenceIF sequence;

	private StringObject name;

	SymbolicUnionType(StringObject name, SymbolicTypeSequenceIF sequence) {
		super(SymbolicTypeKind.TUPLE);
		assert name != null;
		assert sequence != null;
		this.name = name;
		this.sequence = sequence;
	}

	@Override
	protected boolean typeEquals(SymbolicType thatType) {
		SymbolicUnionType that = (SymbolicUnionType) thatType;

		return name.equals(that.name) && sequence.equals(that.sequence);
	}

	@Override
	protected int computeHashCode() {
		return SymbolicTypeKind.UNION.hashCode() ^ name.hashCode()
				^ sequence.hashCode();
	}

	@Override
	public String toString() {
		return "Union[" + name + "," + sequence + "]";
	}

	@Override
	public StringObject name() {
		return name;
	}

	@Override
	public SymbolicTypeSequenceIF sequence() {
		return sequence;
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		if (!sequence.isCanonic())
			sequence = (SymbolicTypeSequence) factory.canonic(sequence);
		if (!name.isCanonic())
			name = (StringObject) factory.canonic(name);
	}
}
