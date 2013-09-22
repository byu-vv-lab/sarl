package edu.udel.cis.vsl.sarl.object.common;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.CharObject;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;

/**
 * Simple stub object factory. Implmeents only those methods related to
 * {@link #canonic}.
 * 
 * @author siegel
 * 
 */
public class ObjectFactoryStub implements ObjectFactory {

	private Map<SymbolicObject, SymbolicObject> objectMap = new LinkedHashMap<SymbolicObject, SymbolicObject>();

	@Override
	public void setExpressionComparator(Comparator<SymbolicExpression> c) {
	}

	@Override
	public void setCollectionComparator(Comparator<SymbolicCollection<?>> c) {
	}

	@Override
	public void setTypeComparator(Comparator<SymbolicType> c) {
	}

	@Override
	public void setTypeSequenceComparator(Comparator<SymbolicTypeSequence> c) {
	}

	@Override
	public void init() {
	}

	@Override
	public ObjectComparator comparator() {
		return null;
	}

	@Override
	public <T extends SymbolicObject> T canonic(T object) {
		@SuppressWarnings("unchecked")
		T result = (T) objectMap.get(object);

		if (result == null) {
			result = object;
			((CommonSymbolicObject) result).setId(objectMap.size());
			objectMap.put(result, result);
		}
		return result;
	}

	@Override
	public BooleanObject trueObj() {
		return null;
	}

	@Override
	public BooleanObject falseObj() {
		return null;
	}

	@Override
	public IntObject zeroIntObj() {
		return null;
	}

	@Override
	public IntObject oneIntObj() {
		return null;
	}

	@Override
	public NumberObject zeroIntegerObj() {
		return null;
	}

	@Override
	public NumberObject oneIntegerObj() {
		return null;
	}

	@Override
	public NumberObject zeroRealObj() {
		return null;
	}

	@Override
	public NumberObject oneRealObj() {
		return null;
	}

	@Override
	public NumberObject numberObject(Number value) {
		return null;
	}

	@Override
	public CharObject charObject(char value) {
		return null;
	}

	@Override
	public StringObject stringObject(String string) {
		return null;
	}

	@Override
	public IntObject intObject(int value) {
		return null;
	}

	@Override
	public BooleanObject booleanObject(boolean value) {
		return null;
	}

	@Override
	public SymbolicObject objectWithId(int index) {
		return null;
	}

	@Override
	public Collection<SymbolicObject> objects() {
		return objectMap.keySet();
	}

	@Override
	public int numObjects() {
		return objectMap.size();
	}

	@Override
	public NumberFactory numberFactory() {
		return null;
	}

}
