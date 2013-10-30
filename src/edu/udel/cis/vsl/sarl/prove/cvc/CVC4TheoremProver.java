package edu.udel.cis.vsl.sarl.prove.cvc;

import java.io.PrintStream;

/*
import edu.nyu.acsys.CVC4.Expr;
import edu.nyu.acsys.CVC4.ExprManager;
import edu.nyu.acsys.CVC4.Kind;
import edu.nyu.acsys.CVC4.Rational;
import edu.nyu.acsys.CVC4.SmtEngine;
*/
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;

public class CVC4TheoremProver implements TheoremProver {

	private PreUniverse universe;
	private BooleanExpression context;
	private PrintStream out = null;
	private boolean showProverQueries = false;
	//private ExprManager em = new ExprManager();
	//private SmtEngine smt = new SmtEngine(em);
	//private Expr cvcAssumption;

	CVC4TheoremProver(PreUniverse universe, BooleanExpression context) {
		assert universe != null;
		assert context != null;
		this.universe = universe;
		this.context = context;
		//cvcAssumption = translate(context);
		//smt.assertFormula(cvcAssumption);
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
//	private Expr bigArrayLength(Expr bigArray) {
//		Expr constant = em.mkConst(new Rational(0));
//		return em.mkExpr(Kind.TUPLE_SELECT, bigArray, constant);
//	}

	/**
	 * This methods takes any Expr that is of incomplete array type and returns
	 * the value.
	 * 
	 * @param bigArray
	 *            CVC4 Expr of bigArray
	 * @return value of CVC4 Expr of incomplete array type
	 */
/*	private Expr bigArrayValue(Expr bigArray) {
		Expr index = em.mkConst(new Rational(1));
		return em.mkExpr(Kind.TUPLE_SELECT, bigArray, index);
	}

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

	private Expr translate(SymbolicExpression expr) {
		Expr result;
		result = translateWork(expr);
		return result;
	}
*/
	@Override
	public PreUniverse universe() {
		return universe;
	}

	@Override
	public ValidityResult valid(BooleanExpression predicate) {
		// TODO Auto-generated method stub
		return null;
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
