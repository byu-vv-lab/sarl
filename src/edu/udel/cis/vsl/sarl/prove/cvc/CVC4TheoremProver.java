package edu.udel.cis.vsl.sarl.prove.cvc;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.nyu.acsys.CVC4.Expr;
import edu.nyu.acsys.CVC4.ExprManager;
import edu.nyu.acsys.CVC4.Kind;
import edu.nyu.acsys.CVC4.Result;
import edu.nyu.acsys.CVC4.Rational;
import edu.nyu.acsys.CVC4.SmtEngine;
import edu.nyu.acsys.CVC4.Type;
import edu.nyu.acsys.CVC4.Exception;
import edu.nyu.acsys.CVC4.vectorType;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;

public class CVC4TheoremProver implements TheoremProver {

	private PreUniverse universe;
	private BooleanExpression context;
	private PrintStream out = null;
	private boolean showProverQueries = false;
	private ExprManager em = new ExprManager();
	private SmtEngine smt = new SmtEngine(em);
	private Expr cvcAssumption;

	/**
	 * Mapping of SARL symbolic type to corresponding CVC4 type. Set in method
	 * reset().
	 */
	private Map<SymbolicType, Type> typeMap = new HashMap<SymbolicType, Type>();

	CVC4TheoremProver(PreUniverse universe, BooleanExpression context) {
		assert universe != null;
		assert context != null;
		this.universe = universe;
		this.context = context;
		cvcAssumption = translate(context);
		smt.assertFormula(cvcAssumption); 
	}

/**
 * Translate expr from SARL to CVC4. This results in two things: a CVC4
 * expression (which is returned) and also side-effects: constraints added
 * to the CVC4 assumption set, possibly involving auxiliary variables.
 * @param SymbolicExpression
 * @returns Expr
 */
	public Expr translate(SymbolicExpression expr) {
		Expr result;
		result = translateWork(expr);
		return result;
	}

	/**
	 * Symbolic expressions of incomplete array type are represented by ordered
	 * pairs (length, array). This method tells whether the given symbolic
	 * expression type requires such a representation.
	 * 
	 * @param type
	 *            any symbolic type
	 * @return true iff the type is an incomplete array type
	 */
	private boolean isBigArrayType(SymbolicType type) {
		return type instanceof SymbolicArrayType
				&& !((SymbolicArrayType) type).isComplete();
	}

	/**
	 * Like above, but takes SymbolicExpression as input.
	 * 
	 * @param expr
	 * @return true iff the type of expr is an incomplete array type
	 */
	private boolean isBigArray(SymbolicExpression expr) {
		return isBigArrayType(expr.type());
	}

	/**
	 * This method takes any Expr that is of incomplete array type and returns
	 * the length.
	 * 
	 * @param bigArray
	 *            CVC4 Expr of bigArray
	 * @return length of CVC4 Expr of incomplete array type
	 */
	private Expr bigArrayLength(Expr bigArray) {
		Expr constant = em.mkConst(new Rational(0));
		return em.mkExpr(Kind.TUPLE_SELECT, bigArray, constant);
	}

	/**
	 * This methods takes any Expr that is of incomplete array type and returns
	 * the value.
	 * 
	 * @param bigArray
	 *            CVC4 Expr of bigArray
	 * @return value of CVC4 Expr of incomplete array type
	 */
	private Expr bigArrayValue(Expr bigArray) {
		Expr index = em.mkConst(new Rational(1));
		return em.mkExpr(Kind.TUPLE_SELECT, bigArray, index);
	}

	/**
	 * Translates a multiplication SymbolicExpression (a*b) into an 
	 * equivalent CVC4 multiplication Expr based upon number of 
	 * arguments given. 
	 * 
	 * @param expr
	 * 			a SARL SymbolicExpression of form a*b
	 * @return CVC4 Expr
	 */
	private Expr translateMultiply(SymbolicExpression expr) {
		int numArgs = expr.numArguments();
		Expr result;

		if (numArgs == 1) {
			result = em.mkConst(new Rational(1));
			for (SymbolicExpression operand : (SymbolicCollection<?>) expr
					.argument(0))
				result = em.mkExpr(Kind.MULT, result, translate(operand));
		} else if (numArgs == 2)
			result = em.mkExpr(Kind.MULT,
					translate((SymbolicExpression) expr.argument(0)),
					translate((SymbolicExpression) expr.argument(1)));
		else
			throw new SARLInternalException(
					"Wrong number of arguments to multiply: " + expr);
		return result;
	}

