package edu.udel.cis.vsl.sarl.prove.cvc;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.CharObject;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.util.FastList;
import edu.udel.cis.vsl.sarl.util.Pair;

/**
 * <p>
 * A CVC4Translator object is used to translate a single symbolic expression to
 * the language of CVC4. The expression is specified and the translation takes
 * place at construction time. The result is available in two parts: the
 * resulting declarations and the resulting translated expression. These are
 * obtained by two getter methods: {@link #getDeclarations()} and
 * {@link #getTranslation()}, respectively.
 * </p>
 * 
 * <p>
 * In SARL, a complete array type T[N] is considered a subtype of the incomplete
 * type T[]. Therefore an expression of type T[N] may be used wherever one of
 * type T[] is expected. For example, a function that takes something of type
 * T[] as an argument may be called with something of type T[N]. The translation
 * must take this into account by using the same representation for expressions
 * of both types. This translator represents any such expression as an ordered
 * pair (len,val), where len is an expression of integer type representing the
 * length of the array object, an val is an expression of array type (with
 * domain the set of integers) specifying the values of the array.
 * </p>
 * 
 * <p>
 * CVC4 does not like tuples of length 1. Therefore all SARL tuples (x) of
 * length 1 are translated to just x, and the SARL tuple type [T] is translated
 * to just T.
 * </p>
 * 
 * @author siegel
 *
 */
public class CVC4Translator {

	private PreUniverse universe;

	/**
	 * The number of auxiliary CVC4 variables created. These are the variables
	 * that do not correspond to any SARL variable but are needed for some
	 * reason to translate an expression. Includes both ordinary and bound CVC4
	 * variables.
	 */
	private int auxVarCount;

	private int sarlAuxVarCount;

	/**
	 * Mapping of SARL symbolic expression to corresponding CVC4 expresssion.
	 * Used to cache the results of translation.
	 */
	private Map<SymbolicExpression, FastList<String>> expressionMap;

	/**
	 * Mapping of pairs (t1,t2) of SARL types to the uninterpreted function
	 * symbol which represents casting from t1 to t2. The function has type
	 * "function from translate(t1) to translate(t2)".
	 */
	private Map<Pair<SymbolicType, SymbolicType>, String> castMap;

	/**
	 * Map from SARL symbolic constants to corresponding CVC4 expressions.
	 * Entries are a subset of those of {@link #expressionMap}.
	 */
	private Map<SymbolicConstant, FastList<String>> variableMap;

	/**
	 * Mapping of SARL symbolic type to corresponding CVC4 type. Used to cache
	 * results of translation.
	 */
	private Map<SymbolicType, FastList<String>> typeMap;

	/**
	 * The declarations section resulting from the translation. This contains
	 * all the declarations of symbols used in the resulting CVC4 input.
	 */
	private FastList<String> cvcDeclarations;

	/**
	 * The expression which is the result of translating the given symbolic
	 * expression.
	 */
	private FastList<String> cvcTranslation;

	// Constructors...

	CVC4Translator(PreUniverse universe, SymbolicExpression theExpression) {
		this.universe = universe;
		this.auxVarCount = 0;
		this.sarlAuxVarCount = 0;
		this.expressionMap = new HashMap<>();
		this.castMap = new HashMap<>();
		this.variableMap = new HashMap<>();
		this.typeMap = new HashMap<>();
		this.cvcDeclarations = new FastList<>();
		this.cvcTranslation = translate(theExpression);
	}

	CVC4Translator(CVC4Translator startingContext,
			SymbolicExpression theExpression) {
		this.universe = startingContext.universe;
		this.auxVarCount = startingContext.auxVarCount;
		this.sarlAuxVarCount = startingContext.sarlAuxVarCount;
		this.expressionMap = new HashMap<>(startingContext.expressionMap);
		this.castMap = new HashMap<>(startingContext.castMap);
		this.variableMap = new HashMap<>(startingContext.variableMap);
		this.typeMap = new HashMap<>(startingContext.typeMap);
		this.cvcDeclarations = new FastList<>();
		this.cvcTranslation = translate(theExpression);
	}

	// Private methods...

	/**
	 * Computes the name of the index-th selector function into a union type.
	 * This is the function that taken an element of the union and returns an
	 * element of the index-th member type.
	 * 
	 * @param unionType
	 *            a union type
	 * @param index
	 *            integer in [0,n), where n is the number of member types of the
	 *            union type
	 * @return the name of the index-th selector function
	 */
	private String selector(SymbolicUnionType unionType, int index) {
		return unionType.name().toString() + "_extract_" + index;
	}

	/**
	 * Computes the name of the index-th constructor function for a union type.
	 * This is the function which takes as input an element of the index-th
	 * member type and returns an element of the union type.
	 * 
	 * @param unionType
	 *            a union type
	 * @param index
	 *            an integer in [0,n), where n is the number of member types of
	 *            the union type
	 * @return the name of the index-th constructor function
	 */
	private String constructor(SymbolicUnionType unionType, int index) {
		return unionType.name().toString() + "_inject_" + index;
	}

