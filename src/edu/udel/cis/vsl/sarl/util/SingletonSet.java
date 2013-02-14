package edu.udel.cis.vsl.sarl.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;

public class SingletonSet<E> implements Set<E> {

	private E theElement;

	public SingletonSet(E element) {
		theElement = element;
		assert element != null;
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean contains(Object o) {
		return theElement.equals(o);
	}

	@Override
	public Iterator<E> iterator() {
		return new SingletonIterator<E>(theElement);
	}

	@Override
	public Object[] toArray() {
		return new Object[] { theElement };
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO
		throw new UnsupportedOperationException("Not sure what this does");
	}

	@Override
	public boolean add(E e) {
		throw new SARLInternalException("SingletonSet is immutable");
	}

	@Override
	public boolean remove(Object o) {
		throw new SARLInternalException("SingletonSet is immutable");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		if (c.size() == 0)
			return true;
		else if (c.size() == 1)
			return theElement.equals(c.iterator().next());
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new SARLInternalException("SingletonSet is immutable");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new SARLInternalException("SingletonSet is immutable");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new SARLInternalException("SingletonSet is immutable");
	}

	@Override
	public void clear() {
		throw new SARLInternalException("SingletonSet is immutable");
	}

}
