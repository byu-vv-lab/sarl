/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.expr.common;

import java.util.Collection;
import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.ArrayElementReference;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.OffsetReference;
import edu.udel.cis.vsl.sarl.IF.expr.ReferenceExpression;
import edu.udel.cis.vsl.sarl.IF.expr.ReferenceExpression.ReferenceKind;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.expr.TupleComponentReference;
import edu.udel.cis.vsl.sarl.IF.expr.UnionMemberReference;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

/**
 * CommonExpressionFactory is used to create many CommonExpressions.
 * 
 * Implements the ExpressionFactory interface.
 * 
 * @author siegel
 * 
 */
public class CommonExpressionFactory implements ExpressionFactory {

	private ObjectFactory objectFactory;

	private ExpressionComparator expressionComparator;

	private NumericExpressionFactory numericFactory;

	private NumberFactory numberFactory;

	private BooleanExpressionFactory booleanFactory;

	private SymbolicTypeFactory typeFactory;

	private CollectionFactory collectionFactory;

	private SymbolicExpression nullExpression;

	private SymbolicIntegerType integerType;

	private SymbolicType referenceType;

	private SymbolicConstant arrayElementReferenceFunction;

	private SymbolicConstant tupleComponentReferenceFunction;

	private SymbolicConstant unionMemberReferenceFunction;

	private SymbolicConstant offsetReferenceFunction;

	private ReferenceExpression nullReference;

	private ReferenceExpression identityReference;

	private SymbolicSequence<NumericExpression> zeroSequence;

	private SymbolicSequence<NumericExpression> oneSequence;

	/**
	 * Constructor that builds a CommonExpressionFactory.
	 * 
	 * @param numericFactory 
	 */
	public CommonExpressionFactory(NumericExpressionFactory numericFactory) {
		NumericExpression zero = numericFactory.zeroInt();
		NumericExpression one = numericFactory.oneInt();

		this.numericFactory = numericFactory;
		this.numberFactory = numericFactory.numberFactory();
		this.objectFactory = numericFactory.objectFactory();
		this.booleanFactory = numericFactory.booleanFactory();
		this.typeFactory = numericFactory.typeFactory();
		this.collectionFactory = numericFactory.collectionFactory();
		this.expressionComparator = new ExpressionComparator(
				numericFactory.comparator(), objectFactory.comparator(),
				typeFactory.typeComparator());
		this.nullExpression = objectFactory.canonic(expression(
				SymbolicOperator.NULL, null, new SymbolicObject[] {}));
		typeFactory.setExpressionComparator(expressionComparator);
		collectionFactory.setElementComparator(expressionComparator);
		objectFactory.setExpressionComparator(expressionComparator);
		zeroSequence = objectFactory.canonic(collectionFactory
				.singletonSequence(zero));
		oneSequence = objectFactory.canonic(collectionFactory
				.singletonSequence(one));
	}

	/**
	 * Private method that extracts integer value from NumericExpression.
	 * 
	 * @param expr
	 * 
	 * @return int
	 */
	private int extractInt(NumericExpression expr) {
		int result = ((IntegerNumber) ((NumberObject) expr.argument(0))
				.getNumber()).intValue();

		return result;
	}

	/**
	 * Private method that builds a concrete ReferenceExpression.
	 * 
	 * @param operator
	 * @param arg0
	 * 
	 * @return ReferenceExpression
	 */
	private ReferenceExpression concreteReferenceExpression(
			SymbolicOperator operator, SymbolicObject arg0) {
		if (operator != SymbolicOperator.CONCRETE)
			throw new SARLInternalException("Expected CONCRETE operator, not "
					+ operator);
		if (zeroSequence.equals(arg0))
			return nullReference;
		if (oneSequence.equals(arg0))
			return identityReference;
		throw new SARLInternalException(
				"Unexpected concrete argument to reference: " + arg0);
	}