	/**
	 * Creates a new CVC4 (ordinary) variable of given type with unique name;
	 * increments {@link #auxVarCount}.
	 * 
	 * @param type
	 *            a CVC4 type; it is consumed, so cannot be used after invoking
	 *            this method
	 * @return the new CVC4 variable
	 */
	private String newAuxVar(FastList<String> type) {
		String name = "tmp" + auxVarCount;

		cvcDeclarations.addAll(name, ":");
		cvcDeclarations.append(type);
		cvcDeclarations.add(";\n");
		auxVarCount++;
		return name;
	}

	/**
	 * Returns a new SARL symbolic constant of integer type. Increments
	 * {@link #sarlAuxVarCount}.
	 * 
	 * @return new symbolic constant of integer type
	 */
	private NumericSymbolicConstant newSarlAuxVar() {
		NumericSymbolicConstant result = (NumericSymbolicConstant) universe
				.symbolicConstant(
						universe.stringObject("_i" + sarlAuxVarCount),
						universe.integerType());

		sarlAuxVarCount++;
		return result;
	}

	/**
	 * Creates "big array" expression: an ordered pair consisting of an integer
	 * expression which is the length of the array, and an expression of array
	 * type which is the contents.
	 * 
	 * @param length
	 *            CVC expression yielding length of array; it is consumed (so
	 *            cannot be used after invoking this method)
	 * @param value
	 *            CVC expression of type "array-of-T"; it is consumed (so cannot
	 *            be used after invoking this method)
	 * @return ordered pair (tuple), consisting of length and value
	 */
	private FastList<String> bigArray(FastList<String> length,
			FastList<String> value) {
		FastList<String> result = new FastList<String>();

		result.addAll("(");
		result.append(length);
		result.add(",");
		result.append(value);
		result.add(")");
		return result;
	}

	/**
	 * <p>
	 * Given a SARL expression of array type, this method computes the CVC
	 * representation of the length of that array. This is a CVC expression of
	 * integer type.
	 * </p>
	 * 
	 * <p>
	 * Convergence criterion: this method calls {@link #translate}, and
	 * {@link #translate} calls this method. In order for the recursion to
	 * terminate, the following protocol must be followed: {@link #translate}
	 * should never call this method on its entire argument; it should only call
	 * this method on a proper sub-tree of its argument.
	 * </p>
	 * 
	 * @param array
	 *            a SARL expression of array type
	 * @return translation into CVC of length of that array
	 */
	private FastList<String> lengthOfArray(SymbolicExpression array) {
		SymbolicArrayType type = (SymbolicArrayType) array.type();

		// imagine translating "length(a)" for a symbolic constant a.
		// this calls lengthOfArray(a). This calls translate(a).
		// Since a is a symbolic constant, this yields a CVC symbolic
		// constant A. The result returned is "(A).0".

		if (type instanceof SymbolicCompleteArrayType)
			return translate(((SymbolicCompleteArrayType) type).extent());
		// there are three kinds of array expressions for which translation
		// results in a literal ordered pair [int,array]: CONCRETE,
		// ARRAY_WRITE, DENSE_ARRAY_WRITE. A CONCRETE array always
		// has complete type.
		switch (array.operator()) {
		case CONCRETE:
			throw new SARLInternalException("Unreachable");
		case ARRAY_WRITE:
		case DENSE_ARRAY_WRITE:
			return lengthOfArray((SymbolicExpression) array.argument(0));
		default:
			FastList<String> result = new FastList<>("(");

			result.append(translate(array));
			result.add(").0");
			return result;
		}
	}

	private FastList<String> pretranslateConcreteArray(SymbolicExpression array) {
		SymbolicCompleteArrayType arrayType = (SymbolicCompleteArrayType) array
				.type();
		SymbolicType elementType = arrayType.elementType();
		NumericExpression extentExpression = arrayType.extent();
		IntegerNumber extentNumber = (IntegerNumber) universe
				.extractNumber(extentExpression);
		SymbolicSequence<?> elements = (SymbolicSequence<?>) array.argument(0);
		int size = elements.size();
		FastList<String> cvcArrayType = new FastList<>("ARRAY INT OF (");

		cvcArrayType.append(translateType(elementType));
		cvcArrayType.add(")");
		assert extentNumber != null && extentNumber.intValue() == size;

		FastList<String> result = new FastList<>(newAuxVar(cvcArrayType));

		if (size > 0) {
			result.add(" WITH [0] := (");
			result.append(translate(elements.get(0)));
			result.add(")");
			for (int i = 1; i < size; i++) {
				result.addAll(", [", Integer.toString(i), "] := (");
				result.append(translate(elements.get(i)));
				result.add(")");
			}
		}
		return result;
	}

	private FastList<String> pretranslateArrayWrite(
			SymbolicExpression arrayWrite) {
		// syntax: a WITH [10] := 2/3
		SymbolicExpression arrayExpression = (SymbolicExpression) arrayWrite
				.argument(0);
		NumericExpression indexExpression = (NumericExpression) arrayWrite
				.argument(1);
		SymbolicExpression valueExpression = (SymbolicExpression) arrayWrite
				.argument(2);
		FastList<String> result = new FastList<>("(");

		result.append(valueOfArray(arrayExpression));
		result.add(") WITH [");
		result.append(translate(indexExpression));
		result.add("] := (");
		result.append(translate(valueExpression));
		result.add(")");
		return result;
	}

