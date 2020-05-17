package faasTestHelper;

import java.io.IOException;

/**
 * 
 * @author lukasklinger
 *
 */
public class Main {
	public static void main(String[] args) {
		FaaSTestHelper helper = new FaaSTestHelper();

		if (args.length < 2) {
			System.out.println(
					"Please start this program with the path to the directory as the first parameter and the name of the file as the second.");
			System.exit(-1);
		}

		try {
			helper.setupFramework(args[0], args[1]);
			
			System.out.println("A new file called 'modified-file.js' has been written to the directory.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
