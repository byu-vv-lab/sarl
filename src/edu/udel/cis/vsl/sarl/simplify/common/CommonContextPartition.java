package edu.udel.cis.vsl.sarl.simplify.common;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.simplify.IF.ContextPartition;

/**
 * <p>
 * Provides a partition of the set of conjunctive clauses of the path condition.
 * Let pc be a path condition (any boolean symbolic expression), and say pc is
 * the conjunction over some set C of clauses. Define an equivalence relation on
 * C as follows: if two clauses have a free (not bound) symbolic constant in
 * common, they are equivalent; complete by taking the transitive closure to an
 * equivalence relation. Say C is the disjoint union of P1, P2, ..., where the
 * Pi are the equivalence classes.
 * </p>
 * 
 * <p>
 * Given any symbolic constant X that occurs in the path condition, there is a
 * unique i such that X occurs in at least one clause in Pi. Let P(X) denote
 * this Pi.
 * </p>
 * 
 * <p>
 * Give any boolean symbolic expression q, let T(q) be the set of all clauses
 * P(X) where X ranges over all symbolic constants occurring in q for which X
 * also occurs in the path condition. Let subpc(pc, q) be the conjunction of
 * these P(X). The expression subpc(pc, q) can be used for the context when
 * attempting to determine the validity of q, and the result will be equivalent
 * to using the full path condition pc.
 * </p>
 * 
 * <p>
 * It is possible that the original path condition is unsatisfiable, yet pc(q)
 * is satisfiable. However in this case any result can be returned since the
 * contract for a prover leaves unspecified what is returned by "valid" when the
 * context is unsatisfiable.
 * </p>
 * 
 * <p>
 * The main method (after construction) is
 * {@link #minimizeFor(SymbolicExpression, PreUniverse)}. The results are
 * cached.
 * </p>
 * 
 * @author siegel
 *
 */
public class CommonContextPartition implements ContextPartition {

	public final static boolean debug = false;

	/**
	 * The equivalence classes. Each class represents the conjunction of a set
	 * of clauses. Any two distinct classes represent mutually disjoint sets of
	 * clauses. It is possible for there to be 0 classes: this happens iff the
	 * context is "true".
	 */
	private BooleanExpression[] classes;

	/**
	 * Maps each symbolic constant X to the index of the equivalence class to
	 * which it belongs (i.e., the index in the array <code>classes</code>), or
	 * <code>null</code> if X does not occur in the context (path condition). It
	 * is possible for this map to be empty, this happens iff no symbolic
	 * constants occur in the context.
	 */
	private Map<SymbolicConstant, Integer> partitionMap = new HashMap<>();

	/**
	 * Cached results of {@link #minimizeFor(SymbolicExpression, PreUniverse)}.
	 */
	private Map<SymbolicExpression, BooleanExpression> minimalContextMap = new HashMap<>();

	/**
	 * Given a boolean-valued symbolic expression <code>boolExpr</code>, returns
	 * a sequence of boolean expressions whose conjunction is equivalent to
	 * <code>boolExpr</code>. The decomposition should be highly non-trivial in
	 * general, and this method should be efficient.
	 * 
	 * @param boolExpr
	 *            a boolean symbolic expression (non-<code>null</code>)
	 * @return sequence of symbolic expressions whose conjunction is equivalent
	 *         to <code>boolExpr</code>
	 */
	private static BooleanExpression[] getClauses(BooleanExpression boolExpr) {
		SymbolicOperator operator = boolExpr.operator();

		if (operator == SymbolicOperator.AND) {
			int numChildren = boolExpr.numArguments();
			BooleanExpression[] clauses;

			if (numChildren == 1) {
				SymbolicCollection<?> collection = (SymbolicCollection<?>) boolExpr
						.argument(0);
				int count = 0;

				clauses = new BooleanExpression[collection.size()];
				for (SymbolicExpression expr : collection) {
					clauses[count] = (BooleanExpression) expr;
					count++;
				}
			} else if (numChildren == 2) {
				ArrayList<BooleanExpression> clauseList = new ArrayList<>();

				for (int i = 0; i < 2; i++) {
					for (BooleanExpression b : getClauses((BooleanExpression) boolExpr
							.argument(i))) {
						clauseList.add(b);
					}
				}
				clauses = new BooleanExpression[clauseList.size()];
				clauseList.toArray(clauses);
			} else {
				throw new RuntimeException("unreachable");
			}
			return clauses;
		} else {
			return new BooleanExpression[] { boolExpr };
		}
	}

	/**
	 * A class to use for temporary storage of data while the partition of the
	 * set of clauses is being computed. An instance represents a changing set
	 * of clauses that will eventually form an equivalence class.
	 * 
	 * @author siegel
	 *
	 */
	class Partition {
		/**
		 * The set of variables which belong to this partition, i.e., the
		 * variables v such v occurs in at least one of the clauses associated
		 * to this.
		 */
		Set<SymbolicConstant> vars = new HashSet<>();

		/**
		 * The indexes of the clauses that comprise this partition. The clauses
		 * will be numbered from 0.
		 */
		BitSet clauses;

