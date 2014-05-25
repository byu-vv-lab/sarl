package edu.udel.cis.vsl.sarl.prove.cvc;

import java.io.PrintStream;

import edu.nyu.acsys.CVC4.Expr;
import edu.nyu.acsys.CVC4.ExprManager;
import edu.nyu.acsys.CVC4.IntegerType;
import edu.nyu.acsys.CVC4.Kind;
import edu.nyu.acsys.CVC4.Rational;
import edu.nyu.acsys.CVC4.SExpr;
import edu.nyu.acsys.CVC4.SmtEngine;
import edu.nyu.acsys.CVC4.Type;
import edu.nyu.acsys.CVC4.vectorExpr;
import edu.nyu.acsys.CVC4.vectorType;

public class LittleCVC4Example5 {

	static {
		System.loadLibrary("cvc4jni");
	}

	private static PrintStream out = System.out;

	private static ExprManager em;

	private static SmtEngine smt;

	private static IntegerType intType;

	private static Expr zero, one, two;

	public static void main(String[] args) {

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
		one = em.mkConst(new Rational(1));
		two = em.mkConst(new Rational(2));

		{ // basic tuple experiment...
			Expr tuple = em.mkExpr(Kind.TUPLE, zero, one, two);
			out.println("tuple = " + tuple);
			Expr x = tuple.getChild(0);
			out.println("tuple child 0 = " + x);
			out.println("tuple child 1 = " + tuple.getChild(1));
			out.println("tuple type = " + tuple.getType());
			out.flush();
		}
		// translate function from char[] to int.
		// function from Tuple[int, int[]] to int.

		vectorType tupleTypeVec = new vectorType();
		tupleTypeVec.add(intType);
		Type arrayType = em.mkArrayType(intType, intType);
		tupleTypeVec.add(arrayType);
		Type tupleType = em.mkTupleType(tupleTypeVec);
		vectorType argTypes = new vectorType();
		argTypes.add(tupleType);
		Type functionType = em.mkFunctionType(argTypes, intType);

		out.println("functionType = " + functionType);

		Expr f = em.mkVar(functionType);
		Expr a = em.mkVar(arrayType);
		Expr array = em.mkExpr(Kind.STORE, a, zero, zero);
		array = em.mkExpr(Kind.STORE, array, one, one);
		Expr tuple = em.mkExpr(Kind.TUPLE, two, array);
		Expr application = em.mkExpr(Kind.APPLY_UF, f, tuple);

		out.println("application = " + application);
		out.flush();

		Expr context = em.mkExpr(Kind.GEQ, application, one);
		smt.assertFormula(context);

		vectorExpr assertions = smt.getAssertions();
		int n = (int) assertions.size();

		for (int i = 0; i < n; i++) {
			Expr p = assertions.get(i);

			out.println(p);
		}

		out.flush();
	}
}
