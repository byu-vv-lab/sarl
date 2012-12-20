package edu.udel.cis.vsl.sarl.symbolic.cnf;

import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;

/**
 * Marks a class as representing a "basic" boolean expression. This is a
 * component in the canonical form for boolean expressions.
 * 
 * And (CnfBooleanExpression): conjunction over set of Or
 * 
 * Or: disjunction of set of Basic
 * 
 * Basic: Literal | Quantifier | Relational
 * 
 * Hence the classes implementing Literal, Quantifier, and Relational
 * expressions will be marked "basic" by having them implement this interface.
 * Then certain methods which require basic expressions can use this type.
 * 
 * @author siegel
 * 
 */
public interface BasicExpression extends TreeExpressionIF {

}
