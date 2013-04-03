package edu.udel.cis.vsl.sarl.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class EmptyIterator<E> implements Iterator<E> {

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public E next() {
		throw new NoSuchElementException();
	}

	@Override
	public void remove() {
		throw new IllegalStateException();
	}

	public String toString() {
		return "{}";
	}

}
