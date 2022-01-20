package by.epamtc.melnikov.multithreading.logic;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;

import by.epamtc.melnikov.multithreading.bean.matrix.Matrix;

public class Writer{
	
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("matrix");
	
	private final static String FILE_PATH = RESOURCE_BUNDLE.getString("result.file.path");
	private final static String INDENT = "\r\n";

	private FileOutputStream fileOutputStream;
    
	public void writeText(String text) throws IOException {
		fileOutputStream = new FileOutputStream(FILE_PATH, true);
		byte[] buffer = text.getBytes();
		fileOutputStream.write(buffer);
		fileOutputStream.flush();
		fileOutputStream.close();
	}

	public void writeMatrix (Matrix matrix) throws IOException {
		writeText(matrix.toString());
	}

	public void writeTemp(Map<Integer, String> tempResults) throws IOException {
		StringBuilder text = new StringBuilder();
		tempResults.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach((l) -> text.append("Thread ").
				append(l).append(INDENT));
		writeText(text.toString());
	}
    
}