	/**
	 * Translates a SymbolicExpression of type (a || b) into an equivalent CVC4 Expr 
	 * @param expr
	 * @return CVC4 representation of expr
	 */
	private Expr translateOr(SymbolicExpression expr) {
		int numArgs = expr.numArguments();
		Expr result;

		if (numArgs == 1)
			// NEEDS TO BE ADDED FOR ONE ARG
			return null;
		else if (numArgs == 2)
			result = em.mkExpr(Kind.OR,
					translate((SymbolicExpression) expr.argument(0)),
					translate((SymbolicExpression) expr.argument(1)));
		else
			throw new SARLInternalException("Wrong number of arguments to or: "
					+ expr);
		return result;
	}
	
	/**
	 * Translates which operation to perform based upon the given 
	 * SymbolicExpression and the SymbolicOperator provided.  Depending 
	 * upon the number of arguments given, a different conditional will
	 * be executed.  The result will be a CVC4 Expr. 
	 * 
	 * @param expr
	 * 			a SymbolicExpression 
	 * @return
	 */
	private Expr translateWork(SymbolicExpression expr) {
		int numArgs = expr.numArguments();
		Expr result;

		// NEED TO WORK ON
		switch (expr.operator()) {
		case ADD:
			if (numArgs == 2)
				result = em.mkExpr(Kind.PLUS,
						translate((SymbolicExpression) expr.argument(0)),
						translate((SymbolicExpression) expr.argument(1)));
			else if (numArgs == 1)
				// NEEDS TO BE ADDED FOR ONE ARG
				return null;
			else
				throw new SARLInternalException(
						"Expected 1 or 2 arguments for ADD");
			break;
		case AND:
			if (numArgs == 2)
				result = em.mkExpr(Kind.AND,
						translate((SymbolicExpression) expr.argument(0)),
						translate((SymbolicExpression) expr.argument(1)));
			else if (numArgs == 1)
				// NEEDS TO BE ADDED FOR ONE ARG
				return null;
			else
				throw new SARLInternalException(
						"Expected 1 or 2 arguments for AND: " + expr);
			break;
		case CAST:
			result = this.translate((SymbolicExpression) expr.argument(0));
			break;
		case COND:
			result = em.mkExpr(Kind.ITE,
					translate((SymbolicExpression) expr.argument(0)),
					translate((SymbolicExpression) expr.argument(1)),
					translate((SymbolicExpression) expr.argument(2)));
			break;
		case DIVIDE: // real division
			result = em.mkExpr(Kind.DIVISION,
					translate((SymbolicExpression) expr.argument(0)),
					translate((SymbolicExpression) expr.argument(1)));
			break;
		case INT_DIVIDE:
			result = em.mkExpr(Kind.INTS_DIVISION,
					translate((SymbolicExpression) expr.argument(0)),
					translate((SymbolicExpression) expr.argument(1)));
			break;
		case LENGTH:
			result = bigArrayLength(translate((SymbolicExpression) expr
					.argument(0)));
			break;
		case LESS_THAN:
			result = em.mkExpr(Kind.LT,
					translate((SymbolicExpression) expr.argument(0)),
					translate((SymbolicExpression) expr.argument(1)));
			break;
		case LESS_THAN_EQUALS:
			result = em.mkExpr(Kind.LEQ,
					translate((SymbolicExpression) expr.argument(0)),
					translate((SymbolicExpression) expr.argument(1)));
			break;
		case MODULO:
			result = em.mkExpr(Kind.INTS_MODULUS,
					translate((SymbolicExpression) expr.argument(0)),
					translate((SymbolicExpression) expr.argument(1)));
			break;
		case MULTIPLY:
			result = translateMultiply(expr);
			break;
		case NEGATIVE:
			result = em.mkExpr(Kind.UMINUS,
					translate((SymbolicExpression) expr.argument(0)));
			break;
		case NEQ:
			result = em.mkExpr(Kind.NOT,
					translateEquality((SymbolicExpression) expr.argument(0)));
			break;
		case NOT:
			result = em.mkExpr(Kind.NOT,
					translate((SymbolicExpression) expr.argument(0)));
			break;
		case OR:
			result = translateOr(expr);
			break;
		case POWER: {
			SymbolicObject exponent = expr.argument(1);

			if (exponent instanceof IntObject)
				result = em.mkExpr(Kind.POW,
						translate((SymbolicExpression) expr.argument(0)), em
						.mkConst(new Rational(((IntObject) exponent)
								.getInt())));
			else
				result = em.mkExpr(Kind.POW,
						translate((SymbolicExpression) expr.argument(0)),
						translate((SymbolicExpression) exponent));
			break;
		}
		case TUPLE_READ:
			result = em.mkExpr(Kind.TUPLE_SELECT,
					translate((SymbolicExpression) expr.argument(0)), em
					.mkConst(new Rational(
							((IntObject) expr.argument(1)).getInt())));
			break;
		case TUPLE_WRITE:
			result = em.mkExpr(Kind.TUPLE_UPDATE,
					translate((SymbolicExpression) expr.argument(0)), em
					.mkConst(new Rational(
							((IntObject) expr.argument(1)).getInt())),
							translate((SymbolicExpression) expr.argument(2)));
			break;
		case SUBTRACT:
			result = em.mkExpr(Kind.MINUS,
					translate((SymbolicExpression) expr.argument(0)),
					translate((SymbolicExpression) expr.argument(1)));
			break;

		default:
			throw new SARLInternalException("unreachable");
		}
		return result;
	}
	private Expr translateEquality(SymbolicExpression argument) {
		return null;
	}

