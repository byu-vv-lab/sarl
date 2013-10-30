package edu.udel.cis.vsl.sarl.prove.cvc;

import java.io.PrintStream;


import cvc3.Expr;
/*
import edu.nyu.acsys.CVC4.Expr;
import edu.nyu.acsys.CVC4.ExprManager;
import edu.nyu.acsys.CVC4.Kind;
import edu.nyu.acsys.CVC4.SmtEngine;
*/
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;

public class CVC4TheoremProver implements TheoremProver {

	private PreUniverse universe;
	private BooleanExpression context;
	private PrintStream out = null;
	private boolean showProverQueries = false;
/*	private ExprManager em = new ExprManager();
	private SmtEngine smt = new SmtEngine(em);
	private Expr cvcAssumption;
*/
	CVC4TheoremProver(PreUniverse universe, BooleanExpression context) {
		assert universe != null;
		assert context != null;
		this.universe = universe;
		this.context = context;
/*		cvcAssumption = translate(context);
		smt.assertFormula(cvcAssumption); */
	}
/*
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
				result = null;
			// result = em.mkExpr(Kind.PLUS,
			// translateCollection((SymbolicCollection<?>) expr
			// .argument(0)));
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
				result = null;
			// result = em.mkExpr(Kind.AND,
			// translateCollection((SymbolicCollection<?>) expr
			// .argument(0)));
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
		case NEGATIVE:
			result = em.mkExpr(Kind.UMINUS,
					translate((SymbolicExpression) expr.argument(0)));
			break;
		case NOT:
			result = em.mkExpr(Kind.NOT,
					translate((SymbolicExpression) expr.argument(0)));
			break;
		case OR:
			if (numArgs == 1)
				result = null;
			// result = em.mkExpr(Kind.OR,
			// translateCollection((SymbolicCollection<?>) expr
			// .argument(0)));
			else if (numArgs == 2)
				result = em.mkExpr(Kind.OR,
						translate((SymbolicExpression) expr.argument(0)),
						translate((SymbolicExpression) expr.argument(1)));
			else
				throw new SARLInternalException(
						"Wrong number of arguments for OR: " + expr);
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
	//	result = translateWork(expr);
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
