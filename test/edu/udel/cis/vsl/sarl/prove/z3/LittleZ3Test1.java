package edu.udel.cis.vsl.sarl.prove.z3;

import java.io.PrintStream;

import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.ArraySort;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.IntNum;
import com.microsoft.z3.IntSort;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Sort;
import com.microsoft.z3.Symbol;
import com.microsoft.z3.TupleSort;
import com.microsoft.z3.Z3Exception;

public class LittleZ3Test1 {

	private static PrintStream out = System.out;

	private Context ctx;

	private Solver solver;

	public LittleZ3Test1() throws Z3Exception {
		ctx = new Context();
		solver = ctx.mkSolver();
		IntNum one = ctx.mkInt(1), two = ctx.mkInt(2);
		IntSort intSort = ctx.mkIntSort();
		ArraySort arraySort = ctx.mkArraySort(intSort, intSort);
		ArrayExpr a = (ArrayExpr) ctx.mkConst("a", arraySort);
		TupleSort tupleSort = ctx.mkTupleSort(ctx.mkSymbol("MyTuple"),
				new Symbol[] { ctx.mkSymbol("length"), ctx.mkSymbol("value") },
				new Sort[] { intSort, arraySort });
		FuncDecl constructor = tupleSort.mkDecl();
		FuncDecl lengthSelector = tupleSort.getFieldDecls()[0];
		FuncDecl valueSelector = tupleSort.getFieldDecls()[1];
		Expr tuple = ctx.mkApp(constructor, two, a);

		out.println("a = " + a);
		out.println("tuple = " + tuple);

		Sort tupleSort2 = tuple.getSort();

		out.println("tupleSort:  " + tupleSort);
		out.println("tupleSort2: " + tupleSort2);

		out.println("tupleSort equals tupleSort2: "
				+ (tupleSort.equals(tupleSort2)));

		out.println("tupleSort2 instanceof TupleSort: "
				+ (tupleSort2 instanceof TupleSort));
		
		
		// how do I make a TupleSort instance out of tupleSort2?
		
		//new TupleSort();
		
		//new TupleSort();
		
		// tupleSort2.

		if (tupleSort.equals(tupleSort2)) {
			IntExpr length = (IntExpr) tuple.getArgs()[0];

			out.println("length = " + length);
		}

	}

	public final static void main(String[] args) throws Z3Exception {
		LittleZ3Test1 test = new LittleZ3Test1();
	}

}
