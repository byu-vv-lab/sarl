package edu.udel.cis.vsl.sarl.symbolic.cnf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import edu.udel.cis.vsl.sarl.symbolic.BooleanPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.cnf.QuantifierExpression.Quantifier;
import edu.udel.cis.vsl.sarl.symbolic.concrete.ConcreteFactory;
import edu.udel.cis.vsl.sarl.symbolic.constant.SymbolicConstantExpression;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpressionKey;
import edu.udel.cis.vsl.sarl.symbolic.relation.RelationalExpression;
import edu.udel.cis.vsl.sarl.symbolic.relation.RelationalFactory;
import edu.udel.cis.vsl.sarl.symbolic.type.SymbolicTypeFactory;

/**
 * 
 * This is a factory for producing boolean expressions in a canonical form.
 * 
 * Here is the syntax defining the canonical form for a boolean expression:
 * 
 * And (CnfBooleanExpression): this is the root of the expression. It represents
 * a conjunction over a set of "Or" clauses.
 * 
 * Or: disjunction of set of Basic
 * 
 * Basic: Literal | Quantifier | Relational
 * 
 * Relational: e>0 | e=0 | e>=0 | e!=0
 * 
 * Quantifier: Q[SymbolicConstant].And
 * 
 * (Q is either "forall" or "exists". "And" is the top-level element (instance
 * of CnfBooleanExpression). The SymbolicConstant is the bound variable.)
 * 
 * Literal: BooleanPrimitive | !BooleanPrimitive
 * 
 * BooleanPrimitive: SymbolicConstant | f(x) | a[i] | r.f
 * 
 * Note:
 * 
 * not(e>0) => -e>=0, not(e=0) => e!=0, not(e!=0) => e=0. not(e>=0) => -e>0,
 * 
 * Note: true = And{}. false=And{Or{}}.
 * 
 * This factory uses the flyweight pattern. It also represents sets of elements
 * as arrays in which the elements are ordered according to some total order.
 * 
 * @author siegel
 */
public class CnfFactory {

	public final static boolean DEBUG = false;

	ConcreteFactory concreteFactory;

	SymbolicTypeFactory typeFactory;

	RelationalFactory relationalFactory;

	private SymbolicTypeIF booleanType;

	private CnfBooleanExpression trueCanonical, falseCanonical;

	private Map<SymbolicExpressionKey<CnfBooleanExpression>, CnfBooleanExpression> andMap = new HashMap<SymbolicExpressionKey<CnfBooleanExpression>, CnfBooleanExpression>();

	private Map<SymbolicExpressionKey<OrExpression>, OrExpression> orMap = new HashMap<SymbolicExpressionKey<OrExpression>, OrExpression>();

	private Map<SymbolicExpressionKey<LiteralExpression>, LiteralExpression> literalMap = new HashMap<SymbolicExpressionKey<LiteralExpression>, LiteralExpression>();

	private Map<SymbolicExpressionKey<QuantifierExpression>, QuantifierExpression> quantifierMap = new HashMap<SymbolicExpressionKey<QuantifierExpression>, QuantifierExpression>();

	public CnfFactory(RelationalFactory relationalFactory,
			ConcreteFactory concreteFactory) {
		this.relationalFactory = relationalFactory;
		this.concreteFactory = concreteFactory;
		typeFactory = concreteFactory.typeFactory();
		booleanType = typeFactory.booleanType();
		trueCanonical = cnf(new OrExpression[] {});
		falseCanonical = cnf(new OrExpression[] { or(new BasicExpression[] {}) });
	}

	/* Production of Basic Expression (literals, quantifier) */

	private LiteralExpression literal(boolean not, BooleanPrimitive primitive) {
		return SymbolicExpression.flyweight(literalMap, new LiteralExpression(
				booleanType, not, primitive));
	}

	private LiteralExpression literal(BooleanPrimitive primitive) {
		return literal(false, primitive);
	}