	private FastList<String> pretranslateDenseArrayWrite(
			SymbolicExpression denseArrayWrite) {
		// syntax: a WITH [10] := 2/3, [42] := 3/2;
		SymbolicExpression arrayExpression = (SymbolicExpression) denseArrayWrite
				.argument(0);
		SymbolicSequence<?> elements = (SymbolicSequence<?>) denseArrayWrite
				.argument(1);
		int n = elements.size();
		FastList<String> result = new FastList<>("(");
		boolean first = true;

		result.append(valueOfArray(arrayExpression));
		result.add(")");
		for (int i = 0; i < n; i++) {
			SymbolicExpression element = elements.get(i);

			if (!element.isNull()) {
				if (first) {
					result.add(" WITH ");
					first = false;
				} else {
					result.add(", ");
				}
				result.addAll("[", Integer.toString(i), "] := (");
				result.append(translate(element));
				result.add(")");
			}
		}
		return result;
	}

	/**
	 * Given a SARL expression of array type, this method computes the CVC
	 * representation of array type corresponding to that array. The result will
	 * be a CVC expression of type array-of-T, where T is the element type.
	 * 
	 * @param array
	 * @return
	 */
	private FastList<String> valueOfArray(SymbolicExpression array) {
		// the idea is to catch any expression which would be translated
		// as an explicit ordered pair [len,val] and return just the val.
		// for expressions that are not translated to an explicit
		// ordered pair, just append ".1" to get the array value component.
		switch (array.operator()) {
		case CONCRETE: {
			return pretranslateConcreteArray(array);
		}
		case ARRAY_WRITE: {
			return pretranslateArrayWrite(array);
		}
		case DENSE_ARRAY_WRITE: {
			return pretranslateDenseArrayWrite(array);
		}
		default: {
			FastList<String> result = new FastList<>("(");

			result.append(translate(array));
			result.add(").1");
			return result;
		}
		}
	}

	/**
	 * Translates a concrete SARL array into language of CVC.
	 * 
	 * @param arrayType
	 *            a SARL complete array type
	 * @param elements
	 *            a sequence of elements whose types are all the element type of
	 *            the arrayType
	 * @return CVC translation of the concrete array
	 */
	private FastList<String> translateConcreteArray(SymbolicExpression array) {
		FastList<String> result = pretranslateConcreteArray(array);
		int size = ((SymbolicSequence<?>) array.argument(0)).size();

		result = bigArray(new FastList<>(Integer.toString(size)), result);
		return result;
	}

	/**
	 * Translates any concrete SymbolicExpression with concrete type to
	 * equivalent CVC4 Expr using the ExprManager.
	 * 
	 * @param expr
	 * @return the CVC4 equivalent Expr
	 */
	private FastList<String> translateConcrete(SymbolicExpression expr) {
		SymbolicType type = expr.type();
		SymbolicTypeKind kind = type.typeKind();
		SymbolicObject object = expr.argument(0);
		FastList<String> result;

		switch (kind) {
		case ARRAY:
			result = translateConcreteArray(expr);
			break;
		case BOOLEAN:
			result = new FastList<>(
					((BooleanObject) object).getBoolean() ? "TRUE" : "FALSE");
			break;
		case CHAR:
			result = new FastList<>(
					Integer.toString((int) ((CharObject) object).getChar()));
			break;
		case INTEGER:
		case REAL:
			result = new FastList<>(object.toString());
			break;
		case TUPLE: {
			// syntax:(x,y,z)
			SymbolicSequence<?> sequence = (SymbolicSequence<?>) object;
			int n = sequence.size();

			if (n == 1) {
				result = translate(sequence.get(0));
			} else {
				result = new FastList<String>("(");
				if (n > 0) { // possible to have tuple with 0 components
					result.append(translate(sequence.get(0)));
					for (int i = 1; i < n; i++) {
						result.add(",");
						result.append(translate(sequence.get(i)));
					}
				}
				result.add(")");
			}
			break;
		}
		default:
			throw new SARLInternalException("Unknown concrete object: " + expr);
		}
		return result;
	}

	/**
	 * Translates a symbolic constant. It returns simply the name of the
	 * symbolic constant (in the form of a <code>FastList</code> of strings).
	 * For an ordinary (i.e., not quantified) symbolic constant, this method
	 * also adds to {@link #cvcDeclarations} a declaration of the symbolic
	 * constant.
	 * 
	 * @param symbolicConstant
	 *            a SARL symbolic constant
	 * @param isBoundVariable
	 *            is this a bound variable?
	 * @return the name of the symbolic constant as a fast string list
	 */
	private FastList<String> translateSymbolicConstant(
			SymbolicConstant symbolicConstant, boolean isBoundVariable) {
		String name = symbolicConstant.name().getString();
		SymbolicType symbolicType = symbolicConstant.type();
		FastList<String> type = translateType(symbolicType);
		FastList<String> result = new FastList<>(name);

		if (!isBoundVariable) {
			cvcDeclarations.addAll(name, " : ");
			cvcDeclarations.append(type);
			cvcDeclarations.add(";\n");
		}
		this.variableMap.put(symbolicConstant, result);
		return result.clone();
	}

