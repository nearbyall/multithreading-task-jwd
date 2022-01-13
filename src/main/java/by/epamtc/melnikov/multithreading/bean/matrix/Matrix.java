package by.epamtc.melnikov.multithreading.bean.matrix;

import java.util.ResourceBundle;

public class Matrix {

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("matrix");
    
	private static final String DIMENSION = RESOURCE_BUNDLE.getString("dimension");
	private static final String FILL_VALUE = RESOURCE_BUNDLE.getString("fill.value");
	private static final String THREADS_COUNT = RESOURCE_BUNDLE.getString("threads.count");
    
	private Integer dimension;
	private Integer threadsCount;
	private volatile int[][] jaggedArray;
	    
	private Matrix() {
		dimension = Integer.parseInt(DIMENSION);
		threadsCount = Integer.parseInt(THREADS_COUNT);
		jaggedArray = new int[dimension][dimension];
		for (int i = 0; i < dimension; i++)
			for (int j = 0; j < dimension; j++)
				jaggedArray[i][j] = Integer.parseInt(FILL_VALUE);
	}
    
	public static class MatrixHolder {
		public static final Matrix HOLDER_INSTANCE = new Matrix();
	}

	public static Matrix getInstance() {
		return MatrixHolder.HOLDER_INSTANCE;
	}

	public void editElement(int rowNumber, int colNumber, int value) {
		jaggedArray[rowNumber][colNumber] = value;
	}
    
	public Integer getDimension() {
		return dimension;
	}

	public int[][] getJaggedArray() {
		return jaggedArray;
	}

	public Integer getThreadsCount() {
		return threadsCount;
	}

	public void setThreadsCount(Integer threadsCount) {
		this.threadsCount = threadsCount;
	}

	@Override
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(String.format("%s \n Dimension %d \n", 
				getClass().getSimpleName(), dimension));
		
		for (int i = 0; i < dimension; i++) {
			for(int j = 0; j < dimension; j++) {
				sb.append(String.format("%10d ", jaggedArray[i][j]));
			}
			sb.append("\n");
		}
				
		return sb.toString();
		
	}
	
}