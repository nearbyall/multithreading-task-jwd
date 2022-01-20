package by.epamtc.melnikov.multithreading.thread;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epamtc.melnikov.multithreading.bean.matrix.Matrix;
import by.epamtc.melnikov.multithreading.bean.matrix.MatrixState;

public class MatrixFillingThread implements Runnable {

	private final static Logger logger = LogManager.getLogger(MatrixFillingThread.class);
	
	private int name;
	private ReentrantLock lock;
	private CountDownLatch latch;
	private MatrixState matrixState;
	
	public MatrixFillingThread(int name, ReentrantLock lock, CountDownLatch latch, MatrixState matrixState) {
		this.name = name;
		this.lock = lock;
		this.latch = latch;
		this.matrixState = matrixState;
	}

	@Override
	public void run() {
		
		logger.info("Thread " + name + " started ");
		lock.lock();
		
		logger.info("Thread " + name + " is working");
		Matrix matrix = Matrix.getInstance();
		int[][] values = matrix.getValues();
		Map<String, Integer> diagonalIndexes = findFreeDiagonalElement(values);
		if (diagonalIndexes.get("row") != null) {
			updateMatrix(values, diagonalIndexes);
		}
		
		logger.info("Thread " + name + " has finished working");
		latch.countDown();
		lock.unlock();
		
	}
	
	public void updateMatrix(int[][] values, Map<String, Integer> diagonalIndexes) {
    	
		int diagonalRow = diagonalIndexes.get("row");
		int diagonalColumn = diagonalIndexes.get("column");
		values[diagonalRow][diagonalColumn] = name;
		matrixState.changeState(diagonalRow, diagonalColumn);
        
		Map<String, Integer> freeColumnOrRow = findFreeColOrRowElement(diagonalRow, diagonalColumn, values);
		int freeRowElement = freeColumnOrRow.get("row");
		int freeColumnElement = freeColumnOrRow.get("column");
		values[freeRowElement][freeColumnElement] = name;
		matrixState.changeState(freeRowElement, freeColumnElement);
        
		int rowSum = calculateRowSum(values, diagonalRow);
		int colSum = calculateColSum(values, diagonalColumn);
        
		String threadWork = "The diagonal element has been changed [%s,%s] and free element:" +
				" [%s,%s]. Row sum : %s, col sum : %s";
		TempInfo tempResultsKeeper = TempInfo.getInstance();
		Map<Integer, String> tempResults = tempResultsKeeper.getTempResults();
		tempResults.put(name, String.format(threadWork, diagonalRow, diagonalColumn, freeRowElement, freeColumnElement, rowSum, colSum));
        
    }
	
	public Map<String, Integer> findFreeDiagonalElement(int[][] values) {
		Map<String, Integer> indexes = new HashMap<>();
		for (int i = 0; i < values.length; i++) {
			if (!matrixState.isChanged(i, i)) {
				indexes.put("row", i);
				indexes.put("column", i);
				return indexes;
			}
		}
		return indexes;
	}
    
	public Map<String, Integer> findFreeColOrRowElement(int rowIndex, int colIndex, int[][] values) {
		Map<String, Integer> indexes = new HashMap<>();
		
		int[] randomIndexes = calculateRandomIndexes(values.length);
		
		for (int i = 0; i < values.length; i++) {
			if (!matrixState.isChanged(rowIndex, randomIndexes[i])) {
				indexes.put("row", rowIndex);
				indexes.put("column", randomIndexes[i]);
				return indexes;
			}
		}
		
		for (int i = 0; i < values.length; i++) {
			if (!matrixState.isChanged(randomIndexes[i], colIndex)) {
				indexes.put("row", randomIndexes[i]);
				indexes.put("column", colIndex);
				return indexes;
			}
		}
		
		return indexes;
	}
    
	public int calculateRowSum(int[][] values, int rowIndex) {
		return Arrays.stream(values[rowIndex]).sum();
	}

	public int calculateColSum(int[][] values, int colIndex) {
		return Arrays.stream(values).map((l) -> l[colIndex]).reduce(Integer::sum).get();
	}
	
	private int[] calculateRandomIndexes(int n) {
		int[] indexes = new int[n];
		for (int i = 0; i < n; i++) {
			indexes[i] = i;
		}
		shuffleArray(indexes);
		return indexes;
	}
	
	private void shuffleArray(int[] array) {
		Random rnd = ThreadLocalRandom.current();
		for (int i = array.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			int a = array[index];
			array[index] = array[i];
			array[i] = a;
		}
	}
	
}
