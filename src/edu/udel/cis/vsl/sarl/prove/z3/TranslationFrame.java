package edu.udel.cis.vsl.sarl.prove.z3;

import java.util.HashMap;
import java.util.Map;

import com.microsoft.z3.Expr;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

public class TranslationFrame {

	Map<SymbolicExpression, Expr> expressionMap = new HashMap<>();
	
	Map<SymbolicConstant, Expr> variableMap = new HashMap<>();

}
