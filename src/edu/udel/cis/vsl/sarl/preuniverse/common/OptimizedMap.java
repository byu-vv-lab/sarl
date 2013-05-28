package edu.udel.cis.vsl.sarl.preuniverse.common;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

public class OptimizedMap implements
		Map<SymbolicExpression, SymbolicExpression> {

	private Map<SymbolicConstant, SymbolicExpression> map;

	public OptimizedMap(Map<SymbolicConstant, SymbolicExpression> map) {
		assert map != null;
		this.map = map;
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return key instanceof SymbolicConstant ? map.containsKey(key) : false;
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public SymbolicExpression get(Object key) {
		return key instanceof SymbolicConstant ? map.get(key) : null;
	}

	@Override
	public SymbolicExpression put(SymbolicExpression key,
			SymbolicExpression value) {
		throw new UnsupportedOperationException("Immutable map");
	}

	@Override
	public SymbolicExpression remove(Object key) {
		throw new UnsupportedOperationException("Immutable map");
	}

	@Override
	public void putAll(
			Map<? extends SymbolicExpression, ? extends SymbolicExpression> m) {
		throw new UnsupportedOperationException("Immutable map");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("Immutable map");
	}

	@Override
	public Set<SymbolicExpression> keySet() {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public Collection<SymbolicExpression> values() {
		return map.values();
	}

	@Override
	public Set<java.util.Map.Entry<SymbolicExpression, SymbolicExpression>> entrySet() {
		throw new UnsupportedOperationException("Not supported");
	}

}
