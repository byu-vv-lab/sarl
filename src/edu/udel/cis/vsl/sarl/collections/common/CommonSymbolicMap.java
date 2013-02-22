package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Map.Entry;

import edu.udel.cis.vsl.sarl.IF.BinaryOperator;
import edu.udel.cis.vsl.sarl.IF.UnaryOperator;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

public abstract class CommonSymbolicMap<K extends SymbolicExpression, V extends SymbolicExpression>
		extends CommonSymbolicCollection<V> implements SymbolicMap<K, V> {

	public CommonSymbolicMap() {
		super(SymbolicCollectionKind.MAP);
	}

	@Override
	public SymbolicMap<K, V> apply(UnaryOperator<V> operator) {
		SymbolicMap<K, V> result = this;

		for (Entry<K, V> entry : this.entries())
			result = result.put(entry.getKey(),
					operator.apply(entry.getValue()));
		return result;
	}

	@Override
	public SymbolicMap<K, V> combine(BinaryOperator<V> operator,
			SymbolicMap<K, V> map) {
		SymbolicMap<K, V> result = this;

		for (Entry<K, V> entry : map.entries()) {
			K key = entry.getKey();
			V value2 = entry.getValue();
			V value1 = this.get(key);

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
