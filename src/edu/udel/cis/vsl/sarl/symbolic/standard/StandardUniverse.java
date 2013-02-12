package edu.udel.cis.vsl.sarl.symbolic.standard;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import edu.udel.cis.vsl.sarl.IF.BooleanConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.IF.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverseIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstantIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberIF;
import edu.udel.cis.vsl.sarl.IF.prove.SimplifierIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionTypeIF;
import edu.udel.cis.vsl.sarl.expr.common.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.IF.SymbolicConstantExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF.SymbolicKind;
import edu.udel.cis.vsl.sarl.symbolic.concrete.ConcreteFactory;
import edu.udel.cis.vsl.sarl.symbolic.constant.SymbolicConstant;
import edu.udel.cis.vsl.sarl.symbolic.constant.SymbolicConstantFactory;
import edu.udel.cis.vsl.sarl.type.common.CommonSymbolicTypeFactory;
import edu.udel.cis.vsl.sarl.universe.common.CommonSymbolicUniverse;

public class StandardUniverse extends CommonSymbolicUniverse implements
		SymbolicUniverseIF {

	private Vector<SymbolicExpressionIF> expressionVector = new Vector<SymbolicExpressionIF>();

	private Map<SymbolicExpressionKey<StandardSymbolicExpression>, StandardSymbolicExpression> map = new HashMap<SymbolicExpressionKey<StandardSymbolicExpression>, StandardSymbolicExpression>();

	private NumberFactoryIF numberFactory;

	private CommonSymbolicTypeFactory typeFactory;

	private ConcreteFactory concreteFactory;

	private SymbolicConstantFactory constantFactory;

	private SymbolicTypeIF booleanType, integerType, realType;

	public StandardUniverse(NumberFactoryIF numberFactory,
			CommonSymbolicTypeFactory typeFactory) {
		this.numberFactory = numberFactory;
		this.typeFactory = typeFactory;
		concreteFactory = new ConcreteFactory(typeFactory, numberFactory);
		constantFactory = new SymbolicConstantFactory();
		booleanType = typeFactory.booleanType();
		integerType = typeFactory.integerType();
		realType = typeFactory.realType();
	}

	public StandardUniverse(NumberFactoryIF numberFactory) {
		this(numberFactory, new CommonSymbolicTypeFactory());
	}

	public NumberFactoryIF numberFactory() {
		return numberFactory;
	}

	public CommonSymbolicTypeFactory typeFactory() {
		return typeFactory;
	}

	public ConcreteFactory concreteFactory() {
		return concreteFactory;
	}

	public SymbolicConstantFactory constantFactory() {
		return constantFactory;
	}

	private TreeExpressionIF flyweight(StandardSymbolicExpression expression) {
		StandardSymbolicExpression result = CommonSymbolicExpression.flyweight(map,
				expression);

		if (result.id() < 0) {
			result.setId(expressionVector.size());
			expressionVector.add(result);
		}
		return result;
	}

	private TreeExpressionIF unary(SymbolicOperator kind, SymbolicTypeIF type,
			SymbolicExpressionIF arg0) {
		return flyweight(new StandardSymbolicExpression(kind, type,
				new SymbolicExpressionIF[] { arg0 }));
	}

	private TreeExpressionIF binary(SymbolicOperator kind, SymbolicTypeIF type,
			SymbolicExpressionIF arg0, SymbolicExpressionIF arg1) {
		return flyweight(new StandardSymbolicExpression(kind, type,
				new SymbolicExpressionIF[] { arg0, arg1 }));
	}

	private TreeExpressionIF expression(SymbolicOperator kind, SymbolicTypeIF type,
			SymbolicExpressionIF[] args) {
		return flyweight(new StandardSymbolicExpression(kind, type, args));
	}

	@Override
	public TreeExpressionIF add(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		SymbolicTypeIF type = arg0.type();

		assert type.equals(arg1.type());
		assert type.isNumeric();
		return binary(SymbolicOperator.ADD, type, arg0, arg1);
	}

	@Override
	public TreeExpressionIF and(SymbolicExpressionIF[] args) {
		return expression(SymbolicOperator.AND, booleanType, args);
	}

	@Override
	public TreeExpressionIF and(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		return and(new SymbolicExpressionIF[] { arg0, arg1 });
	}

	@Override
	public TreeExpressionIF apply(SymbolicExpressionIF function,
			SymbolicExpressionIF[] arguments) {
		SymbolicExpressionIF[] args = new SymbolicExpressionIF[arguments.length + 1];

		args[0] = function;
		for (int i = 0; i < arguments.length; i++)
			args[i + 1] = arguments[i];
		return expression(SymbolicOperator.APPLY,
				((SymbolicFunctionTypeIF) function.type()).outputType(), args);
	}

	@Override
	public TreeExpressionIF arrayRead(SymbolicExpressionIF array,
			SymbolicExpressionIF index) {
		return binary(SymbolicOperator.ARRAY_READ,
				((SymbolicArrayTypeIF) array.type()).elementType(), array,
				index);
	}

	@Override
	public SymbolicCompleteArrayTypeIF arrayType(SymbolicTypeIF elementType,
			SymbolicExpressionIF extent) {
		return typeFactory.arrayType(elementType, extent);
	}

	@Override
	public TreeExpressionIF arrayWrite(SymbolicExpressionIF array,
			SymbolicExpressionIF index, SymbolicExpressionIF value) {
		return expression(SymbolicOperator.ARRAY_WRITE, array.type(),
				new SymbolicExpressionIF[] { array, index, value });
	}

	@Override
	public SymbolicTypeIF booleanType() {
		return typeFactory.booleanType();
	}

	@Override
	public TreeExpressionIF canonicalize(TreeExpressionIF standard) {
		return standard;
	}

	@Override
	public TreeExpressionIF canonicalizeTree(TreeExpressionIF tree) {
		return tree;
	}

	@Override
	public TreeExpressionIF castToReal(SymbolicExpressionIF numericExpression) {
		return unary(SymbolicOperator.CAST, realType, numericExpression);
	}

	@Override
	public TreeExpressionIF concreteExpression(NumberIF value) {
		return concreteFactory.concrete(value);
	}

	@Override
	public TreeExpressionIF concreteExpression(int value) {
		return concreteFactory.concrete(value);
	}

	@Override
	public TreeExpressionIF concreteExpression(boolean value) {
		return concreteFactory.concrete(value);
	}

	@Override
	public TreeExpressionIF cond(SymbolicExpressionIF predicate,
			SymbolicExpressionIF trueValue, SymbolicExpressionIF falseValue) {
		return expression(SymbolicOperator.COND, trueValue.type(),
				new SymbolicExpressionIF[] { predicate, trueValue, falseValue });
	}

	@Override
	public TreeExpressionIF divide(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		SymbolicTypeIF type = arg0.type();

		if (type.isInteger()) {
			return binary(SymbolicOperator.INT_DIVIDE, integerType, arg0, arg1);
		} else {
			return binary(SymbolicOperator.DIVIDE, realType, arg0, arg1);
		}
	}

	@Override
	public TreeExpressionIF equals(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		return binary(SymbolicOperator.EQUALS, booleanType, arg0, arg1);
	}

	@Override
	public TreeExpressionIF exists(SymbolicConstantIF boundVariable,
			SymbolicExpressionIF predicate) {
		return binary(SymbolicOperator.EXISTS, booleanType,
				symbolicConstantExpression(boundVariable), predicate);
	}

	@Override
	public Boolean extractBoolean(SymbolicExpressionIF expression) {
		if (((TreeExpressionIF) expression).operator().equals(
				SymbolicOperator.CONCRETE_BOOLEAN)) {
			return ((BooleanConcreteExpressionIF) expression).value();
		}
		return null;
	}

	@Override
	public NumberIF extractNumber(SymbolicExpressionIF expression) {
		if (((TreeExpressionIF) expression).operator().equals(
				SymbolicOperator.CONCRETE_NUMBER)) {
			return ((NumericConcreteExpressionIF) expression).value();
		}
		return null;
	}

	@Override
	public SymbolicConstantIF extractSymbolicConstant(
			SymbolicExpressionIF expression) {
		if (((TreeExpressionIF) expression).operator().equals(
				SymbolicOperator.SYMBOLIC_CONSTANT)) {
			return ((SymbolicConstantExpressionIF) expression)
					.symbolicConstant();
		}
		return null;
	}

	@Override
	public TreeExpressionIF forall(SymbolicConstantIF boundVariable,
			SymbolicExpressionIF predicate) {
		return binary(SymbolicOperator.FORALL, booleanType,
				symbolicConstantExpression(boundVariable), predicate);
	}

	@Override
	public SymbolicFunctionTypeIF functionType(SymbolicTypeIF[] inputTypes,
			SymbolicTypeIF outputType) {
		return typeFactory.functionType(inputTypes, outputType);
	}

	@Override
	public SymbolicConstantIF getOrCreateSymbolicConstant(String name,
			SymbolicTypeIF type) {
		return constantFactory.getOrCreateSymbolicConstant(name, type);
	}

	@Override
	public SymbolicConstantIF getSymbolicConstant(String name,
			SymbolicTypeIF type) {
		return constantFactory.getSymbolicConstant(name, type);
	}

	@Override
	public SymbolicTypeIF integerType() {
		return typeFactory.integerType();
	}

	@Override
	public TreeExpressionIF lessThan(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		return binary(SymbolicOperator.LESS_THAN, booleanType, arg0, arg1);
	}

	@Override
	public TreeExpressionIF lessThanEquals(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		return binary(SymbolicOperator.LESS_THAN_EQUALS, booleanType, arg0, arg1);
	}

	@Override
	public TreeExpressionIF minus(SymbolicExpressionIF arg) {
		return unary(SymbolicOperator.NEGATIVE, arg.type(), arg);
	}

	@Override
	public TreeExpressionIF multiply(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		SymbolicTypeIF type = arg0.type();

		if (!type.equals(arg1.type()))
			throw new RuntimeException(
					"Incompatible types in multiplication:\n" + type + "\n"
							+ arg1.type());
		// assert type.equals(arg1.type());
		assert type.isNumeric();
		return binary(SymbolicOperator.MULTIPLY, type, arg0, arg1);
	}

	@Override
	public TreeExpressionIF neq(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		SymbolicTypeIF type = arg0.type();

		assert type.equals(arg1.type());
		assert type.isNumeric();
		return binary(SymbolicOperator.NEQ, type, arg0, arg1);
	}

	@Override
	public SymbolicConstantIF newSymbolicConstant(String name,
			SymbolicTypeIF type) {
		return constantFactory.newSymbolicConstant(name, type);
	}

	@Override
	public TreeExpressionIF not(SymbolicExpressionIF arg) {
		assert arg.type().isBoolean();
		return unary(SymbolicOperator.NOT, booleanType, arg);
	}

	@Override
	public TreeExpressionIF oneInt() {
		return concreteFactory.oneIntExpression();
	}

	@Override
	public TreeExpressionIF oneReal() {
		return concreteFactory.oneRealExpression();
	}

	@Override
	public TreeExpressionIF or(SymbolicExpressionIF[] args) {
		return expression(SymbolicOperator.OR, booleanType, args);
	}

	@Override
	public TreeExpressionIF or(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		return or(new SymbolicExpressionIF[] { arg0, arg1 });
	}

	@Override
	public SymbolicTypeIF realType() {
		return realType;
	}

	@Override
	public TreeExpressionIF tree(SymbolicExpressionIF expression) {
		return (TreeExpressionIF) expression;
	}

	@Override
	public TreeExpressionIF subtract(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		SymbolicTypeIF type = arg0.type();

		assert type.equals(arg1.type());
		assert type.isNumeric();
		return binary(SymbolicOperator.SUBTRACT, type, arg0, arg1);
	}

	@Override
	public SymbolicConstantExpressionIF symbolicConstantExpression(
			SymbolicConstantIF symbolicConstant) {
		return constantFactory.expression(symbolicConstant);
	}

	@Override
	public TreeExpressionIF symbolicConstantTreeExpression(
			SymbolicConstantIF symbolicConstant) {
		return constantFactory.expression(symbolicConstant);
	}

	@Override
	public TreeExpressionIF tupleExpression(SymbolicTupleTypeIF type,
			SymbolicExpressionIF[] components) {
		return expression(SymbolicOperator.CONCRETE_TUPLE, type, components);
	}

	@Override
	public TreeExpressionIF tupleRead(SymbolicExpressionIF tuple,
			SymbolicExpressionIF index) {
		int indexInt = ((IntegerNumberIF) extractNumber(index)).intValue();

		return binary(SymbolicOperator.TUPLE_READ,
				((SymbolicTupleTypeIF) tuple.type()).fieldType(indexInt),
				tuple, index);
	}

	@Override
	public SymbolicTupleTypeIF tupleType(String name,
			SymbolicTypeIF[] fieldTypes) {
		return typeFactory.tupleType(name, fieldTypes);
	}

	@Override
	public TreeExpressionIF tupleWrite(SymbolicExpressionIF tuple,
			SymbolicExpressionIF index, SymbolicExpressionIF value) {
		int indexInt = ((IntegerNumberIF) extractNumber(index)).intValue();

		assert indexInt >= 0;
		return expression(SymbolicOperator.TUPLE_WRITE, tuple.type(),
				new SymbolicExpressionIF[] { tuple, index, value });
	}

	@Override
	public TreeExpressionIF zeroInt() {
		return concreteFactory.zeroIntExpression();
	}

	@Override
	public TreeExpressionIF zeroReal() {
		return concreteFactory.zeroRealExpression();
	}

	@Override
	public TreeExpressionIF expressionWithId(int index) {
		return (TreeExpressionIF) expressionVector.elementAt(index);
	}

	@Override
	public Collection<SymbolicExpressionIF> expressions() {
		return expressionVector;
	}

	@Override
	public int numExpression() {
		return expressionVector.size();
	}

	@Override
	public TreeExpressionIF modulo(SymbolicExpressionIF arg0,
			SymbolicExpressionIF arg1) {
		assert arg0.type().isInteger();
		assert arg1.type().isInteger();
		return binary(SymbolicOperator.MODULO, integerType, arg0, arg1);
	}

	@Override
	public TreeExpressionIF power(SymbolicExpressionIF base,
			SymbolicExpressionIF exponent) {
		return binary(SymbolicOperator.POWER, base.type(), base, exponent);
	}

	@Override
	public TreeExpressionIF tupleRead(SymbolicExpressionIF tuple, int index) {
		return tupleRead(tuple, concreteExpression(index));
	}

	@Override
	public TreeExpressionIF tupleWrite(SymbolicExpressionIF tuple, int index,
			SymbolicExpressionIF value) {
		return tupleWrite(tuple, concreteExpression(index), value);
	}

	@Override
	public TreeExpressionIF make(SymbolicOperator operator, SymbolicTypeIF type,
			SymbolicExpressionIF[] arguments) {
		return (TreeExpressionIF) super.make(operator, type, arguments);
	}

	@Override
	public SimplifierIF simplifier(SymbolicExpressionIF assumption) {
		throw new RuntimeException("No simplification in standard universe");
	}

	@Override
	public Collection<SymbolicConstant> symbolicConstants() {
		return constantFactory.symbolicConstants();
	}

	@Override
	public SymbolicExpressionIF arrayExpression(SymbolicExpressionIF origin,
			SymbolicExpressionIF[] elements) {
		TreeExpressionIF result = (TreeExpressionIF) origin;
		int length = elements.length;

		for (int i = 0; i < length; i++) {
			TreeExpressionIF element = (TreeExpressionIF) elements[i];

			if (element != null) {
				result = arrayWrite(result, concreteExpression(i), element);
			}
		}
		return result;
	}

	@Override
	public SymbolicExpressionIF[] getArrayElements(SymbolicExpressionIF array) {
		return new SymbolicExpressionIF[0];
	}

	@Override
	public SymbolicExpressionIF getArrayOrigin(SymbolicExpressionIF array) {
		return array;
	}

	@Override
	public SymbolicExpressionIF arrayLambda(
			SymbolicCompleteArrayTypeIF arrayType, SymbolicExpressionIF function) {
		return unary(SymbolicOperator.ARRAY_LAMBDA, arrayType, function);
	}

	@Override
	public SymbolicExpressionIF lambda(SymbolicConstantIF boundVariable,
			SymbolicExpressionIF expression) {
		SymbolicConstantExpressionIF symbolicConstantExpression = symbolicConstantExpression(boundVariable);
		SymbolicFunctionTypeIF type = typeFactory.functionType(
				new SymbolicTypeIF[] { boundVariable.type() },
				expression.type());

		return binary(SymbolicOperator.LAMBDA, type, symbolicConstantExpression,
				expression);
	}

	@Override
	public SymbolicExpressionIF length(SymbolicExpressionIF array) {
		SymbolicArrayTypeIF arrayType = (SymbolicArrayTypeIF) array.type();

		if (arrayType instanceof SymbolicCompleteArrayTypeIF) {
			return ((SymbolicCompleteArrayTypeIF) arrayType).extent();
		} else {
			StandardSymbolicExpression standardArray = (StandardSymbolicExpression) array;
			SymbolicOperator kind = standardArray.operator();

			if (kind == SymbolicOperator.ARRAY_WRITE) {
				return length(standardArray.argument(0));
			}
			return unary(SymbolicOperator.LENGTH, integerType, standardArray);
		}
	}

	@Override
	public SymbolicArrayTypeIF arrayType(SymbolicTypeIF elementType) {
		return typeFactory.arrayType(elementType);
	}

	@Override
	public SymbolicUnionTypeIF unionType(String name,
			SymbolicTypeIF[] memberTypes) {
		return typeFactory.unionType(name, memberTypes);
	}

	@Override
	public SymbolicExpressionIF unionInject(SymbolicUnionTypeIF unionType,
			int memberIndex, TreeExpressionIF object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF unionTest(SymbolicUnionTypeIF unionType,
			int memberIndex, TreeExpressionIF object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbolicExpressionIF unionExtract(SymbolicUnionTypeIF unionType,
			int memberIndex, TreeExpressionIF object) {
		// TODO Auto-generated method stub
		return null;
	}

}
