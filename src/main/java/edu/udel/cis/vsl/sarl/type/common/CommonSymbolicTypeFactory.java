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
package edu.udel.cis.vsl.sarl.type.common;

import java.util.Arrays;
import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicMapType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType.RealKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicSetType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

/**
 * an implementation of {@link SymbolicTypeFactory}
 * 
 * @author alali
 * 
 */
public class CommonSymbolicTypeFactory implements SymbolicTypeFactory {

	/**
	 * an ObjectFactory object to be used to create a TypeFactory
	 */
	private ObjectFactory objectFactory;

	/**
	 * a TypeComparator to be used in comparing two types
	 */
	private TypeComparator typeComparator;

	/**
	 * a TypeSequenceComparator to be used in comparing SymbolicSequenceType
	 */
	private TypeSequenceComparator typeSequenceComparator;

	/**
	 * a booleanType CommonSymbolicPrimitiveType to be returned by the factory
	 */
	private CommonSymbolicPrimitiveType booleanType;

	/**
	 * a SymbolicIntegerType object to be returned by the factory
	 */
	private SymbolicIntegerType integerType, herbrandIntegerType;

	/**
	 * a SymbolicRealType objects to be returned by the factory
	 */
	private SymbolicRealType realType, herbrandRealType;

	/**
	 * a SymbolicType object to be returned for Char or
	 * CommonSymbolicPrimitiveType
	 */
	private SymbolicType characterType;

