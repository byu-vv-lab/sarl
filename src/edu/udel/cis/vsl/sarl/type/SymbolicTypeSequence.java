package edu.udel.cis.vsl.sarl.type;

import java.util.ArrayList;
import java.util.Iterator;

import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequenceIF;
import edu.udel.cis.vsl.sarl.object.CommonSymbolicObject;
import edu.udel.cis.vsl.sarl.object.ObjectFactory;

public class SymbolicTypeSequence extends CommonSymbolicObject implements
		SymbolicTypeSequenceIF {

	private ArrayList<SymbolicTypeIF> elements;

	SymbolicTypeSequence(Iterable<SymbolicTypeIF> types) {
		super(SymbolicObjectKind.TYPE_SEQUENCE);
		elements = new ArrayList<SymbolicTypeIF>();
		for (SymbolicTypeIF type : types) {
			elements.add(type);
		}
	}

	SymbolicTypeSequence(SymbolicTypeIF[] types) {
		super(SymbolicObjectKind.TYPE_SEQUENCE);
		elements = new ArrayList<SymbolicTypeIF>(types.length);
		for (SymbolicTypeIF type : types) {
			elements.add(type);
		}
	}

	@Override
	public Iterator<SymbolicTypeIF> iterator() {
		return elements.iterator();
	}

	@Override
	public int numTypes() {
		return elements.size();
	}

	@Override
	public SymbolicTypeIF getType(int index) {
		return elements.get(index);
	}

	@Override
	protected boolean intrinsicEquals(SymbolicObject object) {
		if (object instanceof SymbolicTypeSequence) {
			return elements.equals(((SymbolicTypeSequence) object).elements);
		}
		return false;
	}

	@Override
	public int compareLocal(SymbolicObject o) {
		SymbolicTypeSequence that = (SymbolicTypeSequence) o;
		int size = elements.size();
		int result = size - that.numTypes();

		if (result != 0)
			return result;
		for (int i = 0; i < size; i++) {
			result = getType(i).compareTo(that.getType(i));
			if (result != 0)
				return result;
		}
		return 0;
	}

	@Override
	public String toString() {
		String result = "<";
		int n = numTypes();

		for (int i = 0; i < n; i++) {
			if (i > 0)
				result += ",";
			result += getType(i);
		}
		result += ">";
		return result;
	}

	@Override
	protected int computeHashCode() {
		return SymbolicObjectKind.TYPE_SEQUENCE.hashCode()
				^ elements.hashCode();
	}

	@Override
	public void canonizeChildren(ObjectFactory factory) {
		int numElements = elements.size();

		for (int i = 0; i < numElements; i++) {
			SymbolicTypeIF type = elements.get(i);

			if (!type.isCanonic())
				elements.set(i, factory.canonic(type));
		}
	}

}
