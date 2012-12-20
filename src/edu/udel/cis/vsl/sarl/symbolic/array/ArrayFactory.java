package edu.udel.cis.vsl.sarl.symbolic.array;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicCompleteArrayTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.concrete.ConcreteFactory;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpressionKey;

public class ArrayFactory {

	private ConcreteFactory concreteFactory;

	private Map<SymbolicExpressionKey<ArrayRead>, ArrayRead> arrayReadMap = new HashMap<SymbolicExpressionKey<ArrayRead>, ArrayRead>();

	private Map<SymbolicExpressionKey<ArrayLength>, ArrayLength> arrayLengthMap = new HashMap<SymbolicExpressionKey<ArrayLength>, ArrayLength>();

	private Map<SymbolicExpressionKey<ArrayWrite>, ArrayWrite> arrayWriteMap = new HashMap<SymbolicExpressionKey<ArrayWrite>, ArrayWrite>();

	private Map<SymbolicExpressionKey<ArrayExpression>, ArrayExpression> arrayExpressionMap = new HashMap<SymbolicExpressionKey<ArrayExpression>, ArrayExpression>();

	private Map<SymbolicExpressionKey<ArrayLambdaExpression>, ArrayLambdaExpression> arrayLambdaExpressionMap = new HashMap<SymbolicExpressionKey<ArrayLambdaExpression>, ArrayLambdaExpression>();

	public ArrayFactory(ConcreteFactory concreteFactory) {
		this.concreteFactory = concreteFactory;
	}

	public ArrayRead arrayRead(TreeExpressionIF array, TreeExpressionIF index) {
		return SymbolicExpression.flyweight(arrayReadMap, new ArrayRead(array,
				index));
	}

	public ArrayLength arrayLength(TreeExpressionIF array) {
		return SymbolicExpression.flyweight(arrayLengthMap, new ArrayLength(
				array, concreteFactory.typeFactory().integerType()));
	}

	public ArrayWrite arrayWrite(TreeExpressionIF array,
			TreeExpressionIF index, TreeExpressionIF value) {
		return SymbolicExpression.flyweight(arrayWriteMap, new ArrayWrite(
				array, index, value));
	}

	public ArrayWrite arrayWrite(TreeExpressionIF array, int index,
			TreeExpressionIF value) {
		return arrayWrite(array, concreteFactory.concrete(index), value);
	}

	public ArrayRead arrayRead(TreeExpressionIF array, int index) {
		return arrayRead(array, concreteFactory.concrete(index));
	}

	public ArrayExpression arrayExpression(TreeExpressionIF origin,
			TreeExpressionIF[] elements) {
		return SymbolicExpression.flyweight(arrayExpressionMap,
				new ArrayExpression(origin, elements, this));
	}

	public ArrayLambdaExpression arrayLambdaExpression(
			SymbolicCompleteArrayTypeIF arrayType, TreeExpressionIF function) {
		return SymbolicExpression.flyweight(arrayLambdaExpressionMap,
				new ArrayLambdaExpression(arrayType, function));
	}

}
