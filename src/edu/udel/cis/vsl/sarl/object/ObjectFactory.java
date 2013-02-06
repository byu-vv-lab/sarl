package edu.udel.cis.vsl.sarl.object;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberIF;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;

public class ObjectFactory {

	private Map<SymbolicObject, SymbolicObject> objectMap = new HashMap<SymbolicObject, SymbolicObject>();

	private ArrayList<SymbolicObject> objectList = new ArrayList<SymbolicObject>();

	private BooleanObject trueObj, falseObj;

	private IntObject zeroIntObj, oneIntObj;

	private NumberObject zeroIntegerObj, zeroRealObj, oneIntegerObj,
			oneRealObj;

	public ObjectFactory(NumberFactoryIF numberFactory) {
		this.trueObj = (BooleanObject) canonic(new CommonBooleanObject(true));
		this.falseObj = (BooleanObject) canonic(new CommonBooleanObject(false));
		this.zeroIntObj = (IntObject) canonic(intObject(0));
		this.oneIntObj = (IntObject) canonic(intObject(1));
		this.zeroIntegerObj = (NumberObject) canonic(numberObject(numberFactory
				.zeroInteger()));
		this.zeroRealObj = (NumberObject) canonic(numberObject(numberFactory
				.zeroRational()));
		this.oneIntegerObj = (NumberObject) canonic(numberObject(numberFactory
				.oneInteger()));
		this.oneRealObj = (NumberObject) canonic(numberObject(numberFactory
				.oneRational()));

	}

	/**
	 * This canonic will be used for all symbolic objects including types,
	 * expressions, sets, etc.
	 * 
	 * @param object
	 * @return
	 */
	public SymbolicObject canonic(SymbolicObject object) {
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
			return result;
		}
	}

	public SymbolicTypeIF canonic(SymbolicTypeIF type) {
		return (SymbolicTypeIF) canonic(type);
	}

	public SymbolicExpressionIF canonic(SymbolicExpressionIF expression) {
		return (SymbolicExpressionIF) canonic(expression);
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

	public NumberObject numberObject(NumberIF value) {
		return new CommonNumberObject(value);
	}

	public StringObject stringObject(String string) {
		return new CommonStringObject(string);
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
