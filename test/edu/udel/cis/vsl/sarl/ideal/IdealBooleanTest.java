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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
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

public class IdealBooleanTest {

	private NumberFactory numberFactory;
	private ObjectFactory objectFactory;
	private SymbolicTypeFactory typeFactory;
	private CollectionFactory collectionFactory;
	private IdealFactory idealFactory;
	private BooleanExpressionFactory booleanFactory;
	private CommonIdealFactory commonIdealFactory;

	private Constant intOne; // int constant 1
	StringObject Xobj; // "X"
	NumericSymbolicConstant x; // int symbolic constant "X"

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
		intOne = idealFactory.intConstant(1);
		Xobj = objectFactory.stringObject("X");
		x = objectFactory.canonic(idealFactory.symbolicConstant(Xobj,
				typeFactory.integerType()));
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	/**
	 * Returns true if the first argument is 'not less than' the second argument and 
	 * vice-versa.
	 * 
	 * @param type
	 * 				NumericExpression
	 * 
	 * @return type
	 * 				BooleanExpression
	 */
	@Test
	public void notLessThan() {
		NumericExpression n1 = idealFactory.subtract(x,intOne);
		NumericExpression n2 = idealFactory.add(x, intOne);		
		BooleanExpression n = commonIdealFactory.notLessThan(n2, n1);
		BooleanExpression nn = commonIdealFactory.notLessThan(n1, n2);
		BooleanExpression m1 = booleanFactory.symbolic(false);
		BooleanExpression m2 = booleanFactory.symbolic(true);
		
		assertEquals(m2, n);
		assertEquals(m1, nn);
	}

	/**
	 * Returns true if the first argument is 'not less than or equal' to the second argument 
	 * and vice-versa.
	 * 
	 * @param type
	 * 				NumericExpression
	 * 
	 * @return type
	 * 				BooleanExpression
	 */
	@Test
	public void notLessThanEquals() {		
		NumericExpression n1 = idealFactory.subtract(x, intOne);
		NumericExpression n2 = idealFactory.add(x, intOne);
		BooleanExpression m = booleanFactory.symbolic(true);
		BooleanExpression n = booleanFactory.symbolic(false);				
		BooleanExpression n01 = commonIdealFactory.notLessThanEquals(n1, n2);
		BooleanExpression n02 = commonIdealFactory.notLessThanEquals(n2, n1);
		
		assertEquals(n, n01);
		assertEquals(m, n02);
	}
}