	private QuantifierExpression forallExpression(
			SymbolicConstantExpression variable, CnfBooleanExpression predicate) {
		return SymbolicExpression
				.flyweight(quantifierMap, new QuantifierExpression(
						Quantifier.FORALL, variable, predicate));
	}

	private QuantifierExpression existsExpression(
			SymbolicConstantExpression variable, CnfBooleanExpression predicate) {
		return SymbolicExpression
				.flyweight(quantifierMap, new QuantifierExpression(
						Quantifier.EXISTS, variable, predicate));
	}

	/* Production of Or Expressions */

	public OrExpression or(BasicExpression[] clauses) {

		// debug: check no two clauses are the same:
		if (DEBUG) {
			HashSet<BasicExpression> clauseSet = new HashSet<BasicExpression>();

			for (BasicExpression clause : clauses) {
				if (clauseSet.contains(clause)) {
					throw new IllegalArgumentException("Clause occurs twice: "
							+ clause);
				}
				clauseSet.add(clause);
			}
		}
		return SymbolicExpression.flyweight(orMap, new OrExpression(
				booleanType, clauses));
	}

	public OrExpression or(BasicExpression basic) {
		return or(new BasicExpression[] { basic });
	}

	/* Production of CNF Boolean Expressions */

	private CnfBooleanExpression cnf(OrExpression[] clauses) {
		// debug: check no two clauses are the same:
		if (DEBUG) {
			HashSet<OrExpression> clauseSet = new HashSet<OrExpression>();

			for (OrExpression clause : clauses) {
				if (clauseSet.contains(clause)) {
					throw new IllegalArgumentException("Clause occurs twice: "
							+ clause);
				}
				clauseSet.add(clause);
			}
		}
		return SymbolicExpression.flyweight(andMap, new CnfBooleanExpression(
				booleanType, clauses));
	}

	public CnfBooleanExpression cnf(OrExpression or) {
		return cnf(new OrExpression[] { or });
	}

	public CnfBooleanExpression cnf(BasicExpression basic) {
		return cnf(or(basic));
	}

	public CnfBooleanExpression cnf(BooleanPrimitive primitive) {
		return cnf(literal(primitive));
	}

	/* Operations */

	public CnfBooleanExpression forall(SymbolicConstantExpression variable,
			CnfBooleanExpression predicate) {
		int numClauses = predicate.numClauses();
		CnfBooleanExpression result = trueCanonical;

		for (int i = 0; i < numClauses; i++) {
			OrExpression clause = predicate.clause(i);

			result = and(result, cnf(forallExpression(variable, cnf(clause))));
		}
		return result;
	}

	public CnfBooleanExpression exists(SymbolicConstantExpression variable,
			CnfBooleanExpression predicate) {
		// todo: convert to disjunctive normal form and distribute exists
		return cnf(existsExpression(variable, predicate));
	}

	public CnfBooleanExpression booleanExpression(boolean value) {
		return (value ? trueCanonical : falseCanonical);
	}

	public CnfBooleanExpression booleanExpression(BooleanPrimitive primitive) {
		assert primitive.type().isBoolean();
		return cnf(primitive);
	}

	// TODO:
	// more intelligent: if any clause x is "contained in" a clause y, drop y.
	// could be expensive (quadratic) to implement?

