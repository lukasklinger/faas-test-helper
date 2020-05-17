package faasTestHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import helpers.CodeHelper;
import helpers.FileHelper;

/**
 * This class automatically modifies a JavaScript file to include the FaaS
 * testing framework.
 * 
 * @author lukasklinger
 *
 */
public class FaaSTestHelper {
	// Framework import statement
	private String frameworkImport = "const faasTest = require('faas-test');";
	
	// Search term to find handler
	private String handlerSearchTerm = "exports";
	
	// default options for the FaaS testing framework
	private String testOptions = "{\n" + "\t\tsimulateConcurrencyLimit: {random: false},\n"
			+ "\t\tsimulateColdStart: {max: 2000, min: 0, exact: false},\n"
			+ "\t\tsimulateTimeout: {timeoutInMinutes: 20},\n" + "\t\tsimulateCacheUse: {size: 256, dir: '/tmp'},\n"
			+ "\t\tsimulateMemoryUse: {size: 1024},\n" + "\t\tsimulateRetries: {retries: 3},\n"
			+ "\t\thandlerFunction: oldHandler,\n" + "\t\thandlerParams: params\n" + "\t};";

	private List<String> lines;
	
	/**
	 * This function will add the FaaS testing framework and default options to a
	 * given JavaScript file.
	 * 
	 * @param path {@link String} of path to the file, not including file name (e.g.
	 *             '/path/to/').
	 * @param file {@link String} of file name (e.g. 'index.js').
	 * @return Returns the modified code as a {@link String}.
	 * @throws IOException May throw an {@link IOException} if the input or output
	 *                     file cannot be accessed.
	 */
	public String setupFramework(String path, String file) throws IOException {
		readFile(path + file);

		addFramework();
		moveHandlerFunction();
		writeNewHandler();

		writeFile(path, lines);
		return String.join("\n", lines);
	}

	/**
	 * Function adds framework import.
	 */
	private void addFramework() {
		lines.add(0, frameworkImport);
	}

	/**
	 * Function removes handler content and moves it to a new function called
	 * 'oldHandler()'.
	 */
	private void moveHandlerFunction() {
		int startOrigHandler = CodeHelper.searchLine(handlerSearchTerm, lines);
		int endOrigHandler = CodeHelper.findEndOfFunction(startOrigHandler, lines);

		List<String> functionBody = new ArrayList<String>(lines.subList(startOrigHandler + 1, endOrigHandler));
		lines.subList(startOrigHandler + 1, endOrigHandler).clear();

		functionBody.add(0, "\n// FaaS-Test helper moved handler function body here");
		functionBody.add(1, "async function oldHandler(params) {");

		String[] functionParams = CodeHelper.getFunctionParameters(startOrigHandler, lines);
		for (int i = 0; i < functionParams.length; i++) {
			functionBody.add(i + 2, "\tlet " + functionParams[i] + " = params." + functionParams[i] + ";");
		}

		functionBody.add("};");

		lines.addAll(functionBody);
	}

	/**
	 * Function writes the new handler including a call to the FaaS testing
	 * framework.
	 */
	private void writeNewHandler() {
		int handlerStart = CodeHelper.searchLine(handlerSearchTerm, lines);
		String parameterObject = "\tlet params = {";
		String[] functionParams = CodeHelper.getFunctionParameters(handlerStart, lines);

		for (int i = 0; i < functionParams.length; i++) {
			parameterObject += functionParams[i] + ": " + functionParams[i];

			if (i + 1 < functionParams.length)
				parameterObject += ", ";
		}

		parameterObject += "};";

		lines.add(handlerStart + 1, parameterObject);
		lines.add(handlerStart + 2, "\tlet testOptions = " + testOptions);
		lines.add(handlerStart + 3, "\treturn await faasTest.test(testOptions);");
	}

	private void writeFile(String path, List<String> lines) throws IOException {
		FileHelper.writeFile(path + "/modified-file.js", String.join("\n", lines));
	}

	private void readFile(String path) throws IOException {
		lines = FileHelper.readFileToStringList(path);
	}
}