	/**
	 * Syntax example:
	 * 
	 * <pre>
	 * LAMBDA (x: REAL, i:INT): x + i - 1
	 * </pre>
	 * 
	 * @param expr
	 * @return
	 */
	private FastList<String> translateLambda(SymbolicExpression expr) {
		FastList<String> result = new FastList<>("LAMBDA (",
				((SymbolicConstant) expr.argument(0)).name().getString(), ":");

		result.append(translateType(expr.type()));
		result.add("):");
		result.append(translate((SymbolicExpression) expr.argument(1)));
		return result;
	}

	/**
	 * Translates an array-read expression a[i] into equivalent CVC4 expression
	 * 
	 * @param expr
	 *            a SARL symbolic expression of form a[i]
	 * @return an equivalent CVC4 expression
	 */
	private FastList<String> translateArrayRead(SymbolicExpression expr) {
		SymbolicExpression arrayExpression = (SymbolicExpression) expr
				.argument(0);
		NumericExpression indexExpression = (NumericExpression) expr
				.argument(1);
		FastList<String> result = new FastList<>("(");

		result.append(valueOfArray(arrayExpression));
		result.add(")[");
		result.append(translate(indexExpression));
		result.add("]");
		return result;
	}

	/**
	 * Translates an tuple-read expression t.i into equivalent CVC4 expression.
	 * 
	 * Recall: TUPLE_READ: 2 arguments: arg0 is the tuple expression. arg1 is an
	 * IntObject giving the index in the tuple.
	 * 
	 * @param expr
	 *            a SARL symbolic expression of form t.i
	 * @return an equivalent CVC4 expression
	 */
	private FastList<String> translateTupleRead(SymbolicExpression expr) {
		SymbolicExpression tupleExpression = (SymbolicExpression) expr
				.argument(0);
		int tupleLength = ((SymbolicTupleType) expr.type()).sequence()
				.numTypes();
		int index = ((IntObject) expr.argument(1)).getInt();

		if (tupleLength == 1) {
			assert index == 0;
			return translate((SymbolicExpression) expr.argument(0));
		} else {
			FastList<String> result = new FastList<>("(");

			result.append(translate(tupleExpression));
			result.add(")." + index);
			return result;
		}
	}

	/**
	 * Translates an array-write (or array update) SARL symbolic expression to
	 * equivalent CVC4 expression.
	 * 
	 * @param expr
	 *            a SARL array update expression "a WITH [i] := v"
	 * @return the result of translating to CVC4
	 */
	private FastList<String> translateArrayWrite(SymbolicExpression expr) {
		FastList<String> result = pretranslateArrayWrite(expr);

		result = bigArray(lengthOfArray(expr), result);
		return result;
	}

	/**
	 * Translates a tuple-write (or tuple update) SARL symbolic expression to
	 * equivalent CVC4 expression.
	 * 
	 * Recall: TUPLE_WRITE: 3 arguments: arg0 is the original tuple expression,
	 * arg1 is an IntObject giving the index, arg2 is the new value to write
	 * into the tuple.
	 * 
	 * @param expr
	 *            a SARL tuple update expression
	 * @return the result of translating to CVC4
	 */
	private FastList<String> translateTupleWrite(SymbolicExpression expr) {
		SymbolicExpression tupleExpression = (SymbolicExpression) expr
				.argument(0);
		int index = ((IntObject) expr.argument(1)).getInt();
		SymbolicExpression valueExpression = (SymbolicExpression) expr
				.argument(2);
		int tupleLength = ((SymbolicTupleType) expr.type()).sequence()
				.numTypes();

		if (tupleLength == 1) {
			assert index == 0;
			return translate(valueExpression);
		} else {
			FastList<String> result = new FastList<>("(");

			result.append(translate(tupleExpression));
			result.addAll(") WITH .", Integer.toString(index), " := (");
			result.append(translate(valueExpression));
			result.add(")");
			return result;
		}
	}

	/**
	 * Translates a multiple array-write (or array update) SARL symbolic
	 * expression to equivalent CVC4 expression.
	 * 
	 * @param expr
	 *            a SARL expression of kind DENSE_ARRAY_WRITE
	 * @return the result of translating expr to CVC4
	 */
	private FastList<String> translateDenseArrayWrite(SymbolicExpression expr) {
		FastList<String> result = pretranslateDenseArrayWrite(expr);

		result = bigArray(lengthOfArray(expr), result);
		return result;
	}

	/**
	 * Translate a multiple tuple-write (or tuple update) SARL symbolic
	 * expression to equivalent CVC4 expression.
	 * 
	 * @param expr
	 *            a SARL expression of kind
	 *            {@link SymbolicOperator#DENSE_TUPLE_WRITE}
	 * @return result of translating to CVC4
	 */
	private FastList<String> translateDenseTupleWrite(SymbolicExpression expr) {
		SymbolicExpression tupleExpression = (SymbolicExpression) expr
				.argument(0);
		int tupleLength = ((SymbolicTupleType) expr.type()).sequence()
				.numTypes();
		// syntax t WITH .0 := blah, .1 := blah, ...
		SymbolicSequence<?> values = (SymbolicSequence<?>) expr.argument(1);
		int numValues = values.size();

		if (numValues == 0) {
			return translate(tupleExpression);
		}
		if (tupleLength == 1) {
			assert numValues == 1;
			SymbolicExpression value = values.get(0);

			if (value.isNull()) {
				return translate(tupleExpression);
			} else {
				return translate(value);
			}
		} else {
			FastList<String> result = new FastList<>("(");
			boolean first = true;

			result.append(translate(tupleExpression));
			result.add(")");
			for (int i = 0; i < numValues; i++) {
				SymbolicExpression value = values.get(i);

				if (!value.isNull()) {
					if (first) {
						result.add(" WITH ");
						first = false;
					} else {
						result.add(", ");
					}
					result.addAll(".", Integer.toString(i), " := (");
					result.append(translate(value));
					result.add(")");
				}
			}
			return result;
		}
	}

