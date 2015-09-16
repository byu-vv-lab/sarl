package edu.udel.cis.vsl.sarl.collections.common;

import static edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection.SymbolicCollectionKind.SORTED_MAP;
import edu.udel.cis.vsl.sarl.IF.UnaryOperator;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.SortedSymbolicMap;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.util.BinaryOperator;

public abstract class CommonSortedMap<K extends SymbolicExpression, V extends SymbolicExpression>
		extends CommonSymbolicMap<K, V> implements SortedSymbolicMap<K, V> {

	CommonSortedMap() {
		super(SORTED_MAP);
	}

	@Override
	public SortedSymbolicMap<K, V> apply(UnaryOperator<V> operator) {
		return (SortedSymbolicMap<K, V>) super.apply(operator);
	}

	@Override
	public SortedSymbolicMap<K, V> combine(BinaryOperator<V> operator,
			SymbolicMap<K, V> map) {
		return (SortedSymbolicMap<K, V>) super.combine(operator, map);
	}

}
