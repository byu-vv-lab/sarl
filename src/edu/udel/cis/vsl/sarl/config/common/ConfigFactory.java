package edu.udel.cis.vsl.sarl.config.common;

import static java.io.StreamTokenizer.TT_EOF;
import static java.io.StreamTokenizer.TT_NUMBER;
import static java.io.StreamTokenizer.TT_WORD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.config.ProverInfo;
import edu.udel.cis.vsl.sarl.IF.config.ProverInfo.ProverKind;
import edu.udel.cis.vsl.sarl.IF.config.SARLConfig;

/**
 * Factory for producing and manipulating SARL configuration files and
 * {@link SARLConfig} objects.
 * 
 * @author siegel
 *
 */
public class ConfigFactory {

	public final static PrintStream err = System.err;

	public final static PrintStream out = System.out;

	/**
	 * <p>
	 * Function converting a string representation of the theorem prover kind to
	 * an actual {@link ProverKind} object.
	 * </p>
	 * 
	 * <p>
	 * The {@link ProverKind} enumerated type provides some classification of
	 * theorem provers. There might be multiple entries for one actual prover,
	 * for example, because one entry is needed for the executable version using
	 * one input language, another entry is needed for the executable version
	 * using a different input language, and another version is needed for the
	 * API version. Add entries as needed.
	 * </p>
	 * 
	 * @param kindString
	 *            the exact string representation of the kind
	 * @return the kind which corresponds exactly to that string, or
	 *         <code>null</code> is there is no such kind
	 */
	private static ProverKind kind(String kindString) {
		switch (kindString) {
		case "CVC3":
			return ProverKind.CVC3;
		case "CVC3_API":
			return ProverKind.CVC3_API;
		case "CVC4":
			return ProverKind.CVC4;
		case "CVC4_API":
			return ProverKind.CVC4_API;
		case "Z3":
			return ProverKind.Z3;
		case "Z3_API":
			return ProverKind.Z3_API;
		default:
			return null;
		}
	}

	/**
	 * Map from the typical file names of the executable theorem provers to the
	 * kind of theorem prover. Add more entries as needed.
	 */
	private static Map<String, ProverKind> executableMap = new HashMap<>();

	static {
		executableMap.put("cvc3", ProverKind.CVC3); // working
		executableMap.put("cvc4", ProverKind.CVC4); // working
		// executableMap.put("z3", ProverKind.Z3); // not yet implemented
	}

	/**
	 * Map from the names of the (JNI) dynamic libraries to the kind of the
	 * theorem prover. Add more entries as needed.
	 */
	private static Map<String, ProverKind> dylibMap = new HashMap<>();

	static {
		// dylibMap.put("cvc3jni", ProverKind.CVC3_API); // too fragile
		// dylibMap.put("cvc4jni", ProverKind.CVC4_API); // crashes too much
		// dylibMap.put("z3java", ProverKind.Z3_API); // not yet implemented
	}

	/**
	 * Returns the command argument(s) for the given kind of theorem prover that
	 * causes that prover to print its version number. Add new entries as
	 * needed.
	 * 
	 * @param kind
	 *            the kind of theorem prover
	 * @return the command line arguments that cause the version number to be
	 *         printed
	 * @throws SARLException
	 *             if the kind is not of the executable variety
	 */
	private static String versionCommand(ProverKind kind) {
		switch (kind) {
		case CVC3:
			return "-version";
		case CVC4:
			return "--version";
		case Z3:
			return "-version";
		default:
			throw new SARLException("Unknown executable prover kind: " + kind);
		}
	}

	/**
	 * Returns an exception object in the case where a SARL configuration file
	 * has incorrect syntax.
	 * 
	 * @param file
	 *            the SARL configuration file being parsed
	 * @param st
	 *            the stream tokenizer being used to parse the configuration
	 *            file
	 * @param msg
	 *            an error message
	 * @return a {@link SARLException} with a nice presentation of the error
	 *         message, file name, and line number
	 */
	private static SARLException parseErr(File file, StreamTokenizer st,
			String msg) {
		return new SARLException(file + " " + st.toString() + " " + msg);
	}

