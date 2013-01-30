package edu.udel.cis.vsl.sarl.symbolic.type;

import edu.udel.cis.vsl.sarl.IF.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequenceIF;

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
	protected boolean intrinsicEquals(SymbolicType thatType) {
		SymbolicTupleType that = (SymbolicTupleType) thatType;

		return name.equals(that.name) && sequence.equals(that.sequence);
	}

	@Override
	protected int computeHashCode() {
		return kind().hashCode() ^ name.hashCode() ^ sequence.hashCode();
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
	protected int intrinsicCompare(SymbolicType thatType) {
		SymbolicTupleType that = (SymbolicTupleType) thatType;
		int result = name.compareTo(that.name);

		if (result != 0)
			return result;
		return sequence.compareTo(that.sequence);
	}

}
