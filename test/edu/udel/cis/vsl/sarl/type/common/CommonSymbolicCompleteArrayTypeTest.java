package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.ideal.common.NTConstant;
import edu.udel.cis.vsl.sarl.ideal.common.NumericPrimitive;
import edu.udel.cis.vsl.sarl.object.common.CommonIntObject;

public class CommonSymbolicCompleteArrayTypeTest {
	
	CommonSymbolicCompleteArrayType completeArray;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		//NumericExpression number = new NumericPrimitive(SymbolicOperator.CONCRETE, SymbolicType.SymbolicTypeKind.INTEGER, new CommonIntObject(2));
		//completeArray = new CommonSymbolicCompleteArrayType(SymbolicType.SymbolicTypeKind.ARRAY,)
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testComputeHashCode() {
		//fail("Not yet implemented");
	}
/*
	@Test
	public void testCanonizeChildren() {
		fail("Not yet implemented");
	}

	@Test
	public void testExtentString() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsComplete() {
		fail("Not yet implemented");
	}

	@Test
	public void testCommonSymbolicCompleteArrayType() {
		fail("Not yet implemented");
	}

	@Test
	public void testExtent() {
		fail("Not yet implemented");
	}
*/
}
