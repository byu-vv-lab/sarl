package edu.udel.cis.vsl.sarl.simplify.IF;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.Simplifier;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection.SymbolicCollectionKind;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject.SymbolicObjectKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpression;

/**
 * A partial implementation of Simplifer which can be extended.
 * 
 * @author siegel
 * 
 */
public abstract class CommonSimplifier implements Simplifier {

	// why Simplification. Why not just say success iff original!=result.
	// i.e. routines must return same object if no simplification possible

	protected SymbolicUniverse universe;

	/** Cached simplifications. */
	protected Map<SymbolicObject, SymbolicObject> simplifyMap = new HashMap<SymbolicObject, SymbolicObject>();

	public CommonSimplifier(SymbolicUniverse universe) {
		this.universe = universe;
	}

	@Override
	public SymbolicUniverse universe() {
		return universe;
	}

	protected abstract NumericExpression simplifyNumeric(
			NumericExpression expression);

	protected SymbolicType simplifyTypeWork(SymbolicType type) {
		switch (type.typeKind()) {
		case BOOLEAN:
		case INTEGER:
		case REAL:
			return type;
		case ARRAY: {
			SymbolicArrayType arrayType = (SymbolicArrayType) type;
			SymbolicType elementType = arrayType.elementType();
			SymbolicType simplifiedElementType = simplifyType(elementType);

			if (arrayType.isComplete()) {
				SymbolicExpression extent = ((SymbolicCompleteArrayType) arrayType)
						.extent();
				SymbolicExpression simplifiedExtent = simplify(extent);

				if (elementType != simplifiedElementType
						|| extent != simplifiedExtent)
					return universe.arrayType(simplifiedElementType,
							simplifiedExtent);
				return arrayType;
			} else {
				if (elementType != simplifiedElementType)
					return universe.arrayType(simplifiedElementType);
				return arrayType;
			}
		}
		case FUNCTION: {
			SymbolicFunctionType functionType = (SymbolicFunctionType) type;
			SymbolicTypeSequence inputs = functionType.inputTypes();
			SymbolicTypeSequence simplifiedInputs = simplifyTypeSequence(inputs);
			SymbolicType output = functionType.outputType();
			SymbolicType simplifiedOutput = simplifyType(output);

			if (inputs != simplifiedInputs || output != simplifiedOutput)
				return universe
						.functionType(simplifiedInputs, simplifiedOutput);
			return type;
		}
		case TUPLE: {
			SymbolicTypeSequence sequence = ((SymbolicTupleType) type)
					.sequence();
			SymbolicTypeSequence simplifiedSequence = simplifyTypeSequence(sequence);

			if (simplifiedSequence != sequence)
				return universe.tupleType(((SymbolicTupleType) type).name(),
						simplifiedSequence);
			return type;
		}
		case UNION: {
			SymbolicTypeSequence sequence = ((SymbolicUnionType) type)
					.sequence();
			SymbolicTypeSequence simplifiedSequence = simplifyTypeSequence(sequence);

			if (simplifiedSequence != sequence)
				return universe.unionType(((SymbolicUnionType) type).name(),
						simplifiedSequence);
			return type;
		}
		default:
			throw new SARLInternalException("unreachable");
		}
	}

	protected SymbolicType simplifyType(SymbolicType type) {
		SymbolicType result = (SymbolicType) simplifyMap.get(type);

		if (result == null) {
			result = simplifyTypeWork(type);
			simplifyMap.put(type, result);
		}
		return result;
	}

	protected SymbolicTypeSequence simplifyTypeSequenceWork(
			SymbolicTypeSequence sequence) {
		int size = sequence.numTypes();

		for (int i = 0; i < size; i++) {
			SymbolicType type = sequence.getType(i);
			SymbolicType simplifiedType = simplifyType(type);

			if (type != simplifiedType) {
				SymbolicType[] newTypes = new SymbolicType[size];

				for (int j = 0; j < i; j++)
					newTypes[j] = sequence.getType(j);
				newTypes[i] = simplifiedType;
				for (int j = i + 1; j < size; j++)
					newTypes[j] = simplifyType(sequence.getType(j));
				return universe.typeSequence(newTypes);
			}
		}
		return sequence;
	}

