package edu.udel.cis.vsl.sarl.type.common;

import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequenceIF;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class SymbolicTupleType extends SymbolicType implements
		SymbolicTupleTypeIF {

	private SymbolicTypeSequenceIF sequence;

	private StringObject name;

	SymbolicTupleType(StringObject name, SymbolicTypeSequenceIF sequence) {
		super(SymbolicTypeKind.TUPLE);
		assert name != null;
		assert sequence != null;
		this.name = name;
		this.sequence = sequence;
	}

	@Override
	protected boolean typeEquals(SymbolicType thatType) {
		SymbolicTupleType that = (SymbolicTupleType) thatType;

		return name.equals(that.name) && sequence.equals(that.sequence);
	}

	@Override
	protected int computeHashCode() {
		return SymbolicTypeKind.TUPLE.hashCode() ^ name.hashCode()
				^ sequence.hashCode();
	}

	@Override
	public String toString() {
		return name.toString() + sequence;
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
			sequence = (SymbolicTypeSequenceIF) factory.canonic(sequence);
		if (!name.isCanonic())
			name = (StringObject) factory.canonic(name);
	}

}
