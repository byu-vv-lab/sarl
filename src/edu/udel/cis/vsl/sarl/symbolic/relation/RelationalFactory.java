package edu.udel.cis.vsl.sarl.symbolic.relation;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.expr.common.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.factorpoly.FactoredPolynomial;
import edu.udel.cis.vsl.sarl.symbolic.factorpoly.FactoredPolynomialFactory;
import edu.udel.cis.vsl.sarl.symbolic.rational.RationalExpression;
import edu.udel.cis.vsl.sarl.symbolic.rational.RationalFactory;
import edu.udel.cis.vsl.sarl.symbolic.relation.RelationalExpression.RelationKind;
import edu.udel.cis.vsl.sarl.type.common.CommonSymbolicTypeFactory;

public class RelationalFactory {

	private SymbolicTypeIF booleanType;

	private Map<SymbolicExpressionKey<RelationalExpression>, RelationalExpression> map = new HashMap<SymbolicExpressionKey<RelationalExpression>, RelationalExpression>();

	private TreeExpressionIF zeroInt, zeroReal;

	private RationalFactory rationalFactory;

	private FactoredPolynomialFactory fpFactory;

	public RelationalFactory(CommonSymbolicTypeFactory typeFactory,
			FactoredPolynomialFactory fpFactory,
			RationalFactory rationalFactory, TreeExpressionIF zeroInt,
			TreeExpressionIF zeroReal) {
		this.rationalFactory = rationalFactory;
		this.fpFactory = fpFactory;
		booleanType = typeFactory.booleanType();
		this.zeroInt = zeroInt;
		this.zeroReal = zeroReal;
	}

	public RelationalExpression relational(RelationKind kind,
			TreeExpressionIF expression) {

		return CommonSymbolicExpression.flyweight(map, new RelationalExpression(
				booleanType, kind, expression,
				(expression.type().isInteger() ? zeroInt : zeroReal)));
	}

	private TreeExpressionIF negate(TreeExpressionIF tree) {
		if (tree instanceof FactoredPolynomial) {
			return fpFactory.negate((FactoredPolynomial) tree);
		} else if (tree instanceof RationalExpression) {
			return rationalFactory.negate((RationalExpression) tree);
		} else {
			throw new IllegalArgumentException(
					"Unknown type of expression in relational expression: "
							+ tree);
		}
	}

	public RelationalExpression negate(RelationalExpression relational) {
		switch (relational.relationKind()) {
		case EQ0:
			return relational(RelationKind.NEQ0, relational.expression());
		case NEQ0:
			return relational(RelationKind.EQ0, relational.expression());
		case GT0:
			return relational(RelationKind.GTE0,
					negate(relational.expression()));
		case GTE0: {
			TreeExpressionIF expression = relational.expression();

			if (expression.type().isInteger()) {
				FactoredPolynomial fp = (FactoredPolynomial) expression;
				// !(x>=0) <=> -x>0 <=> -x>=1 <=> -x-1>=0.
				return relational(RelationKind.GTE0, negate(fpFactory.add(fp,
						fpFactory.oneIntFactoredPolynomial())));
			}
			return relational(RelationKind.GT0, negate(relational.expression()));
		}
		default:
			throw new IllegalArgumentException(
					"Unknown kind of relational expression: " + relational);
		}
	}
}
