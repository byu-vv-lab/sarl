package edu.udel.cis.vsl.sarl.type.common;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
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

	private CommonSymbolicPrimitiveType booleanType, integerType, realType;

	public CommonSymbolicTypeFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
		typeComparator = new TypeComparator();
		typeSequenceComparator = new TypeSequenceComparator();
		typeComparator.setTypeSequenceComparator(typeSequenceComparator);
		typeSequenceComparator.setTypeComparator(typeComparator);
		booleanType = objectFactory.canonic(new CommonSymbolicPrimitiveType(
				SymbolicTypeKind.BOOLEAN));
		integerType = objectFactory.canonic(new CommonSymbolicPrimitiveType(
				SymbolicTypeKind.INTEGER));
		realType = objectFactory.canonic(new CommonSymbolicPrimitiveType(
				SymbolicTypeKind.REAL));
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
	public CommonSymbolicPrimitiveType integerType() {
		return integerType;
	}

	@Override
	public CommonSymbolicPrimitiveType realType() {
		return realType;
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
