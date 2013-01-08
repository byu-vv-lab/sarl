package edu.udel.cis.vsl.sarl.prove.cvc;

import java.io.PrintWriter;

import edu.udel.cis.vsl.sarl.prove.IF.CVC3TheoremProverIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.SymbolicUniverseIF;

public class CVC3TheoremProverFactory {
  public static CVC3TheoremProverIF newCVC3TheoremProver(
      SymbolicUniverseIF universe, PrintWriter out, boolean showProverQueries) {
    return new CVC3TheoremProver(universe, out, showProverQueries);
  }
}
