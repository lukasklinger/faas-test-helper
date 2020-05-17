package helpers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * Class contains static methods to access, read and write files.
 * 
 * @author lukasklinger
 *
 */
public class FileHelper {
	/**
	 * Method will read a text file after checking the path.
	 * 
	 * @param path {@link String} of file path, e.g. '/temp/file.txt'.
	 * @return Returns a {@link List} of {@link String}s.
	 * @throws IOException May throw an {@link IOException} if the file cannot be
	 *                     opened.
	 */
	public static List<String> readFileToStringList(String path) throws IOException {
		return Files.readAllLines(checkPath(path));
	}

	/**
	 * Writes text to a file.
	 * 
	 * @param path    {@link String} of file path, e.g. '/temp/file.txt'.
	 * @param content {@link String} that will be written to file.
	 * @throws IOException May throw an {@link IOException} if the file cannot be
	 *                     written to.
	 */
	public static void writeFile(String path, String content) throws IOException {
		File file = new File(path);

		FileWriter fw = new FileWriter(file);
		fw.write(content);
		fw.close();
	}

	private static Path checkPath(String pathString) throws IOException {
		Path path = Paths.get(pathString);

		if (Objects.isNull(pathString)) {
			throw new IOException("Path string null.");
		}

		if (pathString.isEmpty()) {
			throw new IOException("Path string empty.");
		}

		if (!Files.isReadable(path)) {
			throw new IOException("File not readable.");
		}

		if (!Files.isWritable(path)) {
			throw new IOException("File not writable.");
		}

		return path;
	}
}
