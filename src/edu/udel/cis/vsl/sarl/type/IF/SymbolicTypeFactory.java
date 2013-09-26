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
package edu.udel.cis.vsl.sarl.type.IF;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.common.TypeComparator;
import edu.udel.cis.vsl.sarl.type.common.TypeSequenceComparator;

public interface SymbolicTypeFactory {
	
	/**
	 * setting the way you want to comparator to compare two expressions
	 * 
	 * @param c
	 */
	void setExpressionComparator(Comparator<SymbolicExpression> c);
	
	
	/**
	 * Initializing the factory
	 */
	void init();

	
	/**
	 * @return an object factory of the type factory
	 */
	ObjectFactory objectFactory();
	
	
	/**
	 * @return a booleanType from the factory
	 */
	SymbolicType booleanType();

	/**
	 * @return a SymbolicIntegerType from the factory of objects
	 */
	SymbolicIntegerType integerType();

	/**
	 * @return a SymbolicIntegerType that has a herbrand type.
	 */
	SymbolicIntegerType herbrandIntegerType();

	/**
	 * @param min
	 * @param max
	 * @param cyclic
	 * @return a SymbolicIntegerType 
	 */
	SymbolicIntegerType boundedIntegerType(NumericExpression min,
			NumericExpression max, boolean cyclic);

	/**
	 * @return a real type of kind ideal.
	 */
	SymbolicRealType realType();

	/**
	 * @return a herbrand real type from the factory
	 */
	SymbolicRealType herbrandRealType();

	/**
	 * @return a primitive type of kind char.
	 */
	SymbolicType characterType();

	/**
	 * @param elements
	 * @return a symbolic type sequence for any SymbolicType elements.
	 * elements can be of any type that extends SymbolicType.
	 */
	SymbolicTypeSequence sequence(Iterable<? extends SymbolicType> elements);

	SymbolicTypeSequence sequence(SymbolicType[] elements);

	SymbolicTypeSequence singletonSequence(SymbolicType type);

	/**
	 * @param elementType
	 * @return an incomplete arrayType from elementType.
	 */
	SymbolicArrayType arrayType(SymbolicType elementType);

	/**
	 * @param elementType
	 * @param extent
	 * @return a complete array type from elementType
	 */
	SymbolicCompleteArrayType arrayType(SymbolicType elementType,
			NumericExpression extent);

	/**
	 * @param name
	 * @param fieldTypes
	 * @return a symbolic tuple type
	 */
	SymbolicTupleType tupleType(StringObject name,
			SymbolicTypeSequence fieldTypes);

	/**
	 * @param name
	 * @param memberTypes
	 * @return a symbolic union type.
	 */
	SymbolicUnionType unionType(StringObject name,
			SymbolicTypeSequence memberTypes);

	/**
	 * @param inputTypes
	 * @param outputType
	 * @return a symbolic function type
	 */
	SymbolicFunctionType functionType(SymbolicTypeSequence inputTypes,
			SymbolicType outputType);

	/**
	 * @return a typeComparator to compare two symbolic types
	 */
	TypeComparator typeComparator();

	/**
	 * @return typeSequenceComparator that is used 
	 * when comparing two symbolic tuble, union, or function types
	 */
	TypeSequenceComparator typeSequenceComparator();

	/**
	 * @param type
	 * @return the pure type of the type
	 */
	SymbolicType pureType(SymbolicType type);

}
