package by.epamtc.melnikov.multithreading.bean.matrix;

import java.util.Map;
import java.util.HashMap;

public class MatrixState {
	
	private int dimension;
	private Map<String, Boolean> states = new HashMap<>();

	public MatrixState(int n) {
		this.dimension = n;
	}

	public void setDefaultState() {
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				String key = createKey(i, j);
				states.put(key, false);
			}
		}
	}

	public boolean isChanged(int rowElement, int colElement) {
		String key = createKey(rowElement, colElement);
		return states.get(key);
	}

	public void changeElementState(int rowElement, int colElement) {
		String key = createKey(rowElement, colElement);
		states.replace(key, true);
	}

	public String createKey(int key1, int key2) {
		return key1 + " " + key2;
	}
    
}
