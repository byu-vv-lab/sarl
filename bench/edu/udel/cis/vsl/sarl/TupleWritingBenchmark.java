package edu.udel.cis.vsl.sarl;

import java.util.Arrays;

import javax.swing.JSpinner.NumberEditor;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;

public class TupleWritingBenchmark {
	
	public final static SymbolicUniverse universe = SARL.newIdealUniverse();
	public final static FactorySystem system = PreUniverses.newIdealFactorySystem();

	public final static SymbolicType integerType = universe.integerType();
	
	public final static ExpressionFactory expressionFactory = system.expressionFactory();
	
	public static SymbolicTupleType tupleType;
	
	public static SymbolicExpression tuple;


	public static void main(String[] args) {

		int maxSize =(int) Math.pow(2,20);
		SymbolicType typeArray[];
		NumericExpression tupleArray[];
		long startingTime, stoppingTime;
		double totalTime;
		IntObject index;

		System.out.println("Testing of tuple creation" );
		startingTime = System.nanoTime();
		typeArray = new SymbolicType[maxSize];
		tupleArray = new NumericExpression[maxSize];
		for(int i = 0 ; i< maxSize; i++){
			typeArray[i] = universe.integerType();
			tupleArray[i] = universe.integer(i);
		}
		
		tupleType = universe.tupleType(universe.stringObject("type1"), Arrays.asList(typeArray));
		tuple = universe.tuple(tupleType, Arrays.asList(tupleArray));
		stoppingTime = System.nanoTime();
		
		totalTime =((double) (stoppingTime - startingTime))/1000000000.0;
		
		System.out.println("Total Time in seconds: " + totalTime + " for size: "+maxSize);

		System.out.println("Testing of tuple write ");

		startingTime = System.nanoTime();
		for(int i = 0;i<100; i++){
			index = universe.intObject(i);
			tuple = universe.tupleWrite(tuple, index, universe.integer(i*2));
		}
		stoppingTime = System.nanoTime();
		totalTime =((double) (stoppingTime - startingTime))/1000000000.0;
		
		System.out.println("Total Time in seconds: " + totalTime + " for size: "+maxSize);


		
		
		
		
	}

}
