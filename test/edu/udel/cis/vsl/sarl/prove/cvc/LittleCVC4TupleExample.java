package edu.udel.cis.vsl.sarl.prove.cvc;

import java.io.PrintStream;

import edu.nyu.acsys.CVC4.Expr;
import edu.nyu.acsys.CVC4.ExprManager;
import edu.nyu.acsys.CVC4.IntegerType;
import edu.nyu.acsys.CVC4.Rational;
import edu.nyu.acsys.CVC4.TupleSelect;
import edu.nyu.acsys.CVC4.TupleUpdate;
import edu.nyu.acsys.CVC4.Type;
import edu.nyu.acsys.CVC4.vectorType;

public class LittleCVC4TupleExample {

	static {
		System.loadLibrary("cvc4jni");
	}

	private static PrintStream out = System.out;

	private static ExprManager em;

	private static IntegerType intType;

	private static Expr two;

	public static void main(String[] args) {

		out.println("Starting little CVC4 example...");
		em = new ExprManager();
		intType = em.integerType();
		two = em.mkConst(new Rational(2));

		vectorType tupleTypeVec = new vectorType();
		tupleTypeVec.add(intType);
		Type arrayType = em.mkArrayType(intType, intType);
		tupleTypeVec.add(arrayType);
		Type tupleType = em.mkTupleType(tupleTypeVec);
		Expr a = em.mkVar("a", tupleType);

		out.println("a = " + a);
		out.println("type of a: " + a.getType());
		out.println();

		Expr len = em.mkExpr(em.mkConst(new TupleSelect(0)), a);
		out.println("len = " + len);
		out.println("type of len: " + len.getType());
		out.println();

		Expr val = em.mkExpr(em.mkConst(new TupleSelect(1)), a);
		out.println("val = " + val);
		out.println("type of val: " + val.getType());
		out.println();

		out.flush();

		Expr b = em.mkExpr(em.mkConst(new TupleUpdate(0)), a, two);
		out.println("b = " + b);
		out.println("type of b: " + b.getType());
		out.println();
	}
}
