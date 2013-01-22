package edu.udel.cis.vsl.sarl.symbolic.type;

import java.util.ArrayList;
import java.util.Iterator;

import edu.udel.cis.vsl.sarl.IF.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicTypeSequenceIF;

public class SymbolicTypeSequence implements SymbolicTypeSequenceIF {

	private ArrayList<SymbolicTypeIF> elements;

	SymbolicTypeSequence(Iterable<SymbolicTypeIF> types) {
		elements = new ArrayList<SymbolicTypeIF>();
		for (SymbolicTypeIF type : types) {
			elements.add(type);
		}
	}

	SymbolicTypeSequence(SymbolicTypeIF[] types) {
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
	public boolean equals(Object object) {
		if (object instanceof SymbolicTypeSequence) {
			return elements.equals(((SymbolicTypeSequence) object).elements);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return elements.hashCode();
	}

	@Override
	public int compareTo(SymbolicTypeSequenceIF o) {
		int size = elements.size();
		int result = size - o.numTypes();

		if (result != 0)
			return result;
		for (int i = 0; i < size; i++) {
			result = getType(i).compareTo(o.getType(i));
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

}