	/**
	 * Looks for a SARL configuration file in the specified directory. First
	 * looks for file named ".sarl", and if that doesn't exist, looks for one
	 * name ".sarl_default".
	 * 
	 * @param dir
	 *            directory in which to look
	 * @return the SARL configuration file or <code>null</code> if it cannot be
	 *         found in the given directory
	 */
	private static File findSARLConfigInDir(File dir) {
		if (dir.isDirectory()) {
			File configFile = new File(dir, ".sarl");

			if (configFile.isFile())
				return configFile;
			configFile = new File(dir, ".sarl_default");
			if (configFile.isFile())
				return configFile;
		}
		return null;
	}

	/**
	 * Checks that an executable prover actually can be executed and created an
	 * entry for it in the configuration file specified by the given stream.
	 * 
	 * @param kind
	 *            the kind of theorem prover
	 * @param alias
	 *            the alias string to use in the configuration entry
	 * @param stream
	 *            the stream writing to the configuration file
	 * @param executableFile
	 *            the executable theorem prover
	 * @return <code>true</code> if the prover could be executed correctly and
	 *         an entry was created, else <code>false</code>
	 */
	private static boolean processProver(ProverKind kind, String alias,
			PrintStream stream, File executableFile) {
		String fullPath = executableFile.getAbsolutePath();
		ProcessBuilder pb = new ProcessBuilder(fullPath, versionCommand(kind));
		Process process = null;

		try {
			process = pb.start();

			BufferedReader stdout = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line = stdout.readLine().trim();

			process.destroy();

			int pos = line.lastIndexOf("version ");

			if (pos < 0) {
				out.println("Unexpected output from " + fullPath);
				return false;
			}

			String version = line.substring(pos + "version ".length());
			ProverInfo info = new CommonProverInfo();

			info.addAlias(alias);
			info.setKind(kind);
			info.setPath(executableFile);
			info.setVersion(version);
			info.setTimeout(20.0);
			info.setShowQueries(false);
			info.setShowInconclusives(false);
			info.setShowErrors(true);
			info.print(stream);
			out.println("Adding " + kind + " version " + version + " in "
					+ fullPath + " to .sarl");
			out.flush();
			return true;
		} catch (IOException e) {
			if (process != null)
				process.destroy();
			out.println("Failed to execute " + fullPath);
			return false;
		}
	}

	/**
	 * Checks that a candidate dynamic library prover can be loaded, and, if so,
	 * adds an entry for it to the configuration file.
	 * 
	 * @param kind
	 *            the kind of the candidate theorem prover (should end in
	 *            "_API")
	 * @param alias
	 *            the alias to assign to this prover in the configuration file
	 * @param stream
	 *            the print stream into the configuration file
	 * @param jnidylib
	 *            the name of the JNI dynamic library that should be loaded,
	 *            e.g. "cvc3jni"
	 * @return <code>true</code> if that dynamic library was loaded
	 *         successfully, in which case an entry is also added to the
	 *         configuration file; otherwise, <code>false</code>
	 */
	private static boolean processDynamicProver(ProverKind kind, String alias,
			PrintStream stream, String jnidylib) {
		try {
			String libraryName = System.mapLibraryName(jnidylib);
			System.loadLibrary(jnidylib);

			out.println("Adding " + kind + " implemented as native library "
					+ libraryName + " to .sarl");
			out.flush();

			ProverInfo info = new CommonProverInfo();

			info.addAlias(alias);
			info.setKind(kind);
			info.setPath(new File(libraryName)); // TODO: do better
			info.setVersion("UNKNOWN");
			info.setTimeout(20.0);
			info.setShowQueries(false);
			info.setShowInconclusives(false);
			info.setShowErrors(true);
			info.print(stream);
			return true;
		} catch (Error e) {
			return false;
		}
	}

	// Public methods...

