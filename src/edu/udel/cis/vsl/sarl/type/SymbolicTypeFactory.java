package edu.udel.cis.vsl.sarl.type;

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
import edu.udel.cis.vsl.sarl.object.ObjectFactory;

public class SymbolicTypeFactory {

	private ObjectFactory objectFactory;

	private SymbolicPrimitiveType booleanType, integerType, realType;

	public SymbolicTypeFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
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

	public TypeComparator newTypeComparator() {
		return new TypeComparator();
	}

	public TypeSequenceComparator newTypeSequenceComparator() {
		return new TypeSequenceComparator();
	}

}
