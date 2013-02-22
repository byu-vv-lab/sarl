package edu.udel.cis.vsl.sarl.object.IF;

import java.util.Collection;
import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.object.common.ObjectComparator;

public interface ObjectFactory {

	void setExpressionComparator(Comparator<SymbolicExpression> c);

	void setCollectionComparator(Comparator<SymbolicCollection<?>> c);

	void setTypeComparator(Comparator<SymbolicType> c);

	void setTypeSequenceComparator(Comparator<SymbolicTypeSequence> c);

	public void init();

	ObjectComparator comparator();

	/**
	 * This canonic will be used for all symbolic objects including types,
	 * expressions, sets, etc.
	 * 
	 * @param object
	 * @return
	 */
	<T extends SymbolicObject> T canonic(T object);

	// SymbolicType canonic(SymbolicType type);

	// SymbolicExpression canonic(SymbolicExpression expression);

	BooleanObject trueObj();

	BooleanObject falseObj();

	IntObject zeroIntObj();

	IntObject oneIntObj();

	NumberObject zeroIntegerObj();

	NumberObject oneIntegerObj();

	NumberObject zeroRealObj();

	NumberObject oneRealObj();

	NumberObject numberObject(Number value);

	StringObject stringObject(String string);

	IntObject intObject(int value);

	BooleanObject booleanObject(boolean value);

	SymbolicObject objectWithId(int index);

	Collection<SymbolicObject> objects();

	int numObjects();

	NumberFactory numberFactory();
}
