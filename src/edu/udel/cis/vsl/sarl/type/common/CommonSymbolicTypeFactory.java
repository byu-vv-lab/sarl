/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.type.common;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType.RealKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class CommonSymbolicTypeFactory implements SymbolicTypeFactory {

	private ObjectFactory objectFactory;

	private TypeComparator typeComparator;

	private TypeSequenceComparator typeSequenceComparator;

	private CommonSymbolicPrimitiveType booleanType;

	private SymbolicIntegerType integerType, herbrandIntegerType;

	private SymbolicRealType realType, herbrandRealType;

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

}