	/**
	 * Private method that builds a non-trivial ReferenceExpression.
	 * 
	 * @param operator
	 * @param arg0
	 * @param arg1
	 * 
	 * @return SymbolicExpression
	 */
	private SymbolicExpression nonTrivialReferenceExpression(
			SymbolicOperator operator, SymbolicObject arg0, SymbolicObject arg1) {
		if (operator == SymbolicOperator.APPLY) {
			SymbolicExpression function = (SymbolicExpression) arg0;
			ReferenceKind kind = null;

			if (arrayElementReferenceFunction.equals(function))
				kind = ReferenceKind.ARRAY_ELEMENT;
			else if (tupleComponentReferenceFunction.equals(function))
				kind = ReferenceKind.TUPLE_COMPONENT;
			else if (unionMemberReferenceFunction.equals(function))
				kind = ReferenceKind.UNION_MEMBER;
			else if (offsetReferenceFunction.equals(function))
				kind = ReferenceKind.OFFSET;
			if (kind != null) {
				SymbolicSequence<?> parentIndexSequence = (SymbolicSequence<?>) arg1;
				ReferenceExpression parent = (ReferenceExpression) parentIndexSequence
						.get(0);
				NumericExpression index = (NumericExpression) parentIndexSequence
						.get(1);

				if (kind == ReferenceKind.ARRAY_ELEMENT)
					return arrayElementReference(parent, index);
				if (kind == ReferenceKind.TUPLE_COMPONENT)
					return tupleComponentReference(parent,
							objectFactory.intObject(extractInt(index)));
				if (kind == ReferenceKind.UNION_MEMBER)
					return unionMemberReference(parent,
							objectFactory.intObject(extractInt(index)));
				if (kind == ReferenceKind.OFFSET)
					return offsetReference(parent, index);
				throw new SARLInternalException("unreachable");
			}
		}
		return new CommonSymbolicExpression(operator, referenceType, arg0, arg1);
	}

	/**
	 * Reconstructs a reference expression from operator and arguments.
	 * Arguments array can have length 1 (for concrete case: null or identity
	 * reference), or 2 (for non-trivial reference case: arg0 is function and
	 * arg1 is parent-index sequence).
	 * 
	 * @param operator
	 *            {@link SymbolicOperator.CONCRETE} or
	 *            {@link SymbolicOperator.APPLY}
	 * @param arguments
	 *            array of length 1 or 2 as specified above
	 * @return instance of ReferenceExpression determined by above parameters
	 */
	private SymbolicExpression referenceExpression(SymbolicOperator operator,
			SymbolicObject[] arguments) {
		if (operator == SymbolicOperator.CONCRETE)
			return concreteReferenceExpression(operator, arguments[0]);
		else if (operator == SymbolicOperator.APPLY)
			return nonTrivialReferenceExpression(operator, arguments[0],
					arguments[1]);
		return new CommonSymbolicExpression(operator, referenceType, arguments);
		// throw new SARLInternalException(
		// "Unexpected operator in reference expression: " + operator);
	}

	/**
	 * Method that initializes a CommonExpressionFactory.
	 */
	@Override
	public void init() {
		SymbolicTypeSequence referenceIndexSeq; // Ref x Int
		SymbolicType referenceFunctionType; // Ref x Int -> Ref

		numericFactory.init();
		integerType = objectFactory.canonic(typeFactory.integerType());
		referenceType = objectFactory.canonic(typeFactory.tupleType(
				objectFactory.stringObject("Ref"),
				typeFactory.sequence(new SymbolicType[] { integerType })));
		referenceIndexSeq = typeFactory.sequence(new SymbolicType[] {
				referenceType, integerType });
		referenceFunctionType = objectFactory.canonic(typeFactory.functionType(
				referenceIndexSeq, referenceType));
		arrayElementReferenceFunction = objectFactory.canonic(symbolicConstant(
				objectFactory.stringObject("ArrayElementRef"),
				referenceFunctionType));
		tupleComponentReferenceFunction = objectFactory
				.canonic(symbolicConstant(
						objectFactory.stringObject("TupleComponentRef"),
						referenceFunctionType));
		unionMemberReferenceFunction = objectFactory.canonic(symbolicConstant(
				objectFactory.stringObject("UnionMemberRef"),
				referenceFunctionType));
		offsetReferenceFunction = objectFactory
				.canonic(symbolicConstant(
						objectFactory.stringObject("OffsetRef"),
						referenceFunctionType));
		nullReference = objectFactory.canonic(new CommonNullReference(
				referenceType, zeroSequence));
		identityReference = objectFactory.canonic(new CommonIdentityReference(
				referenceType, oneSequence));
	}

	/**
	 * Getter method that returns the NumericExpressionFactory.
	 * 
	 * @return NumericExpressionFactory
	 */
	@Override
	public NumericExpressionFactory numericFactory() {
		return numericFactory;
	}

	/**
	 * Getter method that returns the ObjectFactory.
	 * 
	 * @return ObjectFactory
	 */
	@Override
	public ObjectFactory objectFactory() {
		return objectFactory;
	}