	/**
	 * Translates SymbolicExpressions of the type "exists" and "forall" into the
	 * CVC4 equivalent.
	 * 
	 * @param expr
	 *            a SARL "exists" or "forall" expression
	 * @return result of translating to CVC4
	 */
	private FastList<String> translateQuantifier(SymbolicExpression expr) {
		// syntax: FORALL (x : T) : pred
		SymbolicOperator kind = expr.operator();
		SymbolicConstant boundVariable = (SymbolicConstant) expr.argument(0);
		String name = boundVariable.name().getString();
		BooleanExpression predicate = (BooleanExpression) expr.argument(1);
		FastList<String> result = new FastList<>(kind.toString());

		expressionMap.put(boundVariable, new FastList<>(name));
		result.addAll(" (", name, " : ");
		result.append(translateType(boundVariable.type()));
		result.add(") : (");
		result.append(translate(predicate));
		result.add(")");
		return result;
	}

	private FastList<String> processEquality(SymbolicExpression expr1,
			SymbolicExpression expr2) {
		FastList<String> result;

		if (expr1.type().typeKind() == SymbolicTypeKind.ARRAY) {
			// lengths are equal and forall i (0<=i<length).a[i]=b[i].
			// syntax: ((len1) = (len2)) AND
			// FORALL (i : INT) : (( 0 <= i AND i < len1) => ( ... ))
			FastList<String> extent1 = lengthOfArray(expr1);
			NumericSymbolicConstant index = newSarlAuxVar();
			String indexString = index.name().getString();
			SymbolicExpression read1 = universe.arrayRead(expr1, index);
			SymbolicExpression read2 = universe.arrayRead(expr2, index);

			result = new FastList<>("((");
			result.append(extent1.clone());
			result.add(") = (");
			result.append(lengthOfArray(expr2));
			result.addAll(")) AND FORALL (", indexString, " : INT) : ((0 <= ",
					indexString, " AND ", indexString, " < ");
			result.append(extent1);
			result.add(") => (");
			result.append(processEquality(read1, read2));
			result.add(")");
		} else {
			result = new FastList<>("(");
			result.append(translate(expr1));
			result.add(") = (");
			result.append(translate(expr2));
			result.add(")");
		}
		return result;
	}

	/**
	 * Translates a SymbolicExpression that represents a = b into the CVC4
	 * equivalent.
	 * 
	 * @param expr
	 *            the equals type expression
	 * @return the equivalent CVC4
	 */
	private FastList<String> translateEquality(SymbolicExpression expr) {
		SymbolicExpression leftExpression = (SymbolicExpression) expr
				.argument(0);
		SymbolicExpression rightExpression = (SymbolicExpression) expr
				.argument(1);
		FastList<String> result = processEquality(leftExpression,
				rightExpression);

		return result;
	}

	/**
	 * <p>
	 * Translates a union-extract expression. The result has the form
	 * 
	 * <pre>
	 * UT_extract_i(y)
	 * </pre>
	 * 
	 * where <code>UT</code> is the name of the union type, <code>y</code> is
	 * the argument belonging to the union type, and <code>i</code> is the index
	 * argument.
	 * </p>
	 * 
	 * <p>
	 * UNION_EXTRACT: 2 arguments: arg0 is an IntObject giving the index of a
	 * member type of a union type; arg1 is a symbolic expression whose type is
	 * the union type. The resulting expression has type the specified member
	 * type. This essentially pulls the expression out of the union and casts it
	 * to the member type. If arg1 does not belong to the member type (as
	 * determined by a UNION_TEST expression), the value of this expression is
	 * undefined.
	 * </p>
	 * 
	 * <p>
	 * Note that the union type will be declared as follows:
	 * 
	 * <pre>
	 * DATATYPE
	 *   UT = UT_inject_0(UT_extract_0 : T0) | UT_inject_1(UT_extract_1 : T1) | ...
	 * END;
	 * </pre>
	 * 
	 * Usage:
	 * 
	 * <pre>
	 *   UT_inject_i(x)
	 *   UT_extract_i(y)
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param expr
	 *            a "union extract" expression
	 * @return result of translating to CVC4
	 */
	private FastList<String> translateUnionExtract(SymbolicExpression expr) {
		int index = ((IntObject) expr.argument(0)).getInt();
		SymbolicExpression arg = (SymbolicExpression) expr.argument(1);
		SymbolicUnionType unionType = (SymbolicUnionType) arg.type();
		FastList<String> result = new FastList<>(selector(unionType, index));

		result.add("(");
		result.append(translate(arg));
		result.add(")");
		return result;
	}