	/**
	 * Parses a SARL configuration file.
	 * 
	 * @param configFile
	 *            the configuration file
	 * @return the {@link SARLConfig} object resulting from parsing that file
	 * @throws IOException
	 *             if anything goes wrong reading the file
	 * @throws SARLException
	 *             if the configuration file does not conform to the proper
	 *             syntax
	 */
	public static SARLConfig fromFile(File configFile) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(configFile));
		StreamTokenizer st = new StreamTokenizer(reader);
		LinkedList<ProverInfo> proverInfos = new LinkedList<>();
		Set<String> allAliases = new HashSet<>();

		st.commentChar('#');
		st.quoteChar('"');
		st.quoteChar('\'');
		st.parseNumbers();
		st.eolIsSignificant(false);
		st.slashSlashComments(true);
		st.slashSlashComments(true);
		st.lowerCaseMode(false);
		st.wordChars('_', '_');
		for (int token = st.nextToken(); token != TT_EOF; token = st
				.nextToken()) {
			if (token != TT_WORD || !"prover".equalsIgnoreCase(st.sval))
				throw parseErr(configFile, st, "expected \"prover\"");
			token = st.nextToken();
			if (token != '{')
				throw parseErr(configFile, st, "expected '{'");

			ProverInfo info = new CommonProverInfo();

			for (token = st.nextToken(); token != '}'; token = st.nextToken()) {
				String keyword;

				if (token != TT_WORD)
					throw parseErr(configFile, st, "expected a key-word");
				keyword = st.sval;
				token = st.nextToken();
				if (token != '=')
					throw parseErr(configFile, st, "expected '='");
				switch (keyword) {
				case "aliases": {
					int numAliases = 0;

					for (token = st.nextToken(); token != ';'; token = st
							.nextToken()) {
						if (numAliases > 0) {
							if (token != ',')
								throw parseErr(configFile, st, "expected ','");
							token = st.nextToken();
						}
						if (token != TT_WORD)
							throw parseErr(configFile, st,
									"expected a plain word to be used as alias");
						if (!allAliases.add(st.sval))
							throw parseErr(configFile, st,
									"alias used more than once");
						info.addAlias(st.sval);
						numAliases++;
					}
					if (numAliases == 0)
						throw parseErr(configFile, st,
								"expected at least one alias");
					break;
				}
				case "path": {
					token = st.nextToken();
					if (token != '"' && token != '\'')
						throw parseErr(configFile, st, "expected quoted string");
					if (info.getPath() != null)
						throw parseErr(configFile, st, "more than one path");
					info.setPath(new File(st.sval));
					token = st.nextToken();
					if (token != ';')
						throw parseErr(configFile, st, "expected ';'");
					break;
				}
				case "options": {
					boolean first = true;

					for (token = st.nextToken(); token != ';'; token = st
							.nextToken()) {
						if (first)
							first = false;
						else {
							if (token != ',')
								throw parseErr(configFile, st, "expected ','");
							token = st.nextToken();
						}
						if (token != '"' && token != '\'')
							throw parseErr(configFile, st,
									"expected a quoted string");
						info.addOption(st.sval);
					}
					break;
				}
				case "version": {
					token = st.nextToken();
					if (token != '"' && token != '\'')
						throw parseErr(configFile, st, "expected quoted string");
					if (info.getVersion() != null)
						throw parseErr(configFile, st, "more than one version");
					info.setVersion(st.sval);
					token = st.nextToken();
					if (token != ';')
						throw parseErr(configFile, st, "expected ';'");
					break;
				}
				case "kind": {
					token = st.nextToken();
					if (token != TT_WORD)
						throw parseErr(configFile, st, "expected word");
					if (info.getKind() != null)
						throw parseErr(configFile, st, "more than one kind");

					ProverKind kind = kind(st.sval);

					if (kind == null)
						throw parseErr(configFile, st, "unknown prover kind");
					info.setKind(kind);
					token = st.nextToken();
					if (token != ';')
						throw parseErr(configFile, st, "expected ';'");
					break;
				}
				case "showQueries":
				case "showInconclusives":
				case "showErrors": {
					token = st.nextToken();
					if (token != TT_WORD)
						throw parseErr(configFile, st, "expected word");

					boolean value;

					switch (st.sval) {
					case "true":
						value = true;
						break;
					case "false":
						value = false;
						break;
					default:
						throw parseErr(configFile, st, "expected true or false");
					}
					switch (keyword) {
					case "showQueries":
						info.setShowQueries(value);
						break;
					case "showInconclusives":
						info.setShowInconclusives(value);
						break;
					case "showErrors":
						info.setShowErrors(value);
						break;
					default:
						throw new SARLInternalException("Unreachable");
					}
					token = st.nextToken();
					if (token != ';')
						throw parseErr(configFile, st, "expected ';'");
					break;
				}
				case "timeout": {
					token = st.nextToken();
					if (token != TT_NUMBER)
						throw parseErr(configFile, st,
								"expected number (time in seconds)");
					info.setTimeout(st.nval);
					token = st.nextToken();
					if (token != ';')
						throw parseErr(configFile, st, "expected ';'");
					break;
				}
				default:
					throw parseErr(configFile, st, "unknown keyword: "
							+ keyword);
				} // end of switch
			} // end of for
			proverInfos.add(info);
		} // end of for
		return new CommonSARLConfig(proverInfos);
	}

	/**
	 * Looks for a SARL configuration file, first in the current working
	 * directory, and, if not found there, then in the user's home directory.
	 * 
	 * @return the SARL configuration file, or <code>null</code> if not found
	 */
	public static File findSARLConfigFile() {
		File result = findSARLConfigInDir(new File(
				System.getProperty("user.dir")));

		if (result != null)
			return result;
		result = findSARLConfigInDir(new File(System.getProperty("user.home")));
		return result;
	}

	/**
	 * Finds and parses the SARL configuration file.
	 * 
	 * @return {@link SARLConfig} object resulting from parsing the
	 *         configuration file, or <code>null</code> if a configuration file
	 *         was not found
	 * @throws IOException
	 *             if anything goes wrong reading the configuration file
	 * @throws SARLException
	 *             if the configuration file does not conform to the proper
	 *             syntax
	 */
	public static SARLConfig findConfig() throws IOException {
		File configFile = findSARLConfigFile();

		if (configFile == null)
			return null;
		return fromFile(configFile);
	}

	/**
	 * Searches for theorem provers on the user's system and creates a SARL
	 * configuration file <code>.sarl</code> in the user's home directory.
	 * 
	 * @throws FileNotFoundException
	 *             if the configuration file cannot be created in the home
	 *             directory
	 */
	public static void makeConfigFile() throws FileNotFoundException {
		File userDir = new File(System.getProperty("user.home"));
		File configFile = new File(userDir, ".sarl");

		if (configFile.exists()) {
			if (configFile.isFile()) {
				File newLocation = new File(userDir, ".sarl.old");

				out.println("Moving existing SARL configuration file to "
						+ newLocation.getAbsolutePath());
				out.flush();
				configFile.renameTo(newLocation);
			} else {
				System.err.println("Remove the non-ordinary-file "
						+ configFile.getAbsolutePath() + " and try again.");
				System.err.flush();
				System.exit(2);
			}
		}

		String path = System.getenv("PATH");
		String[] pathDirs = path.split(File.pathSeparator);
		PrintStream configFileStream = new PrintStream(new FileOutputStream(
				configFile));
		Map<ProverKind, Integer> executableCountMap = new HashMap<>();
		Map<ProverKind, Integer> dynamicCountMap = new HashMap<>();

		out.println("Creating SARL configuration file in " + configFile);
		out.flush();
		for (String dirName : pathDirs) {
			File dir = new File(dirName);

			if (dir.isDirectory()) {
				File[] files = dir.listFiles();

				for (File file : files) {
					if (!file.canExecute())
						continue;

					String name = file.getName();
					ProverKind kind = executableMap.get(name);

					if (kind != null) {
						int count = executableCountMap.containsKey(kind) ? executableCountMap
								.get(kind) : 0;
						String alias = kind.toString().toLowerCase();

						if (count > 0)
							alias += "_" + count;
						if (processProver(kind, alias, configFileStream, file)) {
							executableCountMap.put(kind, count + 1);
						}
					}
				}
			}
		}
		// now the dynamic libraries...
		for (Entry<String, ProverKind> entry : dylibMap.entrySet()) {
			String libraryName = entry.getKey();
			ProverKind kind = entry.getValue();
			int count = dynamicCountMap.containsKey(kind) ? dynamicCountMap
					.get(kind) : 0;
			String alias = kind.toString().toLowerCase();

			if (count > 0)
				alias += "_" + count;
			if (processDynamicProver(kind, alias, configFileStream, libraryName)) {
				dynamicCountMap.put(kind, count + 1);
			}
		}
		//
		configFileStream.close();
		if (executableCountMap.isEmpty() && dynamicCountMap.isEmpty()) {
			err.println("No appropriate theorem provers were found in your PATH.");
			err.println("SARL's theorem proving capability will be very limited.");
			err.println("Consider installing at least one of CVC3, CVC4, or Z3.");
			err.flush();
		}
		out.println("SARL configuration file created successfully in "
				+ configFile.getAbsolutePath());
		out.println("By default, SARL will use all provers listed in the configuration file,");
		out.println("in order, until a conclusive result is obtained.");
		out.println("Edit the file as necessary to remove or change the order of provers.");
		out.flush();
	}
}
