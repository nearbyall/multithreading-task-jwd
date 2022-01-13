package by.epamtc.melnikov.multithreading.main;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epamtc.melnikov.multithreading.bean.matrix.Matrix;
import by.epamtc.melnikov.multithreading.bean.matrix.MatrixState;
import by.epamtc.melnikov.multithreading.logic.Writer;
import by.epamtc.melnikov.multithreading.thread.ThreadManager;

public class Main {
	
	private final static Logger logger = LogManager.getLogger(Main.class);
	
	public static void main(String[] args) {
		
		Writer writer = new Writer();
		ReentrantLock lock = new ReentrantLock();
		Matrix matrix = Matrix.getInstance();
		MatrixState matrixState = new MatrixState(matrix.getDimension());
		matrixState.setDefaultState();
		ThreadManager threadManager = new ThreadManager(lock, writer, matrixState);
		try {
			threadManager.runThreads(matrix.getThreadsCount(), matrix.getDimension());
		} catch (InterruptedException | IOException e) {
			logger.error("Threads error");
			System.exit(0);
		}
		
	}
	
}
