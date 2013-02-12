package edu.udel.cis.vsl.sarl.type.IF;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequenceIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionTypeIF;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.common.SymbolicPrimitiveType;
import edu.udel.cis.vsl.sarl.type.common.TypeComparator;
import edu.udel.cis.vsl.sarl.type.common.TypeSequenceComparator;

public interface SymbolicTypeFactory {

	void setExpressionComparator(Comparator<SymbolicExpressionIF> c);

	void init();

	ObjectFactory objectFactory();

	SymbolicPrimitiveType booleanType();

	SymbolicPrimitiveType integerType();

	SymbolicPrimitiveType realType();

	SymbolicTypeSequenceIF sequence(Iterable<SymbolicTypeIF> elements);

	SymbolicTypeSequenceIF sequence(SymbolicTypeIF[] elements);

	SymbolicTypeSequenceIF singletonSequence(SymbolicTypeIF type);

	SymbolicArrayTypeIF arrayType(SymbolicTypeIF elementType);

	SymbolicCompleteArrayTypeIF arrayType(SymbolicTypeIF elementType,
			SymbolicExpressionIF extent);

	SymbolicTupleTypeIF tupleType(StringObject name,
			SymbolicTypeSequenceIF fieldTypes);

	SymbolicUnionTypeIF unionType(StringObject name,
			SymbolicTypeSequenceIF memberTypes);

	SymbolicFunctionTypeIF functionType(SymbolicTypeSequenceIF inputTypes,
			SymbolicTypeIF outputType);

	TypeComparator typeComparator();

	TypeSequenceComparator typeSequenceComparator();

}
