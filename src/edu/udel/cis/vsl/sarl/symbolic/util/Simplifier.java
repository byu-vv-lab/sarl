package edu.udel.cis.vsl.sarl.symbolic.util;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.symbolic.IF.SimplifierIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.SymbolicUniverseIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicArrayTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicCompleteArrayTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicFunctionTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTupleTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF.SymbolicTypeKind;

/**
 * Provides partial implementatino of the SimplifierIF interface for simplifying
 * symbolic expressions.
 * 
 * @author siegel
 */
public abstract class Simplifier implements SimplifierIF {

	protected SymbolicUniverseIF universe;

	private Map<SymbolicTypeIF, SymbolicTypeIF> typeMap = new HashMap<SymbolicTypeIF, SymbolicTypeIF>();

	public Simplifier(SymbolicUniverseIF universe) {
		this.universe = universe;
	}

	@Override
	public abstract SymbolicExpressionIF newAssumption();

	@Override
	public abstract SymbolicExpressionIF simplify(
			SymbolicExpressionIF expression);

	@Override
	public SymbolicUniverseIF universe() {
		return universe;
	}

	public SymbolicTypeIF simplifyType(SymbolicTypeIF type) {
		SymbolicTypeIF result = typeMap.get(type);
		if (result != null)
			return result;
		SymbolicTypeKind kind = type.kind();

		switch (kind) {
		case BOOLEAN:
		case INTEGER:
		case REAL:
			result = type;
			break;
		case ARRAY: {
			SymbolicArrayTypeIF arrayType = (SymbolicArrayTypeIF) type;
			SymbolicTypeIF oldElementType = arrayType.elementType();
			SymbolicTypeIF newElementType = simplifyType(oldElementType);

			if (arrayType instanceof SymbolicCompleteArrayTypeIF) {
				SymbolicExpressionIF oldExtent = ((SymbolicCompleteArrayTypeIF) arrayType)
						.extent();
				SymbolicExpressionIF newExtent = simplify(oldExtent);

				if (newElementType == oldElementType && newExtent == oldExtent)
					result = type;
				else
					result = universe.arrayType(newElementType, newExtent);
			} else {
				if (newElementType == oldElementType)
					result = type;
				else
					result = universe.arrayType(newElementType);
			}

			break;
		}
		case TUPLE: {
			SymbolicTupleTypeIF tupleType = (SymbolicTupleTypeIF) type;
			int numFields = tupleType.numFields();
			SymbolicTypeIF[] newFieldTypes = new SymbolicTypeIF[numFields];
			boolean change = false;

			for (int i = 0; i < numFields; i++) {
				SymbolicTypeIF oldFieldType = tupleType.fieldType(i);

				newFieldTypes[i] = simplifyType(oldFieldType);
				change = change || newFieldTypes[i] != oldFieldType;
			}
			result = (change ? universe.tupleType(tupleType.name(),
					newFieldTypes) : type);
			break;
		}
		case FUNCTION: {
			SymbolicFunctionTypeIF functionType = (SymbolicFunctionTypeIF) type;
			int numInputs = functionType.numInputs();
			SymbolicTypeIF oldOutputType = functionType.outputType();
			SymbolicTypeIF newOutputType = simplifyType(oldOutputType);
			boolean change = (newOutputType != oldOutputType);
			SymbolicTypeIF[] newInputTypes = new SymbolicTypeIF[numInputs];

			for (int i = 0; i < numInputs; i++) {
				SymbolicTypeIF oldInputType = functionType.inputType(i);
				SymbolicTypeIF newInputType = simplifyType(oldInputType);

				change = change || oldInputType != newInputType;
				newInputTypes[i] = newInputType;
			}
			result = (change ? universe.functionType(newInputTypes,
					newOutputType) : type);
			break;
		}
		default:
			throw new RuntimeException(
					"TASS internal error: unknown symbolic type: " + type);
		}
		// if (configuration().verbose()) {
		// configuration().out().println(
		// "Result of simplify applied to symbolic type\n  " + type
		// + "\nis\n  " + result + "\n");
		// configuration().out().flush();
		// }
		typeMap.put(type, result);
		return result;
	}

}
