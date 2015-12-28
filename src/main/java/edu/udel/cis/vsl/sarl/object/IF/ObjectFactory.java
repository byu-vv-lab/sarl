/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.object.IF;

import java.util.Collection;
import java.util.Comparator;

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
import edu.udel.cis.vsl.sarl.object.common.ObjectComparator;

public interface ObjectFactory {
	
	/**
	 * @return the numberFactory of the ObjectFactory
	 */
    NumberFactory numberFactory(); 

    /**
     * Sets the Expression Comparator of the ObjectFactory
     * @param c
     */
	void setExpressionComparator(Comparator<SymbolicExpression> c);

	/**
	 * Sets the Collection Comparator of the ObjectFactory
	 * @param c
	 */
	void setCollectionComparator(Comparator<SymbolicCollection<?>> c);

	/**
	 * Sets the TypeComparator of the ObjectFactory
	 * @param c
	 */
	void setTypeComparator(Comparator<SymbolicType> c);

	/**
	 * Sets the TypeSequenceComparator of the ObjectFactory
	 * @param c
	 */
	void setTypeSequenceComparator(Comparator<SymbolicTypeSequence> c);

	/**
	 * Asserts that expressionComparator, collectionComparator, typeComparator, and typeSequenceComparator 
	 * are set for this object
	 */
	public void init();

	
	/**
	 * 
	 * @return the object's comparator
	 */
	ObjectComparator comparator();

	/**
	 * Returns the canonic representative of the object's equivalence class.
	 * This will be used for the "canonicalization" of all symbolic objects in a
	 * universe.
	 * 
	 * @param object
	 *            any symbolic object
	 * @return the canonic representative
	 */
	<T extends SymbolicObject> T canonic(T object);

	/**
	 * @return Returns a new canonic CommonBooleanObject of value true
	 */
	BooleanObject trueObj();

	/**
	 * @return Returns a new canonic CommonBooleanObject of value false
	 */
	BooleanObject falseObj();

	/**
	 * @return Returns a canonic intObject of value 0
	 */
	IntObject zeroIntObj();

	/**
	 * @return Returns a canonic intObject of value 1
	 */
	IntObject oneIntObj();

	/**
	 * @return Returns a canonic NumberObject (IntegerNumber) of value 0
	 */
	NumberObject zeroIntegerObj();

	/**
	 * @return Returns a canonic NumberObject (IntegerNumber) of value 1
	 */
	NumberObject oneIntegerObj();

	/**
	 * @return Returns a canonic NumberObject (RationalNumber) of value 0
	 */
	NumberObject zeroRealObj();

	/**
	 * @return Returns a canonic NumberObject (RationalNumber) of value 1
	 */
	NumberObject oneRealObj();

	/**
	 * @return Returns a NumberObject of specified value
	 */
	NumberObject numberObject(Number value);

	/**
	 * @return Returns a CharObject of specified value
	 */
	CharObject charObject(char value);

	/**
	 * @return Returns a canonic StringObject of specified value
	 */
	StringObject stringObject(String string);

	/**
	 * @return Returns a IntObject of specified value
	 */
	IntObject intObject(int value);

	/**
	 * @return Returns a BooleanObject of specified value
	 */
	BooleanObject booleanObject(boolean value);

	/**
	 * @return Returns an object from the objectList at specified index
	 */
	SymbolicObject objectWithId(int index);

	/**
	 * @return Returns the entire objectList
	 */
	Collection<SymbolicObject> objects();

	/**
	 * @return Returns the length of the objectList
	 */
	int numObjects();

}
