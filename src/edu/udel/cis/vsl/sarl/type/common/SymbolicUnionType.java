package edu.udel.cis.vsl.sarl.type.common;

import java.util.LinkedHashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequenceIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionTypeIF;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class SymbolicUnionType extends SymbolicType implements
		SymbolicUnionTypeIF {

	private SymbolicTypeSequenceIF sequence;

	private StringObject name;

	private Map<SymbolicTypeIF, Integer> indexMap = new LinkedHashMap<SymbolicTypeIF, Integer>();

	/**
	 * The elements of the sequence must be unique, i.e., no repetitions.
	 * 
	 * @param name
	 * @param sequence
	 */
	SymbolicUnionType(StringObject name, SymbolicTypeSequenceIF sequence) {
		super(SymbolicTypeKind.TUPLE);
		assert sequence != null;

		int n = sequence.numTypes();

		for (int i = 0; i < n; i++) {
			SymbolicTypeIF type1 = sequence.getType(i);
			Integer index = indexMap.get(type1);

			if (index != null)
				throw new IllegalArgumentException("Component of union type "
						+ name + " occurred twice, at positions " + index
						+ " and " + i + ": " + type1);
			indexMap.put(type1, i);
		}
		assert name != null;
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

	@Override
	public Integer indexOfType(SymbolicTypeIF type) {
		return indexMap.get(type);
	}
}
