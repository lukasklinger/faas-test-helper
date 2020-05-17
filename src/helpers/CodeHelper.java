package helpers;

import java.util.List;

/**
 * Collection of static methods that simplify interacting with JavaScript code.
 * 
 * @author lukasklinger
 *
 */
public class CodeHelper {
	/**
	 * Function will retrieve JavaScript function parameters
	 * 
	 * @param functionSig Line number of function signature
	 * @param lines       Code as {@link List} of {@link String}s.
	 * @return Returns an {@link Array} of {@link String}s for all parameters.
	 */
	public static String[] getFunctionParameters(int functionSig, List<String> lines) {
		String line = lines.get(functionSig);

		int start = line.indexOf('(');
		int end = line.indexOf(')');

		return line.substring(start + 1, end).replace(" ", "").split(",");
	}

	/**
	 * Function returns last line of a JavaScript function by counting brackets.
	 * NOTE: Will return -1 if the end cannot be found.
	 * 
	 * @param firstLine Line number of function signature.
	 * @param lines     Code as {@link List} of {@link String}s.
	 * @return Returns last line of function.
	 */
	public static int findEndOfFunction(int firstLine, List<String> lines) {
		long counter = 0;

		for (int i = firstLine; i < lines.size(); i++) {
			String line = lines.get(i);
			
			counter += line.chars().filter(c -> c == '{').count();
			counter -= line.chars().filter(c -> c == '}').count();

			if (counter == 0) return i;
		}

		return -1;
	}

	/**
	 * Function will search for a line in a list of strings.
	 * NOTE: Will return -1 if the search term cannot be found.
	 * 
	 * @param searchTerm Search term to use
	 * @param lines      {@link List} of {@link String}s to search through.
	 * @return Returns the line number of first occurrence.
	 */
	public static int searchLine(String searchTerm, List<String> lines) {
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);

			if (line.contains(searchTerm)) return i;
		}

		return -1;
	}
}