		/**
		 * When the algorithm completes, the final set of partitions will form
		 * the equivalence classes, and they will be numbered from 0.
		 */
		int id = -1;

		/**
		 * Forms a new empty partition which is optimized to deal with the given
		 * number of clauses.
		 * 
		 * @param numClauses
		 *            the number of clauses this partition will deal with
		 */
		public Partition(int numClauses) {
			clauses = new BitSet(numClauses);
		}
	}

	/**
	 * Constructs a new context partition by analyzing the given
	 * <code>context</code>, partitioning its set of conjunctive clauses into
	 * mutually disjoint equivalence classes, and storing the resulting
	 * information for later use in variables <code>classes</code> and
	 * <code>partitionMap</code>.
	 * 
	 * @param context
	 *            a non-<code>null</code> boolean expression (typically the path
	 *            condition)
	 */
	public CommonContextPartition(BooleanExpression context,
			PreUniverse universe) {
		BooleanExpression[] clauses = getClauses(context);
		int numClauses = clauses.length;
		Map<SymbolicConstant, Partition> pMap = new HashMap<>();
		int numClasses = 0;

		if (debug) {
			System.out.println("Forming partition for: " + context);
		}

		for (int i = 0; i < numClauses; i++) {
			BooleanExpression clause = clauses[i];
			// the partition containing this clause:
			Partition partition = null;
			Collection<SymbolicConstant> vars = universe
					.getFreeSymbolicConstants(clause);

			/*
			 * Loop invariant: partition == null or parition.clauses contains i.
			 * 
			 * For all symbolic constants v, Partitions p: v is contained in
			 * p.vars iff pMap.get(v)==p.
			 * 
			 * partition starts out null, but is set to something not null in
			 * the first iteration, i.e., in processing the first symbolic
			 * constant to occur in the clause
			 */
			for (SymbolicConstant var : vars) {
				Partition oldPartition = pMap.get(var);

				if (oldPartition == null) {
					// first time we've encountered var
					// put var in the current partition
					if (partition == null) {
						// current clause not in any partition yet
						partition = new Partition(numClauses);
						numClasses++;
						partition.clauses.set(i);
					}
					partition.vars.add(var);
					pMap.put(var, partition);
				} else {
					assert oldPartition.vars.contains(var);
					if (partition == null) {
						// current clause not in any partition yet
						partition = oldPartition;
						partition.clauses.set(i);
					} else if (partition != oldPartition) {
						// merge partition and oldPartition:
						for (SymbolicConstant oldVar : oldPartition.vars)
							pMap.put(oldVar, partition);
						partition.vars.addAll(oldPartition.vars);
						partition.clauses.or(oldPartition.clauses);
						numClasses--;
						// oldPartition can now get swept up by garb. col.
					}
				}
			}
		}

		this.classes = new BooleanExpression[numClasses];
		int classId = 0;

		for (Entry<SymbolicConstant, Partition> entry : pMap.entrySet()) {
			SymbolicConstant var = entry.getKey();
			Partition partition = entry.getValue();
			BitSet bitSet = partition.clauses;
			int id = partition.id;

			if (id < 0) {
				id = partition.id = classId;
				BooleanExpression newClass = universe.trueExpression();

				for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet
						.nextSetBit(i + 1)) {
					newClass = universe.and(newClass, clauses[i]);
				}
				classes[classId] = newClass;
				classId++;
			}
			this.partitionMap.put(var, id);
		}
		if (debug) {
			System.out.println(this);
			System.out.println();
		}
	}

	/**
	 * Given a boolean expression <code>expr</code> returns the boolean
	 * expression <code>subpc(pc, expr)</code> which is a weakening of the
	 * <code>pc</code> used to from this context partitioner and is sufficient
	 * for determining the validity of <code>expr</code>
	 * 
	 * @param expr
	 *            any non-<code>null</code> boolean expression
	 * @param universe
	 *            universe use to perform "and" operations on boolean
	 *            expressions
	 * @return <code>subpc(pc, expr)</code>: the conjunction of the clauses in
	 *         the classes corresponding to the symbolic constants occurring in
	 *         <code>expr</code>
	 */
	@Override
	public BooleanExpression minimizeFor(SymbolicExpression expr,
			PreUniverse universe) {
		BooleanExpression result = minimalContextMap.get(expr);

		if (result == null) {
			Set<SymbolicConstant> vars = universe
					.getFreeSymbolicConstants(expr);
			Set<Integer> resultClasses = new HashSet<>();

			for (SymbolicConstant var : vars) {
				Integer classId = partitionMap.get(var);

				if (classId != null)
					resultClasses.add(classId);
			}

			result = universe.trueExpression();

			for (int classId : resultClasses) {
				result = universe.and(result, classes[classId]);
			}
			if (debug) {
				System.out.println("Context minimization: ");
				System.out.print(this);
				System.out.println("Expression: " + expr);
				System.out.println("Minimized context: " + result);
				System.out.println();
			}
			minimalContextMap.put(expr, result);
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < classes.length; i++) {
			buf.append("Class " + i + ": ");
			buf.append(classes[i]);
			buf.append("\n");
		}
		return buf.toString();
	}

}
