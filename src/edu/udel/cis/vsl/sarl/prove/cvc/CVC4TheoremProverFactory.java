package edu.udel.cis.vsl.sarl.prove.cvc;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC4TheoremProverFactory implements TheoremProverFactory {
	
	
	static { System.loadLibrary("cvc4jni"); }
	
	/**
	 * The symbolic universe used for managing symbolic expressions. Initialized
	 * by constructor and never changes.
	 */
	private PreUniverse universe;
	
	/**
	 * cvc4jni library should be loaded explicitly.
	 * use LoadCVC4JNI as a tag to make sure that cvc4jni has already been loaded
	 * */
	//static private boolean LoadCVC4JNI = false;

	/**
	 * Constructs a CVC4 theorem prover factory with the given symbolic universe.
	 * @param universe
	 */
	public CVC4TheoremProverFactory(PreUniverse universe) {
		this.universe = universe;
		
//		/**
//		 * Only need to load cvc4jni once.
//		 */
//		if(! LoadCVC4JNI){
//			System.loadLibrary("cvc4jni");
//			LoadCVC4JNI = true;
//		}
	}

	/**
	 * This is where the factory produces instances with a given context
	 * @param context
	 * @return a new CVC4 theorem prover
	 */
	@Override
	public TheoremProver newProver(BooleanExpression context) {
		return new CVC4TheoremProver(universe, context);
	}
}
