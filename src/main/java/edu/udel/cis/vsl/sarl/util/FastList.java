package edu.udel.cis.vsl.sarl.util;

import java.io.PrintStream;

/**
 * <p>
 * An efficient doubly-linked list. What the standard Java collections are
 * missing is the ability to concatenate two lists in constant time. You can do
 * that here using method {@link #append(FastList)}.
 * </p>
 * 
 * <p>
 * This class uses class {@link FastNode} to represent the nodes in the list.
 * Each {@link FastNode} contains a reference to the contents of the node, and
 * references to the next and previous nodes.
 * </p>
 * 
 * <p>
 * There are methods to get the first and last nodes. The rest of the nodes of
 * the list can be obtained by the methods {@link FastNode#getNext()} and
 * {@link FastNode#getPrev()}.
 * </p>
 * 
 * @author siegel
 * 
 * @param T
 *            the type of the elements (contents)
 *
 */
public class FastList<T> {

	/**
	 * The first element of the list. May be null.
	 */
	private FastNode<T> first;

	/**
	 * The last element of the list. May be null.
	 */
	private FastNode<T> last;

	/**
	 * Constructs a new empty list.
	 */
	public FastList() {
		this.first = null;
		this.last = null;
	}

	/**
	 * Constructs new list with specified first and last elements. No checking
	 * is done. It is assumed that: if <code>first</code> is </code>null, so is
	 * <code>last</code>. If <code>first</code> is not <code>null</code>, then
	 * <code>first.prev</code> is <code>null</code>, <code>last</code> is not
	 * <code>null</code>, and <code>last.next</code> is <code>null</code>. And
	 * so on, satisfying all the usual well-formed properties of a doubly-linked
	 * list with first element <code>first</code> and last element
	 * <code>last</code>.
	 * 
	 * @param first
	 *            first element of the list
	 * @param last
	 *            last element of the list
	 */
	FastList(FastNode<T> first, FastNode<T> last) {
		this.first = first;
		this.last = last;
	}

	public FastList(@SuppressWarnings("unchecked") T... values) {
		int n = values.length;

		if (n == 0)
			return;

		FastNode<T> prev = new FastNode<T>(values[0]), curr = prev;

		first = prev;
		for (int i = 1; i < n; i++) {
			curr = new FastNode<T>(values[i]);
			curr.setPrev(prev);
			prev.setNext(curr);
			prev = curr;
		}
		last = curr;

	}

	public boolean isEmpty() {
		return first == null;
	}

	/**
	 * Returns the node containing the first element of the list.
	 * 
	 * @return the first node, or <code>null</code> is this list is empty
	 */
	public FastNode<T> getFirst() {
		return first;
	}

	/**
	 * Returns the node containing the last element of the list.
	 * 
	 * @return the last node, or <code>null</code> is this list is empty
	 */
	public FastNode<T> getLast() {
		return last;
	}

	/**
	 * Empties this list, i.e., sets the {@link #first} and {@link #last} fields
	 * to <code>null</code>.
	 */
	public void empty() {
		first = last = null;
	}

	/**
	 * Adds an element to the end of this list.
	 * 
	 * @param value
	 *            the value that will form the contents of the new node added to
	 *            the list
	 * @return the newly created and added node containing <code>value</code>,
	 *         which is now the last node in this list
	 */
	public FastNode<T> add(T value) {
		FastNode<T> node = new FastNode<T>(value);

		if (first == null) {
			first = last = node;
		} else {
			last.setNext(node);
			node.setPrev(last);
			last = node;
		}
		return node;
	}

	/**
	 * Adds a sequence of elements to the end (back) of this list. The elements
	 * will be appended to the back of this list in the order in which they
	 * occur. Hence if the original list is L, and the arguments are v0,v1, ...,
	 * the new list will have the form L,v0,v1,....
	 * 
	 * @param values
	 *            any non-null sequence of elements of T. The sequence may be
	 *            empty.
	 */
	public void addAll(@SuppressWarnings("unchecked") T... values) {
		int n = values.length;

		if (n == 0)
			return;

		FastNode<T> prev = new FastNode<T>(values[0]), curr = prev;

		if (first != null) {
			last.setNext(curr);
			curr.setPrev(last);
		} else {
			first = curr;
		}
		for (int i = 1; i < n; i++) {
			curr = new FastNode<T>(values[i]);
			curr.setPrev(prev);
			prev.setNext(curr);
			prev = curr;
		}
		last = curr;
	}

	/**
	 * Adds an element to the front of this list.
	 * 
	 * @param value
	 *            the value that will form the contents of the new node added to
	 *            the list
	 * @return the newly created and added node containing <code>value</code>
	 */
	public FastNode<T> addFront(T value) {
		FastNode<T> node = new FastNode<T>(value);

		if (first == null) {
			first = last = node;
		} else {
			first.setPrev(node);
			node.setNext(first);
			first = node;
		}
		return node;
	}

