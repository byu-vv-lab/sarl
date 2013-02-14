package edu.udel.cis.vsl.sarl.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Simple implementation of Iterator for iterating over a sequence consisting of
 * a single element.
 * 
 * @author siegel
 * 
 * @param <E>
 *            type of the element
 */
public class SingletonIterator<E> implements Iterator<E> {

	private E thing;

	private boolean hasNext = true;

	public SingletonIterator(E thing) {
		this.thing = thing;
	}

	@Override
	public boolean hasNext() {
		return hasNext;
	}

	@Override
	public E next() {
		if (hasNext) {
			hasNext = false;
			return thing;
		}
		throw new NoSuchElementException();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
