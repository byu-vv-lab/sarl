package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Map.Entry;

import edu.udel.cis.vsl.sarl.IF.BinaryOperatorIF;
import edu.udel.cis.vsl.sarl.IF.UnaryOperatorIF;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;

public abstract class CommonSymbolicMap extends CommonSymbolicCollection
		implements SymbolicMap {

	public CommonSymbolicMap() {
		super(SymbolicCollectionKind.MAP);
	}

	@Override
	public SymbolicMap apply(UnaryOperatorIF operator) {
		SymbolicMap result = this;

		for (Entry<SymbolicExpressionIF, SymbolicExpressionIF> entry : this
				.entries())
			result = result.put(entry.getKey(),
					operator.apply(entry.getValue()));
		return result;
	}

	@Override
	public SymbolicMap combine(BinaryOperatorIF operator, SymbolicMap map) {
		SymbolicMap result = this;

		for (Entry<SymbolicExpressionIF, SymbolicExpressionIF> entry : map
				.entries()) {
			SymbolicExpressionIF key = entry.getKey();
			SymbolicExpressionIF value2 = entry.getValue();
			SymbolicExpressionIF value1 = this.get(key);

			if (value1 == null)
				result = result.put(key, value2);
			else {
				SymbolicExpressionIF newValue = operator.apply(value1, value2);

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

		for (Entry<SymbolicExpressionIF, SymbolicExpressionIF> entry : this
				.entries()) {
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
