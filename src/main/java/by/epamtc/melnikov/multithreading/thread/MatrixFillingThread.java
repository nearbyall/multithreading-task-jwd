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
		int[][] jaggedArray = matrix.getJaggedArray();
		Map<String, Integer> diagonalIndexes = findFreeDiagonalElement(jaggedArray);
		if (diagonalIndexes.get("row") != null) {
			updateMatrix(jaggedArray, diagonalIndexes);
		}
		logger.info("Thread " + name + " has finished working");
		latch.countDown();
		lock.unlock();
	}
	
	public void updateMatrix(int[][] jaggedArray, Map<String, Integer> diagonalCoords) {
    	
		int diagonalRow = diagonalCoords.get("row");
		int diagonalColumn = diagonalCoords.get("column");
		jaggedArray[diagonalRow][diagonalColumn] = name;
		matrixState.changeElementState(diagonalRow, diagonalColumn);
        
		Map<String, Integer> freeColumnOrRow = findFreeColOrRowElement(diagonalRow, diagonalColumn, jaggedArray);
		int freeRowElement = freeColumnOrRow.get("row");
		int freeColumnElement = freeColumnOrRow.get("column");
		jaggedArray[freeRowElement][freeColumnElement] = name;
		matrixState.changeElementState(freeRowElement, freeColumnElement);
        
		int rowSum = calculateRowSum(jaggedArray, diagonalRow);
		int colSum = calculateColSum(jaggedArray, diagonalColumn);
        
		String threadWork = "The diagonal element has been changed [%s,%s] and free element:" +
				" [%s,%s]. Row sum : %s, col sum : %s";
		String threadWorkInfo = String.format(threadWork, diagonalRow, diagonalColumn, freeRowElement,
				freeColumnElement, rowSum, colSum);
		TempResultsKeeper tempResultsKeeper = TempResultsKeeper.getInstance();
		Map<Integer, String> tempResults = tempResultsKeeper.getTempResults();
		tempResults.put(name, threadWorkInfo);
        
    }
	
	public Map<String, Integer> findFreeDiagonalElement(int[][] jaggedArray) {
		Map<String, Integer> indexes = new HashMap<>();
		for (int i = 0; i < jaggedArray.length; i++) {
			if (!matrixState.isChanged(i, i)) {
				indexes.put("row", i);
				indexes.put("column", i);
				return indexes;
			}
		}
		return indexes;
	}
    
	//TODO Randomize finding a free element, instead of selecting the leftmost or topmost element
	public Map<String, Integer> findFreeColOrRowElement(int rowNumber, int columnNumber, int[][] jaggedArray) {
		Map<String, Integer> indexes = new HashMap<>();
		
		int[] randomIndexes = calculateRandomIndexes(jaggedArray.length);
		
		for (int i = 0; i < jaggedArray.length; i++) {
			if (!matrixState.isChanged(rowNumber, randomIndexes[i])) {
				indexes.put("row", rowNumber);
				indexes.put("column", randomIndexes[i]);
				return indexes;
			}
		}
		for (int i = 0; i < jaggedArray.length; i++) {
			if (!matrixState.isChanged(randomIndexes[i], columnNumber)) {
				indexes.put("row", randomIndexes[i]);
				indexes.put("column", columnNumber);
				return indexes;
			}
		}
		return indexes;
	}
    
	public int calculateRowSum(int[][] jaggedArray, int rowIndex) {
		return Arrays.stream(jaggedArray[rowIndex]).sum();
	}

	public int calculateColSum(int[][] jaggedArray, int colIndex) {
		return Arrays.stream(jaggedArray).map((l) -> l[colIndex]).reduce(Integer::sum).get();
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
	    for (int i = array.length - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      int a = array[index];
	      array[index] = array[i];
	      array[i] = a;
	    }
	}
	
}
