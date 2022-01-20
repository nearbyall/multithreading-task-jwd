package by.epamtc.melnikov.multithreading.bean.matrix;

import java.io.Serializable;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Matrix implements Serializable {

	private static final long serialVersionUID = -7977477818607348092L;

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("matrix");
    
	private static final String DIMENSION = RESOURCE_BUNDLE.getString("dimension");
	private static final String FILL_VALUE = RESOURCE_BUNDLE.getString("fill.value");
	private static final String THREADS_COUNT = RESOURCE_BUNDLE.getString("threads.count");
    
	private Integer dimension;
	private Integer threadsCount;
	private int[][] values;
	    
	private Matrix() {
		dimension = Integer.parseInt(DIMENSION);
		threadsCount = Integer.parseInt(THREADS_COUNT);
		values = new int[dimension][dimension];
		for (int i = 0; i < dimension; i++)
			for (int j = 0; j < dimension; j++)
				values[i][j] = Integer.parseInt(FILL_VALUE);
	}
    
	public static class MatrixHolder {
		public static final Matrix HOLDER_INSTANCE = new Matrix();
	}

	public static Matrix getInstance() {
		return MatrixHolder.HOLDER_INSTANCE;
	}

	public Integer getDimension() {
		return dimension;
	}

	public int[][] getValues() {
		return values;
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
				sb.append(String.format("%10d ", values[i][j]));
			}
			sb.append("\n");
		}
				
		return sb.toString();
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dimension == null) ? 0 : dimension.hashCode());
		result = prime * result + Arrays.deepHashCode(values);
		result = prime * result + ((threadsCount == null) ? 0 : threadsCount.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matrix other = (Matrix) obj;
		if (dimension == null) {
			if (other.dimension != null)
				return false;
		} else if (!dimension.equals(other.dimension))
			return false;
		if (!Arrays.deepEquals(values, other.values))
			return false;
		if (threadsCount == null) {
			if (other.threadsCount != null)
				return false;
		} else if (!threadsCount.equals(other.threadsCount))
			return false;
		return true;
	}
	
}