	/**
	 * Getter Method that returns the Comparator<SymbolicExpression>
	 * 
	 * @return Comparator<SymbolicExpression>
	 */
	@Override
	public Comparator<SymbolicExpression> comparator() {
		return expressionComparator;
	}

	// replace all of these by just one:
	/**
	 * One of several methods that builds a symbolic expression.
	 * 
	 * @param operator
	 * @param type
	 * @param arguments
	 * 	arguments is a SymbolicObject array
	 * 
	 * @return SymbolicExpression
	 */
	@Override
	public SymbolicExpression expression(SymbolicOperator operator,
			SymbolicType type, SymbolicObject[] arguments) {
		if (type != null) {
			if (type.isNumeric())
				return numericFactory.expression(operator, type, arguments);
			if (type.isBoolean())
				return booleanFactory.booleanExpression(operator, arguments);
			if (type.equals(referenceType))
				return referenceExpression(operator, arguments);
		}
		return new CommonSymbolicExpression(operator, type, arguments);
	}

	/**
	 * One of several methods that builds a symbolic expression.
	 * 
	 * @param operator
	 * @param type
	 * @param arg0
	 * 	arg0 is a SymbolicObject
	 * 
	 * @return SymbolicExpression
	 */
	@Override
	public SymbolicExpression expression(SymbolicOperator operator,
			SymbolicType type, SymbolicObject arg0) {
		if (type != null) {
			if (type.isNumeric())
				return numericFactory.expression(operator, type, arg0);
			if (type.isBoolean())
				return booleanFactory.booleanExpression(operator, arg0);
			if (type.equals(referenceType))
				return concreteReferenceExpression(operator, arg0);
		}
		return new CommonSymbolicExpression(operator, type, arg0);
	}

	/**
	 * One of several methods that builds a symbolic expression.
	 * 
	 * @param operator
	 * @param type
	 * @param arg0
	 * 	arg0 is a SymbolicObject
	 * @param arg1
	 * 	arg1 is a SymbolicObject
	 * 
	 * @return SymbolicExpression
	 */
	@Override
	public SymbolicExpression expression(SymbolicOperator operator,
			SymbolicType type, SymbolicObject arg0, SymbolicObject arg1) {
		if (type != null) {
			if (type.isNumeric())
				return numericFactory.expression(operator, type, arg0, arg1);
			if (type.isBoolean())
				return booleanFactory.booleanExpression(operator, arg0, arg1);
			if (type.equals(referenceType))
				return nonTrivialReferenceExpression(operator, arg0, arg1);
		}
		return new CommonSymbolicExpression(operator, type, arg0, arg1);
	}

	/**
	 * One of several methods that builds a symbolic expression.
	 * 
	 * @param operator
	 * @param type
	 * @param arg0
	 * 	arg0 is a SymbolicObject
	 * @param arg1
	 * 	arg1 is a SymbolicObject
	 * @param arg2
	 * 	arg2 is a SymbolicObject
	 * 
	 * @return SymbolicExpression
	 */
	@Override
	public SymbolicExpression expression(SymbolicOperator operator,
			SymbolicType type, SymbolicObject arg0, SymbolicObject arg1,
			SymbolicObject arg2) {
		if (type != null) {
			if (type.isNumeric())
				return numericFactory.expression(operator, type, arg0, arg1,
						arg2);
			if (type.isBoolean())
				return booleanFactory.booleanExpression(operator, arg0, arg1,
						arg2);
		}
		return new CommonSymbolicExpression(operator, type, arg0, arg1, arg2);
	}

	/**
	 * One of several methods that builds a symbolic expression.
	 * 
	 * @param operator
	 * @param type
	 * @param args
	 * 	args is a Collection<SymbolicObject>
	 * 
	 * @return SymbolicExpression
	 */
	@Override
	public SymbolicExpression expression(SymbolicOperator operator,
			SymbolicType type, Collection<SymbolicObject> args) {
		if (type != null) {
			if (type.isNumeric())
				return numericFactory.expression(operator, type, args);
			if (type.isBoolean())
				return booleanFactory.booleanExpression(operator, args);
			if (type.equals(referenceType))
				return referenceExpression(operator,
						args.toArray(new SymbolicObject[args.size()]));
		}
		return new CommonSymbolicExpression(operator, type, args);

	}

