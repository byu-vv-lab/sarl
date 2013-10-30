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
package edu.udel.cis.vsl.sarl.ideal; 

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.common.CommonIdealFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class IdealModulusTest {

	private static PrintStream out = System.out;
	private NumberFactory numberFactory;
	private ObjectFactory objectFactory;
	private SymbolicTypeFactory typeFactory;
	private CollectionFactory collectionFactory;
	private IdealFactory idealFactory;
	private BooleanExpressionFactory booleanFactory;
	private CommonIdealFactory commonIdealFactory;

	private Constant constOnePointFive; // real constant 3/2
	private Constant constOne;
	private Constant intZero; // int constant 0
	private Constant intOne; // int constant 1
	private RationalNumber ratOnePointFive; // 3/2
	private RationalNumber ratOne; // 1
	StringObject Xobj; // "X"
	NumericSymbolicConstant x; // int symbolic constant "X"
	NumericSymbolicConstant y; // int symbolic constant "Y"

	@Before
	public void setUp() throws Exception {
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		numberFactory = system.numberFactory();
		objectFactory = system.objectFactory();
		typeFactory = system.typeFactory();
		collectionFactory = system.collectionFactory();
		idealFactory = (IdealFactory) system.numericFactory();
		booleanFactory = system.booleanFactory();
		commonIdealFactory = new CommonIdealFactory(numberFactory,
				objectFactory, typeFactory, collectionFactory, booleanFactory);
		ratOnePointFive = numberFactory.rational("1.25");
		constOnePointFive = idealFactory.constant(ratOnePointFive);
		ratOne = numberFactory.rational("1");
		constOne = idealFactory.constant(ratOne);
		intZero = idealFactory.intConstant(0);
		intOne = idealFactory.intConstant(1);
		Xobj = objectFactory.stringObject("X");
		x = objectFactory.canonic(idealFactory.symbolicConstant(Xobj,
				typeFactory.integerType()));
	}

	@After
	public void tearDown() throws Exception {
		
	}
	/**
	 * Integer modulus. Assume numerator is nonnegative and denominator is
	 * positive.
	 * 
	 * (ad)%(bd) = (a%b)d
	 * 
	 * Ex: (2u)%2 = (u%1)2 = 0
	 * 
	 * @param numerator
	 *            a nonnegative integer polynomial
	 *            
	 * @param denominator
	 *            a positive integer polynomial
	 *            
	 * @return the polynomial of the form numerator%denominator
	 */
	@Test
	public void intModulusPoly() {
		NumericExpression n1 = idealFactory.subtract(idealFactory.multiply(x, x),
				intOne);
		NumericExpression n2 = idealFactory.add(x, intOne);
		NumericExpression ne1 = idealFactory.modulo(n1, n2);		
		NumericExpression n = commonIdealFactory.modulo(n1, n2);
		NumericExpression m = commonIdealFactory.modulo(intZero, n2);
		NumericExpression p = commonIdealFactory.modulo(n1, intOne);
		NumericExpression q = commonIdealFactory.modulo(constOnePointFive, constOne);
		
		out.println("modulo=" + q);
		
		assertEquals(ne1, n);
		assertEquals(intZero, m);
		assertEquals(intZero, p);
	}
}