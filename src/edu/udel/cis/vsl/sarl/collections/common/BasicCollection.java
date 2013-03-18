package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Collection;
import java.util.Iterator;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class BasicCollection<T extends SymbolicExpression> extends
		CommonSymbolicCollection<T> {

	private static int classCode = BasicCollection.class.hashCode();

	private Collection<T> javaCollection;

	public BasicCollection(Collection<T> javaCollection) {
		super(SymbolicCollectionKind.BASIC);
		this.javaCollection = javaCollection;
	}

	@Override
	public int size() {
		return javaCollection.size();
	}

	@Override
	public Iterator<T> iterator() {
		return javaCollection.iterator();
	}

	@Override
	protected boolean collectionEquals(SymbolicCollection<T> o) {
		return javaCollection.equals(((BasicCollection<T>) o).javaCollection);
	}

	@Override
	protected int computeHashCode() {
		return classCode ^ javaCollection.hashCode();
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		throw new SARLInternalException(
				"canonization not implemented in BasicCollection");
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		StringBuffer result = new StringBuffer("{");
		boolean first = true;

		for (T element : this) {
			if (first)
				first = false;
			else
				result.append(", ");
			result.append(element.toStringBuffer(false));
		}
		result.append("}");
		return result;
	}

	@Override
	public StringBuffer toStringBufferLong() {
		StringBuffer result = new StringBuffer("Collection");

		result.append(toStringBuffer(true));
		return result;
	}

}
