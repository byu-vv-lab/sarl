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
package edu.udel.cis.vsl.sarl.prove.z3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.LinkedList;

import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.config.ProverInfo;
import edu.udel.cis.vsl.sarl.IF.config.ProverInfo.ProverKind;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;
import edu.udel.cis.vsl.sarl.util.FastList;
import edu.udel.cis.vsl.sarl.util.ProcessControl;

/**
 * An implementation of {@link TheoremProver} using the automated theorem
 * provers Z3. Transforms a theorem proving query into the language of Z3,
 * invokes Z3 through its command line interface in a new process, and
 * interprets the output.
 * 
 * <p>
 * Invocation:
 * 
 * <pre>
 * z3 -smt2 -in
 * </pre>
 * 
 * Commands:
 * 
 * <pre>
 * (assert expr)
 * (check-sat)
 * </pre>
 * 
 * To check if predicate is valid under context:
 * 
 * <pre>
 * (assert context)
 * (assert (not predicate))
 * (check-sat)
 * </pre>
 * 
 * If result is "sat": answer is NO (not valid). If result is "unsat": answer is
 * YES (valid). Otherwise, MAYBE.
 * </p>
 * 
 * @author siegel
 */
public class RobustZ3TheoremProver implements TheoremProver {

	// ************************** Static Fields *************************** //

	/**
	 * Nick-name for <code>stderr</code>, where warnings and error messages will
	 * be sent.
	 */
	public final static PrintStream err = System.err;

	// ****************************** Fields ****************************** //

	/**
	 * Info object on underlying theorem prover, which will have
	 * {@link ProverKind} {@link ProverKind#Z3}.
	 */
	private ProverInfo info;

	/**
	 * Java object for producing new OS-level processes executing a specified
	 * command.
	 */
	private ProcessBuilder processBuilder;

	/**
	 * The symbolic universe used for managing symbolic expressions. Initialized
	 * by constructor and never changes.
	 */
	private PreUniverse universe;

	/**
	 * The translation of the given context to a Z3 expression. Created once
	 * during instantiation and never modified.
	 */
	private Z3Translator assumptionTranslator;

	// *************************** Constructors *************************** //

	/**
	 * Constructs new Z3 theorem prover for the given context.
	 * 
	 * @param universe
	 *            the controlling symbolic universe
	 * @param context
	 *            the assumption(s) the prover will use for queries
	 * @param ProverInfo
	 *            information object on the underlying theorem prover, which
	 *            must have {@link ProverKind} {@link ProverKind#Z3}
	 */
	RobustZ3TheoremProver(PreUniverse universe, BooleanExpression context,
			ProverInfo info) {
		LinkedList<String> command = new LinkedList<>();

		assert universe != null;
		assert context != null;
		assert info != null;
		this.universe = universe;
		this.info = info;
		context = (BooleanExpression) universe.cleanBoundVariables(context);
		this.assumptionTranslator = new Z3Translator(universe, context, true);
		command.add(info.getPath().getAbsolutePath());
		command.addAll(info.getOptions());
		command.add("-smt2");
		command.add("-in");
		this.processBuilder = new ProcessBuilder(command);
	}

	@Override
	public PreUniverse universe() {
		return universe;
	}

	// a little different: checking sat or unsat
	private ValidityResult readZ3Output(BufferedReader z3Out,
			BufferedReader z3Err) {
		try {
			String line = z3Out.readLine();

			if (line == null) {
				if (info.getShowErrors() || info.getShowInconclusives()) {
					for (String errline = z3Err.readLine(); errline != null; errline = z3Err
							.readLine()) {
						err.println(errline);
					}
					err.flush();
				}
				return Prove.RESULT_MAYBE;
			}
			line = line.trim();
			if ("unsat".equals(line))
				return Prove.RESULT_YES;
			if ("sat".equals(line))
				return Prove.RESULT_NO;
			if (info.getShowInconclusives()) {
				err.println(info.getFirstAlias()
						+ " inconclusive with message: " + line);
				for (line = z3Out.readLine(); line != null; line = z3Out
						.readLine()) {
					err.println(line);
				}
			}
			return Prove.RESULT_MAYBE;
		} catch (IOException e) {
			if (info.getShowErrors())
				err.println("I/O error reading " + info.getFirstAlias()
						+ " output: " + e.getMessage());
			return Prove.RESULT_MAYBE;
		}
	}

	@Override
	public ValidityResult valid(BooleanExpression predicate) {
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
			out.print(info.getFirstAlias() + " assumptions " + id + ":\n");
			assumptionDecls.print(out);
			assumptionText.print(out);
			out.println();
			out.flush();
		}
		try {
			process = processBuilder.start();
		} catch (Throwable e) {
			if (info.getShowErrors())
				err.println("I/O exception reading " + info.getFirstAlias()
						+ " output: " + e.getMessage());
			result = Prove.RESULT_MAYBE;
		}

		PrintStream stdin = new PrintStream(process.getOutputStream());
		BufferedReader stdout = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		BufferedReader stderr = new BufferedReader(new InputStreamReader(
				process.getErrorStream()));

		assumptionDecls.print(stdin);
		stdin.print("(assert ");
		assumptionText.print(stdin);
		stdin.println(")");
		predicate = (BooleanExpression) universe.cleanBoundVariables(predicate);

		Z3Translator translator = new Z3Translator(assumptionTranslator,
				predicate);
		FastList<String> predicateDecls = translator.getDeclarations();
		FastList<String> predicateText = translator.getTranslation();

		predicateDecls.print(stdin);
		stdin.print("(assert (not ");
		predicateText.print(stdin);
		stdin.println("))");
		stdin.println("(check-sat)");
		stdin.flush();
		stdin.close();
		if (show) {
			out.print("\n" + info.getFirstAlias() + " predicate   " + id
					+ ":\n");
			predicateDecls.print(out);
			predicateText.print(out);
			out.println();
			out.println();
			out.flush();
		}
		if (info.getTimeout() > 0
				&& !ProcessControl.waitForProcess(process, info.getTimeout())) {
			if (info.getShowErrors() || info.getShowInconclusives())
				err.println(info.getFirstAlias() + " query       " + id
						+ ": time out");
			result = Prove.RESULT_MAYBE;
		} else {
			result = readZ3Output(stdout, stderr);
		}
		if (show) {
			out.println(info.getFirstAlias() + " result      " + id + ": "
					+ result);
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
		return "RobustZ3TheoremProver[" + info.getFirstAlias() + "]";
	}
}
