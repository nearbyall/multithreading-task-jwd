package by.epamtc.melnikov.multithreading.bean.matrix;

import java.util.Map;
import java.util.HashMap;

public class MatrixState {
	
	private int dimension;
	private Map<String, Boolean> states;

	public MatrixState(int n) {
		this.dimension = n;
		this.states = new HashMap<>();
	}

	public void resetStates() {
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				String key = createKey(i, j);
				states.put(key, false);
			}
		}
	}

	public boolean isChanged(int rowIndex, int colIndex) {
		String key = createKey(rowIndex, colIndex);
		return states.get(key);
	}

	public void changeState(int rowIndex, int colIndex) {
		String key = createKey(rowIndex, colIndex);
		states.replace(key, true);
	}

	public String createKey(int firstIndex, int secondIndex) {
		return firstIndex + " " + secondIndex;
	}
    
}
