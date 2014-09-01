/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Collection;
import java.util.Iterator;

import org.pcollections.PVector;
import org.pcollections.TreePVector;

import edu.udel.cis.vsl.sarl.IF.Transform;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

/**
 * Note TreePVector cannot take null elements!!! Use the symbolic expression
 * "nullExpression()" instead.
 * 
 * @author siegel
 * 
 */
public class PcollectionsSequence<T extends SymbolicExpression> extends
		CommonSymbolicCollection<T> implements SymbolicSequence<T> {

	private PVector<T> pvector;

	int numNull;

	PcollectionsSequence(PVector<T> pvector, int numNull) {
		super(SymbolicCollectionKind.SEQUENCE);
		this.pvector = pvector;
		this.numNull = numNull;
	}

	public PcollectionsSequence(PVector<T> pvector) {
		super(SymbolicCollectionKind.SEQUENCE);
		this.pvector = pvector;

		numNull = 0;
		for (SymbolicExpression expr : pvector)
			if (expr.isNull())
				numNull++;
	}

	public PcollectionsSequence() {
		super(SymbolicCollectionKind.SEQUENCE);
		pvector = TreePVector.empty();
		numNull = 0;
	}

	/**
	 * The caller is responsible for making sure "elements" does not contain any
	 * null elements.
	 * 
	 * @param elements
	 */
	public PcollectionsSequence(Collection<T> elements) {
		this(TreePVector.from(elements));
	}

	public PcollectionsSequence(Iterable<? extends T> elements) {
		this();
		for (T expr : elements) {
			if (expr == null)
				throw new NullPointerException(
						"Attempt to place null element in a symbolic sequence");
			pvector = pvector.plus(expr);
		}
	}

	public PcollectionsSequence(T[] elements) {
		this();
		for (T expr : elements) {
			if (expr == null)
				throw new NullPointerException(
						"Attempt to place null element in a symbolic sequence");
			pvector = pvector.plus(expr);
		}
	}

	public PcollectionsSequence(T element) {
		this();
		if (element == null)
			throw new NullPointerException(
					"Attempt to place null element in a symbolic sequence");
		pvector = pvector.plus(element);
	}

	@Override
	public Iterator<T> iterator() {
		return pvector.iterator();
	}

	@Override
	public int size() {
		return pvector.size();
	}

	@Override
	public T get(int index) {
		return pvector.get(index);
	}

	@Override
	public SymbolicSequence<T> add(T element) {
		return new PcollectionsSequence<T>(pvector.plus(element),
				element.isNull() ? numNull + 1 : numNull);
	}

	@Override
	public SymbolicSequence<T> set(int index, T element) {
		int newNumNull = numNull;

		if (pvector.get(index).isNull())
			newNumNull--;
		if (element.isNull())
			newNumNull++;
		return new PcollectionsSequence<T>(pvector.with(index, element),
				newNumNull);
	}

	@Override
	public SymbolicSequence<T> remove(int index) {
		return new PcollectionsSequence<T>(pvector.minus(index), pvector.get(
				index).isNull() ? numNull - 1 : numNull);
	}

	@Override
	protected boolean collectionEquals(SymbolicCollection<T> o) {
		if (this == o) {
			return true;
		}
		if (this.size() != o.size()) {
			return false;
		}

		SymbolicSequence<T> that = (SymbolicSequence<T>) o;

		if (numNull != that.getNumNull())
			return false;

		Iterator<T> these = this.iterator();
		Iterator<T> those = that.iterator();

		while (these.hasNext())
			if (!those.hasNext() || !these.next().equals(those.next()))
				return false;
		return !those.hasNext();
	}

	@Override
	protected int computeHashCode() {
		return pvector.hashCode();
	}

	@Override
	public SymbolicSequence<T> subSequence(int start, int end) {
		return new PcollectionsSequence<T>(pvector.subList(start, end));
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		Iterator<T> iter = iterator();
		int count = 0;

		while (iter.hasNext()) {
			T expr = iter.next();

			if (!expr.isCanonic()) {
				PVector<T> newVector = pvector.subList(0, count);

				T canonic = factory.canonic(expr);

				assert canonic != null;
				newVector = newVector.plus(canonic);
				while (iter.hasNext()) {
					canonic = factory.canonic(iter.next());
					assert canonic != null;
					newVector = newVector.plus(canonic);
				}
				pvector = newVector;
				return;
			}
			count++;
		}
	}

	@Override
	public SymbolicSequence<T> setExtend(int index, T value, T filler) {
		int size = pvector.size();

		if (index < size)
			return set(index, value);
		else {
			PVector<T> newVector = pvector;
			int newNumNull = numNull;

			for (int i = size; i < index; i++)
				newVector = newVector.plus(filler);
			if (filler.isNull())
				newNumNull += index - size;
			newVector = newVector.plus(value);
			if (value.isNull())
				newNumNull++;
			return new PcollectionsSequence<T>(newVector, newNumNull);
		}
	}

	@Override
	public <U extends SymbolicExpression> SymbolicSequence<U> apply(
			Transform<T, U> transform) {
		int count = 0;
		Iterator<T> iter = pvector.iterator();

		while (iter.hasNext()) {
			T t = iter.next();
			U u = transform.apply(t);

			if (t != u) {
				@SuppressWarnings("unchecked")
				PVector<U> newVector = (PVector<U>) pvector.subList(0, count);

				newVector = newVector.plus(u);
				while (iter.hasNext())
					newVector = newVector.plus(transform.apply(iter.next()));
				return new PcollectionsSequence<U>(newVector);
			}
			count++;
		}
		{
			@SuppressWarnings("unchecked")
			SymbolicSequence<U> result = (SymbolicSequence<U>) this;

			return result;
		}
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		StringBuffer result = new StringBuffer();
		boolean first = true;

		result.append("<");

		for (T element : this) {
			if (first)
				first = false;
			else
				result.append(",");
			result.append(element == null ? "null" : element
					.toStringBuffer(false));
		}
		result.append(">");
		return result;
	}

	@Override
	public StringBuffer toStringBufferLong() {
		StringBuffer result = new StringBuffer("Sequence");

		result.append(toStringBuffer(true));
		return result;
	}

	@Override
	public SymbolicSequence<T> insert(int index, T element) {

		return new PcollectionsSequence<T>(pvector.plus(index, element),
				element.isNull() ? numNull + 1 : numNull);
	}

	@Override
	public int getNumNull() {
		return numNull;
	}
}
