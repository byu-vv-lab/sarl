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

/**
 * A SymbolicTypeFactory interface that sets fields and methods
 * for creating all different types:
 * 	- CommonSymbolicIntegerType
 * 	- CommonSymbolicRealType
 *  - CommonSymbolicCompleteArrayType
 *  - CommonSymbolicArrayType
 *  - CommonSymbolicPrimitiveType
 *  - CommonSymbolicFunctionType
 *  - CommonSymbolicTypeSequence
 *  - TypeComparator
 * @author alali
 *
 */
public interface SymbolicTypeFactory {
	
	/**
	 * setting the way you want to comparator to compare two expressions
	 * used when comparing CompleteArrayType
	 * @param c used to compare
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
	 * @return a SymbolicIntegerType that has a herbrand kind.
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
	 * Creates a SymbolicTypeSequence from a list of SymbolicTypes
	 * @param elements elements can be of any type that extends SymbolicType.
	 * @return a symbolic type sequence for any SymbolicType elements.
	 * 
	 */
	SymbolicTypeSequence sequence(Iterable<? extends SymbolicType> elements);

	/**
	 * Creates a SymbolicTypeSequecne from an array of SymbolicType
	 * @param elements array of any length of any SymbolicType, e.g. integer, real, primitve, array, ...
	 * @return a SymbolicTypeSequence for the elements in the array.
	 */
	SymbolicTypeSequence sequence(SymbolicType[] elements);

	/**
	 * Creates a SymbolicTypeSequence of 1 element that has a SymbolicType
	 * @param type any SymbolicType
	 * @return a SymbolicTypeSequence that contains one element only.
	 */
	SymbolicTypeSequence singletonSequence(SymbolicType type);

	/**
	 * Creates a SymbolicArrayType that has elements of type elementType
	 * @param elementType any SymbolicType that represents that type of the SymbolicArrayType
	 * @return an SymbolicArrayType of elements of type elementType.
	 */
	SymbolicArrayType arrayType(SymbolicType elementType);

	/**
	 * Creates a SymbolicCompleteArrayType
	 * @param elementType the type of elements in the array
	 * @param extent the length of the array as a NumericExpression
	 * @return a SymbolicCompleteArrayType of length extent and type elementType
	 */
	SymbolicCompleteArrayType arrayType(SymbolicType elementType,
			NumericExpression extent);

	/**
	 * Creates a SymbolicTupleType
	 * @param name the name of the SymbolicTupleType
	 * @param fieldTypes a finite, ordered typeSequence
	 * @return a SymbolicTupleType that contains a name and fieldTypes as typeSequence
	 */
	SymbolicTupleType tupleType(StringObject name,
			SymbolicTypeSequence fieldTypes);

	/**
	 * Creates a SymbolicUnionType
	 * @param name the name of the unionType
	 * @param memberTypes a typeSequence of the elements in the UnionType
	 * @return a SymbolicUnionType that contains a name and a memberTypes.
	 */
	SymbolicUnionType unionType(StringObject name,
			SymbolicTypeSequence memberTypes);

	/**
	 * Creates a SymbolicFunctionType, which represents an abstract mathematical function
	 * @param inputTypes a SymbolicTypeSequence of SymbolicTypes.
	 * @param outputType a SymbolicType that represents the output of the function
	 * @return a SymbolicFunctionType of sequence inputTypes and outputType.
	 */
	SymbolicFunctionType functionType(SymbolicTypeSequence inputTypes,
			SymbolicType outputType);

	/**
	 * Creates a TypeComparator that is used to compare SymbolicTypes
	 * @return a typeComparator to compare two symbolic types
	 */
	TypeComparator typeComparator();

	/**
	 * @return typeSequenceComparator that is used 
	 * when comparing two symbolic Tuple, Union, or Function types
	 */
	TypeSequenceComparator typeSequenceComparator();

	/**
	 * The pureType of t1 is t1 after removing the length
	 * for example, CommonSymbolicCompleteArrayType(type1) should have the same pureType as
	 * CommonSymbolicArrayType(type1)
	 * 
	 * @param type
	 * @return the pure type of the type
	 */
	SymbolicType pureType(SymbolicType type);

}
