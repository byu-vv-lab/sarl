package edu.udel.cis.vsl.sarl.object.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

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
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;

public class CommonObjectFactory implements ObjectFactory {

	private NumberFactory numberFactory;

	private Map<SymbolicObject, SymbolicObject> objectMap = new HashMap<SymbolicObject, SymbolicObject>();

	private ArrayList<SymbolicObject> objectList = new ArrayList<SymbolicObject>();

	// TODO: think about this: cache the total order for fast
	// comparisons...
	// private NavigableSet<SymbolicObject> sortedSet;

	private BooleanObject trueObj, falseObj;

	private IntObject zeroIntObj, oneIntObj;

	private NumberObject zeroIntegerObj, zeroRealObj, oneIntegerObj,
			oneRealObj;

	private ObjectComparator comparator;

	public CommonObjectFactory(NumberFactory numberFactory) {
		this.numberFactory = numberFactory;
		this.comparator = new ObjectComparator();
		this.trueObj = canonic(new CommonBooleanObject(true));
		this.falseObj = canonic(new CommonBooleanObject(false));
		this.zeroIntObj = canonic(intObject(0));
		this.oneIntObj = canonic(intObject(1));
		this.zeroIntegerObj = canonic(numberObject(numberFactory.zeroInteger()));
		this.zeroRealObj = canonic(numberObject(numberFactory.zeroRational()));
		this.oneIntegerObj = canonic(numberObject(numberFactory.oneInteger()));
		this.oneRealObj = canonic(numberObject(numberFactory.oneRational()));
	}

	@Override
	public NumberFactory numberFactory() {
		return numberFactory;
	}

	public void setExpressionComparator(Comparator<SymbolicExpression> c) {
		comparator.setExpressionComparator(c);
	}

	public void setCollectionComparator(Comparator<SymbolicCollection<?>> c) {
		comparator.setCollectionComparator(c);
	}

	public void setTypeComparator(Comparator<SymbolicType> c) {
		comparator.setTypeComparator(c);
	}

	public void setTypeSequenceComparator(Comparator<SymbolicTypeSequence> c) {
		comparator.setTypeSequenceComparator(c);
	}

	public void init() {
		assert comparator.expressionComparator() != null;
		assert comparator.collectionComparator() != null;
		assert comparator.typeComparator() != null;
		assert comparator.typeSequenceComparator() != null;
		// TODO set the orders of all the objects you already created??
		// maybe only do this the first time they are used in
		// a comparison. How will the other comparators
		// do this
	}

	public ObjectComparator comparator() {
		return comparator;
	}

	/**
	 * This canonic will be used for all symbolic objects including types,
	 * expressions, sets, etc.
	 * 
	 * @param object
	 * @return
	 */
	public <T extends SymbolicObject> T canonic(T object) {
		if (object.isCanonic())
			return object;
		else {
			SymbolicObject result = objectMap.get(object);

			if (result == null) {
				((CommonSymbolicObject) object).setId(objectList.size());
				objectMap.put(object, object);
				objectList.add(object);
				return object;
			}
			@SuppressWarnings("unchecked")
			T result2 = (T) result;

			// TODO set the order if you can.

			return result2;
		}
	}

	public BooleanObject trueObj() {
		return trueObj;
	}

	public BooleanObject falseObj() {
		return falseObj;
	}

	public IntObject zeroIntObj() {
		return zeroIntObj;
	}

	public IntObject oneIntObj() {
		return oneIntObj;
	}

	public NumberObject zeroIntegerObj() {
		return zeroIntegerObj;
	}

	public NumberObject oneIntegerObj() {
		return oneIntegerObj;
	}

	public NumberObject zeroRealObj() {
		return zeroRealObj;
	}

	public NumberObject oneRealObj() {
		return oneRealObj;
	}

	public NumberObject numberObject(Number value) {
		return new CommonNumberObject(value);
	}

	public StringObject stringObject(String string) {
		return canonic(new CommonStringObject(string));
	}

	public IntObject intObject(int value) {
		return new CommonIntObject(value);
	}

	public BooleanObject booleanObject(boolean value) {
		return value ? trueObj : falseObj;
	}

	public SymbolicObject objectWithId(int index) {
		return objectList.get(index);
	}

	public Collection<SymbolicObject> objects() {
		return objectList;
	}

	public int numObjects() {
		return objectList.size();
	}

}
