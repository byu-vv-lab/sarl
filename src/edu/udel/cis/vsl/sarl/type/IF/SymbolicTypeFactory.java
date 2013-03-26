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

	void setExpressionComparator(Comparator<SymbolicExpression> c);

	void init();

	ObjectFactory objectFactory();

	SymbolicType booleanType();

	SymbolicIntegerType integerType();

	SymbolicIntegerType herbrandIntegerType();

	SymbolicIntegerType boundedIntegerType(NumericExpression min,
			NumericExpression max, boolean cyclic);

	SymbolicRealType realType();

	SymbolicRealType herbrandRealType();

	SymbolicTypeSequence sequence(Iterable<? extends SymbolicType> elements);

	SymbolicTypeSequence sequence(SymbolicType[] elements);

	SymbolicTypeSequence singletonSequence(SymbolicType type);

	SymbolicArrayType arrayType(SymbolicType elementType);

	SymbolicCompleteArrayType arrayType(SymbolicType elementType,
			NumericExpression extent);

	SymbolicTupleType tupleType(StringObject name,
			SymbolicTypeSequence fieldTypes);

	SymbolicUnionType unionType(StringObject name,
			SymbolicTypeSequence memberTypes);

	SymbolicFunctionType functionType(SymbolicTypeSequence inputTypes,
			SymbolicType outputType);

	TypeComparator typeComparator();

	TypeSequenceComparator typeSequenceComparator();

}
