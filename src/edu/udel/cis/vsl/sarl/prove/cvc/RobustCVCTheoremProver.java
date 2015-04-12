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

import static edu.udel.cis.vsl.sarl.IF.config.ProverInfo.ProverKind.CVC3;
import static edu.udel.cis.vsl.sarl.IF.config.ProverInfo.ProverKind.CVC4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.LinkedList;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.TheoremProverException;
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
 * An implementation of {@link TheoremProver} using one of the automated theorem
 * provers CVC3 or CVC4. Transforms a theorem proving query into the language of
 * CVC, invokes CVC through its command line interface in a new process, and
 * interprets the output.
 * 
 * @author siegel
 */
public class RobustCVCTheoremProver implements TheoremProver {

	// ************************** Static Fields *************************** //

	/**
	 * Nick-name for <code>stderr</code>, where warnings and error messages will
	 * be sent.
	 */
	public final static PrintStream err = System.err;

	// ****************************** Fields ****************************** //

	/**
	 * Info object on underlying theorem prover, which will have
	 * {@link ProverKind} either {@link ProverKind#CVC3} or
	 * {@link ProverKind#CVC4}
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
	 * The translation of the given context to a CVC3 expression. Created once
	 * during instantiation and never modified.
	 */
	private CVCTranslator assumptionTranslator;

	// *************************** Constructors *************************** //

	/**
	 * Constructs new CVC theorem prover for the given context.
	 * 
	 * @param universe
	 *            the controlling symbolic universe
	 * @param context
	 *            the assumption(s) the prover will use for queries
	 * @param ProverInfo
	 *            information object on the underlying theorem prover, which
	 *            must have {@link ProverKind} either {@link ProverKind#CVC3} or
	 *            {@link ProverKind#CVC4}
	 * @throws TheoremProverException
	 *             if the context contains something CVC just can't handle
	 */
	RobustCVCTheoremProver(PreUniverse universe, BooleanExpression context,
			ProverInfo info) throws TheoremProverException {
		LinkedList<String> command = new LinkedList<>();

		assert universe != null;
		assert context != null;
		assert info != null;
		this.universe = universe;
		this.info = info;
		// The following is apparently necessary since the same bound symbolic
		// constant can be used in different scopes in the context; CVC*
		// requires that these map to distinct variables.
		// context = (BooleanExpression) universe.cleanBoundVariables(context);
		this.assumptionTranslator = new CVCTranslator(universe, context, true);
		command.add(info.getPath().getAbsolutePath());
		command.addAll(info.getOptions());
		if (info.getKind() == CVC3) {
			command.add("-lang");
			command.add("presentation");
		} else if (info.getKind() == CVC4) {
			command.add("--quiet");
			command.add("--lang=cvc4");
			command.add("--no-interactive");
			command.add("--rewrite-divk");
			// also try "--use-theory=idl", which can sometimes solve non-linear
			// queries
		} else
			throw new SARLInternalException("unreachable");
		this.processBuilder = new ProcessBuilder(command);
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
			if ((info.getKind() == CVC3 ? "Valid." : "valid").equals(line))
				return Prove.RESULT_YES;
			if ((info.getKind() == CVC3 ? "Invalid." : "invalid").equals(line))
				return Prove.RESULT_NO;
			if (info.getShowInconclusives()) {
				err.println(info.getFirstAlias()
						+ " inconclusive with message: " + line);
				for (line = cvcOut.readLine(); line != null; line = cvcOut
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

	private ValidityResult runCVC(BooleanExpression predicate, int id,
			boolean show, PrintStream out) throws TheoremProverException {
		Process process = null;
		ValidityResult result = null;

		try {
			process = processBuilder.start();
		} catch (IOException e) {
			if (info.getShowErrors())
				err.println("I/O exception reading " + info.getFirstAlias()
						+ " output: " + e.getMessage());
			result = Prove.RESULT_MAYBE;
		}
		if (result == null) {
			PrintStream stdin = new PrintStream(process.getOutputStream());
			BufferedReader stdout = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			BufferedReader stderr = new BufferedReader(new InputStreamReader(
					process.getErrorStream()));
			FastList<String> assumptionDecls = assumptionTranslator
					.getDeclarations();
			FastList<String> assumptionText = assumptionTranslator
					.getTranslation();

			assumptionDecls.print(stdin);
			stdin.print("ASSERT ");
			assumptionText.print(stdin);
			stdin.println(";");
			predicate = (BooleanExpression) universe
					.cleanBoundVariables(predicate);

			CVCTranslator translator = new CVCTranslator(assumptionTranslator,
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
				out.print("\n" + info.getFirstAlias() + " predicate   " + id
						+ ":\n");
				predicateDecls.print(out);
				predicateText.print(out);
				out.println();
				out.println();
				out.flush();
			}
			if (info.getTimeout() > 0
					&& !ProcessControl.waitForProcess(process,
							info.getTimeout())) {
				if (info.getShowErrors() || info.getShowInconclusives())
					err.println(info.getFirstAlias() + " query       " + id
							+ ": time out");
				result = Prove.RESULT_MAYBE;
			} else {
				result = readCVCOutput(stdout, stderr);
			}
		}
		if (process != null)
			process.destroy();
		return result;
	}

	@Override
	public ValidityResult valid(BooleanExpression predicate) {
		PrintStream out = universe.getOutputStream();
		int id = universe.numProverValidCalls();
		FastList<String> assumptionDecls = assumptionTranslator
				.getDeclarations();
		FastList<String> assumptionText = assumptionTranslator.getTranslation();
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

		ValidityResult result;

		try {
			result = runCVC(predicate, id, show, out);
		} catch (TheoremProverException e) {
			if (show)
				err.println("Warning: " + e.getMessage());
			result = Prove.RESULT_MAYBE;
		}
		if (show) {
			out.println(info.getFirstAlias() + " result      " + id + ": "
					+ result);
			out.flush();
		}
		return result;
	}

	@Override
	public ValidityResult validOrModel(BooleanExpression predicate) {
		return Prove.RESULT_MAYBE;
	}

	@Override
	public String toString() {
		return "RobustCVCTheoremProver[" + info.getFirstAlias() + "]";
	}
}