	/**
	 * Method that builds a SymbolicConstant.
	 * 
	 * @param name
	 * @param type
	 * 
	 * @return SymbolicConstant
	 */
	@Override
	public SymbolicConstant symbolicConstant(StringObject name,
			SymbolicType type) {
		if (type.isNumeric())
			return numericFactory.symbolicConstant(name, type);
		if (type.isBoolean())
			return booleanFactory.booleanSymbolicConstant(name);
		return new CommonSymbolicConstant(name, type);
	}

	/** 
	 * Getter method that returns the nullExpression.
	 * 
	 * @return SymbolicExpression
	 */
	@Override
	public SymbolicExpression nullExpression() {
		return nullExpression;
	}

	/**
	 * Getter method that returns the booleanFactory.
	 * 
	 * @return BooleanExpressionFactory
	 * 
	 */
	@Override
	public BooleanExpressionFactory booleanFactory() {
		return booleanFactory;
	}
	
	/**
	 * Getter method that returns the typeFactory.
	 * 
	 * @return SymbolicTypeFactory
	 * 
	 */
	@Override
	public SymbolicTypeFactory typeFactory() {
		return typeFactory;
	}

	/**
	 * Getter method that returns the nullReference.
	 * 
	 * @return ReferenceExpression
	 */
	@Override
	public ReferenceExpression nullReference() {
		return nullReference;
	}

	/**
	 * Getter method that returns the identityReference.
	 * 
	 * @return ReferenceExpression
	 */
	@Override
	public ReferenceExpression identityReference() {
		return identityReference;
	}

	/**
	 * One of two private methods that returns SymbolicSequence<SymbolicExpression>.
	 * 
	 * @param parent
	 * @param index
	 * 	index is a NumericExpression
	 * 
	 * @return SymbolicSequence<SymbolicExpression>
	 * 
	 */
	private SymbolicSequence<SymbolicExpression> parentIndexSequence(
			ReferenceExpression parent, NumericExpression index) {
		return collectionFactory.sequence(new SymbolicExpression[] { parent,
				index });
	}

	/**
	 * One of two private methods that returns SymbolicSequence<SymbolicExpression>.
	 * 
	 * @param parent
	 * @param index
	 * 	index is an IntObject
	 * 
	 * @return SymbolicSequence<SymbolicExpression>
	 */
	private SymbolicSequence<SymbolicExpression> parentIndexSequence(
			ReferenceExpression parent, IntObject index) {
		return collectionFactory.sequence(new SymbolicExpression[] {
				parent,
				numericFactory.number(objectFactory.numberObject(numberFactory
						.integer(index.getInt()))) });
	}

	/**
	 * method that builds an ArrayElementReference.
	 * 
	 * @param arrayReference
	 * @param index
	 * 
	 * @return ArrayElementReference
	 */
	@Override
	public ArrayElementReference arrayElementReference(
			ReferenceExpression arrayReference, NumericExpression index) {
		return new CommonArrayElementReference(referenceType,
				arrayElementReferenceFunction, parentIndexSequence(
						arrayReference, index));
	}

	/**
	 * Method that builds a TupleComponentReference.
	 * 
	 * @param tupleReference
	 * @param fieldIndex
	 * 
	 * @return TupleComponentReference
	 */
	@Override
	public TupleComponentReference tupleComponentReference(
			ReferenceExpression tupleReference, IntObject fieldIndex) {

		return new CommonTupleComponentReference(referenceType,
				tupleComponentReferenceFunction, parentIndexSequence(
						tupleReference, fieldIndex), fieldIndex);
	}

	/**
	 * Method that builds a UnionMemberReference.
	 * 
	 * @param unionReference
	 * @param memberIndex
	 * 
	 * @return UnionMemberReference
	 */
	@Override
	public UnionMemberReference unionMemberReference(
			ReferenceExpression unionReference, IntObject memberIndex) {
		return new CommonUnionMemberReference(referenceType,
				unionMemberReferenceFunction, parentIndexSequence(
						unionReference, memberIndex), memberIndex);
	}

	/**
	 * Method that builds an OffsetReference.
	 * 
	 * @param reference
	 * @param offset
	 * 
	 * @return OffsetReference
	 */
	@Override
	public OffsetReference offsetReference(ReferenceExpression reference,
			NumericExpression offset) {
		return new CommonOffsetReference(referenceType,
				offsetReferenceFunction, parentIndexSequence(reference, offset));

	}

	/**
	 * Getter method that returns referenceType.
	 * 
	 * @return SymbolicType
	 */
	@Override
	public SymbolicType referenceType() {
		return referenceType;
	}

}
