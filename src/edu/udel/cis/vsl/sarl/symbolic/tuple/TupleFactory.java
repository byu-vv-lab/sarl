package edu.udel.cis.vsl.sarl.symbolic.tuple;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.symbolic.IF.tree.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTupleTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpressionKey;

public class TupleFactory {

	Map<SymbolicExpressionKey<TupleRead>, TupleRead> tupleReadMap = new HashMap<SymbolicExpressionKey<TupleRead>, TupleRead>();

	Map<SymbolicExpressionKey<TupleWrite>, TupleWrite> tupleWriteMap = new HashMap<SymbolicExpressionKey<TupleWrite>, TupleWrite>();

	Map<SymbolicExpressionKey<Tuple>, Tuple> tupleMap = new HashMap<SymbolicExpressionKey<Tuple>, Tuple>();

	public TupleRead tupleRead(TreeExpressionIF tuple,
			NumericConcreteExpressionIF index) {
		return SymbolicExpression.flyweight(tupleReadMap, new TupleRead(tuple,
				index));
	}

	public TupleWrite tupleWrite(TreeExpressionIF tuple,
			NumericConcreteExpressionIF index, TreeExpressionIF value) {
		return SymbolicExpression.flyweight(tupleWriteMap, new TupleWrite(
				tuple, index, value));
	}

	public Tuple tuple(TreeExpressionIF[] components,
			SymbolicTupleTypeIF tupleType) {
		return SymbolicExpression.flyweight(tupleMap, new Tuple(tupleType,
				components));
	}

}
