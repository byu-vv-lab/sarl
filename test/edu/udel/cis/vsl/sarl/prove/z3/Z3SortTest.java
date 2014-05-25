package edu.udel.cis.vsl.sarl.prove.z3;

import java.io.PrintStream;

import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.ArraySort;
import com.microsoft.z3.Context;
import com.microsoft.z3.DatatypeSort;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.IntNum;
import com.microsoft.z3.IntSort;
import com.microsoft.z3.Sort;
import com.microsoft.z3.Symbol;
import com.microsoft.z3.TupleSort;
import com.microsoft.z3.Z3Exception;

public class Z3SortTest {
	public final static void main(String[] args) throws Z3Exception {
		PrintStream out = System.out;
		Context ctx = new Context();
		IntNum two = ctx.mkInt(2);
		IntSort intSort = ctx.mkIntSort();
		ArraySort arraySort = ctx.mkArraySort(intSort, intSort);
		ArrayExpr a = (ArrayExpr) ctx.mkConst("a", arraySort);
		TupleSort tupleSort1 = ctx.mkTupleSort(ctx.mkSymbol("make-tuple"),
				new Symbol[] { ctx.mkSymbol("length"), ctx.mkSymbol("value") },
				new Sort[] { intSort, arraySort });
		FuncDecl constructor1 = tupleSort1.mkDecl();
		FuncDecl accessor10 = tupleSort1.getFieldDecls()[0];
		FuncDecl accessor11 = tupleSort1.getFieldDecls()[1];
		Expr tuple = ctx.mkApp(constructor1, two, a);
		Sort tupleSort2 = tuple.getSort();

		// Now we have two Sorts: the original TupleSort created
		// with ctx.mkTupleSort, and the Sort obtained by the Expr
		// tuple. What is the relation beween them? Apparently
		// they are not ==, but they are equal, even though the
		// first is an instance of TupleSort and the second isn't.
		// However they are both instances of DatatypeSort...

		out.println("tuple: " + tuple);
		out.println("tupleSort1: " + tupleSort1);
		out.println("tupleSort2: " + tupleSort2);
		out.println("tupleSort1 kind: " + tupleSort1.getSortKind());
		out.println("tupleSort2 kind: " + tupleSort2.getSortKind());
		out.println("tupleSort1 equals tupleSort2: "
				+ (tupleSort1.equals(tupleSort2)));
		out.println("tupleSort1 instsanceof TupleSort: "
				+ (tupleSort1 instanceof TupleSort));
		out.println("tupleSort2 instanceof TupleSort: "
				+ (tupleSort2 instanceof TupleSort));
		out.println("tupleSort2 instanceof DatatypeSort: "
				+ (tupleSort2 instanceof DatatypeSort));

		// Are the constructors and accessors obtained from
		// the new Sort the same as those obtained from the
		// original Sorr? Apparently they are not "equal",
		// but they behave exactly the same...

		DatatypeSort datatypeSort = (DatatypeSort) tupleSort2;
		int numConstructors = datatypeSort.getNumConstructors();

		out.println("numConstructors: " + numConstructors);

		FuncDecl constructor2 = datatypeSort.getConstructors()[0];
		FuncDecl accessor20 = datatypeSort.getAccessors()[0][0];
		FuncDecl accessor21 = datatypeSort.getAccessors()[0][1];

		out.println("constructor1: " + constructor1);
		out.println("constructor2: " + constructor2);
		out.println("constructor1 equals constructor2: "
				+ (constructor1.equals(constructor2)));
		out.println();

		out.println("accessor10: " + accessor10);
		out.println("accessor20: " + accessor20);
		out.println("accessor10 equals accessor20: "
				+ (accessor10.equals(accessor20)));
		out.println();

		out.println("accessor11: " + accessor11);
		out.println("accessor21: " + accessor21);
		out.println("accessor11 equals accessor21: "
				+ (accessor11.equals(accessor21)));

		Expr field10 = accessor10.apply(tuple);
		Expr field20 = accessor20.apply(tuple);

		out.println("field10: " + field10);
		out.println("field20: " + field20);
		out.println("field10 equals field20: " + field10.equals(field20));

		// Does the application of the accessor do the obvious
		// simplification? Apparently not...

		out.println("field20 equals two: " + two.equals(field20));

		// Is field20 an IntExpr: Yes!
		out.println("is field20 an IntExpr: " + (field20 instanceof IntExpr));
		out.println();

		// What is the FunclDecl associated to a symbolic constant?
		// Apparently a function with same name as the constant
		// which takes 0 arguments...

		Expr t = ctx.mkConst("t", tupleSort1);
		FuncDecl tDecl = t.getFuncDecl();
		Symbol tDeclName = tDecl.getName();

		out.println("t: " + t);
		out.println("tDecl: " + tDecl);
		out.println("tDeclName: " + tDeclName);
		out.println();

		// Can the same symbol be used to make two unrelated
		// tuple sorts? Apparently yes.

		{
			Symbol s1 = ctx.mkSymbol("MySymbol");
			TupleSort mySort1 = ctx.mkTupleSort(s1,
					new Symbol[] { ctx.mkSymbol("myAccessor") },
					new Sort[] { intSort });
			TupleSort mySort2 = ctx.mkTupleSort(s1,
					new Symbol[] { ctx.mkSymbol("myAccessor2") },
					new Sort[] { intSort });

			out.println("mySort1: " + mySort1);
			out.println("mySort2: " + mySort2);
			out.println("mySort1 equals mySort2: " + (mySort1.equals(mySort2)));
			out.println("Do the two sorts have the same symbol name: "
					+ mySort1.getName().equals(mySort2.getName()));
			out.println("Do the two sorts have the same symbol name string: "
					+ mySort1.getName().toString()
							.equals(mySort2.getName().toString()));
		}

	}
}