	protected SymbolicTypeSequence simplifyTypeSequence(
			SymbolicTypeSequence sequence) {
		SymbolicTypeSequence result = (SymbolicTypeSequence) simplifyMap
				.get(sequence);

		if (result == null) {
			result = simplifyTypeSequenceWork(sequence);
			simplifyMap.put(sequence, result);
		}
		return result;
	}

	protected SymbolicCollection<?> simplifyCollectionWork(
			SymbolicCollection<?> collection) {
		// TODO
		SymbolicCollectionKind kind = collection.collectionKind();
		// BASIC, SET, SEQUENCE, MAP
		// have unary operator apply
		return null;
	}

	protected SymbolicCollection<?> simplifyCollection(
			SymbolicCollection<?> collection) {
		SymbolicCollection<?> result = (SymbolicCollection<?>) simplifyMap
				.get(collection);

		if (result == null) {
			result = simplifyCollectionWork(collection);
			simplifyMap.put(collection, result);
		}
		return result;
	}

	protected SymbolicObject simplifyObject(SymbolicObject object) {
		switch (object.symbolicObjectKind()) {
		case BOOLEAN:
		case INT:
		case NUMBER:
		case STRING:
			return object;
		case EXPRESSION:
			return simplify((SymbolicExpression) object);
		case EXPRESSION_COLLECTION:
			return simplifyCollection((SymbolicCollection<?>) object);
		case TYPE:
			return simplifyType((SymbolicType) object);
		case TYPE_SEQUENCE:
			return simplifyTypeSequence((SymbolicTypeSequence) object);
		default:
			throw new SARLInternalException("unreachable");
		}
	}

	protected SymbolicExpression simplifyGenericExpression(
			SymbolicExpression expression) {
		SymbolicOperator operator = expression.operator();

		if (operator == SymbolicOperator.CONCRETE) {
			SymbolicObject object = (SymbolicObject) expression.argument(0);
			SymbolicObjectKind kind = object.symbolicObjectKind();

			switch (kind) {
			case BOOLEAN:
			case INT:
			case NUMBER:
			case STRING:
				return expression;
			default:
			}
		}
		{
			SymbolicType type = expression.type();
			SymbolicType simplifiedType = simplifyType(type);
			int numArgs = expression.numArguments();
			SymbolicObject[] simplifiedArgs = null;

			if (type == simplifiedType) {
				for (int i = 0; i < numArgs; i++) {
					SymbolicObject arg = expression.argument(i);
					SymbolicObject simplifiedArg = simplifyObject(arg);

					if (simplifiedArg != arg) {
						simplifiedArgs = new SymbolicObject[numArgs];
						for (int j = 0; j < i; j++)
							simplifiedArgs[j] = expression.argument(j);
						simplifiedArgs[i] = simplifiedArg;
						for (int j = i + 1; j < numArgs; j++)
							simplifiedArgs[j] = simplifyObject(expression
									.argument(j));
					}
				}
			} else {
				simplifiedArgs = new SymbolicObject[numArgs];
				for (int i = 0; i < numArgs; i++)
					simplifiedArgs[i] = simplifyObject(expression.argument(i));
			}
			if (simplifiedArgs == null)
				return expression;
			return universe.make(operator, simplifiedType, simplifiedArgs);
		}
	}

	@Override
	public SymbolicExpression simplify(SymbolicExpression expression) {
		SymbolicExpression result = (SymbolicExpression) simplifyMap
				.get(expression);

		if (result == null) {
			if (expression.isNumeric())
				result = simplifyNumeric((NumericExpression) expression);
			else
				result = simplifyGenericExpression(expression);
			simplifyMap.put(expression, result);
		}
		return result;
	}

}
