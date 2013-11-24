package edu.udel.cis.vsl.sarl.prove.cvc;

import java.io.PrintStream;

import edu.nyu.acsys.CVC4.Expr;
import edu.nyu.acsys.CVC4.ExprManager;
import edu.nyu.acsys.CVC4.ExprManagerMapCollection;
import edu.nyu.acsys.CVC4.FunctionType;
import edu.nyu.acsys.CVC4.Kind;
import edu.nyu.acsys.CVC4.Rational;
import edu.nyu.acsys.CVC4.RealType;
import edu.nyu.acsys.CVC4.Result;
import edu.nyu.acsys.CVC4.Result.Validity;
import edu.nyu.acsys.CVC4.SExpr;
import edu.nyu.acsys.CVC4.SmtEngine;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;

public class LittleCVC4Example2 {

	static {
		System.loadLibrary("cvc4jni");
	}

	private static PrintStream out = System.out;

	private static ExprManager em;

	private static SmtEngine smt;

	private static RealType realType;

	private static Expr zero;

	public static void main(String[] args) {
		Expr x, xeq0;

		out.println("Starting little CVC4 example...");
		em = new ExprManager();
		smt = new SmtEngine(em);
		// the following is necessary if you are going to make
		// multiple verify calls with the same SmtEngine:
		// (this may change in the future)
		smt.setOption("incremental", new SExpr(true));
		smt.setOption("produce-models", new SExpr(true));
		
		realType = em.realType();
		zero = em.mkConst(new Rational(0));
		x = em.mkVar("x", realType); // real variable x
		xeq0 = em.mkExpr(Kind.EQUAL, x, zero); // f(x)=0
		// out.println("Asserting x=0: " + xeq0);
		//smt.assertFormula(xeq0);
		out.println("Does x=0? " + smt.query(xeq0));
		
		
		out.println("Why is it invalid? " + smt.getValue(xeq0));
		
		// answer should be "invalid"
		// Assertion failed: (clazz != NULL && jenv->ExceptionOccurred() ==
		// NULL), function Java_edu_nyu_acsys_CVC4_CVC4JNI_SmtEngine_1query,
		// file java.cpp, line 38741.
		// the assertion failure goes away if the first call to query is removed
		out.flush();
	}

}
