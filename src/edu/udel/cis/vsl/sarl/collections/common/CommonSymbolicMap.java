package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Map.Entry;

import edu.udel.cis.vsl.sarl.IF.BinaryOperator;
import edu.udel.cis.vsl.sarl.IF.UnaryOperator;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;

public abstract class CommonSymbolicMap<K extends SymbolicExpression, V extends SymbolicExpression>
		extends CommonSymbolicCollection<V> implements SymbolicMap<K, V> {

	public CommonSymbolicMap() {
		super(SymbolicCollectionKind.MAP);
	}

	@Override
	public SymbolicMap<K, V> apply(UnaryOperator<V> operator) {
		SymbolicMap<K, V> result = this;

		for (Entry<K, V> entry : this.entries()) {
			K key = entry.getKey();
			V value = entry.getValue();
			V newValue = operator.apply(value);

			if (newValue == null)
				result = result.remove(key);
			else if (value != newValue)
				result = result.put(key, newValue);
		}
		return result;
	}

	// public <K2 extends SymbolicExpression, V2 extends SymbolicExpression>
	// SymbolicMap<K2, V2> apply(
	// Transform<K, K2> keyTransform, Transform<V, V2> valueTransform) {
	// // TODO
	// return null;
	// }

	@Override
	public SymbolicMap<K, V> combine(BinaryOperator<V> operator,
			SymbolicMap<K, V> map) {
		SymbolicMap<K, V> result, map2;

		if (this.size() >= map.size()) {
			result = this;
			map2 = map;
		} else {
			result = map;
			map2 = this;
		}
		for (Entry<K, V> entry : map2.entries()) {
			K key = entry.getKey();
			V value2 = entry.getValue();
			V value1 = result.get(key);

			if (value1 == null)
				result = result.put(key, value2);
			else {
				V newValue = operator.apply(value1, value2);

				if (newValue == null)
					result = result.remove(key);
				else
					result = result.put(key, newValue);
			}
		}
		return result;
	}

	public StringBuffer toStringBuffer() {
		StringBuffer buffer = new StringBuffer("{");
		boolean first = true;

		for (Entry<K, V> entry : this.entries()) {
			if (first)
				first = false;
			else
				buffer.append(",");
			buffer.append(entry.getKey().toString());
			buffer.append("->");
			buffer.append(entry.getValue().toString());
		}
		buffer.append("}");
		return buffer;
	}

	@Override
	public String toString() {
		return toStringBuffer().toString();
	}

}
