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
import edu.udel.cis.vsl.sarl.IF.config.ProverInfo;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;
import edu.udel.cis.vsl.sarl.util.FastList;

/**
 * An implementation of TheoremProver using the automated theorem prover CVC3,
 * and in which CVC3 is run in a separate process for each query. Transforms a
 * theorem proving query into the language of CVC3, invokes CVC3 through its
 * command line interface, and interprets the output.
 * 
 * @author siegel
 */
public class RobustCVC3TheoremProver implements TheoremProver {

	// ************************** Static Fields *************************** //

	// cvc3 nicer in this regard: by default, it doesn't print anything
	// except the final result:

	// public final static String prompt = "CVC> ";

	// public final static char[] promptChars = prompt.toCharArray();

	public final static PrintStream err = System.err;

	// ****************************** Fields ****************************** //

	private ProverInfo info;

	private ProcessBuilder processBuilder;

	/**
	 * The symbolic universe used for managing symbolic expressions. Initialized
	 * by constructor and never changes.
	 */
	private PreUniverse universe;

	/**
	 * The translation of the given context to a CVC3 expression. Created once
	 * during instantiation and never modified.
	 */
	private CVC3Translator assumptionTranslator;

	// *************************** Constructors *************************** //

	/**
	 * Constructs new CVC3 theorem prover with given symbolic universe.
	 * 
	 * @param universe
	 *            the controlling symbolic universe
	 * @param context
	 *            the assumption(s) the prover will use for queries
	 */
	RobustCVC3TheoremProver(PreUniverse universe, BooleanExpression context,
			ProverInfo info) {
		assert universe != null;
		assert context != null;
		assert info != null;
		this.universe = universe;
		this.info = info;
		// The following is apparently necessary since the same bound symbolic
		// constant can be used in different scopes in the context; CVC3
		// requires that these map to distinct variables.
		context = (BooleanExpression) universe.cleanBoundVariables(context);
		this.assumptionTranslator = new CVC3Translator(universe, context);
		// set up process builder with command
		// also try "--use-theory=idl"
		// make these options part of the config?
		this.processBuilder = new ProcessBuilder(info.getPath()
				.getAbsolutePath(), "-lang", "presentation");
	}

	@Override
	public PreUniverse universe() {
		return universe;
	}

	private ValidityResult readCVCOutput(BufferedReader cvcOut,
			BufferedReader cvcErr) {
		try {
			String line = cvcOut.readLine();

			if (line == null) {
				if (info.getShowErrors() || info.getShowInconclusives()) {
					for (String errline = cvcErr.readLine(); errline != null; errline = cvcErr
							.readLine()) {
						err.println(errline);
					}
					err.flush();
				}
				return Prove.RESULT_MAYBE;
			}
			line = line.trim();
			if ("Valid.".equals(line))
				return Prove.RESULT_YES;
			if ("Invalid.".equals(line))
				return Prove.RESULT_NO;
			if (info.getShowInconclusives()) {
				err.println("CVC3 inconclusive with message: " + line);
				for (line = cvcOut.readLine(); line != null; line = cvcOut
						.readLine()) {
					err.println(line);
				}
			}
			return Prove.RESULT_MAYBE;
		} catch (IOException e) {
			if (info.getShowErrors())
				err.println("I/O error reading CVC3 output: " + e.getMessage());
			return Prove.RESULT_MAYBE;
		}
	}

	@Override
	public ValidityResult valid(BooleanExpression predicate) {
		// TODO: clean bound vars on predicate?
		PrintStream out = universe.getOutputStream();
		int id = universe.numProverValidCalls();
		FastList<String> assumptionDecls = assumptionTranslator
				.getDeclarations();
		FastList<String> assumptionText = assumptionTranslator.getTranslation();
		Process process = null;
		ValidityResult result;
		boolean show = universe.getShowProverQueries() || info.getShowQueries();

		universe.incrementProverValidCount();
		if (show) {
			out.println();
			out.print("CVC3 assumptions " + id + ":\n");
			assumptionDecls.print(out);
			assumptionText.print(out);
			out.println();
			out.flush();
		}
		try {
			process = processBuilder.start();
		} catch (IOException e) {
			if (info.getShowErrors())
				err.println("I/O exception reading CVC3 output: "
						+ e.getMessage());
			result = Prove.RESULT_MAYBE;
		}

		PrintStream stdin = new PrintStream(process.getOutputStream());
		BufferedReader stdout = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		BufferedReader stderr = new BufferedReader(new InputStreamReader(
				process.getErrorStream()));

		assumptionDecls.print(stdin);
		stdin.print("ASSERT ");
		assumptionText.print(stdin);
		stdin.println(";");

		CVC3Translator translator = new CVC3Translator(assumptionTranslator,
				predicate);
		FastList<String> predicateDecls = translator.getDeclarations();
		FastList<String> predicateText = translator.getTranslation();

		predicateDecls.print(stdin);
		stdin.print("QUERY ");
		predicateText.print(stdin);
		stdin.println(";\n");

		stdin.flush();
		stdin.close();
		if (show) {
			out.print("\nCVC3 predicate   " + id + ":\n");
			predicateDecls.print(out);
			predicateText.print(out);
			out.println();
			out.flush();
		}
		result = readCVCOutput(stdout, stderr);
		if (show) {
			out.println("CVC3 result      " + id + ": " + result);
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
		return "RobustCVC3TheoremProver";
	}
}