	/**
	 * Returns the result of "and" applied to two expressions in canonical form.
	 * To keep the set of clauses ordered, it does an order-preserving merge.
	 */
	public CnfBooleanExpression and(CnfBooleanExpression arg0,
			CnfBooleanExpression arg1) {
		int numClauses0 = arg0.numClauses();
		int numClauses1 = arg1.numClauses();
		int index0 = 0, index1 = 0;
		LinkedList<OrExpression> union = new LinkedList<OrExpression>();

		while (index0 < numClauses0 && index1 < numClauses1) {
			OrExpression or0 = arg0.clause(index0);
			OrExpression or1 = arg1.clause(index1);

			// p^false=false:
			if (or0.numClauses() == 0 || or1.numClauses() == 0)
				return falseCanonical;

			int compare = SymbolicExpression.compare(or0, or1);

			if (compare == 0) {
				union.add(or0);
				index0++;
				index1++;
			} else if (compare < 0) {
				union.add(or0);
				index0++;
			} else {
				union.add(or1);
				index1++;
			}
		}
		while (index0 < numClauses0) {
			union.add(arg0.clause(index0));
			index0++;
		}
		while (index1 < numClauses1) {
			union.add(arg1.clause(index1));
			index1++;
		}
		return cnf((OrExpression[]) union
				.toArray(new OrExpression[union.size()]));
	}

	public CnfBooleanExpression or(CnfBooleanExpression arg0,
			CnfBooleanExpression arg1) {
		// (p && q) || r = (p || r) && (q || r)
		CnfBooleanExpression result = trueCanonical;
		int numClauses0 = arg0.numClauses();
		int numClauses1 = arg1.numClauses();

		for (int i = 0; i < numClauses0; i++)
			for (int j = 0; j < numClauses1; j++)
				result = and(result, or(arg0.clause(i), arg1.clause(j)));
		return result;
	}

	private CnfBooleanExpression or(OrExpression arg0, OrExpression arg1) {
		int numClauses0 = arg0.numClauses();
		int numClauses1 = arg1.numClauses();
		int index0 = 0, index1 = 0;
		LinkedList<BasicExpression> union = new LinkedList<BasicExpression>();

		while (index0 < numClauses0 && index1 < numClauses1) {
			BasicExpression basic0 = arg0.clause(index0);
			BasicExpression basic1 = arg1.clause(index1);
			int compare = SymbolicExpression.compare(
					(SymbolicExpression) basic0, (SymbolicExpression) basic1);

			if (compare == 0) {
				union.add(basic0);
				index0++;
				index1++;
			} else if (compare < 0) {
				union.add(basic0);
				index0++;
			} else {
				union.add(basic1);
				index1++;
			}
		}
		while (index0 < numClauses0) {
			union.add(arg0.clause(index0));
			index0++;
		}
		while (index1 < numClauses1) {
			union.add(arg1.clause(index1));
			index1++;
		}
		return cnf(or((BasicExpression[]) union
				.toArray(new BasicExpression[union.size()])));
	}

	private CnfBooleanExpression not(BasicExpression arg0) {
		if (arg0 instanceof LiteralExpression) {
			LiteralExpression literal = (LiteralExpression) arg0;

			return cnf(new OrExpression[] { or(new BasicExpression[] { literal(
					!literal.not(), literal.primitive()) }) });
		} else if (arg0 instanceof QuantifierExpression) {
			QuantifierExpression expression = (QuantifierExpression) arg0;
			CnfBooleanExpression predicate = expression.predicate();
			Quantifier quantifier = expression.quantifier();
			SymbolicConstantExpression variable = expression.variable();

			if (quantifier == Quantifier.EXISTS)
				return forall(variable, not(predicate));
			else if (quantifier == Quantifier.FORALL)
				return exists(variable, not(predicate));
			else
				throw new RuntimeException("Unknown quantifier: " + quantifier);
		} else if (arg0 instanceof RelationalExpression) {
			RelationalExpression relational = (RelationalExpression) arg0;

			return cnf(relationalFactory.negate(relational));
		} else {
			throw new IllegalArgumentException(
					"Unknown BasicExpression kind: arg0");
		}
	}

	private CnfBooleanExpression not(OrExpression arg0) {
		CnfBooleanExpression result = trueCanonical;
		int numClauses = arg0.numClauses();

		for (int i = 0; i < numClauses; i++)
			result = and(result, not(arg0.clause(i)));
		return result;
	}

