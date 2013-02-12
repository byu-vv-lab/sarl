package edu.udel.cis.vsl.sarl.object.IF;

import java.util.Collection;
import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberIF;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequenceIF;
import edu.udel.cis.vsl.sarl.object.common.ObjectComparator;

public interface ObjectFactory {

	void setExpressionComparator(Comparator<SymbolicExpressionIF> c);

	void setCollectionComparator(Comparator<SymbolicCollection> c);

	void setTypeComparator(Comparator<SymbolicTypeIF> c);

	void setTypeSequenceComparator(Comparator<SymbolicTypeSequenceIF> c);

	public void init();

	ObjectComparator comparator();

	/**
	 * This canonic will be used for all symbolic objects including types,
	 * expressions, sets, etc.
	 * 
	 * @param object
	 * @return
	 */
	SymbolicObject canonic(SymbolicObject object);

	SymbolicTypeIF canonic(SymbolicTypeIF type);

	SymbolicExpressionIF canonic(SymbolicExpressionIF expression);

	BooleanObject trueObj();

	BooleanObject falseObj();

	IntObject zeroIntObj();

	IntObject oneIntObj();

	NumberObject zeroIntegerObj();

	NumberObject oneIntegerObj();

	NumberObject zeroRealObj();

	NumberObject oneRealObj();

	NumberObject numberObject(NumberIF value);

	StringObject stringObject(String string);

	IntObject intObject(int value);

	BooleanObject booleanObject(boolean value);

	SymbolicObject objectWithId(int index);

	Collection<SymbolicObject> objects();

	int numObjects();

	NumberFactoryIF numberFactory();
}