	/**
	 * Constructs a CommonSymbolicTypeFactory from an ObjectFactory
	 * 
	 * @param objectFactory
	 */
	public CommonSymbolicTypeFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
		typeComparator = new TypeComparator();
		typeSequenceComparator = new TypeSequenceComparator();
		typeComparator.setTypeSequenceComparator(typeSequenceComparator);
		typeSequenceComparator.setTypeComparator(typeComparator);
		booleanType = objectFactory.canonic(new CommonSymbolicPrimitiveType(
				SymbolicTypeKind.BOOLEAN));
		integerType = objectFactory.canonic(new CommonSymbolicIntegerType(
				IntegerKind.IDEAL));
		herbrandIntegerType = objectFactory
				.canonic(new CommonSymbolicIntegerType(IntegerKind.HERBRAND));
		realType = objectFactory.canonic(new CommonSymbolicRealType(
				RealKind.IDEAL));
		herbrandRealType = objectFactory.canonic(new CommonSymbolicRealType(
				RealKind.HERBRAND));
		characterType = objectFactory.canonic(new CommonSymbolicPrimitiveType(
				SymbolicTypeKind.CHAR));
		objectFactory.setTypeComparator(typeComparator);
		objectFactory.setTypeSequenceComparator(typeSequenceComparator);
	}

	@Override
	public ObjectFactory objectFactory() {
		return objectFactory;
	}

	@Override
	public CommonSymbolicPrimitiveType booleanType() {
		return booleanType;
	}

	@Override
	public SymbolicIntegerType integerType() {
		return integerType;
	}

	@Override
	public SymbolicIntegerType herbrandIntegerType() {
		return herbrandIntegerType;
	}

	@Override
	public SymbolicIntegerType boundedIntegerType(NumericExpression min,
			NumericExpression max, boolean cyclic) {
		// TODO
		throw new UnsupportedOperationException(
				"Bounded integer not yet supported");
	}

	@Override
	public SymbolicRealType realType() {
		return realType;
	}

	@Override
	public SymbolicRealType herbrandRealType() {
		return herbrandRealType;
	}

	@Override
	public SymbolicType characterType() {
		return characterType;
	}

	@Override
	public SymbolicTypeSequence sequence(
			Iterable<? extends SymbolicType> elements) {
		return new CommonSymbolicTypeSequence(elements);
	}

	@Override
	public SymbolicTypeSequence sequence(SymbolicType[] elements) {
		return new CommonSymbolicTypeSequence(elements);
	}

	@Override
	public SymbolicTypeSequence singletonSequence(SymbolicType type) {
		return new CommonSymbolicTypeSequence(new SymbolicType[] { type });
	}

	@Override
	public SymbolicArrayType arrayType(SymbolicType elementType) {
		return new CommonSymbolicArrayType(elementType);
	}

	@Override
	public SymbolicCompleteArrayType arrayType(SymbolicType elementType,
			NumericExpression extent) {
		return new CommonSymbolicCompleteArrayType(elementType, extent);
	}

	@Override
	public SymbolicTupleType tupleType(StringObject name,
			SymbolicTypeSequence fieldTypes) {
		return new CommonSymbolicTupleType(name, fieldTypes);
	}

	@Override
	public SymbolicUnionType unionType(StringObject name,
			SymbolicTypeSequence memberTypes) {
		return new CommonSymbolicUnionType(name, memberTypes);
	}

	@Override
	public SymbolicFunctionType functionType(SymbolicTypeSequence inputTypes,
			SymbolicType outputType) {
		return new CommonSymbolicFunctionType(inputTypes, outputType);
	}

	@Override
	public TypeComparator typeComparator() {
		return typeComparator;
	}

	@Override
	public TypeSequenceComparator typeSequenceComparator() {
		return typeSequenceComparator;
	}

	@Override
	public void setExpressionComparator(Comparator<SymbolicExpression> c) {
		typeComparator.setExpressionComparator(c);
	}

	@Override
	public void init() {
		assert typeComparator.expressionComparator() != null;
	}

	/**
	 * Private method, only called within this class Creates a
	 * SymbolicTypeSequence that contains the pureType of all the elements in
	 * the sequence
	 * 
	 * @param sequence
	 *            , a sequence of SymbolicTypes
	 * @return a sequence of pureType of elements in the original sequence
	 */
	private SymbolicTypeSequence pureSequence(SymbolicTypeSequence sequence) {
		int length = sequence.numTypes();

		for (int i = 0; i < length; i++) {
			SymbolicType type = sequence.getType(i);
			SymbolicType pureType = pureType(type);

			if (type != pureType) {
				SymbolicType[] pureList = new SymbolicType[length];

				for (int j = 0; j < i; j++)
					pureList[j] = sequence.getType(j);
				pureList[i] = pureType;
				for (int j = i + 1; j < length; j++)
					pureList[j] = pureType(sequence.getType(j));
				return sequence(pureList);
			}
		}
		return sequence;
	}

	@Override
	public SymbolicType pureType(SymbolicType type) {
		SymbolicType cached = ((CommonSymbolicType) type).getPureType();

		if (cached != null)
			return cached;
		switch (type.typeKind()) {
		case ARRAY: {
			CommonSymbolicArrayType arrayType = (CommonSymbolicArrayType) type;
			SymbolicType elementType = arrayType.elementType();
			SymbolicType pureElementType = pureType(elementType);
			CommonSymbolicArrayType result;

			if (elementType == pureElementType && !arrayType.isComplete())
				result = arrayType;
			else {
				result = (CommonSymbolicArrayType) arrayType(pureElementType);
				if (type.isCanonic())
					result = objectFactory.canonic(result);
				result.setPureType(result);
			}
			arrayType.setPureType(result);
			return result;
		}
		case FUNCTION: {
			CommonSymbolicFunctionType functionType = (CommonSymbolicFunctionType) type;
			SymbolicType outputType = functionType.outputType();
			SymbolicType pureOutputType = pureType(outputType);
			SymbolicTypeSequence inputs = functionType.inputTypes();
			SymbolicTypeSequence pureInputs = pureSequence(inputs);
			CommonSymbolicFunctionType result;

			if (outputType == pureOutputType && inputs == pureInputs)
				result = functionType;
			else {
				result = (CommonSymbolicFunctionType) functionType(pureInputs,
						pureOutputType);
				if (type.isCanonic())
					result = objectFactory.canonic(result);
				result.setPureType(result);
			}
			functionType.setPureType(result);
			return result;
		}
		case TUPLE: {
			CommonSymbolicTupleType tupleType = (CommonSymbolicTupleType) type;
			SymbolicTypeSequence sequence = tupleType.sequence();
			SymbolicTypeSequence pureSequence = pureSequence(sequence);
			CommonSymbolicTupleType result;

			if (sequence == pureSequence)
				result = tupleType;
			else {
				result = (CommonSymbolicTupleType) tupleType(tupleType.name(),
						pureSequence);
				if (type.isCanonic())
					result = objectFactory.canonic(result);
				result.setPureType(result);
			}
			tupleType.setPureType(result);
			return result;
		}
		case UNION: {
			CommonSymbolicUnionType unionType = (CommonSymbolicUnionType) type;
			SymbolicTypeSequence sequence = unionType.sequence();
			SymbolicTypeSequence pureSequence = pureSequence(sequence);
			CommonSymbolicUnionType result;

			if (sequence == pureSequence)
				result = unionType;
			else {
				result = (CommonSymbolicUnionType) unionType(unionType.name(),
						pureSequence);
				if (type.isCanonic())
					result = objectFactory.canonic(result);
				result.setPureType(result);
			}
			unionType.setPureType(result);
			return result;
		}
		case SET: {
			CommonSymbolicSetType setType = (CommonSymbolicSetType) type;
			SymbolicType elementType = setType.elementType();
			SymbolicType pureElementType = pureType(elementType);
			CommonSymbolicSetType result;

			if (elementType == pureElementType)
				result = setType;
			else {
				result = (CommonSymbolicSetType) setType(pureElementType);
				if (type.isCanonic())
					result = objectFactory.canonic(result);
				result.setPureType(result);
			}
			setType.setPureType(result);
			return result;
		}
		case MAP: {
			CommonSymbolicMapType mapType = (CommonSymbolicMapType) type;
			SymbolicType keyType = mapType.keyType();
			SymbolicType pureKeyType = pureType(keyType);
			SymbolicType valueType = mapType.valueType();
			SymbolicType pureValueType = pureType(valueType);
			CommonSymbolicMapType result;

			if (keyType == pureKeyType && valueType == pureValueType)
				result = mapType;
			else {
				result = (CommonSymbolicMapType) mapType(pureKeyType,
						pureValueType);
				if (type.isCanonic())
					result = objectFactory.canonic(result);
				result.setPureType(result);
			}
			mapType.setPureType(result);
			return result;
		}
		case BOOLEAN:
		case CHAR:
		case INTEGER:
		case REAL:
			throw new SARLInternalException(
					"unreachable because these types always return themselves");
		}
		throw new SARLInternalException(
				"unreachable because the switch covers all cases");
	}

	@Override
	public SymbolicSetType setType(SymbolicType elementType) {
		return new CommonSymbolicSetType(elementType);
	}

	@Override
	public SymbolicMapType mapType(SymbolicType keyType, SymbolicType valueType) {
		return new CommonSymbolicMapType(keyType, valueType);
	}

	@Override
	public SymbolicTupleType entryType(SymbolicMapType mapType) {
		SymbolicTupleType result = ((CommonSymbolicMapType) mapType)
				.getEntryType();

		if (result != null)
			return result;
		result = tupleType(objectFactory.stringObject("Entry"),
				sequence(Arrays.asList(mapType.keyType(), mapType.valueType())));
		if (mapType.isCanonic())
			result = objectFactory.canonic(result);
		((CommonSymbolicMapType) mapType).setEntryType(result);
		return result;
	}

}