	public CnfBooleanExpression not(CnfBooleanExpression arg0) {
		CnfBooleanExpression result = falseCanonical;
		int numClauses = arg0.numClauses();

		for (int i = 0; i < numClauses; i++)
			result = or(result, not(arg0.clause(i)));
		return result;

	}

	public BooleanPrimitive extractPrimitive(CnfBooleanExpression cnf) {
		if (cnf.numClauses() == 1) {
			OrExpression or = cnf.clause(0);

			if (or.numClauses() == 1) {
				BasicExpression basic = or.clause(0);

				if (basic instanceof LiteralExpression) {
					LiteralExpression literal = (LiteralExpression) basic;

					if (literal.not() == false) {
						return literal.primitive();
					}
				}
			}
		}
		return null;
	}

	/*
	 * Improvements:
	 * 
	 * define implies(p,q)
	 * 
	 * implies(and_ip_i, and_jq_j) : forall j.implies(and_ip_i,q_j)
	 * 
	 * implies(and_ip_i, q) : exists j.implies(p_j,q)
	 * 
	 * implies(or_ip_i, or_jq_j) : forall i.implies(p_i, or_jq_j)
	 * 
	 * implies(p, or_jq_j) : exists j.implies(p,q_j)
	 * 
	 * when performing or: simplify p||!p to true
	 * 
	 * when performing and: simplify p&&!p to false
	 * 
	 * when performing and:
	 * 
	 * and(and_ip_i, and_jq_j): loop over all i,j. if implies(p_i,q_j), delete
	 * q_j. if implies(q_j,p_i) : delete p_i. Then perform usual mesh of streams
	 * algorithm.
	 * 
	 * or(or_ip_i, or_jq_j): loop over all i,j. if implies(p_i,q_j), delete p_i.
	 * if implies(q_j,p_i), delte q_j. then mesh.
	 * 
	 * basic expressions: implies(b,b) is true.
	 * 
	 * implies(x>0,y>0): now algebra is required. Bounds checking. Beyond scope?
	 * simplify will do it. implies(x=0,y>0). implies(x!=0,y>0). etc. Cases:
	 * =,>,!=,!> squared. Could put this reasoning in Relation class. All basic
	 * expressions must support implies?
	 */

	// public CnfBooleanExpression simplify(CnfBooleanExpression cnf,
	// Map<BooleanPrimitive, Boolean> constantMap) {
	// int numClauses = cnf.numClauses();
	// OrExpression[] newOrs = new OrExpression[numClauses];
	//
	// for (int i = 0; i < numClauses; i++) {
	// newOrs[i] = simplify(cnf.clause(i), constantMap);
	// }
	// return cnf(newOrs);
	// }
	//
	// private OrExpression simplify(OrExpression or,
	// Map<BooleanPrimitive, Boolean> constantMap) {
	// int numClauses = or.numClauses();
	// BasicExpression[] newBasics = new BasicExpression[numClauses];
	//
	// for (int i = 0; i < numClauses; i++) {
	// newBasics[i] = simplify(or.clause(i), constantMap);
	// }
	// return or(newBasics);
	// }
	//
	// // get rid of simplify from here....not enough info to do it
	// // properly (need to go down to primitive level)
	//
	// private BasicExpression simplify(BasicExpression basic,
	// Map<BooleanPrimitive, Boolean> constantMap) {
	// if (basic instanceof BooleanConcreteExpression) {
	// return basic;
	// } else if (basic instanceof LiteralExpression) {
	// LiteralExpression literal = (LiteralExpression) basic;
	// BooleanPrimitive primitive = literal.primitive();
	// boolean not = literal.not();
	// Boolean value = constantMap.get(primitive);
	//
	// if (value == null) {
	// return basic;
	// } else {
	// boolean result = (not ? !value : value);
	//
	// return (result ? trueValue : falseValue);
	// }
	// } else {
	// throw new RuntimeException("Unknown type of basic expression: "
	// + basic);
	// }
	// }
}
