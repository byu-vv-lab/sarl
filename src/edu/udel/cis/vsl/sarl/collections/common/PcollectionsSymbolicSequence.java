package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Collection;
import java.util.Iterator;

import org.pcollections.PVector;
import org.pcollections.TreePVector;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicSequence;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

/**
 * Note TreePVector cannot take null elements!!! Use the symbolic expression
 * "nullExpression()" instead.
 * 
 * @author siegel
 * 
 */
public class PcollectionsSymbolicSequence extends CommonSymbolicCollection
		implements SymbolicSequence {

	private PVector<SymbolicExpressionIF> pvector;

	public PcollectionsSymbolicSequence() {
		super(SymbolicCollectionKind.SEQUENCE);
		pvector = TreePVector.empty();
	}

	public PcollectionsSymbolicSequence(
			Collection<SymbolicExpressionIF> elements) {
		super(SymbolicCollectionKind.SEQUENCE);
		pvector = TreePVector.from(elements);
	}

	public PcollectionsSymbolicSequence(
			Iterable<? extends SymbolicExpressionIF> elements) {
		this();
		for (SymbolicExpressionIF expr : elements)
			pvector = pvector.plus(expr);
	}

	public PcollectionsSymbolicSequence(SymbolicExpressionIF[] elements) {
		this();
		for (SymbolicExpressionIF expr : elements)
			pvector = pvector.plus(expr);
	}

	public PcollectionsSymbolicSequence(SymbolicExpressionIF element) {
		this();
		pvector = pvector.plus(element);
	}

	@Override
	public Iterator<SymbolicExpressionIF> iterator() {
		return pvector.iterator();
	}

	@Override
	public int size() {
		return pvector.size();
	}

	@Override
	public SymbolicExpressionIF get(int index) {
		return pvector.get(index);
	}

	@Override
	public SymbolicSequence add(SymbolicExpressionIF element) {
		return new PcollectionsSymbolicSequence(pvector.plus(element));
	}

	@Override
	public SymbolicSequence set(int index, SymbolicExpressionIF element) {
		return new PcollectionsSymbolicSequence(pvector.with(index, element));
	}

	@Override
	public SymbolicSequence remove(int index) {
		return new PcollectionsSymbolicSequence(pvector.minus(index));
	}

	@Override
	protected boolean collectionEquals(SymbolicCollection o) {
		if (this == o)
			return true;
		if (size() != o.size())
			return false;
		SymbolicSequence that = (SymbolicSequence) o;
		Iterator<SymbolicExpressionIF> these = this.iterator();
		Iterator<SymbolicExpressionIF> those = that.iterator();

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
	public void canonizeChildren(CommonObjectFactory factory) {
		Iterator<SymbolicExpressionIF> iter = iterator();
		int count = 0;

		while (iter.hasNext()) {
			SymbolicExpressionIF expr = iter.next();

			if (!expr.isCanonic()) {
				PVector<SymbolicExpressionIF> newVector = pvector.subList(0,
						count);

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
	public SymbolicSequence setExtend(int index, SymbolicExpressionIF value,
			SymbolicExpressionIF filler) {
		int size = pvector.size();

		if (index < size)
			return set(index, value);
		for (int i = size; i < index; i++)
			pvector = pvector.plus(filler);
		pvector = pvector.plus(value);
		return new PcollectionsSymbolicSequence(pvector);
	}

}
