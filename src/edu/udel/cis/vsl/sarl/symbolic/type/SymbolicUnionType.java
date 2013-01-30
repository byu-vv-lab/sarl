package edu.udel.cis.vsl.sarl.symbolic.type;

import edu.udel.cis.vsl.sarl.IF.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequenceIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionTypeIF;

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
	protected boolean intrinsicEquals(SymbolicType thatType) {
		SymbolicUnionType that = (SymbolicUnionType) thatType;

		return name.equals(that.name) && sequence.equals(that.sequence);
	}

	@Override
	protected int computeHashCode() {
		return kind().hashCode() ^ name.hashCode() ^ sequence.hashCode();
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
	protected int intrinsicCompare(SymbolicType thatType) {
		SymbolicUnionType that = (SymbolicUnionType) thatType;
		int result = name.compareTo(that.name);

		if (result != 0)
			return result;
		return sequence.compareTo(that.sequence);
	}
}