	/**
	 * <p>
	 * Translates a union-inject expression. The result has the form
	 * 
	 * <pre>
	 * UT_inject_i(x)
	 * </pre>
	 * 
	 * where <code>UT</code> is the name of the union type, <code>x</code> is
	 * the argument belonging to the member type, and <code>i</code> is the
	 * index argument.
	 * </p>
	 * 
	 * <p>
	 * UNION_INJECT: injects an element of a member type into a union type that
	 * includes that member type. 2 arguments: arg0 is an IntObject giving the
	 * index of the member type of the union type; arg1 is a symbolic expression
	 * whose type is the member type. The union type itself is the type of the
	 * UNION_INJECT expression.
	 * </p>
	 * 
	 * @param expr
	 *            a "union inject" expression
	 * @return the CVC4 translation of that expression
	 */
	private FastList<String> translateUnionInject(SymbolicExpression expr) {
		int index = ((IntObject) expr.argument(0)).getInt();
		SymbolicExpression arg = (SymbolicExpression) expr.argument(1);
		SymbolicUnionType unionType = (SymbolicUnionType) expr.type();
		FastList<String> result = new FastList<>(constructor(unionType, index));

		result.add("(");
		result.append(translate(arg));
		result.add(")");
		return result;
	}

	/**
	 * <p>
	 * Translates a union-test expression. The result has the form
	 * 
	 * <pre>
	 * is_UT_inject_i(y)
	 * </pre>
	 * 
	 * where <code>UT</code> is the name of the union type, <code>y</code> is
	 * the argument belonging to the union type, and <code>i</code> is the index
	 * argument.
	 * </p>
	 * 
	 * <p>
	 * UNION_TEST: 2 arguments: arg0 is an IntObject giving the index of a
	 * member type of the union type; arg1 is a symbolic expression whose type
	 * is the union type. This is a boolean-valued expression whose value is
	 * true iff arg1 belongs to the specified member type of the union type.
	 * </p>
	 * 
	 * @param expr
	 *            a "union test" expression
	 * @return the CVC4 translation of that expression
	 */
	private FastList<String> translateUnionTest(SymbolicExpression expr) {
		int index = ((IntObject) expr.argument(0)).getInt();
		SymbolicExpression arg = (SymbolicExpression) expr.argument(1);
		SymbolicUnionType unionType = (SymbolicUnionType) arg.type();
		FastList<String> result = new FastList<>("is_"
				+ constructor(unionType, index));

		result.add("(");
		result.append(translate(arg));
		result.add(")");
		return result;
	}

	private FastList<String> translateCast(SymbolicExpression expression) {
		SymbolicExpression argument = (SymbolicExpression) expression
				.argument(0);
		SymbolicType originalType = argument.type();
		SymbolicType newType = expression.type();

		if (originalType.equals(newType)
				|| (originalType.isInteger() && newType.isReal()))
			return translate(argument);

		Pair<SymbolicType, SymbolicType> key = new Pair<>(originalType, newType);
		String castFunction = castMap.get(key);

		if (castFunction == null) {
			castFunction = "cast" + castMap.size();
			cvcDeclarations.addAll(castFunction, " : (");
			cvcDeclarations.append(translateType(originalType));
			cvcDeclarations.add(") -> (");
			cvcDeclarations.append(translateType(newType));
			cvcDeclarations.add(");\n");
			castMap.put(key, castFunction);
		}

		FastList<String> result = new FastList<>(castFunction, "(");

		result.append(translate(argument));
		result.add(")");
		return result;
	}

	private FastList<String> translateApply(SymbolicExpression expression) {
		SymbolicExpression function = (SymbolicExpression) expression
				.argument(0);
		SymbolicSequence<?> arguments = (SymbolicSequence<?>) expression
				.argument(1);
		boolean first = true;
		FastList<String> result = new FastList<String>("(");

		result.append(translate(function));
		result.add(")(");
		for (SymbolicExpression arg : arguments) {
			if (first)
				first = false;
			else
				result.add(", ");
			result.append(translate(arg));
		}
		result.add(")");
		return result;
	}

	private FastList<String> translateNegative(SymbolicExpression expression) {
		FastList<String> result = new FastList<>("-(");

		result.append(translate((SymbolicExpression) expression.argument(0)));
		result.add(")");
		return result;
	}

	private FastList<String> translateNEQ(SymbolicExpression expression) {
		FastList<String> result = new FastList<>("NOT(");

		result.append(processEquality(
				(SymbolicExpression) expression.argument(0),
				(SymbolicExpression) expression.argument(1)));
		result.add(")");
		return result;
	}

	private FastList<String> translateNot(SymbolicExpression expression) {
		FastList<String> result = new FastList<>("NOT(");

		result.append(translate((SymbolicExpression) expression.argument(0)));
		result.add(")");
		return result;
	}

	private FastList<String> translatePower(SymbolicExpression expression) {
		// apparently "^" but not documented
		SymbolicObject exponent = expression.argument(1);
		FastList<String> result = new FastList<>("(");

		result.append(translate((SymbolicExpression) expression.argument(0)));
		result.add(")^");
		if (exponent instanceof IntObject)
			result.add(exponent.toString());
		else {
			result.add("(");
			result.append(translate((SymbolicExpression) exponent));
			result.add(")");
		}
		return result;
	}

