package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Comparator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType.IntegerKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType.RealKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

/**
 * a class to test TypeSequenceComparator
 * 
 * @author mohammedalali
 *
 */
public class TypeSequenceComparatorTest {
	
	/**
	 * an object to be used for comparing two TypeSequences.
	 */
	TypeSequenceComparator typeSequenceComparator;
	
	/**
	 * Declaring two TypeSequences to be used for testing
	 */
	CommonSymbolicTypeSequence typeSequence, typeSequence2;
	
	/**
	 * Declaring two IntegerTypes to be added in the typeSequence
	 */
	CommonSymbolicIntegerType idealIntKind, boundedIntKind;
	
	/**
	 * Declaring two realTypes to be added in the typeSequence
	 */
	CommonSymbolicRealType idealRealKind, floatRealKind;
	
	/**
	 * lists to be filled with different SymbolicTypes 
	 * to create SymbolicTypeSequences.
	 */
	ArrayList<CommonSymbolicType> typesList, typesList2;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		typeSequenceComparator = new TypeSequenceComparator();
		typesList = new ArrayList<CommonSymbolicType>();
		typesList2 = new ArrayList<CommonSymbolicType>();
		idealIntKind = new CommonSymbolicIntegerType(IntegerKind.IDEAL);
		boundedIntKind = new CommonSymbolicIntegerType(IntegerKind.BOUNDED);
		idealRealKind = new CommonSymbolicRealType(RealKind.IDEAL);
		floatRealKind = new CommonSymbolicRealType(RealKind.FLOAT);
		
		typesList.add(idealIntKind);
		typesList.add(boundedIntKind);
		typesList.add(idealRealKind);
		typesList.add(floatRealKind);
		
		typesList2.add(idealRealKind);
		typesList2.add(floatRealKind);
		typesList2.add(idealIntKind);
		typesList2.add(boundedIntKind);
		
	/*
		I have to put those instantiations at the end of the method
	 	after adding element to the list. Otherwise the list will be empty
	 	when creating the TypeSequences.
	 * 
	 */
	typeSequence = new CommonSymbolicTypeSequence(typesList);
	typeSequence2 = new CommonSymbolicTypeSequence(typesList2);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	

	@Test
	public void testCompare() {
		Comparator<SymbolicType> comparator = new Comparator<SymbolicType>() {
			
			@Override
			public int compare(SymbolicType o1, SymbolicType o2) {
				if(o1.equals(o2))
					return 0;
				
				return 1;
			}
		};
		typeSequenceComparator.setTypeComparator(comparator);
		assertEquals(typeSequenceComparator.compare(typeSequence, typeSequence2), 1);
	}
}
