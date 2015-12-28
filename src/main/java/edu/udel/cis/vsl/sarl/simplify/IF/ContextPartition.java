package edu.udel.cis.vsl.sarl.simplify.IF;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;

/**
 * <p>
 * Provides a partition of the set of conjunctive clauses of a
 * {@link BooleanExpression} (the <strong>context</strong>) and a method to use
 * that partition for <strong>context minimization</strong>. Given a predicate
 * (a {@link BooleanExpression} to check for validity or to be simplified),
 * method {@link #minimizeFor(SymbolicExpression, PreUniverse)} produces a new
 * context which is possibly weaker than the original one, but which is
 * guaranteed to produce an equivalent result when used as the context for
 * validity or simplification.
 * </p>
 * 
 * <p>
 * Let <i>pc</i> be the context, and say <i>pc</i> is the conjunction over some
 * set <i>C</i> of clauses. Define an equivalence relation on <i>C</i> as
 * follows: if two clauses have a free (not bound) symbolic constant in common,
 * they are equivalent; complete by taking the transitive closure to an
 * equivalence relation. Say <i>C</i> is the disjoint union of <i>P1</i>,
 * <i>P2</i>, ..., where the <i>Pi</i> are the equivalence classes.
 * </p>
 * 
 * <p>
 * Given any symbolic constant <i>X</i> that occurs in the path condition, there
 * is a unique <i>i</i> such that <i>X</i> occurs in at least one clause in
 * <i>Pi</i>. Let <i>P(X)</i> denote this <i>Pi</i>.
 * </p>
 * 
 * <p>
 * Give any boolean symbolic expression <i>q</i>, let <i>T(q)</i> be the set of
 * all clauses <i>P(X)</i> where <i>X</i> ranges over all symbolic constants
 * occurring in <i>q</i> for which <i>X</i> also occurs in <i>pc</i>. Let
 * <i>subpc(pc, q)</i> be the conjunction of these <i>P(X)</i>. The expression
 * <i>subpc(pc, q)</i> can be used for the context when attempting to determine
 * the validity of <i>q</i>, and the result will be equivalent to using the full
 * path condition <i>pc</i>.
 * </p>
 * 
 * <p>
 * It is possible that the context <i>pc</i> is unsatisfiable, yet <i>pc(q)</i>
 * is satisfiable. However in this case any result can be returned for a
 * validity request, since the contract for a prover leaves unspecified what is
 * returned by {@link TheoremProver#valid(BooleanExpression)} when the context
 * is unsatisfiable.
 * </p>
 * 
 * <p>
 * The main method (after construction) is
 * {@link #minimizeFor(SymbolicExpression, PreUniverse)}. The results are
 * cached.
 * </p>
 * 
 * @author Stephen F. Siegel
 */
public interface ContextPartition {

	/**
	 * Returns a formula equivalent to or weaker than the original context but
	 * which can be used as the context for check validity or simplifying
	 * <code>expr</code>.
	 * 
	 * @param expr
	 *            any non-<code>null</code> boolean expression
	 * @param universe
	 *            the symbolic universe used to construct new symbolic
	 *            expressions and which produced the context and
	 *            <code>expr</code>
	 * @return the reduced context
	 */
	BooleanExpression minimizeFor(SymbolicExpression expr, PreUniverse universe);

}
