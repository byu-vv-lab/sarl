package edu.udel.cis.vsl.sarl.IF;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject.SymbolicObjectKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.universe.Universes;

/**
 * This is a Demo class to demonstrate Array Operations in SARL:
 * - creating arrays
 * - creating empty arrays
 * - appending elements to an array
 * - removing an element fron an array
 * - getting and using the length of an array
 * - reading elements of an array
 * - writing elements to an array
 * - writing a list of elements to an array
 * 
 * @author mohammedalali
 *
 */
public class ArrayOperationsDemo {
	
	/**
	 * to be used to do everything
	 */
	SymbolicUniverse myUniverse;
	
	/**
	 * different types to be used with SymbolicExpressions
	 */
	SymbolicType integerType;
	
	/**
	 * concrete integers constants
	 */
	NumericExpression zero, one, two, three, four,
		five, ten, twenty, thirty, fourty, fifty, hundred, thousand;
	
	/**
	 * List of integers
	 */
	List<NumericExpression> numericList, numericList2;
	
	/**
	 * array used for testing many operations
	 */
	SymbolicExpression array1, emptyArray;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		myUniverse = Universes.newIdealUniverse();
		integerType = myUniverse.integerType();
		zero = myUniverse.integer(0);
		one = myUniverse.integer(1);
		two = myUniverse.integer(2);
		three = myUniverse.integer(3);
		four = myUniverse.integer(4);
		five = myUniverse.integer(5);
		ten = myUniverse.integer(10);
		twenty = myUniverse.integer(20);
		thirty = myUniverse.integer(30);
		fourty = myUniverse.integer(40);
		fifty = myUniverse.integer(50);
		hundred = myUniverse.integer(100);
		thousand = myUniverse.integer(1000);
		numericList = Arrays.asList(new NumericExpression[] {ten, twenty, thirty, fourty});
		numericList2 = Arrays.asList(new NumericExpression[] {fifty, hundred, thousand});
		array1 = myUniverse.array(integerType, numericList);
		array1.commit();
		emptyArray = myUniverse.emptyArray(integerType);
		emptyArray.commit();

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		SymbolicExpression  array2, array3, array4, array5, array6;

		array2 = myUniverse.append(array1, one);
		array2.commit();
		array3 = myUniverse.removeElementAt(array2, 4);
		array3.commit();
		array4 = myUniverse.arrayWrite(array2, one, fifty);
		array4.commit();
		array5 = myUniverse.denseArrayWrite(emptyArray, numericList2);
		array5.commit();
		array6 = myUniverse.arrayWrite(array4, one, hundred);
		array6.commit();
		
		System.out.println("Array Operations");
		System.out.println("** Creating and Mainpulating Arrays **");
		System.out.println("created a new array");
		System.out.println("(array1): " + array1);
		//System.out.println(array1.arguments());
		
		assertEquals(array1.symbolicObjectKind(), SymbolicObjectKind.EXPRESSION);
		
		System.out.println("creating an empty array");
		System.out.println("(emptyArray): " + emptyArray);
		
		System.out.println("appending a new element '1' to (array1) ");
		System.out.println("(array2): " + array2);
		
		System.out.println("removing the new element from (array2)");
		System.out.println("(array3): " + array3);
		assertEquals(array1, array3);
		
		System.out.println("** Checking Lengths of Arrays **");
		System.out.println("(array1) is of length: " + myUniverse.length(array1));
		assertEquals(myUniverse.length(array1), four);
		System.out.println("(emptyArray) is of length: " + myUniverse.length(emptyArray));
		assertEquals(myUniverse.length(emptyArray), zero);
		System.out.println("(array2) is of length: " + myUniverse.length(array2));
		assertEquals(myUniverse.length(array2), five);
		assertEquals(myUniverse.length(array1), myUniverse.length(array3));
		
		System.out.println("** Reading elements of Arrays **");
		System.out.println("(array1)");
		for(int i = 0; i < Integer.parseInt(myUniverse.length(array1).toString()); i++){
			System.out.println(myUniverse.arrayRead(array1, myUniverse.integer(i)));
		}
		System.out.println("(array2)");
		for(int i = 0; i < Integer.parseInt(myUniverse.length(array2).toString()); i++){
			System.out.println(myUniverse.arrayRead(array2, myUniverse.integer(i)));
		}
		System.out.println("(array3)");
		for(int i = 0; i < Integer.parseInt(myUniverse.length(array3).toString()); i++){
			System.out.println(myUniverse.arrayRead(array3, myUniverse.integer(i)));
		}
		System.out.println("** Writing (changing values) of the Arrays **");
		System.out.println("changing the first element of (array2) to get:");
		System.out.println("(array4): " + array4);
		
		System.out.println("writing elements in the emptyArray");
		System.out.println("(array5): " + array5);
		System.out.println("length of (array5): " + myUniverse.length(array5));
		System.out.println("writing an element to array4");
		System.out.println("(array6): " + array6);
		assertEquals(myUniverse.length(array6), myUniverse.length(array4));
		
		
		
	}

}
