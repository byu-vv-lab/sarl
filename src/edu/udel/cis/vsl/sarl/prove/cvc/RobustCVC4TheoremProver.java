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
package edu.udel.cis.vsl.sarl.prove.cvc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.config.Prover;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;
import edu.udel.cis.vsl.sarl.util.FastList;

/**
 * An implementation of TheoremProver using the automated theorem prover CVC4,
 * and in which CVC4 is run in a separate process for each query. Transforms a
 * theorem proving query into the language of CVC4, invokes CVC4 through its
 * Java interface, and interprets the output.
 * 
 * @author siegel
 */
public class RobustCVC4TheoremProver implements TheoremProver {

	// ************************** Static Fields *************************** //

	public final static String prompt = "CVC4> ";

	public final static char[] promptChars = prompt.toCharArray();

	// TODO: make these part of the configuration.
	// something that can be set in the universe?

	public final static boolean showInconclusiveResult = true;

	public final static boolean showProverErrors = true;

	public final static PrintStream err = System.err;

	// ****************************** Fields ****************************** //

	// private Prover prover;

	private ProcessBuilder processBuilder;

	/**
	 * The symbolic universe used for managing symbolic expressions. Initialized
	 * by constructor and never changes.
	 */
	private PreUniverse universe;

	/**
	 * The translation of the given context to a CVC4 expression. Created once
	 * during instantiation and never modified.
	 */
	private CVC4Translator assumptionTranslator;

	// *************************** Constructors *************************** //

	/**
	 * Constructs new CVC4 theorem prover with given symbolic universe.
	 * 
	 * @param universe
	 *            the controlling symbolic universe
	 * @param context
	 *            the assumption(s) the prover will use for queries
	 */
	RobustCVC4TheoremProver(PreUniverse universe, BooleanExpression context,
			Prover prover) {
		assert universe != null;
		assert context != null;
		this.universe = universe;
		// this.prover = prover;
		// The following is apparently necessary since the same bound symbolic
		// constant can be used in different scopes in the context; CVC4
		// requires that these map to distinct variables.
		context = (BooleanExpression) universe.cleanBoundVariables(context);
		this.assumptionTranslator = new CVC4Translator(universe, context);
		// set up process builder with command
		// also try "--use-theory=idl"
		// make these options part of the config?
		this.processBuilder = new ProcessBuilder(prover.getPath()
				.getAbsolutePath(), "--rewrite-divk", "--quiet",
				"--interactive", "--lang=cvc4");
	}

	@Override
	public PreUniverse universe() {
		return universe;
	}

	private ValidityResult readCVCOutput(BufferedReader in) {
		try {
			String line = in.readLine();

			// debug:
			// System.out.println(line);
			// System.out.flush();
			line = line.replace(prompt, "");
			line = line.trim();
			if ("valid".equals(line))
				return Prove.RESULT_YES;
			if ("invalid".equals(line))
				return Prove.RESULT_NO;
			if (showInconclusiveResult) {
				StringBuffer sb = new StringBuffer(line);
				int promptpos = 0;
				int read;

				sb.append('\n');
				while (true) {
					read = in.read();
					if (read == -1)
						break;
					if (read == promptChars[promptpos]) {
						promptpos++;
						if (promptpos >= promptChars.length)
							break;
					} else {
						for (int j = 0; j < promptpos; j++)
							sb.append(promptChars[j]);
						promptpos = 0;
						sb.append((char) read);
					}
				}
				err.println("CVC4 inconclusive with message: " + sb);
			}
			return Prove.RESULT_MAYBE;
		} catch (IOException e) {
			if (showProverErrors)
				err.println("I/O error reading CVC4 output: " + e.getMessage());
			return Prove.RESULT_MAYBE;
		}
	}

	@Override
	public ValidityResult valid(BooleanExpression predicate) {
		// TODO: clean bound vars on predicate?

		FastList<String> assumptionDecls = assumptionTranslator
				.getDeclarations();
		FastList<String> assumptionText = assumptionTranslator.getTranslation();
		CVC4Translator translator = new CVC4Translator(assumptionTranslator,
				predicate);
		FastList<String> predicateDecls = translator.getDeclarations();
		FastList<String> predicateText = translator.getTranslation();
		Process process = null;
		ValidityResult result;
		boolean show = universe.getShowProverQueries();

		universe.incrementProverValidCount();
		if (show) {
			PrintStream out = universe.getOutputStream();
			int id = universe.numProverValidCalls() - 1;

			out.println();
			out.print("CVC4 assumptions " + id + ":\n");
			assumptionDecls.print(out);
			assumptionText.print(out);
			out.print("\nCVC4 predicate   " + id + ":\n");
			predicateDecls.print(out);
			predicateText.print(out);
			out.println();
			out.flush();
		}

		try {
			process = processBuilder.start();

			PrintStream stdin = new PrintStream(process.getOutputStream());
			BufferedReader stdout = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			// BufferedReader stderr = new BufferedReader(new InputStreamReader(
			// process.getErrorStream()));

			assumptionDecls.print(stdin);
			stdin.print("ASSERT ");
			assumptionText.print(stdin);
			stdin.println(";");
			predicateDecls.print(stdin);
			stdin.print("QUERY ");
			predicateText.print(stdin);
			stdin.println(";\n");
			stdin.flush();
			result = readCVCOutput(stdout);
		} catch (IOException e) {
			if (showProverErrors)
				err.println("I/O exception reading CVC4 output: "
						+ e.getMessage());
			result = Prove.RESULT_MAYBE;
		}
		if (show) {
			PrintStream out = universe.getOutputStream();
			int id = universe.numProverValidCalls() - 1;

			out.println("CVC4 result      " + id + ": " + result);
			out.flush();
		}
		if (process != null)
			process.destroy();
		return result;
	}

	@Override
	public ValidityResult validOrModel(BooleanExpression predicate) {
		return Prove.RESULT_MAYBE;
	}

	@Override
	public String toString() {
		return "RobustCVC4TheoremProver";
	}
}