	/**
	 * Appends the given list to this one. The given list is emptied.
	 * 
	 * @param that
	 *            a non-null {@link FastList}. It may be empty, but it cannot be
	 *            <code>null</code>.
	 */
	public void append(FastList<T> that) {
		if (that.first != null) {
			if (first == null) {
				first = that.first;
			} else {
				last.setNext(that.first);
				that.first.setPrev(last);
			}
			last = that.last;
			that.empty();
		}
	}

	/**
	 * Prepends the given list to the front of this one. The given list is
	 * emptied.
	 * 
	 * @param that
	 *            a non-null {@link FastList}. It may be empty, but it cannot be
	 *            <code>null</code>.
	 */
	public void prepend(FastList<T> that) {
		if (that.first != null) {
			if (first == null) {
				last = that.last;
			} else {
				first.setPrev(that.last);
				that.last.setNext(first);
			}
			first = that.first;
			that.empty();
		}
	}

	/**
	 * Insert a new element in the list immediately after the specified place.
	 * 
	 * @param place
	 *            a non-null node in this list. If <code>place</code> is not a
	 *            node in this list, the behavior is undefined
	 * @param value
	 *            an element of T that will form the contents of the new node to
	 *            be inserted
	 * @return the newly created and inserted node containing <code>value</code>
	 */
	public FastNode<T> insertAfter(FastNode<T> place, T value) {
		FastNode<T> newNode = new FastNode<T>(value);
		FastNode<T> newNext = place.getNext();

		if (newNext == null) {
			this.last = newNode;
		} else {
			newNode.setNext(newNext);
			newNext.setPrev(newNode);
		}
		place.setNext(newNode);
		newNode.setPrev(place);
		return newNode;
	}

	/**
	 * Insert a new element in the list immediately before the specified place.
	 * 
	 * @param place
	 *            a non-null node in this list. If <code>place</code> is not a
	 *            node in this list, the behavior is undefined
	 * @param value
	 *            an element of T that will form the contents of the new node
	 * @return the new node containing <code>value</code>
	 */
	public FastNode<T> insertBefore(FastNode<T> place, T value) {
		FastNode<T> newNode = new FastNode<T>(value);
		FastNode<T> newPrev = place.getPrev();

		if (newPrev == null) {
			this.first = newNode;
		} else {
			newNode.setPrev(newPrev);
			newPrev.setNext(newNode);
		}
		place.setPrev(newNode);
		newNode.setNext(place);
		return newNode;
	}

	/**
	 * Splices the given list into this one at the point just after
	 * <code>place</code>. The given list <code>that</code> is destroyed.
	 * 
	 * @param place
	 *            a non-null node in this list. If it is not a node in this
	 *            list, behavior is undefined.
	 * @param that
	 *            a non-null (but possibly empty) list
	 */
	public void spliceInAfter(FastNode<T> place, FastList<T> that) {
		if (that.first == null)
			return;

		FastNode<T> newNext = place.getNext();

		place.setNext(that.first);
		that.first.setPrev(place);
		if (newNext == null) {
			this.last = that.last;
		} else {
			that.last.setNext(newNext);
			newNext.setPrev(that.last);
		}
		that.empty();
	}

	/**
	 * Splices the given list into this one at the point just before
	 * <code>place</code>. The given list <code>that</code> is destroyed.
	 * 
	 * @param place
	 *            a non-null node in this list. If it is not a node in this
	 *            list, behavior is undefined.
	 * @param that
	 *            a non-null (but possibly empty) list
	 */
	public void spliceInBefore(FastNode<T> place, FastList<T> that) {
		if (that.first == null)
			return;

		FastNode<T> newPrev = place.getNext();

		place.setPrev(that.last);
		that.last.setNext(place);
		if (newPrev == null) {
			this.first = that.first;
		} else {
			that.first.setPrev(newPrev);
			newPrev.setNext(that.first);
		}
		that.empty();
	}

	/**
	 * Returns a copy of this list. All the nodes are duplicated. The only
	 * objects shared between the old and new structures are the elements which
	 * are the data fields of the nodes.
	 * 
	 * @return a copy of this list
	 */
	@Override
	public FastList<T> clone() {
		FastList<T> result = new FastList<T>();

		if (first == null)
			return result;

		FastNode<T> prev = new FastNode<T>(first.getData()), curr = prev, old = first
				.getNext();

		result.first = prev;

		while (old != null) {
			// loop invariant:
			assert curr != null && prev == curr;
			curr = new FastNode<T>(old.getData());
			curr.setPrev(prev);
			prev.setNext(curr);
			prev = curr;
			old = old.getNext();
		}
		assert curr != null && prev == curr;
		result.last = curr;
		return result;
	}

	public void print(PrintStream out) {
		for (FastNode<T> node = first; node != null; node = node.getNext()) {
			T data = node.getData();

			if (data != null)
				out.print(data);
		}
		out.flush();
	}
}
