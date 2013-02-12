package edu.udel.cis.vsl.sarl.type.common;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequenceIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionTypeIF;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class CommonSymbolicTypeFactory implements SymbolicTypeFactory {

	private ObjectFactory objectFactory;

	private TypeComparator typeComparator;

	private TypeSequenceComparator typeSequenceComparator;

	private SymbolicPrimitiveType booleanType, integerType, realType;

	public CommonSymbolicTypeFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
		typeComparator = new TypeComparator();
		typeSequenceComparator = new TypeSequenceComparator();
		typeComparator.setTypeSequenceComparator(typeSequenceComparator);
		typeSequenceComparator.setTypeComparator(typeComparator);
		booleanType = (SymbolicPrimitiveType) objectFactory
				.canonic(new SymbolicPrimitiveType(SymbolicTypeKind.BOOLEAN));
		integerType = (SymbolicPrimitiveType) objectFactory
				.canonic(new SymbolicPrimitiveType(SymbolicTypeKind.INTEGER));
		realType = (SymbolicPrimitiveType) objectFactory
				.canonic(new SymbolicPrimitiveType(SymbolicTypeKind.REAL));
	}

	public ObjectFactory objectFactory() {
		return objectFactory;
	}

	public SymbolicPrimitiveType booleanType() {
		return booleanType;
	}

	public SymbolicPrimitiveType integerType() {
		return integerType;
	}

	public SymbolicPrimitiveType realType() {
		return realType;
	}

	public SymbolicTypeSequenceIF sequence(Iterable<SymbolicTypeIF> elements) {
		return new SymbolicTypeSequence(elements);
	}

	public SymbolicTypeSequenceIF sequence(SymbolicTypeIF[] elements) {
		return new SymbolicTypeSequence(elements);
	}

	public SymbolicTypeSequenceIF singletonSequence(SymbolicTypeIF type) {
		return new SymbolicTypeSequence(new SymbolicTypeIF[] { type });
	}

	public SymbolicArrayTypeIF arrayType(SymbolicTypeIF elementType) {
		return new SymbolicArrayType(elementType);
	}

	public SymbolicCompleteArrayTypeIF arrayType(SymbolicTypeIF elementType,
			SymbolicExpressionIF extent) {
		return new SymbolicCompleteArrayType(elementType, extent);
	}

	public SymbolicTupleTypeIF tupleType(StringObject name,
			SymbolicTypeSequenceIF fieldTypes) {
		return new SymbolicTupleType(name, fieldTypes);
	}

	public SymbolicUnionTypeIF unionType(StringObject name,
			SymbolicTypeSequenceIF memberTypes) {
		return new SymbolicUnionType(name, memberTypes);
	}

	public SymbolicFunctionTypeIF functionType(
			SymbolicTypeSequenceIF inputTypes, SymbolicTypeIF outputType) {
		return new SymbolicFunctionType(inputTypes, outputType);
	}

	public TypeComparator typeComparator() {
		return typeComparator;
	}

	public TypeSequenceComparator typeSequenceComparator() {
		return typeSequenceComparator;
	}

	@Override
	public void setExpressionComparator(Comparator<SymbolicExpressionIF> c) {
		typeComparator.setExpressionComparator(c);
	}

	@Override
	public void init() {
		assert typeComparator.expressionComparator() != null;
	}

}