	private FastList<String> translateCond(SymbolicExpression expression) {
		// syntax: IF b THEN x ELSE y ENDIF
		FastList<String> result = new FastList<>("IF (");

		result.append(translate((SymbolicExpression) expression.argument(0)));
		result.add(") THEN (");
		result.append(translate((SymbolicExpression) expression.argument(1)));
		result.add(") ELSE (");
		result.append(translate((SymbolicExpression) expression.argument(2)));
		result.add(") ENDIF");
		return result;
	}

	private FastList<String> translateAssoc(String operator,
			String defaultValue, SymbolicCollection<?> terms) {
		int size = terms.size();

		if (size == 0) {
			return new FastList<>(defaultValue);
		} else if (size == 1) {
			return translate(terms.getFirst());
		} else {
			boolean first = true;
			FastList<String> result = new FastList<>();

			for (SymbolicExpression term : terms) {
				if (first) {
					first = false;
				} else {
					result.add(operator);
				}
				result.add("(");
				result.append(translate(term));
				result.add(")");
			}
			return result;
		}
	}

	private FastList<String> translateBinary(String operator,
			SymbolicExpression arg0, SymbolicExpression arg1) {
		FastList<String> result = new FastList<>("(");

		result.append(translate(arg0));
		result.addAll(") ", operator, " (");
		result.append(translate(arg1));
		result.add(")");
		return result;
	}

	private FastList<String> translateBinaryOrAssoc(String operator,
			String defaultValue, SymbolicExpression expression) {
		int numArgs = expression.numArguments();

		if (numArgs == 2) {
			return translateBinary(operator,
					(SymbolicExpression) expression.argument(0),
					(SymbolicExpression) expression.argument(1));
		} else if (numArgs == 1) {
			return translateAssoc(operator, defaultValue,
					(SymbolicCollection<?>) expression.argument(0));
		} else {
			throw new SARLInternalException("Expected 1 or 2 arguments for "
					+ operator);
		}
	}

	/**
	 * Translates a SARL symbolic expression to the language of CVC4.
	 * 
	 * @param expression
	 *            a non-null SymbolicExpression
	 * @return translation to CVC4 as a fast list of strings
	 */
	private FastList<String> translateWork(SymbolicExpression expression) {
		SymbolicOperator operator = expression.operator();
		FastList<String> result;

		switch (operator) {
		case ADD:
			result = translateBinaryOrAssoc(" + ", "0", expression);
			break;
		case AND:
			result = translateBinaryOrAssoc(" AND ", "TRUE", expression);
			break;
		case APPLY:
			result = translateApply(expression);
			break;
		case ARRAY_LAMBDA:
			throw new UnsupportedOperationException(
					"Array lambdas are not supported in CVC4");
		case ARRAY_READ:
			result = translateArrayRead(expression);
			break;
		case ARRAY_WRITE:
			result = translateArrayWrite(expression);
			break;
		case CAST:
			result = translateCast(expression);
			break;
		case CONCRETE:
			result = translateConcrete(expression);
			break;
		case COND:
			result = translateCond(expression);
			break;
		case DENSE_ARRAY_WRITE:
			result = translateDenseArrayWrite(expression);
			break;
		case DENSE_TUPLE_WRITE:
			result = translateDenseTupleWrite(expression);
			break;
		case DIVIDE: // real division
			result = translateBinary(" / ",
					(SymbolicExpression) expression.argument(0),
					(SymbolicExpression) expression.argument(1));
			break;
		case EQUALS:
			result = translateEquality(expression);
			break;
		case EXISTS:
		case FORALL:
			result = translateQuantifier(expression);
			break;
		case INT_DIVIDE:
			// apparently "DIV", though documentation doesn't say it
			result = translateBinary(" DIV ",
					(SymbolicExpression) expression.argument(0),
					(SymbolicExpression) expression.argument(1));
			break;
		case LENGTH:
			result = lengthOfArray((SymbolicExpression) expression.argument(0));
			break;
		case LESS_THAN:
			result = translateBinary(" < ",
					(SymbolicExpression) expression.argument(0),
					(SymbolicExpression) expression.argument(1));
			break;
		case LESS_THAN_EQUALS:
			result = translateBinary(" <= ",
					(SymbolicExpression) expression.argument(0),
					(SymbolicExpression) expression.argument(1));
			break;
		case MODULO:
			// apparently "MOD", though documentation doesn't say it
			result = translateBinary(" MOD ",
					(SymbolicExpression) expression.argument(0),
					(SymbolicExpression) expression.argument(1));
			break;
		case MULTIPLY:
			result = translateBinaryOrAssoc(" * ", "1", expression);
			break;
		case NEGATIVE:
			result = translateNegative(expression);
			break;
		case NEQ:
			result = translateNEQ(expression);
			break;
		case NOT:
			result = translateNot(expression);
			break;
		case OR:
			result = translateBinaryOrAssoc(" OR ", "FALSE", expression);
			break;
		case POWER:
			result = translatePower(expression);
			break;
		case SUBTRACT:
			result = translateBinary(" - ",
					(SymbolicExpression) expression.argument(0),
					(SymbolicExpression) expression.argument(1));
			break;
		case SYMBOLIC_CONSTANT:
			result = translateSymbolicConstant((SymbolicConstant) expression,
					false);
			break;
		case TUPLE_READ:
			result = translateTupleRead(expression);
			break;
		case TUPLE_WRITE:
			result = translateTupleWrite(expression);
			break;
		case UNION_EXTRACT:
			result = translateUnionExtract(expression);
			break;
		case UNION_INJECT:
			result = translateUnionInject(expression);
			break;
		case UNION_TEST:
			result = translateUnionTest(expression);
			break;
		case LAMBDA:
			result = translateLambda(expression);
			break;
		case NULL:
			result = null;
			break;
		default:
			throw new SARLInternalException("unreachable: unknown operator: "
					+ operator);
		}
		return result;
	}

