package edu.udel.cis.vsl.sarl.prove.cvc;

import java.io.PrintStream;

import edu.nyu.acsys.CVC4.Expr;
import edu.nyu.acsys.CVC4.ExprManager;
import edu.nyu.acsys.CVC4.IntegerType;
import edu.nyu.acsys.CVC4.Kind;
import edu.nyu.acsys.CVC4.Rational;
import edu.nyu.acsys.CVC4.SExpr;
import edu.nyu.acsys.CVC4.SmtEngine;

public class LittleCVC4Example3 {

	static {
		System.loadLibrary("cvc4jni");
	}

	private static PrintStream out = System.out;

	private static ExprManager em;

	private static SmtEngine smt;

	private static IntegerType intType;

	private static Expr zero;

	public static void main(String[] args) {
		Expr x, negOne, three, t1, e1;

		out.println("Starting little CVC4 example...");
		em = new ExprManager();
		smt = new SmtEngine(em);
		// the following is necessary if you are going to make
		// multiple verify calls with the same SmtEngine:
		// (this may change in the future)
		smt.setOption("incremental", new SExpr(true));
		// the following is needed to use method getAssertions:
		smt.setOption("interactive-mode", new SExpr(true));
		intType = em.integerType();
		zero = em.mkConst(new Rational(0));
		negOne = em.mkConst(new Rational(-1));
		three = em.mkConst(new Rational(3));
		x = em.mkBoundVar("x", intType);
		// 0=-x+3
		t1 = em.mkExpr(Kind.EQUAL, zero,
				em.mkExpr(Kind.PLUS, em.mkExpr(Kind.MULT, negOne, x), three));
		// exists x. 0=-x+3
		e1 = em.mkExpr(Kind.EXISTS, em.mkExpr(Kind.BOUND_VAR_LIST, x), t1);
		out.println("Query: " + e1);
		out.println("Result: " + smt.query(e1));
		out.flush();
	}
}