	/**
	 * Translates the symbolic type to a CVC4 type.
	 * 
	 * @param type
	 *            a SARL symbolic expression type
	 * @return the equivalent CVC4 type
	 * @throws Exception
	 *             if CVC4 throws an exception
	 */
	public Type translateType(SymbolicType type) throws Exception
	{
		Type result = typeMap.get(type);

		if (result != null)
			return result;

		SymbolicTypeKind kind = type.typeKind();

		switch (kind) {

		case BOOLEAN:
			result = em.booleanType();
			break;
		case INTEGER:
			result = em.integerType();
			break;
		case REAL:
			result = em.realType();
			break;
		case ARRAY:
			result = em.mkArrayType(em.integerType(),
					translateType(((SymbolicArrayType) type).elementType()));
			if (!(type instanceof SymbolicCompleteArrayType))
				// tuple:<extent,array>
				result = em.mkTupleType((vectorType) Arrays
						.asList(em.integerType(), result));
			break;
//		case TUPLE:
//			result = em
//					.tupleType(translateTypeSequence(((SymbolicTupleType) type)
//							.sequence()));
//			break;
//		case FUNCTION:
//			result = vc.funType(
//					translateTypeSequence(((SymbolicFunctionType) type)
//							.inputTypes()),
//					translateType(((SymbolicFunctionType) type).outputType()));
//			break;
//		case UNION: {	
//			SymbolicUnionType unionType = (SymbolicUnionType) type;
//			List<String> constructors = new LinkedList<String>();
//			List<List<String>> selectors = new LinkedList<List<String>>();
//			List<List<Expr>> types = new LinkedList<List<Expr>>();
//			SymbolicTypeSequence sequence = unionType.sequence();
//			int index = 0;
//
//			for (SymbolicType t : sequence) {
//				List<String> selectorList = new LinkedList<String>();
//				List<Expr> typeList = new LinkedList<Expr>();
//
//				selectorList.add(selector(unionType, index));
//				typeList.add(translateType(t).getExpr());
//				selectors.add(selectorList);
//				types.add(typeList);
//				constructors.add(constructor(unionType, index));
//				index++;
//			}
//			result = vc.dataType(unionType.name().getString(), constructors,
//					selectors, types);
//			break;
//		}
		default:
			throw new RuntimeException("Unknown type: " + type);
		}
		typeMap.put(type, result);
		return result;
	}

	@Override
	public PreUniverse universe() {
		return universe;
	}
	/**
	 * Takes a BooleanExpression and passes it through translate. 
	 * The translated predicate is then queried via smt and pops the smt.
	 * @param expr
	 * @return ValidityResult from using translateResult
	 */
	public ValidityResult valid(BooleanExpression symbolicPredicate)
	{
		Expr cvc4Predicate = translate(symbolicPredicate);
		Result result = smt.query(cvc4Predicate);
		smt.pop();

		return (ValidityResult) result.asValidityResult();
	}

	@Override
	public ValidityResult validOrModel(BooleanExpression predicate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOutput(PrintStream out) {
		this.out = out;
		showProverQueries = out != null;
	}

	@Override
	public String toString() {
		return "CVC4TheoremProver";
	}
}