	private FastList<String> translateType(SymbolicType type) {
		FastList<String> result = typeMap.get(type);

		if (result != null)
			return result.clone();

		SymbolicTypeKind kind = type.typeKind();

		switch (kind) {
		case BOOLEAN:
			result = new FastList<>("BOOLEAN");
			break;
		case INTEGER:
		case CHAR:
			result = new FastList<>("INT");
			break;
		case REAL:
			result = new FastList<>("REAL");
			break;
		case ARRAY: {
			SymbolicArrayType arrayType = (SymbolicArrayType) type;

			result = new FastList<>("ARRAY INT OF (");
			result.append(translateType(arrayType.elementType()));
			result.add(")");
			result.addFront("[INT, ");
			result.add("]");
			break;
		}
		case TUPLE: {
			SymbolicTupleType tupleType = (SymbolicTupleType) type;
			SymbolicTypeSequence sequence = tupleType.sequence();
			int numTypes = sequence.numTypes();

			if (numTypes == 1) {
				result = translateType(sequence.getType(0));
			} else {
				boolean first = true;

				result = new FastList<>("[");
				for (SymbolicType memberType : sequence) {
					if (first)
						first = false;
					else
						result.add(", ");
					result.append(translateType(memberType));
				}
				result.add("]");
			}
			break;
		}
		case FUNCTION: {
			SymbolicFunctionType funcType = (SymbolicFunctionType) type;
			SymbolicTypeSequence inputs = funcType.inputTypes();
			int numInputs = inputs.numTypes();
			boolean first = true;

			if (numInputs == 0)
				throw new SARLException(
						"CVC* requires a function type to have at least one input");
			result = new FastList<>("(");
			for (SymbolicType inputType : inputs) {
				if (first)
					first = false;
				else
					result.add(", ");
				result.append(translateType(inputType));
			}
			result.add(") -> (");
			result.append(translateType(funcType.outputType()));
			result.add(")");
			break;
		}
		case UNION: {
			// this is the first time this type has been encountered, so
			// it must be declared...
			//
			// Declaration of a union type UT, with member types T0, T1, ...:
			//
			// DATATYPE
			// UT = UT_inject_0(UT_extract_0 : T0) | UT_inject_1(UT_extract_1 :
			// T1) | ...
			// END;
			//
			// Usage:
			//
			// UT_inject_i(x)
			// UT_extract_i(y)
			SymbolicUnionType unionType = (SymbolicUnionType) type;
			SymbolicTypeSequence sequence = unionType.sequence();
			String name = unionType.name().getString();
			int n = sequence.numTypes();

			cvcDeclarations.addAll("DATATYPE\n", name, " = ");
			for (int i = 0; i < n; i++) {
				SymbolicType memberType = sequence.getType(i);

				if (i > 0)
					cvcDeclarations.add(" | ");
				cvcDeclarations.addAll(constructor(unionType, i), "(",
						selector(unionType, i), " : ");
				cvcDeclarations.append(translateType(memberType));
				cvcDeclarations.add(")");
			}
			cvcDeclarations.add("END;\n");
			result = new FastList<>(name);
			break;
		}
		default:
			throw new SARLInternalException("Unknown SARL type: " + type);
		}
		typeMap.put(type, result);
		return result.clone();
	}

	private FastList<String> translate(SymbolicExpression expression) {
		FastList<String> result = expressionMap.get(expression);

		if (result == null) {
			result = translateWork(expression);
			this.expressionMap.put(expression, result);
		}
		return result.clone();
	}

	// Exported methods...

	/**
	 * Returns the result of translating the symbolic expression specified at
	 * construction into the language of CVC4. The result is returned as a
	 * {@link FastList}. The elements of that list are Strings, which,
	 * concatenated, yield the translation result. In most cases you never want
	 * to convert the result to a single string. Rather, you should iterate over
	 * this list, printing each element to the appropriate output stream.
	 * 
	 * @return result of translation of the specified symbolic expression
	 */
	FastList<String> getTranslation() {
		return cvcTranslation;
	}

	/**
	 * Returns the text of the declarations of the CVC symbols that occur in the
	 * translated expression. Typically, the declarations are submitted to CVC4
	 * first, followed by a query or assertion of the translated expression.
	 * 
	 * @return the declarations of the CVC symbols
	 */
	FastList<String> getDeclarations() {
		return cvcDeclarations;
	}

}
