package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Collection;
import java.util.Iterator;

import org.pcollections.PVector;
import org.pcollections.TreePVector;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSequence;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

/**
 * Note TreePVector cannot take null elements!!! Use the symbolic expression
 * "nullExpression()" instead.
 * 
 * @author siegel
 * 
 */
public class PcollectionsSymbolicSequence<T extends SymbolicExpression> extends
		CommonSymbolicCollection<T> implements SymbolicSequence<T> {

	private PVector<T> pvector;

	public PcollectionsSymbolicSequence() {
		super(SymbolicCollectionKind.SEQUENCE);
		pvector = TreePVector.empty();
	}

	public PcollectionsSymbolicSequence(Collection<T> elements) {
		super(SymbolicCollectionKind.SEQUENCE);
		pvector = TreePVector.from(elements);
	}

	public PcollectionsSymbolicSequence(Iterable<? extends T> elements) {
		this();
		for (T expr : elements)
			pvector = pvector.plus(expr);
	}

	public PcollectionsSymbolicSequence(T[] elements) {
		this();
		for (T expr : elements)
			pvector = pvector.plus(expr);
	}

	public PcollectionsSymbolicSequence(T element) {
		this();
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
		return new PcollectionsSymbolicSequence<T>(pvector.plus(element));
	}

	@Override
	public SymbolicSequence<T> set(int index, T element) {
		return new PcollectionsSymbolicSequence<T>(pvector.with(index, element));
	}

	@Override
	public SymbolicSequence<T> remove(int index) {
		return new PcollectionsSymbolicSequence<T>(pvector.minus(index));
	}

	@Override
	protected boolean collectionEquals(SymbolicCollection<T> o) {
		if (this == o)
			return true;
		if (size() != o.size())
			return false;

		SymbolicSequence<T> that = (SymbolicSequence<T>) o;
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
		return new PcollectionsSymbolicSequence<T>(pvector.subList(start, end));
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
		Iterator<T> iter = iterator();
		int count = 0;

		while (iter.hasNext()) {
			T expr = iter.next();

			if (!expr.isCanonic()) {
				PVector<T> newVector = pvector.subList(0, count);

				newVector = newVector.plus(factory.canonic(expr));
				while (iter.hasNext())
					newVector = newVector.plus(factory.canonic(iter.next()));
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

			for (int i = size; i < index; i++)
				newVector = newVector.plus(filler);
			newVector = newVector.plus(value);
			return new PcollectionsSymbolicSequence<T>(newVector);
		}
	}

	@Override
	public String toString() {
		return pvector.toString();
	}

}
